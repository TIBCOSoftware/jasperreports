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
package net.sf.jasperreports.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.data.DataFile;
import net.sf.jasperreports.data.DataFileServiceFactory;
import net.sf.jasperreports.data.FileDataAdapter;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.Designated;
import net.sf.jasperreports.engine.util.Designator;
import net.sf.jasperreports.engine.util.JRQueryExecuterUtils;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PropertiesMetadataUtil
{
	
	public static PropertiesMetadataUtil getInstance(JasperReportsContext context)
	{
		return new PropertiesMetadataUtil(context, Locale.getDefault());
	}
	
	public static PropertiesMetadataUtil getInstance(JasperReportsContext context, Locale locale)
	{
		return new PropertiesMetadataUtil(context, locale);
	}
	
	private JasperReportsContext context;
	private Locale locale;
	
	private volatile Map<String, PropertyMetadata> loadedProperties;

	protected PropertiesMetadataUtil(JasperReportsContext context, Locale locale)
	{
		this.context = context;
		this.locale = locale;
	}
	
	protected Collection<PropertyMetadata> allProperties()
	{
		Map<String, PropertyMetadata> allProperties = loadedProperties;
		if (allProperties == null)
		{
			loadedProperties = allProperties = ResourcePropertiesMetadataReader.instance().readProperties(context, locale);
		}
		return allProperties.values();
	}
	
	public List<PropertyMetadata> getProperties()
	{
		Collection<PropertyMetadata> allProperties = allProperties();
		return new ArrayList<>(allProperties);
	}
	
	public List<PropertyMetadata> getQueryExecuterFieldProperties(String queryLanguage) throws JRException
	{
		String qualification = queryExecuterQualification(queryLanguage);
		if (qualification == null)
		{
			return Collections.emptyList();
		}
		
		List<PropertyMetadata> properties = filterQualifiedProperties(PropertyScope.FIELD, qualification);
		return properties;
	}
	
	protected String queryExecuterQualification(String queryLanguage) throws JRException
	{
		QueryExecuterFactory queryExecuterFactory = JRQueryExecuterUtils.getInstance(context).getExecuterFactory(queryLanguage);
		if (!(queryExecuterFactory instanceof Designated))
		{
			return null;
		}
		String queryExecuterName = ((Designated) queryExecuterFactory).getDesignation();
		return queryExecuterName;
	}

	protected List<PropertyMetadata> filterQualifiedProperties(PropertyScope primaryScope, String qualificationName)
	{
		List<PropertyMetadata> properties = new ArrayList<>();
		Collection<PropertyMetadata> allProperties = allProperties();
		for (PropertyMetadata property : allProperties)
		{
			if (property.getScopes().contains(primaryScope)
					&& property.getScopeQualifications().contains(qualificationName))
			{
				properties.add(property);
			}
		}
		return properties;
	}
	
	public List<PropertyMetadata> getParameterProperties(DataAdapter dataAdapter)
	{
		String qualification = dataFileQualification(dataAdapter);
		if (qualification == null)
		{
			return Collections.emptyList();
		}
		
		List<PropertyMetadata> properties = filterQualifiedProperties(PropertyScope.PARAMETER, qualification);
		return properties;
	}
	
	@SuppressWarnings("unchecked")
	protected String dataFileQualification(DataAdapter dataAdapter)
	{
		if (!(dataAdapter instanceof FileDataAdapter))
		{
			return null;
		}
		
		DataFile dataFile = ((FileDataAdapter) dataAdapter).getDataFile();
		String name = null;
		List<DataFileServiceFactory> factories = context.getExtensions(DataFileServiceFactory.class);
		if (factories != null)
		{
			for (DataFileServiceFactory factory : factories)
			{
				if (factory instanceof Designator<?>)
				{
					name = ((Designator<DataFile>) factory).getName(dataFile);
					if (name != null)
					{
						break;
					}
				}
			}
		}
		return name;
	}
	
	public List<PropertyMetadata> getElementProperties(JRElement element)
	{
		Collection<PropertyMetadata> allProperties = allProperties();
		List<PropertyMetadata> elementProperties = new ArrayList<PropertyMetadata>();
		for (PropertyMetadata propertyMetadata : allProperties)
		{
			if (inScope(propertyMetadata, element))
			{
				elementProperties.add(propertyMetadata);
			}
		}
		return elementProperties;
	}
	
	protected boolean inScope(PropertyMetadata property, JRElement element)
	{
		List<PropertyScope> scopes = property.getScopes();
		if (scopes.contains(PropertyScope.ELEMENT))
		{
			return true;
		}
		
		if (element instanceof JRTextElement && scopes.contains(PropertyScope.TEXT_ELEMENT))
		{
			return true;
		}
		
		if (element instanceof JRChart && scopes.contains(PropertyScope.CHART_ELEMENT))
		{
			return true;
		}
		
		if (element instanceof JRCrosstab && scopes.contains(PropertyScope.CROSSTAB))
		{
			return true;
		}
		
		if (element instanceof JRComponentElement && scopes.contains(PropertyScope.COMPONENT))
		{
			List<String> qualifications = property.getScopeQualifications();
			if (qualifications == null || qualifications.isEmpty())
			{
				//assuming all components
				return true;
			}
			
			JRComponentElement componentElement = (JRComponentElement) element;
			String qualification;
			if (componentElement.getComponent() instanceof Designated)
			{
				qualification = ((Designated) componentElement.getComponent()).getDesignation();
			}
			else
			{
				ComponentKey key = componentElement.getComponentKey();
				if (key == null || key.getNamespace() == null || key.getName() == null)
				{
					//key is missing
					qualification = null;
				}
				else
				{
					qualification = key.getNamespace() 
							+ PropertyConstants.COMPONENT_KEY_QUALIFICATION_SEPARATOR 
							+ key.getName();
				}
			}
			
			return qualification != null && qualifications.contains(qualification);
		}
		
		return false;
	}
	
	public List<PropertyMetadata> getReportProperties(JRReport report)
	{
		Collection<PropertyMetadata> allProperties = allProperties();
		List<PropertyMetadata> reportProperties = new ArrayList<PropertyMetadata>();
		for (PropertyMetadata propertyMetadata : allProperties)
		{
			List<PropertyScope> scopes = propertyMetadata.getScopes();
			if (scopes != null && scopes.contains(PropertyScope.REPORT))
			{
				reportProperties.add(propertyMetadata);
			}
		}
		return reportProperties;
	}
	
	protected String dataAdapterQualification(JRDataset dataset, DataAdapter dataAdapter)
	{
		ParameterContributorContext contributorContext = new ParameterContributorContext(context,
				dataset, Collections.<String, Object>emptyMap());
		DataAdapterServiceUtil serviceUtil = DataAdapterServiceUtil.getInstance(contributorContext);
		DataAdapterService service = serviceUtil.getService(dataAdapter);
		return service instanceof Designated ? ((Designated) service).getDesignation() : null;
	}
	
	public List<PropertyMetadata> getDatasetProperties(JRDataset dataset, DataAdapter dataAdapter) throws JRException
	{
		String queryLanguage = dataset.getQuery() == null ? null : dataset.getQuery().getLanguage();
		String queryQualification = queryLanguage == null ? null : queryExecuterQualification(queryLanguage);
		
		String dataAdapterQualification = dataAdapter == null ? null : dataAdapterQualification(dataset, dataAdapter);
		String dataFileQualification = dataAdapter == null ? null : dataFileQualification(dataAdapter);
		
		Collection<PropertyMetadata> allProperties = allProperties();
		List<PropertyMetadata> reportProperties = new ArrayList<PropertyMetadata>();
		for (PropertyMetadata propertyMetadata : allProperties)
		{
			List<PropertyScope> scopes = propertyMetadata.getScopes();
			if (scopes != null && scopes.contains(PropertyScope.DATASET))
			{
				boolean matches;
				List<String> qualifications = propertyMetadata.getScopeQualifications();
				if (qualifications == null || qualifications.isEmpty())
				{
					matches = true;
				}
				else
				{
					matches = queryQualification != null && qualifications.contains(queryQualification)
							|| dataAdapterQualification != null && qualifications.contains(dataAdapterQualification)
							|| dataFileQualification != null && qualifications.contains(dataFileQualification);
				}
				
				if (matches)
				{
					reportProperties.add(propertyMetadata);
				}
			}
		}
		return reportProperties;
	}

}
