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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRPrintText extends JRPrintElement, JRAlignment, JRPrintAnchor, JRPrintHyperlink, JRBox, JRFont, JRText
{


	/**
	 *
	 */
	public static final byte RUN_DIRECTION_LTR = 0;
	public static final byte RUN_DIRECTION_RTL = 1;

	
	/**
	 * Returns the possibly truncated (when {@link #getTextTruncateIndex()} is not null) text of this object.
	 * 
	 * @return the text of this object
	 * @see #getFullText()
	 */
	public String getText();
		
	/**
	 * Set the text for this object.
	 * 
	 * @param text the text
	 * @see #setTextTruncateIndex(Integer)
	 */
	public void setText(String text);

	/**
	 * Returns the index to which this object's text is to be truncated.
	 * 
	 * This index is usually set at report fill time when the engine is instructed
	 * to keep the full text in the print text object.
	 * 
	 * @return the index to which this object's text is to be truncated
	 * @see JRTextElement#PROPERTY_PRINT_KEEP_FULL_TEXT
	 */
	public Integer getTextTruncateIndex();

	/**
	 * Sets the index to which this object's text is to be truncated.
	 * 
	 * The test is truncated when {@link #getText()} or {@link #getStyledText(JRStyledTextAttributeSelector)}
	 * are called.
	 * 
	 * @param index the index to which this object's text is to be truncated
	 */
	public void setTextTruncateIndex(Integer index);
	
	/**
	 * Returns the full (not truncated) text of this object.
	 * 
	 * @return the full text of this object
	 * @see #getText()
	 * @see #getTextTruncateIndex()
	 */
	public String getFullText();
	
	/**
	 * Returns the styled text for this object.
	 * 
	 * The text is truncated according to {@link #getText()}.
	 * 
	 * @param attributeSelector the styled text attribute selector
	 * @return the possibly truncated styled text for this object
	 */
	public JRStyledText getStyledText(JRStyledTextAttributeSelector attributeSelector);
	
	/**
	 * Returns the full styled text of this object.
	 * 
	 * @param attributeSelector the styled text attribute selector
	 * @return the full styled text of this object
	 * @see #getFullText()
	 */
	public JRStyledText getFullStyledText(JRStyledTextAttributeSelector attributeSelector);
	
	/**
	 *
	 */
	public float getLineSpacingFactor();
		
	/**
	 *
	 */
	public void setLineSpacingFactor(float lineSpacingFactor);

	/**
	 *
	 */
	public float getLeadingOffset();
		
	/**
	 *
	 */
	public void setLeadingOffset(float leadingOffset);

	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignment()}.
	 */
	public byte getTextAlignment();
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(byte)}.
	 */
	public void setTextAlignment(byte horizontalAlignment);
		
	/**
	 *
	 */
	public Byte getOwnRotation();
		
	/**
	 *
	 */
	public void setRotation(byte rotation);
		
	/**
	 *
	 */
	public void setRotation(Byte rotation);
		
	/**
	 *
	 */
	public byte getRunDirection();
		
	/**
	 *
	 */
	public void setRunDirection(byte rotation);
		
	/**
	 *
	 */
	public float getTextHeight();
		
	/**
	 *
	 */
	public void setTextHeight(float textHeight);
		
	/**
	 *
	 */
	public Byte getOwnLineSpacing();
		
	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing);
		
	/**
	 *
	 */
	public void setLineSpacing(Byte lineSpacing);
		
	/**
	 *
	 */
	public Boolean isOwnStyledText();
		
	/**
	 *
	 */
	public void setStyledText(boolean isStyledText);
		
	/**
	 *
	 */
	public void setStyledText(Boolean isStyledText);
		
	/**
	 * @deprecated
	 */
	public JRBox getBox();

	/**
	 * @deprecated
	 */
	public void setBox(JRBox box);

	/**
	 * @deprecated
	 */
	public JRFont getFont();

	/**
	 * @deprecated
	 */
	public void setFont(JRFont font);
	
	/**
	 * Returns the type of the value which was used to generate this text.
	 * <p>
	 * {@link JRTextField Text fields} that have a non-<code>String</code> expression
	 * save the value type using this attribute.  This information can be used by
	 * exporters to treat numerical or date texts (for instance) in a special manner.
	 * </p>
	 * 
	 * @return the type of the original value used to generate the text
	 */
	public String getValueClassName();
	
	/**
	 * Returns the pattern used to format a value that was the source of this text. 
	 * <p>
	 * The pattern can be used to parse the text back to its source value.
	 * </p>
	 * 
	 * @return the pattern used to format this text's source value
	 * @see #getValueClassName()
	 */
	public String getPattern();
	

	/**
	 * Returns the name of the class implementing the {@link net.sf.jasperreports.engine.util.FormatFactory FormatFactory}
	 * interface to use with this text element, in case it is not the same as the one for the overall document.
	 */
	public String getFormatFactoryClass();
	
	
	/**
	 * Returns the code of the <code>java.util.Locale</code> which was used 
	 * while formatting the source value of the text.
	 * <p>
	 * The code is created using the {@link java.util.Locale#toString() java.util.Locale.toString()}
	 * convention.
	 * </p>
	 * <p>
	 * When this attribute is null, the locale returned by
	 * {@link JasperPrint#getLocaleCode() JasperPrint.getLocaleCode()} is used.
	 * This way, the locale is specified in a single place when all the (or many ) texts from a print object
	 * were formatted using the same locale.
	 * </p>
	 * 
	 * @return the code of the <code>java.util.Locale</code> used when formatting this text's source value
	 */
	public String getLocaleCode();
	
	
	/**
	 * Returns the {@link java.util.TimeZone#getID() ID} of the <code>java.util.TimeZone</code>
	 * used to format this text's date source value.
	 * <p>
	 * When this attribute is null, the time zone returned by
	 * {@link JasperPrint#getTimeZoneId() JasperPrint.getTimeZoneId()} is used.
	 * </p>
	 * 
	 * @return the {@link java.util.TimeZone#getID() ID} of the <code>java.util.TimeZone</code>
	 * used to format this text's date source value
	 */
	public String getTimeZoneId();
}
