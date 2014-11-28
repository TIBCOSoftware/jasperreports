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

/*
 * Contributors:
 * Gaganis Giorgos - gaganis@users.sourceforge.net
 */
package net.sf.jasperreports.engine.util;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.fasterxml.jackson.core.io.JsonStringEncoder;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class JRStringUtil
{

	protected static final String JAVA_IDENTIFIER_PREFIX = "j";

	/**
	 * This method replaces all occurrences of the CR character with the LF character, 
	 * except when the CR is immediately followed by a LF (CRLF sequences), in which case the CR is removed.
	 */
	public static String replaceCRwithLF(String text)
	{
		if (text != null)
		{
			int length = text.length();
			char[] chars = text.toCharArray();
			int r = 0;
			boolean dirty = false;
			for (int i = 0; i < length; ++i)
			{
				char ch = chars[i];
				if (ch == '\r')
				{
					dirty = true;
					if (i + 1 < length && chars[i + 1] == '\n')
					{
						r++;
					}
					else
					{
						chars[i - r] = '\n';
					}
				}
				else
				{
					chars[i - r] = ch;
				}
			}

			return dirty ? new String(chars, 0, length - r) : text;
		}
		return null;
	}


	/**
	 *
	 */
	public static String xmlEncode(String text)
	{
		return xmlEncode(text, null);
	}


	/**
	 *
	 */
	public static String xmlEncode(String text, String invalidCharReplacement)
	{
		if (text == null || text.length() == 0)
		{
			return text;
		}
		
		int length = text.length();
		StringBuffer ret = new StringBuffer(length * 12 / 10);
		int last = 0;
		
		for (int i = 0; i < length; i++)
		{
			char c = text.charAt(i);
			if(Character.isISOControl(c) && c!='\t' && c!='\r' && c!='\n')
			{
				last = appendText(text, ret, i, last);
				if(invalidCharReplacement == null)
				{
					//the invalid character is preserved
					ret.append(c);
				}
				else if(invalidCharReplacement.length() == 0)
				{
					//the invalid character is removed
					continue;
				}
				else
				{
					//the invalid character is replaced
					ret.append(invalidCharReplacement);
				}
			}
			else
			{
				switch (c)
				{
					case '&' :
						last = appendText(text, ret, i, last);
						ret.append("&amp;");
						break;
					case '>' :
						last = appendText(text, ret, i, last);
						ret.append("&gt;");
						break;
					case '<' :
						last = appendText(text, ret, i, last);
						ret.append("&lt;");
						break;
					case '\"' :
						last = appendText(text, ret, i, last);
						ret.append("&quot;");
						break;
					case '\'' :
						last = appendText(text, ret, i, last);
						ret.append("&apos;");
						break;
					default :
						break;
				}
			}
		}
		appendText(text, ret, length, last);
		return ret.toString();
	}
	
	public static String encodeXmlAttribute(String text)
	{
		if (text == null || text.length() == 0)
		{
			return text;
		}
		
		int length = text.length();
		StringBuffer ret = new StringBuffer(length * 12 / 10);//FIXME avoid creating this when not necessary
		int last = 0;
		for (int i = 0; i < length; i++)
		{
			char c = text.charAt(i);
			switch (c)
			{
				case '&' :
					last = appendText(text, ret, i, last);
					ret.append("&amp;");
					break;
				case '>' :
					last = appendText(text, ret, i, last);
					ret.append("&gt;");
					break;
				case '<' :
					last = appendText(text, ret, i, last);
					ret.append("&lt;");
					break;
				case '\"' :
					last = appendText(text, ret, i, last);
					ret.append("&quot;");
					break;
				case '\'' :
					last = appendText(text, ret, i, last);
					ret.append("&apos;");
					break;
				// encoding tabs and newlines because otherwise they get replaced by spaces on parsing
				case '\t' :
					last = appendText(text, ret, i, last);
					ret.append("&#x9;");
					break;
				case '\r' :
					last = appendText(text, ret, i, last);
					ret.append("&#xD;");
					break;
				case '\n' :
					last = appendText(text, ret, i, last);
					ret.append("&#xA;");
					break;
				default :
					// not checking for invalid characters, preserving them as per xmlEncode() with invalidCharReplacement = null
					break;
			}
		}
		
		if (last == 0)
		{
			// no changes made to the string
			return text;
		}
		
		appendText(text, ret, length, last);
		return ret.toString();
	}
	
	private static int appendText(String text, StringBuffer ret, int current, int old)
	{
		if(old < current)
		{
			ret.append(text.substring(old, current));
		}
		return current+1;
	}
	
	/**
	 *
	 */
	public static String htmlEncode(String text)
	{
		if (text == null || text.length() == 0)
		{
			return text;
		}
		
		int length = text.length();
		StringBuffer ret = new StringBuffer(length * 12 / 10);

		boolean isEncodeSpace = true;
		int last = 0;
		for (int i = 0; i < length; i ++)
		{
			char c = text.charAt(i);
			switch (c)
			{
				case ' ' : 
					if (isEncodeSpace)
					{
						if (last < i)
						{
							ret.append(text.substring(last, i));
						}
						last = i + 1;
						
						ret.append("&nbsp;");
						isEncodeSpace = false;
					}
					else
					{
						isEncodeSpace = true;
					}
					break;
				case '&' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&amp;");
					isEncodeSpace = false;
					break;
				case '>' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&gt;");
					isEncodeSpace = false;
					break;
				case '<' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&lt;");
					isEncodeSpace = false;
					break;
				case '\"' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&quot;");
					isEncodeSpace = false;
					break;
// it does not work in IE
//					case '\'' :
//						if (last < i)
//						{
//							ret.append(text.substring(last, i));
//						}
//						last = i + 1;
//						
//						ret.append("&apos;");
//						isEncodeSpace = false;
//						break;
				case '\n' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("<br/>");
					isEncodeSpace = false;
					break;

				default :
					isEncodeSpace = false;
				break;
			}
		}

		if (last < length)
		{
			ret.append(text.substring(last));
		}

		return ret.toString();
	}


	/**
	 * Takes a name and returns the same if it is a Java identifier;
	 * else it substitutes the illegal characters so that it can be an identifier
	 *
	 * @param name
	 */
	public static String getJavaIdentifier(String name)
	{
		if (isValidJavaIdentifier(name))
		{
			return name;
		}

		StringBuffer buffer = new StringBuffer(name.length() + 5);
		
		char[] literalChars = new char[name.length()];
		name.getChars(0, literalChars.length, literalChars, 0);
		
		for (int i = 0; i < literalChars.length; i++)
		{
			if (i == 0 && !Character.isJavaIdentifierStart(literalChars[i]))
			{
				buffer.append(JAVA_IDENTIFIER_PREFIX);
				buffer.append((int)literalChars[i]);
			}
			else if (i != 0 && !Character.isJavaIdentifierPart(literalChars[i]))
			{
				buffer.append((int)literalChars[i]);
			}
			else
			{
				buffer.append(literalChars[i]);
			}
		}
		
		return buffer.toString();
	}
	
	
	/**
	 * Checks if the input is a valid Java identifier
	 * @param literal
	 * @author Gaganis Giorgos (gaganis@users.sourceforge.net) 
	 */
	private static boolean isValidJavaIdentifier(String literal)
	{
		boolean result = true;
		
		char[] literalChars = new char[literal.length()];
		literal.getChars(0, literalChars.length, literalChars, 0);
		
		for (int i = 0; i < literalChars.length; i++)
		{
			if (i == 0 && !Character.isJavaIdentifierStart(literalChars[i]))
			{
				result = false;
				break;
			}
			
			if (i != 0 && !Character.isJavaIdentifierPart(literalChars[i]))
			{
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Escapes a text so that it can be used as a Java String literal.
	 * 
	 * @param text the text
	 * @return the text with escaped quotes and backslashes
	 */
	public static String escapeJavaStringLiteral(String text)
	{
		if (text == null)
		{
			return text;
		}
		
		StringBuffer sbuffer = new StringBuffer();
		StringTokenizer tkzer = new StringTokenizer(text, "\\\"\n\r\t", true);
		while(tkzer.hasMoreTokens())
		{
			String token = tkzer.nextToken();
			//TODO optimize ifs?
			if ("\\".equals(token))
			{
				sbuffer.append("\\\\");
			}
			else if ("\"".equals(token))
			{
				sbuffer.append("\\\"");
			}
			else if ("\n".equals(token))
			{
				sbuffer.append("\\n");
			}
			else if ("\r".equals(token))
			{
				sbuffer.append("\\r");
			}
			else if ("\t".equals(token))
			{
				sbuffer.append("\\t");
			}
			else
			{
				sbuffer.append(token);
			}
		}
		
		return sbuffer.toString();
	}
	
	/**
	 * Escapes a text so that it can be used as a Java String literal.
	 * 
	 * @param input
	 */
	public static String escapeJavaScript(String input) {
		if (input == null) {
			return input;
		}

		StringBuilder filtered = new StringBuilder(input.length());
		char prevChar = '\u0000';
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == '"') {
				filtered.append("\\\"");
			}
			else if (c == '\'') {
				filtered.append("\\'");
			}
			else if (c == '\\') {
				filtered.append("\\\\");
			}
			else if (c == '/') {
				filtered.append("\\/");
			}
			else if (c == '\t') {
				filtered.append("\\t");
			}
			else if (c == '\n') {
				if (prevChar != '\r') {
					filtered.append("\\n");
				}
			}
			else if (c == '\r') {
				filtered.append("\\n");
			}
			else if (c == '\f') {
				filtered.append("\\f");
			}
			else {
				filtered.append(c);
			}
			prevChar = c;

		}
		return filtered.toString();
	}

	/**
	 * Escapes a Java String so that it can be used as a JavaScript String literal.
	 * 
	 * @param input
	 */
	public static String escapeString4JavaScript(String input) {
		if (input == null) {
			return input;
		}
		
		StringBuilder filtered = new StringBuilder(input.length());
		char prevChar = '\u0000';
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == '"') {
				filtered.append("\\\"");
			}
			else if (c == '\\') {
				filtered.append("\\\\");
			}
			else if (c == '/') {
				filtered.append("\\/");
			}
			else if (c == '\t') {
				filtered.append("\\t");
			}
			else if (c == '\n') {
				if (prevChar != '\r') {
					filtered.append("\\n");
				}
			}
			else if (c == '\r') {
				filtered.append("\\n");
			}
			else if (c == '\f') {
				filtered.append("\\f");
			}
			else {
				filtered.append(c);
			}
			prevChar = c;
			
		}
		return filtered.toString();
	}
	
	
	/**
	 * 
	 */
	public static List<Integer> getTabIndexes(String text)
	{
		List<Integer> tabIndexes = null;
		
		if (text != null)
		{
			int index = text.indexOf('\t');
			// returning null if no tabs
			if (index >= 0)
			{
				tabIndexes = new ArrayList<Integer>();
				do
				{
					tabIndexes.add(index);
					index = text.indexOf('\t', index + 1);
				}
				while (index >= 0);
			}
		}
		
		return tabIndexes;
	}
	

	/**
	 * 
	 */
	public static List<String> split(String[] srcArray, String delimiterRegExp)
	{
		List<String> tokens = null;
		if (srcArray != null)
		{
			tokens = new ArrayList<String>();
			for(int i = 0; i < srcArray.length; i++)
			{
				if (srcArray[i] == null)
				{
					tokens.add(null);
				}
				else
				{
					String[] currentTokensArray = srcArray[i].split(delimiterRegExp);
					for(int j = 0; j < currentTokensArray.length; j++)
					{
						tokens.add(currentTokensArray[j].trim());
					}
				}
			}
		}
		return tokens;
	}

	
	/**
	 * 
	 */
	public static String getString(Object value)
	{
		return value == null ? null : value.toString();
	}
	
	/**
	 * Escapes a text to be used for a JSON string value.
	 * 
	 * @param text the text to escape for JSON
	 * @return the escaped text if not null
	 */
	public static String escapeJSONString(String text)
	{
		if (text == null)
		{
			return null;
		}
		
		// using Jackson's string quote method
		char[] escapedChars = JsonStringEncoder.getInstance().quoteAsString(text);
		if (text.contentEquals(CharBuffer.wrap(escapedChars)))
		{
			// nothing changed
			return text;
		}
		
		return String.valueOf(escapedChars);
	}
	

	private JRStringUtil()
	{
	}
}
