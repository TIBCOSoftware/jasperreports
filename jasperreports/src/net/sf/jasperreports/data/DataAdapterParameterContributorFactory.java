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
package net.sf.jasperreports.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.repo.DataAdapterResource;
import net.sf.jasperreports.repo.RepositoryUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class DataAdapterParameterContributorFactory implements ParameterContributorFactory
{

	/**
	 * A report/dataset level property that provides the location of a data adapter resource 
	 * to be used for the dataset.
	 */
	public static final String PROPERTY_DATA_ADAPTER_LOCATION = JRPropertiesUtil.PROPERTY_PREFIX + "data.adapter";

	private static final DataAdapterParameterContributorFactory INSTANCE = new DataAdapterParameterContributorFactory();
	
	private DataAdapterParameterContributorFactory()
	{
	}
	
	/**
	 * 
	 */
	public static DataAdapterParameterContributorFactory getInstance()
	{
		return INSTANCE;
	}

	/**
	 *
	 */
	public List<ParameterContributor> getContributors(ParameterContributorContext context) throws JRException
	{
		List<ParameterContributor> contributors = new ArrayList<ParameterContributor>();

		String dataAdapterUri = JRPropertiesUtil.getInstance(context.getJasperReportsContext()).getProperty(context.getDataset(), PROPERTY_DATA_ADAPTER_LOCATION); 
		if (dataAdapterUri != null)
		{
			DataAdapterResource dataAdapterResource = RepositoryUtil.getInstance(context.getJasperReportsContext()).getResourceFromLocation(dataAdapterUri, DataAdapterResource.class);
			ParameterContributor dataAdapterService = DataAdapterServiceUtil.getInstance(context.getJasperReportsContext()).getService(dataAdapterResource.getDataAdapter());
			
			return Collections.singletonList(dataAdapterService);
		}

		return contributors;
	}
	
}
