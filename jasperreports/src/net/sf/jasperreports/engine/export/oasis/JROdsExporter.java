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

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 *
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Sch�nheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.oasis;

import java.awt.geom.Dimension2D;
import java.io.IOException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Exports a JasperReports document to Open Document Spreadsheet format. It has character output type
 * and exports the document to a grid-based layout.
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JROdsExporter extends JROpenDocumentExporter
{
	private static final Log log = LogFactory.getLog(JROdsExporter.class);
	
	protected static final String ODS_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.ods.";
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String ODS_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "ods";
	
	protected class ExporterContext extends BaseExporterContext implements JROdsExporterContext
	{
		TableBuilder tableBuilder = null;
		
		public ExporterContext(TableBuilder tableBuidler)
		{
			this.tableBuilder = tableBuidler;
		}
		
		public TableBuilder getTableBuilder()
		{
			return tableBuilder;
		}

		public String getExportPropertiesPrefix()//FIXMENOW if this is moved in abstract exporter, it can be removed from context
		{
			return ODS_EXPORTER_PROPERTIES_PREFIX;
		}
	}
	
	/**
	 *
	 * @see net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporter#getExporterNature(net.sf.jasperreports.engine.export.ExporterFilter)
	 */
	protected ExporterNature getExporterNature(ExporterFilter filter) {
		return new JROdsExporterNature(filter);
	}

	/**
	 *
	 */
	protected String getExporterPropertiesPrefix()
	{
		return ODS_EXPORTER_PROPERTIES_PREFIX;
	}

	/**
	 *
	 */
	protected void exportLine(TableBuilder tableBuilder, JRPrintLine line, JRExporterGridCell gridCell) throws IOException
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = null;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				pen = box.getTopPen();
			}
			else
			{
				pen = box.getBottomPen();
			}
		}
		else
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				pen = box.getLeftPen();
			}
			else
			{
				pen = box.getRightPen();
			}
		}
		pen.setLineColor(line.getLinePen().getLineColor());
		pen.setLineStyle(line.getLinePen().getLineStyleValue());
		pen.setLineWidth(line.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box

		tableBuilder.buildCellHeader(styleCache.getCellStyle(gridCell), gridCell.getColSpan(), gridCell.getRowSpan());

//		double x1, y1, x2, y2;
//
//		if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
//		{
//			x1 = Utility.translatePixelsToInches(0);
//			y1 = Utility.translatePixelsToInches(0);
//			x2 = Utility.translatePixelsToInches(line.getWidth() - 1);
//			y2 = Utility.translatePixelsToInches(line.getHeight() - 1);
//		}
//		else
//		{
//			x1 = Utility.translatePixelsToInches(0);
//			y1 = Utility.translatePixelsToInches(line.getHeight() - 1);
//			x2 = Utility.translatePixelsToInches(line.getWidth() - 1);
//			y2 = Utility.translatePixelsToInches(0);
//		}

		tempBodyWriter.write("<text:p>");
		insertPageAnchor();
//		tempBodyWriter.write(
//				"<draw:line text:anchor-type=\"paragraph\" "
//				+ "draw:style-name=\"" + styleCache.getGraphicStyle(line) + "\" "
//				+ "svg:x1=\"" + x1 + "in\" "
//				+ "svg:y1=\"" + y1 + "in\" "
//				+ "svg:x2=\"" + x2 + "in\" "
//				+ "svg:y2=\"" + y2 + "in\">"
//				//+ "</draw:line>"
//				+ "<text:p/></draw:line>"
//				);
		tempBodyWriter.write("</text:p>");
		tableBuilder.buildCellFooter();
	}


	/**
	 *
	 */
	protected void exportEllipse(TableBuilder tableBuilder, JRPrintEllipse ellipse, JRExporterGridCell gridCell) throws IOException
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = box.getPen();
		pen.setLineColor(ellipse.getLinePen().getLineColor());
		pen.setLineStyle(ellipse.getLinePen().getLineStyleValue());
		pen.setLineWidth(ellipse.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box
		
		tableBuilder.buildCellHeader(styleCache.getCellStyle(gridCell), gridCell.getColSpan(), gridCell.getRowSpan());
		tempBodyWriter.write("<text:p>");
		insertPageAnchor();
//		tempBodyWriter.write(
//			"<draw:ellipse text:anchor-type=\"paragraph\" "
//			+ "draw:style-name=\"" + styleCache.getGraphicStyle(ellipse) + "\" "
//			+ "svg:width=\"" + Utility.translatePixelsToInches(ellipse.getWidth()) + "in\" "
//			+ "svg:height=\"" + Utility.translatePixelsToInches(ellipse.getHeight()) + "in\" "
//			+ "svg:x=\"0in\" "
//			+ "svg:y=\"0in\">"
//			+ "<text:p/></draw:ellipse>"
//			);
		tempBodyWriter.write("</text:p>");
		tableBuilder.buildCellFooter();
	}


	/**
	 *
	 */
	public void exportImage(TableBuilder tableBuilder, JRPrintImage image, JRExporterGridCell gridCell) throws JRException, IOException
	{
		int topPadding = 
			Math.max(image.getLineBox().getTopPadding().intValue(), Math.round(image.getLineBox().getTopPen().getLineWidth().floatValue()));
		int leftPadding = 
			Math.max(image.getLineBox().getLeftPadding().intValue(), Math.round(image.getLineBox().getLeftPen().getLineWidth().floatValue()));
		int bottomPadding = 
			Math.max(image.getLineBox().getBottomPadding().intValue(), Math.round(image.getLineBox().getBottomPen().getLineWidth().floatValue()));
		int rightPadding = 
			Math.max(image.getLineBox().getRightPadding().intValue(), Math.round(image.getLineBox().getRightPen().getLineWidth().floatValue()));

		int availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		int width = availableImageWidth;
		int height = availableImageHeight;

//		int xoffset = 0;
//		int yoffset = 0;

		tableBuilder.buildCellHeader(styleCache.getCellStyle(gridCell), gridCell.getColSpan(), gridCell.getRowSpan());

		JRRenderable renderer = image.getRenderer();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE && !image.isLazy())
			{
				// Non-lazy image renderers are all asked for their image data at some point.
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, image.getOnErrorTypeValue());
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
//			float xalignFactor = getXAlignFactor(image);
//			float yalignFactor = getYAlignFactor(image);

			switch (image.getScaleImageValue())
			{
				case FILL_FRAME :
				{
					width = availableImageWidth;
					height = availableImageHeight;
//					xoffset = 0;
//					yoffset = 0;
					break;
				}
				case CLIP :
				case RETAIN_SHAPE :
				default :
				{
					double normalWidth = availableImageWidth;
					double normalHeight = availableImageHeight;

					if (!image.isLazy())
					{
						// Image load might fail.
						JRRenderable tmpRenderer =
							JRImageRenderer.getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
						Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension();
						// If renderer was replaced, ignore image dimension.
						if (tmpRenderer == renderer && dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}
					}

					if (availableImageHeight > 0)
					{
						double ratio = normalWidth / normalHeight;

						if( ratio > availableImageWidth / (double)availableImageHeight )
						{
							width = availableImageWidth;
							height = (int)(width/ratio);

						}
						else
						{
							height = availableImageHeight;
							width = (int)(ratio * height);
						}
					}

//					xoffset = (int)(xalignFactor * (availableImageWidth - width));
//					yoffset = (int)(yalignFactor * (availableImageHeight - height));
				}
			}

			tempBodyWriter.write("<text:p>");
			insertPageAnchor();
			if (image.getAnchorName() != null)
			{
				exportAnchor(JRStringUtil.xmlEncode(image.getAnchorName()));
			}


			boolean startedHyperlink = startHyperlink(image,false);

//FIXMEODS check images
//			tempBodyWriter.write("<draw:frame text:anchor-type=\"paragraph\" "
//					+ "draw:style-name=\"" + styleCache.getGraphicStyle(image) + "\" "
//					+ "svg:x=\"" + Utility.translatePixelsToInches(leftPadding + xoffset) + "in\" "
//					+ "svg:y=\"" + Utility.translatePixelsToInches(topPadding + yoffset) + "in\" "
//					+ "svg:width=\"" + Utility.translatePixelsToInches(width) + "in\" "
//					+ "svg:height=\"" + Utility.translatePixelsToInches(height) + "in\">"
//					);
//			tempBodyWriter.write("<draw:image ");
//			String imagePath = getImagePath(renderer, image.isLazy(), gridCell);
//			tempBodyWriter.write(" xlink:href=\"" + JRStringUtil.xmlEncode(imagePath) + "\"");
//			tempBodyWriter.write(" xlink:type=\"simple\"");
//			tempBodyWriter.write(" xlink:show=\"embed\"");
//			tempBodyWriter.write(" xlink:actuate=\"onLoad\"");
//			tempBodyWriter.write("/>\n");
//
//			tempBodyWriter.write("</draw:frame>");
			if(startedHyperlink)
			{
				endHyperlink(false);
			}
			tempBodyWriter.write("</text:p>");
		}

		tableBuilder.buildCellFooter();
	}

	
	/**
	 *
	 */
	protected void exportAnchor(String anchorName) throws IOException //FIXMEODS check anchors
	{
	}
	
	
	/**
	 *
	 */
	protected void exportGenericElement(TableBuilder tableBuilder, JRGenericPrintElement element, JRExporterGridCell gridCell) throws IOException, JRException
	{
		GenericElementOdsHandler handler = (GenericElementOdsHandler) 
		GenericElementHandlerEnviroment.getHandler(
				element.getGenericType(), ODS_EXPORTER_KEY);

		if (handler != null)
		{
			JROdsExporterContext exporterContext = new ExporterContext(tableBuilder);

			handler.exportElement(exporterContext, element, gridCell);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No ODS generic element handler for " 
						+ element.getGenericType());
			}
		}
	}


	/**
	 *
	 */
	protected String getExporterKey()
	{
		return ODS_EXPORTER_KEY;
	}
}

