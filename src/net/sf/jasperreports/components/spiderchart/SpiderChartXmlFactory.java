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
package net.sf.jasperreports.components.spiderchart;

import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;

/**
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: SpiderChartXmlFactory.java 3874 2010-07-13 14:58:41Z shertage $
 */
public class SpiderChartXmlFactory extends JRBaseFactory
{

	public Object createObject(Attributes attrs) throws Exception
	{
		SpiderChartComponent chartComponent = new SpiderChartComponent();
		
		EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.getByName(attrs.getValue(JRXmlConstants.ATTRIBUTE_evaluationTime));
		if (evaluationTime != null)
		{
			chartComponent.setEvaluationTime(evaluationTime);
		}
		else
		{
			chartComponent.setEvaluationTime(EvaluationTimeEnum.NOW);
		}
		
		if (chartComponent.getEvaluationTime() == EvaluationTimeEnum.GROUP)
		{
			String groupName = attrs.getValue(JRXmlConstants.ATTRIBUTE_evaluationGroup);
			chartComponent.setEvaluationGroup(groupName);
		}
		
		return chartComponent;
	}

}
