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
 * General purpose JasperReports exception.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRException extends Exception
{
	

	/**
	 *
	 */
	private Throwable nestedThrowable = null;


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
	public JRException(Throwable e)
	{
		super(e.getMessage());
		nestedThrowable = e;
	}


	/**
	 *
	 */
	public JRException(String message, Throwable e)
	{
		super(message);
		nestedThrowable = e;
	}


	/**
	 *
	 */
	public Throwable getCause()
	{
		return this.nestedThrowable;
	}


	/**
	 *
	 */
	public void printStackTrace()
	{
		if (nestedThrowable != null)
		{
			nestedThrowable.printStackTrace();
			System.err.println("\nNESTED BY :");
		}

		super.printStackTrace();
	}
	

	/**
	 *
	 */
	public void printStackTrace(PrintStream s)
	{
		if (nestedThrowable != null)
		{
			nestedThrowable.printStackTrace(s);
			s.println("\nNESTED BY :");
		}

		super.printStackTrace(s);
	}
	

	/**
	 *
	 */
	public void printStackTrace(PrintWriter s)
	{
		if (nestedThrowable != null)
		{
			nestedThrowable.printStackTrace(s);
			s.println("\nNESTED BY :");
		}

		super.printStackTrace(s);
	}
	
	
}
