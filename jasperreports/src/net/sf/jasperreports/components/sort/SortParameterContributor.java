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
package net.sf.jasperreports.components.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.CompositeDatasetFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.design.JRAbstractCompiler;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class SortParameterContributor implements ParameterContributor
{
	private static final Log log = LogFactory.getLog(SortParameterContributor.class);
	
	private final ParameterContributorContext context;

	public SortParameterContributor (ParameterContributorContext context)
	{
		this.context = context;
	}
	
	public void contributeParameters(Map<String, Object> parameterValues) throws JRException
	{
		ReportContext reportContext = (ReportContext) parameterValues.get(JRParameter.REPORT_CONTEXT);
		if (reportContext != null)
		{
			String reportActionData = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_SORT_DATA);
			String paramTableName = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_DATASET_RUN);
			
			String currentDataset = JRAbstractCompiler.getUnitName(context.getJasperReport(), context.getDataset());
			if (paramTableName == null || !paramTableName.equals(currentDataset))
			{
				return;
			}
			
			if (reportActionData != null)
			{
				@SuppressWarnings("unchecked")
				List<JRSortField> existingFields = (List<JRSortField>) parameterValues.get(JRParameter.SORT_FIELDS);
				List<JRSortField> sortFields = new ArrayList<JRSortField>();
				// add existing sort fields first
				if (existingFields != null)
				{
					sortFields.addAll(existingFields);
				}
				
				String[] tokens = reportActionData.split(",");
				for (int i = 0; i < tokens.length; i++)
				{
					String token = tokens[i];
					String[] chunks = SortElementUtils.extractColumnInfo(token);
					
					if (log.isDebugEnabled())
					{
						log.debug("Adding sort " + token);
					}
					
					sortFields.add(
						new JRDesignSortField(
							chunks[0],
							SortFieldTypeEnum.getByName(chunks[1]),
							SortElementUtils.getSortOrder(chunks[2])
							)
						);
				}
				
				parameterValues.put(JRParameter.SORT_FIELDS, sortFields);
			}
			
			String paramFieldName = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_FIELD);
			String paramFieldValue = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_VALUE);
			if (paramFieldName != null && paramFieldValue != null)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Filtering by " + paramFieldName + ": " + paramFieldValue);
				}
				
				DatasetFilter filter = new FieldFilter(paramFieldName, paramFieldValue);
				DatasetFilter existingFilter = (DatasetFilter) parameterValues.get(JRParameter.FILTER);
				DatasetFilter combined = CompositeDatasetFilter.combine(existingFilter, filter);
				parameterValues.put(JRParameter.FILTER, combined);
			}
		}
	}

}
