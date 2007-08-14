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

/*
 * Contributors:
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRPrintImage extends JRPrintGraphicElement, JRPrintAnchor, JRPrintHyperlink, JRAlignment, JRBox
{


	/**
	 *
	 */
	public JRRenderable getRenderer();
		
	/**
	 *
	 */
	public void setRenderer(JRRenderable renderer);
		
	/**
	 *
	 */
	public byte getScaleImage();

	/**
	 *
	 */
	public Byte getOwnScaleImage();

	/**
	 *
	 */
	public void setScaleImage(byte scaleImage);
	
	/**
	 *
	 */
	public void setScaleImage(Byte scaleImage);
	
	/**
	 *
	 */
	public boolean isUsingCache();

	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache);
	
	/**
	 *
	 */
	public byte getHorizontalAlignment();
		
	/**
	 *
	 */
	public Byte getOwnHorizontalAlignment();
		
	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment);
		
	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment);
		
	/**
	 *
	 */
	public byte getVerticalAlignment();
		
	/**
	 *
	 */
	public Byte getOwnVerticalAlignment();
		
	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment);
		
	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment);
		
	/**
	 *
	 */
	public boolean isLazy();

	/**
	 *
	 */
	public void setLazy(boolean isLazy);

	/**
	 *
	 */
	public byte getOnErrorType();

	/**
	 *
	 */
	public void setOnErrorType(byte onErrorType);

	/**
	 * @deprecated
	 */
	public JRBox getBox();

	/**
	 * @deprecated
	 */
	public void setBox(JRBox box);


}
