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
