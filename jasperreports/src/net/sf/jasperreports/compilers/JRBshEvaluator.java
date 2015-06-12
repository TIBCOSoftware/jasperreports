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
package net.sf.jasperreports.compilers;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;


/**
 * BeanShell expression evaluator that compiles expressions at fill time.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @see JRBshCompiler
 */
public class JRBshEvaluator extends JREvaluator
{

	public static final String EXCEPTION_MESSAGE_KEY_INITIALIZING_REPORT_CALCULATOR = "compilers.beanshell.initializing.report.calculator";
	public static final String EXCEPTION_MESSAGE_KEY_EXPRESSIONS_EVALUATING_ERROR = "compilers.beanshell.expressions.evaluating.error";
	public static final String EXCEPTION_MESSAGE_KEY_EXPRESSIONS_TESTING_ERROR = "compilers.beanshell.expressions.testing.error";

	/**
	 *
	 */
	private String bshScript;
	private Interpreter interpreter;


	/**
	 *
	 */
	public JRBshEvaluator(String bshScript) throws JRException
	{
		super();
		
		this.bshScript = bshScript;

		interpreter = new Interpreter();
		
		interpreter.setClassLoader(Thread.currentThread().getContextClassLoader());

		try
		{
			interpreter.eval(new StringReader(bshScript));
		}
		catch(EvalError e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_EXPRESSIONS_EVALUATING_ERROR,
					new Object[]{e.getMessage(), e.getErrorLineNumber(), extractLineContent(e)});
		}
	}


	/**
	 *
	 */
	public <T> void verify(Collection<T> expressions) throws JRException
	{
		try
		{
			interpreter.eval("bshEvaluator = createBshEvaluator()");
			
			if (expressions != null)
			{
				for(Iterator<T> it = expressions.iterator(); it.hasNext();)
				{
					JRExpression expression = (JRExpression)it.next();
					interpreter.eval("bshEvaluator.evaluateOld(" + expression.getId() + ")");
				}
			}
		}
		catch(TargetError te)
		{
			//ignore
		}
		catch(EvalError e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_EXPRESSIONS_TESTING_ERROR,
					new Object[]{e.getMessage(), e.getErrorLineNumber(), extractLineContent(e)});
		}
	}


	/**
	 *
	 */
	protected void customizedInit(
			Map<String, JRFillParameter> pars, 
			Map<String, JRFillField> fldsm, 
			Map<String, JRFillVariable> varsm
		) throws JRException
	{
		try
		{
			interpreter.set("calculator", this);
			interpreter.set("fldsm", fldsm);
			interpreter.set("varsm", varsm);
			interpreter.set("parsm", pars);
			interpreter.eval("bshEvaluator = createBshEvaluator()");
			interpreter.eval("bshEvaluator.init(calculator, parsm, fldsm, varsm)");
		}
		catch(EvalError e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_INITIALIZING_REPORT_CALCULATOR, 
					null, 
					e);
		}
	}


	/**
	 *
	 */
	protected Object evaluateOld(int id) throws Throwable //NOSONAR
	{
		try
		{
			return interpreter.eval("bshEvaluator.evaluateOld(" + id + ")");
		}
		catch(TargetError te)
		{
			throw te.getTarget();
		}
		catch(EvalError ee)
		{
			throw ee;
		}
	}


	/**
	 *
	 */
	protected Object evaluateEstimated(int id) throws Throwable //NOSONAR
	{
		try
		{
			return interpreter.eval("bshEvaluator.evaluateEstimated(" + id + ")");
		}
		catch(TargetError te)
		{
			throw te.getTarget();
		}
		catch(EvalError ee)
		{
			throw ee;
		}
	}


	/**
	 *
	 */
	protected Object evaluate(int id) throws Throwable //NOSONAR
	{
		try
		{
			return interpreter.eval("bshEvaluator.evaluate(" + id + ")");
		}
		catch(TargetError te)
		{
			throw te.getTarget();
		}
		catch(EvalError ee)
		{
			throw ee;
		}
	}

	
	/**
	 * 
	 */
	private String extractLineContent(EvalError e)
	{
		String lineContent = "";

		LineNumberReader reader = null;

		try
		{
			reader = new LineNumberReader(new StringReader(bshScript));
			int lineNumber = e.getErrorLineNumber();
			
			for(int i = 0; i < lineNumber; i++)
			{
				lineContent = reader.readLine();
			}
		}
		catch(IOException ioe)
		{
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch(IOException ioe)
				{
				}
			}
		}
			
		return lineContent;
	}


}
