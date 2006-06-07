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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDefaultCompiler;
import net.sf.jasperreports.engine.design.JRDesignSubreportReturnValue;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSingletonCache;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillSubreport extends JRFillElement implements JRSubreport
{


	/**
	 *
	 */
	private static final Log log = LogFactory.getLog(JRFillSubreport.class);
	
	private static final JRSingletonCache runnerFactoryCache = new JRSingletonCache(JRSubreportRunnerFactory.class);

	/**
	 *
	 */
	private Map parameterValues = null;
	private JRSubreportParameter[] parameters = null;
	private Connection connection = null;
	private JRDataSource dataSource = null;
	private JasperReport jasperReport = null;

	private Map loadedEvaluators = null;
	
	/**
	 * Values to be copied from the subreport.
	 */
	private JRFillSubreportReturnValue[] returnValues = null;

	/**
	 *
	 */
	private JRBaseFiller subreportFiller = null;
	private JRPrintPage printPage = null;
	private JRReportFont[] subreportFonts = null;
	private JRStyle[] subreportStyles = null;

	private JRSubreportRunner runner;
	
	/**
	 * Set of checked reports.
	 */
	private Set checkedReports;


	/**
	 *
	 */
	protected JRFillSubreport(
		JRBaseFiller filler,
		JRSubreport subreport, 
		JRFillObjectFactory factory
		)
	{
		super(filler, subreport, factory);

		parameters = subreport.getParameters();
		JRSubreportReturnValue[] subrepReturnValues = subreport.getReturnValues();
		if (subrepReturnValues != null)
		{
			List returnValuesList = new ArrayList(subrepReturnValues.length * 2);
			
			returnValues = new JRFillSubreportReturnValue[subrepReturnValues.length];
			for (int i = 0; i < subrepReturnValues.length; i++)
			{
				addReturnValue(subrepReturnValues[i], returnValuesList, factory);
			}
			
			returnValues = new JRFillSubreportReturnValue[returnValuesList.size()];
			returnValuesList.toArray(returnValues);
		}
		
		loadedEvaluators = new HashMap();
		checkedReports = new HashSet();
	}


	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}

	/**
	 *
	 */
	public boolean isUsingCache()
	{
		return ((JRSubreport)parent).isUsingCache();
	}
		
	/**
	 * @deprecated Replaced by {@link #setUsingCache(Boolean)}.
	 */
	public void setUsingCache(boolean isUsingCache)
	{
	}
		
	/**
	 *
	 */
	public JRExpression getParametersMapExpression()
	{
		return ((JRSubreport)parent).getParametersMapExpression();
	}

	/**
	 *
	 */
	public JRSubreportParameter[] getParameters()
	{
		return parameters;
	}

	/**
	 *
	 */
	public JRExpression getConnectionExpression()
	{
		return ((JRSubreport)parent).getConnectionExpression();
	}

	/**
	 *
	 */
	public JRExpression getDataSourceExpression()
	{
		return ((JRSubreport)parent).getDataSourceExpression();
	}

	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return ((JRSubreport)parent).getExpression();
	}

	/**
	 *
	 */
	protected JRTemplateRectangle getJRTemplateRectangle()
	{
		JRStyle style = getStyle();
		JRTemplateRectangle template = (JRTemplateRectangle) getTemplate(style);
		if (template == null)
		{
			template = new JRTemplateRectangle(filler.getJasperPrint().getDefaultStyleProvider(), this);
			registerTemplate(style, template);
		}
		return template;
	}


	/**
	 *
	 */
	protected JRReportFont[] getFonts()
	{
		return subreportFonts;
	}


	/**
	 *
	 */
	protected JRStyle[] getStyles()
	{
		return subreportStyles;
	}


	/**
	 *
	 */
	protected Collection getPrintElements()
	{
		Collection printElements = null;
		
		if (printPage != null)
		{
			printElements = printPage.getElements();
		}
		
		return printElements;
	}


	/**
	 *
	 */
	protected void evaluate(
		byte evaluation
		) throws JRException
	{
		reset();
		
		evaluatePrintWhenExpression(evaluation);

		if (
			(isPrintWhenExpressionNull() ||
			(!isPrintWhenExpressionNull() && 
			isPrintWhenTrue()))
			)
		{
			JRExpression expression = getExpression();
			Object source = evaluateExpression(expression, evaluation);
			if (source != null) // FIXME put some default broken image like in browsers
			{
				JREvaluator evaluator = null;
				
				if (isUsingCache() && filler.loadedSubreports.containsKey(source))
				{
					jasperReport = (JasperReport)filler.loadedSubreports.get(source);
					evaluator = (JREvaluator)loadedEvaluators.get(jasperReport);

					if (evaluator == null)
					{
						evaluator = JRDefaultCompiler.getInstance().loadEvaluator(jasperReport);
						loadedEvaluators.put(jasperReport, evaluator);
					}
				}
				else
				{
					Class expressionClass = expression.getValueClass();
					
					if (expressionClass.equals(net.sf.jasperreports.engine.JasperReport.class))
					{
						jasperReport = (JasperReport)source;
					}
					else if (expressionClass.equals(java.io.InputStream.class))
					{
						jasperReport = (JasperReport)JRLoader.loadObject((InputStream)source);
					}
					else if (expressionClass.equals(java.net.URL.class))
					{
						jasperReport = (JasperReport)JRLoader.loadObject((URL)source);
					}
					else if (expressionClass.equals(java.io.File.class))
					{
						jasperReport = (JasperReport)JRLoader.loadObject((File)source);
					}
					else if (expressionClass.equals(java.lang.String.class))
					{
						jasperReport = (JasperReport)JRLoader.loadObjectFromLocation((String)source, filler.reportClassLoader,
								filler.urlHandlerFactory);
					}
					
					if (jasperReport != null)
					{
						evaluator = JRDefaultCompiler.getInstance().loadEvaluator(jasperReport);
					}
					
					if (isUsingCache())
					{
						filler.loadedSubreports.put(source, jasperReport);
						loadedEvaluators.put(jasperReport, evaluator);
					}
				}
				
				if (jasperReport != null)
				{
					/*   */
					expression = getConnectionExpression();
					connection = (Connection) evaluateExpression(expression, evaluation);
			
					/*   */
					expression = getDataSourceExpression();
					dataSource = (JRDataSource) evaluateExpression(expression, evaluation);
					
					boolean hasResourceBundle = jasperReport.getResourceBundle() != null;
					parameterValues = getParameterValues(filler, getParametersMapExpression(), getParameters(), evaluation, false, hasResourceBundle);

					if (subreportFiller != null)
					{
						filler.unregisterSubfiller(subreportFiller);
					}
		
					/*   */
					initSubreportFiller(evaluator);
					
					checkReturnValues();
				}
			}
		}
	}


	protected void initSubreportFiller(JREvaluator evaluator) throws JRException
	{
		switch (jasperReport.getPrintOrder())
		{
			case JRReport.PRINT_ORDER_HORIZONTAL :
			{
				subreportFiller = new JRHorizontalFiller(jasperReport, evaluator, filler);
				break;
			}
			case JRReport.PRINT_ORDER_VERTICAL :
			{
				subreportFiller = new JRVerticalFiller(jasperReport, evaluator, filler);
				break;
			}
			default :
			{
				throw new JRRuntimeException("Unkown print order " + jasperReport.getPrintOrder() + ".");
			}
		}
		
		runner = getRunnerFactory().createSubreportRunner(this, subreportFiller);
		subreportFiller.setSubreportRunner(runner);
	}


	/**
	 * Utility method used for constructing a parameter values map for subreports, sub datasets and crosstabs.
	 * 
	 * @param filler report filler
	 * @param parametersMapExpression expression that yields bulk parameter values map
	 * @param subreportParameters list of individual parameter values
	 * @param evaluation evaluation type
	 * @param ignoreNullExpressions whether to ignore individual parameter value expressions
	 * @param removeResourceBundle whether to remove the {@link JRParameter#REPORT_RESOURCE_BUNDLE REPORT_RESOURCE_BUNDLE}
	 * 	value from the bulk values map
	 * @return the parameter values map
	 * @throws JRException
	 */
	public static Map getParameterValues(
			JRBaseFiller filler, 
			JRExpression parametersMapExpression, 
			JRDatasetParameter[] subreportParameters, 
			byte evaluation, 
			boolean ignoreNullExpressions, 
			boolean removeResourceBundle) throws JRException
	{
		Map parameterValues = null;
		if (parametersMapExpression != null)
		{
			parameterValues = (Map) filler.evaluateExpression(parametersMapExpression, evaluation);
		}		
		
		if (parameterValues != null)
		{
			//parameterValues.remove(JRParameter.REPORT_LOCALE);
			if (removeResourceBundle)
			{
				parameterValues.remove(JRParameter.REPORT_RESOURCE_BUNDLE);
			}
			parameterValues.remove(JRParameter.REPORT_CONNECTION);
			parameterValues.remove(JRParameter.REPORT_MAX_COUNT);
			parameterValues.remove(JRParameter.REPORT_DATA_SOURCE);
			parameterValues.remove(JRParameter.REPORT_SCRIPTLET);
			parameterValues.remove(JRParameter.REPORT_VIRTUALIZER);
			//parameterValues.remove(JRParameter.REPORT_CLASS_LOADER);
			parameterValues.remove(JRParameter.IS_IGNORE_PAGINATION);
			parameterValues.remove(JRParameter.REPORT_PARAMETERS_MAP);
		}
		
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}
		
		/*   */
		if (subreportParameters != null && subreportParameters.length > 0)
		{
			Object parameterValue = null;
			for(int i = 0; i < subreportParameters.length; i++)
			{
				JRExpression expression = subreportParameters[i].getExpression();
				if (expression != null || !ignoreNullExpressions)
				{
					parameterValue = filler.evaluateExpression(expression, evaluation);
					if (parameterValue == null)
					{
						parameterValues.remove(subreportParameters[i].getName());
					}
					else
					{
						parameterValues.put(subreportParameters[i].getName(), parameterValue);
					}
				}
			}
		}
		
		if (!parameterValues.containsKey(JRParameter.REPORT_LOCALE))
		{
			parameterValues.put(JRParameter.REPORT_LOCALE, filler.getLocale());
		}

		if (!parameterValues.containsKey(JRParameter.REPORT_CLASS_LOADER) &&
				filler.reportClassLoader != null)
		{
			parameterValues.put(JRParameter.REPORT_CLASS_LOADER, filler.reportClassLoader);
		}

		if (!parameterValues.containsKey(JRParameter.REPORT_URL_HANDLER_FACTORY) &&
				filler.urlHandlerFactory != null)
		{
			parameterValues.put(JRParameter.REPORT_URL_HANDLER_FACTORY, filler.urlHandlerFactory);
		}
		
		return parameterValues;
	}

	protected void fillSubreport() throws JRException
	{
		if (getConnectionExpression() != null)
		{
			subreportFiller.fill(parameterValues, connection);
		}
		else if (getDataSourceExpression() != null)
		{
			subreportFiller.fill(parameterValues, dataSource);
		}
		else
		{
			subreportFiller.fill(parameterValues);
		}
	}
	

	/**
	 *
	 */
	protected boolean prepare(
		int availableStretchHeight,
		boolean isOverflow
		) throws JRException
	{
		boolean willOverflow = false;

		super.prepare(availableStretchHeight, isOverflow);
		
		if (subreportFiller == null)
		{
			setToPrint(false);
		}

		if (!isToPrint())
		{
			return willOverflow;
		}

		if (availableStretchHeight < getRelativeY() - getY() - getBandBottomY())
		{
			setToPrint(false);
			return true;//willOverflow;
		}
			
		//willOverflow = prepareTextField((JRFillTextField)fillElement, availableStretchHeight);
		
		//subreportFiller.setPageHeight(getHeight() + availableStretchHeight);
		
		boolean filling = runner.isFilling();
		boolean toPrint = !isOverflow || isPrintWhenDetailOverflows() || !isAlreadyPrinted();
		boolean reprinted = isOverflow && isPrintWhenDetailOverflows();
		
		if (!filling && toPrint && reprinted)
		{
			rewind();
		}
		
		int availableHeight = getHeight() + availableStretchHeight - getRelativeY() + getY() + getBandBottomY();
		subreportFiller.setPageHeight(availableHeight);

		synchronized (subreportFiller)
		{
			JRSubreportRunResult result;
			if (filling)
			{
				result = runner.resume();
			}
			else if (toPrint)
			{
				setReprinted(reprinted);

				result = runner.start();
			}
			else
			{
				printPage = null;
				subreportFonts = null;
				subreportStyles = null;
				setStretchHeight(getHeight());
				setToPrint(false);

				return willOverflow;
			}
			
			if (result.getException() != null)
			{
				Throwable error = result.getException();
				if (error instanceof RuntimeException)
				{
					throw (RuntimeException) error;
				}

				throw new JRRuntimeException(error);
			}

			if (result.hasFinished())
			{
				copyValues();
			}

			printPage = subreportFiller.getCurrentPage();
			subreportFonts = subreportFiller.getFonts();
			subreportStyles = subreportFiller.getStyles();
			setStretchHeight(result.hasFinished() ? subreportFiller.getCurrentPageStretchHeight() : availableHeight);

			//if the subreport fill thread has not finished, 
			// it means that the subreport will overflow on the next page
			willOverflow = !result.hasFinished();
			
			if (!willOverflow)
			{
				//the subreport fill thread has finished and the next time we shall create a new one
				runner.reset();
			}
		}// synchronized
		
		Collection printElements = getPrintElements();
		if (
			(printElements == null || printElements.size() == 0) &&
			isRemoveLineWhenBlank() //FIXME if the line won't be removed, the background does not appear
			)
		{
			setToPrint(false);
		}

		return willOverflow;
	}


	/**
	 *
	 */
	public void rewind() throws JRException
	{
		if (subreportFiller == null)
		{
			return;
		}
		
		// marking the subreport filler for interruption
		subreportFiller.setInterrupted(true);
		
		synchronized (subreportFiller)
		{
			// forcing the creation of a new thread and a new subreport filler
			runner.cancel();
			runner.reset();
		}

		filler.unregisterSubfiller(subreportFiller);
		
		initSubreportFiller(null);//FIXME used chached evaluator

		if (getConnectionExpression() == null && dataSource != null)
		{
			if(dataSource instanceof JRRewindableDataSource)
			{
				((JRRewindableDataSource) dataSource).moveFirst();
			}
			else
			{
				if (log.isDebugEnabled())
					log.debug("The subreport is placed on a non-splitting band, but it does not have a rewindable data source.");
			}
		}
	}


	/**
	 *
	 */
	protected JRPrintElement fill()
	{
		JRPrintRectangle printRectangle = new JRTemplatePrintRectangle(getJRTemplateRectangle());

		printRectangle.setX(getX());
		printRectangle.setY(getRelativeY());
		printRectangle.setWidth(getWidth());
		printRectangle.setHeight(getStretchHeight());
		
		return printRectangle;
	}


	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getSubreport(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeSubreport(this);
	}


	private JRFillSubreportReturnValue addReturnValue (JRSubreportReturnValue parentReturnValue, List returnValueList, JRFillObjectFactory factory)
	{
		JRFillSubreportReturnValue returnValue = factory.getSubreportReturnValue(parentReturnValue);
		
		byte calculation = returnValue.getCalculation();
		switch (calculation)
		{
			case JRVariable.CALCULATION_AVERAGE:
			case JRVariable.CALCULATION_VARIANCE:
			{
				JRSubreportReturnValue countVal = createHelperReturnValue(parentReturnValue, "_COUNT", JRVariable.CALCULATION_COUNT);
				addReturnValue(countVal, returnValueList, factory);

				JRSubreportReturnValue sumVal = createHelperReturnValue(parentReturnValue, "_SUM", JRVariable.CALCULATION_SUM);
				addReturnValue(sumVal, returnValueList, factory);

				filler.addVariableCalculationReq(returnValue.getToVariable(), calculation);

				break;
			}
			case JRVariable.CALCULATION_STANDARD_DEVIATION:
			{
				JRSubreportReturnValue varianceVal = createHelperReturnValue(parentReturnValue, "_VARIANCE", JRVariable.CALCULATION_VARIANCE);
				addReturnValue(varianceVal, returnValueList, factory);
				
				filler.addVariableCalculationReq(returnValue.getToVariable(), calculation);
				break;
			}
		}

		returnValueList.add(returnValue);
		return returnValue;

	}

	
	protected JRSubreportReturnValue createHelperReturnValue(JRSubreportReturnValue returnValue, String nameSuffix, byte calculation)
	{
		JRDesignSubreportReturnValue helper = new JRDesignSubreportReturnValue();
		helper.setToVariable(returnValue.getToVariable() + nameSuffix);
		helper.setSubreportVariable(returnValue.getSubreportVariable());
		helper.setCalculation(calculation);
		helper.setIncrementerFactoryClassName(helper.getIncrementerFactoryClassName());
		
		return helper;
	}
	

	public JRSubreportReturnValue[] getReturnValues()
	{
		return this.returnValues;
	}
	

	/**
	 * Copies the values from the subreport to the variables of the master report.
	 */
	private void copyValues()
	{
		if (returnValues != null && returnValues.length > 0)
		{
			for (int i = 0; i < returnValues.length; i++)
			{
				try
				{
					JRFillVariable variable = filler.getVariable(returnValues[i].getToVariable());
					Object value = subreportFiller.getVariableValue(returnValues[i].getSubreportVariable());
					
					Object newValue = returnValues[i].getIncrementer().increment(variable, value, AbstractValueProvider.getCurrentValueProvider());
					variable.setOldValue(newValue);
					variable.setValue(newValue);
				}
				catch (JRException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
	}


	/**
	 * Verifies the list of copied values against the subreport.
	 * 
	 * @throws JRException
	 */
	private void checkReturnValues() throws JRException
	{
		if (returnValues != null && returnValues.length > 0 && !checkedReports.contains(jasperReport))
		{
			for (int i = 0; i < returnValues.length; i++)
			{
				JRSubreportReturnValue returnValue = returnValues[i];
				String subreportVariableName = returnValue.getSubreportVariable();
				JRVariable subrepVariable = subreportFiller.getVariable(subreportVariableName);
				if (subrepVariable == null)
				{
					throw new JRException("Subreport variable " + subreportVariableName + " not found.");
				}
				
				JRVariable variable = filler.getVariable(returnValue.getToVariable());
				if (returnValue.getCalculation() == JRVariable.CALCULATION_COUNT)
				{
					if (!Number.class.isAssignableFrom(variable.getValueClass()))
					{
						throw new JRException("Variable " + returnValue.getToVariable() + 
								" must have a numeric type.");
					}
				}
				else if (!variable.getValueClass().isAssignableFrom(subrepVariable.getValueClass()) &&
						!(Number.class.isAssignableFrom(variable.getValueClass()) && Number.class.isAssignableFrom(subrepVariable.getValueClass())))
				{
					throw new JRException("Variable " + returnValue.getToVariable() + 
							" is not assignable from subreport variable " + 
							subreportVariableName);
				}
			}
			
			if (isUsingCache())
				checkedReports.add(jasperReport);
		}
	}
	
	
	protected void resolveElement (JRPrintElement element, byte evaluation)
	{
		// nothing
	}


	public Boolean isOwnUsingCache()
	{
		return ((JRSubreport)parent).isOwnUsingCache();
	}


	public void setUsingCache(Boolean isUsingCache)
	{
	}


	public JRCloneable createClone(JRFillCloneFactory factory)
	{
		//not needed
		return null;
	}
	
	protected static JRSubreportRunnerFactory getRunnerFactory() throws JRException
	{
		String factoryClassName = JRProperties.getProperty(JRProperties.SUBREPORT_RUNNER_FACTORY);
		if (factoryClassName == null)
		{
			throw new JRException("Property \"" + JRProperties.SUBREPORT_RUNNER_FACTORY + "\" must be set");
		}
		return (JRSubreportRunnerFactory) runnerFactoryCache.getCachedInstance(factoryClassName);
	}
}
