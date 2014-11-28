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
package net.sf.jasperreports.components.ofc;

import java.io.IOException;
import java.util.List;

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
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BarChartXmlWriter extends AbstractComponentXmlWriter
{
	/**
	 * @deprecated Replaced by {@link BarChartXmlWriter#BarChartXmlWriter(JasperReportsContext)}.
	 */
	public BarChartXmlWriter()
	{
		super(DefaultJasperReportsContext.getInstance());
	}
	
	public BarChartXmlWriter(JasperReportsContext jasperReportsContext)
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
		BarChartComponent chart = (BarChartComponent) component;
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		ComponentKey componentKey = componentElement.getComponentKey();
		
		String namespaceURI = componentKey.getNamespace();
		String schemaLocation = 
			ComponentsEnvironment.getInstance(jasperReportsContext)
				.getBundle(namespaceURI).getXmlParser().getPublicSchemaLocation();
		XmlNamespace namespace = new XmlNamespace(namespaceURI, componentKey.getNamespacePrefix(),
				schemaLocation);
		
		writer.startElement("barChart", namespace);
		
		writer.addAttribute("evaluationTime", chart.getEvaluationTime(), EvaluationTimeEnum.NOW);
		if (chart.getEvaluationTime() == EvaluationTimeEnum.GROUP)
		{
			writer.addEncodedAttribute("evaluationGroup", chart.getEvaluationGroup());
		}
		
		BarDataset dataset = chart.getDataset();
		writer.startElement("barDataset");
		
		reportWriter.writeElementDataset(dataset);
		
		List<BarSeries> seriesList = dataset.getSeries();
		for(BarSeries series : seriesList)
		{
			writer.startElement("barSeries");
			writer.writeExpression("seriesExpression", series.getSeriesExpression());
			writer.writeExpression("categoryExpression", series.getCategoryExpression());
			writer.writeExpression("valueExpression", series.getValueExpression());
			writer.closeElement();//barSeries
		}
		writer.closeElement();//barDataset
		
		writer.writeExpression("titleExpression", chart.getTitleExpression());
		
		writer.closeElement();//barChart
	}

	
}
