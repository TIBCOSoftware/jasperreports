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
package net.sf.jasperreports.swing;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import net.sf.jasperreports.engine.xml.JRPrintXmlLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRViewerController
{
	private static final Log log = LogFactory.getLog(JRViewerController.class);

	protected static final int TYPE_FILE_NAME = 1;
	protected static final int TYPE_INPUT_STREAM = 2;
	protected static final int TYPE_OBJECT = 3;
	
	private JasperReportsContext jasperReportsContext;
	private LocalJasperReportsContext localJasperReportsContext;
	private ResourceBundle resourceBundle;
	private Locale locale;
	private final List<JRViewerListener> listeners = new ArrayList<JRViewerListener>();
	
	protected int type = TYPE_FILE_NAME;
	protected boolean isXML;
	protected String reportFileName;
	protected boolean reloadSupported;
	
	protected JasperPrint jasperPrint;
	private int pageIndex;
	private float zoom;
	private boolean fitPage;
	private boolean fitWidth;

	/**
	 * @see #JRViewerController(JasperReportsContext, Locale, ResourceBundle)
	 */
	public JRViewerController(Locale locale, ResourceBundle resBundle)
	{
		this(DefaultJasperReportsContext.getInstance(), locale, resBundle);
	}

	/**
	 * 
	 */
	public JRViewerController(
		JasperReportsContext jasperReportsContext,
		Locale locale, 
		ResourceBundle resBundle
		)
	{
		this.jasperReportsContext = jasperReportsContext;
		
		if (locale != null)
		{
			this.locale = locale;
		}
		else
		{
			this.locale = Locale.getDefault();
		}

		if (resBundle == null)
		{
			this.resourceBundle = ResourceBundle.getBundle("net/sf/jasperreports/view/viewer", getLocale());
		}
		else
		{
			this.resourceBundle = resBundle;
		}
	}

	public void addListener(JRViewerListener listener)
	{
		listeners.add(listener);
	}

	public boolean removeListener(JRViewerListener listener)
	{
		return listeners.remove(listener);
	}
	
	protected void fireListeners(int eventCode)
	{
		if (!listeners.isEmpty())
		{
			JRViewerEvent event = new JRViewerEvent(this, eventCode);
			for (Iterator<JRViewerListener> it = listeners.iterator(); it.hasNext();)
			{
				JRViewerListener listener = it.next();
				listener.viewerEvent(event);
			}
		}
	}
	
	protected void setReport(String fileName, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			jasperPrint = JRPrintXmlLoader.loadFromFile(jasperReportsContext, fileName);
		}
		else
		{
			jasperPrint = (JasperPrint)JRLoader.loadObjectFromFile(fileName);
		}
	}

	public void loadReport(String fileName, boolean isXmlReport) throws JRException
	{
		setReport(fileName, isXmlReport);

		type = TYPE_FILE_NAME;
		this.isXML = isXmlReport;
		reportFileName = fileName;

		SimpleFileResolver fileResolver = new SimpleFileResolver(Arrays.asList(new File[]{new File(fileName).getParentFile(), new File(".")}));
		fileResolver.setResolveAbsolutePath(true);
		if (localJasperReportsContext == null)
		{
			localJasperReportsContext = new LocalJasperReportsContext(jasperReportsContext);
			jasperReportsContext = localJasperReportsContext;
		}
		localJasperReportsContext.setFileResolver(fileResolver);
		
		reloadSupported = true;
		fireListeners(JRViewerEvent.EVENT_REPORT_LOADED);
		setPageIndex(0);
	}

	protected void setReport(InputStream is, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			jasperPrint = JRPrintXmlLoader.load(jasperReportsContext, is);
		}
		else
		{
			jasperPrint = (JasperPrint)JRLoader.loadObject(is);
		}
	}

	public void loadReport(InputStream is, boolean isXmlReport) throws JRException
	{
		setReport(is, isXmlReport);

		type = TYPE_INPUT_STREAM;
		this.isXML = isXmlReport;
		reloadSupported = false;
		fireListeners(JRViewerEvent.EVENT_REPORT_LOADED);
		setPageIndex(0);
	}

	public void loadReport(JasperPrint jrPrint)
	{
		jasperPrint = jrPrint;
		type = TYPE_OBJECT;
		isXML = false;
		reloadSupported = false;
		fireListeners(JRViewerEvent.EVENT_REPORT_LOADED);
		setPageIndex(0);
	}

	public void reload()
	{
		if (type == TYPE_FILE_NAME)
		{
			try
			{
				loadReport(reportFileName, isXML);
			}
			catch (JRException e)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Reload failed.", e);
				}
				jasperPrint = null;
				setPageIndex(0);
				refreshPage();

				fireListeners(JRViewerEvent.EVENT_REPORT_LOAD_FAILED);
			}

			forceRefresh();
		}
	}

	public boolean hasPages()
	{
		return jasperPrint != null &&
			jasperPrint.getPages() != null &&
			jasperPrint.getPages().size() > 0;
	}
	
	public void refreshPage()
	{
		fireListeners(JRViewerEvent.EVENT_REFRESH_PAGE);
	}

	protected void forceRefresh()
	{
		zoom = 0;//force pageRefresh()
		setZoomRatio(1);
	}

	public void setZoomRatio(float newZoom)
	{
		if (newZoom > 0)
		{
			fitPage = false;
			fitWidth = false;
			
			float old = zoom;
			zoom = newZoom;

			fireListeners(JRViewerEvent.EVENT_ZOOM_CHANGED);
			
			if (zoom != old)
			{
				refreshPage();
			}
		}
	}
	
	public void setPageIndex(int index)
	{
		if (hasPages())
		{
			if (index >= 0 && index < jasperPrint.getPages().size())
			{
				pageIndex = index;
				fireListeners(JRViewerEvent.EVENT_PAGE_CHANGED);
			}
		}
		else
		{
			fireListeners(JRViewerEvent.EVENT_PAGE_CHANGED);
		}
	}
	
	public JasperReportsContext getJasperReportsContext()
	{
		return jasperReportsContext;
	}

	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}

	public Locale getLocale()
	{
		return locale;
	}
	
	public String getBundleString(String key)
	{
		return resourceBundle.getString(key);
	}

	public JasperPrint getJasperPrint()
	{
		return jasperPrint;
	}

	public int getPageCount()
	{
		return jasperPrint.getPages().size();
	}
	
	public PrintPageFormat getPageFormat()
	{
		return getJasperPrint().getPageFormat(getPageIndex());
	}
	
	public void clear()
	{
		jasperPrint = null;
	}

	public int getPageIndex()
	{
		return pageIndex;
	}
	
	public float getZoom()
	{
		return zoom;
	}

	public boolean isReloadSupported()
	{
		return reloadSupported;
	}

	public boolean isFitPage()
	{
		return fitPage;
	}

	public boolean isFitWidth()
	{
		return fitWidth;
	}

	public void fitPage()
	{
		fireListeners(JRViewerEvent.EVENT_FIT_PAGE);
		fitPage = true;
	}

	public void fitWidth()
	{
		fireListeners(JRViewerEvent.EVENT_FIT_WIDTH);
		fitWidth = true;
	}
}
