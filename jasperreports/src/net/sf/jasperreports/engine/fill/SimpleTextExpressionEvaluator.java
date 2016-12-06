/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.type.ExpressionTypeEnum;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SimpleTextExpressionEvaluator
{

	public static String evaluateExpression(JRExpression expression, ExpressionValues values)
	{
		if (expression.getType() != ExpressionTypeEnum.SIMPLE_TEXT)
		{
			throw new IllegalArgumentException("Simple text expression expected");
		}
		
		JRExpressionChunk[] chunks = expression.getChunks();
		if (chunks == null || chunks.length == 0)
		{
			return null;
		}
		
		if (chunks.length == 1)
		{
			//avoid string builder
			return chunkValue(chunks[0], values);
		}
		
		StringBuilder result = new StringBuilder();
		for (JRExpressionChunk chunk : chunks)
		{
			String chunkValue = chunkValue(chunk, values);
			result.append(chunkValue);
		}
		return result.toString();
	}
	
	private static String chunkValue(JRExpressionChunk chunk, ExpressionValues values)
	{
		String value;
		switch (chunk.getType())
		{
		case JRExpressionChunk.TYPE_TEXT:
			value = chunk.getText();
			break;

		case JRExpressionChunk.TYPE_RESOURCE:
			value = values.getMessage(chunk.getText());
			break;

		case JRExpressionChunk.TYPE_PARAMETER:
			value = stringValue(values.getParameterValue(chunk.getText()));
			break;

		case JRExpressionChunk.TYPE_FIELD:
			value = stringValue(values.getFieldValue(chunk.getText()));
			break;

		case JRExpressionChunk.TYPE_VARIABLE:
			value = stringValue(values.getVariableValue(chunk.getText()));
			break;

		default:
			throw new JRRuntimeException("Unknown expression chunk type " + chunk.getType());
		}
		//TODO lucianc recurse
		return value;
	}
	
	private static String stringValue(Object value)
	{
		return String.valueOf(value);
	}
	
}
