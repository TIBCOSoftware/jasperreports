/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.fill;

import java.util.Iterator;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRVerticalFiller extends JRBaseFiller
{

	private static final Log log = LogFactory.getLog(JRVerticalFiller.class);

	/**
	 *
	 */
	protected JRVerticalFiller(JasperReport jasperReport) throws JRException
	{
		this(jasperReport, null, null);
	}

	/**
	 *
	 */
	protected JRVerticalFiller(JasperReport jasperReport, JRFillSubreport parentElement) throws JRException
	{
		super(jasperReport, null, parentElement);

		setPageHeight(pageHeight);
	}

	/**
	 *
	 */
	protected JRVerticalFiller(JasperReport jasperReport, JREvaluator evaluator, JRFillSubreport parentElement) throws JRException
	{
		super(jasperReport, evaluator, parentElement);

		setPageHeight(pageHeight);
	}


	/**
	 *
	 */
	protected void setPageHeight(int pageHeight)
	{
		this.pageHeight = pageHeight;

		columnFooterOffsetY = pageHeight - bottomMargin;
		if (pageFooter != null)
			columnFooterOffsetY -= pageFooter.getHeight();
		if (columnFooter != null)
			columnFooterOffsetY -= columnFooter.getHeight();

		lastPageColumnFooterOffsetY = pageHeight - bottomMargin;
		if (lastPageFooter != null)//FIXMENOW testing with null is awkward since bands can never be null, but rather equal to missingFillBand
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
			if (log.isDebugEnabled())
			{
				log.debug("Fill " + fillerId + ": no data");
			}

			switch (whenNoDataType)
			{
				case JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL :
				{
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": all sections");
					}

					scriptlet.callBeforeReportInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_REPORT);
					scriptlet.callAfterReportInit();

					printPage = newPage();
					addPage(printPage);
					setFirstColumn();
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
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": blank page");
					}

					printPage = newPage();
					addPage(printPage);
					break;
				}
				case JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION:
				{
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": all sections");
					}

					scriptlet.callBeforeReportInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_REPORT);
					scriptlet.callAfterReportInit();

					printPage = newPage();
					addPage(printPage);
					setFirstColumn();
					offsetY = topMargin;

					fillBackground();

					fillNoData();
					
					break;

				}
				case JRReport.WHEN_NO_DATA_TYPE_NO_PAGES :
				default :
				{
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": no pages");
					}
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

			if (fillContext.isUsingVirtualizer())
			{
				removePageIdentityDataProvider();
			}
		}

		if (fillContext.isIgnorePagination())
		{
			jasperPrint.setPageHeight(offsetY + bottomMargin);
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

		printPage = newPage();
		addPage(printPage);
		setFirstColumn();
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

		resolveGroupBoundElements(JRExpression.EVALUATION_OLD, false);
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
		if (log.isDebugEnabled() && !title.isEmpty())
		{
			log.debug("Fill " + fillerId + ": title");
		}

		title.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (title.isToPrint())
		{
			while (
				title.getBreakHeight() > pageHeight - bottomMargin - offsetY
				)
			{
				addPage(false);
			}

			title.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = title.fill(pageHeight - bottomMargin - offsetY);

			if (title.willOverflow() && title.isSplitPrevented() && isSubreport())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = title.refill(pageHeight - bottomMargin - offsetY);
			}

			fillBand(printBand);
			offsetY += printBand.getHeight();

			while (title.willOverflow())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = title.fill(pageHeight - bottomMargin - offsetY);

				fillBand(printBand);
				offsetY += printBand.getHeight();
			}

			resolveBandBoundElements(title, JRExpression.EVALUATION_DEFAULT);

			if (isTitleNewPage)
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
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
		if (log.isDebugEnabled() && !pageHeader.isEmpty())
		{
			log.debug("Fill " + fillerId + ": page header");
		}

		setNewPageColumnInBands();

		pageHeader.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (pageHeader.isToPrint())
		{
			int reattempts = getMasterColumnCount();
			if (isCreatingNewPage)
			{
				--reattempts;
			}

			boolean filled = fillBandNoOverflow(pageHeader, evaluation);

			for (int i = 0; !filled && i < reattempts; ++i)
			{
				resolveGroupBoundElements(evaluation, false);
				resolveColumnBoundElements(evaluation);
				resolvePageBoundElements(evaluation);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				filled = fillBandNoOverflow(pageHeader, evaluation);
			}

			if (!filled)
			{
				throw new JRRuntimeException("Infinite loop creating new page due to page header overflow.");
			}
		}

		columnHeaderOffsetY = offsetY;

		isNewPage = true;
		isFirstPageBand = true;
	}


	private boolean fillBandNoOverflow(JRFillBand band, byte evaluation) throws JRException
	{
		int availableHeight = columnFooterOffsetY - offsetY;
		boolean overflow = availableHeight < band.getHeight();

		if (!overflow)
		{
			band.evaluate(evaluation);
			JRPrintBand printBand = band.fill(availableHeight);

			overflow = band.willOverflow();
			if (overflow)
			{
				band.rewind();
			}
			else
			{
				fillBand(printBand);
				offsetY += printBand.getHeight();

				resolveBandBoundElements(band, evaluation);
			}
		}

		return !overflow;
	}


	/**
	 *
	 */
	private void fillColumnHeader(byte evaluation) throws JRException
	{
		if (log.isDebugEnabled() && !columnHeader.isEmpty())
		{
			log.debug("Fill " + fillerId + ": column header");
		}

		setNewPageColumnInBands();

		columnHeader.evaluatePrintWhenExpression(evaluation);

		if (columnHeader.isToPrint())
		{
			int reattempts = getMasterColumnCount();
			if (isCreatingNewPage)
			{
				--reattempts;
			}

			boolean filled = fillBandNoOverflow(columnHeader, evaluation);

			for (int i = 0; !filled && i < reattempts; ++i)
			{
				while (columnIndex < columnCount - 1)
				{
					resolveGroupBoundElements(evaluation, false);
					resolveColumnBoundElements(evaluation);
					scriptlet.callBeforeColumnInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_COLUMN);
					scriptlet.callAfterColumnInit();

					columnIndex += 1;
					offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
					offsetY = columnHeaderOffsetY;

					setColumnNumberVar();
				}

				fillPageFooter(evaluation);

				resolveGroupBoundElements(evaluation, false);
				resolveColumnBoundElements(evaluation);
				resolvePageBoundElements(evaluation);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				fillPageHeader(evaluation);

				filled = fillBandNoOverflow(columnHeader, evaluation);
			}

			if (!filled)
			{
				throw new JRRuntimeException("Infinite loop creating new page due to column header overflow.");
			}
		}

		isNewColumn = true;
		isFirstColumnBand = true;
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
		JRFillSection groupHeaderSection = (JRFillSection)group.getGroupHeaderSection();

		if (log.isDebugEnabled() && !groupHeaderSection.isEmpty())
		{
			log.debug("Fill " + fillerId + ": " + group.getName() + " header");
		}

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

		JRBand[] groupHeaderBands = groupHeaderSection.getBands();
		if (groupHeaderBands != null)
		{
			for(int i = 0; i < groupHeaderBands.length; i++)
			{
				JRFillBand groupHeaderBand = (JRFillBand)groupHeaderBands[i];

				groupHeaderBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

				if (groupHeaderBand.isToPrint())
				{
					while (
						groupHeaderBand.getBreakHeight() > columnFooterOffsetY - offsetY ||
						group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
						)
					{
						fillColumnBreak(
							evalPrevPage,
							JRExpression.EVALUATION_DEFAULT
							);
					}
				}

				if (i == 0)
				{
					setNewGroupInBands(group);

					group.setFooterPrinted(false);
				}

				if (groupHeaderBand.isToPrint())
				{
					fillColumnBand(groupHeaderBand, JRExpression.EVALUATION_DEFAULT);

					isFirstPageBand = false;
					isFirstColumnBand = false;
				}
			}
		}

		group.setHeaderPrinted(true);

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
			(!group.hasChanged() || (group.hasChanged() && group.isHeaderPrinted()))
			)
		{
			JRFillSection groupHeaderSection = (JRFillSection)group.getGroupHeaderSection();

			JRBand[] groupHeaderBands = groupHeaderSection.getBands();
			if (groupHeaderBands != null)
			{
				for(int i = 0; i < groupHeaderBands.length; i++)
				{
					JRFillBand groupHeaderBand = (JRFillBand)groupHeaderBands[i];

					groupHeaderBand.evaluatePrintWhenExpression(evaluation);

					if (groupHeaderBand.isToPrint())
					{
						while (
							groupHeaderBand.getBreakHeight() > columnFooterOffsetY - offsetY 
							|| group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
							)
						{
							fillColumnBreak(evaluation, evaluation);
						}

						fillColumnBand(groupHeaderBand, evaluation);

						isFirstPageBand = false;
						isFirstColumnBand = false;
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void fillDetail() throws JRException
	{
		if (log.isDebugEnabled() && !detailSection.isEmpty())
		{
			log.debug("Fill " + fillerId + ": detail");
		}

		if (!detailSection.areAllPrintWhenExpressionsNull())
		{
			calculator.estimateVariables();
		}

		JRBand[] detailBands = detailSection.getBands();
		if (detailBands != null)
		{
			for(int i = 0; i < detailBands.length; i++)
			{
				JRFillBand detailBand = (JRFillBand)detailBands[i];
				
				detailBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_ESTIMATED);

				if (detailBand.isToPrint())
				{
					while (
						detailBand.getBreakHeight() > columnFooterOffsetY - offsetY
						)
					{
						byte evalPrevPage = (isNewGroup?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD);

						fillColumnBreak(
							evalPrevPage,
							JRExpression.EVALUATION_DEFAULT
							);
					}
					
					break;
				}
			}
		}

		scriptlet.callBeforeDetailEval();
		calculator.calculateVariables();
		scriptlet.callAfterDetailEval();

		if (detailBands != null)
		{
			for(int i = 0; i < detailBands.length; i++)
			{
				JRFillBand detailBand = (JRFillBand)detailBands[i];
						
				detailBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

				if (detailBand.isToPrint())
				{
					fillColumnBand(detailBand, JRExpression.EVALUATION_DEFAULT);

					isFirstPageBand = false;
					isFirstColumnBand = false;
				}
			}
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
		JRFillSection groupFooterSection = (JRFillSection)group.getGroupFooterSection();

		if (log.isDebugEnabled() && !groupFooterSection.isEmpty())
		{
			log.debug("Fill " + fillerId + ": " + group.getName() + " footer");
		}

		JRBand[] groupFooterBands = groupFooterSection.getBands();
		if (groupFooterBands != null)
		{
			for(int i = 0; i < groupFooterBands.length; i++)
			{
				JRFillBand groupFooterBand = (JRFillBand)groupFooterBands[i];
				
				groupFooterBand.evaluatePrintWhenExpression(evaluation);

				if (groupFooterBand.isToPrint())
				{
					if (
						groupFooterBand.getBreakHeight() > columnFooterOffsetY - offsetY
						)
					{
						fillColumnBreak(evaluation, evaluation);
					}

					fillColumnBand(groupFooterBand, evaluation);

					isFirstPageBand = false;
					isFirstColumnBand = false;
				}
			}
		}

		isNewPage = false;
		isNewColumn = false;

		group.setHeaderPrinted(false);
		group.setFooterPrinted(true);
	}


	/**
	 *
	 */
	 private void fillColumnFooter(byte evaluation) throws JRException
	 {
		if (log.isDebugEnabled() && !columnFooter.isEmpty())
		{
			log.debug("Fill " + fillerId + ": column footer");
		}

		/*
		if (!isSubreport)
		{
			offsetY = columnFooterOffsetY;
		}
		*/

		if (isSubreport() && !isSubreportRunToBottom() && columnIndex == 0)
		{
			columnFooterOffsetY = offsetY;
		}

		int oldOffsetY = offsetY;
		if (!isFloatColumnFooter && !fillContext.isIgnorePagination())
		{
			offsetY = columnFooterOffsetY;
		}

		columnFooter.evaluatePrintWhenExpression(evaluation);

		if (columnFooter.isToPrint())
		{
			fillFixedBand(columnFooter, evaluation);
		}

		if (isFloatColumnFooter && !fillContext.isIgnorePagination())
		{
			offsetY += columnFooterOffsetY - oldOffsetY;
		}
	}


	/**
	 *
	 */
	private void fillPageFooter(byte evaluation) throws JRException
	{
		JRFillBand crtPageFooter = getCurrentPageFooter();

		if (log.isDebugEnabled() && !crtPageFooter.isEmpty())
		{
			log.debug("Fill " + fillerId + ": " + (isLastPageFooter ? "last " : "") + "page footer");
		}

		offsetX = leftMargin;

		if ((!isSubreport() || isSubreportRunToBottom()) && !fillContext.isIgnorePagination())
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
		if (log.isDebugEnabled() && !summary.isEmpty())
		{
			log.debug("Fill " + fillerId + ": summary");
		}

		if (lastPageFooter == missingFillBand)
		{
			if (
				!isSummaryNewPage
				&& columnIndex == 0
				&& summary.getBreakHeight() <= columnFooterOffsetY - offsetY
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
				&& summary.getBreakHeight() <= lastPageColumnFooterOffsetY - offsetY
				)
			{
				setLastPageFooter(true);

				fillSummarySamePage();
			}
			else if (
				!isSummaryNewPage
				&& columnIndex == 0
				&& summary.getBreakHeight() <= columnFooterOffsetY - offsetY
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

		resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
		resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
		resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
		resolveReportBoundElements();
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

			JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

			if (summary.willOverflow() && summary.isSplitPrevented())
			{
				fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);

				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = summary.refill(pageHeight - bottomMargin - offsetY);

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
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = summary.fill(pageHeight - bottomMargin - offsetY);

				fillBand(printBand);
				offsetY += printBand.getHeight();
			}

			resolveBandBoundElements(summary, JRExpression.EVALUATION_DEFAULT);
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

			JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

			if (summary.willOverflow() && summary.isSplitPrevented())
			{
				if (offsetY <= lastPageColumnFooterOffsetY)
				{
					setLastPageFooter(true);

					fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);

					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);

					printBand = summary.refill(pageHeight - bottomMargin - offsetY);

					fillBand(printBand);
					offsetY += printBand.getHeight();
				}
				else
				{
					fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);

					setLastPageFooter(true);

					printBand = summary.refill(lastPageColumnFooterOffsetY - offsetY);

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
					printBand = summary.fill(lastPageColumnFooterOffsetY - offsetY);

					fillBand(printBand);
					offsetY += printBand.getHeight();
				}

				fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
			}

			while (summary.willOverflow())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = summary.fill(pageHeight - bottomMargin - offsetY);

				fillBand(printBand);
				offsetY += printBand.getHeight();
			}

			resolveBandBoundElements(summary, JRExpression.EVALUATION_DEFAULT);
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
			resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
			resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
			scriptlet.callBeforePageInit();
			calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
			scriptlet.callAfterPageInit();

			addPage(false);

			columnIndex = -1;// FIXME why?

			summary.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY);

			if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = summary.refill(pageHeight - bottomMargin - offsetY);
			}

			fillBand(printBand);
			offsetY += printBand.getHeight();

			while (summary.willOverflow())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = summary.fill(pageHeight - bottomMargin - offsetY);

				fillBand(printBand);
				offsetY += printBand.getHeight();
			}

			resolveBandBoundElements(summary, JRExpression.EVALUATION_DEFAULT);
		}
	}


	/**
	 *
	 */
	private void fillBackground() throws JRException
	{
		if (log.isDebugEnabled() && !background.isEmpty())
		{
			log.debug("Fill " + fillerId + ": background");
		}

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

					JRPrintBand printBand = background.fill(pageHeight - bottomMargin - offsetY);

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
			if (!parentFiller.isBandOverFlowAllowed())
			{
				throw new JRRuntimeException("Subreport overflowed on a band that does not support overflow.");
			}

			//if (
			//	columnIndex == 0 ||
			//	(columnIndex > 0 && printPageStretchHeight < offsetY + bottomMargin)
			//	)
			//{
				printPageStretchHeight = offsetY + bottomMargin;
			//}

			if (fillContext.isUsingVirtualizer())
			{
				removePageIdentityDataProvider();
			}

			suspendSubreportRunner();
		}

		printPage = newPage();

		if (isSubreport() && fillContext.isUsingVirtualizer())
		{
			addPageIdentityDataProvider();
		}

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

		addPage(printPage);

		setFirstColumn();
		offsetY = topMargin;

		fillBackground();
	}

	private void setFirstColumn()
	{
		columnIndex = 0;
		offsetX = leftMargin;
		setColumnNumberVar();
	}

	private void setColumnNumberVar()
	{
		JRFillVariable columnNumber = calculator.getColumnNumber();
		columnNumber.setValue(new Integer(columnIndex + 1));
		columnNumber.setOldValue(columnNumber.getValue());
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

		resolveGroupBoundElements(evalPrevPage, false);
		resolveColumnBoundElements(evalPrevPage);
		resolvePageBoundElements(evalPrevPage);
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

			resolveGroupBoundElements(evalPrevPage, false);
			resolveColumnBoundElements(evalPrevPage);
			scriptlet.callBeforeColumnInit();
			calculator.initializeVariables(JRVariable.RESET_TYPE_COLUMN);
			scriptlet.callAfterColumnInit();

			columnIndex += 1;
			offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
			offsetY = columnHeaderOffsetY;

			setColumnNumberVar();

			fillColumnHeader(evalNextPage);
		}
	}


	/**
	 *
	 */
	protected void fillPageBand(JRFillBand band, byte evaluation) throws JRException
	{
		band.evaluate(evaluation);

		JRPrintBand printBand = band.fill(columnFooterOffsetY - offsetY);

		if (band.willOverflow() && band.isSplitPrevented())
		{
			fillPageBreak(false, evaluation, evaluation, true);

			printBand = band.refill(columnFooterOffsetY - offsetY);
		}

		fillBand(printBand);
		offsetY += printBand.getHeight();

		while (band.willOverflow())
		{
			fillPageBreak(false, evaluation, evaluation, true);

			printBand = band.fill(columnFooterOffsetY - offsetY);

			fillBand(printBand);
			offsetY += printBand.getHeight();
		}

		resolveBandBoundElements(band, evaluation);
	}


	/**
	 *
	 */
	protected void fillColumnBand(JRFillBand band, byte evaluation) throws JRException
	{
		band.evaluate(evaluation);

		JRPrintBand printBand = band.fill(columnFooterOffsetY - offsetY);

		if (band.willOverflow() && band.isSplitPrevented())
		{
			fillColumnBreak(evaluation, evaluation);

			printBand = band.refill(columnFooterOffsetY - offsetY);
		}

		fillBand(printBand);
		offsetY += printBand.getHeight();

		while (band.willOverflow())
		{
			fillColumnBreak(evaluation, evaluation);

			printBand = band.fill(columnFooterOffsetY - offsetY);

			fillBand(printBand);
			offsetY += printBand.getHeight();
		}

		resolveBandBoundElements(band, evaluation);
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

		resolveBandBoundElements(band, evaluation);
	}


	/**
	 *
	 */
	protected void fillBand(JRPrintBand band)
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
		detailSection.setNewPageColumn(true);
		columnFooter.setNewPageColumn(true);
		pageFooter.setNewPageColumn(true);
		lastPageFooter.setNewPageColumn(true);
		summary.setNewPageColumn(true);
		noData.setNewPageColumn(true);

		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				((JRFillSection)groups[i].getGroupHeaderSection()).setNewPageColumn(true);
				((JRFillSection)groups[i].getGroupFooterSection()).setNewPageColumn(true);
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
		detailSection.setNewGroup(group, true);
		columnFooter.setNewGroup(group, true);
		pageFooter.setNewGroup(group, true);
		lastPageFooter.setNewGroup(group, true);
		summary.setNewGroup(group, true);
		noData.setNewGroup(group, true);

		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				((JRFillSection)groups[i].getGroupHeaderSection()).setNewGroup(group, true);
				((JRFillSection)groups[i].getGroupFooterSection()).setNewGroup(group, true);
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

	/**
	 *
	 */
	private void fillNoData() throws JRException
	{
		if (log.isDebugEnabled() && !noData.isEmpty())
		{
			log.debug("Fill " + fillerId + ": noData");
		}

		noData.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (noData.isToPrint())
		{
			while (noData.getBreakHeight() > pageHeight - bottomMargin - offsetY)
			{
				addPage(false);
			}

			noData.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = noData.fill(pageHeight - bottomMargin - offsetY);

			if (noData.willOverflow() && noData.isSplitPrevented() && isSubreport())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
				
				addPage(false);
				
				printBand = noData.refill(pageHeight - bottomMargin - offsetY);
			}

			fillBand(printBand);
			offsetY += printBand.getHeight();

			while (noData.willOverflow())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
				scriptlet.callAfterPageInit();
				
				addPage(false);
				
				printBand = noData.fill(pageHeight - bottomMargin - offsetY);
				
				fillBand(printBand);
				offsetY += printBand.getHeight();
			}

			resolveBandBoundElements(noData, JRExpression.EVALUATION_DEFAULT);
		}
	}

}
