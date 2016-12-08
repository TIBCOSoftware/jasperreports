/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ExpressionParser
{

	private static final ExpressionParser INSTANCE = new ExpressionParser();
	
	public static ExpressionParser instance()
	{
		return INSTANCE;
	}
	
	public static final Pattern PLACEHOLDER_PATTERN = 
			Pattern.compile("\\$([RPFV])\\{(.*?)\\}", Pattern.MULTILINE | Pattern.DOTALL);
	
	protected static final int PLACEHOLDER_TYPE_INDEX = 1;
	protected static final int PLACEHOLDER_TEXT_INDEX = 2;
	
	public static interface ParseResult
	{
		void addTextChunk(String text);

		void addChunk(byte chunkType, String chunkText);
	}
	
	public void parseExpression(String text, ParseResult result)
	{
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);

		int textChunkStart = 0;
		StringBuilder textChunk = new StringBuilder(text.length());
		while(matcher.find())
		{
			int matchStart = matcher.start();
			int matchEnd = matcher.end();
			if (matchStart > 0 && text.charAt(matchStart - 1) == '$')
			{
				// we have a $$ escape, append it to the text chunk with a single $
				textChunk.append(text, textChunkStart, matchStart - 1);
				textChunk.append(text, matchStart, matchEnd);
			}
			else
			{
				// we have a proper placeholder
				textChunk.append(text, textChunkStart, matchStart);
				if (textChunk.length() > 0)
				{
					result.addTextChunk(textChunk.toString());
					textChunk.delete(0, textChunk.length());
				}
				
				String chunkStringType = matcher.group(PLACEHOLDER_TYPE_INDEX);
				byte chunkType = chunkStringToType(chunkStringType);
				String chunkText = matcher.group(PLACEHOLDER_TEXT_INDEX);
				result.addChunk(chunkType, chunkText);
			}
			
			textChunkStart = matchEnd;
		}
		
		textChunk.append(text, textChunkStart, text.length());
		if (textChunk.length() > 0)
		{
			result.addTextChunk(textChunk.toString());
		}
	}
	
	protected byte chunkStringToType(String chunkStringType)
	{
		byte chunkType;
		//FIXME faster way to do this
		if (chunkStringType.startsWith("P"))
		{
			chunkType = JRExpressionChunk.TYPE_PARAMETER;
		}
		else if (chunkStringType.startsWith("F"))
		{
			chunkType = JRExpressionChunk.TYPE_FIELD;
		}
		else if (chunkStringType.startsWith("V"))
		{
			chunkType = JRExpressionChunk.TYPE_VARIABLE;
		}
		else if (chunkStringType.startsWith("R"))
		{
			chunkType = JRExpressionChunk.TYPE_RESOURCE;
		}
		else
		{
			throw 
				new JRRuntimeException(
					JRExpressionUtil.EXCEPTION_MESSAGE_KEY_UNKNOWN_EXPRESSION_CHUNK_TYPE,
					new Object[]{chunkStringType});
		}
		return chunkType;
	}
}
