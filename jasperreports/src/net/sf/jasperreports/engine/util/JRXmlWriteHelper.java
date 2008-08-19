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

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRExpression;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlWriteHelper
{
	
	public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	
	public static final String XML_SCHEMA_NAMESPACE_PREFIX = "xsi";
	
	public static final String XML_NAMESPACE_ATTRIBUTE = "xmlns";
	
	public static final String XML_SCHEMA_LOCATION_ATTRIBUTE = "schemaLocation";
	
	private final Writer writer;
	
	private final List indents;
	
	private int indent;
	private final List elementStack;
	private StringBuffer buffer;
	private StackElement lastElement;
		
	protected static class Attribute
	{
		String name;
		String value;
		
		Attribute(String name, String value)
		{
			this.name = name;
			this.value = value;
		}
	}
	
	protected static class StackElement
	{
		String name;
		List atts;
		boolean hasChildren;
		XmlNamespace namespace;
		String qName;
		boolean hasAttributes = false;

		StackElement(String name, XmlNamespace namespace)
		{
			this.name = name;
			this.atts = new ArrayList();
			this.hasChildren = false;
			this.namespace = namespace;
			this.qName = getQualifiedName(this.name, this.namespace);
		}
		
		void addAttribute(String attName, String value)
		{
			addAttribute(attName, value, true);
		}
		
		void addAttribute(String attName, String value, boolean count)
		{
			atts.add(new Attribute(attName, value));
			hasAttributes |= count;
		}
	}
	
	public JRXmlWriteHelper(Writer writer)
	{
		this.writer = writer;
		
		indents = new ArrayList();
		
		indent = 0;
		elementStack = new ArrayList();
		lastElement = null;
		
		clearBuffer();
	}
	
	public void writeProlog(String encoding) throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
	}
	
	public void writePublicDoctype(String rootElement, String description, String dtdLocation) throws IOException
	{
		writer.write("<!DOCTYPE " + rootElement + " PUBLIC \"" + description  + "\" \"" + dtdLocation + "\">\n\n");
	}
	
	public void startElement(String name)
	{
		startElement(name, null);
	}
	
	public void startElement(String name, XmlNamespace namespace)
	{
		boolean startsNS = false;
		XmlNamespace elementNS = null;
		if (namespace == null)
		{
			elementNS = getParentNamespace();
		}
		else
		{
			elementNS = findContextNamespace(namespace.getNamespaceURI());
			if (elementNS == null)
			{
				startsNS = true;
				elementNS = namespace;
			}
		}
		
		++indent;
		lastElement = new StackElement(name, elementNS);
		elementStack.add(lastElement);
		
		if (startsNS)
		{
			String xmlnsAttr = XML_NAMESPACE_ATTRIBUTE;
			if (namespace.getPrefix() != null)
			{
				xmlnsAttr += ":" + namespace.getPrefix();
			}
			lastElement.addAttribute(xmlnsAttr, namespace.getNamespaceURI(), false);
			
			if (indent == 1)//root element
			{
				//add the XML Schema namespace
				String xmlSchemaXmlns = XML_NAMESPACE_ATTRIBUTE + ":" + XML_SCHEMA_NAMESPACE_PREFIX;
				lastElement.addAttribute(xmlSchemaXmlns, XML_SCHEMA_NAMESPACE, false);
			}
			
			if (namespace.getSchemaURI() != null)
			{
				String schemaLocationAttr = getQualifiedName(
						XML_SCHEMA_LOCATION_ATTRIBUTE, XML_SCHEMA_NAMESPACE_PREFIX);
				String schemaLocation = namespace.getNamespaceURI() 
						+ " " + namespace.getSchemaURI();
				lastElement.addAttribute(schemaLocationAttr, schemaLocation, false);
			}
		}
	}
	
	protected XmlNamespace getParentNamespace()
	{
		return lastElement == null ? null : lastElement.namespace;
	}

	protected XmlNamespace findContextNamespace(String namespaceURI)
	{
		XmlNamespace ns = null;
		for (ListIterator it = elementStack.listIterator(elementStack.size()); it.hasPrevious();)
		{
			StackElement element = (StackElement) it.previous();
			if (element.namespace != null && namespaceURI.equals(element.namespace.getNamespaceURI()))
			{
				ns = element.namespace;
				break;
			}
		}
		return ns;
	}
	
	protected static String getQualifiedName(String name, XmlNamespace ns)
	{
		return ns == null ? name : getQualifiedName(name, ns.getPrefix()); 
	}
	
	protected static String getQualifiedName(String name, String nsPrefix)
	{
		String qName;
		if (nsPrefix == null)
		{
			qName = name;
		}
		else
		{
			qName = nsPrefix + ":" + name;
		}
		return qName;
	}
	
	protected void writeParents(boolean content) throws IOException
	{
		int stackSize = elementStack.size();
		
		int startWrite = stackSize - 1;
		while (startWrite >= 0)
		{
			StackElement element = (StackElement) elementStack.get(startWrite);
			
			if (element.hasChildren)
			{
				break;
			}
			
			if (startWrite < stackSize - 1)
			{
				element.hasChildren = true;
			}
			else
			{
				element.hasChildren |= content;
			}
			
			--startWrite;
		}
		
		for (int i = startWrite + 1; i < stackSize; ++i)
		{
			StackElement element = (StackElement) elementStack.get(i);
			writeElementAttributes(element, i);
		}
	}

	public void writeCDATA(String data) throws IOException
	{
		if (data != null)
		{
			writeParents(true);

			buffer.append(getIndent(indent));
			buffer.append("<![CDATA[");
			buffer.append(data);
			buffer.append("]]>\n");
			flushBuffer();
		}
	}
	
	public void writeCDATAElement(String name, String data) throws IOException
	{
		if (data != null)
		{
			writeParents(true);

			buffer.append(getIndent(indent));
			buffer.append('<');
			String qName = getQualifiedName(name, getParentNamespace());
			buffer.append(qName);
			buffer.append("><![CDATA[");
			buffer.append(data);
			buffer.append("]]></");
			buffer.append(qName);
			buffer.append(">\n");
			flushBuffer();
		}
	}
	
	public void writeCDATAElement(String name, String data, String attName, String attValue) throws IOException
	{
		writeCDATAElement(name, data, attName, (Object) attValue);
	}
	
	public void writeCDATAElement(String name, String data, String attName, Object attValue) throws IOException
	{
		if (data != null)
		{
			writeParents(true);

			buffer.append(getIndent(indent));
			buffer.append('<');
			String qName = getQualifiedName(name, getParentNamespace());
			buffer.append(qName);
			if (attValue != null)
			{
				buffer.append(' ');
				buffer.append(attName);
				buffer.append("=\"");
				buffer.append(attValue);
				buffer.append("\"");
			}
			buffer.append("><![CDATA[");
			buffer.append(data);
			buffer.append("]]></");
			buffer.append(qName);
			buffer.append(">\n");
			flushBuffer();
		}
	}
	
	protected void writeElementAttributes(StackElement element, int level) throws IOException
	{
		buffer.append(getIndent(level));
		buffer.append('<');
		buffer.append(element.qName);
		for (Iterator i = element.atts.iterator(); i.hasNext();)
		{
			Attribute att = (Attribute) i.next();
			buffer.append(' ');
			buffer.append(att.name);
			buffer.append("=\"");
			buffer.append(att.value);
			buffer.append('"');
		}
		
		if (element.hasChildren)
		{
			buffer.append(">\n");
		}
		else
		{
			buffer.append("/>\n");
		}
		
		flushBuffer();
	}

	public void closeElement() throws IOException
	{
		closeElement(false);
	}
	
	public void closeElement(boolean skipIfEmpty) throws IOException
	{
		--indent;

		if (skipIfEmpty && !lastElement.hasAttributes && !lastElement.hasChildren)
		{
			clearBuffer();
		}
		else
		{
			writeParents(false);
			
			if (lastElement.hasChildren)
			{
				buffer.append(getIndent(indent));
				buffer.append("</");
				buffer.append(lastElement.qName);
				buffer.append(">\n");
				flushBuffer();
			}
		}
		
		elementStack.remove(indent);
		lastElement = indent > 0 ? (StackElement) elementStack.get(indent - 1) : null;
	}
	
	protected char[] getIndent(int level)
	{
		if (level >= indents.size())
		{
			for (int i = indents.size(); i <= level; ++i)
			{
				char[] str = new char[i];
				Arrays.fill(str, '\t');
				indents.add(str);
			}
		}
		
		return (char[]) indents.get(level);
	}
	
	protected void flushBuffer() throws IOException
	{
		writer.write(buffer.toString());
		clearBuffer();
	}

	protected void clearBuffer()
	{
		buffer = new StringBuffer();
	}
	

	public void writeExpression(String name, JRExpression expression, boolean writeClass) throws IOException
	{
		writeExpression(name, expression, writeClass, null);
	}


	public void writeExpression(String name, JRExpression expression, boolean writeClass, String defaultClassName) throws IOException
	{
		if (expression != null)
		{
			if (writeClass &&
					(defaultClassName == null || !defaultClassName.equals(expression.getValueClassName())))
			{
				writeCDATAElement(name, expression.getText(), "class", expression.getValueClassName());
			}
			else
			{
				writeCDATAElement(name, expression.getText());
			}
		}
	}

	protected void writeAttribute(String name, String value)
	{
		lastElement.addAttribute(name, value);
	}
	
	public void addAttribute(String name, String value)
	{
		if (value != null)
		{
			writeAttribute(name, value);
		}
	}
	
	public void addEncodedAttribute(String name, String value)
	{
		if (value != null)
		{
			writeAttribute(name, JRStringUtil.xmlEncode(value));
		}
	}
	
	public void addAttribute(String name, String value, String defaultValue)
	{
		if (value != null && !value.equals(defaultValue))
		{
			writeAttribute(name, value);
		}
	}
	
	public void addEncodedAttribute(String name, String value, String defaultValue)
	{
		if (value != null && !value.equals(defaultValue))
		{
			writeAttribute(name, JRStringUtil.xmlEncode(value));
		}
	}
	
	public void addAttribute(String name, Object value)
	{
		if (value != null)
		{
			writeAttribute(name, String.valueOf(value));
		}
	}
	
	public void addAttribute(String name, int value)
	{
		writeAttribute(name, String.valueOf(value));
	}
	
	public void addAttributePositive(String name, int value)
	{
		if (value > 0)
		{
			writeAttribute(name, String.valueOf(value));
		}
	}
	
	public void addAttribute(String name, float value)
	{
		writeAttribute(name, String.valueOf(value));
	}
	
	public void addAttribute(String name, float value, float defaultValue)
	{
		if (value != defaultValue)
		{
			writeAttribute(name, String.valueOf(value));
		}
	}
	
	public void addAttribute(String name, double value)
	{
		writeAttribute(name, String.valueOf(value));
	}
	
	public void addAttribute(String name, double value, double defaultValue)
	{
		if (value != defaultValue)
		{
			writeAttribute(name, String.valueOf(value));
		}
	}
	
	public void addAttribute(String name, int value, int defaultValue)
	{
		if (value != defaultValue)
		{
			addAttribute(name, value);
		}
	}
	
	public void addAttribute(String name, boolean value)
	{
		writeAttribute(name, String.valueOf(value));
	}
	
	public void addAttribute(String name, boolean value, boolean defaultValue)
	{
		if (value != defaultValue)
		{
			addAttribute(name, value);
		}
	}
	
	public void addAttribute(String name, Color color)
	{
		if (color != null)
		{
			writeAttribute(name, "#" + JRColorUtil.getColorHexa(color));
		}
	}
	
	public void addAttribute(String name, Color value, Color defaultValue)
	{
		if (value != null && value.getRGB() != defaultValue.getRGB())
		{
			addAttribute(name, value);
		}
	}
	
	public void addAttribute(String name, byte value, Map xmlValues)
	{
		String xmlValue = (String) xmlValues.get(new Byte(value));
		writeAttribute(name, xmlValue);
	}
	
	public void addAttribute(String name, int value, Map xmlValues)
	{
		String xmlValue = (String) xmlValues.get(new Integer(value));
		writeAttribute(name, xmlValue);
	}
	
	public void addAttribute(String name, byte value, Map xmlValues, byte defaultValue)
	{
		if (value != defaultValue)
		{
			addAttribute(name, value, xmlValues);
		}
	}
	
	public void addAttribute(String name, Object value, Map xmlValues)
	{
		if (value != null)
		{
			String xmlValue = (String) xmlValues.get(value);
			writeAttribute(name, xmlValue);
		}
	}
	
	public void addAttribute(String name, Object value, Map xmlValues, Object defaultValue)
	{
		if (!value.equals(defaultValue))
		{
			addAttribute(name, value, xmlValues);
		}
	}
	
	public Writer getUnderlyingWriter()
	{
		return writer;
	}
}
