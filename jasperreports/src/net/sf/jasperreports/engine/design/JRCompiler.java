/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JREvaluator;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCompiler
{

	/**
	 * The name of the class to be used for report compilation.
	 * <p>
	 * No default value.
	 * 
	 * @deprecated Replaced by {@link net.sf.jasperreports.engine.design.JRCompiler#COMPILER_PREFIX}.
	 */
	public static final String COMPILER_CLASS = JRPropertiesUtil.PROPERTY_PREFIX + "compiler.class";

	/**
	 * Prefix for properties that map report compilers to expression languages.
	 * <p/>
	 * Properties having this prefix are used to indicate the JRCompiler implementation to be used when compiling
	 * report designs that rely on the expression language specified as property suffix.
	 */
	public static final String COMPILER_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "compiler.";
	
	/**
	 * Whether to keep the java file generated when the report is compiled.
	 * <p>
	 * Defaults to <code>false</code>.
	 */
	public static final String COMPILER_KEEP_JAVA_FILE = JRPropertiesUtil.PROPERTY_PREFIX + "compiler.keep.java.file";
	
	/**
	 * The temporary directory used by the report compiler. 
	 * <p>
	 * Defaults to <code>System.getProperty("user.dir")</code>.
	 */
	public static final String COMPILER_TEMP_DIR = JRPropertiesUtil.PROPERTY_PREFIX + "compiler.temp.dir";
	
	/**
	 * The classpath used by the report compiler. 
	 * <p>
	 * Defaults to <code>System.getProperty("java.class.path")</code>.
	 */
	public static final String COMPILER_CLASSPATH = JRPropertiesUtil.PROPERTY_PREFIX + "compiler.classpath";

	
	/**
	 * Compiles a report design.
	 * <p>
	 * The compilation consists of verification of the design, generation of expression evaluators
	 * and construction of a serializable read-only version of the report.
	 * <p>
	 * A report compiler should usually extend {@link JRAbstractCompiler JRAbstractCompiler}.
	 * 
	 * 
	 * @param jasperDesign the report design
	 * @return the compiled report
	 * @throws JRException
	 */
	public JasperReport compileReport(JasperDesign jasperDesign) throws JRException;


	/**
	 * Loads the evaluator for a report's main dataset.
	 * 
	 * @param jasperReport the report
	 * @return the evaluator for the report's main dataset
	 * @throws JRException
	 */
	public JREvaluator loadEvaluator(JasperReport jasperReport) throws JRException;

	
	/**
	 * Loads a expression evaluator class for a dataset of a report.
	 * 
	 * @param jasperReport the report
	 * @param dataset the dataset
	 * @return an instance of the dataset evaluator class
	 * @throws JRException
	 */
	public JREvaluator loadEvaluator(JasperReport jasperReport, JRDataset dataset) throws JRException;

	
	/**
	 * Loads a expression evaluator class for a crosstab of a report.
	 * 
	 * @param jasperReport the report
	 * @param crosstab the crosstab
	 * @return an instance of the dataset evaluator class
	 * @throws JRException
	 */
	public JREvaluator loadEvaluator(JasperReport jasperReport, JRCrosstab crosstab) throws JRException;

}
