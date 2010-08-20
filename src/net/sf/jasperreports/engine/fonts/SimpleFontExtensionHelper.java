/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.fonts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRStyledTextParser.java 3082 2009-10-02 12:11:22Z teodord $
 */
public final class SimpleFontExtensionHelper implements ErrorHandler
{
	private static final Log log = LogFactory.getLog(SimpleFontExtensionHelper.class);

	/**
	 *
	 */
	private static final String NODE_fontFamily = "fontFamily";
	private static final String NODE_normal = "normal";
	private static final String NODE_bold = "bold";
	private static final String NODE_italic = "italic";
	private static final String NODE_boldItalic = "boldItalic";
	private static final String NODE_pdfEncoding = "pdfEncoding";
	private static final String NODE_pdfEmbedded = "pdfEmbedded";
	private static final String NODE_exportFonts = "exportFonts";
	private static final String NODE_export = "export";
	private static final String NODE_locales = "locales";
	private static final String NODE_locale = "locale";
	private static final String ATTRIBUTE_name = "name";
	private static final String ATTRIBUTE_key = "key";

	/**
	 * Thread local soft cache of instances.
	 */
	private static final ThreadLocal threadInstances = new ThreadLocal();
	
	/**
	 * Return a cached instance.
	 * 
	 * @return a cached instance
	 */
	public static SimpleFontExtensionHelper getInstance()
	{
		SimpleFontExtensionHelper instance = null;
		SoftReference instanceRef = (SoftReference) threadInstances.get();
		if (instanceRef != null)
		{
			instance = (SimpleFontExtensionHelper) instanceRef.get();
		}
		if (instance == null)
		{
			instance = new SimpleFontExtensionHelper();
			threadInstances.set(new SoftReference(instance));
		}
		return instance;
	}
	
	/**
	 *
	 */
	private DocumentBuilder documentBuilder;


	/**
	 *
	 */
	private SimpleFontExtensionHelper()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			documentBuilder = factory.newDocumentBuilder();
			documentBuilder.setErrorHandler(this);
		}
		catch (ParserConfigurationException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	/**
	 *
	 */
	public List loadFontFamilies(String file)
	{
		InputStream is = null; 
		
		try
		{
			is = JRLoader.getLocationInputStream(file);
			return loadFontFamilies(is);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}
	
	/**
	 *
	 */
	public List loadFontFamilies(InputStream is)
	{
		List fontFamilies = null;

		try
		{
			Document document = documentBuilder.parse(new InputSource(new InputStreamReader(is, "UTF-8")));
			fontFamilies = parseFontFamilies(document.getDocumentElement());
		}
		catch (SAXException e)
		{
			throw new JRRuntimeException(e);
		}
		
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return fontFamilies;
	}

	/**
	 *
	 */
	private List parseFontFamilies(Node fontFamiliesNode) throws SAXException
	{
		List fontFamilies = new ArrayList();
		
		NodeList nodeList = fontFamiliesNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (
				node.getNodeType() == Node.ELEMENT_NODE
				&& NODE_fontFamily.equals(node.getNodeName())
				)
			{
				fontFamilies.add(parseFontFamily(node));
			}
		}
		
		return fontFamilies;
	}

	/**
	 *
	 */
	private FontFamily parseFontFamily(Node fontFamilyNode) throws SAXException
	{
		SimpleFontFamily fontFamily = new SimpleFontFamily();
		
		NamedNodeMap nodeAttrs = fontFamilyNode.getAttributes();
		
		if (nodeAttrs.getNamedItem(ATTRIBUTE_name) != null)
		{
			fontFamily.setName(nodeAttrs.getNamedItem(ATTRIBUTE_name).getNodeValue());
		}

		NodeList nodeList = fontFamilyNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				if (NODE_normal.equals(node.getNodeName()))
				{
					fontFamily.setNormal(node.getTextContent());
				}
				else if (NODE_bold.equals(node.getNodeName()))
				{
					fontFamily.setBold(node.getTextContent());
				}
				else if (NODE_italic.equals(node.getNodeName()))
				{
					fontFamily.setItalic(node.getTextContent());
				}
				else if (NODE_boldItalic.equals(node.getNodeName()))
				{
					fontFamily.setBoldItalic(node.getTextContent());
				}
				else if (NODE_pdfEncoding.equals(node.getNodeName()))
				{
					fontFamily.setPdfEncoding(node.getTextContent());
				}
				else if (NODE_pdfEmbedded.equals(node.getNodeName()))
				{
					fontFamily.setPdfEmbedded(Boolean.valueOf(node.getTextContent()));
				}
				else if (NODE_exportFonts.equals(node.getNodeName()))
				{
					fontFamily.setExportFonts(parseExportFonts(node));
				}
				else if (NODE_locales.equals(node.getNodeName()))
				{
					fontFamily.setLocales(parseLocales(node));
				}
			}
		}
		
		return fontFamily;
	}

	/**
	 *
	 */
	private Map parseExportFonts(Node exportFontsNode) throws SAXException
	{
		Map exportFonts = new HashMap();
		
		NodeList nodeList = exportFontsNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (
				node.getNodeType() == Node.ELEMENT_NODE
				&& NODE_export.equals(node.getNodeName())
				)
			{
				NamedNodeMap nodeAttrs = node.getAttributes();
				
				if (nodeAttrs.getNamedItem(ATTRIBUTE_key) != null)
				{
					exportFonts.put(nodeAttrs.getNamedItem(ATTRIBUTE_key).getNodeValue(), node.getTextContent());
				}
			}
		}
		
		return exportFonts;
	}

	/**
	 *
	 */
	private Set parseLocales(Node localesNode) throws SAXException
	{
		Set locales = new HashSet();
		
		NodeList nodeList = localesNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (
				node.getNodeType() == Node.ELEMENT_NODE
				&& NODE_locale.equals(node.getNodeName())
				)
			{
				locales.add(node.getTextContent());
			}
		}
		
		return locales;
	}


	public void error(SAXParseException e) {
		if(log.isErrorEnabled())
		{
			log.error("Error parsing styled text.", e);
		}
	}

	public void fatalError(SAXParseException e) {
		if(log.isFatalEnabled())
		{
			log.fatal("Error parsing styled text.", e);
		}
	}

	public void warning(SAXParseException e) {
		if(log.isWarnEnabled())
		{
			log.warn("Error parsing styled text.", e);
		}
	}

}
