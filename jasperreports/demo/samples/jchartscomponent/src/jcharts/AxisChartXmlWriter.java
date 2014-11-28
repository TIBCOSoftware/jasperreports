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
package jcharts;

import java.io.IOException;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class AxisChartXmlWriter extends AbstractComponentXmlWriter
{
	
	/**
	 * @deprecated Replaced by {@link AxisChartXmlWriter#AxisChartXmlWriter(JasperReportsContext)}. 
	 */
	public AxisChartXmlWriter()
	{
		super(DefaultJasperReportsContext.getInstance());
	}
	
	/**
	 * 
	 */
	public AxisChartXmlWriter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	
	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter)
	{
		return true;
	}

	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		AxisChartComponent chart = (AxisChartComponent) component;
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		ComponentKey componentKey = componentElement.getComponentKey();
		
		String namespaceURI = componentKey.getNamespace();
		String schemaLocation = 
			ComponentsEnvironment.getInstance(jasperReportsContext)
				.getBundle(namespaceURI).getXmlParser().getPublicSchemaLocation();
		XmlNamespace namespace = new XmlNamespace(namespaceURI, componentKey.getNamespacePrefix(),
				schemaLocation);
		
		writer.startElement("axisChart", namespace);
		
		writer.addAttribute("evaluationTime", chart.getEvaluationTime(), 
				EvaluationTimeEnum.NOW);
		if (chart.getEvaluationTime() == EvaluationTimeEnum.GROUP)
		{
			writer.addEncodedAttribute("evaluationGroup", chart.getEvaluationGroup());
		}
		
		writer.addAttribute("areaColor", chart.getAreaColor());
		
		AxisDataset dataset = chart.getDataset();
		writer.startElement("axisDataset");
		
		reportWriter.writeElementDataset(dataset);
		
		writeExpression("labelExpression", dataset.getLabelExpression(), false, componentElement, reportWriter);
		writeExpression("valueExpression", dataset.getValueExpression(), false, componentElement, reportWriter);
		
		writer.closeElement();//axisDataset
		
		writeExpression("legendLabelExpression", chart.getLegendLabelExpression(), false, componentElement, reportWriter);
		
		writer.closeElement();//axisChart
	}

}
