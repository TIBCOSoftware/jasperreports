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

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRSubreportParameterFactory;
import net.sf.jasperreports.engine.xml.JRSubreportReturnValueFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.parts.subreport.StandardSubreportPartComponent;

import org.apache.commons.digester.Digester;

/**
 * XML digester for built-in part component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ComponentsXmlDigesterConfigurer.java 6771 2013-11-22 14:52:25Z shertage $
 * @see PartComponentsExtensionsRegistryFactory
 */
public class PartComponentsXmlDigesterConfigurer implements XmlDigesterConfigurer
{
	public void configureDigester(Digester digester)
	{
		addSubreportRules(digester);
	}

	protected void addSubreportRules(Digester digester)
	{
		String subreportPattern = "*/part/subreportPart";
		digester.addObjectCreate(subreportPattern, StandardSubreportPartComponent.class);
		digester.addSetProperties(subreportPattern);

		String partNamespace = digester.getRuleNamespaceURI();
		String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;

		digester.setRuleNamespaceURI(jrNamespace);

		String paramMapExpressionPattern = subreportPattern + "/parametersMapExpression";
		digester.addFactoryCreate(paramMapExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(paramMapExpressionPattern, "setText", 0);
		digester.addSetNext(paramMapExpressionPattern, "setParametersMapExpression", 
				JRExpression.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreportPart/subreportParameter", JRSubreportParameterFactory.class.getName());
		digester.addSetNext("*/subreportPart/subreportParameter", "addParameter", JRSubreportParameter.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreportPart/subreportParameter/subreportParameterExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/subreportPart/subreportParameter/subreportParameterExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreportPart/subreportParameter/subreportParameterExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreportPart/returnValue", JRSubreportReturnValueFactory.class.getName());
		digester.addSetNext("*/subreportPart/returnValue", "addReturnValue", JRSubreportReturnValue.class.getName());

		String subreportExpressionPattern = subreportPattern + "/subreportExpression";
		digester.addFactoryCreate(subreportExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(subreportExpressionPattern, "setText", 0);
		digester.addSetNext(subreportExpressionPattern, "setExpression", 
				JRExpression.class.getName());

		digester.setRuleNamespaceURI(partNamespace);
	}
}
