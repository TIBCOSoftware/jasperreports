/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Gaganis Giorgos - gaganis@users.sourceforge.net
 */
package net.sf.jasperreports.engine.util;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStringUtil
{


	/**
	 *
	 */
	public static String replaceTabWithBlank(String source)
	{
		String result = source;

		if (source != null && source.length() > 0)
		{
			StringBuffer sbuffer = new StringBuffer(source);
			
			int offset = 0;
			int pos = source.indexOf("\t", offset);
			while (pos >= 0)
			{
				sbuffer.setCharAt(pos, ' ');
				offset = pos + 1;
				pos = source.indexOf("\t", offset);
			}
			
			result = sbuffer.toString();
		}
		
		return result;
	}
		

	/**
	 *
	 */
	public static String xmlEncode(String text)
	{
		if (text != null)
		{
			StringBuffer ret = new StringBuffer();

			for (int i = 0; i < text.length(); i ++)
			{
				switch (text.charAt(i))
				{
		//			case ' ' : ret.append("&nbsp;"); break;
					case '&' : ret.append("&amp;"); break;
					case '>' : ret.append("&gt;"); break;
					case '<' : ret.append("&lt;"); break;
					case '\"' : ret.append("&quot;"); break;

					default : ret.append(text.substring(i, i + 1)); break;
				}
			}

			return ret.toString();
		}
		else
		{
			return null;
		}
	}


	/**
	 * Takes a name and returns the same if it is a Java identifier;
	 * else it substitutes the illegal characters so that it can be an identifier
	 *
	 * @param name
	 * @return
	 * @author Gaganis Giorgos (gaganis@users.sourceforge.net) 
	 */
	public static String getLiteral(String name)
	{
		if (isValidLiteral(name))
		{
			return name;
		}
		else
		{
			StringBuffer buffer = new StringBuffer(name.length() + 5);
			
			char[] literalChars = new char[name.length()];
			name.getChars(0, literalChars.length, literalChars, 0);
			
			for (int i = 0; i < literalChars.length; i++)
			{
				if (i == 0 && !Character.isJavaIdentifierStart(literalChars[i]))
				{
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
	}
	
	
	/**
	 * Checks if the input is a valid Java literal
	 * @param literal
	 * @return
	 * @author Gaganis Giorgos (gaganis@users.sourceforge.net) 
	 */
	private static boolean isValidLiteral(String literal)
	{
		boolean result = true;
		
		char[] literalChars = new char[literal.length()];
		literal.getChars(0, literalChars.length, literalChars, 0);
		
		for (int i = 0; i < literalChars.length; i++)
		{
			if (i == 0 && !Character.isJavaIdentifierStart(literalChars[i]))
			{
				result = false;
			}
			
			if (i != 0 && !Character.isJavaIdentifierPart(literalChars[i]))
			{
				result = false;
			}
		}
		
		return result;
	}

	
}
