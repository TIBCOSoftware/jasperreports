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
package net.sf.jasperreports.components.table.fill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.ColumnVisitor;
import net.sf.jasperreports.components.table.TableComponent;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.design.JRAbstractCompiler;
import net.sf.jasperreports.engine.design.JRReportCompileData;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRTemplateFrame;
import net.sf.jasperreports.engine.fill.JRTemplatePrintFrame;
import net.sf.jasperreports.engine.util.JRReportUtils;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillTable extends BaseFillComponent
{

	private static final Log log = LogFactory.getLog(FillTable.class);
	
	private final TableComponent table;
	private final JRFillObjectFactory factory;
	private final Map<List<FillColumn>, FillTableSubreport> fillSubreports = 
		new HashMap<List<FillColumn>, FillTableSubreport>();
	private FillTableSubreport fillSubreport;
	
	private boolean filling;
	private List<FillColumn> fillColumns;
	private int fillWidth;
	private Map<JRStyle, JRTemplateFrame> printFrameTemplates = new HashMap<JRStyle, JRTemplateFrame>();

	public FillTable(TableComponent table, JRFillObjectFactory factory)
	{
		this.table = table;
		this.factory = factory;
	}

	public void evaluate(byte evaluation) throws JRException
	{
		if (filling)
		{
			log.warn("Table fill did not complete, canceling previous table subreport");
			fillSubreport.cancelSubreportFill();
		}
		
		filling = false;
		
		evaluateColumns(evaluation);
		if (!fillColumns.isEmpty())
		{
			createFillSubreport();
			fillSubreport.evaluateSubreport(evaluation);
		}
	}

	protected boolean toPrintColumn(BaseColumn column, byte evaluation) throws JRException
	{
		boolean toPrint;
		JRExpression printWhenExpression = column.getPrintWhenExpression();
		if (printWhenExpression == null)
		{
			toPrint = true;
		}
		else
		{
			Boolean printWhenVal = (Boolean) evaluateExpression(
					printWhenExpression, evaluation);
			if (printWhenVal == null)
			{
				toPrint = false;
			}
			else
			{
				toPrint = printWhenVal.booleanValue();
			}
		}
		return toPrint;
	}
	
	protected class FillColumnEvaluator implements ColumnVisitor<FillColumn>
	{
		final byte evaluation;
		
		public FillColumnEvaluator(byte evaluation)
		{
			this.evaluation = evaluation;
		}

		public FillColumn visitColumn(Column column)
		{
			try
			{
				boolean toPrint = toPrintColumn(column, evaluation);
				return toPrint ? new FillColumn(column) : null;
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		public FillColumn visitColumnGroup(ColumnGroup columnGroup)
		{
			try
			{
				boolean toPrint = toPrintColumn(columnGroup, evaluation);
				FillColumn fillColumn;
				if (toPrint)
				{
					List<BaseColumn> columns = columnGroup.getColumns();
					List<FillColumn> subColumns = new ArrayList<FillColumn>(columns.size());
					int printWidth = 0;
					for (BaseColumn column : columns)
					{
						FillColumn fillSubColumn = column.visitColumn(this);
						if (fillSubColumn != null)
						{
							printWidth += fillSubColumn.getWidth();
							subColumns.add(fillSubColumn);
						}
					}
					
					if (subColumns.isEmpty())
					{
						// no sub columns prints
						// the column group won't print either
						fillColumn = null;
					}
					else
					{
						fillColumn = new FillColumn(columnGroup, printWidth, subColumns);
					}
				}
				else
				{
					fillColumn = null;
				}
				return fillColumn;
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}
	
	protected void evaluateColumns(byte evaluation)
	{
		FillColumnEvaluator columnEvaluator = new FillColumnEvaluator(evaluation);
		List<BaseColumn> columns = table.getColumns();
		fillColumns = new ArrayList<FillColumn>(columns.size());
		fillWidth = 0;
		for (BaseColumn column : columns)
		{
			FillColumn fillColumn = column.visitColumn(columnEvaluator);
			if (fillColumn != null)
			{
				fillColumns.add(fillColumn);
				fillWidth += fillColumn.getWidth();
			}
		}
	}

	protected void createFillSubreport() throws JRException
	{
		fillSubreport = fillSubreports.get(fillColumns);
		if (fillSubreport == null)
		{
			fillSubreport = createFillTableSubreport();
			fillSubreports.put(fillColumns, fillSubreport);
		}
	}

	protected FillTableSubreport createFillTableSubreport() throws JRException
	{
		JasperReport parentReport = fillContext.getFiller().getJasperReport();
		JRDataset reportSubdataset = JRReportUtils.findSubdataset(table.getDatasetRun(), 
				parentReport);
		
		Map<JRExpression, BuiltinExpressionEvaluator> builtinEvaluators = 
			new HashMap<JRExpression, BuiltinExpressionEvaluator>();
		
		String tableReportName = JRAbstractCompiler.getUnitName(parentReport, reportSubdataset);
		
		// clone the table subdataset in order to have a different instance for other
		// elements that might be using it.
		// we're cloning the subdataset via an object factory in order to preserve
		// the relationship between objects (e.g. variables and groups) in the cloned
		// dataset
		JRDataset tableSubdataset = DatasetCloneObjectFactory.cloneDataset(reportSubdataset);
		TableReportDataset reportDataset = new TableReportDataset(tableSubdataset, tableReportName);

		TableReport tableReport = new TableReport(fillContext, table, reportDataset, fillColumns, builtinEvaluators);
		
		if (log.isDebugEnabled())
		{
			String tableReportXml = JRXmlWriter.writeReport(tableReport, "UTF-8");
			log.debug("Generated table report:\n" + tableReportXml);
		}
		
		JRReportCompileData tableReportCompileData = createTableReportCompileData(
				parentReport, reportDataset);//reportSubdataset); //FIXMEJIVE check this
		
		JasperReport compiledTableReport = new JasperReport(tableReport, 
				parentReport.getCompilerClass(), 
				tableReportCompileData, 
				new TableReportBaseObjectFactory(reportDataset),
				"");// no suffix as already included in the report name
		
		TableSubreport subreport = new TableSubreport(table.getDatasetRun(), fillContext);
		return new FillTableSubreport(
				fillContext.getFiller(), subreport, factory, compiledTableReport,
				builtinEvaluators);
	}
	
	protected JRReportCompileData createTableReportCompileData(
			JasperReport parentReport, JRDataset reportSubdataset)
			throws JRException
	{
		Serializable reportCompileDataObj = parentReport.getCompileData();
		if (!(reportCompileDataObj instanceof JRReportCompileData))
		{
			throw new JRRuntimeException("Unsupported compiled report data of type " 
					+ reportCompileDataObj.getClass().getName());
		}
		
		JRReportCompileData reportCompileData = (JRReportCompileData) reportCompileDataObj;
		Serializable datasetCompileData = reportCompileData.getDatasetCompileData(
				reportSubdataset);
		
		JRReportCompileData tableReportCompileData = new TableReportCompileData(
				parentReport);
		tableReportCompileData.setMainDatasetCompileData(datasetCompileData);
		
		JRDataset[] datasets = parentReport.getDatasets();
		if (datasets != null)
		{
			for (JRDataset dataset : datasets)
			{
				Serializable compileData = reportCompileData.getDatasetCompileData(dataset);
				tableReportCompileData.setDatasetCompileData(dataset, compileData);
			}
		}
		return tableReportCompileData;
	}
	
	public FillPrepareResult prepare(int availableHeight)
	{
		try
		{
			if (fillColumns.isEmpty())
			{
				//no columns to print
				return FillPrepareResult.NO_PRINT_NO_OVERFLOW;
			}
			
			FillPrepareResult result = fillSubreport.prepareSubreport(
					availableHeight, filling);
			filling = result.willOverflow();
			return result;
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public JRPrintElement fill()
	{
		JRTemplatePrintFrame printFrame = new JRTemplatePrintFrame(getFrameTemplate(), elementId);
		printFrame.setX(fillContext.getComponentElement().getX());
		printFrame.setY(fillContext.getElementPrintY());
		printFrame.setWidth(fillWidth);
		printFrame.setHeight(fillSubreport.getContentsStretchHeight());
		
		List<JRStyle> styles = fillSubreport.getSubreportStyles();
		for (Iterator<JRStyle> it = styles.iterator(); it.hasNext();)
		{
			JRStyle style = it.next();
			try
			{
				fillContext.getFiller().addPrintStyle(style);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		
		List<JROrigin> origins = fillSubreport.getSubreportOrigins();
		for (Iterator<JROrigin> it = origins.iterator(); it.hasNext();)
		{
			JROrigin origin = it.next();
			fillContext.getFiller().getJasperPrint().addOrigin(origin);
		}
		
		Collection<JRPrintElement> elements = fillSubreport.getPrintElements();
		if (elements != null)
		{
			for (Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();)
			{
				JRPrintElement element = it.next();
				printFrame.addElement(element);
			}
		}
		
		return printFrame;
	}

	protected JRTemplateFrame getFrameTemplate()
	{
		JRStyle style = fillContext.getElementStyle();
		JRTemplateFrame frameTemplate = printFrameTemplates.get(style);
		if (frameTemplate == null)
		{
			frameTemplate = new JRTemplateFrame(
						fillContext.getElementOrigin(),
						fillContext.getDefaultStyleProvider());
			frameTemplate.setElement(fillContext.getComponentElement());
			frameTemplate = deduplicate(frameTemplate);
			
			printFrameTemplates.put(style, frameTemplate);
		}

		return frameTemplate;
	}

	@Override
	public void rewind()
	{
		if (filling)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Rewinding table subreport");
			}
			
			try
			{
				fillSubreport.rewind();
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
			
			filling = false;
		}
	}

}
