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
package net.sf.jasperreports.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.base.JRBaseBox;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.fill.TextMeasurer;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.xml.sax.SAXException;


/**
 * Report design preview renderer.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 * @see JRDesignViewer
 */
public class JRDesignRenderer
{
	
	private static final double THIN_CORNER_OFFSET = 0.25d;
	private static final double ONE_POINT_CORNER_OFFSET = 0.5d;

	private static final Stroke STROKE_THIN = new BasicStroke(0.5f);
	private static final Stroke STROKE_1_POINT = new BasicStroke(1f);
	private static final Stroke STROKE_2_POINT = new BasicStroke(2f);
	private static final Stroke STROKE_4_POINT = new BasicStroke(4f);
	private static final Stroke STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_MITER,
			10f,
			new float[]{5f, 3f},
			0f
			);
	
	private static final Stroke BORDER_STROKE_THIN = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static final Stroke BORDER_STROKE_1_POINT = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static final Stroke BORDER_STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER,
			10f,
			new float[]{5f, 3f},
			0f
			);

	protected JRReport report;
	protected ImageObserver imageObserver;
	protected int offsetY;
	protected int upColumns;
	protected int downColumns;
	
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();
	protected TextRenderer textRenderer = TextRenderer.getInstance();
	
	
	/**
	 * Creates a report design renderer.
	 * 
	 * @param report the report
	 * @param imageObserver an image observer to be used when drawing images
	 */
	public JRDesignRenderer(JRReport report, ImageObserver imageObserver)
	{
		this.report = report;
		this.imageObserver = imageObserver;
		
		setOffsetY();
	}
	
	private void setOffsetY()
	{
		offsetY = report.getTopMargin();
		offsetY += (report.getTitle() != null ? report.getTitle().getHeight() : 0);
		offsetY += (report.getPageHeader() != null ? report.getPageHeader().getHeight() : 0);
		upColumns = offsetY;
		offsetY += (report.getColumnHeader() != null ? report.getColumnHeader().getHeight() : 0);

		JRGroup group = null;
		JRGroup[] groups = report.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				group = groups[i];

				offsetY += (group.getGroupHeader() != null ? group.getGroupHeader().getHeight() : 0);
			}
		}

		offsetY += (report.getDetail() != null ? report.getDetail().getHeight() : 0);

		if (groups != null && groups.length > 0)
		{
			for(int i = groups.length - 1; i >= 0; i--)
			{
				group = groups[i];

				offsetY += (group.getGroupFooter() != null ? group.getGroupFooter().getHeight() : 0);
			}
		}

		offsetY += (report.getColumnFooter() != null ? report.getColumnFooter().getHeight() : 0);
		downColumns = offsetY;
		offsetY += (report.getPageFooter() != null ? report.getPageFooter().getHeight() : 0);
		offsetY += (report.getLastPageFooter() != null ? report.getLastPageFooter().getHeight() : 0);
		offsetY += (report.getSummary() != null ? report.getSummary().getHeight() : 0);
		offsetY += report.getBottomMargin();
	}

	
	/**
	 * Returns the report for which the renderer has been created.
	 * 
	 * @return the renderer report
	 */
	public JRReport getReport()
	{
		return report;
	}

	
	/**
	 * A 2D dimension.
	 * 
	 * @see JRDesignRenderer#getDimension(float)
	 */
	public static class RenderDimesion
	{
		/**
		 * The width.
		 */
		public final int width;
		
		/**
		 * The height.
		 */
		public final int height;
		
		/**
		 * Creates a dimension.
		 * 
		 * @param width the width
		 * @param height the height
		 */
		public RenderDimesion(int width, int height)
		{
			this.width = width;
			this.height = height;
		}
	}
	
	
	/**
	 * Returns the dimension of the design preview for a specified zoom.
	 * 
	 * @param zoom the zoom level
	 * @return the dimension of the preview
	 */
	public RenderDimesion getDimension(float zoom)
	{
		RenderDimesion dim = new RenderDimesion(
				(int)(report.getPageWidth() * zoom) + 1, // 1 extra for the image
				(int)(offsetY * zoom) + 1
				);
		return dim;
	}
	
	
	/**
	 * Renders a design preview on an image.
	 * 
	 * @param zoom the zoom level to be used while rendering
	 * @return an image on which the design preview has been rendered.
	 * The image dimension is calculated using {@link #getDimension(float) getDimension(zoom)}.
	 */
	public Image renderToImage(float zoom)
	{
		RenderDimesion dim = getDimension(zoom);
		Image designImage = new BufferedImage(
			dim.width,
			dim.height,
			BufferedImage.TYPE_INT_RGB
			);
		Graphics2D grx = (Graphics2D)designImage.getGraphics();

		grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		grx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		grx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		AffineTransform atrans = new AffineTransform();
		atrans.scale(zoom, zoom);
		grx.transform(atrans);

		render(grx, zoom);

		grx.dispose();
		
		return designImage;
	}

	
	/**
	 * Renders a design preview.
	 * 
	 * @param grx a graphics instance to render on
	 */
	public void render(Graphics2D grx)
	{
		render(grx, 1);
	}

	
	/**
	 * Renders a design preview.
	 * 
	 * @param grx a graphics instance to render on
	 * @param zoom the zoom level to be used while rendering
	 */
	public void render(Graphics2D grx, float zoom)
	{
		setZoom(zoom);
		try
		{
			renderDesign(grx);
		}
		finally
		{
			resetZoom();
		}
	}

	protected void renderDesign(Graphics2D grx)
	{
		Stroke dashedStroke =
			new BasicStroke(
				1f / getZoom(),
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL,
				0f,
				new float[]{5f, 3f},
				0f
				);
		Color dashedColor = new Color(170, 170, 255);

		grx.setColor(Color.white);
		grx.fillRect(
			0, 
			0, 
			report.getPageWidth() + 1,
			offsetY + 1
			);

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			report.getLeftMargin(), 
			0,
			report.getLeftMargin(), 
			offsetY + 1
			);
		grx.drawLine(
			(report.getPageWidth() - report.getRightMargin()), 
			0,
			(report.getPageWidth() - report.getRightMargin()), 
			offsetY + 1
			);
		grx.drawLine(
			(report.getLeftMargin() + report.getColumnWidth()), 
			upColumns,
			(report.getLeftMargin() + report.getColumnWidth()), 
			downColumns
			);
		grx.drawLine(
			(report.getLeftMargin() + report.getColumnWidth() + report.getColumnSpacing()), 
			upColumns,
			(report.getLeftMargin() + report.getColumnWidth() + report.getColumnSpacing()), 
			downColumns
			);


		grx.translate(
			report.getLeftMargin(), 
			report.getTopMargin()
			);

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getTitle(), grx);
		grx.translate(
			0, 
			(report.getTitle() != null ? report.getTitle().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getPageHeader(), grx);
		grx.translate(
			0, 
			(report.getPageHeader() != null ? report.getPageHeader().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getColumnHeader(), grx);
		grx.translate(
			0, 
			(report.getColumnHeader() != null ? report.getColumnHeader().getHeight() : 0)
			);


		JRGroup group = null;
		JRGroup[] groups = report.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				group = groups[i];

				grx.setStroke(dashedStroke);
				grx.setColor(dashedColor);
				grx.drawLine(
					- report.getLeftMargin(), 
					0,
					report.getPageWidth() + 1, 
					0
					);
				grx.setColor(Color.black);
				grx.setStroke(new BasicStroke(1f));
				printBand(group.getGroupHeader(), grx);
				grx.translate(
					0, 
					(group.getGroupHeader() != null ? group.getGroupHeader().getHeight() : 0)
					);
			}
		}

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getDetail(), grx);
		grx.translate(
			0, 
			(report.getDetail() != null ? report.getDetail().getHeight() : 0)
			);

		if (groups != null && groups.length > 0)
		{
			for(int i = groups.length - 1; i >= 0; i--)
			{
				group = groups[i];

				grx.setStroke(dashedStroke);
				grx.setColor(dashedColor);
				grx.drawLine(
					- report.getLeftMargin(), 
					0,
					report.getPageWidth() + 1, 
					0
					);
				grx.setColor(Color.black);
				grx.setStroke(new BasicStroke(1f));
				printBand(group.getGroupFooter(), grx);
				grx.translate(
					0, 
					(group.getGroupFooter() != null ? group.getGroupFooter().getHeight() : 0)
					);
			}
		}

		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getColumnFooter(), grx);
		grx.translate(
			0, 
			(report.getColumnFooter() != null ? report.getColumnFooter().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getPageFooter(), grx);
		grx.translate(
			0, 
			(report.getPageFooter() != null ? report.getPageFooter().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getLastPageFooter(), grx);
		grx.translate(
			0, 
			(report.getLastPageFooter() != null ? report.getLastPageFooter().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1f));
		printBand(report.getSummary(), grx);
		grx.translate(
			0, 
			(report.getSummary() != null ? report.getSummary().getHeight() : 0)
			);


		grx.setStroke(dashedStroke);
		grx.setColor(dashedColor);
		grx.drawLine(
			- report.getLeftMargin(), 
			0,
			report.getPageWidth() + 1, 
			0
			);
	}

	protected void printBand(JRBand band, Graphics2D grx)
	{
		if (band != null)
		{
			printElements(band.getElements(), grx);
		}
	}


	protected void printElements(JRElement[] elements, Graphics2D grx)
	{
		if (elements != null && elements.length > 0)
		{
			for(int i = 0; i < elements.length; i++)
			{
				JRElement element = elements[i];

				if (element instanceof JRBreak)
				{
					printBreak((JRBreak)element, grx);
				}
				else if (element instanceof JRLine)
				{
					printLine((JRLine)element, grx);
				}
				else if (element instanceof JRRectangle)
				{
					printRectangle((JRRectangle)element, grx);
				}
				else if (element instanceof JREllipse)
				{
					printEllipse((JREllipse)element, grx);
				}
				else if (element instanceof JRImage)
				{
					printImage((JRImage)element, grx);
				}
				else if (element instanceof JRStaticText)
				{
					printText((JRTextElement)element, grx);
				}
				else if (element instanceof JRTextField)
				{
					printText((JRTextElement)element, grx);
				}
				else if (element instanceof JRSubreport)
				{
					printSubreport((JRSubreport)element, grx);
				}
				else if (element instanceof JRChart)
				{
					printChart((JRChart)element, grx);
				}
				else if (element instanceof JRCrosstab)
				{
					printCrosstab((JRCrosstab)element, grx);
				}
				else if (element instanceof JRFrame)
				{
					printFrame((JRFrame) element, grx);
				}
			}
		}
	}
	
	protected void printBreak(JRBreak breakElement, Graphics2D grx)
	{
		grx.setColor(breakElement.getForecolor());

		grx.setStroke(getStroke(JRGraphicElement.PEN_DOTTED));
		
		grx.drawLine(
				0, 
				breakElement.getY(),
				report.getColumnWidth(), 
				breakElement.getY()
				);
	}
	
	protected static Stroke getStroke(byte pen)
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
	
	protected void printLine(JRLine line, Graphics2D grx)
	{
		grx.setColor(line.getForecolor());

		Stroke stroke = getStroke(line.getPen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			grx.translate(.5, .5);
			
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				grx.drawLine(
					line.getX(), 
					line.getY(),
					line.getX() + line.getWidth() - 1,  
					line.getY() + line.getHeight() - 1
					);
			}
			else
			{
				grx.drawLine(
					line.getX(), 
					line.getY() + line.getHeight() - 1,
					line.getX() + line.getWidth() - 1,  
					line.getY()
					);
			}
			
			grx.translate(-.5, -.5);
		}
	}
	
	protected void printRectangle(JRRectangle rectangle, Graphics2D grx)
	{
		if (rectangle.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(rectangle.getBackcolor());

			if (rectangle.getRadius() > 0)
			{
				grx.fillRoundRect(
						rectangle.getX(), 
						rectangle.getY(), 
						rectangle.getWidth(),
						rectangle.getHeight(),
						2 * rectangle.getRadius(),
						2 * rectangle.getRadius()
						);
			}
			else
			{
				grx.fillRect(
						rectangle.getX(), 
						rectangle.getY(), 
						rectangle.getWidth(),
						rectangle.getHeight()
						);
			}
		}

		grx.setColor(rectangle.getForecolor());

		byte pen = rectangle.getPen();
		Stroke stroke = getStroke(pen);

		if (stroke != null)
		{
			double cornerOffset = getBorderCornerOffset(pen);
			int sizeAdjust = getRectangleSizeAdjust(pen);
			
			AffineTransform transform = grx.getTransform();
			
			grx.translate(rectangle.getX() + cornerOffset, rectangle.getY() + cornerOffset);
			if (pen == JRGraphicElement.PEN_THIN)
			{
				grx.scale((rectangle.getWidth() - .5) / rectangle.getWidth(), (rectangle.getHeight() - .5) / rectangle.getHeight());
			}
			
			grx.setStroke(stroke);
	
			if (rectangle.getRadius() > 0)
			{
				grx.drawRoundRect(
						0, 
						0, 
						rectangle.getWidth() - sizeAdjust,
						rectangle.getHeight() - sizeAdjust,
						2 * rectangle.getRadius(),
						2 * rectangle.getRadius()
						);
			}
			else
			{
				grx.drawRect(
						0, 
						0, 
						rectangle.getWidth() - sizeAdjust,
						rectangle.getHeight() - sizeAdjust
						);
			}
			
			grx.setTransform(transform);
		}
	}
	
	protected double getBorderCornerOffset(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_THIN :
			{
				return THIN_CORNER_OFFSET;
			}
			case JRGraphicElement.PEN_1_POINT :
			case JRGraphicElement.PEN_DOTTED :
			{
				return ONE_POINT_CORNER_OFFSET;
			}
			default :
			{
				return 0;
			}
		}
	}
	
	protected int getRectangleSizeAdjust(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_1_POINT:
			case JRGraphicElement.PEN_DOTTED:
				return 1;
			default:
				return 0;
		}
	}
	
	protected void printEllipse(JREllipse ellipse, Graphics2D grx)
	{
		if (ellipse.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(ellipse.getBackcolor());

			grx.fillOval(
				ellipse.getX(), 
				ellipse.getY(), 
				ellipse.getWidth(),
				ellipse.getHeight()
				);
		}

		grx.setColor(ellipse.getForecolor());

		byte pen = ellipse.getPen();
		Stroke stroke = getStroke(pen);

		if (stroke != null)
		{
			double cornerOffset = getBorderCornerOffset(pen);
			int sizeAdjust = getRectangleSizeAdjust(pen);
			
			AffineTransform transform = grx.getTransform();
			
			grx.translate(ellipse.getX() + cornerOffset, ellipse.getY() + cornerOffset);
			if (pen == JRGraphicElement.PEN_THIN)
			{
				grx.scale((ellipse.getWidth() - .5) / ellipse.getWidth(), (ellipse.getHeight() - .5) / ellipse.getHeight());
			}
			
			grx.setStroke(stroke);
	
			grx.drawOval(
				0, 
				0, 
				ellipse.getWidth() - sizeAdjust,
				ellipse.getHeight() - sizeAdjust
				);
			
			grx.setTransform(transform);
		}
	}
	
	protected void printImage(JRImage jrImage, Graphics2D grx)
	{
		if (jrImage.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(jrImage.getBackcolor());

			grx.fillRect(
				jrImage.getX(), 
				jrImage.getY(), 
				jrImage.getWidth(),
				jrImage.getHeight()
				);
		}

		int topPadding = jrImage.getTopPadding();
		int leftPadding = jrImage.getLeftPadding();
		int bottomPadding = jrImage.getBottomPadding();
		int rightPadding = jrImage.getRightPadding();
		
		int availableImageWidth = jrImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = jrImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;
		
		if (availableImageWidth > 0 && availableImageHeight > 0)
		{
			Image awtImage = null;
			
			JRExpression jrExpression = jrImage.getExpression();
			if (jrExpression != null && jrExpression.getChunks().length == 1)
			{
				JRExpressionChunk firstChunk = jrExpression.getChunks()[0];
				if (firstChunk.getType() == JRExpressionChunk.TYPE_TEXT)
				{
					String location = firstChunk.getText().trim();
					if (location.startsWith("\"") && location.endsWith("\""))
					{
						location = location.substring(1, location.length() - 1);
						try
						{
							awtImage = JRImageLoader.loadImage(
								JRLoader.loadBytesFromLocation(location)
								);
						}
						catch (JRException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
	
			if (awtImage != null)
			{
				int awtWidth = awtImage.getWidth(null);
				int awtHeight = awtImage.getHeight(null);

				float xalignFactor = 0f;
				switch (jrImage.getHorizontalAlignment())
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
				switch (jrImage.getVerticalAlignment())
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

				switch (jrImage.getScaleImage())
				{
					case JRImage.SCALE_IMAGE_CLIP :
					{
						int xoffset = (int)(xalignFactor * (availableImageWidth - awtWidth));
						int yoffset = (int)(yalignFactor * (availableImageHeight - awtHeight));

						Shape clip = grx.getClip();
						grx.clipRect(
							jrImage.getX() + leftPadding, 
							jrImage.getY() + topPadding, 
							availableImageWidth, 
							availableImageHeight
							);
						grx.drawImage(
							awtImage, 
							jrImage.getX() + leftPadding + xoffset, 
							jrImage.getY() + topPadding + yoffset, 
							awtWidth, 
							awtHeight, 
							imageObserver
							);
						grx.setClip(clip);
		
						break;
					}
					case JRImage.SCALE_IMAGE_FILL_FRAME :
					{
						grx.drawImage(
							awtImage, 
							jrImage.getX() + leftPadding, 
							jrImage.getY() + topPadding, 
							availableImageWidth, 
							availableImageHeight,
							imageObserver
							);
		
						break;
					}
					case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
					default :
					{
						if (jrImage.getHeight() > 0)
						{
							double ratio = (double)awtWidth / (double)awtHeight;
							
							if( ratio > (double)availableImageWidth / (double)availableImageHeight )
							{
								awtWidth = availableImageWidth; 
								awtHeight = (int)(availableImageWidth / ratio); 
							}
							else
							{
								awtWidth = (int)(availableImageHeight * ratio); 
								awtHeight = availableImageHeight; 
							}

							int xoffset = (int)(xalignFactor * (availableImageWidth - awtWidth));
							int yoffset = (int)(yalignFactor * (availableImageHeight - awtHeight));

							grx.drawImage(
								awtImage, 
								jrImage.getX() + leftPadding + xoffset, 
								jrImage.getY() + topPadding + yoffset, 
								awtWidth, 
								awtHeight, 
								imageObserver
								);
						}
						
						break;
					}
				}
			}
			else
			{
				try
				{
					awtImage = JRImageLoader.getImage(JRImageLoader.NO_IMAGE);
				}
				catch (JRException e)
				{
					e.printStackTrace();
				}
	
				Shape clip = grx.getClip();
				grx.clipRect(
					jrImage.getX() + leftPadding, 
					jrImage.getY() + topPadding, 
					availableImageWidth, 
					availableImageHeight
					);
				grx.drawImage(
					awtImage, 
					jrImage.getX() + leftPadding + 2, 
					jrImage.getY() + topPadding + 2, 
					awtImage.getWidth(null), 
					awtImage.getHeight(null), 
					imageObserver
					);
				grx.setClip(clip);

				//borderOffset = 0;
				//stroke = new BasicStroke(1f / zoom);
			}
		}
		
		if (
			jrImage.getTopBorder() == JRGraphicElement.PEN_NONE &&
			jrImage.getLeftBorder() == JRGraphicElement.PEN_NONE &&
			jrImage.getBottomBorder() == JRGraphicElement.PEN_NONE &&
			jrImage.getRightBorder() == JRGraphicElement.PEN_NONE
			)
		{
			if (jrImage.getPen() != JRGraphicElement.PEN_NONE)
			{
				JRBox box = new JRBaseBox(jrImage.getPen(), jrImage.getForecolor());
				printBox(box, jrImage, grx);
			}
		}
		else
		{
			/*   */
			printBox(
				jrImage,
				jrImage,
				grx
				);
		}
	}

	protected void printBox(JRBox box, JRElement element, Graphics2D grx)
	{
		printBox(box, element.getForecolor(), element.getX(), element.getY(), element.getWidth(), element.getHeight(), grx);
	}

	protected void printBox(JRBox box, Color defaultBorderColor, int x, int y, int width, int height, Graphics2D grx)
	{
		Stroke topStroke = null;
		Stroke leftStroke = null;
		Stroke bottomStroke = null;
		Stroke rightStroke = null;
		if (box != null)
		{
			topStroke = getBorderStroke(box.getTopBorder());
			leftStroke = getBorderStroke(box.getLeftBorder());
			bottomStroke = getBorderStroke(box.getBottomBorder());
			rightStroke = getBorderStroke(box.getRightBorder());
		}

		if (topStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getTopBorder());
			
			grx.setStroke(topStroke);
			grx.setColor(box.getTopBorderColor() == null ? defaultBorderColor : box.getTopBorderColor());
	
			grx.translate(0, cornerOffset);
			grx.drawLine(x, y, x + width, y);
			grx.translate(0, -cornerOffset);
		}

		if (leftStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getLeftBorder());
			
			grx.setStroke(leftStroke);
			grx.setColor(box.getLeftBorderColor() == null ? defaultBorderColor : box.getLeftBorderColor());
	
			grx.translate(cornerOffset, 0);
			grx.drawLine(x, y, x, y + height);
			grx.translate(-cornerOffset, 0);
		}

		if (bottomStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getBottomBorder());
			
			grx.setStroke(bottomStroke);
			grx.setColor(box.getBottomBorderColor() == null ? defaultBorderColor : box.getBottomBorderColor());
	
			grx.translate(0, -cornerOffset);
			grx.drawLine(x, y + height, x + width, y + height); 
			grx.translate(0, cornerOffset);
		}

		if (rightStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getRightBorder());
			
			grx.setStroke(rightStroke);
			grx.setColor(box.getRightBorderColor() == null ? defaultBorderColor : box.getRightBorderColor());
	
			grx.translate(-cornerOffset, 0);
			grx.drawLine(x + width, y, x + width, y + height);
			grx.translate(cornerOffset, 0);
		}

		if (
			topStroke == null
			&& leftStroke == null
			&& bottomStroke == null
			&& rightStroke == null
			)
		{
			grx.setColor(defaultBorderColor);
			grx.setStroke(new BasicStroke(1f / getZoom()));
		
			grx.drawRect(x, y, width, height);
		}
	}
	
	protected Stroke getBorderStroke(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				return BORDER_STROKE_DOTTED;
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
				return BORDER_STROKE_THIN;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				return BORDER_STROKE_1_POINT;
			}
		}
	}

	protected void printText(JRTextElement text, Graphics2D grx)
	{
		JRStyledText styledText = getStyledText(text);
		
		if (styledText == null)
		{
			return;
		}

		String allText = styledText.getText();
		
		int x = text.getX();
		int y = text.getY();
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
				y = text.getY() + text.getHeight();
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
				x = text.getX() + text.getWidth();
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
			case JRTextElement.ROTATION_UPSIDE_DOWN :
			{
				x = text.getX() + text.getWidth();
				y = text.getY() + text.getHeight();
				int tmpPadding = topPadding;
				topPadding = bottomPadding;
				bottomPadding = tmpPadding;
				tmpPadding = leftPadding;
				leftPadding = rightPadding;
				rightPadding = tmpPadding;
				angle = Math.PI;
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

		grx.setColor(text.getForecolor());

		/*   */
		TextMeasurer textMeasurer = new TextMeasurer(text);
		textMeasurer.measure(
			styledText, 
			allText,
			0,
			0
			);
		
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
			textMeasurer.getTextHeight(), 
			text.getHorizontalAlignment(), 
			text.getVerticalAlignment(), 
			textMeasurer.getLineSpacingFactor(),
			textMeasurer.getLeadingOffset(),
			text.getFontSize(),
			text.isStyledText(),
			styledText, 
			allText
			);

		grx.rotate(-angle, x, y);

		/*   */
		printBox(
			text,
			text,
			grx
			);
	}
	
	protected JRStyledText getStyledText(JRTextElement textElement)
	{
		JRStyledText styledText = null;

		String text = null;
		if (textElement instanceof JRStaticText)
		{
			text = ((JRStaticText)textElement).getText();
		}
		else if (textElement instanceof JRTextField)
		{
			JRExpression textExpression = ((JRTextField) textElement).getExpression();
			if (textExpression != null)
			{
				text = textExpression.getText();
			}
		}
		
		if (text == null)
		{
			text = "";
		}
		
		//text = JRStringUtil.treatNewLineChars(text);

		Map attributes = new HashMap(); 
		JRFontUtil.setAttributes(attributes, textElement);
		attributes.put(TextAttribute.FOREGROUND, textElement.getForecolor());

		if (
			textElement instanceof JRStaticText
			&& textElement.isStyledText()
			)
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
		
		return styledText;
	}

	protected void printSubreport(JRSubreport subreport, Graphics2D grx)
	{
		if (subreport.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(subreport.getBackcolor());

			grx.fillRect(
				subreport.getX(), 
				subreport.getY(), 
				subreport.getWidth(),
				subreport.getHeight()
				);
		}

		Image image = null;
		try
		{
			image = JRImageLoader.getImage(JRImageLoader.SUBREPORT_IMAGE);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}

		Shape clip = grx.getClip();
		grx.clipRect(
			subreport.getX(), 
			subreport.getY(), 
			subreport.getWidth(), 
			subreport.getHeight()
			);
		grx.drawImage(
			image, 
			subreport.getX() + 2, 
			subreport.getY() + 2, 
			image.getWidth(null), 
			image.getHeight(null), 
			imageObserver
			);
		grx.setClip(clip);

		grx.setColor(subreport.getForecolor());
		grx.setStroke(new BasicStroke(1f / getZoom()));
		grx.drawRect(
			subreport.getX(), 
			subreport.getY(), 
			subreport.getWidth() - 1,
			subreport.getHeight() - 1
			);
	}

	private void printChart(JRChart chart, Graphics2D grx)
	{
		if (chart.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(chart.getBackcolor());

			grx.fillRect(
				chart.getX(), 
				chart.getY(), 
				chart.getWidth(),
				chart.getHeight()
				);
		}

		Image image = null;
		try
		{
			image = JRImageLoader.getImage(JRImageLoader.CHART_IMAGE);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}

		grx.setClip(
			chart.getX(), 
			chart.getY(), 
			chart.getWidth(), 
			chart.getHeight()
			);
		grx.drawImage(
			image, 
			chart.getX() + 2, 
			chart.getY() + 2, 
			image.getWidth(null), 
			image.getHeight(null), 
			imageObserver
			);
		grx.setClip(
			- report.getLeftMargin(), 
			0, 
			report.getPageWidth(), 
			report.getPageHeight()
			);

		grx.setColor(chart.getForecolor());
		grx.setStroke(new BasicStroke(1f / getZoom()));
		grx.drawRect(
			chart.getX(), 
			chart.getY(), 
			chart.getWidth() - 1,
			chart.getHeight() - 1
			);
	}


	protected void printFrame(JRFrame frame, Graphics2D grx)
	{
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(frame.getBackcolor());

			grx.fillRect(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
		}
		
		int topPadding = frame.getTopPadding();
		int leftPadding = frame.getLeftPadding();

		grx.translate(frame.getX() + leftPadding, frame.getY() + topPadding);
		printElements(frame.getElements(), grx);
		grx.translate(-(frame.getX() + leftPadding), -(frame.getY() + topPadding));
		
		printBox(frame, frame, grx);
	}

	protected void printCrosstab(JRCrosstab crosstab, Graphics2D grx)
	{
		grx.setClip(
				crosstab.getX(), 
				crosstab.getY(), 
				crosstab.getWidth(), 
				crosstab.getHeight()
				);
		
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		int rowHeadersXOffset = 0;
		for (int i = 0; i < rowGroups.length; i++)
		{
			rowHeadersXOffset += rowGroups[i].getWidth();
		}
		
		JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
		int colHeadersYOffset = 0;
		for (int i = 0; i < columnGroups.length; i++)
		{
			colHeadersYOffset += columnGroups[i].getHeight();
		}
		
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			grx.translate(crosstab.getX(), crosstab.getY());
			printCellContents(headerCell, grx, 0, 0, false, false);
			grx.translate(-crosstab.getX(), -crosstab.getY());
		}
		
		grx.translate(crosstab.getX() + rowHeadersXOffset, crosstab.getY());
		printCrosstabColumnHeaders(crosstab, grx);
		grx.translate(-(crosstab.getX() + rowHeadersXOffset), -crosstab.getY());
		
		grx.translate(crosstab.getX(), crosstab.getY() + colHeadersYOffset);
		printCrosstabRows(crosstab, grx, rowHeadersXOffset);
		grx.translate(-crosstab.getX(), -(crosstab.getY() + colHeadersYOffset));
		
		grx.setClip(
				- report.getLeftMargin(), 
				0, 
				report.getPageWidth(), 
				report.getPageHeight()
				);
	}
	
	protected void printCellContents(JRCellContents cell, Graphics2D grx, int x, int y, boolean left, boolean top)
	{
		if (cell.getWidth() == 0 || cell.getHeight() == 0)
		{
			return;
		}
		
		JRDesignFrame frame = createCrosstabCellFrame(cell, x, y, left, top);
		printFrame(frame, grx);
	}

	protected JRDesignFrame createCrosstabCellFrame(JRCellContents cell, int x, int y, boolean left, boolean top)
	{
		JRDesignFrame frame = new JRDesignFrame(cell.getDefaultStyleProvider());
		frame.setX(x);
		frame.setY(y);
		frame.setWidth(cell.getWidth());
		frame.setHeight(cell.getHeight());
		
		frame.setMode(cell.getMode());
		frame.setBackcolor(cell.getBackcolor());
		frame.setStyle(cell.getStyle());
		
		JRBox box = cell.getBox();
		if (box != null)
		{
			frame.setBox(box);
			
			boolean copyLeft = left && box.getLeftBorder() == JRGraphicElement.PEN_NONE && box.getRightBorder() != JRGraphicElement.PEN_NONE;
			boolean copyTop = top && box.getTopBorder() == JRGraphicElement.PEN_NONE && box.getBottomBorder() != JRGraphicElement.PEN_NONE;
			
			if (copyLeft)
			{
				frame.setLeftBorder(box.getRightBorder());
				frame.setLeftBorderColor(box.getRightBorderColor());
			}
			
			if (copyTop)
			{
				frame.setTopBorder(box.getBottomBorder());
				frame.setTopBorderColor(box.getBottomBorderColor());
			}
		}
		
		List children = cell.getChildren();
		if (children != null)
		{
			for (Iterator it = children.iterator(); it.hasNext();)
			{
				JRChild child = (JRChild) it.next();
				if (child instanceof JRElement)
				{
					frame.addElement((JRElement) child);
				}
				else if (child instanceof JRElementGroup)
				{
					frame.addElementGroup((JRElementGroup) child);
				}
			}
		}
		
		return frame;
	}
	
	protected void printCrosstabColumnHeaders(JRCrosstab crosstab, Graphics2D grx)
	{
		JRCrosstabColumnGroup[] groups = crosstab.getColumnGroups();
		for (int i = 0, x = 0, y = 0; i < groups.length; i++)
		{
			JRCrosstabColumnGroup group = groups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x, y, x == 0 && crosstab.getHeaderCell() == null, false);
				x += totalHeader.getWidth();
			}
			
			JRCellContents header = group.getHeader();
			printCellContents(header, grx, x, y, x == 0 && crosstab.getHeaderCell() == null, false);
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x + header.getWidth(), y, false, false);
			}
			
			y += group.getHeight();
		}
	}

	protected void printCrosstabRows(JRCrosstab crosstab, Graphics2D grx, int rowHeadersXOffset)
	{
		JRCrosstabRowGroup[] groups = crosstab.getRowGroups();
		for (int i = 0, x = 0, y = 0; i < groups.length; i++)
		{
			JRCrosstabRowGroup group = groups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x, y, false, y == 0 && crosstab.getHeaderCell() == null);
				printCrosstabDataCellsRow(crosstab, grx, rowHeadersXOffset, y, i);
				y += totalHeader.getHeight();
			}
			
			JRCellContents header = group.getHeader();
			printCellContents(header, grx, x, y, false, y == 0 && crosstab.getHeaderCell() == null);
			
			if (i == groups.length - 1)
			{
				printCrosstabDataCellsRow(crosstab, grx, rowHeadersXOffset, y, groups.length);				
			}
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				printCellContents(totalHeader, grx, x, y + header.getHeight(), false, false);
				printCrosstabDataCellsRow(crosstab, grx, rowHeadersXOffset, y + header.getHeight(), i);
			}
			
			x += group.getWidth();
		}
	}

	protected void printCrosstabDataCellsRow(JRCrosstab crosstab, Graphics2D grx, int rowOffsetX, int rowOffsetY, int rowIndex)
	{
		grx.translate(rowOffsetX, rowOffsetY);
		
		JRCrosstabColumnGroup[] colGroups = crosstab.getColumnGroups();
		JRCrosstabCell[][] cells = crosstab.getCells();
		for (int i = 0, x = 0; i < colGroups.length; i++)
		{
			JRCrosstabColumnGroup group = colGroups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				printCellContents(cells[rowIndex][i].getContents(), grx, x, 0, false, false);
				x += cells[rowIndex][i].getContents().getWidth();
			}
			
			if (i == colGroups.length - 1)
			{
				printCellContents(cells[rowIndex][colGroups.length].getContents(), grx, x, 0, false, false);
			}
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				printCellContents(cells[rowIndex][i].getContents(), grx, x + group.getHeader().getWidth(), 0, false, false);
			}
		}
		
		grx.translate(-rowOffsetX, -rowOffsetY);
	}
	
	private static final ThreadLocal threadZoom = new ThreadLocal();
	
	protected void setZoom(float zoom)
	{
		threadZoom.set(new Float(zoom));
	}
	
	protected float getZoom()
	{
		return ((Float) threadZoom.get()).floatValue();
	}
	
	protected void resetZoom()
	{
		threadZoom.remove();
	}
}
