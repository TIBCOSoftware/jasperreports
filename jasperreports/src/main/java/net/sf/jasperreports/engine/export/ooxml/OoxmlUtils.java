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
package net.sf.jasperreports.engine.export.ooxml;

import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.export.ooxml.type.PaperSizeEnum;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public final class OoxmlUtils {

	public static PaperSizeEnum getSuitablePaperSize(PrintPageFormat pageFormat)
	{
		if (pageFormat != null && pageFormat.getPageWidth() != 0 && pageFormat.getPageHeight() != 0)
		{
			long mmPageWidth = Math.round(((double)pageFormat.getPageWidth() / 72d) * 25.4);
			long mmPageHeight = Math.round(((double)pageFormat.getPageHeight() / 72d) * 25.4);

			for (PaperSizeEnum paperSize : PaperSizeEnum.values())
			{
				if (
					((paperSize.getWidth() == mmPageWidth) && (paperSize.getHeight() == mmPageHeight)) 
					|| ((paperSize.getHeight() == mmPageWidth) && (paperSize.getWidth() == mmPageHeight))
					)
				{
					return paperSize;
				}
			}
		}
		return PaperSizeEnum.UNDEFINED;
	}

}
