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
package net.sf.jasperreports.components.table.fill;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.DatasetExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRFillSubreport;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillTableSubreport extends JRFillSubreport
{

	private final TableJasperReport tableReport;
	private final BuiltinExpressionEvaluatorFactory builtinEvaluatorFactory;

	protected FillTableSubreport(FillContext fillContext, JRSubreport subreport,
			JRFillObjectFactory factory, TableJasperReport tableReport, 
			BuiltinExpressionEvaluatorFactory builtinEvaluatorFactory)
	{
		super(fillContext.getFiller(), subreport, factory);
		
		this.fillContainerContext = fillContext.getFillContainerContext();
		this.tableReport = tableReport;
		this.builtinEvaluatorFactory = builtinEvaluatorFactory;
	}

	public FillTableSubreport(FillTableSubreport tableSubreport, JRFillCloneFactory factory)
	{
		super(tableSubreport, factory);
		
		this.fillContainerContext = tableSubreport.fillContainerContext;
		this.tableReport = tableSubreport.tableReport;
		this.builtinEvaluatorFactory = tableSubreport.builtinEvaluatorFactory;
	}

	public TableJasperReport getTableReport()
	{
		return tableReport;
	}

	@Override
	protected JasperReport evaluateReport(byte evaluation) throws JRException
	{
		return tableReport;
	}

	@Override
	protected DatasetExpressionEvaluator createEvaluator() throws JRException
	{
		DatasetExpressionEvaluator evaluator = super.createEvaluator();
		return builtinEvaluatorFactory.decorate(evaluator);
	}
	
	@Override
	protected void evaluateSubreport(byte evaluation) throws JRException
	{
		// overriding this for package access
		super.evaluateSubreport(evaluation);
	}

	@Override
	protected Map<String, Object> evaluateParameterValues(byte evaluation) throws JRException
	{
		Map<String, Object> values = super.evaluateParameterValues(evaluation);
		copyConnectionParameter(values);
		copyResourceBundleParameter(values);
		copyTemplatesParameter(values);
		// TODO other built-in parameters?
		return values;
	}

	@Override
	protected boolean isReorderBandElements()
	{
		return true;
	}
	
	protected void copyConnectionParameter(Map<String, Object> parameterValues)
	{
		// copy the main report's connection parameter to the table subreport
		// this is done for consistency with subdataset runs
		if (!parameterValues.containsKey(JRParameter.REPORT_CONNECTION)
				&& getConnectionExpression() == null)
		{
			JRQuery query = tableReport.getQuery();
			if (query != null)
			{
				String language = query.getLanguage();
				if (language.equals("sql") || language.equals("SQL"))
				{
					Connection connection = (Connection) filler.getParameterValuesMap().get(
							JRParameter.REPORT_CONNECTION);
					if (connection != null)
					{
						parameterValues.put(JRParameter.REPORT_CONNECTION, connection);
					}
				}
			}
		}
	}

	protected void copyResourceBundleParameter(Map<String, Object> parameterValues)
	{
		// copy the main report's resource bundle if the subdataset has no
		// resource bundle of its own
		if (!parameterValues.containsKey(JRParameter.REPORT_RESOURCE_BUNDLE)
				&& tableReport.getResourceBundle() == null)
		{
			ResourceBundle resourceBundle = (ResourceBundle) filler.getParameterValuesMap().get(
					JRParameter.REPORT_RESOURCE_BUNDLE);
			if (resourceBundle != null)
			{
				parameterValues.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
			}
		}
	}

	protected void copyTemplatesParameter(Map<String, Object> parameterValues)
	{
		// copy the main report's templates
		List<JRTemplate> templates = filler.getTemplates();
		parameterValues.put(JRParameter.REPORT_TEMPLATES, templates);
	}
	
	protected FillPrepareResult prepareSubreport(int availableHeight, boolean isOverflow) 
		throws JRException
	{
		boolean willOverflow = prepare(availableHeight, isOverflow);
		FillPrepareResult result;
		if (printPage == null)
		{
			// don't produce any result
			result = FillPrepareResult.NO_PRINT_NO_OVERFLOW;
		}
		else
		{
			result = FillPrepareResult.printStretch(getStretchHeight(), willOverflow);
		}
		return result;
	}
	
	@Override
	protected Collection<JRPrintElement> getPrintElements()
	{
		// overriding this for package access
		return super.getPrintElements();
	}

	@Override
	protected int getContentsStretchHeight()
	{
		// overriding this for package access
		return super.getContentsStretchHeight();
	}

	protected List<JRStyle> getSubreportStyles()
	{
		return subreportFiller.getJasperPrint().getStylesList();
	}

	protected List<JROrigin> getSubreportOrigins()
	{
		return subreportFiller.getJasperPrint().getOriginsList();
	}

	@Override
	protected void cancelSubreportFill() throws JRException
	{
		// overriding this for package access
		super.cancelSubreportFill();
	}

	@Override
	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		// not actually used, but implemented for safety
		return new FillTableSubreport(this, factory);
	}
}
