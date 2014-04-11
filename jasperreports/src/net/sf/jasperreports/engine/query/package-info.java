/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
/**
 * Provides support for report queries and query executers. 
 * <br/>
 * <h2>Report SQL Queries</h2>
 * To fill a report, provide the reporting engine with the report data, or at least instruct it 
 * how to get this data. 
 * <p>
 * JasperReports normally expects to receive a 
 * {@link net.sf.jasperreports.engine.JRDataSource} object as the report data source, but 
 * it has also been enhanced to work with JDBC so that it can retrieve data from relational 
 * databases if required. 
 * </p><p>
 * The library allows the report template to specify the SQL query for report data if this 
 * data is located in relational databases. 
 * The SQL query specified in the report template is taken into account and executed only if 
 * a <code>java.sql.Connection</code> object is supplied instead of the normal 
 * {@link net.sf.jasperreports.engine.JRDataSource} object when filling the report. 
 * </p><p>
 * This query can be introduced in the JRXML report template using the <code>&lt;queryString&gt;</code> 
 * element. If present, this element comes after the report parameter declarations and before 
 * the report fields. 
 * </p><p>
 * Report parameters in the query string are important to further refine the data retrieved 
 * from the database. These parameters can act as dynamic filters in the query that supplies 
 * data for the report. Parameters are introduced using a special syntax, similar to the one 
 * used in report expressions. 
 * </p><p>
 * There are three possible ways to use parameters in the query, described below:
 * <ul>
 * <li><code>$P{paramName}</code> Syntax - the parameters are used like normal <code>java.sql.PreparedStatement</code> parameters</li>
 * <li><code>$P!{paramName}</code> Syntax - the parameters are used to dynamically modify portions of the SQL 
 * query or to pass the entire SQL query as a parameter to the report-filling routines. The 
 * value supplied for those parameters replace the parameter references in the query, before 
 * it is sent to the database server using a <code>java.sql.PreparedStatement</code> object.</li>
 * <li><code>$X{functionName, param1, param2,...}</code> Syntax - There are also cases when a part of the query 
 * needs to be dynamically built starting from a report parameter value, with the query part containing both 
 * query text and bind parameters. This is the case, for instance, with <code>IN</code> and <code>NOT IN</code> 
 * query clauses that need to use a collection report parameter as a list of values. Such complex query clauses are 
 * introduced into the query using the $X{} syntax.</li>
 * </ul>
 * In the majority of cases, the SQL query text placed inside a report template is a <code>SELECT</code> 
 * statement. JasperReports uses a <code>java.sql.PreparedStatement</code> behind the scenes to 
 * execute that SQL query through JDBC and retrieve a <code>java.sql.ResultSet</code> object to 
 * use for report filling. However, the SQL query string might also contain stored procedure 
 * calls. 
 * </p><p>
 * Certain conditions must be met to put stored procedure calls in the SQL query string of a 
 * report template: 
 * <ul>
 * <li>The stored procedure must return a <code>java.sql.ResultSet</code> when called through JDBC.</li>
 * <li>The stored procedure cannot have OUT parameters.</li>
 * </ul>
 * <h2>Query Executers</h2>
 * Report data can be produced by specifying queries in languages other than SQL. Each query language 
 * is associated a query executer factory implementation. JasperReports has built-in query executer 
 * implementations for SQL, Hibernate 3, XPath, EJB-QL, CSV and Excel queries. 
 * <p>
 * The query language is specified in JRXML using the <code>language</code> attribute of the 
 * <code>&lt;queryString&gt;</code> tag. Using the API, the query language is set by 
 * <code>JRDesignQuery.setLanguage(String)</code>. The default language is SQL, thus ensuring 
 * backward compatibility for report queries that do not specify a query language. 
 * </p><p>
 * To register a query executer factory for a query language, one has to define a 
 * JasperReports property named 
 * <code>net.sf.jasperreports.query.executer.factory.&lt;language&gt;</code>. The same mechanism can be used to 
 * override the built-in query executers for a query language, for instance to use a custom 
 * query executer for SQL queries. 
 * </p><p>
 * The API for query executers involves an executer factory interface, a query executer 
 * interface, implementations of these interfaces, and {@link net.sf.jasperreports.engine.JRDataSource} implementations. 
 * </p><p>
 * {@link net.sf.jasperreports.engine.query.QueryExecuterFactory} is a factory interface used to query executers for a specific 
 * language and to provide information regarding the connection parameters required by the 
 * query executer to run the query. It has the following methods: 
 * <ul>
 * <li><code>public JRQueryExecuter createQueryExecuter(JasperReportsContext jasperReportsContext, JRDataset dataset, Map<String,? extends JRValueParameter> parameters)</code> - 
 * This method creates a query executer. The dataset includes the query string and the fields that 
 * will be requested from the data source created by the query executer. The parameters map contains 
 * parameter types and runtime values to be used for query parameters. This method usually sends the 
 * dataset and parameters map to the created query executer.</li>
 * <li><code>public Object[] getBuiltinParameters()</code> - This method returns parameters that will 
 * be automatically registered with a report/dataset based on the query language. These parameters will 
 * be used by query executers as the context/connection on which to execute the query. For instance, 
 * the Hibernate query executer factory specifies a <code>HIBERNATE_SESSION</code> parameter of type 
 * <code>org.hibernate.Session</code> whose value will be used by the query executer to run the query.</li>
 * <li><code>public boolean supportsQueryParameterType(String className)</code> - This method is used on 
 * report validation to determine whether a query parameter type (for a parameter specified in the query 
 * using <code>$P{..}</code>) is supported by the query executer implementation.</li>
 * </ul>
 * A {@link net.sf.jasperreports.engine.query.JRQueryExecuter} is responsible for running a query, creating a 
 * data source out of the result, and closing the result. It includes these methods:
 * <ul>
 * <li><code>public JRDataSource createDatasource()</code> - This method processes and runs the query 
 * and creates a data source out of the query result. Usually, the required data (query string and parameter 
 * values) is made available to the query executer by the factory on creation.</li>
 * <li><code>public void close()</code> - This method closes the query execution result and any other 
 * resource associated with it. It is called after all data produced by the query executer has been fetched.</li>
 * <li><code>public boolean cancelQuery()</code> - This method is called when the user decides to cancel 
 * a report fill process. The implementation should check whether the query is 
 * currently being executed and ask the underlying mechanism to abort the execution. 
 * The method should return true if the query was being executed and the execution 
 * was canceled. If execution abortion is not supported, the method will always return false.</li>
 * </ul>
 * Query executer implementation can benefit from using 
 * {@link net.sf.jasperreports.engine.query.JRAbstractQueryExecuter} as 
 * a base. The abstract base provides query parameter processing functionality and other 
 * utility methods. 
 * </p><p>
 * In most cases, a query executer needs a new {@link net.sf.jasperreports.engine.JRDataSource} 
 * implementation to wrap its specific query results. Still, in some of the cases, query executers 
 * can use existing {@link net.sf.jasperreports.engine.JRDataSource} implementations.  
 * </p><p>
 * Note that registering new query executer implementations by adding properties in the 
 * <code>jasperreports.properties</code> file, as mentioned above, is only one way of registering 
 * the executers. They can be registered in a more transparent way by using the 
 * JasperReports extension support. One ore more query executer implementations can be 
 * packaged in a query executer bundle that can be deployed as a single JAR file. This 
 * approach obviates the need to modify existing application files. The query executer 
 * extension point in JasperReports is represented by the 
 * {@link net.sf.jasperreports.engine.query.QueryExecuterFactoryBundle} interface. 
 * </p>
 * <h2>SQL Query Executer</h2>
 * The SQL query executer is a JDBC-based executer for SQL queries. 
 * <p>
 * The SQL query executer factory does not register any parameter as the 
 * {@link net.sf.jasperreports.engine.JRParameter#REPORT_CONNECTION REPORT_CONNECTION} parameter 
 * is kept in all reports for backward compatibility. The 
 * SQL query executer uses this parameter to retrieve a <code>java.sql.Connection</code> object. 
 * The query executer creates a {@link net.sf.jasperreports.engine.JRResultSetDataSource} data source to wrap the JDBC 
 * result set. 
 * </p><p>
 * Aborting the currently running query is supported using 
 * <code>java.sql.PreparedStatement.cancel()</code>. The fetch size of the JDBC statement used 
 * by the query executer behind the scenes can be set using the 
 * <code>net.sf.jasperreports.jdbc.fetch.size</code> configuration property at report level or 
 * globally.
 * </p>
 * <h2>XPath Query Executer</h2>
 * The XPath query executer permits reports using XML data sources to specify the XPath 
 * that produces the list of nodes/records as the report query. 
 * <p>
 * The query executer factory registers a parameter named 
 * {@link net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory#PARAMETER_XML_DATA_DOCUMENT XML_DATA_DOCUMENT} 
 * of type <code>org.w3c.dom.Document</code>. The query executer will run the XPath query against this 
 * document and produce a {@link net.sf.jasperreports.engine.data.JRXmlDataSource} data source. 
 * </p><p>
 * Parameters are supported in the XPath query. All parameters will be replaced in the 
 * query string by their <code>java.lang.String</code> values. 
 * This query executer recognizes four additional parameters that serve for localization 
 * purposes when creating the {@link net.sf.jasperreports.engine.data.JRXmlDataSource} instance: 
 * <ul>
 * <li>{@link net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory#XML_LOCALE XML_LOCALE}</li>
 * <li>{@link net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory#XML_NUMBER_PATTERN XML_NUMBER_PATTERN}</li>
 * <li>{@link net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory#XML_DATE_PATTERN XML_DATE_PATTERN}</li>
 * <ul>
 * </p>
 * <h2>Hibernate Query Executer</h2>
 * JasperReports includes support for Hibernate 3 in the form of a query executer. This 
 * allows users to specify in a report an HQL query that should be used to retrieve report 
 * data. 
 * <p>
 * For reports having an HQL query, the executor factory will automatically define a 
 * parameter named {@link net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory#PARAMETER_HIBERNATE_SESSION HIBERNATE_SESSION} 
 * of type <code>org.hibernate.Session</code>. Its value will be used by the query executor to create the query. 
 * </p><p>
 * Like SQL queries, HQL queries can embed two types of parameters:
 * <ul>
 * <li>using <code>$P{..}</code> syntax - These parameters are used as named parameters of the Hibernate query.</li>
 * <li>using <code>$P!{..}</code> syntax - The <code>java.lang.String</code> value of the parameter is substituted 
 * as-is in the query string before creating the Hibernate query. This type of parameter can be used to dynamically 
 * specify query clauses/parts.</li>
 * </ul>
 * The result of a Hibernate query can be obtained in several ways. The Hibernate query 
 * executer chooses the way the query result will be produced based on a property named 
 * <code>net.sf.jasperreports.hql.query.run.type</code>. The run type can be one of the following: 
 * <ul>
 * <li><code>list</code> - The result is fetched using <code>org.hibernate.Query.list()</code>. The result 
 * rows can be fetched all at once or in fixed-sized chunks. To enable paginated result row retrieval, 
 * the <code>net.sf.jasperreports.hql.query.list.page.size</code> configuration property should have a positive value.</li>
 * <li><code>scroll</code> - The result is fetched using <code>org.hibernate.Query.scroll()</code>.</li>
 * <li><code>iterate</code> - The result is fetched using <code>org.hibernate.Query.iterate()</code>.</li>
 * </ul>
 * The fetch size of the query can be set using the 
 * <code>net.sf.jasperreports.jdbc.fetch.size</code> configuration property at report level or 
 * globally. 
 * </p><p>
 * However, when dealing with large amounts of data, using pagination is the most 
 * common way to present the document content. In this case, it is necessary to clear 
 * Hibernate's first-level cache after each page fetching, otherwise Hibernate will 
 * eventually cause an OutOfMemory error. If the Hibernate's session cache is regularly 
 * cleared, the memory trap can be avoided. Because flushing data and clearing the cache is 
 * a time-consuming process, you should use it only if really huge datasets are involved. 
 * This is why the <code>net.sf.jasperreports.hql.clear.cache</code> property was introduced. 
 * Normally, it defaults to false. If set to true, the periodic Hibernate session cache 
 * cleanup is performed after each page fetching. 
 * </p><p>
 * A report/dataset field is mapped to a value from the Hibernate query result either by its 
 * description or its name. By default, the program uses the report field name, but the report 
 * field description property can be used instead if the 
 * <code>net.sf.jasperreports.hql.field.mapping.descriptions</code> configuration 
 * property is set to true either in the report template or globally. 
 * </p><p>
 * The mappings are similar to the ones used by JavaBeans data sources, except that select aliases are 
 * used when queries return tuples instead of single objects. 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * <h2>Related Documentation</h2>
 * <a href="http://community.jaspersoft.com/wiki/jasperreports-library-tutorial">JasperReports Tutorial</a>
 * @see net.sf.jasperreports.engine.JRDataSource
 * @see net.sf.jasperreports.engine.JRResultSetDataSource
 * @see net.sf.jasperreports.engine.data.JRXmlDataSource
 */
package net.sf.jasperreports.engine.query;

