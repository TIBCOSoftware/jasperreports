/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.jasperreports.data.cache.DataCacheHandler;
import net.sf.jasperreports.data.cache.DataRecorder;
import net.sf.jasperreports.data.cache.DataSnapshot;
import net.sf.jasperreports.engine.Deduplicable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.util.DeduplicableRegistry;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.Pair;

/**
 * Context class shared by all the fillers involved in a report (master and subfillers).
 * <p>
 * The context is created by the master filler and inherited by the subfillers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see net.sf.jasperreports.engine.fill.JRBaseFiller
 */
public class JRFillContext
{
	private final BaseReportFiller masterFiller;
	
	private Map<Object,JRPrintImage> loadedImages;
	private Map<Object,JasperReport> loadedSubreports;
	private Map<Object,JRTemplate> loadedTemplates;
	private DeduplicableRegistry deduplicableRegistry;
	private boolean usingVirtualizer;
	private JRPrintPage printPage;
	private JRQueryExecuter queryExecuter;
	
	private JasperReportsContext jasperReportsContext;
	private ReportContext reportContext;
	private DataCacheHandler cacheHandler;
	private DataSnapshot dataSnapshot;
	private DataRecorder dataRecorder;
	private List<Pair<FillDatasetPosition, Object>> recordedData;

	private JRVirtualizationContext virtualizationContext;
	
	private FormatFactory masterFormatFactory;
	private Locale masterLocale;
	private TimeZone masterTimeZone;
	
	private volatile boolean canceled;
	
	private final AtomicInteger fillerIdSeq = new AtomicInteger();
	private final AtomicInteger fillElementSeq = new AtomicInteger();
	
	private Map<String, Object> fillCaches = new HashMap<String, Object>();

	
	/**
	 * Constructs a fill context.
	 */
	public JRFillContext(BaseReportFiller masterFiller)
	{
		this.masterFiller = masterFiller;
		this.jasperReportsContext = masterFiller.getJasperReportsContext();
		
		loadedImages = new HashMap<Object,JRPrintImage>();
		loadedSubreports = new HashMap<Object,JasperReport>();
		loadedTemplates = new HashMap<Object,JRTemplate>();
		deduplicableRegistry = new DeduplicableRegistry();
		
		FontUtil.getInstance(jasperReportsContext).resetThreadMissingFontsCache();
	}

	public BaseReportFiller getMasterFiller()
	{
		return masterFiller;
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
			virtualizationContext = new JRVirtualizationContext(jasperReportsContext);
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
	 * Decides whether the filling should ignore pagination.
	 *  
	 * @return whether the filling should ignore pagination
	 * @see net.sf.jasperreports.engine.JRParameter#IS_IGNORE_PAGINATION
	 * @see JRBaseFiller#isIgnorePagination()
	 */
	public boolean isIgnorePagination()
	{
		return masterFiller.isIgnorePagination();
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
	 * Returns the virtualization context.
	 * 
	 * @return the virtualization context
	 */
	public JRVirtualizationContext getVirtualizationContext()
	{
		return virtualizationContext;
	}
	
	public void lockVirtualizationContext()
	{
		if (virtualizationContext != null)
		{
			virtualizationContext.lock();
		}
	}
	
	public void unlockVirtualizationContext()
	{
		if (virtualizationContext != null)
		{
			virtualizationContext.unlock();
		}
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
	 * @return a generated Id for a fill element
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

	public ReportContext getReportContext()
	{
		return reportContext;
	}

	public void setReportContext(ReportContext reportContext)
	{
		this.reportContext = reportContext;
		
		this.cacheHandler = (DataCacheHandler) getContextParameterValue(
				DataCacheHandler.PARAMETER_DATA_CACHE_HANDLER);
		if (cacheHandler != null)
		{
			if (cacheHandler.isSnapshotPopulated())
			{
				dataSnapshot = cacheHandler.getDataSnapshot();
			}
			else if (cacheHandler.isRecordingEnabled())
			{
				dataRecorder = cacheHandler.createDataRecorder();
				recordedData = new ArrayList<Pair<FillDatasetPosition,Object>>();
			}
		}
	}

	protected Object getContextParameterValue(String parameterName)
	{
		if (reportContext == null)
		{
			return null;
		}

		Object value = reportContext.getParameterValue(parameterName);
		return value;
	}

	public DataCacheHandler getCacheHandler()
	{
		return cacheHandler;
	}

	public DataSnapshot getDataSnapshot()
	{
		return dataSnapshot;
	}

	public boolean hasDataSnapshot()
	{
		return dataSnapshot != null;
	}

	public DataRecorder getDataRecorder()
	{
		return dataRecorder;
	}

	public void addDataRecordResult(FillDatasetPosition fillPosition, Object recorded)
	{
		recordedData.add(new Pair<FillDatasetPosition, Object>(fillPosition, recorded));
	}
	
	public void cacheDone()
	{
		if (dataRecorder != null && dataRecorder.isEnabled())
		{
			// add all recorded data
			for (Pair<FillDatasetPosition, Object> recorededItem : recordedData)
			{
				dataRecorder.addRecordResult(recorededItem.first(), recorededItem.second());
			}
			
			dataRecorder.setSnapshotPopulated();
		}
	}
	
	public void markCanceled()
	{
		canceled = true;
	}
	
	public boolean isCanceled()
	{
		return canceled;
	}
	
	public Object getFillCache(String key)
	{
		return fillCaches.get(key);
	}
	
	public void setFillCache(String key, Object value)
	{
		fillCaches.put(key, value);
	}

	public void dispose()
	{
		for (Object cacheObject : fillCaches.values())
		{
			if (cacheObject instanceof FillCacheDisposable)
			{
				((FillCacheDisposable) cacheObject).dispose();
			}
		}
	}
	
	public static interface FillCacheDisposable
	{
		void dispose();
	}

	public boolean isCollectingBookmarks()
	{
		return getMasterFiller().bookmarkHelper != null;
	}
}
