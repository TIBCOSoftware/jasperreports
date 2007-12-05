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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRPenUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.LineBoxWrapper;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePrintText extends JRBasePrintElement implements JRPrintText
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String text = "";
	protected Integer textTruncateIndex;
	protected String textTruncateSuffix;
	protected transient String truncatedText;
	protected float lineSpacingFactor = 0;
	protected float leadingOffset = 0;
	protected Byte horizontalAlignment = null;
	protected Byte verticalAlignment = null;
	protected Byte rotation = null;
	protected byte runDirection = RUN_DIRECTION_LTR;
	protected float textHeight = 0;
	protected Byte lineSpacing = null;
	protected Boolean isStyledText = null;
	protected String anchorName = null;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NULL;
	private String linkType;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	protected String hyperlinkReference = null;
	protected String hyperlinkAnchor = null;
	protected Integer hyperlinkPage = null;
	protected String hyperlinkTooltip;
	protected JRPrintHyperlinkParameters hyperlinkParameters;

	/**
	 * The bookmark level for the anchor associated with this field.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;

	/**
	 *
	 */
	protected JRLineBox lineBox;

	protected JRReportFont reportFont = null;
	protected String fontName = null;
	protected Boolean isBold = null;
	protected Boolean isItalic = null;
	protected Boolean isUnderline = null;
	protected Boolean isStrikeThrough = null;
	protected Integer fontSize = null;
	protected String pdfFontName = null;
	protected String pdfEncoding = null;
	protected Boolean isPdfEmbedded = null;

	protected String valueClassName;
	protected String pattern;
	protected String formatFactoryClass;
	protected String localeCode;
	protected String timeZoneId;
	
	/**
	 *
	 */
	public JRBasePrintText(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
		
		lineBox = new JRBaseLineBox(this);
	}


	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, JRElement.MODE_TRANSPARENT);
	}
		
	/**
	 *
	 */
	public String getText()
	{
		if (truncatedText == null && text != null)
		{
			if (getTextTruncateIndex() == null)
			{
				truncatedText = text;
			}
			else
			{
				if (isStyledText())
				{
					truncatedText = JRStyledTextParser.getInstance().write(
							getFullStyledText(JRStyledTextAttributeSelector.ALL), 
							0, getTextTruncateIndex().intValue());
				}
				else
				{
					truncatedText = text.substring(0, getTextTruncateIndex().intValue());
				}
			}
			
			if (textTruncateSuffix != null)
			{
				truncatedText += textTruncateSuffix;
			}
		}
		return truncatedText;
	}
		
	/**
	 *
	 */
	public void setText(String text)
	{
		this.text = text;
		this.truncatedText = null;
	}

	public Integer getTextTruncateIndex()
	{
		return textTruncateIndex;
	}

	public void setTextTruncateIndex(Integer textTruncateIndex)
	{
		this.textTruncateIndex = textTruncateIndex;
		this.truncatedText = null;
	}

	public String getTextTruncateSuffix()
	{
		return textTruncateSuffix;
	}

	public void setTextTruncateSuffix(String textTruncateSuffix)
	{
		this.textTruncateSuffix = textTruncateSuffix;
		this.truncatedText = null;
	}

	public String getFullText()
	{
		String fullText = this.text;
		if (textTruncateIndex == null && textTruncateSuffix != null)
		{
			fullText += textTruncateSuffix;
		}
		return fullText;
	}

	public String getOriginalText()
	{
		return text;
	}

	public JRStyledText getStyledText(JRStyledTextAttributeSelector attributeSelector)
	{
		if (getText() == null)
		{
			return null;
		}
		
		return JRStyledTextParser.getInstance().getStyledText(
				attributeSelector.getStyledTextAttributes(this), 
				getText(), 
				isStyledText());
	}

	public JRStyledText getFullStyledText(JRStyledTextAttributeSelector attributeSelector)
	{
		if (getFullText() == null)
		{
			return null;
		}

		return JRStyledTextParser.getInstance().getStyledText(
				attributeSelector.getStyledTextAttributes(this), 
				getFullText(), 
				isStyledText());
	}

	/**
	 *
	 */
	public float getLineSpacingFactor()
	{
		return lineSpacingFactor;
	}
		
	/**
	 *
	 */
	public void setLineSpacingFactor(float lineSpacingFactor)
	{
		this.lineSpacingFactor = lineSpacingFactor;
	}

	/**
	 *
	 */
	public float getLeadingOffset()
	{
		return leadingOffset;
	}
		
	/**
	 *
	 */
	public void setLeadingOffset(float leadingOffset)
	{
		this.leadingOffset = leadingOffset;
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignment()}.
	 */
	public byte getTextAlignment()
	{
		return getHorizontalAlignment();
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(byte)}.
	 */
	public void setTextAlignment(byte horizontalAlignment)
	{
		setHorizontalAlignment(horizontalAlignment);
	}

	/**
	 *
	 */
	public byte getHorizontalAlignment()
	{
		return JRStyleResolver.getHorizontalAlignment(this);
	}
		
	public Byte getOwnHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
		this.horizontalAlignment = new Byte(horizontalAlignment);
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return JRStyleResolver.getVerticalAlignment(this);
	}
		
	public Byte getOwnVerticalAlignment()
	{
		return verticalAlignment;
	}

	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
		this.verticalAlignment = new Byte(verticalAlignment);
	}

	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 *
	 */
	public byte getRotation()
	{
		return JRStyleResolver.getRotation(this);
	}
		
	public Byte getOwnRotation()
	{
		return rotation;
	}

	/**
	 *
	 */
	public void setRotation(byte rotation)
	{
		this.rotation = new Byte(rotation);
	}

	/**
	 *
	 */
	public void setRotation(Byte rotation)
	{
		this.rotation = rotation;
	}

	/**
	 *
	 */
	public byte getRunDirection()
	{
		return runDirection;
	}
		
	/**
	 *
	 */
	public void setRunDirection(byte runDirection)
	{
		this.runDirection = runDirection;
	}

	/**
	 *
	 */
	public float getTextHeight()
	{
		return textHeight;
	}
		
	/**
	 *
	 */
	public void setTextHeight(float textHeight)
	{
		this.textHeight = textHeight;
	}

	/**
	 *
	 */
	public byte getLineSpacing()
	{
		return JRStyleResolver.getLineSpacing(this);
	}
		
	public Byte getOwnLineSpacing()
	{
		return lineSpacing;
	}

	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing)
	{
		this.lineSpacing = new Byte(lineSpacing);
	}

	/**
	 *
	 */
	public void setLineSpacing(Byte lineSpacing)
	{
		this.lineSpacing = lineSpacing;
	}

	/**
	 *
	 */
	public boolean isStyledText()
	{
		return JRStyleResolver.isStyledText(this);
	}
		
	public Boolean isOwnStyledText()
	{
		return isStyledText;
	}

	/**
	 *
	 */
	public void setStyledText(boolean isStyledText)
	{
		setStyledText(isStyledText ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setStyledText(Boolean isStyledText)
	{
		this.isStyledText = isStyledText;
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public JRBox getBox()
	{
		return new LineBoxWrapper(getLineBox());
	}

	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	/**
	 * @deprecated Replaced by {@link #copyBox(JRLineBox)}
	 */
	public void setBox(JRBox box)
	{
		JRBoxUtil.setBoxToLineBox(box, lineBox);
	}

	/**
	 *
	 */
	public void copyBox(JRLineBox lineBox)
	{
		this.lineBox = lineBox.clone(this);
	}

	/**
	 * @deprecated
	 */
	public JRFont getFont()
	{
		return this;
	}

	/**
	 * @deprecated
	 */
	public void setFont(JRFont font)
	{
		reportFont = font.getReportFont();
		
		fontName = font.getOwnFontName();
		isBold = font.isOwnBold();
		isItalic = font.isOwnItalic();
		isUnderline = font.isOwnUnderline();
		isStrikeThrough = font.isOwnStrikeThrough();
		fontSize = font.getOwnSize();
		pdfFontName = font.getOwnPdfFontName();
		pdfEncoding = font.getOwnPdfEncoding();
		isPdfEmbedded = font.isOwnPdfEmbedded();
	}

	/**
	 *
	 */
	public String getAnchorName()
	{
		return anchorName;
	}
		
	/**
	 *
	 */
	public void setAnchorName(String anchorName)
	{
		this.anchorName = anchorName;
	}
		
	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return JRHyperlinkHelper.getHyperlinkType(getLinkType());
	}
		
	/**
	 *
	 */
	public void setHyperlinkType(byte hyperlinkType)
	{
		setLinkType(JRHyperlinkHelper.getLinkType(hyperlinkType));
	}

	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return hyperlinkTarget;
	}
		
	/**
	 *
	 */
	public void setHyperlinkTarget(byte hyperlinkTarget)
	{
		this.hyperlinkTarget = hyperlinkTarget;
	}

	/**
	 *
	 */
	public String getHyperlinkReference()
	{
		return hyperlinkReference;
	}
		
	/**
	 *
	 */
	public void setHyperlinkReference(String hyperlinkReference)
	{
		this.hyperlinkReference = hyperlinkReference;
	}
		
	/**
	 *
	 */
	public String getHyperlinkAnchor()
	{
		return hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public void setHyperlinkAnchor(String hyperlinkAnchor)
	{
		this.hyperlinkAnchor = hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public Integer getHyperlinkPage()
	{
		return hyperlinkPage;
	}
		
	/**
	 *
	 */
	public void setHyperlinkPage(Integer hyperlinkPage)
	{
		this.hyperlinkPage = hyperlinkPage;
	}
		
	/**
	 *
	 */
	public void setHyperlinkPage(String hyperlinkPage)
	{
		this.hyperlinkPage = new Integer(hyperlinkPage);
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	public void setBookmarkLevel(int bookmarkLevel)
	{
		this.bookmarkLevel = bookmarkLevel;
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public byte getBorder()
	{
		return JRPenUtil.getPenFromLinePen(lineBox.getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Byte getOwnBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBorder(byte border)
	{
		JRPenUtil.setLinePenFromPen(border, lineBox.getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBorder(Byte border)
	{
		JRPenUtil.setLinePenFromPen(border, lineBox.getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getBorderColor()
	{
		return lineBox.getPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getOwnBorderColor()
	{
		return lineBox.getPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBorderColor(Color borderColor)
	{
		lineBox.getPen().setLineColor(borderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public int getPadding()
	{
		return lineBox.getPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Integer getOwnPadding()
	{
		return lineBox.getOwnPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setPadding(int padding)
	{
		lineBox.setPadding(padding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setPadding(Integer padding)
	{
		lineBox.setPadding(padding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public byte getTopBorder()
	{
		return JRPenUtil.getPenFromLinePen(lineBox.getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Byte getOwnTopBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setTopBorder(byte topBorder)
	{
		JRPenUtil.setLinePenFromPen(topBorder, lineBox.getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setTopBorder(Byte topBorder)
	{
		JRPenUtil.setLinePenFromPen(topBorder, lineBox.getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getTopBorderColor()
	{
		return lineBox.getTopPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getOwnTopBorderColor()
	{
		return lineBox.getTopPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
		lineBox.getTopPen().setLineColor(topBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public int getTopPadding()
	{
		return lineBox.getTopPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Integer getOwnTopPadding()
	{
		return lineBox.getOwnTopPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setTopPadding(int topPadding)
	{
		lineBox.setTopPadding(topPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setTopPadding(Integer topPadding)
	{
		lineBox.setTopPadding(topPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public byte getLeftBorder()
	{
		return JRPenUtil.getPenFromLinePen(lineBox.getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Byte getOwnLeftBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setLeftBorder(byte leftBorder)
	{
		JRPenUtil.setLinePenFromPen(leftBorder, lineBox.getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setLeftBorder(Byte leftBorder)
	{
		JRPenUtil.setLinePenFromPen(leftBorder, lineBox.getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getLeftBorderColor()
	{
		return lineBox.getLeftPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getOwnLeftBorderColor()
	{
		return lineBox.getLeftPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
		lineBox.getLeftPen().setLineColor(leftBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public int getLeftPadding()
	{
		return lineBox.getLeftPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Integer getOwnLeftPadding()
	{
		return lineBox.getOwnLeftPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setLeftPadding(int leftPadding)
	{
		lineBox.setLeftPadding(leftPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		lineBox.setLeftPadding(leftPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public byte getBottomBorder()
	{
		return JRPenUtil.getPenFromLinePen(lineBox.getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Byte getOwnBottomBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBottomBorder(byte bottomBorder)
	{
		JRPenUtil.setLinePenFromPen(bottomBorder, lineBox.getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
		JRPenUtil.setLinePenFromPen(bottomBorder, lineBox.getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getBottomBorderColor()
	{
		return lineBox.getBottomPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getOwnBottomBorderColor()
	{
		return lineBox.getBottomPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
		lineBox.getBottomPen().setLineColor(bottomBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public int getBottomPadding()
	{
		return lineBox.getBottomPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Integer getOwnBottomPadding()
	{
		return lineBox.getOwnBottomPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBottomPadding(int bottomPadding)
	{
		lineBox.setBottomPadding(bottomPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		lineBox.setBottomPadding(bottomPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public byte getRightBorder()
	{
		return JRPenUtil.getPenFromLinePen(lineBox.getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Byte getOwnRightBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setRightBorder(byte rightBorder)
	{
		JRPenUtil.setLinePenFromPen(rightBorder, lineBox.getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setRightBorder(Byte rightBorder)
	{
		JRPenUtil.setLinePenFromPen(rightBorder, lineBox.getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getRightBorderColor()
	{
		return lineBox.getRightPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Color getOwnRightBorderColor()
	{
		return lineBox.getRightPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
		lineBox.getRightPen().setLineColor(rightBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public int getRightPadding()
	{
		return lineBox.getRightPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public Integer getOwnRightPadding()
	{
		return lineBox.getOwnRightPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setRightPadding(int rightPadding)
	{
		lineBox.setRightPadding(rightPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getBox()}
	 */
	public void setRightPadding(Integer rightPadding)
	{
		lineBox.setRightPadding(rightPadding);
	}

	/**
	 *
	 */
	public JRReportFont getReportFont()
	{
		return reportFont;
	}

	/**
	 *
	 */
	public void setReportFont(JRReportFont reportFont)
	{
		this.reportFont = reportFont;
	}

	/**
	 *
	 */
	public String getFontName()
	{
		return JRStyleResolver.getFontName(this);
	}

	/**
	 *
	 */
	public String getOwnFontName()
	{
		return fontName;
	}

	/**
	 *
	 */
	public void setFontName(String fontName)
	{
		this.fontName = fontName;
	}


	/**
	 *
	 */
	public boolean isBold()
	{
		return JRStyleResolver.isBold(this);
	}

	/**
	 *
	 */
	public Boolean isOwnBold()
	{
		return isBold;
	}

	/**
	 *
	 */
	public void setBold(boolean isBold)
	{
		setBold(isBold ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setBold method which allows also to reset
	 * the "own" isBold property.
	 */
	public void setBold(Boolean isBold)
	{
		this.isBold = isBold;
	}


	/**
	 *
	 */
	public boolean isItalic()
	{
		return JRStyleResolver.isItalic(this);
	}

	/**
	 *
	 */
	public Boolean isOwnItalic()
	{
		return isItalic;
	}

	/**
	 *
	 */
	public void setItalic(boolean isItalic)
	{
		setItalic(isItalic ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setItalic method which allows also to reset
	 * the "own" isItalic property.
	 */
	public void setItalic(Boolean isItalic)
	{
		this.isItalic = isItalic;
	}

	/**
	 *
	 */
	public boolean isUnderline()
	{
		return JRStyleResolver.isUnderline(this);
	}

	/**
	 *
	 */
	public Boolean isOwnUnderline()
	{
		return isUnderline;
	}

	/**
	 *
	 */
	public void setUnderline(boolean isUnderline)
	{
		setUnderline(isUnderline ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setUnderline method which allows also to reset
	 * the "own" isUnderline property.
	 */
	public void setUnderline(Boolean isUnderline)
	{
		this.isUnderline = isUnderline;
	}

	/**
	 *
	 */
	public boolean isStrikeThrough()
	{
		return JRStyleResolver.isStrikeThrough(this);
	}

	/**
	 *
	 */
	public Boolean isOwnStrikeThrough()
	{
		return isStrikeThrough;
	}

	/**
	 *
	 */
	public void setStrikeThrough(boolean isStrikeThrough)
	{
		setStrikeThrough(isStrikeThrough ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setStrikeThrough method which allows also to reset
	 * the "own" isStrikeThrough property.
	 */
	public void setStrikeThrough(Boolean isStrikeThrough)
	{
		this.isStrikeThrough = isStrikeThrough;
	}

	/**
	 *
	 */
	public int getFontSize()
	{
		return JRStyleResolver.getFontSize(this);
	}

	/**
	 *
	 */
	public Integer getOwnFontSize()
	{
		return fontSize;
	}

	/**
	 *
	 */
	public void setFontSize(int fontSize)
	{
		setFontSize(new Integer(fontSize));
	}

	/**
	 * Alternative setSize method which allows also to reset
	 * the "own" size property.
	 */
	public void setFontSize(Integer fontSize)
	{
		this.fontSize = fontSize;
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
		setFontSize(size);
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Integer)}.
	 */
	public void setSize(Integer size)
	{
		setFontSize(size);
	}

	/**
	 *
	 */
	public String getPdfFontName()
	{
		return JRStyleResolver.getPdfFontName(this);
	}

	/**
	 *
	 */
	public String getOwnPdfFontName()
	{
		return pdfFontName;
	}

	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName)
	{
		this.pdfFontName = pdfFontName;
	}


	/**
	 *
	 */
	public String getPdfEncoding()
	{
		return JRStyleResolver.getPdfEncoding(this);
	}

	/**
	 *
	 */
	public String getOwnPdfEncoding()
	{
		return pdfEncoding;
	}

	/**
	 *
	 */
	public void setPdfEncoding(String pdfEncoding)
	{
		this.pdfEncoding = pdfEncoding;
	}


	/**
	 *
	 */
	public boolean isPdfEmbedded()
	{
		return JRStyleResolver.isPdfEmbedded(this);
	}

	/**
	 *
	 */
	public Boolean isOwnPdfEmbedded()
	{
		return isPdfEmbedded;
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
		this.isPdfEmbedded = isPdfEmbedded;
	}

	
	public String getPattern()
	{
		return pattern;
	}

	
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	
	public String getValueClassName()
	{
		return valueClassName;
	}

	
	public void setValueClassName(String valueClassName)
	{
		this.valueClassName = valueClassName;
	}

	
	public String getFormatFactoryClass()
	{
		return formatFactoryClass;
	}

	
	public void setFormatFactoryClass(String formatFactoryClass)
	{
		this.formatFactoryClass = formatFactoryClass;
	}

	
	public String getLocaleCode()
	{
		return localeCode;
	}

	
	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	
	public String getTimeZoneId()
	{
		return timeZoneId;
	}

	
	public void setTimeZoneId(String timeZoneId)
	{
		this.timeZoneId = timeZoneId;
	}

	
	public JRPrintHyperlinkParameters getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}

	
	public void setHyperlinkParameters(JRPrintHyperlinkParameters hyperlinkParameters)
	{
		this.hyperlinkParameters = hyperlinkParameters;
	}

	
	/**
	 * Adds a custom hyperlink parameter.
	 * 
	 * @param parameter the parameter to add
	 * @see #getHyperlinkParameters()
	 * @see JRPrintHyperlinkParameters#addParameter(JRPrintHyperlinkParameter)
	 */
	public void addHyperlinkParameter(JRPrintHyperlinkParameter parameter)
	{
		if (hyperlinkParameters == null)
		{
			hyperlinkParameters = new JRPrintHyperlinkParameters();
		}
		hyperlinkParameters.addParameter(parameter);
	}
	
	
	public String getLinkType()
	{
		return linkType;
	}


	
	public void setLinkType(String linkType)
	{
		this.linkType = linkType;
	}
	
	
	protected void normalizeLinkType()
	{
		if (linkType == null)
		{
			 linkType = JRHyperlinkHelper.getLinkType(hyperlinkType);
		}
		hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NULL;
	}


	
	public String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
	}


	
	public void setHyperlinkTooltip(String hyperlinkTooltip)
	{
		this.hyperlinkTooltip = hyperlinkTooltip;
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}


	/**
	 * These fields are only for serialization backward compatibility.
	 */
	private Byte border = null;
	private Byte topBorder = null;
	private Byte leftBorder = null;
	private Byte bottomBorder = null;
	private Byte rightBorder = null;
	private Color borderColor = null;
	private Color topBorderColor = null;
	private Color leftBorderColor = null;
	private Color bottomBorderColor = null;
	private Color rightBorderColor = null;
	private Integer padding = null;
	private Integer topPadding = null;
	private Integer leftPadding = null;
	private Integer bottomPadding = null;
	private Integer rightPadding = null;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (lineBox == null)
		{
			lineBox = new JRBaseLineBox(this);
			JRBoxUtil.setToBox(
				border,
				topBorder,
				leftBorder,
				bottomBorder,
				rightBorder,
				borderColor,
				topBorderColor,
				leftBorderColor,
				bottomBorderColor,
				rightBorderColor,
				padding,
				topPadding,
				leftPadding,
				bottomPadding,
				rightPadding,
				lineBox
				);
			border = null;
			topBorder = null;
			leftBorder = null;
			bottomBorder = null;
			rightBorder = null;
			borderColor = null;
			topBorderColor = null;
			leftBorderColor = null;
			bottomBorderColor = null;
			rightBorderColor = null;
			padding = null;
			topPadding = null;
			leftPadding = null;
			bottomPadding = null;
			rightPadding = null;
		}

		normalizeLinkType();
	}
}
