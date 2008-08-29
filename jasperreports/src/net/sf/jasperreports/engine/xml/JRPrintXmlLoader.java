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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.digester.SetNestedPropertiesRule;
import org.apache.commons.digester.SetPropertiesRule;
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
			throw new JRException(e);
		}

		return this.jasperPrint;
	}


	/**
	 *
	 */
	private JRXmlDigester prepareDigester() throws ParserConfigurationException, SAXException
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		
		boolean validating = JRProperties.getBooleanProperty(JRProperties.EXPORT_XML_VALIDATION);		
		saxParserFactory.setValidating(validating);

		SAXParser saxParser = saxParserFactory.newSAXParser();
		//XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		XMLReader xmlReader = saxParser.getXMLReader();

		xmlReader.setFeature("http://xml.org/sax/features/validation", validating);

		JRXmlDigester digester = new JRXmlDigester(xmlReader);
		digester.push(this);
		//digester.setDebug(3);
		digester.setErrorHandler(this);
		digester.setValidating(true);
		
		/*   */
		digester.addFactoryCreate("jasperPrint", JasperPrintFactory.class.getName());
		digester.addSetNext("jasperPrint", "setJasperPrint", JasperPrint.class.getName());

		/*   */
		digester.addRule("*/property", new JRPropertyDigesterRule());

		/*   */
		digester.addFactoryCreate("jasperPrint/origin", JROriginFactory.class.getName());
		digester.addSetNext("jasperPrint/origin", "addOrigin", JROrigin.class.getName());

		/*   */
		digester.addFactoryCreate("jasperPrint/reportFont", JRReportFontFactory.class.getName());
		digester.addSetNext("jasperPrint/reportFont", "addFont", JRReportFont.class.getName());

		/*   */
		digester.addFactoryCreate("jasperPrint/style", JRPrintStyleFactory.class.getName());
		digester.addSetNext("jasperPrint/style", "addStyle", JRStyle.class.getName());
		
		/*   */
		digester.addFactoryCreate("*/style/pen", JRPenFactory.Style.class.getName());

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
		digester.addFactoryCreate("*/pen", JRPenFactory.class.getName());

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

		/*   */
		digester.addFactoryCreate("*/box/pen", JRPenFactory.Box.class.getName());
		digester.addFactoryCreate("*/box/topPen", JRPenFactory.Top.class.getName());
		digester.addFactoryCreate("*/box/leftPen", JRPenFactory.Left.class.getName());
		digester.addFactoryCreate("*/box/bottomPen", JRPenFactory.Bottom.class.getName());
		digester.addFactoryCreate("*/box/rightPen", JRPenFactory.Right.class.getName());

		/*   */
		digester.addFactoryCreate("*/image/imageSource", JRPrintImageSourceFactory.class.getName());
		digester.addCallMethod("*/image/imageSource", "setImageSource", 0);

		/*   */
		digester.addFactoryCreate("*/text", JRPrintTextFactory.class.getName());
		digester.addSetNext("*/text", "addElement", JRPrintElement.class.getName());
		SetNestedPropertiesRule textRule = new SetNestedPropertiesRule(
				new String[]{"textContent", "textTruncateSuffix", "reportElement", "box", "font"}, 
				new String[]{"text", "textTruncateSuffix"});
		textRule.setTrimData(false);
		textRule.setAllowUnknownChildElements(true);
		digester.addRule("*/text", textRule);

		digester.addRule("*/text/textContent", 
				new SetPropertiesRule(JRXmlConstants.ATTRIBUTE_truncateIndex, "textTruncateIndex"));
		
		/*   */
		digester.addFactoryCreate("*/text/font", JRPrintFontFactory.class.getName());
		digester.addSetNext("*/text/font", "setFont", JRFont.class.getName());
		
		addFrameRules(digester);

		addHyperlinkParameterRules(digester);
		
		addGenericElementRules(digester);
		
		return digester;
	}


	private void addFrameRules(JRXmlDigester digester)
	{
		digester.addFactoryCreate("*/frame", JRPrintFrameFactory.class.getName());
		digester.addSetNext("*/frame", "addElement", JRPrintElement.class.getName());
	}


	protected void addHyperlinkParameterRules(JRXmlDigester digester)
	{
		String parameterPattern = "*/" + JRXmlConstants.ELEMENT_hyperlinkParameter;
		digester.addFactoryCreate(parameterPattern, JRPrintHyperlinkParameterFactory.class);
		digester.addSetNext(parameterPattern, "addHyperlinkParameter", JRPrintHyperlinkParameter.class.getName());
		
		String parameterValuePattern = parameterPattern + "/" + JRXmlConstants.ELEMENT_hyperlinkParameterValue;
		digester.addFactoryCreate(parameterValuePattern, JRPrintHyperlinkParameterValueFactory.class);
		digester.addCallMethod(parameterValuePattern, "setData", 0);
	}


	protected void addGenericElementRules(JRXmlDigester digester)
	{
		String elementPattern = "*/" + JRXmlConstants.ELEMENT_genericElement;
		digester.addFactoryCreate(elementPattern, 
				JRGenericPrintElementFactory.class);
		digester.addSetNext(elementPattern, "addElement", 
				JRPrintElement.class.getName());
		
		String elementTypePattern = elementPattern + "/" 
				+ JRXmlConstants.ELEMENT_genericElementType;
		digester.addFactoryCreate(elementTypePattern, 
				JRGenericElementTypeFactory.class);
		digester.addSetNext(elementTypePattern, "setGenericType");
		
		String elementParameterPattern = elementPattern + "/" 
				+ JRXmlConstants.ELEMENT_genericElementParameter;
		digester.addFactoryCreate(elementParameterPattern, 
				JRGenericPrintElementParameterFactory.class);
		digester.addCallMethod(elementParameterPattern, "addParameter");
		
		String elementParameterValuePattern = elementParameterPattern + "/"
				+ JRXmlConstants.ELEMENT_genericElementParameterValue;
		digester.addFactoryCreate(elementParameterValuePattern, 
				JRGenericPrintElementParameterFactory.ParameterValueFactory.class);
		digester.addSetNext(elementParameterValuePattern, "setValue");
		digester.addCallMethod(elementParameterValuePattern, "setData", 0);
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
