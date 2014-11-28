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
package net.sf.jasperreports.engine.util;

import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AbstractDocument.LeafElement;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument.RunElement;

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JEditorPaneHtmlMarkupProcessor extends JEditorPaneMarkupProcessor
{
	private static final Log log = LogFactory.getLog(JEditorPaneHtmlMarkupProcessor.class);

	private static JEditorPaneHtmlMarkupProcessor instance;  
	
	/**
	 * 
	 */
	public static JEditorPaneHtmlMarkupProcessor getInstance()
	{
		if (instance == null)
		{
			instance = new JEditorPaneHtmlMarkupProcessor();
		}
		return instance;
	}
	
	/**
	 * 
	 */
	public String convert(String srcText)
	{
		JEditorPane editorPane = new JEditorPane("text/html", srcText);
		editorPane.setEditable(false);

		List<Element> elements = new ArrayList<Element>();

		Document document = editorPane.getDocument();

		Element root = document.getDefaultRootElement();
		if (root != null)
		{
			addElements(elements, root);
		}

		int startOffset = 0;
		int endOffset = 0;
		int crtOffset = 0;
		String chunk = null;
		JRPrintHyperlink hyperlink = null;
		Element element = null;
		Element parent = null;
		boolean bodyOccurred = false;
		int[] orderedListIndex = new int[elements.size()];
		String whitespace = "    ";
		String[] whitespaces = new String[elements.size()];
		for(int i = 0; i < elements.size(); i++)
		{
			whitespaces[i] = "";
		}
		JRStyledText styledText = new JRStyledText();
		
		for(int i = 0; i < elements.size(); i++)
		{
			if (bodyOccurred && chunk != null)
			{
				styledText.append(chunk);
				Map<Attribute,Object> styleAttributes = getAttributes(element.getAttributes());
				if (hyperlink != null)
				{
					styleAttributes.put(JRTextAttribute.HYPERLINK, hyperlink);
					hyperlink = null;
				}
				if (!styleAttributes.isEmpty())
				{
					styledText.addRun(new JRStyledText.Run(styleAttributes, 
							startOffset + crtOffset, endOffset + crtOffset));
				}
			}

			chunk = null;
			element = elements.get(i);
			parent = element.getParentElement();
			startOffset = element.getStartOffset();
			endOffset = element.getEndOffset();
			AttributeSet attrs = element.getAttributes();

			Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
			Object object = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
			if (object instanceof HTML.Tag) 
			{
				
				HTML.Tag htmlTag = (HTML.Tag) object;
				if(htmlTag == Tag.BODY)
				{
					bodyOccurred = true;
					crtOffset = - startOffset;
				}
				else if(htmlTag == Tag.BR)
				{
					chunk = "\n";
				}
				else if(htmlTag == Tag.OL)
				{
					orderedListIndex[i] = 0;
					String parentName = parent.getName().toLowerCase();
					whitespaces[i] = whitespaces[elements.indexOf(parent)] + whitespace;
					if(parentName.equals("li"))
					{
						chunk = "";
					}
					else
					{
						chunk = "\n";
						++crtOffset;
					}
				}
				else if(htmlTag == Tag.UL)
				{
					whitespaces[i] = whitespaces[elements.indexOf(parent)] + whitespace;

					String parentName = parent.getName().toLowerCase();
					if(parentName.equals("li"))
					{
						chunk = "";
					}
					else
					{
						chunk = "\n";
						++crtOffset;
					}
					
				}
				else if(htmlTag == Tag.LI)
				{
					
					whitespaces[i] = whitespaces[elements.indexOf(parent)];
					if(element.getElement(0) != null && 
							(element.getElement(0).getName().toLowerCase().equals("ol") || element.getElement(0).getName().toLowerCase().equals("ul"))
							)
					{
						chunk = "";
					}
					else if(parent.getName().equals("ol"))
					{
						int index = elements.indexOf(parent);
						chunk = whitespaces[index] + String.valueOf(++orderedListIndex[index]) + ".  ";
					} 
					else
					{
						chunk = whitespaces[elements.indexOf(parent)] + "\u2022  ";
					}
					crtOffset += chunk.length();
				}
				else if (element instanceof LeafElement)
				{
					if (element instanceof RunElement)
					{
						RunElement runElement = (RunElement)element;
						AttributeSet attrSet = (AttributeSet)runElement.getAttribute(Tag.A);
						if (attrSet != null)
						{
							hyperlink = new JRBasePrintHyperlink();
							hyperlink.setHyperlinkType(HyperlinkTypeEnum.REFERENCE);
							hyperlink.setHyperlinkReference((String)attrSet.getAttribute(HTML.Attribute.HREF));
							hyperlink.setLinkTarget((String)attrSet.getAttribute(HTML.Attribute.TARGET));
						}
					}
					try
					{
						chunk = document.getText(startOffset, endOffset - startOffset);
					}
					catch(BadLocationException e)
					{
						if (log.isDebugEnabled())
						{
							log.debug("Error converting markup.", e);
						}
					}
				}
			}
		}

		if (chunk != null && !"\n".equals(chunk))
		{
			styledText.append(chunk);
			Map<Attribute,Object> styleAttributes = getAttributes(element.getAttributes());
			if (hyperlink != null)
			{
				styleAttributes.put(JRTextAttribute.HYPERLINK, hyperlink);
				hyperlink = null;
			}
			if (!styleAttributes.isEmpty())
			{
				styledText.addRun(new JRStyledText.Run(styleAttributes, 
						startOffset + crtOffset, endOffset + crtOffset));
			}
		}
		
		styledText.setGlobalAttributes(new HashMap<Attribute,Object>());
		
		return JRStyledTextParser.getInstance().write(styledText);
	}
	
	/**
	 * 
	 */
	protected void addElements(List<Element> elements, Element element) 
	{
		//if(element instanceof LeafElement)
		{
			elements.add(element);
		}
		for(int i = 0; i < element.getElementCount(); i++)
		{
			Element child = element.getElement(i);
			addElements(elements, child);
		}
	}
	
	/**
	 * 
	 */
	protected Map<Attribute,Object> getAttributes(AttributeSet attrSet) 
	{
		Map<Attribute,Object> attrMap = new HashMap<Attribute,Object>();
		if (attrSet.isDefined(StyleConstants.FontFamily))
		{
			attrMap.put(
				TextAttribute.FAMILY,
				StyleConstants.getFontFamily(attrSet)
				);
		}
					
		if (attrSet.isDefined(StyleConstants.Bold))
		{
			attrMap.put(
				TextAttribute.WEIGHT,
				StyleConstants.isBold(attrSet) ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR
				);
		}
					
		if (attrSet.isDefined(StyleConstants.Italic))
		{
			attrMap.put(
				TextAttribute.POSTURE,
				StyleConstants.isItalic(attrSet) ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR
				);
		}
					
		if (attrSet.isDefined(StyleConstants.Underline))
		{
			attrMap.put(
				TextAttribute.UNDERLINE,
				StyleConstants.isUnderline(attrSet) ? TextAttribute.UNDERLINE_ON : null
				);
		}
					
		if (attrSet.isDefined(StyleConstants.StrikeThrough))
		{
			attrMap.put(
				TextAttribute.STRIKETHROUGH,
				StyleConstants.isStrikeThrough(attrSet) ? TextAttribute.STRIKETHROUGH_ON : null
				);
		}
					
		if (attrSet.isDefined(StyleConstants.FontSize))
		{
			attrMap.put(
				TextAttribute.SIZE,
				new Float(StyleConstants.getFontSize(attrSet))
				);
		}
					
		if (attrSet.isDefined(StyleConstants.Foreground))
		{
			attrMap.put(
				TextAttribute.FOREGROUND,
				StyleConstants.getForeground(attrSet)
				);
		}
					
		if (attrSet.isDefined(StyleConstants.Background))
		{
			attrMap.put(
				TextAttribute.BACKGROUND,
				StyleConstants.getBackground(attrSet)
				);
		}
		
		//FIXME: why StyleConstants.isSuperscript(attrSet) does return false
		if (attrSet.isDefined(StyleConstants.Superscript) && !StyleConstants.isSubscript(attrSet))
		{
			attrMap.put(
				TextAttribute.SUPERSCRIPT,
				TextAttribute.SUPERSCRIPT_SUPER
				);
		}
					
		if (attrSet.isDefined(StyleConstants.Subscript) && StyleConstants.isSubscript(attrSet))
		{
			attrMap.put(
				TextAttribute.SUPERSCRIPT,
				TextAttribute.SUPERSCRIPT_SUB
				);
		}
					
		return attrMap;
	}
	
}
