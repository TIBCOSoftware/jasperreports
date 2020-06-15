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
package net.sf.jasperreports.chrome;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PageOptions
{

	private Long timeout;
	
	private Integer screenWidth;
	
	private Integer screenHeight;
	
	private Boolean isolate;
	
	public PageOptions()
	{
	}

	public Long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(Long timeout)
	{
		this.timeout = timeout;
	}

	public Integer getScreenWidth()
	{
		return screenWidth;
	}

	public void setScreenWidth(Integer screenWidth)
	{
		this.screenWidth = screenWidth;
	}

	public Integer getScreenHeight()
	{
		return screenHeight;
	}

	public void setScreenHeight(Integer screenHeight)
	{
		this.screenHeight = screenHeight;
	}

	public Boolean getIsolate()
	{
		return isolate;
	}

	public void setIsolate(Boolean isolate)
	{
		this.isolate = isolate;
	}
	
}
