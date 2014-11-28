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
package net.sf.jasperreports.web.commands;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.repo.JasperDesignCache;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ResetInCacheCommand implements Command 
{
	
	private Command command;
	private JasperReportsContext jasperReportsContext;
	private ReportContext reportContext;
	private String uri;
	
	public ResetInCacheCommand(
		Command command, 
		JasperReportsContext jasperReportsContext, 
		ReportContext reportContext, 
		String uri
		) 
	{
		this.command = command;
		this.jasperReportsContext = jasperReportsContext;
		this.reportContext = reportContext;
		this.uri = uri;
	}

	public void execute() throws CommandException
	{
		command.execute();
		
		JasperDesignCache.getInstance(jasperReportsContext, reportContext).resetJasperReport(uri);
	}
	
	public void undo() 
	{
		command.undo();
		
		JasperDesignCache.getInstance(jasperReportsContext, reportContext).resetJasperReport(uri);
	}

	public void redo() 
	{
		command.redo();
		
		JasperDesignCache.getInstance(jasperReportsContext, reportContext).resetJasperReport(uri);
	}

}
