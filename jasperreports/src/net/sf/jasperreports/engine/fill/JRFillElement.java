/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillElement implements JRElement, JRFillCloneable, JRStyleSetter
{


	/**
	 *
	 */
	protected JRElement parent;
	protected Map<JRStyle,JRTemplateElement> templates = new HashMap<JRStyle,JRTemplateElement>();

	/**
	 *
	 */
	protected JRBaseFiller filler;
	protected JRFillExpressionEvaluator expressionEvaluator;

	protected JRDefaultStyleProvider defaultStyleProvider;
	
	/**
	 *
	 */
	protected JRGroup printWhenGroupChanges;
	protected JRFillElementGroup elementGroup;

	/**
	 *
	 */
	protected JRFillBand band;
	
	protected JROriginProvider originProvider;
	
	protected int elementId;

	/**
	 *
	 */
	private boolean isPrintWhenExpressionNull = true;
	private boolean isPrintWhenTrue = true;
	private boolean isToPrint = true;
	private boolean isReprinted;
	private boolean isAlreadyPrinted;
	private Collection<JRElement> dependantElements = new ArrayList<JRElement>();
	private int relativeY;
	private int stretchHeight;

	private int x;
	private int y;
	private int width;
	private int height;
	
	private boolean isValueRepeating;
	
	protected byte currentEvaluation;
	
	// used by elements that support evaluationTime=Auto
	protected Map<JREvaluationTime,DelayedEvaluations> delayedEvaluationsMap;

	protected JRFillElementContainer conditionalStylesContainer;
	
	protected JRStyle initStyle;
	
	protected JRStyle currentStyle;
	
	/**
	 * Flag indicating whether the element is shrinkable.
	 * @see #setShrinkable(boolean)
	 */
	private boolean shrinkable;

	protected JRPropertiesMap staticProperties;
	protected JRPropertiesMap dynamicProperties;
	protected JRPropertiesMap mergedProperties;
	
	/**
	 *
	 *
	private JRElement topElementInGroup;
	private JRElement bottomElementInGroup;


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
			
			elementId = filler.assignElementId(this);

			/*   */
			printWhenGroupChanges = factory.getGroup(element.getPrintWhenGroupChanges());
			elementGroup = (JRFillElementGroup)factory.getVisitResult(element.getElementGroup());
			
			x = element.getX();
			y = element.getY();
			width = element.getWidth();
			height = element.getHeight();
			
			staticProperties = element.hasProperties() ? element.getPropertiesMap().cloneProperties() : null;
			mergedProperties = staticProperties;
			
			factory.registerDelayedStyleSetter(this, parent);
		}

	
	protected JRFillElement(JRFillElement element, JRFillCloneFactory factory)
	{
		factory.put(element, this);

		this.parent = element.parent;
		this.filler = element.filler;
		this.expressionEvaluator = element.expressionEvaluator;
		this.defaultStyleProvider = element.defaultStyleProvider;
		this.originProvider = element.originProvider;
		
		elementId = element.elementId;

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
		
		staticProperties = element.staticProperties == null ? null : element.staticProperties.cloneProperties();
		mergedProperties = staticProperties;
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
	public PositionTypeEnum getPositionTypeValue()
	{
		return parent.getPositionTypeValue();//FIXME optimize this by consolidating style properties
	}

	/**
	 *
	 */
	public void setPositionType(PositionTypeEnum positionType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public StretchTypeEnum getStretchTypeValue()
	{
		return parent.getStretchTypeValue();
	}

	/**
	 *
	 */
	public void setStretchType(StretchTypeEnum stretchType)
	{
		throw new UnsupportedOperationException();
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
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.OPAQUE);
	}

	/**
	 *
	 */
	public ModeEnum getOwnModeValue()
	{
		return parent.getOwnModeValue();
	}

	/**
	 *
	 */
	public void setMode(ModeEnum modeValue)
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
	protected Collection<JRElement> getDependantElements()
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
		
		if (this.originProvider == null)
		{
			setOriginProvider(band);
		}
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

	protected JRTemplateElement getElementTemplate()
	{
		JRStyle style = getStyle();
		JRTemplateElement template = getTemplate(style);
		if (template == null)
		{
			template = createElementTemplate();
			transferProperties(template);
			
			// deduplicate to previously created identical objects
			template = filler.fillContext.deduplicate(template);
			
			registerTemplate(style, template);
		}
		return template;
	}

	protected abstract JRTemplateElement createElementTemplate();
	
	/**
	 *
	 */
	protected boolean prepare(
		int availableHeight,
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
		switch (getStretchTypeValue())
		{
			case RELATIVE_TO_BAND_HEIGHT :
			{
				setStretchHeight(getHeight() + bandStretch);
				break;
			}
			case RELATIVE_TO_TALLEST_OBJECT :
			{
				if (elementGroup != null)
				{
					//setStretchHeight(getHeight() + getStretchHeightDiff());
					setStretchHeight(getHeight() + elementGroup.getStretchHeightDiff());
				}

				break;
			}
			case NO_STRETCH :
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
		Collection<JRElement> elements = getDependantElements();
		if (elements != null && elements.size() > 0)
		{
			JRFillElement element = null;
			int diffY = 0;
			for(Iterator<JRElement> it = elements.iterator(); it.hasNext();)
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
	
	protected void performDelayedEvaluation(JRPrintElement element, byte evaluation) 
			throws JRException
	{
		if (isDelayedStyleEvaluation())
		{
			JRStyle elementStyle = initStyle;
			if (elementStyle == null)
			{
				elementStyle = filler.getDefaultStyle();
			}
			
			if (elementStyle != null)
			{
				JRStyle evaluatedStyle = conditionalStylesContainer.evaluateConditionalStyle(
						elementStyle, evaluation);
				// if the evaluated style differs from the existing style
				if (evaluatedStyle != element.getStyle())
				{
					// set the evaluated style as current style
					this.currentStyle = evaluatedStyle;
					
					// get/create an element template that corresponds to the
					// current style
					JRTemplateElement newTemplate = getElementTemplate();
					((JRTemplatePrintElement) element).updateElementTemplate(
							newTemplate);
				}
			}
		}
		
		resolveElement(element, evaluation);
		
		// reset the current style
		this.currentStyle = null;
	}


	/**
	 * Evaluates an expression.
	 * 
	 * @param expression the expression
	 * @param evaluation the evaluation type
	 * @return the evaluation result
	 * @throws JRException
	 */
	public final Object evaluateExpression(JRExpression expression, byte evaluation) throws JRException
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
	protected EvaluationTimeEnum getEvaluationTimeValue()
	{
		return EvaluationTimeEnum.NOW;
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
		EvaluationTimeEnum evaluationTimeType = getEvaluationTimeValue();
		switch (evaluationTimeType)
		{
			case NOW:
				break;
			case AUTO:
				delayedEvaluate((JRRecordedValuesPrintElement) element, evaluationTime, evaluation);
				break;
			default:
				performDelayedEvaluation(element, evaluation);
				break;
		}
	}

	private static class DelayedEvaluations implements Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		final Set<String> fields;
		final Set<String> variables;

		DelayedEvaluations()
		{
			fields = new HashSet<String>();
			variables = new HashSet<String>();
		}
	}

	protected void initDelayedEvaluations()
	{
		if (getEvaluationTimeValue() == EvaluationTimeEnum.AUTO && delayedEvaluationsMap == null)
		{
			delayedEvaluationsMap = new HashMap<JREvaluationTime,DelayedEvaluations>();
			collectDelayedEvaluations();
		}
	}
	
	protected void collectDelayedEvaluations()
	{
		if (isDelayedStyleEvaluation())
		{
			collectStyleDelayedEvaluations();
		}
	}

	protected void collectStyleDelayedEvaluations()
	{
		JRStyle elementStyle = initStyle;
		if (elementStyle == null)
		{
			elementStyle = filler.getDefaultStyle();
		}
		
		if (elementStyle != null)
		{
			JRStyle style = elementStyle;
			while (style != null)
			{
				collectDelayedEvaluations(style);
				
				// proceed to the parent style
				style = style.getStyle();
			}
		}
	}

	protected void collectDelayedEvaluations(JRStyle style)
	{
		JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();
		// collect delayed evaluations from conditional style expressions
		if (conditionalStyles != null && conditionalStyles.length > 0)
		{
			for (int i = 0; i < conditionalStyles.length; i++)
			{
				collectDelayedEvaluations(
						conditionalStyles[i].getConditionExpression());
			}
		}
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
		DelayedEvaluations delayedEvaluations = delayedEvaluationsMap.get(time);
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
		switch (variable.getResetTypeValue())
		{
			case REPORT:
				evaluationTime = JREvaluationTime.EVALUATION_TIME_REPORT;
				break;
			case PAGE:
				evaluationTime = JREvaluationTime.EVALUATION_TIME_PAGE;
				break;
			case COLUMN:
				evaluationTime = JREvaluationTime.EVALUATION_TIME_COLUMN;
				break;
			case GROUP:
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
		
		if (variable.getCalculationValue() == CalculationEnum.SYSTEM &&
				evaluationTime.equals(JREvaluationTime.EVALUATION_TIME_NOW) &&
				band.isVariableUsedInSubreportReturns(variableName))
		{
			evaluationTime = JREvaluationTime.getBandEvaluationTime(band);
		}

		return evaluationTime;
	}
	
	
	protected void initDelayedEvaluationPrint(JRRecordedValuesPrintElement printElement) throws JRException
	{
		for (Iterator<JREvaluationTime> it = delayedEvaluationsMap.keySet().iterator(); it.hasNext();)
		{
			JREvaluationTime evaluationTime = it.next();
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
			DelayedEvaluations delayedEvaluations = delayedEvaluationsMap.get(evaluationTime);
			
			for (Iterator<String> it = delayedEvaluations.fields.iterator(); it.hasNext();)
			{
				String fieldName = it.next();
				JRFillField field = getField(fieldName);
				recordedValues.recordFieldValue(fieldName, field.getValue(evaluation));
			}

			for (Iterator<String> it = delayedEvaluations.variables.iterator(); it.hasNext();)
			{
				String variableName = it.next();
				JRFillVariable variable = getVariable(variableName);
				recordedValues.recordVariableValue(variableName, variable.getValue(evaluation));
			}
		}

		recordedValues.doneEvaluation(evaluationTime);
		
		if (recordedValues.finishedEvaluations())
		{
			overwriteWithRecordedValues(recordedValues, evaluation);
			performDelayedEvaluation(printElement, evaluation);
			restoreValues(recordedValues, evaluation);
			printElement.deleteRecordedValues();
		}
	}

	
	private void overwriteWithRecordedValues(JRRecordedValues recordedValues, byte evaluation)
	{
		Map<String,Object> fieldValues = recordedValues.getRecordedFieldValues();
		if (fieldValues != null)
		{
			for (Iterator<Map.Entry<String,Object>> it = fieldValues.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<String,Object> entry = it.next();
				String fieldName = entry.getKey();
				Object fieldValue = entry.getValue();
				JRFillField field = getField(fieldName);
				field.overwriteValue(fieldValue, evaluation);
			}
		}
		
		Map<String,Object> variableValues = recordedValues.getRecordedVariableValues();
		if (variableValues != null)
		{
			for (Iterator<Map.Entry<String,Object>> it = variableValues.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<String,Object> entry = it.next();
				String variableName = entry.getKey();
				Object variableValue = entry.getValue();
				JRFillVariable variable = getVariable(variableName);
				variable.overwriteValue(variableValue, evaluation);
			}
		}
	}

	private void restoreValues(JRRecordedValues recordedValues, byte evaluation)
	{
		Map<String,Object> fieldValues = recordedValues.getRecordedFieldValues();
		if (fieldValues != null)
		{
			for (Iterator<String> it = fieldValues.keySet().iterator(); it.hasNext();)
			{
				String fieldName = it.next();
				JRFillField field = getField(fieldName);
				field.restoreValue(evaluation);
			}
		}
		
		Map<String,Object> variableValues = recordedValues.getRecordedVariableValues();
		if (variableValues != null)
		{
			for (Iterator<String> it = variableValues.keySet().iterator(); it.hasNext();)
			{
				String variableName = it.next();
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
		// the current style overrides other style objects
		if (currentStyle != null)
		{
			return currentStyle;
		}
		
		JRStyle crtStyle = initStyle;
		
		boolean isUsingDefaultStyle = false;

		if (crtStyle == null)
		{
			crtStyle = filler.getDefaultStyle();
			isUsingDefaultStyle = true;
		}

		JRStyle evalStyle = crtStyle;

		if (conditionalStylesContainer != null)
		{
			evalStyle = conditionalStylesContainer.getEvaluatedConditionalStyle(crtStyle);
		}
		if (isUsingDefaultStyle && evalStyle == crtStyle)
		{
			evalStyle = null;
		}
		
		return evalStyle;
	}
	
	/**
	 * 
	 */
	protected JRTemplateElement getTemplate(JRStyle style)
	{
		return templates.get(style);
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
		switch (getEvaluationTimeValue())
		{
			case NOW:
				evaluateNow = true;
				break;

			case AUTO:
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
		return getEvaluationTimeValue() == EvaluationTimeEnum.AUTO && !isAutoEvaluateNow();
	}

	public String getStyleNameReference()
	{
		return null;
	}

	public void setStyle(JRStyle style)
	{
		initStyle = style;
		conditionalStylesContainer.collectConditionalStyle(style);
	}

	public void setStyleNameReference(String name)
	{
		throw new UnsupportedOperationException("Style name references not allowed at fill time");
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Object clone(JRElementGroup parentGroup) 
	{
		throw new UnsupportedOperationException();
	}

	public boolean hasProperties()
	{
		return mergedProperties != null && mergedProperties.hasProperties();
	}

	public JRPropertiesMap getPropertiesMap()
	{
		return mergedProperties;
	}

	public JRPropertiesHolder getParentProperties()
	{
		//element properties default to report properties
		return filler.getJasperReport();
	}


	public JRPropertyExpression[] getPropertyExpressions()
	{
		return parent.getPropertyExpressions();
	}
	
	protected void transferProperties(JRTemplateElement template)
	{
		JRProperties.transferProperties(parent, template, 
				JasperPrint.PROPERTIES_PRINT_TRANSFER_PREFIX);
	}
	
	protected void transferProperties(JRPrintElement element)
	{
		JRProperties.transferProperties(dynamicProperties, element, 
				JasperPrint.PROPERTIES_PRINT_TRANSFER_PREFIX);
	}
	
	protected JRPropertiesMap getEvaluatedProperties()
	{
		return mergedProperties;
	}
	
	protected void evaluateProperties(byte evaluation) throws JRException
	{
		JRPropertyExpression[] propExprs = getPropertyExpressions();
		if (propExprs == null || propExprs.length == 0)
		{
			dynamicProperties = null;
			mergedProperties = staticProperties;
		}
		else
		{
			dynamicProperties = new JRPropertiesMap();
			
			for (int i = 0; i < propExprs.length; i++)
			{
				JRPropertyExpression prop = propExprs[i];
				String value = (String) evaluateExpression(prop.getValueExpression(), evaluation);
				if (value != null)
				{
					dynamicProperties.setProperty(prop.getName(), value);
				}
			}
			
			mergedProperties = dynamicProperties.cloneProperties();
			mergedProperties.setBaseProperties(staticProperties);
		}
	}
	
	protected void setOriginProvider(JROriginProvider originProvider)
	{
		this.originProvider = originProvider;
	}

	protected JROrigin getElementOrigin()
	{
		JROrigin elementOrigin = null;
		if (originProvider != null)
		{
			elementOrigin = originProvider.getOrigin();
		}
		return elementOrigin;
	}
	
	protected boolean isDelayedStyleEvaluation()
	{
		return JRProperties.getBooleanProperty(this, 
				JRStyle.PROPERTY_EVALUATION_TIME_ENABLED, false);
	}
}
