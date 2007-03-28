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

import java.util.StringTokenizer;


/**
 * Report query parser.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRQueryParser
{

	private static final JRQueryParser singleton = new JRQueryParser();
	
	/**
	 * Returns a query parser instance.
	 * 
	 * @return a query parser instance
	 */
	public static JRQueryParser instance()
	{
		return singleton;
	}
	
	
	/**
	 * Parses a report query.
	 * 
	 * @param text the query text
	 * @param chunkHandler a handler that will be asked to handle parsed query chunks
	 */
	public void parse(String text, JRQueryChunkHandler chunkHandler)
	{
		if (text != null)
		{
			int end = 0;
			StringBuffer textChunk = new StringBuffer();
			String parameterChunk = null;
			String parameterClauseChunk = null;
			
			StringTokenizer tkzer = new StringTokenizer(text, "$", true);
			String token = null;
			boolean wasDelim = false;
			while (tkzer.hasMoreTokens())
			{
				token = tkzer.nextToken();
	
				if (token.equals("$"))
				{
					if (wasDelim)
					{
						textChunk.append("$");
					}
	
					wasDelim = true;
				}
				else
				{
					if ( token.startsWith("P{") && wasDelim )
					{
						end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								chunkHandler.handleTextChunk(textChunk.toString());					
							}
							parameterChunk = token.substring(2, end);
							chunkHandler.handleParameterChunk(parameterChunk);					
							textChunk = new StringBuffer(token.substring(end + 1));
						}
						else
						{
							if (wasDelim)
							{
								textChunk.append("$");
							}
							textChunk.append(token);
						}
					}
					else if ( token.startsWith("P!{") && wasDelim )
					{
						end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								chunkHandler.handleTextChunk(textChunk.toString());					
							}
							parameterClauseChunk = token.substring(3, end);
							chunkHandler.handleParameterClauseChunk(parameterClauseChunk);					
							textChunk = new StringBuffer(token.substring(end + 1));
						}
						else
						{
							if (wasDelim)
							{
								textChunk.append("$");
							}
							textChunk.append(token);
						}
					}
					else
					{
						if (wasDelim)
						{
							textChunk.append("$");
						}
						textChunk.append(token);
					}
	
					wasDelim = false;
				}
			}
			if (wasDelim)
			{
				textChunk.append("$");
			}
			if (textChunk.length() > 0)
			{
				chunkHandler.handleTextChunk(textChunk.toString());					
			}
		}
	}
	
}
