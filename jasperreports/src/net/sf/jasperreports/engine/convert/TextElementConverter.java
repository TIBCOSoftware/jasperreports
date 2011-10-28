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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.fill.JRMeasuredText;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.JRTextMeasurerUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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
		printText.setFontSize(textElement.getOwnFontSize());
		printText.setHorizontalAlignment(textElement.getOwnHorizontalAlignmentValue());
		printText.setItalic(textElement.isOwnItalic());
		printText.setPdfEmbedded(textElement.isOwnPdfEmbedded());
		printText.setPdfEncoding(textElement.getOwnPdfEncoding());
		printText.setPdfFontName(textElement.getOwnPdfFontName());
		printText.setRotation(textElement.getOwnRotationValue());
		printText.setStrikeThrough(textElement.isOwnStrikeThrough());
		printText.setMarkup(textElement.getOwnMarkup());
		printText.setUnderline(textElement.isOwnUnderline());
		printText.setVerticalAlignment(textElement.getOwnVerticalAlignmentValue());
	}

	
	/**
	 * 
	 */
	public static void measureTextElement(JRPrintText printText)//FIXMEHANDLER consider putting in JRTextMeasurerUtil
	{
		String text = printText.getText();
		
		JRTextMeasurer textMeasurer = JRTextMeasurerUtil.createTextMeasurer(printText);//FIXME use element properties?
		
		if (text == null)
		{
			text = "";
		}
		JRStyledText styledText = 
			JRStyledTextParser.getInstance().getStyledText(
				JRStyledTextAttributeSelector.NO_BACKCOLOR.getStyledTextAttributes(printText), 
				text, 
				JRCommonText.MARKUP_STYLED_TEXT.equals(printText.getMarkup()),//FIXMEMARKUP only static styled text appears on preview. no other markup
				JRStyledTextAttributeSelector.getTextLocale(printText)
				);
		
		JRMeasuredText measuredText = textMeasurer.measure(
				styledText, 
				0,
				0,
				false
				);
		printText.setTextHeight(measuredText.getTextHeight() < printText.getHeight() ? measuredText.getTextHeight() : printText.getHeight());
		//printText.setLeadingOffset(measuredText.getLeadingOffset());
		//printText.setLineSpacingFactor(measuredText.getLineSpacingFactor());
		
		int textEnd = measuredText.getTextOffset();
		String printedText;
		if (JRCommonText.MARKUP_STYLED_TEXT.equals(printText.getMarkup()))
		{
			printedText = JRStyledTextParser.getInstance().write(styledText, 0, textEnd);
		}
		else
		{
			printedText = text.substring(0, textEnd);
		}
		printText.setText(printedText);
	}


}
