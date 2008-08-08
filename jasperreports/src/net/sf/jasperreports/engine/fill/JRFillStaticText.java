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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVisitor;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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
		JRStyle style = getStyle();
		JRTemplateText template = (JRTemplateText) getTemplate(style);
		if (template == null)
		{
			template = 
				new JRTemplateText(
					band == null ? null : band.getOrigin(), 
					filler.getJasperPrint().getDefaultStyleProvider(), 
					this
					);
			transferProperties(template);
			registerTemplate(style, template);
		}
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
		
		setTextStart(0);
		setTextEnd(0);
		
		setValueRepeating(true);
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
			availableStretchHeight < getRelativeY() - getY() - getBandBottomY()
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

		setTextStart(0);
		setTextEnd(0);

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
		JRTemplatePrintText text = new JRTemplatePrintText(getJRTemplateText());
		text.setX(getX());
		text.setY(getRelativeY());
		text.setWidth(getWidth());
		if (getRotation() == ROTATION_NONE)
		{
			text.setHeight(getStretchHeight());
		}
		else
		{
			text.setHeight(getHeight());
		}
		text.setRunDirection(getRunDirection());
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
