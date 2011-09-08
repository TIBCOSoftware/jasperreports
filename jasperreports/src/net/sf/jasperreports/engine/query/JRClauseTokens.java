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
package net.sf.jasperreports.engine.query;

import net.sf.jasperreports.engine.JRQueryChunk;


/**
 * Query clause chunk wrapper.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRQueryChunk#getTokens()
 * @see JRQueryChunk#TYPE_CLAUSE_TOKENS
 */
public class JRClauseTokens
{

	private final String[] tokens;

	/**
	 * Wraps an array of tokens.
	 * 
	 * @param tokens the tokens
	 */
	public JRClauseTokens(final String[] tokens)
	{
		this.tokens = tokens;
	}
	
	/**
	 * Returns a token at a specified position.
	 * <p>
	 * If the specified position is greater than the number of tokens or
	 * the token is empty, the method returns <code>null</code>.
	 * </p>
	 * 
	 * @param position the position
	 * @return the token at the specified position 
	 */
	public String getToken(int position)
	{
		String token;
		if (tokens == null || tokens.length <= position)
		{
			token = null;
		}
		else
		{
			token = tokens[position];
		}
		if (token != null)
		{
			token = token.trim();
			if (token.length() == 0)
			{
				token = null;
			}
		}
		return token;
	}

}
