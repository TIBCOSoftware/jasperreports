/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.parts;

import java.io.IOException;

import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.part.PartComponent;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.parts.subreport.SubreportPartComponent;

/**
 * XML writer for built-in part component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ComponentsXmlWriter.java 6802 2014-01-09 11:38:46Z shertage $
 * @see ComponentsExtensionsRegistryFactory
 */
public class PartComponentsXmlWriter extends AbstractPartComponentXmlWriter
{
	/**
	 * 
	 */
	public PartComponentsXmlWriter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}

	public void writeToXml(JRPart part, JRXmlWriter reportWriter) throws IOException
	{
		PartComponent component = part.getComponent();
		if (component instanceof SubreportPartComponent)
		{
			writeSubreport(part, reportWriter);
		}
	}

	protected void writeSubreport(JRPart part, JRXmlWriter reportWriter) throws IOException
	{
		SubreportPartComponent subreport = (SubreportPartComponent) part.getComponent();
		ComponentKey componentKey = part.getComponentKey();
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		XmlNamespace namespace = new XmlNamespace(
				PartComponentsExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				PartComponentsExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement(PartComponentsExtensionsRegistryFactory.SUBREPORT_PART_COMPONENT_NAME, namespace);
		writer.addAttribute("usingCache", subreport.getUsingCache());

		writer.writeExpression(JRXmlConstants.ELEMENT_parametersMapExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, subreport.getParametersMapExpression());

		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				reportWriter.writeSubreportParameter(parameters[i], JRXmlWriter.JASPERREPORTS_NAMESPACE);
			}
		}

		JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
		if (returnValues != null && returnValues.length > 0)
		{
			for(int i = 0; i < returnValues.length; i++)
			{
				reportWriter.writeSubreportReturnValue(returnValues[i], JRXmlWriter.JASPERREPORTS_NAMESPACE);
			}
		}
		
		writer.writeExpression(JRXmlConstants.ELEMENT_subreportExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, subreport.getExpression());
		
		writer.closeElement();
	}

	@Override
	public boolean isToWrite(JRPart part, JRXmlWriter reportWriter) 
	{
		ComponentKey componentKey = part.getComponentKey();
		if (ComponentsExtensionsRegistryFactory.NAMESPACE.equals(componentKey.getNamespace()))
		{
			if(PartComponentsExtensionsRegistryFactory.SUBREPORT_PART_COMPONENT_NAME.equals(componentKey.getName()))
			{
				return isNewerVersionOrEqual(part, reportWriter, JRConstants.VERSION_6_0_0);
			}
		}

		return true;
	}
}
