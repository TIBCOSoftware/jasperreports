/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRVisitor;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRFillStaticText extends JRFillTextElement implements JRStaticText
{


	/**
	 *
	 */
	protected JRFillStaticText(
			JRBaseFiller filler,
			JRStaticText staticText, 
			JRFillObjectFactory factory
			)
		{
			super(filler, staticText, factory);
			
			String text = processMarkupText(staticText.getText());
			if (text == null)
			{
				text = "";
			}
			setRawText(text);
		}


	protected JRFillStaticText(JRFillStaticText staticText, JRFillCloneFactory factory)
	{
		super(staticText, factory);

		String text = processMarkupText(staticText.getText());
		if (text == null)
		{
			text = "";
		}
		setRawText(text);
	}


	/**
	 * 
	 */
	public void setText(String text)
	{
	}


	/**
	 *
	 */
	protected JRTemplateText getJRTemplateText()
	{
		return (JRTemplateText) getElementTemplate();
	}


	protected JRTemplateElement createElementTemplate()
	{
		JRTemplateText template = new JRTemplateText(
				getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider(), 
				this
				);
		template.copyParagraph(getPrintParagraph());
		template.copyLineBox(getPrintLineBox());
		return template;
	}


	/**
	 *
	 */
	protected void evaluate(
		byte evaluation
		) throws JRException
	{
		reset();
		
		evaluatePrintWhenExpression(evaluation);
		evaluateProperties(evaluation);
		evaluateStyle(evaluation);

		resetTextChunk();
		
		setValueRepeating(true);
	}


	/**
	 *
	 */
	protected boolean prepare(
		int availableHeight,
		boolean isOverflow
		) throws JRException
	{
		boolean willOverflow = false;

		super.prepare(availableHeight, isOverflow);
		
		if (!isToPrint())
		{
			return willOverflow;
		}
		
		boolean isToPrint = true;
		boolean isReprinted = false;

		if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
		{
			isToPrint = false;
		}

		if (
			isToPrint && 
			isPrintWhenExpressionNull() &&
			!isPrintRepeatedValues()
			)
		{
			if (
				( !isPrintInFirstWholeBand() || !getBand().isFirstWholeOnPageColumn()) &&
				( getPrintWhenGroupChanges() == null || !getBand().isNewGroup(getPrintWhenGroupChanges()) ) &&
				( !isOverflow || !isPrintWhenDetailOverflows() )
				)
			{
				isToPrint = false;
			}
		}

		if (
			isToPrint && 
			availableHeight < getRelativeY() + getHeight()
			)
		{
			isToPrint = false;
			willOverflow = true;
		}
		
		if (
			isToPrint && 
			isOverflow && 
			//(isAlreadyPrinted() || !isPrintRepeatedValues())
			(isPrintWhenDetailOverflows() && (isAlreadyPrinted() || (!isAlreadyPrinted() && !isPrintRepeatedValues())))
			)
		{
			isReprinted = true;
		}

		resetTextChunk();

		if (isToPrint)
		{
			chopTextElement(0);
		}
		
		setToPrint(isToPrint);
		setReprinted(isReprinted);
		
		return willOverflow;
	}


	/**
	 *
	 */
	protected JRPrintElement fill()
	{
		JRTemplatePrintText text = new JRTemplatePrintText(getJRTemplateText(), printElementOriginator);
		text.setUUID(getUUID());
		text.setX(getX());
		text.setY(getRelativeY());
		text.setWidth(getWidth());
//		if (getRotation() == ROTATION_NONE)
//		{
			text.setHeight(getPrintElementHeight());
//		}
//		else
//		{
//			text.setHeight(getHeight());
//		}
		text.setRunDirection(getRunDirectionValue());
		text.setLineSpacingFactor(getLineSpacingFactor());
		text.setLeadingOffset(getLeadingOffset());
		text.setTextHeight(getTextHeight());
		transferProperties(text);

		//text.setText(getRawText());
		setPrintText(text);
		
		return text;
	}


	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitStaticText(this);
	}

	
	protected void resolveElement (JRPrintElement element, byte evaluation)
	{
		// nothing
	}


	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillStaticText(this, factory);
	}


	protected boolean canOverflow()
	{
		return false;
	}

}
