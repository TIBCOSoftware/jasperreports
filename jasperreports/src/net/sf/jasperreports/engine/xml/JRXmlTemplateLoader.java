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

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;


/**
 * Utility class that loads {@link JRTemplate templates} from XML representations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRXmlTemplateLoader
{
	
	private static final Log log = LogFactory.getLog(JRXmlTemplateLoader.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_TEMPLATE_NOT_FOUND = "xml.template.loader.template.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_TEMPLATE_PARSING_ERROR = "xml.template.loader.template.parsing.error";
	public static final String EXCEPTION_MESSAGE_KEY_TEMPLATE_READING_ERROR = "xml.template.loader.template.reading.error";
	public static final String EXCEPTION_MESSAGE_KEY_URL_CONNECTION_ERROR = "xml.template.loader.url.connection.error";
	
	private JasperReportsContext jasperReportsContext;
	
	/**
	 * @deprecated Replaced by {@link #JRXmlTemplateLoader(JasperReportsContext)}.
	 */
	protected JRXmlTemplateLoader()
	{
		this(DefaultJasperReportsContext.getInstance());
	}
	
	/**
	 *
	 */
	private JRXmlTemplateLoader(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	/**
	 *
	 */
	private static JRXmlTemplateLoader getDefaultInstance()
	{
		return new JRXmlTemplateLoader(DefaultJasperReportsContext.getInstance());
	}
	
	
	/**
	 *
	 */
	public static JRXmlTemplateLoader getInstance(JasperReportsContext jasperReportsContext)
	{
		return new JRXmlTemplateLoader(jasperReportsContext);
	}
	
	
	/**
	 * Parses a template XML found at a specified location into a {@link JRTemplate template object}.
	 * 
	 * @param location the template XML location.
	 * 	Can be a URL, a file path or a classloader resource name.
	 * @return the template object
	 * @throws JRException when the location cannot be resolved or read
	 * @see RepositoryUtil#getBytesFromLocation(String)
	 */
	public JRTemplate loadTemplate(String location) throws JRException
	{
		byte[] data = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(location);
		return load(new ByteArrayInputStream(data));
	}
	
	/**
	 * Parses a template XML file into a {@link JRTemplate template object}.
	 * 
	 * @param file the template XML file
	 * @return the template object
	 */
	public JRTemplate loadTemplate(File file)
	{
		BufferedInputStream fileIn;
		try
		{
			fileIn = new BufferedInputStream(new FileInputStream(file));
		}
		catch (FileNotFoundException e)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_TEMPLATE_NOT_FOUND,
					(Object[])null,
					e);
		}

		try
		{
			return load(fileIn);
		}
		finally
		{
			try
			{
				fileIn.close();
			}
			catch (IOException e)
			{
				log.warn("Error closing XML file", e);
			}
		}		
	}
	
	/**
	 * Parses a template XML located at a URL into a {@link JRTemplate template object}.
	 * 
	 * @param url the location of the template XML
	 * @return the template object
	 */
	public JRTemplate loadTemplate(URL url)
	{
		InputStream input;
		try
		{
			input = url.openStream();
		}
		catch (IOException e)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_URL_CONNECTION_ERROR,
					new Object[]{url},
					e);
		}

		try
		{
			return load(input);
		}
		finally
		{
			try
			{
				input.close();
			}
			catch (IOException e)
			{
				log.warn("Error closing connection to template URL " + url, e);
			}
		}		
	}
	
	/**
	 * Parses a template XML data stream into a {@link JRTemplate template object}.
	 * 
	 * @param data the data stream
	 * @return the template object
	 */
	public JRTemplate loadTemplate(InputStream data)
	{
		JRXmlDigester digester = JRXmlTemplateDigesterFactory.instance().createDigester(jasperReportsContext);
		try
		{
			return (JRTemplate) digester.parse(data);
		}
		catch (IOException e)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_TEMPLATE_READING_ERROR,
					(Object[])null,
					e);
		}
		catch (SAXException e)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_TEMPLATE_PARSING_ERROR,
					(Object[])null,
					e);
		}
	}
	
	/**
	 * @see #loadTemplate(String)
	 */
	public static JRTemplate load(String location) throws JRException
	{
		return getDefaultInstance().loadTemplate(location);
	}
	
	/**
	 * @see #loadTemplate(File)
	 */
	public static JRTemplate load(File file)
	{
		return getDefaultInstance().loadTemplate(file);
	}
	
	/**
	 * @see #loadTemplate(URL)
	 */
	public static JRTemplate load(URL url)
	{
		return getDefaultInstance().loadTemplate(url);
	}
	
	/**
	 * @see #loadTemplate(InputStream)
	 */
	public static JRTemplate load(InputStream data)
	{
		return getDefaultInstance().loadTemplate(data);
	}

}
