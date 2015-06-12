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
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * Base class for multiple class compilers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class JRAbstractMultiClassCompiler extends JRAbstractClassCompiler
{
	public static final String EXCEPTION_MESSAGE_KEY_JAVA_SOURCE_COMPILE_ERROR = "compilers.java.source.compile.error";

	/**
	 * 
	 */
	public JRAbstractMultiClassCompiler(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}

	/**
	 * @deprecated Replaced by {@link #JRAbstractMultiClassCompiler(JasperReportsContext)}.
	 */
	public JRAbstractMultiClassCompiler()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	public String compileClass(File sourceFile, String classpath) throws JRException
	{
		return compileClasses(new File[]{sourceFile}, classpath);
	}

}
