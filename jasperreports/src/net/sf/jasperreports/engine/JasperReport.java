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
package net.sf.jasperreports.engine;

import java.io.Serializable;

import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.base.JRBaseReport;


/**
 * The actual representation of a compiled report. The main difference between a report and a report design is that
 * reports are already compiled and validated, so many characteristics are read only.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperReport extends JRBaseReport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * The name of the compiler class used to compile this report.
	 * The compiler is used to instantiate expression evaluators.
	 */
	private String compilerClass;

	/**
	 * Unique string generated at compile time to distinguish between distinct compilations of reports having the same name.
	 */
	private String compileNameSuffix;
	
	/**
	 * Expression evaluators compiled data.
	 */
	private Serializable compileData;


	/**
	 * Constructs a report by specifying the template report and compile information.
	 * 
	 * @param report the report template
	 * @param compilerClass the name of the class used to compile the report
	 * @param compileData the report/main dataset compile data
	 * @param expressionCollector instance used to collect expressions from the report design
	 * @param compileNameSuffix unique string used to distinguish between distinct compilations of reports having the same name
	 * <p>
	 * The collector is used to fetch the generated expression IDs.
	 */
	public JasperReport(
		JRReport report,
		String compilerClass, 
		Serializable compileData,
		JRExpressionCollector expressionCollector,
		String compileNameSuffix
		)
	{
		super(report, expressionCollector);
		
		this.compilerClass = compilerClass;
		this.compileData = compileData;
		this.compileNameSuffix = compileNameSuffix;
	}

	public JasperReport(
			JRReport report,
			String compilerClass, 
			Serializable compileData,
			JRBaseObjectFactory factory,
			String compileNameSuffix
			)
	{
		super(report, factory);
			
		this.compilerClass = compilerClass;
		this.compileData = compileData;
		this.compileNameSuffix = compileNameSuffix;
	}
	
	/**
	 * Returns the name of the compiler class used to compile this report.
	 * <p>
	 * The compiler is used to instantiate expression evaluators.
	 * 
	 * @return the name of the compiler class used to compile this report
	 */
	public String getCompilerClass()
	{
		return this.compilerClass;
	}


	/**
	 * Returns data resulted from the expression evaluators compilation.
	 * <p>
	 * This data is used to create expression evaluators for report filling.
	 * 
	 * @return expression evaluators compiled data
	 */
	public Serializable getCompileData()
	{
		return this.compileData;
	}

	/**
	 * Returns the suffix of the class/unit names generated at report compilation.
	 * <p>
	 * This is used to distinguish between disctinct compilations of reports having the same name.
	 * 
	 * @return the suffix of the class/unit names generated at report compilation
	 */
	public String getCompileNameSuffix()
	{
		return compileNameSuffix;
	}
}
