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


/**
 * An abstract representation of a break element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRLine.java 1229 2006-04-19 13:27:35 +0300 (Wed, 19 Apr 2006) teodord $
 */
public interface JRBreak extends JRElement
{


	/**
	 * Constant used for specifying that the break type.
	 */
	public static final byte TYPE_PAGE = 1;

	/**
	 * Constant used for specifying that the break type.
	 */
	public static final byte TYPE_COLUMN = 2;


	/**
	 * Gets the break type.
	 * @return one of the type constants
	 */
	public byte getType();

	/**
	 * Sets the break type.
	 * @param type one of the type constants
	 */
	public void setType(byte type);


}
