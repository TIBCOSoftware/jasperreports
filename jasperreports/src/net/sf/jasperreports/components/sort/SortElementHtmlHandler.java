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
package net.sf.jasperreports.components.sort;

import java.io.IOException;
import java.io.StringWriter;

import net.sf.jasperreports.components.BaseElementHtmlHandler;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.web.servlets.ReportServlet;
import net.sf.jasperreports.web.servlets.ResourceServlet;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class SortElementHtmlHandler extends BaseElementHtmlHandler
{
	private static final String RESOURCE_SORT_JS = "net/sf/jasperreports/components/sort/resources/sort.js";
	private static final String RESOURCE_IMAGE_CLOSE = "net/sf/jasperreports/components/sort/resources/images/delete_edit.gif";
	private static final String SORT_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/sort/resources/SortElementHtmlTemplate.vm";
	
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";
	protected static final String ASCENDING_SORT_SYMBOL = "&#9650;";
	protected static final String DESCENDING_SORT_SYMBOL = "&#9660;";
	

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		String htmlFragment = null;
		ReportContext reportContext = context.getExporter().getReportContext();
		if (reportContext != null)//FIXMEJIVE
		{
			String sortColumnName = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_NAME);
			String sortColumnType = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_TYPE);
			
			String sortHandlerColor = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_COLOR);
			String sortHandlerFontSize = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT_SIZE);
			String sortHandlerVAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN);
			String sortHandlerHAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN);
			String sortDatasetName = element.getPropertiesMap().getProperty(SortElement.PROPERTY_DATASET_RUN);
			String isFilterable = element.getPropertiesMap().getProperty(SortElement.PROPERTY_IS_FILTERABLE);
			
			Template template = getVelocityEngine().getTemplate(SortElementHtmlHandler.SORT_ELEMENT_HTML_TEMPLATE);
			
			String appContextPath = (String)reportContext.getParameterValue("net.sf.jasperreports.web.app.context.path");//FIXMEJIVE define constant
			
			VelocityContext velocityContext = new VelocityContext();
			String webResourcesBasePath = JRProperties.getProperty("net.sf.jasperreports.web.resources.base.path");
			if (webResourcesBasePath == null)
			{
				webResourcesBasePath = ResourceServlet.DEFAULT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=";
			}
			velocityContext.put("resourceSortJs", webResourcesBasePath + SortElementHtmlHandler.RESOURCE_SORT_JS);
			velocityContext.put("elementX", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			velocityContext.put("elementY", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
			velocityContext.put("elementWidth", element.getWidth());
			velocityContext.put("elementHeight", element.getHeight());
			velocityContext.put("sortLinkClass", sortDatasetName);
			velocityContext.put("sortHandlerHAlign", sortHandlerHAlign != null ? sortHandlerHAlign : CSS_TEXT_ALIGN_LEFT);
			velocityContext.put("sortHandlerVAlign", sortHandlerVAlign != null ? sortHandlerVAlign : HTML_VERTICAL_ALIGN_TOP);
			velocityContext.put("sortHandlerColor", sortHandlerColor != null ? sortHandlerColor : "white");
			velocityContext.put("sortHandlerFontSize", sortHandlerFontSize != null ? sortHandlerFontSize : "10");
			
			velocityContext.put("isFilterable", isFilterable != null && isFilterable.equalsIgnoreCase("true"));
			velocityContext.put("filterDivId", "filter_" + sortDatasetName + "_" + sortColumnName);
			velocityContext.put("filterFormAction", getFilterFormActionLink(context));
			velocityContext.put("filterReportUriParamName", ReportServlet.REQUEST_PARAMETER_REPORT_URI);
			velocityContext.put("filterReportUriParamValue", reportContext.getParameterValue(ReportServlet.REQUEST_PARAMETER_REPORT_URI));
			velocityContext.put("filterFieldParamName", SortElement.REQUEST_PARAMETER_FILTER_FIELD);
			velocityContext.put("filterValueParamName", SortElement.REQUEST_PARAMETER_FILTER_VALUE);
			velocityContext.put("filterColumnName", sortColumnName);
			velocityContext.put("filterTableNameParam", SortElement.REQUEST_PARAMETER_DATASET_RUN);
			velocityContext.put("filterTableNameValue", sortDatasetName);
			velocityContext.put("filterCloseDialogImageResource", (appContextPath == null ? "" : appContextPath) + webResourcesBasePath + SortElementHtmlHandler.RESOURCE_IMAGE_CLOSE);//FIXMEJIVE
			
			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}

			String sortField = getCurrentSortField(reportContext, sortDatasetName, sortColumnName, sortColumnType);
			if (sortField == null) 
			{
				velocityContext.put("href", getSortLink(context, sortColumnName, sortColumnType, SortElement.SORT_ORDER_ASC, sortDatasetName));
				velocityContext.put("sortSymbol", "");
			}
			else 
			{
				String[] sortActionData = SortElementUtils.extractColumnInfo(sortField);
				boolean isAscending = !SortElement.SORT_ORDER_ASC.equals(sortActionData[2]);
				String sortOrder = isAscending ? SortElement.SORT_ORDER_ASC : SortElement.SORT_ORDER_DESC;
				velocityContext.put("href", getSortLink(context, sortColumnName, sortColumnType, sortOrder, sortDatasetName));
				velocityContext.put("sortSymbol", isAscending ? ASCENDING_SORT_SYMBOL : DESCENDING_SORT_SYMBOL);
			}
			
			StringWriter writer = new StringWriter(128);
			template.merge(velocityContext, writer);
			writer.flush();
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			htmlFragment = writer.getBuffer().toString();
		}
		
		return htmlFragment;
	}
	
	private String getSortLink(JRHtmlExporterContext context, String sortColumnName, String sortColumnType, String sortOrder, String sortTableName) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType("ReportExecution");
		
		JRPrintHyperlinkParameters parameters = new JRPrintHyperlinkParameters();
		parameters.addParameter(new JRPrintHyperlinkParameter(
				SortElement.REQUEST_PARAMETER_SORT_DATA,
				String.class.getName(), 
				SortElementUtils.packSortColumnInfo(sortColumnName, sortColumnType, sortOrder)));
		parameters.addParameter(new JRPrintHyperlinkParameter(SortElement.REQUEST_PARAMETER_DATASET_RUN, String.class.getName(), sortTableName));
		hyperlink.setHyperlinkParameters(parameters);
		
		return context.getHyperlinkURL(hyperlink);
	}
	
	private String getFilterFormActionLink(JRHtmlExporterContext context) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType("ReportExecution");
		return context.getHyperlinkURL(hyperlink);
	}
	
	
	private String getCurrentSortField(ReportContext reportContext, String sortDatasetName, String sortColumnName, String sortColumnType) 
	{
		String currentSortDataset = (String) reportContext.getParameterValue(
				SortElement.REQUEST_PARAMETER_DATASET_RUN);
		if (sortDatasetName == null || !sortDatasetName.equals(currentSortDataset))
		{
			// sorting is on a different dataset
			return null;
		}
		
		String sortField = null;
		String sortData = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_SORT_DATA);
		
		if (SortElementUtils.isValidSortData(sortData))
		{
			String[] tokens = sortData.split(",");
			for (int i = 0; i < tokens.length; i++)
			{
				String token = tokens[i];
				String[] sortActionData = SortElementUtils.extractColumnInfo(token);
				if (sortActionData.length > 1 && sortActionData[0].equals(sortColumnName) && sortActionData[1].equals(sortColumnType)) {
					sortField = token;
					break;
				}
			}
		}
		
		return sortField;
	}
	
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
 }
