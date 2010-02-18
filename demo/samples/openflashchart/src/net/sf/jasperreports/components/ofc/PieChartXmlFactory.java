/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.XmlConstants;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: PieChartXmlFactory.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class PieChartXmlFactory extends JRBaseFactory
{

	public Object createObject(Attributes attrs) throws Exception
	{
		PieChartComponent chart = new PieChartComponent();
		
		String evaluationAttr = attrs.getValue(XmlConstants.ATTRIBUTE_evaluationTime);
		if (evaluationAttr != null)
		{
			Byte evaluationTime = (Byte) JRXmlConstants.getEvaluationTimeMap().get(evaluationAttr);
			chart.setEvaluationTime(evaluationTime.byteValue());
		}

		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			String groupName = attrs.getValue(XmlConstants.ATTRIBUTE_evaluationGroup);
			chart.setEvaluationGroup(groupName);
		}
		
		return chart;
	}

}
