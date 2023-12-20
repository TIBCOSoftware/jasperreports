/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractSampleApp
{
	/**
	 *
	 */
	protected String usage()
	{
		StringBuilder sb = new StringBuilder();
		
		String appName = this.getClass().getName(); 
		
		sb.append(appName + " usage:" + "\n\tjava " + appName + " task" + "\n\tTasks : ");
		
		TreeSet<String> tasks = new TreeSet<>();
		Method[] methods = getClass().getMethods();
		for (Method method:methods)
		{
			if (
				method.getDeclaringClass().getName().endsWith("App")
				&& ((method.getModifiers() & Modifier.STATIC) == 0)
				&& ((method.getModifiers() & Modifier.PUBLIC) == 1)
				)
			{
				tasks.add(method.getName());
			}
		}
		for (String task:tasks)
		{
			sb.append(task).append(" | ");
		}
		
		return sb.toString().substring(0, sb.length() - 3);
	}

	
	/**
	 *
	 */
	protected void executeTask(String taskName)
	{
		try
		{
			Method method = getClass().getMethod(taskName, new Class[]{});
			method.invoke(this, new Object[]{});
		}
		catch (NoSuchMethodException e)
		{
			System.out.println(usage());
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.getCause().printStackTrace();
		}
	}
	

	/**
	 *
	 */
	protected File[] getFiles(File parentFile, String extension)
	{
		List<File> fileList = new ArrayList<>();
		String[] files = parentFile.list();
		if (files != null)
		{
			for(int i = 0; i < files.length; i++)
			{
				String reportFile = files[i];
				if (reportFile.endsWith("." + extension))
				{
					fileList.add(new File(parentFile, reportFile)); 
				}
			}
		}
		return fileList.toArray(new File[fileList.size()]);
	}
	
	
	/**
	 *
	 */
	protected Connection getDemoHsqldbConnection() throws JRException
	{
		Connection conn;

		try
		{
			//Change these settings according to your local configuration
			String driver = "org.hsqldb.jdbcDriver";
			String connectString = "jdbc:hsqldb:hsql://localhost";
			String user = "sa";
			String password = "";


			Class.forName(driver);
			conn = DriverManager.getConnection(connectString, user, password);
		}
		catch (ClassNotFoundException | SQLException e)
		{
			throw new JRException(e);
			
		}

		return conn;
	}

	
	/**
	 *
	 */
	public static void main(AbstractSampleApp app, String[] args)
	{
		try
		{
			if (args.length > 0)
			{
				for (String arg : args)
				{
					app.executeTask(arg);
				}
			}
			else
			{
				System.out.println(app.usage());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 *
	 */
	public abstract void test() throws JRException;


	/**
	 *
	 */
	public void compile() throws JRException
	{
		File[] files = getFiles(new File("reports"), "jrxml");
		if (files.length > 0)
		{
			File destFileParent = new File("target/reports");
			if (!destFileParent.exists())
			{
				destFileParent.mkdirs();
			}

			System.out.println("Compiling " + files.length + " report design files."); // deliberately using System.out.println instead of logging in the sample apps

			for (int i = 0; i < files.length; i++)
			{
				File srcFile = files[i];
				String srcFileName = srcFile.getName();
				String destFileName = srcFileName.substring(0, srcFileName.lastIndexOf(".jrxml")) + ".jasper";

				System.out.print("Compiling: " + srcFileName + " ... ");

				JasperCompileManager.compileReportToFile(
					srcFile.getAbsolutePath(),
					new File(destFileParent, destFileName).getAbsolutePath()
					);

				System.out.println("OK.");
			}
		}
		else
		{
			System.out.println("No report design files found to compile.");
		}
	}


	/**
	 *
	 */
	public void decompile() throws JRException
	{
		File[] files = getFiles(new File("target/reports"), "jasper");
		if (files.length > 0)
		{
			File destFileParent = new File("target/reports");
			if (!destFileParent.exists())
			{
				destFileParent.mkdirs();
			}

			System.out.println("Decompiling " + files.length + " report design files.");

			for (int i = 0; i < files.length; i++)
			{
				File srcFile = files[i];
				String srcFileName = srcFile.getName();
				String destFileName = srcFileName + ".jrxml";

				System.out.print("Decompiling: " + srcFileName + " ... ");

				new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(
					(JasperReport)JRLoader.loadObjectFromFile(srcFile.getAbsolutePath()), 
					new File(destFileParent, destFileName).getAbsolutePath(), 
					"UTF-8"
					);

				System.out.println("OK.");
			}
		}
		else
		{
			System.out.println("No report design files found to decompile.");
		}
	}


	/**
	 *
	 */
	public void writeApi() throws JRException
	{
		File[] files = getFiles(new File("target/reports"), "jasper");
		if (files.length > 0)
		{
			File destFileParent = new File("target/reports");
			if (!destFileParent.exists())
			{
				destFileParent.mkdirs();
			}

			System.out.println("Writing API for " + files.length + " report design files.");

			for (int i = 0; i < files.length; i++)
			{
				File srcFile = files[i];
				String srcFileName = srcFile.getName();
				String destFileName = srcFileName.substring(0, srcFileName.lastIndexOf(".jasper")) + ".java";

				System.out.print("Writing API for: " + srcFileName + " ... ");

				JRReport report = (JRReport)JRLoader.loadObjectFromFile(srcFile.getAbsolutePath());

				new JRApiWriter(DefaultJasperReportsContext.getInstance()).write(
					report, 
					new File(destFileParent, destFileName).getAbsolutePath()
					);
				
				System.out.println("OK.");
			}
		}
		else
		{
			System.out.println("No report design files found to write API for.");
		}
	}


	/**
	 *
	 */
	public void writeApiXml() throws JRException
	{
		File[] files = getFiles(new File("target/reports"), "jasper");
		if (files.length > 0)
		{
			File destFileParent = new File("target/reports");
			if (!destFileParent.exists())
			{
				destFileParent.mkdirs();
			}

			System.out.println("Running " + files.length + " API report design files.");

			for (int i = 0; i < files.length; i++)
			{
				File srcFile = files[i];
				String srcFileName = srcFile.getName();
				String srcClassName = srcFileName.substring(0, srcFileName.lastIndexOf(".jasper"));
				String destFileName = srcFileName.substring(0, srcFileName.lastIndexOf(".jasper")) + ".api.jrxml";

				System.out.print("Running: " + srcFileName + " ... ");

				try
				{
					Class<?> reportCreatorClass = JRClassLoader.loadClassForName(srcClassName);
					ReportCreator reportCreator = (ReportCreator)reportCreatorClass.getDeclaredConstructor().newInstance();
					JasperDesign jasperDesign = reportCreator.create();
					new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(
						jasperDesign, 
						new File(destFileParent, destFileName).getAbsolutePath(), 
						"UTF-8"
						);

					System.out.println("OK.");
				}
				catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e)
				{
					throw new JRException(e);
				}
			}
		}
		else
		{
			System.out.println("No API report design files found to run.");
		}
	}
}
