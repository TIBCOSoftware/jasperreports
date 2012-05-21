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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.RotationEnum;

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
	public static final String MARKUP_NONE = "none";
	public static final String MARKUP_STYLED_TEXT = "styled";
	public static final String MARKUP_HTML = "html";
	public static final String MARKUP_RTF = "rtf";

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
	 * @deprecated Replaced by {@link JRParagraph#getLineSpacing()}.
	 */
	public LineSpacingEnum getLineSpacingValue();
	
	/**
	 * @deprecated Replaced by {@link JRParagraph#getOwnLineSpacing()}.
	 */
	public LineSpacingEnum getOwnLineSpacingValue();
	
	/**
	 * @deprecated Replaced by {@link JRParagraph#setLineSpacing(LineSpacingEnum)}.
	 */
	public void setLineSpacing(LineSpacingEnum lineSpacingEnum);
	
	/**
	 * Returns the text markup.
	 */
	public String getMarkup();

	public String getOwnMarkup();
	
	public void setMarkup(String markup);

	int getFontSize();
	
}
