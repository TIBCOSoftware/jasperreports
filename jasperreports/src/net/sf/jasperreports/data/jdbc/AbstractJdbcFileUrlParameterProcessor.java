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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.repo.RepositoryUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractJdbcFileUrlParameterProcessor 
{
	private AbstractDataAdapterService dataAdapterService;
	
	/**
	 * 
	 */
	public AbstractJdbcFileUrlParameterProcessor(AbstractDataAdapterService dataAdapterService) 
	{
		this.dataAdapterService = dataAdapterService;
	}


	protected File getUrlParameterFile(String urlParameterResourcePath) throws JRException
	{
		File urlParameterFile = null;
		
		if (urlParameterResourcePath != null && urlParameterResourcePath.trim().length() > 0)
		{
			urlParameterFile = new File(urlParameterResourcePath);
			if (!urlParameterFile.exists())
			{
				byte[] fileData = getUrlParameterResourceData(urlParameterResourcePath);

				urlParameterFile = 
					new File(
						new File(System.getProperty("java.io.tmpdir")), 
						getUrlParameterTempFilePrefix() + (31 * urlParameterResourcePath.hashCode() + Arrays.hashCode(fileData)) + "." + getUrlParameterTempFileExtension()
						);

				if (!urlParameterFile.exists())
				{
					FileOutputStream fos = null;
					
					try
					{
						fos = new FileOutputStream(urlParameterFile);
						fos.write(fileData);
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
					
					urlParameterFile.deleteOnExit();
				}
			}
		}
		
		return urlParameterFile;
	}


	protected abstract String getUrlParameterTempFilePrefix();


	protected abstract String getUrlParameterTempFileExtension();


	protected byte[] getUrlParameterResourceData(String urlParameterResourcePath) throws JRException
	{
		return RepositoryUtil.getInstance(dataAdapterService.getParameterContributorContext().getRepositoryContext()).getBytesFromLocation(urlParameterResourcePath);
	}
}
