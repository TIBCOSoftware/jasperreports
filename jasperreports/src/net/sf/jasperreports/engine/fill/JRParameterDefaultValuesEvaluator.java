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
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.repo.SimpleRepositoryContext;


/**
 * Utility class to be used to evaluate parameter default value expressions for a report
 * without actually filling it.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public final class JRParameterDefaultValuesEvaluator
{

	/**
	 * Evaluates the default values for the parameters of a report.
	 * 
	 * @param report the report
	 * @param initialParameters initial parameter value map
	 * @return a map containing parameter values indexed by parameter names
	 * @throws JRException
	 */
	public static Map<String,Object> evaluateParameterDefaultValues(JasperReport report, Map<String,Object> initialParameters) throws JRException
	{
		Map<String,Object> valuesMap = initialParameters == null ? new HashMap<String,Object>() : new HashMap<String,Object>(initialParameters);
		
		valuesMap.put(JRParameter.JASPER_REPORT, report);
		
		ObjectFactory factory = new ObjectFactory();
		JRDataset reportDataset = report.getMainDataset();
		JRFillDataset fillDataset = factory.getDataset(reportDataset);
		fillDataset.createCalculator(report);
		fillDataset.initCalculator();

		RepositoryUtil.setRepositoryContext(new SimpleRepositoryContext(valuesMap));

		try
		{
			fillDataset.setParameterValues(valuesMap);
			
			Map<String,Object> parameterValues = new HashMap<String,Object>();
			JRParameter[] parameters = reportDataset.getParameters();
			for (int i = 0; i < parameters.length; i++)
			{
				JRParameter param = parameters[i];
				if (!param.isSystemDefined())
				{
					String name = param.getName();
					Object value = fillDataset.getParameterValue(name);
					parameterValues.put(name, value);
				}
			}
			
			return parameterValues;
		}
		finally
		{
			RepositoryUtil.revertRepositoryContext();
		}
	}
	
	protected static class ObjectFactory extends JRFillObjectFactory
	{
		protected ObjectFactory()
		{
			super((JRBaseFiller) null, null);
		}

		protected JRFillGroup getGroup(JRGroup group)
		{
			return super.getGroup(null);
		}
	}
	

	private JRParameterDefaultValuesEvaluator()
	{
	}
}
