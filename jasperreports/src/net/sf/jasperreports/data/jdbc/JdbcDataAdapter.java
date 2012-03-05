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
package net.sf.jasperreports.data.jdbc;

import java.util.Map;

import net.sf.jasperreports.data.ClasspathAwareDataAdapter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JdbcDataAdapter extends ClasspathAwareDataAdapter
{
	public String getDatabase();
	
	public void setDatabase(String database);
	
	public String getDriver();
	
	public void setDriver(String driver);
	
	public String getPassword();
	
	public void setPassword(String password);
	
	public boolean isSavePassword();
	
	public void setSavePassword(boolean savePassword);
	
	public String getUrl();
	
	public void setUrl(String url);
	
	public String getUsername();
	
	public void setUsername(String username);
	
	public String getServerAddress();
	
	public void setServerAddress(String serverAddress);
	
	public void setProperties(Map<String, String> properties);

	public Map<String, String> getProperties();
}
