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
package net.sf.jasperreports.components.headertoolbar;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.sort.FieldFilter;
import net.sf.jasperreports.components.sort.actions.FilterCommand;
import net.sf.jasperreports.engine.CompositeDatasetFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ReportContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class HeaderToolbarParameterContributor implements ParameterContributor
{
	private static final Log log = LogFactory.getLog(HeaderToolbarParameterContributor.class);
	
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
			
			if (serializedFilters != null) {
				ObjectMapper mapper = new ObjectMapper();
				List<DatasetFilter> existingFilters = null;
				try {
					existingFilters = mapper.readValue(serializedFilters, new TypeReference<List<FieldFilter>>(){});
				} catch (Exception e) {
					throw new JRRuntimeException(e);
				}
				
				parameterValues.put(JRParameter.FILTER, new CompositeDatasetFilter(existingFilters));
			}
		}
	}
	
	public void dispose() {
	}

}
