/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Common interface of design and print text elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCommonText extends JRCommonElement, JRBoxContainer, JRParagraphContainer
{
	/**
	 * 
	 */
	public static final String DEFAULT_TAB_STOP = JRProperties.PROPERTY_PREFIX + "default.tab.stop";
	
	public static final String MARKUP_NONE = "none";
	public static final String MARKUP_STYLED_TEXT = "styled";
	public static final String MARKUP_HTML = "html";
	public static final String MARKUP_RTF = "rtf";

	/**
	 * @deprecated Replaced by {@link #getRotationValue()}.
	 */
	byte getRotation();
	
	/**
	 * @deprecated Replaced by {@link #getOwnRotationValue()}.
	 */
	public Byte getOwnRotation();
	
	/**
	 * @deprecated Replaced by {@link #setRotation(RotationEnum)}.
	 */
	public void setRotation(byte rotation);
		
	/**
	 * @deprecated Replaced by {@link #setRotation(RotationEnum)}.
	 */
	public void setRotation(Byte rotation);

	/**
	 * Gets the text rotation.
	 * @return a value representing one of the text rotation constants in {@link RotationEnum}
	 */
	public RotationEnum getRotationValue();
	
	/**
	 * Gets the text own rotation.
	 * @return a value representing one of the text rotation constants in {@link RotationEnum}
	 */
	public RotationEnum getOwnRotationValue();
	
	/**
	 * Sets the text rotation.
	 * @param rotationEnum a value representing one of the text rotation constants in {@link RotationEnum}
	 */
	public void setRotation(RotationEnum rotationEnum);
	
	/**
	 * @deprecated Replaced by {@link #getLineSpacingValue()}.
	 */
	byte getLineSpacing();

	/**
	 * @deprecated Replaced by {@link #getOwnLineSpacingValue()}.
	 */
	public Byte getOwnLineSpacing();

	/**
	 * @deprecated Replaced by {@link #setLineSpacing(LineSpacingEnum)}.
	 */
	public void setLineSpacing(byte lineSpacing);
		
	/**
	 * @deprecated Replaced by {@link #setLineSpacing(LineSpacingEnum)}.
	 */
	public void setLineSpacing(Byte lineSpacing);
	
	/**
	 * Gets the text line spacing.
	 * @return a value representing one of the line spacing constants in {@link LineSpacingEnum}
	 */
	public LineSpacingEnum getLineSpacingValue();
	
	/**
	 * Gets the text own line spacing.
	 * @return a value representing one of the line spacing constants in {@link LineSpacingEnum}
	 */
	public LineSpacingEnum getOwnLineSpacingValue();
	
	/**
	 * Sets the text line spacing.
	 * @param lineSpacingEnum a value representing one of the line spacing constants in {@link LineSpacingEnum}
	 */
	public void setLineSpacing(LineSpacingEnum lineSpacingEnum);
	
	/**
	 * Returns true if the text can contain style tags.
	 * @deprecated Replaced by {@link #getMarkup()}
	 */
	boolean isStyledText();

	/**
	 * @deprecated Replaced by {@link #getOwnMarkup()}
	 */
	public Boolean isOwnStyledText();
	
	/**
	 * Returns the text markup.
	 */
	public String getMarkup();

	public String getOwnMarkup();
	
	public void setMarkup(String markup);

	int getFontSize();
	
}
