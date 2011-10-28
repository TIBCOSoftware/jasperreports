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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.type.JsonOperatorEnum;

import org.codehaus.jackson.JsonNode;

/**
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JsonUtil {
	
	public static boolean evaluateJsonExpression(JsonNode contextNode, String attributeExpression) throws JRException {
		
		if (attributeExpression == null) {
			return true;
		}
		
		String attribute = null;
		JsonOperatorEnum operator = null;
		String value = null;
		boolean result = false;
		
		for (JsonOperatorEnum joe: JsonOperatorEnum.values()) {
			int indexOfOperator = attributeExpression.indexOf(joe.getValue());
			if (indexOfOperator != -1) {
				operator = joe;
				attribute = attributeExpression.substring(0, indexOfOperator).trim();
				value = attributeExpression.substring(indexOfOperator + joe.getValue().length()).trim();
				break;
			}
		}
		
		if (operator == null) {
			StringBuffer possibleOperations = new StringBuffer();
			for (JsonOperatorEnum op: JsonOperatorEnum.values()) {
				possibleOperations.append(op.getValue()).append(",");
			}
			throw new JRException("Unknown operator in expression: " + attributeExpression + "; Operator must be one of: " + possibleOperations);
		}
		
		if (attribute != null && operator != null && value != null) {
			// going down the path of the attribute must return a value node 
			if (!contextNode.path(attribute).isValueNode()) {
				result = false;	
			} else {
				String contextValue = contextNode.path(attribute).getValueAsText();
				switch(operator) {
				case LT:
					try {
						result = Double.parseDouble(contextValue) < Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						result = false;
					}
					break;
				case LE:
					try {
						result = Double.parseDouble(contextValue) <= Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						result = false;
					}
					break;
				case GT:
					try {
						result = Double.parseDouble(contextValue) > Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						result = false;
					}
					break;
				case GE:
					try {
						result = Double.parseDouble(contextValue) >= Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						result = false;
					}
					break;
				case EQ:
					result = contextValue.equals(value);
					break;
				case NE:
					result = !contextValue.equals(value);
					break;
				}
			}
		}
		
		return result;
	}
}
