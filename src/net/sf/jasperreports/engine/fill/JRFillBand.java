/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRReportFont;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBand extends JRFillElementGroup implements JRBand
{
	

	/**
	 *
	 */
	private JRBand parent = null;

	/**
	 *
	 */
	protected JRBaseFiller filler = null;

	/**
	 *
	 */
	private JRFillElement[] ySortedElements = null;
	private JRFillElement[] stretchElements = null;
	private JRFillElement[] bandBottomElements = null;
	private JRFillElement[] removableElements = null;
	private boolean willOverflow = false;
	private boolean isOverflow = false;
	private boolean isPrintWhenTrue = true;
	
	/**
	 *
	 */
	private int stretchHeight = 0;
	private int firstY = 0;
	private boolean isFirstYFound = false;

	/**
	 *
	 */
	private boolean isNewPageColumn = false;
	private Map isNewGroupMap = new HashMap();
	

	/**
	 *
	 */
	protected JRFillBand(
		JRBaseFiller filler,
		JRBand band, 
		JRFillObjectFactory factory
		)
	{
		super(band, factory);

		this.parent = band;
		this.filler = filler;
		
		if (this.elements != null && this.elements.length > 0)
		{
			List sortedElemsList = new ArrayList();
			List stretchElemsList = new ArrayList();
			List bandBottomElemsList = new ArrayList();
			List removableElemsList = new ArrayList();
			for(int i = 0; i < this.elements.length; i++)
			{
				this.elements[i].setBand(this);

				this.elements[i].setBandBottomY(
					this.getHeight() - this.elements[i].getY() - this.elements[i].getHeight()
					);
				
				sortedElemsList.add(this.elements[i]);
				
				if (this.elements[i].getPositionType() == JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM)
				{
					bandBottomElemsList.add(elements[i]);
				}

				if (this.elements[i].getStretchType() != JRElement.STRETCH_TYPE_NO_STRETCH)
				{
					stretchElemsList.add(elements[i]);
				}
				
				if (this.elements[i].isRemoveLineWhenBlank())
				{
					removableElemsList.add(elements[i]);
				}
			}

			/*    */
			Collections.sort(sortedElemsList, new JRYComparator());
			this.ySortedElements = new JRFillElement[this.elements.length];
			sortedElemsList.toArray(this.ySortedElements);

			/*    */
			this.stretchElements = new JRFillElement[stretchElemsList.size()];
			stretchElemsList.toArray(this.stretchElements);

			/*    */
			this.bandBottomElements = new JRFillElement[bandBottomElemsList.size()];
			bandBottomElemsList.toArray(this.bandBottomElements);

			/*    */
			this.removableElements = new JRFillElement[removableElemsList.size()];
			removableElemsList.toArray(this.removableElements);
		}
		
		/*    */
		this.setDependentElements();
	}

		
	/**
	 *
	 */
	private void setDependentElements()
	{
		if (ySortedElements != null && ySortedElements.length > 0)
		{
			JRFillElement iElem = null;
			JRFillElement jElem = null;
			int left = 0;
			int right = 0;
			for(int i = 0; i < ySortedElements.length - 1; i++)
			{
				iElem = ySortedElements[i];

				for(int j = i + 1; j < ySortedElements.length; j++)
				{
					jElem = ySortedElements[j];
					
					left = Math.min(iElem.getX(), jElem.getX());
					right = Math.max(iElem.getX() + iElem.getWidth(), jElem.getX() + jElem.getWidth());
					
					if (
						jElem.getPositionType() == JRElement.POSITION_TYPE_FLOAT &&
						iElem.getY() + iElem.getHeight() <= jElem.getY() &&
						iElem.getWidth() + jElem.getWidth() > right - left // FIXME band bottom elements should not have dependent elements
						)
					{
						iElem.addDependantElement(jElem);
					}
				}

				/*
				if (iElem.getParent().getElementGroup() != null) //parent might be null
				{
					iElem.setGroupElements(
						iElem.getParent().getElementGroup().getElements()
						);
				}
				*/
			}
		}
	}


	/**
	 *
	 */
	protected void setNewPageColumn(boolean isNew)
	{
		this.isNewPageColumn = isNew;
	}


	/**
	 *
	 */
	protected boolean isNewPageColumn()
	{
		return this.isNewPageColumn;
	}


	/**
	 *
	 */
	protected void setNewGroup(JRGroup group, boolean isNew)
	{
		this.isNewGroupMap.put(group, isNew ? Boolean.TRUE : Boolean.FALSE);
	}


	/**
	 *
	 */
	protected boolean isNewGroup(JRGroup group)
	{
		Boolean value = (Boolean)this.isNewGroupMap.get(group);
		
		if (value == null)
		{
			value = Boolean.FALSE;
		}
		
		return value.booleanValue();
	}


	/**
	 *
	 */
	public int getHeight()
	{
		return (this.parent != null ? this.parent.getHeight() : 0);
	}
		
	/**
	 *
	 */
	public boolean isSplitAllowed()
	{
		return this.parent.isSplitAllowed();
	}
		
	/**
	 *
	 */
	public void setSplitAllowed(boolean isSplitAllowed)
	{
	}
		
	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return (this.parent != null ? this.parent.getPrintWhenExpression() : null);
	}
	
	/**
	 *
	 */
	protected boolean willOverflow()
	{
		return this.willOverflow;
	}

	/**
	 *
	 */
	protected boolean isPrintWhenExpressionNull()
	{
		return (this.getPrintWhenExpression() == null);
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
		return 
			(this.isPrintWhenExpressionNull() ||
			(!this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue()));
	}
	
	/**
	 *
	 */
	protected void evaluate(byte evaluation) throws JRException
	{
		//this.evaluatePrintWhenExpression(evaluation);

		//if (
		//	(this.isPrintWhenExpressionNull() ||
		//	(!this.isPrintWhenExpressionNull() && 
		//	this.isPrintWhenTrue()))
		//	)
		//{
			JRElement[] elements = this.getElements();
			if (elements != null && elements.length > 0)
			{
				for(int i = 0; i < elements.length; i++)
				{
					((JRFillElement)elements[i]).evaluate(evaluation);
				}
			}
		//}
	}


	/**
	 *
	 */
	protected void evaluatePrintWhenExpression(
		byte evaluation
		) throws JRException
	{
		boolean isPrintTrue = false;

		JRExpression expression = this.getPrintWhenExpression();
		if (expression != null)
		{
			Boolean printWhenExpressionValue = (Boolean)this.filler.calculator.evaluate(expression, evaluation);
			if (printWhenExpressionValue == null)
			{
				isPrintTrue = false;
			}
			else
			{
				isPrintTrue = printWhenExpressionValue.booleanValue();
			}
		}

		this.setPrintWhenTrue(isPrintTrue);
	}



	/**
	 *
	 */
	protected JRPrintBand refill(
		int availableStretchHeight
		) throws JRException
	{
		this.rewind();

		return this.fill(availableStretchHeight);
	}
	
	
	/**
	 *
	 */
	protected JRPrintBand fill() throws JRException
	{
		return this.fill(0, false);
	}
	
	
	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableStretchHeight
		) throws JRException
	{
		return this.fill(availableStretchHeight, true);
	}
	
	
	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableStretchHeight,
		boolean isOverflowAllowed
		) throws JRException
	{
		if (
			Thread.currentThread().isInterrupted()
			|| this.filler.isInterrupted()
			)
		{
			// child fillers will stop if this parent filler was marked as interrupted
			this.filler.setInterrupted(true);

			throw new JRFillInterruptedException();
		}

		this.isOverflow = this.willOverflow;
		this.firstY = 0;
		this.isFirstYFound = false;
		
		this.resetElements();

		this.prepareElements(availableStretchHeight, isOverflowAllowed);

		this.stretchElements();

		this.moveBandBottomElements();

		this.removeBlankElements();
		
		this.isNewPageColumn = false;
		this.isNewGroupMap = new HashMap();

		return this.fillElements();
	}
	
	
	/**
	 *
	 */
	protected void rewind() throws JRException
	{
		JRElement[] elements = this.ySortedElements;
		if (elements != null && elements.length > 0)
		{
			JRFillElement element = null;
			for(int i = 0; i < elements.length; i++)
			{
				element = (JRFillElement)elements[i];

				element.rewind();

				element.setAlreadyPrinted(false);
			}
		}
		
		this.willOverflow = false;
	}


	/**
	 *
	 */
	private void resetElements() throws JRException
	{
		JRElement[] elements = this.ySortedElements;
		if (elements != null && elements.length > 0)
		{
			JRFillElement element = null;
			for(int i = 0; i < elements.length; i++)
			{
				element = (JRFillElement)elements[i];

				element.reset();
				
				if (!this.isOverflow)
				{
					element.setAlreadyPrinted(false);
				}
			}
		}
	}


	/**
	 *
	 */
	private void prepareElements(
		int availableStretchHeight,
		boolean isOverflowAllowed
		) throws JRException
	{
		boolean willOverflow = false;

		int maxBandStretch = 0;
		int bandStretch = 0;

		firstY = isOverflow ? getHeight() : 0;

		JRElement[] elements = this.ySortedElements;
		if (elements != null && elements.length > 0)
		{
			JRFillElement element = null;
			for(int i = 0; i < elements.length; i++)
			{
				element = (JRFillElement)elements[i];

				willOverflow = element.prepare(availableStretchHeight, this.isOverflow) || willOverflow;

				element.moveDependantElements();

				if (element.isToPrint())
				{
					if (this.isOverflow)
					{
						if (element.isReprinted())
						{
							this.firstY = 0;
						}
						else if (!this.isFirstYFound)
						{
							this.firstY = element.getY();
						}
						
						this.isFirstYFound = true;
					}

					bandStretch = element.getRelativeY() + element.getStretchHeight() - this.getHeight() + element.getBandBottomY();
					if (bandStretch > maxBandStretch)
					{
						maxBandStretch = bandStretch;
					}
				}
			}
		}

		if (maxBandStretch > availableStretchHeight)
		{
			//willOverflow = true;
		}
		
		if (willOverflow)
		{
			this.stretchHeight = this.getHeight() + availableStretchHeight;
		}
		else
		{
			this.stretchHeight = this.getHeight() + maxBandStretch;
		}

		this.willOverflow = willOverflow && isOverflowAllowed;
	}


	/**
	 *
	 */
	private void stretchElements() throws JRException
	{
		JRElement[] elements = this.stretchElements;
		if (elements != null && elements.length > 0)
		{
			JRFillElement element = null;
			for(int i = 0; i < elements.length; i++)
			{
				element = (JRFillElement)elements[i];
				
				element.stretchElement(this.stretchHeight - this.getHeight());
				
				element.moveDependantElements();
			}
		}
	}


	/**
	 *
	 */
	private void moveBandBottomElements() throws JRException
	{
		//if (!this.willOverflow)
		//{
			JRElement[] elements = this.bandBottomElements;
			if (elements != null && elements.length > 0)
			{
				JRFillElement element = null;
				for(int i = 0; i < elements.length; i++)
				{
					element = (JRFillElement)elements[i];

					element.setRelativeY(
						element.getY() + this.stretchHeight - this.getHeight()
						);

					// band bottom elements do not print if there will be an overflow
					element.setToPrint(element.isToPrint() && !this.willOverflow);
				}
			}
		//}
	}


	/**
	 *
	 */
	private void removeBlankElements() throws JRException
	{
		JRElement[] remElems = this.removableElements;
		if (remElems != null && remElems.length > 0)
		{
			JRElement[] elems = this.ySortedElements;
			
			JRFillElement iElem = null;
			JRFillElement jElem = null;

			int top = 0;
			int bottom = 0;
			boolean isToRemove = true;
			
			for(int i = 0; i < remElems.length; i++)
			{
				iElem = (JRFillElement)remElems[i];

				if (
					!iElem.isToPrint() && 
					iElem.getRelativeY() + iElem.getStretchHeight() <= this.stretchHeight &&
					iElem.getRelativeY() >= this.firstY
					)
				{
					isToRemove = true;
					
					for(int j = 0; j < elems.length; j++)
					{
						jElem = (JRFillElement)elems[j];
						
						if (iElem != jElem && jElem.isToPrint())
						{
							top = 
								Math.min(iElem.getRelativeY(), jElem.getRelativeY());
							bottom = 
								Math.max(
									iElem.getRelativeY() + iElem.getHeight(), 
									jElem.getRelativeY() + jElem.getStretchHeight()
									);
							
							if (iElem.getHeight() + jElem.getStretchHeight() > bottom - top)
							{
								isToRemove = false;
								break;
							}
						}
					}
					
					if (isToRemove)
					{
						for(int j = 0; j < elems.length; j++)
						{
							jElem = (JRFillElement)elems[j];
							
							if (jElem.getRelativeY() >= iElem.getRelativeY() + iElem.getHeight())
							{
								jElem.setRelativeY(jElem.getRelativeY() - iElem.getHeight());
							}
						}
						
						this.stretchHeight = this.stretchHeight - iElem.getHeight();
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private JRPrintBand fillElements() throws JRException
	{
		JRPrintBand printBand = new JRPrintBand();

		//int maxStretch = 0;
		//int stretch = 0;
		JRElement[] elements = this.getElements();
		if (elements != null && elements.length > 0)
		{
			JRFillElement element = null;
			JRPrintElement printElement = null;
			for(int i = 0; i < elements.length; i++)
			{
				element = (JRFillElement)elements[i];
				
				element.setRelativeY(element.getRelativeY() - this.firstY);

				if (element.getRelativeY() + element.getStretchHeight() > this.stretchHeight)
				{
					element.setToPrint(false);
				}
				
				element.setAlreadyPrinted(element.isToPrint() || element.isAlreadyPrinted());
				
				if (element.isToPrint())
				{
					printElement = element.fill();
					//printElement.setY(printElement.getY() - this.firstY);

					if (printElement != null)
					{
						//FIXME not all elements affect height
						//stretch = printElement.getY() + this.firstY + printElement.getHeight() - element.getY() - element.getHeight();
						//if (stretch > maxStretch)
						//{
						//	maxStretch = stretch;
						//}
						printBand.addElement(printElement);
						
						if (element instanceof JRFillSubreport)
						{
							JRFillSubreport subreport = (JRFillSubreport)element;
							
							JRReportFont[] fonts = subreport.getFonts();
							if (fonts != null)
							{
								for(int j = 0; j < fonts.length; j++)
								{
									try
									{
										filler.getJasperPrint().addFont(fonts[j]);
									}
									catch(JRException e)
									{
										//ignore font duplication exception
									}
								}
							}
							
							Collection printElements = subreport.getPrintElements();
							if (printElements != null && printElements.size() > 0)
							{
								for(Iterator it = printElements.iterator(); it.hasNext();)
								{
									printElement = (JRPrintElement)it.next();
									printElement.setX(element.getX() + printElement.getX());
									printElement.setY(element.getRelativeY() + printElement.getY());
									printBand.addElement(printElement);
								}
							}
						}
					}
				}
			}
		}
		
		//printBand.setHeight(this.getHeight() + maxStretch - this.firstY);
		printBand.setHeight(this.stretchHeight - this.firstY);

		return printBand;
	}


}
