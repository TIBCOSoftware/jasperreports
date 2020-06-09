/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.data.jdbc;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.ParameterContributorContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractGbqDataAdapterService extends AbstractJdbcUrlParameterProcessingDataAdapterService 
{
	private final AbstractJdbcFileUrlParameterProcessor privateKeyProcessor = new GbqPrivateKeyProcessor(this);

	/**
	 * 
	 */
	public AbstractGbqDataAdapterService(ParameterContributorContext paramContribContext, JdbcDataAdapter jdbcDataAdapter) 
	{
		super(paramContribContext, jdbcDataAdapter);
	}


	@Override
	protected String processUrlParameter(String paramName, String paramValue) throws JRException
	{
		if (paramName.equalsIgnoreCase(getPrivateKeyConnectionParameter()) && paramValue != null && paramValue.length() > 0)
		{
			File privateKeyFile = privateKeyProcessor.getUrlParameterFile(paramValue);
			if (privateKeyFile != null)
			{
				return getPrivateKeyConnectionParameter() + "=" + privateKeyFile.getAbsolutePath();
			}
		}
		
		return paramName + (paramValue == null ? "" : ("=" + paramValue));
	}


	protected abstract String getPrivateKeyConnectionParameter();
}
