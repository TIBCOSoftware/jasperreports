/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
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


}
