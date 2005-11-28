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
import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBshEvaluator extends JREvaluator
{


	/**
	 *
	 */
	private String bshScript = null;
	private Interpreter interpreter = null;


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
			throw new JRException(
				"Error evaluating report expressions BeanShell script."
				+ "\nMessage : " + e.getMessage() 
				+ "\nLine " + e.getErrorLineNumber() + " : " + extractLineContent(e) 
				);
		}
	}


	/**
	 *
	 */
	public void verify(Collection expressions) throws JRException
	{
		try
		{
			interpreter.eval("bshEvaluator = createBshEvaluator()");
			
			if (expressions != null)
			{
				for(Iterator it = expressions.iterator(); it.hasNext();)
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
			throw new JRException(
				"Error testing report expressions BeanShell script."
				+ "\nMessage : " + e.getMessage() 
				+ "\nLine " + e.getErrorLineNumber() + " : " + extractLineContent(e) 
				);
		}
	}


	/**
	 *
	 */
	protected void customizedInit(
		Map pars,
		Map fldsm,
		Map varsm
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
			throw new JRException("Error initializing report BeanShell calculator.", e);
		}
	}


	/**
	 *
	 */
	protected Object evaluateOld(int id) throws Throwable
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
	protected Object evaluateEstimated(int id) throws Throwable
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
	protected Object evaluate(int id) throws Throwable
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
