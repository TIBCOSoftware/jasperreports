/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.fill.JRPrintFrame;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * Exports a JasperReports document to a <tt>Graphics2D</tt> object. Since all font measurement and layout
 * calculation during report filling is done using AWT, this is considered the perfect exporter, unlike the others,
 * which are only approximations of the initial document.
 * <p>
 * As its name indicates, this exporter is special because it does not produce files or does not send character
 * or binary data to an output stream.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRGraphics2DExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	protected Graphics2D grx = null;
	protected JRExportProgressMonitor progressMonitor = null;
	protected float zoom = 1f;

	protected TextRenderer textRenderer = new TextRenderer();


	/**
	 *
	 */
	public JRGraphics2DExporter() throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();
	}
	

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		/*   */
		setClassLoader();

		/*   */
		setInput();

		/*   */
		setPageRange();

		grx = (Graphics2D)parameters.get(JRGraphics2DExporterParameter.GRAPHICS_2D);
		if (grx == null)
		{
			throw new JRException("No output specified for the exporter. java.awt.Graphics2D object expected.");
		}
		
		Float zoomRatio = (Float)parameters.get(JRGraphics2DExporterParameter.ZOOM_RATIO);
		if (zoomRatio != null)
		{
			zoom = zoomRatio.floatValue();
			if (zoom <= 0)
			{
				throw new JRException("Invalid zoom ratio : " + zoom);
			}
		}

		exportReportToGraphics2D();

		/*   */
		resetClassLoader();
	}
		

	/**
	 *
	 */
	public void exportReportToGraphics2D() throws JRException
	{
		grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		grx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		grx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		AffineTransform atrans = new AffineTransform();
		atrans.scale(zoom, zoom);
		grx.transform(atrans);

		java.util.List pages = jasperPrint.getPages();
		if (pages != null)
		{
			Shape oldClipShape = grx.getClip();
	
			grx.setClip(
				0, 
				0, 
				jasperPrint.getPageWidth(), 
				jasperPrint.getPageHeight()
				);
	
			try
			{
				JRPrintPage page = (JRPrintPage)pages.get(startPageIndex);
				exportPage(page);
			}
			finally
			{
				grx.setClip(oldClipShape);
			}
		}
	}
	

	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException
	{
		grx.setColor(Color.white);
		grx.fillRect(
			0, 
			0, 
			jasperPrint.getPageWidth(), 
			jasperPrint.getPageHeight()
			);

		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1));

		/*   */
		Collection elements = page.getElements();
		exportElements(elements);
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void exportElements(Collection elements) throws JRException
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element;
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement)it.next();
				
				if (element instanceof JRPrintLine)
				{
					exportLine((JRPrintLine)element);
				}
				else if (element instanceof JRPrintRectangle)
				{
					exportRectangle((JRPrintRectangle)element);
				}
				else if (element instanceof JRPrintEllipse)
				{
					exportEllipse((JRPrintEllipse)element);
				}
				else if (element instanceof JRPrintImage)
				{
					exportImage((JRPrintImage)element);
				}
				else if (element instanceof JRPrintText)
				{
					exportText((JRPrintText)element);
				}
				else if (element instanceof JRPrintFrame)
				{
					exportFrame((JRPrintFrame) element);
				}
			}
		}
	}


	/**
	 *
	 */
	protected void exportLine(JRPrintLine line)
	{
		grx.setColor(line.getForecolor());
		
		Stroke stroke = getStroke(line.getPen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				grx.drawLine(
					line.getX() + getOffsetX(), 
					line.getY() + getOffsetY(),
					line.getX() + getOffsetX() + line.getWidth() - 1,  
					line.getY() + getOffsetY() + line.getHeight() - 1
					);
			}
			else
			{
				grx.drawLine(
					line.getX() + getOffsetX(), 
					line.getY() + getOffsetY() + line.getHeight() - 1,
					line.getX() + getOffsetX() + line.getWidth() - 1,  
					line.getY() + getOffsetY()
					);
			}
		}
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle)
	{
		if (rectangle.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(rectangle.getBackcolor());
			grx.fillRoundRect(
				rectangle.getX() + getOffsetX(), 
				rectangle.getY() + getOffsetY(), 
				rectangle.getWidth(),
				rectangle.getHeight(),
				2 * rectangle.getRadius(),
				2 * rectangle.getRadius()
				);
		}

		grx.setColor(rectangle.getForecolor());

		Stroke stroke = getStroke(rectangle.getPen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			grx.drawRoundRect(
				rectangle.getX() + getOffsetX(), 
				rectangle.getY() + getOffsetY(), 
				rectangle.getWidth() - 1,
				rectangle.getHeight() - 1,
				2 * rectangle.getRadius(),
				2 * rectangle.getRadius()
				);
		}
	}


	/**
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse)
	{
		if (ellipse.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(ellipse.getBackcolor());
			grx.fillOval(
				ellipse.getX() + getOffsetX(), 
				ellipse.getY() + getOffsetY(), 
				ellipse.getWidth(),
				ellipse.getHeight()
				);
		}

		grx.setColor(ellipse.getForecolor());

		Stroke stroke = getStroke(ellipse.getPen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			grx.drawOval(
				ellipse.getX() + getOffsetX(), 
				ellipse.getY() + getOffsetY(), 
				ellipse.getWidth() - 1,
				ellipse.getHeight() - 1
				);
		}
	}


	/**
	 *
	 */
	protected void exportImage(JRPrintImage printImage) throws JRException
	{
		if (printImage.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(printImage.getBackcolor());

			grx.fillRect(
				printImage.getX() + getOffsetX(), 
				printImage.getY() + getOffsetY(), 
				printImage.getWidth(),
				printImage.getHeight()
				);
		}

		int topPadding = printImage.getTopPadding();
		int leftPadding = printImage.getLeftPadding();
		int bottomPadding = printImage.getBottomPadding();
		int rightPadding = printImage.getRightPadding();
		
		int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;
		
		JRRenderable renderer = printImage.getRenderer();
		
		if (
			availableImageWidth > 0 
			&& availableImageHeight > 0 
			&& renderer != null
			)
		{
			int normalWidth = availableImageWidth;
			int normalHeight = availableImageHeight;

			Dimension2D dimension = renderer.getDimension();
			if (dimension != null)
			{
				normalWidth = (int)dimension.getWidth();
				normalHeight = (int)dimension.getHeight();
			}
	
			float xalignFactor = 0f;
			switch (printImage.getHorizontalAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
				{
					xalignFactor = 1f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER :
				{
					xalignFactor = 0.5f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_LEFT :
				default :
				{
					xalignFactor = 0f;
					break;
				}
			}

			float yalignFactor = 0f;
			switch (printImage.getVerticalAlignment())
			{
				case JRAlignment.VERTICAL_ALIGN_BOTTOM :
				{
					yalignFactor = 1f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_MIDDLE :
				{
					yalignFactor = 0.5f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_TOP :
				default :
				{
					yalignFactor = 0f;
					break;
				}
			}

			switch (printImage.getScaleImage())// FIXME maybe put this in JRFiller
			{
				case JRImage.SCALE_IMAGE_CLIP :
				{
					int xoffset = (int)(xalignFactor * (availableImageWidth - normalWidth));
					int yoffset = (int)(yalignFactor * (availableImageHeight - normalHeight));

					grx.setClip(
						printImage.getX() + leftPadding + getOffsetX(), 
						printImage.getY() + topPadding + getOffsetY(), 
						availableImageWidth, 
						availableImageHeight
						);
					renderer.render(
						grx, 
						new Rectangle(
							printImage.getX() + leftPadding + getOffsetX() + xoffset, 
							printImage.getY() + topPadding + getOffsetY() + yoffset, 
							normalWidth, 
							normalHeight
							) 
						);
					grx.setClip(
						0, 
						0, 
						jasperPrint.getPageWidth(), 
						jasperPrint.getPageHeight()
						);
	
					break;
				}
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					renderer.render(
						grx,
						new Rectangle(
							printImage.getX() + leftPadding + getOffsetX(), 
							printImage.getY() + topPadding + getOffsetY(), 
							availableImageWidth, 
							availableImageHeight
							)
						);
	
					break;
				}
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
				default :
				{
					if (printImage.getHeight() > 0)
					{
						double ratio = (double)normalWidth / (double)normalHeight;
						
						if( ratio > (double)availableImageWidth / (double)availableImageHeight )
						{
							normalWidth = availableImageWidth; 
							normalHeight = (int)(availableImageWidth / ratio); 
						}
						else
						{
							normalWidth = (int)(availableImageHeight * ratio); 
							normalHeight = availableImageHeight; 
						}

						int xoffset = (int)(xalignFactor * (availableImageWidth - normalWidth));
						int yoffset = (int)(yalignFactor * (availableImageHeight - normalHeight));

						renderer.render(
							grx,
							new Rectangle(
								printImage.getX() + leftPadding + getOffsetX() + xoffset, 
								printImage.getY() + topPadding + getOffsetY() + yoffset, 
								normalWidth, 
								normalHeight
								) 
							);
					}
					
					break;
				}
			}
		}

		if (
			printImage.getTopBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getLeftBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getBottomBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getRightBorder() == JRGraphicElement.PEN_NONE
			)
		{
			Stroke stroke = getStroke(printImage.getPen());
			if (stroke != null)
			{
				grx.setColor(printImage.getForecolor());
				
				grx.setStroke(stroke);
		
				grx.drawRect(
					printImage.getX() + getOffsetX(), 
					printImage.getY() + getOffsetY(), 
					printImage.getWidth() - 1,
					printImage.getHeight() - 1
					);
			}
		}
		else
		{
			/*   */
			exportBox(
				printImage,
				printImage
				);
		}
	}


	/**
	 *
	 */
	protected void exportText(JRPrintText text)
	{
		JRStyledText styledText = getStyledText(text);
		
		if (styledText == null)
		{
			return;
		}

		String allText = styledText.getText();
		
		int x = text.getX() + getOffsetX();
		int y = text.getY() + getOffsetY();
		int width = text.getWidth();
		int height = text.getHeight();
		int topPadding = text.getTopPadding();
		int leftPadding = text.getLeftPadding();
		int bottomPadding = text.getBottomPadding();
		int rightPadding = text.getRightPadding();
		
		double angle = 0;
		
		switch (text.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				y = text.getY() + getOffsetY() + text.getHeight();
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = leftPadding;
				leftPadding = bottomPadding;
				bottomPadding = rightPadding;
				rightPadding = tmpPadding;
				angle = - Math.PI / 2;
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				x = text.getX() + getOffsetX() + text.getWidth();
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = rightPadding;
				rightPadding = bottomPadding;
				bottomPadding = leftPadding;
				leftPadding = tmpPadding;
				angle = Math.PI / 2;
				break;
			}
			case JRTextElement.ROTATION_NONE :
			default :
			{
			}
		}
		
		grx.rotate(angle, x, y);

		if (text.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(text.getBackcolor());
			grx.fillRect(x, y, width, height); 
		}
		else
		{
			/*
			grx.setColor(text.getForecolor());
			grx.setStroke(new BasicStroke(1));
			grx.drawRect(x, y, width, height);
			*/
		}

		if (allText.length() > 0)
		{
			grx.setColor(text.getForecolor());

			/*   */
			textRenderer.render(
				grx, 
				x, 
				y, 
				width, 
				height, 
				topPadding,
				leftPadding,
				bottomPadding,
				rightPadding,
				text.getTextHeight(), 
				text.getHorizontalAlignment(), 
				text.getVerticalAlignment(), 
				text.getLineSpacing(), 
				styledText, 
				allText
				);
			
		}
		
		grx.rotate(-angle, x, y);

		/*   */
		exportBox(
			text,
			text
			);
	}


	/**
	 *
	 */
	protected void exportBox(JRBox box, JRPrintElement element)
	{
		Stroke topStroke = null;
		Stroke leftStroke = null;
		Stroke bottomStroke = null;
		Stroke rightStroke = null;
		if (box != null)
		{
			topStroke = getStroke(box.getTopBorder());
			leftStroke = getStroke(box.getLeftBorder());
			bottomStroke = getStroke(box.getBottomBorder());
			rightStroke = getStroke(box.getRightBorder());
		}

		if (topStroke != null)
		{
			grx.setStroke(topStroke);
			grx.setColor(box.getTopBorderColor() == null ? element.getForecolor() : box.getTopBorderColor());
	
			if (topStroke == STROKE_THIN)
			{
				grx.translate(-THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX() + getOffsetX(), 
					element.getY() + getOffsetY(), 
					element.getX() + getOffsetX() + element.getWidth(),
					element.getY() + getOffsetY()
					);
				grx.translate(THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX() + getOffsetX(), 
					element.getY() + getOffsetY(), 
					element.getX() + getOffsetX() + element.getWidth() - 1,
					element.getY() + getOffsetY()
					);
			}
		}

		if (leftStroke != null)
		{
			grx.setStroke(leftStroke);
			grx.setColor(box.getLeftBorderColor() == null ? element.getForecolor() : box.getLeftBorderColor());
	
			if (leftStroke == STROKE_THIN)
			{
				grx.translate(-THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX() + getOffsetX(), 
					element.getY() + getOffsetY(), 
					element.getX() + getOffsetX(),
					element.getY() + getOffsetY() + element.getHeight()
					);
				grx.translate(THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX() + getOffsetX(), 
					element.getY() + getOffsetY(), 
					element.getX() + getOffsetX(),
					element.getY() + getOffsetY() + element.getHeight() - 1
					);
			}
		}

		if (bottomStroke != null)
		{
			grx.setStroke(bottomStroke);
			grx.setColor(box.getBottomBorderColor() == null ? element.getForecolor() : box.getBottomBorderColor());
	
			if (bottomStroke == STROKE_THIN)
			{
				grx.translate(-THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX() + getOffsetX(), 
					element.getY() + getOffsetY() + element.getHeight() - 1,
					element.getX() + getOffsetX() + element.getWidth(),
					element.getY() + getOffsetY() + element.getHeight() - 1
					);
				grx.translate(THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX() + getOffsetX(), 
					element.getY() + getOffsetY() + element.getHeight() - 1,
					element.getX() + getOffsetX() + element.getWidth() - 1,
					element.getY() + getOffsetY() + element.getHeight() - 1
					);
			}
		}

		if (rightStroke != null)
		{
			grx.setStroke(rightStroke);
			grx.setColor(box.getRightBorderColor() == null ? element.getForecolor() : box.getRightBorderColor());
	
			if (rightStroke == STROKE_THIN)
			{
				grx.translate(THIN_CORNER_OFFSET, -THIN_CORNER_OFFSET);
				grx.drawLine(
					element.getX() + getOffsetX() + element.getWidth() - 1,
					element.getY() + getOffsetY(),
					element.getX() + getOffsetX() + element.getWidth() - 1,
					element.getY() + getOffsetY() + element.getHeight()
					);
				grx.translate(-THIN_CORNER_OFFSET, THIN_CORNER_OFFSET);
			}
			else
			{
				grx.drawLine(
					element.getX() + getOffsetX() + element.getWidth() - 1,
					element.getY() + getOffsetY(),
					element.getX() + getOffsetX() + element.getWidth() - 1,
					element.getY() + getOffsetY() + element.getHeight() - 1
					);
			}
		}
	}


	/**
	 * 
	 */
	private static final double THIN_CORNER_OFFSET = 0.25d;
	private static final Stroke STROKE_THIN = new BasicStroke(0.5f);
	private static final Stroke STROKE_1_POINT = new BasicStroke(1f);
	private static final Stroke STROKE_2_POINT = new BasicStroke(2f);
	private static final Stroke STROKE_4_POINT = new BasicStroke(4f);
	private static final Stroke STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL,
			0f,
			new float[]{5f, 3f},
			0f
			);

	/**
	 * 
	 */
	private static Stroke getStroke(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				return STROKE_DOTTED;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				return STROKE_4_POINT;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				return STROKE_2_POINT;
			}
			case JRGraphicElement.PEN_NONE :
			{
				return null;
			}
			case JRGraphicElement.PEN_THIN :
			{
				return STROKE_THIN;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				return STROKE_1_POINT;
			}
		}
	}
	
	
	protected void exportFrame(JRPrintFrame frame) throws JRException
	{		
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			int x = frame.getX() + getOffsetX();
			int y = frame.getY() + getOffsetY();
			
			grx.setColor(frame.getBackcolor());
			grx.fillRect(x, y, frame.getWidth(), frame.getHeight()); 
		}
		
		setFrameElementsOffset(frame, false);
		try
		{
			exportElements(frame.getElements());
		}
		finally
		{
			restoreElementOffsets();
		}
		
		exportBox(frame, frame);
	}

}
