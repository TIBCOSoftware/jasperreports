/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRClassLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

/**
 * 
 */
public class JRJdtCompiler extends JRAbstractJavaCompiler
{

	/**
	 *  
	 */
	private static final Log log = LogFactory.getLog(JRJdtCompiler.class);

	
	/**
	 *
	 */
	public JasperReport compileReport(JasperDesign jasperDesign) throws JRException
	{
		JasperReport jasperReport = null;
		
		if (!JRReport.LANGUAGE_JAVA.equals(jasperDesign.getLanguage()))
		{
			throw 
				new JRException(
					"Language \"" + jasperDesign.getLanguage() 
					+ "\" not supported by this report compiler.\n"
					+ "Expecting \"java\" instead."
					);
		}
		
		Collection brokenRules = JRVerifier.verifyDesign(jasperDesign);
		if (brokenRules != null && brokenRules.size() > 0)
		{
			StringBuffer sbuffer = new StringBuffer();
			sbuffer.append("Report design not valid : ");
			int i = 1;
			for(Iterator it = brokenRules.iterator(); it.hasNext(); i++)
			{
				sbuffer.append("\n\t " + i + ". " + (String)it.next());
			}
			throw new JRException(sbuffer.toString());
		}
		else
		{
			//Report design OK

			//Generating expressions class source code
			String sourceCode = JRClassGenerator.generateClass(jasperDesign);

			String classpath = System.getProperty("jasper.reports.compile.class.path");
			if (classpath == null || classpath.length() == 0)
			{
				classpath = System.getProperty("java.class.path");
			}
	
			try
			{
				ClassFile[] classFiles = new ClassFile[1];
				//Compiling expression class source file
				String compileErrors = compileClass(sourceCode, jasperDesign.getName(), classFiles);
				if (compileErrors != null)
				{
					throw new JRException("Errors were encountered when compiling report expressions class file:\n" + compileErrors);
				}
	
				//Reading class byte codes from compiled class file
				jasperReport = 
					new JasperReport(
						jasperDesign,
						JRJavacCompiler.class.getName(),
						classFiles[0].getBytes()
						);
			}
			catch (JRException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new JRException("Error compiling report design.", e);
			}
		}

		return jasperReport;
	}

	
	/**
	 *
	 */
	private String compileClass(final String sourceCode, final String targetClassName, final ClassFile[] classFiles)
	{
		final ClassLoader classLoader = getClassLoader();
		final StringBuffer problemBuffer = new StringBuffer();


		class CompilationUnit implements ICompilationUnit 
		{
			private String sourceCode;
			private String className;

			public CompilationUnit(String sourceCode, String className) 
			{
				this.sourceCode = sourceCode;
				this.className = className;
			}

			public char[] getFileName() 
			{
				return className.toCharArray();
			}

			public char[] getContents() 
			{
				return sourceCode.toCharArray();
			}

			public char[] getMainTypeName() 
			{
				return className.toCharArray();
			}

			public char[][] getPackageName() 
			{
				return new char[0][0];
			}
		}

		
		final INameEnvironment env = new INameEnvironment() 
		{
			public NameEnvironmentAnswer findType(char[][] compoundTypeName) 
			{
				String result = "";
				String sep = "";
				for (int i = 0; i < compoundTypeName.length; i++) {
					result += sep;
					result += new String(compoundTypeName[i]);
					sep = ".";
				}
				return findType(result);
			}

			public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) 
			{
				String result = "";
				String sep = "";
				for (int i = 0; i < packageName.length; i++) {
					result += sep;
					result += new String(packageName[i]);
					sep = ".";
				}
				result += sep;
				result += new String(typeName);
				return findType(result);
			}

			private NameEnvironmentAnswer findType(String className) 
			{
				try 
				{
					if (className.equals(targetClassName)) 
					{
						ICompilationUnit compilationUnit = 
							new CompilationUnit(
								sourceCode, className);
						return new NameEnvironmentAnswer(compilationUnit);
					}
					String resourceName = className.replace('.', '/') + ".class";
					InputStream is = classLoader.getResourceAsStream(resourceName);
					if (is != null) 
					{
						byte[] classBytes;
						byte[] buf = new byte[8192];
						ByteArrayOutputStream baos = new ByteArrayOutputStream(buf.length);
						int count;
						while ((count = is.read(buf, 0, buf.length)) > 0) 
						{
							baos.write(buf, 0, count);
						}
						baos.flush();
						classBytes = baos.toByteArray();
						char[] fileName = className.toCharArray();
						ClassFileReader classFileReader = 
							new ClassFileReader(classBytes, fileName, true);
						return new NameEnvironmentAnswer(classFileReader);
					}
				}
				catch (IOException exc) 
				{
					log.error("Compilation error", exc);
				}
				catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException exc) 
				{
					log.error("Compilation error", exc);
				}
				return null;
			}

			private boolean isPackage(String result) 
			{
				if (result.equals(targetClassName)) 
				{
					return false;
				}
				String resourceName = result.replace('.', '/') + ".class";
				InputStream is = classLoader.getResourceAsStream(resourceName);
				return is == null;
			}

			public boolean isPackage(char[][] parentPackageName, char[] packageName) 
			{
				String result = "";
				String sep = "";
				if (parentPackageName != null) 
				{
					for (int i = 0; i < parentPackageName.length; i++) 
					{
						result += sep;
						String str = new String(parentPackageName[i]);
						result += str;
						sep = ".";
					}
				}
				String str = new String(packageName);
				if (Character.isUpperCase(str.charAt(0))) 
				{
					if (!isPackage(result)) 
					{
						return false;
					}
				}
				result += sep;
				result += str;
				return isPackage(result);
			}

			public void cleanup() 
			{
			}

		};

		final IErrorHandlingPolicy policy = 
			DefaultErrorHandlingPolicies.proceedWithAllProblems();

		final Map settings = new HashMap();
		settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
//        if (ctxt.getOptions().getJavaEncoding() != null) 
//        {
//            settings.put(CompilerOptions.OPTION_Encoding,
//                    ctxt.getOptions().getJavaEncoding());
//        }
//        if (ctxt.getOptions().getClassDebugInfo()) 
//        {
//            settings.put(CompilerOptions.OPTION_LocalVariableAttribute,
//                         CompilerOptions.GENERATE);
//        }

		final IProblemFactory problemFactory = 
			new DefaultProblemFactory(Locale.getDefault());

		final ICompilerRequestor requestor = 
			new ICompilerRequestor() 
			{
				public void acceptResult(CompilationResult result) 
				{
					if (result.hasErrors()) 
					{
						IProblem[] problems = result.getErrors();
						for (int i = 0; i < problems.length; i++) 
						{
							IProblem problem = problems[i];
							//if (problem.isError()) 
							{
								problemBuffer.append(i + 1);
								problemBuffer.append(". ");
								problemBuffer.append(problem.getMessage());

								if (
									problem.getSourceStart() >= 0
									&& problem.getSourceEnd() >= 0
									)
								{
									int problemStartIndex = sourceCode.lastIndexOf("\n", problem.getSourceStart()) + 1;
									int problemEndIndex = sourceCode.indexOf("\n", problem.getSourceEnd());
									if (problemEndIndex < 0)
									{
										problemEndIndex = sourceCode.length();
									}
									
									problemBuffer.append("\n");
									problemBuffer.append(
										sourceCode.substring(
											problemStartIndex,
											problemEndIndex
											)
										);
									problemBuffer.append("\n");
									for(int j = problemStartIndex; j < problem.getSourceStart(); j++)
									{
										problemBuffer.append(" ");
									}
									if (problem.getSourceStart() == problem.getSourceEnd())
									{
										problemBuffer.append("^");
									}
									else
									{
										problemBuffer.append("<");
										for(int j = problem.getSourceStart() + 1; j < problem.getSourceEnd(); j++)
										{
											problemBuffer.append("-");
										}
										problemBuffer.append(">");
									}
								}

								problemBuffer.append("\n");
							}
						}
						problemBuffer.append(problems.length);
						problemBuffer.append(" errors\n");
					}
					if (problemBuffer.length() == 0) 
					{
						ClassFile[] resultClassFiles = result.getClassFiles();
						for (int i = 0; i < resultClassFiles.length; i++) 
						{
							classFiles[0] = resultClassFiles[i];
						}
					}
				}
			};

		ICompilationUnit[] compilationUnits = new ICompilationUnit[1];
		compilationUnits[0] = new CompilationUnit(sourceCode, targetClassName);

		Compiler compiler = 
			new Compiler(env, policy, settings, requestor, problemFactory);
		compiler.compile(compilationUnits);

        if (problemBuffer.length() > 0) 
        {
        	return problemBuffer.toString();
        }

		return null;  
	}

	
	/**
	 *
	 */
	private ClassLoader getClassLoader()
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (classLoader != null)
		{
			try
			{
				Class.forName(JRJdtCompiler.class.getName(), true, Thread.currentThread().getContextClassLoader());
			}
			catch (ClassNotFoundException e)
			{
				classLoader = null;
				//if (log.isWarnEnabled())
				//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRJdtCompiler class. Using JRJdtCompiler.class.getClassLoader() instead.");
			}
		}
	
		if (classLoader == null)
		{
			classLoader = JRClassLoader.class.getClassLoader();
		}

		return classLoader;
	}


}