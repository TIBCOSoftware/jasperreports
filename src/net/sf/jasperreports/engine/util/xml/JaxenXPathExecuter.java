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
package net.sf.jasperreports.engine.util.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections.ReferenceMap;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * XPath executer implementation that uses <a href="http://jaxen.org/" target="_blank">Jaxen</a>.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JaxenXPathExecuter implements JRXPathExecuter
{

	private final Map cachedXPaths = new ReferenceMap();//soft cache
	
	public JaxenXPathExecuter()
	{
	}
	
	protected XPath getXPath(String expression) throws JRException
	{
		XPath xPath = (XPath) cachedXPaths.get(expression);
		if (xPath == null)
		{
			try
			{
				xPath = new DOMXPath(expression);
			}
			catch (JaxenException e)
			{
				throw new JRException("XPath compilation failed. Expression: " + expression, e);
			}
			cachedXPaths.put(expression, xPath);
		}
		return xPath;
	}
	
	public NodeList selectNodeList(Node contextNode, String expression) throws JRException
	{
		try
		{
			XPath xpath = getXPath(expression);
			Object object = xpath.evaluate(contextNode);
			List nodes;
			if (object instanceof List)
			{
				nodes = (List) object;
			}
			else
			{
				nodes = new ArrayList();
				nodes.add(object);
			}
			return new NodeListWrapper(nodes);
		}
		catch (JaxenException e)
		{
			throw new JRException("XPath selection failed. Expression: " + expression, e);
		}		
	}

	public Object selectObject(Node contextNode, String expression) throws JRException
	{
		try
		{
			XPath xpath = new DOMXPath(expression);
			Object object = xpath.evaluate(contextNode);
			Object value;
			if (object instanceof List)
			{
				List list = (List) object;
				if (list.isEmpty())
				{
					value = null;
				}
				else
				{
					value = list.get(0);
				}
			}
			else if (object instanceof Number || object instanceof Boolean)
			{
				value = object;
			}
			else
			{
				value = object.toString();
			}
			return value;
		}
		catch (JaxenException e)
		{
			throw new JRException("XPath selection failed. Expression: " + expression, e);
		}
	}

	protected static final class NodeListWrapper implements NodeList
	{

		private final List nodes;
		
		public NodeListWrapper(List nodes)
		{
			this.nodes = nodes;
		}
		
		public int getLength()
		{
			return nodes.size();
		}

		public Node item(int index)
		{
			return (Node) nodes.get(index);
		}
		
	}
	
}
