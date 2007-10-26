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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementContainer;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.util.JRStyleResolver;

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
	protected JRFillElement firstYElement = null;
	
	protected final JRFillExpressionEvaluator expressionEvaluator;
	
	protected JRFillElement[] deepElements;

	/**
	 *
	 */
	protected Set stylesToEvaluate = new HashSet();
	protected Map evaluatedStyles = new HashMap();
	
	protected boolean hasPrintWhenOverflowElement;
	
	protected JRFillElementContainer(JRBaseFiller filler, JRElementGroup container, JRFillObjectFactory factory)
	{
		super(container, factory);
		
		expressionEvaluator = factory.getExpressionEvaluator();
		initDeepElements();
		
		this.filler = filler;
	}
	
	protected JRFillElementContainer(JRFillElementContainer container, JRFillCloneFactory factory)
	{
		super(container, factory);
		
		expressionEvaluator = container.expressionEvaluator;
		initDeepElements();
		
		this.filler = container.filler;
	}


	private void initDeepElements()
	{
		if (elements == null)
		{
			deepElements = new JRFillElement[0];
		}
		else
		{
			List deepElementsList = new ArrayList(elements.length);
			collectDeepElements(elements, deepElementsList);
			deepElements = new JRFillElement[deepElementsList.size()];
			deepElementsList.toArray(deepElements);
		}
	}

	private static void collectDeepElements(JRElement[] elements, List deepElementsList)
	{
		for (int i = 0; i < elements.length; i++)
		{
			JRElement element = elements[i];
			deepElementsList.add(element);
			
			if (element instanceof JRFillFrame)
			{
				JRFrame frame = (JRFrame) element;
				collectDeepElements(frame.getElements(), deepElementsList);
			}
		}
	}

	protected final void initElements()
	{
		hasPrintWhenOverflowElement = false;
		
		if (elements != null && elements.length > 0)
		{
			List sortedElemsList = new ArrayList();
			List stretchElemsList = new ArrayList();
			List bandBottomElemsList = new ArrayList();
			List removableElemsList = new ArrayList();
			for(int i = 0; i < elements.length; i++)
			{
				JRFillElement element = elements[i];
				sortedElemsList.add(element);
				
				if (element.getPositionType() == JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM)
				{
					bandBottomElemsList.add(element);
				}

				if (element.getStretchType() != JRElement.STRETCH_TYPE_NO_STRETCH)
				{
					stretchElemsList.add(element);
				}
				
				if (element.isRemoveLineWhenBlank())
				{
					removableElemsList.add(element);
				}
				
				if (element.isPrintWhenDetailOverflows())
				{
					hasPrintWhenOverflowElement = true;
				}
			}

			/*   */
			Collections.sort(sortedElemsList, new JRYComparator());
			ySortedElements = new JRFillElement[elements.length];
			sortedElemsList.toArray(ySortedElements);

			/*   */
			stretchElements = new JRFillElement[stretchElemsList.size()];
			stretchElemsList.toArray(stretchElements);

			/*   */
			bandBottomElements = new JRFillElement[bandBottomElemsList.size()];
			bandBottomElemsList.toArray(bandBottomElements);

			/*   */
			removableElements = new JRFillElement[removableElemsList.size()];
			removableElemsList.toArray(removableElements);
		}
		
		/*   */
		setDependentElements();
		
		setElementsBandBottomY();
	}

	protected final void setElementsBandBottomY()
	{
		if (elements != null && elements.length > 0)
		{
			for(int i = 0; i < elements.length; i++)
			{
				elements[i].setBandBottomY(
					getContainerHeight() - elements[i].getY() - elements[i].getHeight()
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
			for(int i = 0; i < ySortedElements.length - 1; i++)
			{
				JRFillElement iElem = ySortedElements[i];
				boolean isBreakElem = iElem instanceof JRFillBreak;

				for(int j = i + 1; j < ySortedElements.length; j++)
				{
					JRFillElement jElem = ySortedElements[j];
					
					int left = Math.min(iElem.getX(), jElem.getX());
					int right = Math.max(iElem.getX() + iElem.getWidth(), jElem.getX() + jElem.getWidth());
					
					if (
						((isBreakElem && jElem.getPositionType() == JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP) || jElem.getPositionType() == JRElement.POSITION_TYPE_FLOAT) &&
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
		//evaluatePrintWhenExpression(evaluation);

		//if (
		//	(isPrintWhenExpressionNull() ||
		//	(!isPrintWhenExpressionNull() && 
		//	isPrintWhenTrue()))
		//	)
		//{
			JRElement[] allElements = getElements();
			if (allElements != null && allElements.length > 0)
			{
				for(int i = 0; i < allElements.length; i++)
				{
					JRFillElement element = (JRFillElement)allElements[i];
					element.setCurrentEvaluation(evaluation);
					element.evaluate(evaluation);
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
				
				if (!isOverflow)
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
		return willOverflow;
	}


	protected void initFill()
	{
		isOverflow = willOverflow;
		firstY = 0;
		firstYElement = null;
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
		firstYElement = null;
		boolean isFirstYFound = false;

		if (ySortedElements != null && ySortedElements.length > 0)
		{
			for(int i = 0; i < ySortedElements.length; i++)
			{
				JRFillElement element = ySortedElements[i];

				tmpWillOverflow = 
					element.prepare(
						availableStretchHeight + getElementFirstY(element), 
						isOverflow
						) 
					|| tmpWillOverflow;

				element.moveDependantElements();

				if (element.isToPrint())
				{
					if (isOverflow)
					{
						if (element.isReprinted())
						{
							firstY = 0;
						}
						else if (!isFirstYFound)
						{
							firstY = element.getY();
						}
						isFirstYFound = true;
					}

					firstYElement = element;

					bandStretch = element.getRelativeY() + element.getStretchHeight() - getContainerHeight() + element.getBandBottomY();
					if (bandStretch > maxBandStretch)
					{
						maxBandStretch = bandStretch;
					}
				}
			}
		}

		if (maxBandStretch > availableStretchHeight + firstY)
		{
			tmpWillOverflow = true;
		}
		
		if (tmpWillOverflow)
		{
			stretchHeight = getContainerHeight() + availableStretchHeight;
		}
		else
		{
			stretchHeight = getContainerHeight() + maxBandStretch;
		}

		willOverflow = tmpWillOverflow && isOverflowAllowed;
	}

	private int getElementFirstY(JRFillElement element)
	{
		int elemFirstY;
		if (!isOverflow || hasPrintWhenOverflowElement)
		{
			elemFirstY = 0;
		}
		else if (element.getY() >= firstY)
		{
			elemFirstY = firstY;
		}
		else
		{
			elemFirstY = element.getY();
		}
		return elemFirstY;
	}

	protected void setStretchHeight(int stretchHeight)
	{
		if (stretchHeight > this.stretchHeight)
		{
			this.stretchHeight = stretchHeight;
		}
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
				
				element.stretchElement(stretchHeight - getContainerHeight());
				
				element.moveDependantElements();
			}
		}
		
		if (ySortedElements != null && ySortedElements.length > 0)
		{
			for(int i = 0; i < ySortedElements.length; i++)
			{
				JRFillElement element = ySortedElements[i];

				element.stretchHeightFinal();
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
		//if (!willOverflow)
		//{
			if (bandBottomElements != null && bandBottomElements.length > 0)
			{
				for(int i = 0; i < bandBottomElements.length; i++)
				{
					JRFillElement element = bandBottomElements[i];

					element.setRelativeY(
						element.getY() + stretchHeight - getContainerHeight()
						);

					// band bottom elements do not print if there will be an overflow
					element.setToPrint(element.isToPrint() && !willOverflow);
				}
			}
		//}
	}


	/**
	 *
	 */
	protected void removeBlankElements()
	{
		JRElement[] remElems = removableElements;
		if (remElems != null && remElems.length > 0)
		{
			JRElement[] elems = ySortedElements;
			
			for(int i = 0; i < remElems.length; i++)
			{
				JRFillElement iElem = (JRFillElement)remElems[i];

				int blankHeight;
				if (iElem.isToPrint())
				{
					blankHeight = iElem.getHeight() - iElem.getStretchHeight();
				}
				else
				{
					blankHeight = iElem.getHeight();//FIXME subreports that strech and then don't print, will not remove all space
				}
				
				if (
					blankHeight > 0 && 
					iElem.getRelativeY() + iElem.getStretchHeight() <= stretchHeight &&
					iElem.getRelativeY() >= firstY
					)
				{
					int blankY = iElem.getRelativeY() + iElem.getHeight() - blankHeight;
					boolean isToRemove = true;
					
					for(int j = 0; j < elems.length; j++)
					{
						JRFillElement jElem = (JRFillElement)elems[j];
						
						if (iElem != jElem && jElem.isToPrint())
						{
							int top = 
								Math.min(blankY, jElem.getRelativeY());
							int bottom = 
								Math.max(
									blankY + blankHeight, 
									jElem.getRelativeY() + jElem.getStretchHeight()
									);
							
							if (blankHeight + jElem.getStretchHeight() > bottom - top)
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
							JRFillElement jElem = (JRFillElement)elems[j];
							
							if (jElem.getRelativeY() >= blankY + blankHeight)
							{
								jElem.setRelativeY(jElem.getRelativeY() - blankHeight);
							}
						}
						
						stretchHeight = stretchHeight - blankHeight;
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
		JRElement[] allElements = getElements();
		if (allElements != null && allElements.length > 0)
		{
			for(int i = 0; i < allElements.length; i++)
			{
				JRFillElement element = (JRFillElement)allElements[i];
				
				element.setRelativeY(element.getRelativeY() - firstY);

				if (element.getRelativeY() + element.getStretchHeight() > stretchHeight)
				{
					element.setToPrint(false);
				}
				
				element.setAlreadyPrinted(element.isToPrint() || element.isAlreadyPrinted());
				
				if (element.isToPrint())
				{
					JRPrintElement printElement = element.fill();
					//printElement.setY(printElement.getY() - firstY);

					if (printElement != null)
					{
						//FIXME not all elements affect height
						//stretch = printElement.getY() + firstY + printElement.getHeight() - element.getY() - element.getHeight();
						//if (stretch > maxStretch)
						//{
						//	maxStretch = stretch;
						//}
						printContainer.addElement(printElement);
						
						if (element instanceof JRFillSubreport)
						{
							JRFillSubreport subreport = (JRFillSubreport)element;
							
							List fonts = subreport.subreportFiller.getJasperPrint().getFontsList();
							for(int j = 0; j < fonts.size(); j++)
							{
								filler.getJasperPrint().addFont((JRReportFont)fonts.get(j), true);
							}
							
							List styles = subreport.subreportFiller.getJasperPrint().getStylesList();
							for(int j = 0; j < styles.size(); j++)
							{
								filler.addPrintStyle((JRStyle)styles.get(j));
							}
							
							List origins = subreport.subreportFiller.getJasperPrint().getOriginsList();
							for(int j = 0; j < origins.size(); j++)
							{
								filler.getJasperPrint().addOrigin((JROrigin)origins.get(j));
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
		
		//printBand.setHeight(getHeight() + maxStretch - firstY);
		printContainer.setHeight(stretchHeight - firstY);
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
		
		willOverflow = false;
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


	/**
	 * Find all styles containing conditional styles which are referenced by elements in this band.
	 */
	protected void initConditionalStyles()
	{
		filler.addDefaultStyleListener(new JRBaseFiller.DefaultStyleListener(){
			public void defaultStyleSet(JRStyle style)
			{
				collectConditionalStyle(style);
			}
		});
		
		for (int i = 0; i < deepElements.length; i++)
		{
			JRStyle style = deepElements[i].initStyle;
			collectConditionalStyle(style);
		}
		
		if (deepElements.length > 0)
		{
			for(int i = 0; i < deepElements.length; i++)
			{
				deepElements[i].setConditionalStylesContainer(this);
			}
		}
	}

	protected void collectConditionalStyle(JRStyle style)
	{
		if (style != null)// && style.getConditionalStyles() != null)
		{
			stylesToEvaluate.add(style);
		}
	}


	protected void evaluateConditionalStyles(byte evaluation) throws JRException
	{
		for (Iterator it = stylesToEvaluate.iterator(); it.hasNext();) 
		{
			evaluateConditionalStyle((JRStyle) it.next(), evaluation);
		}
	}


	protected JRStyle evaluateConditionalStyle(JRStyle initialStyle, byte evaluation) throws JRException
	{
		JRStyle consolidatedStyle = initialStyle;

		StringBuffer code = new StringBuffer();
		List condStylesToApply = new ArrayList();
		
		boolean anyTrue = buildConsolidatedStyle(initialStyle, evaluation, code, condStylesToApply);
		
		if (anyTrue)
		{
			String consolidatedStyleName = initialStyle.getName() + code.toString();
			consolidatedStyle = (JRStyle)filler.getJasperPrint().getStylesMap().get(consolidatedStyleName);
			if (consolidatedStyle == null)
			{
				consolidatedStyle = new JRBaseStyle(consolidatedStyleName);
				for (int j = condStylesToApply.size() - 1; j >= 0; j--)
				{
					JRStyleResolver.appendStyle(consolidatedStyle, (JRStyle)condStylesToApply.get(j));
				}

				filler.addPrintStyle(consolidatedStyle);
			}
		}

		evaluatedStyles.put(initialStyle, consolidatedStyle);
		
		return consolidatedStyle;
	}


	protected boolean buildConsolidatedStyle(JRStyle style, byte evaluation, StringBuffer code, List condStylesToApply) throws JRException
	{
		boolean anyTrue = false;
		
		JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();
		if (conditionalStyles != null && conditionalStyles.length > 0)
		{
			for (int j = 0; j < conditionalStyles.length; j++) 
			{
				JRConditionalStyle conditionalStyle = conditionalStyles[j];
				Boolean expressionValue = 
					(Boolean) expressionEvaluator.evaluate(
						conditionalStyle.getConditionExpression(),
						evaluation
						);
				
				boolean condition;
				if (expressionValue == null)
				{
					condition = false;
				}
				else
				{
					condition = expressionValue.booleanValue();
				}
				
				code.append(condition ? '1' : '0');
				anyTrue = anyTrue | condition;

				if (condition)
					condStylesToApply.add(conditionalStyle);
			}
		}

		condStylesToApply.add(style);
		
		if (style.getStyle() != null)
			anyTrue = anyTrue | buildConsolidatedStyle(style.getStyle(), evaluation, code, condStylesToApply);
		
		return anyTrue;
	}


	public JRStyle getEvaluatedConditionalStyle(JRStyle parentStyle)
	{
		return (JRStyle) evaluatedStyles.get(parentStyle);
	}
}
