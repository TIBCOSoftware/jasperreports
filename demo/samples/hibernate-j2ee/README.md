
# JasperReports - Hibernate Sample (Using Legacy Java EE API) <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how HQL could be used in reports.

## Main Features in This Sample

[Hibernate (HQL) Query Executer](#hibernate_j2ee)\
[Query Executers](#queryexecuters)

## <a name='queryexecuters'>Query</a> Executers
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to implement a custom query executer and how to associate it with a custom report query language.

**Since:** 1.2.0

**Other Samples**\
[/demo/samples/csvdatasource](../csvdatasource/README.md)\
[/demo/samples/ejbql](../ejbql/README.md)\
[/demo/samples/mondrian](../mondrian/README.md)\
[/demo/samples/xmldatasource](../xmldatasource/README.md)

### Data Sources and Report Queries

Report generation relies on creating report templates, compiling and filling them with data. At fill time, the data necessary to populate the compiled report should be provided to the engine. Usually the engine expects to receive these data already stored in a [JRDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDataSource.html) object as the report data source (see the [Data Source](../datasource/README.md) sample for a complete reference).

There are situations when creating from scratch a filled [JRDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDataSource.html) object is quite impossible (for instance, data are stored in relational databases with thousands of records, or in huge XML files. In this case, automatic generation of filled [JRDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDataSource.html) objects is required.

One possibility is to instruct the engine how to retrieve the necessary data and then create a related JRDataSource. The most common way to get relevant data is to filter them by running a query and picking up filtered data from the data container. For more information about report queries, please consult the [Query](../query/README.md) sample.

### Query Executers

Query executers are part of the JasperReports API dedicated to collect and organize data into [JRDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDataSource.html) objects used by the engine at report filling time.
A query executer should be able to perform three main tasks, exposed by the [JRQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRQueryExecuter.html) interface:

- To execute queries and organize retrieved data into a [JRDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDataSource.html) implementation. This happens when `createDatasource()` is called.
- To cancel a running query, if the user decides to interrupt that query. For doing this, the `cancelQuery()` method should be called.
- To close resources kept open during the data source iteration, if they are no more necessary. Resource closing is handled via the `close()` method.

Query executer implementations can benefit from using [JRAbstractQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRAbstractQueryExecuter.html) as a base. The abstract base provides query parameter processing functionality and other utility methods.

There are several query languages, related to the data storage type. One can use SQL for retrieving data from relational databases, XPath or XQuery for navigating through XML elements in a document, HQL when performing queries using hibernate, etc. Depending on the query language, at fill time the engine calls a specific query executer class to execute the query, retrieve the data and finally create the data source object.

Anytime a report query is defined one has to specify the query language using the language attribute in the <query/> element. If no language is specified, the SQL is considered by default, for backward compatibility reasons. Below is defined a query written using the HQL language:

```
  <query language="hql">
    <![CDATA[from Address address where city not in ($P{CityFilter}) order by $P!{OrderClause}]] >
  </query>
```

Query executer implementations are produced in query executer factories. To register a query executer factory for a query language, you have to define a global property named `net.sf.jasperreports.query.executer.factory.<language>` in the `/src/default.jasperreports.properties` file. The same mechanism can be used to override the built-in query executers for a query language, for instance to use a custom query executer for SQL queries.

A query executer factory should implement the [QueryExecuterFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/QueryExecuterFactory.html) interface with the following methods:

- `public Object[] getBuiltinParameters();` - retrieves the built-in parameters associated with the query type.
- `public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters) throws JRException;` - creates the query executer implementation.
- `public boolean supportsQueryParameterType(String className);` - decides whether the query executers created by this factory support a query parameter type.

Another way to register new query executer factories, without modifying existing application files, is to register them as JasperReports extensions. One or more query executer implementations can be packaged in a query executer bundle that can be deployed as a single JAR file. The extension point for query executers is represented by the [JRQueryExecuterFactoryBundle](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRQueryExecuterFactoryBundle.html) interface.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='hibernate_j2ee'>Hibernate</a> (HQL) Query Executer
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill reports using embedded Hibernate (HQL) queries.

**Since:** 1.2.0

### The Hibernate Query Executer

A large variety of multi-tier applications rely on database interrogations. In these applications accessing data represents a well-delimited layer with specific processing tools, able to create/close connections to a database, to access/modify/retrieve data or metadata, to commit/rollback transactions, etc.

One of these middle-tier tools dedicated to data access is Hibernate (today: Hibernate 3.3): a collection of correlated projects using POJO-style domain models that extend the Object/Relational mapping the initial Hibernate releases relied on.

In order to perform queries, Hibernate uses HQL (the **H**ibernate **Q**uery **L**anguage), its specific query language with a syntax almost similar to SQL (one can find the HQL basics [here](http://docs.jboss.org/hibernate/core/3.3/reference/en/html/queryhql.html)).
So, we have a dedicated tool to interrogate a database, and a related query language. Now it's time to register a new query executer factory:

```
net.sf.jasperreports.query.executer.factory.hql=net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory
```

The [JRHibernateQueryExecuterFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/j2ee/hibernate/JRHibernateQueryExecuterFactory.html) creates [JRHibernateQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/j2ee/hibernate/JRHibernateQueryExecuter.html) instances to handle data access through Hibernate APIs. The factory automatically defines a parameter named `HIBERNATE_SESSION` of type `org.hibernate.Session`, used by the query executer to create the query.

HQL queries can embed two types of parameters:

- Query parameters: embedded using the `$P{..}` syntax.
- Statement substitution parameters: embedded using the `$P!{..}` syntax.

The HQL query execution can be configured via the following properties:

- `net.sf.jasperreports.hql.query.run.type` - configures how the query result is fetched. It can take the following possible values: `list, scroll, iterate`.
- `net.sf.jasperreports.jdbc.fetch.size` - specifies the fetch size.
- `net.sf.jasperreports.hql.clear.cache` - flag used to clear the Hibernate’s first-level cache after each page fetching when working with a large amount of data.
- `net.sf.jasperreports.hql.query.list.page.size` - enables paginated result retrieval and specifies the page size.
- `net.sf.jasperreports.hql.field.mapping.descriptions` - instructs the engine to use rather field descriptions than names when mapping report fields to values from the Hibernate result.

### The Hibernate Sample

This sample shows how to perform queries and retrieve data from a relational database using Hibernate. The output consists in two separate reports: the standalone document AddressesReport, and the HibernateQueryReport which contains a subreport. Two tables in the HSQLDB database are involved: `ADDRESS` and `DOCUMENT`.

The Hibernate configuration information is kept in the `src/hibernate.cfg.xml` file:

```
<hibernate-configuration>

  <session-factory>
    <property name="connection.driver_class">org.hsqldb.jdbcDriver</property>
    <property name="connection.url">jdbc:hsqldb:hsql://localhost</property>
    <property name="connection.username">sa</property>
    <property name="connection.password"></property>
    <property name="dialect">org.hibernate.dialect.HSQLDialect</property>
    <property name="connection.pool_size">1</property>
    <mapping resource="Address.hbm.xml"/>
    <mapping resource="Document.hbm.xml"/>
  </session-factory>

</hibernate-configuration>
```

In order to create a Hibernate session one has to specify the properties and mapping resources in the `<session-factory>` element above.

In our case there are two mapping resources configurated in the XML files below:

1. `src/Address.hbm.xml` mapping the `Address` entity object (See the `src/Address.java` file) to the `ADDRESS` table in the database:

```
  <hibernate-mapping>

  <class name="Address" table="ADDRESS">
    <id name="id" column="ID">
      <generator class="increment"/>
    </id>

    <property name="firstName" column="FIRSTNAME"/>
    <property name="lastName" column="LASTNAME"/>
    <property name="street" column="STREET"/>
    <property name="city" column="CITY"/>

    <set name="documents" inverse="true">
      <key column="ADDRESSID"/>
      <one-to-many class="Document"/>
    </set>
  </class>

  </hibernate-mapping>
```

2. `src/Document.hbm.xml` mapping the `Document` entity object (See the `src/Document.java` file) to the `DOCUMENT` table in the database:

```
  <hibernate-mapping>

    <class name="Document" table="DOCUMENT">
      <id name="id" column="ID">
        <generator class="increment"/>
      </id>

      <many-to-one name="address" column="ADDRESSID"/>

      <property name="total" column="TOTAL"/>
    </class>

  </hibernate-mapping>
```

One can see above that an `Address` object contains a set of `Document` objects, all with the same `ADDRESSID` value. The one-to-many relationship between the `ADDRESS` table and `DOCUMENT` is intermediated by the `ADDRESS.ADDRESSID` foreign key column.

The `AddressReport` enumerates customers with their addresses and related documents and document totals, filtered by their city location (see the `CityFilter` parameter). The HQL query string is the following:

```
  <parameter name="CityFilter" class="java.util.List"/>
  <query language="hql">
    <![CDATA[select address as address, document.id as documentId, document.total as documentTotal
      from Address as address join address.documents as document
      where city not in ($P{CityFilter})
      order by address.city, address.lastName, address.firstName, address.id]] >
  </query>
```

In the query above, because of the table join, customers with no related documents in the `DOCUMENTS` table are not included.

The `HibernateQueryReport` report retrieves all customer addresses from the `ADDRESS` table, without taking into account if there are or no customer related documents in the `DOCUMENTS` table. Customers are also filtered by the `CityFilter` parameter. If related documents exist, their data are retrieved using the `DocumentsReport` subreport (see the `reports/DocumentsReport.jrxml` file).

Here customers with no related documents are included too.\
Customers are also grouped by their city.

Now it's time to take a look in the `reports/HibernateQueryReport.jrxml` file at the report HQL query string:

```
  <query language="hql">
    <![CDATA[from Address address where city not in ($P{CityFilter}) order by $P!{OrderClause}]] >
  </query>
```

And then, at the `Documents` subreport, for each customer:

```
<element kind="subreport" x="65" y="21" width="50" height="20" removeLineWhenBlank="true">
  <dataSourceExpression><![CDATA[new JRBeanCollectionDataSource($F{documents})]] ></dataSourceExpression>
  <expression><![CDATA["DocumentsReport.jasper"]] ></expression>
</element>
```

The `CityFilter` parameter and all other report parameters are created in the `src/HibernateApp.java` class file, via the `getParameters(Session session)` method:

```
private static Map<String, Object> getParameters(Session session)
{
  Map<String, Object> parameters = new HashMap<String, Object>();
  parameters.put(HibernateConstants.PARAMETER_HIBERNATE_SESSION, session);
  parameters.put("ReportTitle", "Address Report");
  List<String> cityFilter = new ArrayList<String>(3);
  cityFilter.add("Boston");
  cityFilter.add("Chicago");
  cityFilter.add("Oslo");
  parameters.put("CityFilter", cityFilter);
  parameters.put("OrderClause", "city");
  return parameters;
}
```

At fill time, a Hibernate session is created and a transaction gets started. Report parameters are set and then the two compiled reports are filled with data. If all goes ok, the transaction is then rolled back and the session is closed:

```
public void fill() throws JRException
{
  SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
  Session session = sessionFactory.openSession();
  Transaction transaction = session.beginTransaction();

  Map<String, Object> params = getParameters(session);

  File[] files =
    new File[]{
    new File("target/reports/AddressesReport.jasper"),
      new File("target/reports/HibernateQueryReport.jasper")
    };
  for(int i = 0; i < files.length; i++)
  {
    File reportFile = files[i];
    long start = System.currentTimeMillis();
    JasperFillManager.fillReportToFile(reportFile.getAbsolutePath(), new HashMap<String, Object>(params));
    System.err.println("Report : " + reportFile + ". Filling time : " + (System.currentTimeMillis() - start));
  }

  transaction.rollback();
  sessionFactory.close();
}
```

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/hibernate-j2ee` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/hibernate-j2ee/target/reports` directory.
