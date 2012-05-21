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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.fonts.FontFamily;

/**
 * @deprecated Replaced by {@link FontFamily#getExportFont(String)}.
 * 
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class PdfFont
{
	private String pdfFontName;
	private String pdfEncoding;
	private boolean isPdfEmbedded;
	private boolean isPdfSimulatedBold;
	private boolean isPdfSimulatedItalic;

	
	public PdfFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded)
	{
		this(pdfFontName, pdfEncoding, isPdfEmbedded, false, false);
	}

	public PdfFont(
		String pdfFontName, 
		String pdfEncoding, 
		boolean isPdfEmbedded,
		boolean isPdfSimulatedBold,
		boolean isPdfSimulatedItalic
		)
	{
		this.pdfFontName = pdfFontName;
		this.pdfEncoding = pdfEncoding;
		this.isPdfEmbedded = isPdfEmbedded;
		this.isPdfSimulatedBold = isPdfSimulatedBold;
		this.isPdfSimulatedItalic = isPdfSimulatedItalic;
	}

	public String getPdfFontName()
	{
		return pdfFontName;
	}

	public String getPdfEncoding()
	{
		return pdfEncoding;
	}

	public boolean isPdfEmbedded()
	{
		return isPdfEmbedded;
	}

	public boolean isPdfSimulatedBold()
	{
		return isPdfSimulatedBold;
	}

	public boolean isPdfSimulatedItalic()
	{
		return isPdfSimulatedItalic;
	}

}
