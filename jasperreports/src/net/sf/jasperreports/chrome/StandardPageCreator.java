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

import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.types.ChromeTab;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class StandardPageCreator implements PageCreator
{
	
	private static final Log log = LogFactory.getLog(StandardPageCreator.class);
	
	private static final StandardPageCreator INSTANCE = new StandardPageCreator();
	
	public static StandardPageCreator instance()
	{
		return INSTANCE;
	}

	@Override
	public <T> T runInPage(ChromeInstanceHandle chromeInstance, Function<ChromeDevToolsService, T> tabAction)
	{
		return chromeInstance.runWithChromeInstance(chromeService ->
		{
			ChromeTab tab = null;
			try
			{
				tab = chromeService.createTab();
				if (log.isDebugEnabled())
				{
					log.debug("created tab " + tab.getId());
				}
				
				try(ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab))
				{
					return tabAction.apply(devToolsService);
				}
			}
			finally
			{
				if (tab != null)
				{
					chromeService.closeTab(tab);
					if (log.isDebugEnabled())
					{
						log.debug("closed tab " + tab.getId());
					}
				}
			}
		});
	}

}
