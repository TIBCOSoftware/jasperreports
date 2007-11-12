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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlinkParameter;


/**
 * Base implementation of {@link JRHyperlinkParameter JRHyperlinkParameter}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseHyperlinkParameter implements JRHyperlinkParameter, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected String name;
	protected JRExpression valueExpression;
	
	
	/**
	 * Creates a blank instance.
	 */
	protected JRBaseHyperlinkParameter()
	{
	}

	
	/**
	 * Creates an instance equivalent to an existing hyperlink parameter.
	 * 
	 * @param parameter the parameter to replicate
	 * @param factory the base object factory
	 */
	public JRBaseHyperlinkParameter(JRHyperlinkParameter parameter, JRBaseObjectFactory factory)
	{
		factory.put(parameter, this);

		this.name = parameter.getName();
		this.valueExpression = factory.getExpression(parameter.getValueExpression());
	}

	public String getName()
	{
		return name;
	}

	public JRExpression getValueExpression()
	{
		return valueExpression;
	}

	/**
	 *
	 */
	public Object clone() throws CloneNotSupportedException 
	{
		JRBaseHyperlinkParameter clone = (JRBaseHyperlinkParameter)super.clone();
		if (valueExpression != null)
		{
			clone.valueExpression = (JRExpression)valueExpression.clone();
		}
		return clone;
	}

	
}
