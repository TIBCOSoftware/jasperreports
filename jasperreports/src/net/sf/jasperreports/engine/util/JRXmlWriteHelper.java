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
import java.util.Map;

import net.sf.jasperreports.engine.JRExpression;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlWriteHelper
{
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

		StackElement(String name)
		{
			this.name = name;
			this.atts = new ArrayList();
			this.hasChildren = false;
		}
		
		void addAttribute(String attName, String value)
		{
			atts.add(new Attribute(attName, value));
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
		++indent;
		lastElement = new StackElement(name);
		elementStack.add(lastElement);
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
			buffer.append(name);
			buffer.append("><![CDATA[");
			buffer.append(data);
			buffer.append("]]></");
			buffer.append(name);
			buffer.append(">\n");
			flushBuffer();
		}
	}
	
	public void writeCDATAElement(String name, String data, String attName, String attValue) throws IOException
	{
		if (data != null)
		{
			writeParents(true);

			buffer.append(getIndent(indent));
			buffer.append('<');
			buffer.append(name);
			buffer.append(' ');
			buffer.append(attName);
			buffer.append("=\"");
			buffer.append(attValue);
			buffer.append("\"><![CDATA[");
			buffer.append(data);
			buffer.append("]]></");
			buffer.append(name);
			buffer.append(">\n");
			flushBuffer();
		}
	}
	
	protected void writeElementAttributes(StackElement element, int level) throws IOException
	{
		buffer.append(getIndent(level));
		buffer.append('<');
		buffer.append(element.name);
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

		if (skipIfEmpty && lastElement.atts.size() == 0 && !lastElement.hasChildren)
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
				buffer.append(lastElement.name);
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
	
	public void addAttribute(String name, Color value)
	{
		if (value != null)
		{
			writeAttribute(name, "#" + getHexaColor(value));
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
	
	protected static final int COLOR_MASK = Integer.parseInt("FFFFFF", 16);
	protected static String getHexaColor(Color color)
	{
		String hexa = Integer.toHexString(color.getRGB() & COLOR_MASK).toUpperCase();
		return ("000000" + hexa).substring(hexa.length());
	}
}
