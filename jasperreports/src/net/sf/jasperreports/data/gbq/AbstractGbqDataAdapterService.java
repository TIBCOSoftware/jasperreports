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
package net.sf.jasperreports.data.gbq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import net.sf.jasperreports.data.jdbc.JdbcDataAdapter;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapterService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.repo.RepositoryUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractGbqDataAdapterService extends JdbcDataAdapterService 
{
	protected static final String GBQ_PRIVATE_KEY_TEMP_FILE_PREFIX = "jr_gbq_";

	/**
	 * 
	 */
	public AbstractGbqDataAdapterService(ParameterContributorContext paramContribContext, JdbcDataAdapter jdbcDataAdapter) 
	{
		super(paramContribContext, jdbcDataAdapter);
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		JdbcDataAdapter jdbcDataAdapter = getJdbcDataAdapter();
		
		String connectionUrl = jdbcDataAdapter.getUrl();
		if (connectionUrl != null && connectionUrl.trim().length() > 0)
		{
			StringBuffer connectionUrlBuffer = new StringBuffer();
			
			String connectionParams[] = connectionUrl.split(";");
			for (String connectionParam : connectionParams)
			{
				connectionUrlBuffer.append(";");

				if (connectionParam.startsWith(getPrivateKeyConnectionParameter()) && connectionParam.contains("="))
				{
					File privateKeyFile = getPrivateKeyFile(connectionParam.split("=")[1]);
					if (privateKeyFile != null)
					{
						connectionParam = getPrivateKeyConnectionParameter() + "=" + privateKeyFile.getAbsolutePath();
					}
				}

				connectionUrlBuffer.append(connectionParam);
			}
			
			jdbcDataAdapter.setUrl(connectionUrlBuffer.substring(1));
		}
		
		super.contributeParameters(parameters);
	}
	
	protected File getPrivateKeyFile(String privateKeyResourcePath) throws JRException
	{
		File privateKeyFile = null;
		
		if (privateKeyResourcePath != null && privateKeyResourcePath.trim().length() > 0)
		{
			privateKeyFile = new File(privateKeyResourcePath);
			if (!privateKeyFile.exists())
			{
				byte[] privateKeyData = RepositoryUtil.getInstance(getJasperReportsContext()).getBytesFromLocation(privateKeyResourcePath);

				privateKeyFile = 
					new File(
						new File(System.getProperty("java.io.tmpdir")), 
						GBQ_PRIVATE_KEY_TEMP_FILE_PREFIX + (31 * privateKeyResourcePath.hashCode() + Arrays.hashCode(privateKeyData)) + ".json"
						);

				if (!privateKeyFile.exists())
				{
					FileOutputStream fos = null;
					
					try
					{
						fos = new FileOutputStream(privateKeyFile);
						fos.write(privateKeyData);
					}
					catch (IOException e)
					{
						throw new JRException(e);
					}
					finally
					{
						if (fos != null)
						{
							try
							{
								fos.close();
							}
							catch (IOException e)
							{
							}
						}
					}
					
					privateKeyFile.deleteOnExit();
				}
			}
		}
		
		return privateKeyFile;
	}
	
	protected abstract String getPrivateKeyConnectionParameter();
}
