/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintXmlLoader implements ErrorHandler
{

	/**
	 *
	 */
	private JasperPrint jasperPrint = null;
	private List errors = new ArrayList();


	/**
	 *
	 */
	protected JRPrintXmlLoader()
	{
	}
	

	/**
	 *
	 */
	public void setJasperPrint(JasperPrint jasperPrint)
	{
		this.jasperPrint = jasperPrint;
	}


	/**
	 *
	 */
	public static JasperPrint load(String sourceFileName) throws JRException
	{
		JasperPrint jasperPrint = null;

		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(sourceFileName);
			JRPrintXmlLoader printXmlLoader = new JRPrintXmlLoader();
			jasperPrint = printXmlLoader.loadXML(fis);
		}
		catch(IOException e)
		{
			throw new JRException(e);
		}
		finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
				}
				catch(IOException e)
				{
				}
			}
		}

		return jasperPrint;
	}


	/**
	 *
	 */
	public static JasperPrint load(InputStream is) throws JRException
	{
		JasperPrint jasperPrint = null;

		JRPrintXmlLoader printXmlLoader = new JRPrintXmlLoader();
		jasperPrint = printXmlLoader.loadXML(is);

		return jasperPrint;
	}


	/**
	 *
	 */
	private JasperPrint loadXML(InputStream is) throws JRException
	{
		try
		{
			JRXmlDigester digester = prepareDigester();

			/*   */
			digester.parse(is);
		}
		catch(ParserConfigurationException e)
		{
			throw new JRException(e);
		}
		catch(SAXException e)
		{
			throw new JRException(e);
		}
		catch(IOException e)
		{
			throw new JRException(e);
		}
		
		if (errors.size() > 0)
		{
			Exception e = (Exception)errors.get(0);
			if (e instanceof JRException)
			{
				throw (JRException)e;
			}
			else
			{
				throw new JRException(e);
			}
		}

		return this.jasperPrint;
	}


	/**
	 *
	 */
	private JRXmlDigester prepareDigester() throws ParserConfigurationException, SAXException
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		String validation = System.getProperty("jasper.reports.export.xml.validation");
		if (validation == null || validation.length() == 0)
		{
			validation = "true";
		}
		
		saxParserFactory.setValidating(Boolean.valueOf(validation).booleanValue());

		SAXParser saxParser = saxParserFactory.newSAXParser();
		//XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		XMLReader xmlReader = saxParser.getXMLReader();

		xmlReader.setFeature("http://xml.org/sax/features/validation", Boolean.valueOf(validation).booleanValue());

		JRXmlDigester digester = new JRXmlDigester(xmlReader);
		digester.push(this);
		//digester.setDebug(3);
		digester.setErrorHandler(this);
		digester.setValidating(true);
		
		/*   */
		digester.addFactoryCreate("jasperPrint", JasperPrintFactory.class.getName());
		digester.addSetNext("jasperPrint", "setJasperPrint", JasperPrint.class.getName());

		/*   */
		digester.addFactoryCreate("jasperPrint/reportFont", JRReportFontFactory.class.getName());
		digester.addSetNext("jasperPrint/reportFont", "addFont", JRReportFont.class.getName());

		/*   */
		digester.addFactoryCreate("jasperPrint/page", JRPrintPageFactory.class.getName());
		digester.addSetNext("jasperPrint/page", "addPage", JRPrintPage.class.getName());

		/*   */
		digester.addFactoryCreate("*/line", JRPrintLineFactory.class.getName());
		digester.addSetNext("*/line", "addElement", JRPrintElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/reportElement", JRPrintElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/graphicElement", JRPrintGraphicElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/rectangle", JRPrintRectangleFactory.class.getName());
		digester.addSetNext("*/rectangle", "addElement", JRPrintElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/ellipse", JRPrintEllipseFactory.class.getName());
		digester.addSetNext("*/ellipse", "addElement", JRPrintElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/image", JRPrintImageFactory.class.getName());
		digester.addSetNext("*/image", "addElement", JRPrintElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/image/imageSource", JRPrintImageSourceFactory.class.getName());
		digester.addCallMethod("*/image/imageSource", "setImageSource", 0);

		/*   */
		digester.addFactoryCreate("*/text", JRPrintTextFactory.class.getName());
		digester.addSetNext("*/text", "addElement", JRPrintElement.class.getName());
		digester.addCallMethod("*/text/textContent", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/text/font", JRPrintFontFactory.class.getName());
		digester.addSetNext("*/text/font", "setFont", JRFont.class.getName());

		return digester;
	}


	/**
	 *
	 */
	public void addError(Exception e)
	{
		this.errors.add(e);
	}
	
	/**
	 *
	 */
	public void error(SAXParseException e)
	{
		this.errors.add(e);
	}
	
	/**
	 *
	 */
	public void fatalError(SAXParseException e)
	{
		this.errors.add(e);
	}
	
	/**
	 *
	 */
	public void warning(SAXParseException e)
	{
		this.errors.add(e);
	}


}
