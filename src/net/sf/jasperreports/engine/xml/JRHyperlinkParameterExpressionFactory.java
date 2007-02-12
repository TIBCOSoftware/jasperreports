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

import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlinkParameter;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;

/**
 * Factory that creates {@link JRHyperlinkParameter#getValueExpression() hyperlink parameter value expressions}
 * from <code>hyperlinkParameterExpression</code> XML elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRDesignHyperlinkParameter#setValueExpression(net.sf.jasperreports.engine.JRExpression)
 */
public class JRHyperlinkParameterExpressionFactory extends JRBaseFactory
{
	
	public Object createObject(Attributes attributes)
	{
		JRDesignExpression expression = new JRDesignExpression();
		String valueClass = attributes.getValue(JRXmlConstants.ATTRIBUTE_class);
		if (valueClass == null)
		{
			expression.setValueClass(String.class); 
		}
		else
		{
			expression.setValueClassName(valueClass);
		}

		return expression;
	}
}
