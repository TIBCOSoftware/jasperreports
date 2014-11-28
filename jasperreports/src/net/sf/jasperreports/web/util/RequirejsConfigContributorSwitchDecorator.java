/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.web.util;

import net.sf.jasperreports.engine.JRPropertiesUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class RequirejsConfigContributorSwitchDecorator implements RequirejsConfigContributor
{
	
	private static final Log log = LogFactory.getLog(RequirejsConfigContributorSwitchDecorator.class);

	private boolean defaultEnabled;
	private String propertyName;
	private RequirejsConfigContributor contributor;
	
	@Override
	public void contribute(WebRequestContext context, ObjectNode config)
	{
		boolean enabled = JRPropertiesUtil.getInstance(context.getJasperReportsContext()).getBooleanProperty(
				propertyName, defaultEnabled);
		
		if (log.isDebugEnabled())
		{
			log.debug("switch for " + propertyName + " is " + enabled);
		}
		
		if (enabled)
		{
			contributor.contribute(context, config);
		}
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}

	public RequirejsConfigContributor getContributor()
	{
		return contributor;
	}

	public void setContributor(RequirejsConfigContributor contributor)
	{
		this.contributor = contributor;
	}

	public boolean isDefaultEnabled()
	{
		return defaultEnabled;
	}

	public void setDefaultEnabled(boolean defaultEnabled)
	{
		this.defaultEnabled = defaultEnabled;
	}

}
