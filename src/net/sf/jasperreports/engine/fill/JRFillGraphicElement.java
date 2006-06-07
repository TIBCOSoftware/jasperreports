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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillGraphicElement extends JRFillElement implements JRGraphicElement
{


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
	}


	protected JRFillGraphicElement(
		JRFillGraphicElement graphicElement, 
		JRFillCloneFactory factory
		)
	{
		super(graphicElement, factory);
	}
	

	/**
	 *
	 */
	public byte getPen()
	{
		return JRStyleResolver.getPen(this, PEN_1_POINT);
	}
	
	/**
	 *
	 */
	public Byte getOwnPen()
	{
		return ((JRGraphicElement)this.parent).getOwnPen();
	}

	/**
	 *
	 */
	public void setPen(byte pen)
	{
	}
	
	/**
	 *
	 */
	public void setPen(Byte pen)
	{
	}
	
	/**
	 *
	 */
	public byte getFill()
	{
		return JRStyleResolver.getFill(this, FILL_SOLID);
	}

	/**
	 *
	 */
	public Byte getOwnFill()
	{
		return ((JRGraphicElement)this.parent).getOwnFill();
	}

	/**
	 *
	 */
	public void setFill(byte fill)
	{
	}
	
	/**
	 *
	 */
	public void setFill(Byte fill)
	{
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
		int availableStretchHeight,
		boolean isOverflow
		) throws JRException
	{
		boolean willOverflow = false;

		super.prepare(availableStretchHeight, isOverflow);
		
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
			availableStretchHeight < this.getRelativeY() - this.getY() - this.getBandBottomY()
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
