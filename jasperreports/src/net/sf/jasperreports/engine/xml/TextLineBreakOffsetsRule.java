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
package net.sf.jasperreports.engine.xml;

import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRPrintText;

import org.apache.commons.digester.Rule;

/**
 * A digester rule that parses comma-separated values for
 * {@link JRPrintText#setLineBreakOffsets(short[])}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TextLineBreakOffsetsRule extends Rule
{

	public void body(String namespace, String name, String text)
			throws Exception
	{
		if (text != null)
		{
			StringTokenizer tokenizer = new StringTokenizer(text, 
					JRXmlConstants.LINE_BREAK_OFFSET_SEPARATOR);
			int tokenCount = tokenizer.countTokens();
			short[] offsets;
			if (tokenCount == 0)
			{
				//use the zero length array singleton
				offsets = JRPrintText.ZERO_LINE_BREAK_OFFSETS;
			}
			else
			{
				offsets = new short[tokenCount];
				for (int i = 0; i < offsets.length; i++)
				{
					String token = tokenizer.nextToken();
					offsets[i] = Short.parseShort(token);
				}
			}
			
			JRPrintText printText = (JRPrintText) getDigester().peek();
			printText.setLineBreakOffsets(offsets);
		}
	}
	
}
