/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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

import org.apache.batik.bridge.ExternalResourceSecurity;
import org.apache.batik.bridge.FontFamilyResolver;
import org.apache.batik.bridge.RelaxedExternalResourceSecurity;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.util.ParsedURL;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.properties.PropertyConstants;


/**
 * SVG renderer implementation based on <a href="http://xmlgraphics.apache.org/batik/">Batik</a>.
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BatikUserAgent extends UserAgentAdapter
{

	@Property(
			category = PropertyConstants.CATEGORY_OTHER,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_7_0_1,
			valueType = Boolean.class)
	public static final String PROPERTY_ALLOW_EXTERNAL_RESOURCES = JRPropertiesUtil.PROPERTY_PREFIX + "svg.allow.external.resources";

	public static final float PIXEL_TO_MM_72_DPI = 0.35277777777777777777777777777778f; // 72dpi
	public static final float PIXEL_TO_MM_96_DPI = 0.26458333333333333333333333333333f; // 96dpi

	private final FontFamilyResolver fontFamilyResolver;
	private final float pixel2mm;
	private final boolean allowExternalResources;
	
	public BatikUserAgent(
		JasperReportsContext jasperReportsContext
		) 
	{
		this.fontFamilyResolver = BatikFontFamilyResolver.getInstance(jasperReportsContext);
		this.pixel2mm = PIXEL_TO_MM_72_DPI;
		this.allowExternalResources = JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(
				PROPERTY_ALLOW_EXTERNAL_RESOURCES, false);
	}
	
	public BatikUserAgent(
		FontFamilyResolver fontFamilyResolver,
		float pixel2mm
		) 
	{
		this.fontFamilyResolver = fontFamilyResolver;
		this.pixel2mm = pixel2mm;
		this.allowExternalResources = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getBooleanProperty(
				PROPERTY_ALLOW_EXTERNAL_RESOURCES, false);
	}
	
	@Override
	public float getPixelUnitToMillimeter()
	{
		return pixel2mm;
	}

	@Override
	public FontFamilyResolver getFontFamilyResolver() 
	{
		return fontFamilyResolver;
	}

	@Override
	public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL resourceURL, ParsedURL docURL)
	{
		if (allowExternalResources)
		{
			return new RelaxedExternalResourceSecurity(resourceURL, docURL);
		}

		return super.getExternalResourceSecurity(resourceURL, docURL);
	}
}
