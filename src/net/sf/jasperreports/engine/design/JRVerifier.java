/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.fill.JRPercentageCalculator;
import net.sf.jasperreports.crosstabs.fill.JRPercentageCalculatorFactory;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRQueryExecuterUtils;


/**
 * A report verifier.
 *
 * <p>
 * The verifier checks that a report design meets certain rules in order to pass
 * report compilation.  
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRVerifier
{


	/**
	 *
	 */
	private static String[] textFieldClassNames = null;
	private static String[] imageClassNames = null;
	private static String[] subreportClassNames = null;

	private static Class[] templateTypes = new Class[] {
		String.class, java.io.File.class, java.net.URL.class, java.io.InputStream.class,
		JRTemplate.class};

	/**
	 *
	 */
	private JasperDesign jasperDesign = null;
	private Collection brokenRules = null;

	private JRExpressionCollector expressionCollector;


	/**
	 *
	 */
	protected JRVerifier(JasperDesign jrDesign)
	{
		this(jrDesign, null);
	}


	protected JRVerifier(JasperDesign jrDesign, JRExpressionCollector expressionCollector)
	{
		jasperDesign = jrDesign;
		brokenRules = new ArrayList();

		if (expressionCollector != null)
		{
			this.expressionCollector = expressionCollector;
		}
		else
		{
			this.expressionCollector = JRExpressionCollector.collector(jasperDesign);
		}
	}

	public JasperDesign getReportDesign()
	{
		return jasperDesign;
	}
	
	/**
	 * Logs a broken rule for the report.
	 * 
	 * @param message the message
	 * @param source the source object to which the rule applies; can be null
	 * if not available
	 */
	public void addBrokenRule(String message, Object source)
	{
		JRValidationFault fault = new JRValidationFault();
		fault.setMessage(message);
		fault.setSource(source);
		brokenRules.add(fault);
	}

	/**
	 * Logs a broken report rule which was caused by an exception.
	 * 
	 * @param e the exception that caused the broken rule
	 * @param source the source object if available
	 */
	public void addBrokenRule(Exception e, Object source)
	{
		JRValidationFault fault = new JRValidationFault();
		fault.setMessage(e.getMessage());
		fault.setSource(source);
		brokenRules.add(fault);
	}

	/**
	 * Validates a {@link JasperDesign report design}.
	 *
	 * @param jasperDesign the report design
	 * @param expressionCollector a collector which was used to collect expressions from the report design;
	 * 	if null, a new collector will be created and used to collect the expressions
	 *
	 * @return a list of {@link JRValidationFault design faults};
	 * 	the report design is valid iff the list is empty
	 */
	public static Collection verifyDesign(JasperDesign jasperDesign, JRExpressionCollector expressionCollector)
	{
		JRVerifier verifier = new JRVerifier(jasperDesign, expressionCollector);
		return verifier.verifyDesign();
	}

	/**
	 * Validates a {@link JasperDesign report design}.
	 *
	 * @param jasperDesign the report design
	 *
	 * @return a list of {@link JRValidationFault design faults};
	 * 	the report design is valid iff the list is empty
	 */
	public static Collection verifyDesign(JasperDesign jasperDesign)
	{
		return verifyDesign(jasperDesign, null);
	}

	/**
	 *
	 */
	protected Collection verifyDesign()
	{
		/*   */
		jasperDesign.preprocess();//FIXME either calculate twice or use change listeners

		/*   */
		verifyDesignAttributes();

		verifyReportTemplates();

		/*   */
		verifyReportFonts();

		verifyDataset(jasperDesign.getMainDesignDataset());

		verifyDatasets();

		/*   */
		verifyStyles();

		/*   */
		verifyBand(jasperDesign.getBackground());
		verifyBand(jasperDesign.getTitle());
		verifyBand(jasperDesign.getPageHeader());
		verifyBand(jasperDesign.getColumnHeader());
		verifyBand(jasperDesign.getDetail());
		verifyBand(jasperDesign.getColumnFooter());
		verifyBand(jasperDesign.getPageFooter());
		verifyBand(jasperDesign.getLastPageFooter());
		verifyBand(jasperDesign.getSummary());
		verifyBand(jasperDesign.getNoData());

		return brokenRules;
	}


	/**
	 *
	 */
	private void verifyDesignAttributes()
	{
		if (jasperDesign.getName() == null || jasperDesign.getName().trim().length() == 0)
		{
			addBrokenRule("Report name is missing.", jasperDesign);
		}

		if (jasperDesign.getColumnCount() <= 0)
		{
			addBrokenRule("Column count must be greater than zero.", jasperDesign);
		}

		if (jasperDesign.getPageWidth() < 0)
		{
			addBrokenRule("Page width must be positive.", jasperDesign);
		}

		if (jasperDesign.getPageHeight() < 0)
		{
			addBrokenRule("Page height must be positive.", jasperDesign);
		}

		if (jasperDesign.getColumnWidth() < 0)
		{
			addBrokenRule("Column width must be positive.", jasperDesign);
		}

		if (jasperDesign.getColumnSpacing() < 0)
		{
			addBrokenRule("Column spacing must be positive.", jasperDesign);
		}

		if (jasperDesign.getLeftMargin() < 0)
		{
			addBrokenRule("Left margin must be positive.", jasperDesign);
		}

		if (jasperDesign.getRightMargin() < 0)
		{
			addBrokenRule("Right margin must be positive.", jasperDesign);
		}

		if (jasperDesign.getTopMargin() < 0)
		{
			addBrokenRule("Top margin must be positive.", jasperDesign);
		}

		if (jasperDesign.getBottomMargin() < 0)
		{
			addBrokenRule("Bottom margin must be positive.", jasperDesign);
		}

		if (
			jasperDesign.getLeftMargin() +
			jasperDesign.getColumnCount() * jasperDesign.getColumnWidth() +
			(jasperDesign.getColumnCount() - 1) * jasperDesign.getColumnSpacing() +
			jasperDesign.getRightMargin() >
			jasperDesign.getPageWidth()
			)
		{
			addBrokenRule("The columns and the margins do not fit the page width.", jasperDesign);
		}

		if (
			jasperDesign.getTopMargin() +
			(jasperDesign.getBackground() != null ? jasperDesign.getBackground().getHeight() : 0) +
			jasperDesign.getBottomMargin() >
			jasperDesign.getPageHeight()
			)
		{
			addBrokenRule("The background section and the margins do not fit the page height.", jasperDesign);
		}

		if (jasperDesign.isTitleNewPage())
		{
			if (
				jasperDesign.getTopMargin() +
				(jasperDesign.getTitle() != null ? jasperDesign.getTitle().getHeight() : 0) +
				jasperDesign.getBottomMargin() >
				jasperDesign.getPageHeight()
				)
			{
				addBrokenRule("The title section and the margins do not fit the page height.", jasperDesign);
			}
		}
		else
		{
			if (
				jasperDesign.getTopMargin() +
				(jasperDesign.getTitle() != null ? jasperDesign.getTitle().getHeight() : 0) +
				(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
				(jasperDesign.getColumnHeader() != null ? jasperDesign.getColumnHeader().getHeight() : 0) +
				(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
				(jasperDesign.getPageFooter() != null ? jasperDesign.getPageFooter().getHeight() : 0) +
				jasperDesign.getBottomMargin() >
				jasperDesign.getPageHeight()
				)
			{
				addBrokenRule("The title section, the page and column headers and footers and the margins do not fit the page height.", jasperDesign);
			}
		}

		if (
			jasperDesign.getTopMargin() +
			(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
			(jasperDesign.getColumnHeader() != null ? jasperDesign.getColumnHeader().getHeight() : 0) +
			(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
			(jasperDesign.getPageFooter() != null ? jasperDesign.getPageFooter().getHeight() : 0) +
			jasperDesign.getBottomMargin() >
			jasperDesign.getPageHeight()
			)
		{
			addBrokenRule("The page and column headers and footers and the margins do not fit the page height.", jasperDesign);
		}

		if (
			jasperDesign.getTopMargin() +
			(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
			(jasperDesign.getColumnHeader() != null ? jasperDesign.getColumnHeader().getHeight() : 0) +
			(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
			(jasperDesign.getLastPageFooter() != null ? jasperDesign.getLastPageFooter().getHeight() : 0) +
			jasperDesign.getBottomMargin() >
			jasperDesign.getPageHeight()
			)
		{
			addBrokenRule("The page and column headers and footers and the margins do not fit the last page height.", jasperDesign);
		}

		if (
			jasperDesign.getTopMargin() +
			(jasperDesign.getSummary() != null ? jasperDesign.getSummary().getHeight() : 0) +
			jasperDesign.getBottomMargin() >
			jasperDesign.getPageHeight()
			)
		{
			addBrokenRule("The summary section and the margins do not fit the page height.", jasperDesign);
		}

		if (
			jasperDesign.getTopMargin() +
			(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
			(jasperDesign.getColumnHeader() != null ? jasperDesign.getColumnHeader().getHeight() : 0) +
			(jasperDesign.getDetail() != null ? jasperDesign.getDetail().getHeight() : 0) +
			(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
			(jasperDesign.getPageFooter() != null ? jasperDesign.getPageFooter().getHeight() : 0) +
			jasperDesign.getBottomMargin() >
			jasperDesign.getPageHeight()
			)
		{
			addBrokenRule("The detail section, the page and column headers and footers and the margins do not fit the page height.", jasperDesign);
		}

		if (
				jasperDesign.getTopMargin() +
				(jasperDesign.getNoData() != null ? jasperDesign.getNoData().getHeight() : 0) +
				jasperDesign.getBottomMargin() >
				jasperDesign.getPageHeight()
				)
		{
			addBrokenRule("The noData section and the margins do not fit the page height.", jasperDesign);
		}

		verifyFormatFactoryClass();
	}


	protected void verifyFormatFactoryClass()
	{
		String formatFactoryClassName = jasperDesign.getFormatFactoryClass();
		if (formatFactoryClassName != null)
		{
			try
			{
				Class formatFactoryClass = JRClassLoader.loadClassForName(formatFactoryClassName);
				if (!FormatFactory.class.isAssignableFrom(formatFactoryClass))
				{
					addBrokenRule("The report format factory class is not compatible with " + FormatFactory.class.getName(), jasperDesign);
				}
			}
			catch (ClassNotFoundException e)
			{
				addBrokenRule(e.toString(), jasperDesign);
			}
		}
	}


	/**
	 *
	 */
	private void verifyQuery(JRDesignDataset dataset)
	{
		JRQuery query = dataset.getQuery();
		if (query != null)
		{
			String language = query.getLanguage();
			JRQueryExecuterFactory queryExecuterFactory = null;
			if (language == null || language.length() == 0)
			{
				addBrokenRule("Query language not set.", query);
			}
			else
			{
				try
				{
					queryExecuterFactory = JRQueryExecuterUtils.getQueryExecuterFactory(query.getLanguage());
				}
				catch (JRException e1)
				{
					addBrokenRule("Query executer factory for " + language + " cannot be created.", query);
				}
			}


			JRQueryChunk[] chunks = query.getChunks();
			if (chunks != null && chunks.length > 0)
			{
				Map parametersMap = dataset.getParametersMap();

				for(int j = 0; j < chunks.length; j++)
				{
					JRQueryChunk queryChunk = chunks[j];
					switch (queryChunk.getType())
					{
						case JRQueryChunk.TYPE_PARAMETER :
						{
							JRParameter parameter = (JRParameter)parametersMap.get(queryChunk.getText());
							if ( parameter == null )
							{
								addBrokenRule("Query parameter not found : " + queryChunk.getText(), query);
							}
							else if (queryExecuterFactory != null)
							{
								String parameterType = null;
								try
								{
									parameterType = parameter.getValueClassName();
								}
								catch (JRRuntimeException e)
								{
									// ignore, already added when the parameter got verified
								}
								if (parameterType != null && !queryExecuterFactory.supportsQueryParameterType(parameterType))
								{
									addBrokenRule("Parameter type not supported in query : " + queryChunk.getText() + " class " + parameterType, query);
								}
							}

							break;
						}
						case JRQueryChunk.TYPE_PARAMETER_CLAUSE :
						{
							if (!parametersMap.containsKey(queryChunk.getText()))
							{
								addBrokenRule("Query parameter not found : " + queryChunk.getText(), query);
							}
							break;
						}
						case JRQueryChunk.TYPE_TEXT :
						default :
						{
						}
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyExpressions(List expressions, Map parametersMap, Map fieldsMap, Map variablesMap)
	{
		if (expressions != null && expressions.size() > 0)
		{
			for(Iterator it = expressions.iterator(); it.hasNext();)
			{
				JRExpression expression = (JRExpression)it.next();
				JRExpressionChunk[] chunks = expression.getChunks();
				if (chunks != null && chunks.length > 0)
				{
					for(int j = 0; j < chunks.length; j++)
					{
						JRExpressionChunk expressionChunk = chunks[j];
						switch (expressionChunk.getType())
						{
							case JRExpressionChunk.TYPE_VARIABLE :
							{
								if ( !variablesMap.containsKey(expressionChunk.getText()) )
								{
									addBrokenRule("Variable not found : " + expressionChunk.getText(), expression);
								}
								break;
							}
							case JRExpressionChunk.TYPE_FIELD :
							{
								if ( !fieldsMap.containsKey(expressionChunk.getText()) )
								{
									addBrokenRule("Field not found : " + expressionChunk.getText(), expression);
								}
								break;
							}
							case JRExpressionChunk.TYPE_PARAMETER :
							{
								if ( !parametersMap.containsKey(expressionChunk.getText()) )
								{
									addBrokenRule("Parameter not found : " + expressionChunk.getText(), expression);
								}
								break;
							}
							case JRExpressionChunk.TYPE_RESOURCE :
							case JRExpressionChunk.TYPE_TEXT :
							default :
							{
							}
						}
					}
				}
			}
		}
	}


	private void verifyExpressions(JRDesignDataset dataset)
	{
		verifyExpressions(
				expressionCollector.getExpressions(dataset),
				dataset.getParametersMap(),
				dataset.getFieldsMap(),
				dataset.getVariablesMap());
	}

	/**
	 *
	 */
	private void verifyReportFonts()
	{
		JRReportFont[] fonts = jasperDesign.getFonts();
		if (fonts != null && fonts.length > 0)
		{
			for(int index = 0; index < fonts.length; index++)
			{
				JRReportFont font = fonts[index];

				if (font.getName() == null || font.getName().trim().length() == 0)
				{
					addBrokenRule("Report font name missing.", font);
				}
			}
		}
	}


	protected void verifyReportTemplates()
	{
		JRReportTemplate[] templates = jasperDesign.getTemplates();
		if (templates != null)
		{
			for (int i = 0; i < templates.length; i++)
			{
				JRReportTemplate template = templates[i];
				verifyTemplate(template);
			}
		}
	}


	protected void verifyTemplate(JRReportTemplate template)
	{
		JRExpression sourceExpression = template.getSourceExpression();
		if (sourceExpression == null)
		{
			addBrokenRule("Template source expression missing.", template);
		}
		else
		{
			try
			{
				Class valueClass = sourceExpression.getValueClass();
				if (valueClass == null)
				{
					addBrokenRule("Template source expression value class not set.", sourceExpression);
				}
				else if (!verifyTemplateSourceType(valueClass))
				{
					addBrokenRule("Template source expression value class " + valueClass.getName() + "not supported.", sourceExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, sourceExpression);
			}
		}
	}


	protected boolean verifyTemplateSourceType(Class valueClass)
	{
		boolean valid = false;
		for (int i = 0; i < templateTypes.length; i++)
		{
			Class type = templateTypes[i];
			if (type.isAssignableFrom(valueClass))
			{
				valid = true;
				break;
			}
		}
		return valid;
	}

	/**
	 *
	 */
	private void verifyStyles()
	{
		JRStyle[] styles = jasperDesign.getStyles();
		if (styles != null && styles.length > 0)
		{
			for(int index = 0; index < styles.length; index++)
			{
				JRStyle style = styles[index];

				if (style.getName() == null || style.getName().trim().length() == 0)
				{
					addBrokenRule("Report style name missing.", style);
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyParameters(JRDesignDataset dataset)
	{
		JRParameter[] parameters = dataset.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int index = 0; index < parameters.length; index++)
			{
				JRParameter parameter = parameters[index];

				Object errorSource = parameter;
				if (parameter.isSystemDefined())
				{
					errorSource = jasperDesign;
				}

				if (parameter.getName() == null || parameter.getName().trim().length() == 0)
				{
					addBrokenRule("Parameter name missing.", errorSource);
				}

				if (parameter.getValueClassName() == null)
				{
					addBrokenRule("Class not set for parameter : " + parameter.getName(), errorSource);
				}
				else
				{
					try
					{
						Class parameterType = parameter.getValueClass();
						JRExpression expression = parameter.getDefaultValueExpression();
						if (expression != null)
						{
							try
							{
								if (expression.getValueClass() == null)
								{
									addBrokenRule("No value class defined for the expression in parameter: " + parameter.getName(), expression);
								}
								else
								{
									if (!parameterType.isAssignableFrom(expression.getValueClass()))
									{
										addBrokenRule("The parameter default value expression class is not compatible with the parameter's class : " + parameter.getName(), expression);
									}
								}
							}
							catch (JRRuntimeException e)
							{
								addBrokenRule(e, expression);
							}
						}
					}
					catch (JRRuntimeException e)
					{
						addBrokenRule(e, errorSource);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyFields(JRDesignDataset dataset)
	{
		JRField[] fields = dataset.getFields();
		if (fields != null && fields.length > 0)
		{
			for(int index = 0; index < fields.length; index++)
			{
				JRField field = fields[index];

				if (field.getName() == null || field.getName().trim().length() == 0)
				{
					addBrokenRule("Field name missing.", field);
				}

				try
				{
					Class fieldType = field.getValueClass();
					if (fieldType == null)
					{
						addBrokenRule("Class not set for field : " + field.getName(), field);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, field);
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifySortFields(JRDesignDataset dataset)
	{
		JRField[] fields = dataset.getFields();
		JRSortField[] sortFields = dataset.getSortFields();
		if (sortFields != null && sortFields.length > 0)
		{
			for(int index = 0; index < sortFields.length; index++)
			{
				JRSortField sortField = sortFields[index];
				String sortFieldName = sortField.getName();

				if (sortFieldName == null || sortFieldName.trim().length() == 0)
				{
					addBrokenRule("Sort field name missing.", sortField);
				}
				else
				{
					boolean isFound = false;

					int j = 0;
					while (!isFound && j < fields.length)
					{
						isFound = sortFieldName.equals(fields[j].getName());
						j++;
					}

					if (!isFound)
					{
						addBrokenRule("Sort field \"" + sortFieldName + "\" not declared.", sortField);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyVariables(JRDesignDataset dataset) throws JRRuntimeException
	{
		JRVariable[] variables = dataset.getVariables();
		if (variables != null && variables.length > 0)
		{
			boolean isMainDataset = dataset.isMainDataset();
			for(int index = 0; index < variables.length; index++)
			{
				JRVariable variable = variables[index];

				if (variable.getName() == null || variable.getName().trim().length() == 0)
				{
					addBrokenRule("Variable name missing.", variable);
				}

				try
				{
					Class valueClass = variable.getValueClass();
					if (valueClass == null)
					{
						addBrokenRule("Class not set for variable : " + variable.getName(), variable);
					}
					else
					{
						JRExpression expression = variable.getExpression();
						if (expression != null)
						{
							try
							{
								if (expression.getValueClass() == null)
								{
									addBrokenRule("No value class for the expression has been set in variable: " + variable.getName(), expression);
								}
								else
								{
									if (variable.getCalculation() != JRVariable.CALCULATION_COUNT && variable.getCalculation() != JRVariable.CALCULATION_DISTINCT_COUNT
											&& variable.getCalculation() != JRVariable.CALCULATION_SYSTEM && !valueClass.isAssignableFrom(expression.getValueClass()))
									{
										addBrokenRule("The variable expression class is not compatible with the variable's class : " + variable.getName(), expression);
									}
								}
							}
							catch (JRRuntimeException e)
							{
								addBrokenRule(e, expression);
							}
						}

						if (variable.getInitialValueExpression() != null)
						{
							try
							{
								if (!valueClass.isAssignableFrom(variable.getInitialValueExpression().getValueClass()))
								{
									addBrokenRule("The initial value class is not compatible with the variable's class : " + variable.getName(), variable.getInitialValueExpression());
								}
							}
							catch (JRRuntimeException e)
							{
								addBrokenRule(e, variable.getInitialValueExpression());
							}
						}
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, variable);
				}

				byte resetType = variable.getResetType();
				if (resetType == JRVariable.RESET_TYPE_GROUP)
				{
					if (variable.getResetGroup() == null)
					{
						addBrokenRule("Reset group missing for variable : " + variable.getName(), variable);
					}
					else
					{
						Map groupsMap = dataset.getGroupsMap();

						if (!groupsMap.containsKey(variable.getResetGroup().getName()))
						{
							addBrokenRule("Reset group \"" + variable.getResetGroup().getName() + "\" not found for variable : " + variable.getName(), variable);
						}
					}
				}

				byte incrementType = variable.getIncrementType();
				if (incrementType == JRVariable.RESET_TYPE_GROUP)
				{
					if (variable.getIncrementGroup() == null)
					{
						addBrokenRule("Increment group missing for variable : " + variable.getName(), variable);
					}
					else
					{
						Map groupsMap = dataset.getGroupsMap();

						if (!groupsMap.containsKey(variable.getIncrementGroup().getName()))
						{
							addBrokenRule("Increment group \"" + variable.getIncrementGroup().getName() + "\" not found for variable : " + variable.getName(), variable);
						}
					}
				}

				if (!isMainDataset)
				{
					if (resetType == JRVariable.RESET_TYPE_COLUMN || resetType == JRVariable.RESET_TYPE_PAGE)
					{
						addBrokenRule("Variable " + variable.getName() + " of dataset " + dataset.getName() + " cannot have Column or Page reset type.", variable);
					}

					if (incrementType == JRVariable.RESET_TYPE_COLUMN || incrementType == JRVariable.RESET_TYPE_PAGE)
					{
						addBrokenRule("Variable " + variable.getName() + " of dataset " + dataset.getName() + " cannot have Column or Page increment type.", variable);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyGroups(JRDesignDataset dataset)
	{
		JRGroup[] groups = dataset.getGroups();
		if (groups != null && groups.length > 0)
		{
			boolean isMainDataset = dataset.isMainDataset();
			for(int index = 0; index < groups.length; index++)
			{
				JRGroup group = groups[index];

				if (group.getName() == null || group.getName().trim().length() == 0)
				{
					addBrokenRule("Group name missing.", group);
				}

				if (isMainDataset)
				{
					verifyGroupHeaderAndFooter(group);
				}
				else
				{
					if (group.getGroupHeader() != null || group.getGroupFooter() != null)
					{
						addBrokenRule("Group " + group.getName() + " cannot have header or footer sections.", group);
					}
				}

				JRExpression expression = group.getExpression();

				if (expression != null)
				{
					try
					{
						Class clazz = expression.getValueClass();
						if (clazz == null)
						{
							addBrokenRule("Class not set for group expression : " + group.getName(), expression);
						}
					}
					catch (JRRuntimeException e)
					{
						addBrokenRule(e, expression);
					}
				}

				if (isMainDataset)
				{
					verifyBand(group.getGroupHeader());
					verifyBand(group.getGroupFooter());
				}
			}
		}
	}


	private void verifyGroupHeaderAndFooter(JRGroup group)
	{
		if (jasperDesign.isTitleNewPage())
		{
			if (
				jasperDesign.getTopMargin() +
				(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
				(jasperDesign.getColumnHeader() != null ? jasperDesign.getColumnHeader().getHeight() : 0) +
				(group.getGroupHeader() != null ? group.getGroupHeader().getHeight() : 0) +
				(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
				(jasperDesign.getPageFooter() != null ? jasperDesign.getPageFooter().getHeight() : 0) +
				jasperDesign.getBottomMargin() >
				jasperDesign.getPageHeight()
				)
			{
				addBrokenRule("The '" + group.getName() + "' group header section, the page and column headers and footers and the margins do not fit the page height.", group.getGroupHeader());
			}

			if (
				jasperDesign.getTopMargin() +
				(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
				(jasperDesign.getColumnHeader() != null ?  jasperDesign.getColumnHeader().getHeight() : 0) +
				(group.getGroupFooter() != null ? group.getGroupFooter().getHeight() : 0) +
				(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
				(jasperDesign.getPageFooter() != null ? jasperDesign.getPageFooter().getHeight() : 0) +
				jasperDesign.getBottomMargin() >
				jasperDesign.getPageHeight()
				)
			{
				addBrokenRule("The '" + group.getName() + "' group footer section, the page and column headers and footers and the margins do not fit the page height.", group.getGroupFooter());
			}
		}
		else
		{
			if (
				jasperDesign.getTopMargin() +
				(jasperDesign.getTitle() != null ? jasperDesign.getTitle().getHeight() : 0) +
				(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
				(jasperDesign.getColumnHeader() != null ? jasperDesign.getColumnHeader().getHeight() : 0) +
				(group.getGroupHeader() != null ? group.getGroupHeader().getHeight() : 0) +
				(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
				(jasperDesign.getPageFooter() != null ? jasperDesign.getPageFooter().getHeight() : 0) +
				jasperDesign.getBottomMargin() >
				jasperDesign.getPageHeight()
				)
			{
				addBrokenRule("The '" + group.getName() + "' group header section, the title, the page and column headers and footers and the margins do not fit the first page height.", group.getGroupHeader());
			}

			if (
				jasperDesign.getTopMargin() +
				(jasperDesign.getTitle() != null ? jasperDesign.getTitle().getHeight() : 0) +
				(jasperDesign.getPageHeader() != null ? jasperDesign.getPageHeader().getHeight() : 0) +
				(jasperDesign.getColumnHeader() != null ? jasperDesign.getColumnHeader().getHeight() : 0) +
				(group.getGroupFooter() != null ? group.getGroupFooter().getHeight() : 0) +
				(jasperDesign.getColumnFooter() != null ? jasperDesign.getColumnFooter().getHeight() : 0) +
				(jasperDesign.getPageFooter() != null ? jasperDesign.getPageFooter().getHeight() : 0) +
				jasperDesign.getBottomMargin() >
				jasperDesign.getPageHeight()
				)
			{
				addBrokenRule("The '" + group.getName() + "' group footer section, the title, the page and column headers and footers and the margins do not fit the first page height.", group.getGroupFooter());
			}
		}
	}


	/**
	 *
	 */
	private void verifyBand(JRBand band)
	{
		if (band != null)
		{
			JRElement[] elements = band.getElements();
			if (elements != null && elements.length > 0)
			{
				JRExpression expression = band.getPrintWhenExpression();

				if (expression != null)
				{
					try
					{
						Class clazz = expression.getValueClass();
						if (clazz == null)
						{
							addBrokenRule("Class not set for band \"print when\" expression.", expression);
						}
						else if (!java.lang.Boolean.class.isAssignableFrom(clazz))
						{
							addBrokenRule("Class " + clazz + " not supported for band \"print when\" expression. Use java.lang.Boolean instead.", expression);
						}
					}
					catch (JRRuntimeException e)
					{
						addBrokenRule(e, expression);
					}
				}

				for(int index = 0; index < elements.length; index++)
				{
					JRElement element = elements[index];

					verifyPrintWhenExpr(element);

					/*
					if (element.getY() < 0)
					{
						System.out.println(
							"Warning : Element placed outside band area : y=" + element.getY()
							);
						//addBrokenRule("Element placed outside band area.");
					}
					else if (element.getY() + element.getHeight() > band.getHeight())
					*/
					if (element.getY() + element.getHeight() > band.getHeight())
					{
//						if (log.isWarnEnabled())
//							log.warn(
//								"Warning : Element bottom reaches outside band area : y=" + element.getY() +
//								" height=" + element.getHeight() +
//								" band-height=" + band.getHeight()
//								);
						addBrokenRule(
							"Warning : Element bottom reaches outside band area : y=" + element.getY() +
							" height=" + element.getHeight() +
							" band-height=" + band.getHeight(),
							element
							);
					}

					verifyElement(element);
				}
			}
		}
	}


	public void verifyElement(JRElement element)
	{
		if (element instanceof JRStaticText)
		{
			verifyStaticText((JRStaticText)element);
		}
		else if (element instanceof JRTextField)
		{
			verifyTextField((JRTextField)element);
		}
		else if (element instanceof JRImage)
		{
			verifyImage((JRImage)element);
		}
		else if (element instanceof JRSubreport)
		{
			verifySubreport((JRSubreport)element);
		}
		else if (element instanceof JRCrosstab)
		{
			verifyCrosstab((JRDesignCrosstab) element);
		}
		else if (element instanceof JRChart)
		{
			verifyChart((JRChart) element);
		}
		else if (element instanceof JRFrame)
		{
			verifyFrame((JRFrame) element);
		}
		else if (element instanceof JRComponentElement)
		{
			verifyComponentElement((JRComponentElement) element);
		}
		else if (element instanceof JRGenericElement)
		{
			verifyGenericElement((JRGenericElement) element);
		}
	}


	private void verifyPrintWhenExpr(JRElement element)
	{
		JRExpression expression;
		expression = element.getPrintWhenExpression();

		if (expression != null)
		{
			try
			{
				Class clazz = expression.getValueClass();
				if (clazz == null)
				{
					addBrokenRule("Class not set for element \"print when\" expression.", expression);
				}
				else if (!java.lang.Boolean.class.isAssignableFrom(clazz))
				{
					addBrokenRule("Class " + clazz + " not supported for element \"print when\" expression. Use java.lang.Boolean instead.", expression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, expression);
			}
		}
	}


	/**
	 *
	 */
	private void verifyStaticText(JRStaticText staticText)
	{
		verifyReportElement(staticText);
		verifyFont(staticText);
	}


	/**
	 *
	 */
	private void verifyTextField(JRTextField textField)
	{
		verifyReportElement(textField);
		verifyFont(textField);
		verifyAnchor(textField);
		verifyHyperlink(textField);

		if (textField != null)
		{
			JRExpression expression = textField.getExpression();

			if (expression != null)
			{
				try
				{
					String className = expression.getValueClassName();
					if (className == null)
					{
						addBrokenRule("Class not set for text field expression.", expression);
					}
					else if (Arrays.binarySearch(getTextFieldClassNames(), className) < 0)
					{
						addBrokenRule("Class \"" + className + "\" not supported for text field expression.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyFont(JRFont font)
	{
		JRReportFont reportFont = font.getReportFont();

		if (reportFont != null && reportFont.getName() != null)
		{
			Map fontsMap = jasperDesign.getFontsMap();

			if (!fontsMap.containsKey(reportFont.getName()))
			{
				addBrokenRule("Report font not found : " + reportFont.getName(), font);
			}
		}
	}


	/**
	 *
	 */
	private void verifyAnchor(JRAnchor anchor)
	{
		if (anchor != null)
		{
			JRExpression expression = anchor.getAnchorNameExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for anchor name expression.", expression);
					}
					else if (!java.lang.String.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for anchor name expression. Use java.lang.String instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			if (anchor.getBookmarkLevel() != JRAnchor.NO_BOOKMARK && anchor.getBookmarkLevel() < 1)
			{
				addBrokenRule("Bookmark level should be " + JRAnchor.NO_BOOKMARK + " or greater than 0", anchor);
			}
		}
	}


	/**
	 *
	 */
	private void verifyHyperlink(JRHyperlink hyperlink)
	{
		if (hyperlink != null)
		{
			JRExpression expression = hyperlink.getHyperlinkReferenceExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for hyperlink reference expression.", expression);
					}
					else if (!java.lang.String.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for hyperlink reference expression. Use java.lang.String instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			expression = hyperlink.getHyperlinkAnchorExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for hyperlink anchor expression.", expression);
					}
					else if (!java.lang.String.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for hyperlink anchor expression. Use java.lang.String instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			expression = hyperlink.getHyperlinkPageExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for hyperlink page expression.", expression);
					}
					else if (!java.lang.Integer.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for hyperlink page expression. Use java.lang.Integer instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			expression = hyperlink.getHyperlinkTooltipExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for hyperlink tooltip expression.", expression);
					}
					else if (!java.lang.String.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for hyperlink tooltip expression. Use java.lang.String instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			JRHyperlinkParameter[] parameters = hyperlink.getHyperlinkParameters();
			if (parameters != null)
			{
				for (int i = 0; i < parameters.length; i++)
				{
					JRHyperlinkParameter parameter = parameters[i];
					verifyHyperlinkParameter(parameter);
				}
			}
		}
	}


	protected void verifyHyperlinkParameter(JRHyperlinkParameter parameter)
	{
		if (parameter != null)
		{
			String name = parameter.getName();
			if (name == null || name.length() == 0)
			{
				addBrokenRule("Hyperlink parameter name missing.", parameter);
			}
		}
	}


	/**
	 *
	 */
	private void verifyImage(JRImage image)
	{
		verifyReportElement(image);
		verifyAnchor(image);
		verifyHyperlink(image);

		if (image != null)
		{
			JRExpression expression = image.getExpression();

			if (expression != null)
			{
				try
				{
					String className = expression.getValueClassName();
					if (className == null)
					{
						addBrokenRule("Class not set for image expression.", expression);
					}
					else if (Arrays.binarySearch(getImageClassNames(), className) < 0)
					{
						addBrokenRule("Class \"" + className + "\" not supported for image expression.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifySubreport(JRSubreport subreport)
	{
		if (subreport != null)
		{
			verifyReportElement(subreport);
			
			JRExpression expression = subreport.getExpression();

			if (expression != null)
			{
				try
				{
					String className = expression.getValueClassName();
					if (className == null)
					{
						addBrokenRule("Class not set for subreport expression.", expression);
					}
					else if (Arrays.binarySearch(getSubreportClassNames(), className) < 0)
					{
						addBrokenRule("Class \"" + className + "\" not supported for subreport expression.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			expression = subreport.getParametersMapExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for subreport parameters map expression.", expression);
					}
					else if (!java.util.Map.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for subreport parameters map expression. Use java.util.Map instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			JRSubreportParameter[] parameters = subreport.getParameters();
			if (parameters != null && parameters.length > 0)
			{
				for(int index = 0; index < parameters.length; index++)
				{
					JRSubreportParameter parameter = parameters[index];

					if (parameter.getName() == null || parameter.getName().trim().length() == 0)
					{
						addBrokenRule("Subreport parameter name missing.", expression);
					}

					expression = parameter.getExpression();

					if (expression != null)
					{
						try
						{
							Class clazz = expression.getValueClass();
							if (clazz == null)
							{
								addBrokenRule("Class not set for subreport parameter expression : " + parameter.getName() + ". Use java.lang.Object class.", expression);
							}
						}
						catch (JRRuntimeException e)
						{
							addBrokenRule(e, expression);
						}
					}
				}
			}

			if (
				subreport.getConnectionExpression() != null &&
				subreport.getDataSourceExpression() != null
				)
			{
				addBrokenRule("Subreport cannot have both connection expresion and data source expression.", subreport);
			}

			expression = subreport.getConnectionExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for subreport connection expression.", expression);
					}
					else if (!java.sql.Connection.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for subreport connection expression. Use java.sql.Connection instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			expression = subreport.getDataSourceExpression();

			if (expression != null)
			{
				try
				{
					Class clazz = expression.getValueClass();
					if (clazz == null)
					{
						addBrokenRule("Class not set for subreport data source expression.", expression);
					}
					else if (!net.sf.jasperreports.engine.JRDataSource.class.isAssignableFrom(clazz))
					{
						addBrokenRule("Class " + clazz + " not supported for subreport data source expression. Use net.sf.jasperreports.engine.JRDataSource instead.", expression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, expression);
				}
			}

			JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
			if (returnValues != null && returnValues.length > 0)
			{
				for (int i = 0; i < returnValues.length; i++)
				{
					JRSubreportReturnValue returnValue = returnValues[i];

					if (returnValue.getSubreportVariable() == null || returnValue.getSubreportVariable().trim().length() == 0)
					{
						addBrokenRule("Subreport return value variable name missing.", returnValue);
					}

					if (returnValue.getToVariable() == null || returnValue.getToVariable().trim().length() == 0)
					{
						addBrokenRule("Subreport return value to variable name missing.", returnValue);
					}

					if (!jasperDesign.getVariablesMap().containsKey(returnValue.getToVariable()))
					{
						addBrokenRule("Subreport return value to variable not found.", returnValue);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private static synchronized String[] getTextFieldClassNames()
	{
		if (textFieldClassNames == null)
		{
			textFieldClassNames = new String[]
			{
				java.lang.Boolean.class.getName(),
				java.lang.Byte.class.getName(),
				java.util.Date.class.getName(),
				java.sql.Timestamp.class.getName(),
				java.sql.Time.class.getName(),
				java.lang.Double.class.getName(),
				java.lang.Float.class.getName(),
				java.lang.Integer.class.getName(),
				java.lang.Long.class.getName(),
				java.lang.Short.class.getName(),
				java.math.BigDecimal.class.getName(),
				java.lang.Number.class.getName(),
				java.lang.String.class.getName()
			};

			Arrays.sort(textFieldClassNames);
		}

		return textFieldClassNames;
	}


	/**
	 *
	 */
	private static synchronized String[] getImageClassNames()
	{
		if (imageClassNames == null)
		{
			imageClassNames = new String[]
			{
				java.lang.String.class.getName(),
				java.io.File.class.getName(),
				java.net.URL.class.getName(),
				java.io.InputStream.class.getName(),
				java.awt.Image.class.getName(),
				net.sf.jasperreports.engine.JRRenderable.class.getName()
			};

			Arrays.sort(imageClassNames);
		}

		return imageClassNames;
	}


	/**
	 *
	 */
	private static synchronized String[] getSubreportClassNames()
	{
		if (subreportClassNames == null)
		{
			subreportClassNames = new String[]
			{
				java.lang.String.class.getName(),
				java.io.File.class.getName(),
				java.net.URL.class.getName(),
				java.io.InputStream.class.getName(),
				net.sf.jasperreports.engine.JasperReport.class.getName()
			};

			Arrays.sort(subreportClassNames);
		}

		return subreportClassNames;
	}


	private void verifyCrosstab(JRDesignCrosstab crosstab)
	{
		verifyReportElement(crosstab);
		verifyParameters(crosstab);

		JRCrosstabDataset dataset = crosstab.getDataset();
		if (dataset == null)
		{
			addBrokenRule("Crosstab dataset missing.", crosstab);
		}
		else
		{
			verifyElementDataset(dataset);
		}

		verifyCellContents(crosstab.getHeaderCell(), "crosstab cell");

		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		if (rowGroups == null || rowGroups.length == 0)
		{
			addBrokenRule("Crosstab should have at least one row group.", crosstab);
		}
		else
		{
			for (int i = 0; i < rowGroups.length; i++)
			{
				verifyCrosstabRowGroup(rowGroups[i]);
			}
		}

		JRCrosstabColumnGroup[] colGroups = crosstab.getColumnGroups();
		if (colGroups == null || colGroups.length == 0)
		{
			addBrokenRule("Crosstab should have at least one column group.", crosstab);
		}
		else
		{
			for (int i = 0; i < colGroups.length; i++)
			{
				verifyCrosstabColumnGroup(colGroups[i]);
			}
		}

		JRCrosstabMeasure[] measures = crosstab.getMeasures();
		if (measures == null || measures.length == 0)
		{
			addBrokenRule("Crosstab should have at least one measure.", crosstab);
		}
		else
		{
			for (int i = 0; i < measures.length; i++)
			{
				verifyCrosstabMeasure(measures[i]);
			}
		}

		verifyCrosstabCells(crosstab);

		verifyCellContents(crosstab.getWhenNoDataCell(), "when no data cell");

		verifyExpressions(crosstab);
	}


	private void verifyParameters(JRDesignCrosstab crosstab)
	{
		JRExpression paramMapExpression = crosstab.getParametersMapExpression();

		if (paramMapExpression != null)
		{
			try
			{
				Class clazz = paramMapExpression.getValueClass();
				if (clazz == null)
				{
					addBrokenRule("Class not set for crosstab parameters map expression.", paramMapExpression);
				}
				else if (!java.util.Map.class.isAssignableFrom(clazz))
				{
					addBrokenRule("Class " + clazz + " not supported for crosstab parameters map expression. Use java.util.Map instead.", paramMapExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, paramMapExpression);
			}
		}

		JRCrosstabParameter[] parameters = crosstab.getParameters();
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				JRCrosstabParameter parameter = parameters[i];

				String paramName = parameter.getName();
				if (paramName == null || paramName.length() == 0)
				{
					addBrokenRule("Missing parameter name for crosstab.", parameter);
				}

				JRExpression expression = parameter.getExpression();
				Class expressionClass = null;
				if (expression != null)
				{
					try
					{
						expressionClass = expression.getValueClass();
						if (expressionClass == null)
						{
							addBrokenRule("Expression class not set for crosstab parameter " + paramName + ".", expression);
						}
					}
					catch (JRRuntimeException e)
					{
						addBrokenRule(e, expression);
					}
				}

				try
				{
					Class valueClass = parameter.getValueClass();
					if (valueClass == null)
					{
						addBrokenRule("Class not set for crosstab parameter " + paramName + ".", parameter);
					}
					else if (expressionClass != null && !valueClass.isAssignableFrom(expressionClass))
					{
						addBrokenRule("Incompatible expression class for crosstab parameter " + paramName + ".", parameter);
					}
				}
				catch (Exception e)
				{
					addBrokenRule(e, parameter);
				}
			}
		}
	}


	private void verifyCrosstabRowGroup(JRCrosstabRowGroup group)
	{
		verifyCrosstabGroup(group);
	}


	private void verifyCrosstabColumnGroup(JRCrosstabColumnGroup group)
	{
		verifyCrosstabGroup(group);
	}


	private void verifyCrosstabGroup(JRCrosstabGroup group)
	{
		String groupName = group.getName();
		if (groupName == null || groupName.length() == 0)
		{
			addBrokenRule("Crosstab group name missing.", group);
		}

		verifyCrosstabBucket(group);
		verifyCellContents(group.getHeader(), groupName + " header");
		if (group.hasTotal())
		{
			verifyCellContents(group.getTotalHeader(), groupName + " total header");
		}
	}


	private void verifyCrosstabBucket(JRCrosstabGroup group)
	{
		JRCrosstabBucket bucket = group.getBucket();

		JRExpression expression = bucket.getExpression();
		Class expressionClass = null;
		if (expression == null)
		{
			addBrokenRule("Crosstab bucket expression missing for group " + group.getName() + ".", bucket);
		}
		else
		{
			try
			{
				expressionClass = expression.getValueClass();
				if (expressionClass == null)
				{
					addBrokenRule("Crosstab bucket expression class missing for group " + group.getName() + ".", expression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, expression);
			}
		}

		try
		{
			Class valueClass = expression == null ? null : expression.getValueClass();
			if (valueClass == null)
			{
				addBrokenRule("Crosstab bucket value class missing for group " + group.getName() + ".", bucket);
			}
			else if (expressionClass != null && !valueClass.isAssignableFrom(expressionClass))
			{
				addBrokenRule("The class of the expression is not compatible with the class of the crosstab bucket for group " + group.getName() + ".", expression);
			}
			JRExpression comparatorExpression = bucket.getComparatorExpression();
			if (comparatorExpression == null)
			{
				if (valueClass != null && !Comparable.class.isAssignableFrom(valueClass))
				{
					addBrokenRule("No comparator expression specified and the value class is not comparable for crosstab group " + group.getName() + ".", bucket);
				}
			}
			else
			{
				try
				{
					Class comparatorClass = comparatorExpression.getValueClass();
					if (comparatorClass == null)
					{
						addBrokenRule("Crosstab bucket comparator expression class missing for group " + group.getName() + ".", comparatorExpression);
					}
					else if (!Comparator.class.isAssignableFrom(comparatorClass))
					{
						addBrokenRule("The comparator expression should be compatible with java.util.Comparator for crosstab group " + group.getName() + ".", comparatorExpression);
					}
				}
				catch (JRRuntimeException e)
				{
					addBrokenRule(e, comparatorExpression);
				}
			}
		}
		catch (JRRuntimeException e)
		{
			addBrokenRule(e, expression);
		}
	}


	private void verifyCrosstabCells(JRDesignCrosstab crosstab)
	{
		JRCrosstabCell[][] cells = crosstab.getCells();
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();

		JRCrosstabCell baseCell = cells[rowGroups.length][columnGroups.length];
		if(baseCell == null || baseCell.getWidth() == null)
		{
			addBrokenRule("Crosstab base cell width not specified.", crosstab);
		}

		if(baseCell == null || baseCell.getHeight() == null)
		{
			addBrokenRule("Crosstab base cell height not specified.", crosstab);
		}

		for (int i = rowGroups.length; i >= 0 ; --i)
		{
			for (int j = columnGroups.length; j >= 0 ; --j)
			{
				JRCrosstabCell cell = cells[i][j];

				String cellText = getCrosstabCellText(rowGroups, columnGroups, i, j);

				if (cell != null)
				{
					JRCellContents contents = cell.getContents();

					if (i < rowGroups.length)
					{
						JRCrosstabCell colCell = cells[rowGroups.length][j];
						if (colCell != null && colCell.getContents().getWidth() != contents.getWidth())
						{
							addBrokenRule("Crosstab " + cellText + " width should be " + colCell.getContents().getWidth() + ".", cell);
						}
					}

					if (j < columnGroups.length)
					{
						JRCrosstabCell rowCell = cells[i][columnGroups.length];
						if (rowCell != null && rowCell.getContents().getHeight() != contents.getHeight())
						{
							addBrokenRule("Crosstab " + cellText + " height should be " + rowCell.getContents().getHeight() + ".", cell);
						}
					}

					verifyCellContents(contents, cellText);
				}
			}
		}
	}

	private String getCrosstabCellText(JRCrosstabRowGroup[] rowGroups, JRCrosstabColumnGroup[] columnGroups,
			int rowIndex, int columnIndex)
	{
		String text;

		if (rowIndex == rowGroups.length)
		{
			if (columnIndex == columnGroups.length)
			{
				text = "cell";
			}
			else
			{
				text = columnGroups[columnIndex].getName() + " total cell";
			}
		}
		else
		{
			if (columnIndex == columnGroups.length)
			{
				text = rowGroups[rowIndex].getName() + " total cell";
			}
			else
			{
				text = rowGroups[rowIndex].getName() + "," + columnGroups[columnIndex].getName() + " total cell";
			}
		}

		return text;
	}

	private void verifyCrosstabMeasure(JRCrosstabMeasure measure)
	{
		String measureName = measure.getName();
		if (measureName == null || measureName.trim().length() == 0)
		{
			addBrokenRule("Measure name missing.", measure);
		}

		byte calculation = measure.getCalculation();
		if (calculation == JRVariable.CALCULATION_SYSTEM)
		{
			addBrokenRule("Crosstab mesures cannot have system calculation", measure);
		}

		JRExpression valueExpression = measure.getValueExpression();
		Class expressionClass = null;
		if (valueExpression == null)
		{
			addBrokenRule("Missing expression for measure " + measureName, measure);
		}
		else
		{
			try
			{
				expressionClass = valueExpression.getValueClass();
				if (expressionClass == null)
				{
					addBrokenRule("Crosstab measure expression class missing for " + measureName + ".", valueExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, valueExpression);
			}
		}

		try
		{
			Class valueClass = measure.getValueClass();
			if (valueClass == null)
			{
				addBrokenRule("Measure value class missing.", measure);
			}
			else if (expressionClass != null && calculation != JRVariable.CALCULATION_COUNT && calculation != JRVariable.CALCULATION_DISTINCT_COUNT && !valueClass.isAssignableFrom(expressionClass))
			{
				addBrokenRule("The class of the expression is not compatible with the class of the measure " + measureName + ".", valueExpression);
			}
			if (measure.getPercentageOfType() != JRCrosstabMeasure.PERCENTAGE_TYPE_NONE)
			{
				Class percentageCalculatorClass = measure.getPercentageCalculatorClass();
				if (percentageCalculatorClass == null)
				{
					if (valueClass != null && !JRPercentageCalculatorFactory.hasBuiltInCalculator(valueClass))
					{
						addBrokenRule("Percentage calculator class needs to be specified for measure " + measureName + ".", measure);
					}
				}
				else
				{
					if (!JRPercentageCalculator.class.isAssignableFrom(percentageCalculatorClass))
					{
						addBrokenRule("Incompatible percentage calculator class for measure " + measureName + ".", measure);
					}
				}
			}
		}
		catch (JRRuntimeException e)
		{
			addBrokenRule(e, measure);
		}

		try
		{
			Class incrementerFactoryClass = measure.getIncrementerFactoryClass();
			if (incrementerFactoryClass != null && !JRExtendedIncrementerFactory.class.isAssignableFrom(incrementerFactoryClass))
			{
				addBrokenRule("Crosstab measures need extended incrementers (net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory).", measure);
			}
		}
		catch (JRRuntimeException e)
		{
			addBrokenRule(e, measure);
		}
	}


	private void verifyExpressions(JRDesignCrosstab crosstab)
	{
		verifyExpressions(expressionCollector.getExpressions(crosstab),
				crosstab.getParametersMap(),
				new HashMap(),
				crosstab.getVariablesMap());
	}


	private void verifyChart(JRChart chart)
	{
		verifyReportElement(chart);
		
		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_AUTO)
		{
			addBrokenRule("Charts do not support Auto evaluation time.", chart);
		}

		JRChartDataset dataset = chart.getDataset();
		if (dataset == null)
		{
			addBrokenRule("Chart dataset missing.", chart);
		}
		else
		{
			dataset.validate(this);
		}
	}


	private void verifyCellContents(JRCellContents contents, String cellText)
	{
		if (contents != null)
		{
			JRElement[] elements = contents.getElements();
			if (elements != null && elements.length > 0)
			{
				int topPadding = 0;
				int leftPadding = 0;
				int bottomPadding = 0;
				int rightPadding = 0;

				JRLineBox box = contents.getLineBox();
				if (box != null)
				{
					topPadding = box.getTopPadding().intValue();
					leftPadding = box.getLeftPadding().intValue();
					bottomPadding = box.getBottomPadding().intValue();
					rightPadding = box.getRightPadding().intValue();
				}

				int cellWidth = contents.getWidth();
				boolean widthCalculated = cellWidth != JRCellContents.NOT_CALCULATED;
				int avlblWidth = cellWidth - leftPadding - rightPadding;
				int cellHeight = contents.getHeight();
				boolean heightCalculated = cellHeight != JRCellContents.NOT_CALCULATED;
				int avlblHeight = cellHeight - topPadding - bottomPadding;

				for (int i = 0; i < elements.length; i++)
				{
					JRElement element = elements[i];

					verifyPrintWhenExpr(element);

					if (widthCalculated && element.getX() + element.getWidth() > avlblWidth)
					{
						addBrokenRule("Element reaches outside " + cellText + " width: x=" + element.getX() + ", width="
								+ element.getWidth() + ", available width=" + avlblWidth + ".",
								element);
					}

					if (heightCalculated && element.getY() + element.getHeight() > avlblHeight)
					{
						addBrokenRule("Element reaches outside " + cellText + " height: y=" + element.getY() + ", height="
								+ element.getHeight() + ", available height=" + avlblHeight + ".",
								element);
					}

					if (element instanceof JRStaticText)
					{
						verifyStaticText((JRStaticText)element);
					}
					else if (element instanceof JRTextField)
					{
						JRTextField textField = (JRTextField) element;

						if (textField.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
						{
							addBrokenRule("Elements with delayed evaluation time are not supported inside crosstab cells.", textField);
						}

						verifyTextField(textField);
					}
					else if (element instanceof JRImage)
					{
						JRImage image = (JRImage) element;

						if (image.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
						{
							addBrokenRule("Elements with delayed evaluation time are not supported inside crosstab cells.", image);
						}

						verifyImage(image);
					}
					else if (element instanceof JRFrame)
					{
						verifyFrame((JRFrame) element);
					}
					else if (element instanceof JRSubreport)
					{
						addBrokenRule("Subreports are not allowed inside crosstab cells.", element);
					}
					else if (element instanceof JRCrosstab)
					{
						addBrokenRule("Crosstabs are not allowed inside crosstab cells.", element);
					}
					else if (element instanceof JRChart)
					{
						addBrokenRule("Charts are not allowed inside crosstab cells.", element);
					}
				}
			}
		}
	}


	public void verifyElementDataset(JRElementDataset dataset)
	{
		JRDatasetRun datasetRun = dataset.getDatasetRun();

		if (datasetRun != null)
		{
			byte incrementType = dataset.getIncrementType();
			if (incrementType == JRVariable.RESET_TYPE_PAGE || incrementType == JRVariable.RESET_TYPE_COLUMN)
			{
				addBrokenRule("Chart datasets with dataset run cannont have Column or Page increment type.", dataset);
			}

			byte resetType = dataset.getResetType();
			if (resetType == JRVariable.RESET_TYPE_PAGE || resetType == JRVariable.RESET_TYPE_COLUMN)
			{
				addBrokenRule("Chart datasets with dataset run cannont have Column or Page reset type.", dataset);
			}
			else if (resetType != JRVariable.RESET_TYPE_REPORT)
			{
				//doesn't make sense, but let it go
			}

			verifyDatasetRun(datasetRun);
		}

		JRExpression incrementWhenExpression = dataset.getIncrementWhenExpression();
		if (incrementWhenExpression != null)
		{
			try
			{
				Class valueClass = incrementWhenExpression.getValueClass();
				if (valueClass == null)
				{
					addBrokenRule("Class not set for data set \"increment when\" expression.", incrementWhenExpression);
				}
				else if (!Boolean.class.isAssignableFrom(valueClass))
				{
					addBrokenRule("Class " + valueClass + " not supported for dataset \"increment when\" expression. Use java.lang.Boolean instead.", incrementWhenExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, incrementWhenExpression);
			}
		}
	}


	private void verifyDatasetRun(JRDatasetRun datasetRun)
	{
		JRDesignDataset dataset = null;

		String datasetName = datasetRun.getDatasetName();
		if (datasetName == null || datasetName.length() == 0)
		{
			addBrokenRule("Dataset name is missing for dataset run.", datasetRun);
		}
		else
		{
			dataset = (JRDesignDataset) jasperDesign.getDatasetMap().get(datasetName);

			if (dataset == null)
			{
				addBrokenRule("Unknown dataset name " + datasetName + ".", datasetRun);
			}
		}

		JRExpression parametersMapExpression = datasetRun.getParametersMapExpression();

		if (parametersMapExpression != null)
		{
			try
			{
				Class clazz = parametersMapExpression.getValueClass();
				if (clazz == null)
				{
					addBrokenRule("Class not set for dataset " + datasetName + " parameters map expression.", parametersMapExpression);
				}
				else if (!java.util.Map.class.isAssignableFrom(clazz))
				{
					addBrokenRule("Class " + clazz + " not supported for dataset " + datasetName + " parameters map expression. Use java.util.Map instead.", parametersMapExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, parametersMapExpression);
			}
		}

		JRDatasetParameter[] parameters = datasetRun.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int index = 0; index < parameters.length; index++)
			{
				JRDatasetParameter parameter = parameters[index];

				String paramName = parameter.getName();
				if (paramName == null || paramName.trim().length() == 0)
				{
					addBrokenRule("Dataset " + datasetName + " parameter name missing.", parameter);
				}

				JRParameter datasetParam = null;
				if (dataset != null)
				{
					datasetParam = (JRParameter) dataset.getParametersMap().get(paramName);

					if (datasetParam == null)
					{
						addBrokenRule("Unknown parameter " + paramName + " in dataset " + datasetName + ".", parameter);
					}
				}

				JRExpression expression = parameter.getExpression();

				if (expression != null)
				{
					try
					{
						Class clazz = expression.getValueClass();
						if (clazz == null)
						{
							addBrokenRule("Class not set for dataset " + datasetName + " parameter expression : " + paramName + ".", expression);
						}
						else if (datasetParam != null && !datasetParam.getValueClass().isAssignableFrom(clazz))
						{
							addBrokenRule("Class " + clazz + " not supported for parameter " + paramName + " of dataset " + datasetName + ". Use " + datasetParam.getValueClass() + " instead.",
									expression);
						}
					}
					catch (JRRuntimeException e)
					{
						addBrokenRule(e, expression);
					}
				}
			}
		}

		JRExpression connectionExpression = datasetRun.getConnectionExpression();
		JRExpression dataSourceExpression = datasetRun.getDataSourceExpression();

		if (connectionExpression != null && dataSourceExpression != null)
		{
			addBrokenRule("Dataset " + datasetName + " cannot have both connection expresion and data source expression.", datasetRun);
		}

		if (connectionExpression != null)
		{
			try
			{
				Class clazz = connectionExpression.getValueClass();
				if (clazz == null)
				{
					addBrokenRule("Class not set for dataset " + datasetName + " connection expression.", connectionExpression);
				}
				else if (!java.sql.Connection.class.isAssignableFrom(clazz))
				{
					addBrokenRule("Class " + clazz + " not supported for dataset " + datasetName + " connection expression. Use java.sql.Connection instead.", connectionExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, connectionExpression);
			}
		}

		if (dataSourceExpression != null)
		{
			try
			{
				Class clazz = dataSourceExpression.getValueClass();
				if (clazz == null)
				{
					addBrokenRule("Class not set for dataset " + datasetName + " data source expression.", dataSourceExpression);
				}
				else if (!net.sf.jasperreports.engine.JRDataSource.class.isAssignableFrom(clazz))
				{
					addBrokenRule("Class " + clazz + " not supported for dataset " + datasetName + " data source expression. Use net.sf.jasperreports.engine.JRDataSource instead.",
							dataSourceExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, dataSourceExpression);
			}
		}
	}


	private void verifyDatasets()
	{
		JRDataset[] datasets = jasperDesign.getDatasets();
		if (datasets != null && datasets.length > 0)
		{
			for (int i = 0; i < datasets.length; ++i)
			{
				JRDesignDataset dataset = (JRDesignDataset) datasets[i];

				if (dataset.getName() == null || dataset.getName().trim().length() == 0)
				{
					addBrokenRule("Dataset name is missing.", dataset);
				}

				verifyDataset(dataset);
			}
		}
	}


	private void verifyDataset(JRDesignDataset dataset)
	{
		verifyExpressions(dataset);

		verifyParameters(dataset);

		verifyQuery(dataset);

		verifyFields(dataset);

		verifySortFields(dataset);

		verifyVariables(dataset);

		verifyGroups(dataset);

		JRExpression filterExpression = dataset.getFilterExpression();
		if (filterExpression != null)
		{
			try
			{
				Class valueClass = filterExpression.getValueClass();
				if (valueClass == null)
				{
					addBrokenRule("Class not set for filter expression.", filterExpression);
				}
				else if (!Boolean.class.isAssignableFrom(valueClass))
				{
					addBrokenRule("Class " + valueClass + " not supported for filter expression. Use java.lang.Boolean instead.", filterExpression);
				}
			}
			catch (JRRuntimeException e)
			{
				addBrokenRule(e, filterExpression);
			}
		}
	}


	private void verifyFrame(JRFrame frame)
	{
		verifyReportElement(frame);
		
		JRElement[] elements = frame.getElements();
		if (elements != null && elements.length > 0)
		{
			int topPadding = frame.getLineBox().getTopPadding().intValue();
			int leftPadding = frame.getLineBox().getLeftPadding().intValue();
			int bottomPadding = frame.getLineBox().getBottomPadding().intValue();
			int rightPadding = frame.getLineBox().getRightPadding().intValue();

			int avlblWidth = frame.getWidth() - leftPadding - rightPadding;
			int avlblHeight = frame.getHeight() - topPadding - bottomPadding;

			for (int i = 0; i < elements.length; i++)
			{
				JRElement element = elements[i];

				if (element.getX() + element.getWidth() > avlblWidth)
				{
					addBrokenRule("Element reaches outside frame width: x=" + element.getX() + ", width="
							+ element.getWidth() + ", available width=" + avlblWidth + ".", element);
				}

				if (element.getY() + element.getHeight() > avlblHeight)
				{
					addBrokenRule("Element reaches outside frame height: y=" + element.getY() + ", height="
							+ element.getHeight() + ", available height=" + avlblHeight + ".", element);
				}

				verifyElement(element);
			}
		}
	}


	public void verify(JRCategoryDataset dataset)
	{
		verifyElementDataset(dataset);

		JRCategorySeries[] series = dataset.getSeries();
		if (series != null)
		{
			for (int i = 0; i < series.length; i++)
			{
				verify(series[i]);
			}
		}
	}


	protected void verify(JRCategorySeries series)
	{
		verifyHyperlink(series.getItemHyperlink());
	}


	public void verify(JRPieDataset dataset)
	{
		verifyElementDataset(dataset);
		verifyHyperlink(dataset.getSectionHyperlink());
		verifyHyperlink(dataset.getOtherSectionHyperlink());
	}


	public void verify(JRHighLowDataset dataset)
	{
		verifyElementDataset(dataset);
		verifyHyperlink(dataset.getItemHyperlink());
	}


	public void verify(JRTimePeriodDataset dataset)
	{
		verifyElementDataset(dataset);

		JRTimePeriodSeries[] series = dataset.getSeries();
		if (series != null)
		{
			for (int i = 0; i < series.length; i++)
			{
				verify(series[i]);
			}
		}
	}


	protected void verify(JRTimePeriodSeries series)
	{
		verifyHyperlink(series.getItemHyperlink());
	}


	public void verify(JRTimeSeriesDataset dataset)
	{
		verifyElementDataset(dataset);

		JRTimeSeries[] series = dataset.getSeries();
		if (series != null)
		{
			for (int i = 0; i < series.length; i++)
			{
				verify(series[i]);
			}
		}
	}


	protected void verify(JRTimeSeries series)
	{
		verifyHyperlink(series.getItemHyperlink());
	}


	/**
	 * Verify the design of a value dataset.  Since value dataset's only
	 * contain a single value and do not support hyperlinks there is nothing
	 * to verify.
	 */
	public void verify(JRValueDataset dataset)
	{
	}

	public void verify(JRXyDataset dataset)
	{
		verifyElementDataset(dataset);

		JRXySeries[] series = dataset.getSeries();
		if (series != null)
		{
			for (int i = 0; i < series.length; i++)
			{
				verify(series[i]);
			}
		}
	}


	protected void verify(JRXySeries series)
	{
		verifyHyperlink(series.getItemHyperlink());
	}


	protected void verify(JRGanttSeries series)
	{
		verifyHyperlink(series.getItemHyperlink());
	}


	public void verify(JRXyzDataset dataset)
	{
		verifyElementDataset(dataset);

		JRXyzSeries[] series = dataset.getSeries();
		if (series != null)
		{
			for (int i = 0; i < series.length; i++)
			{
				verify(series[i]);
			}
		}
	}


	public void verify(JRGanttDataset dataset)
	{
		verifyElementDataset(dataset);
		
		JRGanttSeries[] series = dataset.getSeries();
		
		if (series != null)
		{
			for (int i = 0; i < series.length; i++)
			{
				verify(series[i]);
			}
		}
	}
	

	protected void verify(JRXyzSeries series)
	{
		verifyHyperlink(series.getItemHyperlink());
	}
	
	protected void verifyReportElement(JRElement element)
	{
		verifyProperyExpressions(element.getPropertyExpressions());
	}

	protected void verifyProperyExpressions(
			JRPropertyExpression[] propertyExpressions)
	{
		if (propertyExpressions != null)
		{
			for (int i = 0; i < propertyExpressions.length; i++)
			{
				verifyPropertyExpression(propertyExpressions[i]);
			}
		}
	}

	protected void verifyPropertyExpression(JRPropertyExpression propertyExpression)
	{
		String name = propertyExpression.getName();
		if (name == null)
		{
			addBrokenRule("Property name missing.", propertyExpression);
		}
		
		JRExpression expr = propertyExpression.getValueExpression();
		if (expr == null)
		{
			addBrokenRule("Property value expression missing.", propertyExpression);
		}
		else
		{
			String valueExprClassName = expr.getValueClassName();
			if (valueExprClassName == null)
			{
				addBrokenRule("Class not set for property value expression.", expr);
			}
			else if (!String.class.getName().equals(valueExprClassName))
			{
				addBrokenRule("Class " + valueExprClassName 
						+ " not supported for anchor name expression. Use java.lang.String instead.", expr);
			}
		}
	}


	protected void verifyComponentElement(JRComponentElement element)
	{
		verifyReportElement(element);
		
		ComponentKey componentKey = element.getComponentKey();
		if (componentKey == null)
		{
			addBrokenRule("No component key set for component element", element);
		}
		
		Component component = element.getComponent();
		if (component == null)
		{
			addBrokenRule("No component set for component element", element);
		}
		
		if (componentKey != null && component != null)
		{
			ComponentCompiler compiler = ComponentsEnvironment.
				getComponentManager(componentKey).getComponentCompiler();
			compiler.verify(component, this);
		}
	}


	protected void verifyGenericElement(JRGenericElement element)
	{
		verifyReportElement(element);

		if (element.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			String groupName = element.getEvaluationGroupName();
			if (groupName == null)
			{
				addBrokenRule("Evaluation group not set for generic element", element);
			}
			else
			{
				if (!jasperDesign.getGroupsMap().containsKey(groupName))
				{
					addBrokenRule("Generic element evaluation group " + groupName 
							+ " not found in report", element);
				}
			}
		}
		
		JRGenericElementType type = element.getGenericType();
		if (type == null)
		{
			addBrokenRule("No type set for generic element", element);
		}
		else
		{
			if (type.getNamespace() == null)
			{
				addBrokenRule("No namespace set for generic element type", type);
			}
			
			if (type.getName() == null)
			{
				addBrokenRule("No name set for generic element type", type);
			}
		}
		
		JRGenericElementParameter[] parameters = element.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			JRGenericElementParameter parameter = parameters[i];
			
			if (parameter.getName() == null)
			{
				addBrokenRule("No name set for generic element parameter", parameter);
			}
		}
	}
}
