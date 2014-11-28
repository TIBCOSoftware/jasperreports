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
import java.text.AttributedCharacterIterator;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class MaxFontSizeFinder//FIXMETAB deprecate?
{
	
	/**
	 * 
	 */
	public static final MaxFontSizeFinder STYLED_TEXT_MAX_FONT_FINDER = 
		new MaxFontSizeFinder()
		{
			/**
			 * 
			 */
			private final Float ZERO = new Float(0);
			
			/**
			 * 
			 */
			public float findMaxFontSize(AttributedCharacterIterator line, float defaultFontSize)
			{
				line.setIndex(0);
				Float maxFontSize = ZERO;
				int runLimit = 0;
	
				while(runLimit < line.getEndIndex() && (runLimit = line.getRunLimit(TextAttribute.SIZE)) <= line.getEndIndex())
				{
					Float size = (Float)line.getAttribute(TextAttribute.SIZE);
					if (maxFontSize.compareTo(size) < 0)
					{
						maxFontSize = size;
					}
					line.setIndex(runLimit);
				}
	
				return maxFontSize;
			}
			
			/**
			 * @deprecated Replaced by {@link #findMaxFontSize(AttributedCharacterIterator, float)}.
			 */
			public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize)
			{
				return (int)findMaxFontSize(line, (float)defaultFontSize);
			}
		};
	
	
	/**
	 * 
	 */
	public static final MaxFontSizeFinder DEFAULT_MAX_FONT_FINDER = 
		new MaxFontSizeFinder()
		{
			/**
			 * 
			 */
			public float findMaxFontSize(AttributedCharacterIterator line, float defaultFontSize)
			{
				return defaultFontSize;
			}

			/**
			 * @deprecated Replaced by {@link #findMaxFontSize(AttributedCharacterIterator, float)}.
			 */
			public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize)
			{
				return (int)findMaxFontSize(line, (float)defaultFontSize);
			}
		};
		
	
	/**
	 * 
	 */
	public static MaxFontSizeFinder getInstance(boolean isStyledText)
	{
		if (isStyledText)
		{
			return MaxFontSizeFinder.STYLED_TEXT_MAX_FONT_FINDER;
		}
		return MaxFontSizeFinder.DEFAULT_MAX_FONT_FINDER;
	}

		
	/**
	 * 
	 */
	public abstract float findMaxFontSize(AttributedCharacterIterator line, float defaultFontSize);

	
	/**
	 * @deprecated Replaced by {@link #findMaxFontSize(AttributedCharacterIterator, float)}. 
	 */
	public abstract int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize);
}
