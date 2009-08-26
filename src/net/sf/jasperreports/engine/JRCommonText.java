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
 *(at your option) any later version.
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

/**
 * Common interface of design and print text elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCommonText extends JRCommonElement, JRBoxContainer
{
	public static final String MARKUP_NONE = "none";
	public static final String MARKUP_STYLED_TEXT = "styled";
	public static final String MARKUP_HTML = "html";
	public static final String MARKUP_RTF = "rtf";

	int getWidth();
	
	int getHeight();
	
	/**
	 * Gets the text rotation.
	 * @return a value representing one of the rotation constants in this class
	 */
	byte getRotation();
	
	public Byte getOwnRotation();

	/**
	 * Gets the line spacing.
	 * @return a value representing one of the line spacing constants in this class
	 */
	byte getLineSpacing();

	public Byte getOwnLineSpacing();
	
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
	String getMarkup();

	public String getOwnMarkup();
	
	int getFontSize();
	
}
