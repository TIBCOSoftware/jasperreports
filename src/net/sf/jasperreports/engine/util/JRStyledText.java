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

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyledText
{

	private static final String PROPERTY_AWT_SUPERSCRIPT_FIX_ENABLED = JRProperties.PROPERTY_PREFIX + "awt.superscript.fix.enabled";
	private static final boolean AWT_SUPERSCRIPT_FIX_ENABLED = 
		System.getProperty("java.version").startsWith("1.6") 
		&& JRProperties.getBooleanProperty(PROPERTY_AWT_SUPERSCRIPT_FIX_ENABLED);
	
	private static final Set FONT_ATTRS = new HashSet();
	static
	{
		FONT_ATTRS.add(TextAttribute.FAMILY);
		FONT_ATTRS.add(TextAttribute.WEIGHT);
		FONT_ATTRS.add(TextAttribute.POSTURE);
		FONT_ATTRS.add(TextAttribute.SIZE);
		FONT_ATTRS.add(TextAttribute.SUPERSCRIPT);
	}
	
	/**
	 *
	 */
	private StringBuffer sbuffer = new StringBuffer();
	private List runs = new ArrayList();
	private AttributedString attributedString = null;
	private AttributedString awtAttributedString = null;
	private Map globalAttributes;
	private Locale locale;

	
	/**
	 *
	 */
	public JRStyledText()
	{
	}


	/**
	 *
	 */
	public void append(String text)
	{
		sbuffer.append(text);
		attributedString = null;
		awtAttributedString = null;
	}

	/**
	 *
	 */
	public void addRun(Run run)
	{
		runs.add(run);
		attributedString = null;
		awtAttributedString = null;
	}

	/**
	 *
	 */
	public int length()
	{
		return sbuffer.length();
	}

	/**
	 *
	 */
	public String getText()
	{
		return sbuffer.toString();
	}

	/**
	 *
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 *
	 */
	public AttributedString getAttributedString()
	{
		if (attributedString == null)
		{
			attributedString = new AttributedString(sbuffer.toString());

			for(int i = runs.size() - 1; i >= 0; i--)
			{
				Run run = (Run)runs.get(i);
				if (run.startIndex != run.endIndex && run.attributes != null)
				{
					attributedString.addAttributes(run.attributes, run.startIndex, run.endIndex);
				}
			}
		}
		
		return attributedString;
	}

	/**
	 * Returns an attributed string that only contains standard Java text
	 * attributes; JasperReports specific text attributes of the styled text
	 * are ignored.
	 * 
	 * @return an attributed string that only contains standard Java text
	 * attributes
	 */
	public AttributedString getAwtAttributedString()
	{
		if (awtAttributedString == null)
		{
			awtAttributedString = new AttributedString(sbuffer.toString());

			for(int i = runs.size() - 1; i >= 0; i--)
			{
				Run run = (Run)runs.get(i);
				if (
					run.startIndex != run.endIndex 
					&& run.attributes != null 
					&& !run.attributes.isEmpty()
					)
				{
					for (Iterator it = run.attributes.entrySet().iterator(); it.hasNext();)
					{
						Map.Entry entry = (Map.Entry) it.next();
						AttributedCharacterIterator.Attribute attribute = 
							(AttributedCharacterIterator.Attribute) entry.getKey();
						if (!(attribute instanceof JRTextAttribute))
						{
							Object value = entry.getValue();
							awtAttributedString.addAttribute(attribute, value, run.startIndex, run.endIndex);
						}
					}
				}
			}
			
			AttributedCharacterIterator iterator = awtAttributedString.getIterator();
			
			int runLimit = 0;
			AffineTransform atrans = null;

			while(runLimit < iterator.getEndIndex() && (runLimit = iterator.getRunLimit(FONT_ATTRS)) <= iterator.getEndIndex())
			{
				Map attrs = iterator.getAttributes();
					
				Font awtFont = 
					JRFontUtil.getAwtFontFromBundles(
						(String)attrs.get(TextAttribute.FAMILY), 
						((TextAttribute.WEIGHT_BOLD.equals(attrs.get(TextAttribute.WEIGHT))?Font.BOLD:Font.PLAIN)
							|(TextAttribute.POSTURE_OBLIQUE.equals(attrs.get(TextAttribute.POSTURE))?Font.ITALIC:Font.PLAIN)), 
						((Float)attrs.get(TextAttribute.SIZE)).intValue(),
						locale
						);
				if (awtFont != null)
				{
					if (AWT_SUPERSCRIPT_FIX_ENABLED && atrans != null)
					{
						double y = atrans.getTranslateY();
						atrans = new AffineTransform();
						atrans.translate(0, - y);
						awtFont = awtFont.deriveFont(atrans);
						atrans = null;
					}
					Integer superscript = (Integer)attrs.get(TextAttribute.SUPERSCRIPT);
					if (TextAttribute.SUPERSCRIPT_SUPER.equals(superscript))
					{
						atrans = new AffineTransform();
						atrans.scale(2 / 3d, 2 / 3d);
						atrans.translate(0, - awtFont.getSize() / 2f);
						awtFont = awtFont.deriveFont(atrans);
					}
					else if (TextAttribute.SUPERSCRIPT_SUB.equals(superscript))
					{
						atrans = new AffineTransform();
						atrans.scale(2 / 3d, 2 / 3d);
						atrans.translate(0, awtFont.getSize() / 2f);
						awtFont = awtFont.deriveFont(atrans);
					}
					awtAttributedString.addAttribute(TextAttribute.FONT, awtFont, iterator.getIndex(), runLimit);
				}
				
				iterator.setIndex(runLimit);
			}

		}
		
		return awtAttributedString;
	}


	/**
	 *
	 */
	public List getRuns()
	{
		return runs;
	}

	/**
	 * 
	 */
	public static class Run 
	{
		/**
		 *
		 */
		public Map attributes = null;
		public int startIndex = 0;
		public int endIndex = 0;

		/**
		 *
		 */
		public Run(Map attributes, int startIndex, int endIndex) 
		{
			this.attributes = attributes;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}
	}

	public void setGlobalAttributes(Map attributes)
	{
		this.globalAttributes = attributes;
		addRun(new Run(attributes, 0, length()));
	}
	
	
	public Map getGlobalAttributes()
	{
		return globalAttributes;
	}
}
