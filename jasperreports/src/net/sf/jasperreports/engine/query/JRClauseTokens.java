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
