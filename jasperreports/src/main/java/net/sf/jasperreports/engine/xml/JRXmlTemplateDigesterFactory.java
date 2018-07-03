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
package net.sf.jasperreports.engine.xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.TabStop;


/**
 * Factory for template XML digesters.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JRTemplate
 */
public class JRXmlTemplateDigesterFactory implements ErrorHandler
{
	
	private static final Log log = LogFactory.getLog(JRXmlTemplateDigesterFactory.class);

	protected static final String PATTERN_ROOT = JRXmlConstants.TEMPLATE_ELEMENT_ROOT;
	protected static final String PATTERN_INCLUDED_TEMPLATE = PATTERN_ROOT + "/" + JRXmlConstants.TEMPLATE_ELEMENT_INCLUDED_TEMPLATE;
	protected static final String PATTERN_STYLE = PATTERN_ROOT + "/" + JRXmlConstants.ELEMENT_style;
	protected static final String PATTERN_STYLE_PEN = PATTERN_STYLE + "/" + JRXmlConstants.ELEMENT_pen;
	protected static final String PATTERN_BOX = PATTERN_STYLE + "/" + JRXmlConstants.ELEMENT_box;
	protected static final String PATTERN_BOX_PEN = PATTERN_BOX + "/" + JRXmlConstants.ELEMENT_pen;
	protected static final String PATTERN_BOX_TOP_PEN = PATTERN_BOX + "/" + JRXmlConstants.ELEMENT_topPen;
	protected static final String PATTERN_BOX_LEFT_PEN = PATTERN_BOX + "/" + JRXmlConstants.ELEMENT_leftPen;
	protected static final String PATTERN_BOX_BOTTOM_PEN = PATTERN_BOX + "/" + JRXmlConstants.ELEMENT_bottomPen;
	protected static final String PATTERN_BOX_RIGHT_PEN = PATTERN_BOX + "/" + JRXmlConstants.ELEMENT_rightPen;
	protected static final String PATTERN_PARAGRAPH = PATTERN_STYLE + "/" + JRXmlConstants.ELEMENT_paragraph;
	protected static final String PATTERN_TAB_STOP = PATTERN_PARAGRAPH + "/" + JRXmlConstants.ELEMENT_tabStop;
	
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
			@Override
			public void addRuleInstances(Digester digester)
			{
				digester.addObjectCreate(PATTERN_ROOT, JRSimpleTemplate.class);
				
				digester.addCallMethod(PATTERN_INCLUDED_TEMPLATE, "addIncludedTemplate", 0);
				
				digester.addFactoryCreate(PATTERN_STYLE, JRTemplateStyleFactory.class);
				digester.addSetNext(PATTERN_STYLE, "addStyle", JRStyle.class.getName());
				
				digester.addFactoryCreate(PATTERN_STYLE_PEN, JRPenFactory.Style.class.getName());
				
				digester.addFactoryCreate(PATTERN_BOX, JRBoxFactory.class.getName());
				digester.addFactoryCreate(PATTERN_BOX_PEN, JRPenFactory.Box.class.getName());
				digester.addFactoryCreate(PATTERN_BOX_TOP_PEN, JRPenFactory.Top.class.getName());
				digester.addFactoryCreate(PATTERN_BOX_LEFT_PEN, JRPenFactory.Left.class.getName());
				digester.addFactoryCreate(PATTERN_BOX_BOTTOM_PEN, JRPenFactory.Bottom.class.getName());
				digester.addFactoryCreate(PATTERN_BOX_RIGHT_PEN, JRPenFactory.Right.class.getName());

				digester.addFactoryCreate(PATTERN_PARAGRAPH, JRParagraphFactory.class.getName());
				digester.addFactoryCreate(PATTERN_TAB_STOP, TabStopFactory.class.getName());
				digester.addSetNext(PATTERN_TAB_STOP, "addTabStop", TabStop.class.getName());
			}
		};
	}
	
	/**
	 * Creates and configures a digester for template XML.
	 * 
	 * @return a template XML digester
	 */
	public JRXmlDigester createDigester(JasperReportsContext jasperReportsContext)
	{
		SAXParser parser = createParser(jasperReportsContext);
		JRXmlDigester digester = new JRXmlDigester(parser);
		try
		{
			configureDigester(jasperReportsContext, digester);
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

	protected SAXParser createParser(JasperReportsContext jasperReportsContext)
	{
		String parserFactoryClass = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(
				JRSaxParserFactory.PROPERTY_TEMPLATE_PARSER_FACTORY);
		
		if (log.isDebugEnabled())
		{
			log.debug("Using template SAX parser factory class " + parserFactoryClass);
		}
		
		JRSaxParserFactory factory = BaseSaxParserFactory.getFactory(jasperReportsContext, parserFactoryClass);
		return factory.createParser();
	}
	
	protected void configureDigester(JasperReportsContext jasperReportsContext, Digester digester) throws SAXException, ParserConfigurationException 
	{
		digester.setNamespaceAware(true);
		digester.setRuleNamespaceURI(JRXmlConstants.JASPERTEMPLATE_NAMESPACE);
		
		boolean validating = JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(JRReportSaxParserFactory.COMPILER_XML_VALIDATION);
		
		digester.setErrorHandler(this);
		digester.setValidating(validating);
		digester.setFeature("http://xml.org/sax/features/validation", validating);

		digester.addRuleSet(rules);
	}
	
	@Override
	public void error(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}
	
}
