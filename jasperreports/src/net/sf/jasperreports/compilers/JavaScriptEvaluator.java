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
package net.sf.jasperreports.compilers;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.util.JRClassLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Script;

/**
 * JavaScript expression evaluator that compiles expressions at fill time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JavaScriptCompiler
 */
public class JavaScriptEvaluator extends JREvaluator
{
	
	/**
	 * Property that determines the optimization level used when compiling expressions.
	 * 
	 * See <a href="http://www-archive.mozilla.org/rhino/apidocs/org/mozilla/javascript/Context.html#setOptimizationLevel%28int%29"/>
	 */
	public static final String PROPERTY_OPTIMIZATION_LEVEL = JRPropertiesUtil.PROPERTY_PREFIX 
			+ "javascript.evaluator.optimization.level";
	
	private static final Log log = LogFactory.getLog(JavaScriptEvaluator.class);
	
	private final JasperReportsContext jrContext;
	private final JavaScriptCompileData compileData;
	private Context context;
	private JavaScriptEvaluatorScope evaluatorScope;
	private Map<String, Class<?>> loadedTypes = new HashMap<String, Class<?>>();
	private Map<String, Script> compiledExpressions = new HashMap<String, Script>();

	/**
	 * Create a JavaScript expression evaluator.
	 * 
	 * @param compileData the report compile data
	 */
	public JavaScriptEvaluator(JasperReportsContext jrContext, JavaScriptCompileData compileData)
	{
		this.jrContext = jrContext;
		this.compileData = compileData;
	}

	protected void customizedInit(
			Map<String, JRFillParameter> parametersMap, 
			Map<String, JRFillField> fieldsMap,
			Map<String, JRFillVariable> variablesMap
			) throws JRException
	{
		context = ContextFactory.getGlobal().enterContext();//TODO exit context
		
		int optimizationLevel = JRPropertiesUtil.getInstance(jrContext).getIntegerProperty(PROPERTY_OPTIMIZATION_LEVEL);
		if (log.isDebugEnabled())
		{
			log.debug("optimization level " + optimizationLevel);
		}
		context.setOptimizationLevel(optimizationLevel);
		
		context.getWrapFactory().setJavaPrimitiveWrap(false);
		
		evaluatorScope = new JavaScriptEvaluatorScope(context);
		evaluatorScope.init(parametersMap, fieldsMap, variablesMap);
	}
	
	protected Object evaluate(int id) throws Throwable //NOSONAR
	{
		JavaScriptCompileData.Expression expression = getExpression(id);
		return evaluateExpression(expression.getDefaultExpression());
	}

	protected Object evaluateEstimated(int id) throws Throwable //NOSONAR
	{
		JavaScriptCompileData.Expression expression = getExpression(id);
		return evaluateExpression(expression.getEstimatedExpression());
	}

	protected Object evaluateOld(int id) throws Throwable //NOSONAR
	{
		JavaScriptCompileData.Expression expression = getExpression(id);
		return evaluateExpression(expression.getOldExpression());
	}

	protected JavaScriptCompileData.Expression getExpression(int id)
	{
		return compileData.getExpression(id);
	}
	
	/**
	 * @deprecated Replaced by {@link #evaluateExpression(String)}.
	 */
	protected Object evaluateExpression(String type, String expression)
	{
		return evaluateExpression(expression);
	}
	
	protected Object evaluateExpression(String expression)
	{
		Script compiledExpression = getCompiledExpression(expression);
		return evaluatorScope.evaluateExpression(compiledExpression);
	}
	
	protected Script getCompiledExpression(String expression)
	{
		Script compiledExpression = compiledExpressions.get(expression);
		if (compiledExpression == null)
		{
			if (log.isTraceEnabled())
			{
				log.trace("compiling expression " + expression);
			}
			
			compiledExpression = context.compileString(expression, "expression", 0, null);
			compiledExpressions.put(expression, compiledExpression);
		}
		return compiledExpression;
	}
	
	/**
	 * @deprecated To be removed.
	 */
	protected Class<?> getTypeClass(String type)
	{
		Class<?> typeClass = loadedTypes.get(type);
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
