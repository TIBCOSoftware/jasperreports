/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export;

import java.util.List;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: HtmlExporter.java 6502 2013-09-11 14:07:25Z lucianc $
 */
public abstract class AbstractHtmlExporter extends JRAbstractExporter
{
	/**
	 *
	 */
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";
	protected static final String CSS_TEXT_ALIGN_RIGHT = "right";
	protected static final String CSS_TEXT_ALIGN_CENTER = "center";
	protected static final String CSS_TEXT_ALIGN_JUSTIFY = "justify";

	/**
	 *
	 */
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String HTML_VERTICAL_ALIGN_MIDDLE = "middle";
	protected static final String HTML_VERTICAL_ALIGN_BOTTOM = "bottom";

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	protected static final float DEFAULT_ZOOM = 1f;
	
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();

	/**
	 * 
	 */
	protected HtmlResourceHandler imageHandler;
	protected HtmlResourceHandler fontHandler;
	protected HtmlResourceHandler resourceHandler;
	
	/**
	 * 
	 */
	public AbstractHtmlExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	
	/**
	 * 
	 */
	public HtmlResourceHandler getImageHandler() 
	{
		return imageHandler;
	}

	/**
	 * 
	 */
	public void setImageHandler(HtmlResourceHandler imageHandler) 
	{
		this.imageHandler = imageHandler;
	}

	/**
	 * 
	 */
	public HtmlResourceHandler getFontHandler() 
	{
		return fontHandler;
	}

	/**
	 * 
	 */
	public void setFontHandler(HtmlResourceHandler fontHandler) 
	{
		this.fontHandler = fontHandler;
	}

	/**
	 * 
	 */
	public HtmlResourceHandler getResourceHandler() 
	{
		return resourceHandler;
	}

	/**
	 * 
	 */
	public void setResourceHandler(HtmlResourceHandler resourceHandler) 
	{
		this.resourceHandler = resourceHandler;
	}


	/**
	 * 
	 */
	public static JRPrintImage getImage(List<JasperPrint> jasperPrintList, String imageName)
	{
		return getImage(jasperPrintList, getPrintElementIndex(imageName));
	}


	/**
	 * 
	 */
	public static JRPrintImage getImage(List<JasperPrint> jasperPrintList, JRPrintElementIndex imageIndex)
	{
		JasperPrint report = jasperPrintList.get(imageIndex.getReportIndex());
		JRPrintPage page = report.getPages().get(imageIndex.getPageIndex());

		Integer[] elementIndexes = imageIndex.getAddressArray();
		Object element = page.getElements().get(elementIndexes[0].intValue());

		for (int i = 1; i < elementIndexes.length; ++i)
		{
			JRPrintFrame frame = (JRPrintFrame) element;
			element = frame.getElements().get(elementIndexes[i].intValue());
		}

		return (JRPrintImage) element;
	}

	
	/**
	 *
	 */
	public static JRPrintElementIndex getPrintElementIndex(String imageName)
	{
		if (!imageName.startsWith(IMAGE_NAME_PREFIX))
		{
			throw new JRRuntimeException("Invalid image name: " + imageName);
		}

		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH));
	}

}
