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

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


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
		
		JRExpression expression = getExpression();
		if (expression != null)
		{
			Class expressionClass = expression.getValueClass();
			if (
				java.util.Date.class.isAssignableFrom(expressionClass)
				|| java.lang.Number.class.isAssignableFrom(expressionClass)
				)
			{
				filler.formattedTextFields.add(this);
			}
		}
		
		evaluationGroup = factory.getGroup(textField.getEvaluationGroup());
	}

	
	protected JRFillTextField(JRFillTextField textField, JRFillCloneFactory factory)
	{
		super(textField, factory);

		this.format = textField.format;
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
		

	/**
	 *
	 */
	protected JRTemplateText getJRTemplateText()
	{
		JRStyle style = getStyle();
		JRTemplateText template = (JRTemplateText) getTemplate(style);
		if (template == null)
		{
			template = new JRTemplateText(filler.getJasperPrint().getDefaultStyleProvider(), this);
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
		if (getEvaluationTime() == JRExpression.EVALUATION_TIME_AUTO && !delayedEvaluationsInitialized())
		{
			initDelayedEvaluations();
			collectDelayedEvaluations();
		}
		
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
			if (format != null)
			{
				textFieldValue = format.format(textFieldValue);
			}
		}

/*
		String newRawText = 
			JRStringUtil.treatNewLineChars(
				String.valueOf(textFieldValue)
				);
*/		
		String newRawText = String.valueOf(textFieldValue);
		String oldRawText = getRawText();

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
		if (evaluationType == JRExpression.EVALUATION_TIME_AUTO)
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

		if (evaluationType == JRExpression.EVALUATION_TIME_NOW)
		{
			copy(text);
		}
		else if (evaluationType == JRExpression.EVALUATION_TIME_AUTO)
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

		text.setText(textChopper.chop(this, getTextStart(), getTextEnd()));

		text.setAnchorName(getAnchorName());
		text.setHyperlinkReference(getHyperlinkReference());
		text.setHyperlinkAnchor(getHyperlinkAnchor());
		text.setHyperlinkPage(getHyperlinkPage());
		text.setBookmarkLevel(getBookmarkLevel());
	}
	
	
	/**
	 *
	 */
	protected void setFormat()
	{
		String pattern = getPattern();
		Class expressionClass = getExpression().getValueClass();
		if (java.util.Date.class.isAssignableFrom(expressionClass))
		{
			setDateFormat(pattern);
		}
		else if (java.lang.Number.class.isAssignableFrom(expressionClass))
		{
			if (pattern != null && pattern.trim().length() > 0)
			{
				format = NumberFormat.getNumberInstance(filler.getLocale());
				if (format instanceof DecimalFormat)
				{
					((DecimalFormat)format).applyPattern(pattern);
				}
			}
		}
	}


	protected void setDateFormat(String pattern)
	{
		int[] dateStyle = null;
		int[] timeStyle = null;
		if (pattern != null && pattern.trim().length() > 0)
		{			
			int sepIdx = pattern.indexOf(JRTextField.STANDARD_DATE_FORMAT_SEPARATOR);
			String dateTok = sepIdx < 0 ? pattern : pattern.substring(0, sepIdx);
			dateStyle = getDateStyle(dateTok);
			if (dateStyle != null)
			{
				if (sepIdx >= 0)
				{
					String timeTok = pattern.substring(sepIdx + JRTextField.STANDARD_DATE_FORMAT_SEPARATOR.length());
					timeStyle = getDateStyle(timeTok);
				}
				else
				{
					timeStyle = dateStyle;
				}
			}
		}
		
		if (dateStyle != null && timeStyle != null)
		{
			format = getDateFormat(dateStyle, timeStyle, filler.getLocale());
		}
		else
		{			
			format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, filler.getLocale());
			if (
				pattern != null && pattern.trim().length() > 0
				&& format instanceof SimpleDateFormat
				)
			{
				((SimpleDateFormat)format).applyPattern(pattern);
			}
		}
	}

	
	protected static int[] getDateStyle(String pattern)
	{
		if (pattern.equalsIgnoreCase(JRTextField.STANDARD_DATE_FORMAT_DEFAULT))
		{
			return new int[]{DateFormat.DEFAULT};
		}
		else if (pattern.equalsIgnoreCase(JRTextField.STANDARD_DATE_FORMAT_SHORT))
		{
			return new int[]{DateFormat.SHORT};
		}
		else if (pattern.equalsIgnoreCase(JRTextField.STANDARD_DATE_FORMAT_MEDIUM))
		{
			return new int[]{DateFormat.MEDIUM};
		}
		else if (pattern.equalsIgnoreCase(JRTextField.STANDARD_DATE_FORMAT_LONG))
		{
			return new int[]{DateFormat.LONG};
		}
		else if (pattern.equalsIgnoreCase(JRTextField.STANDARD_DATE_FORMAT_FULL))
		{
			return new int[]{DateFormat.FULL};
		}
		else if (pattern.equalsIgnoreCase(JRTextField.STANDARD_DATE_FORMAT_HIDE))
		{
			return new int[0];
		}
		else
		{
			return null;
		}
	}

	
	protected static DateFormat getDateFormat(int[] dateStyle, int[] timeStyle, Locale locale)
	{
		if (dateStyle.length == 0)
		{
			if (timeStyle.length == 0)
			{
				return new SimpleDateFormat("");
			}

			return DateFormat.getTimeInstance(timeStyle[0], locale);
		}

		if (timeStyle.length == 0)
		{
			return DateFormat.getDateInstance(dateStyle[0], locale);
		}

		return DateFormat.getDateTimeInstance(dateStyle[0], timeStyle[0], locale);
	}

	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getTextField(this);
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
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeTextField(this);
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


	public JRCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillTextField(this, factory);
	}
	
	private void collectDelayedEvaluations()
	{
		collectDelayedEvaluations(getExpression());
		collectDelayedEvaluations(getAnchorNameExpression());
		collectDelayedEvaluations(getHyperlinkReferenceExpression());
		collectDelayedEvaluations(getHyperlinkAnchorExpression());
		collectDelayedEvaluations(getHyperlinkPageExpression());	
	}
}
