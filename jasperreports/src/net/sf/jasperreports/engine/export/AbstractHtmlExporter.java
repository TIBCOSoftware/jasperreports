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
package net.sf.jasperreports.engine.export;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.HtmlReportConfiguration;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractHtmlExporter<RC extends HtmlReportConfiguration, C extends HtmlExporterConfiguration> extends JRAbstractExporter<RC, C, HtmlExporterOutput, JRHtmlExporterContext>
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
	protected static final String JR_BOOKMARK_ANCHOR_PREFIX = "JR_BKMRK_";

	protected static final float DEFAULT_ZOOM = 1f;
	
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();

	/**
	 * @deprecated To be removed.
	 */
	protected HtmlResourceHandler imageHandler;
	/**
	 * @deprecated To be removed.
	 */
	protected HtmlResourceHandler fontHandler;
	/**
	 * @deprecated To be removed.
	 */
	protected HtmlResourceHandler resourceHandler;
	
	protected Map<String, HtmlFontFamily> fontsToProcess;
	
	/**
	 * 
	 */
	public AbstractHtmlExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	
	/**
	 * @deprecated Replaced by {@link HtmlExporterOutput#getImageHandler()}.
	 */
	public HtmlResourceHandler getImageHandler() 
	{
		return imageHandler;
	}

	/**
	 * @deprecated Replaced by {@link HtmlExporterOutput#getImageHandler()}.
	 */
	public void setImageHandler(HtmlResourceHandler imageHandler) 
	{
		this.imageHandler = imageHandler;
	}

	/**
	 * @deprecated Replaced by {@link HtmlExporterOutput#getFontHandler()}.
	 */
	public HtmlResourceHandler getFontHandler() 
	{
		return fontHandler;
	}

	/**
	 * @deprecated Replaced by {@link HtmlExporterOutput#getFontHandler()}.
	 */
	public void setFontHandler(HtmlResourceHandler fontHandler) 
	{
		this.fontHandler = fontHandler;
	}

	/**
	 * @deprecated Replaced by {@link HtmlExporterOutput#getResourceHandler()}.
	 */
	public HtmlResourceHandler getResourceHandler() 
	{
		return resourceHandler;
	}

	/**
	 * @deprecated Replaced by {@link HtmlExporterOutput#getResourceHandler()}.
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
	 * @deprecated Replaced by {@link #getImageName(JRPrintElementIndex, String)}.
	 */
	public static String getImageName(JRPrintElementIndex printElementIndex)
	{
		return getImageName(printElementIndex, null);
	}

	
	/**
	 * 
	 */
	public static String getImageName(JRPrintElementIndex printElementIndex, String fileExtension)
	{
		return IMAGE_NAME_PREFIX + printElementIndex.toString() + (fileExtension == null ? "" : ("." + fileExtension));
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
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_INVALID_IMAGE_NAME,
					new Object[]{imageName});
		}

		int fileExtensionStart = imageName.lastIndexOf('.');
		fileExtensionStart = fileExtensionStart < 0 ? imageName.length() : fileExtensionStart;
		
		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH, fileExtensionStart));
	}
	
	/**
	 * 
	 */
	public String getFontFamily(
		boolean ignoreCase,
		String fontFamily,
		Locale locale
		)
	{
		FontInfo fontInfo =
			ignoreCase
			? FontUtil.getInstance(jasperReportsContext).getFontInfoIgnoreCase(fontFamily, locale)
			: FontUtil.getInstance(jasperReportsContext).getFontInfo(fontFamily, locale);
			
		if (fontInfo != null)
		{
			//fontName found in font extensions
			FontFamily family = fontInfo.getFontFamily();
			String exportFont = family.getExportFont(getExporterKey());
			if (exportFont == null)
			{
				HtmlExporterOutput output = getExporterOutput();
				HtmlResourceHandler resourceHandler = 
					output.getResourceHandler() == null
					? getResourceHandler()
					: output.getResourceHandler();
				if (resourceHandler != null)
				{
					HtmlFontFamily htmlFontFamily = HtmlFontFamily.getInstance(locale, fontInfo);
					
					if (htmlFontFamily != null)
					{
						addFontFamily(htmlFontFamily);
						
						fontFamily = "'" + htmlFontFamily.getShortId() + "'";
					}
				}
			}
			else
			{
				fontFamily = exportFont;
			}
		}

		return fontFamily;
	}

	
	/**
	 *
	 */
	public void addFontFamily(HtmlFontFamily htmlFontFamily) 
	{
		if (!fontsToProcess.containsKey(htmlFontFamily.getId()))
		{
			fontsToProcess.put(htmlFontFamily.getId(), htmlFontFamily);

			if (getReportContext() == null)
			{
				HtmlExporterOutput output = getExporterOutput();
				HtmlResourceHandler resourceHandler = 
					output.getResourceHandler() == null
					? getResourceHandler()
					: output.getResourceHandler();
					
				// create font resources only in static HTML export
				HtmlFontUtil.getInstance(jasperReportsContext).handleHtmlFont(
					resourceHandler, 
					null,
					resourceHandler,
					htmlFontFamily,
					true,
					true
					);
			}
		}
	}


	/**
	 * 
	 */
	protected boolean isEmbedImage(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(HtmlReportConfiguration.PROPERTY_EMBED_IMAGE)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, HtmlReportConfiguration.PROPERTY_EMBED_IMAGE, getCurrentItemConfiguration().isEmbedImage());
		}
		return getCurrentItemConfiguration().isEmbedImage();
	}


	/**
	 * 
	 */
	protected boolean isEmbeddedSvgUseFonts(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(HtmlReportConfiguration.PROPERTY_EMBEDDED_SVG_USE_FONTS)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, HtmlReportConfiguration.PROPERTY_EMBEDDED_SVG_USE_FONTS, getCurrentItemConfiguration().isEmbeddedSvgUseFonts());
		}
		return getCurrentItemConfiguration().isEmbeddedSvgUseFonts();
	}

}
