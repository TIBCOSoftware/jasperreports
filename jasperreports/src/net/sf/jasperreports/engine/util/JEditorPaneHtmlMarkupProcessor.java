/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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

import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.util.JRStyledText.Run;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JEditorPaneHtmlMarkupProcessor extends JEditorPaneMarkupProcessor
{
	private static final Log log = LogFactory.getLog(JEditorPaneHtmlMarkupProcessor.class);
	
	private Document document;
	private boolean bodyOccurred = false;
	private boolean honourBlockElementNewLine = false;
	private Stack<HTML.Tag> htmlTagStack;

	private Stack<StyledTextListInfo> htmlListStack;
	private boolean insideLi;
	private boolean liStart;
	private StyledTextListInfo justClosedList;
	
	/**
	 * 
	 */
	public static JEditorPaneHtmlMarkupProcessor getInstance()
	{
		return new JEditorPaneHtmlMarkupProcessor();
	}
	
	@Override
	public String convert(String srcText)
	{
		if (srcText.indexOf('<') >= 0 || srcText.indexOf('&') >= 0)
		{
			JRStyledText styledText = new JRStyledText();
			
			htmlTagStack = new Stack<>();
			htmlListStack = new Stack<>();

			JEditorPane editorPane = new JEditorPane("text/html", srcText);
			editorPane.setEditable(false);

			document = editorPane.getDocument();
			bodyOccurred = false;

			Element root = document.getDefaultRootElement();
			if (root != null)
			{
				processElement(styledText, root);
			}

			styledText.setGlobalAttributes(new HashMap<>());
			
			return JRStyledTextParser.getInstance().write(styledText);
		}
		
		return srcText;
	}
	
	
	private void processElement(JRStyledText styledText, Element parentElement)
	{
		for (int i = 0; i < parentElement.getElementCount(); i++)
		{
			Element element = parentElement.getElement(i);

			AttributeSet attrs = element.getAttributes();

			Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
			Object object = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
			HTML.Tag htmlTag = object instanceof HTML.Tag ? (HTML.Tag) object : null;
			
			if (
				bodyOccurred 
				&& honourBlockElementNewLine 
				&& htmlTag != Tag.UL
				&& htmlTag != Tag.OL
				&& htmlTag != Tag.LI
				)
			{
				// since the newline introduced by a block element is always at the end of the block element,
				// it means the block element could not have any nested child element after this newline,
				// so it it is followed by either a sibling or a sibling of its parent or ancestor;
				// it is before the processing of this next element that we honour the block element newline
				// character, but only if the next element is not a list or list item element;
				// this is because the text measurer and text renderer already treat list and list item styled
				// text runs as paragraphs, so they start them on new lines;
				// while the honourBlockElementNewLine flag takes care of skipping newlines introduced by the html
				// parser at the end of the list or list item block elements, here we need to test again if the
				// newline should be honoured because it might come from non list elements such as <p> or <div>
				// and should be ignore if immediately follow by the current list or list item element
				styledText.append("\n");
				resizeRuns(styledText.getRuns(), styledText.length(), 1);
			}

			honourBlockElementNewLine = false;
			
			if (htmlTag != null) 
			{
				htmlTagStack.push(htmlTag);
				
				if (htmlTag == Tag.BODY)
				{
					bodyOccurred = true;
					processElement(styledText, element);
				}
				else if (htmlTag == Tag.BR)
				{
					styledText.append("\n");

					int startIndex = styledText.length();
					resizeRuns(styledText.getRuns(), startIndex, 1);

					processElement(styledText, element);
					styledText.addRun(new JRStyledText.Run(new HashMap<>(), startIndex, styledText.length()));

					// a second newline is added in case the <br> tag was not empty, as it is usually used 
					if (startIndex < styledText.length()) {
						styledText.append("\n");
						resizeRuns(styledText.getRuns(), startIndex, 1);
					}
				}
				else if (htmlTag == Tag.OL || htmlTag == Tag.UL)
				{
					Object type = attrs.getAttribute(HTML.Attribute.TYPE);
					Object start = attrs.getAttribute(HTML.Attribute.START);

					StyledTextListInfo htmlList = 
						new StyledTextListInfo(
							htmlTag == Tag.OL,
							htmlTag == Tag.OL && type != null ? String.valueOf(type) : null,
							htmlTag == Tag.OL && start != null ? Integer.valueOf(start.toString()) : null,
							insideLi
							);
					
					htmlList.setAtLiStart(liStart);

					htmlListStack.push(htmlList);
					
					insideLi = false;
					
					Map<Attribute,Object> styleAttrs = new HashMap<>();

					styleAttrs.put(JRTextAttribute.HTML_LIST, htmlListStack.toArray(new StyledTextListInfo[htmlListStack.size()]));
					styleAttrs.put(JRTextAttribute.HTML_LIST_ITEM, StyledTextListItemInfo.NO_LIST_ITEM_FILLER);
					
					int startIndex = styledText.length();

					processElement(styledText, element);

					styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
					
					justClosedList = htmlListStack.pop();
				}
				else if (htmlTag == Tag.LI)
				{
					Map<Attribute,Object> styleAttrs = new HashMap<>();

					StyledTextListInfo htmlList = null;
					
					boolean ulAdded = false;
					if (htmlListStack.size() == 0)
					{
						htmlList = new StyledTextListInfo(false, null, null, false);
						htmlListStack.push(htmlList);
						styleAttrs.put(JRTextAttribute.HTML_LIST, htmlListStack.toArray(new StyledTextListInfo[htmlListStack.size()]));
						styleAttrs.put(JRTextAttribute.HTML_LIST_ITEM, StyledTextListItemInfo.NO_LIST_ITEM_FILLER);
						ulAdded = true;
					}
					else
					{
						htmlList = htmlListStack.peek();
					}
					htmlList.setItemCount(htmlList.getItemCount() + 1);
					insideLi = true;
					liStart = true;
					justClosedList = null;
					
					styleAttrs.put(JRTextAttribute.HTML_LIST_ITEM, new StyledTextListItemInfo(htmlList.getItemCount() - 1));
					
					int startIndex = styledText.length();

					processElement(styledText, element);

					styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
					
					insideLi = false;
					liStart = false;
					if (justClosedList != null)
					{
						justClosedList.setAtLiEnd(true);
					}

					if (ulAdded)
					{
						htmlListStack.pop();
					}
				}
				else if (element instanceof LeafElement)
				{
					String chunk = null;
					try
					{
						chunk = document.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
					}
					catch (BadLocationException e)
					{
						if (log.isDebugEnabled())
						{
							log.debug("Error converting markup.", e);
						}
					}
					
					honourBlockElementNewLine = false;
					
					if ("\n".equals(chunk)) 
					{
						// chunk is a newline char only when the parser processes the end of a block element;
						// inline elements do not have newline chars at the end and the html parser always ignores
						// newline chars in text content

						honourBlockElementNewLine = true;

						HTML.Tag grandParentHtmlTag = htmlTagStack.size() >= 3 ? htmlTagStack.get(htmlTagStack.size() - 3) : null;
						if (
							grandParentHtmlTag == Tag.UL
							|| grandParentHtmlTag == Tag.OL
							|| grandParentHtmlTag == Tag.LI
							)
						{
							// ignoring newline characters introduced by the html parser at the end of list or list item elements,
							// because they are the only ones which cannot be skipped by the text measurer and text renderer;
							// this is because the text measurer and the text renderer are responsible for skipping newline
							// characters that are actually in the text content at the end of the <ul>, <ol> and <li> tags in styled text markup;
							// if two newline characters would be found at the end of the <ul>, <ol> or <li> tag, with the second one being
							// introduced by the html parser, then only one of them would get skipped during text measuring and rendering,
							// which would not match html browser behavior, that is able to ignore <br> tags placed at the end of <ul>, <ol> and <li> tags
							honourBlockElementNewLine = false;
						}
					}
					
					if (
						chunk != null
						&& !"\n".equals(chunk) 
						)
					{
						liStart = false;
						justClosedList = null;

						int startIndex = styledText.length();

						styledText.append(chunk);
						
						Map<Attribute,Object> styleAttributes = getAttributes(element.getAttributes());

						if (element instanceof RunElement)
						{
							RunElement runElement = (RunElement)element;
							AttributeSet attrSet = (AttributeSet)runElement.getAttribute(Tag.A);
							if (attrSet != null)
							{
								JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
								hyperlink.setHyperlinkType(HyperlinkTypeEnum.REFERENCE);
								hyperlink.setHyperlinkReference((String)attrSet.getAttribute(HTML.Attribute.HREF));
								hyperlink.setLinkTarget((String)attrSet.getAttribute(HTML.Attribute.TARGET));
								styleAttributes.put(JRTextAttribute.HYPERLINK, hyperlink);
							}
						}

						styledText.addRun(
							new JRStyledText.Run(styleAttributes, startIndex, styledText.length())
							);
					}
				}
				else
				{
					if (bodyOccurred)
					{
						processElement(styledText, element);
					}
				}
				
				htmlTagStack.pop();
			}
		}
	}
	
	/**
	 *
	 */
	private void resizeRuns(List<Run> runs, int startIndex, int count)
	{
		for (int j = 0; j < runs.size(); j++)
		{
			JRStyledText.Run run = runs.get(j);
			if (run.startIndex <= startIndex && run.endIndex > startIndex - count)
			{
				run.endIndex += count;
			}
		}
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
