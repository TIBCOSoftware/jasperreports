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
package net.sf.jasperreports.engine.util;

import java.awt.Color;

import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.type.RotationEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class JRBoxUtil
{

	/**
	 * 
	 */
	public static JRLineBox copyBordersNoPadding(JRLineBox box, boolean keepLeft, boolean keepRight, boolean keepTop, boolean keepBottom, JRLineBox complementaryBox)
	{
		JRLineBox clone = box.clone(box.getBoxContainer());
		
		clone.setTopPadding(0);
		clone.setLeftPadding(0);
		clone.setBottomPadding(0);
		clone.setRightPadding(0);
		
		//FIXMEBORDER does not copy padding correctly, if we only test line width
		if (!keepLeft || box.getLeftPen().getLineWidth().floatValue() <= 0f)
		{
			if (complementaryBox != null)
			{
				clone.getLeftPen().setLineWidth(complementaryBox.getLeftPen().getLineWidth());
				clone.getLeftPen().setLineColor(complementaryBox.getLeftPen().getLineColor());
				clone.getLeftPen().setLineStyle(complementaryBox.getLeftPen().getLineStyleValue());
				//clone.setLeftPadding(complementaryBox.getLeftPadding());
			}
			else
			{
				clone.getLeftPen().setLineWidth(0);
			}
		}
		
		if (!keepRight || box.getRightPen().getLineWidth().floatValue() <= 0f)
		{
			if (complementaryBox != null)
			{
				clone.getRightPen().setLineWidth(complementaryBox.getRightPen().getLineWidth());
				clone.getRightPen().setLineColor(complementaryBox.getRightPen().getLineColor());
				clone.getRightPen().setLineStyle(complementaryBox.getRightPen().getLineStyleValue());
				//clone.setRightPadding(complementaryBox.getRightPadding());
			}
			else
			{
				clone.getRightPen().setLineWidth(0);
			}
		}
		
		if (!keepTop || box.getTopPen().getLineWidth().floatValue() <= 0f)
		{
			if (complementaryBox != null)
			{
				clone.getTopPen().setLineWidth(complementaryBox.getTopPen().getLineWidth());
				clone.getTopPen().setLineColor(complementaryBox.getTopPen().getLineColor());
				clone.getTopPen().setLineStyle(complementaryBox.getTopPen().getLineStyleValue());
				//clone.setTopPadding(complementaryBox.getTopPadding());
			}
			else
			{
				clone.getTopPen().setLineWidth(0);
			}
		}
		
		if (!keepBottom || box.getBottomPen().getLineWidth().floatValue() <= 0f)
		{
			if (complementaryBox != null)
			{
				clone.getBottomPen().setLineWidth(complementaryBox.getBottomPen().getLineWidth());
				clone.getBottomPen().setLineColor(complementaryBox.getBottomPen().getLineColor());
				clone.getBottomPen().setLineStyle(complementaryBox.getBottomPen().getLineStyleValue());
				//clone.setBottomPadding(complementaryBox.getBottomPadding());
			}
			else
			{
				clone.getBottomPen().setLineWidth(0);
			}
		}
		
		return clone;
	}

	
	/**
	 * 
	 */
	public static void reset(JRLineBox box, boolean resetLeft, boolean resetRight, boolean resetTop, boolean resetBottom)
	{
		if (resetLeft)
		{
			box.getLeftPen().setLineWidth(0f);
		}
		
		if (resetRight)
		{
			box.getRightPen().setLineWidth(0f);
		}

		if (resetTop)
		{
			box.getTopPen().setLineWidth(0f);
		}
		
		if (resetBottom)
		{
			box.getBottomPen().setLineWidth(0f);
		}
	}
	

	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static void setToBox(
		Byte border,
		Byte topBorder,
		Byte leftBorder,
		Byte bottomBorder,
		Byte rightBorder,
		Color borderColor,
		Color topBorderColor,
		Color leftBorderColor,
		Color bottomBorderColor,
		Color rightBorderColor,
		Integer padding,
		Integer topPadding,
		Integer leftPadding,
		Integer bottomPadding,
		Integer rightPadding,
		JRLineBox box
		)
	{
		JRPenUtil.setLinePenFromPen(border, box.getPen());
		JRPenUtil.setLinePenFromPen(topBorder, box.getTopPen());
		JRPenUtil.setLinePenFromPen(leftBorder, box.getLeftPen());
		JRPenUtil.setLinePenFromPen(bottomBorder, box.getBottomPen());
		JRPenUtil.setLinePenFromPen(rightBorder, box.getRightPen());
		
		box.getPen().setLineColor(borderColor);
		box.getTopPen().setLineColor(topBorderColor);
		box.getLeftPen().setLineColor(leftBorderColor);
		box.getBottomPen().setLineColor(bottomBorderColor);
		box.getRightPen().setLineColor(rightBorderColor);
		
		box.setPadding(padding);
		box.setTopPadding(topPadding);
		box.setLeftPadding(leftPadding);
		box.setBottomPadding(bottomPadding);
		box.setRightPadding(rightPadding);
	}
	

	/**
	 * 
	 */
	public static void copy(JRLineBox source, JRLineBox dest)
	{
		dest.setLeftPadding(source.getOwnLeftPadding());
		dest.copyLeftPen(source.getLeftPen());
		dest.setRightPadding(source.getOwnRightPadding());
		dest.copyRightPen(source.getRightPen());
		dest.setTopPadding(source.getOwnTopPadding());
		dest.copyTopPen(source.getTopPen());
		dest.setBottomPadding(source.getOwnBottomPadding());
		dest.copyBottomPen(source.getBottomPen());
		dest.setPadding(source.getOwnPadding());
		dest.copyPen(source.getPen());
	}
	

	/**
	 * 
	 */
	public static void rotate(JRLineBox box, RotationEnum rotation)
	{
		switch (rotation)
		{
			case LEFT : 
			{
				JRBoxPen topPen = box.getTopPen();
				Integer topPadding = box.getTopPadding();
				
				box.copyTopPen(box.getLeftPen());
				box.setTopPadding(box.getLeftPadding());

				box.copyLeftPen(box.getBottomPen());
				box.setLeftPadding(box.getBottomPadding());
				
				box.copyBottomPen(box.getRightPen());
				box.setBottomPadding(box.getRightPadding());
				
				box.copyRightPen(topPen);
				box.setRightPadding(topPadding);

				break;
			}
			case RIGHT : 
			{
				JRBoxPen topPen = box.getTopPen();
				Integer topPadding = box.getTopPadding();
				
				box.copyTopPen(box.getRightPen());
				box.setTopPadding(box.getRightPadding());

				box.copyRightPen(box.getBottomPen());
				box.setRightPadding(box.getBottomPadding());

				box.copyBottomPen(box.getLeftPen());
				box.setBottomPadding(box.getLeftPadding());

				box.copyLeftPen(topPen);
				box.setLeftPadding(topPadding);
				
				break;
			}
			case UPSIDE_DOWN : 
			{
				JRBoxPen topPen = box.getTopPen();
				Integer topPadding = box.getTopPadding();
				
				box.copyTopPen(box.getBottomPen());
				box.setTopPadding(box.getBottomPadding());

				box.copyBottomPen(topPen);
				box.setBottomPadding(topPadding);
				
				JRBoxPen leftPen = box.getLeftPen();
				Integer leftPadding = box.getLeftPadding();
				
				box.copyLeftPen(box.getRightPen());
				box.setLeftPadding(box.getRightPadding());

				box.copyRightPen(leftPen);
				box.setRightPadding(leftPadding);

				break;
			}
			case NONE :
			default :
			{
			}
		}
	}
	

	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static void setBoxToLineBox(
		net.sf.jasperreports.engine.JRBox box,
		JRLineBox lineBox
		)
	{
		setToBox(
			box.getOwnBorder(),
			box.getOwnTopBorder(),
			box.getOwnLeftBorder(),
			box.getOwnBottomBorder(),
			box.getOwnRightBorder(),
			box.getOwnBorderColor(),
			box.getOwnTopBorderColor(),
			box.getOwnLeftBorderColor(),
			box.getOwnBottomBorderColor(),
			box.getOwnRightBorderColor(),
			box.getOwnPadding(),
			box.getOwnTopPadding(),
			box.getOwnLeftPadding(),
			box.getOwnBottomPadding(),
			box.getOwnRightPadding(),
			lineBox
			);
	}
	
	public static void eraseBox(JRLineBox box)
	{
		box.setBottomPadding(0);
		box.setTopPadding(0);
		box.setLeftPadding(0);
		box.setRightPadding(0);
		box.getBottomPen().setLineWidth(0);
		box.getTopPen().setLineWidth(0);
		box.getLeftPen().setLineWidth(0);
		box.getRightPen().setLineWidth(0);
	}

	private JRBoxUtil()
	{
	}
}
