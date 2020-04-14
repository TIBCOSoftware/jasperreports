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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ReportExpressionsCompilation
{
	
	private final List<JRExpression> sourceExpressions;
	private final Set<String> sourceParameters;
	private final Set<String> sourceFields;
	private final Set<String> sourceVariables;
	private final Map<Integer, DirectExpressionEvaluation> directEvaluations;
	
	public ReportExpressionsCompilation(List<JRExpression> sourceExpressions,
			Set<String> sourceParameters, Set<String> sourceFields, Set<String> sourceVariables,
			Map<Integer, DirectExpressionEvaluation> directEvaluations)
	{
		this.sourceExpressions = sourceExpressions;
		this.sourceParameters = sourceParameters;
		this.sourceFields = sourceFields;
		this.sourceVariables = sourceVariables;
		this.directEvaluations = directEvaluations;
	}

	public List<JRExpression> getSourceExpressions()
	{
		return sourceExpressions;
	}

	public Set<String> getSourceParameters()
	{
		return sourceParameters;
	}

	public Set<String> getSourceFields()
	{
		return sourceFields;
	}

	public Set<String> getSourceVariables()
	{
		return sourceVariables;
	}

	public Map<Integer, DirectExpressionEvaluation> getDirectEvaluations()
	{
		return directEvaluations;
	}
	
	public <P extends JRParameter> Map<String, P> filterSourceParameters(Map<String, P> parameters)
	{
		if (parameters == null || parameters.isEmpty())
		{
			return parameters;
		}
		
		Map<String, P> sourceParams = new LinkedHashMap<>();
		for (Entry<String, P> entry : parameters.entrySet())
		{
			if (sourceParameters.contains(entry.getKey()))
			{
				sourceParams.put(entry.getKey(), entry.getValue());
			}
		}
		return sourceParams.size() == parameters.size() ? parameters : sourceParams;
	}
	
	public Map<String, JRField> filterSourceFields(Map<String, JRField> fields)
	{
		if (fields == null || fields.isEmpty())
		{
			return fields;
		}
		
		Map<String, JRField> sourceMap = new LinkedHashMap<>();
		for (Entry<String, JRField> entry : fields.entrySet())
		{
			if (sourceFields.contains(entry.getKey()))
			{
				sourceMap.put(entry.getKey(), entry.getValue());
			}
		}
		return sourceMap.size() == fields.size() ? fields : sourceMap;
	}
	
	public Map<String, JRVariable> filterSourceVariables(Map<String, JRVariable> variables)
	{
		if (variables == null || variables.isEmpty())
		{
			return variables;
		}
		
		Map<String, JRVariable> sourceVars = new LinkedHashMap<>();
		for (Entry<String, JRVariable> entry : variables.entrySet())
		{
			if (sourceVariables.contains(entry.getKey()))
			{
				sourceVars.put(entry.getKey(), entry.getValue());
			}
		}
		return sourceVars.size() == variables.size() ? variables : sourceVars;
	}
	
	public JRVariable[] filterSourceVariables(JRVariable[] variables)
	{
		if (variables == null || variables.length == 0)
		{
			return variables;
		}
		
		List<JRVariable> sourceVars = new ArrayList<>();
		for (JRVariable variable : variables)
		{
			if (sourceVariables.contains(variable.getName()))
			{
				sourceVars.add(variable);
			}
		}
		return sourceVars.size() == variables.length ? variables 
				: sourceVars.toArray(new JRVariable[sourceVars.size()]);
	}

}
