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

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillElement implements JRElement, JRCloneable, JRStyleSetter
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

	protected JRDefaultStyleProvider defaultStyleProvider;
	
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
	
	private boolean isValueRepeating = false;
	
	protected byte currentEvaluation;
	
	// used by elements that support evaluationTime=Auto
	protected Map delayedEvaluationsMap;

	protected JRFillElementContainer conditionalStylesContainer;
	
	protected JRStyle initStyle;
	
	/**
	 * Flag indicating whether the element is shrinkable.
	 * @see #setShrinkable(boolean)
	 */
	private boolean shrinkable;
	
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
			this.defaultStyleProvider = factory.getDefaultStyleProvider();

			/*   */
			printWhenGroupChanges = factory.getGroup(element.getPrintWhenGroupChanges());
			elementGroup = (JRFillElementGroup)factory.getElementGroup(element.getElementGroup());
			
			x = element.getX();
			y = element.getY();
			width = element.getWidth();
			height = element.getHeight();
			
			factory.setStyle(this, parent);
		}

	
	protected JRFillElement(JRFillElement element, JRFillCloneFactory factory)
	{
		factory.put(element, this);

		this.parent = element.parent;
		this.filler = element.filler;
		this.expressionEvaluator = element.expressionEvaluator;
		this.defaultStyleProvider = element.defaultStyleProvider;

		/*   */
		printWhenGroupChanges = element.printWhenGroupChanges;
		elementGroup = (JRFillElementGroup) factory.getClone((JRFillElementGroup) element.getElementGroup());

		x = element.getX();
		y = element.getY();
		width = element.getWidth();
		height = element.getHeight();
		
		templates = element.templates;
		
		initStyle = element.initStyle;
		
		shrinkable = element.shrinkable;
	}


	/**
	 * 
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}


	/**
	 *
	 */
	public String getKey()
	{
		return parent.getKey();
	}

	/**
	 *
	 */
	public byte getPositionType()
	{
		return parent.getPositionType();//FIXME optimize this by consolidating style properties
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
		return parent.getStretchType();
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
		return parent.isPrintRepeatedValues();
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
		return JRStyleResolver.getMode(this, MODE_OPAQUE);
	}

	/**
	 *
	 */
	public Byte getOwnMode()
	{
		return parent.getOwnMode();
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
		return parent.isRemoveLineWhenBlank();
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
		return parent.isPrintInFirstWholeBand();
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
		return parent.isPrintWhenDetailOverflows();
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
		return JRStyleResolver.getForecolor(this);
	}

	public Color getOwnForecolor()
	{
		return parent.getOwnForecolor();
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
		return JRStyleResolver.getBackcolor(this);
	}

	/**
	 *
	 */
	public Color getOwnBackcolor()
	{
		return parent.getOwnBackcolor();
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
		return parent.getPrintWhenExpression();
	}

	/**
	 *
	 */
	public JRGroup getPrintWhenGroupChanges()
	{
		return printWhenGroupChanges;
	}

	/**
	 *
	 */
	public JRElementGroup getElementGroup()
	{
		return elementGroup;
	}

	/**
	 *
	 */
	protected boolean isPrintWhenExpressionNull()
	{
		return isPrintWhenExpressionNull;
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
		return isPrintWhenTrue;
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
		return isToPrint;
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
		return isReprinted;
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
		return isAlreadyPrinted;
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
	 */
	protected Collection getDependantElements()
	{
		return dependantElements;
	}

	/**
	 *
	 */
	protected void addDependantElement(JRElement element)
	{
		dependantElements.add(element);
	}

	/**
	 *
	 */
	protected int getRelativeY()
	{
		return relativeY;
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
		return stretchHeight;
	}

	/**
	 *
	 */
	protected void setStretchHeight(int stretchHeight)
	{
		if (stretchHeight > getHeight() || (shrinkable && isRemoveLineWhenBlank()))
		{
			this.stretchHeight = stretchHeight;
		}
		else
		{
			this.stretchHeight = getHeight();
		}
	}

	/**
	 *
	 */
	protected int getBandBottomY()
	{
		return bandBottomY;
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
		return band;
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

		JRExpression expression = getPrintWhenExpression();
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
			isPrintWhenExpressionNull() ||
			( !isPrintWhenExpressionNull() &&
			  isPrintWhenTrue() )
			)
		{
			setToPrint(true);
		}
		else
		{
			setToPrint(false);
		}

		setReprinted(false);

		return false;
	}



	/**
	 *
	 */
	protected void stretchElement(int bandStretch)
	{
		switch (getStretchType())
		{
			case JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT :
			{
				setStretchHeight(getHeight() + bandStretch);
				break;
			}
			case JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT :
			{
				if (elementGroup != null)
				{
					//setStretchHeight(getHeight() + getStretchHeightDiff());
					setStretchHeight(getHeight() + elementGroup.getStretchHeightDiff());
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
		Collection elements = getDependantElements();
		if (elements != null && elements.size() > 0)
		{
			JRFillElement element = null;
			int diffY = 0;
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRFillElement)it.next();

				diffY = element.getY() - getY() - getHeight() -
						(element.getRelativeY() - getRelativeY() - getStretchHeight());

				if (diffY < 0)
				{
					diffY = 0;
				}

				element.setRelativeY(element.getRelativeY() + diffY);
			}
		}
	}


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
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		final Set fields;
		final Set variables;

		DelayedEvaluations()
		{
			fields = new HashSet();
			variables = new HashSet();
		}
	}

	protected void initDelayedEvaluations()
	{
		if (getEvaluationTime() == JRExpression.EVALUATION_TIME_AUTO && delayedEvaluationsMap == null)
		{
			delayedEvaluationsMap = new HashMap();
			collectDelayedEvaluations();
		}
	}
	
	protected void collectDelayedEvaluations()
	{
		//to be overridden by elements that support "Auto" evaluation
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


	/**
	 * 
	 */
	protected void setConditionalStylesContainer(JRFillElementContainer conditionalStylesContainer)
	{
		this.conditionalStylesContainer = conditionalStylesContainer;
	}

	/**
	 * 
	 */
	public JRStyle getStyle()
	{
		JRStyle crtStyle = initStyle;
		
		boolean isUsingDefaultStyle = false;

		if (crtStyle == null)
		{
			crtStyle = filler.getDefaultStyle();
			isUsingDefaultStyle = true;
		}

		JRStyle evalStyle = crtStyle;

		if (conditionalStylesContainer != null)
			evalStyle = conditionalStylesContainer.getEvaluatedConditionalStyle(crtStyle);
		
		if (isUsingDefaultStyle && evalStyle == crtStyle)
			evalStyle = null;
		
		return evalStyle;
	}
	
	/**
	 * 
	 */
	protected JRTemplateElement getTemplate(JRStyle style)
	{
		return (JRTemplateElement) templates.get(style);
	}

	/**
	 * 
	 */
	protected void registerTemplate(JRStyle style, JRTemplateElement template)
	{
		templates.put(style, template);
	}
	
	
	/**
	 * Indicates whether an element is shrinkable.
	 * <p>
	 * This flag is only effective when {@link #isRemoveLineWhenBlank() isRemoveLineWhenBlank} is also set.
	 * 
	 * @param shrinkable whether the element is shrinkable
	 */
	protected final void setShrinkable(boolean shrinkable)
	{
		this.shrinkable = shrinkable;
	}


	/**
	 * Called when the stretch height of an element is final so that
	 * the element can perform any adjustments.
	 */
	protected void stretchHeightFinal()
	{
		// nothing		
	}
	
	
	protected boolean isEvaluateNow()
	{
		boolean evaluateNow;
		switch (getEvaluationTime())
		{
			case JRExpression.EVALUATION_TIME_NOW:
				evaluateNow = true;
				break;

			case JRExpression.EVALUATION_TIME_AUTO:
				evaluateNow = isAutoEvaluateNow();
				break;

			default:
				evaluateNow = false;
				break;
		}
		return evaluateNow;
	}
	
	
	protected boolean isAutoEvaluateNow()
	{
		return delayedEvaluationsMap == null || delayedEvaluationsMap.isEmpty() 
				|| (delayedEvaluationsMap.size() == 1 
						&& delayedEvaluationsMap.containsKey(JREvaluationTime.EVALUATION_TIME_NOW));
	}
	
	
	protected boolean isEvaluateAuto()
	{
		return getEvaluationTime() == JRExpression.EVALUATION_TIME_AUTO && !isAutoEvaluateNow();
	}
	
	
	public void setStyleDelayed(JRStyle style)
	{
		initStyle = style;
		conditionalStylesContainer.collectConditionalStyle(style);
	}

	public String getStyleNameReference()
	{
		return null;
	}

	public void setStyle(JRStyle style)
	{
		initStyle = style;
	}

	public void setStyleNameReference(String name)
	{
		throw new UnsupportedOperationException("Style name references not allowed at fill time");
	}
	
}
