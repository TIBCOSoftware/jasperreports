/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 * 
 * JasperReports - Free Java report-generating library. Copyright (C) 2001-2003
 * Teodor Danciu teodord@hotmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributor: Manuel Paul <mpaul@ratundtat.com>, 
 *				Rat & Tat Beratungsgesellschaft mbH, 
 *				Muehlenkamp 6c,
 *				22303 Hamburg,
 *				Germany.
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jxl.CellView;
import jxl.JXLException;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.BoldStyle;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.Pattern;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGridLayout.ExporterElements;
import net.sf.jasperreports.engine.util.JRStyledText;
import com.keypoint.PngEncoderB;

import org.apache.commons.collections.ReferenceMap;


/**
 * @author Manuel Paul (mpaul@ratundtat.com)
 * @version $Id$
 */
public class JExcelApiExporter extends JRXlsAbstractExporter
{
	private static final Colour WHITE = Colour.WHITE;
	private static final Colour BLACK = Colour.BLACK;
	
	private static Map colorsCache = new ReferenceMap();


	private Map loadedCellStyles = new HashMap();

	private WritableWorkbook workbook = null;

	private WritableSheet sheet = null;

	private WritableCellFormat emptyCellStyle = null;

	private boolean isFontSizeFixEnabled = false;

	private Pattern backgroundMode = Pattern.SOLID;
	
	private PngEncoderB pngEncoder;
	
	public JExcelApiExporter()
	{
		pngEncoder = new PngEncoderB();
	}

	protected void setParameters()
	{
		super.setParameters();

		Boolean isFontSizeFixEnabledParameter = (Boolean) this.parameters.get(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED);
		if (isFontSizeFixEnabledParameter != null)
		{
			this.isFontSizeFixEnabled = isFontSizeFixEnabledParameter.booleanValue();
		}
	}

	protected void setBackground()
	{
		this.backgroundMode = Pattern.SOLID;
	}

	protected void openWorkbook(OutputStream os) throws JRException
	{
		try
		{
			workbook = Workbook.createWorkbook(os);
			emptyCellStyle = new WritableCellFormat();
			emptyCellStyle.setBackground(WHITE, backgroundMode);
		}
		catch (IOException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
		catch (WriteException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}

	protected void createSheet(String name)
	{
		sheet = workbook.createSheet(name, Integer.MAX_VALUE);
		setSheetSettings(sheet);
	}

	protected void closeWorkbook(OutputStream os) throws JRException
	{
		try
		{
			workbook.write();
			workbook.close();
		}
		catch (IOException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
		catch (WriteException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}

	protected void setColumnWidth(short index, short width)
	{
		CellView cv = new CellView();
		cv.setSize(width);
		sheet.setColumnView(index, cv);
	}

	protected void setRowHeight(int y, int lastRowHeight) throws JRException
	{
		try
		{
			sheet.setRowView(y, (lastRowHeight * 20)); // twips
		}
		catch (RowsExceededException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}

	protected void setCell(int x, int y)
	{
	}

	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException
	{
		try
		{
			Colour forecolor = BLACK;
			if (gridCell.getForecolor() != null)
			{
				forecolor = getNearestColour(gridCell.getForecolor());
			}
			
			Colour backcolor = WHITE;
			if (gridCell.getBackcolor() != null)
			{
				backcolor = getNearestColour(gridCell.getBackcolor());
			}
			
			Pattern mode = backgroundMode;

			WritableFont cellFont = getLoadedFont(getDefaultFont(), forecolor.getValue());
			WritableCellFormat cellStyle = getLoadedCellStyle(mode, backcolor, 
					Alignment.LEFT.getValue(), VerticalAlignment.TOP.getValue(), Orientation.HORIZONTAL.getValue(),
					cellFont, gridCell);
			
			sheet.addCell(new Blank(colIndex, rowIndex, cellStyle));
		}
		catch (RowsExceededException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
		catch (WriteException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}

	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		addMergeRegion(gridCell, x, y);

		Colour forecolor2 = getNearestColour(line.getForecolor());
		WritableFont cellFont2 = this.getLoadedFont(getDefaultFont(), forecolor2.getValue());
		WritableCellFormat cellStyle2 = this.getLoadedCellStyle(Pattern.SOLID, forecolor2, Alignment.LEFT.getValue(),
																VerticalAlignment.TOP.getValue(), Orientation.HORIZONTAL.getValue(),
																cellFont2, gridCell);
		
		Blank cell2 = new Blank(x, y, cellStyle2);

		try
		{
			sheet.addCell(cell2);
		}
		catch (Exception e)
		{
			throw new JRException("Can't add cell.", e);
		}
	}

	protected void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		addMergeRegion(gridCell, x, y);

		Colour forecolor = getNearestColour(element.getForecolor());
		Colour backcolor = WHITE;
		Pattern mode = this.backgroundMode;

		if (element.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = Pattern.SOLID;
			backcolor = getNearestColour(element.getBackcolor());
		}
		else if (gridCell.getBackcolor() != null)
		{
			mode = Pattern.SOLID;
			backcolor = getNearestColour(gridCell.getBackcolor());
		}

		WritableFont cellFont2 = this.getLoadedFont(getDefaultFont(), forecolor.getValue());
		WritableCellFormat cellStyle2 = this.getLoadedCellStyle(mode, backcolor, Alignment.LEFT.getValue(), 
																VerticalAlignment.TOP.getValue(), Orientation.HORIZONTAL.getValue(),
																cellFont2, gridCell);
		Blank cell2 = new Blank(x, y, cellStyle2);

		try
		{
			sheet.addCell(cell2);
		}
		catch (Exception e)
		{
			throw new JRException("Can't add cell.", e);
		}
	}

	protected void exportText(JRPrintText text, JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		addMergeRegion(gridCell, x, y);

		JRStyledText styledText = getStyledText(text);

		if (styledText != null)
		{
			Colour forecolor = getNearestColour(text.getForecolor());
			WritableFont cellFont2 = this.getLoadedFont(text, forecolor.getValue());
			
			TextAlignHolder alignment = getTextAlignHolder(text);
			int horizontalAlignment = getHorizontalAlignment(alignment);
			int verticalAlignment = getVerticalAlignment(alignment);
			int rotation = getRotation(alignment);

			Pattern mode = this.backgroundMode;
			Colour backcolor = WHITE;

			if (text.getMode() == JRElement.MODE_OPAQUE)
			{
				mode = Pattern.SOLID;
				backcolor = getNearestColour(text.getBackcolor());
			}
			else if (gridCell.getBackcolor() != null)
			{
				mode = Pattern.SOLID;
				backcolor = getNearestColour(gridCell.getBackcolor());
			}
				

			WritableCellFormat cellStyle2 = this.getLoadedCellStyle(mode, backcolor, horizontalAlignment,
																	verticalAlignment, rotation, cellFont2,
																	gridCell);
			
			try
			{
				String textStr = styledText.getText();
				
				switch (text.getHyperlinkType())
				{
					case JRHyperlink.HYPERLINK_TYPE_REFERENCE:
					{
						if (text.getHyperlinkReference() != null)
						{
							URL url = new URL(text.getHyperlinkReference());
							WritableHyperlink hyperlink = new WritableHyperlink(x, y, x, y, url, textStr);
							sheet.addHyperlink(hyperlink);
							break;
						}
					}
					
					case JRHyperlink.HYPERLINK_TYPE_NONE:
					default:
					{
						if (isAutoDetectCellType)
						{
							try
							{
								double d = Double.parseDouble(textStr);
								sheet.addCell(new Number(x, y, d, cellStyle2));
							}
							catch (NumberFormatException nfe)
							{
								sheet.addCell(new Label(x, y, textStr, cellStyle2));
							}
						}
						else
						{
							sheet.addCell(new Label(x, y, textStr, cellStyle2));
						}
					}
				}
			}
			catch (Exception e)
			{
				throw new JRException("Can't add cell.", e);
			}
		}
	}

	protected void addMergeRegion(JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			try
			{
				sheet.mergeCells(x, y, (x + gridCell.colSpan - 1), (y + gridCell.rowSpan - 1));
			}
			catch (JXLException e)
			{
				throw new JRException("Can't merge cells.", e);
			}
		}
	}

	private int getHorizontalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.horizontalAlignment)
		{
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
				return Alignment.RIGHT.getValue();
			case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				return Alignment.CENTRE.getValue();
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
				return Alignment.JUSTIFY.getValue();
			case JRAlignment.HORIZONTAL_ALIGN_LEFT:
			default:
				return Alignment.LEFT.getValue();
		}
	}

	private int getVerticalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.verticalAlignment)
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM:
				return VerticalAlignment.BOTTOM.getValue();
			case JRAlignment.VERTICAL_ALIGN_MIDDLE:
				return VerticalAlignment.CENTRE.getValue();
			case JRAlignment.VERTICAL_ALIGN_JUSTIFIED:
				return VerticalAlignment.JUSTIFY.getValue();
			case JRAlignment.VERTICAL_ALIGN_TOP:
			default:
				return VerticalAlignment.TOP.getValue();
		}
	}

	private int getRotation(TextAlignHolder alignment)
	{
		switch (alignment.rotation)
		{
			case JRTextElement.ROTATION_LEFT:
				return Orientation.PLUS_90.getValue();
			case JRTextElement.ROTATION_RIGHT:
				return Orientation.MINUS_90.getValue();
			case JRTextElement.ROTATION_NONE:
			default:
				return Orientation.HORIZONTAL.getValue();
		}
	}

	protected void exportImage(JRPrintImage element, JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		addMergeRegion(gridCell, x, y);

		//if ((element.getRenderer() != null) && (element.getRenderer().getImageData() != null))
		//{
			try
			{
				JRRenderable renderer = element.getRenderer();
				
				int leftPadding = element.getLeftPadding();
				int topPadding = element.getTopPadding();
				int rightPadding = element.getRightPadding();
				int bottomPadding = element.getBottomPadding();

				int availableImageWidth = element.getWidth() - leftPadding - rightPadding;
				availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

				int availableImageHeight = element.getHeight() - topPadding - bottomPadding;
				availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;


				if (availableImageWidth > 0 && availableImageHeight > 0 && renderer != null)
				{
					int normalWidth = availableImageWidth;
					int normalHeight = availableImageHeight;

					Dimension2D dimension = renderer.getDimension();
					if (dimension != null)
					{
						normalWidth = (int) dimension.getWidth();
						normalHeight = (int) dimension.getHeight();
					}

					float xalignFactor = 0f;
					switch (element.getHorizontalAlignment())
					{
						case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
						{
							xalignFactor = 1f;
							break;
						}
						case JRAlignment.HORIZONTAL_ALIGN_CENTER:
						{
							xalignFactor = 0.5f;
							break;
						}
						case JRAlignment.HORIZONTAL_ALIGN_LEFT:
						default:
						{
							xalignFactor = 0f;
							break;
						}
					}

					float yalignFactor = 0f;
					switch (element.getVerticalAlignment())
					{
						case JRAlignment.VERTICAL_ALIGN_BOTTOM:
						{
							yalignFactor = 1f;
							break;
						}
						case JRAlignment.VERTICAL_ALIGN_MIDDLE:
						{
							yalignFactor = 0.5f;
							break;
						}
						case JRAlignment.VERTICAL_ALIGN_TOP:
						default:
						{
							yalignFactor = 0f;
							break;
						}
					}
					
					BufferedImage bi = new BufferedImage(availableImageWidth, availableImageHeight, BufferedImage.TYPE_INT_RGB);
					Graphics2D grx = bi.createGraphics();
					grx.setColor(element.getBackcolor());
					grx.fillRect(0, 0, availableImageWidth, availableImageHeight);
					
				
					switch (element.getScaleImage())
					{
						case JRImage.SCALE_IMAGE_CLIP:
						{
							int xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
							int yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));

							renderer.render(grx, new Rectangle(xoffset, yoffset,
								normalWidth, normalHeight));

							break;
						}
						case JRImage.SCALE_IMAGE_FILL_FRAME:
						{
							renderer.render(grx, new Rectangle(0, 0,
								availableImageWidth, availableImageHeight));

							break;
						}
						case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
						default:
						{
							if (element.getHeight() > 0)
							{
								double ratio = (double) normalWidth / (double) normalHeight;

								if (ratio > (double) availableImageWidth / (double) availableImageHeight)
								{
									normalWidth = availableImageWidth;
									normalHeight = (int) (availableImageWidth / ratio);
								}
								else
								{
									normalWidth = (int) (availableImageHeight * ratio);
									normalHeight = availableImageHeight;
								}

								int xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
								int yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));

								renderer.render(grx, new Rectangle(xoffset, yoffset,
									normalWidth, normalHeight));
							}

							break;
						}
					}
					
					Pattern mode = this.backgroundMode;
					Colour background = WHITE;
					Colour forecolor = getNearestColour(element.getForecolor());
					
					if (element.getBorderColor() != null ){
						forecolor = getNearestColour(element.getBorderColor());
					}
					
					WritableFont cellFont2 = this.getLoadedFont(getDefaultFont(), forecolor.getValue());
					
					if(element.getMode() == JRElement.MODE_OPAQUE ){
						mode = Pattern.SOLID;
						background = getNearestColour(element.getBackcolor());
					}
					
					WritableCellFormat cellStyle2 = this.getLoadedCellStyle(mode, background, Alignment.LEFT.getValue(),
																VerticalAlignment.TOP.getValue(), Orientation.HORIZONTAL.getValue(),
																cellFont2, gridCell);
					
				
					sheet.addCell(new Blank(x, y, cellStyle2));
					WritableImage image = new WritableImage(x, y, gridCell.colSpan, gridCell.rowSpan, 
															loadImageDataFromAWTImage(bi));
					
						
					sheet.addImage(image);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw new JRException("The cell cannot be added", ex);
			}
			catch (Error err)
			{
				err.printStackTrace();
				throw new JRException("The cell cannot be added", err);
			}
		//}
	}

	protected byte[] loadImageDataFromAWTImage(BufferedImage bi)
	{
		try
		{
			pngEncoder.setImage(bi);
			return pngEncoder.pngEncode();
		}
		finally
		{
			pngEncoder.setImage(null);
		}
	}

	private static Colour getNearestColour(Color awtColor)
	{
		Colour color = (Colour) colorsCache.get(awtColor);
		
		if (color == null)
		{
			Colour[] colors = Colour.getAllColours();
			if ((colors != null) && (colors.length > 0))
			{
				Colour crtColor = null;
				int[] rgb = null;
				int diff = 0;
				int minDiff = 999;

				for (int i = 0; i < colors.length; i++)
				{
					crtColor = colors[i];
					rgb = new int[3];
					rgb[0] = crtColor.getDefaultRGB().getRed();
					rgb[1] = crtColor.getDefaultRGB().getGreen();
					rgb[2] = crtColor.getDefaultRGB().getBlue();

					diff = Math.abs(rgb[0] - awtColor.getRed()) + Math.abs(rgb[1] - awtColor.getGreen()) + Math.abs(rgb[2] - awtColor.getBlue());

					if (diff < minDiff)
					{
						minDiff = diff;
						color = crtColor;
					}
				}
			}
			
			colorsCache.put(awtColor, color);
		}
		
		return color;
	}
	
	
	/*private static Colour getNearestColour(Color awtColor) {
		Colour retVal = null;
		Colour[] colors = Colour.getAllColours();
		
		int diff = 50;
		
		if (colors != null && colors.length > 0 ){
			Colour crtColor = null;
			for (int i = 0; i < colors.length; i++) {
				crtColor = colors[i];
				
				int red = crtColor.getDefaultRGB().getRed();
				if (Math.abs(awtColor.getRed() - red) < diff) {
					int green = crtColor.getDefaultRGB().getGreen();
					if (Math.abs(awtColor.getGreen() - green) < diff) {
						int blue = crtColor.getDefaultRGB().getBlue();
						if (Math.abs(awtColor.getBlue() - blue) < diff) {
							retVal = crtColor;
						}
					}
				}
			}
		}
		
		return retVal;
	}*/

	private WritableFont getLoadedFont(JRFont font, int forecolor) throws JRException
	{
		WritableFont cellFont2 = null;
		int fontSize;

		if (this.loadedFonts != null && this.loadedFonts.size() > 0)
		{
			WritableFont cf2 = null;

			for (int i = 0; i < this.loadedFonts.size(); i++)
			{
				cf2 = (WritableFont) this.loadedFonts.get(i);

				// FontSizeFix
				fontSize = font.getFontSize();
				if (isFontSizeFixEnabled)
					fontSize -= 1;

				if ((cf2.getName().equals(font.getFontName())) 
						&& (cf2.getColour().getValue() == forecolor) 
						&& (cf2.getPointSize() == fontSize)
						&& (cf2.getUnderlineStyle() == UnderlineStyle.SINGLE ? (font.isUnderline()) : (!font.isUnderline())) 
						&& (cf2.isStruckout() == font.isStrikeThrough())
						&& (cf2.getBoldWeight() == BoldStyle.BOLD.getValue() ? (font.isBold()) : (!font.isBold())) 
						&& (cf2.isItalic() == font.isItalic()))
				{
					cellFont2 = cf2;
					break;
				}
			}
		}

		try
		{
			if (cellFont2 == null)
			{

				// FontSizeFix
				fontSize = font.getFontSize();
				if (isFontSizeFixEnabled){
					fontSize -= 1;
				}

				cellFont2 = new WritableFont(WritableFont.createFont(font.getFontName()),
										fontSize, 
										font.isBold() ? WritableFont.BOLD : WritableFont.NO_BOLD, 
									    font.isItalic(),
									    font.isUnderline() ? UnderlineStyle.SINGLE : UnderlineStyle.NO_UNDERLINE,
									    Colour.getInternalColour(forecolor));
				
				this.loadedFonts.add(cellFont2);
			}
		}
		catch (Exception e)
		{
			throw new JRException("Can't get loaded fonts.", e);
		}
		return cellFont2;
	}

	protected static class StyleKey
	{
		int mode;
		int backcolor;
		int horizontalAlignment;
		int verticalAlignment;
		int rotation;
		int font;
		JRBox box;
		final int hashCode;
		
		protected StyleKey(Pattern mode, Colour backcolor, int horizontalAlignment, int verticalAlignment, int rotation, WritableFont font, JRBox box)
		{
			this.mode = mode.getValue();
			this.backcolor = backcolor.getValue();
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
			this.font = font.getFontIndex();
			this.box = box;
			
			int hash = this.mode;
			hash = 31*hash + this.backcolor;
			hash = 31*hash + horizontalAlignment;
			hash = 31*hash + verticalAlignment;
			hash = 31*hash + rotation;
			hash = 31*hash + this.font;
			if (box != null)
			{
				hash = 31*hash + box.hashCode();
			}
			
			hashCode = hash;
		}
		
		public int hashCode()
		{
			return hashCode;
		}
		
		public boolean equals(Object o)
		{
			StyleKey k = (StyleKey) o;
			
			return k.mode == mode && k.backcolor == backcolor &&
				k.horizontalAlignment == horizontalAlignment && k.verticalAlignment == verticalAlignment &&
				k.rotation == rotation && k.font == font &&
				(k.box == null ? box == null : (box != null && k.box.equals(box)));
		}
	}
	
	private WritableCellFormat getLoadedCellStyle(Pattern mode, Colour backcolor, int horizontalAlignment, 
												  int verticalAlignment, int rotation, WritableFont font, JRExporterGridCell gridCell) throws JRException
	{
		StyleKey styleKey = new StyleKey(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, gridCell.getBox());
		
		WritableCellFormat cellStyle = (WritableCellFormat) loadedCellStyles.get(styleKey);
		
		if (cellStyle == null)
		{
			BorderLineStyle topBorder = BorderLineStyle.NONE;
			BorderLineStyle bottomBorder = BorderLineStyle.NONE;
			BorderLineStyle leftBorder = BorderLineStyle.NONE;
			BorderLineStyle rightBorder = BorderLineStyle.NONE;
			Colour topBorderColour = BLACK;
			Colour bottomBorderColour = BLACK;
			Colour leftBorderColour = BLACK;
			Colour rightBorderColour = BLACK;
			
			JRBox box = gridCell.getBox();
			if(box != null)
			{
				if(box.getTopBorder() != JRGraphicElement.PEN_NONE)
				{
					topBorder = getBorderLineStyle(box.getTopBorder());
					topBorderColour = getNearestColour(box.getTopBorderColor());
				}
					
				if(box.getBottomBorder() != JRGraphicElement.PEN_NONE)
				{
					bottomBorder = getBorderLineStyle(box.getBottomBorder());
					bottomBorderColour = getNearestColour(box.getBottomBorderColor());
				}
					
				if(box.getLeftBorder()!= JRGraphicElement.PEN_NONE)
				{
					leftBorder = getBorderLineStyle(box.getLeftBorder());
					leftBorderColour = getNearestColour(box.getLeftBorderColor());
				}
					
				if(box.getRightBorder() != JRGraphicElement.PEN_NONE)
				{
					rightBorder = getBorderLineStyle(box.getRightBorder());
					rightBorderColour = getNearestColour(box.getRightBorderColor());
				}	
			}
			
			
			try
			{
				cellStyle = new WritableCellFormat(font);
				cellStyle.setBackground(backcolor, mode);
				cellStyle.setAlignment(Alignment.getAlignment(horizontalAlignment));
				cellStyle.setVerticalAlignment(VerticalAlignment.getAlignment(verticalAlignment));
				cellStyle.setOrientation(Orientation.getOrientation(rotation));
				cellStyle.setWrap(true);
				
				cellStyle.setBorder(Border.TOP, topBorder, topBorderColour);
				cellStyle.setBorder(Border.BOTTOM, bottomBorder, bottomBorderColour);
				cellStyle.setBorder(Border.LEFT, leftBorder, leftBorderColour);
				cellStyle.setBorder(Border.RIGHT, rightBorder, rightBorderColour);
			}
			catch (Exception e)
			{
				throw new JRException("Error setting cellFormat-template.", e);
			}
			
			loadedCellStyles.put(styleKey, cellStyle);
		}
		
		return cellStyle;
	}
	
	/**
	 * @param lineStyle
	 */
	private BorderLineStyle getBorderLineStyle(byte lineStyle) {
		BorderLineStyle retVal = null;
		switch(lineStyle) {
			case JRGraphicElement.PEN_THIN:
				retVal =  BorderLineStyle.THIN;
				break;
				
			case JRGraphicElement.PEN_1_POINT:
			case JRGraphicElement.PEN_2_POINT:
				retVal = BorderLineStyle.MEDIUM;
				break;
			
			case JRGraphicElement.PEN_4_POINT:
				retVal = BorderLineStyle.THICK;
				break;
				
			case JRGraphicElement.PEN_DOTTED:
				retVal = BorderLineStyle.DOTTED;
				break;
				
			default:
				retVal = BorderLineStyle.NONE;
		}
		
		return retVal;
	}

	private final void setSheetSettings(WritableSheet sheet)
	{
		PageOrientation po;
		PaperSize ps;

		if (jasperPrint.getOrientation() == JRReport.ORIENTATION_PORTRAIT)
			po = PageOrientation.PORTRAIT;
		else
			po = PageOrientation.LANDSCAPE;

		if ((ps = getSuitablePaperSize(jasperPrint)) != null)
			sheet.setPageSetup(po, ps, 0, 0);
		else
			sheet.setPageSetup(po);

		SheetSettings sheets = sheet.getSettings();

		sheets.setTopMargin(0.0);
		sheets.setLeftMargin(0.0);
		sheets.setRightMargin(0.0);
		sheets.setBottomMargin(0.0);

		sheets.setHeaderMargin(0.0);
		sheets.setFooterMargin(0.0);
	}

	private final PaperSize getSuitablePaperSize(JasperPrint jasP)
	{

		if (jasP == null)
			return null;

		long width = 0;
		long height = 0;
		PaperSize ps = null;

		if ((jasP.getPageWidth() != 0) && (jasP.getPageHeight() != 0))
		{

			double dWidth = (jasP.getPageWidth() / 72.0);
			double dHeight = (jasP.getPageHeight() / 72.0);

			height = Math.round(dHeight * 25.4);
			width = Math.round(dWidth * 25.4);

			// Compare to ISO 216 A-Series (A3-A5). All other ISO 216 formats
			// not supported by JExcelApi yet.
			for (int i = 3; i < 6; i++)
			{
				int w = calculateWidthForDinAN(i);
				int h = calculateHeightForDinAN(i);

				if (((w == width) && (h == height)) || ((h == width) && (w == height)))
				{
					if (i == 3)
						ps = PaperSize.A3;
					else if (i == 4)
						ps = PaperSize.A4;
					else if (i == 5)
						ps = PaperSize.A5;
					break;
				}
			}

			// Compare to common North American Paper Sizes (ANSI X3.151-1987).
			if (ps == null)
			{
				// ANSI X3.151-1987 - "Letter" (216 � 279 mm)
				if (((width == 216) && (height == 279)) || ((width == 279) && (height == 216)))
				{
					ps = PaperSize.LETTER;
				}
				// ANSI X3.151-1987 - "Legal" (216 � 356 mm)
				if (((width == 216) && (height == 356)) || ((width == 356) && (height == 216)))
				{
					ps = PaperSize.LEGAL;
				}
				// ANSI X3.151-1987 - "Executive" (190 � 254 mm)
				// Not supperted by JExcelApi yet.

				// ANSI X3.151-1987 - "Ledger/Tabloid" (279 � 432 mm)
				// Not supperted by JExcelApi yet.
			}
		}
		return ps;
	}

	
	public static TextAlignHolder getTextAlignHolder(JRPrintText textElement)
	{
		short horizontalAlignment;
		short verticalAlignment;
		short rotation = textElement.getRotation();

		switch (textElement.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				switch (textElement.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_MIDDLE;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_CENTER;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				switch (textElement.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_MIDDLE;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_CENTER;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalAlignment();
				verticalAlignment = textElement.getVerticalAlignment();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}
	
	
	
	// Berechnungsvorschriften f�r die DIN Formate A, B, und C.
	// Die Angabe der Breite/H�he erfolgt in [mm].

	private final int calculateWidthForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (-0.25 - (n / 2.0))) * 1000.0);
	}

	private final int calculateHeightForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (0.25 - (n / 2.0))) * 1000.0);
	}

	private final int calculateWidthForDinBN(int n)
	{
		return (int) (Math.pow(2.0, -(n / 2.0)) * 1000.0);
	}

	private final int calculateHeightForDinBN(int n)
	{
		return (int) (Math.pow(2.0, (0.5 - (n / 2.0))) * 1000.0);
	}

	private final int calculateWidthForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (-0.125 - (n / 2.0))) * 1000.0);
	}

	private final int calculateHeightForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (0.375 - (n / 2.0))) * 1000.0);
	}

	protected ExporterElements getExporterElements()
	{
		return JRGridLayout.UNIVERSAL_EXPORTER;
	}

	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		addMergeRegion(gridCell, x, y);

		Colour forecolor = getNearestColour(frame.getForecolor());
		Colour backcolor = WHITE;
		Pattern mode = backgroundMode;

		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = Pattern.SOLID;
			backcolor = getNearestColour(frame.getBackcolor());
		}

		WritableFont cellFont = getLoadedFont(getDefaultFont(), forecolor.getValue());
		WritableCellFormat cellStyle = getLoadedCellStyle(mode, backcolor, 
				Alignment.LEFT.getValue(), VerticalAlignment.TOP.getValue(), Orientation.HORIZONTAL.getValue(),
				cellFont, gridCell);
		
		Blank cell = new Blank(x, y, cellStyle);
		try
		{
			sheet.addCell(cell);
		}
		catch (JXLException e)
		{
			throw new JRException("Can't add cell.", e);
		}
	}
}
