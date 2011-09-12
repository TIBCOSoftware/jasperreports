/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.AbstractDocument.LeafElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JEditorPaneRtfMarkupProcessor extends JEditorPaneMarkupProcessor
{
	private static final Log log = LogFactory.getLog(JEditorPaneRtfMarkupProcessor.class);

	private static JEditorPaneRtfMarkupProcessor instance;  
	
	/**
	 * 
	 */
	public static JEditorPaneRtfMarkupProcessor getInstance()
	{
		if (instance == null)
		{
			instance = new JEditorPaneRtfMarkupProcessor();
		}
		return instance;
	}
	
	/**
	 * 
	 */
	public String convert(String srcText)
	{
		JEditorPane editorPane = new JEditorPane("text/rtf", srcText);
		editorPane.setEditable(false);

		List<Element> elements = new ArrayList<Element>();

		Document document = editorPane.getDocument();

		Element root = document.getDefaultRootElement();
		if (root != null)
		{
			addElements(elements, root);
		}

		String chunk = null;
		Element element = null;
		int startOffset = 0;
		int endOffset = 0;
		
		JRStyledText styledText = new JRStyledText();
		styledText.setGlobalAttributes(new HashMap<Attribute,Object>());
		for(int i = 0; i < elements.size(); i++)
		{
			if (chunk != null)
			{
				styledText.append(chunk);
				styledText.addRun(new JRStyledText.Run(getAttributes(element.getAttributes()), startOffset, endOffset));
			}

			chunk = null;
			element = elements.get(i);
			startOffset = element.getStartOffset();
			endOffset = element.getEndOffset();

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

		if (chunk != null && !"\n".equals(chunk))
		{
			styledText.append(chunk);
			styledText.addRun(new JRStyledText.Run(getAttributes(element.getAttributes()), startOffset, endOffset));
		}

		return JRStyledTextParser.getInstance().write(styledText);
	}
	
	/**
	 * 
	 */
	protected void addElements(List<Element> elements, Element element) 
	{
		if(element instanceof LeafElement)
		{
			elements.add(element);
		}
		for(int i = 0; i < element.getElementCount(); i++)
		{
			Element child = element.getElement(i);
			addElements(elements, child);
		}
	}
	
}
