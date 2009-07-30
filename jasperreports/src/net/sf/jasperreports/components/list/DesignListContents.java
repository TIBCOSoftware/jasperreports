/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.components.list;

import net.sf.jasperreports.engine.design.JRDesignElementGroup;

/**
 * {@link ListContents} implementation used at report design time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class DesignListContents extends JRDesignElementGroup implements
		ListContents
{
	
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_HEIGHT = "height";
	
	private int height;

	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Sets the list contents height.
	 * 
	 * @param height the list contents height
	 * @see #getHeight()
	 */
	public void setHeight(int height)
	{
		int old = this.height;
		this.height = height;
		getEventSupport().firePropertyChange(PROPERTY_HEIGHT, old, this.height);
	}

}
