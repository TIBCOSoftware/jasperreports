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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.util.JRFontUtil;
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
	
	protected final JRReportFont reportFont;

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

		reportFont = factory.getReportFont(textElement.getReportFont());
		
		/*   */
		createTextMeasurer();
		createTextChopper();
	}
	

	protected JRFillTextElement(JRFillTextElement textElement, JRFillCloneFactory factory)
	{
		super(textElement, factory);
		
		reportFont = textElement.reportFont;

		createTextMeasurer();
		createTextChopper();
	}


	private void createTextMeasurer()
	{
		textMeasurer = new TextMeasurer(this);
	}


	private void createTextChopper()
	{
		textChopper = isStyledText() ? styledTextChopper : simpleTextChopper;
	}


	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignment()}.
	 */
	public byte getTextAlignment()
	{
		return ((JRTextElement)parent).getHorizontalAlignment();
	}

	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(byte)}.
	 */
	public void setTextAlignment(byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public byte getHorizontalAlignment()
	{
		return ((JRTextElement)parent).getHorizontalAlignment();
	}
		
	public Byte getOwnHorizontalAlignment()
	{
		return ((JRTextElement)parent).getOwnHorizontalAlignment();
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return ((JRTextElement)parent).getVerticalAlignment();
	}
		
	public Byte getOwnVerticalAlignment()
	{
		return ((JRTextElement)parent).getOwnVerticalAlignment();
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
	public void setVerticalAlignment(Byte verticalAlignment)
	{
	}
		
	/**
	 *
	 */
	public byte getRotation()
	{
		return ((JRTextElement)parent).getRotation();
	}
		
	public Byte getOwnRotation()
	{
		return ((JRTextElement)parent).getOwnRotation();
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
	public void setRotation(Byte rotation)
	{
	}
		
	/**
	 *
	 */
	public byte getLineSpacing()
	{
		return ((JRTextElement)parent).getLineSpacing();
	}
		
	public Byte getOwnLineSpacing()
	{
		return ((JRTextElement)parent).getOwnLineSpacing();
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
	public void setLineSpacing(Byte lineSpacing)
	{
	}
		
	/**
	 *
	 */
	public boolean isStyledText()
	{
		return ((JRTextElement)parent).isStyledText();
	}
		
	public Boolean isOwnStyledText()
	{
		return ((JRTextElement)parent).isOwnStyledText();
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
	public void setStyledText(Boolean isStyledText)
	{
	}
		
	/**
	 * @deprecated
	 */
	public JRBox getBox()
	{
		return this;
	}

	/**
	 * @deprecated
	 */
	public JRFont getFont()
	{
		return this;
	}

	
	/**
	 *
	 */
	protected Map getStyledTextAttributes()
	{
		if (styledTextAttributes == null)
		{
			styledTextAttributes = new HashMap(); 
			JRFontUtil.setAttributes(styledTextAttributes, (JRTextElement)parent);
			styledTextAttributes.put(TextAttribute.FOREGROUND, parent.getForecolor());
			if (parent.getMode() == JRElement.MODE_OPAQUE)
			{
				styledTextAttributes.put(TextAttribute.BACKGROUND, parent.getBackcolor());
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
			setStretchHeight((int)getTextHeight() + getTopPadding() + getBottomPadding());
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

	/**
	 *
	 */
	public byte getBorder()
	{
		return ((JRBox)parent).getBorder();
	}

	public Byte getOwnBorder()
	{
		return ((JRBox)parent).getOwnBorder();
	}

	/**
	 *
	 */
	public void setBorder(byte border)
	{
	}

	/**
	 *
	 */
	public Color getBorderColor()
	{
		return ((JRBox)parent).getBorderColor();
	}

	public Color getOwnBorderColor()
	{
		return ((JRBox)parent).getOwnBorderColor();
	}

	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
	}

	/**
	 *
	 */
	public int getPadding()
	{
		return ((JRBox)parent).getPadding();
	}

	public Integer getOwnPadding()
	{
		return ((JRBox)parent).getOwnPadding();
	}

	/**
	 *
	 */
	public void setPadding(int padding)
	{
	}

	/**
	 *
	 */
	public byte getTopBorder()
	{
		return ((JRBox)parent).getTopBorder();
	}

	/**
	 *
	 */
	public Byte getOwnTopBorder()
	{
		return ((JRBox)parent).getOwnTopBorder();
	}

	/**
	 *
	 */
	public void setTopBorder(byte topBorder)
	{
	}

	/**
	 *
	 */
	public Color getTopBorderColor()
	{
		return ((JRBox)parent).getTopBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnTopBorderColor()
	{
		return ((JRBox)parent).getOwnTopBorderColor();
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
	}

	/**
	 *
	 */
	public int getTopPadding()
	{
		return ((JRBox)parent).getTopPadding();
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return ((JRBox)parent).getOwnTopPadding();
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
	}

	/**
	 *
	 */
	public byte getLeftBorder()
	{
		return ((JRBox)parent).getLeftBorder();
	}

	/**
	 *
	 */
	public Byte getOwnLeftBorder()
	{
		return ((JRBox)parent).getOwnLeftBorder();
	}

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder)
	{
	}

	/**
	 *
	 */
	public Color getLeftBorderColor()
	{
		return ((JRBox)parent).getLeftBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnLeftBorderColor()
	{
		return ((JRBox)parent).getOwnLeftBorderColor();
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
	}

	/**
	 *
	 */
	public int getLeftPadding()
	{
		return ((JRBox)parent).getLeftPadding();
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return ((JRBox)parent).getOwnLeftPadding();
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
	}

	/**
	 *
	 */
	public byte getBottomBorder()
	{
		return ((JRBox)parent).getBottomBorder();
	}

	/**
	 *
	 */
	public Byte getOwnBottomBorder()
	{
		return ((JRBox)parent).getOwnBottomBorder();
	}

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public Color getBottomBorderColor()
	{
		return ((JRBox)parent).getBottomBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnBottomBorderColor()
	{
		return ((JRBox)parent).getOwnBottomBorderColor();
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
	}

	/**
	 *
	 */
	public int getBottomPadding()
	{
		return ((JRBox)parent).getBottomPadding();
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return ((JRBox)parent).getOwnBottomPadding();
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
	}

	/**
	 *
	 */
	public byte getRightBorder()
	{
		return ((JRBox)parent).getRightBorder();
	}

	/**
	 *
	 */
	public Byte getOwnRightBorder()
	{
		return ((JRBox)parent).getOwnRightBorder();
	}

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder)
	{
	}

	/**
	 *
	 */
	public Color getRightBorderColor()
	{
		return ((JRBox)parent).getRightBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnRightBorderColor()
	{
		return ((JRBox)parent).getOwnRightBorderColor();
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
	}

	/**
	 *
	 */
	public int getRightPadding()
	{
		return ((JRBox)parent).getRightPadding();
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return ((JRBox)parent).getOwnRightPadding();
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
	}


	/**
	 *
	 */
	public JRReportFont getReportFont()
	{
		return reportFont;
	}

	public void setReportFont(JRReportFont reportFont)
	{
	}
	
	/**
	 *
	 */
	public String getFontName()
	{
		return ((JRFont)parent).getFontName();
	}

	/**
	 *
	 */
	public String getOwnFontName()
	{
		return ((JRFont)parent).getOwnFontName();
	}

	/**
	 *
	 */
	public void setFontName(String fontName)
	{
	}


	/**
	 *
	 */
	public boolean isBold()
	{
		return ((JRFont)parent).isBold();
	}

	/**
	 *
	 */
	public Boolean isOwnBold()
	{
		return ((JRFont)parent).isOwnBold();
	}

	/**
	 *
	 */
	public void setBold(boolean isBold)
	{
	}

	/**
	 * Alternative setBold method which allows also to reset
	 * the "own" isBold property.
	 */
	public void setBold(Boolean isBold)
	{
	}


	/**
	 *
	 */
	public boolean isItalic()
	{
		return ((JRFont)parent).isItalic();
	}

	/**
	 *
	 */
	public Boolean isOwnItalic()
	{
		return ((JRFont)parent).isOwnItalic();
	}

	/**
	 *
	 */
	public void setItalic(boolean isItalic)
	{
	}

	/**
	 * Alternative setItalic method which allows also to reset
	 * the "own" isItalic property.
	 */
	public void setItalic(Boolean isItalic)
	{
	}

	/**
	 *
	 */
	public boolean isUnderline()
	{
		return ((JRFont)parent).isUnderline();
	}

	/**
	 *
	 */
	public Boolean isOwnUnderline()
	{
		return ((JRFont)parent).isOwnUnderline();
	}

	/**
	 *
	 */
	public void setUnderline(boolean isUnderline)
	{
	}

	/**
	 * Alternative setUnderline method which allows also to reset
	 * the "own" isUnderline property.
	 */
	public void setUnderline(Boolean isUnderline)
	{
	}

	/**
	 *
	 */
	public boolean isStrikeThrough()
	{
		return ((JRFont)parent).isStrikeThrough();
	}

	/**
	 *
	 */
	public Boolean isOwnStrikeThrough()
	{
		return ((JRFont)parent).isOwnStrikeThrough();
	}

	/**
	 *
	 */
	public void setStrikeThrough(boolean isStrikeThrough)
	{
	}

	/**
	 * Alternative setStrikeThrough method which allows also to reset
	 * the "own" isStrikeThrough property.
	 */
	public void setStrikeThrough(Boolean isStrikeThrough)
	{
	}

	/**
	 *
	 */
	public int getFontSize()
	{
		return ((JRFont)parent).getFontSize();
	}

	/**
	 *
	 */
	public Integer getOwnFontSize()
	{
		return ((JRFont)parent).getOwnFontSize();
	}

	/**
	 *
	 */
	public void setFontSize(int size)
	{
	}

	/**
	 * Alternative setSize method which allows also to reset
	 * the "own" size property.
	 */
	public void setFontSize(Integer size)
	{
	}

	/**
	 * @deprecated Replaced by {@link #getFontSize()}.
	 */
	public int getSize()
	{
		return getFontSize();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnFontSize()}.
	 */
	public Integer getOwnSize()
	{
		return getOwnFontSize();
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(int)}.
	 */
	public void setSize(int size)
	{
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Integer)}.
	 */
	public void setSize(Integer size)
	{
	}

	/**
	 *
	 */
	public String getPdfFontName()
	{
		return ((JRFont)parent).getPdfFontName();
	}

	/**
	 *
	 */
	public String getOwnPdfFontName()
	{
		return ((JRFont)parent).getOwnPdfFontName();
	}

	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName)
	{
	}


	/**
	 *
	 */
	public String getPdfEncoding()
	{
		return ((JRFont)parent).getPdfEncoding();
	}

	/**
	 *
	 */
	public String getOwnPdfEncoding()
	{
		return ((JRFont)parent).getOwnPdfEncoding();
	}

	/**
	 *
	 */
	public void setPdfEncoding(String pdfEncoding)
	{
	}


	/**
	 *
	 */
	public boolean isPdfEmbedded()
	{
		return ((JRFont)parent).isPdfEmbedded();
	}

	/**
	 *
	 */
	public Boolean isOwnPdfEmbedded()
	{
		return ((JRFont)parent).isOwnPdfEmbedded();
	}

	/**
	 *
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
		setPdfEmbedded(isPdfEmbedded ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setPdfEmbedded method which allows also to reset
	 * the "own" isPdfEmbedded property.
	 */
	public void setPdfEmbedded(Boolean isPdfEmbedded)
	{
	}

	/**
	 *
	 */
	public void setBorder(Byte border)
	{
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
	}

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder)
	{
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
	}

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder)
	{
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
	}

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
	}

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder)
	{
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
	}

	/**
	 *
	 */
	public void setHeight(int height)
	{
		super.setHeight(height);
		
		createTextMeasurer();
	}


	public void setWidth(int width)
	{
		super.setWidth(width);
		
		createTextMeasurer();
	}

}
