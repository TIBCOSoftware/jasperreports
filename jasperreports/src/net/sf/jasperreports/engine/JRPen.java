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

import java.awt.Color;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRPen
{

	/**
	 * @deprecated Replaced by {@link LineStyleEnum#SOLID}.
	 */
	public static final byte LINE_STYLE_SOLID = 0;

	/**
	 * @deprecated Replaced by {@link LineStyleEnum#DASHED}.
	 */
	public static final byte LINE_STYLE_DASHED = 1;

	/**
	 * @deprecated Replaced by {@link LineStyleEnum#DOTTED}.
	 */
	public static final byte LINE_STYLE_DOTTED = 2;

	/**
	 * @deprecated Replaced by {@link LineStyleEnum#DOUBLE}.
	 */
	public static final byte LINE_STYLE_DOUBLE = 3;

	public static final Float LINE_WIDTH_0 = new Float(0f);
	public static final Float LINE_WIDTH_1 = new Float(1f);


	/**
	 * 
	 */
	public JRStyleContainer getStyleContainer();

	/**
	 * 
	 */
	public JRPen clone(JRPenContainer penContainer);

	
	/**
	 * Gets the line width used for this pen.
	 * @return line width
	 */
	public Float getLineWidth();

	public Float getOwnLineWidth();

	/**
	 * Sets the line width.
	 * @param lineWidth the line width
	 */
	public void setLineWidth(float lineWidth);

	public void setLineWidth(Float lineWidth);

	/**
	 * @deprecated Replaced by {@link getLineStyleValue()}.
	 */
	public Byte getLineStyle();

	/**
	 * @deprecated Replaced by {@link getOwnLineStyleValue()}.
	 */
	public Byte getOwnLineStyle();

	/**
	 * @deprecated Replaced by {@link setLineStyle(LineStyleEnum)}.
	 */
	public void setLineStyle(byte lineStyle);

	/**
	 * @deprecated Replaced by {@link setLineStyle(LineStyleEnum)}.
	 */
	public void setLineStyle(Byte lineStyle);

	//TODO: uncomment these below

//	/**
//	 * Indicates the line style used for this pen.
//	 * @return a value representing one of the line style constants in {@link LineStyleEnum}
//	 */
//	public LineStyleEnum getLineStyleValue();
//	
//	/**
//	 * Indicates the line style used for this pen.
//	 * @return a value representing one of the line style constants in {@link LineStyleEnum}
//	 */
//	public LineStyleEnum getOwnLineStyleValue();
//	
//	/**
//	 * Specifies the line style.
//	 * @param lineStyleEnum a value representing one of the line style constants in {@link LineStyleEnum}
//	 */
//	public void setLineStyle(LineStyleEnum lineStyleEnum);
	
	/**
	 * Gets the line color.
	 */
	public Color getLineColor();

	public Color getOwnLineColor();

	/**
	 * Sets the line color.
	 */
	public void setLineColor(Color color);

}
