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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.fill.JRMeasuredText;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextMeasurerUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public abstract class TextElementConverter extends ElementConverter
{

	/**
	 *
	 */
	protected void copyTextElement(ReportConverter reportConverter, JRTextElement textElement, JRPrintText printText)
	{
		copyElement(reportConverter, textElement, printText);
		
		copyBox(textElement, printText);
		
		printText.setBold(textElement.isOwnBold());
		printText.setFontName(textElement.getOwnFontName());
		printText.setFontSize(textElement.getOwnFontSize());
		printText.setHorizontalAlignment(textElement.getOwnHorizontalAlignment());
		printText.setItalic(textElement.isOwnItalic());
		printText.setLineSpacing(textElement.getOwnLineSpacing());
		printText.setPdfEmbedded(textElement.isOwnPdfEmbedded());
		printText.setPdfEncoding(textElement.getOwnPdfEncoding());
		printText.setPdfFontName(textElement.getOwnPdfFontName());
		printText.setReportFont(textElement.getReportFont());
		printText.setRotation(textElement.getOwnRotation());
		printText.setStrikeThrough(textElement.isOwnStrikeThrough());
		printText.setStyledText(textElement.isOwnStyledText());
		printText.setUnderline(textElement.isOwnUnderline());
		printText.setVerticalAlignment(textElement.getOwnVerticalAlignment());
	}

	
	/**
	 * 
	 */
	protected void measureTextElement(JRPrintText printText)
	{
		JRTextMeasurer textMeasurer = JRTextMeasurerUtil.createTextMeasurer(printText);//FIXME use element properties?
		JRStyledText styledText = getStyledText(printText);
		JRMeasuredText measuredText = textMeasurer.measure(
				styledText, 
				styledText.getText(),
				0,
				0
				);
		printText.setTextHeight(measuredText.getTextHeight() < printText.getHeight() ? measuredText.getTextHeight() : printText.getHeight());
		printText.setLeadingOffset(measuredText.getLeadingOffset());
		printText.setLineSpacingFactor(measuredText.getLineSpacingFactor());
	}

	
	/**
	 *
	 */
	protected abstract JRStyledText getStyledText(JRPrintText printText);


}
