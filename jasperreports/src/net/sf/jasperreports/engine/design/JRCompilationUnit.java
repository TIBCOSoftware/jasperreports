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
import java.util.List;

import net.sf.jasperreports.engine.JRExpression;

/**
 * Expression evaluator compilation unit used by report compilers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRCompilationUnit
{
	/**
	 * The name of the unit.
	 */
	private final String name;
	
	/**
	 * The source code generated for the unit.
	 */
	private final JRCompilationSourceCode source;
	
	/**
	 * The file where the source code was saved.
	 */
	private final File sourceFile;
	
	/**
	 * The list of expressions.
	 */
	private final List<JRExpression> expressions;

	private final JRSourceCompileTask compileTask;

	/**
	 * The compilation data used for creating expression evaluators.
	 */
	private Serializable compileData;
	
	
	/**
	 * Creates a compilation unit.
	 * 
	 * @param name the name of the unit
	 * @param sourceCode the source code generated for the unit
	 * @param sourceFile the file where the source code was saved
	 * @param expressions the list of expressions
	 * @param compileTask the compile task for the unit
	 */
	public JRCompilationUnit(String name, JRCompilationSourceCode sourceCode, File sourceFile, 
			List<JRExpression> expressions, JRSourceCompileTask compileTask)
	{
		this.name = name;
		this.source = sourceCode;
		this.sourceFile = sourceFile;
		this.expressions = expressions;
		this.compileTask = compileTask;
	}

	
	/**
	 * Returns the name of the unit.
	 * 
	 * @return the name of the unit
	 */
	public String getName()
	{
		return name;
	}

	
	/**
	 * Returns the source code generated for the unit.
	 * @return the source code generated for the unit
	 */
	public String getSourceCode()
	{
		return source.getCode();
	}

	
	/**
	 * Returns the compilation source code unit.
	 * 
	 * @return the compilation source code
	 */
	public JRCompilationSourceCode getCompilationSource()
	{
		return source;
	}
	
	/**
	 * Returns the file where the source code was saved.
	 * @return the file where the source code was saved
	 */
	public File getSourceFile()
	{
		return sourceFile;
	}
	
	
	/**
	 * Returns the list of expressions.
	 * @return the list of expressions
	 */
	public List<JRExpression> getExpressions()
	{
		return expressions;
	}
	
	
	/**
	 * Sets the compilation data used for creating expression evaluators.
	 * 
	 * @param compileData the compilation data
	 */
	public void setCompileData(Serializable compileData)
	{
		this.compileData = compileData;
	}
	
	
	/**
	 * Returns the compilation data used for creating expression evaluators.
	 * @return the compilation data used for creating expression evaluators
	 */
	public Serializable getCompileData()
	{
		return compileData;
	}
	
	/**
	 * Returns the compile task for the unit.
	 * 
	 * @return the compile task
	 */
	public JRSourceCompileTask getCompileTask()
	{
		return compileTask;
	}
}
