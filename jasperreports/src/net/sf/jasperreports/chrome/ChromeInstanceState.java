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
public class ChromeInstanceState
{

	public static ChromeInstanceState create()
	{
		return new ChromeInstanceState(0, System.currentTimeMillis(), false);
	}
	
	private final long useCount;
	private final long useTimestamp;
	private final boolean closed;
	
	public ChromeInstanceState(long useCount, long useTimestamp, boolean closed)
	{
		this.useCount = useCount;
		this.useTimestamp = useTimestamp;
		this.closed = closed;
	}

	public long getUseCount()
	{
		return useCount;
	}

	public long getUseTimestamp()
	{
		return useTimestamp;
	}

	public boolean isClosed()
	{
		return closed;
	}

	public ChromeInstanceState incrementUse()
	{
		return new ChromeInstanceState(useCount + 1, System.currentTimeMillis(), closed);
	}

	public ChromeInstanceState decrementUse()
	{
		return new ChromeInstanceState(useCount - 1, System.currentTimeMillis(), closed);
	}

	public ChromeInstanceState close()
	{
		return new ChromeInstanceState(useCount, useTimestamp, true);
	}
	
	public boolean shouldClose()
	{
		return useCount == 0 && closed;
	}

}
