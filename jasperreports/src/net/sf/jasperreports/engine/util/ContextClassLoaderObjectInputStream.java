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
package net.sf.jasperreports.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * A subclass of {@link ObjectInputStream} that uses
 * {@link Thread#getContextClassLoader() the context class loader} to resolve
 * classes encountered in the input stream.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ContextClassLoaderObjectInputStream extends ObjectInputStream
{

	/**
	 * Creates an object input stream that reads data from the specified
	 * {@link InputStream}.
	 * 
	 * @param in the input stream to read data from
	 * @throws IOException
	 * @see {@link ObjectInputStream#ObjectInputStream(InputStream)}
	 */
	public ContextClassLoaderObjectInputStream(InputStream in) throws IOException
	{
		super(in);
	}

	/**
	 * Calls <code>super.resolveClass()</code> and in case this fails with
	 * {@link ClassNotFoundException} attempts to load the class using the
	 * context class loader.
	 */
	protected Class resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException
	{
		try
		{
			return super.resolveClass(desc);
		}
		catch (ClassNotFoundException e)
		{
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if (contextClassLoader == null)
			{
				throw e;
			}
			
			String name = desc.getName();
			try
			{
				//attempt to load the class using the context class loader
			    return Class.forName(name, false, contextClassLoader);
			}
			catch (ClassNotFoundException e2)
			{
				//fallback to the original exception
				throw e;
			}
		}
	}

}
