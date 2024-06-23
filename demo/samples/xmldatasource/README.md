
# JasperReports - XML Data Source Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the built-in XPath-based data source could be used for reporting out of XML data.

### Main Features in This Sample

[XML Data Source](#xmldatasource)\
[XPath Query Executer](#xpathqueryexecuter)

### Secondary Features
[Query Executers](../hibernate/README.md#queryexecuters)\
[Data Sources](../datasource/README.md#datasources)\
[Subreports](../subreports/README.md#subreports)

## <a name='xmldatasource'>XML</a> Data Source
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill a report using data from an XML file.

**Since:** 0.6.0

### XML Data Sources

XML documents can be used as report data sources based on appropriate [JRDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDataSource.html) implementations. JasperReports features a built-in XML data source implementation ([JRXmlDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRXmlDataSource.html)) that relies on DOM and uses XPath expressions to select data from the XML document.

To instantiate an XML data source, the following inputs are required:

- An XML document. The parsed document, its location, or its source is provided as an argument to the data source constructor.
- An XPath expression to select the node set that corresponds to the data source record list. The expression is passed to the data source as a constructor argument. The default XPath expression selects the document node itself. The XPath expression is executed when the data source is instantiated; each item in the resulting node set will generate a record in the data source.
- An XPath expression to select the field value for every field in the data set, when iterating through records. The field’s XPath expression is provided by the [`net.sf.jasperreports.xpath.field.expression`](https://jasperreports.sourceforge.net/config.reference.html#net.sf.jasperreports.xpath.field.expression) custom property at field level. The use of the [`JRField#getDescription()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRField.html#getDescription()) field description to specify the XPath expression is still supported, but is now discouraged, the above mentioned custom property taking precedence over the field description.

One interesting particularity of XML data sources is that they can be used to create sub–data sources to be used for subreports or subdatasets, using different XPath expressions to select the appropriate nodes. There are two ways to accomplish this:

- A sub–data source can be created for a new document that uses the current node as a root node.
- The same document can be reused for a new sub–data source, which would specify a different XPath expression for the main node set.

XML data sources are created by interpreting XPath expressions and selecting nodes and values from the XML document based on these expressions. Processing XPath expressions relies on a generic service interface called [JRXPathExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/xml/JRXPathExecuter.html). The XPath executer implementation used by XML data sources can be configured with a JasperReports property named `net.sf.jasperreports.xpath.executer.factory`. This property gives the name of an XPath executer factory class, which has to implement the [JRXPathExecuterFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/xml/JRXPathExecuterFactory.html).

The XML data source provides localization support for both number and date/time values rendered as text in the wrapped XML document. In order to get the appropriate localization one have to set the following information in the [JRXmlDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRXmlDataSource.html) object:

- the Number pattern - see the `setNumberPattern(java.lang.String)` method;
- the Date pattern - see the `setDatePattern(java.lang.String)` method;
- the Locale property - see the `setLocale(java.util.Locale)` method;
- the time zone - see the `setTimeZone(java.util.TimeZone)` method.

Patterns should be non-localized and in accordance with the `java.text.DecimalFormat` and `java.text.SimpleDateFormat` pattern syntax. If specific patterns are not supplied, the defaults for these two format classes apply.

Before running the sample, please consult the next section regarding the XPath query executer.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='xpathqueryexecuter'>XPath</a> Query Executer
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill reports using embedded XPath queries.

**Since:** 1.2.0

**Other Samples**\
[/demo/samples/csvdatasource](../csvdatasource/README.md)\
[/demo/samples/ejbql](../ejbql/README.md)\
[/demo/samples/hibernate](../hibernate/README.md)\
[/demo/samples/mondrian](../mondrian/README.md)

### The XPath Query Executer

XPath (the XML Path language) is an expression language with specific syntax used to navigate through nodes in an XML document and retrieve their information, based on a tree representation of XML documents. When using XML data sources, one can specify an XPath expression that produces the list of nodes/records as the report query:

```
  <query language="xPath"><![CDATA[/Northwind/Orders[CustomerID='ALFKI']]]></query>
```

This query returns a list containing only Orders elements with `CustomerID` set to `ALFKI`.

In the case of the XPath language the query executer factory (see the [JRXPathQueryExecuterFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRXPathQueryExecuterFactory.html) class) registers a parameter named [XML_DATA_DOCUMENT](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRXPathQueryExecuterFactory.html#PARAMETER_XML_DATA_DOCUMENT) of type `org.w3c.dom.Document`. The XPath query executer (see the [JRXPathQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRXPathQueryExecuter.html) class) runs the XPath query against this document and stores the result in an in-memory [JRXmlDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRXmlDataSource.html) object. For more information about XML data sources please consult the XML Data Sources above section.

Being an expression language itself, XPath supports parameters. Parameters will be processed and replaced by their `java.lang.String` values. When creating the [JRXmlDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRXmlDataSource.html) instance, the query executer also provides the four additional parameters containing localization settings required by the data source:

- [`XML_LOCALE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRXPathQueryExecuterFactory.html#XML_LOCALE)
- [`XML_NUMBER_PATTERN`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRXPathQueryExecuterFactory.html#XML_NUMBER_PATTERN)
- [`XML_DATE_PATTERN`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRXPathQueryExecuterFactory.html#XML_DATE_PATTERN)
- [`XML_TIME_ZONE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRXPathQueryExecuterFactory.html#XML_TIME_ZONE)

In the next section you can see how these additional parameters are set in the `fill()` method of the `/src/XmlDataSourceApp.java` class.

For more information about the XPath syntax and queries, one can consult the official documentation: http://www.w3.org/TR/xpath20.

### XML Data Source Example

In our example data records are stored as node elements in the `data/northwind.xml` file.

The `<Northwind/>` root element contains two children types: `<Customers/>` and `<Orders/>`. Below is an example of a `<Customers/> `entity, completely characterized by the following elements:

```
  <Customers>
    <CustomerID>ALFKI</CustomerID>
    <CompanyName>Alfreds Futterkiste</CompanyName>
    <ContactName>Maria Anders</ContactName>
    <ContactTitle>Sales Representative</ContactTitle>
    <Address>Obere Str. 57</Address>
    <City>Berlin</City>
    <PostalCode>12209</PostalCode>
    <Country>Germany</Country>
    <Phone>030-0074321</Phone>
    <Fax>030-0076545</Fax>
  </Customers>
```

And below is an example of an <Orders/> entity:

```
  <Orders>
    <OrderID>10643</OrderID>
    <CustomerID>ALFKI</CustomerID>
    <EmployeeID>6</EmployeeID>
    <OrderDate>1997-08-25</OrderDate>
    <RequiredDate>1997-09-22</RequiredDate>
    <ShippedDate>1997-09-02</ShippedDate>
    <ShipVia>1</ShipVia>
    <Freight>29.46</Freight>
    <ShipName>Alfreds Futterkiste</ShipName>
    <ShipAddress>Obere Str. 57</ShipAddress>
    <ShipCity>Berlin</ShipCity>
    <ShipPostalCode>12209</ShipPostalCode>
    <ShipCountry>Germany</ShipCountry>
  </Orders>
```

One can see that an `<Orders>` element has a `<CustomerID>` property which points to the `<CustomerID>` element in the `<Customers>` node, just like a foreign key in a relational database. One can identify a one-to-many relationship between `<Customers>` and `<Orders>`.

In order to navigate through elements in the `Northwind.xml` document, an XPath query executer implementation should be specified using the `net.sf.jasperreports.xpath.executer.factory` property. In our case it is set in the `src/jasperreports.properties` file:

```
net.sf.jasperreports.xpath.executer.factory=net.sf.jasperreports.jaxen.util.xml.JaxenXPathExecuterFactory
```

Two [JRXmlDataSource]([JRXmlDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRXmlDataSource.html)) objects are necessary to store the two element types. One of them will be populated with `Customers` records. In this case record field names are given by the property names in the `<Customers/>` node:

```
CustomerID
CompanyName
ContactName
Sales Representative
ContactTitle
Address
City
PostalCode
Country
Phone
Fax
```

The other [JRXmlDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRXmlDataSource.html) object will be populated with `Orders` records. For this record type, field names are:

```
OrderID
CustomerID
EmployeeID
OrderDate
RequiredDate
ShippedDate
ShipVia
Freight
ShipName
ShipAddress
ShipCity
ShipPostalCode
ShipCountry
```

As we'll see later, not all fields above are required by the generated report.

There are two JRXML files in the reports directory. The `CustomersReport.jrxml` is the main report template. The `OrdersReport.jrxml` is used for generate subreports for the main report.\
The main report iterates through available customers in the XML data source, and prints for each of them the `Customer ID`, the `Company Name` and the own list of ship orders. The orders list exposes only the `Order ID`, `Order Date`, `Ship City` and `Freight` fields. For each customer are calculated the orders count and the total freight.

In the `CustomersReport.jrxml` template the query language and XPath query expression are declared in the `<query/>` expression:

```
  <query language="xPath"><![CDATA[/Northwind/Customers]]></query>
```

The fields required for each Customer in the main report are:

```
  <field name="CustomerID" class="java.lang.String">
    <property name="net.sf.jasperreports.xpath.field.expression" value="CustomerID"/>
  </field>
  <field name="CompanyName" class="java.lang.String">
    <property name="net.sf.jasperreports.xpath.field.expression" value="CompanyName"/>
  </field>
```

The `OrdersReport` subreport is called when the orders list has to be completed for each customers:

```
<element kind="subreport" printRepeatedValues="false" removeLineWhenBlank="true" ...>
  <expression><![CDATA["OrdersReport.jasper"]] ></expression>
  <parameter name="XML_DATA_DOCUMENT">
    <expression><![CDATA[$P{XML_DATA_DOCUMENT}]] ></expression>
  </parameter>
  <parameter name="XML_DATE_PATTERN">
    <expression><![CDATA[$P{XML_DATE_PATTERN}]] ></expression>
  </parameter>
  <parameter name="XML_NUMBER_PATTERN">
    <expression><![CDATA[$P{XML_NUMBER_PATTERN}]] ></expression>
  </parameter>
  <parameter name="XML_LOCALE">
    <expression><![CDATA[$P{XML_LOCALE}]] ></expression>
  </parameter>
  <parameter name="XML_TIME_ZONE">
    <expression><![CDATA[$P{XML_TIME_ZONE}]] ></expression>
  </parameter>
  <parameter name="CustomerID">
    <expression><![CDATA[$F{CustomerID}]] ></expression>
  </parameter>
</element>
```

The Number pattern, Date pattern, Locale and time zone parameters above are necessary for data formatting. The `CustomerID` parameter is required in order to filter data in the subreport.

The `<query/>` expression in the `reports/OrdersReport.jrxml` template is:

```
  <query language="xPath"><![CDATA[/Northwind/Orders[CustomerID='$P{CustomerID}']]]></query>
```

Only `Orders` with the given `CustomerID` are taken into account.

The only fields required from an `Orders` record are:

```
  <field name="Id" class="java.lang.String">
	<property name="net.sf.jasperreports.xpath.field.expression" value="OrderID"/>
  </field>
  <field name="OrderDate" class="java.util.Date">
	<property name="net.sf.jasperreports.xpath.field.expression" value="OrderDate"/>
  </field>
  <field name="ShipCity" class="java.lang.String">
	<property name="net.sf.jasperreports.xpath.field.expression" value="ShipCity"/>
  </field>
  <field name="Freight" class="java.lang.Float">
	<property name="net.sf.jasperreports.xpath.field.expression" value="Freight"/>
  </field>
```

In the `src/XmlDataSourceApp.java` all necessary information is prepared to be sent at fill time: the parsed `Northwind` document, data formatting parameters and the `CustomersReport` compiled report:

```
  public void fill() throws JRException
  {
  	long start = System.currentTimeMillis();
  	Map<String, Object> params = new HashMap<String, Object>();
  	Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream("data/northwind.xml"));
  	params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
  	params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
  	params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
  	params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
  	params.put(JRParameter.REPORT_LOCALE, Locale.US);

  	JasperFillManager.fillReportToFile("target/reports/CustomersReport.jasper", params);
  	System.err.println("Filling time : " + (System.currentTimeMillis() - start));
  }
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/xmldatasource` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/xmldatasource/target/reports` directory.
