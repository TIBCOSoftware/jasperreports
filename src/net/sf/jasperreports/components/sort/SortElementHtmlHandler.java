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
package net.sf.jasperreports.components.sort;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.components.BaseElementHtmlHandler;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.servlets.AbstractViewer;
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
		HttpServletRequest request = (HttpServletRequest)context.getExportParameters().get(AbstractViewer.HTTP_REQUEST);
		if (request != null)//FIXMEJIVE
		{
			String sortColumnName = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_NAME);
			String sortColumnType = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_TYPE);
			String sortHandlerColor = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_COLOR);
			String sortHandlerFontSize = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT_SIZE);
			String sortHandlerVAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN);
			String sortHandlerHAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN);
			String sortTableName = element.getPropertiesMap().getProperty(JRProperties.PROPERTY_PREFIX + "export." + SortElement.PARAMETER_TABLE_NAME);
			String sortField = getCurrentSortField(request, sortColumnName, sortColumnType);
			
			Template template = getVelocityEngine().getTemplate(SortElementHtmlHandler.SORT_ELEMENT_HTML_TEMPLATE);
			
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("resourceSortJs", ResourceServlet.DEFAULT_CONTEXT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=" + SortElementHtmlHandler.RESOURCE_SORT_JS);
			velocityContext.put("elementX", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			velocityContext.put("elementY", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
			velocityContext.put("elementWidth", element.getWidth());
			velocityContext.put("elementHeight", element.getHeight());
			velocityContext.put("sortHandlerHAlign", sortHandlerHAlign != null ? sortHandlerHAlign : CSS_TEXT_ALIGN_LEFT);
			velocityContext.put("sortHandlerVAlign", sortHandlerVAlign != null ? sortHandlerVAlign : HTML_VERTICAL_ALIGN_TOP);
			velocityContext.put("sortHandlerColor", sortHandlerColor != null ? sortHandlerColor : "white");
			velocityContext.put("sortHandlerFontSize", sortHandlerFontSize != null ? sortHandlerFontSize : "10");
			
			velocityContext.put("filterDivId", "filter_dialog_" + this.hashCode());
			velocityContext.put("filterFormAction", getFilterFormActionLink(context));
			velocityContext.put("filterReportUriParamName", ReportServlet.REQUEST_PARAMETER_REPORT_URI);
			velocityContext.put("filterReportUriParamValue", request.getParameter(ReportServlet.REQUEST_PARAMETER_REPORT_URI));
			velocityContext.put("filterReportActionParamName", ReportServlet.REPORT_ACTION);
			velocityContext.put("filterReportActionParamValue", SortElementAction.NAME);
			velocityContext.put("filterFieldParamName", SortElement.PARAMETER_FILTER_FIELD);
			velocityContext.put("filterValueParamName", SortElement.PARAMETER_FILTER_VALUE);
			velocityContext.put("filterColumnName", sortColumnName);
			velocityContext.put("filterTableNameParam", SortElement.PARAMETER_TABLE_NAME);
			velocityContext.put("filterTableNameValue", sortTableName);
			velocityContext.put("filterCloseDialogImageResource", request.getContextPath() + ResourceServlet.DEFAULT_CONTEXT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=" + SortElementHtmlHandler.RESOURCE_IMAGE_CLOSE);
			
			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}
			
			if (sortField != null) 
			{
				String[] sortActionData = sortField.split(":");
				boolean isAscending = !"Ascending".equals(sortActionData[2]);
				velocityContext.put("href", getSortLink(context, sortColumnName, sortColumnType, isAscending, sortTableName));
				velocityContext.put("sortSymbol", isAscending ? ASCENDING_SORT_SYMBOL : DESCENDING_SORT_SYMBOL);
				
			} else {
				velocityContext.put("href", getSortLink(context, sortColumnName, sortColumnType, true, sortTableName));
				velocityContext.put("sortSymbol", "");
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
	
	private String getSortLink(JRHtmlExporterContext context, String sortColumnName, String sortColumnType, boolean isAscending , String sortTableName) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType("ReportExecution");
		
		JRPrintHyperlinkParameters parameters = new JRPrintHyperlinkParameters();
		parameters.addParameter(new JRPrintHyperlinkParameter(ReportServlet.REPORT_ACTION, String.class.getName(), SortElementAction.NAME));
		parameters.addParameter(new JRPrintHyperlinkParameter(SortElementAction.REPORT_ACTION_DATA, String.class.getName(), sortColumnName + ":" + sortColumnType + ":" +
				(isAscending?"Ascending":"Descending")));
		parameters.addParameter(new JRPrintHyperlinkParameter(SortElement.PARAMETER_TABLE_NAME, String.class.getName(), sortTableName));
		hyperlink.setHyperlinkParameters(parameters);
		
		return context.getHyperlinkURL(hyperlink);
	}
	
	private String getFilterFormActionLink(JRHtmlExporterContext context) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType("ReportExecution");
		return context.getHyperlinkURL(hyperlink);
	}
	
	
	private String getCurrentSortField(HttpServletRequest request, String sortColumnName, String sortColumnType) 
	{
		String result = null;
		String reportActionData = request.getParameter(SortElementAction.REPORT_ACTION_DATA);
		
		if (reportActionData != null && reportActionData.indexOf(":") != -1 && reportActionData.split(":").length > 1)
		{
			String[] tokens = reportActionData.split(",");
			for (int i = 0; i < tokens.length; i++)
			{
				String token = tokens[i];
				String[] sortActionData = token.split(":");
				if (sortActionData.length > 1 && sortActionData[0].equals(sortColumnName) && sortActionData[1].equals(sortColumnType)) {
					result = token;
					break;
				}
			}
		} else 
		{
			String reportUri = ReportServlet.extractReportUri(request.getParameter(ReportServlet.REQUEST_PARAMETER_REPORT_URI));
			WebReportContext webReportContext = WebReportContext.getInstance(request, false);
			if (webReportContext != null)
			{
				Map paramMap = webReportContext.getParameterValues();
				//Map paramMap = (Map)request.getSession().getAttribute(ReportServlet.REPORT_CONTEXT_PREFIX + reportUri);
				if (paramMap != null && paramMap.get(SortElementAction.SORT_FIELDS_PARAM) != null) {
					List<String> sortFields = (List<String>)paramMap.get(SortElementAction.SORT_FIELDS_PARAM);
					for (String _sortField: sortFields) {
						String[] sortActionData = _sortField.split(":");
						if (sortActionData.length > 1 && sortActionData[0].equals(sortColumnName) && sortActionData[1].equals(sortColumnType)) {
							result = _sortField;
							break;
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
 }
