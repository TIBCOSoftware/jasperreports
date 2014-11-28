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
package net.sf.jasperreports.forms.textinput;

import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.apache.commons.digester.Digester;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class TextInputComponentDigester implements XmlDigesterConfigurer
{
	public void configureDigester(Digester digester)
	{
		addTextInputComponentRules(digester);
	}

	public static void addTextInputComponentRules(Digester digester)
	{
		String componentNamespace = digester.getRuleNamespaceURI();
		
		String textInputComponentPattern = "*/componentElement/textInput";
//		digester.addObjectCreate(textInputComponentPattern, TextInputComponent.class.getName());
		digester.addFactoryCreate(textInputComponentPattern, TextInputComponentFactory.class.getName());

		digester.addSetProperties(textInputComponentPattern);
		
//		digester.addSetProperties(textInputComponentPattern,
//			//properties to be ignored by this rule
//			new String[]{JRXmlConstants.ATTRIBUTE_evaluationTime, StandardBarbecueComponent.PROPERTY_ROTATION}, 
//			new String[0]);
		
		digester.setRuleNamespaceURI(JRXmlConstants.JASPERREPORTS_NAMESPACE);

		digester.addFactoryCreate(textInputComponentPattern + "/textField", TextInputTextFieldFactory.class.getName());

		digester.setRuleNamespaceURI(componentNamespace);
	}

}
