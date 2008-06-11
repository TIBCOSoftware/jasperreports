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
package net.sf.jasperreports.swing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRPrintXmlLoader;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRViewerController
{
	protected static final int TYPE_FILE_NAME = 1;
	protected static final int TYPE_INPUT_STREAM = 2;
	protected static final int TYPE_OBJECT = 3;
	
	private ResourceBundle resourceBundle = null;
	private Locale locale = null;
	private final List listeners = new ArrayList();
	
	protected int type = TYPE_FILE_NAME;
	protected boolean isXML = false;
	protected String reportFileName = null;
	protected boolean reloadSupported;
	
	private JasperPrint jasperPrint = null;
	private int pageIndex = 0;
	private float zoom = 0f;
	private boolean fitPage;
	private boolean fitWidth;

	public JRViewerController(Locale locale, ResourceBundle resBundle)
	{
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
			for (Iterator it = listeners.iterator(); it.hasNext();)
			{
				JRViewerListener listener = (JRViewerListener) it.next();
				listener.viewerEvent(event);
			}
		}
	}
	
	public void loadReport(String fileName, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			jasperPrint = JRPrintXmlLoader.load(fileName);
		}
		else
		{
			jasperPrint = (JasperPrint)JRLoader.loadObject(fileName);
		}

		type = TYPE_FILE_NAME;
		this.isXML = isXmlReport;
		reportFileName = fileName;
		reloadSupported = true;
		fireListeners(JRViewerEvent.EVENT_REPORT_LOADED);
		setPageIndex(0);
	}

	public void loadReport(InputStream is, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			jasperPrint = JRPrintXmlLoader.load(is);
		}
		else
		{
			jasperPrint = (JasperPrint)JRLoader.loadObject(is);
		}

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
				e.printStackTrace();

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
	
	protected void setPageIndex(int index)
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
