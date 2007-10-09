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
			Class sourceType = sourceExpression.getValueClass();
			if (JRTemplate.class.isAssignableFrom(sourceType))
			{
				template = (JRTemplate) source;
			}
			else
			{
				template = loadTemplate(source, sourceType, filler.fillContext);
			}
		}
		return template;
	}

	protected static JRTemplate loadTemplate(Object source, Class sourceType, JRFillContext fillContext) throws JRException
	{
		JRTemplate template;
		if (fillContext.hasLoadedTemplate(source))
		{
			template = fillContext.getLoadedTemplate(source);
		}
		else
		{
			if (String.class.equals(sourceType))
			{
				template = JRXmlTemplateLoader.load((String) source);
			}
			else if (File.class.isAssignableFrom(sourceType))
			{
				template = JRXmlTemplateLoader.load((File) source);
			}
			else if (URL.class.isAssignableFrom(sourceType))
			{
				template = JRXmlTemplateLoader.load((URL) source);
			}
			else if (InputStream.class.isAssignableFrom(sourceType))
			{
				template = JRXmlTemplateLoader.load((InputStream) source);
			}
			else
			{
				throw new JRRuntimeException("Unknown template source class " + sourceType.getName());
			}
			
			fillContext.registerLoadedTemplate(source, template);
		}
		return template;
	}
	
}
