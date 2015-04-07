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
import net.sf.jasperreports.engine.fill.JasperReportsContextAware;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.functions.FunctionsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JavaScript expression evaluator that compiles expressions at fill time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JavaScriptCompiler
 */
public class JavaScriptEvaluator extends JREvaluator implements JasperReportsContextAware
{
	
	/**
	 * Property that determines the optimization level used when compiling expressions.
	 * 
	 * See <a href="http://www-archive.mozilla.org/rhino/apidocs/org/mozilla/javascript/Context.html#setOptimizationLevel%28int%29"/>
	 */
	public static final String PROPERTY_OPTIMIZATION_LEVEL = JRPropertiesUtil.PROPERTY_PREFIX 
			+ "javascript.evaluator.optimization.level";
	
	public static final String EXCEPTION_MESSAGE_KEY_EVALUATOR_LOAD_ERROR = "compilers.javascript.evaluator.load.error";
	
	private static final Log log = LogFactory.getLog(JavaScriptEvaluator.class);
	
	private final JasperReportsContext jrContext;
	private final JavaScriptCompileData compileData;
	private FunctionsUtil functionsUtil;
	private JavaScriptEvaluatorScope evaluatorScope;
	private Map<String, Class<?>> loadedTypes = new HashMap<String, Class<?>>();

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
	
	@Override
	public void setJasperReportsContext(JasperReportsContext context)
	{
		this.functionsUtil = FunctionsUtil.getInstance(context);
	}

	protected void customizedInit(
			Map<String, JRFillParameter> parametersMap, 
			Map<String, JRFillField> fieldsMap,
			Map<String, JRFillVariable> variablesMap
			) throws JRException
	{
		evaluatorScope = new JavaScriptEvaluatorScope(jrContext, this, functionsUtil);
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
		return evaluatorScope.evaluateExpression(expression);
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
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_EVALUATOR_LOAD_ERROR,
						new Object[]{type},
						e);
			}
			loadedTypes.put(type, typeClass);
		}
		return typeClass;
	}

}
