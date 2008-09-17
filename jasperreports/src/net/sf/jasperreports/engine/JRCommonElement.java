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
 * An abstract representation of a report element. All report elements implement this interface. The interface contains
 * constants and methods that apply to all report elements.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRElement.java 1959 2007-11-14 15:08:22Z lucianc $
 */
public interface JRCommonElement extends JRStyleContainer
{

	/**
	 * Returns the string value that uniquely identifies the element.
	 */
	public String getKey();

	/**
	 * Returns the element transparency mode.
	 * The default value depends on the type of the report element. Graphic elements like rectangles and lines are
	 * opaque by default, but the images are transparent. Both static texts and text fields are transparent
	 * by default, and so are the subreport elements.
	 * @return MODE_OPAQUE or MODE_TRANSPARENT
	 */
	public byte getMode();
	
	public Byte getOwnMode();

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
	public Color getBackcolor();
	
	/**
	 *
	 */
	public Color getOwnBackcolor();
	
}
