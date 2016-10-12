/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.phantomjs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.ConcurrentMapping;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ScriptManager
{
	private static final Log log = LogFactory.getLog(ScriptManager.class);
	
	private static final String TEMP_FILE_PREFIX = "jr_script_";
	private static final int COPY_BUFFER_SIZE = 0x4000;
	
	private final File tempFolder;
	private final ConcurrentMapping<String, File> scriptFiles;
	
	public ScriptManager(JasperReportsContext jasperReportsContext)
	{
		String tempPath = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PhantomJS.PROPERTY_PHANTOMJS_TEMPDIR_PATH);
		if (tempPath == null)
		{
			tempPath = System.getProperty("java.io.tmpdir");
		}
		
		this.tempFolder = new File(tempPath);
		if (this.tempFolder.exists() && this.tempFolder.isDirectory())
		{
			log.info("PhantomJS temp folder is " + tempPath);
		}
		else
		{
			log.error("The PhantomJS temp folder " + tempPath + " does not exist.");
		}
		
		this.scriptFiles = new ConcurrentMapping<>(new ConcurrentMapping.Mapper<String, File>()
		{
			@Override
			public File compute(String key)
			{
				return copyScript(key);
			}
		});
	}
	
	public File getTempFolder()
	{
		return tempFolder;
	}

	public String getScriptFilename(String scriptLocation)
	{
		//TODO lucianc use safer keys (classloader)
		File scriptFile = scriptFiles.get(scriptLocation);
		return scriptFile.getName();
	}

	protected File copyScript(String scriptLocation)
	{
		String resourceName = getResourceName(scriptLocation);
		try
		{
			File file = File.createTempFile(TEMP_FILE_PREFIX, "_" + resourceName, tempFolder);
			file.deleteOnExit();//TODO lucianc leak
			
			if (log.isDebugEnabled())
			{
				log.debug("copying " + scriptLocation + " to " + file);
			}
		
			byte[] buf = new byte[COPY_BUFFER_SIZE];
			try (InputStream input = JRLoader.getLocationInputStream(scriptLocation);
					OutputStream output = new FileOutputStream(file))
			{
				int read = 0;
				while ((read = input.read(buf)) > 0)
				{
					output.write(buf, 0, read);
				}
			}
			
			return file;
		}
		catch (IOException | JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	protected String getResourceName(String scriptLocation)
	{
		// location can be both classpath resource and file path
		int slashIndex = scriptLocation.lastIndexOf('/');
		int separatorIndex = scriptLocation.lastIndexOf(File.separator);
		int nameIndex = Math.max(slashIndex, separatorIndex);
		return nameIndex >= 0 ? scriptLocation.substring(nameIndex + 1) : scriptLocation;
	}

	public void dispose()
	{
		for (Iterator<File> fileIt = scriptFiles.currentValues(); fileIt.hasNext();)
		{
			File file = fileIt.next();
			boolean deleted = file.delete();
			if (log.isDebugEnabled())
			{
				log.debug("deleted " + file + ": " + deleted);
			}
		}
		
		scriptFiles.clear();
	}
}
