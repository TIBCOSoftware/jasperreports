/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
	 * This method inserts a blank character between to consecutive
	 * newline characters if encoutered. Also appends a blank character at
	 * the beginning of the text, if the first character is a newline character
	 * and at the end of the text, if the last character is also a newline.
	 * This is useful when trying to layout the paragraphs.
	 */
	public static String treatNewLineChars(String source)
	{
		String result = source;

		if (source != null && source.length() > 0)
		{
			StringBuffer sbuffer = new StringBuffer(source);
			
			// insert a blank character between every two consecutives
			// newline characters
			int offset = source.length() - 1;
			int pos = source.lastIndexOf("\n\n", offset);
			while (pos >= 0 && offset > 0)
			{
				sbuffer = sbuffer.insert(pos + 1, " ");
				offset = pos - 1;
				pos = source.lastIndexOf("\n\n", offset);
			}
			
			// append a blank character at the and of the text
			// if the last character is a newline character
			if (sbuffer.charAt(sbuffer.length() - 1) == '\n')
			{
				sbuffer.append(' ');
			}

			// append a blank character at the begining of the text
			// if the first character is a newline character
			if (sbuffer.charAt(0) == '\n')
			{
				sbuffer.insert(0, ' ');
			}
			
			result = sbuffer.toString();
		}
		
		// remove this if you want to treat the tab characters in a special way
		result = replaceTabWithBlank(result);
		
		return result;
	}
		

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
