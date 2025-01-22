/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2025 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.annotations.documentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class CategoryDoc
{

	private String key;
	private Element nameElement;
	private List<PropertyDoc> properties = new ArrayList<>();

	public CategoryDoc(String key)
	{
		this.key = key;
	}
	
	public void sortProperties()
	{
		Collections.sort(properties, PropertyNameComparator.instance());
	}

	public String getKey()
	{
		return key;
	}

	public Element getNameElement()
	{
		return nameElement;
	}

	public void setNameElement(Element nameElement)
	{
		this.nameElement = nameElement;
	}

	public void addProperty(PropertyDoc property)
	{
		properties.add(property);
	}
	
	public List<PropertyDoc> getProperties()
	{
		return properties;
	}
	
}
