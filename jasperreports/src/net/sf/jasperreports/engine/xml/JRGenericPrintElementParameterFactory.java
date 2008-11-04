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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.util.JRValueStringUtils;

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
		protected ParameterValue value;
		
		public Parameter(JRGenericPrintElement element, String name)
		{
			this.element = element;
			this.name = name;
		}
		
		public void setValue(ParameterValue value)
		{
			this.value = value;
		}
		
		public void addParameter()
		{
			element.setParameterValue(name, value == null ? null : value.value);
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

}
