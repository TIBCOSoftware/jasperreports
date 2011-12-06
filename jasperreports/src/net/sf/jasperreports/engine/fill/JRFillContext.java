/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.jasperreports.engine.Deduplicable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.util.DeduplicableRegistry;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRFontUtil;

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
	private Map<Object,JRPrintImage> loadedImages;
	private Map<Object,JasperReport> loadedSubreports;
	private Map<Object,JRTemplate> loadedTemplates;
	private DeduplicableRegistry deduplicableRegistry;
	private boolean usingVirtualizer;
	private JRPrintPage printPage;
	private boolean ignorePagination;
	private JRQueryExecuter queryExecuter;

	private JRVirtualizationContext virtualizationContext;
	
	private FormatFactory masterFormatFactory;
	private Locale masterLocale;
	private TimeZone masterTimeZone;
	
	private final AtomicInteger fillerIdSeq = new AtomicInteger();
	private final AtomicInteger fillElementSeq = new AtomicInteger();

	
	/**
	 * Constructs a fill context.
	 */
	public JRFillContext()
	{
		loadedImages = new HashMap<Object,JRPrintImage>();
		loadedSubreports = new HashMap<Object,JasperReport>();
		loadedTemplates = new HashMap<Object,JRTemplate>();
		deduplicableRegistry = new DeduplicableRegistry();
		
		JRFontUtil.resetThreadMissingFontsCache();
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
		return loadedImages.get(source); 
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
		return loadedSubreports.get(source); 
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
	 * @return <code>true</code> if and only if a virtualizer is used
	 * @see #setUsingVirtualizer(boolean)
	 * @see net.sf.jasperreports.engine.JRParameter#REPORT_VIRTUALIZER
	 */
	public boolean isUsingVirtualizer()
	{
		return usingVirtualizer;
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
	 * @return <code>true</code> if and only if there is a running query and it has been canceled.
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
		return loadedTemplates.get(source); 
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
	
	/**
	 * Search for a duplicate of a given object in the fill context, and add the object
	 * to the context if no duplicate found.
	 * 
	 * @param object the object to be searched or added
	 * @return a duplicate of the object if found, or the passed object if not
	 */
	public <T extends Deduplicable> T deduplicate(T object)
	{
		return deduplicableRegistry.deduplicate(object);
	}

	/**
	 * Generates a fresh fill element Id.
	 * 
	 * This method is called once by each fill element, and the returned Id is used
	 * for the generated print elements.
	 * 
	 * @return
	 * @see JRPrintElement#getSourceElementId()
	 */
	public int generateFillElementId() 
	{
		return fillElementSeq.incrementAndGet();
	}
	
	protected int generatedFillerId()
	{
		return fillerIdSeq.incrementAndGet();
	}
}
