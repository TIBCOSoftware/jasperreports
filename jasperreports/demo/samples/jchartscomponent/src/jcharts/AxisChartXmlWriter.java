/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package jcharts;

import java.io.IOException;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class AxisChartXmlWriter implements ComponentXmlWriter
{

	public void writeToXml(ComponentKey componentKey, Component component,
			JRXmlWriter reportWriter) throws IOException
	{
		AxisChartComponent chart = (AxisChartComponent) component;
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		String namespaceURI = componentKey.getNamespace();
		String schemaLocation = ComponentsEnvironment
			.getComponentsBundle(namespaceURI).getXmlParser().getPublicSchemaLocation();
		XmlNamespace namespace = new XmlNamespace(namespaceURI, componentKey.getNamespacePrefix(),
				schemaLocation);
		
		writer.startElement("axisChart", namespace);
		
		writer.addAttribute("evaluationTime", chart.getEvaluationTime(), 
				JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);
		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			writer.addEncodedAttribute("evaluationGroup", chart.getEvaluationGroup());
		}
		
		writer.addAttribute("areaColor", chart.getAreaColor());
		
		AxisDataset dataset = chart.getDataset();
		writer.startElement("axisDataset");
		
		reportWriter.writeElementDataset(dataset);
		
		writer.writeExpression("labelExpression", dataset.getLabelExpression(), false);
		writer.writeExpression("valueExpression", dataset.getValueExpression(), false);
		
		writer.closeElement();//axisDataset
		
		writer.writeExpression("legendLabelExpression", chart.getLegendLabelExpression(), false);
		
		writer.closeElement();//axisChart
	}

}
