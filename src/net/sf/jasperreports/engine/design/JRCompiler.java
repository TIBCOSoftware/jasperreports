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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JREvaluator;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCompiler
{


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
