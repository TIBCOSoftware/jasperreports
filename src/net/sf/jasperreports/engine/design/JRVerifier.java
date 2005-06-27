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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRVerifier
{
	
	
	/**
	 *
	 */
	private static final Log log = LogFactory.getLog(JRVerifier.class);

	/**
	 *
	 */
	private static String[] queryParameterClassNames = null;
	private static String[] textFieldClassNames = null;
	private static String[] imageClassNames = null;
	private static String[] subreportClassNames = null;

	/**
	 *
	 */
	private JasperDesign jasperDesign = null;
	private Collection brokenRules = null;


	/**
	 *
	 */
	protected JRVerifier(JasperDesign jrDesign)
	{
		jasperDesign = jrDesign;
		brokenRules = new ArrayList();
	}


	/**
	 *
	 */
	public static Collection verifyDesign(JasperDesign jasperDesign) throws JRException
	{
		JRVerifier verifier = new JRVerifier(jasperDesign);
		return verifier.verifyDesign();
	}

	/**
	 *
	 */
	protected Collection verifyDesign() throws JRException
	{
		/*   */
		verifyDesignAttributes();

		/*   */
		verifyExpressions();

		/*   */
		verifyReportFonts();

		/*   */
		verifyParameters();

		/*   */
		verifyQuery();

		/*   */
		verifyFields();

		/*   */
		verifyVariables();

		/*   */
		verifyGroups();

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
	private void verifyDesignAttributes() throws JRException
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
	private void verifyQuery() throws JRException
	{
		JRQuery query = jasperDesign.getQuery();
		if (query != null)
		{
			JRQueryChunk[] chunks = query.getChunks();
			if (chunks != null && chunks.length > 0)
			{
				Map parametersMap = jasperDesign.getParametersMap();
	
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
							else 
							{
								if (Arrays.binarySearch(getQueryParameterClassNames(), parameter.getValueClassName()) < 0)
								{
									brokenRules.add("Parameter type not supported in query : " + queryChunk.getText() + " class " + parameter.getValueClassName());
								}
							}
	
							break;
						}
						case JRQueryChunk.TYPE_PARAMETER_CLAUSE :
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
	private void verifyExpressions() throws JRException
	{
		Collection expressions = jasperDesign.getExpressions();
		if (expressions != null && expressions.size() > 0)
		{
			Map parametersMap = jasperDesign.getParametersMap();
			Map fieldsMap = jasperDesign.getFieldsMap();
			Map variablesMap = jasperDesign.getVariablesMap();

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


	/**
	 *
	 */
	private void verifyReportFonts() throws JRException
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
	private void verifyParameters() throws JRException
	{
		JRParameter[] parameters = jasperDesign.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int index = 0; index < parameters.length; index++)
			{
				JRParameter parameter = parameters[index];
				
				if (parameter.getName() == null || parameter.getName().trim().length() == 0)
				{
					brokenRules.add("Parameter name missing.");
				}

				Class valueClass = parameter.getValueClass();

				if (valueClass == null)
				{
					brokenRules.add("Class not set for parameter : " + parameter.getName());
				}
				else
				{
					JRExpression expression = parameter.getDefaultValueExpression();
					if (expression != null)
					{
						if (
							!valueClass.isAssignableFrom(
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


	/**
	 *
	 */
	private void verifyFields() throws JRException
	{
		JRField[] fields = jasperDesign.getFields();
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
	private void verifyVariables() throws JRException
	{
		JRVariable[] variables = jasperDesign.getVariables();
		if (variables != null && variables.length > 0)
		{
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
				
				if (variable.getResetType() == JRVariable.RESET_TYPE_GROUP)
				{
					if (variable.getResetGroup() == null)
					{
						brokenRules.add("Reset group missing for variable : " + variable.getName());
					}
					else
					{
						Map groupsMap = jasperDesign.getGroupsMap();
		
						if (!groupsMap.containsKey(variable.getResetGroup().getName()))
						{
							brokenRules.add("Reset group \"" + variable.getResetGroup().getName() + "\" not found for variable : " + variable.getName());
						}
					}
				}

				if (variable.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
				{
					if (variable.getIncrementGroup() == null)
					{
						brokenRules.add("Increment group missing for variable : " + variable.getName());
					}
					else
					{
						Map groupsMap = jasperDesign.getGroupsMap();
		
						if (!groupsMap.containsKey(variable.getIncrementGroup().getName()))
						{
							brokenRules.add("Increment group \"" + variable.getIncrementGroup().getName() + "\" not found for variable : " + variable.getName());
						}
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void verifyGroups() throws JRException
	{
		JRGroup[] groups = jasperDesign.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int index = 0; index < groups.length; index++)
			{
				JRGroup group = groups[index];

				if (group.getName() == null || group.getName().trim().length() == 0)
				{
					brokenRules.add("Group name missing.");
				}

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
				
				JRExpression expression = group.getExpression();
				
				if (expression != null)
				{
					Class clazz = expression.getValueClass();
	
					if (clazz == null)
					{
						brokenRules.add("Class not set for group expression : " + group.getName());
					}
				}

				verifyBand(group.getGroupHeader());
				verifyBand(group.getGroupFooter());
			}
		}
	}


	/**
	 *
	 */
	private void verifyBand(JRBand band) throws JRException
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

					if (element instanceof JRTextField)
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
				}
			}
		}
	}
		

	/**
	 *
	 */
	private void verifyTextField(JRTextField textField) throws JRException
	{
		verifyTextElement(textField);
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
	private void verifyTextElement(JRTextElement textElement) throws JRException
	{
		if (textElement != null)
		{
			JRFont font = textElement.getFont();
			
			if (font != null)
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
		}
	}
		

	/**
	 *
	 */
	private void verifyAnchor(JRAnchor anchor) throws JRException
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
		}
	}
		

	/**
	 *
	 */
	private void verifyHyperlink(JRHyperlink hyperlink) throws JRException
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
	private void verifyImage(JRImage image) throws JRException
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
	private void verifySubreport(JRSubreport subreport) throws JRException
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
		}
	}
		

	/**
	 *
	 */
	private static synchronized String[] getQueryParameterClassNames()
	{
		if (queryParameterClassNames == null)
		{
			queryParameterClassNames = new String[]
			{
				java.lang.Object.class.getName(),
				java.lang.Boolean.class.getName(),
				java.lang.Byte.class.getName(),
				java.lang.Double.class.getName(),
				java.lang.Float.class.getName(),
				java.lang.Integer.class.getName(),
				java.lang.Long.class.getName(),
				java.lang.Short.class.getName(),
				java.math.BigDecimal.class.getName(),
				java.lang.String.class.getName(),
				java.util.Date.class.getName(),
				java.sql.Timestamp.class.getName(),
				java.sql.Time.class.getName()
			};

			Arrays.sort(queryParameterClassNames);
		}
		
		return queryParameterClassNames;
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


}
