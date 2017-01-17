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
package net.sf.jasperreports.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.annotations.properties.PropertyScopeQualificationType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.Designatable;
import net.sf.jasperreports.engine.util.JRQueryExecuterUtils;
import net.sf.jasperreports.metadata.properties.PropertyMetadata;
import net.sf.jasperreports.metadata.properties.PropertyMetadataScopeQualification;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PropertiesMetadataUtil
{

	public static PropertiesMetadataUtil getInstance(JasperReportsContext context)
	{
		return new PropertiesMetadataUtil(context);
	}
	
	private JasperReportsContext context;
	
	private volatile List<PropertyMetadata> loadedProperties;

	public PropertiesMetadataUtil(JasperReportsContext context)
	{
		this.context = context;
	}
	
	protected List<PropertyMetadata> allProperties()
	{
		List<PropertyMetadata> allProperties = loadedProperties;
		if (allProperties == null)
		{
			allProperties = new ArrayList<>();
			List<PropertiesMetadataProvider> providers = context.getExtensions(PropertiesMetadataProvider.class);
			for (PropertiesMetadataProvider provider : providers)
			{
				List<PropertyMetadata> providerProperties = provider.getProperties();
				if (providerProperties != null)
				{
					allProperties.addAll(providerProperties);
				}
			}
			
			loadedProperties = allProperties;
		}
		return allProperties;
	}
	
	public List<PropertyMetadata> getProperties()
	{
		return Collections.unmodifiableList(allProperties());
	}
	
	public List<PropertyMetadata> getQueryExecuterFieldProperties(String queryLanguage) throws JRException
	{
		QueryExecuterFactory queryExecuterFactory = JRQueryExecuterUtils.getInstance(context).getExecuterFactory(queryLanguage);
		if (!(queryExecuterFactory instanceof Designatable))
		{
			return Collections.emptyList();
		}
		String queryExecuterName = ((Designatable) queryExecuterFactory).getName();
		
		List<PropertyMetadata> properties = new ArrayList<>();
		List<PropertyMetadata> allProperties = allProperties();
		for (PropertyMetadata property : allProperties)
		{
			if (property.getScopes().contains(PropertyScope.FIELD))
			{
				List<? extends PropertyMetadataScopeQualification> scopeQualifications = property.getScopeQualifications();
				boolean foundQualification = false;
				for (PropertyMetadataScopeQualification scopeQualification : scopeQualifications)
				{
					if (scopeQualification.getType() == PropertyScopeQualificationType.QUERY_LANGUAGE
							&& scopeQualification.getValue().equals(queryExecuterName))
					{
						foundQualification = true;
						break;
					}
				}
				
				if (foundQualification)
				{
					properties.add(property);
				}
			}
		}
		
		return properties;
	}

}
