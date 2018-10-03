/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
package xchart;

import java.io.IOException;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * 
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class XYChartXmlWriter extends AbstractComponentXmlWriter
{
	public static final String ELEMENT_XYChart = "XYChart";
	public static final String ELEMENT_XYDataset = "XYDataset";
	public static final String ELEMENT_XYSeries = "XYSeries";
	public static final String ELEMENT_chartTitleExpression = "chartTitleExpression";
	public static final String ELEMENT_xAxisTitleExpression = "xAxisTitleExpression";
	public static final String ELEMENT_yAxisTitleExpression = "yAxisTitleExpression";
	public static final String ELEMENT_colorExpression = "colorExpression";
	public XYChartXmlWriter()
	{
		super(DefaultJasperReportsContext.getInstance());
	}
	
	/**
	 * 
	 */
	public XYChartXmlWriter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	
	@Override
	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter)
	{
		return true;
	}

	@Override
	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		XYChartComponent chart = (XYChartComponent) component;
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		ComponentKey componentKey = componentElement.getComponentKey();
		
		String namespaceURI = componentKey.getNamespace();
		String schemaLocation = 
			ComponentsEnvironment.getInstance(jasperReportsContext)
				.getBundle(namespaceURI).getXmlParser().getPublicSchemaLocation();
		XmlNamespace namespace = new XmlNamespace(namespaceURI, componentKey.getNamespacePrefix(),
				schemaLocation);
		
		writer.startElement(ELEMENT_XYChart, namespace);
		
		writer.addAttribute("evaluationTime", chart.getEvaluationTime(), 
				EvaluationTimeEnum.NOW);
		if (chart.getEvaluationTime() == EvaluationTimeEnum.GROUP)
		{
			writer.addEncodedAttribute("evaluationGroup", chart.getEvaluationGroup());
		}
				
		XYDataset dataset = chart.getDataset();
		writer.startElement(ELEMENT_XYDataset);
		
		reportWriter.writeElementDataset(dataset);
		
		XYSeries[] xySeriesArray = dataset.getSeries();
		if (xySeriesArray != null && xySeriesArray.length > 0)
		{
			for(XYSeries xySeries : xySeriesArray)
			{
				writer.startElement(ELEMENT_XYSeries);
				writeExpression(JRXmlConstants.ELEMENT_seriesExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, xySeries.getSeriesExpression(), false, writer);
				writeExpression(JRXmlConstants.ELEMENT_xValueExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, xySeries.getXValueExpression(), false, writer);
				writeExpression(JRXmlConstants.ELEMENT_yValueExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, xySeries.getYValueExpression(), false, writer);
				writeExpression(ELEMENT_colorExpression, xySeries.getColorExpression(), false, componentElement, reportWriter);
				writer.closeElement();
			}
		}
		
		writer.closeElement();//XYDataset
		
		writeExpression(ELEMENT_chartTitleExpression, chart.getChartTitleExpression(), false, componentElement, reportWriter);
		writeExpression(ELEMENT_xAxisTitleExpression, chart.getXAxisTitleExpression(), false, componentElement, reportWriter);
		writeExpression(ELEMENT_yAxisTitleExpression, chart.getYAxisTitleExpression(), false, componentElement, reportWriter);
		
		writer.closeElement();//XYChart
	}
	
	protected void writeExpression(String name, XmlNamespace namespace, JRExpression expression, boolean writeClass, JRXmlWriteHelper writer)  throws IOException
	{
		writer.writeExpression(name, namespace, expression);
 	}
}
