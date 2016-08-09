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
package net.sf.jasperreports.engine.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.json.JRJsonNode;
import net.sf.jasperreports.engine.util.JsonUtil;
import net.sf.jasperreports.engine.util.json.JsonExecuter;
import net.sf.jasperreports.engine.util.json.JsonExecuterUtil;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * JSON data source implementation
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class JsonDataSource extends JRAbstractTextDataSource implements JsonData {

	public static final String EXCEPTION_MESSAGE_KEY_JSON_FIELD_VALUE_NOT_RETRIEVED = "data.json.field.value.not.retrieved";
	public static final String EXCEPTION_MESSAGE_KEY_NO_DATA = "data.json.no.data";

	// the JSON select expression that gives the nodes to iterate
	private String selectExpression;

	private List<JRJsonNode> nodeList;

	private JRJsonNode currentNode;

	// the node list length
	private int nodeListLength;

	// current node index
	private int currentNodeIndex = - 1;

	private JsonExecuter jsonExecuter;

	private JRJsonNode rootNode;

	
	public JsonDataSource(InputStream stream) throws JRException {
		this(stream, null);
	}
	
	public JsonDataSource(InputStream jsonStream, String selectExpression) throws JRException {
		this(JsonUtil.parseJson(jsonStream), selectExpression);
	}

	protected JsonDataSource(JsonNode jsonTree, String selectExpression) throws JRException {
		this(DefaultJasperReportsContext.getInstance(), jsonTree, selectExpression);
	}
	
	protected JsonDataSource(JasperReportsContext jasperReportsContext, JsonNode jsonTree, String selectExpression) throws JRException {
		this.rootNode = new JRJsonNode(null, jsonTree);
		this.selectExpression = selectExpression;
		this.jsonExecuter = JsonExecuterUtil.getJsonExecuter(jasperReportsContext);
		
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
		this(jasperReportsContext, JsonUtil.parseJson(jasperReportsContext, location), selectExpression);
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
	@Override
	public void moveFirst() throws JRException {
		if (rootNode == null || rootNode.getDataNode().isMissingNode()) {
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_NO_DATA,
					(Object[])null);
		}

		currentNode = null;
		currentNodeIndex = -1;
		nodeListLength = 0;
		nodeList = jsonExecuter.selectNodes(rootNode, selectExpression);
		nodeListLength = nodeList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	@Override
	public boolean next() {
		if(currentNodeIndex == nodeListLength - 1) {
			return false;
		}
		currentNode = nodeList.get(++ currentNodeIndex);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 */
	@Override
	public Object getFieldValue(JRField jrField) throws JRException 
	{
		if(currentNode == null) {
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
		JRJsonNode selectedNode = jsonExecuter.selectObjectNode(currentNode, expression);
		JsonNode selectedObject = selectedNode.getDataNode();

		
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
		if(currentNode == null)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_NODE_NOT_AVAILABLE,
					(Object[])null);
		}

		JsonDataSource subDataSource = new JsonDataSource(currentNode.getDataNode(), selectExpression);
		subDataSource.setTextAttributes(this);
		return subDataSource;
	}


	/**
	 * @deprecated no longer required
	 */
	@Deprecated
	public void close() {
		//NOP
	}

}
