/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.design;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseReport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperDesign extends JRBaseReport
{

	
	/**
	 *
	 */
	private static final long serialVersionUID = 602;

	/**
	 *
	 */
	private Map fontsMap = new HashMap();
	private List fontsList = new ArrayList();
	private Map parametersMap = new HashMap();
	private List parametersList = new ArrayList();
	private Map fieldsMap = new HashMap();
	private List fieldsList = new ArrayList();
	private Map variablesMap = new HashMap();
	private List variablesList = new ArrayList();
	private Map groupsMap = new HashMap();
	private List groupsList = new ArrayList();


	/**
	 *
	 */
	public JasperDesign()
	{
		/*   */
		JRDesignParameter parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_PARAMETERS_MAP);
		parameter.setValueClass(java.util.Map.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_CONNECTION);
		parameter.setValueClass(Connection.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_DATA_SOURCE);
		parameter.setValueClass(JRDataSource.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_SCRIPTLET);
		parameter.setValueClass(JRAbstractScriptlet.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_LOCALE);
		parameter.setValueClass(Locale.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_RESOURCE_BUNDLE);
		parameter.setValueClass(ResourceBundle.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		JRDesignVariable variable = new JRDesignVariable();
		variable.setName(JRVariable.PAGE_NUMBER);
		variable.setValueClass(Integer.class);
		//variable.setResetType(JRVariable.RESET_TYPE_PAGE);
		variable.setResetType(JRVariable.RESET_TYPE_REPORT);
		variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		variable.setSystemDefined(true);
		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.PAGE_NUMBER);
		//expression.setText("($V{PAGE_NUMBER} != null)?(new Integer($V{PAGE_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.COLUMN_NUMBER);
		variable.setValueClass(Integer.class);
		//variable.setResetType(JRVariable.RESET_TYPE_COLUMN);
		variable.setResetType(JRVariable.RESET_TYPE_REPORT);
		variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.COLUMN_NUMBER);
		//expression.setText("($V{COLUMN_NUMBER} != null)?(new Integer($V{COLUMN_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.REPORT_COUNT);
		variable.setValueClass(Integer.class);
		variable.setResetType(JRVariable.RESET_TYPE_REPORT);
		variable.setCalculation(JRVariable.CALCULATION_COUNT);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variable + JRVariable.REPORT_COUNT);
		expression.setText("new Integer(1)");
		variable.setExpression(expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.REPORT_COUNT);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.PAGE_COUNT);
		variable.setValueClass(Integer.class);
		variable.setResetType(JRVariable.RESET_TYPE_PAGE);
		variable.setCalculation(JRVariable.CALCULATION_COUNT);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variable + JRVariable.PAGE_COUNT);
		expression.setText("new Integer(1)");
		variable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.PAGE_COUNT);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.COLUMN_COUNT);
		variable.setValueClass(Integer.class);
		variable.setResetType(JRVariable.RESET_TYPE_COLUMN);
		variable.setCalculation(JRVariable.CALCULATION_COUNT);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variable + JRVariable.COLUMN_COUNT);
		expression.setText("new Integer(1)");
		variable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.COLUMN_COUNT);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 *
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 *
	 */
	public void setColumnCount(int columnCount)
	{
		this.columnCount = columnCount;
	}
		

	/**
	 *
	 */
	public void setPrintOrder(byte printOrder)
	{
		this.printOrder = printOrder;
	}
		

	/**
	 *
	 */
	public void setPageWidth(int pageWidth)
	{
		this.pageWidth = pageWidth;
	}
		

	/**
	 *
	 */
	public void setPageHeight(int pageHeight)
	{
		this.pageHeight = pageHeight;
	}
		

	/**
	 *
	 */
	public void setOrientation(byte orientation)
	{
		this.orientation = orientation;
	}
		

	/**
	 *
	 */
	public void setColumnWidth(int columnWidth)
	{
		this.columnWidth = columnWidth;
	}
		

	/**
	 *
	 */
	public void setColumnSpacing(int columnSpacing)
	{
		this.columnSpacing = columnSpacing;
	}
		

	/**
	 *
	 */
	public void setLeftMargin(int leftMargin)
	{
		this.leftMargin = leftMargin;
	}
		

	/**
	 *
	 */
	public void setRightMargin(int rightMargin)
	{
		this.rightMargin = rightMargin;
	}
		

	/**
	 *
	 */
	public void setTopMargin(int topMargin)
	{
		this.topMargin = topMargin;
	}
		

	/**
	 *
	 */
	public void setBottomMargin(int bottomMargin)
	{
		this.bottomMargin = bottomMargin;
	}
		

	/**
	 *
	 */
	public void setBackground(JRBand background)
	{
		this.background = background;
	}
		

	/**
	 *
	 */
	public void setTitle(JRBand title)
	{
		this.title = title;
	}
		

	/**
	 *
	 */
	public void setTitleNewPage(boolean isTitleNewPage)
	{
		this.isTitleNewPage = isTitleNewPage;
	}
		

	/**
	 *
	 */
	public void setSummary(JRBand summary)
	{
		this.summary = summary;
	}
		

	/**
	 *
	 */
	public void setSummaryNewPage(boolean isSummaryNewPage)
	{
		this.isSummaryNewPage = isSummaryNewPage;
	}
		

	/**
	 *
	 */
	public void setPageHeader(JRBand pageHeader)
	{
		this.pageHeader = pageHeader;
	}
		

	/**
	 *
	 */
	public void setPageFooter(JRBand pageFooter)
	{
		this.pageFooter = pageFooter;
	}
		

	/**
	 *
	 */
	public void setLastPageFooter(JRBand lastPageFooter)
	{
		this.lastPageFooter = lastPageFooter;
	}
		

	/**
	 *
	 */
	public void setColumnHeader(JRBand columnHeader)
	{
		this.columnHeader = columnHeader;
	}
		

	/**
	 *
	 */
	public void setColumnFooter(JRBand columnFooter)
	{
		this.columnFooter = columnFooter;
	}
		

	/**
	 *
	 */
	public void setDetail(JRBand detail)
	{
		this.detail = detail;
	}
		

	/**
	 *
	 */
	public void setScriptletClass(String scriptletClass)
	{
		this.scriptletClass = scriptletClass;
	}
		

	/**
	 *
	 */
	public void setResourceBundle(String resourceBundle)
	{
		this.resourceBundle = resourceBundle;
	}
		

	/**
	 *
	 */
	public void addImport(String value)
	{
		if (importsSet == null)
		{
			importsSet = new HashSet();
		}
		importsSet.add(value);
	}


	/**
	 *
	 */
	public void removeImport(String value)
	{
		if (importsSet != null)
		{
			importsSet.remove(value);
		}
	}


	/**
	 *
	 */
	public void setDefaultFont(JRReportFont font)
	{
		this.defaultFont = font;
	}
		

	/**
	 *
	 */
	public JRReportFont[] getFonts()
	{
		JRReportFont[] fontsArray = new JRReportFont[fontsList.size()];
		
		fontsList.toArray(fontsArray);

		return fontsArray;
	}
	

	/**
	 *
	 */
	public List getFontsList()
	{
		return fontsList;
	}
	

	/**
	 *
	 */
	public Map getFontsMap()
	{
		return fontsMap;
	}
	

	/**
	 *
	 */
	public void addFont(JRReportFont reportFont) throws JRException
	{
		if (fontsMap.containsKey(reportFont.getName()))
		{
			throw new JRException("Duplicate declaration of report font : " + reportFont.getName());
		}

		fontsList.add(reportFont);
		fontsMap.put(reportFont.getName(), reportFont);
		
		if (reportFont.isDefault())
		{
			setDefaultFont(reportFont);
		}
	}
	

	/**
	 *
	 */
	public JRReportFont removeFont(String name)
	{
		return removeFont(
			(JRReportFont)fontsMap.get(name)
			);
	}


	/**
	 *
	 */
	public JRReportFont removeFont(JRReportFont reportFont)
	{
		if (reportFont != null)
		{
			if (reportFont.isDefault())
			{
				setDefaultFont(null);
			}

			fontsList.remove(reportFont);
			fontsMap.remove(reportFont.getName());
		}

		return reportFont;
	}


	/**
	 *
	 */
	public JRParameter[] getParameters()
	{
		JRParameter[] parametersArray = new JRParameter[parametersList.size()];

		parametersList.toArray(parametersArray);

		return parametersArray;
	}
	

	/**
	 *
	 */
	public List getParametersList()
	{
		return parametersList;
	}
	

	/**
	 *
	 */
	public Map getParametersMap()
	{
		return parametersMap;
	}
	

	/**
	 *
	 */
	public void addParameter(JRParameter parameter) throws JRException
	{
		if (parametersMap.containsKey(parameter.getName()))
		{
			throw new JRException("Duplicate declaration of parameter : " + parameter.getName());
		}

		parametersList.add(parameter);
		parametersMap.put(parameter.getName(), parameter);
	}
	

	/**
	 *
	 */
	public JRParameter removeParameter(String parameterName)
	{
		return removeParameter(
			(JRParameter)parametersMap.get(parameterName)
			);
	}


	/**
	 *
	 */
	public JRParameter removeParameter(JRParameter parameter)
	{
		if (parameter != null)
		{
			parametersList.remove(parameter);
			parametersMap.remove(parameter.getName());
		}
		
		return parameter;
	}


	/**
	 *
	 */
	public void setQuery(JRQuery query)
	{
		this.query = query;
	}
		

	/**
	 *
	 */
	public JRField[] getFields()
	{
		JRField[] fieldsArray = new JRField[fieldsList.size()];
		
		fieldsList.toArray(fieldsArray);

		return fieldsArray;
	}
	

	/**
	 *
	 */
	public List getFieldsList()
	{
		return fieldsList;
	}
	

	/**
	 *
	 */
	public Map getFieldsMap()
	{
		return fieldsMap;
	}
	

	/**
	 *
	 */
	public void addField(JRField field) throws JRException
	{
		if (fieldsMap.containsKey(field.getName()))
		{
			throw new JRException("Duplicate declaration of field : " + field.getName());
		}

		fieldsList.add(field);
		fieldsMap.put(field.getName(), field);
	}
	

	/**
	 *
	 */
	public JRField removeField(String fieldName)
	{
		return removeField(
			(JRField)fieldsMap.get(fieldName)
			);
	}


	/**
	 *
	 */
	public JRField removeField(JRField field)
	{
		if (field != null)
		{
			fieldsList.remove(field);
			fieldsMap.remove(field.getName());
		}
		
		return field;
	}


	/**
	 *
	 */
	public JRVariable[] getVariables()
	{
		JRVariable[] variablesArray = new JRVariable[variablesList.size()];
		
		variablesList.toArray(variablesArray);

		return variablesArray;
	}
	

	/**
	 *
	 */
	public List getVariablesList()
	{
		return variablesList;
	}
	

	/**
	 *
	 */
	public Map getVariablesMap()
	{
		return variablesMap;
	}
	

	/**
	 *
	 */
	public void addVariable(JRDesignVariable variable) throws JRException
	{
		if (variablesMap.containsKey(variable.getName()))
		{
			throw new JRException("Duplicate declaration of variable : " + variable.getName());
		}
		
		byte calculation = variable.getCalculation();

		if (
			calculation == JRVariable.CALCULATION_AVERAGE ||
			calculation == JRVariable.CALCULATION_VARIANCE
			)
		{
			JRDesignVariable countVariable = new JRDesignVariable();
			countVariable.setName(variable.getName() + "_COUNT");
			countVariable.setValueClassName(variable.getValueClassName());
			countVariable.setResetType(variable.getResetType());
			countVariable.setResetGroup(variable.getResetGroup());
			countVariable.setCalculation(JRVariable.CALCULATION_COUNT);
			countVariable.setSystemDefined(true);
			countVariable.setExpression(variable.getExpression());
			addVariable(countVariable);
			variable.setCountVariable(countVariable);

			JRDesignVariable sumVariable = new JRDesignVariable();
			sumVariable.setName(variable.getName() + "_SUM");
			sumVariable.setValueClassName(variable.getValueClassName());
			sumVariable.setResetType(variable.getResetType());
			sumVariable.setResetGroup(variable.getResetGroup());
			sumVariable.setCalculation(JRVariable.CALCULATION_SUM);
			sumVariable.setSystemDefined(true);
			sumVariable.setExpression(variable.getExpression());
			addVariable(sumVariable);
			variable.setSumVariable(sumVariable);
		}

		if (calculation == JRVariable.CALCULATION_STANDARD_DEVIATION)
		{
			JRDesignVariable varianceVariable = new JRDesignVariable();
			varianceVariable.setName(variable.getName() + "_VARIANCE");
			varianceVariable.setValueClassName(variable.getValueClassName());
			varianceVariable.setResetType(variable.getResetType());
			varianceVariable.setResetGroup(variable.getResetGroup());
			varianceVariable.setCalculation(JRVariable.CALCULATION_VARIANCE);
			varianceVariable.setSystemDefined(true);
			varianceVariable.setExpression(variable.getExpression());
			addVariable(varianceVariable);
			variable.setVarianceVariable(varianceVariable);
		}

		variablesList.add(variable);
		variablesMap.put(variable.getName(), variable);
	}
	

	/**
	 *
	 */
	public JRVariable removeVariable(String variableName)
	{
		return removeVariable(
			(JRVariable)variablesMap.get(variableName)
			);
	}


	/**
	 *
	 */
	public JRVariable removeVariable(JRVariable variable)
	{
		if (variable != null)
		{
			removeVariable(variable.getSumVariable());
			removeVariable(variable.getVarianceVariable());

			byte calculation = variable.getCalculation();

			if (
				calculation == JRVariable.CALCULATION_AVERAGE ||
				calculation == JRVariable.CALCULATION_VARIANCE
				)
			{
				removeVariable(variable.getCountVariable());
				removeVariable(variable.getSumVariable());
			}

			if (calculation == JRVariable.CALCULATION_STANDARD_DEVIATION)
			{
				removeVariable(variable.getVarianceVariable());
			}

			variablesList.remove(variable);
			variablesMap.remove(variable.getName());
		}
		
		return variable;
	}


	/**
	 *
	 */
	public JRGroup[] getGroups()
	{
		JRGroup[] groupsArray = new JRGroup[groupsList.size()];
		
		groupsList.toArray(groupsArray);

		return groupsArray;
	}
	

	/**
	 *
	 */
	public List getGroupsList()
	{
		return groupsList;
	}
	

	/**
	 *
	 */
	public Map getGroupsMap()
	{
		return groupsMap;
	}
	

	/**
	 *
	 */
	public void addGroup(JRDesignGroup group) throws JRException
	{
		if (groupsMap.containsKey(group.getName()))
		{
			throw new JRException("Duplicate declaration of group : " + group.getName());
		}
		
		JRDesignVariable countVariable = new JRDesignVariable();
		countVariable.setName(group.getName() + "_COUNT");
		countVariable.setValueClass(Integer.class);
		countVariable.setResetType(JRVariable.RESET_TYPE_GROUP);
		countVariable.setResetGroup(group);
		countVariable.setCalculation(JRVariable.CALCULATION_COUNT);
		countVariable.setSystemDefined(true);
		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variable + countVariable.getName());
		expression.setText("new Integer(1)");
		countVariable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + countVariable.getName());
		expression.setText("new Integer(0)");
		countVariable.setInitialValueExpression((JRExpression)expression);

		addVariable(countVariable);

		group.setCountVariable(countVariable);

		groupsList.add(group);
		groupsMap.put(group.getName(), group);
	}
	

	/**
	 *
	 */
	public JRGroup removeGroup(String groupName)
	{
		return removeGroup(
			(JRGroup)groupsMap.get(groupName)
			);
	}


	/**
	 *
	 */
	public JRGroup removeGroup(JRGroup group)
	{
		if (group != null)
		{
			removeVariable(group.getCountVariable());
			groupsList.remove(group);
			groupsMap.remove(group.getName());
		}
		
		return group;
	}


	/**
	 *
	 */
	public Collection getExpressions()
	{
		Collection expressions = new HashSet();
		
		expressions.addAll(getParameterExpressions());
		expressions.addAll(getVariableExpressions());
		expressions.addAll(getGroupExpressions());

		expressions.addAll(getBandExpressions(background));
		expressions.addAll(getBandExpressions(title));
		expressions.addAll(getBandExpressions(pageHeader));
		expressions.addAll(getBandExpressions(columnHeader));
		expressions.addAll(getBandExpressions(detail));
		expressions.addAll(getBandExpressions(columnFooter));
		expressions.addAll(getBandExpressions(pageFooter));
		expressions.addAll(getBandExpressions(lastPageFooter));
		expressions.addAll(getBandExpressions(summary));
		
		return expressions;
	}
		

	/**
	 *
	 */
	private Collection getParameterExpressions()
	{
		Collection expressions = new HashSet();
		
		if (parametersList != null && parametersList.size() > 0)
		{
			JRParameter parameter = null;
			JRExpression expression = null;
			for(int i = 0; i < parametersList.size(); i++)
			{
				parameter = (JRParameter)parametersList.get(i);
				expression = parameter.getDefaultValueExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}
			}
		}
		
		return expressions;
	}


	/**
	 *
	 */
	private Collection getVariableExpressions()
	{
		Collection expressions = new HashSet();
		
		if (variablesList != null && variablesList.size() > 0)
		{
			JRVariable variable = null;
			JRExpression expression = null;
			for(int i = 0; i < variablesList.size(); i++)
			{
				variable = (JRVariable)variablesList.get(i);
				expression = variable.getExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}

				expression = variable.getInitialValueExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}
			}
		}
		
		return expressions;
	}


	/**
	 *
	 */
	private Collection getGroupExpressions()
	{
		Collection expressions = new HashSet();
		
		if (groupsList != null && groupsList.size() > 0)
		{
			JRGroup group = null;
			JRExpression expression = null;
			for(int i = 0; i < groupsList.size(); i++)
			{
				group = (JRGroup)groupsList.get(i);
				expression = group.getExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}

				expressions.addAll(getBandExpressions(group.getGroupHeader()));
				expressions.addAll(getBandExpressions(group.getGroupFooter()));
			}
		}
		
		return expressions;
	}


	/**
	 *
	 */
	private Collection getBandExpressions(JRBand band)
	{
		Collection expressions = new HashSet();
		
		if (band != null)
		{
			JRExpression expression = null;
			expression = band.getPrintWhenExpression();
			if (expression != null)
			{
				expressions.add(expression);
			}
	
			JRElement[] elements = band.getElements();
			if (elements != null && elements.length > 0)
			{
				JRElement element = null;
				for(int i = 0; i < elements.length; i++)
				{
					element = elements[i];
					expression = element.getPrintWhenExpression();
					if (expression != null)
					{
						expressions.add(expression);
					}
	
					if (element instanceof JRImage)
					{
						expression = ((JRImage)element).getExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
					else if (element instanceof JRTextField)
					{
						expression = ((JRTextField)element).getExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
					else if (element instanceof JRSubreport)
					{
						expression = ((JRSubreport)element).getParametersMapExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
	
						JRSubreportParameter[] parameters = ((JRSubreport)element).getParameters();
						if (parameters != null && parameters.length > 0)
						{
							for(int j = 0; j < parameters.length; j++)
							{
								expression = parameters[j].getExpression();
								if (expression != null)
								{
									expressions.add(expression);
								}
							}
						}
	
						expression = ((JRSubreport)element).getConnectionExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRSubreport)element).getDataSourceExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRSubreport)element).getExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
	
					if (element instanceof JRAnchor)
					{
						expression = ((JRAnchor)element).getAnchorNameExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
	
					if (element instanceof JRHyperlink)
					{
						expression = ((JRHyperlink)element).getHyperlinkReferenceExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRHyperlink)element).getHyperlinkAnchorExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRHyperlink)element).getHyperlinkPageExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
				}
			}
		}
		
		return expressions;
	}
	

}
