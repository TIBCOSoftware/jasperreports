/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.compilers;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.ExpressionValues;
import net.sf.jasperreports.engine.fill.FillExpressionDefaultValues;
import net.sf.jasperreports.engine.fill.FillExpressionEstimatedValues;
import net.sf.jasperreports.engine.fill.FillExpressionOldValues;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.fill.SimpleTextExpressionEvaluator;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class StandardExpressionEvaluators implements DirectExpressionEvaluators
{
	
	private static DirectExpressionEvaluator NULL_PLACEHOLDER = new UniformExpressionEvaluator()
	{
		@Override
		protected Object defaultEvaluate()
		{
			throw new UnsupportedOperationException();
		}
	};
	
	private Map<Integer, DirectExpressionEvaluation> directEvaluations;
	private DirectExpressionValueFilter valueFilter;
	
	private Map<Integer, DirectExpressionEvaluator> evaluators;
	
	private JREvaluator evaluator;
	private Map<String, JRFillParameter> parametersMap;
	private Map<String, JRFillField> fieldsMap;
	private Map<String, JRFillVariable> variablesMap;
	
	private ExpressionValues defaultValues;
	private ExpressionValues oldValues;
	private ExpressionValues estimatedValues;

	public StandardExpressionEvaluators(Map<Integer, DirectExpressionEvaluation> directEvaluations,
			DirectExpressionValueFilter valueFilter)
	{
		this.directEvaluations = directEvaluations;
		this.valueFilter = valueFilter;
		
		this.evaluators = new HashMap<>();
	}
	
	@Override
	public void init(JREvaluator evaluator, Map<String, JRFillParameter> parametersMap,
			Map<String, JRFillField> fieldsMap, Map<String, JRFillVariable> variablesMap)
	{
		this.evaluator = evaluator;
		this.parametersMap = parametersMap;
		this.fieldsMap = fieldsMap;
		this.variablesMap = variablesMap;
		
		this.defaultValues = new FillExpressionDefaultValues(evaluator, parametersMap, fieldsMap, variablesMap);
		this.oldValues =  new FillExpressionOldValues(evaluator, parametersMap, fieldsMap, variablesMap);
		this.estimatedValues = new FillExpressionEstimatedValues(evaluator, parametersMap, fieldsMap, variablesMap);
		
		this.evaluators.clear();
	}

	@Override
	public DirectExpressionEvaluator getEvaluator(JRExpression expression)
	{
		int expressionId = expression.getId();
		DirectExpressionEvaluator evaluator = evaluators.get(expressionId);
		if (evaluator == null)
		{
			DirectExpressionEvaluation directEvaluation = directEvaluation(expression.getId());
			if (directEvaluation == null)
			{
				evaluator = NULL_PLACEHOLDER;
			}
			else
			{
				evaluator = createDirectEvaluator(directEvaluation);
			}
			evaluators.put(expressionId, evaluator);
		}
		return evaluator == NULL_PLACEHOLDER ? null : evaluator;
	}
	
	protected DirectExpressionEvaluation directEvaluation(int expressionId)
	{
		DirectExpressionEvaluation directEvaluation = directEvaluations == null ? null 
				: directEvaluations.get(expressionId);
		return directEvaluation;
	}

	protected DirectExpressionEvaluator createDirectEvaluator(DirectExpressionEvaluation evaluation)
	{
		DirectExpressionEvaluator evaluator;
		switch (evaluation.getType())
		{
		case CONSTANT:
			evaluator = new ConstantEvaluator(((ConstantExpressionEvaluation) evaluation).getValue());
			break;
		case SIMPLE_TEXT:
			evaluator = new SimpleTextEvaluator(((SimpleTextEvaluation) evaluation).getChunks());
			break;
		case PARAMETER:
			String parameterName = ((ParameterEvaluation) evaluation).getName();
			JRFillParameter parameter = parametersMap.get(parameterName);
			if (parameter == null)
			{
				//shout not happen
				throw new JRRuntimeException("Did not find parameter " + parameterName);
			}
			evaluator = new ParameterEvaluator(parameter);
			break;
		case FIELD:
			String fieldName = ((FieldEvaluation) evaluation).getName();
			JRFillField field = fieldsMap.get(fieldName);
			if (field == null)
			{
				//shout not happen
				throw new JRRuntimeException("Did not find field " + fieldName);
			}
			evaluator = new FieldEvaluator(field);
			break;
		case VARIABLE:
			String variableName = ((VariableEvaluation) evaluation).getName();
			JRFillVariable variable = variablesMap.get(variableName);
			if (variable == null)
			{
				//shout not happen
				throw new JRRuntimeException("Did not find variable " + variableName);
			}
			evaluator = new VariableEvaluator(variable);
			break;
		case RESOURCE:
			evaluator = new ResourceEvaluator(((ResourceEvaluation) evaluation).getMessageKey());
			break;
		default:
			//should not happen
			throw new JRRuntimeException("Unknown direct expression evaluation type " + evaluation.getType());
		}
		return evaluator;
	}
	
	protected Object filterValue(Object value, Class<?> expectedType)
	{
		return valueFilter == null ? value : valueFilter.filterValue(value, expectedType);
	}
	
	protected class ConstantEvaluator extends UniformExpressionEvaluator
	{
		private Object value;
		
		public ConstantEvaluator(Object value)
		{
			this.value = value;
		}

		@Override
		protected Object defaultEvaluate()
		{
			return filterValue(value, null);
		}
	}
	
	protected class SimpleTextEvaluator implements DirectExpressionEvaluator
	{
		private JRExpressionChunk[] chunks;
		
		public SimpleTextEvaluator(JRExpressionChunk[] chunks)
		{
			this.chunks = chunks;
		}

		@Override
		public Object evaluate()
		{
			return SimpleTextExpressionEvaluator.evaluate(chunks, defaultValues);
		}

		@Override
		public Object evaluateOld()
		{
			return SimpleTextExpressionEvaluator.evaluate(chunks, oldValues);
		}

		@Override
		public Object evaluateEstimated()
		{
			return SimpleTextExpressionEvaluator.evaluate(chunks, estimatedValues);
		}	
	}
	
	protected class ParameterEvaluator extends UniformExpressionEvaluator
	{
		private JRFillParameter parameter;
		
		public ParameterEvaluator(JRFillParameter parameter)
		{
			this.parameter = parameter;
		}

		@Override
		protected Object defaultEvaluate()
		{
			return filterValue(parameter.getValue(), parameter.getValueClass());
		}
	}
	
	protected class FieldEvaluator implements DirectExpressionEvaluator
	{
		private JRFillField field;
		
		public FieldEvaluator(JRFillField field)
		{
			this.field = field;
		}

		@Override
		public Object evaluate()
		{
			return filterValue(field.getValue(), field.getValueClass());
		}

		@Override
		public Object evaluateOld()
		{
			return filterValue(field.getOldValue(), field.getValueClass());
		}

		@Override
		public Object evaluateEstimated()
		{
			return filterValue(field.getValue(), field.getValueClass());
		}
	}
	
	protected class VariableEvaluator implements DirectExpressionEvaluator
	{
		private JRFillVariable variable;
		
		public VariableEvaluator(JRFillVariable variable)
		{
			this.variable = variable;
		}

		@Override
		public Object evaluate()
		{
			return filterValue(variable.getValue(), variable.getValueClass());
		}

		@Override
		public Object evaluateOld()
		{
			return filterValue(variable.getOldValue(), variable.getValueClass());
		}

		@Override
		public Object evaluateEstimated()
		{
			return filterValue(variable.getEstimatedValue(), variable.getValueClass());
		}
	}
	
	protected class ResourceEvaluator extends UniformExpressionEvaluator
	{
		private String messageKey;
		
		public ResourceEvaluator(String messageKey)
		{
			this.messageKey = messageKey;
		}

		@Override
		protected Object defaultEvaluate()
		{
			return filterValue(evaluator.str(messageKey), null);
		}
	}

}
