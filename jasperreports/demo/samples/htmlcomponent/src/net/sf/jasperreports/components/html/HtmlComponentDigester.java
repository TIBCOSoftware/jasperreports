/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.html;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.XmlConstantPropertyRule;

import org.apache.commons.digester.Digester;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlComponentDigester implements XmlDigesterConfigurer
{

	public void configureDigester(Digester digester)
	{
		setHtmlComponentRules(digester);
	}

	protected void setHtmlComponentRules(Digester digester)
	{
		String htmlComponentPattern = "*/componentElement/html";
		digester.addObjectCreate(htmlComponentPattern, HtmlComponent.class.getName());
		
		digester.addSetProperties(htmlComponentPattern, new String[] {
				HtmlComponent.PROPERTY_SCALE_TYPE,
				HtmlComponent.PROPERTY_HORIZONTAL_ALIGN,
				HtmlComponent.PROPERTY_VERTICAL_ALIGN,
				HtmlComponent.PROPERTY_EVALUATION_TIME }, 
				new String[0]);
				
		digester.addRule(htmlComponentPattern, 
				new XmlConstantPropertyRule(
						HtmlComponent.PROPERTY_SCALE_TYPE,
						ScaleImageEnum.values()));
		digester.addRule(htmlComponentPattern, 
				new XmlConstantPropertyRule(
						HtmlComponent.PROPERTY_HORIZONTAL_ALIGN,
						HorizontalAlignEnum.values()));
		digester.addRule(htmlComponentPattern, 
				new XmlConstantPropertyRule(
						HtmlComponent.PROPERTY_VERTICAL_ALIGN,
						VerticalAlignEnum.values()));
		digester.addRule(htmlComponentPattern, 
				new XmlConstantPropertyRule(
						HtmlComponent.PROPERTY_EVALUATION_TIME,
						EvaluationTimeEnum.values()));

		String htmlContentPattern = htmlComponentPattern + "/htmlContentExpression";
		digester.addFactoryCreate(htmlContentPattern, JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(htmlContentPattern, "setText", 0);
		digester.addSetNext(htmlContentPattern, "setHtmlContentExpression", JRExpression.class.getName());
	}

}
