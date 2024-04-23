
# JasperReports - Jasper Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how different features of the library could be used to generate a complex document.

### Main Features in This Sample

[Data Grouping](#groups)\
[Exporting to CSV Format Using Report Metadata](#csvmetadataexport)\
[Exporting to XLSX Format Using Report Metadata](#xlsxmetadataexport)\
[Exporting to XLS Format Using Report Metadata](#xlsmetadataexport)\
[Conditional Styles in Reports](#conditionalStylesInReports)\
[Updating current date and slide numbers in PPTX export](#pptxfields)

## <a name='groups'>Data</a> Grouping
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to group data at different levels and perform aggregate calculations using report groups.

**Since:** 0.1.0

**Other Samples**\
[/demo/samples/datasource](../datasource/README.md)

### Grouping Data in JasperReports

A report's content can be better organized if the report data are grouped by meaningful criteria. For instance, a shipping report becomes more relevant when its data are grouped by product type, or by customer's country, etc.\
And as a bonus, grouped data allow statistic calculations to be performed and additional statistic information to be added to the report at runtime.

As seen above, there are multiple reasons of grouping and aggregating data before they are represented. In JasperReports there are two ways to accomplish this:

- If a report query is declared, it could be customized to retrieve already aggregated data from the data source (A SQL query could use the GROUP BY clause and statistical functions, for example). The main advantage is that the JR engine needs no more to prepare data before representing it, and the report is running faster in this case. The main disadvantage is that one cannot use the group imbrication, along with group header and footer facilities, so the data presentation can be realized in simplified layouts only.
- If a rich presentation context of data grouping is required, one could use the report `<group/>` elements. Groups represent a flexible way to organize data on a report. In this case, the engine iterates through records in the data source and retrieves only those according to the grouping criteria.

A report group has three components:

- Group expression - contains the data grouping criteria. When the value of the group expression changes during the iteration through the data source at report-filling time, a group rupture occurs and the corresponding `<groupFooter>` and `<groupHeader>` sections are inserted into the resulting document.
- Group header section - a multiband section containing header additional information.
- Group footer section - a multiband section containing footer additional information.

**Note:**

- The order of groups declared in a report template is important because groups contain each other. One group contains the following group, and so on. When a larger group encounters a rupture, all subsequent groups are reinitialized.
- You can declare as many groups as you want in a report.
- In order to get an accurate data representation, the data in the data source should be already ordered according to the group expressions used in the report. One can either perform data sorting through the report query, or use the `<sortField/>` element.

### Group Attributes

Any group in a report can be customized through its attributes:

- `name` - Acts as a group identifier. This attribute is mandatory, and should be unique per report.
- `startNewColumn` - Instructs the engine to start a new column when a new group starts. The default value is `false`.
- `startNewPage` - Instructs the engine to start a new page when a new group starts. The default value is `false`.
- `resetPageNumber` - If true, when a new group starts, the built-in PAGE_NUMBER variable will be reset. The default value is `false`.
- `reprintHeaderOnEachPage` - Instructs the engine to print the group header on each page containing the group data. The default value is `false`.
- `minHeightToStartNewPage` - Represents the minimum amount of vertical space at the bottom of the page or column, measured in pixels, that prevents the group from starting a new page of its own.
- `footerPosition` - Specifies the rendering position of the group footer on the page. Possible values are: 
    - `Normal` (default)
    - `StackAtBottom`
    - `ForceAtBottom`
    - `CollateAtBottom`
- `keepTogether` - If true, prevents the group from splitting on its first break attempt. The default value is `false`.

### Data Grouping and Report Variables

Report variables are special objects built on top of a report expression in order to apply the code reusability during the report design stage. Report templates are a lot simplified when expressions frequently used throughout the report template are declared in a single place as objects that can be referenced by their name as unique identifier.

Another great advantage of declaring expressions as variables is the ability to perform various calculations based on their corresponding expression.

A report variable is characterized by the following attributes:

- `name` - The variable's name used as unique identifier. It is mandatory.
- `class` - The class name of the variable's values. The default class is java.lang.String
- `resetType` - Indicates when the variable should be reinitialized during the report-filling process. Possible values are:
    - `None`
    - `Report` (default)
    - `Page`
    - `Column`
    - `Group`
- `resetGroup - If the `resetType` is set to `Group`, indicates the name of the group which triggers the variable reinitializing each time it breaks.
- `incrementType` - Indicates when the variable should be incremented during the report-filling process. Possible values are:
    - `None` (default)
    - `Report`
    - `Page`
    - `Column`
    - `Group`
- `incrementGroup` - If the `incrementType` is set to `Group`, indicates the name of the group which triggers the variable increment each time it breaks.
- `calculation` - Indicates the built-in calculation type to be performed on the variable expression value. Possible values are: `Nothing, Count, DistinctCount, Sum, Average, Lowest, Highest, StandardDeviation, Variance, System, First`. The default value is `Nothing`.
- `incrementerFactoryClass` - The name of the incrementer factory class.

**Note:** With every iteration through the data source, variables are evaluated/incremented in the same order as they are declared. Therefore, the order of variables as they appear in the report template is very important.

For more about report variables one can consult also the [tutorial](http://community.jaspersoft.com/wiki/jasperreports-library-tutorial#Variables).

Being familiarized with variables, it's time to focus back on data grouping. Report variables are very useful objects in a report, that can operate on any collection of data. Since groups are aggregating data in meaningful collections, variables could be used to perform operations at group level too. As seen above, they can be reset or incremented any time a group breaks, and can be used to perform various built-in calculations over parameters, other variables and/or field values in a group.
Related to groups, there is a special built-in counting variable, used to count the number of records in a group. This type of variables are always named with the related group's name followed by the `_COUNT` suffix. In this sample we'll find some usage examples of this group counting variables.

### The Jasper Sample

This sample illustrates how groups can be used to produce documents with complex data grouping in a multicolumn layout. The `reports/FirstJasper.jrxml` file describes a shipping report presented in a 2-column layout, with data organized according to several specific grouping criteria.\
The 2-column layout is given by the `columnCount="2"` attribute setting in the `<jasperReport/>` element.

Further, one can see the report query retrieving data ordered by the `ShipCountry` field:

```
<query language="sql"><![CDATA[SELECT * FROM Orders WHERE OrderID <= $P{MaxOrderID} ORDER BY ShipCountry]] ></query>
Next in the report are defined some variables related to groups:
<variable name="FirstLetter" resetType="None" class="java.lang.String">
  <expression><![CDATA[$F{ShipCountry}.substring(0, 1).toUpperCase()]] ></expression>
</variable>
<variable name="FreightSumFirstLetterGroup" resetType="Group" calculation="Sum" resetGroup="FirstLetterGroup" class="java.lang.Double">
  <expression><![CDATA[$F{Freight}]] ></expression>
</variable>
<variable name="FreightSumCountryGroup" resetType="Group" calculation="Sum" resetGroup="CountryGroup" class="java.lang.Double">
  <expression><![CDATA[$F{Freight}]] ></expression>
</variable>
<variable name="FreightSumColumn" resetType="Column" calculation="Sum" class="java.lang.Double">
  <expression><![CDATA[$F{Freight}]] ></expression>
</variable>
<variable name="FreightSumPage" resetType="Page" calculation="Sum" class="java.lang.Double">
  <expression><![CDATA[$F{Freight}]] ></expression>
</variable>
<variable name="FreightSumReport" calculation="Sum" class="java.lang.Double">
  <expression><![CDATA[$F{Freight}]] ></expression>
</variable>
<variable name="DateHighestCountryGroup" resetType="Group" calculation="Highest" resetGroup="CountryGroup" class="java.sql.Timestamp">
  <expression><![CDATA[$F{OrderDate}]] ></expression>
</variable>
<variable name="RegionCountCountryGroup" resetType="Group" calculation="Count" resetGroup="CountryGroup" class="java.lang.Integer">
  <expression><![CDATA[$F{ShipRegion}]] ></expression>
</variable>
<variable name="FirstLetterStartPageNumber" resetType="Group" calculation="Sum" resetGroup="FirstLetterGroup" class="java.lang.Integer">
  <expression><![CDATA[$V{FirstLetterGroup_COUNT} <= 1 ? $V{PAGE_NUMBER} : 0]] ></expression>
</variable>
```

Now let's take a look at the groups themselves:

```
<group name="FirstLetterGroup" minHeightToStartNewPage="200" startNewColumn="true" reprintHeaderOnEachPage="true">
  <expression><![CDATA[$V{FirstLetter}]] ></expression>
  <groupHeader>
    <band height="25">
      <element kind="staticText" mode="Opaque" y="14" width="130" height="11" forecolor="#FF0000" backcolor="#FFDDDD" underline="true" style="Sans_Italic">
      <text><![CDATA[Countries Starting With Letter :]] ></text>
    </element>
    <element kind="textField" mode="Opaque" x="130" y="14" width="70" height="11" forecolor="#FF0000" backcolor="#FFDDDD" style="Sans_Bold">
      <expression><![CDATA[$V{FirstLetter}]] ></expression>
    </element>
    <element kind="textField"... evaluationTime="Group" evaluationGroup="FirstLetterGroup" style="Sans_Bold">
      <expression><![CDATA[$V{PAGE_NUMBER} - $V{FirstLetterStartPageNumber} + 1]] ></expression>
    </element>
    <element kind="textField" mode="Opaque" x="200" y="14" width="30" height="11" forecolor="#FF0000" backcolor="#FFDDDD" hTextAlign="Right" style="Sans_Bold">
      <expression><![CDATA[$V{FirstLetterGroup_COUNT} == 0 ? 1 : ($V{PAGE_NUMBER} - $V{FirstLetterStartPageNumber} + 1)]] ></expression>
    </element>
    <element kind="textField" mode="Opaque" x="230" y="14" width="10" height="11" forecolor="#FF0000" backcolor="#FFDDDD" hTextAlign="Center" style="Sans_Bold">
      <expression><![CDATA["/"]] ></expression>
    </element>
  </band>
  </groupHeader>
  <groupFooter>
  <band height="15">
    <element kind="line" width="270" height="1" forecolor="#FF0000"/>
    <element kind="staticText" y="1" width="45" height="11" forecolor="#FF0000" hTextAlign="Right" style="Sans_Bold">
      <text><![CDATA[Count :]] ></text>
    </element>
    <element kind="textField" x="45" y="1" width="25" height="11" forecolor="#FF0000" hTextAlign="Right" style="Sans_Bold">
      <expression><![CDATA[$V{FirstLetterGroup_COUNT}]] ></expression>
    </element>
    <element kind="staticText" x="70" y="1" width="100" height="11" forecolor="#FF0000" hTextAlign="Right" style="Sans_Bold">
      <text><![CDATA[Total :]] ></text>
    </element>
    <element kind="textField" x="170" y="1" width="60" height="11" forecolor="#FF0000" hTextAlign="Right" pattern="0.00" style="Sans_Bold">
      <expression><![CDATA[$V{FreightSumFirstLetterGroup}]] ></expression>
    </element>
    <element kind="textField" x="230" y="1" width="40" height="11" forecolor="#FF0000" hTextAlign="Right" evaluationTime="Auto" style="Sans_Bold">
      <expression><![CDATA[msg("{0,number,0.00}%", 100d * $V{FreightSumFirstLetterGroup} / $V{FreightSumReport})]] ></expression>
      </element>
    </band>
  </groupFooter>
</group>
```

According to the group expression above, the `FirstLetterGroup` groups data with the same first letter of the `ShipCountry` field. All countries starting with the same letter will be grouped together. When the first letter changes, the `FirstLetterGroup` group will break.

One can see also, from attribute settings, that this group will start in a new column, each time printing its header too, and requires a minimum 200px amount of vertical space in order to prevent starting on a new page by its own.

Another interesting element here is the built-in variable `FirstLetterGroup_COUNT` which counts the number of records in the group. One could notice the variable's name consisting in the related group's name followed by the `_COUNT` suffix.

Other report variables, such as `FreightSumFirstLetterGroup` and `FreightSumReport` were used too, in order to perform statistic calculations.

Now let's jump to the next group in the report. The `FirstLetterGroup` group contains records for all countries starting with the same letter. It means that for groups containing more than one country, records for different countries are mixed together, and this might be not a very good idea. Obviously, a better data grouping procedure is required. For instance, to create a child group inside the FirstLetterGroup for each distinct country starting with that letter. So, the information will become more readable and significant than before.

This is what the second group in the report does, the `CountryGroup`:

```
<group name="CountryGroup" reprintHeaderOnEachPage="true">
  <expression><![CDATA[$F{ShipCountry}]] ></expression>
  <groupHeader>
  <band height="15">
    <element kind="line"y="14" width="270" height="1">
      <property name="net.sf.jasperreports.export.xls.repeat.value" value="true"/>
      <property name="net.sf.jasperreports.export.xls.column.name" value="LineShape"/>
    </element>
    <element kind="textField" x="10" y="2" width="150" height="11" style="Sans_Bold">
      <expression><![CDATA[$F{ShipCountry}]] ></expression>
      <property name="net.sf.jasperreports.export.csv.column.name" value="ShipCountry"/>
      <property name="net.sf.jasperreports.export.csv.repeat.value" value="true"/>
      <property name="net.sf.jasperreports.export.xls.column.name" value="ShipCountry"/>
      <property name="net.sf.jasperreports.export.xls.column.width.metadata" value="100"/>
      <property name="net.sf.jasperreports.export.xls.repeat.value" value="true"/>
      <property name="net.sf.jasperreports.export.json.path" value="ShipCountry"/>
      <property name="net.sf.jasperreports.export.json.repeat.value" value="true"/>
    </element>
    <element kind="textField" ... evaluationTime="Group" evaluationGroup="CountryGroup" style="Sans_Bold">
      <expression><![CDATA[$V{DateHighestCountryGroup}]] ></expression>
      <patternExpression><![CDATA["EEE, MMM d, yyyy"]] ></patternExpression>
    </element>
  </band>
  </groupHeader>
  <groupFooter>
  <band height="15">
    <element kind="staticText" mode="Opaque" width="45" height="11" backcolor="#C0C0C0" hTextAlign="Right" style="Sans_Bold">
      <text><![CDATA[Count :]] ></text>
    </element>
    <element kind="textField" mode="Opaque" x="45" width="25" height="11" backcolor="#C0C0C0" hTextAlign="Right" style="Sans_Bold">
      <expression><![CDATA[$V{CountryGroup_COUNT}]] ></expression>
    </element>
    <element kind="staticText" mode="Opaque" x="70" width="100" height="11" backcolor="#C0C0C0" hTextAlign="Right" style="Sans_Bold">
      <text><![CDATA[Total :]] ></text>
    </element>
    <element kind="textField" mode="Opaque" x="170" width="60" height="11" backcolor="#C0C0C0" hTextAlign="Right" pattern="0.00" style="Sans_Bold">
      <expression><![CDATA[$V{FreightSumCountryGroup}]] ></expression>
    </element>
    <element kind="textField" mode="Opaque" x="230" width="40" height="11" backcolor="#C0C0C0" hTextAlign="Right" evaluationTime="Auto" style="Sans_Bold">
      <expression><![CDATA[msg("{0,number,0.00}%", 100d * $V{FreightSumCountryGroup} / $V{FreightSumFirstLetterGroup})]] ></expression>
      </element>
    </band>
  </groupFooter>
</group>
```

Here one can observe another built-in counting variable: `CountryGroup_COUNT`. Again some other report variables are used to perform various calculations: `DateHighestCountryGroup`, `FreightSumCountryGroup`, `FreightSumFirstLetterGroup`. The group header will also be reprinted on each new page.

At this stage we can represent data in a user-friendly form, well separated by countries and country first letters, in a 2-columns layout. What else could be done to separate data better than that?

Well, there is one more thing. What if for a given country there are thousands of records? One after one, after one, and so on. Thousands of records looking the same, with nothing special to separate them, increasing a lot the eye's effort... It doesn't sound very good, indeed.

This is why the third group in the report is present. It is an empty dummy group that breaks after every 5 records, introducing a supplementary amount of vertical space between the resulting 5-records groups:

```
<group name="BreakGroup">
  <expression><![CDATA[$V{BreakGroup_COUNT} > 5]] ></expression>
  <groupHeader>
    <band height="5"/>
  </groupHeader>
  <groupFooter>
    <band height="5"/>
  </groupFooter>
</group>
```

Once finished the data grouping work, the report template continues with its usual sections, such as title, title, pageHeader, columnHeader, detail, columnFooter, pageFooter and summary.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/jasper` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/jasper/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='csvmetadataexport'>Exporting</a> to CSV Format Using Report Metadata
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to export the report to CSV format using metadata associated with report elements at design time, to mark data columns.

**Since:** 4.0.0

### Pixel-Perfect Requirement and Excel Output Format

Documents stored in a [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object are ready to be printed out in a pixel-perfect representation. This means that objects' own dimensions, absolute and relative positions within the document are completely preserved. The pixel-perfect representation is a must for the read-only or layout-oriented output formats, such as PDF or Graphics2D, but it could generate some inconvenients for data-oriented output formats like the Excel-based ones. In this case more important than the pixel-perfectness of the document is to allow the generated data in the report to be properly manipulated by the Excel engine, therefore to generate as much as possible Excel-like documents. When opening an Excel document, ideally is to see:

- data with correct field type (numeric, currency, date, etc);
- no extra columns/rows added by the grid generation constraints;
- no page margins and other padding settings;
- no page numbers;
- no repetition of page headers;
- data organized in columns/rows in order to allow formula calculations;
- some report sections to be excluded/hidden;
- outline data grouping;
- etc.

### The CSV Metadata Exporter

A possibility to extract relevant data from a pixel-perfect [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object in order to generate a data-oriented document, is to customize the export. Some properties are needed to tell the JR engine which elements should be exported and where should they be placed in the current sheet. The [JRCsvMetadataExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/JRCsvMetadataExporter.html) is designed to process such kind of metadata information and to generate consistent columns of data, with or without column headers on their top.

Before starting the export work, the CSV metadata exporter looks for the presence of the column names by calling the `getColumnNames()` exporter configuration setting. One can populate this setting either directly, using the APIs, or by collecting values from the `net.sf.jasperreports.export.csv.column.names.{suffix}` properties at report level. If present, they provide a comma-separated list of column names, each one referencing a column that should be exported. For accurate results it's recommended that the column names in the list to follow the same order as the original columns.

Below is an example of populating the column names via JasperReports APIs:

```
JRCsvMetadataExporter exporter = new JRCsvMetadataExporter();
SimpleCsvMetadataExporterConfiguration configuration = new SimpleCsvMetadataExporterConfiguration();
configuration.setColumnNames(new String[]{"Name,Id"});
exporter.setConfiguration(configuration);
```

It ensures that only 2 columns will be printed out: the column with names and the column with Ids.

Now, that we have the column names, it's time to see which elements will be exported into these columns and how. Let's introduce the other metadata properties:

- `net.sf.jasperreports.export.csv.column.name` - this is an important element-level property. If the `net.sf.jasperreports.export.csv.column.names.{suffix}` properties are not present in the report, this property specifies the name of an exported column and assumes that the current element will be placed in that column. If there are defined `net.sf.jasperreports.export.csv.column.names.{suffix}` properties in the report, then the column name specified by `the net.sf.jasperreports.export.csv.column.name` property must be one of the names declared in the `net.sf.jasperreports.export.csv.column.names.{suffix}` properties. The current element will be placed in that column.
- `net.sf.jasperreports.export.csv.write.header` - a report-level flag property indicating whether the column names should be exported as column headers or not. The default value is `false`.
- `net.sf.jasperreports.export.csv.repeat.value` - an element-level flag property indicating whether the value associated to that column should be repeated or not when it is missing.
- `net.sf.jasperreports.export.csv.data` - element-level property containing the exported value for that element. By default is considered the text value of the report element itself.

In this sample only five columns are exported to CSV format: `ShipCountry, Order, ShipCity, ShipRegion` and `OrderDate`. Column headers are not exported and for the ShipCity column the exported data is customized using the `net.sf.jasperreports.export.csv.data` property. All these settings apply to the `<detail/>` section:

```
<element kind="textField"  x="1" width="35" height="11" hTextAlign="Right" style="OrderIdStyle">
  <expression><![CDATA[$F{OrderID}]] ></expression>
  <property name="net.sf.jasperreports.export.csv.column.name" value="Order"/>
  ...
</element>
<element kind="textField" positionType="Float" x="40" width="110" height="11" textAdjust="StretchHeight">
  <expression><![CDATA[$F{ShipName} + ", " + $F{ShipCity}]] ></expression>
  <property name="net.sf.jasperreports.export.csv.column.name" value="ShipCity"/>
  ...
  <propertyExpression name="net.sf.jasperreports.export.csv.data"><![CDATA[$F{ShipCity}]] ></propertyExpression>
  ...
</element>
<element kind="textField" x="155" width="25" height="11" blankWhenNull="true">
  <expression><![CDATA[$F{ShipRegion}]] ></expression>
  <property name="net.sf.jasperreports.export.csv.column.name" value="ShipRegion"/>
  ...
</element>
<element kind="textField" x="185" width="50" height="11" pattern="dd/MM/yyyy">
  <expression><![CDATA[$F{OrderDate}]] ></expression>
  <property name="net.sf.jasperreports.export.csv.column.name" value="OrderDate"/>
  ...
</element>
<element kind="textField" x="235" width="35" height="11" hTextAlign="Right" pattern="0.00">
  <expression><![CDATA[$F{Freight}]] ></expression>
  <property name="net.sf.jasperreports.export.csv.column.name" value="Freight"/>
  ...
</element>
```

After running the 

```
> mvn clean compile exec:exec@all
```

command the data-centric document exported with the CSV metadata exporter will be available in the `demo/samples/jasper/build/reports` directory as `FirstJasper.metadata.csv`.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='xlsxmetadataexport'>Exporting</a> to XLSX Format Using Report Metadata
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to export the report to XLSX format using metadata associated with report elements at design time, to mark data columns.

**Since:** 6.20.0

### The Excel (.xlsx) Metadata Exporter

As shown in the section above, the CSV metadata exporter is a very useful tool when data-oriented outputs are preferred. The only problem is that any format setting is dropped when the CSV file is saved. CSVs do not store layout and style, they only keep data safe. Sometimes this is not enough for complex documents. In vary situations data need to be presented in a more specific way, column headers should be centered, totals should appear in a bold style, some cells might require a particular background, etc. More than that, if we want to perform calculations on data, we need to know the associated data type. And another limitation is that the CSV format cannot be used to export graphic elements, like images and shapes.

In such cases we have to keep together relevant data with their style and formatting information. Since the CSV metadata exporter is limited to pure text-data only, another metadata-based exporter can be used to solve the problem. This is the [XlsxMetadataExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ooxml/XlsxMetadataExporter.html) that provides a columnar Excel (XLSX) output. It relies on equivalent metadata properties and parameters having the same meanings, but dedicated to the XLSX output format:

- `net.sf.jasperreports.export.xls.column.names.{arbitrary_name}`
- `net.sf.jasperreports.export.xls.column.name`
- `net.sf.jasperreports.export.xls.write.header`
- `net.sf.jasperreports.export.xls.repeat.value`
- `net.sf.jasperreports.export.xls.data` - in the XLSX metadata export this property is applicable for text elements only.

Another property, which is specific to XLSX metadata export only, is `net.sf.jasperreports.export.xls.column.width.metadata`. This is useful when we choose to export column names as column headers longer than the column width available for the non-metadata XLSX export, without affecting the non-metadata XLSX export.

In this sample the following columns are exported to XLSX format: `JasperImage` (which is a picture), `LineShape` (a shape element), `ShipCountry`, `Order`, `ShipCity`, `ShipRegion` and `OrderDate`. Column headers are exported too, and for the `ShipCity` column the exported data is customized using the `net.sf.jasperreports.export.xls.data` property.

The elements were set as in the following example:

```
<groupHeader>
  <band height="15">
    <element kind="line" y="14" width="270" height="1">
      <property name="net.sf.jasperreports.export.xls.repeat.value" value="true"/>
      <property name="net.sf.jasperreports.export.xls.column.name" value="LineShape"/>
    </element>
    <element kind="textField" x="10" y="2" width="150" height="11" style="Sans_Bold">
      <expression><![CDATA[$F{ShipCountry}]] ></expression>
      ...
      <property name="net.sf.jasperreports.export.xls.column.name" value="ShipCountry"/>
      <property name="net.sf.jasperreports.export.xls.column.width.metadata" value="100"/>
      <property name="net.sf.jasperreports.export.xls.repeat.value" value="true"/>
      ...
    </element>
    ...
  </band>
</groupHeader>

...

<title height="100">
  <element kind="elementGroup">
    ...
    <element kind="image" key="TransparentImage" y="5" width="165" height="40" scaleImage="Clip" onErrorType="Error">
      <expression><![CDATA["jasperreports.png"]] ></expression>
      <hyperlinkTooltipExpression><![CDATA["The JasperReports Logo"]] ></hyperlinkTooltipExpression>
      <property name="net.sf.jasperreports.export.xls.repeat.value" value="true"/>
      <property name="net.sf.jasperreports.export.xls.column.name" value="JasperImage"/>
      <property name="net.sf.jasperreports.export.xls.column.width.metadata" value="200"/>
    </element>
  </element>
  ...
</title>

...

<detail>
  <band height="13">
  ...
    <element kind="textField" x="1" width="35" height="11" hTextAlign="Right" style="OrderIdStyle">
      <expression><![CDATA[$F{OrderID}]] ></expression>
      ...
      <property name="net.sf.jasperreports.export.xls.column.name" value="Order"/>
      <property name="net.sf.jasperreports.export.xls.column.width.metadata" value="50"/>
      ...
    </element>
    <element kind="textField" positionType="Float" x="40" width="110" height="11" textAdjust="StretchHeight">
      <expression><![CDATA[$F{ShipName} + ", " + $F{ShipCity}]] ></expression>
      ...
      <property name="net.sf.jasperreports.export.xls.column.name" value="ShipCity"/>
      ...
      <propertyExpression name="net.sf.jasperreports.export.xls.data"><![CDATA[$F{ShipCity}]] ></propertyExpression>
      ...
    </element>
    <element kind="textField" x="155" width="25" height="11" blankWhenNull="true">
      <expression><![CDATA[$F{ShipRegion}]] ></expression>
      ...
      <property name="net.sf.jasperreports.export.xls.column.name" value="ShipRegion"/>
      <property name="net.sf.jasperreports.export.xls.column.width.metadata" value="60"/>
      ...
    </element>
    <element kind="textField" x="185" width="50" height="11" pattern="dd/MM/yyyy">
      <expression><![CDATA[$F{OrderDate}]] ></expression>
      ...
      <property name="net.sf.jasperreports.export.xls.column.name" value="OrderDate"/>
      <property name="net.sf.jasperreports.export.xls.column.width.metadata" value="100"/>
      <property name="net.sf.jasperreports.export.xls.pattern" value="dd\/MM\/yyyy"/>
      ...
    </element>
    <element kind="textField" x="235" width="35" height="11" hTextAlign="Right" pattern="0.00">
      <expression><![CDATA[$F{Freight}]] ></expression>
      ...
      <property name="net.sf.jasperreports.export.xls.column.name" value="Freight"/>
      <property name="net.sf.jasperreports.export.xls.column.width.metadata" value="60"/>
      ...
    </element>    ...
  </band>
</detail>
```

After running the

```
>mvn clean compile exec:exec@all
```

command the data-centric document exported with the XLSX metadata exporter will be available in the `demo/samples/jasper/build/reports` directory in 2 forms:

- `FirstJasper.singleSheet.metadata.xlsx` - as single sheet document, and
- `FirstJasper.multiSheet.metadata.xlsx` - as multisheet Excel document. Notice the differences in text formatting between these files and the `FirstJasper.metadata.csv` CSV document.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='xlsmetadataexport'>Exporting</a> to XLS Format Using Report Metadata
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to export the report to XLS format using metadata associated with report elements at design time, to mark data columns.

**Since:** 4.0.0

### The Excel 2003 Metadata Exporter

This metadata exporter can be used to export data in an older Microsoft Excel format (Excel 2003, or `".xls"`). It is strongly recommended to use the XLSX metadata exporter instead. The XLSX metadata exporter is described in the **"The Excel (.xlsx) Metadata Exporter** feature section of this document. All metadata export properties presented in the **The Excel (.xlsx) Metadata Exporter** feature are also applicable for the Excel 2003 metadata exporter, except `net.sf.jasperreports.export.xls.column.width.metadata`.

After running the

```
>mvn clean compile exec:exec@all
```

command the data-centric document exported with the Excel 2003 metadata exporter will be available in the `demo/samples/jasper/build/reports` directory as `FirstJasper.metadata.xls`.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='conditionalStylesInReports'>Conditional</a> Styles in Reports
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to define and use a conditional style in a report template.

**Since:** 1.2.0

**Other Samples**\
[/demo/samples/scriptlet](../scriptlet/README.md)\
[/demo/samples/crosstabs](../crosstabs/README.md)\
[/demo/samples/list](../list/README.md)\
[/demo/samples/query](../query/README.md)

### Conditional Styles

In JasperReports using styles represents the most common way to generate the report elements appearance. Styles mean anything about fonts, but also mean borders, horizontal and vertical alignment, element transparency or opacity, background and foreground colors, format patterns, text markup information, text rotation, and so on... Almost anything needed to set up a complex, elegant and meaningful element look and feel.

However, there are situations when the L&F depends on certain conditions determined only at runtime. A common example is the necessity to use alternate row colors in a table with a large number of rows, in order to better distinguish between them. When the total number of rows is unknown at report design time, or if the row is used in the detail section, a tool for setting up different backgrounds for the same cell is definitely required.

And here come conditional styles to get some help. They can be defined either in the report itself, or, for some particular values, in style templates.
For more information about conditional styles defined in style templates you could take a look at the [Query](../query/README.md#conditionalStylesInTemplates) sample.

Next we'll present conditional styles defined in reports.

Conditional styles consist in two elements: a Boolean condition expression and a style. When the condition evaluates to true, the corresponding style is applied. In this sample one can see a conditional style definition:

```
  <style name="Sans_Normal" fontName="DejaVu Sans" fontSize="8.0" default="true" bold="false" italic="false" underline="false" strikeThrough="false"/>
  ...
<style name="OrderIdStyle" style="Sans_Normal">
  <conditionalStyle forecolor="#FF0000" bold="true">
    <conditionExpression><![CDATA[$F{OrderID} % 10 == 0]] ></conditionExpression>
  </conditionalStyle>
</style>
```

The `OrderIdStyle` defined above derives from the `Sans_Normal` parent style. Usually, elements with `OrderIdStyle` will have the default black forecolor and a normal font weight. But when the condition expression evaluates to true (this happens each time the `OrderID` field value is a multiple of 10) the element's style changes: the forecolor becomes red and the font is displayed in bold style.

Therefore, when applied, a conditional style will override the properties of its parent style.

A style definition may contain many conditional styles, any of them inheriting from the parent style. In this case, all conditions that evaluate to true will append their own style properties to the existing style, in the same order they were defined in the report (the second style will be appended to the first one, and so on). If more than one conditional style affect the same style property, the property value will be done by the first conditional style in the styles sequence which evaluates its condition to `true`.

Here is a more elaborate example:

```
<style name="alternateStyle" fontName="Arial" forecolor="red">
  <conditionalStyle forecolor="blue">
    <conditionExpression><![CDATA[$V{REPORT_COUNT} % 2 == 0]] ></conditionExpression>
  </conditionalStyle>
  <conditionalStyle bold="true">
    <conditionExpression><![CDATA[$F{AMOUNT} > 10000]] ></conditionExpression>
  </conditionalStyle>
  <conditionalStyle bold="false" italic="true">
    <conditionExpression><![CDATA[$F{AMOUNT} > 20000]] ></conditionExpression>
  </conditionalStyle>
</style>
```

One can see above that on each even row the element forecolor becomes blue. More than that, if the `AMOUNT` field value is greater than 10,000, the second conditional style evaluates to `true`, and the element displays in bold font (this doesn't affect the red color or blue color set by the first conditional style, because the forecolor and font weight properties do not interfere). Further, if the `AMOUNT` field value is greater than 20,000, the font style becomes italic, without affecting the font color. But because any value greater than 20,000 is also greater than 10,000, then the `bold="false"` font setting is not applied anymore, because both conditions >10,000 and >20,000 evaluate to true and `bold="true"` style was appended first.

**Note:**

- By default, the style condition expressions are evaluated during the report filling at the moment they are actually used. The conditional expression evaluation is performed with the current values of referenced variables and fields in that moment, regardless of the evaluationTime attribute of the element itself.If the condition expression of the style needs a delayed evaluation, just like the value of the text field or the image element that uses the conditional style, the `net.sf.jasperreports.style.evaluation.time.enabled` configuration property should be set to true.
- Conditional styles may be defined in style templates only when the condition expression is a simple reference to a Boolean report parameter, field or variable (see [Query](../query/README.md#conditionalStylesInTemplates) sample). Also, styles defined in style templates can be used as parent styles for conditional styles defined in the report.
- Other examples of conditional styles can be found in [Query](../query/README.md), [Scriptlet](../scriptlet/README.md), [List](../list/README.md) and [Crosstabs](../crosstabs/README.md) samples.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='pptxfields'>Updating</a> current date and slide numbers in PPTX export
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to use PPTX fields to update some predefined values such as current date or slide number.

**Since:** 6.8.0

**Other Samples**\
[/demo/samples/i18n](../i18n/README.md)

### Exporting text elements as PPTX fields

The following situations may frequently occur when working with PowerPoint presentations:

- when a slide is removed from a presentation that displays slide numbers on each page, the remaining slides should rearrange their slide number accordingly;
- a presentation needs to display the current date each time it is running.

In such cases we need values to be automatically updated, and this can be done if text elements are exported as PPTX fields with specific behavior. To do so, one can set the following text element property:

```
net.sf.jasperreports.export.pptx.field.type
```

There are 2 possible values for this property:

- `slidenum` - applies to text elements that store numeric values, and associates these elements with autogenerated slide numbers
- `datetime` - applies to text elements that store date/time values and associates these elements with the current date/time

When the property is set as datetime, the following date/time patterns can be processed to display the current date in PPTX:

- `MM/dd/yyyy`
- `EEEE, MMMM dd, yyyy`
- `dd MMMM yyyy`
- `MMMM dd, yyyy`
- `dd-MMM-yy`
- `MMMM yy`
- `MMM-yy`
- `MM/dd/yyyy hh:mm a`
- `MM/dd/yyyy hh:mm:ss a`
- `HH:mm`
- `HH:mm:ss`
- `hh:mm a`
- `hh:mm:ss a`

If none of the above patterns are set for the element, the date/time will be displayed using the default pattern of the PPTX viewer.

### PPTX field example

The following example can be found in the `FirstJasper.jrxml` file:

```
<element kind="textField"  x="200" y="5" width="75" height="20" hTextAlign="Right" fontSize="14.0">
  <expression><![CDATA[$V{PAGE_NUMBER}]] ></expression>
  <property name="net.sf.jasperreports.export.pptx.field.type" value="slidenum"/>
</element>
```

At export time this text element will be translated into a PPTX field that generates automatic slide number.

Another example of a datetime PPTX field can be found in the [i18n](../i18n/README.md) sample.
