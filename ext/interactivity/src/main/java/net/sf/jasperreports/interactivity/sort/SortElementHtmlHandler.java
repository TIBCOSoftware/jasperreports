/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.interactivity.sort;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.velocity.VelocityContext;

import net.sf.jasperreports.components.BaseElementHtmlHandler;
import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.components.sort.FieldFilter;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.interactivity.commands.CommandTarget;
import net.sf.jasperreports.interactivity.sort.actions.FilterAction;
import net.sf.jasperreports.interactivity.sort.actions.SortAction;
import net.sf.jasperreports.jackson.util.JacksonUtil;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.velocity.util.VelocityUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SortElementHtmlHandler extends BaseElementHtmlHandler
{
	private static final String CSS_FILTER_DEFAULT = 		"filterBtnDefault";
	private static final String CSS_FILTER_WRONG = 			"filterBtnWrong";
	private static final String CSS_SORT_DEFAULT_ASC = 		"sortAscBtnDefault";
	private static final String CSS_SORT_DEFAULT_DESC = 	"sortDescBtnDefault";

	private static final String SORT_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/sort/resources/SortElementHtmlTemplate.vm";
	
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";

	protected static final String FILTER_SYMBOL_ACTIVE = "Active";
	protected static final String FILTER_SYMBOL_INACTIVE = "Inactive";

	@Override
	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		String htmlFragment = null;
		Exporter<ExporterInput, ? extends HtmlReportConfiguration, ? extends HtmlExporterConfiguration, HtmlExporterOutput> exporter = context.getExporterRef();
		ReportContext reportContext = exporter.getReportContext();
		if (reportContext != null)//FIXMEJIVE
		{
			String sortColumnName = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_NAME);
			String sortColumnType = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_TYPE);
			
			String sortHandlerVAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN);
			String sortHandlerHAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN);
			String sortDatasetName = element.getPropertiesMap().getProperty(SortElement.PROPERTY_DATASET_RUN);
			
			JRBaseFont sortHandlerFont = (JRBaseFont) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT);
			if (sortHandlerFont == null) {
				sortHandlerFont = new JRBaseFont(element);
			}

			Color sortHandlerColor = (Color) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_COLOR);
			if (sortHandlerColor == null) {
				sortHandlerColor = Color.WHITE;
			}
			
			FilterTypesEnum filterType = FilterTypesEnum.getByName(element.getPropertiesMap().getProperty(SortElement.PROPERTY_FILTER_TYPE));
			if (filterType == null)//FIXMEJIVE
			{
				return null;
			}
			
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("uuid", element.getUUID().toString());

			velocityContext.put("elementWidth", element.getWidth());
			velocityContext.put("elementHeight", element.getHeight());
			velocityContext.put("sortHandlerHAlign", sortHandlerHAlign != null ? sortHandlerHAlign : CSS_TEXT_ALIGN_LEFT);
			velocityContext.put("sortHandlerVAlign", sortHandlerVAlign != null ? sortHandlerVAlign : HTML_VERTICAL_ALIGN_TOP);
			velocityContext.put("sortHandlerColor", JRColorUtil.getColorHexa(sortHandlerColor));
			velocityContext.put("sortHandlerFontSize", sortHandlerFont.getFontSize());
			
			if (element.getMode() == ModeEnum.OPAQUE)
			{
				velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}

			String sortField = getCurrentSortField(context.getJasperReportsContext(), reportContext, element.getUUID().toString(), sortDatasetName, sortColumnName, sortColumnType);
			if (sortField == null)
			{
				velocityContext.put("isSorted", false);
			}
			else 
			{
				String[] sortActionData = SortElementUtils.extractColumnInfo(sortField);
				boolean isAscending = SortElement.SORT_ORDER_ASC.equals(sortActionData[2]);
				velocityContext.put("isSorted", true);
				velocityContext.put("sortSymbolResource", isAscending ? CSS_SORT_DEFAULT_ASC : CSS_SORT_DEFAULT_DESC);
			}

			// existing filters
			String filterActiveInactive = FILTER_SYMBOL_INACTIVE;
			boolean isFiltered = false;
			List<FieldFilter> fieldFilters;
			String filterSymbolImageResource = CSS_FILTER_DEFAULT;

			fieldFilters = getExistingFiltersForField(context.getJasperReportsContext(), reportContext, element.getUUID().toString(), sortColumnName);
			
			if (fieldFilters.size() > 0) {
				FieldFilter ff = fieldFilters.get(0);
				filterActiveInactive = FILTER_SYMBOL_ACTIVE;
				isFiltered = true;
				if (ff.getIsValid() != null && !ff.getIsValid()) {
					filterSymbolImageResource = CSS_FILTER_WRONG;
				}
				
			}
			
			velocityContext.put("isFiltered", isFiltered);
			velocityContext.put("filterSymbolImageResource", filterSymbolImageResource);
			velocityContext.put("filterActiveInactive", filterActiveInactive);

			htmlFragment = VelocityUtil.processTemplate(SortElementHtmlHandler.SORT_ELEMENT_HTML_TEMPLATE, velocityContext);
		}
		
		return htmlFragment;
	}
	
	private String getCurrentSortField(JasperReportsContext jasperReportsContext, ReportContext reportContext, String uuid, String sortDatasetName, String sortColumnName, String sortColumnType) 
	{
		JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
		SortAction action = new SortAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(uuid), false);
		if (target != null)
		{
			JasperDesign jasperDesign = cache.getJasperDesign(target.getUri(), false);
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getMainDataset();
			
			List<JRSortField> existingFields =  dataset.getSortFieldsList();
			String sortField = null;
	
			if (existingFields != null && existingFields.size() > 0) {
				for (JRSortField field: existingFields) {
					if (field.getName().equals(sortColumnName) && field.getType().getName().equals(sortColumnType)) {
						sortField = sortColumnName + SortElement.SORT_COLUMN_TOKEN_SEPARATOR + sortColumnType + SortElement.SORT_COLUMN_TOKEN_SEPARATOR;
						switch (field.getOrder()) {
							case ASCENDING:
								sortField += SortElement.SORT_ORDER_ASC;
								break;
							case DESCENDING:
								sortField += SortElement.SORT_ORDER_DESC;
								break;
						}
						break;
					}
				}
			}
		
			return sortField;
		}
		
		return null;
	}
	
	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
	private List<FieldFilter> getExistingFiltersForField(
		JasperReportsContext jasperReportsContext, 
		ReportContext reportContext, 
		String uuid, 
		String filterFieldName
		) 
	{
		JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
		FilterAction action = new FilterAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(uuid), false);
		List<FieldFilter> result = new ArrayList<>();
		if (target != null)
		{
			JasperDesign jasperDesign = cache.getJasperDesign(target.getUri(), false);
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getMainDataset();
			
			// get existing filter as JSON string
			String serializedFilters = "[]";
			JRPropertiesMap propertiesMap = dataset.getPropertiesMap();
			if (propertiesMap.getProperty(HeaderToolbarElement.DATASET_FILTER_PROPERTY) != null) {
				serializedFilters = propertiesMap.getProperty(HeaderToolbarElement.DATASET_FILTER_PROPERTY);
			}
			
			List<? extends DatasetFilter> existingFilters = JacksonUtil.getInstance(jasperReportsContext).loadList(serializedFilters, FieldFilter.class);
			if (existingFilters.size() > 0) {
				for (DatasetFilter filter: existingFilters) {
					if (((FieldFilter)filter).getField().equals(filterFieldName)) {
						result.add((FieldFilter)filter);
						break;
					}
				}
			}
		}
		
		return result;
	}
}
