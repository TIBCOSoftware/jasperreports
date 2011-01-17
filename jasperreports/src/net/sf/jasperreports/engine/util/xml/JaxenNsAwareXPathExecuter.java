/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.util.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections.ReferenceMap;
import org.jaxen.JaxenException;
import org.jaxen.NamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * XPath executer implementation that uses a namespace aware <a href="http://jaxen.org/" target="_blank">Jaxen</a>.
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JaxenNsAwareXPathExecuter extends JaxenXPathExecuter
{

	private final Map cachedXPaths = new ReferenceMap();//soft cache
	
	private Document document;

	private Map<String, String> xmlNamespaceMap;
	
	boolean detectXmlNamespaces;
	
	
	public JaxenNsAwareXPathExecuter()
	{
	}
	
	public void setDocument(Document document) {
		this.document = document;
	}

	
	public Map<String, String> getXmlNamespaceMap() 
	{
		return xmlNamespaceMap;
	}
	
	
	public void setXmlNamespaceMap(Map<String, String> xmlNamespaceMap) throws JRException 
	{
		this.xmlNamespaceMap = xmlNamespaceMap;
	}
	
	
	public boolean getDetectXmlNamespaces()
	{
		return detectXmlNamespaces;
	}


	public void setDetectXmlNamespaces(boolean detectXmlNamespaces) 
	{
		this.detectXmlNamespaces = detectXmlNamespaces;
	}
	
	protected XPath getXPath(String expression) throws JRException
	{
		XPath xPath = (XPath) cachedXPaths.get(expression);
		if (xPath == null)
		{
			try
			{
				xPath = new DOMXPath(expression);
				addNamespaceContext(xPath, expression);
			}
			catch (JaxenException e)
			{
				throw new JRException("XPath compilation failed. Expression: " + expression, e);
			}
			cachedXPaths.put(expression, xPath);
		}
		return xPath;
	}
	

	protected static final class JaxenNamespaceContextWrapper implements NamespaceContext {

		private Map<String, String> namespaceMap;
		
		public JaxenNamespaceContextWrapper(Map<String, String> namespaceMap) {
			this.namespaceMap = namespaceMap;
		}
		
		public String translateNamespacePrefixToUri(String prefix) {
			return namespaceMap.get(prefix);
		}
		
	}
	
	protected boolean containsPrefixes(String expression) {
		String[] tokens = expression.split("::");
		for (String token: tokens) 
		{
			if (token.indexOf(":") != -1) 
			{
				return true;
			}
		}
		return false;
	}
	
	private Map<String, String> extractXmlNamespaces(Document document) throws JRException 
	{
		Map<String, String> namespaces = new HashMap<String, String>();
		List nlist;
		String namespaceXPathString = "//namespace::node()";

		try
		{
			XPath xpath = new DOMXPath(namespaceXPathString);
			nlist = xpath.selectNodes(document);
			
            for (int i = 0; i < nlist.size(); i++) 
            {
                Node node = (Node)nlist.get(i);
                if(node.getParentNode() != null && node.getParentNode().getPrefix() != null)
                {
                	if (!namespaces.containsKey(node.getParentNode().getPrefix()))
                	{
                		namespaces.put(node.getParentNode().getPrefix(), node.getParentNode().getNamespaceURI());
                	}
                }
            }
            
		} catch (JaxenException e)
		{
			throw new JRException("XPath selection failed. Expression: " + namespaceXPathString, e);
		}
		
        return namespaces;
	}
	
	protected void addNamespaceContext(XPath xPath, String expression) throws JRException {
		if (xmlNamespaceMap == null && detectXmlNamespaces && containsPrefixes(expression))
		{
			xmlNamespaceMap = extractXmlNamespaces(document);
		}
		
		if (xmlNamespaceMap != null)
		{
			xPath.setNamespaceContext(new JaxenNamespaceContextWrapper(xmlNamespaceMap));
		}
	}
	
}
