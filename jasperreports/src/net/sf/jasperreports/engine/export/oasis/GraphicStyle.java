/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.export.oasis;

import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class GraphicStyle extends Style
{
	/**
	 * The ratio of 72 dpi to 96 dpi
	 */
	public static final Double DPI_RATIO = 72.0/96.0;
	
	/**
	 *
	 */
	private String backcolor;
	private String forecolor;
	private String style;
	private String width;
	private String hAlign;
	private String vAlign;
	private double cropTop;
	private double cropLeft;
	private double cropBottom;
	private double cropRight;

	private String clip;


	public GraphicStyle(WriterHelper styleWriter, JRPrintGraphicElement element)
	{
		this(styleWriter, element, 0, 0, 0, 0);
	}
	
	/**
	 *
	 */
	public GraphicStyle(
			WriterHelper styleWriter, 
			JRPrintGraphicElement element, 
			double cropTop,
			double cropLeft,
			double cropBottom,
			double cropRight
			)
	{
		super(styleWriter);
		this.cropTop = cropTop;
		this.cropLeft = cropLeft;
		this.cropBottom = cropBottom;
		this.cropRight = cropRight;
		
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			//fill = "solid";
			backcolor = JRColorUtil.getColorHexa(element.getBackcolor());
		}
//		else
//		{
//			//fill = "none";
//		}

		forecolor = JRColorUtil.getColorHexa(element.getLinePen().getLineColor());

		double doubleWidth = element.getLinePen().getLineWidth().doubleValue();
		if (doubleWidth < 0)
		{
			style = "none";
		}
		else
		{
			switch (element.getLinePen().getLineStyleValue())
			{
				case DOTTED : //FIXMEBORDER
				case DASHED :
				{
					style = "dash";
					break;
				}
				case SOLID :
				default :
				{
					style = "solid";
					break;
				}
			}
		}

		width = String.valueOf(LengthUtil.inchNoRound(doubleWidth));
		HorizontalImageAlignEnum horizontalAlignment = HorizontalImageAlignEnum.LEFT;
		VerticalImageAlignEnum verticalAlignment = VerticalImageAlignEnum.TOP;

		if(element instanceof JRPrintImage)
		{
			JRPrintImage imageElement = (JRPrintImage)element;
			horizontalAlignment = imageElement.getHorizontalImageAlign();
			verticalAlignment = imageElement.getVerticalImageAlign();
			if(imageElement.getScaleImageValue() == ScaleImageEnum.CLIP 
					&& (cropTop > 0 || cropLeft > 0 || cropBottom > 0 || cropRight > 0))
			{
				clip = " fo:clip=\"rect("
						+ LengthUtil.inchNoRound(cropTop * DPI_RATIO)
						+ "in,"
						+ LengthUtil.inchNoRound(cropRight * DPI_RATIO) 
						+ "in,"
						+ LengthUtil.inchNoRound(cropBottom * DPI_RATIO) 
						+ "in,"
						+ LengthUtil.inchNoRound(cropLeft * DPI_RATIO) 
						+ "in)\"";
			}
		}

		switch(horizontalAlignment)
		{
			case RIGHT:
			{
				hAlign = "right";
				break;
			}
			case CENTER:
			{
				hAlign = "center";
				break;
			}
			case LEFT:
			default:
			{
				hAlign = "left";
				break;
			}
		}

		switch(verticalAlignment)
		{
			case BOTTOM:
			{
				vAlign = "bottom";
				break;
			}
			case MIDDLE:
			{
				vAlign = "middle";
				break;
			}
			case TOP:
			default:
			{
				vAlign = "top";
				break;
			}

		}
	}

	@Override
	public String getId()
	{
		//return fill + "|" + backcolor
		StringBuilder id = new StringBuilder();
		id.append(backcolor);
		id.append("|");
		id.append(forecolor);
		id.append("|");
		id.append(style);
		id.append("|");
		id.append(width);
		id.append("|");
		id.append(hAlign);
		id.append("|");
		id.append(vAlign);
		id.append("|");
		id.append(cropTop);
		id.append("|");
		id.append(cropLeft);
		id.append("|");
		id.append(cropBottom);
		id.append("|");
		id.append(cropRight);
		return id.toString();
	}

	@Override
	public void write(String lineStyleName)
	{
		styleWriter.write(" <style:style style:name=\"" + lineStyleName + "\"");
		styleWriter.write(" style:family=\"graphic\" style:parent-style-name=\"Graphics\">\n");
		styleWriter.write("   <style:graphic-properties");
		styleWriter.write(" draw:fill-color=\"#" + backcolor + "\"");
		styleWriter.write(" style:horizontal-pos=\""+hAlign+ "\" style:horizontal-rel=\"paragraph\"");
		styleWriter.write(" style:vertical-pos=\""+vAlign+ "\" style:vertical-rel=\"paragraph\"");
		if(clip != null)
		{
			styleWriter.write(clip);
		}
		styleWriter.write(" svg:stroke-color=\"#" + forecolor + "\"");
		styleWriter.write(" draw:stroke=\"" + style + "\"");//FIXMENOW dashed borders do not work; only dashed lines and ellipses seem to work
		styleWriter.write(" draw:stroke-dash=\"Dashed\"");
		styleWriter.write(" svg:stroke-width=\"" + width + "in\"");
		styleWriter.write("/>\n");
		styleWriter.write("</style:style>\n");
	}

}

