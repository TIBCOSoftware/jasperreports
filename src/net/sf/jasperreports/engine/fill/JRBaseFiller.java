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
package dori.jasper.engine.fill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dori.jasper.engine.JRAbstractScriptlet;
import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRDefaultFontProvider;
import dori.jasper.engine.JRDefaultScriptlet;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRField;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRParameter;
import dori.jasper.engine.JRPrintImage;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JRQuery;
import dori.jasper.engine.JRReport;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRResultSetDataSource;
import dori.jasper.engine.JRVariable;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.JasperReport;
import dori.jasper.engine.design.JRDefaultCompiler;
import dori.jasper.engine.util.JRClassLoader;
import dori.jasper.engine.util.JRGraphEnvInitializer;
import dori.jasper.engine.util.JRQueryExecuter;


/**
 *
 */
public abstract class JRBaseFiller implements JRDefaultFontProvider
{

	
	/**
	 *
	 */
	private static final Log log = LogFactory.getLog(JRBaseFiller.class);

	/**
	 *
	 */
	private JRBaseFiller parentFiller = null;

	/**
	 *
	 */
	private boolean isInterrupted = false;

	/**
	 *
	 */
	protected String name = null;
	protected int columnCount = 1;
	protected byte printOrder = JRReport.PRINT_ORDER_VERTICAL;
	protected int pageWidth = 0;
	protected int pageHeight = 0;
	protected byte orientation = JRReport.ORIENTATION_PORTRAIT;
	protected byte whenNoDataType = JRReport.WHEN_NO_DATA_TYPE_NO_PAGES;
	protected int columnWidth = 0;
	protected int columnSpacing = 0;
	protected int leftMargin = 0;
	protected int rightMargin = 0;
	protected int topMargin = 0;
	protected int bottomMargin = 0;
	protected boolean isTitleNewPage = false;
	protected boolean isSummaryNewPage = false;
	protected String scriptletClass = null;

	protected JRReportFont defaultFont = null;
	protected JRReportFont[] fonts = null;
	protected JRFillParameter[] parameters = null;
	protected Map parametersMap = null;
	protected JRQuery query = null;
	protected JRFillField[] fields = null;
	protected Map fieldsMap = null;
	protected JRFillVariable[] variables = null;
	protected Map variablesMap = null;
	protected JRFillGroup[] groups = null;

	protected JRFillBand background = null;
	protected JRFillBand title = null;
	protected JRFillBand pageHeader = null;
	protected JRFillBand columnHeader = null;
	protected JRFillBand detail = null;
	protected JRFillBand columnFooter = null;
	protected JRFillBand pageFooter = null;
	protected JRFillBand summary = null;

	protected JRCalculator calculator = null;
	protected JRAbstractScriptlet scriptlet = null;
	protected JRDataSource dataSource = null;

	protected Map loadedImages = null;
	protected Map loadedSubreports = null;

	protected Map reportBoundImages = null;
	protected Map pageBoundImages = null;
	protected Map columnBoundImages = null;
	protected Map groupBoundImages = null;

	protected Map reportBoundTexts = null;
	protected Map pageBoundTexts = null;
	protected Map columnBoundTexts = null;
	protected Map groupBoundTexts = null;

	/**
	 *
	 */
	protected JasperPrint jasperPrint = null;
	protected JRPrintPage printPage = null;
	protected int printPageStretchHeight = 0;

	/**
	 *
	 */
	protected boolean isParametersAlreadySet = false;

	
	/**
	 *
	 */
	protected JRBaseFiller(JasperReport jasperReport, JRBaseFiller parentFiller) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();
		
		/*   */
		this.parentFiller = parentFiller;

		/*   */
		this.name = jasperReport.getName();
		this.columnCount = jasperReport.getColumnCount();
		this.printOrder = jasperReport.getPrintOrder();
		this.pageWidth = jasperReport.getPageWidth();
		this.pageHeight = jasperReport.getPageHeight();
		this.orientation = jasperReport.getOrientation();
		this.whenNoDataType = jasperReport.getWhenNoDataType();
		this.columnWidth = jasperReport.getColumnWidth();
		this.columnSpacing = jasperReport.getColumnSpacing();
		this.leftMargin = jasperReport.getLeftMargin();
		this.rightMargin = jasperReport.getRightMargin();
		this.topMargin = jasperReport.getTopMargin();
		this.bottomMargin = jasperReport.getBottomMargin();
		this.isTitleNewPage = jasperReport.isTitleNewPage();
		this.isSummaryNewPage = jasperReport.isSummaryNewPage();
		this.scriptletClass = jasperReport.getScriptletClass();

		jasperPrint = new JasperPrint();

		/*   */
		JRFillObjectFactory factory = new JRFillObjectFactory(this);

		/*   */
		defaultFont = factory.getReportFont(jasperReport.getDefaultFont());

		/*   */
		JRReportFont[] jrFonts = jasperReport.getFonts();
		if (jrFonts != null && jrFonts.length > 0)
		{
			fonts = new JRReportFont[jrFonts.length];
			for(int i = 0; i < fonts.length; i++)
			{
				fonts[i] = factory.getReportFont(jrFonts[i]);
			}
		}

		/*   */
		JRParameter[] jrParameters = jasperReport.getParameters();
		if (jrParameters != null && jrParameters.length > 0)
		{
			parameters = new JRFillParameter[jrParameters.length];
			parametersMap = new HashMap();
			for(int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getParameter(jrParameters[i]);
				parametersMap.put(parameters[i].getName(), parameters[i]);
			}
		}

		/*   */
		this.query = jasperReport.getQuery();
		
		/*   */
		JRField[] jrFields = jasperReport.getFields();
		if (jrFields != null && jrFields.length > 0)
		{
			fields = new JRFillField[jrFields.length];
			fieldsMap = new HashMap();
			for(int i = 0; i < fields.length; i++)
			{
				fields[i] = factory.getField(jrFields[i]);
				fieldsMap.put(fields[i].getName(), fields[i]);
			}
		}

		/*   */
		JRVariable[] jrVariables = jasperReport.getVariables();
		if (jrVariables != null && jrVariables.length > 0)
		{
			variables = new JRFillVariable[jrVariables.length];
			variablesMap = new HashMap();
			for(int i = 0; i < variables.length; i++)
			{
				variables[i] = factory.getVariable(jrVariables[i]);
				variablesMap.put(variables[i].getName(), variables[i]);
			}
		}

		/*   */
		JRGroup[] jrGroups = jasperReport.getGroups();
		if (jrGroups != null && jrGroups.length > 0)
		{
			groups = new JRFillGroup[jrGroups.length];
			for(int i = 0; i < groups.length; i++)
			{
				groups[i] = factory.getGroup(jrGroups[i]);
			}
		}

		/*   */
		this.background = factory.getBand(jasperReport.getBackground());
		this.title = factory.getBand(jasperReport.getTitle());
		this.pageHeader = factory.getBand(jasperReport.getPageHeader());
		this.columnHeader = factory.getBand(jasperReport.getColumnHeader());
		this.detail = factory.getBand(jasperReport.getDetail());
		this.columnFooter = factory.getBand(jasperReport.getColumnFooter());
		this.pageFooter = factory.getBand(jasperReport.getPageFooter());
		this.summary = factory.getBand(jasperReport.getSummary());

		/*   */
		this.scriptlet = 
			loadScriptlet(
				scriptletClass,
				parametersMap,
				fieldsMap,
				variablesMap,
				groups
				);

		/*   */
		this.calculator = 
			loadCalculator(
				jasperReport,
				parametersMap,
				fieldsMap,
				variablesMap,
				variables,
				groups
				);
	}


	/**
	 *
	 */
	public JasperPrint getJasperPrint()
	{
		return jasperPrint;
	}


	/**
	 *
	 */
	public JRReportFont getDefaultFont()
	{
		return defaultFont;
	}


	/**
	 *
	 */
	protected boolean isSubreport()
	{
		return (this.parentFiller != null);
	}

	/**
	 *
	 */
	protected boolean isInterrupted()
	{
		return (this.isInterrupted || (this.parentFiller != null && this.parentFiller.isInterrupted()));
	}

	/**
	 *
	 */
	protected void setInterrupted(boolean isInterrupted)
	{
		this.isInterrupted = isInterrupted;
	}

	/**
	 *
	 */
	protected JRPrintPage getCurrentPage()
	{
		return printPage;
	}

	/**
	 *
	 */
	protected JRReportFont[] getFonts()
	{
		return fonts;
	}

	/**
	 *
	 */
	protected int getCurrentPageStretchHeight()
	{
		return this.printPageStretchHeight;
	}

	/**
	 *
	 */
	protected abstract void setPageHeight(int pageHeight) throws JRException;
	

	/**
	 *
	 */
	public JasperPrint fill(
		Map parameterValues, 
		Connection conn
		) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		this.setParameters(parameterValues);
		this.isParametersAlreadySet = true;

		if (conn == null)
		{
			if (log.isWarnEnabled())
				log.warn("The supplied java.sql.Connection object is null.");
		}

		JRFillParameter parameter = null;
		parameterValues.put(JRParameter.REPORT_CONNECTION, conn);
		parameter = (JRFillParameter)this.parametersMap.get(JRParameter.REPORT_CONNECTION);
		if (parameter != null)
		{
			this.setParameter(parameter, conn);
		}

		PreparedStatement pstmt = null; 
		
		try
		{
			JRDataSource ds = null;

			pstmt = 
				JRQueryExecuter.getStatement(
					query, 
					parametersMap, 
					parameterValues, 
					conn
					);
	
			if (pstmt != null)
			{
				ResultSet rs = pstmt.executeQuery();
		
				ds = new JRResultSetDataSource(rs);
			}
		
			this.fill(parameterValues, ds);
		}
		catch (SQLException e)
		{
			throw new JRException("Error executing SQL statement for report : " + name, e);
		}
		finally
		{
			if (pstmt != null)
			{
				try
				{
					pstmt.close();
				}
				catch (SQLException e)
				{
				}
			}
		}
		
		return jasperPrint;
	}

	
	/**
	 *
	 */
	protected JasperPrint fill(
		Map parameterValues,
		JRDataSource ds
		) throws JRException
	{
		this.dataSource = ds;
		
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		JRFillParameter parameter = null;
		parameterValues.put(JRParameter.REPORT_PARAMETERS_MAP, parameterValues);
		parameter = (JRFillParameter)this.parametersMap.get(JRParameter.REPORT_PARAMETERS_MAP);
		if (parameter != null)
		{
			this.setParameter(parameter, parameterValues);
		}

		parameterValues.put(JRParameter.REPORT_DATA_SOURCE, dataSource);
		parameter = (JRFillParameter)this.parametersMap.get(JRParameter.REPORT_DATA_SOURCE);
		if (parameter != null)
		{
			this.setParameter(parameter, dataSource);
		}

		parameterValues.put(JRParameter.REPORT_SCRIPTLET, scriptlet);
		parameter = (JRFillParameter)this.parametersMap.get(JRParameter.REPORT_SCRIPTLET);
		if (parameter != null)
		{
			this.setParameter(parameter, scriptlet);
		}

		if (!this.isParametersAlreadySet)
		{
			this.setParameters(parameterValues);
			this.isParametersAlreadySet = true;
		}

		jasperPrint.setName(name);
		jasperPrint.setPageWidth(pageWidth);
		jasperPrint.setPageHeight(pageHeight);
		jasperPrint.setOrientation(orientation);

		jasperPrint.setDefaultFont(defaultFont);

		/*   */
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				jasperPrint.addFont(fonts[i]);
			}
		}

		this.fillReport();
		
		return jasperPrint;
	}
	

	/**
	 *
	 */
	protected abstract void fillReport() throws JRException;


	/**
	 *
	 */
	protected static JRAbstractScriptlet loadScriptlet(
		String className,
		Map parametersMap,
		Map fieldsMap,
		Map variablesMap,
		JRFillGroup[] groups
		) throws JRException
	{
		JRAbstractScriptlet scriptlet = null;

		if (className != null)
		{
			Class clazz = null;

			try
			{
				clazz = JRClassLoader.loadClassForName(className);
			}
			catch (ClassNotFoundException e)
			{
				throw new JRException("Error loading scriptlet class : " + className, e);
			}

			try
			{
				scriptlet = (JRAbstractScriptlet)clazz.newInstance();
			}
			catch (Exception e)
			{
				throw new JRException("Error creating scriptlet class instance : " + className, e);
			}
		}

		if (scriptlet == null)
		{
			scriptlet = new JRDefaultScriptlet();
		}

		scriptlet.setData(
				parametersMap,
				fieldsMap,
				variablesMap,
				groups
				);
		
		return scriptlet;
	}


	/**
	 *
	 */
	protected static JRCalculator loadCalculator(
		JasperReport jasperReport,
		Map parametersMap,
		Map fieldsMap,
		Map variablesMap,
		JRFillVariable[] variables,
		JRFillGroup[] groups
		) throws JRException
	{
		JRCalculator calculator = new JRDefaultCompiler().loadCalculator(jasperReport);
		
		calculator.init(
			parametersMap,
			fieldsMap,
			variablesMap,
			variables,
			groups
			);
		
		return calculator;
	}


	/**
	 *
	 */
	protected void setParameters(Map parameterValues) throws JRException
	{
		if (this.parameters != null && this.parameters.length > 0)
		{
			Object value = null;
			for(int i = 0; i < this.parameters.length; i++)
			{
				if (parameterValues.containsKey(parameters[i].getName()))
				{
					this.setParameter(
						parameters[i], 
						parameterValues.get(parameters[i].getName())
						);
				}
				else if (!parameters[i].isSystemDefined())
				{
					value = 
						this.calculator.evaluate(
							parameters[i].getDefaultValueExpression(), 
							JRExpression.EVALUATION_DEFAULT
							);
					if (value != null)
					{
						parameterValues.put(parameters[i].getName(), value);
					}
					this.setParameter(parameters[i], value);
				}
			}
		}
	}


	/**
	 *
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
				throw new JRException("Incompatible value assigned to parameter " + parameter.getName() + " : " + this.name);
			}
		}
		else
		{
			parameter.setValue(value);
		}
	}


	/**
	 *
	 */
	protected boolean next() throws JRException
	{
		boolean hasNext = false;
		
		if (dataSource != null)
		{
			hasNext = dataSource.next();

			if (hasNext)
			{
				/*   */
				if (fields != null && fields.length > 0)
				{
					JRFillField field = null;
					Object objValue = null;
					for(int i = 0; i < fields.length; i++)
					{
						field = fields[i];
						objValue = dataSource.getFieldValue(field);
						field.setOldValue(field.getValue());
						field.setValue(objValue);
					}
				}

				/*   */
				if (variables != null && variables.length > 0)
				{
					JRFillVariable variable = null;
					for(int i = 0; i < variables.length; i++)
					{
						variable = variables[i];
						variable.setOldValue(variable.getValue());
					}
				}
			}
		}
		
		return hasNext;
	}
	
	
	/**
	 *
	 */
	protected void resolveReportBoundImages() throws JRException
	{
		Collection images = reportBoundImages.keySet();
		JRPrintImage printImage = null;
		JRFillImage image = null;
		if (images != null && images.size() > 0)
		{
			for(Iterator it = images.iterator(); it.hasNext();)
			{
				printImage = (JRPrintImage)it.next();
				image = (JRFillImage)reportBoundImages.get(printImage);
				
				image.evaluateImage(JRExpression.EVALUATION_DEFAULT);

				image.copy(printImage);
			}
		}
		
		reportBoundImages = new HashMap();
	}


	/**
	 *
	 */
	protected void resolvePageBoundImages(byte evaluation) throws JRException
	{
		Collection images = pageBoundImages.keySet();
		JRPrintImage printImage = null;
		JRFillImage image = null;
		if (images != null && images.size() > 0)
		{
			for(Iterator it = images.iterator(); it.hasNext();)
			{
				printImage = (JRPrintImage)it.next();
				image = (JRFillImage)pageBoundImages.get(printImage);

				image.evaluateImage(evaluation);

				image.copy(printImage);
			}
		}
		
		pageBoundImages = new HashMap();
	}


	/**
	 *
	 */
	protected void resolveColumnBoundImages(byte evaluation) throws JRException
	{
		Collection images = columnBoundImages.keySet();
		JRPrintImage printImage = null;
		JRFillImage image = null;
		if (images != null && images.size() > 0)
		{
			for(Iterator it = images.iterator(); it.hasNext();)
			{
				printImage = (JRPrintImage)it.next();
				image = (JRFillImage)columnBoundImages.get(printImage);

				image.evaluateImage(evaluation);

				image.copy(printImage);
			}
		}
		
		columnBoundImages = new HashMap();
	}


	/**
	 *
	 */
	protected void resolveGroupBoundImages(byte evaluation, boolean isFinal) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			JRFillGroup group = null;
			Collection images = null;
			JRPrintImage printImage = null;
			JRFillImage image = null;
			Map specificGroupBoundImages = null;

			for(int i = 0; i < groups.length; i++)
			{
				group = (JRFillGroup)groups[i];

				if ((group.hasChanged() && group.isFooterPrinted()) || isFinal)
				{
					specificGroupBoundImages = (Map)groupBoundImages.get(group.getName());
	
					images = specificGroupBoundImages.keySet();
					if (images != null && images.size() > 0)
					{
						for(Iterator it = images.iterator(); it.hasNext();)
						{
							printImage = (JRPrintImage)it.next();
							image = (JRFillImage)specificGroupBoundImages.get(printImage);

							image.evaluateImage(evaluation);

							image.copy(printImage);
						}
					}
					
					groupBoundImages.put(group.getName(), new HashMap());
				}
			}
		}
	}


	/**
	 *
	 */
	protected void resolveReportBoundTexts() throws JRException
	{
		Collection texts = reportBoundTexts.keySet();
		JRPrintText text = null;
		JRFillTextField textField = null;
		if (texts != null && texts.size() > 0)
		{
			for(Iterator it = texts.iterator(); it.hasNext();)
			{
				text = (JRPrintText)it.next();
				textField = (JRFillTextField)reportBoundTexts.get(text);
				
				textField.evaluateText(JRExpression.EVALUATION_DEFAULT);

				textField.chopTextElement(0);

				textField.copy(text);
			}
		}
		
		reportBoundTexts = new HashMap();
	}


	/**
	 *
	 */
	protected void resolvePageBoundTexts(byte evaluation) throws JRException
	{
		Collection texts = pageBoundTexts.keySet();
		JRPrintText text = null;
		JRFillTextField textField = null;
		if (texts != null && texts.size() > 0)
		{
			for(Iterator it = texts.iterator(); it.hasNext();)
			{
				text = (JRPrintText)it.next();
				textField = (JRFillTextField)pageBoundTexts.get(text);

				textField.evaluateText(evaluation);

				textField.chopTextElement(0);

				textField.copy(text);
			}
		}
		
		pageBoundTexts = new HashMap();
	}


	/**
	 *
	 */
	protected void resolveColumnBoundTexts(byte evaluation) throws JRException
	{
		Collection texts = columnBoundTexts.keySet();
		JRPrintText text = null;
		JRFillTextField textField = null;
		if (texts != null && texts.size() > 0)
		{
			for(Iterator it = texts.iterator(); it.hasNext();)
			{
				text = (JRPrintText)it.next();
				textField = (JRFillTextField)columnBoundTexts.get(text);

				textField.evaluateText(evaluation);

				textField.chopTextElement(0);

				textField.copy(text);
			}
		}
		
		columnBoundTexts = new HashMap();
	}


	/**
	 *
	 */
	protected void resolveGroupBoundTexts(byte evaluation, boolean isFinal) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			JRFillGroup group = null;
			Collection texts = null;
			JRPrintText text = null;
			JRFillTextField textField = null;
			Map specificGroupBoundTexts = null;

			for(int i = 0; i < groups.length; i++)
			{
				group = (JRFillGroup)groups[i];

				if ((group.hasChanged() && group.isFooterPrinted()) || isFinal)
				{
					specificGroupBoundTexts = (Map)groupBoundTexts.get(group.getName());
	
					texts = specificGroupBoundTexts.keySet();
					if (texts != null && texts.size() > 0)
					{
						for(Iterator it = texts.iterator(); it.hasNext();)
						{
							text = (JRPrintText)it.next();
							textField = (JRFillTextField)specificGroupBoundTexts.get(text);

							textField.evaluateText(evaluation);

							textField.chopTextElement(0);
			
							textField.copy(text);
						}
					}
					
					groupBoundTexts.put(group.getName(), new HashMap());
				}
			}
		}
	}


}
