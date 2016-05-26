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
package net.sf.jasperreports.renderers;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.bridge.DefaultFontFamilyResolver;
import org.apache.batik.bridge.FontFace;
import org.apache.batik.bridge.FontFamilyResolver;
import org.apache.batik.gvt.font.GVTFontFamily;

import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class BatikFontFamilyResolver implements FontFamilyResolver
{
	/**
	 *
	 */
	private final JasperReportsContext jasperReportsContext;
	
	private final Map<String, GVTFontFamily> resolvedFontFamilies = new HashMap<String, GVTFontFamily>();


	/**
	 *
	 */
	private BatikFontFamilyResolver(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}


	/**
	 *
	 */
	public static BatikFontFamilyResolver getInstance(JasperReportsContext jasperReportsContext)
	{
		return new BatikFontFamilyResolver(jasperReportsContext);
	}


	@Override
	public GVTFontFamily resolve(String familyName, FontFace fontFace) 
	{
		return DefaultFontFamilyResolver.SINGLETON.resolve(familyName, fontFace);
	}

	@Override
	public GVTFontFamily loadFont(InputStream in, FontFace ff) throws Exception 
	{
		return DefaultFontFamilyResolver.SINGLETON.loadFont(in, ff);
	}

	@Override
	public GVTFontFamily resolve(String familyName) 
	{
		GVTFontFamily gvtFontFamily = resolvedFontFamilies.get(familyName);
		
		if (gvtFontFamily == null)
		{
			gvtFontFamily = new BatikAWTFontFamily(jasperReportsContext, familyName);
			resolvedFontFamilies.put(familyName, gvtFontFamily);
		}
		
		return gvtFontFamily;
	}

	@Override
	public GVTFontFamily getFamilyThatCanDisplay(char c) 
	{
		return DefaultFontFamilyResolver.SINGLETON.getFamilyThatCanDisplay(c);
	}

	@Override
	public GVTFontFamily getDefault() 
	{
		return DefaultFontFamilyResolver.SINGLETON.getDefault();
	}

}
