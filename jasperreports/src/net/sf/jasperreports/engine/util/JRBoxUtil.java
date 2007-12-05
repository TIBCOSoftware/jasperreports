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
package net.sf.jasperreports.engine.util;

import java.awt.Color;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRLineBox;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRStyleResolver.java 1577 2007-02-09 11:25:48Z teodord $
 */
public class JRBoxUtil
{

	/**
	 * 
	 */
	public static JRLineBox clone(JRLineBox box, boolean keepLeft, boolean keepRight, boolean keepTop, boolean keepBottom, JRLineBox complementaryBox)
	{
		JRLineBox clone = (JRLineBox)box.clone();
		
		if (keepLeft && box.getLeftPen().getLineWidth().floatValue() > 0f)
		{
			clone.getLeftPen().setLineWidth(box.getLeftPen().getLineWidth());
			clone.getLeftPen().setLineColor(box.getLeftPen().getLineColor());
			clone.setLeftPadding(box.getLeftPadding());
		}
		else if (complementaryBox != null)
		{
			clone.getLeftPen().setLineWidth(complementaryBox.getLeftPen().getLineWidth());
			clone.getLeftPen().setLineColor(complementaryBox.getLeftPen().getLineColor());
			clone.setLeftPadding(complementaryBox.getLeftPadding());
		}
		
		if (keepRight && box.getRightPen().getLineWidth().floatValue() > 0f)
		{
			clone.getRightPen().setLineWidth(box.getRightPen().getLineWidth());
			clone.getRightPen().setLineColor(box.getRightPen().getLineColor());
			clone.setRightPadding(box.getRightPadding());
		}
		else if (complementaryBox != null)
		{
			clone.getRightPen().setLineWidth(complementaryBox.getRightPen().getLineWidth());
			clone.getRightPen().setLineColor(complementaryBox.getRightPen().getLineColor());
			clone.setRightPadding(complementaryBox.getRightPadding());
		}
		
		if (keepTop && box.getTopPen().getLineWidth().floatValue() > 0f)
		{
			clone.getTopPen().setLineWidth(box.getTopPen().getLineWidth());
			clone.getTopPen().setLineColor(box.getTopPen().getLineColor());
			clone.setTopPadding(box.getTopPadding());
		}
		else if (complementaryBox != null)
		{
			clone.getTopPen().setLineWidth(complementaryBox.getTopPen().getLineWidth());
			clone.getTopPen().setLineColor(complementaryBox.getTopPen().getLineColor());
			clone.setTopPadding(complementaryBox.getTopPadding());
		}
		
		if (keepBottom && box.getBottomPen().getLineWidth().floatValue() > 0f)
		{
			clone.getBottomPen().setLineWidth(box.getBottomPen().getLineWidth());
			clone.getBottomPen().setLineColor(box.getBottomPen().getLineColor());
			clone.setBottomPadding(box.getBottomPadding());
		}
		else if (complementaryBox != null)
		{
			clone.getBottomPen().setLineWidth(complementaryBox.getBottomPen().getLineWidth());
			clone.getBottomPen().setLineColor(complementaryBox.getBottomPen().getLineColor());
			clone.setBottomPadding(complementaryBox.getBottomPadding());
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
	 * 
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
	public static void setBoxToLineBox(
		JRBox box,
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
	

}
