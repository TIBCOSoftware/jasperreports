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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.metadata.properties.PropertyMetadata;
import net.sf.jasperreports.metadata.properties.StandardPropertiesMetadata;
import net.sf.jasperreports.metadata.properties.StandardPropertiesMetadataSerialization;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ResourcePropertiesMetadataProvider implements PropertiesMetadataProvider
{

	private static final Log log = LogFactory.getLog(ResourcePropertiesMetadataProvider.class);
	
	public ResourcePropertiesMetadataProvider()
	{
	}

	@Override
	public List<PropertyMetadata> getProperties()
	{
		List<PropertyMetadata> properties = new ArrayList<>();
		List<URL> resources = JRLoader.getResources(StandardPropertiesMetadataSerialization.EXTENSION_RESOURCE_NAME);
		StandardPropertiesMetadataSerialization metadataSerialization = StandardPropertiesMetadataSerialization.instance();
		for (URL resource : resources)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Loading properties metadata from " + resource);
			}
			
			InputStream in = null;
			try
			{
				in = resource.openStream();
				StandardPropertiesMetadata resourceProperties = metadataSerialization.readProperties(in);
				if (log.isDebugEnabled())
				{
					log.debug("Loaded " + resourceProperties.getProperties().size() + " properties from " + resource);
				}
				properties.addAll(resourceProperties.getProperties());
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
			finally
			{
				if (in != null)
				{
					try
					{
						in.close();
					}
					catch (IOException e)
					{
						log.warn("Failed to close input stream for " + resource, e);
					}
				}
			}
		}
		return properties;
	}

}
