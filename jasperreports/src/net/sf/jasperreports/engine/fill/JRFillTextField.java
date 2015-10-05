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

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.Pair;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRFillTextField extends JRFillTextElement implements JRTextField
{
	
	protected static final Log log = LogFactory.getLog(JRFillTextField.class);

	protected final Map<Pair<JRStyle, TextFormat>, JRTemplateElement> textTemplates;
	
	/**
	 *
	 */
	private JRGroup evaluationGroup;

	/**
	 *
	 */
	private Object value;

	/**
	 *
	 */
	private TextFormat textFormat;

	/**
	 * 
	 */
	private String pattern;

	/**
	 *
	 */
	private String anchorName;
	private String hyperlinkReference;
	private Boolean hyperlinkWhen;
	private String hyperlinkAnchor;
	private Integer hyperlinkPage;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;
	
	private static final String NULL_VALUE = new String();
	private final Map<String, String> localizedProperties;
	
	//FIXME keep these in the filler/context
	private Map<String, TimeZone> generalPatternTimeZones = new HashMap<String, TimeZone>();

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
		
		this.textTemplates = new HashMap<Pair<JRStyle,TextFormat>, JRTemplateElement>();
		evaluationGroup = factory.getGroup(textField.getEvaluationGroup());
		this.localizedProperties = new HashMap<String, String>();
	}

	
	protected JRFillTextField(JRFillTextField textField, JRFillCloneFactory factory)
	{
		super(textField, factory);

		this.textTemplates = textField.textTemplates;
		this.evaluationGroup = textField.evaluationGroup;
		this.localizedProperties = textField.localizedProperties;
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
	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return ((JRTextField)parent).getEvaluationTimeValue();
	}
		
	/**
	 *
	 */
	protected TextFormat getTextFormat()
	{
		return textFormat;
	}

	/**
	 *
	 */
	public String getPattern()
	{
		if (getPatternExpression() == null)
		{
			return JRStyleResolver.getPattern(this);
		}
		return pattern;
	}
	
	protected String getDatePattern(Object value)
	{
		String pattern = getPattern();
		if (pattern != null || value == null)
		{
			return pattern;
		}
		
		String property;
		if (value instanceof java.sql.Date)
		{
			property = JRTextField.PROPERTY_PATTERN_DATE;
		}
		else if (value instanceof java.sql.Time)
		{
			property = JRTextField.PROPERTY_PATTERN_TIME;
		}
		else 
		{
			property = JRTextField.PROPERTY_PATTERN_DATETIME;
		}
		return getLocalizedProperty(property);
	}
	
	protected String getNumberPattern(Object value)
	{
		String pattern = getPattern();
		if (pattern != null || value == null)
		{
			return pattern;
		}
		
		String property;
		if (value instanceof java.lang.Byte
				|| value instanceof java.lang.Short
				|| value instanceof java.lang.Integer
				|| value instanceof java.lang.Long
				|| value instanceof java.math.BigInteger)
		{
			property = JRTextField.PROPERTY_PATTERN_INTEGER;
		}
		else 
		{
			property = JRTextField.PROPERTY_PATTERN_NUMBER;
		}
		return getLocalizedProperty(property);
	}
	
	protected String getLocalizedProperty(String property)
	{
		// caching locally because it's not cached in JRPropertiesUtil
		String value = localizedProperties.get(property);
		if (value == null)
		{
			value = filler.getPropertiesUtil().getLocalizedProperty(property, filler.getLocale());
			localizedProperties.put(property, value == null ? NULL_VALUE : value);
		}
		else if (value == NULL_VALUE)
		{
			value = null;
		}
		return value;
	}
		
	public String getOwnPattern()
	{
		return providerStyle == null || providerStyle.getOwnPattern() == null ? ((JRTextField)this.parent).getOwnPattern() : providerStyle.getOwnPattern();
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
		return providerStyle == null || providerStyle.isOwnBlankWhenNull() == null ? ((JRTextField)this.parent).isOwnBlankWhenNull() : providerStyle.isOwnBlankWhenNull();
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
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}

	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return ((JRTextField)parent).getHyperlinkTypeValue();
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
	public String getLinkTarget()
	{
		return ((JRTextField)parent).getLinkTarget();
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
	public JRExpression getPatternExpression()
	{
		return ((JRTextField)parent).getPatternExpression();
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
	public JRExpression getHyperlinkWhenExpression()
	{
		return ((JRTextField)parent).getHyperlinkWhenExpression();
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
	protected Object getValue()
	{
		return value;
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
		return (JRTemplateText) getElementTemplate();
	}


	protected JRTemplateElement createElementTemplate()
	{
		JRTemplateText template = 
			new JRTemplateText(
				getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider(), 
				this
				);
		template.copyParagraph(getPrintParagraph());
		template.copyLineBox(getPrintLineBox());
		template.setTextFormat(textFormat);
		return template;
	}


	protected void evaluateTextFormat(Format format, Object value, TimeZone ownTimeZone)
	{
		if (value != null)
//		if (getExpression() != null)
		{
			if (value instanceof String)
			{
				textFormat = null;
			}
			else
			{
				SimpleTextFormat simpleTextFormat = new SimpleTextFormat();
				
				simpleTextFormat.setValueClassName(value.getClass().getName());

				String pattern = getTemplatePattern(format, value);
				if (pattern != null)
				{
					simpleTextFormat.setPattern(pattern);
				}
				
				if (!filler.hasMasterFormatFactory())
				{
					simpleTextFormat.setFormatFactoryClass(filler.getFormatFactory().getClass().getName());
				}
				
				if (!filler.hasMasterLocale())
				{
					simpleTextFormat.setLocaleCode(JRDataUtils.getLocaleCode(filler.getLocale()));
				}

				if (value instanceof java.util.Date)
				{
					// the element's format timezone property has precedence over the report timezone
					TimeZone formatTimeZone = ownTimeZone == null ? filler.getTimeZone() : ownTimeZone;
					// check if the current format timezone differs from the master report timezone
					if (!formatTimeZone.equals(filler.fillContext.getMasterTimeZone()))
					{
						simpleTextFormat.setTimeZoneId(JRDataUtils.getTimeZoneId(formatTimeZone));
					}
				}
				
				textFormat = simpleTextFormat;
			}
		}
	}

	@Override
	protected JRTemplateElement getTemplate(JRStyle style)
	{
		Pair<JRStyle, TextFormat> key = new Pair<JRStyle, TextFormat>(style, textFormat);
		return textTemplates.get(key);
	}

	@Override
	protected void registerTemplate(JRStyle style, JRTemplateElement template)
	{
		Pair<JRStyle, TextFormat> key = new Pair<JRStyle, TextFormat>(style, textFormat);
		textTemplates.put(key, template);
		
		if (log.isDebugEnabled())
		{
			log.debug("created " + template + " for " + key);
		}
	}

	@Override
	protected boolean delayedEvaluationUpdatesTemplate()
	{
		// since the text format is evaluated during the delayed evaluation, 
		// we need to always attempt to update the template.
		// we could test whether the value is String, but there might be some exotic
		// cases in which the same text field is used for both Strings and numbers.
		return true;
	}


	protected TimeZone toFormatTimeZone(String timezoneId)
	{
		JRFillDataset dataset = expressionEvaluator.getFillDataset();
		// not sure whether the dataset can be null, let's be safe
		TimeZone reportTimeZone = dataset == null ? filler.getTimeZone() : dataset.timeZone;
		
		return JRDataUtils.resolveFormatTimeZone(timezoneId, reportTimeZone);
	}


	/**
	 *
	 */
	public void evaluate(
		byte evaluation
		) throws JRException
	{
		initDelayedEvaluations();
		
		reset();
		
		evaluatePrintWhenExpression(evaluation);

		if (isPrintWhenExpressionNull() || isPrintWhenTrue())
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
		evaluateStyle(evaluation);
		
		value = evaluateExpression(getExpression(), evaluation);
		
		String strValue = null;

		pattern = (String) evaluateExpression(getPatternExpression(), evaluation);

		if (value == null)
		{
			if (isBlankWhenNull())
			{
				strValue = "";
			}
			else
			{
				strValue = null;
			}
		}
		else
		{
			TimeZone ownTimeZone = determineOwnTimeZone();
			Format format = getFormat(value, ownTimeZone);

			evaluateTextFormat(format, value, ownTimeZone);

			if (format == null)
			{
				strValue = value.toString();
			}
			else
			{
				strValue = format.format(value);
				
				if (value instanceof java.util.Date && log.isDebugEnabled())
				{
					log.debug(getUUID() + ": formatted value " + value 
							+ " (" + value.getClass().getName() + "/" + ((java.util.Date) value).getTime() + ")"
							+ " to " + strValue);
				}
			}
		}

		String oldRawText = getRawText();
		String newRawText = processMarkupText(String.valueOf(strValue));

		setRawText(newRawText);
		resetTextChunk();

		setValueRepeating(
				(oldRawText == null && newRawText == null) ||
				(oldRawText != null && oldRawText.equals(newRawText))
			);

		anchorName = (String) evaluateExpression(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) evaluateExpression(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkWhen = (Boolean) evaluateExpression(getHyperlinkWhenExpression(), evaluation);
		hyperlinkAnchor = (String) evaluateExpression(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) evaluateExpression(getHyperlinkPageExpression(), evaluation);
		hyperlinkTooltip = (String) evaluateExpression(getHyperlinkTooltipExpression(), evaluation);
		hyperlinkParameters = JRFillHyperlinkHelper.evaluateHyperlinkParameters(this, expressionEvaluator, evaluation);
	}


	protected TimeZone determineOwnTimeZone()
	{
		TimeZone ownTimeZone = null;
		if (value instanceof java.util.Date)
		{
			// read the element's format timezone property
			String ownTimezoneId = hasProperties() ? getPropertiesMap().getProperty(PROPERTY_FORMAT_TIMEZONE) : null;
			ownTimeZone = toFormatTimeZone(ownTimezoneId);
			
			if (ownTimeZone == null)
			{
				// trying to get a timezone for the specific date/time type.
				// should we have timezones for arbitrary types a la oracle.sql.DATE?
				if (value instanceof java.sql.Date)
				{
					ownTimeZone = getPatternTimeZone(PROPERTY_SQL_DATE_FORMAT_TIMEZONE);
				}
				else if (value instanceof java.sql.Timestamp)
				{
					ownTimeZone = getPatternTimeZone(PROPERTY_SQL_TIMESTAMP_FORMAT_TIMEZONE);
				}
				else if (value instanceof java.sql.Time)
				{
					ownTimeZone = getPatternTimeZone(PROPERTY_SQL_TIME_FORMAT_TIMEZONE);
				}
			}
			
			if (ownTimeZone == null)
			{
				// using a general timezone
				ownTimeZone = getPatternTimeZone(PROPERTY_FORMAT_TIMEZONE);
			}
		}
		return ownTimeZone;
	}


	protected TimeZone getPatternTimeZone(String property)
	{
		if (generalPatternTimeZones.containsKey(property))
		{
			return generalPatternTimeZones.get(property);
		}
		
		String propertyVal = filler.propertiesUtil.getProperty(filler.jasperReport, property);
		TimeZone timeZone = toFormatTimeZone(propertyVal);
		generalPatternTimeZones.put(property, timeZone);
		
		if (log.isDebugEnabled())
		{
			log.debug(getUUID() + ": pattern timezone property " + property 
					+ " is " + propertyVal + ", resolved to " + timeZone);
		}
		
		return timeZone;
	}


	/**
	 *
	 */
	public boolean prepare(
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

		if (isEvaluateNow())
		{
			if (isOverflow)
			{
				if (getPositionTypeValue() == PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM)
				{
					// the content of the band bottom text fields is not
					// consumed during overflows, because they only appear on the last overflow
					resetTextChunk();
				}

				if (
					getTextEnd() >= getText().length()
					|| !isStretchWithOverflow()
					|| !getRotationValue().equals(RotationEnum.NONE)
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
							resetTextChunk();

							isReprinted = true;
						}
						else
						{
							isToPrint = false;
						}
					}
//					else
//					{
//						// the text field did not print on the previous page.
//						// we let it go since it is its first time anyway
//					}
				}
//				else
//				{
//					// there is text left inside the stretchable text field.
//					// we simply let it go
//				}

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
				if (availableHeight >= getRelativeY() + getHeight())
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
							&& getRotationValue().equals(RotationEnum.NONE)
							)
						{
							// the text field is allowed to stretch downwards in order to
							// display all its content

							chopTextElement(availableHeight - getRelativeY() - getHeight());
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
				availableHeight < getRelativeY() + getHeight()
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
	public JRPrintElement fill() throws JRException
	{
		EvaluationTimeEnum evaluationTime = getEvaluationTimeValue();
		
		JRTemplatePrintText text;
		JRRecordedValuesPrintText recordedValuesText;
		if (isEvaluateAuto())
		{
			text = recordedValuesText = new JRRecordedValuesPrintText(getJRTemplateText(), printElementOriginator);
		}
		else
		{
			text = new JRTemplatePrintText(getJRTemplateText(), printElementOriginator);
			recordedValuesText = null;
		}
		
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
		text.setBookmarkLevel(getBookmarkLevel());

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
			filler.addBoundElement(this, text, evaluationTime, getEvaluationGroup(), band);
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

		//FIXME do we need to do this when the value is String?
		text.setValue(getValue());
		
		setPrintText(text);

		text.setAnchorName(getAnchorName());
		if (getHyperlinkWhenExpression() == null || hyperlinkWhen == Boolean.TRUE)
		{
			text.setHyperlinkReference(getHyperlinkReference());
			text.setHyperlinkAnchor(getHyperlinkAnchor());
			text.setHyperlinkPage(getHyperlinkPage());
			text.setHyperlinkTooltip(getHyperlinkTooltip());
			text.setHyperlinkParameters(hyperlinkParameters);
		}
		else
		{
			text.setHyperlinkReference(null);
		}
		transferProperties(text);
	}

	@Override
	protected void setPrintText(JRPrintText printText, String text)
	{
		// checking if the text is identical to the one set via setValue.
		// note that we're assuming that this method is called after printText.setValue().
		// JRStyledText no longer creates String copies for simple texts, but keeping this to cover other cases.
		Object printValue = printText.getValue();
		String textObj = text;
		if (text != null && printValue != null && printValue instanceof String
				&& text.equals(printValue))
		{
			textObj = (String) printValue;
		}
		super.setPrintText(printText, textObj);
	}
	
	/**
	 *
	 */
	protected Format getFormat(Object value, TimeZone ownTimeZone)//FIXMEFORMAT optimize this with an interface
	{
		Format format = null;

		if (value instanceof java.util.Date)
		{
			format = filler.getDateFormat(getDatePattern(value), ownTimeZone);
		}
		else if (value instanceof java.lang.Number)
		{
			format = filler.getNumberFormat(getNumberPattern(value));
		}
		
		return format;
	}

	/**
	 *
	 */
	protected String getTemplatePattern(Format format, Object value)//FIXMEFORMAT optimize this with an interface
	{
		String pattern = null;
		String originalPattern;

		if (value instanceof java.util.Date)
		{
			originalPattern = getDatePattern(value);
			if (format instanceof SimpleDateFormat)
			{
				pattern = ((SimpleDateFormat) format).toPattern();
			}
		}
		else if (value instanceof Number)
		{
			originalPattern = getNumberPattern(value);
			if (format instanceof DecimalFormat)
			{
				pattern = ((DecimalFormat) format).toPattern();
			}
		}
		else
		{
			originalPattern = getPattern();
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
		
		//FIXME put this somewhere else, e.g. in ElementEvaluationAction
		filler.updateBookmark(element);
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
		super.collectDelayedEvaluations();
		
		collectDelayedEvaluations(getExpression());
		collectDelayedEvaluations(getPatternExpression());
		collectDelayedEvaluations(getAnchorNameExpression());
		collectDelayedEvaluations(getHyperlinkReferenceExpression());
		collectDelayedEvaluations(getHyperlinkWhenExpression());
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
				&& getRotationValue().equals(RotationEnum.NONE)
				&& isEvaluateNow()
				&& filler.isBandOverFlowAllowed();
	}
	
}
