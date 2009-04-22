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

/**
 * A composite classloader that has a parent classloader and an alternate
 * classloader.
 * 
 * <p>
 * When resolving classes, the parent classloader is consulted first, and if
 * that classloader cannot find the class, the alternate/second classloader is
 * asked to resolve the class. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class CompositeClassloader extends ClassLoader
{

	private final ClassLoader fallback;

	/**
	 * Creates a composite classloader.
	 * 
	 * @param parent the parent classloader
	 * @param fallback the alternate classloader
	 */
	public CompositeClassloader(ClassLoader parent, ClassLoader fallback)
	{
		super(parent);
		
		this.fallback = fallback;
	}

	protected Class findClass(String name) throws ClassNotFoundException
	{
		// this method is only called when the parent classloader cannot
		// resolve the class, so we try the alternate classloder
		
		if (fallback != null)
		{
			return fallback.loadClass(name);
		}
		
		// throw ClassNotFoundException
		return super.findClass(name);
	}
	
}
