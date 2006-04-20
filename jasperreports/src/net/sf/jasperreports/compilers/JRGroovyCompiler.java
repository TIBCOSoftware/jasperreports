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

/*
 * Contributors:
 * Peter Severin - peter_p_s@users.sourceforge.net 
 */
package net.sf.jasperreports.compilers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRAbstractJavaCompiler;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRSourceCompileTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * Calculator compiler that uses groovy to compile expressions.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net), Peter Severin (peter_p_s@users.sourceforge.net)
 * @version $Id$
 */
public class JRGroovyCompiler extends JRAbstractJavaCompiler 
{

	/**
	 *  
	 */
	private static final Log log = LogFactory.getLog(JRGroovyCompiler.class);

	public static final String LANGUAGE_GROOVY = "groovy";

	
	public JRGroovyCompiler()
	{
		super(false);
	}
	

	protected String compileUnits(JRCompilationUnit[] units, String classpath, File tempDirFile) throws JRException
	{
		CompilerConfiguration config = new CompilerConfiguration();
		config.setClasspath(classpath);
		CompilationUnit unit = new CompilationUnit(config);
		
		for (int i = 0; i < units.length; i++)
		{
			unit.addSource("calculator_" + units[i].getName(), new ByteArrayInputStream(units[i].getSourceCode().getBytes()));
		}
		
		ClassCollector collector = new ClassCollector();
		unit.setClassgenCallback(collector);
		try 
		{
			unit.compile(Phases.CLASS_GENERATION);
		} 
		catch (CompilationFailedException e) 
		{
			throw new JRException(
				"Errors were encountered when compiling report expressions class file:\n" 
				+ e.toString()
				);
		}

		if (collector.classes.size() < units.length) 
		{
			throw new JRException("Too few groovy class were generated.");
		} 
		else if (collector.classCount > units.length) 
		{
			throw new JRException(
				"Too many groovy classes were generated.\n"
				+ "Please make sure that you don't use Groovy features such as closures that are not supported by this report compiler.\n"
				);
		}
		
		for (int i = 0; i < units.length; i++)
		{
			units[i].setCompileData((Serializable) collector.classes.get(units[i].getName()));
		}
		
		return null;
	}

	
	/**
	 *
	 */
	private static class ClassCollector extends CompilationUnit.ClassgenCallback 
	{
		public Map classes = new HashMap();
		public int classCount;
	
		/**
		 * @see org.codehaus.groovy.control.CompilationUnit.ClassgenCallback#call(
		 *      org.objectweb.asm.ClassVisitor, 
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
		if (!LANGUAGE_GROOVY.equals(language))
		{
			throw 
				new JRException(
					"Language \"" + language 
					+ "\" not supported by this report compiler.\n"
					+ "Expecting \"groovy\" instead."
					);
		}
	}


	protected String generateSourceCode(JRSourceCompileTask sourceTask) throws JRException
	{
		return JRGroovyGenerator.generateClass(sourceTask);
	}


	protected String getSourceFileName(String unitName)
	{
		return unitName + ".groovy";
	}


}