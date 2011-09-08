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

/*
 * Contributors:
 * Gaganis Giorgos - gaganis@users.sourceforge.net
 */
package net.sf.jasperreports.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;




/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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
		if (text == null || text.length() == 0)
		{
			return text;
		}
		
		int length = text.length();
		StringBuffer ret = new StringBuffer(length * 12 / 10);

		int last = 0;
		for (int i = 0; i < length; i ++)
		{
			char c = text.charAt(i);
			switch (c)
			{
//			case ' ' : ret.append("&nbsp;"); break;
				case '&' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&amp;");
					break;
				case '>' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&gt;");
					break;
				case '<' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&lt;");
					break;
				case '\"' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&quot;");
					break;
				case '\'' :
					if (last < i)
					{
						ret.append(text.substring(last, i));
					}
					last = i + 1;
					
					ret.append("&apos;");
					break;

				default :
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
	 * 
	 */
	public static List<Integer> getTabIndexes(String text)
	{
		List<Integer> tabIndexes = null;
		
		if (text != null)
		{
			tabIndexes = new ArrayList<Integer>();
			
			for (int i = 0; i < text.length(); i++)
			{
				if (text.charAt(i) == '\t') 
				{
					tabIndexes.add(Integer.valueOf(i));
				}
			}
		}
		
		return tabIndexes;
	}
	

	/**
	 * 
	 */
	public static String getString(Object value)
	{
		return value == null ? null : value.toString();
	}
	

	private JRStringUtil()
	{
	}
}
