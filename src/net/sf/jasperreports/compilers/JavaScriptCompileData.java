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
package net.sf.jasperreports.compilers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Compile data for reports that use JavaScript as expression language.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JasperReport#getCompileData()
 */
public class JavaScriptCompileData implements Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected static class Expression implements Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		private final String type;
		private final String defaultExpression;
		private final String oldExpression;
		private final String estimatedExpression;
		
		public Expression(String type, String defaultExpression,
				String estimatedExpression, String oldExpression)
		{
			this.type = type;
			this.defaultExpression = defaultExpression;
			this.estimatedExpression = estimatedExpression;
			this.oldExpression = oldExpression;
		}

		public String getJavaType()
		{
			return type;
		}

		public String getDefaultExpression()
		{
			return defaultExpression;
		}

		public String getOldExpression()
		{
			return oldExpression;
		}

		public String getEstimatedExpression()
		{
			return estimatedExpression;
		}
	}
	
	private final List expressions = new ArrayList();
	
	public void addExpression(int expressionId, Expression expression)
	{
		for (int idx = expressions.size(); idx <= expressionId; ++idx)
		{
			expressions.add(idx, null);
		}
		expressions.set(expressionId, expression);
	}
	
	public Expression getExpression(int id)
	{
		if (id >= expressions.size())
		{
			throw new JRRuntimeException("No expression for id " + id);
		}
		Expression expr = (Expression) expressions.get(id);
		if (expr == null)
		{
			throw new JRRuntimeException("No expression for id " + id);
		}
		return expr;
	}

}
