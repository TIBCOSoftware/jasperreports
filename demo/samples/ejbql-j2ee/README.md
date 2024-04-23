
# JasperReports - EJBQL Sample (Using Legacy Java EE API) <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how EJBQL could be used in reports, based on the legacy Java EE API.

[EJBQL Query Executer](#ejbql)

### Secondary Features

[Query Executers](../hibernate/README.md#queryexecuters)

## <a name='top'>EJBQL</a> Query Executer
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill reports using embedded EJBQL queries (using legacy Java EE API).

**Since:** 1.2.3

**Other Samples**\
[/demo/samples/ejbql](../ejbql)\
[/demo/samples/csvdatasource](../csvdatasource)\
[/demo/samples/hibernate](../hibernate)\
[/demo/samples/mondrian](../mondrian)\
[/demo/samples/xmldatasource](../xmldatasource)

### The EJB QL/JPA Query Executer

The EJB QL query executer adds support for reporting on EJB 3.0 persistent entities data, based on [Java Persistence API](https://www.oracle.com/technical-resources/articles/java/jpa.html) . For an EJB QL query in a report, the query executer will use the EJB 3.0 with [Java Persistence API](https://www.oracle.com/technical-resources/articles/java/jpa.html) to execute the query against an entity manager provided at runtime, and use the query result as a data source for the report.

The built-in EJB QL query executer is registered by default for queries having `EJBQL` or `ejbql` set as their language. This mapping can be changed by using the related JasperReports properties (see properties in the category `net.sf.jasperreports.query.executer.factory.{language}`).

Two built-in parameters are involved in the query execution:

- [`PARAMETER_JPA_ENTITY_MANAGER`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/j2ee/ejbql-j2ee/JRJpaQueryExecuterFactory.html#PARAMETER_JPA_ENTITY_MANAGER) - that specifies the entity manager to be used for executing the query, depending on the particular `EJB/JPA` environment and implementation
- [`JPA_QUERY_HINTS_MAP`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/j2ee/ejbql-j2ee/JRJpaQueryExecuterFactory.html#JPA_QUERY_HINTS_MAP) - that contains a map with hint values mapped on hint names, to be used when running the query. Hints can also be specified statically by using report properties. The query executer treats any report property in the category `net.sf.jasperreports.ejbql.query.hint.{hintName}` as a hint by interpreting the property suffix as the hint name and the property value as the hint value.

An example of query hint property is the following:

```
<property name="net.sf.jasperreports.ejbql.query.hint.cacheType" value="Shared"/>
```

A separate report property can be used to paginate the query result in order to control the amount of Java heap space used by the query executer while filling the report. The property can be set in the following manner:

```
<property name="net.sf.jasperreports.ejbql.query.page.size" value="500"/>
```

meaning that the query result will be fetched in chunks containing 500 rows each. The pagination is achieved via the `javax.persistence.Query.setMaxResults()` and `setFirstResult()` methods.\
Obviously, using pagination could result in performance loss. Therefore enabling it is primarily recommended when the query results are very large.

The result of the query execution is sent to a data source implementation, which iterates over it and extracts report field values. Fields are mapped to specific values in the query result by specifying the mapping as field description or field name. The `JPA` data source can handle two types of query results:

- Queries returning a single entity/bean per row - in this case field mappings are interpreted as bean property names.
- Queries returning object tuples as rows - fields are mapped using one of the following forms:
    - `COLUMN_<index>` - the field is mapped to a value specified by its position in the resulting tuple. The positions start from 1.
    - `COLUMN_<index>.<property>` - the field is mapped to a property of a value specified by its position in the resulting tuple.

### The EJB QL/JPA Query Executer Sample

The movie database sample in the `demo/samples/ejbql` directory is structured as follows:

- the `data` directory contains the SQL script that creates and populates the following tables in the built-in HSQL database:
    - `PERSON` - stores people IDs and names
    - `MOVIE` - stores movie ID, director, title, genre and release date informations
    - `MOVIE_CAST` - stores ID, movie ID, actor ID, character and role importance
    - `MOVIE_VARIA` - stores ID, movie ID, type, description, importance
- the `docs` directory contains the xml file used to generate this documentation
- the `lib` directory contains 3-rd party jars with necessary persistence APIs
- the `reports` directory contains, as usual, report templates (1 master and 2 subreports)
- the `src` directory contains a META-INF subdirectory with the persistence.xml persistence configuration file:

```
<persistence-unit name="pu1">
  <!-- Provider class name is required in Java SE -->
  <provider>org.hibernate.ejb.HibernatePersistence</provider>
  <!-- All persistence classes must be listed -->
  <class>Person</class>
  <class>Movie</class>
  <class>Cast</class>
  <class>Varia</class>    
  <properties>
    <!-- Provider-specific connection properties -->
    <property name="hibernate.connection.driver_class" value="org.hsqldb.jdbcDriver"/>
    <property name="hibernate.connection.url" value="jdbc:hsqldb:file:build/db"/>
    <property name="hibernate.connection.username" value="sa"/>
    <property name="hibernate.connection.password" value=""/>
    <!-- Provider-specific settings -->
    <property name="hibernate.dialect" value="org.hibernate.dialect.HSQLDialect"/>
  </properties>    
</persistence-unit>
```

the annotated entity classes mapped on the tables in the database: `Person, Movie, Cast` and `Varia`. Corresponding tables are specified in the `@Table` annotation:

```
@Entity
@Table(name="movie_cast")
public class Cast {
...
}
```

the `EjbqlApp.java` file that hosts the `main` method.

The `reports/JRMDbReport.jrxml` master report is structured to present movies in the database along with their additional informations, such as casting, awards, quotes, etc. Casts are managed separately in the `reports/JRMDbCastSubreport.jrxml` template, and the rest of additional information is provided by the `reports/JRMDbVariaSubreport.jrxml`

Looking into the `reports/JRMDbReport.jrxml`, one could notice the specific hint properties, the EJB QL query syntax, report fields declarations and how the subreports were set:

```
...
<property name="net.sf.jasperreports.ejbql.query.hint.fetchSize" value="50"/>
<property name="net.sf.jasperreports.ejbql.query.page.size" value="100"/>
<import>net.sf.jasperreports.engine.data.JRBeanCollectionDataSource</import>
...
<query language="ejbql">
  <![CDATA[SELECT   m
    FROM     Movie m
    WHERE    m.releaseDate BETWEEN $P{DateFrom} AND $P{DateTo}
    ORDER BY $P!{OrderClause}]] >
</query>
<field name="id" class="java.lang.Integer"/>
<field name="director.name" class="java.lang.String"/>
<field name="title" class="java.lang.String"/>
<field name="genre" class="java.lang.String"/>
<field name="releaseDate" class="java.sql.Date"/>
<field name="cast" class="java.util.Collection"/>
...
<detail>
  <band height="45">
    ...
  <element kind="subreport" positionType="Float" x="15" y="25" width="245" height="20" backcolor="#99CCFF" removeLineWhenBlank="true">
    <dataSourceExpression><![CDATA[new JRBeanCollectionDataSource($F{cast})]] ></dataSourceExpression>
    <expression><![CDATA["JRMDbCastSubreport.jasper"]] ></expression>
  </element>
  <element kind="subreport" positionType="Float" x="270" y="25" width="245" height="20" backcolor="#99CCFF" removeLineWhenBlank="true">
    <expression><![CDATA["JRMDbVariaSubreport.jasper"]] ></expression>
    <parameter name="MovieId">
      <expression><![CDATA[$F{id}]] ></expression>
    </parameter>
    <parameter name="JPA_ENTITY_MANAGER">
      <expression><![CDATA[$P{JPA_ENTITY_MANAGER}]] ></expression>
    </parameter>
  </element>
  </band>
</detail>
...
```

The `Movie` entities provide an id, a director (who is a `Person` entity), a title, a genre, releaseDate and a collection of cast entities. Notice the report field `director.name` that refers to the name property in the director entity.

The casts collection in the cast field is passed as a [JRBeanCollectionDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRBeanCollectionDataSource.html) to the casting subreport.

The Varia subreport has no expression for data source/connection, but takes the built-in [`PARAMETER_JPA_ENTITY_MANAGER`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/j2ee/ejbql-j2ee/JRJpaQueryExecuterFactory.html#PARAMETER_JPA_ENTITY_MANAGER) parameter into consideration, and the movie ID, in order to identify the `Varia` entities related to the movie.

The `reports/JRMDbCastSubreport.jrxml` provides no query, because the data source comes already prepared here. There are two fields declared in the report:

```
<field name="actor.name" class="java.lang.String"/>
<field name="character" class="java.lang.String"/>
```

Again, the actor.name field references the name property in the actor entity of type `Person`.
In the `reports/JRMDbVariaSubreport.jrxml` one could notice the presence of an EJB QL query and field descriptions based on column positions:

```
...
<parameter name="MovieId" class="java.lang.Integer"/>
<query language="ejbql">
  <![CDATA[SELECT   v.type, v.description
    FROM     Varia v
    WHERE    v.movie.id = $P{MovieId}
    ORDER BY v.importance]] >
</query>
<field name="type" class="java.lang.String">
  <property name="net.sf.jasperreports.javabean.field.property" value="COLUMN_1"/>
</field>
<field name="description" class="java.lang.String">
  <property name="net.sf.jasperreports.javabean.field.property" value="COLUMN_2"/>
</field>
...
```

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/ejbql-j2ee` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/ejbql-j2ee/target/reports` directory.
