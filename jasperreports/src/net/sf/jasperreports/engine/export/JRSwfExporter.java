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
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

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

	/**
	 * Property that provides a default value for the 
	 * {@link net.sf.jasperreports.engine.export.JRPdfExporterParameter#FORCE_SVG_SHAPES JRPdfExporterParameter.FORCE_SVG_SHAPES}
	 * PDF exporter parameter.
	 * 
	 * @see net.sf.jasperreports.engine.export.JRPdfExporterParameter#FORCE_SVG_SHAPES
	 */
	public static final String PDF_FORCE_SVG_SHAPES = JRProperties.PROPERTY_PREFIX + "export.pdf.force.svg.shapes";

	private static final int ALIGN_LEFT = 0;
	private static final int ALIGN_RIGHT = 1;
	private static final int ALIGN_CENTER = 2;
	private static final int ALIGN_JUSTIFY = 3;

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	protected static boolean fontsRegistered = false;

	/**
	 *
	 */
	protected FlashFile flashFile = null;
	protected Frame flashFrame = null;
	protected JRExportProgressMonitor progressMonitor = null;

	protected int reportIndex = 0;

	/**
	 *
	 */
	protected boolean isCreatingBatchModeBookmarks = false;
	protected boolean isCompressed = false;
	protected boolean isEncrypted = false;
	protected boolean is128BitKey = false;
	protected String userPassword = null;
	protected String ownerPassword = null;
	protected int permissions = 0;
	protected Character pdfVersion = null;

	/**
	 *
	 */
	protected Map loadedImagesMap = null;

	private Map fontMap = null;
	
	protected int zorder;

	/**
	 * if multiple pages involved, absoluteOffsetY calculates the total height of pages before current page 
	 */
	protected int absoluteOffsetY;
	
	protected int totalHeight;
	protected int totalWidth;
	;
	/**
	 *
	 */
	public void exportReport() throws JRException
	{
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
			
			Boolean isCreatingBatchModeBookmarksParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS);
			if(isCreatingBatchModeBookmarksParameter != null){
				isCreatingBatchModeBookmarks = isCreatingBatchModeBookmarksParameter.booleanValue();
			}
	
			Boolean isCompressedParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_COMPRESSED);
			if (isCompressedParameter != null)
			{
				isCompressed = isCompressedParameter.booleanValue();
			}
			
			Boolean isEncryptedParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_ENCRYPTED);
			if (isEncryptedParameter != null)
			{
				isEncrypted = isEncryptedParameter.booleanValue();
			}
			
			Boolean is128BitKeyParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_128_BIT_KEY);
			if (is128BitKeyParameter != null)
			{
				is128BitKey = is128BitKeyParameter.booleanValue();
			}
			
			userPassword = (String)parameters.get(JRPdfExporterParameter.USER_PASSWORD);
			ownerPassword = (String)parameters.get(JRPdfExporterParameter.OWNER_PASSWORD);
	
			Integer permissionsParameter = (Integer)parameters.get(JRPdfExporterParameter.PERMISSIONS);
			if (permissionsParameter != null)
			{
				permissions = permissionsParameter.intValue();
			}
	
			pdfVersion = (Character) parameters.get(JRPdfExporterParameter.PDF_VERSION);
	
			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
			
//			setForceSvgShapes();
//			setSplitCharacter();
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
					if( isCreatingBatchModeBookmarks ){
						//add a new level to our outline for this report
//						addBookmark(0, jasperPrint.getName(), 0, 0);
					}

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
					//exportImage((JRPrintImage)element);
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
	        Text textBlock = Text.newText();
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
				Font font = null;
				if(fontMap != null && fontMap.get(fontKey) != null)
				{
					font = (Font)fontMap.get(fontKey);
				}
				else
				{
					String fontDir = JRProperties.getProperty(JRProperties.SWF_FONT_DIRS_PREFIX);
					if (styleFont.isBold())
					{
						if(styleFont.isItalic())
							font = FontDef.load(fontDir + File.separator + "ARIALBI.FFT");
						else
							font = FontDef.load(fontDir + File.separator + "ARIALBD.FFT");
					}
					else if (styleFont.isItalic())
					{
						font = FontDef.load(fontDir + File.separator + "ARIALI.FFT");
					}
					else
					{
						font = FontDef.load(fontDir + File.separator + "ARIAL.FFT");
					}
				}
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
				matrix.setTranslateX(atrans.getTranslateX());
				matrix.setTranslateY(atrans.getTranslateY());
				matrix.setScaleX(atrans.getScaleY());
				matrix.setScaleY(atrans.getScaleX());
				matrix.setRotateSkew0(atrans.getShearX());
				matrix.setRotateSkew1(atrans.getShearY());
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
		return points * 10;
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

	private AlphaColor getAlphaColor(Color color)
	{
		return new AlphaColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
}
