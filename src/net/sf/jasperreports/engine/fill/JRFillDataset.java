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
package net.sf.jasperreports.engine.fill;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDefaultCompiler;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRQueryExecuterUtils;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillDataset implements JRDataset
{
	/**
	 * The filler that created this object.
	 */
	private final JRBaseFiller filler;
	
	/**
	 * The template dataset.
	 */
	private final JRDataset parent;
	
	/**
	 * Whether this is the main dataset of the report.
	 */
	private final boolean isMain;
	
	/**
	 * The dataset query.
	 */
	protected JRQuery query = null;
	
	private boolean useDatasourceParamValue = false;
	private boolean useConnectionParamValue = false;
	
	/**
	 * The dataset parameter.
	 */
	protected JRFillParameter[] parameters = null;

	/**
	 * The dataset parameters indexed by name.
	 */
	protected Map parametersMap = null;

	/**
	 * The dataset fields.
	 */
	protected JRFillField[] fields = null;
	
	/**
	 * The dataset fields indexed by name.
	 */
	protected Map fieldsMap = null;
	
	/**
	 * The dataset variables.
	 */
	protected JRFillVariable[] variables = null;
	
	/**
	 * The dataset variables indexed by name.
	 */
	protected Map variablesMap = null;
	
	/**
	 * Set of {@link VariableCalculationReq VariableCalculationReq} objects.
	 */
	protected Set variableCalculationReqs;

	/**
	 * The element datasets.
	 */
	protected JRFillElementDataset[] elementDatasets;
	
	/**
	 * Used to save the original element datasets when
	 * {@link #filterElementDatasets(JRFillElementDataset) filterElementDatasets} is called.
	 */
	protected JRFillElementDataset[] origElementDatasets;

	/**
	 * The dataset groups.
	 */
	protected JRFillGroup[] groups = null;

	/**
	 * The resource bundle base name.
	 */
	protected String resourceBundleBaseName = null;
	
	/**
	 * The resource missing handle type.
	 */
	protected byte whenResourceMissingType;
	
	/**
	 * The scriptlet class name.
	 */
	protected String scriptletClassName = null;

	/**
	 * The data source. 
	 */
	protected JRDataSource dataSource = null;
	
	/**
	 * The {@link Locale Locale} to be used by the dataset.
	 */
	protected Locale locale = null;
	
	/**
	 * The loaded resource bundle.
	 */
	protected ResourceBundle resourceBundle = null;

	/**
	 * The {@link TimeZone TimeZone} to be used by the dataset.
	 */
	protected TimeZone timeZone = null;
	
	/**
	 * The cursor used when iterating the data source.
	 */
	protected int reportCount = 0;

	/**
	 * The calculator used by the dataset.
	 */
	protected JRCalculator calculator = null;

	/**
	 * The scriptlet used by the dataset.
	 */
	protected JRAbstractScriptlet scriptlet = null;

	/**
	 * The value of the {@link JRParameter#REPORT_MAX_COUNT max count} parameter.
	 */
	protected Integer reportMaxCount = null;

	private JRQueryExecuter queryExecuter;

	
	/**
	 * Creates a fill dataset object.
	 * @param filler the filelr
	 * @param dataset the template dataset
	 * @param factory the fill object factory
	 */
	protected JRFillDataset(JRBaseFiller filler, JRDataset dataset, JRFillObjectFactory factory)
	{
		factory.put(dataset, this);
		
		this.filler = filler;
		this.parent = dataset;
		this.isMain = dataset.isMainDataset();
		
		scriptletClassName = dataset.getScriptletClass();
		resourceBundleBaseName = dataset.getResourceBundle();
		whenResourceMissingType = dataset.getWhenResourceMissingType();
		
		query = dataset.getQuery();
		
		setParameters(dataset, factory);

		setFields(dataset, factory);

		setVariables(dataset, factory);
		
		setGroups(dataset, factory);
	}

	
	private void setParameters(JRDataset dataset, JRFillObjectFactory factory)
	{
		JRParameter[] jrParameters = dataset.getParameters();
		if (jrParameters != null && jrParameters.length > 0)
		{
			parameters = new JRFillParameter[jrParameters.length];
			parametersMap = new HashMap();
			for (int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getParameter(jrParameters[i]);
				parametersMap.put(parameters[i].getName(), parameters[i]);
			}
		}
	}


	private void setGroups(JRDataset dataset, JRFillObjectFactory factory)
	{
		JRGroup[] jrGroups = dataset.getGroups();
		if (jrGroups != null && jrGroups.length > 0)
		{
			groups = new JRFillGroup[jrGroups.length];
			for (int i = 0; i < groups.length; i++)
			{
				groups[i] = factory.getGroup(jrGroups[i]);
			}
		}
	}


	private void setVariables(JRDataset dataset, JRFillObjectFactory factory)
	{
		JRVariable[] jrVariables = dataset.getVariables();
		if (jrVariables != null && jrVariables.length > 0)
		{
			List variableList = new ArrayList(jrVariables.length * 3);

			variablesMap = new HashMap();
			for (int i = 0; i < jrVariables.length; i++)
			{
				addVariable(jrVariables[i], variableList, factory);
			}

			setVariables(variableList);
		}
	}
	
	
	private JRFillVariable addVariable(JRVariable parentVariable, List variableList, JRFillObjectFactory factory)
	{
		JRFillVariable variable = factory.getVariable(parentVariable);

		byte calculation = variable.getCalculation();
		switch (calculation)
		{
			case JRVariable.CALCULATION_AVERAGE:
			case JRVariable.CALCULATION_VARIANCE:
			{
				JRVariable countVar = createHelperVariable(parentVariable, "_COUNT", JRVariable.CALCULATION_COUNT);
				JRFillVariable fillCountVar = addVariable(countVar, variableList, factory);
				variable.setHelperVariable(fillCountVar, JRCalculable.HELPER_COUNT);

				JRVariable sumVar = createHelperVariable(parentVariable, "_SUM", JRVariable.CALCULATION_SUM);
				JRFillVariable fillSumVar = addVariable(sumVar, variableList, factory);
				variable.setHelperVariable(fillSumVar, JRCalculable.HELPER_SUM);

				break;
			}
			case JRVariable.CALCULATION_STANDARD_DEVIATION:
			{
				JRVariable varianceVar = createHelperVariable(parentVariable, "_VARIANCE", JRVariable.CALCULATION_VARIANCE);
				JRFillVariable fillVarianceVar = addVariable(varianceVar, variableList, factory);
				variable.setHelperVariable(fillVarianceVar, JRCalculable.HELPER_VARIANCE);

				break;
			}
			case JRVariable.CALCULATION_DISTINCT_COUNT:
			{
				JRVariable countVar = createDistinctCountHelperVariable(parentVariable);
				JRFillVariable fillCountVar = addVariable(countVar, variableList, factory);
				variable.setHelperVariable(fillCountVar, JRCalculable.HELPER_COUNT);

				break;
			}
		}

		variableList.add(variable);
		return variable;
	}

	private JRVariable createHelperVariable(JRVariable variable, String nameSuffix, byte calculation)
	{
		JRDesignVariable helper = new JRDesignVariable();
		helper.setName(variable.getName() + nameSuffix);
		helper.setValueClassName(variable.getValueClassName());
		helper.setIncrementerFactoryClassName(variable.getIncrementerFactoryClassName());
		helper.setResetType(variable.getResetType());
		helper.setResetGroup(variable.getResetGroup());
		helper.setIncrementType(variable.getIncrementType());
		helper.setIncrementGroup(variable.getIncrementGroup());
		helper.setCalculation(calculation);
		helper.setSystemDefined(true);
		helper.setExpression(variable.getExpression());

		return helper;
	}

	private JRVariable createDistinctCountHelperVariable(JRVariable variable)
	{
		JRDesignVariable helper = new JRDesignVariable();
		helper.setName(variable.getName() + "_DISTINCT_COUNT");
		helper.setValueClassName(variable.getValueClassName());
		helper.setIncrementerFactoryClassName(JRDistinctCountIncrementerFactory.class.getName());
		helper.setResetType(JRVariable.RESET_TYPE_REPORT);

		if (variable.getIncrementType() != JRVariable.RESET_TYPE_NONE)
			helper.setResetType(variable.getIncrementType());
			
		helper.setResetGroup(variable.getIncrementGroup());
		helper.setCalculation(JRVariable.CALCULATION_NOTHING);
		helper.setSystemDefined(true);
		helper.setExpression(variable.getExpression());
		
		return helper;
	}

	private void setVariables(List variableList)
	{
		variables = new JRFillVariable[variableList.size()];
		variables = (JRFillVariable[]) variableList.toArray(variables);

		for (int i = 0; i < variables.length; i++)
		{
			variablesMap.put(variables[i].getName(), variables[i]);
		}
	}


	private void setFields(JRDataset dataset, JRFillObjectFactory factory)
	{
		JRField[] jrFields = dataset.getFields();
		if (jrFields != null && jrFields.length > 0)
		{
			fields = new JRFillField[jrFields.length];
			fieldsMap = new HashMap();
			for (int i = 0; i < fields.length; i++)
			{
				fields[i] = factory.getField(jrFields[i]);
				fieldsMap.put(fields[i].getName(), fields[i]);
			}
		}
	}


	/**
	 * Creates the calculator
	 * @param jasperReport the report
	 * @throws JRException
	 */
	protected void createCalculator(JasperReport jasperReport) throws JRException
	{
		setCalculator(createCalculator(jasperReport, this));
	}

	protected void setCalculator(JRCalculator calculator)
	{
		this.calculator = calculator;
	}

	protected static JRCalculator createCalculator(JasperReport jasperReport, JRDataset dataset) throws JRException
	{
		JREvaluator evaluator = JRDefaultCompiler.getInstance().loadEvaluator(jasperReport, dataset);
		return new JRCalculator(evaluator);
	}


	/**
	 * Initializes the calculator.
	 * 
	 * @throws JRException
	 */
	protected void initCalculator() throws JRException
	{
		calculator.init(this);
	}


	/**
	 * Inherits properties from the report.
	 */
	protected void inheritFromMain()
	{
		if (resourceBundleBaseName == null && !isMain)
		{
			resourceBundleBaseName = filler.mainDataset.resourceBundleBaseName;
			whenResourceMissingType = filler.mainDataset.whenResourceMissingType;
		}
	}
	
	
	/**
	 * Creates the scriptlet.
	 * 
	 * @return the scriptlet
	 * @throws JRException
	 */
	protected JRAbstractScriptlet createScriptlet() throws JRException
	{
		JRAbstractScriptlet tmpScriptlet = null;

		if (scriptletClassName != null)
		{
			try
			{
				Class scriptletClass = JRClassLoader.loadClassForName(scriptletClassName);	
				tmpScriptlet = (JRAbstractScriptlet) scriptletClass.newInstance();
			}
			catch (ClassNotFoundException e)
			{
				throw new JRException("Error loading scriptlet class : " + scriptletClassName, e);
			}
			catch (Exception e)
			{
				throw new JRException("Error creating scriptlet class instance : " + scriptletClassName, e);
			}
		}
		else
		{
			tmpScriptlet = new JRDefaultScriptlet();
		}

		return tmpScriptlet;
	}


	/**
	 * Initializes the element datasets.
	 * 
	 * @param factory the fill object factory used by the filler
	 */
	protected void initElementDatasets(JRFillObjectFactory factory)
	{
		elementDatasets = factory.getElementDatasets(this);
	}


	/**
	 * Filters the element datasets, leaving only one.
	 * <p>
	 * This method is used when a dataset is instantiated by a chart or crosstab.
	 * 
	 * @param elementDataset the element dataset that should remain
	 */
	protected void filterElementDatasets(JRFillElementDataset elementDataset)
	{
		origElementDatasets = elementDatasets;
		elementDatasets = new JRFillElementDataset[]{elementDataset};
	}
	
	
	/**
	 * Restores the original element datasets.
	 * <p>
	 * This method should be called after {@link #filterElementDatasets(JRFillElementDataset) filterElementDatasets}.
	 */
	protected void restoreElementDatasets()
	{
		if (origElementDatasets != null)
		{
			elementDatasets = origElementDatasets;
			origElementDatasets = null;
		}
	}
	

	/**
	 * Loads the resource bundle corresponding to the resource bundle base name and locale.
	 */
	protected ResourceBundle loadResourceBundle()
	{
		ResourceBundle tmpResourceBundle = null;

		if (resourceBundleBaseName != null)
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader != null)
			{
				try
				{
					tmpResourceBundle = ResourceBundle.getBundle(resourceBundleBaseName, locale, classLoader);
				}
				catch (MissingResourceException e)
				{
					// if (log.isWarnEnabled())
					// log.warn("Failure using
					// Thread.currentThread().getContextClassLoader() in
					// JRClassLoader class. Using
					// JRClassLoader.class.getClassLoader() instead.");
				}
			}

			if (tmpResourceBundle == null)
			{
				classLoader = JRClassLoader.class.getClassLoader();

				if (classLoader == null)
				{
					tmpResourceBundle = ResourceBundle.getBundle(resourceBundleBaseName, locale);
				}
				else
				{
					tmpResourceBundle = ResourceBundle.getBundle(resourceBundleBaseName, locale, classLoader);
				}
			}
		}

		return tmpResourceBundle;
	}


	/**
	 * Reads built-in parameter values from the value map.
	 * 
	 * @param parameterValues the parameter values
	 * @throws JRException 
	 */
	protected void setParameterValues(Map parameterValues) throws JRException
	{
		parameterValues.put(JRParameter.REPORT_PARAMETERS_MAP, parameterValues);
		
		reportMaxCount = (Integer) parameterValues.get(JRParameter.REPORT_MAX_COUNT);

		locale = (Locale) parameterValues.get(JRParameter.REPORT_LOCALE);
		if (locale == null)
		{
			locale = Locale.getDefault();
			parameterValues.put(JRParameter.REPORT_LOCALE, locale);
		}
		
		resourceBundle = (ResourceBundle) parameterValues.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		if (resourceBundle == null)
		{
			resourceBundle = loadResourceBundle();
			if (resourceBundle != null)
			{
				parameterValues.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
			}
		}
		
		timeZone = (TimeZone) parameterValues.get(JRParameter.REPORT_TIME_ZONE);
		if (timeZone == null)
		{
			timeZone = TimeZone.getDefault();
			parameterValues.put(JRParameter.REPORT_TIME_ZONE, timeZone);
		}
		
		scriptlet = (JRAbstractScriptlet) parameterValues.get(JRParameter.REPORT_SCRIPTLET);
		if (scriptlet == null)
		{
			scriptlet = createScriptlet();
		}
		scriptlet.setData(parametersMap, fieldsMap, variablesMap, groups);
		
		parameterValues.put(JRParameter.REPORT_SCRIPTLET, scriptlet);
		
		setFillParameterValues(parameterValues);
		
		setDatasource();
	}
	
	
	private void setDatasource() throws JRException
	{
		queryExecuter = null;
		
		dataSource = (JRDataSource) getParameterValue(JRParameter.REPORT_DATA_SOURCE);
		if (!useDatasourceParamValue && (useConnectionParamValue || dataSource == null))
		{
			dataSource = createQueryDatasource();
			setParameter(JRParameter.REPORT_DATA_SOURCE, dataSource);
		}
	}


	/**
	 * Sets the parameter values from the values map.
	 * 
	 * @param parameterValues the values map
	 * @throws JRException
	 */
	private void setFillParameterValues(Map parameterValues) throws JRException
	{
		if (parameters != null && parameters.length > 0)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				Object value = null;
				if (parameterValues.containsKey(parameters[i].getName()))
				{
					value = parameterValues.get(parameters[i].getName());
				}
				else if (!parameters[i].isSystemDefined())
				{
					value = calculator.evaluate(parameters[i].getDefaultValueExpression(), JRExpression.EVALUATION_DEFAULT);
					if (value != null)
					{
						parameterValues.put(parameters[i].getName(), value);
					}
				}
				setParameter(parameters[i], value);
			}
		}
	}

	
	/**
	 * Creates the data source from a connection.
	 * 
	 * @return the data source to be used
	 * @throws JRException
	 */
	private JRDataSource createQueryDatasource() throws JRException
	{
		if (query == null)
		{
			return null;
		}

		try
		{
			JRQueryExecuterFactory queryExecuterFactory = JRQueryExecuterUtils.getQueryExecuterFactory(query.getLanguage());
			queryExecuter = queryExecuterFactory.createQueryExecuter(parent, parametersMap);
			filler.fillContext.setRunningQueryExecuter(queryExecuter);
			
			return queryExecuter.createDatasource();
		}
		finally
		{
			filler.fillContext.clearRunningQueryExecuter();
		}
	}


	protected void reset()
	{
		useDatasourceParamValue = false;
		useConnectionParamValue = false;
	}

	
	/**
	 * Sets the data source to be used.
	 * 
	 * @param parameterValues the parameter values
	 * @param ds the data source
	 */
	protected void setDatasourceParameterValue(Map parameterValues, JRDataSource ds)
	{
		useDatasourceParamValue = true;
		
		if (ds != null)
		{
			parameterValues.put(JRParameter.REPORT_DATA_SOURCE, ds);
		}
	}


	/**
	 * Sets the JDBC connection to be used.
	 * 
	 * @param parameterValues the parameter values
	 * @param conn the connection
	 */
	protected void setConnectionParameterValue(Map parameterValues, Connection conn)
	{
		useConnectionParamValue = true;
		
		if (conn != null)
		{
			parameterValues.put(JRParameter.REPORT_CONNECTION, conn);
		}
	}
	
	
	protected void closeDatasource()
	{
		if (queryExecuter != null)
		{
			queryExecuter.close();
			queryExecuter = null;
		}
		
		reset();
	}

	
	/**
	 * Starts the iteration on the data source.
	 */
	protected void start()
	{
		reportCount = 0;
	}

	
	/**
	 * Moves to the next record in the data source.
	 * 
	 * @return <code>true</code> if the data source was not exhausted
	 * @throws JRException
	 */
	protected boolean next() throws JRException
	{
		boolean hasNext = false;

		if (dataSource != null)
		{
			boolean includeRow = true;
			JRExpression filterExpression = getFilterExpression();
			do
			{
				hasNext = advanceDataSource();
				if (hasNext)
				{
					setOldValues();

					calculator.estimateVariables();
					if (filterExpression != null)
					{
						Boolean filterExprResult = (Boolean) calculator.evaluate(filterExpression, JRExpression.EVALUATION_ESTIMATED);
						includeRow = filterExprResult != null && filterExprResult.booleanValue();
					}
					
					if (!includeRow)
					{
						revertToOldValues();
					}
				}
			}
			while(hasNext && !includeRow);
			
			if (hasNext)
			{
				++reportCount;
			}
		}

		return hasNext;
	}


	protected void setOldValues() throws JRException
	{
		if (fields != null && fields.length > 0)
		{
			for (int i = 0; i < fields.length; i++)
			{
				JRFillField field = fields[i];
				field.setPreviousOldValue(field.getOldValue());
				field.setOldValue(field.getValue());
				field.setValue(dataSource.getFieldValue(field));
			}
		}

		if (variables != null && variables.length > 0)
		{
			for (int i = 0; i < variables.length; i++)
			{
				JRFillVariable variable = variables[i];
				variable.setPreviousOldValue(variable.getOldValue());
				variable.setOldValue(variable.getValue());
			}
		}
	}


	protected void revertToOldValues()
	{
		if (fields != null && fields.length > 0)
		{
			for (int i = 0; i < fields.length; i++)
			{
				JRFillField field = fields[i];
				field.setValue(field.getOldValue());
				field.setOldValue(field.getPreviousOldValue());
			}
		}
		
		if (variables != null && variables.length > 0)
		{
			for (int i = 0; i < variables.length; i++)
			{
				JRFillVariable variable = variables[i];
				variable.setValue(variable.getOldValue());
				variable.setOldValue(variable.getPreviousOldValue());
			}
		}
	}


	protected boolean advanceDataSource() throws JRException
	{
		boolean hasNext;
		hasNext = (reportMaxCount == null || reportMaxCount.intValue() > reportCount) && dataSource.next();
		return hasNext;
	}
	
	
	/**
	 * Sets the value of a parameter.
	 * 
	 * @param parameterName the parameter name
	 * @param value the value
	 * @throws JRException
	 */
	protected void setParameter(String parameterName, Object value) throws JRException
	{
		JRFillParameter parameter = (JRFillParameter) parametersMap.get(parameterName);
		if (parameter != null)
		{
			setParameter(parameter, value);
		}
	}
	
	
	/**
	 * Sets the value of the parameter.
	 * 
	 * @param parameter the parameter
	 * @param value the value
	 * @throws JRException
	 */
	protected void setParameter(JRFillParameter parameter, Object value) throws JRException
	{
		if (value != null)
		{
			if (parameter.getValueClass().isInstance(value))
			{
				parameter.setValue(value);
			}
			else
			{
				throw new JRException(
					"Incompatible " 
					+ value.getClass().getName() 
					+ " value assigned to parameter " 
					+ parameter.getName() 
					+ " in the " + getName() + " dataset."
					);
			}
		}
		else
		{
			parameter.setValue(value);
		}
	}

	
	/**
	 * Returns the value of a variable.
	 * 
	 * @param variableName the variable name
	 * @return the variable value
	 */
	public Object getVariableValue(String variableName)
	{
		JRFillVariable var = (JRFillVariable) variablesMap.get(variableName);
		if (var == null)
		{
			throw new JRRuntimeException("No such variable " + variableName);
		}
		return var.getValue();
	}

	
	/**
	 * Returns the value of a parameter.
	 * 
	 * @param parameterName the parameter name
	 * @return the parameter value
	 */
	public Object getParameterValue(String parameterName)
	{
		JRFillParameter param = (JRFillParameter) parametersMap.get(parameterName);
		if (param == null)
		{
			throw new JRRuntimeException("No such parameter " + parameterName);
		}
		return param.getValue();
	}

	
	/**
	 * Returns the value of a field.
	 * 
	 * @param fieldName the field name
	 * @return the field value
	 */
	public Object getFieldValue(String fieldName)
	{
		JRFillField var = (JRFillField) fieldsMap.get(fieldName);
		if (var == null)
		{
			throw new JRRuntimeException("No such field " + fieldName);
		}
		return var.getValue();
	}
	
	
	/**
	 * Class used to hold expression calculation  requirements.
	 */
	protected static class VariableCalculationReq
	{
		String variableName;

		byte calculation;

		VariableCalculationReq(String variableName, byte calculation)
		{
			this.variableName = variableName;
			this.calculation = calculation;
		}

		public boolean equals(Object o)
		{
			if (o == null || !(o instanceof VariableCalculationReq))
			{
				return false;
			}

			VariableCalculationReq r = (VariableCalculationReq) o;

			return variableName.equals(r.variableName) && calculation == r.calculation;
		}

		public int hashCode()
		{
			return 31 * calculation + variableName.hashCode();
		}
	}
	
	
	/**
	 * Adds a variable calculation requirement.
	 * 
	 * @param variableName the variable name
	 * @param calculation the required calculation
	 */
	protected void addVariableCalculationReq(String variableName, byte calculation)
	{
		if (variableCalculationReqs == null)
		{
			variableCalculationReqs = new HashSet();
		}

		variableCalculationReqs.add(new VariableCalculationReq(variableName, calculation));
	}

	
	/**
	 * Checks if there are variable calculation requirements and creates the required variables.
	 * 
	 * @param factory the fill object factory
	 */
	protected void checkVariableCalculationReqs(JRFillObjectFactory factory)
	{
		if (variableCalculationReqs != null && !variableCalculationReqs.isEmpty())
		{
			List variableList = new ArrayList(variables.length * 2);

			for (int i = 0; i < variables.length; i++)
			{
				JRFillVariable variable = variables[i];
				checkVariableCalculationReq(variable, variableList, factory);
			}

			setVariables(variableList);
		}
	}

	
	private void checkVariableCalculationReq(JRFillVariable variable, List variableList, JRFillObjectFactory factory)
	{
		if (hasVariableCalculationReq(variable, JRVariable.CALCULATION_AVERAGE) || hasVariableCalculationReq(variable, JRVariable.CALCULATION_VARIANCE))
		{
			if (variable.getHelperVariable(JRCalculable.HELPER_COUNT) == null)
			{
				JRVariable countVar = createHelperVariable(variable, "_COUNT", JRVariable.CALCULATION_COUNT);
				JRFillVariable fillCountVar = factory.getVariable(countVar);
				checkVariableCalculationReq(fillCountVar, variableList, factory);
				variable.setHelperVariable(fillCountVar, JRCalculable.HELPER_COUNT);
			}

			if (variable.getHelperVariable(JRCalculable.HELPER_SUM) == null)
			{
				JRVariable sumVar = createHelperVariable(variable, "_SUM", JRVariable.CALCULATION_SUM);
				JRFillVariable fillSumVar = factory.getVariable(sumVar);
				checkVariableCalculationReq(fillSumVar, variableList, factory);
				variable.setHelperVariable(fillSumVar, JRCalculable.HELPER_SUM);
			}
		}

		if (hasVariableCalculationReq(variable, JRVariable.CALCULATION_STANDARD_DEVIATION))
		{
			if (variable.getHelperVariable(JRCalculable.HELPER_VARIANCE) == null)
			{
				JRVariable varianceVar = createHelperVariable(variable, "_VARIANCE", JRVariable.CALCULATION_VARIANCE);
				JRFillVariable fillVarianceVar = factory.getVariable(varianceVar);
				checkVariableCalculationReq(fillVarianceVar, variableList, factory);
				variable.setHelperVariable(fillVarianceVar, JRCalculable.HELPER_VARIANCE);
			}
		}

		if (hasVariableCalculationReq(variable, JRVariable.CALCULATION_DISTINCT_COUNT))
		{
			if (variable.getHelperVariable(JRCalculable.HELPER_COUNT) == null)
			{
				JRVariable countVar = createDistinctCountHelperVariable(variable);
				JRFillVariable fillCountVar = factory.getVariable(countVar);
				checkVariableCalculationReq(fillCountVar, variableList, factory);
				variable.setHelperVariable(fillCountVar, JRCalculable.HELPER_COUNT);
			}
		}

		variableList.add(variable);
	}

	
	private boolean hasVariableCalculationReq(JRVariable var, byte calculation)
	{
		return variableCalculationReqs.contains(new VariableCalculationReq(var.getName(), calculation));
	}


	public String getName()
	{
		return parent.getName();
	}

	public String getScriptletClass()
	{
		return parent.getScriptletClass();
	}

	public JRParameter[] getParameters()
	{
		return parameters;
	}

	public Map getParametersMap()
	{
		return parametersMap;
	}

	public JRQuery getQuery()
	{
		return query;
	}

	public JRField[] getFields()
	{
		return fields;
	}

	public JRVariable[] getVariables()
	{
		return variables;
	}

	public JRGroup[] getGroups()
	{
		return groups;
	}

	public boolean isMainDataset()
	{
		return isMain;
	}

	public String getResourceBundle()
	{
		return parent.getResourceBundle();
	}


	public byte getWhenResourceMissingType()
	{
		return whenResourceMissingType;
	}


	public void setWhenResourceMissingType(byte whenResourceMissingType)
	{
		this.whenResourceMissingType = whenResourceMissingType;
	}


	public JRPropertiesMap getPropertiesMap()
	{
		return parent.getPropertiesMap();
	}


	public JRExpression getFilterExpression()
	{
		return parent.getFilterExpression();
	}
}
