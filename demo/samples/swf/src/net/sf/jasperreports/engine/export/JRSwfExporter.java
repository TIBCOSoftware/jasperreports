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

/*
 * Contributors:
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 * Ling Li - lonecatz@users.sourceforge.net
 * Martin Clough - mtclough@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

import com.iv.flash.api.AlphaColor;
import com.iv.flash.api.FlashFile;
import com.iv.flash.api.Frame;
import com.iv.flash.api.Instance;
import com.iv.flash.api.Matrix;
import com.iv.flash.api.Rect;
import com.iv.flash.api.Script;
import com.iv.flash.api.image.Bitmap;
import com.iv.flash.api.image.LLBitmap;
import com.iv.flash.api.shape.FillStyle;
import com.iv.flash.api.shape.LineStyle;
import com.iv.flash.api.shape.Shape;
import com.iv.flash.api.text.Font;
import com.iv.flash.api.text.FontDef;
import com.iv.flash.api.text.Text;
import com.iv.flash.api.text.TextItem;
import com.iv.flash.util.FlashBuffer;
import com.iv.flash.util.FlashOutput;
import com.iv.flash.util.IVException;


/**
 * Exports a JasperReports document to SWF format.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRSwfExporter extends JRAbstractExporter
{

	private static final int ALIGN_LEFT = 0;
	private static final int ALIGN_RIGHT = 1;
	private static final int ALIGN_CENTER = 2;
    private static final int ALIGN_JUSTIFY = 3;
    private static final String FONT_FILE_EXT = ".fft";

	protected static boolean fontsRegistered = false;

	/**
	 *
	 */
	protected FlashFile flashFile = null;
	protected Frame flashFrame = null;
	protected JRExportProgressMonitor progressMonitor = null;

	protected int reportIndex = 0;

    private String fontDir = "fonts";
    private Map fontMap = new HashMap();
    
	protected int zorder;

	/**
	 * if multiple pages involved, absoluteOffsetY calculates the total height of the document before the current page 
	 */
	protected int absoluteOffsetY;
	
	protected int totalHeight;
	protected int totalWidth;

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
	    reinitialize();
	    progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		try
		{
			/*   */
			setExportContext();
	
			/*   */
			setInput();
	
			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}
			
			String fontDirParameter = (String)parameters.get(JRSwfExporterParameter.SWF_FONT_DIR);
			if(fontDirParameter != null)
			{
			    fontDir = fontDirParameter;
			}
			Map fontMapParameter = (Map) parameters.get(JRExporterParameter.FONT_MAP);
			
			if(!fontsRegistered)
			{
			    registerFonts(fontMapParameter);
			}
			
//			setHyperlinkProducerFactory();

			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				exportReportToStream(os);
			}
			else
			{
				File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
				if (destFile == null)
				{
					String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
					if (fileName != null)
					{
						destFile = new File(fileName);
					}
					else
					{
						throw new JRException("No output specified for the exporter.");
					}
				}
	
				try
				{
					os = new FileOutputStream(destFile);
					exportReportToStream(os);
					os.flush();
				}
				catch (IOException e)
				{
					throw new JRException("Error trying to export to file : " + destFile, e);
				}
				finally
				{
					if (os != null)
					{
						try
						{
							os.close();
						}
						catch(IOException e)
						{
						}
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}
	}

	/**
	 *
	 */
	protected void exportReportToStream(OutputStream os) throws JRException
	{
	    flashFile = FlashFile.newFlashFile();

        Script script = new Script(1);      // create main script with 1 frame
        script.setMain();
        flashFile.setMainScript( script );       // set main script for the file
        flashFrame = script.newFrame();        // create new frame and add to the end of the timeline
		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
		    jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
			
			List pages = jasperPrint.getPages();
        	totalHeight += pages.size() * jasperPrint.getPageHeight();
        	totalWidth = Math.max(totalWidth, jasperPrint.getPageWidth());

			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.currentThread().isInterrupted())
					{
						throw new JRException("Current thread interrupted.");
					}
					JRPrintPage page = (JRPrintPage)pages.get(pageIndex);

					/*   */
					exportPage(page);
					absoluteOffsetY += jasperPrint.getPageHeight();
				}
			}
			else
			{
				//TODO: empty documents
			}
		}
        flashFile.setFrameSize(new Rect(0, 0, twip(totalWidth), twip(totalHeight)));

		try
		{
			FlashOutput flashOutput = flashFile.generate();
			os.write(flashOutput.getBuf(), 0, flashOutput.getSize());
			os.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				os.close();
			}
			catch(IOException e)
			{
			}
		}
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException
	{
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
		if (line.getPen() != JRGraphicElement.PEN_NONE)
		{
	        // create shape rectangle
	        Shape shape = new Shape();                                      
			LineStyle lineStyle = new LineStyle();
			Color forecolor = line.getForecolor();
			lineStyle.setColor(getAlphaColor(forecolor));
			lineStyle.setWidth(getLineWidth(line.getPen()));
			
			shape.setLineStyle(lineStyle);
			
			int x0,y0,x,y = 0;
			int lineHeight = line.getHeight() == 1 ? 0 : line.getHeight();
			int lineWidth = line.getWidth() == 1 ? 0 : line.getWidth();
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				x0 = twip(line.getX() + getOffsetX());
				y0 = twip(absoluteOffsetY + line.getY() + getOffsetY());
				x = twip(line.getX() + getOffsetX() + lineWidth);
				y = twip(absoluteOffsetY + line.getY() + getOffsetY() + lineHeight);
				
				shape.moveTo(x0,y0);
				shape.lineTo(x,y);
			}
			else
			{
				x0 = twip(line.getX() + getOffsetX());
				y0 = twip(absoluteOffsetY + line.getY() + getOffsetY() + lineHeight);
				x = twip(line.getX() + getOffsetX() + lineWidth);
				y = twip(absoluteOffsetY + line.getY() + getOffsetY());
				
				shape.moveTo(x0,y0);
				shape.lineTo(x,y);
			}
			shape.setBounds(new Rect(x0,y0,x,y));
			flashFrame.addInstance(shape, zorder++ , new Matrix(), null);

		}
	}


	/**
	 *
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle)
	{
        Shape shape = new Shape(); 
        byte pen = rectangle.getPen();
        if(pen != JRGraphicElement.PEN_NONE)
        {
			LineStyle lineStyle = new LineStyle();
			lineStyle.setColor(getAlphaColor(rectangle.getForecolor()));
			lineStyle.setWidth(getLineWidth(rectangle.getPen()));
			shape.setLineStyle(lineStyle);
        }
        
        if (rectangle.getMode() == JRElement.MODE_OPAQUE)
		{
			shape.setFillStyle0(FillStyle.newSolid(getAlphaColor(rectangle.getBackcolor())));         
		}
		int x0 = twip(rectangle.getX() + getOffsetX());
		int y0 = twip(absoluteOffsetY + rectangle.getY() + getOffsetY());
		int x = twip(rectangle.getX() + getOffsetX() + rectangle.getWidth());
		int y = twip(absoluteOffsetY + rectangle.getY() + getOffsetY() + rectangle.getHeight());
		
		Rect r = new Rect(x0, y0, x, y);		// create Rect for the rectangle
		int radius = rectangle.getRadius();
		if(radius == 0)
		{
			shape.rectangle( r );                   // draw rectangle (it's rather square :))
		}
		else
		{
			radius = twip(radius);
			shape.moveTo(x0 + radius, y0);
			shape.lineTo(x - radius, y0);
			shape.curveTo(x, y0, x, y0 + radius);
			shape.lineTo(x, y - radius);
			shape.curveTo(x, y, x - radius, y);
			shape.lineTo(x0 + radius, y);
			shape.curveTo(x0, y, x0, y - radius);
			shape.lineTo(x0, y0 + radius);
			shape.curveTo(x0, y0, x0 + radius, y0);
		}
		shape.setBounds( r );                     			
		flashFrame.addInstance(shape, zorder++ , new Matrix(), null);
	}

	/**
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse)
	{
		Shape shape = new Shape(); 
        byte pen = ellipse.getPen();
        if(pen != JRGraphicElement.PEN_NONE)
        {
			LineStyle lineStyle = new LineStyle();
			lineStyle.setColor(getAlphaColor(ellipse.getForecolor()));
			lineStyle.setWidth(getLineWidth(ellipse.getPen()));
			shape.setLineStyle(lineStyle);
        }
        
        if (ellipse.getMode() == JRElement.MODE_OPAQUE)
		{
			shape.setFillStyle0(FillStyle.newSolid(getAlphaColor(ellipse.getBackcolor())));         
		}
		int x0 = twip(ellipse.getX() + getOffsetX());
		int y0 = twip(absoluteOffsetY + ellipse.getY() + getOffsetY());
		int x = twip(ellipse.getX() + getOffsetX() + ellipse.getWidth());
		int y = twip(absoluteOffsetY + ellipse.getY() + getOffsetY() + ellipse.getHeight());
		int ellipseHeight = twip(ellipse.getHeight()/2);
		int ellipseWidth = twip(ellipse.getWidth()/2);
		
		Rect r = new Rect(x0, y0, x, y);		// create Rect for the rectangle
		shape.moveTo(x0, y0 + ellipseHeight);
		shape.curveTo(x0, y0, x0 + ellipseWidth, y0);
		shape.curveTo(x, y0, x, y0 + ellipseHeight);
		shape.curveTo(x, y, x0 + ellipseWidth, y);
		shape.curveTo(x0, y, x0, y0 + ellipseHeight);
		shape.setBounds( r );                     			
		flashFrame.addInstance(shape, zorder++ , new Matrix(), null);
	}

	/**
	 *
	 */
	protected void exportText(JRPrintText text)
	{
		JRStyledText styledText = getStyledText(text, false);

		if (styledText == null)
		{
			return;
		}
		
		exportBox((JRBox)text, text);
		
		int textLength = styledText.length();
		int x = text.getX() + getOffsetX();
		int y = absoluteOffsetY + text.getY() + getOffsetY();
		int width = text.getWidth();
		int height = text.getHeight();
		int topPadding = text.getTopPadding();
		int leftPadding = text.getLeftPadding();
		int bottomPadding = text.getBottomPadding();
		int rightPadding = text.getRightPadding();

		if (textLength > 0)
		{
			int horizontalAlignment = 0;
			switch (text.getHorizontalAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_LEFT :
				{
					if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_LTR)
					{
						horizontalAlignment = JRSwfExporter.ALIGN_LEFT; 
					}
					else
					{
						horizontalAlignment = JRSwfExporter.ALIGN_RIGHT; 
					}
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER :
				{
					horizontalAlignment = JRSwfExporter.ALIGN_CENTER;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
				{
					if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_LTR)
					{
						horizontalAlignment = JRSwfExporter.ALIGN_RIGHT; 
					}
					else
					{
						horizontalAlignment = JRSwfExporter.ALIGN_LEFT; 
					}
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED : 
				{
					horizontalAlignment = JRSwfExporter.ALIGN_JUSTIFY; 
					break;
				}
				default : 
				{
					horizontalAlignment = JRSwfExporter.ALIGN_LEFT; 
				}
			}

	        // create new text block
			com.iv.flash.api.text.Text textBlock = Text.newText();
			String plainText = styledText.getText();
			
			int runLimit = 0;
			AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
			while (
				runLimit < styledText.length()
				&& (runLimit = iterator.getRunLimit()) <= styledText.length()
				)
			{

				Map styledTextAttributes = iterator.getAttributes();
				JRFont styleFont = new JRBaseFont(styledTextAttributes);
				Color styleForeground = (Color) styledTextAttributes.get(TextAttribute.FOREGROUND);
				AlphaColor alphaForeground = getAlphaColor(styleForeground);
				FontKey fontKey = new FontKey(styleFont.getFontName(), styleFont.isBold(), styleFont.isItalic());
				Font font = getFont(fontKey);
//				if(fontMap != null && fontMap.get(fontKey) != null)
//				{
//					font = (Font)fontMap.get(fontKey);
//				}
//				else
//				{
//					if (styleFont.isBold())
//					{
//						if(styleFont.isItalic())
//							font = FontDef.load(fontDir + File.separator + "ARIALBI.FFT");
//						else
//							font = FontDef.load(fontDir + File.separator + "ARIALBD.FFT");
//					}
//					else if (styleFont.isItalic())
//					{
//						font = FontDef.load(fontDir + File.separator + "ARIALI.FFT");
//					}
//					else
//					{
//						font = FontDef.load(fontDir + File.separator + "ARIAL.FFT");
//					}
//				}
		        TextItem textItem = 
		        	new TextItem(
		        		plainText.substring(iterator.getIndex(), runLimit), 
		        		font, 
		        		twip(styleFont.getFontSize()),
		        		alphaForeground
		        		);
		        
		        textItem.align = horizontalAlignment;
		        
		        //TODO: underline, strikethrough, superscript, subscript
		        
		        textBlock.addTextItem( textItem );
				iterator.setIndex(runLimit);
			}

			AffineTransform atrans = null;
			Matrix matrix = new Matrix();
			double angle = 0d;
			switch(text.getRotation())
			{
				case JRTextElement.ROTATION_LEFT :
				{
					y = absoluteOffsetY + text.getY() + getOffsetY() + text.getHeight();
					width = text.getHeight();
					height = text.getWidth();
					int tmpPadding = topPadding;
					topPadding = leftPadding;
					leftPadding = bottomPadding;
					bottomPadding = rightPadding;
					rightPadding = tmpPadding;
					angle = Math.PI / 2;
					atrans = new AffineTransform();
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
					angle = - Math.PI / 2;
					atrans = new AffineTransform();
					break;
				}
				case JRTextElement.ROTATION_UPSIDE_DOWN :
				{
					int tmpPadding = topPadding;
					x = text.getX() + getOffsetX() + text.getWidth();
					y = absoluteOffsetY + text.getY() + getOffsetY() + text.getHeight();
					topPadding = bottomPadding;
					bottomPadding = tmpPadding;
					tmpPadding = leftPadding;
					leftPadding = rightPadding;
					rightPadding = tmpPadding;
					angle = Math.PI;
					atrans = new AffineTransform();
					break;
				}
				case JRTextElement.ROTATION_NONE :
				default :
				{
				}
			
			}
			
			float verticalOffset = 0f, horizontalOffset=0f;
			switch (text.getVerticalAlignment())
			{
				case JRAlignment.VERTICAL_ALIGN_TOP :
				{
					verticalOffset = 0f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_MIDDLE :
				{
					verticalOffset = (height - topPadding - bottomPadding - text.getTextHeight()) / 2f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_BOTTOM :
				{
					verticalOffset = height - topPadding - bottomPadding - text.getTextHeight();
					break;
				}
				default :
				{
					verticalOffset = 0f;
				}
			}
			
			// do not add this to the first switch(text.getRotation())statement, 
			// because vertical offset should be calculate with modified width 
			// and height 
			switch(text.getRotation())
			{
				case JRTextElement.ROTATION_LEFT :
				{
					horizontalOffset = verticalOffset;
					verticalOffset = 0f;
					break;
				}
				case JRTextElement.ROTATION_RIGHT :
				{
					horizontalOffset = - verticalOffset;
					verticalOffset = 0f;
					break;
				}
				case JRTextElement.ROTATION_UPSIDE_DOWN :
				{
					verticalOffset = - verticalOffset;
				}
				case JRTextElement.ROTATION_NONE :
				default :
				{
				}
			
			}
			
			if(atrans != null)
			{
				atrans.rotate(angle, 0,0);
				setMatrixElements(atrans, matrix);
			}
			
			Rect rText = new Rect(0, 0, twip(width - leftPadding - rightPadding),twip(height - topPadding - bottomPadding) );
	        textBlock.setBounds(rText);
	        matrix.translate(twip(x + leftPadding + (int)horizontalOffset), twip(y + topPadding + (int)verticalOffset));
	        flashFrame.addInstance(textBlock, zorder++, matrix, null);
	        
		}
	}

	private void exportFrame(JRPrintFrame frame) throws JRException {
		exportBox((JRBox)frame,frame);
		setFrameElementsOffset(frame, false);
		exportElements(frame.getElements());
		restoreElementOffsets();
	}
	
	private int twip(int points) {
		return points * 20;
	}
	
	private int getLineWidth(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_4_POINT :
			{
				return 40;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				return 20;
			}
			case JRGraphicElement.PEN_NONE :
			{
				return 0;
			}
			case JRGraphicElement.PEN_THIN :
			{
				return 5;
			}
			case JRGraphicElement.PEN_DOTTED :
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				return 10;
			}
		}
		
	}
	
	private AlphaColor getAlphaColor(Color color)
	{
		return new AlphaColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	private void exportBox(JRBox box, JRPrintElement printElement){
		
		int x = printElement.getX() + getOffsetX();
		int y = absoluteOffsetY + printElement.getY() + getOffsetY();
		int width = printElement.getWidth();
		int height = printElement.getHeight();
		
		if (printElement.getMode() == JRElement.MODE_OPAQUE)
		{	Shape backgroundShape = new Shape();
			backgroundShape.setFillStyle0( FillStyle.newSolid(getAlphaColor(printElement.getBackcolor())));   
			Rect r = new Rect(twip(x), 
					twip(y), 
					twip(x + width), 
					twip(y + height));                // create Rect for the rectangle
			backgroundShape.rectangle( r );                                           // draw rectangle (it's rather square :))
			backgroundShape.setBounds( r );                                           // set bounding box for the shape
			flashFrame.addInstance(backgroundShape, zorder++, null, null);
			
		}
		
		if(box.getBorder() != JRGraphicElement.PEN_NONE)
		{
			Shape borderShape = new Shape();
			Color borderColor = box.getBorderColor() == null ? printElement.getForecolor() : box.getBorderColor();
			borderShape.setLineStyle( new LineStyle( getLineWidth(box.getBorder()), getAlphaColor(borderColor) ) );
			
			Rect r = new Rect(twip(x), twip(y), twip(x + width), twip(y + height));                // create Rect for the rectangle
			borderShape.rectangle( r );                                           // draw rectangle (it's rather square :))
			borderShape.setBounds( r );                                           // set bounding box for the shape
			flashFrame.addInstance(borderShape, zorder++, null, null);
		}
		else
		{
			if (box.getTopBorder() != JRGraphicElement.PEN_NONE) {
				Shape topBorderShape = new Shape();
				Color topBorderColor = box.getTopBorderColor() == null ? printElement.getForecolor() : box.getTopBorderColor();
				topBorderShape.setLineStyle( new LineStyle(getLineWidth(box.getTopBorder()), getAlphaColor(topBorderColor)));
				topBorderShape.setBounds(new Rect(twip(x), twip(y), twip(x+width), twip(y)));
				topBorderShape.moveTo(twip(x),twip(y));
				topBorderShape.lineTo(twip(x+width), twip(y));
				flashFrame.addInstance(topBorderShape, zorder++, null, null);
			}
			if (box.getLeftBorder() != JRGraphicElement.PEN_NONE)
			{
				Shape leftBorderShape = new Shape();
				Color leftBorderColor = box.getLeftBorderColor() == null ? printElement.getForecolor() : box.getLeftBorderColor();
				leftBorderShape.setLineStyle( new LineStyle(getLineWidth(box.getLeftBorder()), getAlphaColor(leftBorderColor)));
				leftBorderShape.setBounds(new Rect(twip(x), twip(y), twip(x), twip(y+height)));
				leftBorderShape.moveTo(twip(x),twip(y));
				leftBorderShape.lineTo(twip(x), twip(y+height));
				flashFrame.addInstance(leftBorderShape, zorder++, null, null);
			}
	
			if (box.getBottomBorder() != JRGraphicElement.PEN_NONE)
			{
				Shape bottomBorderShape = new Shape();
				Color bottomBorderColor = box.getBottomBorderColor() == null ? printElement.getForecolor() : box.getBottomBorderColor();
				bottomBorderShape.setLineStyle( new LineStyle(getLineWidth(box.getBottomBorder()), getAlphaColor(bottomBorderColor)));
				bottomBorderShape.setBounds(new Rect(twip(x), twip(y+height), twip(x+width), twip(y+height)));
				bottomBorderShape.moveTo(twip(x),twip(y+height));
				bottomBorderShape.lineTo(twip(x+width), twip(y+height));
				flashFrame.addInstance(bottomBorderShape, zorder++, null, null);
			}
	
			if (box.getRightBorder() != JRGraphicElement.PEN_NONE)
			{
				Shape rightBorderShape = new Shape();
				Color rightBorderColor = box.getRightBorderColor() == null ? printElement.getForecolor() : box.getRightBorderColor();
				rightBorderShape.setLineStyle( new LineStyle(getLineWidth(box.getRightBorder()), getAlphaColor(rightBorderColor)));
				rightBorderShape.setBounds(new Rect(twip(x + width), twip(y), twip(x+width), twip(y+height)));
				rightBorderShape.moveTo(twip(x + width),twip(y));
				rightBorderShape.lineTo(twip(x+width), twip(y+height));
				flashFrame.addInstance(rightBorderShape, zorder++, null, null);
			}
		}
	}
	
	private float getXAlignFactor(JRPrintImage printImage) {
		float xalignFactor = 0f;
		switch (printImage.getHorizontalAlignment()) {
		case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
			xalignFactor = 1f;
			break;
		case JRAlignment.HORIZONTAL_ALIGN_CENTER:
			xalignFactor = 0.5f;
			break;
		case JRAlignment.HORIZONTAL_ALIGN_LEFT: 
		default:
			xalignFactor = 0f;
		}
		return xalignFactor;
	}

	private float getYAlignFactor(JRPrintImage printImage) {
		float yalignFactor = 0f;
		switch (printImage.getVerticalAlignment()) {
		case JRAlignment.VERTICAL_ALIGN_BOTTOM:
			yalignFactor = 1f;
			break;
		case JRAlignment.VERTICAL_ALIGN_MIDDLE:
			yalignFactor = 0.5f;
			break;
		case JRAlignment.VERTICAL_ALIGN_TOP:
		default:
			yalignFactor = 0f;
		}
		return yalignFactor;
	}
	
    protected void exportImage(JRPrintImage printImage) throws JRException {
        exportBox((JRBox)printImage, printImage);
        int topPadding = printImage.getTopPadding();
        int leftPadding = printImage.getLeftPadding();
        int bottomPadding = printImage.getBottomPadding();
        int rightPadding = printImage.getRightPadding();

        int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
        availableImageWidth = (availableImageWidth < 0) ? 0 : availableImageWidth;

        int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
        availableImageHeight = (availableImageHeight < 0) ? 0 : availableImageHeight;

        JRRenderable renderer = printImage.getRenderer();
        
        if (renderer != null && availableImageWidth > 0 && availableImageHeight > 0) {
            Bitmap bm;
            Instance instance;
            int xoffset = 0;
            int yoffset = 0;
            if (renderer.getType() == JRRenderable.TYPE_IMAGE) {
                float xalignFactor = getXAlignFactor(printImage);
                float yalignFactor = getYAlignFactor(printImage);
                int normalWidth = availableImageWidth;
                int normalHeight = availableImageHeight;
            
                Dimension2D dimension = renderer.getDimension();
                if (dimension != null) {
                    normalWidth = (int) dimension.getWidth();
                    normalHeight = (int) dimension.getHeight();
                }

                xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
                yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));

                int minWidth = Math.min(normalWidth, availableImageWidth);
                int minHeight = Math.min(normalHeight, availableImageHeight);
            
                BufferedImage bi = new BufferedImage(minWidth, minHeight, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g = bi.createGraphics();
                if (printImage.getMode() == JRElement.MODE_OPAQUE) {
                    g.setColor(printImage.getBackcolor());
                    g.fillRect(0, 0, minWidth, minHeight);
                }
                renderer.render(g,
                    new Rectangle(
                        (xoffset > 0 ? 0 : xoffset), 
                        (yoffset > 0 ? 0 : yoffset),
                        normalWidth,
                        normalHeight
                    ) 
                );
                g.dispose();

                xoffset = (xoffset < 0 ? 0 : xoffset);
                yoffset = (yoffset < 0 ? 0 : yoffset);
                
                byte[] byteArr = JRImageLoader.loadImageDataFromAWTImage(bi,JRRenderable.IMAGE_TYPE_PNG);
                
                switch(printImage.getScaleImage()) {
                    case JRImage.SCALE_IMAGE_CLIP: {

                        try {
                            bm = LLBitmap.newBitmap(new FlashBuffer(byteArr));
                            instance = bm.newInstance();
                            AffineTransform atrans =AffineTransform.getTranslateInstance(
                                    getOffsetX() + twip(printImage.getX() + printImage.getLeftPadding() + xoffset),
                                    twip(absoluteOffsetY) + getOffsetY() + twip(printImage.getY() + printImage.getTopPadding() + yoffset));
                            setMatrixElements(atrans, instance.matrix);
                        } catch (IVException e) {
                            throw new JRException(e);
                        }
                        break;
                    }
                    case JRImage.SCALE_IMAGE_FILL_FRAME:
                        try {
                            bm = LLBitmap.newBitmap(new FlashBuffer(byteArr));
                            //bm = LLBitmap.newBitmap(new FlashBuffer(baos.toByteArray()));
                        } catch (IVException e) {
                            throw new JRException(e);
                        }
                        instance = bm.newInstance();
                        AffineTransform atrans = AffineTransform.getTranslateInstance(
                                getOffsetX() + twip(printImage.getX() + printImage.getLeftPadding()),
                                twip(absoluteOffsetY) + getOffsetY() + twip(printImage.getY() + printImage.getTopPadding()));
                        setMatrixElements(atrans, instance.matrix);
                        instance.matrix.scale(
                                availableImageWidth / bm.getBounds().getWidth(),
                                availableImageHeight / bm.getBounds().getHeight());
                        break;
                    case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
                    default :
                        try {
                            bm = LLBitmap.newBitmap(new FlashBuffer(byteArr));
                            //bm = LLBitmap.newBitmap(new FlashBuffer(baos.toByteArray()));
                        } catch (IVException e) {
                            throw new JRException(e);
                        }
                        instance = bm.newInstance();
                        AffineTransform atrans1 = AffineTransform.getTranslateInstance(
                                getOffsetX() + twip(printImage.getX() + printImage.getLeftPadding()),
                                twip(absoluteOffsetY) + getOffsetY() + twip(printImage.getY() + printImage.getTopPadding()));
                        setMatrixElements(atrans1, instance.matrix);
                        double scaleFactor = Math.min(
                                availableImageWidth / bm.getBounds().getWidth(),
                                availableImageHeight / bm.getBounds().getHeight());
                        xoffset = (int) (xalignFactor * (availableImageWidth - bm.getBounds().getWidth() * scaleFactor));
                        yoffset = (int) (yalignFactor * (availableImageHeight - bm.getBounds().getHeight() * scaleFactor));
                        instance.matrix.translate(twip(xoffset < 0 ? 0 : xoffset), twip(yoffset < 0 ? 0 : yoffset));
                        instance.matrix.scale(scaleFactor, scaleFactor);
                        break;
                }
            } else { // TYPE_SVG
                double normalWidth = availableImageWidth;
                double normalHeight = availableImageHeight;

                Dimension2D dimension = renderer.getDimension();
                if (dimension != null) {
                    float xalignFactor = getXAlignFactor(printImage);
                    float yalignFactor = getYAlignFactor(printImage);
                    
                    switch (printImage.getScaleImage()) {
                    case JRImage.SCALE_IMAGE_CLIP:
                        normalWidth = dimension.getWidth();
                        normalHeight = dimension.getHeight();
                        xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
                        yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));
                        break;
                    case JRImage.SCALE_IMAGE_FILL_FRAME:
                        xoffset = 0;
                        yoffset = 0;
                        break;
                    case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
                    default:
                        normalWidth = dimension.getWidth();
                        normalHeight = dimension.getHeight();
                        double ratioX = availableImageWidth / normalWidth;
                        double ratioY = availableImageHeight / normalHeight;
                        double ratio = ratioX < ratioY ? ratioX : ratioY;
                        normalWidth *= ratio;
                        normalHeight *= ratio;
                        xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
                        yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));
                        break;
                    }
                }

                BufferedImage bi = new BufferedImage(availableImageWidth, availableImageHeight, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g = bi.createGraphics();
                if (printImage.getMode() == JRElement.MODE_OPAQUE) {
                    g.setColor(printImage.getBackcolor());
                    g.fillRect(0, 0, 
                        normalWidth <= availableImageWidth ? (int) normalWidth : availableImageWidth, 
                        normalHeight <= availableImageHeight ? (int) normalHeight : availableImageHeight);
                }

                Rectangle2D rectangle = new Rectangle2D.Double(
                        (xoffset > 0 ? 0 : xoffset), 
                        (yoffset > 0 ? 0 : yoffset), 
                        normalWidth, 
                        normalHeight);
                
                renderer.render(g, rectangle);
                g.dispose();
                
                xoffset = (xoffset < 0 ? 0 : xoffset);
                yoffset = (yoffset < 0 ? 0 : yoffset);
                
                byte[] byteArr = JRImageLoader.loadImageDataFromAWTImage(bi,JRRenderable.IMAGE_TYPE_PNG);
                try {
                    bm = LLBitmap.newBitmap(new FlashBuffer(byteArr));
                    instance = bm.newInstance();
                    AffineTransform atrans = AffineTransform.getTranslateInstance(
                            getOffsetX() + twip(printImage.getX() + printImage.getLeftPadding() + xoffset),
                            twip(absoluteOffsetY) + getOffsetY() + twip(printImage.getY() + printImage.getTopPadding() + yoffset));
                    setMatrixElements(atrans, instance.matrix);
                } catch (IVException e) {
                    throw new JRException(e);
                }
                
            }
            
            flashFrame.addInstance(instance, zorder++);
        }
    }
	
    private void setMatrixElements(AffineTransform atrans, Matrix matrix)
    {
        matrix.setTranslateX(atrans.getTranslateX());
        matrix.setTranslateY(atrans.getTranslateY());
        matrix.setScaleX(atrans.getScaleY());
        matrix.setScaleY(atrans.getScaleX());
        matrix.setRotateSkew0(atrans.getShearX());
        matrix.setRotateSkew1(atrans.getShearY());
    }
    
    private void registerFonts(Map fontMapParameter)
    {
        //logical font names
        fontMap.put(new FontKey("serif", false, false), FontDef.load(fontDir + File.separator + "TimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("serif", true, false), FontDef.load(fontDir + File.separator + "BTimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("serif", false, true), FontDef.load(fontDir + File.separator + "ITimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("serif", true, true), FontDef.load(fontDir + File.separator + "BITimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("sansserif", false, false), FontDef.load(fontDir + File.separator + "Arial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("sansserif", true, false), FontDef.load(fontDir + File.separator + "BArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("sansserif", false, true), FontDef.load(fontDir + File.separator + "IArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("sansserif", true, true), FontDef.load(fontDir + File.separator + "BIArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("monospaced", false, false), FontDef.load(fontDir + File.separator + "CourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("monospaced", true, false), FontDef.load(fontDir + File.separator + "BCourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("monospaced", false, true), FontDef.load(fontDir + File.separator + "ICourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("monospaced", true, true), FontDef.load(fontDir + File.separator + "BICourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialog", false, false), FontDef.load(fontDir + File.separator + "Arial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialog", true, false), FontDef.load(fontDir + File.separator + "BArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialog", false, true), FontDef.load(fontDir + File.separator + "IArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialog", true, true), FontDef.load(fontDir + File.separator + "BIArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialoginput", false, false), FontDef.load(fontDir + File.separator + "CourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialoginput", true, false), FontDef.load(fontDir + File.separator + "BCourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialoginput", false, true), FontDef.load(fontDir + File.separator + "ICourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("dialoginput", true, true), FontDef.load(fontDir + File.separator + "BICourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        
        //default fonts
        fontMap.put(new FontKey("Arial", false, false), FontDef.load(fontDir + File.separator + "Arial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Arial", true, false), FontDef.load(fontDir + File.separator + "BArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Arial", false, true), FontDef.load(fontDir + File.separator + "IArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Arial", true, true), FontDef.load(fontDir + File.separator + "BIArial" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Arial Black", false, false), FontDef.load(fontDir + File.separator + "ArialGBlack" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Arial Black", true, false), FontDef.load(fontDir + File.separator + "BArialGBlack" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Arial Black", false, true), FontDef.load(fontDir + File.separator + "IArialGBlack" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Arial Black", true, true), FontDef.load(fontDir + File.separator + "BIArialGBlack" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Courier New", false, false), FontDef.load(fontDir + File.separator + "CourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Courier New", true, false), FontDef.load(fontDir + File.separator + "BCourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Courier New", false, true), FontDef.load(fontDir + File.separator + "ICourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Courier New", true, true), FontDef.load(fontDir + File.separator + "BICourierGNew" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Verdana", false, false), FontDef.load(fontDir + File.separator + "Verdana" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Verdana", true, false), FontDef.load(fontDir + File.separator + "BVerdana" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Verdana", false, true), FontDef.load(fontDir + File.separator + "IVerdana" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Verdana", true, true), FontDef.load(fontDir + File.separator + "BIVerdana" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Times New Roman", false, false), FontDef.load(fontDir + File.separator + "TimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Times New Roman", true, false), FontDef.load(fontDir + File.separator + "BTimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Times New Roman", false, true), FontDef.load(fontDir + File.separator + "ITimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Times New Roman", true, true), FontDef.load(fontDir + File.separator + "BITimesGNewGRoman" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Comic Sans MS", false, false), FontDef.load(fontDir + File.separator + "ComicGSansGMS" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Comic Sans MS", true, false), FontDef.load(fontDir + File.separator + "BComicGSansGMS" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Comic Sans MS", false, true), FontDef.load(fontDir + File.separator + "IComicGSansGMS" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Comic Sans MS", true, true), FontDef.load(fontDir + File.separator + "BIComicGSansGMS" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("StandardSymL", false, false), FontDef.load(fontDir + File.separator + "StandardSymL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("StandardSymL", true, false), FontDef.load(fontDir + File.separator + "StandardSymL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("StandardSymL", false, true), FontDef.load(fontDir + File.separator + "IStandardSymL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("StandardSymL", true, true), FontDef.load(fontDir + File.separator + "IStandardSymL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Impact", false, false), FontDef.load(fontDir + File.separator + "Impact" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Impact", true, false), FontDef.load(fontDir + File.separator + "BImpact" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Impact", false, true), FontDef.load(fontDir + File.separator + "IImpact" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Impact", true, true), FontDef.load(fontDir + File.separator + "BIImpact" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusMonL", false, false), FontDef.load(fontDir + File.separator + "NimbusMonL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusMonL", true, false), FontDef.load(fontDir + File.separator + "BNimbusMonL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusMonL", false, true), FontDef.load(fontDir + File.separator + "INimbusMonL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusMonL", true, true), FontDef.load(fontDir + File.separator + "BINimbusMonL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusRomNo9L", false, false), FontDef.load(fontDir + File.separator + "NimbusRomNo9L" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusRomNo9L", true, false), FontDef.load(fontDir + File.separator + "BNimbusRomNo9L" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusRomNo9L", false, true), FontDef.load(fontDir + File.separator + "INimbusRomNo9L" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusRomNo9L", true, true), FontDef.load(fontDir + File.separator + "BINimbusRomNo9L" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusSanL", false, false), FontDef.load(fontDir + File.separator + "NimbusSanL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusSanL", true, false), FontDef.load(fontDir + File.separator + "BNimbusSanL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusSanL", false, true), FontDef.load(fontDir + File.separator + "INimbusSanL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("NimbusSanL", true, true), FontDef.load(fontDir + File.separator + "BINimbusSanL" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Dingbats", false, false), FontDef.load(fontDir + File.separator + "Dingbats" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Dingbats", true, false), FontDef.load(fontDir + File.separator + "Dingbats" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Dingbats", false, true), FontDef.load(fontDir + File.separator + "IDingbats" + JRSwfExporter.FONT_FILE_EXT));
        fontMap.put(new FontKey("Dingbats", true, true), FontDef.load(fontDir + File.separator + "IDingbats" + JRSwfExporter.FONT_FILE_EXT));
        
        //when registering fonts, both default and logical values may be overwritten by values found in the fontMapParameter
        if(fontMapParameter != null && !fontMapParameter.isEmpty())
        {
            Set keys = fontMapParameter.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext())
            {
                FontKey fKey = (FontKey)iterator.next();
                Font font = FontDef.load(fontDir + File.separator + fontMapParameter.get(fKey)+ JRSwfExporter.FONT_FILE_EXT);
                fontMap.put(fKey, font);
            }
        }
        fontsRegistered = true;
    }
    
    private Font getFont(FontKey fontKey)
    {
        Font font = (Font)fontMap.get(fontKey);
        if(font == null)
        {
            //default font
            font = (Font)fontMap.get(new FontKey("serif", fontKey.isBold(), fontKey.isItalic()) );
        }
        return font;
    }
    
    private void reinitialize()
    {
        flashFile = null;
        flashFrame = null;
        fontDir = "../../fonts";
        zorder = 0;
        absoluteOffsetY = 0;
        totalHeight = 0;
        totalWidth = 0;
    }
}
