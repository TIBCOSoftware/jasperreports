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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractClassCompiler extends JRAbstractJavaCompiler implements JRClassCompiler
{


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

		//Report design OK

		//Generating expressions class source code
		String sourceCode = JRClassGenerator.generateClass(jasperDesign);

		boolean isKeepJavaFile = 
			Boolean.valueOf(
				System.getProperty("jasper.reports.compile.keep.java.file")
				).booleanValue();

		String tempDirStr = System.getProperty("jasper.reports.compile.temp");
		if (tempDirStr == null || tempDirStr.length() == 0)
		{
			tempDirStr = System.getProperty("user.dir");
		}

		File tempDirFile = new File(tempDirStr);
		if (!tempDirFile.exists() || !tempDirFile.isDirectory())
		{
			throw new JRException("Temporary directory not found : " + tempDirStr);
		}
	
		File javaFile = new File(tempDirFile, jasperDesign.getName() + ".java");
		File classFile = new File(tempDirFile, jasperDesign.getName() + ".class");

		//Creating expression class source file
		JRSaver.saveClassSource(sourceCode, javaFile);

		String classpath = System.getProperty("jasper.reports.compile.class.path");
		if (classpath == null || classpath.length() == 0)
		{
			classpath = System.getProperty("java.class.path");
		}

		try
		{
			//Compiling expression class source file
			String compileErrors = compileClass(javaFile, classpath);
			if (compileErrors != null)
			{
				throw new JRException("Errors were encountered when compiling report expressions class file:\n" + compileErrors);
			}

			//Reading class byte codes from compiled class file
			jasperReport = 
				new JasperReport(
					jasperDesign,
					getClass().getName(),
					JRLoader.loadBytes(classFile)
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
		finally
		{
			if (!isKeepJavaFile)
			{
				javaFile.delete();
			}
			classFile.delete();
		}

		return jasperReport;
	}

	
}
