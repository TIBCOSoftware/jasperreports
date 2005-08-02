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
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
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
import net.sf.jasperreports.engine.JRImageRenderer;
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




public class JRRtfExporter extends JRAbstractExporter
{

	//    private static Log log = LogFactory.getLog(JRRtfExporter.class);

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
			fontBuf.append("{\\f").append(fontNdx).append("\\fnil ").append(
					fontName).append(";}");
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
			colorBuf.append("\\red").append(color.getRed()).append("\\green")
					.append(color.getGreen()).append("\\blue").append(
							color.getBlue()).append(";");
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
				buf.append("{\\info{\\nofpages").append(pages.size()).append(
						"}}\n");

				// Following is Page formatting control words
				buf.append("\\viewkind1"); // page layout
				int pageWidth = twip(jasperPrint.getPageWidth());
				buf.append("\\paperw").append(pageWidth).append("\\paperh")
						.append(twip(jasperPrint.getPageHeight()));

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
					int startNdx = buf.length();
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
					showElement("Line", element);
					exportLine((JRPrintLine) element);
				}
				else if (element instanceof JRPrintRectangle)
				{
					showElement("Rect", element);
					exportRectangle((JRPrintRectangle) element);
				}
				else if (element instanceof JRPrintEllipse)
				{
					showElement("Elipse", element);
					exportEllipse((JRPrintEllipse) element);
				}
				else if (element instanceof JRPrintImage)
				{
					showElement("Image", element);
					exportImage((JRPrintImage) element);
				}
				else if (element instanceof JRPrintText)
				{
					showElement("Text", element);
					exportText((JRPrintText) element);
				}
				else
				{
					showElement("Unknown", element);
				}
			}
		}
	}

	protected void showElement(String text, JRPrintElement item)
	{
		int x1 = twip(item.getX());
		int y1 = twip(item.getY());
		int x2 = x1 + twip(item.getWidth());
		int y2 = y1 + twip(item.getHeight());
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
			buf.append("\\dpptx").append(x).append("\\dppty").append(y).append(
					"\\dpptx").append(x + w).append("\\dppty").append(y + h);
		}
		else
		{
			buf.append("\\dpptx").append(x).append("\\dppty").append(y + h)
					.append("\\dpptx").append(x + w).append("\\dppty").append(
							y - h);
		}

		//    .append("{\\pard")
		//           .append("\\absw").append(twip(line.getWidth()))
		//           .append("\\absh-").append(twip(line.getHeight()))

		//           .append("\\phpg")
		//           .append("\\posx").append(x)

		//           .append("\\pvpg")
		//           .append("\\posy").append(y)
		//           .append(" ")

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
		int x = twip(ellipse.getX());
		int y = twip(ellipse.getY());
		int h = twip(ellipse.getHeight());
		int w = twip(ellipse.getWidth());
		startGraphic("dpellipse", x, y, w, h);
		finishGraphic(ellipse);
	}

	protected void startGraphic(String type, int x, int y, int w, int h)
	{
		buf.append("{\\*\\do\\dobxpage\\dobypage").append("\\dodhgt").append(
				zorder++).append("\\").append(type).append("\\dpx").append(x)
				.append("\\dpy").append(y).append("\\dpxsize").append(w)
				.append("\\dpysize").append(h);
	}

	protected void finishGraphic(JRPrintGraphicElement elem)
	{
		int mode = 0;
		if (elem.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = 1;
		}
		finishGraphic(elem.getPen(), elem.getForecolor(), elem.getBackcolor(),
				mode);
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
		int penWidth = 20;
		switch (pen)
		{
			case JRGraphicElement.PEN_THIN:
				penWidth = 10;
				buf.append("\\dplinew10");
				break;
			case JRGraphicElement.PEN_1_POINT:
				penWidth = 20;
				buf.append("\\dplinew20");
				break;
			case JRGraphicElement.PEN_2_POINT:
				penWidth = 40;
				buf.append("\\dplinew40");
				break;
			case JRGraphicElement.PEN_4_POINT:
				penWidth = 80;
				buf.append("\\dplinew80");
				break;
			case JRGraphicElement.PEN_DOTTED:
				penWidth = 20;
				buf.append("\\dplinedot");
				break;
			case JRGraphicElement.PEN_NONE:
				penWidth = 0;
				buf.append("\\dplinehollow");
				break;
			default:
				penWidth = 20;
				buf.append("\\dplinew20");
				break;
		}
		buf.append("\\dplinecor").append(fg.getRed()).append("\\dplinecog")
				.append(fg.getGreen()).append("\\dplinecob").append(
						fg.getBlue());
		buf.append("\\dpfillfgcr").append(fg.getRed()).append("\\dpfillfgcg")
				.append(fg.getGreen()).append("\\dpfillfgcb").append(
						fg.getBlue()).append("\\dpfillbgcr")
				.append(bg.getRed()).append("\\dpfillbgcg").append(
						bg.getGreen()).append("\\dpfillbgcb").append(
						bg.getBlue()).append("\\dpfillpat").append(fillPat)
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
		int startNdx = buf.length();

		JRFont font = text.getFont();

		JRBox box = text.getBox();
		if (box != null)
		{
			exportBox(box, text);
		}

		buf.append("{\\pard").append("\\absw").append(twip(text.getWidth()))
				.append("\\absh").append(twip(text.getHeight()))

				.append("\\phpg").append("\\posx").append(twip(text.getX()))

				.append("\\pvpg").append("\\posy").append(twip(text.getY()))

				.append("\\f" + getFontIndex(font)).append(
						"\\cf" + getColorIndex(text.getForecolor())).append(
						"\\cb" + getColorIndex(text.getBackcolor()));

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

	static String[] hex = new String[] { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B",
			"1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26",
			"27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C",
			"3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47",
			"48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52",
			"53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D",
			"5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
			"69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73",
			"74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E",
			"7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
			"8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94",
			"95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
			"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA",
			"AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5",
			"B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
			"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB",
			"CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6",
			"D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1",
			"E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC",
			"ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
			"F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

	static String hex4(int val)
	{
		return hex[val & 255] + hex[(val >> 8) & 255] + hex[(val >> 16) & 255]
				+ hex[(val >> 24) & 255];
	}

	protected void exportImage(JRPrintImage printImage) throws JRException
			
	{
		int borderOffset = 0;
		int borderCorrection = 0;
		int x = twip(printImage.getX());
		int y = twip(printImage.getY());
		int h = twip(printImage.getHeight());
		int w = twip(printImage.getWidth());
		int adjX1 = 0;
		int adjX2 = 0;
		int adjY1 = 0;
		int adjY2 = 0;

		Color fg = printImage.getForecolor();
		Color bg = printImage.getBackcolor();
		int adjustment = 0;

		if (printImage.getMode() == JRElement.MODE_OPAQUE)
		{
			//            System.out.println("OPAQUE");
			buf.append("{\\pard").append("\\absw0").append("\\absh0").append(
					"\\phpg").append("\\posx").append(x).append("\\pvpg")
					.append("\\posy").append(y);
			startGraphic("dprect", x, y, w, h);
			finishGraphic(JRGraphicElement.PEN_NONE, fg, bg, 1);
			buf.append("\\par}");
		}

		//        System.out.println("printImage.xy="+x+":"+y+" wh="+w+":"+h);
		JRRenderable renderer = printImage.getRenderer();

		int posx = x;
		int posy = y;

		int topPadding = 0;
		int leftPadding = 0;
		int bottomPadding = 0;
		int rightPadding = 0;

		JRBox box = printImage.getBox();
		if (box == null)
		{
			adjX1 = adjX2 = adjY1 = adjY2 = getAdjustment(printImage.getPen());
		}
		else
		{
			adjX1 = getAdjustment(box.getLeftBorder());
			adjX2 = getAdjustment(box.getRightBorder());
			adjY1 = getAdjustment(box.getTopBorder());
			adjY2 = getAdjustment(box.getBottomBorder());
			topPadding = box.getTopPadding();
			leftPadding = box.getLeftPadding();
			bottomPadding = box.getBottomPadding();
			rightPadding = box.getRightPadding();
		}
		posx += twip(leftPadding) + 2 * adjX1 + 20;
		posy += twip(rightPadding) + 2 * adjY1 + 20;
		if (posx < 0)
			posx = 0;
		if (posy < 0)
			posy = 0;

		int availableWidth = printImage.getWidth() - leftPadding - rightPadding
				- 2 * (adjX1 + adjX2) / 20 - 1;
		availableWidth = (availableWidth < 0) ? 0 : availableWidth;

		int availableHeight = printImage.getHeight() - topPadding
				- bottomPadding - 2 * (adjY1 + adjY2) / 20 - 1;
		availableHeight = (availableHeight < 0) ? 0 : availableHeight;

		int actualHeight = availableHeight;
		int actualWidth = availableWidth;

		int normalHeight = availableHeight;
		int normalWidth = availableWidth;

		int clippedHeight = availableHeight;
		int clippedWidth = availableWidth;

		Dimension2D dimension = renderer.getDimension();
		if (dimension != null)
		{
			normalWidth = (int) dimension.getWidth();
			normalHeight = (int) dimension.getHeight();
		}

		if (renderer != null && availableWidth > 0 && availableHeight > 0)
		{

			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{

				int clippedXPos = 0;
				int clippedYPos = 0;
				int scaleFactor = 1;
				switch (printImage.getScaleImage())
				{
					default:
					case JRImage.SCALE_IMAGE_CLIP:
					{
						//                        System.out.println("PICTURE CLIP");

						if (normalWidth <= availableWidth)
						{
							clippedWidth = normalWidth;
						}
						else
						{
							clippedWidth = availableWidth;
							switch (printImage.getHorizontalAlignment())
							{
								case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
									clippedXPos = normalWidth - availableWidth;
									break;
								case JRAlignment.HORIZONTAL_ALIGN_CENTER:
									clippedXPos = (normalWidth - availableWidth) / 2;
									break;
							}
						}
						actualWidth = clippedWidth;

						if (normalHeight <= availableHeight)
						{
							clippedHeight = normalHeight;
						}
						else
						{
							clippedHeight = availableHeight;
							switch (printImage.getVerticalAlignment())
							{
								case JRAlignment.VERTICAL_ALIGN_BOTTOM:
									clippedYPos = normalHeight
											- availableHeight;
									break;
								case JRAlignment.VERTICAL_ALIGN_MIDDLE:
									clippedYPos = (normalHeight - availableHeight) / 2;
									break;
							}
						}
						actualHeight = clippedHeight;
						break;
					}
					case JRImage.SCALE_IMAGE_FILL_FRAME:
					{
						// System.out.println("PICTURE FILL FRAME");
						clippedWidth = normalWidth;
						clippedHeight = normalHeight;
						actualWidth = availableWidth;
						actualHeight = availableHeight;
						break;
					}
					case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
					{
						// System.out.println("PICTURE RETAIN SHAPE");

						float normalRatio = (float) normalWidth / (float) normalHeight;
						float availableRatio = (float) availableWidth / (float) availableHeight;

						if (availableRatio > normalRatio)
						{
							// Use availableHeight and scale width to fit
							actualWidth = (int) (availableHeight * normalRatio);
						}
						else
						{
							// Use availableWidth and scale height to fit
							actualWidth = availableWidth;
						}
						int area = actualWidth * actualHeight;
						if (area < 20000)
						{
							scaleFactor = 2;
						}
						actualHeight = (int) (actualWidth / normalRatio);
						clippedHeight = normalHeight;
						clippedWidth = normalWidth;
					}
				}
				if (actualWidth < availableWidth)
				{
					int dif = availableWidth - actualWidth;
					switch (printImage.getHorizontalAlignment())
					{
						case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
							posx += twip(dif);
							break;
						case JRAlignment.HORIZONTAL_ALIGN_CENTER:
							posx += twip(dif / 2);
							break;
					}
				}
				if (actualHeight < availableHeight)
				{
					int dif = availableHeight - actualHeight;
					switch (printImage.getVerticalAlignment())
					{
						case JRAlignment.VERTICAL_ALIGN_BOTTOM:
							posy += twip(dif);
							break;
						case JRAlignment.VERTICAL_ALIGN_MIDDLE:
							posy += twip(dif / 2);
							break;
					}
				}
				actualWidth *= scaleFactor;
				actualHeight *= scaleFactor;

				int adjustedWidth = ((actualWidth + 7) / 8) * 8;
				int adjustedHeight = actualHeight;
				int adjustedScale = SCALE;
				if (adjustedWidth != actualWidth)
				{
					float adjustedRatio = (float) adjustedWidth
							/ (float) actualWidth;
					adjustedHeight = (int) (adjustedRatio * (float) actualHeight);
					adjustedScale = (int) (adjustedRatio * (float) SCALE);
				}

				BufferedImage bi = new BufferedImage(adjustedWidth,
						adjustedHeight, BufferedImage.TYPE_3BYTE_BGR);

				Graphics2D g = bi.createGraphics();
				g.setColor(printImage.getBackcolor());
				g.fillRect(0, 0, adjustedWidth, adjustedHeight);

				Image img = ((JRImageRenderer) renderer).getImage();
				g.drawImage(img, 0, 0, adjustedWidth, adjustedHeight,
						clippedXPos, clippedYPos, clippedWidth, clippedHeight,
						null);
				renderer.render(g, new java.awt.Rectangle(0, 0, actualWidth,
						actualHeight));

				g.dispose();

				Raster raster = bi.getData();

				int bitWidth = raster.getWidth();

				int byteWidth = bitWidth;
				int bitHeight = raster.getHeight();
				System.out.println("pixels=" + bitWidth + ":" + bitHeight
						+ " byteWidth=" + byteWidth);
				buf.append("{\\pard")
				.append("\\absw0")
				.append("\\absh0")
				.append("\\phpg")
				.append("\\posx").append(posx)
				.append("\\pvpg")
				.append("\\posy").append(posy);

				

				buf.append("{\\pict")
				.append("\\dibitmap0")
				.append("\\wbmbitspixel24\\wbmplanes1\\wbmwidthbytes")
				.append(byteWidth)

				// <pictsize>
				.append("\\picw").append(twip(actualWidth / scaleFactor))
				.append("\\pich").append(twip(actualHeight / scaleFactor))
				.append("\n");

				// <data>
				// Create INFOHEADER

				System.out.println("Adjusted scale: " + adjustedScale);
				System.out.println("Scale factor: " + scaleFactor);
				System.out.println("In hex:"
						+ hex4(adjustedScale * scaleFactor));
				buf.append("28000000") // biSize: Size of structure
						.append(hex4(bitWidth)) // biWidth
						.append(hex4(bitHeight)) // biHeight
						.append("0100") // biPlanes
						.append("1800") // biBitCount
						.append("00000000") // biComperssion (BI_RGB - none)
						.append(hex4(byteWidth * bitHeight)) // biSizeImage
						.append(hex4(adjustedScale * scaleFactor)) // biXPelsPerMeter
						.append(hex4(adjustedScale * scaleFactor)) // biYPelsPerMeter
						.append("00000000") // biClrUsed (used?)
						.append("00000000") // biClrImportant (used?)
						.append("\n");
				int by = bitHeight;
				while (by > 0)
				{
					by--;
					int bx = 0;
					while (bx < bitWidth)
					{
						buf.append(hex[raster.getSample(bx, by, 2) & 255])
								.append(hex[raster.getSample(bx, by, 1) & 255])
								.append(hex[raster.getSample(bx, by, 0) & 255]);
						bx++;

					}
					while (bx < byteWidth)
					{
						buf.append("000000");
						bx++;
					}
					buf.append("\n");
				}

				buf.append("}\\par}\n");
			}

		}
		if (printImage.getBox() == null)
		{
			if (printImage.getPen() != JRGraphicElement.PEN_NONE)
			{
				buf.append("{\\pard").append("\\absw0").append("\\absh0")
						.append("\\phpg").append("\\posx").append(x).append(
								"\\pvpg").append("\\posy").append(y);
				startGraphic("dprect", x, y, w, h);
				finishGraphic(printImage);
				buf.append("\\par}");
			}
		}
		else
		{
			buf.append("{\\pard").append("\\absw0").append("\\absh0").append(
					"\\phpg").append("\\posx").append(x).append("\\pvpg")
					.append("\\posy").append(y);
			exportBox(printImage.getBox(), printImage);
			buf.append("\\par}");
		}
	}

}
