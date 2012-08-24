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
package net.sf.jasperreports.components.headertoolbar;

import java.awt.Color;

import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingCommand;
import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingData;
import net.sf.jasperreports.components.headertoolbar.actions.FormatCondition;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.style.StyleProvider;
import net.sf.jasperreports.engine.style.StyleProviderContext;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: HeaderToolbarParameterContributor.java 5349 2012-05-08 14:25:05Z teodord $
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
				if (cfd.getConditions().size() > 0) {
					SortFieldTypeEnum columnType = SortFieldTypeEnum.getByName(cfd.getColumnType());
					Object compareTo = columnType.equals(SortFieldTypeEnum.FIELD) ? context.getFieldValue(cfd.getFieldOrVariableName(), evaluation) : context.getVariableValue(cfd.getFieldOrVariableName(), evaluation);
					for (FormatCondition condition: cfd.getConditions()) 
					{
						if(condition.matches(compareTo, cfd.getConditionType(), cfd.getConditionPattern(), condition.getConditionTypeOperator())) 
						{
							if (style == null) 
							{
								style = new JRBaseStyle();
							}
							
							if (condition.isConditionFontBold() != null) 
							{
								style.setBold(condition.isConditionFontBold());
							}
							if (condition.isConditionFontItalic() != null)
							{
								style.setItalic(condition.isConditionFontItalic());
							}
							if (condition.isConditionFontUnderline() != null)
							{
								style.setUnderline(condition.isConditionFontUnderline());
							}
							if (condition.getConditionFontColor() != null) 
							{
								style.setForecolor(JRColorUtil.getColor("#" + condition.getConditionFontColor(), Color.black));
							}
							if (condition.getConditionFontBackColor() != null) 
							{
								style.setBackcolor(JRColorUtil.getColor("#" + condition.getConditionFontBackColor(), Color.white));
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
