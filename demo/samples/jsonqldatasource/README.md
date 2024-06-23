
# JasperReports - JSONQL Data Source Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the JSONQL data source and the JSONQL query executer implementations could be used to fill reports.

### Main Features in This Sample

[JSONQL Data Source](#jsonqldatasource)\
[The JSONQL query language](#jsonql)

## <a name='jsonqldatasource'>JSONQL</a> Data Source
<div align="right">Documented by <a href='mailto:narcism@users.sourceforge.net'>Narcis Marcu</a></div>

**Description / Goal**\
How to fill a report with data from a JSON file using the JSONQL query language.

**Since:** 6.3.1

### JSONQL Data Source Overview

JSONQL stands for **JSON Q**uery **L**anguage and it is meant to replace the existing language(simply called JSON) for querying JSON data, but in a separate implementation as they are not compatible in terms of syntax and complexity.

It is recommended that you choose JSONQL over JSON due to its extended capabilities of traversing and filtering JSON structures.

The table below gives an overview of some of the features found in the new JSONQL language in comparison to the existing ones in the JSON language.\
The examples are based on the restructured `data/northwind.json` source file.

| Feature	| JSONQL | JSON	 | Description
|:----------|:----------|:----------|:----------|
|Dot-separated path for simple object keys	| `Northwind.Customers.Address` | [the same] | The addresses of all customers |
|Path for complex object keys | `Northwind.Customers["Company Name"]` | [not possible] | The company names of all customers |
|Array index based selection | `Northwind.Customers[0].Orders[0]` | [the same] | The first order of the first customer |
|Object construction expression | `Northwind.Customers[0].Orders[OrderId, OrderDate]` | [not possible] | Select only `OrderId` and `OrderDate` from the orders of the first customer |
|Deep traversal	 | `..[OrderId, OrderDate, ShippedDate, Freight]` | [not possible] | Select the `OrderId`, `OrderDate`, `ShippedDate` and `Freight` from anywhere |
|Filtering with simple expression | `Northwind.Customers.*(City == "México D.F.")` | `Northwind.Customers(City == México D.F.)` | Customers from México D.F. |
|Filtering with complex expression | `Northwind.Customers.Orders.*(Freight > 200 && OrderDate *= "1997"`) | [not possible] | Orders for which the `Freight` is greater than 200 and `OrderDate` contains `1997` |
|Going up the JSON tree	 | `..OrderDate(@val == "1997-10-03")^{3}` or `..OrderDate(@val == "1997-10-03")^^^` | [not possible] | The parent customers(3 levels up) for which there is an order with `OrderDate` that equals `"1997-10-03"` |

### The JSONQL Query Executer

It is triggered when the jsonql language is specified:

- at report level:

```
<query language="jsonql"><![CDATA[ ..Orders(@size > 1).* ]] ></query>
```

- or in the data adapter:

```
<jsonDataAdapter class="net.sf.jasperreports.data.json.JsonDataAdapterImpl">
  ...
  <language>jsonql</language>
  ...
</jsonDataAdapter>
```

The [JsonQLQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQLQueryExecuter.html) uses the query string to produce a [JsonQLDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/data/JsonQLDataSource.html) instance, based on the same built-in parameters (or equivalent report properties) as for the existing [JsonDataSource](jsonqldatasource). This query executer is registered via [JsonQLQueryExecuterFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQLQueryExecuterFactory.html) factory class.

As in the case of the existing JSON query executer, in order to prepare the data source, the JSONQL query executer looks for the [JSON_INPUT_STREAM](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_INPUT_STREAM) parameter that contains the JSON source objects in the form of an `java.io.InputStream`. If no [JSON_INPUT_STREAM](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_INPUT_STREAM) parameter is provided, then the query executer looks for the alternate `net.sf.jasperreports.json.source` String parameter or report property that stores the path to the location of the JSON source file.

[JsonQLQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQLQueryExecuter.html) runs the query over the input source and stores the result in an in-memory [JsonQLDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/data/JsonQLDataSource.html) object.

During the [JsonQLDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/data/JsonQLDataSource.html) instantiation, the query executer also looks for the following additional parameters or report properties, containing the required localization settings:

- [`net.sf.jasperreports.json.date.pattern`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_DATE_PATTERN)
- [`net.sf.jasperreports.json.number.pattern`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_NUMBER_PATTERN)
- [`JSON_LOCALE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_LOCALE) (parameter only) of type `java.util.Locale`
- [`net.sf.jasperreports.json.locale.code`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_LOCALE_CODE) of type `java.lang.String`; this can be used if no `java.util.Locale` parameter is available
- [`JSON_TIME_ZONE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_TIME_ZONE) (parameter only) of type `java.util.TimeZone`
- [`net.sf.jasperreports.json.timezone.id`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/query/JsonQueryExecuterFactory.html#JSON_TIMEZONE_ID) of type `java.lang.String`; this can be used if no `java.util.TimeZone` parameter is available

In the next section you can see how these additional parameters are provided in the `/src/JsonQLDataSourceApp.java` class.

### The JSONQL Data Source Sample

In our example, the data is stored as a hierarchy of `Northwind.Customers.Orders` objects in the `data/northwind.json` file.

The JSONQL configuration is done in the `data/northwindDataAdapter.jrdax` data adapter file that is provided in the `reports/NorthwindOrdersReport.jrxml` via the report property:

```
<property name="net.sf.jasperreports.data.adapter" value="data/northwindDataAdapter.jrdax"/>
```

In the `NorthwindOrdersReport` we run a JSONQL query in order to retrieve orders from anywhere:

```
<query language="jsonql"><![CDATA[ ..Orders(@size > 1).* ]] ></query>
```

In reversed order of application, the query simply translates to: `"Select the children(.*) of Orders - with size greater than 1 - from anywhere"`

This query is possible since the Orders property of each customer is an array. Therefore we can apply an array specific filtering function to each value for the Orders property. In this case we are interested in the Orders that have more than one element.

The "get children" expression `.*` is necessary here because without it we would be looking at an array of arrays(an array with the arrays that the `Orders` properties point to) and not an array of objects that we are interested in.

The properties (fields) that we are interested in are:

- properties of the order itself: `OrderID, OrderDate` and `Freight`:

```
<field name="Id" class="java.lang.String">
  <property name="net.sf.jasperreports.jsonql.field.expression" value="OrderID"/>
</field>
<field name="OrderDate" class="java.util.Date">
  <property name="net.sf.jasperreports.jsonql.field.expression" value="OrderDate"/>
</field>
<field name="Freight" class="java.lang.Float">
  <property name="net.sf.jasperreports.jsonql.field.expression" value="Freight"/>
</field>
properties of the parent customer object: City, CustomerID and Company Name
that are obtained by going up 2 levels
<field name="ShipCity" class="java.lang.String">
  <description>Go up by two levels then select City</description>
  <property name="net.sf.jasperreports.jsonql.field.expression" value="^{2}.City"/>
</field>
<field name="CustomerID" class="java.lang.String">
  <description>Go up by two levels then select CustomerID</description>
  <property name="net.sf.jasperreports.jsonql.field.expression" value="^{2}.CustomerID"/>
</field>
<field name="CompanyName" class="java.lang.String">
  <description>Go up twice by 1 level then select CompanyName</description>
  <propertyExpression name="net.sf.jasperreports.jsonql.field.expression"><![CDATA["^^[\"Company Name\"]"]] ></propertyExpression>
</field>
```

The rest of the configuration necessary for this sample to work is done in the data adapter file: `data/northwindDataAdapter.jrdax`

```
<?xml version="1.0" encoding="UTF-8" ?>
<jsonDataAdapter class="net.sf.jasperreports.data.json.JsonDataAdapterImpl">
  <name>NorthWind JsonQL DA</name>
  <fileName>/data/northwind.json</fileName>
  <useConnection>true</useConnection>
  <language>jsonql</language>
  <datePattern>yyyy-MM-dd</datePattern>
  <numberPattern>#,##0.##</numberPattern>
</jsonDataAdapter>
```

Here we specify:

- the source file with the JSON data: `/data/northwind.json`
- the fact that we want to use the existing connection and the report's query by setting the `useConnection` flag to `true`
- the language of our query: `jsonql`; it is optional in this case since we are using the report's query that already specifies the language
- the date pattern used to parse the date fields
- the numeric pattern used to parse the numeric fields

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/jsonqldatasource` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/jsonqldatasource/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='jsonql'>The</a> JSONQL query language
<div align="right">Documented by <a href='mailto:narcism@users.sourceforge.net'>Narcis Marcu</a></div>

**Description / Goal**\
Detailed description of the JSONQL query language.

**Since:** 6.3.1

### Overview

JSONQL is a language used for querying JSON data structures. It appeared from the need to perform more complex JSON traversals and filtering. JSONQL is not compatible with the existing JSON language due to its possibly complicated syntax.

We typically write JSONQL queries in query expressions marked with the appropriate `jsonql` language:

```
<query language="jsonql"><![CDATA[ ..Orders(@size > 1).* ]] ></query>
```

### Syntax

A JSONQL query is an expression containing one or more chained members:

```
[member1][member2]...[memberN]
```

For example, in the following expression:

```
Northwind.Customers.Orders.*(Freight > 200 && OrderDate *= "1997")
```

we distinguish the following members:

- `[Northwind]`
- `[.Customers]`
- `[.Orders]`
- `[.*(Freight > 200 && OrderDate *= "1997")]`

Each member is applied on the result(s) produced by the previous member. If a member produces no result, the rest of the members, if any, are skipped and no results are returned.

When successful, the final result of the JSONQL expression is a collection of JSON nodes, where each node may be an `Array`, an `Object` or a `Value(boolean, string, number or null)`.

### Absolute expressions

Absolute expressions start with the `$` symbol placed in front of the first member expression:

```
$.Northwind.Customers[0]["Company Name"]
```

or

```
$["Northwind"].Customers[0]["Company Name"]
```

etc.

When used in the report's query, it makes no difference if an expression is absolute or not. The result is always calculated from the root of the JSON data.

The only place where the absolute expression matters is the field's property expression. For example:

```
<field name="FirstCustomerCompanyName" class="java.lang.String">
  <description><![CDATA[ The company name of the first customer]] ></description>
  <propertyExpression name="net.sf.jasperreports.jsonql.field.expression"><![CDATA[ "$.Northwind.Customers[0][\"Company Name\"]" ]] ></propertyExpression>
</field>
```

defines a field whose value is calculated directly from the root Northwind object and not relative to the field in the dataSet produced by the query.
`
You may want to use an absolute expression like this when the value you are interested in is not part of any of the results produced by the main dataSet's query.

### Comments inside expressions

Comments are supported inside JSONQL expressions. A JSONQL expression can be a single or multi-line expression, therefore we have single line comments and multi-line comments:

- Single line comments start with the `//` symbol:

```
Northwind
  .Customers
  .Orders
  .*
  //(Freight > 200 && OrderDate *= "1997")
```

Here we comment out the entire filtering expression.

- Multi-line comments start with `/*` and end with `*/` symbols:

```
Northwind
  .Customers
  /*.Orders
  .*
  (Freight > 200 && OrderDate *= "1997")*/
```

Here we comment out everything that follows the `Customers` member.

We can also use the multi-line comment inside a single line JSONQL expression:

```
Northwind.Customers.Orders.*(Freight > 200 /*&& OrderDate *= "1997"*/)
```

Here we comment out only a part of the filtering expression.

### JSONQL Members

A member is an expression that tells us how to navigate a single level (up or down or anywhere down) into the JSON tree. The generic member expression has this form:

```
[direction][key(s)][filterExpression]
```

The tables below presents all the supported member types within a JSONQL expression. Only the `[direction][key(s)]` part is shown here as the `[filterExpression]` is applicable to all member types and will be described separately.

<table class="doctable">
   <tr class="full_border">
      <th rowspan="2">Member type</th>
      <th rowspan="2">Key(s) value</th>
      <th colspan="2">Direction</th>
      <th rowspan="2">Example in context</th>
      <th rowspan="2">Description</th>
      <th rowspan="2">Expected result type</th>
      <th rowspan="2">Explanation</th>
   </tr>
   <tr class="full_border">
      <th>Symbol</th>
      <th>Description</th>
   </tr>
   <tr class="full_border grey_bg">
      <td rowspan="5">Simple Object Key</td>
      <td rowspan="5">Legal JavaScript Identifier<sup>(<a href="#jsid">1</a>)</sup></td>
      <td rowspan="3"><span class="large">.</span></td>
      <td rowspan="3">Down(implicit)</td>
      <td><b><span class="green">Northwind</span></b></td>
      <td>Go down on Norhtwind key</td>
      <td>Object</td>
      <td>The dot symbol is not necessary at the beginning of the expression. Direction is "Down" by default.
         The result is an Object because the JSON root is of type Object and the Northwind key also points to an Object.
      </td>
   </tr>
   <tr class="full_border grey_bg">
      <td>Northwind<b><span class="green">.Customers</span></b></td>
      <td>Go down on Customers key of the object produced by navigating down Northwind key</td>
      <td>Array of Objects</td>
      <td>The result is an Array of Objects because Northwind member produces an Object whose Customers key points to an Array.
         </td>
   </tr>
   <tr class="full_border grey_bg">
      <td>Northwind.Customers<b><span class="green">.Address</span></b></td>
      <td>Select the addresses of all customers</td>
      <td>Array of String values</td>
      <td>The result is an Array of Strings because the Customers key points to an array that is traversed, and each object
         is queried for the Address key, which in turn, points to a String.
         </td>
   </tr>
   <tr class="full_border grey_bg">
      <td rowspan="2"><span class="large">..</span></td>
      <td rowspan="2">Anywhere Down</td>
      <td>Northwind<b><span class="green">..Orders</span></b></td>
      <td>Select Orders from anywhere down starting from the Northwind root</td>
      <td>Array of Arrays</td>
      <td rowspan="2">The Anywhere Down selection usually builds a collection(array) of nodes. The type of the key dictates the
         type of the nodes contained. In this case each Orders key points to an array.
      </td>
   </tr>
   <tr class="full_border grey_bg">
      <td><b><span class="green">..Orders</span></b></td>
      <td>Select Orders from anywhere down starting from the root of the JSON source.</td>
      <td>Array of Arrays</td>
   </tr>
   <tr class="full_border">
      <td rowspan="3">Wildcard</td>
      <td rowspan="3"><span class="large">*</span></td>
      <td rowspan="2"><span class="large">.</span></td>
      <td rowspan="2">Children from the first level</td>
      <td>..Orders<b><span class="green">.*</span></b></td>
      <td>Select the children of all orders.</td>
      <td>Array of Objects</td>
      <td>Because the Orders keys produce Arrays, we need to select their first level of children in order to have an Array with all
         of them.
      </td>
   </tr>
   <tr class="full_border">
      <td>
         Consider the JSON structure:
         <pre>
{
"Products": {
"Product_1": {
"Id": 123,
"Category": "books"
},
"Product_2": {
"Id": 234,
"Category": "dvds"
}
}
}
</pre>
         and the expression: Products<b><span class="green">.*</span></b>
         </td>
      <td>Select the children of Products.</td>
      <td>Array of Objects
         <pre>
[{
"Id": 123,
"Category": "books"
}, {
"Id": 234,
"Category": "dvds"
}]
</pre>
         </td>
      <td>The first level children of the Products Object are the values for its keys.</td>
   </tr>
   <tr class="full_border">
      <td><span class="large">..</span></td>
      <td>All children from all levels</td>
      <td>Northwind.Customers[0]<b><span class="green">..*</span></b></td>
      <td>Select all children of the first customer.</td>
      <td>Array of Nodes(Array(s), Objects, Values)</td>
      <td>This kind of query:
            <ul>
                  <li>traverses arrays adding each child to the result</li>
                  <li>goes down on each object key adding it to the result</li>
                  <li>stops when the end of the JSON tree is reached</li>
                  <li>can produce huge amounts of result Nodes if no filter expression is applied</li>
               </ul>
         </td>
   </tr>
</table>

<table class="doctable">
   <tr class="full_border">
      <th rowspan="2">Member type</th>
      <th rowspan="2">Key(s) value</th>
      <th colspan="2">Direction</th>
      <th rowspan="2">Example in context</th>
      <th rowspan="2">Description</th>
      <th rowspan="2">Expected result type</th>
      <th rowspan="2">Explanation</th>
   </tr>
   <tr class="full_border">
      <th>Symbol</th>
      <th>Description</th>
   </tr>
   <tr class="full_border grey_bg">
      <td rowspan="2">Complex Object Key</td>
      <td rowspan="2">String Literal<sup>(<a href="#stringlit">2</a>)</sup>
         <br>
         enclosed in square brackets
      </td>
      <td>[none]</td>
      <td>Down(implicit)</td>
      <td>Northwind.Customers<b><span class="green">["Company Name"]</span></b></td>
      <td>Select the company name of all customers</td>
      <td>Array of Strings</td>
      <td>Direction is "Down" by default. The result is an Array because the Customers array is queried on each of
         its items for the "Customer Name" key/property.
      </td>
   </tr>
   <tr class="full_border grey_bg">
      <td><span class="large">..</span></td>
      <td>Anywhere Down</td>
      <td><b><span class="green">..["Company Name"]</span></b></td>
      <td>Select the company name from anywhere</td>
      <td>Array of Strings</td>
      <td>The result is an Array because applying Anywhere Down traversal usually produces a collection of Nodes
         whose type is dictated by the value found at the requested key.
         <br>
         "Company Name" always produces a String in this case.
         </td>
   </tr>
   <tr class="full_border">
      <td rowspan="2">Object Construction</td>
      <td class="maxWidth170" rowspan="2"> At least 2 - legal JavaScript identifiers or String literals - separated by comma and enclosed in square brackets</td>
      <td>[none]</td>
      <td>Down(implicit)</td>
      <td>Northwind.Customers[0].Orders<b><span class="green">[OrderID, OrderDate]</span></b></td>
      <td>Select the OrderID and OrderDate from the orders of the first customer</td>
      <td>Array of Objects
         <pre>
[{
"OrderID": 10643,
"OrderDate": "1997-08-25"
},
...
{
"OrderID": 11011,
"OrderDate": "1998-04-09"
}]
</pre>
         </td>
      <td>Direction is "Down" by default. The result is an Array because the Orders array is queried on each of
         its items for the OrderID and OrderDate keys.
      </td>
   </tr>
   <tr class="full_border">
      <td><span class="large">..</span></td>
      <td>Anywhere Down</td>
      <td><b><span class="green">..["Company Name", Address]</span></b></td>
      <td>Select the company name and address from anywhere</td>
      <td>Array of Objects
         <pre>
[{
"Company Name": "Alfreds Futterkiste",
"Address": "Obere Str. 57"
},
...
{
"Company Name": "Wolski  Zajazd",
"Address": "ul. Filtrowa 68"
}]
</pre>
         </td>
      <td>The result is an Array of Objects because applying Anywhere Down traversal usually produces a collection of Nodes
         whose type is Object in this case.
         <br>
         We are constructing objects with "Company Name" and Address keys.
         </td>
   </tr>
</table>
<table class="doctable">
   <tr class="full_border">
      <th rowspan="2">Member type</th>
      <th rowspan="2">Key(s) value</th>
      <th colspan="2">Direction</th>
      <th rowspan="2">Example in context</th>
      <th rowspan="2">Description</th>
      <th rowspan="2">Expected result type</th>
      <th rowspan="2">Explanation</th>
   </tr>
   <tr class="full_border">
      <th>Symbol</th>
      <th>Description</th>
   </tr>
   <tr class="full_border grey_bg">
      <td rowspan="2">Array Index</td>
      <td class="maxWidth170" rowspan="2">[Integer]</td>
      <td>[none]</td>
      <td>Down(implicit)</td>
      <td>Northwind.Customers<b><span class="green">[2]</span></b></td>
      <td>Select the third customer</td>
      <td>Object</td>
      <td>JavaScript array indexes are 0-based. Direction is "Down" by default. The result is an Object because
         the 3<sup>rd</sup> element in the Customers array is an object.
         <br>
         Indexes outside the valid range(from 0 to array size - 1) produce no results.
      </td>
   </tr>
   <tr class="full_border grey_bg">
      <td><span class="large">..</span></td>
      <td>Anywhere Down</td>
      <td>Northwind.Customers.Orders<b><span class="green">..[0]</span></b></td>
      <td>Select the first order of each customer's Orders</td>
      <td>Array of Objects</td>
      <td>The result is an Array of Objects because applying Anywhere Down traversal usually produces a collection of Nodes
         whose type is Object in this case.
         <br>
         We are selecting the first order(which is an object) from each Orders array.
         </td>
   </tr>
   <tr class="full_border">
      <td rowspan="2">Array Construction</td>
      <td class="maxWidth170" rowspan="2">[Integer1, Integer2, ...]</td>
      <td>[none]</td>
      <td>Down(implicit)</td>
      <td>Northwind.Customers[0].Orders<b><span class="green">[0,2]</span></b></td>
      <td>Select the first and third orders of the first customer</td>
      <td>Array of Objects</td>
      <td>JavaScript array indexes are 0-based. Direction is "Down" by default. The result is an Array of Objects because
         we are constructing an array with only the 1<sup>st</sup> and 3<sup>rd</sup> orders.
         <br>
         Indexes outside the valid range(from 0 to array size - 1) are ignored.
      </td>
   </tr>
   <tr class="full_border">
      <td><span class="large">..</span></td>
      <td>Anywhere Down</td>
      <td>Northwind.Customers.Orders<b><span class="green">..[0,1,3]</span></b></td>
      <td>Select the first, second and fourth orders of each customer's Orders</td>
      <td>Array of Objects</td>
      <td>The result is an Array of Objects because we are constructing an array with only the
         1<sup>st</sup>, 2<sup>nd</sup> and 4<sup>th</sup> orders.
         <br>
         Indexes outside the valid range(from 0 to array size - 1) are ignored.
         </td>
   </tr>
</table>
<table class="doctable">
   <tr class="full_border">
      <th rowspan="2">Member type</th>
      <th rowspan="2">Key(s) value</th>
      <th colspan="2">Direction</th>
      <th rowspan="2">Example in context</th>
      <th rowspan="2">Description</th>
      <th rowspan="2">Expected result type</th>
      <th rowspan="2">Explanation</th>
   </tr>
   <tr class="full_border">
      <th>Symbol</th>
      <th>Description</th>
   </tr>
   <tr class="full_border grey_bg">
      <td rowspan="2">Array Slice</td>
      <td class="maxWidth170" rowspan="2">[Integer startIndex : Integer endIndex]</td>
      <td>[none]</td>
      <td>Down(implicit)</td>
      <td>..Orders.*<b><span class="green">[-1:]</span></b></td>
      <td>Select the last of all orders.</td>
      <td>Object</td>
      <td>
         Direction is "Down" by default.
         <br>
         <br>
         Each of the start/end indexes is 0-based and optional, but not both at the same time.
         <br>
         When startIndex is missing it defaults to 0.
         <br>
         When endIndex is missing it defaults to the array size.
         <br>
         <br>
         The element at <b>endIndex</b> is not included in the results list.
         <br>
         The indexes can have negative values that suggest operating at the end of the array.
         <br>
         <b>-1</b> points to the last item. <b>-2</b> points to the item before last and so on.
         <br>
         The indexes outside the array index range are ignored.
         <br>
         <br>
         The result is Object because the last order is an object.
         </td>
   </tr>
   <tr class="full_border grey_bg">
      <td><span class="large">..</span></td>
      <td>Anywhere Down</td>
      <td>Northwind.Customers.Orders<b><span class="green">..[0:2]</span></b></td>
      <td>Select the first two orders(indexes 0 and 1) of each customer's Orders</td>
      <td>Array of Arrays</td>
      <td>The result is an Array of Arrays because applying Anywhere Down traversal usually produces a collection of Nodes.
         Each node is an array in this case because from each Orders array we select only the first two items.
         <br>
         If we want to reach the deeper order nodes, the .* member(first level children) could be added to the expression, thus obtaining
         an Array of Objects.
         </td>
   </tr>
   <tr class="full_border">
      <td rowspan="3">Multi Level Up</td>
      <td rowspan="3">[no key]</td>
      <td>^</td>
      <td>One level up</td>
      <td>..OrderID<b><span class="green">^</span></b></td>
      <td>Select the parents of all objects that have the OrderID property = Select all orders</td>
      <td>Array of Objects</td>
      <td>The result is an Array of Objects because the parent of each OrderID key is the object that contains the key.</td>
   </tr>
   <tr class="full_border">
      <td rowspan="2"> ^{N} </td>
      <td rowspan="2">Up by N levels; N &gt;= 1</td>
      <td>..[OrderID,OrderDate](<b><span class="green">^{3}</span></b>.City == "Berlin")
      </td>
      <td rowspan="2">Select the OrderID and OrderDate keys for objects(orders) that have the parent customer's(which is 3 levels up) city Berlin.</td>
      <td rowspan="2">Array of Objects
         <pre>
[{
"OrderID": 10643,
"OrderDate": "1997-08-25"
},
...
{
"OrderID": 11011,
"OrderDate": "1998-04-09"
}]
</pre>
         </td>
      <td rowspan="2">The query examples are equivalent: <b><span class="green">^^^</span></b> produces the same result as
         <b><span class="green">^{3}</span></b>.
         <br>
         <br>
         The result is an Array of Objects because we are constructing objects with the OrderID and OrderDate keys.
         <br>
         <br>
         The filter expression travels up to the Customer parent to reach the City key.
         <br>
         The immediate parent of an object with the [OrderID, OrderDate] keys is the object that contains these keys - an order.
         <br>
         The parent of an order is the Orders array that contains the order.
         <br>
         The parent of the Orders array is the object holding the Orders key - the customer object. From here we go down the City key
         to perform the filtering.
         </td>
   </tr>
   <tr class="full_border">
      <td>..[OrderID,OrderDate](<b><span class="green">^^^</span></b>.City == "Berlin")
      </td>
   </tr>
</table>

<sup>(1)</sup> A Legal JavaScript Identifier is a character sequence that:

- must start with one of: letter(lowercase or uppercase), `$` or `_`
- and can be followed by any combination of letter, `$`, `_` or digit(`0-9`).

<sup>(2)</sup> A String Literal is an escaped and double quoted JavaScript String.

### The Filter Expression

Each member expression described above can be accompanied by a filter expression.\
Let's consider the following JSONQL expression:

```
 Northwind.Customers.Orders.*(Freight > 200 && OrderDate *= "1997")
```

It contains the following members:

- `[Northwind]`
- `[.Customers]`
- `[.Orders]`
- `[.*(Freight > 200 && OrderDate *= "1997")]`

Members `#1`, `#2` and `#3` contain no filter expression, only `#4` does:

```
 (Freight > 200 && OrderDate *= "1997")
```

In this case we have a compound filter expression made out of two basic filter expressions joined by the logical and(`&&`) operator.

The filter expression is evaluated as member results are added to the whole expression's result.
The result of a filter expression is a boolean true or false.

### The Basic Filter Expression

The form of a basic filter expression is one of:

`( [filterMember1][filterMember2]...[filterMemberN]@Function`<sup>(*)</sup> `Operator Value )`\
`( [filterMember1][filterMember2]...[filterMemberN]@Function)`

<sup>(*)</sup> The `@Function` element is optional

The `filterMember` is similar to the JSONQL `Member` except it lacks the filter expression. It contains only the: `[direction][key(s)]` part.

This means that the above member type definitions also apply in this case.

The type and presence of the `@Function` element dictates the form of the basic filter expression as follows:

<table class="doctable">
   <tr class="full_border">
      <th>Function type</th>
      <th>Function expression</th>
      <th>Example</th>
      <th>Description</th>
      <th>Observation</th>
   </tr>
   <tr class="full_border grey_bg">
      <td rowspan="5">Path Type Check functions</td>
      <td>@isNull</td>
      <td class="minWidth300">..Orders.*(["nonexistent key"]@isNull)</td>
      <td>Select Orders keys from anywhere and get the children that have a "nonexistent key" property with null value.
         The result of the filter expression is true for all orders, so we are getting all of them.
      </td>
      <td class="maxWidth170" rowspan="5">The path type check functions are logical functions that return true/false. No Operator/Value can be used here.</td>
   </tr>
   <tr class="full_border grey_bg">
      <td>@isNotNull</td>
      <td>..Orders.*(Products@isNotNull)</td>
      <td>Select Orders keys from anywhere and get the children that have a non-null Products property.
         The result of the filter expression is false for all orders, so we are getting nothing as result.
      </td>
   </tr>
   <tr class="full_border grey_bg">
      <td>@isArray</td>
      <td>..Orders(@isArray).*</td>
      <td>Select Orders keys from anywhere that are arrays and get the first level children.
         The result of the filter expression is true for all Orders keys, so we are getting all orders.
      </td>
   </tr>
   <tr class="full_border grey_bg">
      <td>@isObject</td>
      <td>..Orders(@isObject)</td>
      <td>Select Orders keys from anywhere that are of type Object.
         The result of the filter expression is false for all Orders keys because each points to an Array, so we are getting no results.
      </td>
   </tr>
   <tr class="full_border grey_bg">
      <td>@isValue</td>
      <td>..*(@isValue)</td>
      <td>Select everything that is a value: string, number, boolean, null. The result is a huge collection of strings and numbers that
         probably needs additional filtering.
      </td>
   </tr>
   <tr class="full_border">
      <td>Value check function</td>
      <td>@val</td>
      <td>..OrderID(@val &gt; 10600)</td>
      <td>Select the OrderID keys from anywhere that have a value greater than 10600.
         <br>
         <br>
         This function can be used only at the beginning of the filter expression when we want to filter the resulting nodes directly.
         It is applicable only to value nodes.
         <br>
         If we want to check the value of a path we do not need to use this function as the function operators apply to values only.
      </td>
      <td></td>
   </tr>
   <tr class="full_border grey_bg">
      <td rowspan="2">Array size check function</td>
      <td rowspan="2">@size</td>
      <td>Northwind.Customers.*(Orders@size &gt; 10).Orders.*</td>
      <td rowspan="2">Select the orders that come from Orders arrays with more than 10 items. Both query expressions produce the same output.
      </td>
      <td rowspan="2"></td>
   </tr>
   <tr class="full_border grey_bg">
      <td>..Orders(@size &gt; 10).*</td>
   </tr>
   <tr class="full_border">
      <td>[none]</td>
      <td>[none]</td>
      <td>..Orders.*(OrderID &gt; 10600)</td>
      <td>Select the orders for which the OrderID has a value greater than 10600.
         <br>
         <br>
         We don't need functions to check the value of a key.
      </td>
      <td></td>
   </tr>
</table>

### Operator to Operand type application

There is an enforcement at the JSONQL language level that requires the operators to be applied to the right kind of operands. The query expression is not valid unless the matching is correctly done.

| Operator(s) | Description | Operand type | JSONQL Expression Example | Observations |
|:----------|:----------|:----------|:----------|:----------|
| `==, !=` | Equal to,<br/>Not equal to | Number,<br/>String,<br/>Boolean,<br/>null | `..Orders.*(OrderID == 10643)` | |
| `>, >=, <, <=` | Greater than,<br/>Greater than or equal to,<br/>Lower than,<br/>Lower than or equal to | Number | `..Orders.*(Freight >= 60)` | |
| `*=` | Contains | String | `..Orders.*(OrderDate *= "1997")` | String values are String literals<br/>(escaped and double quoted).|

### Logical Operators for Basic Filter Expressions

Basic Filter Expressions can be negated and/or combined in order to allow more complex filtering capabilities.

| Operator | Description | JSONQL Expression Example |
|:----------|:----------|:----------|
| `&&` | `AND` | `..Orders.*(OrderID >= 10600 && OrderDate *= "1997")` |
| `\|` | `OR` | `..Orders.*(Freight > 120 \|\| ShipVia == 2)` |
| `!` | `NOT` | `..Orders(!@size > 10).*` |
| mixed | Grouping expressions is possible | `..Orders.*(!(OrderID >= 10600 && (OrderDate *= "1998" \|\| OrderDate *= "1997")))` |

