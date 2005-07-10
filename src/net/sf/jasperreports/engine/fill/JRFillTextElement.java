/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;


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
	private JRFont font = null;

	private boolean isLeftToRight = true;
	private TextMeasurer textMeasurer = null;
	private float lineSpacingFactor = 0;
	private float leadingOffset = 0;
	private float textHeight = 0;
	private int textStart = 0;
	private int textEnd = 0;
	private String rawText = null;
	private JRStyledText styledText = null;
	private Map styledTextAttributes = null;

	protected TextChopper textChopper = null;
	

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
		textMeasurer = new TextMeasurer(this);

		/*   */
		textChopper = isStyledText() ? styledTextChopper : simpleTextChopper;
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
	public JRBox getBox()
	{
		return ((JRTextElement)parent).getBox();
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
	protected Map getStyledTextAttributes()
	{
		if (styledTextAttributes == null)
		{
			styledTextAttributes = new HashMap(); 
			styledTextAttributes.putAll(getFont().getAttributes());
			styledTextAttributes.put(TextAttribute.FOREGROUND, getForecolor());
			if (getMode() == JRElement.MODE_OPAQUE)
			{
				styledTextAttributes.put(TextAttribute.BACKGROUND, getBackcolor());
			}
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
	protected byte getRunDirection()
	{
		return isLeftToRight ? JRPrintText.RUN_DIRECTION_LTR : JRPrintText.RUN_DIRECTION_RTL;
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
		
		isLeftToRight = true;
		lineSpacingFactor = 0;
		leadingOffset = 0;
		textHeight = 0;
	}


	/**
	 *
	 */
	protected void rewind()
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
		JRStyledText tmpStyledText = getStyledText();

		if (tmpStyledText == null)
		{
			return null;
		}

		return tmpStyledText.getText();
	}
	

	/**
	 *
	 */
	protected void chopTextElement(
		int availableStretchHeight
		)
	{
		JRStyledText tmpStyledText = getStyledText();

		if (tmpStyledText == null)
		{
			return;
		}

		String remainingText = 
			getText().substring(
				getTextEnd()
				);

		if (remainingText.length() == 0)
		{
			return;
		}

		/*   */
		textMeasurer.measure(
			tmpStyledText,
			remainingText,
			getTextEnd(),
			availableStretchHeight 
			);
		
		isLeftToRight = textMeasurer.isLeftToRight();
		setTextHeight(textMeasurer.getTextHeight());
		if (getRotation() == ROTATION_NONE)
		{
			if (getBox() == null)
			{
				setStretchHeight((int)getTextHeight());
			}
			else
			{
				setStretchHeight((int)getTextHeight() + getBox().getTopPadding() + getBox().getBottomPadding());
			}
		}
		else
		{
			setStretchHeight(getHeight());
		}
		setTextStart(getTextEnd());
		setTextEnd(textMeasurer.getTextOffset());
		setLineSpacingFactor(textMeasurer.getLineSpacingFactor());
		setLeadingOffset(textMeasurer.getLeadingOffset());
	}
	
	
	/**
	 *
	 */
	protected static interface TextChopper
	{
		/**
		 *
		 */
		public String chop(JRFillTextElement textElement, int startIndex, int endIndex);
	}


	/**
	 *
	 */
	private static TextChopper simpleTextChopper = 
		new TextChopper()
		{
			public String chop(JRFillTextElement textElement, int startIndex, int endIndex)
			{
				return textElement.getStyledText().getText().substring(startIndex, endIndex);
			}
		};

	/**
	 *
	 */
	private static TextChopper styledTextChopper = 
		new TextChopper()
		{
			public String chop(JRFillTextElement textElement, int startIndex, int endIndex)
			{
				return 
					textElement.filler.getStyledTextParser().write(
						textElement.getStyledTextAttributes(),
						new AttributedString(
							textElement.getStyledText().getAttributedString().getIterator(), 
							startIndex, 
							endIndex
							).getIterator(),
						textElement.getText().substring(startIndex, endIndex)
						);
			}
		};

}
