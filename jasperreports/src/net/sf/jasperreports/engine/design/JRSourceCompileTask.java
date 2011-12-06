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
package net.sf.jasperreports.engine.design;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;

/**
 * Expression evaluator source code generation information.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRSourceCompileTask
{
	private JasperDesign jasperDesign;
	private String unitName;
	private JRExpressionCollector expressionCollector;
	private Map<String, ? extends JRParameter> parametersMap;
	private Map<String, JRField> fieldsMap;
	private Map<String, JRVariable> variablesMap;
	private JRVariable[] variables;
	private List<JRExpression> expressions;
	private boolean onlyDefaultEvaluation;
	
	
	protected JRSourceCompileTask(
			JasperDesign jasperDesign, 
			String unitName, 
			JRExpressionCollector expressionCollector, 
			Map<String, ? extends JRParameter> parametersMap, 
			Map<String, JRField> fieldsMap, 
			Map<String, JRVariable> variablesMap, 
			JRVariable[] variables, 
			boolean onlyDefaultEvaluation
			)
	{
		this.jasperDesign = jasperDesign;
		this.unitName = unitName;
		this.expressionCollector = expressionCollector;
		this.parametersMap = parametersMap;
		this.fieldsMap = fieldsMap;
		this.variablesMap = variablesMap;
		this.variables = variables;
		this.expressions = expressionCollector.getExpressions();
		this.onlyDefaultEvaluation = onlyDefaultEvaluation;
	}
	
	
	/**
	 * Creates source code generation information for a dataset of a report.
	 * 
	 * @param jasperDesign the report
	 * @param dataset the dataset
	 * @param expressionCollector the expression collector used for the report
	 * @param unitName the unit name of the code to be generated
	 */
	public JRSourceCompileTask(JasperDesign jasperDesign, JRDesignDataset dataset, JRExpressionCollector expressionCollector, String unitName)
	{
		this(jasperDesign, unitName, expressionCollector.getCollector(dataset),
				dataset.getParametersMap(), dataset.getFieldsMap(), dataset.getVariablesMap(), dataset.getVariables(),
				false);
	}
	
	
	/**
	 * Creates source code generation information for a crosstab of a report.
	 * 
	 * @param jasperDesign the report
	 * @param crosstab the crosstab
	 * @param expressionCollector the expression collector used for the report
	 * @param unitName the unit name of the code to be generated
	 */
	public JRSourceCompileTask(JasperDesign jasperDesign, JRDesignCrosstab crosstab, JRExpressionCollector expressionCollector, String unitName)
	{
		this(jasperDesign, unitName, expressionCollector.getCollector(crosstab),
				crosstab.getParametersMap(), null, crosstab.getVariablesMap(), crosstab.getVariables(),
				true);
	}


	public List<JRExpression> getExpressions()
	{
		return expressions;
	}


	public Map<String, JRField> getFieldsMap()
	{
		return fieldsMap;
	}


	public JasperDesign getJasperDesign()
	{
		return jasperDesign;
	}

	
	public String[] getImports()
	{
		return jasperDesign.getImports();
	}
	
	
	public boolean isOnlyDefaultEvaluation()
	{
		return onlyDefaultEvaluation;
	}


	public Map<String, ? extends JRParameter> getParametersMap()
	{
		return parametersMap;
	}


	public String getUnitName()
	{
		return unitName;
	}


	public JRVariable[] getVariables()
	{
		return variables;
	}


	public Map<String, JRVariable> getVariablesMap()
	{
		return variablesMap;
	}
	
	
	public Integer getExpressionId(JRExpression expression)
	{
		return expressionCollector.getExpressionId(expression);
	}
	
	
	public JRExpression getExpression(int expressionId)
	{
		return expressionCollector.getExpression(expressionId);
	}
}
