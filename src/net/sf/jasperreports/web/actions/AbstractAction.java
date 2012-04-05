/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.web.actions;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.web.commands.CommandStack;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="actionName")
public abstract class AbstractAction implements Action {
	
	public static final String PARAM_COMMAND_STACK = "net.sf.jasperreports.command.stack";
	
	private JasperReportsContext jasperReportsContext;
	private ReportContext reportContext;
//	private String reportUri;
//	private JasperDesign jasperDesign;
	private CommandStack commandStack;
	
	public AbstractAction(){
	}
	
	public void init(JasperReportsContext jasperReportsContext, ReportContext reportContext)//, String reportUri) 
	{
		this.jasperReportsContext = jasperReportsContext;
		this.reportContext = reportContext;
//		this.reportUri = reportUri;
		
//		jasperDesign = JasperDesignCache.getInstance(reportContext).getJasperDesign(reportUri);
		commandStack = (CommandStack)reportContext.getParameterValue(PARAM_COMMAND_STACK);
		
		if (commandStack == null) {
			commandStack = new CommandStack();
			reportContext.setParameterValue(PARAM_COMMAND_STACK, commandStack);
		}
	}
	
//	public JasperDesign getJasperDesign() {
//		return jasperDesign;
//	}
	
	public JasperReportsContext getJasperReportsContext() {
		return jasperReportsContext;
	}
	
	public ReportContext getReportContext() {
		return reportContext;
	}
	
	public void run() throws ActionException {
		performAction();
		//resetJasperReport();
	}
	
//	public void resetJasperReport() {
//		JasperDesignCache.getInstance(reportContext).set(reportUri, jasperDesign);
//	}
	
	public CommandStack getCommandStack() {
		return commandStack;
	}
	
	public abstract void performAction() throws ActionException;
	
}
