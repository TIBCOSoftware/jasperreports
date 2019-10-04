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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.phantomjs.ScriptManager;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ScriptManagerRepository
{

	private static final ScriptManagerRepository INSTANCE = new ScriptManagerRepository();
	
	public static ScriptManagerRepository instance()
	{
		return INSTANCE;
	}
	
	//TODO weak ref, dispose
	private final Map<JasperReportsContext, ScriptManager> services;
	
	protected ScriptManagerRepository()
	{
		this.services = new HashMap<>();
	}
	
	public ScriptManager getService(JasperReportsContext jasperReportsContext)
	{
		synchronized (services)
		{
			ScriptManager service = services.get(jasperReportsContext);
			if (service == null)
			{
				service = new ScriptManager(jasperReportsContext, Chrome.PROPERTY_TEMPDIR_PATH);
				
				services.put(jasperReportsContext, service);
			}
			return service;
		}
	}
}
