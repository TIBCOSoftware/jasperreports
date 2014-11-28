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
package net.sf.jasperreports.components.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.CompositeDatasetFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.design.JRAbstractCompiler;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
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
			
			JasperReport jasperReport = (JasperReport)context.getParameterValues().get(JRParameter.JASPER_REPORT);
			String currentDataset = JRAbstractCompiler.getUnitName(jasperReport, context.getDataset());
			if (paramTableName == null || !paramTableName.equals(currentDataset))
			{
				return;
			}
			
			String currentTableSortFieldsParam = paramTableName + SortElement.SORT_FIELDS_PARAM_SUFFIX;
			String currentTableFiltersParam = paramTableName + SortElement.FILTER_FIELDS_PARAM_SUFFIX;

			@SuppressWarnings("unchecked")
			List<JRSortField> existingFields = (List<JRSortField>) reportContext.getParameterValue(currentTableSortFieldsParam);
			List<JRSortField> sortFields = new ArrayList<JRSortField>();

			// add existing sort fields first
			if (existingFields != null)
			{
				sortFields.addAll(existingFields);
			}
			
			if (reportActionData != null)
			{
				
				String[] tokens = reportActionData.split(",");
				for (int i = 0; i < tokens.length; i++)
				{
					String token = tokens[i];
					String[] chunks = SortElementUtils.extractColumnInfo(token);
					
					if (log.isDebugEnabled())
					{
						log.debug("Adding sort " + token);
					}
					
					overwriteExistingSortField(
							sortFields,
							new JRDesignSortField(
								chunks[0],
								SortFieldTypeEnum.getByName(chunks[1]),
								SortElementUtils.getSortOrder(chunks[2])
								)
							);
				}
				
			}

			parameterValues.put(JRParameter.SORT_FIELDS, sortFields);
			reportContext.setParameterValue(currentTableSortFieldsParam, sortFields); 
			
			String paramFieldName = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_FIELD);
			String paramFieldValueStart = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_VALUE_START);
			String paramFieldValueEnd = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_VALUE_END);
			
			String paramFilterType = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_TYPE);
			String paramFilterTypeOperator = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_TYPE_OPERATOR);
			String paramFilterPattern = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_FILTER_PATTERN);
			
			DatasetFilter existingFilter = (DatasetFilter) reportContext.getParameterValue(currentTableFiltersParam);
			DatasetFilter combined = null; 
			
			if (paramFieldName != null && paramFieldValueStart != null && paramFilterType != null && paramFilterTypeOperator != null)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Filtering by " + paramFieldName + ": " + paramFieldValueStart);
				}
				
				if (existingFilter != null && true) { //FIXMEJIVE: add parameter to allow single filter on the same field
					List<DatasetFilter> filters = new ArrayList<DatasetFilter>();
					getFieldFilters(existingFilter, filters, null);
					FieldFilter filterForCurrentField = null;
					
					for (DatasetFilter df: filters){
						if (((FieldFilter)df).getField().equals(paramFieldName)) {
							filterForCurrentField = (FieldFilter)df;
							break;
						}
					}
					
					// update filterForCurrentField
					if (filterForCurrentField != null) {
						filterForCurrentField.setFilterTypeOperator(paramFilterTypeOperator);
						filterForCurrentField.setFilterValueEnd(paramFieldValueEnd);
						filterForCurrentField.setFilterValueStart(paramFieldValueStart);
						filterForCurrentField.setIsValid(null);
						combined = new CompositeDatasetFilter(filters);
					} else {
						FieldFilter filter = new FieldFilter(paramFieldName, paramFieldValueStart, paramFieldValueEnd, paramFilterType, paramFilterTypeOperator);
						filter.setFilterPattern(paramFilterPattern);
						combined = CompositeDatasetFilter.combine(existingFilter, filter);
					}
				} 
				else {
					FieldFilter filter = new FieldFilter(paramFieldName, paramFieldValueStart, paramFieldValueEnd, paramFilterType, paramFilterTypeOperator);
					filter.setFilterPattern(paramFilterPattern);
					combined = CompositeDatasetFilter.combine(existingFilter, filter);
				}
			}
			
			if (combined != null) {
				parameterValues.put(JRParameter.FILTER, combined);
				reportContext.setParameterValue(currentTableFiltersParam, combined);
			} else if (existingFilter != null) {
				parameterValues.put(JRParameter.FILTER, existingFilter);
				reportContext.setParameterValue(currentTableFiltersParam, existingFilter);
			}
			
			// remove filters
			String filterForFieldToRemove = (String) reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_REMOVE_FILTER);
			if (filterForFieldToRemove != null && existingFilter != null ) {
				List<DatasetFilter> filters = new ArrayList<DatasetFilter>();
				getFieldFilters(existingFilter, filters, null);
				FieldFilter filterToRemove = null;
				
				for (DatasetFilter df: filters){
					if (((FieldFilter)df).getField().equals(filterForFieldToRemove)) {
						filterToRemove = (FieldFilter)df;
						break;
					}
				}
				
				if (filterToRemove != null) {
					filters.remove(filterToRemove);
					combined = new CompositeDatasetFilter(filters);
					parameterValues.put(JRParameter.FILTER, combined);
					reportContext.setParameterValue(currentTableFiltersParam, combined);
				}
				
			}
		}
	}
	
	
	private void overwriteExistingSortField(List<JRSortField> sortFields, JRSortField newSortField) {
		int indexOfExistingSortField = sortFields.indexOf(newSortField);
		if (indexOfExistingSortField != -1) {
			// remove sortfield if previos order was 'Descending'
			boolean mustRemove = (sortFields.get(indexOfExistingSortField)).getOrderValue().equals(SortOrderEnum.DESCENDING);
			if (mustRemove) {
				sortFields.remove(indexOfExistingSortField);
			} else {
				((JRDesignSortField)sortFields.get(indexOfExistingSortField)).setOrder(newSortField.getOrderValue());
			}
			
		} else if (newSortField.getOrderValue() != null) { // this is necessary because a dummy order - None - is introduced
			sortFields.add(newSortField);
		}
	}
	
	private void getFieldFilters(DatasetFilter existingFilter, List<DatasetFilter> fieldFilters, String fieldName) {
		if (existingFilter instanceof FieldFilter) {
			if ( fieldName == null || (fieldName != null && ((FieldFilter)existingFilter).getField().equals(fieldName))) {
				fieldFilters.add(existingFilter);
			} 
		} else if (existingFilter instanceof CompositeDatasetFilter) {
			for (DatasetFilter filter : ((CompositeDatasetFilter)existingFilter).getFilters())
			{
				getFieldFilters(filter, fieldFilters, fieldName);
			}
		}
	}
	
	public void dispose() {
	}
	
}
