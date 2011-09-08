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

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyledText implements Cloneable
{

	/**
	 * 
	 */
	public static final String PROPERTY_AWT_IGNORE_MISSING_FONT = JRProperties.PROPERTY_PREFIX + "awt.ignore.missing.font";
	
	private static final String PROPERTY_AWT_SUPERSCRIPT_FIX_ENABLED = JRProperties.PROPERTY_PREFIX + "awt.superscript.fix.enabled";
	private static final boolean AWT_SUPERSCRIPT_FIX_ENABLED = 
		System.getProperty("java.version").startsWith("1.6") 
		&& JRProperties.getBooleanProperty(PROPERTY_AWT_SUPERSCRIPT_FIX_ENABLED);
	
	private static final Set<Attribute> FONT_ATTRS = new HashSet<Attribute>();
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
	private List<Run> runs = new ArrayList<Run>();
	private AttributedString attributedString;
	private AttributedString awtAttributedString;
	private Map<Attribute,Object> globalAttributes;
	private Locale locale;

	
	/**
	 *
	 */
	public JRStyledText()
	{
		this(null);
	}


	/**
	 *
	 */
	public JRStyledText(Locale locale)
	{
		this.locale = locale;
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
				Run run = runs.get(i);
				if (run.startIndex != run.endIndex && run.attributes != null)
				{
					attributedString.addAttributes(run.attributes, run.startIndex, run.endIndex);
				}
			}
		}
		
		return attributedString;
	}

	/**
	 * Returns an attributed string that contains the AWT font attribute, as the font is actually loaded.
	 */
	public AttributedString getAwtAttributedString(boolean ignoreMissingFont)
	{
		if (awtAttributedString == null)
		{
			awtAttributedString = new AttributedString(sbuffer.toString());

			for(int i = runs.size() - 1; i >= 0; i--)
			{
				Run run = runs.get(i);
				if (run.startIndex != run.endIndex && run.attributes != null)
				{
					awtAttributedString.addAttributes(run.attributes, run.startIndex, run.endIndex);
				}
//				if (
//					run.startIndex != run.endIndex 
//					&& run.attributes != null 
//					&& !run.attributes.isEmpty()
//					)
//				{
//					for (Iterator it = run.attributes.entrySet().iterator(); it.hasNext();)
//					{
//						Map.Entry entry = (Map.Entry) it.next();
//						AttributedCharacterIterator.Attribute attribute = 
//							(AttributedCharacterIterator.Attribute) entry.getKey();
//						if (!(attribute instanceof JRTextAttribute))
//						{
//							Object value = entry.getValue();
//							awtAttributedString.addAttribute(attribute, value, run.startIndex, run.endIndex);
//						}
//					}
//				}
			}
			
			AttributedCharacterIterator iterator = awtAttributedString.getIterator();
			
			int runLimit = 0;
			AffineTransform atrans = null;

			while(runLimit < iterator.getEndIndex() && (runLimit = iterator.getRunLimit(FONT_ATTRS)) <= iterator.getEndIndex())
			{
				Map<Attribute,Object> attrs = iterator.getAttributes();
					
				String familyName = (String)attrs.get(TextAttribute.FAMILY); 
				
				Font awtFont = 
					JRFontUtil.getAwtFontFromBundles(
						familyName, 
						((TextAttribute.WEIGHT_BOLD.equals(attrs.get(TextAttribute.WEIGHT))?Font.BOLD:Font.PLAIN)
							|(TextAttribute.POSTURE_OBLIQUE.equals(attrs.get(TextAttribute.POSTURE))?Font.ITALIC:Font.PLAIN)), 
						((Float)attrs.get(TextAttribute.SIZE)).intValue(),
						locale,
						ignoreMissingFont
						);
				if (awtFont == null)
				{
					// The font was not found in any of the font extensions, so it is expected that the TextAttribute.FAMILY attribute
					// will be used by AWT. In that case, we want make sure the font family name is available to the JVM.
					JRFontUtil.checkAwtFont(familyName, ignoreMissingFont);
				}
				else
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
	public List<Run> getRuns()
	{
		return runs;
	}

	/**
	 * 
	 */
	public static class Run implements Cloneable
	{
		/**
		 *
		 */
		public Map<Attribute,Object> attributes;
		public int startIndex;
		public int endIndex;

		/**
		 *
		 */
		public Run(Map<Attribute,Object> attributes, int startIndex, int endIndex) 
		{
			this.attributes = attributes;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		protected Object clone()
		{
			return cloneRun();
		}
		
		/**
		 * Clones this object.
		 * 
		 * @return a clone of this object
		 */
		public Run cloneRun()
		{
			try
			{
				Run clone = (Run) super.clone();
				clone.attributes = cloneAttributesMap(attributes);
				return clone;
			}
			catch (CloneNotSupportedException e)
			{
				// never
				throw new JRRuntimeException(e);
			}
		}
	}

	public void setGlobalAttributes(Map<Attribute,Object> attributes)
	{
		this.globalAttributes = attributes;
		addRun(new Run(attributes, 0, length()));
	}
	
	
	public Map<Attribute,Object> getGlobalAttributes()
	{
		return globalAttributes;
	}
	
	protected Object clone() throws CloneNotSupportedException
	{
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	protected static Map<Attribute,Object> cloneAttributesMap(Map<Attribute,Object> attributes)
	{
		return attributes == null ? null : new HashMap<Attribute,Object>(attributes);
	}

	
	/**
	 * Clones this object.
	 * 
	 * @return a clone of this object
	 */
	public JRStyledText cloneText()
	{
		try
		{
			JRStyledText clone = (JRStyledText) super.clone();
			clone.globalAttributes = cloneAttributesMap(globalAttributes);
			
			clone.runs = new ArrayList<Run>(runs.size());
			for (Iterator<Run> it = runs.iterator(); it.hasNext();)
			{
				Run run = it.next();
				Run runClone = run.cloneRun();
				clone.runs.add(runClone);
			}
			
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
	
	/**
	 * Inserts a string at specified positions in the styled text.
	 * 
	 * <p>
	 * The string is inserted in the style runs located at the insertion
	 * positions.  If a style run finished right before the insertion position,
	 * the string will be part of this run (but not of the runs that start
	 * right after the insertion position).
	 * 
	 * @param str the string to insert
	 * @param offsets the incremental offsets of the positions at which to
	 * insert the string
	 */
	public void insert(String str, short[] offsets)
	{
		int insertLength = str.length();
		
		//new buffer to do the insertion
		StringBuffer newText = new StringBuffer(sbuffer.length() + insertLength * offsets.length); //NOPMD
		char[] buffer = null;
		int offset = 0;
		for (int i = 0; i < offsets.length; i++)
		{
			int charCount = offsets[i];
			int prevOffset = offset;
			offset += offsets[i];
			
			//append chunk of text
			if (buffer == null || buffer.length < charCount)
			{
				buffer = new char[charCount];
			}
			sbuffer.getChars(prevOffset, offset, buffer, 0);
			newText.append(buffer, 0, charCount);
			
			//append inserted text
			newText.append(str);
			
			//adjust runs
			//TODO optimize this?
			for (Iterator<Run> it = runs.iterator(); it.hasNext();)
			{
				Run run = it.next();
				if (run.startIndex >= offset)
				{
					//inserted before run
					run.startIndex += insertLength;
					run.endIndex += insertLength;
				}
				else if (run.endIndex >= offset)
				{
					//inserted in the middle or immediately after a run
					//the inserted text is included in the run
					run.endIndex += insertLength;
				}
			}
		}
		
		//append remaining text
		int charCount = sbuffer.length() - offset;
		if (buffer == null || buffer.length < charCount)
		{
			buffer = new char[charCount];
		}
		sbuffer.getChars(offset, sbuffer.length(), buffer, 0);
		newText.append(buffer, 0, charCount);
		
		//overwrite with the inserted buffer
		sbuffer = newText;
		
		attributedString = null;
		awtAttributedString = null;
	}
}
