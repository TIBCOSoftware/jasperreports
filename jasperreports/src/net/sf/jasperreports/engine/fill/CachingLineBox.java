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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBoxPen;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class CachingLineBox implements JRLineBox
{
	private final JRLineBox base;
	
	private final Integer padding;
	private final Integer topPadding;
	private final Integer bottomPadding;
	private final Integer leftPadding;
	private final Integer rightPadding;
	
	public CachingLineBox(JRLineBox base)
	{
		this.base = base;
		
		padding = base.getPadding();
		topPadding = base.getTopPadding();
		bottomPadding = base.getBottomPadding();
		leftPadding = base.getLeftPadding();
		rightPadding = base.getRightPadding();
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return base.getDefaultStyleProvider();
	}

	public Float getDefaultLineWidth()
	{
		return base.getDefaultLineWidth();
	}

	public JRStyle getStyle()
	{
		return base.getStyle();
	}

	public Color getDefaultLineColor()
	{
		return base.getDefaultLineColor();
	}

	public String getStyleNameReference()
	{
		return base.getStyleNameReference();
	}

	public JRBoxContainer getBoxContainer()
	{
		return base.getBoxContainer();
	}

	public JRLineBox clone(JRBoxContainer boxContainer)
	{
		// cloning the base line box
		return base.clone(boxContainer);
	}

	public JRBoxPen getPen()
	{
		return base.getPen();
	}

	public void copyPen(JRBoxPen pen)
	{
		throw new UnsupportedOperationException();
	}

	public JRBoxPen getTopPen()
	{
		return base.getTopPen();
	}

	public void copyTopPen(JRBoxPen topPen)
	{
		throw new UnsupportedOperationException();
	}

	public JRBoxPen getLeftPen()
	{
		return base.getLeftPen();
	}

	public void copyLeftPen(JRBoxPen leftPen)
	{
		throw new UnsupportedOperationException();
	}

	public JRBoxPen getBottomPen()
	{
		return base.getBottomPen();
	}

	public void copyBottomPen(JRBoxPen bottomPen)
	{
		throw new UnsupportedOperationException();
	}

	public JRBoxPen getRightPen()
	{
		return base.getRightPen();
	}

	public void copyRightPen(JRBoxPen rightPen)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getPadding()
	{
		return padding;
	}

	public Integer getOwnPadding()
	{
		return base.getOwnPadding();
	}

	public void setPadding(int padding)
	{
		throw new UnsupportedOperationException();
	}

	public void setPadding(Integer padding)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getTopPadding()
	{
		return topPadding;
	}

	public Integer getOwnTopPadding()
	{
		return base.getOwnTopPadding();
	}

	public void setTopPadding(int padding)
	{
		throw new UnsupportedOperationException();
	}

	public void setTopPadding(Integer padding)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getLeftPadding()
	{
		return leftPadding;
	}

	public Integer getOwnLeftPadding()
	{
		return base.getOwnLeftPadding();
	}

	public void setLeftPadding(int padding)
	{
		throw new UnsupportedOperationException();
	}

	public void setLeftPadding(Integer padding)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getBottomPadding()
	{
		return bottomPadding;
	}

	public Integer getOwnBottomPadding()
	{
		return base.getOwnBottomPadding();
	}

	public void setBottomPadding(int padding)
	{
		throw new UnsupportedOperationException();
	}

	public void setBottomPadding(Integer padding)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getRightPadding()
	{
		return rightPadding;
	}

	public Integer getOwnRightPadding()
	{
		return base.getOwnRightPadding();
	}

	public void setRightPadding(int padding)
	{
		throw new UnsupportedOperationException();
	}

	public void setRightPadding(Integer padding)
	{
		throw new UnsupportedOperationException();
	}

}
