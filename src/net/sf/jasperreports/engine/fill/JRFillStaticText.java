/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStaticText;


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
		
		//setRawText(JRStringUtil.treatNewLineChars(staticText.getText()));
		setRawText(staticText.getText());
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
		if (template == null)
		{
			template = new JRTemplateText((JRStaticText)parent, getFont());
		}
		
		return (JRTemplateText)template;
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
		
		setTextStart(0);
		setTextEnd(0);
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
				( !isPrintInFirstWholeBand() || !getBand().isNewPageColumn() ) &&
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
	protected JRPrintElement fill() throws JRException
	{
		JRPrintText text = null;

		text = new JRTemplatePrintText(getJRTemplateText());
		text.setX(getX());
		text.setY(getRelativeY());
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

		//text.setText(getRawText());
		text.setText(textChopper.chop(this, getTextStart(), getTextEnd()));
		
		return text;
	}


}
