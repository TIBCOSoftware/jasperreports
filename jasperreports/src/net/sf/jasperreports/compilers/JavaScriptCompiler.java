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
package net.sf.jasperreports.compilers;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRAbstractCompiler;
import net.sf.jasperreports.engine.design.JRCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRSourceCompileTask;
import net.sf.jasperreports.engine.fill.JREvaluator;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EvaluatorException;

/**
 * Compiler for reports that use JavaScript as expression language.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JavaScriptCompiler extends JRAbstractCompiler
{

	/**
	 * Creates a JavaScript compiler.
	 */
	public JavaScriptCompiler()
	{
		super(false);
	}

	protected void checkLanguage(String language) throws JRException
	{
		//NOOP
	}

	protected String compileUnits(JRCompilationUnit[] units, String classpath,
			File tempDirFile) throws JRException
	{
		Context context = ContextFactory.getGlobal().enterContext();
		try
		{
			StringBuffer errors = new StringBuffer();
			int errorCount = 0;
			for (int i = 0; i < units.length; i++)
			{
				JRCompilationUnit unit = units[i];
				JavaScriptCompileData compileData = new JavaScriptCompileData();
				for (Iterator<JRExpression> it = unit.getExpressions().iterator(); it.hasNext();)
				{
					JRExpression expr = it.next();
					int id = unit.getCompileTask().getExpressionId(expr).intValue();
					JavaScriptCompileData.Expression jsExpr = 
						JavaScriptEvaluator.createJSExpression(expr);
					
					//compile the default expression to catch syntax errors
					try
					{
						context.compileString(jsExpr.getDefaultExpression(), 
								"expression", 0, null);
					}
					catch (EvaluatorException e)
					{
						++errorCount;
						appendError(errors, errorCount, e);
					}
					
					compileData.addExpression(id, jsExpr);
				}
				unit.setCompileData(compileData);
			}
			
			String errorsMessage = null;
			if (errorCount > 0)
			{
				errorsMessage = errorCount + " error(s):\n" + errors;
			}
			return errorsMessage;
		}
		finally
		{
			Context.exit();
		}
	}

	protected void appendError(StringBuffer errors, int errorCount,
			EvaluatorException e)
	{
		errors.append(errorCount);
		errors.append(". ");
		String message = e.getMessage();
		errors.append(message);
		errors.append(" at column ");
		errors.append(e.columnNumber());
		String lineSource = e.lineSource();
		if (lineSource != null)
		{
			errors.append(" in line\n");
			errors.append(lineSource);
		}
		errors.append("\n");
	}

	protected JRCompilationSourceCode generateSourceCode(
			JRSourceCompileTask sourceTask) throws JRException
	{
		//no source code
		return null;
	}

	protected String getSourceFileName(String unitName)
	{
		return unitName + ".js";
	}

	protected JREvaluator loadEvaluator(Serializable compileData,
			String unitName) throws JRException
	{
		if (!(compileData instanceof JavaScriptCompileData))
		{
			throw new JRException("Invalid compile data, should be an instance of " 
					+ JavaScriptCompileData.class.getName());
		}
		
		JavaScriptCompileData jsCompileData = (JavaScriptCompileData) compileData;
		return new JavaScriptEvaluator(jsCompileData);
	}

}
