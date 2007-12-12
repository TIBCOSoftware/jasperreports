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

import java.awt.Color;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphicElement.java 1229 2006-04-19 10:27:35Z teodord $
 */
public interface JRPen
{

	/**
	 * Contant useful for specifying solid line style.
	 */
	public static final byte LINE_STYLE_SOLID = 0;

	/**
	 * Contant useful for specifying dashed line style.
	 */
	public static final byte LINE_STYLE_DASHED = 1;

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
	 * Gets the line style used for this pen.
	 * @return one of the line style constants in this class
	 */
	public Byte getLineStyle();

	public Byte getOwnLineStyle();

	/**
	 * Sets the line style.
	 * @param lineStyle one of the line style constants in this class
	 */
	public void setLineStyle(byte lineStyle);

	public void setLineStyle(Byte lineStyle);

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
