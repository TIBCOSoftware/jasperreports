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
package net.sf.jasperreports.components.table.fill;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.components.table.TableComponent;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
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

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillTable extends BaseFillComponent
{

	private final TableComponent table;
	private final JRFillObjectFactory factory;
	private FillTableSubreport fillSubreport;
	
	private Map printFrameTemplates = new HashMap();

	public FillTable(TableComponent table, JRFillObjectFactory factory)
	{
		this.table = table;
		this.factory = factory;
	}

	public void evaluate(byte evaluation) throws JRException
	{
		JasperReport parentReport = fillContext.getFiller().getJasperReport();
		JRDataset reportSubdataset = JRReportUtils.findSubdataset(table.getDatasetRun(), 
				parentReport);
		TableReport tableReport = createTableReport(reportSubdataset, parentReport);
		
		Serializable compileData = parentReport.getCompileData();
		if (!(compileData instanceof JRReportCompileData))
		{
			throw new JRRuntimeException("Unsupported compiled report data of type " 
					+ compileData.getClass().getName());
		}
		
		JRReportCompileData reportCompileData = (JRReportCompileData) compileData;
		Serializable datasetCompileData = reportCompileData.getDatasetCompileData(
				reportSubdataset);
		
		JRReportCompileData tableReportCompileData = new JRReportCompileData();
		tableReportCompileData.setMainDatasetCompileData(datasetCompileData);
		
		JasperReport compiledTableReport = new JasperReport(tableReport, 
				parentReport.getCompilerClass(), 
				tableReportCompileData, 
				new TableReportBaseObjectFactory(),
				// no suffix as already included in the report name
				"");
		
		TableSubreport subreport = new TableSubreport(table.getDatasetRun(), fillContext);
		//TODO new factory, run in a container
		fillSubreport = new FillTableSubreport(
				fillContext.getFiller(), subreport, factory, compiledTableReport);
		
		fillSubreport.evaluateSubreport(evaluation);
	}

	protected TableReport createTableReport(JRDataset reportSubdataset, JasperReport parentReport)
	{
		String tableReportName = JRAbstractCompiler.getUnitName(parentReport, reportSubdataset);
		TableReportDataset reportDataset = new TableReportDataset(reportSubdataset, tableReportName);
		TableReport tableReport = new TableReport(table, fillContext, reportDataset);
		return tableReport;
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		try
		{
			return fillSubreport.prepareSubreport(availableHeight, false);//TODO overflow
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public JRPrintElement fill()
	{
		JRTemplatePrintFrame printFrame = new JRTemplatePrintFrame(getFrameTemplate());
		printFrame.setX(fillContext.getComponentElement().getX());
		printFrame.setY(fillContext.getElementPrintY());
		printFrame.setWidth(fillContext.getComponentElement().getWidth());
		printFrame.setHeight(fillSubreport.getStretchHeight());//TODO?
		
		Collection elements = fillSubreport.getPrintElements();
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			JRPrintElement element = (JRPrintElement) it.next();
			printFrame.addElement(element);
		}
		
		return printFrame;
	}

	protected JRTemplateFrame getFrameTemplate()
	{
		JRStyle style = fillContext.getElementStyle();
		JRTemplateFrame frameTemplate = (JRTemplateFrame) printFrameTemplates.get(style);
		if (frameTemplate == null)
		{
			frameTemplate = new JRTemplateFrame(
						fillContext.getElementOrigin(),
						fillContext.getDefaultStyleProvider());
			frameTemplate.setElement(fillContext.getComponentElement());
			
			printFrameTemplates.put(style, frameTemplate);
		}

		return frameTemplate;
	}

}
