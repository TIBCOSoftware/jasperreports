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
package net.sf.jasperreports.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRRuntimeException;


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
			StringBuffer textChunk = new StringBuffer();
			
			StringTokenizer tkzer = new StringTokenizer(text, "$", true);
			boolean wasDelim = false;
			while (tkzer.hasMoreTokens())
			{
				String token = tkzer.nextToken();
	
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
						int end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								chunkHandler.handleTextChunk(textChunk.toString());					
							}
							String parameterChunk = token.substring(2, end);
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
						int end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								chunkHandler.handleTextChunk(textChunk.toString());					
							}
							String parameterClauseChunk = token.substring(3, end);
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
					else if ( token.startsWith("X{") && wasDelim )
					{
						int end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								chunkHandler.handleTextChunk(textChunk.toString());					
							}
							String clauseChunk = token.substring(2, end);
							String[] tokens = parseClause(clauseChunk);
							chunkHandler.handleClauseChunk(tokens);					
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
	
	
	protected String[] parseClause(String clauseChunk)
	{
		List<String> tokens = new ArrayList<String>();
		
		boolean wasClauseToken = false;
		String separator = determineClauseTokenSeparator(clauseChunk);
		StringTokenizer tokenizer = new StringTokenizer(clauseChunk, separator, true);
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			if (token.equals(separator))
			{
				if (!wasClauseToken)
				{
					tokens.add("");
				}
				wasClauseToken = false;
			}
			else
			{
				tokens.add(token);
				wasClauseToken = true;
			}
		}
		if (!wasClauseToken)
		{
			tokens.add("");
		}
		
		return tokens.toArray(new String[tokens.size()]);
	}

	protected String determineClauseTokenSeparator(String clauseChunk)
	{
		String allSeparators = getTokenSeparators();
		if (allSeparators == null || allSeparators.length() == 0)
		{
			throw new JRRuntimeException("No token separators configured");
		}
		
		int firstSepIdx = 0;//if none of the separators are found in the text, return the first separator
		int clauseLenght = clauseChunk.length();
		for (int idx = 0; idx < clauseLenght; ++idx)
		{
			int sepIdx = allSeparators.indexOf(clauseChunk.charAt(idx));
			if (sepIdx >= 0)
			{
				firstSepIdx = sepIdx;
				break;
			}
		}
		
		return String.valueOf(allSeparators.charAt(firstSepIdx));
	}


	protected String getTokenSeparators()
	{
		return JRProperties.getProperty(JRQueryChunk.PROPERTY_CHUNK_TOKEN_SEPARATOR);
	}

	/**
	 * (Re)creates the query text from a list of chunks.
	 * 
	 * @param chunks the chunks
	 * @return the recreated query text
	 */
	public String asText(JRQueryChunk[] chunks)
	{
		String text = "";
		
		if (chunks != null && chunks.length > 0)
		{
			StringBuffer sbuffer = new StringBuffer();

			for(int i = 0; i < chunks.length; i++)
			{
				JRQueryChunk queryChunk = chunks[i];
				switch(queryChunk.getType())
				{
					case JRQueryChunk.TYPE_PARAMETER :
					{
						sbuffer.append("$P{");
						sbuffer.append( queryChunk.getText() );
						sbuffer.append("}");
						break;
					}
					case JRQueryChunk.TYPE_PARAMETER_CLAUSE :
					{
						sbuffer.append("$P!{");
						sbuffer.append( queryChunk.getText() );
						sbuffer.append("}");
						break;
					}
					case JRQueryChunk.TYPE_CLAUSE_TOKENS :
					{
						sbuffer.append("$X{");
						sbuffer.append(queryChunk.getText());
						sbuffer.append("}");
						break;
					}
					case JRQueryChunk.TYPE_TEXT :
					default :
					{
						sbuffer.append( queryChunk.getText() );
						break;
					}
				}
			}

			text = sbuffer.toString();
		}
		
		return text;
	}
	
	/**
	 * (Re)constructs a query clause chunk from the chunk tokens.
	 * 
	 * @param tokens the chunk tokens
	 * @return the reconstructed query clause chunk
	 * @see JRQueryChunk#TYPE_CLAUSE_TOKENS
	 */
	public String asClauseText(String[] tokens)
	{
		StringBuffer sb = new StringBuffer();
		if (tokens != null && tokens.length > 0)
		{
			for (int i = 0; i < tokens.length; i++)
			{
				if (i > 0)
				{
					sb.append(',');
				}
				String token = tokens[i];
				if (token != null)
				{
					sb.append(token);
				}
			}
		}
		return sb.toString();
	}
	
}
