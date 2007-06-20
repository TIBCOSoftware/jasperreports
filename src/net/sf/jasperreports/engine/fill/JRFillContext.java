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
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.util.FormatFactory;

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
	private Map loadedSubreports;
	private Map loadedTemplates;
	private boolean usingVirtualizer = false;
	private boolean perPageBoundElements = false;
	private JRPrintPage printPage = null;
	private boolean ignorePagination = false;
	private JRQueryExecuter queryExecuter;

	private JRVirtualizationContext virtualizationContext;
	
	private FormatFactory masterFormatFactory;
	private Locale masterLocale;
	private TimeZone masterTimeZone;

	
	/**
	 * Constructs a fill context.
	 */
	public JRFillContext()
	{
		loadedImages = new HashMap();
		loadedSubreports = new HashMap();
		loadedTemplates = new HashMap();
	}
	
	
	/**
	 * Checks whether an image given by source has already been loaded and cached.
	 * 
	 * @param source the source of the image
	 * @return whether the image has been cached
	 * @see #getLoadedImage(Object)
	 * @see #registerLoadedImage(Object, JRPrintImage)
	 */
	public boolean hasLoadedImage(Object source)
	{
		return loadedImages.containsKey(source); 
	}
	
	
	/**
	 * Gets a cached image.
	 * 
	 * @param source the source of the image
	 * @return the cached image
	 * @see #registerLoadedImage(Object, JRPrintImage)
	 */
	public JRPrintImage getLoadedImage(Object source)
	{
		return (JRPrintImage) loadedImages.get(source); 
	}
	
	
	/**
	 * Registers an image loaded from a source.
	 * <p>
	 * The image is cached for further use.
	 * 
	 * @param source the source that was used to load the image
	 * @param image the loaded image
	 * @see #getLoadedImage(Object)
	 */
	public void registerLoadedImage(Object source, JRPrintImage image)
	{
		loadedImages.put(source, image);
		if (usingVirtualizer)
		{
			virtualizationContext.cacheRenderer(image);
		}
	}

	
	/**
	 * Checks whether a subreport given by source has already been loaded and cached.
	 * 
	 * @param source the source of the subreport
	 * @return whether the subreport has been cached
	 * @see #getLoadedSubreport(Object)
	 * @see #registerLoadedSubreport(Object, JasperReport)
	 */
	public boolean hasLoadedSubreport(Object source)
	{
		return loadedSubreports.containsKey(source); 
	}
	
	
	/**
	 * Gets a cached subreport.
	 * 
	 * @param source the source of the subreport
	 * @return the cached subreport
	 * @see #registerLoadedSubreport(Object, JasperReport)
	 */
	public JasperReport getLoadedSubreport(Object source)
	{
		return (JasperReport) loadedSubreports.get(source); 
	}
	
	
	/**
	 * Registers a subreport loaded from a source.
	 * <p>
	 * The subreport is cached for further use.
	 * 
	 * @param source the source that was used to load the subreport
	 * @param subreport the loaded subreport
	 * @see #getLoadedSubreport(Object)
	 */
	public void registerLoadedSubreport(Object source, JasperReport subreport)
	{
		loadedSubreports.put(source, subreport);
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
		if (usingVirtualizer && virtualizationContext == null)
		{
			virtualizationContext = new JRVirtualizationContext();
		}
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
	
	
	/**
	 * Sets the running query executer.
	 * <p>
	 * This method is called before firing the query.
	 * 
	 * @param queryExecuter the running query executer
	 */
	public synchronized void setRunningQueryExecuter(JRQueryExecuter queryExecuter)
	{
		this.queryExecuter = queryExecuter;
	}
	
	
	/**
	 * Clears the running query executer.
	 * <p>
	 * This method is called after the query has ended.
	 *
	 */
	public synchronized void clearRunningQueryExecuter()
	{
		this.queryExecuter = null;
	}
	
	
	/**
	 * Cancels the running query.
	 * 
	 * @return <code>true</code> iff there is a running query and it has been cancelled.
	 * @throws JRException
	 */
	public synchronized boolean cancelRunningQuery() throws JRException
	{
		if (queryExecuter != null)
		{
			return queryExecuter.cancelQuery();
		}
		
		return false;
	}


	/**
	 * Ensures that the master page is available when virtualization is used.
	 */
	public void ensureMasterPageAvailable()
	{
		if (usingVirtualizer)
		{
			printPage.getElements();
		}
	}
	
	
	/**
	 * Returns the virtualization context.
	 * 
	 * @return the virtualization context
	 */
	public JRVirtualizationContext getVirtualizationContext()
	{
		return virtualizationContext;
	}

	
	public FormatFactory getMasterFormatFactory()
	{
		return masterFormatFactory;
	}

	
	public void setMasterFormatFactory(FormatFactory masterFormatFactory)
	{
		this.masterFormatFactory = masterFormatFactory;
	}

	
	public Locale getMasterLocale()
	{
		return masterLocale;
	}

	
	public void setMasterLocale(Locale masterLocale)
	{
		this.masterLocale = masterLocale;
	}

	
	public TimeZone getMasterTimeZone()
	{
		return masterTimeZone;
	}

	
	public void setMasterTimeZone(TimeZone masterTimeZone)
	{
		this.masterTimeZone = masterTimeZone;
	}

	
	/**
	 * Checks whether a template given by source has already been loaded and cached.
	 * 
	 * @param source the source of the template
	 * @return whether the template has been cached
	 * @see #getLoadedTemplate(Object)
	 * @see #registerLoadedTemplate(Object, JRTemplate)
	 */
	public boolean hasLoadedTemplate(Object source)
	{
		return loadedTemplates.containsKey(source); 
	}
	
	
	/**
	 * Gets a cached template.
	 * 
	 * @param source the source of the templage
	 * @return the cached templage
	 * @see #registerLoadedTemplate(Object, JRTemplate)
	 */
	public JRTemplate getLoadedTemplate(Object source)
	{
		return (JRTemplate) loadedTemplates.get(source); 
	}
	
	
	/**
	 * Registers a template loaded from a source.
	 * <p>
	 * The template is cached for further use.
	 * 
	 * @param source the source that was used to load the template
	 * @param template the loaded templage
	 * @see #getLoadedTemplate(Object)
	 */
	public void registerLoadedTemplate(Object source, JRTemplate template)
	{
		loadedTemplates.put(source, template);
	}
}
