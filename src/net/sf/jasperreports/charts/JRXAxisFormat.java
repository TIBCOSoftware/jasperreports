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
package net.sf.jasperreports.charts;

import java.awt.Color;

import net.sf.jasperreports.engine.JRFont;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBubblePlot.java 1384 2006-09-06 00:27:41 +0300 (Wed, 06 Sep 2006) bklawans $
 */
public interface JRXAxisFormat
{
	
	/**
	 * 
	 */
	public JRFont getXAxisLabelFont();
	
	/**
	 * 
	 */
	public Color getXAxisLabelColor();
	
	/**
	 * 
	 */
	public Color getOwnXAxisLabelColor();
	
	/**
	 * 
	 */
	public JRFont getXAxisTickLabelFont();
	
	/**
	 * 
	 */
	public Color getXAxisTickLabelColor();

	/**
	 * 
	 */
	public Color getOwnXAxisTickLabelColor();

	/**
	 * 
	 */
	public String getXAxisTickLabelMask();

	/**
	 * 
	 */
	public Color getXAxisLineColor();

	/**
	 * 
	 */
	public Color getOwnXAxisLineColor();

}
