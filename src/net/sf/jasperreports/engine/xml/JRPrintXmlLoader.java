/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
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

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.digester.SetNestedPropertiesRule;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;


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
		digester.addFactoryCreate("*/box", JRBoxFactory.class.getName());
		digester.addSetNext("*/box", "setBox", JRBox.class.getName());

		/*   */
		digester.addFactoryCreate("*/image/imageSource", JRPrintImageSourceFactory.class.getName());
		digester.addCallMethod("*/image/imageSource", "setImageSource", 0);

		/*   */
		digester.addFactoryCreate("*/text", JRPrintTextFactory.class.getName());
		digester.addSetNext("*/text", "addElement", JRPrintElement.class.getName());
		SetNestedPropertiesRule textRule = new SetNestedPropertiesRule("textContent", "text");
		textRule.setTrimData(false);
		textRule.setAllowUnknownChildElements(true);
		digester.addRule("*/text", textRule);

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
