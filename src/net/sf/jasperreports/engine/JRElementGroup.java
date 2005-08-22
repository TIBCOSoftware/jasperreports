/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.util.List;


/**
 * Groups several report elements. Report elements placed in any report section can be arranged in multiple
 * nested groups. The only reason you might have for grouping your elements is to be able to customize the
 * stretch behavior of the report elements.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRElementGroup extends JRChild
{


	/**
	 * Gets a list of all direct children elements or elements groups.
	 */
	public List getChildren();

	/**
	 * Gets the parent element group.
	 * @return an instance of this class, or null if this is the root group.
	 */
	public JRElementGroup getElementGroup();

	/**
	 * Gets an array containing all the elements and element groups in the hierarchy.
	 */
	public JRElement[] getElements();


	/**
	 * Gets an element from this group, based on its element key.
	 */
	public JRElement getElementByKey(String key);


}
