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
 * @version $Id$
 */
public interface JRPrintElement extends JRStyleContainer
{


	/**
	 *
	 */
	public void setStyle(JRStyle style);
	
	/**
	 *
	 */
	public byte getMode();
	
	/**
	 *
	 */
	public Byte getOwnMode();
	
	/**
	 *
	 */
	public void setMode(byte mode);
	
	/**
	 *
	 */
	public void setMode(Byte mode);
	
	/**
	 *
	 */
	public int getX();
	
	/**
	 *
	 */
	public void setX(int x);
	
	/**
	 *
	 */
	public int getY();
	
	/**
	 *
	 */
	public void setY(int y);
	
	/**
	 *
	 */
	public int getWidth();
	
	/**
	 *
	 */
	public void setWidth(int width);
	
	/**
	 *
	 */
	public int getHeight();
	
	/**
	 *
	 */
	public void setHeight(int height);
	
	/**
	 *
	 */
	public Color getForecolor();
	
	/**
	 *
	 */
	public Color getOwnForecolor();
	
	/**
	 *
	 */
	public void setForecolor(Color color);
	
	/**
	 *
	 */
	public Color getBackcolor();

	/**
	 *
	 */
	public Color getOwnBackcolor();

	/**
	 *
	 */
	public void setBackcolor(Color color);
	

}
