package net.sf.jasperreports.components.sort.actions;

public class FilterData {
	
	private String uuid;
	private String fieldName;
	private String fieldValueStart;
	private String fieldValueEnd;
	private String filterType;
	private String filterTypeOperator;
	private String filterPattern;
	private boolean clearFilter;
	
	public FilterData() {
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValueStart() {
		return fieldValueStart;
	}

	public void setFieldValueStart(String fieldValueStart) {
		this.fieldValueStart = fieldValueStart;
	}

	public String getFieldValueEnd() {
		return fieldValueEnd;
	}

	public void setFieldValueEnd(String fieldValueEnd) {
		this.fieldValueEnd = fieldValueEnd;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterTypeOperator() {
		return filterTypeOperator;
	}

	public void setFilterTypeOperator(String filterTypeOperator) {
		this.filterTypeOperator = filterTypeOperator;
	}

	public String getFilterPattern() {
		return filterPattern;
	}

	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}

	public boolean isClearFilter() {
		return clearFilter;
	}

	public void setClearFilter(boolean clearFilter) {
		this.clearFilter = clearFilter;
	}

}
