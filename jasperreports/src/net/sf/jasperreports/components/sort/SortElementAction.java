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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.web.actions.Action;
import net.sf.jasperreports.web.actions.ActionContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FillServlet.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class SortElementAction implements Action
{
	private static final SortElementAction INSTANCE = new SortElementAction();
	
	public static final String NAME = "sort";
	public static final String REPORT_ACTION_DATA = "report.action.data";
	
	public static final String SORT_FIELDS_PARAM = "_sortFields";
	
	/**
	 * 
	 */
	private SortElementAction() 
	{
	}

	/**
	 * 
	 */
	public static SortElementAction getInstance() 
	{
		return INSTANCE;
	}

	/**
	 * 
	 */
	public String getName()
	{
		return NAME;
	}

	/**
	 *
	 */
	public void run(ActionContext context)
	{
		String reportActionData = context.getRequest().getParameter(REPORT_ACTION_DATA);
		String paramTableName = context.getRequest().getParameter(SortElement.PARAMETER_TABLE_NAME);
		
		Map<String, Object> reportContext = (Map<String, Object>)context.getReportParameters().get(JRParameter.REPORT_PARAMETERS_MAP);
//		if (reportContext == null) {
//			reportContext = new HashMap<String, Object>();
//			context.getReportParameters().put(JRParameter.REPORT_PARAMETERS_MAP, reportContext);
//		}

		if (reportActionData != null && paramTableName != null)
		{
			List<JRSortField> sortFields = new ArrayList<JRSortField>();
			List<String> sortFieldsList = new ArrayList<String>();
			String[] tokens = reportActionData.split(",");
			for (int i = 0; i < tokens.length; i++)
			{
				String token = tokens[i];
				sortFieldsList.add(token);
				String[] chunks = token.split(":");
				sortFields.add(
					new JRDesignSortField(
						chunks[0],
						SortFieldTypeEnum.getByName(chunks[1]),
						SortOrderEnum.getByName(chunks[2])
						)
					);
			}
			context.getReportParameters().put(SortElementAction.SORT_FIELDS_PARAM, sortFieldsList);
			reportContext.put(paramTableName, sortFields);
		}
		if (paramTableName != null)
		{
			String paramFieldName = context.getRequest().getParameter(SortElement.PARAMETER_FILTER_FIELD);
			String paramFieldValue = context.getRequest().getParameter(SortElement.PARAMETER_FILTER_VALUE);
			
			if (paramFieldName != null && paramFieldValue != null)
			{
				reportContext.put(paramTableName + "." + SortElement.PARAMETER_FILTER_FIELD, paramFieldName);
				reportContext.put(paramTableName + "." + SortElement.PARAMETER_FILTER_VALUE, paramFieldValue);
			}
		}
	}
}
