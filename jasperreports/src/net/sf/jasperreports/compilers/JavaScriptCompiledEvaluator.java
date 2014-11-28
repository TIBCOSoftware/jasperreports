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
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.fill.JasperReportsContextAware;
import net.sf.jasperreports.functions.FunctionsUtil;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Script;

/**
 * JavaScript expression evaluator that uses Java bytecode compiled by {@link JavaScriptClassCompiler}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JavaScriptCompiledEvaluator extends JREvaluator implements JasperReportsContextAware
{

	private static final Log log = LogFactory.getLog(JavaScriptCompiledEvaluator.class);

	protected static final String EXPRESSION_ID_VAR = "_jreid";
	
	private static final ReferenceMap scriptClassLoaders = new ReferenceMap(ReferenceMap.HARD, ReferenceMap.SOFT);
	
	protected static JavaScriptClassLoader getScriptClassLoader(String unitName)
	{
		JavaScriptClassLoader loader;
		boolean created = false;
		synchronized (scriptClassLoaders)
		{
			loader = (JavaScriptClassLoader) scriptClassLoaders.get(unitName);
			if (loader == null)
			{
				loader = new JavaScriptClassLoader();
				scriptClassLoaders.put(unitName, loader);
				created = true;
			}
		}
		
		if (created && log.isDebugEnabled())
		{
			log.debug("created script class loader " + loader + " for " + unitName);
		}
		return loader;
	}
	
	private final JasperReportsContext jasperReportsContext;
	private final String unitName;
	private final JavaScriptCompiledData compiledData;
	private FunctionsUtil functionsUtil;
	private JavaScriptEvaluatorScope evaluatorScope;
	
	private final Map<Integer, Script> scripts = new HashMap<Integer, Script>();


	/**
	 * Create a JavaScript expression evaluator.
	 * 
	 * @param jasperReportsContext 
	 * @param unitName 
	 * @param compiledData the report compile data
	 */
	public JavaScriptCompiledEvaluator(JasperReportsContext jasperReportsContext, String unitName, JavaScriptCompiledData compiledData)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.unitName = unitName;
		this.compiledData = compiledData;
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
		evaluatorScope = new JavaScriptEvaluatorScope(jasperReportsContext, this, functionsUtil);
		evaluatorScope.init(parametersMap, fieldsMap, variablesMap);
	}
	
	protected Object evaluate(int id) throws Throwable //NOSONAR
	{
		JavaScriptCompiledData.ExpressionIndexes expression = getExpression(id);
		return evaluateExpression(expression.getDefaultExpressionIndex());
	}

	protected Object evaluateEstimated(int id) throws Throwable //NOSONAR
	{
		JavaScriptCompiledData.ExpressionIndexes expression = getExpression(id);
		return evaluateExpression(expression.getEstimatedExpressionIndex());
	}

	protected Object evaluateOld(int id) throws Throwable //NOSONAR
	{
		JavaScriptCompiledData.ExpressionIndexes expression = getExpression(id);
		return evaluateExpression(expression.getOldExpressionIndex());
	}

	protected JavaScriptCompiledData.ExpressionIndexes getExpression(int id)
	{
		return compiledData.getExpression(id);
	}
	
	protected Object evaluateExpression(int expressionIndex)
	{
		int scriptIndex = JavaScriptCompiledData.scriptIndex(expressionIndex);
		Script script = scripts.get(scriptIndex);
		if (script == null)
		{
			if (log.isTraceEnabled())
			{
				log.trace("creating script for expression index " + expressionIndex
						+ ", script index " + scriptIndex);
			}
			
			JavaScriptClassLoader scriptClassLoader = getScriptClassLoader(unitName);
			script = scriptClassLoader.createScript(scriptIndex, compiledData);
			scripts.put(scriptIndex, script);
		}
		
		int expressionId = JavaScriptCompiledData.expressionId(expressionIndex);
		evaluatorScope.setScopeVariable(EXPRESSION_ID_VAR, expressionId);
		Object value = evaluatorScope.evaluateExpression(script);
		if (log.isTraceEnabled())
		{
			log.trace("expression with index " + expressionIndex + ", id " + expressionId 
					+ " evaluated to " + value);
		}
		return value;
	}

}
