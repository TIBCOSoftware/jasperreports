/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JavaScriptEvaluatorScope
{
	
	protected static final String EVALUATOR_VAR = "_jreval";
	
	/**
	 * Base JavaScript value class.
	 */
	public abstract static class JSValue
	{
		private final ScriptableObject scope;

		protected JSValue(ScriptableObject scope)
		{
			this.scope = scope;
		}
		
		protected final Object toJSValue(Object value)
		{
			return Context.javaToJS(value, scope);
		}
	}
	
	/**
	 * Parameter class used in JavaScript expressions.
	 */
	public static class JSParameter extends JSValue
	{
		private final JRFillParameter parameter;
		
		public JSParameter(JRFillParameter parameter, ScriptableObject scope)
		{
			super(scope);
			this.parameter = parameter;
		}
		
		public Object getValue()
		{
			return toJSValue(parameter.getValue());
		}
	}
	
	/**
	 * Field class used in JavaScript expressions.
	 */
	public static class JSField extends JSValue
	{
		private final JRFillField field;
		
		public JSField(JRFillField field, ScriptableObject scope)
		{
			super(scope);
			this.field = field;
		}
		
		public Object getValue()
		{
			return toJSValue(field.getValue());
		}
		
		public Object getOldValue()
		{
			return toJSValue(field.getOldValue());
		}
	}
	
	/**
	 * Variable class used in JavaScript expressions.
	 */
	public static class JSVariable extends JSValue
	{
		private final JRFillVariable variable;
		
		public JSVariable(JRFillVariable variable, ScriptableObject scope)
		{
			super(scope);
			this.variable = variable;
		}
		
		public Object getValue()
		{
			return toJSValue(variable.getValue());
		}
		
		public Object getOldValue()
		{
			return toJSValue(variable.getOldValue());
		}
		
		public Object getEstimatedValue()
		{
			return toJSValue(variable.getEstimatedValue());
		}
	}
	
	private Context context;
	private ScriptableObject scope;

	public JavaScriptEvaluatorScope(Context context, JREvaluator evaluator)
	{
		this.context = context;
		this.scope = context.initStandardObjects();
		this.scope.put(EVALUATOR_VAR, this.scope, evaluator);
	}
	
	public void init(Map<String, JRFillParameter> parametersMap, 
			Map<String, JRFillField> fieldsMap,
			Map<String, JRFillVariable> variablesMap)
	{
		for (Iterator<Map.Entry<String, JRFillParameter>> it = parametersMap.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<String, JRFillParameter> entry = it.next();
			String name = entry.getKey();
			JRFillParameter param = entry.getValue();
			JSParameter jsParam = new JSParameter(param, scope);
			scope.put(JavaScriptCompiler.getParameterVar(name), scope, jsParam);
		}

		for (Iterator<Map.Entry<String, JRFillVariable>> it = variablesMap.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<String, JRFillVariable> entry = it.next();
			String name = entry.getKey();
			JRFillVariable var = entry.getValue();
			JSVariable jsVar = new JSVariable(var, scope);
			scope.put(JavaScriptCompiler.getVariableVar(name), scope, jsVar);
		}

		if (fieldsMap != null)
		{
			for (Iterator<Map.Entry<String, JRFillField>> it = fieldsMap.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<String, JRFillField> entry = it.next();
				String name = entry.getKey();
				JRFillField field = entry.getValue();
				JSField jsField = new JSField(field, scope);
				scope.put(JavaScriptCompiler.getFieldVar(name), scope, jsField);
			}
		}

	}

	public Object evaluateExpression(Script expression)
	{
		Object value = expression.exec(context, scope);
		
		Object javaValue;
		// not converting Number objects because the generic conversion call below
		// always converts to Double
		if (value == null || value instanceof Number)
		{
			javaValue = value;
		}
		else
		{
			try
			{
				javaValue = Context.jsToJava(value, Object.class);
			}
			catch (EvaluatorException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		return javaValue;
	}
	
	public void setScopeVariable(String name, Object value)
	{
		scope.put(name, scope, value);
	}
}
