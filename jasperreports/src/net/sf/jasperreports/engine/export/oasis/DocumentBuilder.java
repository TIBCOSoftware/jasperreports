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

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 * 
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Sch�nheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.oasis;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.zip.FileBufferedZipEntry;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class DocumentBuilder 
{
	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();
	
	/**
	 *
	 */
	protected final Map<String, String> rendererToImagePathMap = new HashMap<String, String>();
	protected final OasisZip oasisZip;
	
	
	/**
	 *
	 */
	public DocumentBuilder(OasisZip oasisZip)
	{
		this.oasisZip = oasisZip;
	}
	
	
	/**
	 *
	 */
	public static String getImageName(JRPrintElementIndex printElementIndex)
	{
		return IMAGE_NAME_PREFIX + printElementIndex.toString();
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
					JRAbstractExporter.EXCEPTION_MESSAGE_KEY_INVALID_IMAGE_NAME,
					new Object[]{imageName});
		}

		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH));
	}

	/**
	 *
	 */
	protected String getHyperlinkURL(JRPrintHyperlink link)
	{
		return getHyperlinkURL(link, true);
	}
	
	/**
	 *
	 */
	protected String getHyperlinkURL(JRPrintHyperlink link, boolean isOnePagePerSheet)
	{
		String href = null;
		JRHyperlinkProducer customHandler = getHyperlinkProducer(link);
		if (customHandler == null)
		{
			switch(link.getHyperlinkTypeValue())
			{
				case REFERENCE :
				{
					if (link.getHyperlinkReference() != null)
					{
						href = link.getHyperlinkReference();
					}
					break;
				}
				case LOCAL_ANCHOR :
				{
					if (link.getHyperlinkAnchor() != null)
					{
						href = "#" + link.getHyperlinkAnchor();
					}
					break;
				}
				case LOCAL_PAGE :
				{
					if (link.getHyperlinkPage() != null)
					{
						href = "#" + JR_PAGE_ANCHOR_PREFIX + getReportIndex() + "_" + (isOnePagePerSheet ? link.getHyperlinkPage().toString() : "1");
					}
					break;
				}
				case REMOTE_ANCHOR :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkAnchor() != null
						)
					{
						href = link.getHyperlinkReference() + "#" + link.getHyperlinkAnchor();
					}
					break;
				}
				case REMOTE_PAGE :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkPage() != null
						)
					{
						href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
					}
					break;
				}
				case NONE :
				default :
				{
					break;
				}
			}
		}
		else
		{
			href = customHandler.getHyperlink(link);
		}

		return href;
	}

	/**
	 *
	 */
	protected String getImagePath(Renderable renderer, JRPrintImage image, JRExporterGridCell gridCell) throws JRException
	{
		String imagePath = null;

		if (renderer != null)
		{
			if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE && rendererToImagePathMap.containsKey(renderer.getId()))
			{
				imagePath = rendererToImagePathMap.get(renderer.getId());
			}
			else
			{
				if (image.isLazy())
				{
					imagePath = ((JRImageRenderer)renderer).getImageLocation();
				}
				else
				{
					JRPrintElementIndex imageIndex = getElementIndex(gridCell);
					processImage(image, imageIndex);

					String imageName = DocumentBuilder.getImageName(imageIndex);
					imagePath = "Pictures/" + imageName;
				}

				rendererToImagePathMap.put(renderer.getId(), imagePath);
			}
		}

		return imagePath;
	}

	/**
	 *
	 */
	protected void processImage(JRPrintImage image, JRPrintElementIndex imageIndex) throws JRException
	{
		Renderable renderer = image.getRenderable();
		if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
		{
			renderer =
				new JRWrappingSvgRenderer(
					renderer,
					new Dimension(image.getWidth(), image.getHeight()),
					ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
					);
		}

		oasisZip.addEntry(//FIXMEODT optimize with a different implementation of entry
			new FileBufferedZipEntry(
				"Pictures/" + DocumentBuilder.getImageName(imageIndex),
				renderer.getImageData(getJasperReportsContext())
				)
			);
	}
	
	/**
	 *
	 */
	protected JRPrintElementIndex getElementIndex(JRExporterGridCell gridCell)
	{
		JRPrintElementIndex imageIndex =
			new JRPrintElementIndex(
					getReportIndex(),
					getPageIndex(),
					gridCell.getElementAddress()
					);
		return imageIndex;
	}

	/**
	 *
	 */
	public abstract JRStyledText getStyledText(JRPrintText text);

	/**
	 *
	 */
	public abstract Locale getTextLocale(JRPrintText text);

	/**
	 *
	 */
	public abstract String getInvalidCharReplacement();
	
	/**
	 * 
	 */
	protected abstract void insertPageAnchor(TableBuilder tableBuilder);

	/**
	 * 
	 */
	protected abstract JRHyperlinkProducer getHyperlinkProducer(JRPrintHyperlink link);

	/**
	 * 
	 */
	protected abstract JasperReportsContext getJasperReportsContext();

	/**
	 * 
	 */
	protected abstract int getReportIndex();

	/**
	 * 
	 */
	protected abstract int getPageIndex();

}