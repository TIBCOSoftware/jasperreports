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
package net.sf.jasperreports.engine.util;

import java.io.Serializable;


/**
 * Utility class used to pair two objects.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
//FIXME use generics everywhere
public class Pair<T, U> implements Serializable
{
	private static final long serialVersionUID = 1; //too late to replace this now
	
	private final T o1;
	private final U o2;
	private final int hash;

	
	/**
	 * Create a pair instance.
	 * 
	 * @param o1 the first member of the pair
	 * @param o2 the second member of the pair
	 */
	public Pair(T o1, U o2)
	{
		this.o1 = o1;
		this.o2 = o2;
		this.hash = computeHash();
	}
	
	public T first()
	{
		return o1;
	}
	
	public U second()
	{
		return o2;
	}

	private int computeHash()
	{
		int hashCode = o1 == null ? 0 : o1.hashCode();
		hashCode *= 31;
		hashCode += o2 == null ? 0 : o2.hashCode();
		return hashCode;
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		
		if (o == null || !(o instanceof Pair))
		{
			return false;
		}
		
		Pair<?, ?> p = (Pair<?, ?>) o;
		
		return (p.o1 == null ? o1 == null : (o1 != null && p.o1.equals(o1))) &&
			(p.o2 == null ? o2 == null : (o2 != null && p.o2.equals(o2)));
	}

	public int hashCode()
	{
		return hash;
	}
	
	public String toString()
	{
		return "(" + String.valueOf(o1) + ", " + String.valueOf(o2) + ")";
	}

}
