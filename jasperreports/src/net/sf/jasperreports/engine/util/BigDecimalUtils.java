/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility methods for {@link BigDecimal} handling.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BigDecimalUtils
{

	private static final Log log = LogFactory.getLog(BigDecimalUtils.class);
	
	private static final BigDecimalHandler handler;
	
	static
	{
		handler = loadHandler();
	}
	
	private static BigDecimalHandler loadHandler()
	{
		BigDecimalHandler handler;
		try
		{
			Class handlerClass = Class.forName(
					"net.sf.jasperreports.engine.util.Java15BigDecimalHandler");
			handler = (BigDecimalHandler) handlerClass.newInstance();
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Failed to instantiate Java 1.5 BigDecimal handler");
			}
			
			//fallback to 1.4 handler
			handler = new Java14BigDecimalHandler();
		}
		return handler;
	}

	/**
	 * Divides two {@link BigDecimal} values.
	 * 
	 * <p>
	 * If running on Java 1.5 or newer, {@link Java15BigDecimalHandler} is used to divide
	 * the values.  Otherwise, the operation is delegated to {@link Java14BigDecimalHandler}. 
	 * 
	 * @param dividend the dividend
	 * @param divisor the divisor
	 * @return the division result computed by the {@link BigDecimalHandler} instance
	 */
	public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor)
	{
		return handler.divide(dividend, divisor);
	}
	
}
