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
package net.sf.jasperreports.engine;

import java.awt.Color;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @deprecated Replaced by {@link JRLineBox}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRBox extends JRStyleContainer
{


	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public Byte getOwnBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public Color getOwnBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnPadding()}
	 */
	public Integer getOwnPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public Byte getOwnTopBorder();
	
	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public Color getOwnTopBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnTopPadding()}
	 */
	public Integer getOwnTopPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public Byte getOwnLeftBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public Color getOwnLeftBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnLeftPadding()}
	 */
	public Integer getOwnLeftPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public Byte getOwnBottomBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public Color getOwnBottomBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnBottomPadding()}
	 */
	public Integer getOwnBottomPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public Byte getOwnRightBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public Color getOwnRightBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnRightPadding()}
	 */
	public Integer getOwnRightPadding();

	
}
