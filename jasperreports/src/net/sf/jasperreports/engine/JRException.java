/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine;

import java.util.Locale;
import java.util.MissingResourceException;

import net.sf.jasperreports.engine.util.MessageUtil;



/**
 * General purpose JasperReports exception.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRException extends Exception
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String ERROR_MESSAGES_BUNDLE = "jasperreports_messages";
	
	private Object[] args;
	private String messageKey;
	private String localizedMessage;
	private boolean hasLocalizedMessage;


	/**
	 *
	 */
	public JRException(String message)
	{
		super(message);
	}


	/**
	 *
	 */
	public JRException(Throwable t)
	{
		super(t);
	}


	/**
	 *
	 */
	public JRException(String message, Throwable t)
	{
		super(message, t);
	}


	/**
	 *
	 */
	public JRException(String messageKey, Object[] args, JasperReportsContext jasperReportsContext, Locale locale)
	{
		super(messageKey);
		this.messageKey = messageKey;
		this.args = args;
		this.localizedMessage = resolveMessage(messageKey, args, jasperReportsContext, locale);
	}


	/**
	 *
	 */
	public String getMessageKey()
	{
		return messageKey;
	}


	/**
	 *
	 */
	public Object[] getArgs()
	{
		return args;
	}


	/**
	 *
	 */
	public boolean hasLocalizedMessage()
	{
		return hasLocalizedMessage;
	}


	@Override
	public String getMessage()
	{
		if (hasLocalizedMessage)
		{
			return localizedMessage;
		}
		return super.getMessage();
	}


	/**
	 *
	 */
	protected String resolveMessage(String messageKey, Object[] args, JasperReportsContext jasperReportsContext, Locale locale)
	{
		if (messageKey != null)
		{
			try
			{
				hasLocalizedMessage = true;
				return MessageUtil.getInstance(jasperReportsContext).getMessageProvider(ERROR_MESSAGES_BUNDLE).getMessage(messageKey, args, locale);
			}
			catch (MissingResourceException e)
			{
			}
		}
		hasLocalizedMessage = false;
		return messageKey;
	}

}
