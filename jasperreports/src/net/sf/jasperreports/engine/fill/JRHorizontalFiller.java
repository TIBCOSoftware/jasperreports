/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine.fill;

import java.util.HashMap;
import java.util.Iterator;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRReport;
import dori.jasper.engine.JRVariable;
import dori.jasper.engine.JasperReport;
import dori.jasper.engine.base.JRBasePrintPage;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRHorizontalFiller extends JRBaseFiller
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

	private int lastDetailOffsetX = -1;
	private int lastDetailOffsetY = -1;


	/**
	 *
	 */
	protected JRHorizontalFiller(JasperReport jasperReport) throws JRException
	{
		this(jasperReport, null);
	}

	/**
	 *
	 */
	protected JRHorizontalFiller(JasperReport jasperReport, JRBaseFiller parentFiller) throws JRException
	{
		super(jasperReport, parentFiller);

		this.setPageHeight(this.pageHeight);
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
	}


	/**
	 *
	 */
	protected synchronized void fillReport() throws JRException
	{
		loadedImages = new HashMap();
		loadedSubreports = new HashMap();

		reportBoundImages = new HashMap();
		pageBoundImages = new HashMap();
		columnBoundImages = new HashMap();

		reportBoundTexts = new HashMap();
		pageBoundTexts = new HashMap();
		columnBoundTexts = new HashMap();

		groupBoundImages = new HashMap();
		groupBoundTexts = new HashMap();

		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				groupBoundImages.put( groups[i].getName(), new HashMap() );
				groupBoundTexts.put( groups[i].getName(), new HashMap() );
			}
		}
		
		if (this.next())
		{
			this.fillReportStart();

			while (this.next())
			{
				this.fillReportContent();
			}
			
			this.fillReportEnd();
		}
		else
		{
			switch (this.whenNoDataType)
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
			
					this.fillBackground();
					
					this.fillTitle();
					
					this.fillPageHeader(JRExpression.EVALUATION_DEFAULT);
			
					this.fillColumnHeaders(JRExpression.EVALUATION_DEFAULT);
			
					this.fillGroupHeaders(true);
			
					this.fillGroupFooters(true);
			
					this.fillSummary();

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

		this.fillBackground();

		this.fillTitle();
		
		this.fillPageHeader(JRExpression.EVALUATION_DEFAULT);

		this.fillColumnHeaders(JRExpression.EVALUATION_DEFAULT);

		this.fillGroupHeaders(true);

		this.fillDetail();
	}


	/**
	 *
	 */
	private void fillReportContent() throws JRException
	{
		calculator.estimateGroupRuptures();

		this.fillGroupFooters(false);

		this.resolveGroupBoundImages(JRExpression.EVALUATION_OLD, false);
		this.resolveGroupBoundTexts(JRExpression.EVALUATION_OLD, false);
		scriptlet.callBeforeGroupInit();
		calculator.initializeVariables(JRVariable.RESET_TYPE_GROUP);
		scriptlet.callAfterGroupInit();

		this.fillGroupHeaders(false);

		this.fillDetail();
	}


	/**
	 *
	 */
	private void fillReportEnd() throws JRException
	{
		this.fillGroupFooters(true);

		this.fillSummary();
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
				title.getHeight() > columnFooterOffsetY - offsetY
				)
			{
				this.addPage(false);
			}
	
			title.evaluate(JRExpression.EVALUATION_DEFAULT);

			JRPrintBand printBand = title.fill(pageHeight - bottomMargin - offsetY - title.getHeight());
			
			if (title.willOverflow() && !title.isSplitAllowed())
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
	
				this.addPage(false);
	
				printBand = title.refill(pageHeight - bottomMargin - offsetY - title.getHeight());
			}

			this.fillBand(printBand);
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
	
				this.addPage(false);
	
				printBand = title.fill(pageHeight - bottomMargin - offsetY - title.getHeight());
	
				this.fillBand(printBand);
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
	
				this.addPage(false);
			}
		}
	}


	/**
	 *
	 */
	private void fillPageHeader(byte evaluation) throws JRException
	{
		this.setNewPageColumnInBands();

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
		
				this.addPage(false);
			}
	
			this.fillPageBand(pageHeader, evaluation);
		}

		columnHeaderOffsetY = offsetY;
		
		isNewPage = true;
	}


	/**
	 *
	 */
	private void fillColumnHeaders(byte evaluation) throws JRException
	{
		this.setNewPageColumnInBands();

		for(columnIndex = 0; columnIndex < columnCount; columnIndex++)
		{
			columnHeader.evaluatePrintWhenExpression(evaluation);
		
			if (columnHeader.isToPrint())
			{
				while (
					columnHeader.getHeight() > columnFooterOffsetY - offsetY
					)
				{
					this.fillPageFooter(evaluation);
				
					resolveGroupBoundImages(evaluation, false);
					resolveColumnBoundImages(evaluation);
					resolvePageBoundImages(evaluation);
					resolveGroupBoundTexts(evaluation, false);
					resolveColumnBoundTexts(evaluation);
					resolvePageBoundTexts(evaluation);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
					scriptlet.callAfterPageInit();
		
					this.addPage(false);
		
					this.fillPageHeader(evaluation);
				}

				offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
				offsetY = columnHeaderOffsetY;
			
				/*FIXME
				calculator.getColumnNumber().setValue(
					new Integer(((Number)calculator.getColumnNumber().getValue()).intValue() + 1)
					);
				calculator.getColumnNumber().setOldValue(
					calculator.getColumnNumber().getValue()
					);
				*/

				this.fillColumnBand(columnHeader, evaluation);
			}
		}

		columnIndex = 0;
		offsetX = leftMargin; 

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
					this.fillGroupHeader(groups[i]);
				}
				else
				{
					if (groups[i].hasChanged())
					{
						this.fillGroupHeader(groups[i]);
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

		if ( 
			(group.isStartNewPage() || group.isResetPageNumber()) && !isNewPage 
			|| ( group.isStartNewColumn() && !isNewColumn )
			)
		{
			fillPageBreak(
				group.isResetPageNumber(), 
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
				fillPageBreak(
					false,
					evalPrevPage,
					JRExpression.EVALUATION_DEFAULT
					);
			}
		}

		this.setNewGroupInBands(group);

		group.setFooterPrinted(false);

		if (groupHeader.isToPrint())
		{
			columnIndex = 0;
			offsetX = leftMargin;

			this.fillColumnBand(groupHeader, JRExpression.EVALUATION_DEFAULT);
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
				this.fillGroupHeaderReprint(groups[i], evaluation);
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
				columnIndex = 0;
				offsetX = leftMargin;

				while (
					groupHeader.getHeight() > columnFooterOffsetY - offsetY ||
					group.getMinHeightToStartNewPage() > columnFooterOffsetY - offsetY
					)
				{
					fillPageBreak(false, evaluation, evaluation);
				}
	
				this.fillColumnBand(groupHeader, evaluation);
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
				columnIndex == columnCount - 1 
				&& detail.getHeight() > columnFooterOffsetY - offsetY
				)
			{
				byte evalPrevPage = (isNewGroup?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD);
				
				fillPageBreak(
					false,
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
			if (
				offsetX == lastDetailOffsetX
				&& offsetY == lastDetailOffsetY
				)
			{
				if (columnIndex == columnCount - 1)
				{
					columnIndex = 0;
					offsetX = leftMargin;
				}
				else
				{
					columnIndex++;
					offsetX += columnWidth + columnSpacing;
					offsetY -= detail.getHeight();
				}
			}

			this.fillFixedBand(detail, JRExpression.EVALUATION_DEFAULT);
		}

		isNewPage = false;
		isNewColumn = false;
		isNewGroup = false;
		
		lastDetailOffsetX = offsetX;
		lastDetailOffsetY = offsetY;
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
					this.fillGroupFooter(groups[i], evaluation);
				}
				else
				{
					if (groups[i].hasChanged())
					{
						this.fillGroupFooter(groups[i], evaluation);
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
			columnIndex = 0;
			offsetX = leftMargin;

			if (
				groupFooter.getHeight() > columnFooterOffsetY - offsetY
				)
			{
				fillPageBreak(false, evaluation, evaluation);
			}
	
			this.fillColumnBand(groupFooter, evaluation);
		}

		isNewPage = false;
		isNewColumn = false;

		group.setFooterPrinted(true);
	}


	/**
	 *
	 */
	 private void fillColumnFooters(byte evaluation) throws JRException
	 {
		/*
		if (!isSubreport)
		{
			offsetY = columnFooterOffsetY;
		}
		*/

		if (isSubreport())
		{
			columnFooterOffsetY = offsetY;
		}

		for(columnIndex = 0; columnIndex < columnCount; columnIndex++)
		{
			offsetX = leftMargin + columnIndex * (columnSpacing + columnWidth);
			offsetY = columnFooterOffsetY;
		
			columnFooter.evaluatePrintWhenExpression(evaluation);

			if (columnFooter.isToPrint())
			{
				this.fillFixedBand(columnFooter, evaluation);
			}
		}
	}


	/**
	 *
	 */
	private void fillPageFooter(byte evaluation) throws JRException
	{
		offsetX = leftMargin;

		if (!isSubreport())
		{
			offsetY = pageHeight - pageFooter.getHeight() - bottomMargin;
		}

		pageFooter.evaluatePrintWhenExpression(evaluation);

		if (pageFooter.isToPrint())
		{
			this.fillFixedBand(pageFooter, evaluation);
		}
	}


	/**
	 *
	 */
	private void fillSummary() throws JRException
	{
		if (
			!isSummaryNewPage &&
			//columnIndex == 0 &&
			summary.getHeight() <= columnFooterOffsetY - offsetY
			)
		{
			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary.isToPrint())
			{
				summary.evaluate(JRExpression.EVALUATION_DEFAULT);
		
				JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY - summary.getHeight());
				
				if (summary.willOverflow() && !summary.isSplitAllowed())
				{
					this.fillColumnFooters(JRExpression.EVALUATION_DEFAULT);
			
					this.fillPageFooter(JRExpression.EVALUATION_DEFAULT);
		
					resolveGroupBoundImages(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundImages(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundImages(JRExpression.EVALUATION_DEFAULT);
					resolveGroupBoundTexts(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundTexts(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundTexts(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
					scriptlet.callAfterPageInit();
		
					this.addPage(false);
		
					printBand = summary.refill(pageHeight - bottomMargin - offsetY - summary.getHeight());
		
					this.fillBand(printBand);
					offsetY += printBand.getHeight();
				}
				else
				{
					this.fillBand(printBand);
					offsetY += printBand.getHeight();
			
					this.fillColumnFooters(JRExpression.EVALUATION_DEFAULT);
			
					this.fillPageFooter(JRExpression.EVALUATION_DEFAULT);
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
		
					this.addPage(false);
		
					printBand = summary.fill(pageHeight - bottomMargin - offsetY - summary.getHeight());
		
					this.fillBand(printBand);
					offsetY += printBand.getHeight();
				}
			}
			else
			{
				this.fillColumnFooters(JRExpression.EVALUATION_DEFAULT);
		
				this.fillPageFooter(JRExpression.EVALUATION_DEFAULT);
			}
		}
		else
		{
			this.fillColumnFooters(JRExpression.EVALUATION_DEFAULT);
	
			this.fillPageFooter(JRExpression.EVALUATION_DEFAULT);

			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);
	
			if (summary.isToPrint())
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
	
				this.addPage(false);
	
				columnIndex = -1;

				summary.evaluate(JRExpression.EVALUATION_DEFAULT);
				
				JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY - summary.getHeight());
				
				if (summary.willOverflow() && !summary.isSplitAllowed())
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
		
					this.addPage(false);
		
					printBand = summary.refill(pageHeight - bottomMargin - offsetY - summary.getHeight());
				}

				this.fillBand(printBand);
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
		
					this.addPage(false);
		
					printBand = summary.fill(pageHeight - bottomMargin - offsetY - summary.getHeight());
		
					this.fillBand(printBand);
					offsetY += printBand.getHeight();
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
	
					this.fillBand(printBand);
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
		
		printPage = new JRBasePrintPage();
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

		lastDetailOffsetX = -1;
		lastDetailOffsetY = -1;

		calculator.getColumnNumber().setValue(
			new Integer(((Number)calculator.getColumnNumber().getValue()).intValue() + 1)
			);
		calculator.getColumnNumber().setOldValue(
			calculator.getColumnNumber().getValue()
			);
			
		this.fillBackground();
	}


	/**
	 *
	 */
	private void fillPageBreak(
		boolean isResetPageNumber, 
		byte evalPrevPage, 
		byte evalNextPage
		) throws JRException
	{
		if (isCreatingNewPage)
		{
			throw new JRException("Infinite loop creating new page.");
		}
		
		isCreatingNewPage = true;
		
		this.fillColumnFooters(evalPrevPage);

		this.fillPageFooter(evalPrevPage);
		
		resolveGroupBoundImages(evalPrevPage, false);
		resolveColumnBoundImages(evalPrevPage);
		resolvePageBoundImages(evalPrevPage);
		resolveGroupBoundTexts(evalPrevPage, false);
		resolveColumnBoundTexts(evalPrevPage);
		resolvePageBoundTexts(evalPrevPage);
		scriptlet.callBeforePageInit();
		calculator.initializeVariables(JRVariable.RESET_TYPE_PAGE);
		scriptlet.callAfterPageInit();

		this.addPage(isResetPageNumber);

		this.fillPageHeader(evalNextPage);

		this.fillColumnHeaders(evalNextPage);

		this.fillGroupHeadersReprint(evalNextPage);

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
			this.fillColumnFooter(evalPrevPage);

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
	
			this.fillColumnHeader(evalNextPage);
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
			this.fillPageBreak(false, evaluation, evaluation);

			printBand = band.refill(columnFooterOffsetY - offsetY - band.getHeight());
		}
		
		this.fillBand(printBand);
		offsetY += printBand.getHeight();

		while (band.willOverflow())
		{
			this.fillPageBreak(false, evaluation, evaluation);

			printBand = band.fill(columnFooterOffsetY - offsetY - band.getHeight());

			this.fillBand(printBand);
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
			this.fillPageBreak(false, evaluation, evaluation);

			printBand = band.refill(columnFooterOffsetY - offsetY - band.getHeight());
		}
		
		this.fillBand(printBand);
		offsetY += printBand.getHeight();

		while (band.willOverflow())
		{
			this.fillPageBreak(false, evaluation, evaluation);

			printBand = band.fill(columnFooterOffsetY - offsetY - band.getHeight());

			this.fillBand(printBand);
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

		this.fillBand(printBand);
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
		this.title.setNewPageColumn(true);
		this.pageHeader.setNewPageColumn(true);
		this.columnHeader.setNewPageColumn(true);
		this.detail.setNewPageColumn(true);
		this.columnFooter.setNewPageColumn(true);
		this.pageFooter.setNewPageColumn(true);
		this.summary.setNewPageColumn(true);
		
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
		this.title.setNewGroup(group, true);
		this.pageHeader.setNewGroup(group, true);
		this.columnHeader.setNewGroup(group, true);
		this.detail.setNewGroup(group, true);
		this.columnFooter.setNewGroup(group, true);
		this.pageFooter.setNewGroup(group, true);
		this.summary.setNewGroup(group, true);
		
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				((JRFillBand)groups[i].getGroupHeader()).setNewGroup(group, true);
				((JRFillBand)groups[i].getGroupFooter()).setNewGroup(group, true);
			}
		}
	}

	
}
