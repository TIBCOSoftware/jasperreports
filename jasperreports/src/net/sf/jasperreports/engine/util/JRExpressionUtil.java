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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
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
				String chunk = chunks[0].getText().trim();
				int chunkLength = chunk.length();
				if (chunk.charAt(0) == '"' && chunk.charAt(chunkLength - 1) == '"')
				{
					value = chunk.substring(1, chunkLength - 1);
				}
			}
		}
		return value;
	}


}
