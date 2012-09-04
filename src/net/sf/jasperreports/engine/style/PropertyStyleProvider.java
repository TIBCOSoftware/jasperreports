package net.sf.jasperreports.engine.style;
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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREvaluation;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: HeaderToolbarParameterContributor.java 5349 2012-05-08 14:25:05Z teodord $
 */
public class PropertyStyleProvider implements StyleProvider
{
	public static final String STYLE_PROPERTY_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "style.";
	public static final String STYLE_PROPERTY_MODE = STYLE_PROPERTY_PREFIX + JRXmlConstants.ATTRIBUTE_mode;
	public static final String STYLE_PROPERTY_BACKCOLOR = STYLE_PROPERTY_PREFIX + JRXmlConstants.ATTRIBUTE_backcolor;
	public static final String STYLE_PROPERTY_FORECOLOR = STYLE_PROPERTY_PREFIX + JRXmlConstants.ATTRIBUTE_forecolor;

	private final StyleProviderContext context;
	
	private final Map<String, JRPropertyExpression> stylePropertyExpressions;
	private final String[] fields;
	private final String[] variables;
	private final boolean lateEvaluated;
	
	public PropertyStyleProvider(
		StyleProviderContext context, 
		Map<String, JRPropertyExpression> stylePropertyExpressions
		)
	{
		this.context = context;
		this.stylePropertyExpressions = stylePropertyExpressions;
		
		List<String> fieldsList = new ArrayList<String>();
		List<String> variablesList = new ArrayList<String>();
		
		if (stylePropertyExpressions != null)
		{
			for(JRPropertyExpression stylePropertyExpression : stylePropertyExpressions.values())
			{
				JRExpression expression = stylePropertyExpression.getValueExpression();
				if (expression != null)
				{
					JRExpressionChunk[] chunks = expression.getChunks();
					if (chunks != null)
					{
						for (int i = 0; i < chunks.length; i++)
						{
							JRExpressionChunk chunk = chunks[i];
							switch (chunk.getType())
							{
								case JRExpressionChunk.TYPE_FIELD:
								{
									fieldsList.add(chunk.getText());
									break;
								}
								case JRExpressionChunk.TYPE_VARIABLE:
								{
									variablesList.add(chunk.getText());
									break;
								}
							}
						}
					}
				}
			}
		}
		
		fields = fieldsList.size() > 0 ? (String[]) fieldsList.toArray(new String[fieldsList.size()]) : null;
		variables = variablesList.size() > 0 ? (String[]) variablesList.toArray(new String[variablesList.size()]) : null;

		JRElement element = context.getElement();
		JREvaluation evaluation = element instanceof JREvaluation ? (JREvaluation)element : null;
		lateEvaluated = evaluation != null && evaluation.getEvaluationTimeValue() != EvaluationTimeEnum.NOW;
	}

	@Override
	public JRStyle getStyle(byte evaluation) 
	{
		JRStyle style = new JRBaseStyle();
		
		String mode = getPropertyValue(STYLE_PROPERTY_MODE, evaluation);
		if (mode != null)
		{
			style.setMode(ModeEnum.getByName(mode));
		}

		String backcolor = getPropertyValue(STYLE_PROPERTY_BACKCOLOR, evaluation);
		if (backcolor != null)
		{
			style.setBackcolor(JRColorUtil.getColor(backcolor, null));
		}

		String forecolor = getPropertyValue(STYLE_PROPERTY_FORECOLOR, evaluation);
		if (forecolor != null)
		{
			style.setForecolor(JRColorUtil.getColor(forecolor, null));
		}

		return style;
	}

	@Override
	public String[] getFields() 
	{
		return fields;
	}

	@Override
	public String[] getVariables() 
	{
		return variables;
	}

	private String getPropertyValue(String propertyName, byte evaluation) 
	{
		String value = null;

		if (lateEvaluated && stylePropertyExpressions.containsKey(propertyName))
		{
			JRPropertyExpression stylePropertyExpression = stylePropertyExpressions.get(propertyName);
			JRExpression expression = stylePropertyExpression.getValueExpression();
			if (expression != null)
			{
				value = (String) context.evaluateExpression(expression, evaluation);
			}
		}
		else
		{
			value = context.getElement().getPropertiesMap().getProperty(propertyName);
		}
		
		return value;
	}

}
