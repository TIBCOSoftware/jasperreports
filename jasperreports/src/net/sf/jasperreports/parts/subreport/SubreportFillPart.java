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
package net.sf.jasperreports.parts.subreport;

import java.util.Map;

import net.sf.jasperreports.data.cache.DataCacheHandler;
import net.sf.jasperreports.engine.BookmarkHelper;
import net.sf.jasperreports.engine.CommonReturnValue;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.SimplePrintPart;
import net.sf.jasperreports.engine.VariableReturnValue;
import net.sf.jasperreports.engine.fill.AbstractVariableReturnValueSourceContext;
import net.sf.jasperreports.engine.fill.BandReportFillerParent;
import net.sf.jasperreports.engine.fill.BaseReportFiller;
import net.sf.jasperreports.engine.fill.DatasetExpressionEvaluator;
import net.sf.jasperreports.engine.fill.FillDatasetPosition;
import net.sf.jasperreports.engine.fill.FillListener;
import net.sf.jasperreports.engine.fill.FillReturnValues;
import net.sf.jasperreports.engine.fill.FillerPageAddedEvent;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.JRFillDataset;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRFillSubreport;
import net.sf.jasperreports.engine.fill.JRHorizontalFiller;
import net.sf.jasperreports.engine.fill.JRVerticalFiller;
import net.sf.jasperreports.engine.fill.PartReportFiller;
import net.sf.jasperreports.engine.part.BasePartFillComponent;
import net.sf.jasperreports.engine.part.FillingPrintPart;
import net.sf.jasperreports.engine.part.PartPrintOutput;
import net.sf.jasperreports.engine.type.SectionTypeEnum;
import net.sf.jasperreports.engine.util.BookmarksFlatDataSource;
import net.sf.jasperreports.parts.PartFillerParent;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SubreportFillPart extends BasePartFillComponent
{

	public static final String PROPERTY_BOOKMARKS_DATA_SOURCE_PARAMETER = JRPropertiesUtil.PROPERTY_PREFIX + "bookmarks.data.source.parameter";
	public static final String EXCEPTION_MESSAGE_KEY_UNKNOWN_REPORT_PRINT_ORDER = "parts.subreport.unknown.report.print.order";
	public static final String EXCEPTION_MESSAGE_KEY_UNKNOWN_REPORT_SECTION_TYPE = "parts.subreport.unknown.report.section.type";
	
	private SubreportPartComponent subreportPart;
	private JRFillExpressionEvaluator expressionEvaluator;
	private FillReturnValues returnValues;
	private FillReturnValues.SourceContext returnValuesSource;

	private JasperReport jasperReport;
	private Map<String, Object> parameterValues;
	
	private FillDatasetPosition datasetPosition;
	private boolean cacheIncluded;
	
	private volatile BaseReportFiller subreportFiller;
	
	public SubreportFillPart(SubreportPartComponent subreportPart, JRFillObjectFactory factory)
	{
		this.subreportPart = subreportPart;
		this.expressionEvaluator = factory.getExpressionEvaluator();
		
		this.returnValues = new FillReturnValues(subreportPart.getReturnValues(), factory, factory.getReportFiller());
		this.returnValuesSource = new AbstractVariableReturnValueSourceContext() 
		{
			@Override
			public Object getValue(CommonReturnValue returnValue) {
				return subreportFiller.getVariableValue(((VariableReturnValue)returnValue).getFromVariable());
			}
			
			@Override
			public JRVariable getToVariable(String name) {
				return fillContext.getFiller().getVariable(name);
			}
			
			@Override
			public JRVariable getFromVariable(String name) {
				return subreportFiller.getVariable(name);
			}
		};
	}

	@Override
	public void evaluate(byte evaluation) throws JRException
	{
		jasperReport = evaluateReport(evaluation);
		
		JRFillDataset parentDataset = expressionEvaluator.getFillDataset();
		datasetPosition = new FillDatasetPosition(parentDataset.getFillPosition());
		datasetPosition.addAttribute("subreportPartUUID", fillContext.getPart().getUUID());
		parentDataset.setCacheRecordIndex(datasetPosition, evaluation);
		
		String cacheIncludedProp = JRPropertiesUtil.getOwnProperty(fillContext.getPart(), DataCacheHandler.PROPERTY_INCLUDED); 
		cacheIncluded = JRPropertiesUtil.asBoolean(cacheIncludedProp, true);// default to true
		//FIXMEBOOK do not evaluate REPORT_DATA_SOURCE
		
		parameterValues = JRFillSubreport.getParameterValues(fillContext.getFiller(), expressionEvaluator, 
				subreportPart.getParametersMapExpression(), subreportPart.getParameters(), 
				evaluation, false, 
				jasperReport.getResourceBundle() != null, jasperReport.getFormatFactoryClass() != null);
		
		setBookmarksParameter();
	}

	private JasperReport evaluateReport(byte evaluation) throws JRException
	{
		Object reportSource = fillContext.evaluate(subreportPart.getExpression(), evaluation);
		return JRFillSubreport.loadReport(reportSource, fillContext.getFiller());//FIXMEBOOK cache
	}

	private void setBookmarksParameter()
	{
		JRPart part = fillContext.getPart();
		String bookmarksParameter = part.hasProperties() ? part.getPropertiesMap().getProperty(PROPERTY_BOOKMARKS_DATA_SOURCE_PARAMETER) : null;
		if (bookmarksParameter == null)
		{
			return;
		}
		
		if (bookmarksParameter.equals(JRParameter.REPORT_DATA_SOURCE))
		{
			// if the bookmarks data source is created as main data source for the part report,
			// automatically exclude it from data snapshots as jive actions might result in different bookmarks.
			// if the data source is not the main report data source people will have to manually inhibit data caching.
			cacheIncluded = false;
		}
		
		BookmarkHelper bookmarks = fillContext.getFiller().getFirstBookmarkHelper();
		BookmarksFlatDataSource bookmarksDataSource = new BookmarksFlatDataSource(bookmarks);
		parameterValues.put(bookmarksParameter, bookmarksDataSource);
	}

	@Override
	public void fill(PartPrintOutput output) throws JRException
	{
		subreportFiller = createSubreportFiller(output);
		returnValues.checkReturnValues(returnValuesSource);
		
		JRFillDataset subreportDataset = subreportFiller.getMainDataset();
		subreportDataset.setFillPosition(datasetPosition);
		subreportDataset.setCacheSkipped(!cacheIncluded);
		
		subreportFiller.fill(parameterValues);
		returnValues.copyValues(returnValuesSource);
	}
	
	protected BaseReportFiller createSubreportFiller(final PartPrintOutput output) throws JRException
	{
		SectionTypeEnum sectionType = jasperReport.getSectionType();
		sectionType = sectionType == null ? SectionTypeEnum.BAND : sectionType;
		
		BaseReportFiller filler;
		switch (sectionType)
		{
		case BAND:
			filler = createBandSubfiller(output);
			break;
		case PART:
			filler = createPartSubfiller(output);
			break;
		default:
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNKNOWN_REPORT_SECTION_TYPE,
					new Object[]{sectionType});
		}
		
		return filler;
	}

	protected JRBaseFiller createBandSubfiller(final PartPrintOutput output) throws JRException
	{
		BandReportFillerParent bandParent = new PartBandParent(output);
		JRBaseFiller bandFiller;
		switch (jasperReport.getPrintOrderValue())
		{
		case HORIZONTAL:
			bandFiller = new JRHorizontalFiller(getJasperReportsContext(), jasperReport, bandParent);
			break;
		case VERTICAL:
			bandFiller = new JRVerticalFiller(getJasperReportsContext(), jasperReport, bandParent);
			break;
		default:
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNKNOWN_REPORT_PRINT_ORDER,
					new Object[]{jasperReport.getPrintOrderValue()});
		}
		
		bandFiller.addFillListener(new FillListener()
		{
			@Override
			public void pageGenerated(JasperPrint jasperPrint, int pageIndex)
			{
				//NOP
			}
			
			@Override
			public void pageUpdated(JasperPrint jasperPrint, int pageIndex)
			{
				output.pageUpdated(pageIndex);
			}
		});

		return bandFiller;
	}

	protected BaseReportFiller createPartSubfiller(PartPrintOutput output) throws JRException
	{
		PartParent partParent = new PartParent(output);
		PartReportFiller partFiller = new PartReportFiller(getJasperReportsContext(), jasperReport, partParent);
		return partFiller;
	}
	
	protected String getPartName()
	{
		return fillContext.getFillPart().getPartName();
	}
	
	protected class PartBandParent implements BandReportFillerParent
	{
		private final PartPrintOutput output;

		protected PartBandParent(PartPrintOutput output)
		{
			this.output = output;
		}

		@Override
		public BaseReportFiller getFiller()
		{
			return fillContext.getFiller();
		}

		@Override
		public DatasetExpressionEvaluator getCachedEvaluator()
		{
			//FIXMEBOOK
			return null;
		}

		@Override
		public void registerSubfiller(JRBaseFiller filler)
		{
			//NOP
		}

		@Override
		public void unregisterSubfiller(JRBaseFiller filler)
		{
			//NOP
		}

		@Override
		public boolean isRunToBottom()
		{
			return true;
		}

		@Override
		public boolean isParentPagination()
		{
			return false;
		}

		@Override
		public boolean isPageBreakInhibited()
		{
			return false;
		}

		@Override
		public void addPage(FillerPageAddedEvent pageAdded) throws JRException
		{
			if (pageAdded.getPageIndex() == 0)
			{
				//first page, adding the part info
				SimplePrintPart printPart = SimplePrintPart.fromJasperPrint(pageAdded.getJasperPrint(), getPartName());
				FillerPrintPart fillingPart = new FillerPrintPart(pageAdded.getFiller());//FIXMEBOOK strange
				output.startPart(printPart, fillingPart);
			}
			
			output.addPage(pageAdded.getPage(), pageAdded.getDelayedActions());
			
			if (pageAdded.hasReportEnded())
			{
				output.addStyles(pageAdded.getJasperPrint().getStylesList());
				output.addOrigins(pageAdded.getJasperPrint().getOriginsList());
			}
		}

		@Override
		public void updateBookmark(JRPrintElement element)
		{
			BookmarkHelper bookmarkHelper = output.getBookmarkHelper();
			if (bookmarkHelper != null)
			{
				bookmarkHelper.updateBookmark(element);
			}
		}
	}
	
	protected static class FillerPrintPart implements FillingPrintPart
	{
		private final JRBaseFiller filler;
		
		protected FillerPrintPart(JRBaseFiller filler)
		{
			this.filler = filler;
		}

		@Override
		public boolean isPageFinal(JRPrintPage page)
		{
			return filler.isPageFinal(page);
		}
	}
	
	protected class PartParent implements PartFillerParent
	{
		private PartPrintOutput output;

		public PartParent(PartPrintOutput output)
		{
			this.output = output;
		}

		@Override
		public PartReportFiller getFiller()
		{
			return fillContext.getFiller();
		}

		@Override
		public DatasetExpressionEvaluator getCachedEvaluator()
		{
			//FIXMEBOOK
			return null;
		}

		@Override
		public void updateBookmark(JRPrintElement element)
		{
			BookmarkHelper bookmarkHelper = output.getBookmarkHelper();
			if (bookmarkHelper != null)
			{
				bookmarkHelper.updateBookmark(element);
			}
		}

		@Override
		public boolean isParentPagination()
		{
			return false;
		}
	}

}
