/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
package dori.jasper.engine.fill;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Map;

import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JRTextField;
import dori.jasper.engine.util.JRStringUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillTextField extends JRFillTextElement implements JRTextField
{


	/**
	 *
	 */
	private JRGroup evaluationGroup = null;

	/**
	 *
	 */
	private Format format = null;
	private boolean isValueRepeating = false;
	private String oldRawText = null;
	private String anchorName = null;
	private String hyperlinkReference = null;
	private String hyperlinkAnchor = null;
	private Integer hyperlinkPage = null;


	/**
	 *
	 */
	protected JRFillTextField(
		JRBaseFiller filler,
		JRTextField textField, 
		JRFillObjectFactory factory
		)
	{
		super(filler, textField, factory);
		
		evaluationGroup = (JRGroup)factory.getGroup(textField.getEvaluationGroup());

		JRExpression expression = textField.getExpression();
		String pattern = textField.getPattern();
		if (
			expression != null &&
			pattern != null && 
			pattern.length() > 0
			)
		{
			Class expressionClass = expression.getValueClass();
			if (java.util.Date.class.isAssignableFrom(expressionClass))
			{
				format = new SimpleDateFormat(pattern);
			}
			else if (java.lang.Number.class.isAssignableFrom(expressionClass))
			{
				format = new DecimalFormat(pattern);
			}
		}
	}


	/**
	 *
	 */
	public boolean isStretchWithOverflow()
	{
		return ((JRTextField)parent).isStretchWithOverflow();
	}

	/**
	 *
	 */
	public void setStretchWithOverflow(boolean isStretchWithOverflow)
	{
	}

	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return ((JRTextField)parent).getEvaluationTime();
	}
		
	/**
	 *
	 */
	public String getPattern()
	{
		return ((JRTextField)parent).getPattern();
	}
		
	/**
	 *
	 */
	public void setPattern(String pattern)
	{
	}
		
	/**
	 *
	 */
	public boolean isBlankWhenNull()
	{
		return ((JRTextField)parent).isBlankWhenNull();
	}

	/**
	 *
	 */
	public void setBlankWhenNull(boolean isBlank)
	{
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return ((JRTextField)parent).getHyperlinkType();
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return ((JRTextField)parent).getExpression();
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return ((JRTextField)parent).getAnchorNameExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRTextField)parent).getHyperlinkReferenceExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRTextField)parent).getHyperlinkAnchorExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return ((JRTextField)parent).getHyperlinkPageExpression();
	}

		
	/**
	 *
	 */
	protected String getAnchorName()
	{
		return anchorName;
	}

	/**
	 *
	 */
	protected String getHyperlinkReference()
	{
		return hyperlinkReference;
	}

	/**
	 *
	 */
	protected String getHyperlinkAnchor()
	{
		return hyperlinkAnchor;
	}

	/**
	 *
	 */
	protected Integer getHyperlinkPage()
	{
		return hyperlinkPage;
	}
		

	/**
	 *
	 */
	protected JRTemplateText getJRTemplateText()
	{
		if (template == null)
		{
			template = new JRTemplateText((JRTextField)parent, getFont());
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

		if (
			(isPrintWhenExpressionNull() ||
			(!isPrintWhenExpressionNull() && 
			isPrintWhenTrue()))
			)
		{
			if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
			{
				evaluateText(evaluation);
			}
		}
	}


	/**
	 *
	 */
	protected void evaluateText(
		byte evaluation
		) throws JRException
	{
		Object textFieldValue = filler.calculator.evaluate(getExpression(), evaluation);

		if (textFieldValue == null)
		{
			if (isBlankWhenNull())
			{
				textFieldValue = "";
			}
		}
		else
		{
			if (format != null)
			{
				textFieldValue = format.format(textFieldValue);
			}
		}

		String newRawText = 
			JRStringUtil.treatNewLineChars(
				String.valueOf(textFieldValue)
				);
		String oldRawText = getRawText();

		this.oldRawText = oldRawText;
		setRawText(newRawText);
		setTextStart(0);
		setTextEnd(0);

		if (
			(oldRawText == null && newRawText == null) ||
			(oldRawText != null && oldRawText.equals(newRawText))
			)
		{
			isValueRepeating = true;
		}
		else
		{
			isValueRepeating = false;
		}

		anchorName = (String)filler.calculator.evaluate(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String)filler.calculator.evaluate(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String)filler.calculator.evaluate(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer)filler.calculator.evaluate(getHyperlinkPageExpression(), evaluation);
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

		if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
		{
			if (isOverflow)
			{
				if (getPositionType() == JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM)
				{
					// the content of the band bottom text fields is not
					// consumed during overflows, because they only appear on the last overflow
					setTextStart(0);
					setTextEnd(0);
				}

				if (
					getTextEnd() >= getText().length()
					|| !isStretchWithOverflow()
					|| getRotation() != ROTATION_NONE
					)
				{
					// there is no more text left in the text field to overflow
					// on the new page, or the text field is not stretchable
					
					if (isAlreadyPrinted())
					{
						// the text field has already displayed all its content
						// on the previous page even if it not stretchable
						
						if (isPrintWhenDetailOverflows())
						{
							// the whole content is reprinted
							setTextStart(0);
							setTextEnd(0);

							isReprinted = true;
						}
						else
						{
							isToPrint = false;
						}
					}
					else
					{
						// the text field did not print on the previous page.
						// we let it go since it is its first time anyway
					}
				}
				else
				{
					// there is text left inside the stretchable text field.
					// we simply let it go
				}

				if (
					isToPrint &&
					isPrintWhenExpressionNull() &&
					!isPrintRepeatedValues() &&
					isValueRepeating
					)
				{
					isToPrint = false; // FIXME, shouldn't we test for the first whole band and the other exceptions to the rule?
				}
			}
			else
			{
				if (
					isPrintWhenExpressionNull() &&
					!isPrintRepeatedValues() &&
					isValueRepeating
					)
				{
					if (
						( !isPrintInFirstWholeBand() || !getBand().isNewPageColumn() ) &&
						( getPrintWhenGroupChanges() == null || !getBand().isNewGroup(getPrintWhenGroupChanges()) )
						)
					{
						isToPrint = false;
					}
				}
			}

			if (isToPrint)
			{
				if (availableStretchHeight >= getRelativeY() - getY() - getBandBottomY())
				{
					// the available vertical space is sufficient

					if (
						getTextEnd() < getText().length() 
						|| getTextEnd() == 0
						)
					{
						// there is still some text left in the text field or
						// the text field is empty

						if (
							isStretchWithOverflow()
							&& getRotation() == ROTATION_NONE
							)
						{
							// the text field is allowed to stretch downwards in order to
							// display all its content

							chopTextElement(availableStretchHeight - getRelativeY() + getY() + getBandBottomY());
							if (getTextEnd() < getText().length())// - 1)
							{
								// even after the current chop operation there is some text left
								// that will overflow on the next page

								willOverflow = true;
							}
						}
						else
						{
							// the text field is not allowed to stretch downwards in order to
							// display all its content

							chopTextElement(0);
						}
					}
					else
					{
						// there is no text left in the text field and the text field was not empty

						// this section is probably unreachable since it is most likely that
						// the isToPrint flag was already set on false in the code above.
						isToPrint = false;
					}
				}
				else
				{
					// the available vertical space is not sufficient

					// no matter if there is some text left inside or not,
					// there was an explicit request to display it, 
					// even if we are on an overflow.
					// since there is no space available, it will overflow
					
					isToPrint = false;
					willOverflow = true;
				}
			}

			if (
				isToPrint &&
				isRemoveLineWhenBlank() && //FIXME if the line won't be removed, the background does not appear
				getText().substring(
					getTextStart(),
					getTextEnd()
					).trim().length() == 0
				)
			{
				isToPrint = false;
			}
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

		switch (getEvaluationTime())
		{
			case JRExpression.EVALUATION_TIME_REPORT :
			{
				filler.reportBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_PAGE :
			{
				filler.pageBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_COLUMN :
			{
				filler.columnBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_GROUP :
			{
				Map specificGroupBoundTexts = (Map)filler.groupBoundTexts.get(getEvaluationGroup().getName());
				specificGroupBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_NOW :
			default :
			{
				copy(text);
			}
		}

		return text;
	}


	/**
	 *
	 */
	protected void copy(JRPrintText text) throws JRException
	{
		text.setLineSpacingFactor(getLineSpacingFactor());
		text.setLeadingOffset(getLeadingOffset());
		text.setTextHeight(getTextHeight());

		/*
		text.setText(
			getRawText()
			);
		*/
		/*
		text.setText(
			getRawText().substring(
				getTextStart(),
				getTextEnd()
				)
			);
		*/
		text.setText(
			getText().substring(
				getTextStart(),
				getTextEnd()
				)
			);

		text.setAnchorName(getAnchorName());
		text.setHyperlinkReference(getHyperlinkReference());
		text.setHyperlinkAnchor(getHyperlinkAnchor());
		text.setHyperlinkPage(getHyperlinkPage());
	}


}
