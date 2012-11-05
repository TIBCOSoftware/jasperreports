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
