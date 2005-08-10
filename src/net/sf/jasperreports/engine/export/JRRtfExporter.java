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
 * Matt Thompson - mthomp1234@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRImageLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRRtfExporter extends JRAbstractExporter
{
	
	static int SCALE = 2835; //2835 points per meter

	protected int reportIndex = 0;

	protected JRFont defaultFont = null;

	protected StringBuffer buf;

	protected StringBuffer fontBuf;

	protected StringBuffer colorBuf;

	protected List fonts;

	protected List colors;

	protected int zorder;

	protected Map loadedImagesMap = null;

	
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

	int twip(int points)
	{
		return points * 20;
	}

	int twip(float points)
	{
		return (int) (points * 20.0);
	}

	int twip(double points)
	{
		return (int) (points * 20.0);
	}

	int getFontIndex(JRFont font)
	{
		String fontName = font.getFontName();
		int fontNdx = fonts.indexOf(fontName);
		if (fontNdx < 0)
		{
			fontNdx = fonts.size();
			fonts.add(fontName);
			fontBuf.append("{\\f").append(fontNdx).append("\\fnil ").append(fontName).append(";}");
		}
		return fontNdx;
	}

	int getColorIndex(Color color)
	{
		int colorNdx = colors.indexOf(color);
		if (colorNdx < 0)
		{
			colorNdx = colors.size();
			colors.add(color);
			colorBuf.append("\\red").append(color.getRed())
			.append("\\green").append(color.getGreen())
			.append("\\blue").append(color.getBlue())
			.append(";");
		}
		return colorNdx;
	}

	public void exportReport() throws JRException
	{
		zorder = 1;
		setInput();
		fonts = new ArrayList();
		fontBuf = new StringBuffer();
		colors = new ArrayList();
		colors.add(null);
		colorBuf = new StringBuffer(";");

		// Make sure default font is first in list
		getDefaultFont();
		getFontIndex(defaultFont);
		buf = (StringBuffer) parameters
				.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (buf != null)
		{

			exportReportToStream(null);
			return;
		}
		buf = new StringBuffer();
		OutputStream os = (OutputStream) parameters
				.get(JRExporterParameter.OUTPUT_STREAM);
		if (os != null)
		{

			exportReportToStream(os);
			return;
		}
		File destFile = (File) parameters.get(JRExporterParameter.OUTPUT_FILE);
		if (destFile == null)
		{
			String fileName = (String) parameters
					.get(JRExporterParameter.OUTPUT_FILE_NAME);
			if (fileName != null)
			{
				destFile = new File(fileName);
			}
			else
			{
				throw new JRException("No output specified for the exporter.");
			}
			try
			{
				os = new FileOutputStream(destFile);
				exportReportToStream(os);
				
				os.flush();
			}
			catch (IOException e)
			{
				throw new JRException("Error trying to export to file : "
						+ destFile, e);
			}
			finally
			{
				if (os != null)
				{
					try
					{
						os.close();
					}
					catch (IOException e)
					{
					}
				}
			}
		}

	}

	protected void exportReportToStream(OutputStream os) throws JRException
	{
		for (reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			jasperPrint = (JasperPrint) jasperPrintList.get(reportIndex);
			setPageRange();
			defaultFont = null;
			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				startPageIndex = 0;
				endPageIndex = pages.size() - 1;
				JRPrintPage page = null;
				buf.append("{\\rtf1\\ansi\\deff0\n");
				buf.append("{\\fonttbl ");
				int fontTableIndex = buf.length(); // insert fonttbl third
				buf.append("}\n");
				buf.append("{\\colortbl ");
				int colorTableIndex = buf.length(); // insert colortbl second
				buf.append("}\n");
				buf.append("{\\info{\\nofpages");
				buf.append(pages.size());
				buf.append("}}\n");

				// Following is Page formatting control words
				buf.append("\\viewkind1"); // page layout
				int pageWidth = twip(jasperPrint.getPageWidth());
				buf.append("\\paperw");
				buf.append(pageWidth);
				buf.append("\\paperh");
				buf.append(twip(jasperPrint.getPageHeight()));

				// Following is Section formating control words (no margins)
				buf.append("\\marglsxn0\\margrsxn0\\margtsxn0\\margbsxn0\n");
				if (jasperPrint.getOrientation() == JRReport.ORIENTATION_LANDSCAPE)
				{
					buf.append("\\lndscpsxn");
				}

				for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.currentThread().isInterrupted())
					{
						throw new JRException("Current thread interrupted.");
					}
					page = (JRPrintPage) pages.get(pageIndex);
					createColorAndFontEntries(page);
				}
				buf.insert(colorTableIndex, colorBuf);
				buf.insert(fontTableIndex, fontBuf);

				// Following is Paragraph formatting
				for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					buf.append("\n");
					if (pageIndex != startPageIndex)
						buf.append("{\\pard\\pagebb\\par}\n");
					if (Thread.currentThread().isInterrupted())
					{
						throw new JRException("Current thread interrupted.");
					}
					page = (JRPrintPage) pages.get(pageIndex);
					exportPage(page);
				}
				buf.append("}\n");
				if (os != null)
				{
					try
					{
						os.write(buf.toString().getBytes());
						buf.setLength(0);
					}
					catch (IOException e)
					{
						throw new JRException("Error generating RTF report : "
								+ jasperPrint.getName(), e);
					}
				}
				
			}
		}
	}

	protected void createColorAndFontEntries(JRPrintPage page)
	{
		JRPrintElement element = null;
		Collection elements = page.getElements();
		if (elements != null && elements.size() > 0)
		{
			for (Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement) it.next();
				getColorIndex(element.getForecolor());
				getColorIndex(element.getBackcolor());
				if (element instanceof JRPrintText)
				{
					getFontIndex(((JRPrintText) element).getFont());
				}
			}
		}
	}

	protected void exportPage(JRPrintPage page) throws JRException
	{
		JRPrintElement element = null;
		Collection elements = page.getElements();
		if (elements != null && elements.size() > 0)
		{
			for (Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement) it.next();
				if (element instanceof JRPrintLine)
				{
					exportLine((JRPrintLine) element);
				}
				else if (element instanceof JRPrintRectangle)
				{
					exportRectangle((JRPrintRectangle) element);
				}
				else if (element instanceof JRPrintEllipse)
				{
					exportEllipse((JRPrintEllipse) element);
				}
				else if (element instanceof JRPrintImage)
				{
					exportImage((JRPrintImage) element);
				}
				else if (element instanceof JRPrintText)
				{
					exportText((JRPrintText) element);
				}
			}
		}
	}

	protected void exportLine(JRPrintLine line)
	{
		int x = twip(line.getX());
		int y = twip(line.getY());
		int h = twip(line.getHeight());
		int w = twip(line.getWidth());

		startGraphic("dpline", x, y, w, h);

		if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
		{
			buf.append("\\dpptx").append(x)
			.append("\\dppty").append(y)
			.append("\\dpptx").append(x + w)
			.append("\\dppty").append(y + h);
		}
		else
		{
			buf.append("\\dpptx").append(x)
			.append("\\dppty").append(y + h)
			.append("\\dpptx").append(x + w)
			.append("\\dppty").append(y - h);
		}

//		.append("{\\pard")
//		.append("\\absw").append(twip(line.getWidth()))
//		.append("\\absh-").append(twip(line.getHeight()))
//		
//		.append("\\phpg")
//		.append("\\posx").append(x)
//		
//		.append("\\pvpg")
//		.append("\\posy").append(y)
//		.append(" ")

		finishGraphic(line);
	}

	protected void exportRectangle(JRPrintRectangle rec)
	{
		int x = twip(rec.getX());
		int y = twip(rec.getY());
		int h = twip(rec.getHeight());
		int w = twip(rec.getWidth());
		startGraphic("dprect", x, y, w, h);
		finishGraphic(rec);
	}

	protected void exportEllipse(JRPrintEllipse ellipse)
	{
		startGraphic(
			"dpellipse", 
			twip(ellipse.getX()), 
			twip(ellipse.getY()), 
			twip(ellipse.getHeight()), 
			twip(ellipse.getWidth())
			);
		finishGraphic(ellipse);
	}

	protected void startGraphic(String type, int x, int y, int w, int h)
	{
		buf.append("{\\*\\do\\dobxpage\\dobypage\\dodhgt")
		.append(zorder++).append("\\").append(type)
		.append("\\dpx").append(x)
		.append("\\dpy").append(y)
		.append("\\dpxsize").append(w)
		.append("\\dpysize").append(h);
	}

	protected void finishGraphic(JRPrintGraphicElement elem)
	{
		int mode = 0;
		if (elem.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = 1;
		}
		finishGraphic(
			elem.getPen(), 
			elem.getForecolor(), 
			elem.getBackcolor(),
			mode
			);
	}

	protected int getAdjustment(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_THIN:
				return 0;
			case JRGraphicElement.PEN_1_POINT:
				return 10;
			case JRGraphicElement.PEN_2_POINT:
				return 20;
			case JRGraphicElement.PEN_4_POINT:
				return 40;
			case JRGraphicElement.PEN_DOTTED:
				return 0;
			case JRGraphicElement.PEN_NONE:
				return 0;
			default:
				return 0;
		}
	}

	protected void finishGraphic(byte pen, Color fg, Color bg, int fillPat)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_THIN:
				buf.append("\\dplinew10");
				break;
			case JRGraphicElement.PEN_1_POINT:
				buf.append("\\dplinew20");
				break;
			case JRGraphicElement.PEN_2_POINT:
				buf.append("\\dplinew40");
				break;
			case JRGraphicElement.PEN_4_POINT:
				buf.append("\\dplinew80");
				break;
			case JRGraphicElement.PEN_DOTTED:
				buf.append("\\dplinedot");
				break;
			case JRGraphicElement.PEN_NONE:
				buf.append("\\dplinehollow");
				break;
			default:
				buf.append("\\dplinew20");
				break;
		}

		buf.append("\\dplinecor").append(fg.getRed())
		.append("\\dplinecog").append(fg.getGreen())
		.append("\\dplinecob").append(fg.getBlue())
		.append("\\dpfillfgcr").append(fg.getRed())
		.append("\\dpfillfgcg").append(fg.getGreen())
		.append("\\dpfillfgcb").append(fg.getBlue())
		.append("\\dpfillbgcr").append(bg.getRed())
		.append("\\dpfillbgcg").append(bg.getGreen())
		.append("\\dpfillbgcb").append(bg.getBlue())
		.append("\\dpfillpat").append(fillPat)
		.append("}\n");
	}

	protected void exportBox(JRBox box, JRPrintElement element)
	{
		int x = twip(element.getX());
		int y = twip(element.getY());
		int h = twip(element.getHeight());
		int w = twip(element.getWidth());
		Color fg = element.getForecolor();
		Color bg = element.getBackcolor();

		// adjustments for top, bottom, left, right
		int rX = x + 20;
		int rY = y + 20;
		int rW = w - 20;
		int rH = h - 20;

		int flag = 0;

		if (box.getTopBorder() != JRGraphicElement.PEN_NONE)
		{
			Color bc = box.getTopBorderColor();
			byte pen = box.getTopBorder();
			int a = getAdjustment(box.getTopBorder());
			if (bc == null)
				bc = fg;
			startGraphic("dpline", x, y + a, w, 0);
			finishGraphic(pen, fg, bg, 1);

			rY = y + a + 20;
			flag++;
		}
		if (box.getLeftBorder() != JRGraphicElement.PEN_NONE)
		{
			Color bc = box.getLeftBorderColor();
			byte pen = box.getLeftBorder();
			int a = getAdjustment(pen);
			if (bc == null)
				bc = fg;
			startGraphic("dpline", x + a, y, 0, h);
			finishGraphic(pen, fg, bg, 1);

			rX = x + a + 20;
			flag++;
		}

		if (box.getBottomBorder() != JRGraphicElement.PEN_NONE)
		{
			Color bc = box.getBottomBorderColor();
			byte pen = box.getBottomBorder();
			int a = getAdjustment(pen);
			if (bc == null)
				bc = fg;
			startGraphic("dpline", x, y + h - a, w, 0);
			finishGraphic(pen, fg, bg, 1);

			rH = h - a - 20;
			flag++;
		}

		if (box.getRightBorder() != JRGraphicElement.PEN_NONE)
		{
			Color bc = box.getRightBorderColor();
			byte pen = box.getRightBorder();
			int a = getAdjustment(pen);
			if (bc == null)
				bc = fg;
			startGraphic("dpline", x + w - a, y, 0, h);
			finishGraphic(pen, fg, bg, 1);

			rW = w - a - 20;
			flag++;
		}

		int oldVal = zorder;
		zorder -= (flag + 1);

		startGraphic("dprect", rX, rY, rW, rH);
		finishGraphic(JRGraphicElement.PEN_NONE, fg, bg, 1);

		zorder = oldVal;

	}

	protected void exportText(JRPrintText text)
	{
		//
		// For now, ignore styled text, all modes except MODE_OPAQUE
		// and ROTATION.
		//
		JRBox box = text.getBox();
		if (box != null)
		{
			exportBox(box, text);
		}

		JRFont font = text.getFont();

		buf.append("{\\pard")
		.append("\\absw").append(twip(text.getWidth()))
		.append("\\absh").append(twip(text.getHeight()))
		.append("\\phpg\\posx").append(twip(text.getX()))
		.append("\\pvpg\\posy").append(twip(text.getY()))
		.append("\\f").append(getFontIndex(font))
		.append("\\cf").append(getColorIndex(text.getForecolor()))
		.append("\\cb").append(getColorIndex(text.getBackcolor()));

		//           .append("\\fi").append(twip(text.getLeadingOffset()));
		if (box != null)
		{
			buf.append("\\li" + twip(box.getLeftPadding()));
			buf.append("\\ri" + twip(box.getRightPadding()));
		}

		if (font.isBold())
			buf.append("\\b");
		if (font.isItalic())
			buf.append("\\i");
		if (font.isStrikeThrough())
			buf.append("\\strike");
		if (font.isUnderline())
			buf.append("\\ul");
		buf.append("\\fs").append(font.getSize() * 2);

		switch (text.getHorizontalAlignment())
		{
			case JRAlignment.HORIZONTAL_ALIGN_LEFT:
				buf.append("\\ql");
				break;
			case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				buf.append("\\qc");
				break;
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
				buf.append("\\qr");
				break;
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
				buf.append("\\qj");
				break;
			default:
				buf.append("\\ql");
				break;
		}

		switch (text.getLineSpacing())
		{
			case JRTextElement.LINE_SPACING_SINGLE:
				break;
			case JRTextElement.LINE_SPACING_1_1_2:
				buf.append("\\sl360\\slmulti");
				break;
			case JRTextElement.LINE_SPACING_DOUBLE:
				buf.append("\\sl480\\slmulti");
				break;
		}

		//        switch (text.getVerticalAlignment()) {
		//        case JRTextElement.VERTICAL_ALIGN_TOP: buf.append("\\posyt"); break;
		//        case JRTextElement.VERTICAL_ALIGN_MIDDLE: buf.append("\\posyc");
		// break;
		//        case JRTextElement.VERTICAL_ALIGN_BOTTOM: buf.append("\\posyb");
		// break;
		//        }

		buf.append(" ");
		buf.append(text.getText());
		buf.append("\\par}\n");
	}


	/**
	 * 
	 */
	protected void exportImage(JRPrintImage printImage) throws JRException	
	{
		int x = twip(printImage.getX() + globalOffsetX);
		int y = twip(printImage.getY() + globalOffsetY);
		int width = twip(printImage.getWidth());
		int height = twip(printImage.getHeight());
		
		if (printImage.getMode() == JRElement.MODE_OPAQUE) 
		{
			buf.append("{\\pard\\fs0\\absw0\\absh0\\phpg\\pvpg")
			.append("\\posx").append(x)
			.append("\\posy").append(y);
			startGraphic("dprect", x, y, width, height);
			finishGraphic(JRGraphicElement.PEN_NONE, printImage.getForecolor(), printImage.getBackcolor(), 1);
			buf.append("\\par}");
		}
		
		int topPadding = 0;
		int leftPadding = 0;
		int rightPadding = 0;
		int bottomPadding = 0;
		
		if (printImage.getBox() != null) 
		{
			leftPadding = printImage.getBox().getLeftPadding();
			topPadding = printImage.getBox().getTopPadding();
			rightPadding = printImage.getBox().getRightPadding();
			bottomPadding = printImage.getBox().getBottomPadding();
		}
	
		int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;
		
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

			BufferedImage bi = 
				new BufferedImage(
					availableImageWidth, 
					availableImageHeight, 
					BufferedImage.TYPE_INT_RGB
					);
			Graphics2D grx = bi.createGraphics();
			grx.setColor(printImage.getBackcolor());
			grx.fillRect(0, 0, availableImageWidth, availableImageHeight);
			
			switch (printImage.getScaleImage())
			{
				case JRImage.SCALE_IMAGE_CLIP :
				{
					int xoffset = (int)(xalignFactor * (availableImageWidth - normalWidth));
					int yoffset = (int)(yalignFactor * (availableImageHeight - normalHeight));

					renderer.render(
						grx, 
						new Rectangle(
							xoffset, 
							yoffset, 
							normalWidth, 
							normalHeight
							) 
						);
	
					break;
				}
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					renderer.render(
						grx,
						new Rectangle(
							0, 
							0, 
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
								xoffset, 
								yoffset, 
								normalWidth, 
								normalHeight
								) 
							);
					}
					
					break;
				}
			}

			buf.append("{\\pard\\fs0\\phpg\\pvpg")
			.append("\\posx").append(twip(printImage.getX() + leftPadding + globalOffsetX))
			.append("\\posy").append(twip(printImage.getY() + topPadding + globalOffsetY))
			.append("{\\pict\\jpegblip")
			.append("\\picwgoal").append(twip(availableImageWidth))
			.append("\\pichgoal").append(twip(availableImageHeight))
			.append("\n");
			
			ByteArrayInputStream bais = new ByteArrayInputStream(JRImageLoader.loadImageDataFromAWTImage(bi));
			
			int count = 0;
			int current = 0;
			while((current = bais.read()) != -1) 
			{
				String helperStr = Integer.toHexString(current);
				if (helperStr.length() < 2)
				{
					helperStr = "0" + helperStr;
				}
				buf.append(helperStr);
				count++;
				if (count == 64) 
				{
					buf.append("\n");
					count = 0;
				}
			}
			
			buf.append("\n}\\par}\n");
		}
		
		if (printImage.getBox() == null)
		{
			if (printImage.getPen() != JRGraphicElement.PEN_NONE)
			{
				buf.append("{\\pard\\fs0\\absw0\\absh0\\phpg\\posx")
				.append(x)
				.append("\\pvpg\\posy")
				.append(y);
				startGraphic("dprect", x, y, width, height);
				finishGraphic(printImage);
				buf.append("\\par}");
			}
		}
		else
		{
			buf.append("{\\pard\\fs0\\absw0\\absh0\\phpg\\posx")
			.append(x)
			.append("\\pvpg\\posy")
			.append(y);
			exportBox(printImage.getBox(), printImage);
			buf.append("\\par}");
		}
	}
	


}
