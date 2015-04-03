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
package net.sf.jasperreports.engine.design;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.JRStringUtil;

/**
 * Base class for report compilers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class JRAbstractCompiler implements JRCompiler
{
	public static final String EXCEPTION_MESSAGE_KEY_CROSSTAB_ID_NOT_FOUND = "compilers.crosstab.id.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_DESIGN_COMPILE_ERROR = "compilers.design.compile.error";
	public static final String EXCEPTION_MESSAGE_KEY_LANGUAGE_NOT_SUPPORTED = "compilers.language.not.supported";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_EXPRESSIONS_COMPILE_ERROR = "compilers.report.expressions.compile.error";
	public static final String EXCEPTION_MESSAGE_KEY_TEMP_DIR_NOT_FOUND = "compilers.temp.dir.not.found";
	
	private static final int NAME_SUFFIX_RANDOM_MAX = 1000000;
	private static final Random random = new Random();
	
	protected final JasperReportsContext jasperReportsContext;
	private final boolean needsSourceFiles;

	/**
	 * Constructor.
	 * 
	 * @param needsSourceFiles whether the compiler needs source files or is able to do in memory compilation
	 * <p>
	 * If true, the generated code is saved in source files to be used by the compiler.
	 */
	protected JRAbstractCompiler(JasperReportsContext jasperReportsContext, boolean needsSourceFiles)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.needsSourceFiles = needsSourceFiles;
	}

	
	/**
	 * @deprecated Replaced by {@link #JRAbstractCompiler(JasperReportsContext, boolean)}.
	 */
	protected JRAbstractCompiler(boolean needsSourceFiles)
	{
		this(DefaultJasperReportsContext.getInstance(), needsSourceFiles);
	}

	
	/**
	 * Returns the name of the expression evaluator unit for a dataset of a report.
	 * 
	 * @param report the report
	 * @param dataset the dataset
	 * @return the generated expression evaluator unit name
	 */
	public static String getUnitName(JasperReport report, JRDataset dataset)
	{
		return getUnitName(report, dataset, report.getCompileNameSuffix());
	}

	protected static String getUnitName(JRReport report, JRDataset dataset, String nameSuffix)
	{
		String className;
		if (dataset.isMainDataset())
		{
			className = report.getName();
		}
		else
		{
			className = report.getName() + "_" + dataset.getName();
		}
		
		className = JRStringUtil.getJavaIdentifier(className) + nameSuffix;
		
		return className;
	}
	
	/**
	 * Returns the name of the expression evaluator unit for a crosstab of a report.
	 * 
	 * @param report the report
	 * @param crosstab the crosstab
	 * @return the generated expression evaluator unit name
	 */
	public static String getUnitName(JasperReport report, JRCrosstab crosstab)
	{
		return getUnitName(report, crosstab.getId(), report.getCompileNameSuffix());
	}

	
	protected static String getUnitName(JRReport report, JRCrosstab crosstab, JRExpressionCollector expressionCollector, String nameSuffix)
	{
		Integer crosstabId = expressionCollector.getCrosstabId(crosstab);
		if (crosstabId == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_CROSSTAB_ID_NOT_FOUND,
					(Object[])null);
		}
		
		return getUnitName(report, crosstabId.intValue(), nameSuffix);
	}

	protected static String getUnitName(JRReport report, int crosstabId, String nameSuffix)
	{
		return JRStringUtil.getJavaIdentifier(report.getName()) + "_CROSSTAB" + crosstabId + nameSuffix;
	}
	
	public final JasperReport compileReport(JasperDesign jasperDesign) throws JRException
	{
		// check if the language is supported by the compiler
		checkLanguage(jasperDesign.getLanguage());
		
		// collect all report expressions
		JRExpressionCollector expressionCollector = JRExpressionCollector.collector(jasperReportsContext, jasperDesign);
		
		// verify the report design
		verifyDesign(jasperDesign, expressionCollector);

		String nameSuffix = createNameSuffix();
		
		// check if saving source files is required
		boolean isKeepJavaFile = JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(JRCompiler.COMPILER_KEEP_JAVA_FILE);
		File tempDirFile = null;
		if (isKeepJavaFile || needsSourceFiles)
		{
			String tempDirStr = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(JRCompiler.COMPILER_TEMP_DIR);

			tempDirFile = new File(tempDirStr);
			if (!tempDirFile.exists() || !tempDirFile.isDirectory())
			{
				throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_TEMP_DIR_NOT_FOUND,
					new Object[]{tempDirStr});
			}
		}

		List<JRDataset> datasets = jasperDesign.getDatasetsList();
		List<JRCrosstab> crosstabs = jasperDesign.getCrosstabs();
		
		JRCompilationUnit[] units = new JRCompilationUnit[datasets.size() + crosstabs.size() + 1];
		
		// generating source code for the main report dataset
		units[0] = createCompileUnit(jasperDesign, jasperDesign.getMainDesignDataset(), expressionCollector, tempDirFile, nameSuffix);

		int sourcesCount = 1;
		for (Iterator<JRDataset> it = datasets.iterator(); it.hasNext(); ++sourcesCount)
		{
			JRDesignDataset dataset = (JRDesignDataset) it.next();
			// generating source code for a sub dataset
			units[sourcesCount] = createCompileUnit(jasperDesign, dataset, expressionCollector, tempDirFile, nameSuffix);
		}
		
		for (Iterator<JRCrosstab> it = crosstabs.iterator(); it.hasNext(); ++sourcesCount)
		{
			JRDesignCrosstab crosstab = (JRDesignCrosstab) it.next();
			// generating source code for a sub dataset
			units[sourcesCount] = createCompileUnit(jasperDesign, crosstab, expressionCollector, tempDirFile, nameSuffix);
		}
		
		//TODO component - component compilation units?

		String classpath = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(JRCompiler.COMPILER_CLASSPATH);
		
		try
		{
			// compiling generated sources
			String compileErrors = compileUnits(units, classpath, tempDirFile);
			if (compileErrors != null)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_REPORT_EXPRESSIONS_COMPILE_ERROR,
						new Object[]{compileErrors});
			}

			// creating the report compile data
			JRReportCompileData reportCompileData = new JRReportCompileData();
			reportCompileData.setMainDatasetCompileData(units[0].getCompileData());
			
			for (ListIterator<JRDataset> it = datasets.listIterator(); it.hasNext();)
			{
				JRDesignDataset dataset = (JRDesignDataset) it.next();
				reportCompileData.setDatasetCompileData(dataset, units[it.nextIndex()].getCompileData());
			}
			
			for (ListIterator<JRCrosstab> it = crosstabs.listIterator(); it.hasNext();)
			{
				JRDesignCrosstab crosstab = (JRDesignCrosstab) it.next();
				Integer crosstabId = expressionCollector.getCrosstabId(crosstab);
				reportCompileData.setCrosstabCompileData(crosstabId.intValue(), units[datasets.size() + it.nextIndex()].getCompileData());
			}

			// creating the report
			JasperReport jasperReport = 
				new JasperReport(
					jasperDesign,
					getCompilerClass(),
					reportCompileData,
					expressionCollector,
					nameSuffix
					);
			
			return jasperReport;
		}
		catch (JRException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_DESIGN_COMPILE_ERROR, 
					null, 
					e);
		}
		finally
		{
			if (needsSourceFiles && !isKeepJavaFile)
			{
				deleteSourceFiles(units);
			}
		}
	}


	private static String createNameSuffix()
	{
		return "_" + System.currentTimeMillis() + "_" + random.nextInt(NAME_SUFFIX_RANDOM_MAX);
	}


	protected String getCompilerClass()
	{
		return getClass().getName();
	}

	
	private void verifyDesign(JasperDesign jasperDesign, JRExpressionCollector expressionCollector) throws JRException
	{
		Collection<JRValidationFault> brokenRules = JRVerifier.verifyDesign(jasperReportsContext, jasperDesign, expressionCollector);
		if (brokenRules != null && brokenRules.size() > 0)
		{
			throw new JRValidationException(brokenRules);
		}
	}
	
	private JRCompilationUnit createCompileUnit(JasperDesign jasperDesign, JRDesignDataset dataset, JRExpressionCollector expressionCollector, File saveSourceDir, String nameSuffix) throws JRException
	{		
		String unitName = JRAbstractCompiler.getUnitName(jasperDesign, dataset, nameSuffix);
		
		JRSourceCompileTask sourceTask = new JRSourceCompileTask(jasperDesign, dataset, expressionCollector, unitName);
		JRCompilationSourceCode sourceCode = generateSourceCode(sourceTask);
		
		File sourceFile = getSourceFile(saveSourceDir, unitName, sourceCode);

		return new JRCompilationUnit(unitName, sourceCode, sourceFile, 
				expressionCollector.getExpressions(dataset), sourceTask);
	}
	
	private JRCompilationUnit createCompileUnit(JasperDesign jasperDesign, JRDesignCrosstab crosstab, JRExpressionCollector expressionCollector, File saveSourceDir, String nameSuffix) throws JRException
	{		
		String unitName = JRAbstractCompiler.getUnitName(jasperDesign, crosstab, expressionCollector, nameSuffix);
		
		JRSourceCompileTask sourceTask = new JRSourceCompileTask(jasperDesign, crosstab, expressionCollector, unitName);
		JRCompilationSourceCode sourceCode = generateSourceCode(sourceTask);
		
		File sourceFile = getSourceFile(saveSourceDir, unitName, sourceCode);

		return new JRCompilationUnit(unitName, sourceCode, sourceFile, 
				expressionCollector.getExpressions(crosstab), sourceTask);
	}


	protected File getSourceFile(File saveSourceDir, String unitName, JRCompilationSourceCode sourceCode)
	{
		File sourceFile = null;
		if (saveSourceDir != null && sourceCode != null && sourceCode.getCode() != null)
		{
			String fileName = getSourceFileName(unitName);
			sourceFile = new File(saveSourceDir,  fileName);

			try
			{
				JRSaver.saveClassSource(sourceCode.getCode(), sourceFile);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		return sourceFile;
	}

	private void deleteSourceFiles(JRCompilationUnit[] units)
	{
		for (int i = 0; i < units.length; i++)
		{
			units[i].getSourceFile().delete();
		}
	}

	public JREvaluator loadEvaluator(JasperReport jasperReport) throws JRException
	{
		return loadEvaluator(jasperReport, jasperReport.getMainDataset());
	}

	public JREvaluator loadEvaluator(JasperReport jasperReport, JRDataset dataset) throws JRException
	{
		JRReportCompileData reportCompileData = (JRReportCompileData) jasperReport.getCompileData();
		String unitName = reportCompileData.getUnitName(jasperReport, dataset);
		Serializable compileData = reportCompileData.getDatasetCompileData(dataset);
		return loadEvaluator(compileData, unitName);
	}

	public JREvaluator loadEvaluator(JasperReport jasperReport, JRCrosstab crosstab) throws JRException
	{
		JRReportCompileData reportCompileData = (JRReportCompileData) jasperReport.getCompileData();
		String unitName = reportCompileData.getUnitName(jasperReport, crosstab);
		Serializable compileData = reportCompileData.getCrosstabCompileData(crosstab);
		return loadEvaluator(compileData, unitName);
	}

	
	/**
	 * Creates an expression evaluator instance from data saved when the report was compiled.
	 * 
	 * @param compileData the data saved when the report was compiled
	 * @param unitName the evaluator unit name
	 * @return an expression evaluator instance
	 * @throws JRException
	 */
	protected abstract JREvaluator loadEvaluator(Serializable compileData, String unitName) throws JRException;

	
	/**
	 * Checks that the report language is supported by the compiler.
	 * 
	 * @param language the report language
	 * @throws JRException
	 */
	protected abstract void checkLanguage(String language) throws JRException;

	
	/**
	 * Generates expression evaluator code.
	 *
	 * @param sourceTask the source code generation information
	 * @return generated expression evaluator code
	 * @throws JRException
	 */
	protected abstract JRCompilationSourceCode generateSourceCode(JRSourceCompileTask sourceTask) throws JRException;

	
	/**
	 * Compiles several expression evaluator units.
	 * <p>
	 * The result of the compilation should be set by calling 
	 * {@link JRCompilationUnit#setCompileData(Serializable) setCompileData} on all compile units.
	 * 
	 * @param units the compilation units
	 * @param classpath the compilation classpath
	 * @param tempDirFile temporary directory
	 * @return a string containing compilation errors, or null if the compilation was successfull
	 * @throws JRException
	 */
	protected abstract String compileUnits(JRCompilationUnit[] units, String classpath, File tempDirFile) throws JRException;

	
	/**
	 * Returns the name of the source file where generated source code for an unit is saved.
	 * <p>
	 * If the compiler needs source files for compilation
	 * or {@link JRCompiler#COMPILER_KEEP_JAVA_FILE COMPILER_KEEP_JAVA_FILE} is set, the generated source
	 * will be saved in a file having the name returned by this method.
	 * 
	 * @param unitName the unit name
	 * @return the source file name
	 */
	protected abstract String getSourceFileName(String unitName);
}
