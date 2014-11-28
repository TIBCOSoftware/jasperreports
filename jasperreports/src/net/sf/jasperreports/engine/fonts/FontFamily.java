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
package net.sf.jasperreports.engine.fonts;

import java.util.Locale;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface FontFamily
{

	/**
	 * 
	 */
	public String getName();
	
	/**
	 * 
	 */
	public FontFace getNormalFace();
	
	/**
	 * 
	 */
	public FontFace getBoldFace();
	
	/**
	 * 
	 */
	public FontFace getItalicFace();
	
	/**
	 * 
	 */
	public FontFace getBoldItalicFace();
	
	/**
	 * @deprecated Replaced by {@link FontFace#getPdf()}.
	 */
	public String getNormalPdfFont();
	
	/**
	 * @deprecated Replaced by {@link FontFace#getPdf()}.
	 */
	public String getBoldPdfFont();
	
	/**
	 * @deprecated Replaced by {@link FontFace#getPdf()}.
	 */
	public String getItalicPdfFont();
	
	/**
	 * @deprecated Replaced by {@link FontFace#getPdf()}.
	 */
	public String getBoldItalicPdfFont();
	
	/**
	 * 
	 */
	public String getPdfEncoding();
	
	/**
	 * 
	 */
	public Boolean isPdfEmbedded();
	
	/**
	 * 
	 */
	public String getExportFont(String key);
	
	/**
	 * 
	 */
	public boolean supportsLocale(Locale locale);
	
	/**
	 * 
	 */
	public boolean isVisible();

}
