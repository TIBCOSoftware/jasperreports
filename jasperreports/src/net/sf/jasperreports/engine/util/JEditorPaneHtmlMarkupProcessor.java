/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
import java.util.ListIterator;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JEditorPaneHtmlMarkupProcessor extends JEditorPaneMarkupProcessor
{
	private static final Log log = LogFactory.getLog(JEditorPaneHtmlMarkupProcessor.class);
	
	public static final String DEFAULT_BULLET_CHARACTER = "\u2022";
	public static final String DEFAULT_BULLET_SEPARATOR = ".";

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
	
	@Override
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
		
		StringBuilder text = new StringBuilder();
		List<JRStyledText.Run> styleRuns = new ArrayList<>();
		
		for(int i = 0; i < elements.size(); i++)
		{
			if (bodyOccurred && chunk != null)
			{
				text.append(chunk);
				Map<Attribute,Object> styleAttributes = getAttributes(element.getAttributes());
				if (hyperlink != null)
				{
					styleAttributes.put(JRTextAttribute.HYPERLINK, hyperlink);
					hyperlink = null;
				}
				if (!styleAttributes.isEmpty())
				{
					styleRuns.add(new JRStyledText.Run(styleAttributes, 
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
						Object type = parent.getAttributes().getAttribute(HTML.Attribute.TYPE);
						Object startObject = parent.getAttributes().getAttribute(HTML.Attribute.START);
						int start = startObject == null ? 0 : Math.max(0, Integer.valueOf(startObject.toString()) - 1);
						String suffix = "";
						
						++orderedListIndex[index];

						if(type !=null)
						{
							switch(((String)type).charAt(0))
							{
								case 'A':
									suffix = getOLBulletChars(orderedListIndex[index] + start, true);
									break;
								case 'a':
									suffix = getOLBulletChars(orderedListIndex[index] + start, false);
									break;
								case 'I':
									suffix = JRStringUtil.getRomanNumeral(orderedListIndex[index] + start, true);
									break;
								case 'i':
									suffix = JRStringUtil.getRomanNumeral(orderedListIndex[index] + start, false);
									break;
								case '1':
								default:
									suffix = String.valueOf(orderedListIndex[index] + start);
									break;
							}
						}
						else
						{
							suffix += orderedListIndex[index] + start;
						}
						chunk = whitespaces[index] + suffix + DEFAULT_BULLET_SEPARATOR + "  ";
						
					} 
					else
					{
						chunk = whitespaces[elements.indexOf(parent)] + DEFAULT_BULLET_CHARACTER + "  ";
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

		if (chunk != null)
		{
			if (!"\n".equals(chunk))
			{
				text.append(chunk);
				Map<Attribute,Object> styleAttributes = getAttributes(element.getAttributes());
				if (hyperlink != null)
				{
					styleAttributes.put(JRTextAttribute.HYPERLINK, hyperlink);
					hyperlink = null;
				}
				if (!styleAttributes.isEmpty())
				{
					styleRuns.add(new JRStyledText.Run(styleAttributes, 
							startOffset + crtOffset, endOffset + crtOffset));
				}
			}
			else
			{
				//final newline, not appending
				//check if there's any style run that would have covered it, that can happen if there's a <li> tag with style
				int length = text.length();
				for (ListIterator<JRStyledText.Run> it = styleRuns.listIterator(); it.hasNext();)
				{
					JRStyledText.Run run = it.next();
					//only looking at runs that end at the position where the newline should have been
					//we don't want to hide bugs in which runs that span after the text length are created
					if (run.endIndex == length + 1)
					{
						if (run.startIndex < run.endIndex - 1)
						{
							it.set(new JRStyledText.Run(run.attributes, run.startIndex, run.endIndex - 1));
						}
						else
						{
							it.remove();
						}
					}
				}
			}
		}
		
		JRStyledText styledText = new JRStyledText(null, text.toString());
		for (JRStyledText.Run run : styleRuns)
		{
			styledText.addRun(run);
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
	
	@Override
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
	
	/**
	 * 
	 * @param index the current index between 0 and 18277
	 * @param isUpperCase specifies whether the result should be made of upper case characters
	 * @return a character representation of the numeric index in an ordered bullet list, that contains up to 3 chars
	 */
	protected static String getOLBulletChars(int index, boolean isUpperCase)
	{
		// max 3-letter index is 18277
		if(index < 0 || index > 18277)	
		{
			throw 
				new JRRuntimeException(
					JRStringUtil.EXCEPTION_MESSAGE_KEY_NUMBER_OUTSIDE_BOUNDS,
					new Object[]{index});
		} 
		
		return JRStringUtil.getLetterNumeral(index, isUpperCase);
	}
}
