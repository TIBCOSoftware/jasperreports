/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JREvaluator;


/**
 * @deprecated Replaced by {@link JasperCompileManager}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class JRDefaultCompiler implements JRCompiler
{


	/**
	 *
	 */
	private static final JRDefaultCompiler instance = new JRDefaultCompiler();

		
	/**
	 *
	 */
	private JRDefaultCompiler()
	{
	}

		
	/**
	 *
	 */
	public static JRDefaultCompiler getInstance()
	{
		return instance;
	}

		
	/**
	 * @deprecated Replaced by {@link JasperCompileManager#compileReport(JasperDesign)}.
	 */
	public JasperReport compileReport(JasperDesign jasperDesign) throws JRException
	{
		return JasperCompileManager.compileReport(jasperDesign);
	}


	/**
	 * @deprecated Replaced by {@link JasperCompileManager#loadEvaluator(JasperReport, JRDataset)}.
	 */
	public JREvaluator loadEvaluator(JasperReport jasperReport, JRDataset dataset) throws JRException
	{
		return JasperCompileManager.loadEvaluator(jasperReport, dataset);
	}


	/**
	 * @deprecated Replaced by {@link JasperCompileManager#loadEvaluator(JasperReport, JRCrosstab)}.
	 */
	public JREvaluator loadEvaluator(JasperReport jasperReport, JRCrosstab crosstab) throws JRException
	{
		return JasperCompileManager.loadEvaluator(jasperReport, crosstab);
	}


	/**
	 * @deprecated Replaced by {@link JasperCompileManager#loadEvaluator(JasperReport)}.
	 */
	public JREvaluator loadEvaluator(JasperReport jasperReport) throws JRException
	{
		return loadEvaluator(jasperReport, jasperReport.getMainDataset());
	}


}
