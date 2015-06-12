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

/*
 * Contributors:
 * Tim Thomas - tthomas48@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.engine.util.xml.JRXPathExecuter;
import net.sf.jasperreports.engine.util.xml.JRXPathExecuterUtils;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XML data source implementation that allows to access the data from a xml
 * document using XPath expressions.
 * <p>
 * The data source is constructed around a node set (record set) selected
 * by an XPath expression from the xml document.
 * </p>
 * <p>
 * Each field can provide an additional XPath expresion that will be used to
 * select its value. This expression must be specified using the "fieldDescription"
 * element of the field. The expression is evaluated in the context of the current
 * node thus the expression should be relative to the current node.
 * </p>
 * <p>
 * To support subreports, sub data sources can be created. There are two different methods 
 * for creating sub data sources. The first one allows to create a sub data source rooted 
 * at the current node. The current node can be seen as a new document around which the 
 * sub data source is created. The second method allows to create a sub data source that
 * is rooted at the same document that is used by the data source but uses a different 
 * XPath select expression. 
 * </p>
 * <p>
 * Example:
 * <pre>
 * &lt;A&gt;
 * 	&lt;B id="0"&gt;
 * 		&lt;C&gt;
 * 		&lt;C&gt;
 * 	&lt;/B&gt;
 * 	&lt;B id="1"&gt;
 * 		&lt;C&gt;
 * 		&lt;C&gt;
 * 	&lt;/B&gt;
 * 	&lt;D id="3"&gt;
 * 		&lt;E&gt;
 * 		&lt;E&gt;
 * 	&lt;/D&gt;
 * &lt;/A&gt;
 * </pre>
 * <p>
 * Data source creation
 * <ul>
 * <li>new JRXmlDataSource(document, "/A/B") - creates a data source with two nodes of type /A/B
 * <li>new JRXmlDataSource(document, "/A/D") - creates a data source with two nodes of type /A/D
 * </ul>
 * Field selection
 * <ul>
 * <li>@id - will select the "id" attribute from the current node
 * <li>C - will select the value of the first node of type C under the current node. 
 * </ul>
 * Sub data source creation
 * <ul>
 * <li>"((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).subDataSource("/B/C")
 * 	- in the context of the node B, creates a data source with elements of type /B/C
 * <li>"((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).dataSource("/A/D")
 * 	- creates a data source with elements of type /A/D
 * </ul>
 * </p>
 * <p>
 * Generally the full power of XPath expression is available. As an example, "/A/B[@id > 0"] will select all the
 * nodes of type /A/B having the id greater than 0. 
 * You'll find a short XPath tutorial <a href="http://www.zvon.org/xxl/XPathTutorial/General/examples.html" target="_blank">here</a>.
 * 
 * </p>
 * <p>
 * Note on performance. Due to the fact that all the XPath expression are interpreted the
 * data source performance is not great. For the cases where more speed is required,
 * consider implementing a custom data source that directly accesses the Document through the DOM API. 
 * </p>
 * @author Peter Severin (peter_p_s@sourceforge.net, contact@jasperassistant.com)
 * @see JRXPathExecuterUtils
 */
public class JRXmlDataSource extends JRAbstractTextDataSource implements JRRewindableDataSource {

	// the xml document
	private Document document;

	// the XPath select expression that gives the nodes to iterate
	private String selectExpression;

	// the node list
	private NodeList nodeList;

	// the node list length
	private int nodeListLength;
	
	// the current node
	private Node currentNode;

	// current node index
	private int currentNodeIndex = - 1;

	private final JRXPathExecuter xPathExecuter;
	
	private InputStream inputStream;
	private boolean closeInputStream;
	
	// -----------------------------------------------------------------
	// Constructors

	/**
	 * Creates the data source by parsing the xml document from the given file.
	 * The data source will contain exactly one record consisting of the document node itself.
	 * 
	 * @param document the document
	 * @throws JRException if the data source cannot be created
	 */
	public JRXmlDataSource(JasperReportsContext jasperReportsContext, Document document) throws JRException {
		this(jasperReportsContext, document, ".");
	}

	/**
	 * @see #JRXmlDataSource(JasperReportsContext, Document)
	 */
	public JRXmlDataSource(Document document) throws JRException {
		this(DefaultJasperReportsContext.getInstance(), document);
	}

	/**
	 * Creates the data source by parsing the xml document from the given file.
	 * An additional XPath expression specifies the select criteria that produces the 
	 * nodes (records) for the data source.
	 * 
	 * @param document the document
	 * @param selectExpression the XPath select expression
	 * @throws JRException if the data source cannot be created
	 */
	public JRXmlDataSource(
		JasperReportsContext jasperReportsContext,
		Document document, 
		String selectExpression
		) throws JRException 
	{
		this.document = document;
		this.selectExpression = selectExpression;
		
		this.xPathExecuter = JRXPathExecuterUtils.getXPathExecuter(jasperReportsContext);
		
		moveFirst();
	}


	/**
	 * @see #JRXmlDataSource(JasperReportsContext, Document, String)
	 */
	public JRXmlDataSource(Document document, String selectExpression) throws JRException 
	{
		this(DefaultJasperReportsContext.getInstance(), document, selectExpression);
	}


	/**
	 * Creates the data source by parsing the xml document from the given input stream.
	 *  
	 * @param in the input stream
	 * @see JRXmlDataSource#JRXmlDataSource(Document) 
	 */
	public JRXmlDataSource(JasperReportsContext jasperReportsContext, InputStream in) throws JRException 
	{
		this(jasperReportsContext, in, false);
	}

	public JRXmlDataSource(JasperReportsContext jasperReportsContext, InputStream in, boolean isNamespaceAware) throws JRException 
	{
		this(jasperReportsContext, in, ".", isNamespaceAware);
	}

	/**
	 * @see #JRXmlDataSource(JasperReportsContext, InputStream)
	 */
	public JRXmlDataSource(InputStream in) throws JRException {
		this(in, false);
	}

	public JRXmlDataSource(InputStream in, boolean isNamespaceAware) throws JRException {
		this(DefaultJasperReportsContext.getInstance(), in, isNamespaceAware);
	}

	/**
	 * Creates the data source by parsing the xml document from the given input stream.
	 * 
	 * @see JRXmlDataSource#JRXmlDataSource(InputStream) 
	 * @see JRXmlDataSource#JRXmlDataSource(Document, String) 
	 */
	public JRXmlDataSource(
		JasperReportsContext jasperReportsContext,
		InputStream in, 
		String selectExpression
		) throws JRException 
	{
		this(jasperReportsContext, in, selectExpression, false);
	}

	public JRXmlDataSource(
			JasperReportsContext jasperReportsContext,
			InputStream in, 
			String selectExpression,
			boolean isNamespaceAware
			) throws JRException 
	{
		this(jasperReportsContext, JRXmlUtils.parse(new InputSource(in), isNamespaceAware), selectExpression);
		
		this.inputStream = in;
		this.closeInputStream = false;
	}

	/**
	 * @see #JRXmlDataSource(JasperReportsContext, InputStream, String)
	 */
	public JRXmlDataSource(InputStream in, String selectExpression)
			throws JRException {
		this(in, selectExpression, false);
	}

	public JRXmlDataSource(InputStream in, String selectExpression, boolean isNamespaceAware)
			throws JRException {
		this(DefaultJasperReportsContext.getInstance(), in, selectExpression, isNamespaceAware);
	}

	/**
	 * Creates the data source by parsing the xml document from the given system identifier (URI).
	 * <p>If the system identifier is a URL, it must be full resolved.</p>
	 * 
	 * @param uri the system identifier
	 * @see JRXmlDataSource#JRXmlDataSource(Document) 
	 */
	public JRXmlDataSource(JasperReportsContext jasperReportsContext, String uri) throws JRException {
		this(jasperReportsContext, uri, false);
	}

	public JRXmlDataSource(JasperReportsContext jasperReportsContext, String uri, boolean isNamespaceAware) throws JRException {
		this(jasperReportsContext, uri, ".", isNamespaceAware);
	}

	/**
	 * @see #JRXmlDataSource(JasperReportsContext, String)
	 */
	public JRXmlDataSource(String uri) throws JRException {
		this(uri, false);
	}

	public JRXmlDataSource(String uri, boolean isNamespaceAware) throws JRException {
		this(DefaultJasperReportsContext.getInstance(), uri, isNamespaceAware);
	}

	/**
	 * Creates the data source by parsing the xml document from the given system identifier (URI).
	 * 
	 * @see JRXmlDataSource#JRXmlDataSource(String) 
	 * @see JRXmlDataSource#JRXmlDataSource(Document, String) 
	 */
	public JRXmlDataSource(
		JasperReportsContext jasperReportsContext, 
		String uri, 
		String selectExpression
		) throws JRException 
	{
		this(jasperReportsContext, uri, selectExpression, false);
	}

	public JRXmlDataSource(
			JasperReportsContext jasperReportsContext, 
			String uri, 
			String selectExpression,
			boolean isNamespaceAware
			) throws JRException 
	{
		this(
			jasperReportsContext, 
			RepositoryUtil.getInstance(jasperReportsContext).getInputStreamFromLocation(uri), 
			selectExpression,
			isNamespaceAware
			);
		this.closeInputStream = true;//FIXME close the stream immediately
	}

	/**
	 * @see #JRXmlDataSource(JasperReportsContext, String, String)
	 */
	public JRXmlDataSource(String uri, String selectExpression)
			throws JRException {
		this(uri, selectExpression, false);
	}

	public JRXmlDataSource(String uri, String selectExpression, boolean isNamespaceAware)
			throws JRException {
		this(DefaultJasperReportsContext.getInstance(), uri, selectExpression, isNamespaceAware);
	}

	/**
	 * Creates the data source by parsing the xml document from the given file.
	 * 
	 * @param file the file
	 * @see JRXmlDataSource#JRXmlDataSource(Document) 
	 */
	public JRXmlDataSource(JasperReportsContext jasperReportsContext, File file) throws JRException {
		this(jasperReportsContext, file, false);
	}

	public JRXmlDataSource(JasperReportsContext jasperReportsContext, File file, boolean isNamespaceAware) throws JRException {
		this(jasperReportsContext, file, ".", isNamespaceAware);
	}

	/**
	 * @see #JRXmlDataSource(JasperReportsContext, File)
	 */
	public JRXmlDataSource(File file) throws JRException {
		this(file, false);
	}

	public JRXmlDataSource(File file, boolean isNamespaceAware) throws JRException {
		this(DefaultJasperReportsContext.getInstance(), file, isNamespaceAware);
	}

	/**
	 * Creates the data source by parsing the xml document from the given file.
	 * 
	 * @see JRXmlDataSource#JRXmlDataSource(File) 
	 * @see JRXmlDataSource#JRXmlDataSource(Document, String) 
	 */
	public JRXmlDataSource(JasperReportsContext jasperReportsContext, File file, String selectExpression)
			throws JRException {
		this(jasperReportsContext, file, selectExpression, false);
	}

	public JRXmlDataSource(JasperReportsContext jasperReportsContext, File file, String selectExpression, boolean isNamespaceAware)
			throws JRException {
		this(jasperReportsContext, JRXmlUtils.parse(file, isNamespaceAware), selectExpression);
	}

	/**
	 * @see #JRXmlDataSource(JasperReportsContext, File, String)
	 */
	public JRXmlDataSource(File file, String selectExpression)
			throws JRException {
		this(file, selectExpression, false);
	}

	public JRXmlDataSource(File file, String selectExpression, boolean isNamespaceAware)
			throws JRException {
		this(DefaultJasperReportsContext.getInstance(), file, selectExpression, isNamespaceAware);
	}
	
	// -----------------------------------------------------------------
	// Implementation
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRRewindableDataSource#moveFirst()
	 */
	public void moveFirst() throws JRException {
		if (document == null)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_NULL_DOCUMENT,
					(Object[])null);
		}
		if (selectExpression == null)
		{
			throw 
			new JRException(
				EXCEPTION_MESSAGE_KEY_NULL_SELECT_EXPRESSION,
				(Object[])null);
		}

		currentNode = null;
		currentNodeIndex = -1;
		nodeListLength = 0;
		nodeList = xPathExecuter.selectNodeList(document,
				selectExpression);
		nodeListLength = nodeList.getLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	public boolean next() 
	{
		if(currentNodeIndex == nodeListLength - 1)
		{
			return false;
		}
		currentNode = nodeList.item(++ currentNodeIndex);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 */
	public Object getFieldValue(JRField jrField) throws JRException 
	{
		if(currentNode == null)
		{
			return null;
		}
		String expression = jrField.getDescription();
		if (expression == null || expression.length() == 0)
		{
			return null;
		}
		Object value = null;
		
		Class<?> valueClass = jrField.getValueClass();
		Object selectedObject = xPathExecuter.selectObject(currentNode, expression);

		if(Object.class != valueClass) 
		{
			if (selectedObject != null) 
			{
				if (selectedObject instanceof Node) 
				{
					String text = getText((Node) selectedObject);
					if (text != null) 
					{
						value = convertStringValue(text, valueClass);
					}
				} 
				else if (selectedObject instanceof Boolean && valueClass.equals(Boolean.class)) 
				{
					value = selectedObject;
				}
				else if (selectedObject instanceof Number && Number.class.isAssignableFrom(valueClass)) 
				{
					value = convertNumber((Number) selectedObject, valueClass);
				} 
				else 
				{
					String text = selectedObject.toString();
					value = convertStringValue(text, valueClass);
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
	 * Creates a sub data source using the current node (record) as the root
	 * of the document. An additional XPath expression specifies the select criteria applied to
	 * this new document and that produces the nodes (records) for the data source.
	 * 
	 * @param selectExpr the XPath select expression
	 * @return the xml sub data source
	 * @throws JRException if the sub data source couldn't be created
	 * @see JRXmlDataSource#JRXmlDataSource(Document, String)
	 */
	public JRXmlDataSource subDataSource(String selectExpr)
			throws JRException {
		// create a new document from the current node
		Document doc = subDocument();
		JRXmlDataSource subDataSource = new JRXmlDataSource(doc, selectExpr);
		subDataSource.setTextAttributes(this);
		return subDataSource;
	}

	/**
	 * Creates a sub data source using the current node (record) as the root
	 * of the document. The data source will contain exactly one record consisting 
	 * of the document node itself.
	 * 
	 * @return the xml sub data source
	 * @throws JRException if the data source cannot be created
	 * @see JRXmlDataSource#subDataSource(String)
	 * @see JRXmlDataSource#JRXmlDataSource(Document)
	 */
	public JRXmlDataSource subDataSource() throws JRException {
		return subDataSource(".");
	}

	
	/**
	 * Creates a document using the current node as root.
	 * 
	 * @return a document having the current node as root
	 * @throws JRException
	 */
	public Document subDocument() throws JRException
	{
		if(currentNode == null)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_NODE_NOT_AVAILABLE,
					(Object[])null);
		}
		
		// create a new document from the current node
		return JRXmlUtils.createDocument(currentNode);
	}
	
	
	/**
	 * Creates a sub data source using as root document the document used by "this" data source.
	 * An additional XPath expression specifies the select criteria applied to
	 * this document and that produces the nodes (records) for the data source.
	 * 
	 * @param selectExpr the XPath select expression
	 * @return the xml sub data source
	 * @throws JRException if the sub data source couldn't be created
	 * @see JRXmlDataSource#JRXmlDataSource(Document, String)
	 */
	public JRXmlDataSource dataSource(String selectExpr)
			throws JRException {
		JRXmlDataSource subDataSource = new JRXmlDataSource(document, selectExpr);
		subDataSource.setTextAttributes(this);
		return subDataSource;
	}

	/**
	 * Creates a sub data source using as root document the document used by "this" data source.
	 * The data source will contain exactly one record consisting  of the document node itself.
	 * 
	 * @return the xml sub data source
	 * @throws JRException if the data source cannot be created
	 * @see JRXmlDataSource#dataSource(String)
	 * @see JRXmlDataSource#JRXmlDataSource(Document)
	 */
	public JRXmlDataSource dataSource() throws JRException {
		return dataSource(".");
	}

	/**
	 * Return the text that a node contains. This routine:
	 * <ul>
	 * <li>Ignores comments and processing instructions.
	 * <li>Concatenates TEXT nodes, CDATA nodes, and the results of recursively
	 * processing EntityRef nodes.
	 * <li>Ignores any element nodes in the sublist. (Other possible options
	 * are to recurse into element sublists or throw an exception.)
	 * </ul>
	 * 
	 * @param node a DOM node
	 * @return a String representing node contents or null
	 */
	public String getText(Node node) {
		if (!node.hasChildNodes())
		{
			return node.getNodeValue();
		}
		StringBuffer result = new StringBuffer();

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node subnode = list.item(i);
			if (subnode.getNodeType() == Node.TEXT_NODE) 
			{
				String value = subnode.getNodeValue();
				if(value != null)
				{
					result.append(value);
				}
			} 
			else if (subnode.getNodeType() == Node.CDATA_SECTION_NODE) 
			{
				String value = subnode.getNodeValue();
				if(value != null)
				{
					result.append(value);
				}
			} else if (subnode.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
				// Recurse into the subtree for text
				// (and ignore comments)
				String value = getText(subnode);
				if(value != null)
				{
					result.append(value);
				}
			}
		}

		return result.toString();
	}
	
	/**
	 * Closes the reader. Users of this data source should close it after usage.
	 */
	public void close()
	{
		try
		{
			if (closeInputStream)
			{
				inputStream.close();
			}
		}
		catch(IOException e)
		{
			//nothing to do
		}
	}

}
