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

import javax.xml.transform.TransformerException;

import net.sf.jasperreports.engine.JRException;

import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * XPath executer implementation that uses <a href="http://xml.apache.org/xalan-j/" target="_blank">Apache Xalan</a>.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class XalanXPathExecuter implements JRXPathExecuter {

	// XPath API facade
	private CachedXPathAPI xpathAPI = new CachedXPathAPI();

	/**
	 * Default constructor.
	 */
	public XalanXPathExecuter() {
	}
	
	public NodeList selectNodeList(Node contextNode, String expression) throws JRException
	{
		try {
			return xpathAPI.selectNodeList(contextNode, expression);
		} catch (TransformerException e) {
			throw new JRException("XPath selection failed. Expression: "
					+ expression, e);
		}
	}

	public Object selectObject(Node contextNode, String expression) throws JRException {
		try {
			Object value;
			XObject object = xpathAPI.eval(contextNode, expression);
			switch (object.getType()) {
				case XObject.CLASS_NODESET:
					value = object.nodeset().nextNode();
					break;
				case XObject.CLASS_BOOLEAN:
					value = object.bool() ? Boolean.TRUE : Boolean.FALSE;
					break;
				case XObject.CLASS_NUMBER:
					value = new Double(object.num());
					break;
				default:
					value = object.str();
					break;
			}
			return value;
		} catch (TransformerException e) {
			throw new JRException("XPath selection failed. Expression: "
					+ expression, e);
		}
	}
	
}
