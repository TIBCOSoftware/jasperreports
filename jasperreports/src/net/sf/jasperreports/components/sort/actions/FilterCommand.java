package net.sf.jasperreports.components.sort.actions;

import java.util.List;

import net.sf.jasperreports.components.sort.FieldFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.commands.CommandTarget;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class FilterCommand implements Command 
{
	
	public static final String DATASET_FILTER_PROPERTY = "net.sf.jasperreports.filters";
	
	protected JRDesignDataset dataset;
	protected FilterData filterData;
//	private JasperDesignCache cache;
	private CommandTarget target;
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
