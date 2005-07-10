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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillElement implements JRElement
{


	/**
	 *
	 */
	protected JRElement parent = null;
	protected JRTemplateElement template = null;

	/**
	 *
	 */
	protected JRBaseFiller filler = null;

	/**
	 *
	 */
	protected JRGroup printWhenGroupChanges = null;
	protected JRFillElementGroup elementGroup = null;

	/**
	 *
	 */
	protected JRFillBand band = null;

	/**
	 *
	 */
	private boolean isPrintWhenExpressionNull = true;
	private boolean isPrintWhenTrue = true;
	private boolean isToPrint = true;
	private boolean isReprinted = false;
	private boolean isAlreadyPrinted = false;
	private Collection dependantElements = new ArrayList();
	private int relativeY = 0;
	private int stretchHeight = 0;
	private int bandBottomY = 0;

	/**
	 *
	 *
	private JRElement topElementInGroup = null;
	private JRElement bottomElementInGroup = null;


	/**
	 *
	 */
	protected JRFillElement(
		JRBaseFiller filler, 
		JRElement element, 
		JRFillObjectFactory factory
		)
	{
		factory.put(element, this);

		this.parent = element;
		this.filler = filler;

		/*   */
		printWhenGroupChanges = factory.getGroup(element.getPrintWhenGroupChanges());
		elementGroup = factory.getElementGroup(element.getElementGroup());
	}


	/**
	 *
	 */
	public String getKey()
	{
		return this.parent.getKey();
	}

	/**
	 *
	 */
	public byte getPositionType()
	{
		return this.parent.getPositionType();
	}

	/**
	 *
	 */
	public void setPositionType(byte positionType)
	{
	}
		
	/**
	 *
	 */
	public byte getStretchType()
	{
		return this.parent.getStretchType();
	}

	/**
	 *
	 */
	public void setStretchType(byte stretchType)
	{
	}

	/**
	 *
	 */
	public boolean isPrintRepeatedValues()
	{
		return this.parent.isPrintRepeatedValues();
	}
	
	/**
	 *
	 */
	public void setPrintRepeatedValues(boolean isPrintRepeatedValues)
	{
	}
		
	/**
	 *
	 */
	public byte getMode()
	{
		return this.parent.getMode();
	}
	
	/**
	 *
	 */
	public void setMode(byte mode)
	{
	}
	
	/**
	 *
	 */
	public int getX()
	{
		return this.parent.getX();
	}
	
	/**
	 *
	 */
	public void setX(int x)
	{
	}
	
	/**
	 *
	 */
	public int getY()
	{
		return this.parent.getY();
	}
	
	/**
	 *
	 */
	public int getWidth()
	{
		return this.parent.getWidth();
	}
	
	/**
	 *
	 */
	public void setWidth(int width)
	{
	}
	
	/**
	 *
	 */
	public int getHeight()
	{
		return this.parent.getHeight();
	}
	
	/**
	 *
	 */
	public boolean isRemoveLineWhenBlank()
	{
		return this.parent.isRemoveLineWhenBlank();
	}
	
	/**
	 *
	 */
	public void setRemoveLineWhenBlank(boolean isRemoveLine)
	{
	}
	
	/**
	 *
	 */
	public boolean isPrintInFirstWholeBand()
	{
		return this.parent.isPrintInFirstWholeBand();
	}
	
	/**
	 *
	 */
	public void setPrintInFirstWholeBand(boolean isPrint)
	{
	}
	
	/**
	 *
	 */
	public boolean isPrintWhenDetailOverflows()
	{
		return this.parent.isPrintWhenDetailOverflows();
	}
	
	/**
	 *
	 */
	public void setPrintWhenDetailOverflows(boolean isPrint)
	{
	}
	
	/**
	 *
	 */
	public Color getForecolor()
	{
		return this.parent.getForecolor();
	}
	
	/**
	 *
	 */
	public void setForecolor(Color forecolor)
	{
	}
	
	/**
	 *
	 */
	public Color getBackcolor()
	{
		return this.parent.getBackcolor();
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
	}

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return this.parent.getPrintWhenExpression();
	}
	
	/**
	 *
	 */
	public JRGroup getPrintWhenGroupChanges()
	{
		return this.printWhenGroupChanges;
	}
	
	/**
	 *
	 */
	public JRElementGroup getElementGroup()
	{
		return this.elementGroup;
	}
	
	/**
	 *
	 */
	protected boolean isPrintWhenExpressionNull()
	{
		return this.isPrintWhenExpressionNull;
	}
	
	/**
	 *
	 */
	protected void setPrintWhenExpressionNull(boolean isPrintWhenExpressionNull)
	{
		this.isPrintWhenExpressionNull = isPrintWhenExpressionNull;
	}
	
	/**
	 *
	 */
	protected boolean isPrintWhenTrue()
	{
		return this.isPrintWhenTrue;
	}
	
	/**
	 *
	 */
	protected void setPrintWhenTrue(boolean isPrintWhenTrue)
	{
		this.isPrintWhenTrue = isPrintWhenTrue;
	}
	
	/**
	 *
	 */
	protected boolean isToPrint()
	{
		return this.isToPrint;
	}
	
	/**
	 *
	 */
	protected void setToPrint(boolean isToPrint)
	{
		this.isToPrint = isToPrint;
	}
	
	/**
	 *
	 */
	protected boolean isReprinted()
	{
		return this.isReprinted;
	}
	
	/**
	 *
	 */
	protected void setReprinted(boolean isReprinted)
	{
		this.isReprinted = isReprinted;
	}
	
	/**
	 *
	 */
	protected boolean isAlreadyPrinted()
	{
		return this.isAlreadyPrinted;
	}
	
	/**
	 *
	 */
	protected void setAlreadyPrinted(boolean isAlreadyPrinted)
	{
		this.isAlreadyPrinted = isAlreadyPrinted;
	}

	/**
	 *
	 */
	protected JRElement[] getGroupElements()
	{
		JRElement[] groupElements = null;
		
		if (elementGroup != null)
		{
			groupElements = elementGroup.getElements();
		}
		
		return groupElements;
	}

	/**
	 *
	 *
	protected void setGroupElements(Collection groupElements)
	{
		this.groupElements = groupElements;
	}

	/**
	 *
	 *
	protected void addGroupElement(JRFElement element)
	{
		this.groupElements.add(element);
	}

	/**
	 *
	 */
	protected Collection getDependantElements()
	{
		return this.dependantElements;
	}

	/**
	 *
	 */
	protected void addDependantElement(JRElement element)
	{
		this.dependantElements.add(element);
	}

	/**
	 *
	 */
	protected int getRelativeY()
	{
		return this.relativeY;
	}

	/**
	 *
	 */
	protected void setRelativeY(int relativeY)
	{
		this.relativeY = relativeY;
	}

	/**
	 *
	 */
	protected int getStretchHeight()
	{
		return this.stretchHeight;
	}

	/**
	 *
	 */
	protected void setStretchHeight(int stretchHeight)
	{
		if (stretchHeight > this.getHeight())
		{
			this.stretchHeight = stretchHeight;
		}
		else
		{
			this.stretchHeight = this.getHeight();
		}
	}

	/**
	 *
	 */
	protected int getBandBottomY()
	{
		return this.bandBottomY;
	}

	/**
	 *
	 */
	protected void setBandBottomY(int bandBottomY)
	{
		this.bandBottomY = bandBottomY;
	}

	/**
	 *
	 */
	protected JRFillBand getBand()
	{
		return this.band;
	}

	/**
	 *
	 */
	protected void setBand(JRFillBand band)
	{
		this.band = band;
	}


	/**
	 *
	 */
	protected void reset()
	{
		relativeY = parent.getY();
		stretchHeight = parent.getHeight();

		if (elementGroup != null)
		{
			elementGroup.reset();
		}
	}


	/**
	 *
	 */
	protected abstract void evaluate(
		byte evaluation
		) throws JRException;
	
	
	/**
	 *
	 */
	protected void evaluatePrintWhenExpression(
		byte evaluation
		) throws JRException
	{
		boolean isPrintWhenExpressionNull = true;
		boolean isPrintWhenTrue = false;

		JRExpression expression = this.getPrintWhenExpression();
		if (expression != null)
		{
			isPrintWhenExpressionNull = false;
			Boolean printWhenExpressionValue = (Boolean)this.filler.calculator.evaluate(expression, evaluation);
			if (printWhenExpressionValue == null)
			{
				isPrintWhenTrue = false;
			}
			else
			{
				isPrintWhenTrue = printWhenExpressionValue.booleanValue();
			}
		}

		this.setPrintWhenExpressionNull(isPrintWhenExpressionNull);
		this.setPrintWhenTrue(isPrintWhenTrue);
	}


	/**
	 *
	 */
	protected abstract void rewind() throws JRException;
	
	
	/**
	 *
	 */
	protected abstract JRPrintElement fill() throws JRException;
	
	
	/**
	 *
	 */
	protected boolean prepare(
		int availableStretchHeight,
		boolean isOverflow
		) throws JRException
	{
		if (
			this.isPrintWhenExpressionNull() ||
			( !this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue() )
			)
		{
			this.setToPrint(true);
		}
		else
		{
			this.setToPrint(false);
		}

		this.setReprinted(false);
		
		return false;
	}
		
	
	
	/**
	 *
	 */
	protected void stretchElement(int bandStretch)
	{
		switch (this.getStretchType())
		{
			case JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT :
			{
				this.setStretchHeight(this.getHeight() + bandStretch);
				break;
			}
			case JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT :
			{
				if (this.elementGroup != null)
				{
					//this.setStretchHeight(this.getHeight() + this.getStretchHeightDiff());
					this.setStretchHeight(this.getHeight() + elementGroup.getStretchHeightDiff());
				}
				
				break;
			}
			case JRElement.STRETCH_TYPE_NO_STRETCH :
			default :
			{
				break;
			}
		}
	}
	
	
	/**
	 *
	 */
	protected void moveDependantElements()
	{
		Collection elements = this.getDependantElements();
		if (elements != null && elements.size() > 0)
		{
			JRFillElement element = null;
			int diffY = 0;
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRFillElement)it.next();
				
				diffY = element.getY() - this.getY() - this.getHeight() - 
					(element.getRelativeY() - this.getRelativeY() - this.getStretchHeight());
				
				if (diffY < 0)
				{
					diffY = 0;
				}
				
				element.setRelativeY(element.getRelativeY() + diffY);
			}
		}
	}
	

	/**
	 *
	 *
	private int getStretchHeightDiff()
	{
		if (this.topElementInGroup == null)
		{
			this.setTopBottomElements();
		}
		
		JRFillElement topElem = null;
		JRFillElement bottomElem = null;

		if (this.elementGroup != null)
		{
			JRElement[] elements = this.elementGroup.getElements();
	
			if (elements != null && elements.length > 0)
			{
				JRFillElement element = null;
				
				for(int i = 0; i < elements.length; i++)
				{
					element = (JRFillElement)elements[i];
					if (element != this && element.isToPrint())
					//if (element.isToPrint())
					{
						if (
							topElem == null ||
							(topElem != null &&
							element.getRelativeY() + element.getStretchHeight() <
							topElem.getRelativeY() + topElem.getStretchHeight())
							)
						{
							topElem = element;
						}
	
						if (
							bottomElem == null ||
							(bottomElem != null &&
							element.getRelativeY() + element.getStretchHeight() >
							bottomElem.getRelativeY() + bottomElem.getStretchHeight())
							)
						{
							bottomElem = element;
						}
					}
				}
			}
		}

		if (topElem == null)
		{
			topElem = this;
		}

		if (bottomElem == null)
		{
			bottomElem = this;
		}

		int diff = 
			bottomElem.getRelativeY() + bottomElem.getStretchHeight() - topElem.getRelativeY() -
			(this.bottomElementInGroup.getY() + this.bottomElementInGroup.getHeight() - this.topElementInGroup.getY());

		if (diff < 0)
		{
			diff = 0;
		}
		
		return diff;
	}


	/**
	 *
	 *
	private void setTopBottomElements()
	{
		if (this.elementGroup != null)
		{
			JRElement[] elements = this.elementGroup.getElements();
	
			if (elements != null && elements.length > 0)
			{
				for(int i = 0; i < elements.length; i++)
				{
					if (elements[i] != this)
					{
						if (
							this.topElementInGroup == null ||
							(this.topElementInGroup != null &&
							elements[i].getY() + elements[i].getHeight() <
							this.topElementInGroup.getY() + this.topElementInGroup.getHeight())
							)
						{
							this.topElementInGroup = elements[i];
						}
	
						if (
							this.bottomElementInGroup == null ||
							(this.bottomElementInGroup != null &&
							elements[i].getY() + elements[i].getHeight() >
							this.bottomElementInGroup.getY() + this.bottomElementInGroup.getHeight())
							)
						{
							this.bottomElementInGroup = elements[i];
						}
					}
				}
			}
		}
		
		if (this.topElementInGroup == null)
		{
			this.topElementInGroup = this;
		}

		if (this.bottomElementInGroup == null)
		{
			this.bottomElementInGroup = this;
		}
		
	}
	*/


}
