/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
package dori.jasper.ant;

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

import dori.jasper.engine.JRException;
import dori.jasper.engine.JasperCompileManager;


/**
 *
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
	 *
	 */
	public void setSrcdir(Path srcdir)
	{
		if (this.src == null) 
		{
			this.src = srcdir;
		}
		else
		{
			this.src.append(srcdir);
		}
	}


	/**
	 *
	 */
	public Path createSrc()
	{
		if (this.src == null)
		{
			this.src = new Path(this.getProject());
		}
		
		return this.src.createPath();
	}
	
	
	/**
	 *
	 */
	public void setDestdir(File destdir)
	{
		this.destdir = destdir;
	}


	/**
	 *
	 */
	public void setTempdir(File tempdir)
	{
		this.tempdir = tempdir;
	}


	/**
	 *
	 */
	public void setKeepjava(boolean keepjava)
	{
		this.keepjava = keepjava;
	}


	/**
	 *
	 */
	public void setCompiler(String compiler)
	{
		this.compiler = compiler;
	}


	/**
	 *
	 */
	public Path createClasspath()
	{
		if (this.classpath == null)
		{
			this.classpath = new Path(this.getProject());
		}
		
		return this.classpath.createPath();
	}
	
	
	/**
	 *
	 */
	public void setXmlvalidation(boolean xmlvalidation)
	{
		this.xmlvalidation = xmlvalidation;
	}


	/**
	 *
	 */
	public void execute() throws BuildException
	{
		this.checkParameters();

		this.reportFilesMap = new HashMap();
		
		String oldTempdir = System.getProperty("jasper.reports.compile.temp");
		String oldKeepjava = System.getProperty("jasper.reports.compile.keep.java.file");
		String oldCompiler = System.getProperty("jasper.reports.compiler.class");
		String oldClasspath = System.getProperty("jasper.reports.compile.class.path");
		String oldXmlvalidation = System.getProperty("jasper.reports.compile.xml.validation");
		
		if (this.tempdir != null)
			System.setProperty("jasper.reports.compile.temp", String.valueOf(this.tempdir));

		System.setProperty("jasper.reports.compile.keep.java.file", String.valueOf(this.keepjava));

		if (this.compiler != null)
			System.setProperty("jasper.reports.compiler.class", this.compiler);

		if (this.classpath != null)
			System.setProperty("jasper.reports.compile.class.path", String.valueOf(this.classpath));

		System.setProperty("jasper.reports.compile.xml.validation", String.valueOf(this.xmlvalidation));

		/*   */
		this.scanSrc();
		
		/*   */
		this.compile();

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
	 *
	 */
	protected void checkParameters() throws BuildException 
	{
		if (this.src == null || this.src.size() == 0)
		{
			throw 
				new BuildException(
					"srcdir attribute must be set!", 
					this.location
					);
		}
		
		if (this.destdir != null && !this.destdir.isDirectory()) 
		{
			throw 
				new BuildException(
					"destination directory \"" 
						+ this.destdir 
						+ "\" does not exist "
						+ "or is not a directory", 
					this.location
					);
		}

		if (this.tempdir != null && !this.tempdir.isDirectory()) 
		{
			throw 
				new BuildException(
					"temporary directory \"" 
						+ this.tempdir 
						+ "\" does not exist "
						+ "or is not a directory", 
					this.location
					);
		}
	}
	
	
	/**
	 *
	 */
	protected void scanSrc() throws BuildException
	{
		String[] list = this.src.list();
		for (int i = 0; i < list.length; i++) 
		{
			File srcdir = project.resolveFile(list[i]);
			if (!srcdir.exists()) 
			{
				throw 
					new BuildException(
						"srcdir \""
							+ srcdir.getPath() 
							+ "\" does not exist!", 
						location
						);
			}
			else
			{
				if (srcdir.isDirectory())
				{
					DirectoryScanner ds = this.getDirectoryScanner(srcdir);
					String[] files = ds.getIncludedFiles();
					
					this.scanDir(srcdir, destdir != null ? destdir : srcdir, files);
				}
				else
				{
					String[] files = new String[]{srcdir.getName()};
					
					this.scanDir(srcdir.getParentFile(), destdir != null ? destdir : srcdir.getParentFile(), files);
				}
			}
		}
	}
	
	
	/**
	 *
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
				this.reportFilesMap.put(
					(new File(srcdir, newFiles[i])).getAbsolutePath(), 
					(new File(destdir, mapper.mapFileName(newFiles[i])[0])).getAbsolutePath()
					);
			}
		}
	}
	
	
	/**
	 *
	 */
	protected void compile() throws BuildException
	{
		Collection files = this.reportFilesMap.keySet();

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
				destFileName = (String)this.reportFilesMap.get(srcFileName);
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
