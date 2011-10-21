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
package net.sf.jasperreports.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.repo.RepositoryUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class DataAdapterParameterContributorFactory implements ParameterContributorFactory
{

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

		String dataAdapterUri = JRProperties.getProperty(context.getDataset(), "net.sf.jasperreports.data.adapter");
		if (dataAdapterUri != null)
		{
			DataAdapter dataAdapter = RepositoryUtil.getResource(dataAdapterUri, DataAdapter.class);
			ParameterContributor dataAdapterService = DataAdapterServiceUtil.getDataAdapterService(dataAdapter);
			
			return Collections.singletonList(dataAdapterService);
		}

		return contributors;
	}
	
}
