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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.repo.JasperDesignReportResource;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;

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
	private CommandStack commandStack;
	protected ActionErrors errors;
	
	public AbstractAction(){
	}

	public String getMessagesBundle() {
		return "net.sf.jasperreports.web.actions.messages";
	}
	
	public void init(JasperReportsContext jasperReportsContext, ReportContext reportContext)//, String reportUri) 
	{
		this.jasperReportsContext = jasperReportsContext;
		this.reportContext = reportContext;
		commandStack = (CommandStack)reportContext.getParameterValue(PARAM_COMMAND_STACK);
		
		if (commandStack == null) {
			commandStack = new CommandStack();
			reportContext.setParameterValue(PARAM_COMMAND_STACK, commandStack);
		}
		errors = new ActionErrors(MessageUtil.getInstance(jasperReportsContext).getMessageProvider(getMessagesBundle()),
				(Locale) reportContext.getParameterValue(JRParameter.REPORT_LOCALE));
	}
	
	public JasperReportsContext getJasperReportsContext() {
		return jasperReportsContext;
	}
	
	public ReportContext getReportContext() {
		return reportContext;
	}
	
	public void run() throws ActionException {
		performAction();
	}
	
	public CommandStack getCommandStack() {
		return commandStack;
	}

	public void setCommandStack(CommandStack commandStack) {
		this.commandStack = commandStack;
	}
	
	
	public abstract void performAction() throws ActionException;


	public static class ActionErrors {
		
		private MessageProvider messageProvider;
		private Locale locale;
		private List<String> errorMessages;


		public ActionErrors (MessageProvider messageProvider, Locale locale) {
			this.messageProvider = messageProvider;
			this.locale = locale;
			this.errorMessages = new ArrayList<String>();
		}
		
		public void add(String messageKey, Object[] args) {
			errorMessages.add(messageProvider.getMessage(messageKey, args, locale));
		}

		public void add(String messageKey) {
			add(messageKey, null);
		}

		public void addAndThrow(String messageKey, Object[] args) throws ActionException {
			errorMessages.add(messageProvider.getMessage(messageKey, args, locale));
			throwAll();
		}
		
		public void addAndThrow(String messageKey) throws ActionException {
			addAndThrow(messageKey, null);
			throwAll();
		}
		
		public boolean isEmpty() {
			return errorMessages.size() == 0;
		}
		
		public void throwAll() throws ActionException {
			if (!errorMessages.isEmpty()) {
				StringBuffer errBuff = new StringBuffer();
				for (String errMsg: errorMessages) {
					errBuff.append(errMsg).append("\n");
				}
				throw new ActionException(errBuff.toString());
			}	
		}
	}


	/**
	 * 
	 */
	public CommandTarget getCommandTarget(UUID uuid)
	{
		JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());

		Map<String, JasperDesignReportResource> cachedResources = cache.getCachedResources();
		Set<String> uris = cachedResources.keySet();
		for (String uri : uris)
		{
			CommandTarget target = new CommandTarget();
			target.setUri(uri);
			return target;
		}
		return null;
	}

}
