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
package net.sf.jasperreports.engine.xml;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * Factory for template XML digesters.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRTemplate
 */
public class JRXmlTemplateDigesterFactory implements ErrorHandler
{

	private static final JRXmlTemplateDigesterFactory instance = new JRXmlTemplateDigesterFactory();
	
	private final RuleSet rules;

	protected JRXmlTemplateDigesterFactory() 
	{
		rules = readRuleSet();
	}

	/**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static JRXmlTemplateDigesterFactory instance()
	{
		return instance;
	}

	protected RuleSet readRuleSet()
	{
		return new RuleSetBase()
		{
			public void addRuleInstances(Digester digester)
			{
				String rootPattern = JRXmlConstants.TEMPLATE_ELEMENT_ROOT;
				digester.addObjectCreate(rootPattern, JRSimpleTemplate.class);
				
				String includedTemplatePattern = rootPattern + "/" + JRXmlConstants.TEMPLATE_ELEMENT_INCLUDED_TEMPLATE;
				digester.addCallMethod(includedTemplatePattern, "addIncludedTemplate", 0);
				
				String stylePattern = rootPattern + "/" + JRXmlConstants.ELEMENT_style;
				digester.addFactoryCreate(stylePattern, JRTemplateStyleFactory.class);
				digester.addSetNext(stylePattern, "addStyle", JRStyle.class.getName());
			}
		};
	}
	
	/**
	 * Creates and configures a digester for template XML.
	 * 
	 * @return a template XML digester
	 */
	public JRXmlDigester createDigester()
	{
		JRXmlDigester digester = new JRXmlDigester();
		try
		{
			configureDigester(digester);
		}
		catch (SAXException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (ParserConfigurationException e)
		{
			throw new JRRuntimeException(e);
		}
		return digester;
	}

	protected void configureDigester(Digester digester) throws SAXException, ParserConfigurationException 
	{
		boolean validating = JRProperties.getBooleanProperty(JRProperties.COMPILER_XML_VALIDATION);
		
		digester.setErrorHandler(this);
		digester.setValidating(validating);
		digester.setFeature("http://xml.org/sax/features/validation", validating);

		digester.addRuleSet(rules);
	}
	
	public void error(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}

	public void fatalError(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}

	public void warning(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}
	
}
