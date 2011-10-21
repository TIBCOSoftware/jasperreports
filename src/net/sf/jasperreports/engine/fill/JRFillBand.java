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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBand extends JRFillElementContainer implements JRBand, JROriginProvider
{

	private static final Log log = LogFactory.getLog(JRFillBand.class);

	/**
	 *
	 */
	private JRBand parent;

	private boolean isPrintWhenTrue = true;

	/**
	 *
	 */
	private boolean isNewPageColumn;
	private boolean isFirstWholeOnPageColumn;
	private Map<JRGroup,Boolean> isNewGroupMap = new HashMap<JRGroup,Boolean>();

	private Set<JREvaluationTime> nowEvaluationTimes;
	
	// used by subreports to save values of variables used as return receptacles
	// so that the values can be restored when the bands gets rewound
	private Map<String,Object> savedVariableValues = new HashMap<String,Object>();

	protected JROrigin origin;
	
	private SplitTypeEnum splitType;
	private int breakHeight;

	
	/**
	 *
	 */
	protected JRFillBand(
		JRBaseFiller filler,
		JRBand band,
		JRFillObjectFactory factory
		)
	{
		super(filler, band, factory);

		parent = band;

		if (deepElements.length > 0)
		{
			for(int i = 0; i < deepElements.length; i++)
			{
				deepElements[i].setBand(this);
			}
		}

		splitType = (parent == null ? null : parent.getSplitTypeValue());
		if (splitType == null)
		{
			splitType = 
				SplitTypeEnum.getByName(
					JRProperties.getProperty(filler.getJasperReport(), JRBand.PROPERTY_SPLIT_TYPE)
					);
		}
		
		breakHeight = getHeight();
		if (
			SplitTypeEnum.IMMEDIATE == getSplitTypeValue()
			&& elements != null && elements.length > 0
			)
		{
			for(int i = 0; i < elements.length; i++)
			{
				JRElement element = elements[i];
				int bottom = element.getY() + element.getHeight();
				breakHeight = bottom < breakHeight ? bottom : breakHeight;
			}
		}

		initElements();

		initConditionalStyles();

		nowEvaluationTimes = new HashSet<JREvaluationTime>();
	}


	/**
	 *
	 */
	public JROrigin getOrigin()
	{
		return origin;
	}

	
	/**
	 *
	 */
	protected void setOrigin(JROrigin origin)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Origin " + origin + " for band " + getId());
		}
		
		this.origin = origin;
		this.filler.getJasperPrint().addOrigin(origin);
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
		return isNewPageColumn;
	}


	/**
	 * Decides whether this band is the for whole band on the page/column.
	 *
	 * @return whether this band is the for whole band on the page/column
	 */
	protected boolean isFirstWholeOnPageColumn()
	{
		return isFirstWholeOnPageColumn;
	}


	/**
	 *
	 */
	protected void setNewGroup(JRGroup group, boolean isNew)
	{
		isNewGroupMap.put(group, isNew ? Boolean.TRUE : Boolean.FALSE);
	}


	/**
	 *
	 */
	protected boolean isNewGroup(JRGroup group)
	{
		Boolean value = isNewGroupMap.get(group);

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
		return (parent == null ? 0 : parent.getHeight());
	}

	/**
	 *
	 */
	public int getBreakHeight()
	{
		return breakHeight;
	}

	/**
	 *
	 */
	public SplitTypeEnum getSplitTypeValue()
	{
		return splitType;
	}

	/**
	 *
	 */
	public void setSplitType(SplitTypeEnum splitType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return (parent == null ? null : parent.getPrintWhenExpression());
	}

	/**
	 *
	 */
	protected boolean isSplitPrevented()
	{
		return SplitTypeEnum.PREVENT == getSplitTypeValue();
	}

	/**
	 *
	 */
	protected boolean isPrintWhenExpressionNull()
	{
		return (getPrintWhenExpression() == null);
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
		return
			(isPrintWhenExpressionNull() ||
			 (!isPrintWhenExpressionNull() &&
			  isPrintWhenTrue()));
	}


	/**
	 *
	 */
	protected void evaluatePrintWhenExpression(
		byte evaluation
		) throws JRException
	{
		boolean isPrintTrue = false;

		JRExpression expression = getPrintWhenExpression();
		if (expression != null)
		{
			Boolean printWhenExpressionValue = (Boolean)filler.evaluateExpression(expression, evaluation);
			if (printWhenExpressionValue == null)
			{
				isPrintTrue = false;
			}
			else
			{
				isPrintTrue = printWhenExpressionValue.booleanValue();
			}
		}

		setPrintWhenTrue(isPrintTrue);
	}



	/**
	 *
	 */
	protected JRPrintBand refill(
		int availableHeight
		) throws JRException
	{
		rewind();
		restoreSavedVariables();

		return fill(availableHeight);
	}


	/**
	 *
	 */
	protected JRPrintBand fill() throws JRException
	{
		return fill(getHeight(), false);
	}


	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableHeight
		) throws JRException
	{
		return fill(availableHeight, true);
	}


	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableHeight,
		boolean isOverflowAllowed
		) throws JRException
	{
		filler.fillContext.ensureMasterPageAvailable();

		if (
			Thread.interrupted()
			|| filler.isInterrupted()
			)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Fill " + filler.fillerId + ": interrupted");
			}

			// child fillers will stop if this parent filler was marked as interrupted
			filler.setInterrupted(true);

			throw new JRFillInterruptedException();
		}

		filler.setBandOverFlowAllowed(isOverflowAllowed);

		initFill();

		if (isNewPageColumn && !isOverflow)
		{
			isFirstWholeOnPageColumn = true;
		}
		
		resetElements();

		prepareElements(availableHeight, isOverflowAllowed);

		stretchElements();

		moveBandBottomElements();

		removeBlankElements();

		isFirstWholeOnPageColumn = isNewPageColumn && isOverflow;
		isNewPageColumn = false;
		isNewGroupMap = new HashMap<JRGroup,Boolean>();

		JRPrintBand printBand = new JRPrintBand();
		fillElements(printBand);

		return printBand;
	}


	protected int getContainerHeight()
	{
		return getHeight();
	}


	protected boolean isVariableUsedInSubreportReturns(String variableName)
	{
		boolean used = false;
		if (deepElements != null)
		{
			for (int i = 0; i < deepElements.length; i++)
			{
				JRFillElement element = deepElements[i];
				if (element instanceof JRFillSubreport)
				{
					JRFillSubreport subreport = (JRFillSubreport) element;
					if (subreport.usesForReturnValue(variableName))
					{
						used = true;
						break;
					}
				}
			}
		}

		return used;
	}


	protected void addNowEvaluationTime(JREvaluationTime evaluationTime)
	{
		nowEvaluationTimes.add(evaluationTime);
	}


	protected void addNowEvaluationTimes(JREvaluationTime[] evaluationTimes)
	{
		for (int i = 0; i < evaluationTimes.length; i++)
		{
			nowEvaluationTimes.add(evaluationTimes[i]);
		}
	}


	protected boolean isNowEvaluationTime(JREvaluationTime evaluationTime)
	{
		return nowEvaluationTimes.contains(evaluationTime);
	}


	protected int getId()
	{
		return System.identityHashCode(this);
	}


	protected void evaluate(byte evaluation) throws JRException
	{
		resetSavedVariables();
		evaluateConditionalStyles(evaluation);
		super.evaluate(evaluation);
	}
	
	protected void resetSavedVariables()
	{
		savedVariableValues.clear();
	}
	
	protected void saveVariable(String variableName)
	{
		if (!savedVariableValues.containsKey(variableName))
		{
			Object value = filler.getVariableValue(variableName);
			savedVariableValues.put(variableName, value);
		}
	}
	
	protected void restoreSavedVariables()
	{
		for (Iterator<Map.Entry<String,Object>> it = savedVariableValues.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<String,Object> entry = it.next();
			String variableName = entry.getKey();
			Object value = entry.getValue();
			JRFillVariable variable = filler.getVariable(variableName);
			variable.setOldValue(value);
			variable.setValue(value);
			variable.setIncrementedValue(value);
		}
	}


	protected boolean isEmpty()
	{
		return this == filler.missingFillBand
			|| (getHeight() == 0
					&& (getElements() == null || getElements().length == 0)
					&& getPrintWhenExpression() == null);
	}

	protected boolean isPageBreakInhibited()
	{
		boolean isPageBreakInhibited = filler.isFirstPageBand && firstYElement == null;
		
		if (isPageBreakInhibited && filler.isSubreport())
		{
			isPageBreakInhibited = filler.parentElement.getBand().isPageBreakInhibited();
		}
		
		return isPageBreakInhibited;
	}

}
