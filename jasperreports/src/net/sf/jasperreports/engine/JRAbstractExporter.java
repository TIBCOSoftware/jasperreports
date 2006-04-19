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

import java.awt.font.TextAttribute;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.base.JRBaseBox;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.JRResourcesUtil;

import org.xml.sax.SAXException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractExporter implements JRExporter
{


	/**
	 *
	 */
	protected Map parameters = new HashMap();

	/**
	 *
	 */
	protected List jasperPrintList = null;
	protected JasperPrint jasperPrint = null;
	protected boolean isModeBatch = true;
	protected int startPageIndex = 0;
	protected int endPageIndex = 0;
	protected int globalOffsetX = 0;
	protected int globalOffsetY = 0;
	protected ClassLoader classLoader = null;
	protected boolean classLoaderSet;
	protected URLStreamHandlerFactory urlHandlerFactory;
	protected boolean urlHandlerFactorySet;
	
	/**
	 *
	 */
	protected JRFont defaultFont = null;
	
	private LinkedList elementOffsetStack;
	private int elementOffsetX;
	private int elementOffsetY;

	private Map penBoxes;

	
	protected JRAbstractExporter()
	{
		elementOffsetStack = new LinkedList();
		
		elementOffsetX = globalOffsetX;
		elementOffsetY = globalOffsetY;
		
		penBoxes = new HashMap();
	}
	
	
	/**
	 *
	 */
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();


	/**
	 *
	 */
	public void setParameter(JRExporterParameter parameter, Object value)
	{
		parameters.put(parameter, value);
	}


	/**
	 *
	 */
	public Object getParameter(JRExporterParameter parameter)
	{
		return parameters.get(parameter);
	}


	/**
	 *
	 */
	public void setParameters(Map parameters)
	{
		this.parameters = parameters;
	}
	

	/**
	 *
	 */
	public Map getParameters()
	{
		return parameters;
	}
	

	/**
	 *
	 */
	public abstract void exportReport() throws JRException;


	/**
	 *
	 */
	protected void setOffset()
	{
		Integer offsetX = (Integer)parameters.get(JRExporterParameter.OFFSET_X);
		if (offsetX != null)
		{
			globalOffsetX = offsetX.intValue();
		}

		Integer offsetY = (Integer)parameters.get(JRExporterParameter.OFFSET_Y);
		if (offsetY != null)
		{
			globalOffsetY = offsetY.intValue();
		}
		
		elementOffsetX = globalOffsetX;
		elementOffsetY = globalOffsetY;
	}
	

	/**
	 *
	 */
	protected void setExportContext()
	{
		classLoaderSet = false;
		urlHandlerFactorySet = false;
		
		classLoader = (ClassLoader)parameters.get(JRExporterParameter.CLASS_LOADER);
		if (classLoader != null)
		{
			JRResourcesUtil.setThreadClassLoader(classLoader);
			classLoaderSet = true;
		}

		urlHandlerFactory = (URLStreamHandlerFactory) parameters.get(JRExporterParameter.URL_HANDLER_FACTORY);
		if (urlHandlerFactory != null)
		{
			JRResourcesUtil.setThreadURLHandlerFactory(urlHandlerFactory);
			urlHandlerFactorySet = true;
		}
	}
		

	/**
	 *
	 */
	protected void resetExportContext()
	{
		if (classLoaderSet)
		{
			JRResourcesUtil.resetClassLoader();
		}
		
		if (urlHandlerFactorySet)
		{
			JRResourcesUtil.resetThreadURLHandlerFactory();
		}
	}

	
	/**
	 * @deprecated replaced by {@link #setExportContext() setExportContext} 
	 */
	protected void setClassLoader()
	{
		setExportContext();
	}

	
	/**
	 * @deprecated replaced by {@link #resetExportContext() resetExportContext} 
	 */
	protected void resetClassLoader()
	{
		resetExportContext();
	}


	/**
	 *
	 */
	protected void setInput() throws JRException
	{
		jasperPrintList = (List)parameters.get(JRExporterParameter.JASPER_PRINT_LIST);
		if (jasperPrintList == null)
		{
			isModeBatch = false;
			
			jasperPrint = (JasperPrint)parameters.get(JRExporterParameter.JASPER_PRINT);
			if (jasperPrint == null)
			{
				InputStream is = (InputStream)parameters.get(JRExporterParameter.INPUT_STREAM);
				if (is != null)
				{
					jasperPrint = (JasperPrint)JRLoader.loadObject(is);
				}
				else
				{
					URL url = (URL)parameters.get(JRExporterParameter.INPUT_URL);
					if (url != null)
					{
						jasperPrint = (JasperPrint)JRLoader.loadObject(url);
					}
					else
					{
						File file = (File)parameters.get(JRExporterParameter.INPUT_FILE);
						if (file != null)
						{
							jasperPrint = (JasperPrint)JRLoader.loadObject(file);
						}
						else
						{
							String fileName = (String)parameters.get(JRExporterParameter.INPUT_FILE_NAME);
							if (fileName != null)
							{
								jasperPrint = (JasperPrint)JRLoader.loadObject(fileName);
							}
							else
							{
								throw new JRException("No input source supplied to the exporter.");
							}
						}
					}
				}
			}

			jasperPrintList = new ArrayList();
			jasperPrintList.add(jasperPrint);
		}
		else
		{
			isModeBatch = true;

			if (jasperPrintList.size() == 0)
			{
				throw new JRException("Empty input source supplied to the exporter in batch mode.");
			}

			jasperPrint = (JasperPrint)jasperPrintList.get(0);
		}
	}
	

	/**
	 *
	 */
	protected void setPageRange() throws JRException
	{
		int lastPageIndex = -1;
		if (jasperPrint.getPages() != null)
		{
			lastPageIndex = jasperPrint.getPages().size() - 1;
		}

		Integer start = (Integer)parameters.get(JRExporterParameter.START_PAGE_INDEX);
		if (start == null)
		{
			startPageIndex = 0;
		}
		else
		{
			startPageIndex = start.intValue();
			if (startPageIndex < 0 || startPageIndex > lastPageIndex)
			{
				throw new JRException("Start page index out of range : " + startPageIndex + " of " + lastPageIndex);
			}
		}

		Integer end = (Integer)parameters.get(JRExporterParameter.END_PAGE_INDEX);
		if (end == null)
		{
			endPageIndex = lastPageIndex;
		}
		else
		{
			endPageIndex = end.intValue();
			if (endPageIndex < 0 || endPageIndex > lastPageIndex)
			{
				throw new JRException("End page index out of range : " + endPageIndex + " of " + lastPageIndex);
			}
		}

		Integer index = (Integer)parameters.get(JRExporterParameter.PAGE_INDEX);
		if (index != null)
		{
			int pageIndex = index.intValue();
			if (pageIndex < 0 || pageIndex > lastPageIndex)
			{
				throw new JRException("Page index out of range : " + pageIndex + " of " + lastPageIndex);
			}
			startPageIndex = pageIndex;
			endPageIndex = pageIndex;
		}
	}
	

	/**
	 *
	 */
	protected JRFont getDefaultFont()
	{
		if (defaultFont == null)
		{
			defaultFont = jasperPrint.getDefaultFont();
			if (defaultFont == null)
			{
				defaultFont = new JRBaseFont();
			}
		}

		return defaultFont;
	}


	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement, boolean setBackcolor)
	{
		JRStyledText styledText = null;

		String text = textElement.getText();
		if (text != null)
		{
			Map attributes = new HashMap(); 
			attributes.putAll(JRFontUtil.setAttributes(attributes, textElement));
			attributes.put(TextAttribute.FOREGROUND, textElement.getForecolor());
			if (setBackcolor && textElement.getMode() == JRElement.MODE_OPAQUE)
			{
				attributes.put(TextAttribute.BACKGROUND, textElement.getBackcolor());
			}

			if (textElement.isStyledText())
			{
				try
				{
					styledText = styledTextParser.parse(attributes, text);
				}
				catch (SAXException e)
				{
					//ignore if invalid styled text and treat like normal text
				}
			}
		
			if (styledText == null)
			{
				styledText = new JRStyledText();
				styledText.append(text);
				styledText.addRun(new JRStyledText.Run(attributes, 0, text.length()));
			}
		}
		
		return styledText;
	}

	
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return getStyledText(textElement, true);
	}

	
	/**
	 *
	 */
	protected void setOutput()
	{
	}


	/**
	 * Returns the X axis offset used for element export.
	 * <p>
	 * This method should be used istead of {@link #globalOffsetX globalOffsetX} when
	 * exporting elements.
	 * 
	 * @return the X axis offset
	 */
	protected int getOffsetX()
	{
		return elementOffsetX;
	}


	/**
	 * Returns the Y axis offset used for element export.
	 * <p>
	 * This method should be used istead of {@link #globalOffsetY globalOffsetY} when
	 * exporting elements.
	 * 
	 * @return the Y axis offset
	 */
	protected int getOffsetY()
	{
		return elementOffsetY;
	}

	
	/**
	 * Sets the offsets for exporting elements from a {@link JRPrintFrame frame}.
	 * <p>
	 * After the frame elements are exported, a call to {@link #restoreElementOffsets() popElementOffsets} is required
	 * so that the previous offsets are resored.
	 * 
	 * @param frame
	 * @param relative
	 * @see #getOffsetX()
	 * @see #getOffsetY()
	 * @see #restoreElementOffsets()
	 */
	protected void setFrameElementsOffset(JRPrintFrame frame, boolean relative)
	{	
		if (relative)
		{
			setElementOffsets(0, 0);
		}
		else
		{
			int topPadding = frame.getTopPadding();
			int leftPadding = frame.getLeftPadding();

			setElementOffsets(getOffsetX() + frame.getX() + leftPadding, getOffsetY() + frame.getY() + topPadding);
		}
	}
	
	
	private void setElementOffsets(int offsetX, int offsetY)
	{
		elementOffsetStack.addLast(new int[]{elementOffsetX, elementOffsetY});
		
		elementOffsetX = offsetX;
		elementOffsetY = offsetY;
	}

	
	/**
	 * Restores offsets after a call to 
	 * {@link #setFrameElementsOffset(JRPrintFrame, boolean) setFrameElementsOffset}.
	 */
	protected void restoreElementOffsets()
	{
		int[] offsets = (int[]) elementOffsetStack.removeLast();
		elementOffsetX = offsets[0];
		elementOffsetY = offsets[1];
	}

	
	protected JRBox getBox(JRPrintGraphicElement element)
	{
		byte pen = element.getPen();
		Object key = new Byte(pen);
		JRBox box = (JRBox) penBoxes.get(key);
		
		if (box == null)
		{
			box = new JRBaseBox(pen, element.getForecolor());
			
			penBoxes.put(key, box);
		}
		
		return box;
	}
}
