/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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

import org.apache.batik.bridge.FontFamilyResolver;
import org.apache.batik.bridge.UserAgentAdapter;

import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * SVG renderer implementation based on <a href="http://xmlgraphics.apache.org/batik/">Batik</a>.
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BatikUserAgent extends UserAgentAdapter
{
	public static final float PIXEL_TO_MM_72_DPI = 0.35277777777777777777777777777778f; // 72dpi
	public static final float PIXEL_TO_MM_96_DPI = 0.26458333333333333333333333333333f; // 96dpi

	private final FontFamilyResolver fontFamilyResolver;
	private final float pixel2mm;
	
	public BatikUserAgent(
		JasperReportsContext jasperReportsContext
		) 
	{
		this.fontFamilyResolver = BatikFontFamilyResolver.getInstance(jasperReportsContext);
		this.pixel2mm = PIXEL_TO_MM_72_DPI;
	}
	
	public BatikUserAgent(
		FontFamilyResolver fontFamilyResolver,
		float pixel2mm
		) 
	{
		this.fontFamilyResolver = fontFamilyResolver;
		this.pixel2mm = pixel2mm;
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
}
