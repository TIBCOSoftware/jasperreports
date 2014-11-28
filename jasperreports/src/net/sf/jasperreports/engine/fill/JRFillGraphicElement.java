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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRFillGraphicElement extends JRFillElement implements JRGraphicElement
{

	protected final JRPen initPen;
	protected JRPen pen;

	/**
	 *
	 */
	protected JRFillGraphicElement(
		JRBaseFiller filler,
		JRGraphicElement graphicElement, 
		JRFillObjectFactory factory
		)
	{
		super(filler, graphicElement, factory);
		
		initPen = graphicElement.getLinePen().clone(this);
	}


	protected JRFillGraphicElement(
		JRFillGraphicElement graphicElement, 
		JRFillCloneFactory factory
		)
	{
		super(graphicElement, factory);
		
		initPen = graphicElement.getLinePen().clone(this);
	}

	
	/**
	 *
	 */
	protected void evaluateStyle(
		byte evaluation
		) throws JRException
	{
		super.evaluateStyle(evaluation);

		pen = null;
		
		if (providerStyle != null)
		{
			pen = initPen.clone(this);
			JRStyleResolver.appendPen(pen, providerStyle.getLinePen());
		}
	}


	/**
	 *
	 */
	public JRPen getLinePen()
	{
		return pen == null ? initPen : pen;
	}

	/**
	 * 
	 */
	public FillEnum getFillValue()
	{
		return JRStyleResolver.getFillValue(this);
	}

	/**
	 * 
	 */
	public FillEnum getOwnFillValue()
	{
		return providerStyle == null || providerStyle.getOwnFillValue() == null ? ((JRGraphicElement)this.parent).getOwnFillValue() : providerStyle.getOwnFillValue();
	}

	/**
	 *
	 */
	public void setFill(FillEnum fill)
	{
		throw new UnsupportedOperationException();
	}
	

	/**
	 * 
	 */
	public Float getDefaultLineWidth() 
	{
		return ((JRGraphicElement)this.parent).getDefaultLineWidth();
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}
	

	/**
	 *
	 */
	public void rewind()
	{
	}


	/**
	 *
	 */
	protected boolean prepare(
		int availableHeight,
		boolean isOverflow
		) throws JRException
	{
		boolean willOverflow = false;

		super.prepare(availableHeight, isOverflow);
		
		if (!this.isToPrint())
		{
			return willOverflow;
		}
		
		boolean isToPrint = true;
		boolean isReprinted = false;

		if (isOverflow && this.isAlreadyPrinted() && !this.isPrintWhenDetailOverflows())
		{
			isToPrint = false;
		}

		if (
			isToPrint && 
			this.isPrintWhenExpressionNull() &&
			!this.isPrintRepeatedValues()
			)
		{
			if (
				( !this.isPrintInFirstWholeBand() || !this.getBand().isFirstWholeOnPageColumn() ) &&
				( this.getPrintWhenGroupChanges() == null || !this.getBand().isNewGroup(this.getPrintWhenGroupChanges()) ) &&
				( !isOverflow || !this.isPrintWhenDetailOverflows() )
				)
			{
				isToPrint = false;
			}
		}

		if (
			isToPrint && 
			availableHeight < this.getRelativeY() + getHeight()
			)
		{
			isToPrint = false;
			willOverflow = true;
		}
		
		if (
			isToPrint && 
			isOverflow && 
			//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
			(this.isPrintWhenDetailOverflows() && (this.isAlreadyPrinted() || (!this.isAlreadyPrinted() && !this.isPrintRepeatedValues())))
			)
		{
			isReprinted = true;
		}

		this.setToPrint(isToPrint);
		this.setReprinted(isReprinted);
		
		return willOverflow;
	}
	
	
}
