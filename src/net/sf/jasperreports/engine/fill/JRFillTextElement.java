/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine.fill;

import java.awt.Dimension;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.util.*;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillTextElement extends JRFillElement implements JRTextElement
{


	/**
	 *
	 */
	private static final Log log = LogFactory.getLog(JRFillTextElement.class);

	/**
	 *
	 */
	private static FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);

	/**
	 *
	 */
	private JRFont font = null;

	private Dimension dimension = null;
	private float floatLineSpacing = 0;
	private float lineSpacingFactor = 0;
	private float leadingOffset = 0;
	private float textHeight = 0;
	private int textStart = 0;
	private int textEnd = 0;
	private String rawText = null;
	private JRStyledText styledText = null;
	private MaxFontSizeFinder maxFontSizeFinder = null;
	private Map styledTextAttributes = null;


	/**
	 *
	 */
	protected JRFillTextElement(
		JRBaseFiller filler,
		JRTextElement textElement, 
		JRFillObjectFactory factory
		)
	{
		super(filler, textElement, factory);

		/*   */
		font = factory.getFont(textElement.getFont());
		
		/*   */
		initializeDimension();
		initilizeFloatLineSpacing();
		initilizeMaxFontFinder();
	}


	/**
	 *
	 */
	public byte getTextAlignment()
	{
		return ((JRTextElement)parent).getTextAlignment();
	}
		
	/**
	 *
	 */
	public void setTextAlignment(byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return ((JRTextElement)parent).getVerticalAlignment();
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
	}
		
	/**
	 *
	 */
	public byte getRotation()
	{
		return ((JRTextElement)parent).getRotation();
	}
		
	/**
	 *
	 */
	public void setRotation(byte rotation)
	{
	}
		
	/**
	 *
	 */
	public byte getLineSpacing()
	{
		return ((JRTextElement)parent).getLineSpacing();
	}
		
	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing)
	{
	}
		
	/**
	 *
	 */
	public boolean isStyledText()
	{
		return ((JRTextElement)parent).isStyledText();
	}
		
	/**
	 *
	 */
	public void setStyledText(boolean isStyledText)
	{
	}
		
	/**
	 *
	 */
	public JRFont getFont()
	{
		return font;
	}
		

	/**
	 *
	 */
	private void initializeDimension()
	{
		int width = getWidth();
		int height = getHeight();
		
		switch (getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				width = getHeight();
				height = getWidth();
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				width = getHeight();
				height = getWidth();
				break;
			}
			case JRTextElement.ROTATION_NONE :
			default :
			{
			}
		}
		
		dimension = new Dimension(width, height);
	}
	/**
	 *
	 */
	private void initilizeFloatLineSpacing()
	{
		switch (getLineSpacing())
		{
			case JRTextElement.LINE_SPACING_SINGLE : 
			{
				floatLineSpacing = 1f;
				break;
			}
			case JRTextElement.LINE_SPACING_1_1_2 : 
			{
				floatLineSpacing = 1.5f;
				break;
			}
			case JRTextElement.LINE_SPACING_DOUBLE : 
			{
				floatLineSpacing = 2f;
				break;
			}
			default : 
			{
				floatLineSpacing = 1f;
			}
		}
	}
		
	/**
	 *
	 */
	private void initilizeMaxFontFinder()
	{
		if (isStyledText())
		{
			maxFontSizeFinder = new StyledTextMaxFontFinder();
		}
		else
		{
			maxFontSizeFinder = new DefaultMaxFontFinder();
		}
	}

	/**
	 *
	 */
	protected Map getStyledTextAttributes()
	{
		if (styledTextAttributes == null)
		{
			styledTextAttributes = new HashMap(); 
			styledTextAttributes.putAll(getFont().getAttributes());
			styledTextAttributes.put(TextAttribute.FOREGROUND, getForecolor());
			styledTextAttributes.put(TextAttribute.BACKGROUND, getBackcolor());
		}
		
		return styledTextAttributes;
	}

	/**
	 *
	 */
	protected float getLineSpacingFactor()
	{
		return lineSpacingFactor;
	}
		
	/**
	 *
	 */
	protected void setLineSpacingFactor(float lineSpacingFactor)
	{
		this.lineSpacingFactor = lineSpacingFactor;
	}

	/**
	 *
	 */
	protected float getLeadingOffset()
	{
		return leadingOffset;
	}
		
	/**
	 *
	 */
	protected void setLeadingOffset(float leadingOffset)
	{
		this.leadingOffset = leadingOffset;
	}

	/**
	 *
	 */
	protected float getTextHeight()
	{
		return textHeight;
	}
		
	/**
	 *
	 */
	protected void setTextHeight(float textHeight)
	{
		this.textHeight = textHeight;
	}

	/**
	 *
	 */
	protected int getTextStart()
	{
		return textStart;
	}
		
	/**
	 *
	 */
	protected void setTextStart(int textStart)
	{
		this.textStart = textStart;
	}

	/**
	 *
	 */
	protected int getTextEnd()
	{
		return textEnd;
	}
		
	/**
	 *
	 */
	protected void setTextEnd(int textEnd)
	{
		this.textEnd = textEnd;
	}

	/**
	 *
	 */
	protected String getRawText()
	{
		return rawText;
	}

	/**
	 *
	 */
	protected void setRawText(String rawText)
	{
		this.rawText = rawText;
		styledText = null;
	}


	/**
	 *
	 */
	protected void reset()
	{
		super.reset();
		
		lineSpacingFactor = 0;
		leadingOffset = 0;
		textHeight = 0;
	}


	/**
	 *
	 */
	protected void rewind() throws JRException
	{
		textStart = 0;
		textEnd = 0;
	}


	/**
	 *
	 */
	protected JRStyledText getStyledText()
	{
		if (styledText == null)
		{
			String text = getRawText();
			if (text != null)
			{
				if (isStyledText())
				{
					try
					{
						styledText = filler.getStyledTextParser().parse(getStyledTextAttributes(), text);
					}
					catch (SAXException e)
					{
						if (log.isWarnEnabled())
							log.warn("Invalid styled text.", e);
					}
				}
		
				if (styledText == null)
				{
					styledText = new JRStyledText();
					styledText.append(text);
					styledText.addRun(new JRStyledText.Run(getStyledTextAttributes(), 0, text.length()));
				}
			}
		}
		
		return styledText;
	}

	/**
	 *
	 */
	public String getText()
	{
		JRStyledText styledText = getStyledText();

		if (styledText == null)
		{
			return null;
		}
		else
		{
			return styledText.getText();
		}
	}
	

	/**
	 *
	 */
	protected void chopTextElement(
		int availableStretchHeight
		)
	{
		JRStyledText styledText = getStyledText();

		if (styledText == null)
		{
			return;
		}

		String allText = 
			getText().substring(
				getTextEnd()
				);

		if (allText.length() == 0)
		{
			return;
		}

		float formatWidth = (float)dimension.width;
		float lineSpacing = floatLineSpacing;
		float drawPosY = 0;
		float firstLineLeading = 0;
		int maxHeight = dimension.height + availableStretchHeight;
		int strpos = 0;
		int lastPosition = 0;
		int lines = 0;
		int fontSizeSum = 0;
		int firstLineMaxFontSize = 0;
		int paragraphStart = 0;
		int paragraphEnd = getTextEnd();
		boolean isMaxHeightReached = false;
		boolean wasDelim = false;

		AttributedCharacterIterator allParagraphs = styledText.getAttributedString().getIterator();

		StringTokenizer tkzer = new StringTokenizer(allText, "\n", true);
	
		while(tkzer.hasMoreTokens() && !isMaxHeightReached) 
		{
			String paragraphText = tkzer.nextToken();

			paragraphStart = paragraphEnd;
			paragraphEnd = paragraphStart + paragraphText.length();

			if ("\n".equals(paragraphText))
			{
				wasDelim = true;
				continue;
			}

			AttributedCharacterIterator paragraph = new AttributedString(allParagraphs, paragraphStart, paragraphEnd).getIterator();
			LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, fontRenderContext);
	
			lastPosition = lineMeasurer.getPosition();
	
			while (lineMeasurer.getPosition() < paragraphText.length() && !isMaxHeightReached)
			{
				int lineStart = lineMeasurer.getPosition();

				TextLayout layout = lineMeasurer.nextLayout(formatWidth);

				drawPosY += layout.getLeading() + lineSpacing * layout.getAscent();

				if (drawPosY + layout.getDescent() <= maxHeight)
				{
				    lines++;

					fontSizeSum += 
						maxFontSizeFinder.findMaxFontSize(
							new AttributedString(
								paragraph, 
								lineStart, 
								lineStart + layout.getCharacterCount()
								).getIterator(),
							getFont().getSize()
							);
							
					if (lines == 1)
					{
						firstLineLeading = drawPosY;
						firstLineMaxFontSize = fontSizeSum;
					}

				    lastPosition = lineMeasurer.getPosition();
					// here is the Y offset where we would draw the line
					//lastDrawPosY = drawPosY;
					//
					drawPosY += layout.getDescent();
				}
				else
				{
				    drawPosY -= layout.getLeading() + lineSpacing * layout.getAscent();
		    	    isMaxHeightReached = true;
				}
			}
	
			if (isMaxHeightReached)
			{
				strpos += lastPosition;
				//if (lastPosition > 0)
				{
					// lastPosition > 0 means that at least one line of text 
					// was layed out from the current paragraph, so we can take into account
					// also the newline character that introduced this paragraph
					if (wasDelim)
					{
						// but we take into account the newline character only
						// if the current paragraph was introduced by a newline caracter and
						// it is not simply the first paragraph in the overall text
						strpos++;
					}
				}
			}
			else
			{
				strpos += paragraphText.length();
				if (wasDelim)
				{
					// we take into account the newline character only
					// if the current paragraph was introduced by a newline caracter and
					// it is not simply the first paragraph in the overall text
					strpos++;
				}
			}
		}

		setTextHeight(drawPosY + 1);
		if (getRotation() == ROTATION_NONE)
		{
			setStretchHeight((int)getTextHeight());
		}
		else
		{
			setStretchHeight(getHeight());
		}
		setTextStart(getTextEnd());
		setTextEnd(getTextStart() + strpos);
		//setTextEnd(strpos);
		if (lines > 0)
		{
			//setLineSpacingFactor((lastDrawPosY - 1) / lines);
			//setLineSpacingFactor(drawPosY / lines);
			//setLineSpacingFactor((getTextHeight() - firstLineLeading) / (fontSizeSum - firstLineMaxFontSize));
			setLineSpacingFactor(getTextHeight() / fontSizeSum);
			//setLeadingOffset(0);
			//setLeadingOffset(- firstLineLeading + firstLineMaxFontSize * getLineSpacingFactor());
			setLeadingOffset(firstLineLeading - firstLineMaxFontSize * getLineSpacingFactor());
			//setLeadingOffset(firstLineLeading);
		}
		else
		{
			setLineSpacingFactor(0);
			setLeadingOffset(0);
		}
		//setLineSpacingFactor(drawPosY / lines);
	}
	
	
}


/**
 * 
 */
interface MaxFontSizeFinder
{
	/**
	 * 
	 */
	public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize);
}


/**
 * 
 */
class StyledTextMaxFontFinder implements MaxFontSizeFinder
{
	
	private static final Float ZERO = new Float(0);
	
	/**
	 * 
	 */
	public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize)
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

		return maxFontSize.intValue();
	}
}


/**
 * 
 */
class DefaultMaxFontFinder implements MaxFontSizeFinder
{
	/**
	 * 
	 */
	public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize)
	{
		return defaultFontSize;
	}
}
