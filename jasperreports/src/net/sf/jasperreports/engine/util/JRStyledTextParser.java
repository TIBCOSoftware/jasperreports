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
package net.sf.jasperreports.engine.util;

import java.awt.font.TextAttribute;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.StringReader;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyledTextParser
{

	/**
	 *
	 */
	private static final String ROOT_START = "<st>";
	private static final String ROOT_END = "</st>";
	private static final String NODE_style = "style";
	private static final String NODE_bold = "b";
	private static final String NODE_italic = "i";
	private static final String NODE_underline = "u";
	private static final String NODE_font = "font";
	private static final String NODE_br = "br";
	private static final String NODE_li = "li";
	private static final String ATTRIBUTE_fontName = "fontName";
	private static final String ATTRIBUTE_fontFace = "face";
	private static final String ATTRIBUTE_color = "color";
	private static final String ATTRIBUTE_size = "size";
	private static final String ATTRIBUTE_isBold = "isBold";
	private static final String ATTRIBUTE_isItalic = "isItalic";
	private static final String ATTRIBUTE_isUnderline = "isUnderline";
	private static final String ATTRIBUTE_isStrikeThrough = "isStrikeThrough";
	private static final String ATTRIBUTE_forecolor = "forecolor";
	private static final String ATTRIBUTE_backcolor = "backcolor";
	private static final String ATTRIBUTE_pdfFontName = "pdfFontName";
	private static final String ATTRIBUTE_pdfEncoding = "pdfEncoding";
	private static final String ATTRIBUTE_isPdfEmbedded = "isPdfEmbedded";

	private static final String SPACE = " ";
	private static final String EQUAL_QUOTE = "=\"";
	private static final String QUOTE = "\"";
	private static final String SHARP = "#";
	private static final String LESS = "<";
	private static final String LESS_SLASH = "</";
	private static final String GREATER = ">";
	private static final String SIX_ZEROS = "000000";
	private static final int colorMask = Integer.parseInt("FFFFFF", 16);

	/**
	 *
	 */
	private DocumentBuilder documentBuilder = null;


	/**
	 *
	 */
	public JRStyledTextParser()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			documentBuilder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	/**
	 *
	 */
	public JRStyledText parse(Map attributes, String text) throws SAXException
	{
		JRStyledText styledText = new JRStyledText();
		
		Document document = null;

		try
		{
			document = documentBuilder.parse(new InputSource(new StringReader(ROOT_START + text + ROOT_END)));
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		
		parseStyle(styledText, document.getDocumentElement());
		
		styledText.addRun(new JRStyledText.Run(attributes, 0, styledText.length()));
		
		return styledText;
	}

	/**
	 *
	 */
	public String write(Map parentAttrs, AttributedCharacterIterator iterator, String text)
	{
		StringBuffer sbuffer = new StringBuffer();
		
		int runLimit = 0;

		while(runLimit < iterator.getEndIndex() && (runLimit = iterator.getRunLimit()) <= iterator.getEndIndex())
		{
			String chunk = text.substring(iterator.getIndex(), runLimit);
			Map attrs = iterator.getAttributes();
			
			StringBuffer styleBuffer = writeStyleAttributes(parentAttrs, attrs);
			if (styleBuffer.length() > 0)
			{
				sbuffer.append(LESS);
				sbuffer.append(NODE_style);
				sbuffer.append(styleBuffer.toString());
				sbuffer.append(GREATER);
				sbuffer.append(chunk);
				sbuffer.append(LESS_SLASH);
				sbuffer.append(NODE_style);
				sbuffer.append(GREATER);
			}
			else
			{
				sbuffer.append(chunk);
			}

			iterator.setIndex(runLimit);
		}
		
		return sbuffer.toString();
	}

	/**
	 *
	 */
	private void parseStyle(JRStyledText styledText, Node parentNode)
	{
		NodeList nodeList = parentNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.TEXT_NODE)
			{
				styledText.append(node.getNodeValue());
			}
			else if (
				node.getNodeType() == Node.ELEMENT_NODE
				&& NODE_style.equals(node.getNodeName())
				)
			{
				NamedNodeMap nodeAttrs = node.getAttributes();

				Map styleAttrs = new HashMap();

				if (nodeAttrs.getNamedItem(ATTRIBUTE_fontName) != null)
				{
					styleAttrs.put(
						TextAttribute.FAMILY, 
						nodeAttrs.getNamedItem(ATTRIBUTE_fontName).getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isBold) != null)
				{
					styleAttrs.put(
						TextAttribute.WEIGHT, 
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isBold).getNodeValue()).booleanValue() 
							? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isItalic) != null)
				{
					styleAttrs.put(
						TextAttribute.POSTURE, 
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isItalic).getNodeValue()).booleanValue() 
							? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isUnderline) != null)
				{
					styleAttrs.put(
						TextAttribute.UNDERLINE, 
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isUnderline).getNodeValue()).booleanValue() 
							? TextAttribute.UNDERLINE_ON : null
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isStrikeThrough) != null)
				{
					styleAttrs.put(
						TextAttribute.STRIKETHROUGH, 
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isStrikeThrough).getNodeValue()).booleanValue() 
							? TextAttribute.STRIKETHROUGH_ON : null
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_size) != null)
				{
					styleAttrs.put(
						TextAttribute.SIZE, 
						new Float(nodeAttrs.getNamedItem(ATTRIBUTE_size).getNodeValue()) 
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_pdfFontName) != null)
				{
					styleAttrs.put(
						JRTextAttribute.PDF_FONT_NAME, 
						nodeAttrs.getNamedItem(ATTRIBUTE_pdfFontName).getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_pdfEncoding) != null)
				{
					styleAttrs.put(
						JRTextAttribute.PDF_ENCODING, 
						nodeAttrs.getNamedItem(ATTRIBUTE_pdfEncoding).getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isPdfEmbedded) != null)
				{
					styleAttrs.put(
						JRTextAttribute.IS_PDF_EMBEDDED, 
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isPdfEmbedded).getNodeValue()) 
						);
				}
				
				if (nodeAttrs.getNamedItem(ATTRIBUTE_forecolor) != null)
				{
					Color color = null;
					String colorStr = nodeAttrs.getNamedItem(ATTRIBUTE_forecolor).getNodeValue();
					if (colorStr != null && colorStr.length() > 0)
					{
						char firstChar = colorStr.charAt(0);
						if (firstChar == '#')
						{
							color = new Color(Integer.parseInt(colorStr.substring(1), 16));
						}
						else if ('0' <= firstChar && firstChar <= '9')
						{
							color = new Color(Integer.parseInt(colorStr));
						}
						else
						{
							if (JRXmlConstants.getColorMap().containsKey(colorStr))
							{
								color = (Color)JRXmlConstants.getColorMap().get(colorStr);
							}
							else
							{
								color = Color.black;
							}
						}
					}
					styleAttrs.put(
						TextAttribute.FOREGROUND, 
						color 
						);
				}
				
				if (nodeAttrs.getNamedItem(ATTRIBUTE_backcolor) != null)
				{
					Color color = null;
					String colorStr = nodeAttrs.getNamedItem(ATTRIBUTE_backcolor).getNodeValue();
					if (colorStr != null && colorStr.length() > 0)
					{
						char firstChar = colorStr.charAt(0);
						if (firstChar == '#')
						{
							color = new Color(Integer.parseInt(colorStr.substring(1), 16));
						}
						else if ('0' <= firstChar && firstChar <= '9')
						{
							color = new Color(Integer.parseInt(colorStr));
						}
						else
						{
							if (JRXmlConstants.getColorMap().containsKey(colorStr))
							{
								color = (Color)JRXmlConstants.getColorMap().get(colorStr);
							}
							else
							{
								color = Color.black;
							}
						}
					}
					styleAttrs.put(
						TextAttribute.BACKGROUND, 
						color 
						);
				}
				
				int startIndex = styledText.length();

				parseStyle(styledText, node);
				
				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_bold.equals(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_italic.equals(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_underline.equals(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_font.equals(node.getNodeName()))
			{
				NamedNodeMap nodeAttrs = node.getAttributes();

				Map styleAttrs = new HashMap();

				if (nodeAttrs.getNamedItem(ATTRIBUTE_fontFace) != null)
				{
					styleAttrs.put(
						JRTextAttribute.HTML_FONT_FACE,
						nodeAttrs.getNamedItem(ATTRIBUTE_fontFace).getNodeValue()
						);
				}
				if (nodeAttrs.getNamedItem(ATTRIBUTE_size) != null)
				{
					styleAttrs.put(
						TextAttribute.SIZE,
						new Float(nodeAttrs.getNamedItem(ATTRIBUTE_size).getNodeValue())
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_color) != null)
				{
					Color color = null;
					String colorStr = nodeAttrs.getNamedItem(ATTRIBUTE_color).getNodeValue();
					if (colorStr != null && colorStr.length() > 0)
					{
						char firstChar = colorStr.charAt(0);
						if (firstChar == '#')
						{
							color = new Color(Integer.parseInt(colorStr.substring(1), 16));
						}
						else if ('0' <= firstChar && firstChar <= '9')
						{
							color = new Color(Integer.parseInt(colorStr));
						}
						else
						{
							if (JRXmlConstants.getColorMap().containsKey(colorStr))
							{
								color = (Color)JRXmlConstants.getColorMap().get(colorStr);
							}
							else
							{
								color = Color.black;
							}
						}
					}
					styleAttrs.put(
						TextAttribute.FOREGROUND,
						color
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_fontFace) != null) {
					String[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
					String fontFaces = nodeAttrs.getNamedItem(ATTRIBUTE_fontFace).getNodeValue();

					StringTokenizer t = new StringTokenizer(fontFaces, ",");
					label:while (t.hasMoreTokens()) {
						String face = t.nextToken().trim();
						for (int j = 0; j < fontList.length; j++)
							if (fontList[j].equals(face)) {
								styleAttrs.put(TextAttribute.FAMILY, face);
								break label;
							}
					}
				}
				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));

			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_br.equals(node.getNodeName()))
			{
				styledText.append("\n");

				int startIndex = styledText.length();
				resizeRuns(styledText.getRuns(), startIndex, 1);

				parseStyle(styledText, node);
				styledText.addRun(new JRStyledText.Run(new HashMap(), startIndex, styledText.length()));

				if (startIndex < styledText.length()) {
					styledText.append("\n");
					resizeRuns(styledText.getRuns(), startIndex, 1);
				}
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_li.equals(node.getNodeName()))
			{
				styledText.append("\n \u2022 ");

				int startIndex = styledText.length();
				resizeRuns(styledText.getRuns(), startIndex, 1);

				parseStyle(styledText, node);
				styledText.addRun(new JRStyledText.Run(new HashMap(), startIndex, styledText.length()));

				styledText.append("\n");
				resizeRuns(styledText.getRuns(), startIndex, 1);
			}
		}
	}

	/**
	 *
	 */
	private void resizeRuns(List runs, int startIndex, int count)
	{
		for (int j = 0; j < runs.size(); j++)
		{
			JRStyledText.Run run = (JRStyledText.Run) runs.get(j);
			if (run.startIndex <= startIndex && run.endIndex > startIndex - count)
				run.endIndex += count;
		}
	}


	/**
	 *
	 */
	private StringBuffer writeStyleAttributes(Map parentAttrs,  Map attrs)
	{
		StringBuffer sbuffer = new StringBuffer();
		
		Object value = attrs.get(TextAttribute.FAMILY);
		Object oldValue = parentAttrs.get(TextAttribute.FAMILY);
		
		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_fontName);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value);
			sbuffer.append(QUOTE);
		}

		value = attrs.get(TextAttribute.WEIGHT);
		oldValue = parentAttrs.get(TextAttribute.WEIGHT);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_isBold);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value.equals(TextAttribute.WEIGHT_BOLD));
			sbuffer.append(QUOTE);
		}

		value = attrs.get(TextAttribute.POSTURE);
		oldValue = parentAttrs.get(TextAttribute.POSTURE);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_isItalic);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value.equals(TextAttribute.POSTURE_OBLIQUE));
			sbuffer.append(QUOTE);
		}

		value = attrs.get(TextAttribute.UNDERLINE);
		oldValue = parentAttrs.get(TextAttribute.UNDERLINE);

		if (
			(value == null && oldValue != null)
			|| (value != null && !value.equals(oldValue))
			)
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_isUnderline);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value != null);
			sbuffer.append(QUOTE);
		}

		value = attrs.get(TextAttribute.STRIKETHROUGH);
		oldValue = parentAttrs.get(TextAttribute.STRIKETHROUGH);

		if (
			(value == null && oldValue != null)
			|| (value != null && !value.equals(oldValue))
			)
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_isStrikeThrough);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value != null);
			sbuffer.append(QUOTE);
		}

		value = attrs.get(TextAttribute.SIZE);
		oldValue = parentAttrs.get(TextAttribute.SIZE);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_size);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(((Float)value).intValue());
			sbuffer.append(QUOTE);
		}

		value = attrs.get(JRTextAttribute.PDF_FONT_NAME);
		oldValue = parentAttrs.get(JRTextAttribute.PDF_FONT_NAME);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_pdfFontName);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value);
			sbuffer.append(QUOTE);
		}

		value = attrs.get(JRTextAttribute.PDF_ENCODING);
		oldValue = parentAttrs.get(JRTextAttribute.PDF_ENCODING);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_pdfEncoding);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value);
			sbuffer.append(QUOTE);
		}

		value = attrs.get(JRTextAttribute.IS_PDF_EMBEDDED);
		oldValue = parentAttrs.get(JRTextAttribute.IS_PDF_EMBEDDED);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_isPdfEmbedded);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(value);
			sbuffer.append(QUOTE);
		}

		value = attrs.get(TextAttribute.FOREGROUND);
		oldValue = parentAttrs.get(TextAttribute.FOREGROUND);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_forecolor);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(SHARP);
			sbuffer.append(getHexaColor((Color)value));
			sbuffer.append(QUOTE);
		}

		value = attrs.get(TextAttribute.BACKGROUND);
		oldValue = parentAttrs.get(TextAttribute.BACKGROUND);

		if (value != null && !value.equals(oldValue))
		{
			sbuffer.append(SPACE);
			sbuffer.append(ATTRIBUTE_backcolor);
			sbuffer.append(EQUAL_QUOTE);
			sbuffer.append(SHARP);
			sbuffer.append(getHexaColor((Color)value));
			sbuffer.append(QUOTE);
		}
		
		return sbuffer;
	}

	/**
	 * 
	 */
	private String getHexaColor(Color color)
	{
		String hexa = Integer.toHexString(color.getRGB() & colorMask).toUpperCase();
		return (SIX_ZEROS + hexa).substring(hexa.length());
	}

}
