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
	private String text = null;
	private Format format = null;
	private boolean isValueRepeating = false;
	private String oldText = null;
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
			if (
				expressionClass.equals(java.util.Date.class) || 
				expressionClass.equals(java.sql.Timestamp.class) ||
				expressionClass.equals(java.sql.Time.class)
				)
			{
				this.format = new SimpleDateFormat(pattern);
			}
			else if (expressionClass.getSuperclass().equals(java.lang.Number.class))
			{
				this.format = new DecimalFormat(pattern);
			}
		}
	}


	/**
	 *
	 */
	public boolean isStretchWithOverflow()
	{
		return ((JRTextField)this.parent).isStretchWithOverflow();
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
		return ((JRTextField)this.parent).getEvaluationTime();
	}
		
	/**
	 *
	 */
	public String getPattern()
	{
		return ((JRTextField)this.parent).getPattern();
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
		return ((JRTextField)this.parent).isBlankWhenNull();
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
		return ((JRTextField)this.parent).getHyperlinkType();
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return this.evaluationGroup;
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return ((JRTextField)this.parent).getExpression();
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return ((JRTextField)this.parent).getAnchorNameExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRTextField)this.parent).getHyperlinkReferenceExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRTextField)this.parent).getHyperlinkAnchorExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return ((JRTextField)this.parent).getHyperlinkPageExpression();
	}

		
	/**
	 *
	 */
	protected String getText()
	{
		return this.text;
	}

	/**
	 *
	 */
	protected String getAnchorName()
	{
		return this.anchorName;
	}

	/**
	 *
	 */
	protected String getHyperlinkReference()
	{
		return this.hyperlinkReference;
	}

	/**
	 *
	 */
	protected String getHyperlinkAnchor()
	{
		return this.hyperlinkAnchor;
	}

	/**
	 *
	 */
	protected Integer getHyperlinkPage()
	{
		return this.hyperlinkPage;
	}
		

	/**
	 *
	 */
	protected JRTemplateText getJRTemplateText()
	{
		if (template == null)
		{
			template = new JRTemplateText((JRTextField)this.parent, getFont());
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
		this.reset();
		
		this.evaluatePrintWhenExpression(evaluation);

		if (
			(this.isPrintWhenExpressionNull() ||
			(!this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue()))
			)
		{
			if (this.getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
			{
				this.evaluateText(evaluation);
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
		Object textFieldValue = this.filler.calculator.evaluate(this.getExpression(), evaluation);

		if (textFieldValue == null)
		{
			if (this.isBlankWhenNull())
			{
				textFieldValue = "";
			}
		}
		else
		{
			if (this.format != null)
			{
				textFieldValue = this.format.format(textFieldValue);
			}
		}

		String newText = 
			JRStringUtil.treatNewLineChars(
				String.valueOf(textFieldValue)
				);
		String oldText = this.getText();

		this.oldText = oldText;
		this.text = newText;
		this.setTextStart(0);
		this.setTextEnd(0);

		if (
			(oldText == null && newText == null) ||
			(oldText != null && oldText.equals(newText))
			)
		{
			this.isValueRepeating = true;
		}
		else
		{
			this.isValueRepeating = false;
		}

		this.anchorName = (String)this.filler.calculator.evaluate(this.getAnchorNameExpression(), evaluation);
		this.hyperlinkReference = (String)this.filler.calculator.evaluate(this.getHyperlinkReferenceExpression(), evaluation);
		this.hyperlinkAnchor = (String)this.filler.calculator.evaluate(this.getHyperlinkAnchorExpression(), evaluation);
		this.hyperlinkPage = (Integer)this.filler.calculator.evaluate(this.getHyperlinkPageExpression(), evaluation);
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

		if (!this.isToPrint())
		{
			return willOverflow;
		}

		boolean isToPrint = true;
		boolean isReprinted = false;

		if (this.getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
		{
			if (isOverflow)
			{
				if (this.getPositionType() == JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM)
				{
					// the content of the band bottom text fields is not
					// consumed during overflows, because they only appear on the last overflow
					this.setTextStart(0);
					this.setTextEnd(0);
				}

				if (
					this.getTextEnd() >= this.getText().length()
					|| !this.isStretchWithOverflow()
					|| this.getRotation() != ROTATION_NONE
					)
				{
					// there is no more text left in the text field to overflow
					// on the new page, or the text field is not stretchable
					
					if (this.isAlreadyPrinted())
					{
						// the text field has already displayed all its content
						// on the previous page even if it not stretchable
						
						if (this.isPrintWhenDetailOverflows())
						{
							// the whole content is reprinted
							this.setTextStart(0);
							this.setTextEnd(0);

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
					this.isPrintWhenExpressionNull() &&
					!this.isPrintRepeatedValues() &&
					this.isValueRepeating
					)
				{
					isToPrint = false; // FIXME, shouldn't we test for the first whole band and the other exceptions to the rule?
				}
			}
			else
			{
				if (
					this.isPrintWhenExpressionNull() &&
					!this.isPrintRepeatedValues() &&
					this.isValueRepeating
					)
				{
					if (
						( !this.isPrintInFirstWholeBand() || !this.getBand().isNewPageColumn() ) &&
						( this.getPrintWhenGroupChanges() == null || !this.getBand().isNewGroup(this.getPrintWhenGroupChanges()) )
						)
					{
						isToPrint = false;
					}
				}
			}

			if (isToPrint)
			{
				if (availableStretchHeight >= this.getRelativeY() - this.getY() - this.getBandBottomY())
				{
					// the available vertical space is sufficient

					if (
						this.getTextEnd() < this.getText().length() 
						|| this.getTextEnd() == 0
						)
					{
						// there is still some text left in the text field or
						// the text field is empty

						if (
							this.isStretchWithOverflow()
							&& this.getRotation() == ROTATION_NONE
							)
						{
							// the text field is allowed to stretch downwards in order to
							// display all its content

							this.chopTextElement(availableStretchHeight - this.getRelativeY() + this.getY() + this.getBandBottomY());
							if (this.getTextEnd() < this.getText().length())// - 1)
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

							this.chopTextElement(0);
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
				this.isRemoveLineWhenBlank() && //FIXME if the line won't be removed, the background does not appear
				this.getText().substring(
					this.getTextStart(),
					this.getTextEnd()
					).trim().length() == 0
				)
			{
				isToPrint = false;
			}
		}

		this.setToPrint(isToPrint);
		this.setReprinted(isReprinted);

		return willOverflow;
	}


	/**
	 *
	 */
	protected JRPrintElement fill() throws JRException
	{
		JRPrintText text = null;

		text = new JRTemplatePrintText(this.getJRTemplateText());
		text.setX(this.getX());
		text.setY(this.getRelativeY());
		if (this.getRotation() == ROTATION_NONE)
		{
			text.setHeight(this.getStretchHeight());
		}
		else
		{
			text.setHeight(this.getHeight());
		}

		switch (this.getEvaluationTime())
		{
			case JRExpression.EVALUATION_TIME_REPORT :
			{
				this.filler.reportBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_PAGE :
			{
				this.filler.pageBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_COLUMN :
			{
				this.filler.columnBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_GROUP :
			{
				Map specificGroupBoundTexts = (Map)this.filler.groupBoundTexts.get(this.getEvaluationGroup().getName());
				specificGroupBoundTexts.put(text, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_NOW :
			default :
			{
				this.copy(text);
			}
		}

		return text;
	}


	/**
	 *
	 */
	protected void copy(JRPrintText text) throws JRException
	{
		text.setAbsoluteLineSpacing(this.getAbsoluteLineSpacing());
		text.setAbsoluteLeading(this.getAbsoluteLeading());
		text.setTextHeight(this.getTextHeight());

		text.setText(
			this.getText().substring(
				this.getTextStart(),
				this.getTextEnd()
				)
			);

		text.setAnchorName(this.getAnchorName());
		text.setHyperlinkReference(this.getHyperlinkReference());
		text.setHyperlinkAnchor(this.getHyperlinkAnchor());
		text.setHyperlinkPage(this.getHyperlinkPage());
	}


}
