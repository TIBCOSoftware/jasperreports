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

import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.util.JRValueStringUtils;

import org.xml.sax.Attributes;


/**
 * Factory that sets {@link JRPrintHyperlinkParameter#getValue() print hyperlink parameter values}
 * from <code>hyperlinkParameterValue</code> XML elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRPrintHyperlinkParameter#setValue(Object)
 */
public class JRPrintHyperlinkParameterValueFactory extends JRBaseFactory
{
	public static final String TAG_HYPERLINK_PARAMETER_VALUE = "hyperlinkParameterValue";
	
	public Object createObject(Attributes attrs)
	{
		JRPrintHyperlinkParameter parameter = (JRPrintHyperlinkParameter) digester.peek();
		return new JRPrintHyperlinkParameterValue(parameter);
	}
	
	public static class JRPrintHyperlinkParameterValue
	{
		private final JRPrintHyperlinkParameter parameter;
		
		public JRPrintHyperlinkParameterValue(JRPrintHyperlinkParameter parameter)
		{
			this.parameter = parameter;
		}
		
		public void setData(String data)
		{
			if (data != null)
			{
				Object value = JRValueStringUtils.deserialize(parameter.getValueClass(), data);
				parameter.setValue(value);
			}
		}
	}

}
