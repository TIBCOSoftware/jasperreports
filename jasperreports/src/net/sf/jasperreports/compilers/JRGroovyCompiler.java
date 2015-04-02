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

/*
 * Contributors:
 * Peter Severin - peter_p_s@users.sourceforge.net 
 */
package net.sf.jasperreports.compilers;

import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.ClassWriter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRAbstractJavaCompiler;
import net.sf.jasperreports.engine.design.JRCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRDefaultCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRSourceCompileTask;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;

/**
 * Calculator compiler that uses groovy to compile expressions.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net), Peter Severin (peter_p_s@users.sourceforge.net)
 */
public class JRGroovyCompiler extends JRAbstractJavaCompiler 
{

	protected static final String SOURCE_ENCODING = "UTF-8";
	public static final String EXCEPTION_MESSAGE_KEY_COMPILING_EXPRESSIONS_CLASS_FILE = "compilers.compiling.expressions.class.file";
	public static final String EXCEPTION_MESSAGE_KEY_TOO_FEW_CLASSES_GENERATED = "compilers.groovy.too.few.classes.generated";
	public static final String EXCEPTION_MESSAGE_KEY_TOO_MANY_CLASSES_GENERATED = "compilers.groovy.too.many.classes.generated";
	
	/**
	 * 
	 */
	public JRGroovyCompiler(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext, false);
	}
	
	/**
	 * @deprecated Replaced by {@link #JRGroovyCompiler(JasperReportsContext)}.
	 */
	public JRGroovyCompiler()
	{
		this(DefaultJasperReportsContext.getInstance());
	}
	

	protected String compileUnits(JRCompilationUnit[] units, String classpath, File tempDirFile) throws JRException
	{
		CompilerConfiguration config = new CompilerConfiguration();
		config.setSourceEncoding(SOURCE_ENCODING);
		//config.setClasspath(classpath);
		CompilationUnit unit = new CompilationUnit(config);
		
		for (int i = 0; i < units.length; i++)
		{
			try
			{
				byte[] sourceBytes = units[i].getSourceCode().getBytes(SOURCE_ENCODING);
				unit.addSource("calculator_" + units[i].getName(), new ByteArrayInputStream(sourceBytes));
			}
			catch (UnsupportedEncodingException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		
		ClassCollector collector = new ClassCollector();
		unit.setClassgenCallback(collector);
		try 
		{
			unit.compile(Phases.CLASS_GENERATION);
		} 
		catch (CompilationFailedException e) 
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_COMPILING_EXPRESSIONS_CLASS_FILE, 
					new Object[] { e.toString()}, 
					e);
		}

		if (collector.classes.size() < units.length) 
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_TOO_FEW_CLASSES_GENERATED,
					(Object[])null);
		} 
		else if (collector.classCount > units.length) 
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_TOO_MANY_CLASSES_GENERATED,
					(Object[])null);
		}
		
		for (int i = 0; i < units.length; i++)
		{
			units[i].setCompileData(collector.classes.get(units[i].getName()));
		}
		
		return null;
	}

	
	/**
	 *
	 */
	private static class ClassCollector extends CompilationUnit.ClassgenCallback 
	{
		public Map<String, byte[]> classes = new HashMap<String, byte[]>();
		public int classCount;
	
		/**
		 * @see org.codehaus.groovy.control.CompilationUnit.ClassgenCallback#call(
		 *      groovyjarjarasm.asm.ClassVisitor, 
		 *      org.codehaus.groovy.ast.ClassNode)
		 */
		public void call(ClassVisitor writer, ClassNode node) throws CompilationFailedException 
		{
			classCount++;
			String name = node.getName();
			if (!classes.containsKey(name))
			{
				byte[] bytes = ((ClassWriter) writer).toByteArray();
				classes.put(name, bytes);
			}
		}
	}


	protected void checkLanguage(String language) throws JRException
	{
		if (
			!JRReport.LANGUAGE_GROOVY.equals(language)
			&& !JRReport.LANGUAGE_JAVA.equals(language)
			)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_LANGUAGE_NOT_SUPPORTED,
					new Object[]{language, JRReport.LANGUAGE_GROOVY, JRReport.LANGUAGE_JAVA});
		}
	}


	protected JRCompilationSourceCode generateSourceCode(JRSourceCompileTask sourceTask) throws JRException
	{
		return new JRDefaultCompilationSourceCode(JRGroovyGenerator.generateClass(sourceTask), null);
	}


	protected String getSourceFileName(String unitName)
	{
		return unitName + ".groovy";
	}


}