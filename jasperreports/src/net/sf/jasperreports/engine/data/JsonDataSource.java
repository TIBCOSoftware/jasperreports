/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JsonUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


/**
 * JSON data source implementation
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class JsonDataSource extends JRAbstractTextDataSource implements JsonData {

	public static final String EXCEPTION_MESSAGE_KEY_JSON_FIELD_VALUE_NOT_RETRIEVED = "data.json.field.value.not.retrieved";
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_ATTRIBUTE_SELECTION = "data.json.invalid.attribute.selection";
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_EXPRESSION = "data.json.invalid.expression";
	public static final String EXCEPTION_MESSAGE_KEY_NO_DATA = "data.json.no.data";

	// the JSON select expression that gives the nodes to iterate
	private String selectExpression;

	private Iterator<JsonNode> jsonNodesIterator;

	// the current node
	private JsonNode currentJsonNode;

	private final String PROPERTY_SEPARATOR = ".";//FIXME static?

	private final String ARRAY_LEFT = "[";

	private final String ARRAY_RIGHT = "]";
	
	private final String ATTRIBUTE_LEFT = "(";
	
	private final String ATTRIBUTE_RIGHT = ")";
	
	// the JSON tree as it is obtained from the JSON source
	private JsonNode jsonTree;
	
	private ObjectMapper mapper;
	
	public JsonDataSource(InputStream stream) throws JRException {
		this(stream, null);
	}
	
	public JsonDataSource(InputStream jsonStream, String selectExpression) throws JRException {
		this(JsonUtil.parseJson(jsonStream), selectExpression);
	}
	
	protected JsonDataSource(JsonNode jsonTree, String selectExpression) throws JRException {
		this.mapper = JsonUtil.createObjectMapper();
		
		this.jsonTree = jsonTree;
		this.selectExpression = selectExpression;
		
		moveFirst();
	}


	public JsonDataSource(File file) throws FileNotFoundException, JRException {
		this(file, null);
	}
	

	public JsonDataSource(File file, String selectExpression) throws FileNotFoundException, JRException {
		this(JsonUtil.parseJson(file), selectExpression);
	}

	/**
	 * Creates a data source instance that reads JSON data from a given location
	 * @param jasperReportsContext the JasperReportsContext
	 * @param location a String representing JSON data source
	 * @param selectExpression a String representing the select expression
	 */
	public JsonDataSource(JasperReportsContext jasperReportsContext, String location, String selectExpression) throws JRException 
	{
		this(JsonUtil.parseJson(jasperReportsContext, location), selectExpression);
	}

	/**
	 * @see #JsonDataSource(JasperReportsContext, String, String)
	 */
	public JsonDataSource(String location, String selectExpression) throws JRException 
	{
		this(DefaultJasperReportsContext.getInstance(), location, selectExpression);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRRewindableDataSource#moveFirst()
	 */
	public void moveFirst() throws JRException {
		if (jsonTree == null || jsonTree.isMissingNode()) {
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_NO_DATA,
					(Object[])null);
		}

		currentJsonNode = null;
		JsonNode result = getJsonData(jsonTree, selectExpression);
		if (result != null && result.isObject()) {
			final List<JsonNode> list = new ArrayList<JsonNode>();
			list.add(result);
			jsonNodesIterator = new Iterator<JsonNode>() {
				private int count = -1;
				public void remove() {
					list.remove(count);
				}
				
				public JsonNode next() {
					count ++;
					return list.get(count);
				}
				
				public boolean hasNext() {
					return count < list.size()-1;
				}
			};
		} else if (result != null && result.isArray()) {
			jsonNodesIterator = result.elements();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	public boolean next() {
		if(jsonNodesIterator == null || !jsonNodesIterator.hasNext()) {
			return false;
		}
		currentJsonNode = jsonNodesIterator.next();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 */
	public Object getFieldValue(JRField jrField) throws JRException 
	{
		if(currentJsonNode == null) {
			return null;
		}
		String expression = jrField.getDescription();
		if (expression == null || expression.length() == 0)
		{
			expression = jrField.getName();
			if (expression == null || expression.length() == 0)
			{
				return null;
			}
		}
		Object value = null;
		
		Class<?> valueClass = jrField.getValueClass();
		JsonNode selectedObject = getJsonData(currentJsonNode, expression);
		
		if(Object.class != valueClass) 
		{
			boolean hasValue = selectedObject != null 
					&& !selectedObject.isMissingNode() && !selectedObject.isNull();
			if (hasValue) 
			{
				try {
					if (valueClass.equals(String.class)) {
                        if (selectedObject.isArray()) {
                            value = selectedObject.toString();
                        } else {
                            value = selectedObject.asText();
                        }

					} else if (valueClass.equals(Boolean.class)) {
						value = selectedObject.booleanValue();
						
					} else if (Number.class.isAssignableFrom(valueClass)) {
						//FIXME if the json node is a number, avoid converting to string and parsing back the value
							value = convertStringValue(selectedObject.asText(), valueClass);
							
					}
					else if (Date.class.isAssignableFrom(valueClass)) {
							value = convertStringValue(selectedObject.asText(), valueClass);
							
					} else {
						throw 
							new JRException(
								EXCEPTION_MESSAGE_KEY_CANNOT_CONVERT_FIELD_TYPE,
								new Object[]{jrField.getName(), valueClass.getName()});
					}
				} catch (Exception e) {
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_JSON_FIELD_VALUE_NOT_RETRIEVED,
							new Object[]{jrField.getName(), valueClass.getName()}, 
							e);
				}
			}
		}
		else
		{
			value = selectedObject;
		}
		
		return value;
	}
	
	/**
	 * Extracts the JSON nodes based on the query expression
	 * 
	 * @param rootNode
	 * @param jsonExpression
	 * @throws JRException
	 */
	protected JsonNode getJsonData(JsonNode rootNode, String jsonExpression) throws JRException {
		if (jsonExpression == null || jsonExpression.length() == 0) {
			return rootNode;
		}
		JsonNode tempNode = rootNode;
		StringTokenizer tokenizer = new StringTokenizer(jsonExpression, PROPERTY_SEPARATOR);
		
		while(tokenizer.hasMoreTokens()) {
			String currentToken = tokenizer.nextToken();
			int currentTokenLength = currentToken.length();
			int indexOfLeftSquareBracket = currentToken.indexOf(ARRAY_LEFT);

			// got Left Square Bracket - LSB
			if (indexOfLeftSquareBracket != -1) {
				// a Right Square Bracket must be the last character in the current token
				if(currentToken.lastIndexOf(ARRAY_RIGHT) != (currentTokenLength-1)) {
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_INVALID_EXPRESSION,
							new Object[]{jsonExpression, currentToken});
				}
				
				// LSB not first character
				if (indexOfLeftSquareBracket > 0) {
					// extract nodes at property
					String property = currentToken.substring(0, indexOfLeftSquareBracket);
					tempNode = goDownPathWithAttribute(tempNode, property);
				}

				String arrayOperators = currentToken.substring(indexOfLeftSquareBracket);
				StringTokenizer arrayOpsTokenizer = new StringTokenizer(arrayOperators,ARRAY_RIGHT);
				while(arrayOpsTokenizer.hasMoreTokens()) {
					if (!tempNode.isMissingNode() && tempNode.isArray()) {
						String currentArrayOperator = arrayOpsTokenizer.nextToken();
						tempNode = tempNode.path(Integer.parseInt(currentArrayOperator.substring(1)));
					}
				}
			} else {
				tempNode = goDownPathWithAttribute(tempNode, currentToken);
			}
		}
		
		return tempNode;
	}
	
	
	/**
	 * Extracts the JSON nodes that match the attribute expression
	 * 
	 * @param rootNode
	 * @param pathWithAttributeExpression : e.g. Orders(CustomerId == HILAA)
	 * @throws JRException
	 */
	protected JsonNode goDownPathWithAttribute(JsonNode rootNode, String pathWithAttributeExpression) throws JRException {
		// check if path has attribute selector
		int indexOfLeftRoundBracket = pathWithAttributeExpression.indexOf(ATTRIBUTE_LEFT); 
		if (indexOfLeftRoundBracket != -1) {
			
			// a Right Round Bracket must be the last character in the current pathWithAttribute
			if(pathWithAttributeExpression.indexOf(ATTRIBUTE_RIGHT) != (pathWithAttributeExpression.length() - 1)) {
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_INVALID_ATTRIBUTE_SELECTION,
						new Object[]{pathWithAttributeExpression});
			}
			
			if(rootNode != null && !rootNode.isMissingNode()) {
				
				String path = pathWithAttributeExpression.substring(0, indexOfLeftRoundBracket);
				
				// an expression in a form like: attribute==value
				String attributeExpression = pathWithAttributeExpression.substring(indexOfLeftRoundBracket + 1, pathWithAttributeExpression.length() - 1);
				
				JsonNode result = null;
				if (rootNode.isObject()) {
					// select only those nodes for which the attribute expression applies
					if (!rootNode.path(path).isMissingNode()) {
						if (rootNode.path(path).isObject()) {
							if (isValidExpression(rootNode.path(path), attributeExpression)) {
								result = rootNode.path(path);
							}
						} else if (rootNode.path(path).isArray()) {
							result = mapper.createArrayNode();
							for (JsonNode node: rootNode.path(path)) {
								if (isValidExpression(node, attributeExpression)) {
									((ArrayNode)result).add(node);
								} 
							}
						}
					}
				} else if (rootNode.isArray()) {
					result = mapper.createArrayNode();
					for (JsonNode node: rootNode) {
						JsonNode deeperNode = node.path(path);
						if (!deeperNode.isMissingNode()) {
							if (deeperNode.isArray()) {
								for(JsonNode arrayNode: deeperNode) {
									if (isValidExpression(arrayNode, attributeExpression)) {
										((ArrayNode)result).add(arrayNode);
									}
								}
							} else if (isValidExpression(deeperNode, attributeExpression)){
								((ArrayNode)result).add(deeperNode);
							}
						} 
					}
				}
				return result;
			} 
			
		} else { // path has no attribute selectors
			return goDownPath(rootNode, pathWithAttributeExpression);
		}
		return rootNode;
	}
	
	
	/**
	 * Extracts the JSON nodes under the simple path
	 * 
	 * @param rootNode
	 * @param simplePath - a simple field name, with no selection by attribute
	 */
	protected JsonNode goDownPath(JsonNode rootNode, String simplePath) {
		if(rootNode != null && !rootNode.isMissingNode()) {
			JsonNode result = null;
			if (rootNode.isObject()) {
				result = rootNode.path(simplePath);
			} else if (rootNode.isArray()) {
				result = mapper.createArrayNode();
				for (JsonNode node: rootNode) {
					JsonNode deeperNode = node.path(simplePath);
					if (!deeperNode.isMissingNode()) {
						if (deeperNode.isArray()) {
							for(JsonNode arrayNode: deeperNode) {
								((ArrayNode)result).add(arrayNode);
							}
						} else {
							((ArrayNode)result).add(deeperNode);
						}
					} 
				}
			}
			return result;
		} 
		return rootNode;
	}
	
	
	/**
	 * Validates an attribute expression on a JsonNode
	 * 
	 * @param operand
	 * @param attributeExpression
	 * @throws JRException
	 */
	protected boolean isValidExpression(JsonNode operand, String attributeExpression) throws JRException {
		return JsonUtil.evaluateJsonExpression(operand, attributeExpression);
	}


	/**
	 * Creates a sub data source using the current node as the base for its input stream.
	 * 
	 * @return the JSON sub data source
	 * @throws JRException
	 */
	@Override
	public JsonDataSource subDataSource() throws JRException {
		return subDataSource(null);
	}


	/**
	 * Creates a sub data source using the current node as the base for its input stream.
	 * An additional expression specifies the select criteria that will be applied to the
	 * JSON tree node. 
	 * 
	 * @param selectExpression
	 * @return the JSON sub data source
	 * @throws JRException
	 */
	@Override
	public JsonDataSource subDataSource(String selectExpression) throws JRException {
		if(currentJsonNode == null)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_NODE_NOT_AVAILABLE,
					(Object[])null);
		}

		try {
			byte[] jsonNodeBytes = currentJsonNode.toString().getBytes("UTF-8");
			JsonDataSource subDataSource = new JsonDataSource(new ByteArrayInputStream(jsonNodeBytes), selectExpression);
			subDataSource.setTextAttributes(this);
			return subDataSource;
		} catch(UnsupportedEncodingException e) {
			throw new JRRuntimeException(e);
		}
	}


	/**
	 * @deprecated no longer required
	 */
	@Deprecated
	public void close() {
		//NOP
	}

}
