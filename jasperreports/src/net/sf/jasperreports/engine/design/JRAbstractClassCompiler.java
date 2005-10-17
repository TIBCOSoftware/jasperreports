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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.design.crosstab.JRDesignCrosstab;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractClassCompiler extends JRAbstractJavaCompiler implements JRMultiClassCompiler
{


	protected JRAbstractClassCompiler()
	{
		super(true);
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
					"Language \"" + language 
					+ "\" not supported by this report compiler.\n"
					+ "Expecting \"java\" instead."
					);
		}
	}

	
	protected String generateSourceCode(JasperDesign jasperDesign, JRDesignDataset dataset, JRExpressionCollector expressionCollector) throws JRException
	{
		return JRClassGenerator.generateClass(jasperDesign, dataset, expressionCollector);
	}

	
	protected String generateSourceCode(JasperDesign jasperDesign, JRDesignCrosstab crosstab, JRExpressionCollector expressionCollector) throws JRException
	{
		return JRClassGenerator.generateClass(jasperDesign, crosstab, expressionCollector);
	}


	protected String getSourceFileName(String unitName)
	{
		return unitName + ".java";
	}
}
