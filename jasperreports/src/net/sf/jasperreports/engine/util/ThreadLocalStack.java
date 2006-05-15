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

import java.util.LinkedList;


/**
 * Thread local stack utility class.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ThreadLocalStack
{
	private final ThreadLocal threadStack;
	
	public ThreadLocalStack()
	{
		threadStack = new ThreadLocal();
	}
	
	public void push(Object o)
	{
		LinkedList stack = (LinkedList) threadStack.get();
		if (stack == null)
		{
			stack = new LinkedList();
			threadStack.set(stack);
		}
		stack.addFirst(o);
	}
	
	public Object top()
	{
		Object o = null;
		LinkedList stack = (LinkedList) threadStack.get();
		if (stack != null && !stack.isEmpty())
		{
			o = stack.getFirst();
		}
		return o;
	}
	
	public Object pop()
	{
		Object o = null;
		LinkedList stack = (LinkedList) threadStack.get();
		if (stack != null)
		{
			o = stack.removeFirst();
		}
		return o;
	}
	
	public boolean empty()
	{
		LinkedList stack = (LinkedList) threadStack.get();
		return stack == null || stack.isEmpty();
	}
}
