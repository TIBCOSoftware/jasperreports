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

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;
import java.util.StringTokenizer;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.design.JRDesignFont;


/**
 *
 */
public abstract class JRFillTextElement extends JRFillElement implements JRTextElement
{


	/**
	 *
	 */
	private JRFont font = null;

	private float floatLineSpacing = 0;
	private float absoluteLineSpacing = 0;
	private float absoluteLeading = 0;
	private float textHeight = 0;
	private int textStart = 0;
	private int textEnd = 0;

	private static FontRenderContext fontRenderContext = null;


	/**
	 *
	 */
	protected JRFillTextElement(
		JRBaseFiller filler,
		JRTextElement textElement, 
		Map fillObjectsMap
		)
	{
		super(filler, textElement, fillObjectsMap);
	}


	/**
	 *
	 */
	public byte getTextAlignment()
	{
		return ((JRTextElement)this.parent).getTextAlignment();
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
		return ((JRTextElement)this.parent).getVerticalAlignment();
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
	public byte getLineSpacing()
	{
		return ((JRTextElement)this.parent).getLineSpacing();
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
	public JRFont getFont()
	{
		if (this.font == null)
		{
			this.font = ((JRTextElement)this.parent).getFont();
			if (this.font == null)
			{
				this.font = this.filler.defaultFont;
				if (this.font == null)
				{
					this.font = new JRDesignFont();
				}
			}
		}
		
		return this.font;
	}
		
	/**
	 *
	 */
	protected float getFloatLineSpacing()
	{
		if (floatLineSpacing == 0)
		{
			floatLineSpacing = 1f;
			switch (this.getLineSpacing())
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
		
		return floatLineSpacing;
	}

	/**
	 *
	 */
	protected static FontRenderContext getFontRenderContext()
	{
		if (fontRenderContext == null)
		{
			AffineTransform atrans = new AffineTransform();
			//atrans.scale(1f, 1f);
			
			fontRenderContext = 
				new FontRenderContext(
					atrans,
					true, 
					true
					);
		}
		
		return fontRenderContext;
	}

	/**
	 *
	 */
	protected float getAbsoluteLineSpacing()
	{
		return this.absoluteLineSpacing;
	}
		
	/**
	 *
	 */
	protected void setAbsoluteLineSpacing(float absoluteLineSpacing)
	{
		this.absoluteLineSpacing = absoluteLineSpacing;
	}

	/**
	 *
	 */
	protected float getAbsoluteLeading()
	{
		return this.absoluteLeading;
	}
		
	/**
	 *
	 */
	protected void setAbsoluteLeading(float absoluteLeading)
	{
		this.absoluteLeading = absoluteLeading;
	}

	/**
	 *
	 */
	protected float getTextHeight()
	{
		return this.textHeight;
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
		return this.textStart;
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
		return this.textEnd;
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
	protected String getText()
	{
		return null;
	}


	/**
	 *
	 */
	protected void reset()
	{
		super.reset();
		
		this.absoluteLineSpacing = 0;
		this.absoluteLeading = 0;
		this.textHeight = 0;
	}


	/**
	 *
	 */
	protected void rewind() throws JRException
	{
		this.textStart = 0;
		this.textEnd = 0;
	}


	/**
	 *
	 */
	protected void chopTextElement(
		int availableStretchHeight
		)
	{
		if (this.getText() == null || this.getText().length() == 0)
		{
			return;
		}

		String allText = 
			this.getText().substring(
				this.getTextEnd()
				);

		if (allText.length() == 0)
		{
			return;
		}

		//allText = JRStringUtil.treatNewLineChars(allText);

		float formatWidth = (float) this.getWidth();
		float lineSpacing = this.getFloatLineSpacing();
		int maxHeight = this.getHeight() + availableStretchHeight;
		FontRenderContext fontRenderContext = getFontRenderContext();
		Map fontAttributes = this.getFont().getAttributes();

		AttributedString atext = null;
		AttributedCharacterIterator paragraph = null;
		LineBreakMeasurer lineMeasurer = null;
		TextLayout layout = null;
		int paragraphStart = 0;
		int paragraphEnd = 0;

		float drawPosY = 0;
		//float lastDrawPosY = 0;
		
		String paragr_text = null;
		int strpos = 0;
		int lastPosition = 0;
		int lines = 0;
		boolean isMaxHeightReached = false;
		boolean wasDelim = false;

		StringTokenizer tkzer = new StringTokenizer(allText, "\n", true);
	
		while(tkzer.hasMoreTokens() && !isMaxHeightReached) 
		{
			paragr_text = tkzer.nextToken();
			if ("\n".equals(paragr_text))
			{
				wasDelim = true;
				continue;
			}
	
			atext = new AttributedString(paragr_text, fontAttributes);
			paragraph = atext.getIterator();
			paragraphStart = paragraph.getBeginIndex();
			paragraphEnd = paragraph.getEndIndex();
			lineMeasurer = new LineBreakMeasurer(paragraph,fontRenderContext);
			lineMeasurer.setPosition(paragraphStart);
	
			lastPosition = lineMeasurer.getPosition();
	
			while (lineMeasurer.getPosition() < paragraphEnd && !isMaxHeightReached)
			{
				layout = lineMeasurer.nextLayout(formatWidth);
	
				if (lines == 0)
				{
					this.absoluteLeading = layout.getLeading() + lineSpacing * layout.getAscent();
				}
				
				drawPosY += layout.getLeading() + lineSpacing * layout.getAscent();

				if (drawPosY + layout.getDescent() <= maxHeight)
				{
				    lines++;
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
				strpos += paragr_text.length();
				if (wasDelim)
				{
					// we take into account the newline character only
					// if the current paragraph was introduced by a newline caracter and
					// it is not simply the first paragraph in the overall text
					strpos++;
				}
			}
		}

		this.setTextHeight(drawPosY + 1);
		this.setStretchHeight((int)this.getTextHeight());
		this.setTextStart(this.getTextEnd());
		this.setTextEnd(this.getTextStart() + strpos);
		//this.setTextEnd(strpos);
		if (lines != 0)
		{
			//this.setAbsoluteLineSpacing((lastDrawPosY - 1) / lines);
			//this.setAbsoluteLineSpacing(drawPosY / lines);
			this.setAbsoluteLineSpacing(this.getTextHeight() / lines);
		}
		else
		{
			this.setAbsoluteLineSpacing(0);
		}
		//this.setAbsoluteLineSpacing(drawPosY / lines);
	}
	

}
