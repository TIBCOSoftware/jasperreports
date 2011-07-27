/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.table.fill;

import java.util.Map;

import net.sf.jasperreports.components.sort.SortElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id: AdvancedTableFilterExpressionEvaluator.java 78 2011-06-24 08:52:29Z narcism $
 */
public class TableFilterExpressionEvaluator implements BuiltinExpressionEvaluator {
	
	private Map<String, JRFillField> fieldsMap;
	private JRExpression originalFilterExpression;
	private FillContext fillContext;
	private ReportContext reportContext;
	private String tableReportName;
	
	public TableFilterExpressionEvaluator(String tableReportName, ReportContext reportContext, FillContext fillContext, JRExpression originalFilterExpression) {
		this.tableReportName = tableReportName;
		this.fillContext = fillContext;
		this.reportContext = reportContext;
		this.originalFilterExpression = originalFilterExpression; 
		
	}
	public <T,U,V> void init(Map<String, T> parametersMap, Map<String, U> fieldsMap, Map<String, V> variablesMap, 
			WhenResourceMissingTypeEnum resourceMissingType)
			throws JRException
	{
		this.fieldsMap = (Map<String, JRFillField>)fieldsMap; 
	}

	public Object evaluate() throws JRExpressionEvalException
	{
		Boolean originalEvaluationResult = null;
		Boolean result = Boolean.TRUE;
		try {
			// FIXME: this is always null?
			originalEvaluationResult = (Boolean)fillContext.evaluate(originalFilterExpression, JRExpression.EVALUATION_DEFAULT);
		} catch (JRException e) {
			throw new JRExpressionEvalException(originalFilterExpression, e);
		}
		
		if (originalEvaluationResult == null) {
			originalEvaluationResult = Boolean.TRUE;
		}
		
		if (reportContext != null)
		{
			String paramFilterField = (String)reportContext.getParameterValue(tableReportName + "." + SortElement.REQUEST_PARAMETER_FILTER_FIELD);
			String paramFilterValue = (String)reportContext.getParameterValue(tableReportName + "." + SortElement.REQUEST_PARAMETER_FILTER_VALUE);
			
			if (paramFilterField != null && paramFilterValue != null) {
				result = ((String)fieldsMap.get(paramFilterField).getValue()).contains(paramFilterValue);
			}
		}
		
		return originalEvaluationResult && result;
	}

	public Object evaluateEstimated() throws JRExpressionEvalException
	{
		return evaluate();
	}

	public Object evaluateOld() throws JRExpressionEvalException
	{
		return evaluate();
	}
}
