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
package net.sf.jasperreports.components.headertoolbar;

import java.awt.Color;

import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingCommand;
import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingData;
import net.sf.jasperreports.components.headertoolbar.actions.FormatCondition;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.style.StyleProvider;
import net.sf.jasperreports.engine.style.StyleProviderContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 *
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class HeaderToolbarConditionalStyleProvider implements StyleProvider
{
	private final StyleProviderContext context;
	private JasperReportsContext jasperreportsContext;

	public HeaderToolbarConditionalStyleProvider(StyleProviderContext context, JasperReportsContext jasperreportsContext)
	{
		this.context = context;
		this.jasperreportsContext = jasperreportsContext;
	}

	@Override
	public JRStyle getStyle(byte evaluation) 
	{
		if (context.getElement().getPropertiesMap() != null)
		{
			String srlzdConditionalFormattingData = context.getElement().getPropertiesMap().getProperty(ConditionalFormattingCommand.COLUMN_CONDITIONAL_FORMATTING_PROPERTY);
			if (srlzdConditionalFormattingData != null)
			{
				JRStyle style = null;

				ConditionalFormattingData cfd = JacksonUtil.getInstance(jasperreportsContext).loadObject(srlzdConditionalFormattingData, ConditionalFormattingData.class);
				if (cfd.getConditions().size() > 0)
				{
					Object compareTo;

					if (context.getElement().getPropertiesMap().containsProperty(HeaderToolbarElement.PROPERTY_COLUMN_FIELD)) {
						String fieldName = context.getElement().getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_FIELD);
						compareTo = context.getFieldValue(fieldName, evaluation);
					} else if (context.getElement().getPropertiesMap().containsProperty(HeaderToolbarElement.PROPERTY_COLUMN_VARIABLE)) {
						String variableName = context.getElement().getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_VARIABLE);
						compareTo = context.getVariableValue(variableName, evaluation);
					} else {
						JRExpression expression = context.getElement() instanceof JRTextField ? ((JRTextField)context.getElement()).getExpression() : null;
						compareTo = context.evaluateExpression(expression, evaluation);
					}

					boolean bgColorSet = false;
					boolean fontBoldSet = false;
					boolean fontItalicSet = false;
					boolean fontUnderlineSet = false;
					boolean foreColorSet = false;
					boolean modeSet = false;
					for (FormatCondition condition: cfd.getConditions()) 
					{
						if(
							condition.matches(
								compareTo, 
								cfd.getConditionType(), 
								cfd.getConditionPattern(), 
								condition.getConditionTypeOperator(),
								cfd.getLocaleCode() == null ? context.getLocale() : JRDataUtils.getLocale(cfd.getLocaleCode()),
								cfd.getTimeZoneId() == null ? context.getTimeZone() : JRDataUtils.getTimeZone(cfd.getTimeZoneId())
								)
							) 
						{
							if (style == null) 
							{
								style = new JRBaseStyle();
							}
							
							if (condition.isConditionFontBold() != null && !fontBoldSet) 
							{
								style.setBold(condition.isConditionFontBold());
								fontBoldSet = true;
							}
							if (condition.isConditionFontItalic() != null && !fontItalicSet)
							{
								style.setItalic(condition.isConditionFontItalic());
								fontItalicSet = true;
							}
							if (condition.isConditionFontUnderline() != null && !fontUnderlineSet)
							{
								style.setUnderline(condition.isConditionFontUnderline());
								fontUnderlineSet = true;
							}
							if (condition.getConditionFontColor() != null && !foreColorSet) 
							{
								style.setForecolor(JRColorUtil.getColor("#" + condition.getConditionFontColor(), Color.black));
								foreColorSet = true;
							}
							if (condition.getConditionMode() != null && !modeSet)
							{
								style.setMode(ModeEnum.getByName(condition.getConditionMode()));
								modeSet = true;
							}
							if (condition.getConditionFontBackColor() != null && !bgColorSet) 
							{
								style.setBackcolor(JRColorUtil.getColor("#" + condition.getConditionFontBackColor(), Color.white));
								bgColorSet = true;
							}
						}
					}
				}
				
				return style;
			}
		}
		return null;
	}

	@Override
	public String[] getFields() 
	{
		return null;
	}

	@Override
	public String[] getVariables() 
	{
		return null;
	}

}
