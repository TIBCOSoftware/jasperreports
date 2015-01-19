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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.util.JRTextMeasurerUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class TextElementConverter extends ElementConverter
{
	
	/**
	 *
	 */
	protected void copyTextElement(ReportConverter reportConverter, JRTextElement textElement, JRBasePrintText printText)
	{
		copyElement(reportConverter, textElement, printText);
		
		printText.copyBox(textElement.getLineBox());
		printText.copyParagraph(textElement.getParagraph());
		
		printText.setBold(textElement.isOwnBold());
		printText.setFontName(textElement.getOwnFontName());
		printText.setFontSize(textElement.getOwnFontsize());
		printText.setHorizontalTextAlign(textElement.getOwnHorizontalTextAlign());
		printText.setItalic(textElement.isOwnItalic());
		printText.setPdfEmbedded(textElement.isOwnPdfEmbedded());
		printText.setPdfEncoding(textElement.getOwnPdfEncoding());
		printText.setPdfFontName(textElement.getOwnPdfFontName());
		printText.setRotation(textElement.getOwnRotationValue());
		printText.setStrikeThrough(textElement.isOwnStrikeThrough());
		printText.setMarkup(textElement.getOwnMarkup());
		printText.setUnderline(textElement.isOwnUnderline());
		printText.setVerticalTextAlign(textElement.getOwnVerticalTextAlign());
	}

	
	/**
	 * @deprecated Replaced by {@link JRTextMeasurerUtil#measureTextElement(JRPrintText)}.
	 */
	public static void measureTextElement(JRPrintText printText)
	{
		JRTextMeasurerUtil.getInstance(DefaultJasperReportsContext.getInstance()).measureTextElement(printText);
	}


}
