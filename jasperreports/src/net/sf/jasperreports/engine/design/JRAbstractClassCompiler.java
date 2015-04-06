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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRAbstractClassCompiler extends JRAbstractJavaCompiler implements JRMultiClassCompiler
{
	/**
	 * 
	 */
	protected JRAbstractClassCompiler(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext, true);
	}


	/**
	 * @deprecated Replaced by {@link #JRAbstractClassCompiler(JasperReportsContext)}.
	 */
	protected JRAbstractClassCompiler()
	{
		this(DefaultJasperReportsContext.getInstance());
	}


	protected String compileUnits(JRCompilationUnit[] units, String classpath, File tempDirFile) throws JRException
	{
		File[] sources = new File[units.length];
		for (int i = 0; i < sources.length; i++)
		{
			sources[i] = units[i].getSourceFile();
		}
		
		File[] classFiles = new File[units.length];
		for (int i = 0; i < classFiles.length; i++)
		{
			classFiles[i] = new File(tempDirFile, units[i].getName() + ".class");
		}
		
		try
		{
			String errors = compileClasses(sources, classpath);

			if (errors == null)
			{
				for (int i = 0; i < units.length; i++)
				{
					byte[] classBytes = JRLoader.loadBytes(classFiles[i]);
					units[i].setCompileData(classBytes);
				}
			}
			
			return errors;
		}
		finally
		{
			for (int i = 0; i < classFiles.length; i++)
			{
				if (classFiles[i].exists())
				{
					classFiles[i].delete();
				}
			}
		}
	}


	protected void checkLanguage(String language) throws JRException
	{		
		if (!JRReport.LANGUAGE_JAVA.equals(language))
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_EXPECTED_JAVA_LANGUAGE,
					new Object[]{language, JRReport.LANGUAGE_JAVA});
		}
	}

	
	protected JRCompilationSourceCode generateSourceCode(JRSourceCompileTask sourceTask) throws JRException
	{
		return JRClassGenerator.generateClass(sourceTask);
	}


	protected String getSourceFileName(String unitName)
	{
		return unitName + ".java";
	}
}
