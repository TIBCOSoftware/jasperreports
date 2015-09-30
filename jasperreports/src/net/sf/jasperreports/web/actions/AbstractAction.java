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
package net.sf.jasperreports.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.engine.util.UniformElementVisitor;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.repo.JasperDesignReportResource;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="actionName")
public abstract class AbstractAction implements Action {
	
	public static final String PARAM_COMMAND_STACK = "net.sf.jasperreports.command.stack";
	public static final String ERR_CONCAT_STRING = "<#_#>";
	
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
		
		public void add(String messageKey, Object... args) {
			errorMessages.add(messageProvider.getMessage(messageKey, args, locale));
		}

		public void add(String messageKey) {
			add(messageKey, (Object[])null);
		}

		public void addAndThrow(String messageKey, Object... args) throws ActionException {
			errorMessages.add(messageProvider.getMessage(messageKey, args, locale));
			throwAll();
		}
		
		public void addAndThrow(String messageKey) throws ActionException {
			addAndThrow(messageKey, (Object[])null);
		}
		
		public boolean isEmpty() {
			return errorMessages.size() == 0;
		}
		
		public void throwAll() throws ActionException {
			if (!errorMessages.isEmpty()) {
				StringBuffer errBuff = new StringBuffer();
				for (int i = 0, ln = errorMessages.size(); i < ln; i++) {
					String errMsg = errorMessages.get(i);
					errBuff.append(errMsg);
					if (i < ln -1) {
						errBuff.append(ERR_CONCAT_STRING);
					}
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
		return getCommandTarget(uuid, JRDesignComponentElement.class);
	}

	public CommandTarget getCommandTarget(final UUID uuid, final Class<? extends JRDesignElement> elementType)
	{
		JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());

		Map<String, JasperDesignReportResource> cachedResources = cache.getCachedResources();
		Set<String> uris = cachedResources.keySet();
		for (String uri : uris)
		{
			final CommandTarget target = new CommandTarget();
			target.setUri(uri);
			
			JasperDesign jasperDesign = cache.getJasperDesign(uri);
			JRElementsVisitor.visitReport(jasperDesign, new UniformElementVisitor()
			{
				private boolean found = false;
				
				@Override
				public void visitElementGroup(JRElementGroup elementGroup)
				{
					//NOP
				}
				
				@Override
				protected void visitElement(JRElement element)
				{
					if (!found && elementType.isInstance(element) && uuid.equals(element.getUUID()))
					{
						target.setIdentifiable(element);
						
						// there's no way to stop the graph visit
						found = true;
					}
				}
			});
			
			if (target.getIdentifiable() != null)
			{
				return target;
			}
		}
		return null;
	}


	@Override
	public boolean requiresRefill() {
		return true;
	}
}
