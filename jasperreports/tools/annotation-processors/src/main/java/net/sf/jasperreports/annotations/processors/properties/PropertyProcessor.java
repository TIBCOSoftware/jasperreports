/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.annotations.processors.properties;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.metadata.properties.StandardPropertiesMetadata;
import net.sf.jasperreports.metadata.properties.StandardPropertiesMetadataSerialization;
import net.sf.jasperreports.metadata.properties.StandardPropertyMetadata;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
@SupportedAnnotationTypes("net.sf.jasperreports.annotations.properties.Property")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class PropertyProcessor extends AbstractProcessor
{

	public PropertyProcessor()
	{
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	{
		StandardPropertiesMetadata props = new StandardPropertiesMetadata();
		
		for (TypeElement annotation : annotations)
		{
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
			for (Element element : elements)
			{
				if (element.getKind() != ElementKind.FIELD)
				{
					processingEnv.getMessager().printMessage(Kind.WARNING, 
							"Annotation " + annotation + " can only be applied to static fields", 
							element);
					continue;
				}
				
				VariableElement varElement = (VariableElement) element;
				Set<Modifier> modifiers = varElement.getModifiers();
				if (!modifiers.contains(Modifier.STATIC) || !modifiers.contains(Modifier.PUBLIC)
						|| !modifiers.contains(Modifier.FINAL))
				{
					processingEnv.getMessager().printMessage(Kind.WARNING, 
							"Annotation " + annotation + " can only be applied to public static final fields", 
							element);
					continue;
				}
				
				TypeMirror varType = varElement.asType();
				if (!varType.toString().equals(String.class.getCanonicalName()))
				{
					processingEnv.getMessager().printMessage(Kind.WARNING, 
							"Annotation " + annotation + " can only be applied to String fields", 
							element);
					continue;
				}
				
				AnnotationMirror propertyAnnotation = findPropertyAnnotation(varElement);
				if (propertyAnnotation == null)
				{
					//should not happen
					continue;
				}
				
				StandardPropertyMetadata property = toPropertyMetadata(varElement, propertyAnnotation);
				props.addProperty(property);
			}
		}
		
		if (!props.getProperties().isEmpty())
		{
			writePropertiesMetadata(props);
		}
		
		return true;
	}
	
	protected AnnotationMirror findPropertyAnnotation(VariableElement element)
	{
		AnnotationMirror propertyAnnotation = null;
		List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
		for (AnnotationMirror annotation : annotationMirrors)
		{
			if (annotation.getAnnotationType().toString().equals(Property.class.getCanonicalName()))
			{
				propertyAnnotation = annotation;
				break;
			}
		}
		return propertyAnnotation;
	}

	protected StandardPropertyMetadata toPropertyMetadata(VariableElement element, AnnotationMirror propertyAnnotation)
	{
		Map<? extends ExecutableElement, ? extends AnnotationValue> annotationValues = processingEnv.getElementUtils().getElementValuesWithDefaults(propertyAnnotation);
		
		StandardPropertyMetadata property = new StandardPropertyMetadata();
		
		String constantValue = (String) element.getConstantValue();
		property.setName(constantValue);//TODO check for nulls
		
		QualifiedNameable enclosingElement = (QualifiedNameable) element.getEnclosingElement();
		property.setConstantDeclarationClass(enclosingElement.getQualifiedName().toString());
		property.setConstantFieldName(element.getSimpleName().toString());
		
		property.setLabel((String) annotationValue(annotationValues, "label").getValue());
		property.setDescription((String) annotationValue(annotationValues, "description").getValue());
		property.setDefaultValue((String) annotationValue(annotationValues, "defaultValue").getValue());
		property.setSinceVersion((String) annotationValue(annotationValues, "sinceVersion").getValue());
		property.setValueType(((TypeMirror) annotationValue(annotationValues, "valueType").getValue()).toString());
		
		@SuppressWarnings("unchecked")
		List<? extends AnnotationValue> scopeValues = (List<? extends AnnotationValue>) annotationValue(annotationValues, "scopes").getValue();
		List<PropertyScope> propertyScopes = new ArrayList<>(scopeValues.size());
		for (AnnotationValue scopeValue : scopeValues)
		{
			PropertyScope scope = Enum.valueOf(PropertyScope.class, ((VariableElement) scopeValue.getValue()).getSimpleName().toString());
			propertyScopes.add(scope); 
		}
		property.setScopes(propertyScopes);
		
		@SuppressWarnings("unchecked")
		List<? extends AnnotationValue> scopeQualificationValues = (List<? extends AnnotationValue>) annotationValue(annotationValues, "scopeQualifications").getValue();
		List<String> scopeQualifications = new ArrayList<>(scopeValues.size());
		for (AnnotationValue qualificationValue : scopeQualificationValues)
		{
			String qualification = (String) qualificationValue.getValue();
			scopeQualifications.add(qualification);
		}
		property.setScopeQualifications(scopeQualifications);
		
		return property;
	}
	
	protected AnnotationValue annotationValue(Map<? extends ExecutableElement, ? extends AnnotationValue> annotationValues, 
			String name)
	{
		AnnotationValue value = null;
		for (Entry<? extends ExecutableElement, ? extends AnnotationValue> valueEntry : annotationValues.entrySet())
		{
			ExecutableElement element = valueEntry.getKey();
			if (element.getSimpleName().contentEquals(name))
			{
				value = valueEntry.getValue();
				break;
			}
		}
		return value;
	}
	
	protected void writePropertiesMetadata(StandardPropertiesMetadata props)
	{
		StandardPropertiesMetadataSerialization propertiesSerialization = StandardPropertiesMetadataSerialization.instance();
		OutputStream out = null;
		try
		{
			FileObject propertiesMetadataResource = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", 
					StandardPropertiesMetadataSerialization.EXTENSION_RESOURCE_NAME, 
					(Element[]) null);//TODO lucianc
			
			out = propertiesMetadataResource.openOutputStream();
			propertiesSerialization.writeProperties(props, out);
			out.close();
			out = null;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					System.err.println("Error closing output stream");
					e.printStackTrace();
				}
			}
		}
	}

}
