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
package net.sf.jasperreports.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.ReportContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class SimpleReportContext implements ReportContext
{

	private static final Random RND = new Random();
	
	private final String id;
	private final Map<String, Object> parameters;

	public SimpleReportContext()
	{
		this.id = System.currentTimeMillis() 
				+ "_" + System.identityHashCode(this)
				+ "_" + RND.nextInt();
		this.parameters = new HashMap<String, Object>();
	}
	
	public String getId()
	{
		return id;
	}

	public Object getParameterValue(String parameterName)
	{
		return parameters.get(parameterName);
	}

	public void setParameterValue(String parameterName, Object value)
	{
		parameters.put(parameterName, value);
	}

	public boolean containsParameter(String parameterName)
	{
		return parameters.containsKey(parameterName);
	}

}
