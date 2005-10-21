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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementContainer;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStyle;

/**
 * Abstract implementation of an element container filler.
 * <p>
 * This is the base for band, frame and crosstab cell fillers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillElementContainer extends JRFillElementGroup
{
	protected JRBaseFiller filler;
	
	private JRFillElement[] ySortedElements = null;
	private JRFillElement[] stretchElements = null;
	private JRFillElement[] bandBottomElements = null;
	private JRFillElement[] removableElements = null;
	
	private boolean willOverflow = false;
	protected boolean isOverflow = false;
	
	private int stretchHeight = 0;
	private int firstY = 0;
	private boolean isFirstYFound = false;
	
	protected JRFillElementContainer(JRBaseFiller filler, JRElementGroup elementGrp, JRFillObjectFactory factory)
	{
		super(elementGrp, factory);
		
		this.filler = filler;
	}


	protected final void initElements()
	{
		if (this.elements != null && this.elements.length > 0)
		{
			List sortedElemsList = new ArrayList();
			List stretchElemsList = new ArrayList();
			List bandBottomElemsList = new ArrayList();
			List removableElemsList = new ArrayList();
			for(int i = 0; i < this.elements.length; i++)
			{
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

			/*   */
			Collections.sort(sortedElemsList, new JRYComparator());
			this.ySortedElements = new JRFillElement[this.elements.length];
			sortedElemsList.toArray(this.ySortedElements);

			/*   */
			this.stretchElements = new JRFillElement[stretchElemsList.size()];
			stretchElemsList.toArray(this.stretchElements);

			/*   */
			this.bandBottomElements = new JRFillElement[bandBottomElemsList.size()];
			bandBottomElemsList.toArray(this.bandBottomElements);

			/*   */
			this.removableElements = new JRFillElement[removableElemsList.size()];
			removableElemsList.toArray(this.removableElements);
		}
		
		/*   */
		setDependentElements();
		
		setElementsBandBottomY();
	}

	protected final void setElementsBandBottomY()
	{
		if (this.elements != null && this.elements.length > 0)
		{
			for(int i = 0; i < this.elements.length; i++)
			{
				this.elements[i].setBandBottomY(
					this.getContainerHeight() - this.elements[i].getY() - this.elements[i].getHeight()
					);
			}
		}
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
	protected void evaluate(byte evaluation) throws JRException
	{
		//this.evaluatePrintWhenExpression(evaluation);

		//if (
		//	(this.isPrintWhenExpressionNull() ||
		//	(!this.isPrintWhenExpressionNull() && 
		//	this.isPrintWhenTrue()))
		//	)
		//{
			JRElement[] allElements = this.getElements();
			if (allElements != null && allElements.length > 0)
			{
				for(int i = 0; i < allElements.length; i++)
				{
					((JRFillElement)allElements[i]).evaluate(evaluation);
				}
			}
		//}
	}


	/**
	 *
	 */
	protected void resetElements()
	{
		if (ySortedElements != null && ySortedElements.length > 0)
		{
			for(int i = 0; i < ySortedElements.length; i++)
			{
				JRFillElement element = ySortedElements[i];

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
	protected boolean willOverflow()
	{
		return this.willOverflow;
	}


	protected void initFill()
	{
		this.isOverflow = this.willOverflow;
		this.firstY = 0;
		this.isFirstYFound = false;
	}


	/**
	 *
	 */
	protected void prepareElements(
		int availableStretchHeight,
		boolean isOverflowAllowed
		) throws JRException
	{
		boolean tmpWillOverflow = false;

		int maxBandStretch = 0;
		int bandStretch = 0;

		firstY = isOverflow ? getContainerHeight() : 0;

		if (ySortedElements != null && ySortedElements.length > 0)
		{
			for(int i = 0; i < ySortedElements.length; i++)
			{
				JRFillElement element = ySortedElements[i];

				tmpWillOverflow = element.prepare(availableStretchHeight, this.isOverflow) || tmpWillOverflow;

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

					bandStretch = element.getRelativeY() + element.getStretchHeight() - getContainerHeight() + element.getBandBottomY();
					if (bandStretch > maxBandStretch)
					{
						maxBandStretch = bandStretch;
					}
				}
			}
		}

		if (maxBandStretch > availableStretchHeight)
		{
			tmpWillOverflow = true;
		}
		
		if (tmpWillOverflow)
		{
			this.stretchHeight = getContainerHeight() + availableStretchHeight;
		}
		else
		{
			this.stretchHeight = getContainerHeight() + maxBandStretch;
		}

		this.willOverflow = tmpWillOverflow && isOverflowAllowed;
	}


	/**
	 *
	 */
	protected void stretchElements()
	{
		if (stretchElements != null && stretchElements.length > 0)
		{
			for(int i = 0; i < stretchElements.length; i++)
			{
				JRFillElement element = stretchElements[i];
				
				element.stretchElement(this.stretchHeight - getContainerHeight());
				
				element.moveDependantElements();
			}
		}
	}

	
	protected int getStretchHeight()
	{
		return stretchHeight;
	}

	
	/**
	 *
	 */
	protected void moveBandBottomElements()
	{
		//if (!this.willOverflow)
		//{
			if (bandBottomElements != null && bandBottomElements.length > 0)
			{
				for(int i = 0; i < bandBottomElements.length; i++)
				{
					JRFillElement element = bandBottomElements[i];

					element.setRelativeY(
						element.getY() + this.stretchHeight - getContainerHeight()
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
	protected void removeBlankElements()
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
	protected void fillElements(JRPrintElementContainer printContainer) throws JRException
	{
		//int maxStretch = 0;
		//int stretch = 0;
		JRElement[] allElements = this.getElements();
		if (allElements != null && allElements.length > 0)
		{
			for(int i = 0; i < allElements.length; i++)
			{
				JRFillElement element = (JRFillElement)allElements[i];
				
				element.setRelativeY(element.getRelativeY() - this.firstY);

				if (element.getRelativeY() + element.getStretchHeight() > this.stretchHeight)
				{
					element.setToPrint(false);
				}
				
				element.setAlreadyPrinted(element.isToPrint() || element.isAlreadyPrinted());
				
				if (element.isToPrint())
				{
					JRPrintElement printElement = element.fill();
					//printElement.setY(printElement.getY() - this.firstY);

					if (printElement != null)
					{
						//FIXME not all elements affect height
						//stretch = printElement.getY() + this.firstY + printElement.getHeight() - element.getY() - element.getHeight();
						//if (stretch > maxStretch)
						//{
						//	maxStretch = stretch;
						//}
						printContainer.addElement(printElement);
						
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
							
							JRStyle[] styles = subreport.getStyles();
							if (styles != null)
							{
								for(int j = 0; j < styles.length; j++)
								{
									try
									{
										filler.getJasperPrint().addStyle(styles[j]);
									}
									catch(JRException e)
									{
										//ignore style duplication exception
									}
								}
							}
							
							
							Collection printElements = subreport.getPrintElements();
							addSubElements(printContainer, element, printElements);
						}
						else if (element instanceof JRFillCrosstab)
						{
							List printElements = ((JRFillCrosstab) element).getPrintElements();
							addSubElements(printContainer, element, printElements);
						}
					}
				}
			}
		}
		
		//printBand.setHeight(this.getHeight() + maxStretch - this.firstY);
		printContainer.setHeight(this.stretchHeight - this.firstY);
	}


	protected void addSubElements(JRPrintElementContainer printContainer, JRFillElement element, Collection printElements)
	{
		JRPrintElement printElement;
		if (printElements != null && printElements.size() > 0)
		{
			for(Iterator it = printElements.iterator(); it.hasNext();)
			{
				printElement = (JRPrintElement)it.next();
				printElement.setX(element.getX() + printElement.getX());
				printElement.setY(element.getRelativeY() + printElement.getY());
				printContainer.addElement(printElement);
			}
		}
	}

	
	/**
	 *
	 */
	protected void rewind() throws JRException
	{
		if (ySortedElements != null && ySortedElements.length > 0)
		{
			for(int i = 0; i < ySortedElements.length; i++)
			{
				JRFillElement element = ySortedElements[i];

				element.rewind();

				element.setAlreadyPrinted(false);
			}
		}
		
		this.willOverflow = false;
	}
	
	protected int getFirstY()
	{
		return firstY;
	}

	
	/**
	 * Returns the height of the element container.
	 * 
	 * @return the height of the element container
	 */
	protected abstract int getContainerHeight();
}
