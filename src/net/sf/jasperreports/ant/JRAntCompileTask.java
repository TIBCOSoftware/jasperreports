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

/*
 * Contributors:
 * Henri Chen - henrichen@users.sourceforge.net
 * Kees Kuip  - keeskuip@users.sourceforge.net
 */
package net.sf.jasperreports.ant;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.RegexpPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;


/**
 * Ant task for batch-compiling XML report design files.
 * Works like the built-in <code>javac</code> Ant task.
 * <p>
 * This task can take the following arguments:
 * <ul>
 * <li>src
 * <li>destdir
 * <li>compiler
 * <li>classpath
 * <li>tempdir
 * <li>keepjava
 * <li>xmlvalidation
 * </ul>
 * Of these arguments, the <code>src</code> and <code>destdir</code> are required.
 * When this task executes, it will recursively scan the <code>src</code> and 
 * <code>destdir</code> looking for XML report design files to compile. 
 * This task makes its compile decision based on timestamp and only XML files 
 * that have no corresponding .jasper file or where the compiled report design file 
 * is older than the XML file will be compiled.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRAntCompileTask extends MatchingTask
{


	/**
	 *
	 */
	private Path src = null;
	private File destdir = null;
	private File tempdir = null;
	private boolean keepjava = false;
	private String compiler = null;
	private Path classpath = null;
	private boolean xmlvalidation = true;

	private Map reportFilesMap = null;


	/**
	 * Sets the source directories to find the XML report design files.
	 * 
	 * @param srcdir source path
	 */
	public void setSrcdir(Path srcdir)
	{
		if (src == null) 
		{
			src = srcdir;
		}
		else
		{
			src.append(srcdir);
		}
	}


	/**
	 * Adds a path for source compilation.
	 * 
	 * @return source path
	 */
	public Path createSrc()
	{
		if (src == null)
		{
			src = new Path(getProject());
		}
		
		return src.createPath();
	}
	
	
	/**
	 * Sets the destination directory into which the XML report design files should be compiled.
	 * 
	 * @param destdir destination directory
	 */
	public void setDestdir(File destdir)
	{
		this.destdir = destdir;
	}


	/**
	 * Sets the temporary working directory into which to store the temporary files 
	 * generated during XML report design file compilation. This is only used by the
	 * Java bytecode report compilers that need to have the Java source files stored 
	 * on disk in order to compile them.
	 * <p>
	 * If not set, the temporary working directory will be the current working directory,
	 * as specified by the <code>user.dir</code> system property.
	 * 
	 * @param tempdir temporary working directory
	 */
	public void setTempdir(File tempdir)
	{
		this.tempdir = tempdir;
	}


	/**
	 * Sets a boolean flag that will instruct the Java bytecode report compilers
	 * to avoid deletion of the Java source files generated in the temporary working 
	 * directory during report generation. This is useful when debugging.
	 * 
	 * @param keepjava flag for preventing the deletion of generated Java source files
	 */
	public void setKeepjava(boolean keepjava)
	{
		this.keepjava = keepjava;
	}


	/**
	 * Sets the name of the report compiler class to use when compiling the XML
	 * report design files.
	 * <p>
	 * The specified class should be an implementation of the 
	 * {@link net.sf.jasperreports.engine.design.JRCompiler} interface.
	 * When specified, this value will temporarily override the value of the
	 * <code>jasper.reports.compiler.class</code> system property which in turn 
	 * is used by the {@link net.sf.jasperreports.engine.design.JRDefaultCompiler}.
	 * 
	 * @param compiler report compiler class name
	 */
	public void setCompiler(String compiler)
	{
		this.compiler = compiler;
	}


	/**
	 * Adds a path to the classpath.
	 * 
	 * @return classpath to use when compiling the report associated Java expressions class
	 */
	public Path createClasspath()
	{
		if (classpath == null)
		{
			classpath = new Path(getProject());
		}
		
		return classpath.createPath();
	}
	
	
	/**
	 * Instructs the XML parser to validate the XML report design file during compilation. 
	 * 
	 * @param xmlvalidation flag for enabling/disabling the validation feature of the XML parser 
	 */
	public void setXmlvalidation(boolean xmlvalidation)
	{
		this.xmlvalidation = xmlvalidation;
	}


	/**
	 * Executes the task.
	 */
	public void execute() throws BuildException
	{
		checkParameters();

		reportFilesMap = new HashMap();
		
		String oldTempdir = System.getProperty("jasper.reports.compile.temp");
		String oldKeepjava = System.getProperty("jasper.reports.compile.keep.java.file");
		String oldCompiler = System.getProperty("jasper.reports.compiler.class");
		String oldClasspath = System.getProperty("jasper.reports.compile.class.path");
		String oldXmlvalidation = System.getProperty("jasper.reports.compile.xml.validation");
		
		if (tempdir != null)
			System.setProperty("jasper.reports.compile.temp", String.valueOf(tempdir));

		System.setProperty("jasper.reports.compile.keep.java.file", String.valueOf(keepjava));

		if (compiler != null)
			System.setProperty("jasper.reports.compiler.class", compiler);

		if (classpath != null)
			System.setProperty("jasper.reports.compile.class.path", String.valueOf(classpath));

		System.setProperty("jasper.reports.compile.xml.validation", String.valueOf(xmlvalidation));

		/*   */
		scanSrc();
		
		/*   */
		compile();

		if (oldTempdir != null)
			System.setProperty("jasper.reports.compile.temp", oldTempdir);

		if (oldKeepjava != null)
			System.setProperty("jasper.reports.compile.keep.java.file", oldKeepjava);

		if (oldCompiler != null)
			System.setProperty("jasper.reports.compiler.class", oldCompiler);

		if (oldClasspath != null)
			System.setProperty("jasper.reports.compile.class.path", oldClasspath);

		if (oldXmlvalidation != null)
			System.setProperty("jasper.reports.compile.xml.validation", oldXmlvalidation);
	}
	
	
	/**
	 * Checks that all required attributes have been set and that the supplied values are valid. 
	 */
	protected void checkParameters() throws BuildException 
	{
		if (src == null || src.size() == 0)
		{
			throw 
				new BuildException(
					"The srcdir attribute must be set.", 
					location
					);
		}
		
		if (destdir != null && !destdir.isDirectory()) 
		{
			throw 
				new BuildException(
					"The destination directory \"" 
						+ destdir 
						+ "\" does not exist "
						+ "or is not a directory.", 
					location
					);
		}

		if (tempdir != null && !tempdir.isDirectory()) 
		{
			throw 
				new BuildException(
					"The temporary directory \"" 
						+ tempdir 
						+ "\" does not exist "
						+ "or is not a directory.", 
					location
					);
		}
	}
	
	
	/**
	 * Scans the source directories looking for source files to be compiled. 
	 */
	protected void scanSrc() throws BuildException
	{
		String[] list = src.list();
		for (int i = 0; i < list.length; i++) 
		{
			File srcdir = project.resolveFile(list[i]);
			if (!srcdir.exists()) 
			{
				throw 
					new BuildException(
						"The srcdir \""
							+ srcdir.getPath() 
							+ "\" does not exist.", 
						location
						);
			}
			else
			{
				if (srcdir.isDirectory())
				{
					DirectoryScanner ds = getDirectoryScanner(srcdir);
					String[] files = ds.getIncludedFiles();
					
					scanDir(srcdir, destdir != null ? destdir : srcdir, files);
				}
				else
				{
					String[] files = new String[]{srcdir.getName()};
					
					scanDir(srcdir.getParentFile(), destdir != null ? destdir : srcdir.getParentFile(), files);
				}
			}
		}
	}
	
	
	/**
	 * Scans the directory looking for source files to be compiled. 
	 * The results are returned in the instance variable <code>reportFilesMap</code>.
	 * 
	 * @param srcdir source directory
	 * @param destdir destination directory
	 * @param files included file names
	 */
	protected void scanDir(File srcdir, File destdir, String[] files) 
	{
		RegexpPatternMapper mapper = new RegexpPatternMapper();
		mapper.setFrom("^(.*)\\.(.*)$");
		mapper.setTo("\\1.jasper");

		SourceFileScanner scanner = new SourceFileScanner(this);
		String[] newFiles = scanner.restrict(files, srcdir, destdir, mapper);
		
		if (newFiles != null && newFiles.length > 0) 
		{
			for (int i = 0; i < newFiles.length; i++)
			{
				reportFilesMap.put(
					(new File(srcdir, newFiles[i])).getAbsolutePath(), 
					(new File(destdir, mapper.mapFileName(newFiles[i])[0])).getAbsolutePath()
					);
			}
		}
	}
	
	
	/**
	 * Performs the compilation of the selected report design files.
	 */
	protected void compile() throws BuildException
	{
		Collection files = reportFilesMap.keySet();

		if (files != null && files.size() > 0)
		{
			boolean isError = false;
		
			System.out.println("Compiling " + files.size() + " report design files.");

			String srcFileName = null;
			String destFileName = null;
			File destFileParent = null;

			for (Iterator it = files.iterator(); it.hasNext();)
			{
				srcFileName = (String)it.next();
				destFileName = (String)reportFilesMap.get(srcFileName);
				destFileParent = new File(destFileName).getParentFile();
				if(!destFileParent.exists())
				{
					destFileParent.mkdirs();
				}

				try
				{
					System.out.print("File : " + srcFileName + " ... ");
					JasperCompileManager.compileReportToFile(srcFileName, destFileName);
					System.out.println("OK.");
				}
				catch(JRException e)
				{
					System.out.println("FAILED.");
					System.out.println("Error compiling report design : " + srcFileName);
					e.printStackTrace(System.out);
					isError = true;
				}
			}
		
			if(isError)
			{
				throw new BuildException("Errors were encountered when compiling report designs.");
			}
		}
	}
	
	
}
