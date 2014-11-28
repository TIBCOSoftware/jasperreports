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
package net.sf.jasperreports.components.headertoolbar;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.sort.FieldFilter;
import net.sf.jasperreports.components.sort.actions.FilterCommand;
import net.sf.jasperreports.engine.CompositeDatasetFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 * 
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class HeaderToolbarParameterContributor implements ParameterContributor
{
	private final ParameterContributorContext context;

	public HeaderToolbarParameterContributor (ParameterContributorContext context)
	{
		this.context = context;
	}
	
	public void contributeParameters(Map<String, Object> parameterValues) throws JRException
	{
		ReportContext reportContext = (ReportContext) parameterValues.get(JRParameter.REPORT_CONTEXT);
		if (reportContext != null)
		{
			String serializedFilters = context.getDataset().getPropertiesMap().getProperty(FilterCommand.DATASET_FILTER_PROPERTY);
			
			if (serializedFilters != null) 
			{
				List<? extends DatasetFilter> existingFilters = getFilters(serializedFilters);
				parameterValues.put(JRParameter.FILTER, new CompositeDatasetFilter(existingFilters));
			}
		}
	}
	
	protected List<? extends DatasetFilter> getFilters(String serializedFilters)
	{
		if (serializedFilters != null) 
		{
			return JacksonUtil.getInstance(context.getJasperReportsContext()).loadList(serializedFilters, FieldFilter.class);
		}
		return null;
	}
	
	public void dispose() {
	}

}
