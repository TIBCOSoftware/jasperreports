/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRVerticalFiller extends JRBaseFiller
{


	/**
	 *
	 */
	private boolean isCreatingNewPage = false;
	private boolean isNewPage = false;
	private boolean isNewColumn = false;
	private boolean isNewGroup = true;

	private int columnIndex = 0;

	private int offsetX = 0;
	private int offsetY = 0;
	private int columnHeaderOffsetY = 0;
	private int columnFooterOffsetY = 0;
	private int lastPageColumnFooterOffsetY = 0;

	private boolean isLastPageFooter = false;


	/**
	 *
	 */
	protected JRVerticalFiller(JasperReport jasperReport) throws JRException
	{
		this(jasperReport, null);
	}

	/**
	 *
	 */
	protected JRVerticalFiller(JasperReport jasperReport, JRBaseFiller parentFiller) throws JRException
	{
		super(jasperReport, parentFiller);

		setPageHeight(pageHeight);
	}


	/**
	 *
	 */
	protected void setPageHeight(int pageHeight) throws JRException
	{
		this.pageHeight = pageHeight;

		columnFooterOffsetY = pageHeight - bottomMargin;
		if (pageFooter != null)
			columnFooterOffsetY -= pageFooter.getHeight();
		if (columnFooter != null)
			columnFooterOffsetY -= columnFooter.getHeight();

		lastPageColumnFooterOffsetY = pageHeight - bottomMargin;
		if (lastPageFooter != null)
			lastPageColumnFooterOffsetY -= lastPageFooter.getHeight();
		if (columnFooter != null)
			lastPageColumnFooterOffsetY -= columnFooter.getHeight();
	}


	/**
	 *
	 */
	protected synchronized void fillReport() throws JRException
	{
		setLastPageFooter(false);

		if (next())
		{
			fillReportStart();

			while (next())
			{
				fillReportContent();
			}
			
			fillReportEnd();
		}
		else
		{
			switch (whenNoDataType)
			{
				case JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL :
				{
					scriptlet.callBeforeReportInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_REPORT);
					scriptlet.callAfterReportInit();
			
					printPage = new JRBasePrintPage();
					jasperPrint.addPage(printPage);
					columnIndex = 0;
					offsetX = leftMargin;
					offsetY = topMargin;
			
					fillBackground();
					
					fillTitle();
					
					fillPageHeader(JRExpression.EVALUATION_DEFAULT);
			
					fillColumnHeader(JRExpression.EVALUATION_DEFAULT);
			
					fillGroupHeaders(true);
			
					fillGroupFooters(true);
			
					fillSummary();

					break;
				}
				case JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE :
				{
					printPage = new JRBasePrintPage();
					jasperPrint.addPage(printPage);
					break;
				}
				case JRReport.WHEN_NO_DATA_TYPE_NO_PAGES :
				default :
				{
				}
			}
		}

		if (isSubreport())
		{
			//if (
			//	columnIndex == 0 ||
			//	(columnIndex > 0 && printPageStretchHeight < offsetY + bottomMargin)
			//	)
			//{
				printPageStretchHeight = offsetY + bottomMargin;
			//}
		}
	}


	/**
	 *
	 */
	private void fillReportStart() throws JRException
	{
		scriptlet.callBeforeReportInit();
		calculator.initializeVariables(JRVariable.RESET_TYPE_REPORT);
		scriptlet.callAfterReportInit();

		printPage = new JRBasePrintPage();
		jasperPrint.addPage(printPage);
		columnIndex = 0;
		offsetX = leftMargin;
		offsetY = topMargin;

		fillBackground();

		fillTitle();
		
		fillPageHeader(JRExpression.EVALUATION_DEFAULT);

		fillColumnHeader(JRExpression.EVALUATION_DEFAULT);

		fillGroupHeaders(true);

		fillDetail();
	}


	/**
	 *
	 */
	private void fillReportContent() throws JRException
	{
		calculator.estimateGroupRuptures();

		fillGroupFooters(false);

		resolveGroupBoundImages(JRExpression.EVALUATION_OLD, false);
		resolveGroupBoundTexts(JRExpression.EVALUATION_OLD, false);
		scriptlet.callBeforeGroupInit();
		calculator.initializeVariables(JRVariable.RESET_TYPE_GROUP);
		scriptlet.callAfterGroupInit();

		fillGroupHeaders(false);

		fillDetail();
	}


	/**
	 *
	 */
	private void fillReportEnd() throws JRException
	{
		fillGroupFooters(true);

		fillSummary();
	}


	/**
	 *
	 */
	 private void fillTitle() throws JRException
	 {
		title.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (title.isToPrint())
		{
			while (
				title.getHeight() > pageHeight - bottomMargin - offsetY
				)
			{
				addPage(false);
			}
	
			title.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = title.fill(pageHeight - bottomMargin - offsetY - title.getHeight());
			
			if (title.willOverflow() && !title.isSplitAllowed() && isSubreport())
			{
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
	
				addPage(false);
	
				printBand = title.refill(pageHeight - bottomMargin - offsetY - title.getHeight());
			}

			fillBand(printBand);
			offsetY += printBand.getHeight();
	
			while (title.willOverflow())
			{
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
	
				addPage(false);
	
				printBand = title.fill(pageHeight - bottomMargin - offsetY - title.getHeight());
	
				fillBand(printBand);
				offsetY += printBand.getHeight();
			}

			if (isTitleNewPage)
			{
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
	
				addPage(false);
			}
		}
	}


	/**
	 *
	 */
	private void fillPageHeader(byte evaluation) throws JRException
	{
		setNewPageColumnInBands();

		pageHeader.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (pageHeader.isToPrint())
		{
			while (
				pageHeader.getHeight() > columnFooterOffsetY - offsetY
				)
			{
				resolveGroupBoundImages(evaluation, false);
				resolveColumnBoundImages(evaluation);
				resolvePageBoundImages(evaluation);
				resolveGroupBoundTexts(evaluation, false);
				resolveColumnBoundTexts(evaluation);
				resolvePageBoundTexts(evaluation);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
		
				addPage(false);
			}
	
			fillPageBand(pageHeader, evaluation);
		}

		columnHeaderOffsetY = offsetY;
		
		isNewPage = true;
	}


	/**
	 *
	 */
	private void fillColumnHeader(byte evaluation) throws JRException
	{
		setNewPageColumnInBands();

		columnHeader.evaluatePrintWhenExpression(evaluation);
		
		if (columnHeader.isToPrint())
		{
			while (
				columnHeader.getHeight() > columnFooterOffsetY - offsetY
				)
			{
				if (columnIndex == columnCount - 1)
				{
					fillPageFooter(evaluation);
					
					resolveGroupBoundImages(evaluation, false);
					resolveColumnBoundImages(evaluation);
					resolvePageBoundImages(evaluation);
					resolveGroupBoundTexts(evaluation, false);
					resolveColumnBoundTexts(evaluation);
					resolvePageBoundTexts(evaluation);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
					scriptlet.callAfterPageInit();
			
					addPage(false);
			
					fillPageHeader(evaluation);
				}
				else
				{
					resolveGroupBoundImages(evaluation, false);
					resolveColumnBoundImages(evaluation);
					resolveGroupBoundTexts(evaluation, false);
					resolveColumnBoundTexts(evaluation);
					scriptlet.callBeforeColumnInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_COLUMN);
					scriptlet.callAfterColumnInit();
		
					columnIndex += 1;
					offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
					offsetY = columnHeaderOffsetY;
			
					calculator.getColumnNumber().setValue(
						new Integer(((Number)calculator.getColumnNumber().getValue()).intValue() + 1)
						);
					calculator.getColumnNumber().setOldValue(
						calculator.getColumnNumber().getValue()
						);
				}
			}
	
			fillColumnBand(columnHeader, evaluation);
		}

		isNewColumn = true;
	}


	/**
	 *
	 */
	private void fillGroupHeaders(boolean isFillAll) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				if(isFillAll)
				{
					fillGroupHeader(groups[i]);
				}
				else
				{
					if (groups[i].hasChanged())
					{
						fillGroupHeader(groups[i]);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void fillGroupHeader(JRFillGroup group) throws JRException
	{
		byte evalPrevPage = (group.isTopLevelChange()?JRExpression.EVALUATION_OLD:JRExpression.EVALUATION_DEFAULT);

		if ( (group.isStartNewPage() || group.isResetPageNumber()) && !isNewPage )
		{
			fillPageBreak(
				group.isResetPageNumber(), 
				evalPrevPage, 
				JRExpression.EVALUATION_DEFAULT,
				true
				);
		}
		else if ( group.isStartNewColumn() && !isNewColumn )
		{
			fillColumnBreak(
				evalPrevPage,
				JRExpression.EVALUATION_DEFAULT
				);
		}
		
		JRFillBand groupHeader = (JRFillBand)group.getGroupHeader();

		groupHeader.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);
		
		if (groupHeader.isToPrint())
		{
			while (
				groupHeader.getHeight() > columnFooterOffsetY - offsetY ||
				group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
				)
			{
				fillColumnBreak(
					evalPrevPage,
					JRExpression.EVALUATION_DEFAULT
					);
			}
		}

		setNewGroupInBands(group);

		group.setFooterPrinted(false);

		if (groupHeader.isToPrint())
		{
			fillColumnBand(groupHeader, JRExpression.EVALUATION_DEFAULT);
		}

		isNewGroup = true;
	}


	/**
	 *
	 */
	private void fillGroupHeadersReprint(byte evaluation) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				fillGroupHeaderReprint(groups[i], evaluation);
			}
		}
	}


	/**
	 *
	 */
	 private void fillGroupHeaderReprint(JRFillGroup group, byte evaluation) throws JRException
	 {
		if (
			group.isReprintHeaderOnEachPage() && 
			(!group.hasChanged() || (group.hasChanged() && !group.isFooterPrinted())) 
			)
		{
			JRFillBand groupHeader = (JRFillBand)group.getGroupHeader();

			groupHeader.evaluatePrintWhenExpression(evaluation);
		
			if (groupHeader.isToPrint())
			{
				while (
					groupHeader.getHeight() > columnFooterOffsetY - offsetY ||
					group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
					)
				{
					fillColumnBreak(evaluation, evaluation);
				}
	
				fillColumnBand(groupHeader, evaluation);
			}
		}
	}


	/**
	 *
	 */
	private void fillDetail() throws JRException
	{
		if (!detail.isPrintWhenExpressionNull())
		{
			calculator.estimateVariables();
			detail.evaluatePrintWhenExpression(JRExpression.EVALUATION_ESTIMATED);
		}
	
		if (detail.isToPrint())
		{
			while (
				detail.getHeight() > columnFooterOffsetY - offsetY
				)
			{
				byte evalPrevPage = (isNewGroup?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD);
				
				fillColumnBreak(
					evalPrevPage,
					JRExpression.EVALUATION_DEFAULT
					);
			}
		}

		scriptlet.callBeforeDetailEval();
		calculator.calculateVariables();
		scriptlet.callAfterDetailEval();

		if (!detail.isPrintWhenExpressionNull())
		{
			detail.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);
		}

		if (detail.isToPrint())
		{
			fillColumnBand(detail, JRExpression.EVALUATION_DEFAULT);
		}

		isNewPage = false;
		isNewColumn = false;
		isNewGroup = false;
	}


	/**
	 *
	 */
	private void fillGroupFooters(boolean isFillAll) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			byte evaluation = (isFillAll)?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD;
			
			for(int i = groups.length - 1; i >= 0; i--)
			{
				if (isFillAll)
				{
					fillGroupFooter(groups[i], evaluation);
				}
				else
				{
					if (groups[i].hasChanged())
					{
						fillGroupFooter(groups[i], evaluation);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void fillGroupFooter(JRFillGroup group, byte evaluation) throws JRException
	{
		JRFillBand groupFooter = (JRFillBand)group.getGroupFooter();
		
		groupFooter.evaluatePrintWhenExpression(evaluation);
	
		if (groupFooter.isToPrint())
		{
			if (
				groupFooter.getHeight() > columnFooterOffsetY - offsetY
				)
			{
				fillColumnBreak(evaluation, evaluation);
			}
	
			fillColumnBand(groupFooter, evaluation);
		}

		isNewPage = false;
		isNewColumn = false;

		group.setFooterPrinted(true);
	}


	/**
	 *
	 */
	 private void fillColumnFooter(byte evaluation) throws JRException
	 {
		/*
		if (!isSubreport)
		{
			offsetY = columnFooterOffsetY;
		}
		*/

		if (isSubreport() && columnIndex == 0)
		{
			columnFooterOffsetY = offsetY;
		}

		if (!isFloatColumnFooter)
		{
			offsetY = columnFooterOffsetY;
		}

		columnFooter.evaluatePrintWhenExpression(evaluation);

		if (columnFooter.isToPrint())
		{
			fillFixedBand(columnFooter, evaluation);
		}
	}


	/**
	 *
	 */
	private void fillPageFooter(byte evaluation) throws JRException
	{
		JRFillBand crtPageFooter = getCurrentPageFooter();
		
		offsetX = leftMargin;

		if (!isSubreport())
		{
			offsetY = pageHeight - crtPageFooter.getHeight() - bottomMargin;
		}

		crtPageFooter.evaluatePrintWhenExpression(evaluation);

		if (crtPageFooter.isToPrint())
		{
			fillFixedBand(crtPageFooter, evaluation);
		}
	}


	/**
	 *
	 */
	private void fillSummary() throws JRException
	{
		if (lastPageFooter == missingFillBand)
		{
			if (
				!isSummaryNewPage 
				&& columnIndex == 0 
				&& summary.getHeight() <= columnFooterOffsetY - offsetY
				)
			{
				fillSummarySamePage();
			}
			else
			{
				fillSummaryNewPage();
			}
		}
		else
		{
			if (
				!isSummaryNewPage 
				&& columnIndex == 0 
				&& summary.getHeight() <= lastPageColumnFooterOffsetY - offsetY
				)
			{
				setLastPageFooter(true);

				fillSummarySamePage();
			}
			else if (
				!isSummaryNewPage 
				&& columnIndex == 0 
				&& summary.getHeight() <= columnFooterOffsetY - offsetY
				)
			{
				fillSummarySamePageMixedFooters();
			}
			else if (columnIndex == 0 && offsetY <= lastPageColumnFooterOffsetY)
			{
				setLastPageFooter(true);

				fillSummaryNewPage();
			}
			else
			{
				fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);
			
				setLastPageFooter(true);
			
				if (isSummaryNewPage)
				{
					fillSummaryNewPage();
				}
				else
				{
					fillSummarySamePage();
				}
			}
		}

		resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
		resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
		resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
		resolveReportBoundImages();
		resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
		resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
		resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
		resolveReportBoundTexts();
	}
		
		
	/**
	 *
	 */
	private void fillSummarySamePage() throws JRException
	{
		summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (summary != missingFillBand && summary.isToPrint())
		{
			summary.evaluate(JRExpression.EVALUATION_DEFAULT);
		
			JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY - summary.getHeight());
				
			if (summary.willOverflow() && !summary.isSplitAllowed())
			{
				fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
			
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
		
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
		
				addPage(false);
		
				printBand = summary.refill(pageHeight - bottomMargin - offsetY - summary.getHeight());
		
				fillBand(printBand);
				offsetY += printBand.getHeight();
			}
			else
			{
				fillBand(printBand);
				offsetY += printBand.getHeight();
			
				fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
			
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
			}
	
			while (summary.willOverflow())
			{
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
		
				addPage(false);
		
				printBand = summary.fill(pageHeight - bottomMargin - offsetY - summary.getHeight());
		
				fillBand(printBand);
				offsetY += printBand.getHeight();
			}
		}
		else
		{
			fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
		
			fillPageFooter(JRExpression.EVALUATION_DEFAULT);
		}
	}


	/**
	 *
	 */
	private void fillSummarySamePageMixedFooters() throws JRException
	{
		summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (summary != missingFillBand && summary.isToPrint())
		{
			summary.evaluate(JRExpression.EVALUATION_DEFAULT);
		
			JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY - summary.getHeight());
				
			if (summary.willOverflow() && !summary.isSplitAllowed())
			{
				if (offsetY <= lastPageColumnFooterOffsetY)
				{
					setLastPageFooter(true);

					fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
		
					fillPageFooter(JRExpression.EVALUATION_DEFAULT);

					resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
					resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
					scriptlet.callAfterPageInit();
		
					addPage(false);
		
					printBand = summary.refill(pageHeight - bottomMargin - offsetY - summary.getHeight());

					fillBand(printBand);
					offsetY += printBand.getHeight();
				}
				else
				{
					fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);
		
					setLastPageFooter(true);

					printBand = summary.refill(lastPageColumnFooterOffsetY - offsetY - summary.getHeight());
					//printBand = summary.refill(pageHeight - bottomMargin - offsetY - summary.getHeight());

					fillBand(printBand);
					offsetY += printBand.getHeight();

					fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
		
					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}
			}
			else
			{
				fillBand(printBand);
				offsetY += printBand.getHeight();
				
				fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);

				setLastPageFooter(true);
				
				if (summary.willOverflow())
				{
					printBand = summary.fill(lastPageColumnFooterOffsetY - offsetY - summary.getHeight());

					fillBand(printBand);
					offsetY += printBand.getHeight();
				}

				fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
		
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
			}

			while (summary.willOverflow())
			{
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
		
				addPage(false);
		
				printBand = summary.fill(pageHeight - bottomMargin - offsetY - summary.getHeight());
		
				fillBand(printBand);
				offsetY += printBand.getHeight();
			}
		}
		else
		{
			if(offsetY > lastPageColumnFooterOffsetY)
			{
				fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);
			}

			setLastPageFooter(true);

			fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
		
			fillPageFooter(JRExpression.EVALUATION_DEFAULT);
		}
	}


	/**
	 *
	 */
	private void fillSummaryNewPage() throws JRException
	{
		fillColumnFooter(JRExpression.EVALUATION_DEFAULT);
	
		fillPageFooter(JRExpression.EVALUATION_DEFAULT);

		summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);
	
		if (summary != missingFillBand && summary.isToPrint())
		{
			resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
			resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
			resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
			resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
			scriptlet.callBeforePageInit();
			calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
			scriptlet.callAfterPageInit();
	
			addPage(false);
	
			columnIndex = -1;// FIXME why?

			summary.evaluate(JRExpression.EVALUATION_DEFAULT);
				
			JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - summary.getHeight());
				
			if (summary.willOverflow() && !summary.isSplitAllowed() && isSubreport())
			{
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
		
				addPage(false);
		
				printBand = summary.refill(pageHeight - bottomMargin - offsetY - summary.getHeight());
			}

			fillBand(printBand);
			offsetY += printBand.getHeight();
		
			while (summary.willOverflow())
			{
				resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
				resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
		
				addPage(false);
		
				printBand = summary.fill(pageHeight - bottomMargin - offsetY - summary.getHeight());
		
				fillBand(printBand);
				offsetY += printBand.getHeight();
			}
		}
	}


	/**
	 *
	 */
	private void fillBackground() throws JRException
	{
		//offsetX = leftMargin;

		//if (!isSubreport)
		//{
		//	offsetY = pageHeight - pageFooter.getHeight() - bottomMargin;
		//}

		if (background.getHeight() <= pageHeight - bottomMargin - offsetY)
		{
			background.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);
	
			if (background.isToPrint())
			{
					background.evaluate(JRExpression.EVALUATION_DEFAULT);
	
					JRPrintBand printBand = background.fill(pageHeight - bottomMargin - offsetY - background.getHeight());
	
					fillBand(printBand);
					//offsetY += printBand.getHeight();
			}
		}
	}


	/**
	 *
	 */
	private void addPage(boolean isResetPageNumber) throws JRException
	{ 
		if (isSubreport())
		{
			//if (
			//	columnIndex == 0 ||
			//	(columnIndex > 0 && printPageStretchHeight < offsetY + bottomMargin)
			//	)
			//{
				printPageStretchHeight = offsetY + bottomMargin;
			//}

			//signals to the master filler that is has finished the page
			this.notifyAll();

			try
			{
				//waits until the master filler notifies it that can continue with the next page
				this.wait();
			}
			catch(InterruptedException e)
			{
				throw new JRException("Error encountered while waiting on the subreport filling thread.", e);
			}
		}
		
		printPage = newPage();
		if (isResetPageNumber)
		{
			calculator.getPageNumber().setValue(new Integer(1));
		}
		else
		{
			calculator.getPageNumber().setValue(
				new Integer(((Number)calculator.getPageNumber().getValue()).intValue() + 1)
				);
		}

		calculator.getPageNumber().setOldValue(
			calculator.getPageNumber().getValue()
			);

		jasperPrint.addPage(printPage);
		columnIndex = 0;
		offsetX = leftMargin;
		offsetY = topMargin;

		calculator.getColumnNumber().setValue(
			new Integer(((Number)calculator.getColumnNumber().getValue()).intValue() + 1)
			);
		calculator.getColumnNumber().setOldValue(
			calculator.getColumnNumber().getValue()
			);
			
		fillBackground();
	}


	/**
	 *
	 */
	private void fillPageBreak(
		boolean isResetPageNumber, 
		byte evalPrevPage, 
		byte evalNextPage,
		boolean isReprintGroupHeaders
		) throws JRException
	{
		if (isCreatingNewPage)
		{
			throw new JRException("Infinite loop creating new page.");
		}
		
		isCreatingNewPage = true;
		
		fillColumnFooter(evalPrevPage);

		fillPageFooter(evalPrevPage);
		
		resolveGroupBoundImages(evalPrevPage, false);
		resolveColumnBoundImages(evalPrevPage);
		resolvePageBoundImages(evalPrevPage);
		resolveGroupBoundTexts(evalPrevPage, false);
		resolveColumnBoundTexts(evalPrevPage);
		resolvePageBoundTexts(evalPrevPage);
		scriptlet.callBeforePageInit();
		calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
		scriptlet.callAfterPageInit();

		addPage(isResetPageNumber);

		fillPageHeader(evalNextPage);

		fillColumnHeader(evalNextPage);

		if (isReprintGroupHeaders)
		{
			fillGroupHeadersReprint(evalNextPage);
		}

		isCreatingNewPage = false;
	}


	/**
	 *
	 */
	private void fillColumnBreak(
		byte evalPrevPage,
		byte evalNextPage
		) throws JRException
	{
		if (columnIndex == columnCount - 1)
		{
			fillPageBreak(false, evalPrevPage, evalNextPage, true);
		}
		else
		{
			fillColumnFooter(evalPrevPage);

			resolveGroupBoundImages(evalPrevPage, false);
			resolveColumnBoundImages(evalPrevPage);
			resolveGroupBoundTexts(evalPrevPage, false);
			resolveColumnBoundTexts(evalPrevPage);
			scriptlet.callBeforeColumnInit();
			calculator.initializeVariables(JRVariable.RESET_TYPE_COLUMN);
			scriptlet.callAfterColumnInit();

			columnIndex += 1;
			offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
			offsetY = columnHeaderOffsetY;

			calculator.getColumnNumber().setValue(
				new Integer(((Number)calculator.getColumnNumber().getValue()).intValue() + 1)
				);
			calculator.getColumnNumber().setOldValue(
				calculator.getColumnNumber().getValue()
				);
	
			fillColumnHeader(evalNextPage);
		}
	}


	/**
	 *
	 */
	protected void fillPageBand(JRFillBand band, byte evaluation) throws JRException
	{
		band.evaluate(evaluation);

		JRPrintBand printBand = band.fill(columnFooterOffsetY - offsetY - band.getHeight());
		
		if (band.willOverflow() && !band.isSplitAllowed())
		{
			fillPageBreak(false, evaluation, evaluation, true);

			printBand = band.refill(columnFooterOffsetY - offsetY - band.getHeight());
		}
		
		fillBand(printBand);
		offsetY += printBand.getHeight();

		while (band.willOverflow())
		{
			fillPageBreak(false, evaluation, evaluation, true);

			printBand = band.fill(columnFooterOffsetY - offsetY - band.getHeight());

			fillBand(printBand);
			offsetY += printBand.getHeight();
		}
	}


	/**
	 *
	 */
	protected void fillColumnBand(JRFillBand band, byte evaluation) throws JRException
	{
		band.evaluate(evaluation);
		
		JRPrintBand printBand = band.fill(columnFooterOffsetY - offsetY - band.getHeight());
		
		if (band.willOverflow() && !band.isSplitAllowed())
		{
			fillColumnBreak(evaluation, evaluation);

			printBand = band.refill(columnFooterOffsetY - offsetY - band.getHeight());
		}
		
		fillBand(printBand);
		offsetY += printBand.getHeight();

		while (band.willOverflow())
		{
			fillColumnBreak(evaluation, evaluation);

			printBand = band.fill(columnFooterOffsetY - offsetY - band.getHeight());

			fillBand(printBand);
			offsetY += printBand.getHeight();
		}
	}


	/**
	 *
	 */
	protected void fillFixedBand(JRFillBand band, byte evaluation) throws JRException
	{
		band.evaluate(evaluation);

		JRPrintBand printBand = band.fill();

		fillBand(printBand);
		offsetY += printBand.getHeight();
	}


	/**
	 *
	 */
	protected void fillBand(JRPrintBand band) throws JRException
	{
		java.util.List elements = band.getElements();
		
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element = null;
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement)it.next();
				element.setX(element.getX() + offsetX);
				element.setY(element.getY() + offsetY);
				printPage.addElement(element);
			}
		}
	}


	/**
	 *
	 */
	private void setNewPageColumnInBands()
	{
		title.setNewPageColumn(true);
		pageHeader.setNewPageColumn(true);
		columnHeader.setNewPageColumn(true);
		detail.setNewPageColumn(true);
		columnFooter.setNewPageColumn(true);
		pageFooter.setNewPageColumn(true);
		lastPageFooter.setNewPageColumn(true);
		summary.setNewPageColumn(true);
		
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				((JRFillBand)groups[i].getGroupHeader()).setNewPageColumn(true);
				((JRFillBand)groups[i].getGroupFooter()).setNewPageColumn(true);
			}
		}
	}


	/**
	 *
	 */
	private void setNewGroupInBands(JRGroup group)
	{
		title.setNewGroup(group, true);
		pageHeader.setNewGroup(group, true);
		columnHeader.setNewGroup(group, true);
		detail.setNewGroup(group, true);
		columnFooter.setNewGroup(group, true);
		pageFooter.setNewGroup(group, true);
		lastPageFooter.setNewGroup(group, true);
		summary.setNewGroup(group, true);
		
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				((JRFillBand)groups[i].getGroupHeader()).setNewGroup(group, true);
				((JRFillBand)groups[i].getGroupFooter()).setNewGroup(group, true);
			}
		}
	}

	
	/**
	 *
	 */
	private JRFillBand getCurrentPageFooter()
	{
		return isLastPageFooter ? lastPageFooter : pageFooter;
	}


	/**
	 *
	 */
	private void setLastPageFooter(boolean isLastPageFooter)
	{
		this.isLastPageFooter = isLastPageFooter;
		
		if (isLastPageFooter)
		{
			columnFooterOffsetY = lastPageColumnFooterOffsetY;
		}
	}


}
