/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.chrome;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.launch.ChromeArguments;
import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeService;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ChromeInstance
{
	
	private static final Log log = LogFactory.getLog(ChromeInstance.class);
	
	private static final AtomicLong ID_SEQ = new AtomicLong();
	
	private final long id;
	private LaunchConfiguration configuration;
	
	private ChromeLauncher launcher;
	private ChromeService chromeService;

	public ChromeInstance(LaunchConfiguration configuration)
	{
		this.id = ID_SEQ.incrementAndGet();
		this.configuration = configuration;
	}

	public long getId()
	{
		return id;
	}
	
	public void start()
	{
		launcher = new ChromeLauncher();
		ChromeArguments args = ChromeArgumentsBuilder.instance().toArguments(configuration);
		
		log.info("Launching Chrome instance " + id + " with configuration " + configuration);
		chromeService = launcher.launch(configuration.getExecutablePath(), args);
	}

	public ChromeService getChromeService()
	{
		return chromeService;
	}

}
