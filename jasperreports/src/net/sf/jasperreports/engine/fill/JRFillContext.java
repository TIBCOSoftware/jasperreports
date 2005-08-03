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
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;

/**
 * Context class shared by all the fillers involved in a report (master and subfillers).
 * <p>
 * The context is created by the master filler and inherited by the subfillers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see net.sf.jasperreports.engine.fill.JRBaseFiller
 */
public class JRFillContext
{
	private Map loadedImages;
	private boolean usingVirtualizer = false;
	private boolean perPageBoundElements = false;
	private JRPrintPage printPage = null;
	private boolean ignorePagination = false;

	
	/**
	 * Constructs a fill context.
	 */
	public JRFillContext()
	{
		loadedImages = new HashMap();
	}
	
	
	/**
	 * Checks whether an image given by location has already been loaded and cached.
	 * 
	 * @param location the location
	 * @return whether the image has been cached
	 * @see #getLoadedImage(String)
	 * @see #registerLoadedImage(String, JRPrintImage)
	 */
	public boolean hasLoadedImage(String location)
	{
		return loadedImages.containsKey(location); 
	}
	
	
	/**
	 * Gets a cached image.
	 * 
	 * @param location the location of the image
	 * @return the cached image
	 * @see #registerLoadedImage(String, JRPrintImage)
	 */
	public JRPrintImage getLoadedImage(String location)
	{
		return (JRPrintImage) loadedImages.get(location); 
	}
	
	
	/**
	 * Registers an image loaded from a location.
	 * <p>
	 * The image is cached for further use.
	 * 
	 * @param location the location where the image was loaded from
	 * @param image the loaded image
	 * @see #getLoadedImage(String)
	 */
	public void registerLoadedImage(String location, JRPrintImage image)
	{
		loadedImages.put(location, image);
	}

	
	/**
	 * Sets the flag indicating whether a virtualizer is used by the filling process.
	 * 
	 * @param usingVirtualizer whether virtualization is used
	 * @see #isUsingVirtualizer()
	 */
	public void setUsingVirtualizer(boolean usingVirtualizer)
	{
		this.usingVirtualizer = usingVirtualizer;
	}
	
	
	/**
	 * Decides whether virtualization is used by the filling process.
	 * 
	 * @return <code>true</code> iff a virtualizer is used
	 * @see #setUsingVirtualizer(boolean)
	 * @see net.sf.jasperreports.engine.JRParameter#REPORT_VIRTUALIZER
	 */
	public boolean isUsingVirtualizer()
	{
		return usingVirtualizer;
	}

	
	/**
	 * Sets the flag indicating whether fillers should keep per page bound
	 * element maps.
	 * 
	 * @param perPageBoundElements the value of the flag
	 * @see #isPerPageBoundElements()
	 */
	public void setPerPageBoundElements(boolean perPageBoundElements)
	{
		this.perPageBoundElements = perPageBoundElements;
	}

	
	/**
	 * Decides whether fillers should keep per page bound element maps.
	 * 
	 * @return <code>true</code> iff fillers should keep per page bound element maps
	 * @see #setPerPageBoundElements(boolean)
	 */
	public boolean isPerPageBoundElements()
	{
		return perPageBoundElements;
	}
	
	
	/**
	 * Sets the current master print page.
	 * 
	 * @param page the master print page
	 * @see #getPrintPage()
	 */
	public void setPrintPage(JRPrintPage page)
	{
		printPage  = page;
	}
	
	
	/**
	 * Returns the current master print page.
	 *  
	 * @return the current master print page
	 * @see #setPrintPage(JRPrintPage)
	 */
	public JRPrintPage getPrintPage()
	{
		return printPage;
	}
	
	
	/**
	 * Sets the flag that decides whether pagination should be ignored during filling.
	 * 
	 * @param ignorePagination
	 * @see #isIgnorePagination()
	 */
	public void setIgnorePagination(boolean ignorePagination)
	{
		this.ignorePagination  = ignorePagination;
	}
	
	
	/**
	 * Decides whether the filling should ignore pagination.
	 *  
	 * @return whether the filling should ignore pagination
	 * @see #setIgnorePagination(boolean)
	 * @see net.sf.jasperreports.engine.JRParameter#IS_IGNORE_PAGINATION
	 */
	public boolean isIgnorePagination()
	{
		return ignorePagination;
	}
}
