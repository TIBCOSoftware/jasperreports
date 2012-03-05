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
package net.sf.jasperreports.repo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jasperreports.data.XmlUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FileRepositoryService.java 4819 2011-11-28 15:24:25Z lucianc $
 */
public class CastorObjectPersistenceService implements PersistenceService
{
	/**
	 * 
	 */
	private static volatile XMLContext xmlContext;
	
	XMLContext getXmlContext()
	{
		if (xmlContext != null)
		{
			return xmlContext;
		}
		
		synchronized (CastorObjectPersistenceService.class)
		{
			// double check
			if (xmlContext != null)
			{
				return xmlContext;
			}
			
			XMLContext context = new XMLContext();

			Mapping mapping  = context.createMapping();
			
			List<CastorMapping> castorMappings = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(CastorMapping.class);
			for (CastorMapping castorMapping : castorMappings)
			{
				loadMapping(mapping, castorMapping.getPath());
			}
			
			try
			{
				context.addMapping(mapping);
			}
			catch (MappingException e)
			{
				throw new JRRuntimeException("Failed to load Castor mappings", e);
			}
			
			xmlContext = context;
		}
		
		return xmlContext;
	}
	
	/**
	 *
	 */
	private static void loadMapping(Mapping mapping, String mappingFile)
	{
		try
		{
			byte[] mappingFileData = JRLoader.loadBytesFromResource(mappingFile);
			InputSource mappingSource = new InputSource(new ByteArrayInputStream(mappingFileData));

			mapping.loadMapping(mappingSource);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	
	/**
	 * 
	 */
	public Resource load(String uri, RepositoryService repositoryService)
	{
		ObjectResource resource = null; 

		InputStreamResource isResource = repositoryService.getResource(uri, InputStreamResource.class);
		
		InputStream is = isResource == null ? null : isResource.getInputStream();
		if (is != null)
		{
			resource = new ObjectResource();
			try
			{
				resource.setValue(XmlUtil.read(is, getXmlContext()));
			}
			finally
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
				}
			}
		}

		return resource;
	}

	
	/**
	 * 
	 */
	public void save(Resource resource, String uri, RepositoryService repositoryService)
	{
		//FIXMEREPO
	}

}
