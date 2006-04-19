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
package net.sf.jasperreports.engine.design;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
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
	private Map parametersMap;
	private Map fieldsMap;
	private Map variablesMap;
	private JRVariable[] variables;
	private List expressions;
	private boolean onlyDefaultEvaluation;
	
	
	protected JRSourceCompileTask(JasperDesign jasperDesign, String unitName, JRExpressionCollector expressionCollector, Map parametersMap, Map fieldsMap, Map variablesMap, JRVariable[] variables, List expressions, boolean onlyDefaultEvaluation)
	{
		this.jasperDesign = jasperDesign;
		this.unitName = unitName;
		this.expressionCollector = expressionCollector;
		this.parametersMap = parametersMap;
		this.fieldsMap = fieldsMap;
		this.variablesMap = variablesMap;
		this.variables = variables;
		this.expressions = expressions;
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
		this(jasperDesign, unitName, expressionCollector,
				dataset.getParametersMap(), dataset.getFieldsMap(), dataset.getVariablesMap(), dataset.getVariables(),
				expressionCollector.getExpressions(dataset), false);
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
		this(jasperDesign, unitName, expressionCollector,
				crosstab.getParametersMap(), null, crosstab.getVariablesMap(), crosstab.getVariables(),
				expressionCollector.getExpressions(crosstab), true);
	}


	public List getExpressions()
	{
		return expressions;
	}


	public Map getFieldsMap()
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


	public Map getParametersMap()
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


	public Map getVariablesMap()
	{
		return variablesMap;
	}
	
	
	public Integer getExpressionId(JRExpression expression)
	{
		return expressionCollector.getExpressionId(expression);
	}
}
