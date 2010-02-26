/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: Barcode4JApp.java 3348 2010-02-01 12:35:35Z teodord $
 */
public abstract class AbstractSampleApp
{


	/**
	 *
	 */
	public abstract String usage();

	
	/**
	 *
	 */
	public abstract void test() throws JRException;

	
	/**
	 *
	 */
	public void executeTask(String taskName)
	{
		try
		{
			Method method = getClass().getMethod(taskName, new Class[]{});
			method.invoke(this, new Object[]{});
		}
		catch (InvocationTargetException e)
		{
			e.getCause().printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

	/**
	 *
	 */
	protected File[] getFiles(File parentFile, String extension)
	{
		List fileList = new ArrayList();
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith("." + extension))
			{
				fileList.add(new File(parentFile, reportFile)); 
			}
		}
		return (File[])fileList.toArray(new File[fileList.size()]);
	}
	
	
	/**
	 *
	 */
	public static void main(AbstractSampleApp app, String[] args)
	{
		try
		{
			if(args.length != 1)
			{
				System.out.println(app.usage());
				return;
			}
					
			app.executeTask(args[0]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
