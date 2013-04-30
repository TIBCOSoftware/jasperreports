/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import groovy.lang.MissingMethodException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JasperReportsContextAware;
import net.sf.jasperreports.functions.FunctionsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.MetaClassHelper;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class GroovyEvaluator extends JREvaluator implements JasperReportsContextAware
{

	private static final Log log = LogFactory.getLog(GroovyEvaluator.class);
	
	private FunctionsUtil functionsUtil;
	private Map<GroovyFunctionKey, MetaMethod> functionClasses = new HashMap<GroovyFunctionKey, MetaMethod>();
	
	@Override
	public void setJasperReportsContext(JasperReportsContext context)
	{
		this.functionsUtil = FunctionsUtil.getInstance(context);
	}

	protected Object functionCall(String methodName, Object[] args)
	{
		Class<?>[] argTypes = MetaClassHelper.castArgumentsToClassArray(args);
		GroovyFunctionKey functionKey = new GroovyFunctionKey(methodName, argTypes);
		
		MetaMethod functionMethod = functionClasses.get(functionKey);
		if (functionMethod != null)
		{
			// found cached method
			return functionMethod.doMethodInvoke(null, args);
		}
		
		Class<?> functionClass = functionsUtil.getClass4Function(methodName);
		if (functionClass == null)
		{
			throw new JRRuntimeException("Function " + methodName + " not found");
		}
		
		MetaClass functionMetaClass = DefaultGroovyMethods.getMetaClass(functionClass);
		// searching for a method that applies to the arguments
		functionMethod = functionMetaClass.getStaticMetaMethod(methodName, args);
		if (functionMethod == null)
		{
			throw new MissingMethodException(methodName, functionMetaClass.getTheClass(), args);
		}
		
		if (!functionMethod.isPublic())
		{
			throw new JRRuntimeException("Method " + functionClass + "." + methodName + " should be public");
		}
		
		// check if the method is overloaded
		int overloadCount = 0;
		for (Method method : functionClass.getMethods())
		{
			int modifiers = method.getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && method.getName().equals(methodName))
			{
				++overloadCount;
			}
		}
		
		if (overloadCount > 1)
		{
			if (log.isDebugEnabled())
			{
				log.debug("caching method " + functionMethod + " for key " + functionKey);
			}
			
			// there are overloaded methods, we can't add the method into MetaClass as it would confuse methodMissing.
			// caching for subsequent use
			functionClasses.put(functionKey, functionMethod);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("registering metadata method " + functionMethod + ", current key " + functionKey);
			}
			
			// adding the method to the evaluator MetaClass so that it doesn't go again into methodMissing
			ExpandoMetaClass extendedMetaClass = new ExpandoMetaClass(getClass(), false);
			extendedMetaClass.registerInstanceMethod(functionMethod);
			extendedMetaClass.initialize();
			
			DefaultGroovyMethods.setMetaClass((GroovyObject) this, extendedMetaClass);
		}
		
		// returning what we have to return
		return functionMethod.doMethodInvoke(null, args);
	}

}
