/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

package net.sf.jasperreports.engine.export.xmlss;

import java.util.StringTokenizer;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class XmlssUtil {

	public static String getIndent(int i) {
		String indent = "";
		for (int j=0; j<i; j++) {
			indent += " ";
		}
		return indent;
	}
	
	public static double translatePixelsToInches(double pixels){
		double inches = pixels/72.0;
		inches = (Math.floor(inches * 100.0))/100.0;
		return inches;
	}
	public static double translatePixelsToInchesRound(double pixels){
		double inches = pixels/72.0;
		inches = (Math.round(inches * 100.0))/100.0;
		return inches;
	}

	public static double translatePixelsToInchesWithNoRoundOff(double pixels){
		double inches = pixels/72.0;
		return inches;
	}
	
	protected static String replaceNewLineWithLineBreak(String source)
	{
		String str = null;
		
		if (source != null)
		{
			StringBuffer sbuffer = new StringBuffer();
			StringTokenizer tkzer = new StringTokenizer(source, "\n", true);
			String token = null;
			while(tkzer.hasMoreTokens())
			{
				token = tkzer.nextToken();
				if ("\n".equals(token))
				{
					sbuffer.append("<text:line-break/>");
				}
				else
				{
					sbuffer.append(token);
				}
			}
			
			str = sbuffer.toString();
		}
		
		return str;
	}
	
}
