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
package net.sf.jasperreports.compilers;

import java.io.File;
import java.io.Serializable;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRAbstractCompiler;
import net.sf.jasperreports.engine.design.JRCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRDefaultCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRSourceCompileTask;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JREvaluator;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBshCompiler extends JRAbstractCompiler
{
	/**
	 * A constant used to specify that the language used by expressions is BeanShell script.
	 */
	public static final String LANGUAGE_BSH = "bsh";


	/**
	 * 
	 */
	public JRBshCompiler(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext, false);
	}


	/**
	 * @deprecated Replaced by {@link #JRBshCompiler(JasperReportsContext)}.
	 */
	public JRBshCompiler()
	{
		this(DefaultJasperReportsContext.getInstance());
	}


	protected JREvaluator loadEvaluator(Serializable compileData, String unitName) throws JRException
	{
		return new JRBshEvaluator((String) compileData);
	}


	protected void checkLanguage(String language) throws JRException
	{
		if (
			!LANGUAGE_BSH.equals(language)
			&& !JRReport.LANGUAGE_JAVA.equals(language)
			)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_LANGUAGE_NOT_SUPPORTED,
					new Object[]{language, LANGUAGE_BSH, JRReport.LANGUAGE_JAVA});
		}
	}


	protected JRCompilationSourceCode generateSourceCode(JRSourceCompileTask sourceTask) throws JRException
	{
		return new JRDefaultCompilationSourceCode(JRBshGenerator.generateScript(sourceTask), null);
	}


	protected String compileUnits(JRCompilationUnit[] units, String classpath, File tempDirFile) throws JRException
	{
		verifyScripts(units);

		for (int i = 0; i < units.length; i++)
		{
			String script = units[i].getSourceCode();
			units[i].setCompileData(script);
		}		
		
		return null;
	}


	private void verifyScripts(JRCompilationUnit[] units) throws JRException
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		// trick for detecting the Ant class loader
		try
		{
			classLoader.loadClass(JRCalculator.class.getName());
		}
		catch(ClassNotFoundException e)
		{
			classLoader = getClass().getClassLoader();
		}

		ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
		try
		{
			Thread.currentThread().setContextClassLoader(classLoader);

			for (int i = 0; i < units.length; i++)
			{
				String script = units[i].getSourceCode();
				
				JRBshEvaluator bshEvaluator = new JRBshEvaluator(script);
				bshEvaluator.verify(units[i].getExpressions());
			}
		}
		finally
		{
			Thread.currentThread().setContextClassLoader(oldContextClassLoader);
		}
	}


	protected String getSourceFileName(String unitName)
	{
		return unitName + ".bsh";
	}


}
