/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.design;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRJdk12Compiler extends JRAbstractJavaCompiler
{


	/**
	 *
	 */
	public String compileClass(File sourceFile, String classpath) throws JRException
	{
		String[] source = new String[3];
		source[0] = sourceFile.getPath();
		source[1] = "-classpath";
		source[2] = classpath;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try 
		{
			Class javacClass = JRClassLoader.loadClassForName("sun.tools.javac.Main");
			Constructor constructor = javacClass.getConstructor(new Class[] {OutputStream.class, String.class});
			Method compileMethod = javacClass.getMethod("compile", new Class[] {String[].class});
			Object javac = constructor.newInstance(new Object[] {baos, source[0]});

			compileMethod.invoke(javac, new Object[] {source});
		}
	 	catch (Exception e)
		{
			throw new JRException("Error compiling report java source file : " + sourceFile, e);
		}

		if( baos.toString().indexOf("error") != -1 )
		{
			return baos.toString();
		}
		
		return null;
	}


}
