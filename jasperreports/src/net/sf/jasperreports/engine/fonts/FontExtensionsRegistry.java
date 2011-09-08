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
package net.sf.jasperreports.engine.fonts;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.extensions.ExtensionsRegistry;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class FontExtensionsRegistry implements ExtensionsRegistry
{

	private final List<String> fontFamiliesLocations;
	private List<FontFamily> fontFamilies;
	
	public FontExtensionsRegistry(List<String> fontFamiliesLocations)
	{
		this.fontFamiliesLocations = fontFamiliesLocations;
	}
	
	public <T> List<T> getExtensions(Class<T> extensionType)
	{
		if (FontFamily.class.equals(extensionType)) 
		{
			if (fontFamilies == null && fontFamiliesLocations != null)
			{
				fontFamilies = new ArrayList<FontFamily>();
				for (String location : fontFamiliesLocations)
				{
					fontFamilies.addAll(SimpleFontExtensionHelper.getInstance().loadFontFamilies(location));
				}
			}
			return (List<T>) fontFamilies;
		}
		return null;
	}

}
