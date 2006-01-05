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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillElement implements JRElement, JRCloneable
{


	/**
	 *
	 */
	protected JRElement parent = null;
	protected Map templates = new HashMap();

	/**
	 *
	 */
	protected JRBaseFiller filler = null;
	protected JRFillExpressionEvaluator expressionEvaluator = null;

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

	private int x;
	private int y;
	private int width;
	private int height;
	
	// default for all static elements
	private boolean isValueRepeating = true;
	
	protected byte currentEvaluation;
	
	// used by elements that support evaluationTime=Auto
	protected Map delayedEvaluationsMap;

	protected JRFillElementContainer conditionalStylesContainer;
	
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
			this.expressionEvaluator = factory.getExpressionEvaluator();

			/*   */
			printWhenGroupChanges = factory.getGroup(element.getPrintWhenGroupChanges());
			elementGroup = (JRFillElementGroup)factory.getElementGroup(element.getElementGroup());
			
			x = element.getX();
			y = element.getY();
			width = element.getWidth();
			height = element.getHeight();
		}

	
	protected JRFillElement(JRFillElement element, JRFillCloneFactory factory)
	{
		factory.put(element, this);

		this.parent = element.parent;
		this.filler = element.filler;
		this.expressionEvaluator = element.expressionEvaluator;

		/*   */
		printWhenGroupChanges = element.printWhenGroupChanges;
		elementGroup = (JRFillElementGroup) factory.getClone((JRFillElementGroup) element.getElementGroup());

		x = element.getX();
		y = element.getY();
		width = element.getWidth();
		height = element.getHeight();
		
		templates = element.templates;
	}


	/**
	 * 
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return parent.getDefaultStyleProvider();
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
		return this.parent.getPositionType();//FIXME optimize this by consolidating style properties
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
	public Byte getOwnMode()
	{
		return this.parent.getOwnMode();
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
	public void setMode(Byte mode)
	{
	}

	/**
	 *
	 */
	public int getX()
	{
		return x;
	}

	/**
	 *
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 *
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 *
	 */
	public int getY()
	{
		return y;
	}

	/**
	 *
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 *
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	/**
	 *
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 *
	 */
	public int getHeight()
	{
		return height;
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

	public Color getOwnForecolor()
	{
		return this.parent.getOwnForecolor();
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
	public Color getOwnBackcolor()
	{
		return this.parent.getOwnBackcolor();
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
		relativeY = y;
		stretchHeight = height;

		if (elementGroup != null)
		{
			elementGroup.reset();
		}
	}

	protected void setCurrentEvaluation(byte evaluation)
	{
		currentEvaluation = evaluation;
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
		boolean isExprNull = true;
		boolean isExprTrue = false;

		JRExpression expression = this.getPrintWhenExpression();
		if (expression != null)
		{
			isExprNull = false;
			Boolean printWhenExpressionValue = (Boolean) evaluateExpression(expression, evaluation);
			if (printWhenExpressionValue == null)
			{
				isExprTrue = false;
			}
			else
			{
				isExprTrue = printWhenExpressionValue.booleanValue();
			}
		}

		setPrintWhenExpressionNull(isExprNull);
		setPrintWhenTrue(isExprTrue);
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

	public JRStyle getStyle()
	{
		return parent.getStyle();
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

	/**
	 * Resolves an element.
	 * 
	 * @param element the element
	 * @param evaluation the evaluation type
	 */
	protected abstract void resolveElement (JRPrintElement element, byte evaluation) throws JRException;


	/**
	 * Evaluates an expression.
	 * 
	 * @param expression the expression
	 * @param evaluation the evaluation type
	 * @return the evaluation result
	 * @throws JRException
	 */
	protected final Object evaluateExpression(JRExpression expression, byte evaluation) throws JRException
	{
		return expressionEvaluator.evaluate(expression, evaluation);
	}


	/**
	 * Decides whether the value for this element is repeating.
	 * <p>
	 * Dynamic elements should call {@link #setValueRepeating(boolean) setValueRepeating(boolean)} on
	 * {@link #evaluate(byte) evaluate(byte)}.  Static elements don't have to do anything, this method
	 * will return <code>true</code> by default.
	 * 
	 * @return whether the value for this element is repeating
	 * @see #setValueRepeating(boolean)
	 */
	protected boolean isValueRepeating()
	{
		return isValueRepeating;
	}


	/**
	 * Sets the repeating flag for this element.
	 * <p>
	 * This method should be called by dynamic elements on {@link #evaluate(byte) evaluate(byte)}.
	 * 
	 * @param isValueRepeating whether the value of the element is repeating
	 * @see #isValueRepeating()
	 */
	protected void setValueRepeating(boolean isValueRepeating)
	{
		this.isValueRepeating = isValueRepeating;
	}


	protected JRFillVariable getVariable(String variableName)
	{
		return filler.getVariable(variableName);
	}


	protected JRFillField getField(String fieldName)
	{
		return filler.getField(fieldName);
	}
	
	// default for elements not supporting evaluationTime
	protected byte getEvaluationTime()
	{
		return JRExpression.EVALUATION_TIME_NOW;
	}

	/**
	 * Resolves an element.
	 * 
	 * @param element the element
	 * @param evaluation the evaluation type
	 * @param evaluationTime the current evaluation time
	 */
	protected void resolveElement (JRPrintElement element, byte evaluation, JREvaluationTime evaluationTime) throws JRException
	{
		byte evaluationTimeType = getEvaluationTime();
		switch (evaluationTimeType)
		{
			case JRExpression.EVALUATION_TIME_NOW:
				break;
			case JRExpression.EVALUATION_TIME_AUTO:
				delayedEvaluate((JRRecordedValuesPrintElement) element, evaluationTime, evaluation);
				break;
			default:
				resolveElement(element, evaluation);
				break;
		}
	}

	private static class DelayedEvaluations implements Serializable
	{
		final Set fields;
		final Set variables;

		DelayedEvaluations()
		{
			fields = new HashSet();
			variables = new HashSet();
		}
	}

	protected boolean delayedEvaluationsInitialized()
	{
		return delayedEvaluationsMap != null;
	}

	protected void initDelayedEvaluations()
	{
		delayedEvaluationsMap = new HashMap();
	}

	protected void collectDelayedEvaluations(JRExpression expression)
	{
		if (expression != null)
		{
			JRExpressionChunk[] chunks = expression.getChunks();
			if (chunks != null)
			{
				for (int i = 0; i < chunks.length; i++)
				{
					JRExpressionChunk chunk = chunks[i];
					switch (chunk.getType())
					{
						case JRExpressionChunk.TYPE_FIELD:
						{
							DelayedEvaluations delayedEvaluations = getDelayedEvaluations(JREvaluationTime.EVALUATION_TIME_NOW);
							delayedEvaluations.fields.add(chunk.getText());
							break;
						}
						case JRExpressionChunk.TYPE_VARIABLE:
						{
							JREvaluationTime time = autogetVariableEvaluationTime(chunk.getText());
							DelayedEvaluations delayedEvaluations = getDelayedEvaluations(time);
							delayedEvaluations.variables.add(chunk.getText());
							break;
						}
					}
				}
			}
		}
	}


	private DelayedEvaluations getDelayedEvaluations(JREvaluationTime time)
	{
		DelayedEvaluations delayedEvaluations = (DelayedEvaluations) delayedEvaluationsMap.get(time);
		if (delayedEvaluations == null)
		{
			delayedEvaluations = new DelayedEvaluations();
			delayedEvaluationsMap.put(time, delayedEvaluations);
		}
		return delayedEvaluations;
	}


	private JREvaluationTime autogetVariableEvaluationTime(String variableName)
	{
		JRFillVariable variable = getVariable(variableName);
		JREvaluationTime evaluationTime;
		switch (variable.getResetType())
		{
			case JRVariable.RESET_TYPE_REPORT:
				evaluationTime = JREvaluationTime.EVALUATION_TIME_REPORT;
				break;
			case JRVariable.RESET_TYPE_PAGE:
				evaluationTime = JREvaluationTime.EVALUATION_TIME_PAGE;
				break;
			case JRVariable.RESET_TYPE_COLUMN:
				evaluationTime = JREvaluationTime.EVALUATION_TIME_COLUMN;
				break;
			case JRVariable.RESET_TYPE_GROUP:
				evaluationTime = JREvaluationTime.getGroupEvaluationTime(variable.getResetGroup().getName());
				break;
			default:
				evaluationTime = JREvaluationTime.EVALUATION_TIME_NOW;
				break;
		}
		
		if (!evaluationTime.equals(JREvaluationTime.EVALUATION_TIME_NOW) &&
				band.isNowEvaluationTime(evaluationTime))
		{
			evaluationTime = JREvaluationTime.EVALUATION_TIME_NOW;
		}
		
		if (variable.getCalculation() == JRVariable.CALCULATION_SYSTEM &&
				evaluationTime.equals(JREvaluationTime.EVALUATION_TIME_NOW) &&
				band.isVariableUsedInSubreportReturns(variableName))
		{
			evaluationTime = JREvaluationTime.getBandEvaluationTime(band);
		}

		return evaluationTime;
	}
	
	
	protected void initDelayedEvaluationPrint(JRRecordedValuesPrintElement printElement) throws JRException
	{
		for (Iterator it = delayedEvaluationsMap.keySet().iterator(); it.hasNext();)
		{
			JREvaluationTime evaluationTime = (JREvaluationTime) it.next();
			if (!evaluationTime.equals(JREvaluationTime.EVALUATION_TIME_NOW))
			{
				filler.addBoundElement(this, printElement, evaluationTime);
			}
		}
		
		printElement.initRecordedValues(delayedEvaluationsMap.keySet());
		
		if (delayedEvaluationsMap.containsKey(JREvaluationTime.EVALUATION_TIME_NOW))
		{
			delayedEvaluate(printElement, JREvaluationTime.EVALUATION_TIME_NOW, currentEvaluation);
		}
	}


	protected void delayedEvaluate(JRRecordedValuesPrintElement printElement, JREvaluationTime evaluationTime, byte evaluation) throws JRException
	{
		JRRecordedValues recordedValues = printElement.getRecordedValues();
		if (!recordedValues.lastEvaluationTime())
		{
			DelayedEvaluations delayedEvaluations = (DelayedEvaluations) delayedEvaluationsMap.get(evaluationTime);
			
			for (Iterator it = delayedEvaluations.fields.iterator(); it.hasNext();)
			{
				String fieldName = (String) it.next();
				JRFillField field = getField(fieldName);
				recordedValues.recordFieldValue(fieldName, field.getValue(evaluation));
			}

			for (Iterator it = delayedEvaluations.variables.iterator(); it.hasNext();)
			{
				String variableName = (String) it.next();
				JRFillVariable variable = getVariable(variableName);
				recordedValues.recordVariableValue(variableName, variable.getValue(evaluation));
			}
		}

		recordedValues.doneEvaluation(evaluationTime);
		
		if (recordedValues.finishedEvaluations())
		{
			overwriteWithRecordedValues(recordedValues, evaluation);
			resolveElement(printElement, evaluation);
			restoreValues(recordedValues, evaluation);
			printElement.deleteRecordedValues();
		}
	}

	
	private void overwriteWithRecordedValues(JRRecordedValues recordedValues, byte evaluation)
	{
		Map fieldValues = recordedValues.getRecordedFieldValues();
		if (fieldValues != null)
		{
			for (Iterator it = fieldValues.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();
				String fieldName = (String) entry.getKey();
				Object fieldValue = entry.getValue();
				JRFillField field = getField(fieldName);
				field.overwriteValue(fieldValue, evaluation);
			}
		}
		
		Map variableValues = recordedValues.getRecordedVariableValues();
		if (variableValues != null)
		{
			for (Iterator it = variableValues.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();
				String variableName = (String) entry.getKey();
				Object variableValue = entry.getValue();
				JRFillVariable variable = getVariable(variableName);
				variable.overwriteValue(variableValue, evaluation);
			}
		}
	}

	private void restoreValues(JRRecordedValues recordedValues, byte evaluation)
	{
		Map fieldValues = recordedValues.getRecordedFieldValues();
		if (fieldValues != null)
		{
			for (Iterator it = fieldValues.keySet().iterator(); it.hasNext();)
			{
				String fieldName = (String) it.next();
				JRFillField field = getField(fieldName);
				field.restoreValue(evaluation);
			}
		}
		
		Map variableValues = recordedValues.getRecordedVariableValues();
		if (variableValues != null)
		{
			for (Iterator it = variableValues.keySet().iterator(); it.hasNext();)
			{
				String variableName = (String) it.next();
				JRFillVariable variable = getVariable(variableName);
				variable.restoreValue(evaluation);
			}
		}
	}


	protected void setConditionalStylesContainer(JRFillElementContainer conditionalStylesContainer)
	{
		this.conditionalStylesContainer = conditionalStylesContainer;
	}

	protected JRStyle getElementStyle()
	{
		JRStyle parentStyle = parent.getStyle();
		JRStyle style = conditionalStylesContainer.getEvaluatedConditionalStyle(parentStyle);
		if (style == null)
		{
			style = parentStyle;
		}
		return style;
	}
	
	protected JRTemplateElement getTemplate(JRStyle style)
	{
		return (JRTemplateElement) templates.get(style);
	}

	protected void registerTemplate(JRStyle style, JRTemplateElement template)
	{
		templates.put(style, template);
	}
}
