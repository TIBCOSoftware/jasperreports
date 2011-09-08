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
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCompiler
{


	/**
	 * Prefix for properties that map report compilers to expression languages.
	 * <p/>
	 * Properties having this prefix are used to indicate the JRCompiler implementation to be used when compiling
	 * report designs that rely on the expression language specified as propety suffix.
	 */
	public static final String COMPILER_PREFIX = JRProperties.PROPERTY_PREFIX + "compiler.";

	
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
