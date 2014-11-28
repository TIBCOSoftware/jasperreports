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
package net.sf.jasperreports.engine.xml;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * {@link JRTemplate Template} XML serializer.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JRXmlTemplateLoader
 */
public class JRXmlTemplateWriter extends JRXmlBaseWriter
{

	private static final Log log = LogFactory.getLog(JRXmlTemplateWriter.class);
	
	/**
	 * Default XML output encoding.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * Returns the XML representation of a template.
	 * <p/>
	 * Uses {@link #DEFAULT_ENCODING the default encoding}.
	 * 
	 * @param template the template
	 * @return the XML representation of the template
	 */
	public static String writeTemplate(JRTemplate template)
	{
		return writeTemplate(template, DEFAULT_ENCODING);
	}
	
	/**
	 * Returns the XML representation of a template.
	 * 
	 * @param template the template
	 * @param encoding the XML encoding to use
	 * @return the XML representation of the template
	 */
	public static String writeTemplate(JRTemplate template, String encoding)
	{
		StringWriter stringOut = new StringWriter();
		try
		{
			writeTemplate(template, stringOut, encoding);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		return stringOut.toString();
	}
	
	/**
	 * Writes the XML representation of a template to an output stream.
	 * <p/>
	 * Uses {@link #DEFAULT_ENCODING the default encoding}.
	 * 
	 * @param template the template
	 * @param out the output stream
	 */
	public static void writeTemplate(JRTemplate template, OutputStream out)
	{
		writeTemplate(template, out, DEFAULT_ENCODING);
	}
	
	/**
	 * Writes the XML representation of a template to an output stream.
	 * 
	 * @param template the template
	 * @param out the output stream
	 * @param encoding the XML encoding to use
	 */
	public static void writeTemplate(JRTemplate template, OutputStream out, String encoding)
	{
		try
		{
			Writer writer = new OutputStreamWriter(out, encoding);
			writeTemplate(template, writer, encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	/**
	 * Writes the XML representation of a template to a file.
	 * <p/>
	 * Uses {@link #DEFAULT_ENCODING the default encoding}.
	 * 
	 * @param template the template
	 * @param outputFile the output file name
	 */
	public static void writeTemplateToFile(JRTemplate template, String outputFile)
	{
		writeTemplateToFile(template, outputFile, DEFAULT_ENCODING);
	}
	
	/**
	 * Writes the XML representation of a template to a file.
	 * 
	 * @param template the template
	 * @param outputFile the output file name
	 * @param encoding the XML encoding to use
	 */
	public static void writeTemplateToFile(JRTemplate template, String outputFile, String encoding)
	{
		BufferedOutputStream fileOut = null;
		boolean close = true;
		try
		{
			fileOut = new BufferedOutputStream(new FileOutputStream(outputFile));
			writeTemplate(template, fileOut, encoding);
			
			fileOut.flush();
			close = false;
			fileOut.close();
		}
		catch (FileNotFoundException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (fileOut != null && close)
			{
				try
				{
					fileOut.close();
				}
				catch (IOException e)
				{
					log.warn("Could not close file " + outputFile, e);
				}
			}
		}
	}
	
	protected static void writeTemplate(JRTemplate template, Writer out, String encoding) throws IOException
	{
		JRXmlTemplateWriter writer = new JRXmlTemplateWriter(template, out, encoding);
		writer.write();
		out.flush();
	}
	
	private final JRTemplate template;
	private final String encoding;
	
	/**
	 * Creates an XML template writer.
	 * 
	 * @param template the template to write
	 * @param out the output writer
	 * @param encoding the XML encoding to use
	 */
	public JRXmlTemplateWriter(JRTemplate template, Writer out, String encoding)
	{
		this.template = template;
		this.encoding = encoding;
		useWriter(new JRXmlWriteHelper(out), null);//FIXMEVERSION
	}

	/**
	 * Writes the template to the output writer.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException
	{
		writer.writeProlog(encoding);
		writer.writePublicDoctype(JRXmlConstants.TEMPLATE_ELEMENT_ROOT, 
				JRXmlConstants.JASPERTEMPLATE_PUBLIC_ID, JRXmlConstants.JASPERTEMPLATE_SYSTEM_ID);
		
		writer.startElement(JRXmlConstants.TEMPLATE_ELEMENT_ROOT);
		writeIncludedTemplates();
		writeStyles();
		writer.closeElement();
	}

	protected void writeIncludedTemplates() throws IOException
	{
		JRTemplateReference[] includedTemplates = template.getIncludedTemplates();
		if (includedTemplates != null)
		{
			for (int i = 0; i < includedTemplates.length; i++)
			{
				JRTemplateReference reference = includedTemplates[i];
				writeIncludedTemplate(reference);
			}
		}
	}

	protected void writeIncludedTemplate(JRTemplateReference reference) throws IOException
	{
		writer.writeCDATAElement(JRXmlConstants.TEMPLATE_ELEMENT_INCLUDED_TEMPLATE, reference.getLocation());
		
	}

	protected void writeStyles() throws IOException
	{
		JRStyle[] styles = template.getStyles();
		if (styles != null)
		{
			for (int i = 0; i < styles.length; i++)
			{
				JRStyle style = styles[i];
				writeStyle(style);
			}
		}
	}

	protected boolean toWriteConditionalStyles()
	{
		return false;
	}
}
