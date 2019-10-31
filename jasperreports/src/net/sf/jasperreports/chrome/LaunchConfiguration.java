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

import java.nio.file.Path;

import net.sf.jasperreports.engine.util.ObjectUtils;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class LaunchConfiguration
{

	private final Path executablePath;
	private final boolean headless;
	//TODO args
	
	public LaunchConfiguration(Path executablePath, boolean headless)
	{
		this.executablePath = executablePath;
		this.headless = headless;
	}

	@Override
	public int hashCode()
	{
		int result = 31 + (headless ? 1231 : 1237);
		result = 31 * result + ((executablePath == null) ? 0 : executablePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		
		LaunchConfiguration other = (LaunchConfiguration) obj;
		return executablePath == other.executablePath
				&& ObjectUtils.equals(executablePath, other.executablePath);
	}

	public Path getExecutablePath()
	{
		return executablePath;
	}

	public boolean isHeadless()
	{
		return headless;
	}
	
	@Override
	public String toString()
	{
		return "executable: " + executablePath + ", headless: " + headless;
	}
	
}
