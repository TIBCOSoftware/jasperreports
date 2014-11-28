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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRPenUtil;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @deprecated Replaced by {@link JRBaseLineBox}
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseBox implements JRBox, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRLineBox lineBox;

	
	/**
	 * @deprecated Replaced by {@link JRBaseLineBox#getDefaultStyleProvider()}
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider() 
	{
		return lineBox.getDefaultStyleProvider();
	}

	/**
	 * @deprecated Replaced by {@link JRBaseLineBox#getStyle()}
	 */
	public JRStyle getStyle() 
	{
		return lineBox.getStyle();
	}

	/**
	 * @deprecated Replaced by {@link JRBaseLineBox#getStyleNameReference()}
	 */
	public String getStyleNameReference()
	{
		return lineBox.getStyleNameReference();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public Byte getOwnBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getPen());
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public Color getOwnBorderColor()
	{
		return lineBox.getPen().getOwnLineColor();
	}
	
	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnPadding()}
	 */
	public Integer getOwnPadding()
	{
		return lineBox.getOwnPadding();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public Byte getOwnTopBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public Color getOwnTopBorderColor()
	{
		return lineBox.getTopPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnTopPadding()}
	 */
	public Integer getOwnTopPadding()
	{
		return lineBox.getOwnTopPadding();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public Byte getOwnLeftBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public Color getOwnLeftBorderColor()
	{
		return lineBox.getLeftPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnLeftPadding()}
	 */
	public Integer getOwnLeftPadding()
	{
		return lineBox.getOwnLeftPadding();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public Byte getOwnBottomBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public Color getOwnBottomBorderColor()
	{
		return lineBox.getBottomPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnBottomPadding()}
	 */
	public Integer getOwnBottomPadding()
	{
		return lineBox.getOwnBottomPadding();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public Byte getOwnRightBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(lineBox.getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public Color getOwnRightBorderColor()
	{
		return lineBox.getRightPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnRightPadding()}
	 */
	public Integer getOwnRightPadding()
	{
		return lineBox.getOwnRightPadding();
	}
	

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	/**
	 * @deprecated
	 */
	private Byte border;
	/**
	 * @deprecated
	 */
	private Byte topBorder;
	/**
	 * @deprecated
	 */
	private Byte leftBorder;
	/**
	 * @deprecated
	 */
	private Byte bottomBorder;
	/**
	 * @deprecated
	 */
	private Byte rightBorder;
	/**
	 * @deprecated
	 */
	private Color borderColor;
	/**
	 * @deprecated
	 */
	private Color topBorderColor;
	/**
	 * @deprecated
	 */
	private Color leftBorderColor;
	/**
	 * @deprecated
	 */
	private Color bottomBorderColor;
	/**
	 * @deprecated
	 */
	private Color rightBorderColor;
	/**
	 * @deprecated
	 */
	private Integer padding;
	/**
	 * @deprecated
	 */
	private Integer topPadding;
	/**
	 * @deprecated
	 */
	private Integer leftPadding;
	/**
	 * @deprecated
	 */
	private Integer bottomPadding;
	/**
	 * @deprecated
	 */
	private Integer rightPadding;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (lineBox == null)
		{
			lineBox = new JRBaseLineBox(null);
			JRBoxUtil.setToBox(
				border,
				topBorder,
				leftBorder,
				bottomBorder,
				rightBorder,
				borderColor,
				topBorderColor,
				leftBorderColor,
				bottomBorderColor,
				rightBorderColor,
				padding,
				topPadding,
				leftPadding,
				bottomPadding,
				rightPadding,
				lineBox
				);
			border = null;
			topBorder = null;
			leftBorder = null;
			bottomBorder = null;
			rightBorder = null;
			borderColor = null;
			topBorderColor = null;
			leftBorderColor = null;
			bottomBorderColor = null;
			rightBorderColor = null;
			padding = null;
			topPadding = null;
			leftPadding = null;
			bottomPadding = null;
			rightPadding = null;
		}
	}

}
