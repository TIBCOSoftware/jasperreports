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
package net.sf.jasperreports.engine.fonts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.repo.RepositoryUtil;

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
 */
public final class SimpleFontExtensionHelper implements ErrorHandler
{
	private static final Log log = LogFactory.getLog(SimpleFontExtensionHelper.class);
	public static final String EXCEPTION_MESSAGE_KEY_FILE_WRITER_ERROR = "fonts.file.writer.error";
	public static final String EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITER_ERROR = "fonts.output.stream.writer.error";
	
	/**
	 * Default XML output encoding.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	

	/**
	 *
	 */
	private static final String NODE_fontFamily = "fontFamily";
	private static final String NODE_normal = "normal";
	private static final String NODE_bold = "bold";
	private static final String NODE_italic = "italic";
	private static final String NODE_boldItalic = "boldItalic";
	private static final String NODE_ttf = "ttf";
	private static final String NODE_pdf = "pdf";
	private static final String NODE_eot = "eot";
	private static final String NODE_svg = "svg";
	private static final String NODE_woff = "woff";
	/**
	 * @deprecated Replaced by {@link #NODE_pdf}.
	 */
	private static final String NODE_normalPdfFont = "normalPdfFont";
	/**
	 * @deprecated Replaced by {@link #NODE_pdf}.
	 */
	private static final String NODE_boldPdfFont = "boldPdfFont";
	/**
	 * @deprecated Replaced by {@link #NODE_pdf}.
	 */
	private static final String NODE_italicPdfFont = "italicPdfFont";
	/**
	 * @deprecated Replaced by {@link #NODE_pdf}.
	 */
	private static final String NODE_boldItalicPdfFont = "boldItalicPdfFont";
	private static final String NODE_pdfEncoding = "pdfEncoding";
	private static final String NODE_pdfEmbedded = "pdfEmbedded";
	private static final String NODE_exportFonts = "exportFonts";
	private static final String NODE_export = "export";
	private static final String NODE_locales = "locales";
	private static final String NODE_locale = "locale";
	private static final String ATTRIBUTE_name = "name";
	private static final String ATTRIBUTE_visible = "visible";
	private static final String ATTRIBUTE_key = "key";
	
	/**
	 * Return a new instance.
	 * 
	 * @return a new instance
	 */
	public static SimpleFontExtensionHelper getInstance()
	{
		SimpleFontExtensionHelper instance = new SimpleFontExtensionHelper();
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
	 * @see #loadFontFamilies(JasperReportsContext, String)
	 */
	public List<FontFamily> loadFontFamilies(String file)
	{
		return loadFontFamilies(DefaultJasperReportsContext.getInstance(), file);
	}
	
	/**
	 *
	 */
	public List<FontFamily> loadFontFamilies(JasperReportsContext jasperReportsContext, String file)
	{
		InputStream is = null; 
		
		try
		{
			is = RepositoryUtil.getInstance(jasperReportsContext).getInputStreamFromLocation(file);
			return loadFontFamilies(jasperReportsContext, is);
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
	public List<FontFamily> loadFontFamilies(JasperReportsContext jasperReportsContext, InputStream is)
	{
		List<FontFamily> fontFamilies = null;

		try
		{
			Document document = documentBuilder.parse(new InputSource(new InputStreamReader(is, "UTF-8")));
			fontFamilies = parseFontFamilies(jasperReportsContext, document.getDocumentElement());
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
	 * @deprecated To be removed.
	 */
	public List<FontFamily> loadFontFamilies(InputStream is)
	{
		return loadFontFamilies(DefaultJasperReportsContext.getInstance(), is);
	}

	/**
	 *
	 */
	private List<FontFamily> parseFontFamilies(JasperReportsContext jasperReportsContext, Node fontFamiliesNode) throws SAXException
	{
		List<FontFamily> fontFamilies = new ArrayList<FontFamily>();
		
		NodeList nodeList = fontFamiliesNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (
				node.getNodeType() == Node.ELEMENT_NODE
				&& NODE_fontFamily.equals(node.getNodeName())
				)
			{
				fontFamilies.add(parseFontFamily(jasperReportsContext, node));
			}
		}
		
		return fontFamilies;
	}

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	private SimpleFontFamily parseFontFamily(JasperReportsContext jasperReportsContext, Node fontFamilyNode) throws SAXException
	{
		SimpleFontFamily fontFamily = new SimpleFontFamily(jasperReportsContext);
		
		NamedNodeMap nodeAttrs = fontFamilyNode.getAttributes();
		
		if (nodeAttrs.getNamedItem(ATTRIBUTE_name) != null)
		{
			fontFamily.setName(nodeAttrs.getNamedItem(ATTRIBUTE_name).getNodeValue());
		}
		if (nodeAttrs.getNamedItem(ATTRIBUTE_visible) != null)
		{
			fontFamily.setVisible(
				Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_visible).getNodeValue()).booleanValue()
				);
		}

		NodeList nodeList = fontFamilyNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				if (NODE_normal.equals(node.getNodeName()))
				{
					fontFamily.setNormalFace(parseFontFace(jasperReportsContext, node));
				}
				else if (NODE_bold.equals(node.getNodeName()))
				{
					fontFamily.setBoldFace(parseFontFace(jasperReportsContext, node));
				}
				else if (NODE_italic.equals(node.getNodeName()))
				{
					fontFamily.setItalicFace(parseFontFace(jasperReportsContext, node));
				}
				else if (NODE_boldItalic.equals(node.getNodeName()))
				{
					fontFamily.setBoldItalicFace(parseFontFace(jasperReportsContext, node));
				}
				else if (NODE_normalPdfFont.equals(node.getNodeName()))
				{
					fontFamily.setNormalPdfFont(node.getTextContent());
				}
				else if (NODE_boldPdfFont.equals(node.getNodeName()))
				{
					fontFamily.setBoldPdfFont(node.getTextContent());
				}
				else if (NODE_italicPdfFont.equals(node.getNodeName()))
				{
					fontFamily.setItalicPdfFont(node.getTextContent());
				}
				else if (NODE_boldItalicPdfFont.equals(node.getNodeName()))
				{
					fontFamily.setBoldItalicPdfFont(node.getTextContent());
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
	private SimpleFontFace parseFontFace(JasperReportsContext jasperReportsContext, Node fontFaceNode) throws SAXException
	{
		SimpleFontFace fontFace = new SimpleFontFace(jasperReportsContext);
		
		NodeList nodeList = fontFaceNode.getChildNodes();

		if (
			nodeList.getLength() == 1 
			&& (fontFaceNode.getFirstChild().getNodeType() == Node.TEXT_NODE
			|| fontFaceNode.getFirstChild().getNodeType() == Node.CDATA_SECTION_NODE)
			)
		{
			fontFace.setTtf(fontFaceNode.getFirstChild().getTextContent());
		}
		else
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					if (NODE_ttf.equals(node.getNodeName()))
					{
						fontFace.setTtf(node.getTextContent());
					}
					else if (NODE_pdf.equals(node.getNodeName()))
					{
						fontFace.setPdf(node.getTextContent());
					}
					else if (NODE_eot.equals(node.getNodeName()))
					{
						fontFace.setEot(node.getTextContent());
					}
					else if (NODE_svg.equals(node.getNodeName()))
					{
						fontFace.setSvg(node.getTextContent());
					}
					else if (NODE_woff.equals(node.getNodeName()))
					{
						fontFace.setWoff(node.getTextContent());
					}
				}
			}
		}
		
		return fontFace;
	}

	/**
	 *
	 */
	private Map<String,String> parseExportFonts(Node exportFontsNode) throws SAXException
	{
		Map<String,String> exportFonts = new HashMap<String,String>();
		
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
	public static String getFontsXml(List<FontFamily> fontFamilies)
	{
		StringBuffer buffer = null;
		
		if(fontFamilies != null)
		{
			buffer = new StringBuffer();
			buffer.append("<?xml version=\"1.0\" encoding=\"" + DEFAULT_ENCODING + "\"?>\n");
			buffer.append("<fontFamilies>\n");
			for (FontFamily fontFamily : fontFamilies)
			{
				writeFontFamily(buffer, fontFamily);
			}
			buffer.append("</fontFamilies>\n");
			return buffer.toString();
		}
		else
		{
			log.error("There are no font families in the list.");
			return null;
		}
	}

	/**
	 *
	 */
	private static void writeFontFamily(StringBuffer buffer, FontFamily fontFamily) 
	{
		if(fontFamily != null)
		{
			if(fontFamily.getName() == null)
			{
				log.error("Font family name is required.");
				return;
			}
			
			String indent = "  ";
			buffer.append(indent + "<fontFamily name=\"" + fontFamily.getName() + "\""); 
			if (!fontFamily.isVisible())
			{
				buffer.append(" visible=\"" + fontFamily.isVisible() + "\""); 
			}
			buffer.append(">\n");
			indent = "    ";
			
			writeFontFace(buffer, fontFamily.getNormalFace(), NODE_normal);
			writeFontFace(buffer, fontFamily.getBoldFace(), NODE_bold);
			writeFontFace(buffer, fontFamily.getItalicFace(), NODE_italic);
			writeFontFace(buffer, fontFamily.getBoldItalicFace(), NODE_boldItalic);

			if(fontFamily.getPdfEncoding() != null)
			{
				buffer.append(indent + "<pdfEncoding>" + fontFamily.getPdfEncoding() +"</pdfEncoding>\n");
				
			}
			if(fontFamily.isPdfEmbedded() != null)
			{
				buffer.append(indent + "<pdfEmbedded>" + fontFamily.isPdfEmbedded() +"</pdfEmbedded>\n");
				
			}
			
			if(fontFamily instanceof SimpleFontFamily)
			{
				SimpleFontFamily simpleFontFamily = (SimpleFontFamily)fontFamily;
				
				Map<String, String> exportFonts = simpleFontFamily.getExportFonts();
				
				if(exportFonts != null)
				{
					buffer.append(indent + "<exportFonts>\n");
					indent = "      ";
					for(String key : exportFonts.keySet())
					{
						buffer.append(indent + "<export key=\"" + key +"\">" + exportFonts.get(key) + "</export>\n");
					}
					indent = "    ";
					buffer.append(indent + "</exportFonts>\n");
				}
				
				Set<String> locales = simpleFontFamily.getLocales();
				
				if(locales != null)
				{
					buffer.append(indent + "<locales>\n");
					indent = "      ";
					for(String locale : locales)
					{
						buffer.append(indent + "<locale>" + locale + "</locale>\n");
					}
					indent = "    ";
					buffer.append(indent + "</locales>\n");
				}
			}
			indent = "  ";
			buffer.append(indent + "</fontFamily>\n\n");
		}		
		else
		{
			log.info("Null font family encountered.");
		}
	}
	


	/**
	 *
	 */
	private static void writeFontFace(StringBuffer buffer, FontFace fontFace, String faceTypeName) 
	{
		String indent = "    ";
		if(fontFace != null)
		{
			if (
				fontFace.getPdf() == null
				&& fontFace.getEot() == null
				&& fontFace.getSvg() == null
				&& fontFace.getWoff() == null
				)
			{
				if (fontFace.getTtf() != null)
				{
					buffer.append(indent + "<" + faceTypeName + ">"	+ fontFace.getTtf() + "</" + faceTypeName + ">\n");
				}
			}
			else
			{
				buffer.append(indent + "<" + faceTypeName + ">\n");
				if (fontFace.getTtf() != null)
				{
					buffer.append(indent + "  <ttf>"	+ fontFace.getTtf() + "</ttf>\n");
				}
				if (fontFace.getPdf() != null)
				{
					buffer.append(indent + "  <pdf>"	+ fontFace.getPdf() + "</pdf>\n");
				}
				if (fontFace.getEot() != null)
				{
					buffer.append(indent + "  <eot>"	+ fontFace.getEot() + "</eot>\n");
				}
				if (fontFace.getSvg() != null)
				{
					buffer.append(indent + "  <svg>"	+ fontFace.getSvg() + "</svg>\n");
				}
				if (fontFace.getWoff() != null)
				{
					buffer.append(indent + "  <woff>"	+ fontFace.getWoff() + "</woff>\n");
				}
				buffer.append(indent + "</" + faceTypeName + ">\n");
			}
		}
	}
	

	/**
	 *
	 */
	public static void writeFontsXml(
		String destFileName,
		List<FontFamily> fontFamilies
		) throws JRException
	{
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(destFileName);
			writeFontsXml(fos, fontFamilies);
		}
		catch (IOException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_FILE_WRITER_ERROR,
					new Object[]{destFileName},
					e);
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
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
	public static void writeFontsXml(
		OutputStream outputStream,
		List<FontFamily> fontFamilies
		) throws JRException
	{
		Writer out = null;
		try
		{
			out = new OutputStreamWriter(outputStream, DEFAULT_ENCODING);
			out.write(getFontsXml(fontFamilies));
			out.flush();
		}
		catch (Exception e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITER_ERROR, 
					null, 
					e);
		}
	}


	/**
	 *
	 */
	public static void writeFontExtensionsProperties(String fontsXmlLocation, String destFileName) throws JRException
	{
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(destFileName, false);
			writeFontExtensionsProperties(fontsXmlLocation, fos);
		}
		catch (IOException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_FILE_WRITER_ERROR,
					new Object[]{destFileName},
					e);
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
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
	public static void writeFontExtensionsProperties(String fontFamiliesPropertyValue, OutputStream outputStream) throws JRException
	{
		writeFontExtensionsProperties(
				SimpleFontExtensionsRegistryFactory.PROPERTY_SIMPLE_FONT_FAMILIES_REGISTRY_FACTORY,
				SimpleFontExtensionsRegistryFactory.class.getName(),
				SimpleFontExtensionsRegistryFactory.SIMPLE_FONT_FAMILIES_PROPERTY_PREFIX +	"location", 
				fontFamiliesPropertyValue,
				outputStream);
	}


	/**
	 *
	 */
	public static void writeFontExtensionsProperties(
			String fontRegistryFactoryPropertyName, 
			String fontRegistryFactoryPropertyValue, 
			String fontFamiliesPropertyName, 
			String fontFamiliesPropertyValue, 
			OutputStream outputStream
			) throws JRException
	{
		Writer out = null;
		try
		{
			out = new OutputStreamWriter(outputStream, DEFAULT_ENCODING);
			out.write(
					fontRegistryFactoryPropertyName + 
					"=" + 
					fontRegistryFactoryPropertyValue + 
					"\n"
					);
			out.write(
					fontFamiliesPropertyName +
					"=" + 
					fontFamiliesPropertyValue + 
					"\n"
					);
			out.flush();
		}
		catch (Exception e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITER_ERROR, 
					null, 
					e);
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
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
	private Set<String> parseLocales(Node localesNode) throws SAXException
	{
		Set<String> locales = new HashSet<String>();
		
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
