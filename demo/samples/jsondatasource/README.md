
# JasperReports - JSON Data Source Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the JSON data source and the JSON query executer implementations could be used to fill reports.

### Main Features in This Sample

[JSON Data Source](#jsondatasource)

## <a name='jsondatasource'>JSON</a> Data Source
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill a report using data from a JSON file.

**Since:** 4.0.1

### JSON Data Source Overview

JSON stands for **J**ava**S**cript **O**bject **N**otation and represents an open standard text format used to transmit data across the network. The main purpose of the JSON format is to provide an alternate way to XML for transporting data between a server and a client (web) application. Wherever possible, the use of a JSON data source implementation (see the built-in [JsonDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/data/JsonDataSource.html)) is recommended, because:

- JSON comes with less memory consumption
- data is retrieved faster from JSON than from an equivalent XML stream
- the JSON syntax rules are very simple, they represent a subset of JavaScript rules:
    - data objects contain properties organized as name : value pairs, in a hierarchical structure
    - each property inside an object has a specific value. Values of a JSON property can be:
        - a number (integer or floating point)
        - a boolean (true or false)
        - a string
        - an object (set of name : value pairs)
        - an array
        - null
    - objects are enclosed in curly braces
    - arrays are enclosed in square brackets; elements in an array are comma-separated
    - distinct name : value pairs are comma-separated
    - there are no reserved words

Let's see, for instance, the JSON objects inside the `data/northwind.json` source file:

```
{"Northwind": {
  "Customers": [
    {
      Phone: "030-0074321",	// nonstandard unquoted field name
      'PostalCode': 12209,	// nonstandard single-quoted field name
      "ContactName": "Maria Anders",	// standard double-quoted field name
      "Fax": "030-0076545",
      "Address": "Obere Str. 57",
      "CustomerID": "ALFKI",
      "CompanyName": "Alfreds Futterkiste",
      "Country": "Germany",
      "City": "Berlin",
      "ContactTitle": "Sales Representative"
    },

    ...

    {
      "Phone": "(26) 642-7012",
      "PostalCode": "01-012",
      "ContactName": "Zbyszek Piestrzeniewicz",
      "Fax": "(26) 642-7012",
      "Address": "ul. Filtrowa 68",
      "CustomerID": "WOLZA",
      "CompanyName": "Wolski  Zajazd",
      "Country": "Poland",
      "City": "Warszawa",
      "ContactTitle": "Owner"
    }
  ],

  "Orders": [
    {
      "ShipPostalCode": 51100,
      "ShippedDate": "1996-07-16",
      "OrderDate": "1996-07-04",
      "OrderID": 10248,
      "Freight": 32.38,
      "RequiredDate": "1996-08-01",
      "ShipCity": "Reims",
      "ShipCountry": "France",
      "EmployeeID": 5,
      "ShipVia": 3,
      "CustomerID": "VINET",
      "ShipAddress": "59 rue de l'Abbaye",
      "ShipName": "Vins et alcools Chevalier"
    },

    ...

    {
      "ShipPostalCode": 87110,
      "OrderDate": "1998-05-06",
      "OrderID": 11077,
      "Freight": 8.53,
      "ShipRegion": "NM",
      "RequiredDate": "1998-06-03",
      "ShipCity": "Albuquerque",
      "ShipCountry": "USA",
      "EmployeeID": 1,
      "ShipVia": 2,
      "CustomerID": "RATTC",
      "ShipAddress": "2817 Milton Dr.",
      "ShipName": "Rattlesnake Canyon Grocery"
    }
  ]
}}
```

There is a `Northwind` parent object enclosing 2 comma-separated objects: `Customers` and `Orders`.

The `Customers` object contains a list of similar structured objects, each representing a customer with the following properties:

- `"Phone"`
- `"PostalCode"`
- `"ContactName"`
- `"Fax"`
- `"Address"`
- `"CustomerID"`
- `"CompanyName"`
- `"Country"`
- `"City"`
- `"ContactTitle"`

The `Orders` object contains a list of order objects, each one exposing the following properties:

- `"ShipPostalCode"`
- `"OrderDate"`
- `"OrderID"`
- `"Freight"`
- `"ShipRegion"`
- `"RequiredDate"`
- `"ShipCity"`
- `"ShipCountry"`
- `"EmployeeID"`
- `"ShipVia"`
- `"CustomerID"`
- `"ShipAddress"`
- `"ShipName"`

### The JSON Query Executer

Properties within a JSON object can be accessed using the period notation. Therefore we have the possibility to query a JSON data source, like below:

```
Northwind.Orders
```

or

```
Northwind.Orders[0].OrderID
```

This can be used as a JSON query expression language to navigate through objects hierarchy in a source document and retrieve their information, based on a tree representation of objects. The query string is processed using the [Jackson](http://wiki.fasterxml.com/JacksonHome) library APIs.

For instance, one can specify the following expression to produce a list of JSON objects as the report query:

```
<query language="json"><![CDATA[Northwind.Customers]] ></query>
```

This query returns a list containing all customer elements in the Customers object. If we need to retrieve only customers from USA, the query have to be refined as:

```
<query language="json">
  <![CDATA[Northwind.Customers(Country == USA)]] >
</query>
```

The JSON query language also supports parameters, in order to allow dynamic queries. Parameters are processed at runtime and replaced by their values. For instance, if we define the `Country` parameter that holds the name of a given country, the parameterized query will look like:

```
<parameter name="Country" class="java.lang.String"/>
...
<query language="json"><![CDATA[Northwind.Orders(CustomerID == $P{CustomerID})]] ></query>
```

The built-in JSON query executer (see the [JsonQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuter.html) class) is a tool that uses the query string to produce a [JsonDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/data/JsonDataSource.html) instance, based on specific built-in parameters (or equivalent report properties). This query executer is registered via [JsonQueryExecuterFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html) factory class.

In order to prepare the data source, the JSON query executer looks for the [JSON_INPUT_STREAM](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_INPUT_STREAM) parameter that contains the JSON source objects in the form of an `java.io.InputStream`. If no [JSON_INPUT_STREAM](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_INPUT_STREAM) parameter is provided, then the query executer looks for the alternate `net.sf.jasperreports.json.source` String parameter or report property that stores the path to the location of the JSON source file.

[JsonQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuter.html) runs the query over the input source and stores the result in an in-memory [JsonDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/data/JsonDataSource.html) object.

During the [JsonDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/data/JsonDataSource.html) instantiation, the query executer also looks for the following additional parameters or report properties, containing the required localization settings:

- [`net.sf.jasperreports.json.date.pattern`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_DATE_PATTERN)
- [`net.sf.jasperreports.json.number.pattern`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_NUMBER_PATTERN)
- [`JSON_LOCALE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_LOCALE) (parameter only) of type `java.util.Locale`
- [`net.sf.jasperreports.json.locale.code`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_LOCALE_CODE) of type `java.lang.String`; this can be used if no `java.util.Locale` parameter is available
- [`JSON_TIME_ZONE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_TIME_ZONE) (parameter only) of type `java.util.TimeZone`
- [`net.sf.jasperreports.json.timezone.id`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_TIMEZONE_ID) of type `java.lang.String`; this can be used if no `java.util.TimeZone` parameter is available

In the next section you can see how these additional parameters are provided in the `/src/JsonDataSourceApp.java` class.

### The JSON Data Source Sample

In our example data are stored as a hierarchy of `Northwind.Customers` and `Northwind.Orders` objects in the `data/northwind.json` file.

The source file name is provided in the `reports/JsonCustomersReport.jrxml` via the report property:

```
<property name="net.sf.jasperreports.json.source" value="data/northwind.json"/>
```

In the `JsonCustomersReport` we run a JSON query in order to retrieve only the customers:

```
<query language="json"><![CDATA[Northwind.Customers]] ></query>
```

The only `Customer` properties (fields) we are interested in are `CustomerID` and `CompanyName`:

```
<field name="CustomerID" class="java.lang.String">
  <property name="net.sf.jasperreports.json.field.expression" value="CustomerID"/>
</field>
<field name="CompanyName" class="java.lang.String">
  <property name="net.sf.jasperreports.json.field.expression" value="CompanyName"/>
</field>
```

Additional parameters are passed to the report execution in the `/src/JsonDataSourceApp.java` class (see the `fill()` method):

```
public void fill() throws JRException
{
  long start = System.currentTimeMillis();

  Map<String, Object> params = new HashMap<String, Object>();
  params.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, "yyyy-MM-dd");
  params.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, "#,##0.##");
  params.put(JsonQueryExecuterFactory.JSON_LOCALE, Locale.ENGLISH);
  params.put(JRParameter.REPORT_LOCALE, Locale.US);

  JasperFillManager.fillReportToFile("build/reports/JsonCustomersReport.jasper", params);
  System.err.println("Filling time : " + (System.currentTimeMillis() - start));
}
```

Each customer in the datasource requires an Orders subreport. Data source and parameters are transmitted from the master report:

```
<element kind="subreport" ... backcolor="#FFCC99" printRepeatedValues="false" removeLineWhenBlank="true">
  <expression><![CDATA["JsonOrdersReport.jasper"]] ></expression>
  <parameter name="net.sf.jasperreports.json.date.pattern">
    <expression><![CDATA[$P{net.sf.jasperreports.json.date.pattern}]] ></expression>
  </parameter>
  <parameter name="net.sf.jasperreports.json.number.pattern">
    <expression><![CDATA[$P{net.sf.jasperreports.json.number.pattern}]] ></expression>
  </parameter>
  <parameter name="JSON_LOCALE">
    <expression><![CDATA[$P{JSON_LOCALE}]] ></expression>
  </parameter>
  <parameter name="CustomerID">
    <expression><![CDATA[$F{CustomerID}]] ></expression>
  </parameter>
  <parameter name="net.sf.jasperreports.json.source">
    <expression><![CDATA["data/northwind.json"]] ></expression>
  </parameter>
</element>
```

The `CustomerID` parameter is required in order to filter data in the subreport.

Next, in the reports/JsonOrdersReport.jrxml file one can see a parametrized query. All orders related to a given `CustomerID` are retrieved:

```
<query language="json"><![CDATA[Northwind.Orders(CustomerID == $P{CustomerID})]] ></query>
From each order we collect the following properties:
<field name="Id" class="java.lang.String">
  <property name="net.sf.jasperreports.json.field.expression" value="OrderID"/>
</field>
<field name="OrderDate" class="java.util.Date">
  <property name="net.sf.jasperreports.json.field.expression" value="OrderDate"/>
</field>
<field name="ShipCity" class="java.lang.String">
  <property name="net.sf.jasperreports.json.field.expression" value="ShipCity"/>
</field>
<field name="Freight" class="java.lang.Float">
  <property name="net.sf.jasperreports.json.field.expression" value="Freight"/>
</field>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/jsondatasource` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/jsondatasource/target/reports` directory.
