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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRHorizontalFiller extends JRBaseFiller
{

	private static final Log log = LogFactory.getLog(JRHorizontalFiller.class);

	private int lastDetailOffsetX = -1;
	private int lastDetailOffsetY = -1;
	private int currentDetailOffsetY;
	private int maxDetailOffsetY;
	

	/**
	 *
	 */
	protected JRHorizontalFiller(
		JasperReportsContext jasperReportsContext, 
		JasperReport jasperReport
		) throws JRException
	{
		this(jasperReportsContext, jasperReport, null);
	}

	/**
	 *
	 */
	public JRHorizontalFiller(
		JasperReportsContext jasperReportsContext, 
		JasperReport jasperReport, 
		BandReportFillerParent parent 
		) throws JRException
	{
		super(jasperReportsContext, jasperReport, parent);

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
		{
			columnFooterOffsetY -= pageFooter.getHeight();
		}
		if (columnFooter != null)
		{
			columnFooterOffsetY -= columnFooter.getHeight();
		}
		lastPageColumnFooterOffsetY = pageHeight - bottomMargin;
		if (lastPageFooter != null)//FIXMENOW testing with null is awkward since bands can never be null, but rather equal to missingFillBand
		{
			lastPageColumnFooterOffsetY -= lastPageFooter.getHeight();
		}
		if (columnFooter != null)
		{
			lastPageColumnFooterOffsetY -= columnFooter.getHeight();
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("Filler " + fillerId + " - pageHeight: " + pageHeight
					+ ", columnFooterOffsetY: " + columnFooterOffsetY
					+ ", lastPageColumnFooterOffsetY: " + lastPageColumnFooterOffsetY);
		}
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
				case ALL_SECTIONS_NO_DETAIL :
				{
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": all sections");
					}

					scriptlet.callBeforeReportInit();
					calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
					scriptlet.callAfterReportInit();

					printPage = newPage();
					addPage(printPage);
					setFirstColumn();
					offsetY = topMargin;

					fillBackground();

					fillTitle();

					fillPageHeader(JRExpression.EVALUATION_DEFAULT);

					fillColumnHeaders(JRExpression.EVALUATION_DEFAULT);

					fillGroupHeaders(true);

					fillGroupFooters(true);

					fillSummary();

					break;
				}
				case BLANK_PAGE :
				{
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": blank page");
					}

					printPage = newPage();
					addPage(printPage);
					break;
				}
				case NO_DATA_SECTION:
				{
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": NoData section");
					}

					scriptlet.callBeforeReportInit();
					calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
					scriptlet.callAfterReportInit();

					printPage = newPage();
					addPage(printPage);
					setFirstColumn();
					offsetY = topMargin;

					fillBackground();

					fillNoData();

					break;

				}
				case NO_PAGES :
				default :
				{
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": no pages");
					}
				}
			}
		}

		if (ignorePagination)
		{
			jasperPrint.setPageHeight(offsetY + bottomMargin);
		}

		if (isSubreport())
		{
			addPageToParent(true);
		}
		else
		{
			addLastPageBookmarks();
		}
		
		if (bookmarkHelper != null)
		{
			jasperPrint.setBookmarks(bookmarkHelper.getRootBookmarks());
		}
	}


	/**
	 *
	 */
	private void fillReportStart() throws JRException
	{
		scriptlet.callBeforeReportInit();
		calculator.initializeVariables(ResetTypeEnum.REPORT, IncrementTypeEnum.REPORT);
		scriptlet.callAfterReportInit();

		printPage = newPage();
		addPage(printPage);
		setFirstColumn();
		offsetY = topMargin;

		fillBackground();

		fillTitle();

		fillPageHeader(JRExpression.EVALUATION_DEFAULT);

		fillColumnHeaders(JRExpression.EVALUATION_DEFAULT);

		fillGroupHeaders(true);

		fillDetail();
	}


	private void setFirstColumn()
	{
		columnIndex = 0;
		offsetX = leftMargin;
		setColumnNumberVariable();
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
		calculator.initializeVariables(ResetTypeEnum.GROUP, IncrementTypeEnum.GROUP);
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
			log.debug("Fill " + fillerId + ": title at " + offsetY);
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
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
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
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
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
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
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
			log.debug("Fill " + fillerId + ": page header at " + offsetY);
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
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				filled = fillBandNoOverflow(pageHeader, evaluation);
			}

			if (!filled)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_PAGE_HEADER_OVERFLOW_INFINITE_LOOP,
						(Object[])null);
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
	private void fillColumnHeaders(byte evaluation) throws JRException
	{
		if (log.isDebugEnabled() && !columnHeader.isEmpty())
		{
			log.debug("Fill " + fillerId + ": column headers at " + offsetY);
		}

		setNewPageColumnInBands();

		for(columnIndex = 0; columnIndex < columnCount; columnIndex++)
		{
			setColumnNumberVariable();

			columnHeader.evaluatePrintWhenExpression(evaluation);

			if (columnHeader.isToPrint())
			{
				int reattempts = getMasterColumnCount();
				if (isCreatingNewPage)
				{
					--reattempts;
				}

				boolean fits = columnHeader.getHeight() <= columnFooterOffsetY - offsetY;
				for (int i = 0; !fits && i < reattempts; ++i)
				{
					fillPageFooter(evaluation);

					resolveGroupBoundElements(evaluation, false);
					resolveColumnBoundElements(evaluation);
					resolvePageBoundElements(evaluation);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);

					fillPageHeader(evaluation);

					fits = columnHeader.getHeight() <= columnFooterOffsetY - offsetY;
				}

				if (!fits)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_COLUMN_HEADER_OVERFLOW_INFINITE_LOOP,
							(Object[])null);
				}

				setOffsetX();
				offsetY = columnHeaderOffsetY;

				fillFixedBand(columnHeader, evaluation, false);
			}
		}

		setFirstColumn();

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
				JRFillGroup group = groups[i];

				if(isFillAll || group.hasChanged())
				{
					SavePoint newSavePoint = fillGroupHeader(group);
					// fillGroupHeader never returns null, because we need a save point 
					// regardless of the group header printing or not
					newSavePoint.groupIndex = i;
					
					if (keepTogetherSavePoint == null && group.isKeepTogether())
					{
						keepTogetherSavePoint = newSavePoint;
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private SavePoint fillGroupHeader(JRFillGroup group) throws JRException
	{
		SavePoint savePoint = null;
		
		JRFillSection groupHeaderSection = (JRFillSection)group.getGroupHeaderSection();

		if (log.isDebugEnabled() && !groupHeaderSection.isEmpty())
		{
			log.debug("Fill " + fillerId + ": " + group.getName() + " header at " + offsetY);
		}

		byte evalPrevPage = (group.isTopLevelChange()?JRExpression.EVALUATION_OLD:JRExpression.EVALUATION_DEFAULT);

		if (
			(group.isStartNewPage() || group.isResetPageNumber()) && !isNewPage
			|| ( group.isStartNewColumn() && !isNewColumn )
			)
		{
			fillPageBreak(
				group.isResetPageNumber(),
				evalPrevPage,
				JRExpression.EVALUATION_DEFAULT,
				true
				);
		}

		JRFillBand[] groupHeaderBands = groupHeaderSection.getFillBands();
		for(int i = 0; i < groupHeaderBands.length; i++)
		{
			JRFillBand groupHeaderBand = groupHeaderBands[i];

			groupHeaderBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (groupHeaderBand.isToPrint())
			{
				while (
					groupHeaderBand.getBreakHeight() > columnFooterOffsetY - offsetY ||
					group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
					)
				{
					fillPageBreak(
						false,
						evalPrevPage,
						JRExpression.EVALUATION_DEFAULT,
						true
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
				setFirstColumn();

				SavePoint newSavePoint = fillColumnBand(groupHeaderBand, JRExpression.EVALUATION_DEFAULT);
				
				savePoint = advanceSavePoint(savePoint, newSavePoint);

				isFirstPageBand = false;
				isFirstColumnBand = true;
			}
		}

		group.setHeaderPrinted(true);

		isNewGroup = true;

		if (savePoint == null)
		{
			// fillGroupHeader never returns null, because we need a save point 
			// regardless of the group header printing or not
			savePoint = 
				new SavePoint(
					getCurrentPage(), 
					columnIndex,
					isNewPage,
					isNewColumn,
					offsetY
					);
		}
		
		return savePoint;
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

			JRFillBand[] groupHeaderBands = groupHeaderSection.getFillBands();
			for(int i = 0; i < groupHeaderBands.length; i++)
			{
				JRFillBand groupHeaderBand = groupHeaderBands[i];

				groupHeaderBand.evaluatePrintWhenExpression(evaluation);

				if (groupHeaderBand.isToPrint())
				{
					setFirstColumn();

					while (
						groupHeaderBand.getBreakHeight() > columnFooterOffsetY - offsetY ||
						group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
						)
					{
						fillPageBreak(false, evaluation, evaluation, true);
					}

					fillColumnBand(groupHeaderBand, evaluation);

					isFirstPageBand = false;
					isFirstColumnBand = true;
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
			log.debug("Fill " + fillerId + ": detail at " + offsetY);
		}

		if (!detailSection.areAllPrintWhenExpressionsNull())
		{
			calculator.estimateVariables();
		}

		JRFillBand[] detailBands = detailSection.getFillBands();
		for(int i = 0; i < detailBands.length; i++)
		{
			JRFillBand detailBand = detailBands[i];

			detailBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_ESTIMATED);

			if (detailBand.isToPrint())
			{
				while (
					(columnIndex == columnCount - 1 || isNewGroup)
					&& detailBand.getHeight() > columnFooterOffsetY - offsetY
					)
				{
					byte evalPrevPage = (isNewGroup?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD);

					fillPageBreak(
						false,
						evalPrevPage,
						JRExpression.EVALUATION_DEFAULT,
						true
						);
				}
				
				break;
			}
		}

		scriptlet.callBeforeDetailEval();
		calculator.calculateVariables();
		scriptlet.callAfterDetailEval();
				
		if (detailBands != null)
		{
			if (
				offsetX == lastDetailOffsetX
				&& offsetY == lastDetailOffsetY
				)
			{
				if (columnIndex == columnCount - 1)
				{
					columnIndex = 0;
					setOffsetX();
					setColumnNumberVariable();

					maxDetailOffsetY = 0;
					currentDetailOffsetY = offsetY;
				}
				else
				{
					columnIndex++;
					setOffsetX();
					offsetY = currentDetailOffsetY;

					setColumnNumberVariable();
				}
			}
			else
			{
				setOffsetX();
				currentDetailOffsetY = offsetY;
			}

			for(int i = 0; i < detailBands.length; i++)
			{
				JRFillBand detailBand = detailBands[i];

				detailBand.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

				if (detailBand.isToPrint())
				{
					while (
						//(columnIndex == columnCount - 1 || isNewGroup) &&
						detailBand.getHeight() > columnFooterOffsetY - offsetY
						)
					{
						byte evalPrevPage = (isNewGroup?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD);

						fillPageBreak(
							false,
							evalPrevPage,
							JRExpression.EVALUATION_DEFAULT,
							true
							);

						currentDetailOffsetY = offsetY;
					}
					
					fillFixedBand(detailBand, JRExpression.EVALUATION_DEFAULT, false);

					isFirstPageBand = false;
					isFirstColumnBand = false;
				}
			}

			maxDetailOffsetY = maxDetailOffsetY < offsetY ? offsetY : maxDetailOffsetY;
			offsetY = maxDetailOffsetY;

			lastDetailOffsetX = offsetX;
			lastDetailOffsetY = offsetY;
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
			SavePoint savePoint = null;
			
			byte evaluation = (isFillAll)?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD;

			for(int i = groups.length - 1; i >= 0; i--)
			{
				JRFillGroup group = groups[i];
				
				if (isFillAll || group.hasChanged())
				{
					SavePoint newSavePoint = fillGroupFooter(group, evaluation);
					// fillGroupFooter might return null, because if the group footer does not print, 
					// its footer position is completely irrelevant
					if (newSavePoint != null)
					{
						switch (group.getFooterPositionValue())
						{
							case STACK_AT_BOTTOM:
							{
								savePoint = advanceSavePoint(savePoint, newSavePoint);

								if (savePoint != null)
								{
									savePoint.footerPosition = FooterPositionEnum.STACK_AT_BOTTOM;
								}

								break;
							}
							case FORCE_AT_BOTTOM:
							{
								savePoint = advanceSavePoint(savePoint, newSavePoint);

								if (savePoint != null)
								{
									savePoint.moveSavePointContent();
									offsetY = columnFooterOffsetY;
								}

								savePoint = null;

								break;
							}
							case COLLATE_AT_BOTTOM:
							{
								savePoint = advanceSavePoint(savePoint, newSavePoint);

								break;
							}
							case NORMAL:
							default:
							{
								if (savePoint != null)
								{
									//only "StackAtBottom" and "CollateAtBottom" save points could get here
									
									// check to see if the new save point is on the same page/column as the previous one
									if (
										savePoint.page == newSavePoint.page
										&& savePoint.columnIndex == newSavePoint.columnIndex
										)
									{
										// if the new save point is on the same page/column, 
										// we just move the marker on the existing save point,
										// but only if was a "StackAtBottom" one
										
										if (savePoint.footerPosition == FooterPositionEnum.STACK_AT_BOTTOM)
										{
											savePoint.saveHeightOffset(newSavePoint.heightOffset);
										}
										else
										{
											// we cancel the "CollateAtBottom" save point
											savePoint = null;
										}
									}
									else
									{
										// page/column break occurred, so the move operation 
										// must be performed on the previous save point, regardless 
										// whether it was a "StackAtBottom" or a "CollateAtBottom"
										savePoint.moveSavePointContent();
										savePoint = null;
									}
								}
								else
								{
									// only "ForceAtBottom" save points could get here, but they are already null
									savePoint = null;
								}
							}
						}
					}
					
					// regardless of whether the fillGroupFooter returned a save point or not 
					// (footer was printed or not), we just need to mark the end of the group 
					if (
						keepTogetherSavePoint != null
						&& i <= keepTogetherSavePoint.groupIndex
						)
					{
						keepTogetherSavePoint = null;
					}
				}
			}
			
			if (savePoint != null)
			{
				savePoint.moveSavePointContent();
				offsetY = columnFooterOffsetY;
			}
		}
	}


	/**
	 *
	 */
	private SavePoint fillGroupFooter(JRFillGroup group, byte evaluation) throws JRException
	{
		SavePoint savePoint = null;
		
		JRFillSection groupFooterSection = (JRFillSection)group.getGroupFooterSection();

		if (log.isDebugEnabled() && !groupFooterSection.isEmpty())
		{
			log.debug("Fill " + fillerId + ": " + group.getName() + " footer at " + offsetY);
		}

		JRFillBand[] groupFooterBands = groupFooterSection.getFillBands();
		for(int i = 0; i < groupFooterBands.length; i++)
		{
			JRFillBand groupFooterBand = groupFooterBands[i];
			
			groupFooterBand.evaluatePrintWhenExpression(evaluation);

			if (groupFooterBand.isToPrint())
			{
				setFirstColumn();

				if (
					groupFooterBand.getBreakHeight() > columnFooterOffsetY - offsetY
					)
				{
					fillPageBreak(false, evaluation, evaluation, true);
				}

				SavePoint newSavePoint = fillColumnBand(groupFooterBand, evaluation);
				newSavePoint.footerPosition = group.getFooterPositionValue();
				
				savePoint = advanceSavePoint(savePoint, newSavePoint);

				isFirstPageBand = false;
				isFirstColumnBand = true;
			}
		}

		isNewPage = false;
		isNewColumn = false;

		group.setHeaderPrinted(false);
		group.setFooterPrinted(true);
		
		return savePoint;
	}


	/**
	 *
	 */
	 private void fillColumnFooters(byte evaluation) throws JRException
	 {
		if (log.isDebugEnabled() && !columnFooter.isEmpty())
		{
			log.debug("Fill " + fillerId + ": column footers at " + offsetY);
		}

		/*
		if (!isSubreport)
		{
			offsetY = columnFooterOffsetY;
		}
		*/

		if (isSubreport() && !isSubreportRunToBottom())
		{
			columnFooterOffsetY = offsetY;
		}

		int tmpColumnFooterOffsetY = columnFooterOffsetY;

		if (isFloatColumnFooter || ignorePagination)
		{
			tmpColumnFooterOffsetY = offsetY;
		}

		for(columnIndex = 0; columnIndex < columnCount; columnIndex++)
		{
			setColumnNumberVariable();

			setOffsetX();
			offsetY = tmpColumnFooterOffsetY;

			columnFooter.evaluatePrintWhenExpression(evaluation);

			if (columnFooter.isToPrint())
			{
				fillFixedBand(columnFooter, evaluation, false);
			}
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
			log.debug("Fill " + fillerId + ": " + (isLastPageFooter ? "last " : "") + "page footer at " + offsetY);
		}

		offsetX = leftMargin;

		if ((!isSubreport() || isSubreportRunToBottom()) && !ignorePagination)
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
			log.debug("Fill " + fillerId + ": summary at " + offsetY);
		}

		offsetX = leftMargin;

		if (lastPageFooter == missingFillBand)
		{
			if (
				!isSummaryNewPage
				//&& columnIndex == 0
				&& summary.getBreakHeight() <= columnFooterOffsetY - offsetY
				)
			{
				fillSummaryNoLastFooterSamePage();
			}
			else
			{
				fillSummaryNoLastFooterNewPage();
			}
		}
		else
		{
			if (isSummaryWithPageHeaderAndFooter)
			{
				fillSummaryWithLastFooterAndPageBands();
			}
			else
			{
				fillSummaryWithLastFooterNoPageBands();
			}
		}

		resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
		resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
		resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
		resolveReportBoundElements();
		if (isMasterReport())
		{
			resolveMasterBoundElements();
		}
	}


	/**
	 *
	 */
	private void fillSummaryNoLastFooterSamePage() throws JRException
	{
		summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (summary != missingFillBand && summary.isToPrint())
		{
			summary.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

			if (summary.willOverflow() && summary.isSplitPrevented())
			{
				fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);

				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);
				
				if (isSummaryWithPageHeaderAndFooter)
				{
					fillPageHeader(JRExpression.EVALUATION_DEFAULT);
				}

				printBand = summary.refill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

				fillBand(printBand);
				offsetY += printBand.getHeight();

				/*   */
				fillSummaryOverflow();
				
				//DONE
			}
			else
			{
				fillBand(printBand);
				offsetY += printBand.getHeight();

				fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
				if (summary.willOverflow())
				{
					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);
					
					if (isSummaryWithPageHeaderAndFooter)
					{
						fillPageHeader(JRExpression.EVALUATION_DEFAULT);
					}

					printBand = summary.fill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

					fillBand(printBand);
					offsetY += printBand.getHeight();

					/*   */
					fillSummaryOverflow();
					
					//DONE
				}
				else
				{
					resolveBandBoundElements(summary, JRExpression.EVALUATION_DEFAULT);

					//DONE
				}
			}
		}
		else
		{
			fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

			fillPageFooter(JRExpression.EVALUATION_DEFAULT);
			
			//DONE
		}
	}


	/**
	 *
	 */
	private void fillSummaryNoLastFooterNewPage() throws JRException
	{
		fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

		fillPageFooter(JRExpression.EVALUATION_DEFAULT);

		summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

		if (summary != missingFillBand && summary.isToPrint())
		{
			resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
			resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
			scriptlet.callBeforePageInit();
			calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
			scriptlet.callAfterPageInit();

			addPage(false);

			if (isSummaryWithPageHeaderAndFooter)
			{
				fillPageHeader(JRExpression.EVALUATION_DEFAULT);
			}

			summary.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

			if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
			{
				if (isSummaryWithPageHeaderAndFooter)
				{
					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}

				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				if (isSummaryWithPageHeaderAndFooter)
				{
					fillPageHeader(JRExpression.EVALUATION_DEFAULT);
				}

				printBand = summary.refill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));
			}

			fillBand(printBand);
			offsetY += printBand.getHeight();

			/*   */
			fillSummaryOverflow();
		}
		
		//DONE
	}


	/**
	 *
	 */
	private void fillSummaryWithLastFooterAndPageBands() throws JRException
	{
		if (
			!isSummaryNewPage
			//&& columnIndex == 0
			&& summary.getBreakHeight() <= columnFooterOffsetY - offsetY
			)
		{
			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary != missingFillBand && summary.isToPrint())
			{
				summary.evaluate(JRExpression.EVALUATION_DEFAULT);

				JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

				if (summary.willOverflow() && summary.isSplitPrevented())
				{
					fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);

					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);
					
					fillPageHeader(JRExpression.EVALUATION_DEFAULT);
					
					printBand = summary.refill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());

					fillBand(printBand);
					offsetY += printBand.getHeight();
				}
				else
				{
					fillBand(printBand);
					offsetY += printBand.getHeight();

					if (!summary.willOverflow())
					{
						setLastPageFooter(true);
					}
					
					fillColumnFooters(JRExpression.EVALUATION_DEFAULT);
				}
				
				/*   */
				fillSummaryOverflow();

				//DONE
			}
			else
			{
				setLastPageFooter(true);

				fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
				//DONE
			}
		}
		else if (
				//columnIndex == 0 && 
				offsetY <= lastPageColumnFooterOffsetY)
		{
			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary != missingFillBand && summary.isToPrint())
			{
				fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);

				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);
				
				fillPageHeader(JRExpression.EVALUATION_DEFAULT);

				summary.evaluate(JRExpression.EVALUATION_DEFAULT);

				JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());

				if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
				{
					fillPageFooter(JRExpression.EVALUATION_DEFAULT);

					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);
					
					fillPageHeader(JRExpression.EVALUATION_DEFAULT);

					printBand = summary.refill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());
				}

				fillBand(printBand);
				offsetY += printBand.getHeight();

				/*   */
				fillSummaryOverflow();
				
				//DONE
			}
			else
			{
				setLastPageFooter(true);

				fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
				//DONE
			}
		}
		else
		{
			fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

			fillPageFooter(JRExpression.EVALUATION_DEFAULT);

			resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
			resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
			scriptlet.callBeforePageInit();
			calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
			scriptlet.callAfterPageInit();

			addPage(false);

			fillPageHeader(JRExpression.EVALUATION_DEFAULT);

			summary.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());

			if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
			{
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);

				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);
				
				fillPageHeader(JRExpression.EVALUATION_DEFAULT);

				printBand = summary.refill(pageHeight - bottomMargin - offsetY - pageFooter.getHeight());
			}

			fillBand(printBand);
			offsetY += printBand.getHeight();

			/*   */
			fillSummaryOverflow();
			
			//DONE
		}
	}


	/**
	 *
	 */
	private void fillSummaryWithLastFooterNoPageBands() throws JRException
	{
		if (
			!isSummaryNewPage
			//&& columnIndex == 0
			&& summary.getBreakHeight() <= lastPageColumnFooterOffsetY - offsetY
			)
		{
			setLastPageFooter(true);

			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary != missingFillBand && summary.isToPrint())
			{
				summary.evaluate(JRExpression.EVALUATION_DEFAULT);

				JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

				if (summary.willOverflow() && summary.isSplitPrevented())
				{
					fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);

					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
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

					fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}

				/*   */
				fillSummaryOverflow();
				
				//DONE
			}
			else
			{
				fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
				//DONE
			}
		}
		else if (
			!isSummaryNewPage
			//&& columnIndex == 0
			&& summary.getBreakHeight() <= columnFooterOffsetY - offsetY
			)
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

						fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

						fillPageFooter(JRExpression.EVALUATION_DEFAULT);

						resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
						resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
						resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
						scriptlet.callBeforePageInit();
						calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
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

						fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

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

					fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}

				/*   */
				fillSummaryOverflow();
				
				//DONE
			}
			else
			{
				if(offsetY > lastPageColumnFooterOffsetY)
				{
					fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);
				}

				setLastPageFooter(true);

				fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
				//DONE
			}
		}
		else if (
				//columnIndex == 0 && 
				offsetY <= lastPageColumnFooterOffsetY)
		{
			setLastPageFooter(true);

			fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

			fillPageFooter(JRExpression.EVALUATION_DEFAULT);

			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary != missingFillBand && summary.isToPrint())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				summary.evaluate(JRExpression.EVALUATION_DEFAULT);

				JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY);

				if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
				{
					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);

					printBand = summary.refill(pageHeight - bottomMargin - offsetY);
				}

				fillBand(printBand);
				offsetY += printBand.getHeight();

				/*   */
				fillSummaryOverflow();
			}
			
			//DONE
		}
		else
		{
			fillColumnFooters(JRExpression.EVALUATION_DEFAULT);

			fillPageFooter(JRExpression.EVALUATION_DEFAULT);

			resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
			resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
			scriptlet.callBeforePageInit();
			calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
			scriptlet.callAfterPageInit();

			addPage(false);

			fillPageHeader(JRExpression.EVALUATION_DEFAULT);

			//fillColumnHeader(JRExpression.EVALUATION_DEFAULT);

			setLastPageFooter(true);

			if (isSummaryNewPage)
			{
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);

				summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

				if (summary != missingFillBand && summary.isToPrint())
				{
					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);

					summary.evaluate(JRExpression.EVALUATION_DEFAULT);

					JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY);

					if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
					{
						resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
						resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
						resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
						scriptlet.callBeforePageInit();
						calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
						scriptlet.callAfterPageInit();

						addPage(false);

						printBand = summary.refill(pageHeight - bottomMargin - offsetY);
					}

					fillBand(printBand);
					offsetY += printBand.getHeight();

					/*   */
					fillSummaryOverflow();
				}
				
				//DONE
			}
			else
			{
				summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

				if (summary != missingFillBand && summary.isToPrint())
				{
					summary.evaluate(JRExpression.EVALUATION_DEFAULT);

					JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

					if (summary.willOverflow() && summary.isSplitPrevented())//FIXMENOW check subreport here?
					{
						fillPageFooter(JRExpression.EVALUATION_DEFAULT);

						resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
						resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
						resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
						scriptlet.callBeforePageInit();
						calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
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

						fillPageFooter(JRExpression.EVALUATION_DEFAULT);
					}

					/*   */
					fillSummaryOverflow();
				}
				else
				{
					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}
				
				//DONE
			}
		}
	}


	/**
	 *
	 */
	private void fillSummaryOverflow() throws JRException
	{
		while (summary.willOverflow())
		{
			if (isSummaryWithPageHeaderAndFooter)
			{
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
			}
			
			resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
			resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
			scriptlet.callBeforePageInit();
			calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
			scriptlet.callAfterPageInit();

			addPage(false);

			if (isSummaryWithPageHeaderAndFooter)
			{
				fillPageHeader(JRExpression.EVALUATION_DEFAULT);
			}
			
			JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - (isSummaryWithPageHeaderAndFooter?pageFooter.getHeight():0));

			fillBand(printBand);
			offsetY += printBand.getHeight();
		}

		resolveBandBoundElements(summary, JRExpression.EVALUATION_DEFAULT);

		if (isSummaryWithPageHeaderAndFooter)
		{
			if (offsetY > pageHeight - bottomMargin - lastPageFooter.getHeight())
			{
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				fillPageHeader(JRExpression.EVALUATION_DEFAULT);
			}
			
			if (lastPageFooter != missingFillBand)
			{
				setLastPageFooter(true);
			}
			
			fillPageFooter(JRExpression.EVALUATION_DEFAULT);
		}
	}


	/**
	 *
	 */
	private void fillBackground() throws JRException
	{
		if (log.isDebugEnabled() && !background.isEmpty())
		{
			log.debug("Fill " + fillerId + ": background at " + offsetY);
		}
		
		//offsetX = leftMargin;
		
		//if (!isSubreport)
		//{
		//  offsetY = pageHeight - pageFooter.getHeight() - bottomMargin;
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
			addPageToParent(false);
		}

		printPage = newPage();

		if (isResetPageNumber)
		{
			calculator.getPageNumber().setValue(Integer.valueOf(1));
		}
		else
		{
			calculator.getPageNumber().setValue(
				Integer.valueOf(((Number)calculator.getPageNumber().getValue()).intValue() + 1)
				);
		}

		calculator.getPageNumber().setOldValue(
			calculator.getPageNumber().getValue()
			);

		addPage(printPage);
		setFirstColumn();
		offsetY = topMargin;

		lastDetailOffsetX = -1;
		lastDetailOffsetY = -1;
		maxDetailOffsetY = 0;

		fillBackground();
	}

	/**
	 * Sets the column number value computed based on {@link #columnIndex columnIndex}
	 */
	private void setColumnNumberVariable()
	{
		JRFillVariable columnNumberVar = calculator.getColumnNumber();
		columnNumberVar.setValue(Integer.valueOf(columnIndex + 1));
		columnNumberVar.setOldValue(columnNumberVar.getValue());
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
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_INFINITE_LOOP_CREATING_NEW_PAGE,  
					(Object[])null 
					);
		}

		if (keepTogetherSavePoint != null)
		{
			keepTogetherSavePoint.saveEndOffsetY(offsetY);
		}
		
		isCreatingNewPage = true;

		fillColumnFooters(evalPrevPage);

		fillPageFooter(evalPrevPage);

		resolveGroupBoundElements(evalPrevPage, false);
		resolveColumnBoundElements(evalPrevPage);
		resolvePageBoundElements(evalPrevPage);
		scriptlet.callBeforePageInit();
		calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
		scriptlet.callAfterPageInit();

		if (
			keepTogetherSavePoint != null
			&& !keepTogetherSavePoint.isNewPage 
			)
		{
			keepTogetherSavePoint.removeContent();
		}

		addPage(isResetPageNumber);

		fillPageHeader(evalNextPage);

		fillColumnHeaders(evalNextPage);

		boolean savePointContentMoved = moveKeepTogetherSavePointContent();
		if (
			!savePointContentMoved
			&& isReprintGroupHeaders
			)
		{
			fillGroupHeadersReprint(evalNextPage);
		}

		isCreatingNewPage = false;
	}


	/**
	 *
	 *
	private void fillColumnBreak(
		byte evalPrevPage,
		byte evalNextPage
		) throws JRException
	{
		if (columnIndex == columnCount - 1)
		{
			fillPageBreak(false, evalPrevPage, evalNextPage);
		}
		else
		{
			if (keepTogetherSavePoint != null)
			{
				keepTogetherSavePoint.saveEndOffsetY(offsetY);
			}
			
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
				Integer.valueOf(((Number)calculator.getColumnNumber().getValue()).intValue() + 1)
				);
			calculator.getColumnNumber().setOldValue(
				calculator.getColumnNumber().getValue()
				);

			fillColumnHeader(evalNextPage);

			if (keepTogetherSavePoint != null)
			{
				moveKeepTogetherSavePointContent();
			}
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
	protected SavePoint fillColumnBand(JRFillBand band, byte evaluation) throws JRException
	{
		band.evaluate(evaluation);

		JRPrintBand printBand = band.fill(columnFooterOffsetY - offsetY);

		if (
			band.willOverflow() 
			&& (band.isSplitPrevented() || keepTogetherSavePoint != null)
			)
		{
			fillPageBreak(false, evaluation, evaluation, true);

			printBand = band.refill(columnFooterOffsetY - offsetY);
		}

		SavePoint savePoint = 
			new SavePoint(
				getCurrentPage(), 
				columnIndex,
				isNewPage,
				isNewColumn,
				offsetY
				);
		
		fillBand(printBand);
		offsetY += printBand.getHeight();
		
		savePoint.saveHeightOffset(columnFooterOffsetY - offsetY);
		// we mark the save point here, because overflow content beyond this point
		// should be rendered normally, not moved in any way 

		while (band.willOverflow())
		{
			fillPageBreak(false, evaluation, evaluation, true);

			printBand = band.fill(columnFooterOffsetY - offsetY);

			fillBand(printBand);
			offsetY += printBand.getHeight();
		}

		resolveBandBoundElements(band, evaluation);
		
		return savePoint;
	}


	/**
	 *
	 */
	protected void fillFixedBand(JRFillBand band, byte evaluation) throws JRException
	{
		fillFixedBand(band, evaluation, true);
	}


	protected void fillFixedBand(JRFillBand band, byte evaluation, boolean allowShrinking) throws JRException
	{
		band.evaluate(evaluation);

		JRPrintBand printBand = band.fill();

		fillBand(printBand);
		offsetY += allowShrinking ? printBand.getHeight() : band.getHeight();

		resolveBandBoundElements(band, evaluation);
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
			log.debug("Fill " + fillerId + ": noData at " + offsetY);
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
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
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
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				printBand = noData.fill(pageHeight - bottomMargin - offsetY);

				fillBand(printBand);
				offsetY += printBand.getHeight();
			}
			resolveBandBoundElements(noData, JRExpression.EVALUATION_DEFAULT);
		}
	}

	
	/**
	 *
	 */
	private void setOffsetX()
	{
		if (columnDirection == RunDirectionEnum.RTL)
		{
			offsetX = pageWidth - rightMargin - columnWidth - columnIndex * (columnSpacing + columnWidth);
		}
		else
		{
			offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
		}
	}

	
}
