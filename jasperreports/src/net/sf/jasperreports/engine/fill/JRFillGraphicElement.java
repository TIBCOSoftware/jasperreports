/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGraphicElement;


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
	

	/**
	 *
	 */
	public byte getPen()
	{
		return ((JRGraphicElement)this.parent).getPen();
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
	public byte getFill()
	{
		return ((JRGraphicElement)this.parent).getFill();
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
	public void rewind() throws JRException
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
				( !this.isPrintInFirstWholeBand() || !this.getBand().isNewPageColumn() ) &&
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
