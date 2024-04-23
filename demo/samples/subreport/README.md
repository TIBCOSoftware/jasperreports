
# JasperReports - Subreport Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how subreport could be used to create complex document layouts.

### Main Features in This Sample

[Subreports](#subreports)\
[Exporting to JSON Format Using Report Metadata](#jsonmetadataexport)

## <a name='subreports'>Subreports</a>
<div align="right">Documented by <a href='mailto:lshannon@users.sourceforge.net'>Luke Shannon</a></div>

**Description / Goal**\
How to use the built-in subreport element to create nested content.

**Since:** 0.3.0

**Other Samples**\
[/demo/samples/xmldatasource](../xmldatasource/README.md)

### What is a Subreport

A Subreport is a JasperReports Template thats embedded within another JasperReports template (which we will refer to as the Master report).\
As the Master report executes, each time the Subreport element is reached it is executed and its content seamlessly embedded into the output of the Master report.\
The end result is a single output containing the blended contents of the Master report and each subreport execution.

Subreports can be nested.

### When to Use Subreports

There are several situations when you wish to embed one report into other. Examples of such situations are:

- Isolating a common layout that is used in a series of reports
- Processing records from a query different from the Master report's query

This current example demonstrates the latter case.

### Configuring a Subreport

The first thing to note is any JasperReport can be used as a Subreport. However, once a report has embedded into another as a Subreport, the Master report is now responsible for:

- Supplying the Subreport with a `JRDataSource`
- Specifying an expression to locate the report design
- Passing Parameters into the Subreport

The Subreport can return data to the main report using variables.

In this example the Master Report contains the `Address` and `Product` reports embedded as Subreport elements.

Lets begin by review the configuration of the Subreport element for the `Products` Subreport.

```
<element kind="subreport" x="5" y="25" width="325" height="20" backcolor="#FFCC99" printRepeatedValues="false" removeLineWhenBlank="true">
  <connectionExpression><![CDATA[$P{REPORT_CONNECTION}]] ></connectionExpression>
  <expression><![CDATA[$P{ProductsSubreport}]] ></expression>
  <returnValue toVariable="ProductTotalPrice" calculation="Sum" subreportVariable="PriceSum"/>
  <parameter name="City">
    <expression><![CDATA[$F{City}]] ></expression>
  </parameter>
</element>
```

This element is in the Detail band of the Master report. This means this Subreport will be executed with each record in the Master report's Result Set.

### The `<parameter/>` Tag

This tag indicates a Parameter in the Product report is being filled by the Master report. In this case, its the `City` field in the Master report filling the `City` Parameter of the `Product` report.\
If we look at the query of the Product report we see that the value of the `City` parameter is injected into the Query to constrain the Results:

```
SELECT Product.ID AS ID, Product.Name AS Name,
Positions.Quantity AS Quantity, Positions.Price AS Price
FROM Positions, Product, Document, Address
WHERE Positions.DocumentID = Document.ID AND
Document.AddressID = Address.ID AND
Positions.ProductID = Product.ID AND
Address.City = $P{City}
ORDER BY Product.ID
```

What this means is the `Product` report executes for each row in the Master report's ResultSet and displays results related to the `city` field in that row.\
(Remember: Fields map to the data source, in this case to columns returned in the Result Set).

For more information on modifying report queries please view the [Query](../query/README.md) Sample.

### The `<connectionExpression/>` tag

This tag specifies the JRDataSource to be used to fill the subreport. In this case the built in parameter `$P{REPORT_CONNECTION}` is used.\
This parameter contains a reference to JDBC Connection that was used to fill the Master Report. This is best practice when working with Subreports which need to be filled with the same JDBC Connection as the Master report.

In situations where your Subreport doesn't use a data source (the report may just contain some text and/or images) a reference to the `JREmptyDataSource` can be passed in here:

```
<dataSourceExpression><![ CDATA[new net.sf.jasperreports.engine.JREmptyDataSource()]] ></dataSourceExpression>
```

### The `<returnValue/>` tag

This tag indicates the value passed from the subreport to the Master report. The calulcation indicates if the value should be accumlated or just passed directly up.\
In this case the calculation is Sum, meaning the `PriceSum` variable in the Subreport is going to be accumlated for each Subreport execution and stored in the `ProductTotalPrice` variable of the master report. If the desired effect is to just pass a variable from the Subreport to the Master, then a calculation type of `None` should be used.

### The `<expression /> tag

This tag indicates where the design of the Subreport can be located. Note the class. In this example the expression is returning a `JasperReport` object.\
The subreport expression represents a very powerful place for extensions/integratoins. Ternary operators can be used here to load different Subreports based on different conditions in the report. Also external Java classes can be called in this expression, provided they return a `JasperReport` reference, the subreport design can be obtained or created using Java. In the `SubreportApp.java` the `JasperReport` reference is obtained by loading a `.jasper` (serialized `JasperReport` object) file from the file system.

```
JasperReport subreport = (JasperReport)JRLoader.loadObjectFromFile("build/reports/ProductReport.jasper");
```

Lets review the `Address` Report configuration.

```
<element kind="subreport" positionType="Float" x="335" y="25" width="175" height="20" backcolor="#99CCFF" removeLineWhenBlank="true">
  <connectionExpression><![CDATA[$P{REPORT_CONNECTION}]] ></connectionExpression>
  <expression><![CDATA["AddressReport.jasper"]] ></expression>
  <returnValue toVariable="CityAddressCount" subreportVariable="REPORT_COUNT"/>
  <parameter name="City">
    <expression><![CDATA[$F{City}]] ></expression>
  </parameter>
</element>
```

The main differences are here:

- `returnValue`: No calculation is set. The default is `None`, which means the variable `REPORT_COUNT` variable in the subreport will be passed directly to the Master.
- `expression`: In this case the expression class is a String and the expression is the location on the file system for the compiled version of the JRXML.

### Is there performance concerns with Subreports?

The answer to this depends on your system, data source and your report design. A few points to note on Subreports:

- Each subreport execution may spawn a new thread (see below).
- As the subreport executes more objects will be created in Heap Memory.

On the subject of threads. Support for Java continuations has been added as an alternative to threads. This was done using the Jakarta Commons Javaflow library.\
The JasperReports property: `net.sf.jasperreports.subreport.runner.factory` can be used with the following two settings:

- `net.sf.jasperreports.engine.fill.JRContinuationSubreportRunnerFactory`
- `net.sf.jasperreports.engine.fill.JRThreadSubreportRunnerFactory`

By default `net.sf.jasperreports.engine.fill.JRThreadSubreportRunnerFactory` is used, however if `net.sf.jasperreports.engine.fill.JRContinuationSubreportRunnerFactory` is set, then a Javaflow approach will be used to fill the reports rather than threads. If this option is chosen, then the Jakarta Commons Javaflow jar must be included in the application classpath. Other alternatives for processing different queries in the same report are usage of the `List` element and Sub Datasets.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='jsonmetadataexport'>Exporting</a> to JSON Format Using Report Metadata
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to export the report to JSON format using metadata associated with report elements at design time, to mark JSON tree nodes.

**Since:** 6.0.0

**Other Samples**\
[/demo/samples/jasper](../jasper/README.md)

### Pixel-Perfect Requirement and JSON Output Format

[JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects are ready to be printed out in a pixel-perfect representation. This means that they are very appropriate for visual representations of data, since objects' dimensions, their absolute and relative positions within the document are completely preserved. The pixel-perfect representation suits very well the read-only or layout-oriented output formats, such as PDF or Graphics2D, but it may generate some inconvenients for data-oriented output formats like CSV, Excel or JSON. In this case more important than the pixel-perfectness of the document is to allow the generated data in the report to be properly stored, transmitted or manipulated by specific engines. To successfully handle a set of data ideally is to know:

- how to navigate through the data structure in order to retrieve the data;
- the field name the data is associated with;
- the correct data type (text, numeric, date, etc);
- the data value

Document pagination and visual layout are not important in this case:

- no page margins and other padding settings;
- no page numbers;
- no repetition of page headers;
- etc.

The JSON output format organizes data hierachically in a tree structure, written in accordance with the JSON syntax:

- arrays are enclosed in square brackets
- objects in arrays are comma-separated
- objects are enclosed in curly braces
- object members come in name:value pairs, separated by commas

### The JSON Metadata Exporter

Extracting relevant data from a pixel-perfect [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object, in order to generate a data-oriented output, may be done by customizing the export process. Some properties are needed to tell the JR engine which elements should be exported and where should they be placed within the JSON tree object. The [JsonMetadataExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/export/JsonMetadataExporter.html) is designed to handle such kind of metadata information and to generate consistent data structures.

Before starting its export work, the JSON metadata exporter looks for the presence of an existing JSON schema by calling the [`getJsonSchemaResource()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/json/export/JsonMetadataReportConfiguration.html#getJsonSchemaResource()) exporter configuration setting.\
This can be configured either directly, using the APIs, or by reading the value from the [`net.sf.jasperreports.export.json.schema`](https://jasperreports.sourceforge.net/config.reference.html#net.sf.jasperreports.export.json.schema) report property.\
If present, this setting provides the path to a JSON schema file that will be used to generate the JSON structure at export time. Nodes in a given schema may provide:

- the `_type` property (mandatory), which can be an object or an array.
- the `_children` property (mandatory for array-type nodes), which defines the type of the objects in the array.
- other properties that describe the object.

An example of JSON schema can be found in `MasterReport.schema.json` file in the [subreport](../subreport/README.md) sample directory:

```
{
  _type: 'array',
  _children: {
    _type: 'object',
    City: 'value',
    products: {
      _type: 'array',
      _children: {
        _type: 'object',
        Id: 'value',
        Name: 'value',
        Quantity: 'value',
        Price: 'value'
      }
    },
    customers: {
      _type: 'array',
      _children: {
        _type: 'object',
        Name: 'value',
        Street: 'value'
      }
    }  
  }
}
```

The above schema instructs the engine to export data into an array of objects, each of them containing a `City`, an array of products and an array of customers. A product is defined by its `Id, Name, Quantity` and `Price` properties, while a customer is characterized by a `Name` and a `Street`

If no schema is provided at export time, then the output structure will be deduced from the elements marked for JSON export, in accordance with `net.sf.jasperreports.export.json.path` and/or `net.sf.jasperreports.export.json.{type}.{arbitrary_path}` metadata properties.

Following are the metadata properties defined at element level, that are taken into account by the JSON metadata exporter:

- `net.sf.jasperreports.export.json.path` - this property uses the period notation to specify a given path within the JSON tree, where the current element will be exported. If the `net.sf.jasperreports.export.json.schema` property is not present in the report, this property can be used to determine a rule in the JSON schema as well. If the JSON schema is provided, then the path specified by `net.sf.jasperreports.export.json.path` must follow the rules in that schema. The current element will be placed in a node according to this path.
- `net.sf.jasperreports.export.json.{type}.{path}` - properties with this prefix specify both the type and a given path within the JSON tree, where the current element will be exported. The exported value is the value of the property. If the `net.sf.jasperreports.export.json.schema` property is not present in the report, these properties can be used to determine rules in the JSON schema as well. If the JSON schema is provided, then the path specified by the `{path}` suffix must follow a rule in that schema. The current exported value will be placed in a node according to this path.\
When working with such properties, numbers have to be written with no thousand separators and using a period as decimal. For instance, `1,000,000` should be written as `"1000000"` and `2,000.45` should be written as `"2000.45"`. Dates must be written according to ISO 8601 standard: `"yyyy-MM-dd'T'HH:mm:ss"`.
- `net.sf.jasperreports.export.json.repeat.value` - an element-level flag property indicating whether the value associated to that exported element should be repeated or not when it is missing.
- `net.sf.jasperreports.export.json.repeat.{path}` - an element-level flag property indicating whether the value associated to that JSON path should be repeated or not when it is missing. Usually this property works in conjunction with one of the `net.sf.jasperreports.export.json.{type}.{path}` properties.
- `net.sf.jasperreports.export.json.data` - element-level property containing the exported value for that element. By default is considered the value of the report element itself.\
When working with this property, numbers have to be written with no thousand separators and using a period as decimal. For instance, `1,000,000` should be written as `"1000000"` and `2,000.45` should be written as `"2000.45"`. Dates must be written according to ISO 8601 standard: `"yyyy-MM-dd'T'HH:mm:ss"`.

To control the appearance of the name of the member properties in an object, the metadata exporter looks for a report-level property called `net.sf.jasperreports.export.json.escape.members`. If this flag is set to `true` (as it is, by default), then member names will be surrounded by double quotes.

In this sample the JSON schema is referenced in `MasterReport.jrxml`. For each `City` given in the master report, a list of products is retrieved based on the `ProductReport.jrxml` subreport, and a list of customers is collected based on the `AddressRport.jrxml` subreport. Below are some coding fragments that show the use of properties discussed above:
In `MasterReport.jrxml`:

```
<jasperReport ...>
  <property name="net.sf.jasperreports.export.json.schema" value="reports/MasterReport.schema.json"/>
  ...
  <detail>
    <band height="50">
      <element kind="textField" x="5" y="5" width="100" height="15" printWhenDetailOverflows="true" style="Sans_Bold">
        <expression><![CDATA[$F{City}]] ></expression>
        <property name="net.sf.jasperreports.export.json.path" value="City"/>
      </element>
      ...
    </band>
  </detail>
  ...
</jasperReport>
```

In ProductReport.jrxml:

```
<jasperReport ...>
  ...
  <group name="ProductGroup">
    <expression><![CDATA[$F{Id}]] ></expression>
    <groupHeader>
      <band height="14">
        <element kind="textField" y="2" width="15" height="10" hTextAlign="Right">
          <expression><![CDATA[$F{Id}]] ></expression>
          <property name="net.sf.jasperreports.export.json.path" value="products.Id"/>
        </element>
        <element kind="textField" positionType="Float" x="20" y="2" width="80" height="10" textAdjust="StretchHeight">
          <expression><![CDATA[$F{Name}]] ></expression>
          <property name="net.sf.jasperreports.export.json.path" value="products.Name"/>
        </element>
        <element kind="textField" ... hTextAlign="Right" textAdjust="StretchHeight" evaluationTime="Group" pattern="#0" evaluationGroup="ProductGroup">
          <expression><![CDATA[$V{QuantityProductSum}]] ></expression>
          <property name="net.sf.jasperreports.export.json.path" value="products.Quantity"/>
        </element>
        <element kind="textField" ... hTextAlign="Right" textAdjust="StretchHeight" evaluationTime="Group" pattern="#0.00" evaluationGroup="ProductGroup">
          <expression><![CDATA[$V{PriceProductSum}]] ></expression>
          <property name="net.sf.jasperreports.export.json.path" value="products.Price"/>
        </element>
      </band>
    </groupHeader>
    <groupFooter>
      <band/>
    </groupFooter>
  </group>
  ...
</jasperReport>
```

In AddressReport.jrxml:

```
<jasperReport ...>
  ...
  <detail>
    <band height="14">
      ...
      <element kind="textField" positionType="Float" x="20" y="2" width="80" height="10" textAdjust="StretchHeight">
        <expression><![CDATA[$F{FirstName} + " " + $F{LastName}]] ></expression>
        <property name="net.sf.jasperreports.export.json.path" value="customers.Name"/>
      </element>
      <element kind="textField" positionType="Float" x="105" y="2" width="70" height="10" textAdjust="StretchHeight">
        <expression><![CDATA[$F{Street}]] ></expression>
        <property name="net.sf.jasperreports.export.json.path" value="customers.Street"/>
      </element>
    </band>
  </detail>
  ...
</jasperReport>
```

After running the

```
>mvn clean compile exec:exec@all
```

command, the pure data exported with the JSON metadata exporter will be available in the `demo/samples/subreport/target/reports` directory as `MasterReport.json`.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/subreport ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/subreport/target/reports` directory.
