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
package net.sf.jasperreports.engine.util;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.SoftReference;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

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
 * @version $Id$
 */
public class JRStyledTextParser implements ErrorHandler
{
    //don't use log because it will be required in applet jar
	//private static final Log log = LogFactory.getLog(JRStyledTextParser.class);

	/**
	 *
	 */
	private static final String ROOT_START = "<st>";
	private static final String ROOT_END = "</st>";
	private static final String NODE_style = "style";
	private static final String NODE_bold = "b";
	private static final String NODE_italic = "i";
	private static final String NODE_underline = "u";
	private static final String NODE_sup = "sup";
	private static final String NODE_sub = "sub";
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
	
	/**
	 * Thread local soft cache of instances.
	 */
	private static final ThreadLocal threadInstances = new ThreadLocal();
	
	/**
	 * Return a cached instance.
	 * 
	 * @return a cached instance
	 */
	public static JRStyledTextParser getInstance()
	{
		JRStyledTextParser instance = null;
		SoftReference instanceRef = (SoftReference) threadInstances.get();
		if (instanceRef != null)
		{
			instance = (JRStyledTextParser) instanceRef.get();
		}
		if (instance == null)
		{
			instance = new JRStyledTextParser();
			threadInstances.set(new SoftReference(instance));
		}
		return instance;
	}
	

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
		
		styledText.setGlobalAttributes(attributes);
		
		return styledText;
	}

	/**
	 * Creates a styled text object by either parsing a styled text String or
	 * by wrapping an unstyled String.
	 * 
	 * @param parentAttributes the element-level styled text attributes
	 * @param text the (either styled or unstyled) text
	 * @param isStyledText flag indicating that the text is styled
	 * @return a styled text object
	 */
	public JRStyledText getStyledText(Map parentAttributes, String text, boolean isStyledText)
	{
		JRStyledText styledText = null;
		if (isStyledText)
		{
			try
			{
				styledText = parse(parentAttributes, text);
			}
			catch (SAXException e)
			{
				//ignore if invalid styled text and treat like normal text
			}
		}
	
		if (styledText == null)
		{
			styledText = new JRStyledText();
			styledText.append(text);
			styledText.setGlobalAttributes(parentAttributes);
		}
		
		return styledText;
	}
	
	/**
	 * Outputs a styled text String given a styled text instance.
	 * 
	 * @param styledText the styled text object
	 * @return the String styled text representation
	 */
	public String write(JRStyledText styledText)
	{
		return write(styledText.getGlobalAttributes(), 
				styledText.getAttributedString().getIterator(), 
				styledText.getText());
	}
	
	/**
	 * Outputs a styled text String given a set of element-level styled text
	 * attributes and a styled text in the form of a String text and an iterator
	 * of style attributes.
	 * 
	 * @param parentAttrs the element-level styled text attributes
	 * @param iterator iterator of styled text attributes
	 * @param text the text
	 * @return the String styled text representation
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
				writeChunk(sbuffer, parentAttrs, attrs, chunk);
				sbuffer.append(LESS_SLASH);
				sbuffer.append(NODE_style);
				sbuffer.append(GREATER);
			}
			else
			{
				writeChunk(sbuffer, parentAttrs, attrs, chunk);
			}

			iterator.setIndex(runLimit);
		}
		
		return sbuffer.toString();
	}

	/**
	 * Outputs the String representation of a styled text chunk.
	 * 
	 * @param styledText the styled text
	 * @param startIndex the start index
	 * @param endIndex the end index
	 * @return the String styled text representation of the chunk delimited by
	 * the start index and the end index
	 * @see #write(Map, AttributedCharacterIterator, String)
	 */
	public String write(JRStyledText styledText, 
			int startIndex, int endIndex)
	{
		AttributedCharacterIterator subIterator = new AttributedString(
				styledText.getAttributedString().getIterator(), 
				startIndex, endIndex).getIterator();
		String subText = styledText.getText().substring(startIndex, endIndex);
		return write(styledText.getGlobalAttributes(), subIterator, subText);
	}

	/**
	 *
	 */
	public void writeChunk(StringBuffer sbuffer, Map parentAttrs, Map attrs, String chunk)
	{
		Object value = attrs.get(TextAttribute.SUPERSCRIPT);
		Object oldValue = parentAttrs.get(TextAttribute.SUPERSCRIPT);

		boolean isSuper = false;
		boolean isSub = false;
		
		if (value != null && !value.equals(oldValue))
		{
			isSuper=TextAttribute.SUPERSCRIPT_SUPER.equals(value);
			isSub=TextAttribute.SUPERSCRIPT_SUB.equals(value);
		}


		if (isSuper || isSub)
		{
			String node = isSuper?NODE_sup:NODE_sub;
			sbuffer.append(LESS);
			sbuffer.append(node);
			sbuffer.append(GREATER);
			sbuffer.append(JRStringUtil.xmlEncode(chunk));
			sbuffer.append(LESS_SLASH);
			sbuffer.append(node);
			sbuffer.append(GREATER);
		}
		else
		{
			sbuffer.append(JRStringUtil.xmlEncode(chunk));
		}
	}

	/**
	 *
	 */
	private void parseStyle(JRStyledText styledText, Node parentNode) throws SAXException
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
					Color color = 
						JRXmlConstants.getColor(
							nodeAttrs.getNamedItem(ATTRIBUTE_forecolor).getNodeValue(),
							Color.black
							);
					styleAttrs.put(
						TextAttribute.FOREGROUND,
						color
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_backcolor) != null)
				{
					Color color = 
						JRXmlConstants.getColor(
							nodeAttrs.getNamedItem(ATTRIBUTE_backcolor).getNodeValue(),
							Color.black
							);
					styleAttrs.put(
						TextAttribute.BACKGROUND,
						color
						);
				}

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_bold.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_italic.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_underline.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_sup.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_sub.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_font.equalsIgnoreCase(node.getNodeName()))
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
					Color color = 
						JRXmlConstants.getColor(
							nodeAttrs.getNamedItem(ATTRIBUTE_color).getNodeValue(),
							Color.black
							);
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
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_br.equalsIgnoreCase(node.getNodeName()))
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
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_li.equalsIgnoreCase(node.getNodeName()))
			{
				String tmpText = styledText.getText();
				if(tmpText.length() > 0 && !tmpText.endsWith("\n"))
				{
					styledText.append("\n");
				}
				styledText.append(" \u2022 ");

				int startIndex = styledText.length();
				resizeRuns(styledText.getRuns(), startIndex, 1);
				parseStyle(styledText, node);
				styledText.addRun(new JRStyledText.Run(new HashMap(), startIndex, styledText.length()));
				
				// if the text in the next node does not start with a '\n', or 
				// if the next node is not a <li /> one, we have to append a new line
				Node nextNode = node.getNextSibling();
				String textContent = getFirstTextOccurence(nextNode);
				if(nextNode != null && 
						!((nextNode.getNodeType() == Node.ELEMENT_NODE &&
								NODE_li.equalsIgnoreCase(nextNode.getNodeName()) ||
						(textContent != null && textContent.startsWith("\n")))
						))
				{
					styledText.append("\n");
					resizeRuns(styledText.getRuns(), startIndex, 1);
				}
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				String nodeName = "<" + node.getNodeName() + ">";
				throw new SAXException("Tag " + nodeName + " is not a valid styled text tag.");
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
			sbuffer.append(JRColorUtil.getColorHexa((Color)value));
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
			sbuffer.append(JRColorUtil.getColorHexa((Color)value));
			sbuffer.append(QUOTE);
		}
		
		return sbuffer;
	}
	
	/**
	 * The method returns the first text occurence in a given node element
	 * @param node
	 * @return String
	 */
	private String getFirstTextOccurence(Node node){
		if(node != null)
		{
			if(node.getNodeValue() != null)
				return node.getNodeValue();
			NodeList nodeList = node.getChildNodes();
			for (int i=0; i< nodeList.getLength(); i++)
			{
				String firstOccurence = getFirstTextOccurence(nodeList.item(i));
				if(firstOccurence != null)
					return firstOccurence;
			}
		}
		return null;
	}

    public void error(SAXParseException e) {
//    	if(log.isErrorEnabled())
//    		log.error("Error parsing styled text.", e);
    }

    public void fatalError(SAXParseException e) {
//    	if(log.isFatalEnabled())
//    		log.fatal("Error parsing styled text.", e);
    }

    public void warning(SAXParseException e) {
//    	if(log.isWarnEnabled())
//    		log.warn("Error parsing styled text.", e);
    }

}
