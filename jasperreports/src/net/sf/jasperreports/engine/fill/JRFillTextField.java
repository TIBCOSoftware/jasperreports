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

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;


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
	private String anchorName = null;
	private String hyperlinkReference = null;
	private String hyperlinkAnchor = null;
	private Integer hyperlinkPage = null;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;

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
		
		evaluationGroup = factory.getGroup(textField.getEvaluationGroup());
	}

	
	protected JRFillTextField(JRFillTextField textField, JRFillCloneFactory factory)
	{
		super(textField, factory);

		this.evaluationGroup = textField.evaluationGroup;
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
		return JRStyleResolver.getPattern(this);
	}
		
	public String getOwnPattern()
	{
		return ((JRTextField)parent).getOwnPattern();
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
		return JRStyleResolver.isBlankWhenNull(this);
	}

	/**
	 *
	 */
	public Boolean isOwnBlankWhenNull()
	{
		return ((JRTextField)parent).isOwnBlankWhenNull();
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
	public void setBlankWhenNull(Boolean isBlank)
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
	public byte getHyperlinkTarget()
	{
		return ((JRTextField)parent).getHyperlinkTarget();
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
		

	protected String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
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
					getElementOrigin(), 
					filler.getJasperPrint().getDefaultStyleProvider(), 
					this
					);
			transferProperties(template);
			setTemplatePattern(template);
			
			registerTemplate(style, template);
		}
		
		return template;
	}


	protected void setTemplatePattern(JRTemplateText template)
	{
		if (getExpression() != null)
		{
			Class valueClass = getExpression().getValueClass();
			if (!String.class.equals(valueClass))
			{
				template.setValueClassName(valueClass.getName());

				String pattern = getTemplatePattern();
				if (pattern != null)
				{
					template.setPattern(pattern);
				}
				
				if (!filler.hasMasterFormatFactory())
				{
					template.setFormatFactoryClass(filler.getFormatFactory().getClass().getName());
				}
				
				if (!filler.hasMasterLocale())
				{
					template.setLocaleCode(JRDataUtils.getLocaleCode(filler.getLocale()));
				}

				if (!filler.hasMasterTimeZone() && java.util.Date.class.isAssignableFrom(valueClass))
				{
					template.setTimeZoneId(JRDataUtils.getTimeZoneId(filler.getTimeZone()));
				}
			}
		}
	}


	/**
	 *
	 */
	protected void evaluate(
		byte evaluation
		) throws JRException
	{
		initDelayedEvaluations();
		
		reset();
		
		evaluatePrintWhenExpression(evaluation);

		if (
			(isPrintWhenExpressionNull() ||
			(!isPrintWhenExpressionNull() && 
			isPrintWhenTrue()))
			)
		{
			if (isEvaluateNow())
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
		evaluateProperties(evaluation);
		
		Object textFieldValue = evaluateExpression(getExpression(), evaluation);

		if (textFieldValue == null)
		{
			if (isBlankWhenNull())
			{
				textFieldValue = "";
			}
		}
		else
		{
			Format format = getFormat();
			if (format != null)
			{
				textFieldValue = format.format(textFieldValue);
			}
		}

		String oldRawText = getRawText();
		String newRawText = processMarkupText(String.valueOf(textFieldValue));

		setRawText(newRawText);
		setTextStart(0);
		setTextEnd(0);

		setValueRepeating(
				(oldRawText == null && newRawText == null) ||
				(oldRawText != null && oldRawText.equals(newRawText))
			);

		anchorName = (String) evaluateExpression(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) evaluateExpression(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String) evaluateExpression(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) evaluateExpression(getHyperlinkPageExpression(), evaluation);
		hyperlinkTooltip = (String) evaluateExpression(getHyperlinkTooltipExpression(), evaluation);
		hyperlinkParameters = JRFillHyperlinkHelper.evaluateHyperlinkParameters(this, expressionEvaluator, evaluation);
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

		if (isEvaluateNow())
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
					isValueRepeating()
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
					isValueRepeating()
					)
				{
					if (
						( !isPrintInFirstWholeBand() || !getBand().isFirstWholeOnPageColumn() ) &&
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
				isRemoveLineWhenBlank() &&	//FIXME if the line won't be removed due to other elements 
				getText().substring(		// present on that line, the background does not appear
					getTextStart(),
					getTextEnd()
					).trim().length() == 0
				)
			{
				isToPrint = false;
			}
		}
		else
		{
			if (isOverflow && isAlreadyPrinted())
			{
				if (isPrintWhenDetailOverflows())
				{
					isReprinted = true;
				}
				else
				{
					isToPrint = false;
				}
			}
			
			if (
				isToPrint && 
				availableStretchHeight < this.getRelativeY() - this.getY() - this.getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
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
		byte evaluationType = getEvaluationTime();
		
		JRTemplatePrintText text;
		JRRecordedValuesPrintText recordedValuesText;
		if (isEvaluateAuto())
		{
			text = recordedValuesText = new JRRecordedValuesPrintText(getJRTemplateText());
		}
		else
		{
			text = new JRTemplatePrintText(getJRTemplateText());
			recordedValuesText = null;
		}
		
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

		if (isEvaluateNow())
		{
			copy(text);
		}
		else if (isEvaluateAuto())
		{
			initDelayedEvaluationPrint(recordedValuesText);
		}
		else
		{
			filler.addBoundElement(this, text, evaluationType, getEvaluationGroup(), band);
		}

		return text;
	}


	/**
	 *
	 */
	protected void copy(JRPrintText text)
	{
		text.setLineSpacingFactor(getLineSpacingFactor());
		text.setLeadingOffset(getLeadingOffset());
		text.setTextHeight(getTextHeight());
		//FIXME rotation and run direction?

		setPrintText(text);

		text.setAnchorName(getAnchorName());
		text.setHyperlinkReference(getHyperlinkReference());
		text.setHyperlinkAnchor(getHyperlinkAnchor());
		text.setHyperlinkPage(getHyperlinkPage());
		text.setHyperlinkTooltip(getHyperlinkTooltip());
		text.setBookmarkLevel(getBookmarkLevel());
		text.setHyperlinkParameters(hyperlinkParameters);
		transferProperties(text);
	}
	
	
	/**
	 *
	 */
	protected Format getFormat()//FIXMEFORMAT optimize this with an interface
	{
		Format format = null;

		JRExpression valueExpression = getExpression();
		if (valueExpression != null)
		{
			Class valueClass = valueExpression.getValueClass();
			if (java.util.Date.class.isAssignableFrom(valueClass))
			{
				format = filler.getDateFormat(getPattern());
			}
			else if (java.lang.Number.class.isAssignableFrom(valueClass))
			{
				format = filler.getNumberFormat(getPattern());
			}
		}
		
		return format;
	}

	/**
	 *
	 */
	protected String getTemplatePattern()//FIXMEFORMAT optimize this with an interface
	{
		String pattern = null;
		String originalPattern = getPattern();
		Format format = getFormat();
		JRExpression valueExpression = getExpression();
		if (format != null && valueExpression != null)
		{
			Class valueClass = valueExpression.getValueClass();
			if (java.util.Date.class.isAssignableFrom(valueClass))
			{
				if (format instanceof SimpleDateFormat)
				{
					pattern = ((SimpleDateFormat) format).toPattern();
				}
			}
			else if (Number.class.isAssignableFrom(valueClass))
			{
				if (format instanceof DecimalFormat)
				{
					pattern = ((DecimalFormat) format).toPattern();
				}
			}
		}
		
		if (pattern == null)//fallback to the original pattern
		{
			pattern = originalPattern;
		}
		
		return pattern;		
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
		visitor.visitTextField(this);
	}
	

	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateText(evaluation);

		chopTextElement(0);

		copy((JRPrintText) element);
	}


	public int getBookmarkLevel()
	{
		return ((JRTextField)parent).getBookmarkLevel();
	}


	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillTextField(this, factory);
	}
	
	protected void collectDelayedEvaluations()
	{
		collectDelayedEvaluations(getExpression());
		collectDelayedEvaluations(getAnchorNameExpression());
		collectDelayedEvaluations(getHyperlinkReferenceExpression());
		collectDelayedEvaluations(getHyperlinkAnchorExpression());
		collectDelayedEvaluations(getHyperlinkPageExpression());	
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return ((JRTextField) parent).getHyperlinkParameters();
	}


	public String getLinkType()
	{
		return ((JRTextField) parent).getLinkType();
	}


	public JRExpression getHyperlinkTooltipExpression()
	{
		return ((JRTextField) parent).getHyperlinkTooltipExpression();
	}


	protected boolean canOverflow()
	{
		return isStretchWithOverflow()
				&& getRotation() == ROTATION_NONE
				&& isEvaluateNow()
				&& filler.isBandOverFlowAllowed();
	}
	
}
