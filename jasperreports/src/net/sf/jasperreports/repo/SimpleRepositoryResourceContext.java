/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.repo;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SimpleRepositoryResourceContext implements RepositoryResourceContext
{

	public static SimpleRepositoryResourceContext of(String resourceLocation)
	{
		SimpleRepositoryResourceContext context = new SimpleRepositoryResourceContext();
		context.setContextResourceLocation(resourceLocation);
		return context;
	}
	
	public static RepositoryResourceContext childOf(RepositoryResourceContext parentContext, String resourceLocation)
	{
		if (resourceLocation == null)
		{
			return parentContext;
		}
		
		SimpleRepositoryResourceContext context = new SimpleRepositoryResourceContext();
		context.setContextResourceLocation(resourceLocation);
		context.setParentContext(parentContext);
		return context;
	}
	
	private RepositoryResourceContext parentContext;
	
	private String resourceLocation;

	@Override
	public RepositoryResourceContext getParentContext()
	{
		return parentContext;
	}

	public void setParentContext(RepositoryResourceContext parentContext)
	{
		this.parentContext = parentContext;
	}
	
	@Override
	public String getContextResourceLocation()
	{
		return resourceLocation;
	}

	public void setContextResourceLocation(String resourceLocation)
	{
		this.resourceLocation = resourceLocation;
	}

}
