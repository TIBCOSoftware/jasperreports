/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
 * @see JasperReport#getCompileData()
 * @see JavaScriptCompiler
 */
public class JavaScriptCompileData implements Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String EXCEPTION_MESSAGE_KEY_EXPRESSION_NOT_FOUND = "compilers.javascript.expression.not.found";
	
	protected static class Expression implements Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		/**
		 * @deprecated To be removed.
		 */
		private final String type;
		private final String defaultExpression;
		private final String oldExpression;
		private final String estimatedExpression;
		
		/**
		 * @deprecated To be removed.
		 */
		public Expression(String type, String defaultExpression,
				String estimatedExpression, String oldExpression)
		{
			this.type = type;
			this.defaultExpression = defaultExpression;
			this.estimatedExpression = estimatedExpression;
			this.oldExpression = oldExpression;
		}

		public Expression(String defaultExpression, String estimatedExpression, String oldExpression)
		{
			this.type = null;
			this.defaultExpression = defaultExpression;
			this.estimatedExpression = estimatedExpression;
			this.oldExpression = oldExpression;
		}

		/**
		 * @deprecated To be removed.
		 */
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
	
	private final List<Expression> expressions = new ArrayList<Expression>();
	
	public void addExpression(int expressionId, Expression expression)
	{
		for (int idx = expressions.size(); idx <= expressionId; ++idx)
		{
			expressions.add(idx, null);
		}
		expressions.set(expressionId, expression);
	}
	
	public void addExpression(int expressionId, 
			String defaultExpression, String estimatedExpression, String oldExpression)
	{
		Expression expression = new Expression(defaultExpression, estimatedExpression, oldExpression);
		addExpression(expressionId, expression);
	}
	
	public Expression getExpression(int id)
	{
		if (id >= expressions.size())
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_EXPRESSION_NOT_FOUND,
					new Object[]{id});
		}
		Expression expr = expressions.get(id);
		if (expr == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_EXPRESSION_NOT_FOUND,
					new Object[]{id});
		}
		return expr;
	}

}
