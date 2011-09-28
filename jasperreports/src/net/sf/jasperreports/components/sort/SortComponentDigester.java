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
package net.sf.jasperreports.components.sort;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.XmlConstantPropertyRule;

import org.apache.commons.digester.Digester;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class SortComponentDigester implements XmlDigesterConfigurer
{
	public void configureDigester(Digester digester)
	{
		addSortComponentRules(digester);
	}

	public static void addSortComponentRules(Digester digester)
	{
		String sortComponentPattern = "*/componentElement/sort";
		digester.addObjectCreate(sortComponentPattern, SortComponent.class.getName());
		
		digester.addSetProperties(sortComponentPattern, new String[] {
				SortComponent.PROPERTY_EVALUATION_TIME,
				}, 
				new String[0]);
		
		digester.addRule(sortComponentPattern, 
				new XmlConstantPropertyRule(
						SortComponent.PROPERTY_EVALUATION_TIME,
						EvaluationTimeEnum.values()));

		digester.addFactoryCreate(sortComponentPattern + "/symbol", SortComponentSymbolFactory.class.getName());
		
		digester.setRuleNamespaceURI(JRXmlConstants.JASPERREPORTS_NAMESPACE);

		digester.addFactoryCreate(sortComponentPattern + "/symbol/font", SortComponentSymbolFontFactory.class.getName());
		digester.addSetNext(sortComponentPattern + "/symbol/font", "setSymbolFont", JRFont.class.getName());
	}

}
