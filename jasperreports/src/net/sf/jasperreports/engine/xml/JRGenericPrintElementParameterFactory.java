/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.util.JRValueStringUtils;

import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Helper factory class used to parse generic print element parameters.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRGenericPrintElement#setParameterValue(String, Object)
 */
public class JRGenericPrintElementParameterFactory extends JRBaseFactory
{

	public Object createObject(Attributes attributes) throws Exception
	{
		JRGenericPrintElement element = (JRGenericPrintElement) digester.peek();
		String name = attributes.getValue(JRXmlConstants.ATTRIBUTE_name);
		return new Parameter(element, name);
	}
	
	public static class Parameter
	{
		protected JRGenericPrintElement element;
		protected String name;
		protected Object value;
		
		public Parameter(JRGenericPrintElement element, String name)
		{
			this.element = element;
			this.name = name;
		}
		
		protected void setValue(Object value)
		{
			this.value = value;
		}
		
		public void addParameter()
		{
			element.setParameterValue(name, value);
		}
	}
	
	public static class ParameterValue
	{
		protected String valueClass;
		protected Object value;
		
		public ParameterValue(String valueClass)
		{
			this.valueClass = valueClass;
		}
		
		public void setData(String data)
		{
			if (data != null)
			{
				value = JRValueStringUtils.deserialize(valueClass, data);
			}
		}
	}
	
	public static class ParameterValueFactory extends JRBaseFactory
	{

		public Object createObject(Attributes attributes) throws Exception
		{
			String valueClass = attributes.getValue(JRXmlConstants.ATTRIBUTE_class);
			return new ParameterValue(valueClass);
		}
		
		
	}

	public static class ArbitraryValueSetter extends Rule
	{
		@Override
		public void begin(String namespace, String name, Attributes attributes) throws Exception
		{
			((JRXmlDigester) digester).clearLastPopped();
		}

		@Override
		public void end(String namespace, String name) throws Exception
		{
			Parameter parameter = (Parameter) digester.peek();
			Object lastPopped = ((JRXmlDigester) digester).lastPopped();
			if (lastPopped != null)
			{
				Object value;
				if (lastPopped instanceof ParameterValue)
				{
					// this is for genericElementParameterValue elements
					value = ((ParameterValue) lastPopped).value;
				}
				else
				{
					// arbitrary elements
					value = lastPopped;
				}
				parameter.setValue(value);
			}
		}
	}
}
