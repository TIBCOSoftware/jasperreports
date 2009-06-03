/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExpressionUtil
{

	
	/**
	 *
	 */
	public static String getExpressionText(JRExpression exp)
	{
		return exp == null ? null : exp.getText();
	}


	private static final Pattern SIMPLE_EXPRESSION_PATTERN = 
		Pattern.compile("\\s*\"([^\"]*((?<=\\\\)\"[^\"]*)*)\"\\s*");
	
	private static final int SIMPLE_EXPRESSION_TEXT_GROUP = 1;
	
	private static final Pattern TEXT_QUOTE_PATTERN = Pattern.compile("\\\\\"");
	private static final String TEXT_QUOTE_REPLACEMENT = "\"";
	
	/**
	 *
	 */
	public static String getSimpleExpressionText(JRExpression expression)
	{
		String value = null;
		if (expression != null)
		{
			JRExpressionChunk[] chunks = expression.getChunks();
			if (
				chunks != null 
				&& chunks.length == 1 
				&& chunks[0].getType() == JRExpressionChunk.TYPE_TEXT
				)
			{
				String chunk = chunks[0].getText();
				Matcher matcher = SIMPLE_EXPRESSION_PATTERN.matcher(chunk);
				if (matcher.matches())
				{
					String text = matcher.group(SIMPLE_EXPRESSION_TEXT_GROUP);
					value = TEXT_QUOTE_PATTERN.matcher(text).replaceAll(
							TEXT_QUOTE_REPLACEMENT);
				}
			}
		}
		return value;
	}


}
