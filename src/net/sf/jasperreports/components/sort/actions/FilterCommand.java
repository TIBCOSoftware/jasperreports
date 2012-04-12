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
package net.sf.jasperreports.components.sort.actions;

import java.util.List;

import net.sf.jasperreports.components.sort.FieldFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.web.commands.Command;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class FilterCommand implements Command 
{
	
	public static final String DATASET_FILTER_PROPERTY = "net.sf.jasperreports.filters";
	
	protected JRDesignDataset dataset;
	protected FilterData filterData;
//	private JasperDesignCache cache;
//	private CommandTarget target;
	private String oldSerializedFilters;
	private String newSerializedFilters;
	
	public FilterCommand(JRDesignDataset dataset, FilterData filterData) 
	{
//		this.cache = JasperDesignCache.getInstance(reportContext);
		this.dataset = dataset;
		this.filterData = filterData;
	}

	public void execute() 
	{
		// get existing filter as JSON string
		String serializedFilters = "[]";
		JRPropertiesMap propertiesMap = dataset.getPropertiesMap();
		if (propertiesMap.getProperty(DATASET_FILTER_PROPERTY) != null) {
			serializedFilters = propertiesMap.getProperty(DATASET_FILTER_PROPERTY);
		}
		
		oldSerializedFilters = serializedFilters;
		
		List<DatasetFilter> existingFilters = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			existingFilters = mapper.readValue(serializedFilters, new TypeReference<List<FieldFilter>>(){});	// FIXMEJIVE place all JSON serialization/deserialization into utility class
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
		
		if (!filterData.isClearFilter()) {	// add filter
			boolean addNewFilter = false;
			
			if (existingFilters.size() == 0) {
				addNewFilter = true;
			} else {
				// lookup new filter
				FieldFilter filterForCurrentField = null;
				
				for (DatasetFilter ff: existingFilters){
					if (((FieldFilter)ff).getField().equals(filterData.getFieldName())) {
						filterForCurrentField = (FieldFilter)ff;
						break;
					}
				}
				
				// update filterForCurrentField
				if (filterForCurrentField != null) {
					filterForCurrentField.setFilterTypeOperator(filterData.getFilterTypeOperator());
					filterForCurrentField.setFilterValueEnd(filterData.getFieldValueEnd());
					filterForCurrentField.setFilterValueStart(filterData.getFieldValueStart());
					filterForCurrentField.setFilterPattern(filterData.getFilterPattern());
					filterForCurrentField.setIsValid(null);
				} else {
					addNewFilter = true;
				}
			}
			
			if (addNewFilter) {
				FieldFilter newFilterField = new FieldFilter(
						filterData.getFieldName(), filterData.getFieldValueStart(),
						filterData.getFieldValueEnd(), filterData.getFilterType(),
						filterData.getFilterTypeOperator());

				newFilterField.setFilterPattern(filterData.getFilterPattern());
				existingFilters.add(newFilterField);
			}
			
		} else { // remove filter
			FieldFilter filterToRemove = null;
			
			for (DatasetFilter df: existingFilters){
				if (((FieldFilter)df).getField().equals(filterData.getFieldName())) {
					filterToRemove = (FieldFilter)df;
					break;
				}
			}
			
			if (filterToRemove != null) {
				existingFilters.remove(filterToRemove);
			}
		}
		
		try {
			newSerializedFilters = mapper.writeValueAsString(existingFilters);
			propertiesMap.setProperty(DATASET_FILTER_PROPERTY, newSerializedFilters);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}
	
	public void undo() 
	{
		dataset.getPropertiesMap().setProperty(DATASET_FILTER_PROPERTY, oldSerializedFilters);
	}

	public void redo() 
	{
		dataset.getPropertiesMap().setProperty(DATASET_FILTER_PROPERTY, newSerializedFilters);
	}
}
