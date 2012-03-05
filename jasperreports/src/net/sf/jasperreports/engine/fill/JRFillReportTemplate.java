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
package net.sf.jasperreports.engine.fill;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Fill-time {@link JRReportTemplate} implementation.
 * <p/>
 * Used to evaluate template source expressions.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillReportTemplate implements JRReportTemplate
{

	private static final Log log = LogFactory.getLog(JRFillReportTemplate.class);
	
	private final JRReportTemplate parent;
	private final JRBaseFiller filler;
	
	public JRFillReportTemplate(JRReportTemplate template, JRBaseFiller filler, JRFillObjectFactory factory)
	{
		factory.put(template, this);
		
		parent = template;
		this.filler = filler;
	}
	
	public JRExpression getSourceExpression()
	{
		return parent.getSourceExpression();
	}

	public JRTemplate evaluate() throws JRException
	{
		JRTemplate template;
		JRExpression sourceExpression = getSourceExpression();
		Object source = filler.evaluateExpression(sourceExpression, JRExpression.EVALUATION_DEFAULT);
		if (source == null)
		{
			template = null;
		}
		else
		{
			if (source instanceof JRTemplate)
			{
				template = (JRTemplate) source;
			}
			else
			{
				template = loadTemplate(source, filler.fillContext);
			}
		}
		return template;
	}

	protected static JRTemplate loadTemplate(Object source, JRFillContext fillContext) throws JRException
	{
		JRTemplate template;
		if (fillContext.hasLoadedTemplate(source))
		{
			template = fillContext.getLoadedTemplate(source);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("Loading styles template from " + source);
			}
			
			if (source instanceof String)
			{
				template = JRXmlTemplateLoader.load((String) source);
			}
			else if (source instanceof File)
			{
				template = JRXmlTemplateLoader.load((File) source);
			}
			else if (source instanceof URL)
			{
				template = JRXmlTemplateLoader.load((URL) source);
			}
			else if (source instanceof InputStream)
			{
				template = JRXmlTemplateLoader.load((InputStream) source);
			}
			else
			{
				throw new JRRuntimeException("Unknown template source class " + source.getClass().getName());
			}
			
			fillContext.registerLoadedTemplate(source, template);
		}
		return template;
	}
	
}
