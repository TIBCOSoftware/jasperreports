/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
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
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRQueryExecuterUtils;


/**
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
			this.expressionCollector = new JRExpressionCollector();
			this.expressionCollector.collect(jasperDesign);
		}
	}


	/**
	 *
	 */
	public static Collection verifyDesign(JasperDesign jasperDesign, JRExpressionCollector expressionCollector)
	{
		JRVerifier verifier = new JRVerifier(jasperDesign, expressionCollector);
		return verifier.verifyDesign();
	}

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

		return brokenRules;
	}


	/**
	 *
	 */
	private void verifyDesignAttributes()
	{
		if (jasperDesign.getName() == null || jasperDesign.getName().trim().length() == 0)
		{
			brokenRules.add("Report name is missing.");
		}
		
		if (jasperDesign.getColumnCount() <= 0)
		{
			brokenRules.add("Column count must be greater than zero.");
		}

		if (jasperDesign.getPageWidth() < 0)
		{
			brokenRules.add("Page width must be positive.");
		}

		if (jasperDesign.getPageHeight() < 0)
		{
			brokenRules.add("Page height must be positive.");
		}

		if (jasperDesign.getColumnWidth() < 0)
		{
			brokenRules.add("Column width must be positive.");
		}

		if (jasperDesign.getColumnSpacing() < 0)
		{
			brokenRules.add("Column spacing must be positive.");
		}

		if (jasperDesign.getLeftMargin() < 0)
		{
			brokenRules.add("Left margin must be positive.");
		}

		if (jasperDesign.getRightMargin() < 0)
		{
			brokenRules.add("Right margin must be positive.");
		}

		if (jasperDesign.getTopMargin() < 0)
		{
			brokenRules.add("Top margin must be positive.");
		}

		if (jasperDesign.getBottomMargin() < 0)
		{
			brokenRules.add("Bottom margin must be positive.");
		}

		if (
			jasperDesign.getLeftMargin() +
			jasperDesign.getColumnCount() * jasperDesign.getColumnWidth() +
			(jasperDesign.getColumnCount() - 1) * jasperDesign.getColumnSpacing() +
			jasperDesign.getRightMargin() >
			jasperDesign.getPageWidth()
			)
		{
			brokenRules.add("The columns and the margins do not fit the page width.");
		}

		if (
			jasperDesign.getTopMargin() +
			(jasperDesign.getBackground() != null ? jasperDesign.getBackground().getHeight() : 0) +
			jasperDesign.getBottomMargin() >
			jasperDesign.getPageHeight()
			)
		{
			brokenRules.add("The background section and the margins do not fit the page height.");
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
				brokenRules.add("The title section and the margins do not fit the page height.");
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
				brokenRules.add("The title section, the page and column headers and footers and the margins do not fit the page height.");
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
			brokenRules.add("The page and column headers and footers and the margins do not fit the page height.");
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
			brokenRules.add("The page and column headers and footers and the margins do not fit the last page height.");
		}

		if (
			jasperDesign.getTopMargin() +
			(jasperDesign.getSummary() != null ? jasperDesign.getSummary().getHeight() : 0) +
			jasperDesign.getBottomMargin() >
			jasperDesign.getPageHeight()
			)
		{
			brokenRules.add("The summary section and the margins do not fit the page height.");
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
			brokenRules.add("The detail section, the page and column headers and footers and the margins do not fit the page height.");
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
				brokenRules.add("Query language not set.");
			}
			else
			{
				try
				{
					queryExecuterFactory = JRQueryExecuterUtils.getQueryExecuterFactory(query.getLanguage());
				}
				catch (JRException e1)
				{
					brokenRules.add("Query executer factory for " + language + " cannot be created.");
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
								brokenRules.add("Query parameter not found : " + queryChunk.getText());
							}
							else if (queryExecuterFactory != null)
							{
								if (!queryExecuterFactory.supportsQueryParameterType(parameter.getValueClassName()))
								{
									brokenRules.add("Parameter type not supported in query : " + queryChunk.getText() + " class " + parameter.getValueClassName());
								}
							}
	
							break;
						}
						case JRQueryChunk.TYPE_PARAMETER_CLAUSE :
						{
							if (!parametersMap.containsKey(queryChunk.getText()))
							{
								brokenRules.add("Query parameter not found : " + queryChunk.getText());
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
									brokenRules.add("Variable not found : " + expressionChunk.getText());
								}
								break;
							}
							case JRExpressionChunk.TYPE_FIELD :
							{
								if ( !fieldsMap.containsKey(expressionChunk.getText()) )
								{
									brokenRules.add("Field not found : " + expressionChunk.getText());
								}
								break;
							}
							case JRExpressionChunk.TYPE_PARAMETER :
							{
								if ( !parametersMap.containsKey(expressionChunk.getText()) )
								{
									brokenRules.add("Parameter not found : " + expressionChunk.getText());
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
					brokenRules.add("Report font name missing.");
				}
			}
		}
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
					brokenRules.add("Report style name missing.");
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
				
				if (parameter.getName() == null || parameter.getName().trim().length() == 0)
				{
					brokenRules.add("Parameter name missing.");
				}

				if (parameter.getValueClassName() == null)
				{
					brokenRules.add("Class not set for parameter : " + parameter.getName());
				}
				else
				{
					JRExpression expression = parameter.getDefaultValueExpression();
					if (expression != null)
					{
						if (expression.getValueClass() == null) {
							brokenRules.add("No value class defined for the expression in parameter: "+parameter.getName());
						}
						else {
							if (
								!parameter.getValueClass().isAssignableFrom(
									expression.getValueClass()
									)
								)
							{
								brokenRules.add("The parameter default value expression class is not compatible with the parameter's class : " + parameter.getName());
							}
						}
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
					brokenRules.add("Field name missing.");
				}

				String className = field.getValueClassName();

				if (className == null)
				{
					brokenRules.add("Class not set for field : " + field.getName());
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyVariables(JRDesignDataset dataset)
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
					brokenRules.add("Variable name missing.");
				}

				Class valueClass = variable.getValueClass();

				if (valueClass == null)
				{
					brokenRules.add("Class not set for variable : " + variable.getName());
				}
				else
				{
					JRExpression expression = variable.getExpression();
					if (expression != null)
					{
						if (expression.getValueClass() == null) {
							brokenRules.add("No value class for the expression has been set in variable: "+variable.getName());
						}
						else {
							if (
								variable.getCalculation() != JRVariable.CALCULATION_COUNT
								&& variable.getCalculation() != JRVariable.CALCULATION_SYSTEM
								&& !valueClass.isAssignableFrom(
									expression.getValueClass()
									)
								)
							{
								brokenRules.add("The variable expression class is not compatible with the variable's class : " + variable.getName());
							}
						}
					}
					
					if (variable.getInitialValueExpression() != null)
					{
						if (
							!valueClass.isAssignableFrom(
								variable.getInitialValueExpression().getValueClass()
								)
							)
						{
							brokenRules.add("The initial value class is not compatible with the variable's class : " + variable.getName());
						}
					}
				}
				
				byte resetType = variable.getResetType();
				if (resetType == JRVariable.RESET_TYPE_GROUP)
				{
					if (variable.getResetGroup() == null)
					{
						brokenRules.add("Reset group missing for variable : " + variable.getName());
					}
					else
					{
						Map groupsMap = dataset.getGroupsMap();
		
						if (!groupsMap.containsKey(variable.getResetGroup().getName()))
						{
							brokenRules.add("Reset group \"" + variable.getResetGroup().getName() + "\" not found for variable : " + variable.getName());
						}
					}
				}

				byte incrementType = variable.getIncrementType();
				if (incrementType == JRVariable.RESET_TYPE_GROUP)
				{
					if (variable.getIncrementGroup() == null)
					{
						brokenRules.add("Increment group missing for variable : " + variable.getName());
					}
					else
					{
						Map groupsMap = dataset.getGroupsMap();
		
						if (!groupsMap.containsKey(variable.getIncrementGroup().getName()))
						{
							brokenRules.add("Increment group \"" + variable.getIncrementGroup().getName() + "\" not found for variable : " + variable.getName());
						}
					}
				}
				
				if (!isMainDataset)
				{
					if (resetType == JRVariable.RESET_TYPE_COLUMN || resetType == JRVariable.RESET_TYPE_PAGE)
					{
						brokenRules.add("Variable " + variable.getName() + " of dataset " + dataset.getName() + " cannot have Column or Page reset type.");
					}
					
					if (incrementType == JRVariable.RESET_TYPE_COLUMN || incrementType == JRVariable.RESET_TYPE_PAGE)
					{
						brokenRules.add("Variable " + variable.getName() + " of dataset " + dataset.getName() + " cannot have Column or Page increment type.");
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
					brokenRules.add("Group name missing.");
				}

				if (isMainDataset)
				{
					verifyGroupHeaderAndFooter(group);
				}
				else
				{
					if (group.getGroupHeader() != null || group.getGroupFooter() != null)
					{
						brokenRules.add("Group " + group.getName() + " cannot have header or footer sections.");
					}
				}
				
				JRExpression expression = group.getExpression();
				
				if (expression != null)
				{
					Class clazz = expression.getValueClass();
	
					if (clazz == null)
					{
						brokenRules.add("Class not set for group expression : " + group.getName());
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
				brokenRules.add("The '" + group.getName() + "' group header section, the page and column headers and footers and the margins do not fit the page height.");
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
				brokenRules.add("The '" + group.getName() + "' group footer section, the page and column headers and footers and the margins do not fit the page height.");
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
				brokenRules.add("The '" + group.getName() + "' group header section, the title, the page and column headers and footers and the margins do not fit the first page height.");
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
				brokenRules.add("The '" + group.getName() + "' group footer section, the title, the page and column headers and footers and the margins do not fit the first page height.");
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
					Class clazz = expression.getValueClass();
	
					if (clazz == null)
					{
						brokenRules.add("Class not set for band \"print when\" expression.");
					}
					else if (!java.lang.Boolean.class.isAssignableFrom(clazz))
					{
						brokenRules.add("Class " + clazz + " not supported for band \"print when\" expression. Use java.lang.Boolean instead.");
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
						//brokenRules.add("Element placed outside band area.");
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
						brokenRules.add(
							"Warning : Element bottom reaches outside band area : y=" + element.getY() + 
							" height=" + element.getHeight() + 
							" band-height=" + band.getHeight()
							);
					}

					verifyElement(element);
				}
			}
		}
	}


	protected void verifyElement(JRElement element)
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
	}


	private void verifyPrintWhenExpr(JRElement element)
	{
		JRExpression expression;
		expression = element.getPrintWhenExpression();
		
		if (expression != null)
		{
			Class clazz = expression.getValueClass();

			if (clazz == null)
			{
				brokenRules.add("Class not set for element \"print when\" expression.");
			}
			else if (!java.lang.Boolean.class.isAssignableFrom(clazz))
			{
				brokenRules.add("Class " + clazz + " not supported for element \"print when\" expression. Use java.lang.Boolean instead.");
			}
		}
	}


	/**
	 *
	 */
	private void verifyStaticText(JRStaticText staticText)
	{
		verifyFont(staticText);
	}
		

	/**
	 *
	 */
	private void verifyTextField(JRTextField textField)
	{
		verifyFont(textField);
		verifyAnchor(textField);
		verifyHyperlink(textField);

		if (textField != null)
		{
			JRExpression expression = textField.getExpression();
			
			if (expression != null)
			{
				String className = expression.getValueClassName();

				if (className == null)
				{
					brokenRules.add("Class not set for text field expression.");
				}
				else if (Arrays.binarySearch(getTextFieldClassNames(), className) < 0) 
				{
					brokenRules.add("Class \"" + className + "\" not supported for text field expression.");
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
				brokenRules.add("Report font not found : " + reportFont.getName());
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
				Class clazz = expression.getValueClass();

				if (clazz == null)
				{
					brokenRules.add("Class not set for anchor name expression.");
				}
				else if (!java.lang.String.class.isAssignableFrom(clazz))
				{
					brokenRules.add("Class " + clazz + " not supported for anchor name expression. Use java.lang.String instead.");
				}
			}
			
			if (anchor.getBookmarkLevel() != JRAnchor.NO_BOOKMARK && anchor.getBookmarkLevel() < 1)
			{
				brokenRules.add("Bookmark level should be " + JRAnchor.NO_BOOKMARK + " or greater than 0");
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
				Class clazz = expression.getValueClass();

				if (clazz == null)
				{
					brokenRules.add("Class not set for hyperlink reference expression.");
				}
				else if (!java.lang.String.class.isAssignableFrom(clazz))
				{
					brokenRules.add("Class " + clazz + " not supported for hyperlink reference expression. Use java.lang.String instead.");
				}
			}

			expression = hyperlink.getHyperlinkAnchorExpression();
			
			if (expression != null)
			{
				Class clazz = expression.getValueClass();

				if (clazz == null)
				{
					brokenRules.add("Class not set for hyperlink anchor expression.");
				}
				else if (!java.lang.String.class.isAssignableFrom(clazz))
				{
					brokenRules.add("Class " + clazz + " not supported for hyperlink anchor expression. Use java.lang.String instead.");
				}
			}

			expression = hyperlink.getHyperlinkPageExpression();
			
			if (expression != null)
			{
				Class clazz = expression.getValueClass();

				if (clazz == null)
				{
					brokenRules.add("Class not set for hyperlink page expression.");
				}
				else if (!java.lang.Integer.class.isAssignableFrom(clazz))
				{
					brokenRules.add("Class " + clazz + " not supported for hyperlink page expression. Use java.lang.Integer instead.");
				}
			}
		}
	}
		

	/**
	 *
	 */
	private void verifyImage(JRImage image)
	{
		verifyAnchor(image);
		verifyHyperlink(image);

		if (image != null)
		{
			JRExpression expression = image.getExpression();
			
			if (expression != null)
			{
				String className = expression.getValueClassName();

				if (className == null)
				{
					brokenRules.add("Class not set for image expression.");
				}
				else if (Arrays.binarySearch(getImageClassNames(), className) < 0)
				{
					brokenRules.add("Class \"" + className + "\" not supported for image expression.");
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
			JRExpression expression = subreport.getExpression();
			
			if (expression != null)
			{
				String className = expression.getValueClassName();

				if (className == null)
				{
					brokenRules.add("Class not set for subreport expression.");
				}
				else if (Arrays.binarySearch(getSubreportClassNames(), className) < 0) 
				{
					brokenRules.add("Class \"" + className + "\" not supported for subreport expression.");
				}
			}

			expression = subreport.getParametersMapExpression();

			if (expression != null)
			{
				Class clazz = expression.getValueClass();

				if (clazz == null)
				{
					brokenRules.add("Class not set for subreport parameters map expression.");
				}
				else if (!java.util.Map.class.isAssignableFrom(clazz))
				{
					brokenRules.add("Class " + clazz + " not supported for subreport parameters map expression. Use java.util.Map instead.");
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
						brokenRules.add("Subreport parameter name missing.");
					}

					expression = parameter.getExpression();
					
					if (expression != null)
					{
						Class clazz = expression.getValueClass();
		
						if (clazz == null)
						{
							brokenRules.add("Class not set for subreport parameter expression : " + parameter.getName() + ". Use java.lang.Object class.");
						}
					}
				}
			}
			
			if (
				subreport.getConnectionExpression() != null &&
				subreport.getDataSourceExpression() != null
				)
			{
				brokenRules.add("Subreport cannot have both connection expresion and data source expression.");
			}
				
			expression = subreport.getConnectionExpression();

			if (expression != null)
			{
				Class clazz = expression.getValueClass();

				if (clazz == null)
				{
					brokenRules.add("Class not set for subreport connection expression.");
				}
				else if (!java.sql.Connection.class.isAssignableFrom(clazz))
				{
					brokenRules.add("Class " + clazz + " not supported for subreport connection expression. Use java.sql.Connection instead.");
				}
			}

			expression = subreport.getDataSourceExpression();

			if (expression != null)
			{
				Class clazz = expression.getValueClass();

				if (clazz == null)
				{
					brokenRules.add("Class not set for subreport data source expression.");
				}
				else if (!net.sf.jasperreports.engine.JRDataSource.class.isAssignableFrom(clazz))
				{
					brokenRules.add("Class " + clazz + " not supported for subreport data source expression. Use net.sf.jasperreports.engine.JRDataSource instead.");
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
						brokenRules.add("Subreport return value variable name missing.");
					}
					
					if (returnValue.getToVariable() == null || returnValue.getToVariable().trim().length() == 0)
					{
						brokenRules.add("Subreport return value to variable name missing.");
					}
					
					if (!jasperDesign.getVariablesMap().containsKey(returnValue.getToVariable()))
					{
						brokenRules.add("Subreport return value to variable not found.");
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
		verifyParameters(crosstab);
		
		JRCrosstabDataset dataset = crosstab.getDataset();
		if (dataset == null)
		{
			brokenRules.add("Crosstab dataset missing.");
		}
		else
		{
			verifyElementDataset(dataset);
		}
		
		verifyCellContents(crosstab.getHeaderCell(), "crosstab cell");		
		
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		if (rowGroups == null || rowGroups.length == 0)
		{
			brokenRules.add("Crosstab should have at least one row group.");
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
			brokenRules.add("Crosstab should have at least one column group.");
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
			brokenRules.add("Crosstab should have at least one measure.");
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
			Class clazz = paramMapExpression.getValueClass();

			if (clazz == null)
			{
				brokenRules.add("Class not set for crosstab parameters map expression.");
			}
			else if (!java.util.Map.class.isAssignableFrom(clazz))
			{
				brokenRules.add("Class " + clazz + " not supported for crosstab parameters map expression. Use java.util.Map instead.");
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
					brokenRules.add("Missing parameter name for crosstab.");
				}
				
				JRExpression expression = parameter.getExpression();
				Class expressionClass = null;
				if (expression != null)
				{
					expressionClass = expression.getValueClass();
					
					if (expressionClass == null)
					{
						brokenRules.add("Expression class not set for crosstab parameter " + paramName + ".");
					}
				}
				
				Class valueClass = parameter.getValueClass();
				if (valueClass == null)
				{
					brokenRules.add("Class not set for crosstab parameter " + paramName + ".");
				}
				else if (expressionClass != null && !valueClass.isAssignableFrom(expressionClass))
				{
					brokenRules.add("Incompatible expression class for crosstab parameter " + paramName + ".");
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
			brokenRules.add("Crosstab group name missing.");
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
			brokenRules.add("Crosstab bucket expression missing for group " + group.getName() + ".");
		}
		else
		{
			expressionClass = expression.getValueClass();
			
			if (expressionClass == null)
			{
				brokenRules.add("Crosstab bucket expression class missing for group " + group.getName() + ".");
			}
		}
		
		Class valueClass = expression == null ? null : expression.getValueClass();		
		if (valueClass == null)
		{
			brokenRules.add("Crosstab bucket value class missing for group " + group.getName() + ".");
		}
		else if (expressionClass != null && !valueClass.isAssignableFrom(expressionClass))
		{
			brokenRules.add("The class of the expression is not compatible with the class of the crosstab bucket for group " + group.getName() + ".");
		}
		
		JRExpression comparatorExpression = bucket.getComparatorExpression();
		if (comparatorExpression == null)
		{
			if (valueClass != null && !Comparable.class.isAssignableFrom(valueClass))
			{
				brokenRules.add("No comparator expression specified and the value class is not comparable for crosstab group " + group.getName() + ".");
			}
		}
		else
		{
			Class comparatorClass = comparatorExpression.getValueClass();
			if (comparatorClass == null)
			{
				brokenRules.add("Crosstab bucket comparator expression class missing for group " + group.getName() + ".");
			}
			else if (!Comparator.class.isAssignableFrom(comparatorClass))
			{
				brokenRules.add("The comparator expression should be compatible with java.util.Comparator for crosstab group " + group.getName() + ".");
			}
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
			brokenRules.add("Crosstab base cell width not specified.");
		}
		
		if(baseCell == null || baseCell.getHeight() == null)
		{
			brokenRules.add("Crosstab base cell height not specified.");
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
							brokenRules.add("Crosstab " + cellText + " width should be " + colCell.getContents().getWidth() + ".");
						}
					}
					
					if (j < columnGroups.length)
					{
						JRCrosstabCell rowCell = cells[i][columnGroups.length];
						if (rowCell != null && rowCell.getContents().getHeight() != contents.getHeight())
						{
							brokenRules.add("Crosstab " + cellText + " height should be " + rowCell.getContents().getHeight() + ".");
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
			brokenRules.add("Measure name missing.");
		}
		
		byte calculation = measure.getCalculation();
		if (calculation == JRVariable.CALCULATION_SYSTEM)
		{
			brokenRules.add("Crosstab mesures cannot have system calculation");
		}
		
		JRExpression valueExpression = measure.getValueExpression();
		Class expressionClass = null;
		if (valueExpression == null)
		{
			brokenRules.add("Missing expression for measure " + measureName);
		}
		else
		{
			expressionClass = valueExpression.getValueClass();
			if (expressionClass == null)
			{
				brokenRules.add("Crosstab measure expression class missing for " + measureName + ".");
			}
		}
		
		Class valueClass = measure.getValueClass();
		if (valueClass == null)
		{
			brokenRules.add("Measure value class missing.");
		}
		else if (expressionClass != null &&
				calculation != JRVariable.CALCULATION_COUNT &&
				!valueClass.isAssignableFrom(expressionClass))
		{
			brokenRules.add("The class of the expression is not compatible with the class of the measure " + measureName + ".");
		}
		
		if (measure.getPercentageOfType() != JRCrosstabMeasure.PERCENTAGE_TYPE_NONE)
		{
			Class percentageCalculatorClass = measure.getPercentageCalculatorClass();
			if (percentageCalculatorClass == null)
			{
				if (valueClass != null && !JRPercentageCalculatorFactory.hasBuiltInCalculator(valueClass))
				{
					brokenRules.add("Percentage calculator class needs to be specified for measure " + measureName + ".");
				}
			}
			else
			{
				if (!JRPercentageCalculator.class.isAssignableFrom(percentageCalculatorClass))
				{
					brokenRules.add("Incompatible percentage calculator class for measure " + measureName + ".");
				}
			}
		}
		
		Class incrementerFactoryClass = measure.getIncrementerFactoryClass();
		if (incrementerFactoryClass != null && !JRExtendedIncrementerFactory.class.isAssignableFrom(incrementerFactoryClass))
		{
			brokenRules.add("Crosstab measures need extended incrementers (net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory).");
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
		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_AUTO)
		{
			brokenRules.add("Charts do not support Auto evaluation time.");
		}
		
		verifyElementDataset(chart.getDataset());
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
				
				JRBox box = contents.getBox();
				if (box != null)
				{
					topPadding = box.getTopPadding();
					leftPadding = box.getLeftPadding();
					bottomPadding = box.getBottomPadding();
					rightPadding = box.getRightPadding();
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
						brokenRules.add("Element reaches outside " + cellText + " width: x=" + element.getX() + ", width="
								+ element.getWidth() + ", available width=" + avlblWidth + ".");
					}
					
					if (heightCalculated && element.getY() + element.getHeight() > avlblHeight)
					{
						brokenRules.add("Element reaches outside " + cellText + " height: y=" + element.getY() + ", height="
								+ element.getHeight() + ", available height=" + avlblHeight + ".");
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
							brokenRules.add("Elements with delayed evaluation time are not supported inside crosstab cells.");
						}
						
						verifyTextField(textField);
					}
					else if (element instanceof JRImage)
					{
						JRImage image = (JRImage) element;
						
						if (image.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
						{
							brokenRules.add("Elements with delayed evaluation time are not supported inside crosstab cells.");
						}
						
						verifyImage(image);
					}
					else if (element instanceof JRFrame)
					{
						verifyFrame((JRFrame) element);
					}
					else if (element instanceof JRSubreport)
					{
						brokenRules.add("Subreports are not allowed inside crosstab cells.");
					}
					else if (element instanceof JRCrosstab)
					{
						brokenRules.add("Crosstabs are not allowed inside crosstab cells.");
					}
					else if (element instanceof JRChart)
					{
						brokenRules.add("Charts are not allowed inside crosstab cells.");
					}
				}
			}
		}
	}


	private void verifyElementDataset(JRElementDataset dataset)
	{
		JRDatasetRun datasetRun = dataset.getDatasetRun();
		
		if (datasetRun != null)
		{
			byte incrementType = dataset.getIncrementType();
			if (incrementType == JRVariable.RESET_TYPE_PAGE || incrementType == JRVariable.RESET_TYPE_COLUMN)
			{
				brokenRules.add("Chart datasets with dataset run cannont have Column or Page increment type.");
			}
			
			byte resetType = dataset.getResetType();
			if (resetType == JRVariable.RESET_TYPE_PAGE || resetType == JRVariable.RESET_TYPE_COLUMN)
			{
				brokenRules.add("Chart datasets with dataset run cannont have Column or Page reset type.");
			}
			else if (resetType != JRVariable.RESET_TYPE_REPORT)
			{
				//doesn't make sense, but let it go
			}
			
			verifyDatasetRun(datasetRun);
		}
	}


	private void verifyDatasetRun(JRDatasetRun datasetRun)
	{
		JRDesignDataset dataset = null;
		
		String datasetName = datasetRun.getDatasetName();
		if (datasetName == null || datasetName.length() == 0)
		{
			brokenRules.add("Dataset name is missing for dataset run.");
		}
		else
		{
			dataset = (JRDesignDataset) jasperDesign.getDatasetMap().get(datasetName);
			
			if (dataset == null)
			{
				brokenRules.add("Unknown dataset name " + datasetName + ".");
			}
		}

		JRExpression parametersMapExpression = datasetRun.getParametersMapExpression();

		if (parametersMapExpression != null)
		{
			Class clazz = parametersMapExpression.getValueClass();

			if (clazz == null)
			{
				brokenRules.add("Class not set for dataset " + datasetName + " parameters map expression.");
			}
			else if (!java.util.Map.class.isAssignableFrom(clazz))
			{
				brokenRules.add("Class " + clazz + " not supported for dataset " + datasetName + " parameters map expression. Use java.util.Map instead.");
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
					brokenRules.add("Dataset " + datasetName + " parameter name missing.");
				}
				
				JRParameter datasetParam = null;
				if (dataset != null)
				{
					datasetParam = (JRParameter) dataset.getParametersMap().get(paramName);
					
					if (datasetParam == null)
					{
						brokenRules.add("Unknown parameter " + paramName + " in dataset " + datasetName + ".");
					}
				}

				JRExpression expression = parameter.getExpression();
				
				if (expression != null)
				{
					Class clazz = expression.getValueClass();
	
					if (clazz == null)
					{
						brokenRules.add("Class not set for dataset " + datasetName + " parameter expression : " + paramName + ".");
					}
					else if (datasetParam != null && !datasetParam.getValueClass().isAssignableFrom(clazz))
					{
						brokenRules.add("Class " + clazz + " not supported for parameter " + paramName + " of dataset " + datasetName + ". Use " + datasetParam.getValueClass() + " instead.");
					}
				}
			}
		}
				
		JRExpression connectionExpression = datasetRun.getConnectionExpression();
		JRExpression dataSourceExpression = datasetRun.getDataSourceExpression();
		
		if (connectionExpression != null && dataSourceExpression != null)
		{
			brokenRules.add("Dataset " + datasetName + " cannot have both connection expresion and data source expression.");
		}

		if (connectionExpression != null)
		{
			Class clazz = connectionExpression.getValueClass();

			if (clazz == null)
			{
				brokenRules.add("Class not set for dataset " + datasetName + " connection expression.");
			}
			else if (!java.sql.Connection.class.isAssignableFrom(clazz))
			{
				brokenRules.add("Class " + clazz + " not supported for dataset " + datasetName + " connection expression. Use java.sql.Connection instead.");
			}
		}

		if (dataSourceExpression != null)
		{
			Class clazz = dataSourceExpression.getValueClass();

			if (clazz == null)
			{
				brokenRules.add("Class not set for dataset " + datasetName + " data source expression.");
			}
			else if (!net.sf.jasperreports.engine.JRDataSource.class.isAssignableFrom(clazz))
			{
				brokenRules.add("Class " + clazz + " not supported for dataset " + datasetName + " data source expression. Use net.sf.jasperreports.engine.JRDataSource instead.");
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
					brokenRules.add("Dataset name is missing.");
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

		verifyVariables(dataset);

		verifyGroups(dataset);
	}


	private void verifyFrame(JRFrame frame)
	{
		JRElement[] elements = frame.getElements();
		if (elements != null && elements.length > 0)
		{
			int topPadding = frame.getTopPadding();
			int leftPadding = frame.getLeftPadding();
			int bottomPadding = frame.getBottomPadding();
			int rightPadding = frame.getRightPadding();
			
			int avlblWidth = frame.getWidth() - leftPadding - rightPadding;
			int avlblHeight = frame.getHeight() - topPadding - bottomPadding;
			
			for (int i = 0; i < elements.length; i++)
			{
				JRElement element = elements[i];
				
				if (element.getX() + element.getWidth() > avlblWidth)
				{
					brokenRules.add("Element reaches outside frame width: x=" + element.getX() + ", width="
							+ element.getWidth() + ", available width=" + avlblWidth + ".");
				}
				
				if (element.getY() + element.getHeight() > avlblHeight)
				{
					brokenRules.add("Element reaches outside frame height: y=" + element.getY() + ", height="
							+ element.getHeight() + ", available height=" + avlblHeight + ".");
				}
				
				verifyElement(element);
			}
		}
	}
}
