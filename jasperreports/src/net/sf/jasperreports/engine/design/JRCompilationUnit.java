/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
import java.util.Map;

import net.sf.jasperreports.compilers.DirectExpressionEvaluation;
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
	private JRCompilationSourceCode source;
	
	/**
	 * The file where the source code was saved.
	 */
	private File sourceFile;
	
	/**
	 * The list of expressions.
	 * @deprecated in favor of {@link JRSourceCompileTask#getExpressions()}
	 */
	@Deprecated
	private List<JRExpression> expressions;

	private JRSourceCompileTask compileTask;

	/**
	 * The compilation data used for creating expression evaluators.
	 */
	private Serializable compileData;
	
	private Map<Integer, DirectExpressionEvaluation> directEvaluations;
	
	/**
	 * Creates a compilation unit.
	 * 
	 * @param name the name of the unit
	 * @param sourceCode the source code generated for the unit
	 * @param sourceFile the file where the source code was saved
	 * @param expressions the list of expressions
	 * @param compileTask the compile task for the unit
	 * @deprecated in favor of {@link JRCompilationUnit#JRCompilationUnit(String)} and
	 * {@link JRCompilationUnit#setSource(JRCompilationSourceCode, File, JRSourceCompileTask)}.
	 * Expressions are available via {@link JRSourceCompileTask#getExpressions()}
	 */
	@Deprecated
	public JRCompilationUnit(String name, JRCompilationSourceCode sourceCode, File sourceFile, 
			List<JRExpression> expressions, JRSourceCompileTask compileTask)
	{
		this.name = name;
		this.source = sourceCode;
		this.sourceFile = sourceFile;
		this.expressions = expressions;
		this.compileTask = compileTask;
	}

	public JRCompilationUnit(String name)
	{
		this.name = name;
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
	 * @deprecated in favor of {@link JRSourceCompileTask#getExpressions()}
	 */
	@Deprecated
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
	
	public void setSource(JRCompilationSourceCode sourceCode, File sourceFile, 
			JRSourceCompileTask compileTask)
	{
		this.source = sourceCode;
		this.sourceFile = sourceFile;
		this.compileTask = compileTask;
	}
	
	public boolean hasSource()
	{
		return compileTask != null;
	}

	public Map<Integer, DirectExpressionEvaluation> getDirectEvaluations()
	{
		return directEvaluations;
	}

	public void setDirectEvaluations(Map<Integer, DirectExpressionEvaluation> directEvaluations)
	{
		this.directEvaluations = directEvaluations;
	}
}
