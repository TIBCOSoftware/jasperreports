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
package dori.jasper.engine.design;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import dori.jasper.engine.JRAbstractScriptlet;
import dori.jasper.engine.JRAnchor;
import dori.jasper.engine.JRBand;
import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRField;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRHyperlink;
import dori.jasper.engine.JRImage;
import dori.jasper.engine.JRParameter;
import dori.jasper.engine.JRQuery;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRSubreport;
import dori.jasper.engine.JRSubreportParameter;
import dori.jasper.engine.JRTextField;
import dori.jasper.engine.JRVariable;
import dori.jasper.engine.base.JRBaseReport;


/**
 *
 */
public class JasperDesign extends JRBaseReport
{

	
	/**
	 *
	 */
	private static final long serialVersionUID = 501;

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
			this.addParameter(parameter);
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
			this.addParameter(parameter);
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
			this.addParameter(parameter);
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
			this.addParameter(parameter);
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
		expression.setName("variableInitialValue_PAGE_NUMBER");
		//expression.setText("($V{PAGE_NUMBER} != null)?(new Integer($V{PAGE_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			this.addVariable(variable);
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
		expression.setName("variableInitialValue_COLUMN_NUMBER");
		//expression.setText("($V{COLUMN_NUMBER} != null)?(new Integer($V{COLUMN_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			this.addVariable(variable);
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
		expression.setName("variable_REPORT_COUNT");
		expression.setText("new Integer(1)");
		variable.setExpression(expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName("variableInitialValue_REPORT_COUNT");
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			this.addVariable(variable);
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
		expression.setName("variable_PAGE_COUNT");
		expression.setText("new Integer(1)");
		variable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName("variableInitialValue_PAGE_COUNT");
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			this.addVariable(variable);
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
		expression.setName("variable_COLUMN_COUNT");
		expression.setText("new Integer(1)");
		variable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName("variableInitialValue_COLUMN_COUNT");
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			this.addVariable(variable);
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
		return this.fontsList;
	}
	

	/**
	 *
	 */
	public Map getFontsMap()
	{
		return this.fontsMap;
	}
	

	/**
	 *
	 */
	public void addFont(JRReportFont reportFont) throws JRException
	{
		if (this.fontsMap.containsKey(reportFont.getName()))
		{
			throw new JRException("Duplicate declaration of report font : " + reportFont.getName());
		}

		this.fontsList.add(reportFont);
		this.fontsMap.put(reportFont.getName(), reportFont);
		
		if (reportFont.isDefault())
		{
			this.setDefaultFont(reportFont);
		}
	}
	

	/**
	 *
	 */
	public JRReportFont removeFont(String name)
	{
		return this.removeFont(
			(JRReportFont)this.fontsMap.get(name)
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
				this.setDefaultFont(null);
			}

			this.fontsList.remove(reportFont);
			this.fontsMap.remove(reportFont.getName());
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
		return this.parametersList;
	}
	

	/**
	 *
	 */
	public Map getParametersMap()
	{
		return this.parametersMap;
	}
	

	/**
	 *
	 */
	public void addParameter(JRParameter parameter) throws JRException
	{
		if (this.parametersMap.containsKey(parameter.getName()))
		{
			throw new JRException("Duplicate declaration of parameter : " + parameter.getName());
		}

		this.parametersList.add(parameter);
		this.parametersMap.put(parameter.getName(), parameter);
	}
	

	/**
	 *
	 */
	public JRParameter removeParameter(String parameterName)
	{
		return this.removeParameter(
			(JRParameter)this.parametersMap.get(parameterName)
			);
	}


	/**
	 *
	 */
	public JRParameter removeParameter(JRParameter parameter)
	{
		if (parameter != null)
		{
			this.parametersList.remove(parameter);
			this.parametersMap.remove(parameter.getName());
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
		return this.fieldsList;
	}
	

	/**
	 *
	 */
	public Map getFieldsMap()
	{
		return this.fieldsMap;
	}
	

	/**
	 *
	 */
	public void addField(JRField field) throws JRException
	{
		if (this.fieldsMap.containsKey(field.getName()))
		{
			throw new JRException("Duplicate declaration of field : " + field.getName());
		}

		this.fieldsList.add(field);
		this.fieldsMap.put(field.getName(), field);
	}
	

	/**
	 *
	 */
	public JRField removeField(String fieldName)
	{
		return this.removeField(
			(JRField)this.fieldsMap.get(fieldName)
			);
	}


	/**
	 *
	 */
	public JRField removeField(JRField field)
	{
		if (field != null)
		{
			this.fieldsList.remove(field);
			this.fieldsMap.remove(field.getName());
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
		return this.variablesList;
	}
	

	/**
	 *
	 */
	public Map getVariablesMap()
	{
		return this.variablesMap;
	}
	

	/**
	 *
	 */
	public void addVariable(JRDesignVariable variable) throws JRException
	{
		if (this.variablesMap.containsKey(variable.getName()))
		{
			throw new JRException("Duplicate declaration of variable : " + variable.getName());
		}
		
		byte calculation = variable.getCalculation();

		if (
			calculation == JRVariable.CALCULATION_AVERAGE ||
			calculation == JRVariable.CALCULATION_VARIANCE
			)
		{
			JRDesignVariable countVariable = null;
			switch (variable.getResetType())
			{
				case JRVariable.RESET_TYPE_REPORT : 
				{
					countVariable = (JRDesignVariable)variablesMap.get(JRVariable.REPORT_COUNT);
					break;
				}
				case JRVariable.RESET_TYPE_PAGE : 
				{
					countVariable = (JRDesignVariable)variablesMap.get(JRVariable.PAGE_COUNT);
					break;
				}
				case JRVariable.RESET_TYPE_COLUMN : 
				{
					countVariable = (JRDesignVariable)variablesMap.get(JRVariable.COLUMN_COUNT);
					break;
				}
				case JRVariable.RESET_TYPE_GROUP : 
				{
					countVariable = new JRDesignVariable();
					countVariable.setName(variable.getName() + "_COUNT");
					countVariable.setValueClass(variable.getValueClass());
					countVariable.setResetType(variable.getResetType());
					countVariable.setResetGroup(variable.getResetGroup());
					countVariable.setCalculation(JRVariable.CALCULATION_COUNT);
					countVariable.setSystemDefined(true);
					countVariable.setExpression(variable.getExpression());
					this.addVariable(countVariable);

					break;
				}
			}
			variable.setCountVariable(countVariable);

			JRDesignVariable sumVariable = new JRDesignVariable();
			sumVariable.setName(variable.getName() + "_SUM");
			sumVariable.setValueClass(variable.getValueClass());
			sumVariable.setResetType(variable.getResetType());
			sumVariable.setResetGroup(variable.getResetGroup());
			sumVariable.setCalculation(JRVariable.CALCULATION_SUM);
			sumVariable.setSystemDefined(true);
			sumVariable.setExpression(variable.getExpression());
			this.addVariable(sumVariable);
			variable.setSumVariable(sumVariable);
		}

		if (calculation == JRVariable.CALCULATION_STANDARD_DEVIATION)
		{
			JRDesignVariable varianceVariable = new JRDesignVariable();
			varianceVariable.setName(variable.getName() + "_VARIANCE");
			varianceVariable.setValueClass(variable.getValueClass());
			varianceVariable.setResetType(variable.getResetType());
			varianceVariable.setResetGroup(variable.getResetGroup());
			varianceVariable.setCalculation(JRVariable.CALCULATION_VARIANCE);
			varianceVariable.setSystemDefined(true);
			varianceVariable.setExpression(variable.getExpression());
			this.addVariable(varianceVariable);
			variable.setVarianceVariable(varianceVariable);
		}

		this.variablesList.add(variable);
		this.variablesMap.put(variable.getName(), variable);
	}
	

	/**
	 *
	 */
	public JRVariable removeVariable(String variableName)
	{
		return this.removeVariable(
			(JRVariable)this.variablesMap.get(variableName)
			);
	}


	/**
	 *
	 */
	public JRVariable removeVariable(JRVariable variable)
	{
		if (variable != null)
		{
			this.removeVariable(variable.getSumVariable());
			this.removeVariable(variable.getVarianceVariable());
			this.variablesList.remove(variable);
			this.variablesMap.remove(variable.getName());
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
		return this.groupsList;
	}
	

	/**
	 *
	 */
	public Map getGroupsMap()
	{
		return this.groupsMap;
	}
	

	/**
	 *
	 */
	public void addGroup(JRDesignGroup group) throws JRException
	{
		if (this.groupsMap.containsKey(group.getName()))
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
		expression.setName("variable_" + countVariable.getName());
		expression.setText("new Integer(1)");
		countVariable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName("variableInitialValue_" + countVariable.getName());
		expression.setText("new Integer(0)");
		countVariable.setInitialValueExpression((JRExpression)expression);

		this.addVariable(countVariable);

		group.setCountVariable(countVariable);

		this.groupsList.add(group);
		this.groupsMap.put(group.getName(), group);
	}
	

	/**
	 *
	 */
	public JRGroup removeGroup(String groupName)
	{
		return this.removeGroup(
			(JRGroup)this.groupsMap.get(groupName)
			);
	}


	/**
	 *
	 */
	public JRGroup removeGroup(JRGroup group)
	{
		if (group != null)
		{
			this.removeVariable(group.getCountVariable());
			this.groupsList.remove(group);
			this.groupsMap.remove(group.getName());
		}
		
		return group;
	}


	/**
	 *
	 */
	public Collection getExpressions()
	{
		Collection expressions = new HashSet();
		
		expressions.addAll(this.getParameterExpressions());
		expressions.addAll(this.getVariableExpressions());
		expressions.addAll(this.getGroupExpressions());

		expressions.addAll(this.getBandExpressions(this.background));
		expressions.addAll(this.getBandExpressions(this.title));
		expressions.addAll(this.getBandExpressions(this.pageHeader));
		expressions.addAll(this.getBandExpressions(this.columnHeader));
		expressions.addAll(this.getBandExpressions(this.detail));
		expressions.addAll(this.getBandExpressions(this.columnFooter));
		expressions.addAll(this.getBandExpressions(this.pageFooter));
		expressions.addAll(this.getBandExpressions(this.summary));
		
		return expressions;
	}
		

	/**
	 *
	 */
	private Collection getParameterExpressions()
	{
		Collection expressions = new HashSet();
		
		if (this.parametersList != null && this.parametersList.size() > 0)
		{
			JRParameter parameter = null;
			JRExpression expression = null;
			for(int i = 0; i < this.parametersList.size(); i++)
			{
				parameter = (JRParameter)this.parametersList.get(i);
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
		
		if (this.variablesList != null && this.variablesList.size() > 0)
		{
			JRVariable variable = null;
			JRExpression expression = null;
			for(int i = 0; i < this.variablesList.size(); i++)
			{
				variable = (JRVariable)this.variablesList.get(i);
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
		
		if (this.groupsList != null && this.groupsList.size() > 0)
		{
			JRGroup group = null;
			JRExpression expression = null;
			for(int i = 0; i < this.groupsList.size(); i++)
			{
				group = (JRGroup)this.groupsList.get(i);
				expression = group.getExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}

				expressions.addAll(this.getBandExpressions(group.getGroupHeader()));
				expressions.addAll(this.getBandExpressions(group.getGroupFooter()));
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
