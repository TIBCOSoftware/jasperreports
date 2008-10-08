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
package net.sf.jasperreports.compilers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRStringUtil;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

/**
 * JavaScript expression evaluator.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JavaScriptEvaluator extends JREvaluator
{
	
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
	
	protected static JavaScriptCompileData.Expression createJSExpression(JRExpression expression)
	{
		StringBuilder defaultExpr = new StringBuilder();
		StringBuilder oldExpr = new StringBuilder();
		StringBuilder estimatedExpr = new StringBuilder();
		
		JRExpressionChunk[] chunks = expression.getChunks();
		if (chunks == null)
		{
			defaultExpr.append("null");
			oldExpr.append("null");
			estimatedExpr.append("null");
		}
		else
		{
			for (int i = 0; i < chunks.length; i++)
			{
				JRExpressionChunk chunk = chunks[i];
				switch (chunk.getType())
				{
				case JRExpressionChunk.TYPE_TEXT:
					defaultExpr.append(chunk.getText());
					oldExpr.append(chunk.getText());
					estimatedExpr.append(chunk.getText());
					break;
				case JRExpressionChunk.TYPE_PARAMETER:
					String paramName = getParameterVar(chunk.getText());
					defaultExpr.append(paramName + ".getValue()");
					oldExpr.append(paramName + ".getValue()");
					estimatedExpr.append(paramName + ".getValue()");
					break;
				case JRExpressionChunk.TYPE_VARIABLE:
					String varName = getVariableVar(chunk.getText());
					defaultExpr.append(varName + ".getValue()");
					oldExpr.append(varName + ".getOldValue()");
					estimatedExpr.append(varName + ".getEstimatedValue()");
					break;
				case JRExpressionChunk.TYPE_FIELD:
					String fieldName = getFieldVar(chunk.getText());
					defaultExpr.append(fieldName + ".getValue()");
					oldExpr.append(fieldName + ".getOldValue()");
					estimatedExpr.append(fieldName + ".getValue()");
					break;
				}
			}
		}
		
		return new JavaScriptCompileData.Expression(expression.getValueClassName(),
				defaultExpr.toString(), estimatedExpr.toString(), oldExpr.toString());
	}

	protected static String getParameterVar(String name)
	{
		return "param_" + JRStringUtil.getLiteral(name);
	}

	protected static String getVariableVar(String name)
	{
		return "var_" + JRStringUtil.getLiteral(name);
	}

	protected static String getFieldVar(String name)
	{
		return "field_" + JRStringUtil.getLiteral(name);
	}
	
	private final JavaScriptCompileData compileData;
	private Context context;
	private ScriptableObject scope;
	private Map loadedTypes = new HashMap();
	private Map compiledExpressions = new HashMap();

	/**
	 * Create a JavaScript expression evaluator.
	 * 
	 * @param compileData the report compile data
	 */
	public JavaScriptEvaluator(JavaScriptCompileData compileData)
	{
		this.compileData = compileData;
	}

	protected void customizedInit(Map parametersMap, Map fieldsMap,
			Map variablesMap) throws JRException
	{
		context = ContextFactory.getGlobal().enterContext();
		context.getWrapFactory().setJavaPrimitiveWrap(false);
		scope = context.initStandardObjects();
		
		for (Iterator it = parametersMap.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			String name = (String) entry.getKey();
			JRFillParameter param = (JRFillParameter) entry.getValue();
			JSParameter jsParam = new JSParameter(param, scope);
			scope.put(getParameterVar(name), scope, jsParam);
		}

		for (Iterator it = variablesMap.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			String name = (String) entry.getKey();
			JRFillVariable var = (JRFillVariable) entry.getValue();
			JSVariable jsVar = new JSVariable(var, scope);
			scope.put(getVariableVar(name), scope, jsVar);
		}

		if (fieldsMap != null)
		{
			for (Iterator it = fieldsMap.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();
				String name = (String) entry.getKey();
				JRFillField field = (JRFillField) entry.getValue();
				JSField jsField = new JSField(field, scope);
				scope.put(getFieldVar(name), scope, jsField);
			}
		}
	}
	
	protected Object evaluate(int id) throws Throwable
	{
		JavaScriptCompileData.Expression expression = getExpression(id);
		return evaluateExpression(expression.getJavaType(), 
				expression.getDefaultExpression());
	}

	protected Object evaluateEstimated(int id) throws Throwable
	{
		JavaScriptCompileData.Expression expression = getExpression(id);
		return evaluateExpression(expression.getJavaType(), 
				expression.getEstimatedExpression());
	}

	protected Object evaluateOld(int id) throws Throwable
	{
		JavaScriptCompileData.Expression expression = getExpression(id);
		return evaluateExpression(expression.getJavaType(), 
				expression.getOldExpression());
	}

	protected JavaScriptCompileData.Expression getExpression(int id)
	{
		return compileData.getExpression(id);
	}
	
	protected Object evaluateExpression(String type, String expression)
	{
		Script compiledExpression = getCompiledExpression(expression);
		Object value = compiledExpression.exec(context, scope);
		
		Class typeClass = getTypeClass(type);
		Object javaValue;
		try
		{
			javaValue = Context.jsToJava(value, typeClass);
		}
		catch (EvaluatorException e)
		{
			throw new JRRuntimeException(e);
		}
		return javaValue;
	}
	
	protected Script getCompiledExpression(String expression)
	{
		Script compiledExpression = (Script) compiledExpressions.get(expression);
		if (compiledExpression == null)
		{
			compiledExpression = context.compileString(expression, "expression", 0, null);
			compiledExpressions.put(expression, compiledExpression);
		}
		return compiledExpression;
	}
	
	protected Class getTypeClass(String type)
	{
		Class typeClass = (Class) loadedTypes.get(type);
		if (typeClass == null)
		{
			try
			{
				typeClass = JRClassLoader.loadClassForName(type);
			}
			catch (ClassNotFoundException e)
			{
				throw new JRRuntimeException("Unable to load class " + type, e);
			}
			loadedTypes.put(type, typeClass);
		}
		return typeClass;
	}
}
