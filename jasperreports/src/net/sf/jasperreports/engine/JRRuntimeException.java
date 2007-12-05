/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRRuntimeException extends RuntimeException
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private static boolean isJre14orLater = true;
	
	static
	{
		isJre14orLater = true;
		try 
		{
			Exception.class.getMethod("getCause", (Class[])null);
		}
		catch (NoSuchMethodException e) 
		{
			isJre14orLater = false;
		}
	}

	/**
	 *
	 */
	private Throwable nestedThrowable = null;


	/**
	 *
	 */
	public JRRuntimeException(String message)
	{
		super(message);
	}


	/**
	 *
	 */
	public JRRuntimeException(Throwable t)
	{
		this(t.toString(), t);
	}


	/**
	 *
	 */
	public JRRuntimeException(String message, Throwable t)
	{
		super(message);
		
		nestedThrowable = t;
	}


	/**
	 *
	 */
	public Throwable getCause()
	{
		return nestedThrowable;
	}


	/**
	 *
	 */
	public void printStackTrace()
	{
		if (!isJre14orLater && nestedThrowable != null)
		{
			nestedThrowable.printStackTrace();
			System.err.println("\nNested by:");
		}

		super.printStackTrace();
	}
	

	/**
	 *
	 */
	public void printStackTrace(PrintStream s)
	{
		if (!isJre14orLater && nestedThrowable != null)
		{
			nestedThrowable.printStackTrace(s);
			s.println("\nNested by:");
		}

		super.printStackTrace(s);
	}
	

	/**
	 *
	 */
	public void printStackTrace(PrintWriter s)
	{
		if (!isJre14orLater && nestedThrowable != null)
		{
			nestedThrowable.printStackTrace(s);
			s.println("\nNested by:");
		}

		super.printStackTrace(s);
	}
	
	
}
