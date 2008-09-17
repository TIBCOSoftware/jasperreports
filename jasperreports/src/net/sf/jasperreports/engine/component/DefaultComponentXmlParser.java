/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.component;


/**
 * The default {@link ComponentsXmlParser} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class DefaultComponentXmlParser implements ComponentsXmlParser
{

	private String namespace;
	private String publicSchemaLocation;
	private String internalSchemaResource;
	private XmlDigesterConfigurer digesterConfigurer;
	
	public String getNamespace()
	{
		return namespace;
	}
	
	/**
	 * Sets the components namespace.
	 * 
	 * @param namespace the components namespace
	 * @see #getNamespace()
	 */
	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}
	
	public XmlDigesterConfigurer getDigesterConfigurer()
	{
		return digesterConfigurer;
	}
	
	/**
	 * Sets the components digester configurer.
	 * 
	 * @param digesterConfigurer the components digester configurer
	 * @see #getDigesterConfigurer()
	 */
	public void setDigesterConfigurer(XmlDigesterConfigurer digesterConfigurer)
	{
		this.digesterConfigurer = digesterConfigurer;
	}

	public String getPublicSchemaLocation()
	{
		return publicSchemaLocation;
	}

	/**
	 * Sets the public location of the components XML schema.
	 * 
	 * @param publicSchemaLocation the components XML schema public location
	 * @see #getPublicSchemaLocation()
	 */
	public void setPublicSchemaLocation(String publicSchemaLocation)
	{
		this.publicSchemaLocation = publicSchemaLocation;
	}

	public String getInternalSchemaResource()
	{
		return internalSchemaResource;
	}

	/**
	 * Sets the internal XML schema resource name. 
	 * 
	 * @param internalSchemaResource the internal XML schema resource name
	 * @see #getInternalSchemaResource()
	 */
	public void setInternalSchemaResource(String internalSchemaResource)
	{
		this.internalSchemaResource = internalSchemaResource;
	}

}
