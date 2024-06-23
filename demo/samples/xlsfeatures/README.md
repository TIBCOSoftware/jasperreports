
# JasperReports - Advanced Excel Features Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the specific Excel features could be turned on when exporting to Excel formats.

### Main Features in This Sample

[Advanced Excel Features](#xlsfeatures)

## <a name='xlsfeatures'>Advanced</a> Excel Features
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to turn on Excel specific features to improve the generated document in Excel formats.

**Since:** 4.1.1

**Other Samples**\
[/demo/samples/xlsformula](../xlsformula/README.md)

### Pixel-Perfect Document Representation and Excel Output Format

One of the main purposes of the JasperReports engine is to prepare documents ready to be printed out in a pixel-perfect representation. In this approach objects own dimensions, absolute and relative positions within the document should be totally preserved. This is completely convenient for usually read-only or layout-oriented output formats, such as PDF or Graphics2D, but could generate some inconvenients for data-oriented output formats like the Excel-based ones. In this case more important than the pixel-perfectness of the document is to allow the generated data in the report to be properly manipulated by the Excel engine, therefore to generate as much as possible Excel-like documents, instead of rigorously pixel-perfect ones.

More, there are certain features at the moment available for Excel documents only, such as freezing panes, grouping rows, stretching row heights or column widths, applying formulas, etc. To provide support for all these specific features in generated Excel documents would amazingly increase their functionality and applicability.

That's why a number of custom export properties were added, in order to allow the Excel features to take precedence over the fixed pixel-perfect document layout. This sample shows one by one these recently added properties and how they work.

### Dynamic Sheet Names

One of the main preoccupations when exporting to Excel output format is to ensure that every sheet in the document has a proper name, because no-named sheets are not allowed in Excel. Older JasperReports versions provided several ways to accomplish this, to cover as many as possible situations. Users either can rely on the engine's default sheet naming procedure, or they can provide themselves custom sheet names at runtime based on the [`getSheetNames()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#getSheetNames()) export configuration setting. This second option is very useful when dynamic sheet names are involved, but it cannot cover all possible situations. One of the main troubles when using this setting is the impossibility to be used at design time, so it has to be Java hardcoded at runtime.

To solve such kind of problems, 2 new export custom properties were added:

- If sheet names are known at design time, they can be specified in one or more properties at report level, starting with the [`net.sf.jasperreports.export.xls.sheet.names.`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_SHEET_NAMES_PREFIX) prefix. A [`net.sf.jasperreports.export.xls.sheet.names.{suffix}`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_SHEET_NAMES_PREFIX) property may contain one or several sheet names separated by a slash (`/`), and does not support dynamic values via the `propertyExpression`.
- Another way is to use the [`net.sf.jasperreports.export.xls.sheet.name`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ExcelAbstractExporter.html#PROPERTY_SHEET_NAME) property. It may be set at element level and, if present, will override all other settings for the current sheet name (ie the sheet name provided by the [`getSheetNames()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#getSheetNames()) exporter setting or by the document-level sheet names properties). Since this property supports also expressions, it's completely suitable for dynamic sheet names.

In our `demo/samples/xlsfeatures/reports/XlsFeaturesReport.jrxml` sample one could see an example of usage in the text element labeled `ID` in the page header:

```
  <propertyExpression name="net.sf.jasperreports.export.xls.sheet.name">
    <![CDATA[$P{Customers}+ " " + $V{PAGE_NUMBER}]] >
  </propertyExpression>
```

Therefore each sheet will be labeled with the `Customers <i>` text instead of the default `Page <i>` (where `i` stands for the sheet index).

### Format Pattern Property

Format patterns are useful when data have to be presented in a completely readable and meaningful form. To see an example, dates are often stored as numbers (the number of milliseconds that have passed since `January 1, 1970 00:00:00.000 GMT`). But reading such kind of numbers does not fit our usual representation of a date, with perfectly delimited year, month and day fields. A format pattern is absolutely necessary here, to get a meaningful representation of these numbers.

Here comes the `pattern` attribute of textfields to solve the problem. For almost all output formats, excepting the Excel ones. That's because the `pattern` attribute contains standard Java format patterns. Unfortunately, not all patterns valid for Java are valid for Excel too. There are situations where such a pattern should be translated in order to be recognized in Excel.

An older way to realize this translation is to use the [`getFormatPatternsMap()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsExporterConfiguration.html#getFormatPatternsMap()) exporter setting at runtime, with the same inconvenient of the need of Java hardcoding.

Instead of this, now one has the possibility to specify the proper format pattern for Excel right in the report sample, at element level, using the custom property

```
net.sf.jasperreports.export.xls.pattern
```

To see it in action, take a look at the current date textfield in the `XlsFeaturesReport.jrxml` sample's `<pageHeader/>` section:

```
<element kind="textField" pattern="EEE, MMM d, yyyy" ...>
	<expression><![CDATA[$P{ReportDate}]] ></expression>
	<property name="net.sf.jasperreports.export.xls.pattern" value="ddd, mmm d, yyyy"/>
	...
</element>
```

In this case the `EEE, MMM d, yyyy` pattern, completely valid in Java but generating unreadable content for Excel, is replaced with the equivalent `ddd, mmm d, yyyy` pattern when exporting report to Excel.

### Format Pattern Properties

In case we have several reports where the same Excel specific patterns should be applied, or if we have several elements in a report sharing the same format pattern, a huge effort will be consumed to write a `net.sf.jasperreports.export.xls.pattern` property per each affected textfield. We need a global setting to manage format patterns at a higher level.

Starting with JasperReports v.6.5.0 were introduced the [`net.sf.jasperreports.export.xls.pattern.{arbitrary_pattern}`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#FORMAT_PATTERN_PREFIX) properties. These properties can be set per report context or per report. Properties defined in the report will override the context properties.

The `{arbitrary_pattern}` suffix represents a Java format pattern that is present in a report, and the property value represents the corresponding Excel format pattern. At export time, whenever an element provides a Java pattern, the exporter looks first for the `net.sf.jasperreports.export.xls.pattern` property at element level. If no such property is found, then the exporter looks into the format patterns map constructed from all the `net.sf.jasperreports.export.xls.pattern.{arbitrary_pattern}` properties. Keys of this map are the Java patterns and values are the related Excel patterns. If the exporter finds the Java pattern it is looking for, it will apply the related Excel pattern for that element.

When these properties are defined in JRXML or in a properties file, special characters need to be escaped (for instance: `'<', '&'`, etc, in JRXML; or `'='`, whitespace, etc, in the properties file).

An element-level property `net.sf.jasperreports.export.xls.pattern` takes precedence over `net.sf.jasperreports.export.xls.pattern.{arbitrary_pattern}`.

An example of working with these properties can be found in the `FormatPatternsMap.jrxml` JRXML sample. Here there are some textfields with Java format patterns declared in their pattern attribute, that are unreadable in Excel. They are translated into Excel patterns using the `net.sf.jasperreports.export.xls.pattern.{arbitrary_pattern}` properties declared in `jasperreports.properties` file.

The JRXML sample:

```
<element kind="textField" pattern="EEE, MMM d, yyyy" ...>
	<expression><![CDATA[new java.util.Date()]] ></expression>
	<property name="net.sf.jasperreports.export.xls.pattern" value="ddd, mmm d, yyyy"/>
</element>

...

<element kind="textField" pattern="hh:mm a" ...>
	<expression><![CDATA[new java.util.Date()]] ></expression>
	<property name="net.sf.jasperreports.export.xls.pattern" value="hh:mm AM/PM"/>
</element>

...

<element kind="textField" pattern="#,##0.00¤" ...>
	<expression><![CDATA[1234.567]] ></expression>
	<property name="net.sf.jasperreports.export.xls.pattern" value="hh:mm AM/PM"/>
</element>

...
```

The jasperreports.properties file:

```
# whitespaces in property key should be prefixed with '\'
net.sf.jasperreports.export.xls.pattern.EEE,\ MMM\ d,\ yyyy=ddd, MMM dd, yyyy
net.sf.jasperreports.export.xls.pattern.#,##0.00¤=#,##0.00$
net.sf.jasperreports.export.xls.pattern.#,##0.00=#,##0.00%
```

In this case the `'EEE, MMM d, yyyy'` pattern is replaced with the equivalent `'ddd, mmm dd, yyyy'` pattern when exporting report to Excel. `'#,##0.00¤'` is replaced with `'#,##0.00$'` and `'#,##0.00'` is replaced with `'#,##0.00%'`.

### Column Width Adjustment Properties

One of the most frequently encountered problems when exporting documents to Excel consists in the inadequate resulting column width. In some cases one could observe that the generated column width differs completely from the expected one. This is due to the fact that Excel uses character-width units to measure column widths, while all sizes defined in a JasperReport document are measured in pixels. Because one cannot guess at design time which will be the default font set in the Excel Normal style template, the JR engine performs an approximative calculation which doesn't fit always the Excel's own calculations.

In all these cases, we have the possibility to adjust the column width by setting a more appropriate width (in pixels) for that column, in order to minimize the difference between JR and Excel calculations. The [`net.sf.jasperreports.export.xls.column.width`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ExcelAbstractExporter.html#PROPERTY_COLUMN_WIDTH) and [`net.sf.jasperreports.export.xls.column.width.ratio`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExcelAbstractExporter.html#PROPERTY_COLUMN_WIDTH_RATIO) properties are introduced to accomplish this task.

1. If the width adjustment is required for particular columns, it could be done using the `net.sf.jasperreports.export.xls.column.width` property. Its value is represented in pixels, and can be set at element level only, to values greater or less than the element's width. When exporting to Excel, the value of this property will be considered instead. One can see this property set in the `XlsFeaturesReport.jrxml` sample for the element labeled `State`:

```
  <property name="net.sf.jasperreports.export.xls.column.width" value="110"/>
```

This means that the column width is no more dependent on element widths on that column, its value being calculated as the equivalent value in character widths for 110 pixels. When running the sample one can see that the State column width remains fixed while other column widths are changing according to other settings present in the report.

2. If the width adjustment is required for all columns in a sheet or in the entire document, the use of the `net.sf.jasperreports.export.xls.column.width` property per each distinct column could be bypassed with `the net.sf.jasperreports.export.xls.column.width.ratio` property. Its value, a floating point number, represents the ratio used to adjust all column widths in the sheet or in the document. When it's defined at report level all columns in the document will be affected, and when it's defined at element level only columns present in the current sheet will be affected. Settings at element level override settings at document level. 

This behavior is illustrated in the JRXML sample as it follows:

First, the net.sf.jasperreports.export.xls.column.width.ratio property is declared at document level:

```
  <property name="net.sf.jasperreports.export.xls.column.width.ratio" value="1.25f"/>
```

This means that all column widths in the document should be multiplied by a 1.25 factor, unless conditions with higher priority are met. This is what happens in the first page of the generated document for all columns excepting State one, with the fixed 110 pixels width (see the `net.sf.jasperreports.export.xls.column.width.ratio`) property discussed above. But on the second page one can see that columns are no more enlarged, on the contrary, they are visibly narrowed down. This is because the Street textfield in the detail section contains a conditional property expression which overrides the document-level setting:

```
  <propertyExpression name="net.sf.jasperreports.export.xls.column.width.ratio">
    <![CDATA[$F{id}.equals(44) ? "0.75f" : null]] >
  </propertyExpression>
```

When the record with field value id = 44 is rendered (and this happens in the second page) the condition is satisfied, so all columns on the current sheet will be multiplied by 0.75 instead of 1.25. The `State` column width remains unchanged on the second page too.

### Setting Sheet Tab Colors

In order to obtain colored sheet tabs, one can set the custom property [`net.sf.jasperreports.export.xls.sheet.tab.color`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_SHEET_TAB_COLOR), either in a jasperreports.properties file or at report or element level in JRXML. The value of this property serves as default for the [`getSheetTabColor()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#getSheetTabColor()) configuration setting. Usually, the property value can be written as a `#`-prefixed CSS color string (for instance: `"#FF0000"`).

If the property is set at report level, then all sheet tabs in the generated document will expose this color. In our sample you can notice the

```
  <property name="net.sf.jasperreports.export.xls.sheet.tab.color" value="#00FF00"/>
```

setting at report level. This will produce green tabs for all sheets in the report, unless another property set at report level will override this value.

If the property is set at element level, it will produce a colored tab only for the sheet the element belongs to. As usual, element-level setting do override report-level settings. If there are several elements in a sheet that provide this property, will be considered only the property of the last exported element in that sheet. In our sample there is an element-level property set for the `$F{address}` textfield where id is 44:

```
<element kind="textField" positionType="Float" x="316" width="199" height="15" textAdjust="StretchHeight">
  <expression><![CDATA[$F{address}]] ></expression>
  <propertyExpression name="net.sf.jasperreports.export.xls.auto.filter">
    <![CDATA[$V{PAGE_NUMBER}.equals(1) ? "End" : null]] >
  </propertyExpression>
  <propertyExpression name="net.sf.jasperreports.export.xls.column.width.ratio">
    <![CDATA[$F{id}.equals(44) ? "0.75f" : null]] >
  </propertyExpression>
  <propertyExpression name="net.sf.jasperreports.export.xls.sheet.tab.color">
    <![CDATA[$F{id}.equals(44) ? "#FF0000" : null]] >
  </propertyExpression>
  ...
</element>
```

According to this one, the tab color of the sheet containing this element will turn to red, overriding the green value set at report level. Running the sample you will notice in the generated XLSX output the first green tab (due to report-level settings) and the second red tab (due to element-level settings).

**Note:** the Tab Color property is considered only for the XLSX and ODS output channels. It has no effect for the older XLS output format.

### Freeze Pane Properties

Freeze panes represent an Excel-specific feature with considerable visual impact, so they have their well deserved place among special features supported by JR. There are multiple ways to define freeze panes in JR and they can also be combined, in order to cover a large variety of situations.

1. The first category of properties contain settings for the entire document. There are separate properties for rows and column in order to allow possible combinations with properties at element level. The 2 document-level properties are:

[`net.sf.jasperreports.export.xls.freeze.row`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_FREEZE_ROW)\
and\
[`net.sf.jasperreports.export.xls.freeze.column`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_FREEZE_COLUMN)

Their values should represent valid row indexes and column names in Excel. The freeze row (or column) property defined at report level can be combined with a freeze column (or row) property defined at element level to delimit the freeze pane edges. As usual, element-level settings will override document-level settings.

2. The element-level properties define the freeze pane edges for the current sheet only. If multiple elements in the same sheet provide freeze pane properties, the maximum values for these properties are taken into account. Element-level properties are

[`net.sf.jasperreports.export.xls.freeze.row.edge`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ExcelAbstractExporter.html#PROPERTY_FREEZE_ROW_EDGE)\
and\
[`net.sf.jasperreports.export.xls.freeze.column.edge`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ExcelAbstractExporter.html#PROPERTY_FREEZE_COLUMN_EDGE)

The element-level properties can take a predefined set of values: `Left` and `Right` for the `net.sf.jasperreports.export.xls.freeze.column.edge`, `Top` and `Bottom` for the `net.sf.jasperreports.export.xls.freeze.row.edge`.

The last 2 freeze pane properties are illustrated in the `XlsFeaturesReport.jrxml` sample report: the column edge is set for the `State` element and the row edge is set for the `Street` element, both in the page header:

```
  <property name="net.sf.jasperreports.export.xls.freeze.column.edge" value="Left"/>
  <property name="net.sf.jasperreports.export.xls.freeze.row.edge" value="Bottom"/>
```

These settings instruct the engine that the left columns to the State element and all rows above, including the current row, are "frozen".

### The Autofilter Property

In editable Excel documents data can be autofiltered allowing users to show/hide only the desired data in a range. This feature is also supported in JR when the [`net.sf.jasperreports.export.xls.auto.filter`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ExcelAbstractExporter.html#PROPERTY_AUTO_FILTER) property is explicitly set. This is an element-level property and may have 2 allowed values:

- `Start` - the data range starts immediately below the current cell; on the current row will be placed the autofilter lists with column data.
- `End` - the data range ends with the current cell (and row and column).

If multiple Start or End values are found in the same sheet, only the last encountered are considered.

To see how it works, take a look at the following settings in the `XlsFeaturesReport.jrxml` sample:

```
<property name="net.sf.jasperreports.export.xls.auto.filter" value="Start"/>
```

for the `State` element in the page header; all column data lists will be placed on this row, and the data range will start immediately below this cell

```
<propertyExpression name="net.sf.jasperreports.export.xls.auto.filter"><![CDATA[$V{PAGE_NUMBER}.equals(1) ? "End" : null]]></propertyExpression/>
```

for the `Street` field in the detail section; therefore for the first page only the data range will end on the last Street detail cell (because all `Street` detail cells do provide this property). For all other pages there is no autofilter `End` value defined, and the data range contains only data in the column enclosing the autofilter `Start` value.

```
<propertyExpression name="net.sf.jasperreports.export.xls.auto.filter"><![CDATA[$F{id}.equals(44) ? "Start" : null]]></propertyExpression/>
```

for the `Name` textfield in the detail section. There are 2 occurrences of the autofilter `Start` value in the second sheet. One is given by the setting of the `State` element in the page header, the other is set in the `Name` detail textfield. At export time, the last encountered setting is provided by the detail element, therefore in the second sheet the data range will start on the `Nam`e column, immediately below the row with field `valueid=44`. Because there is no autofilter `End` value defined on the second sheet, the data range contains only current column data.

### The Outline Row Level Property

Another noticeable feature is the row outline grouping on different indentation levels. To turn this feature on, one has to know which group each row belongs to, and which outline level corresponds to this group. All these informations are managed using a property formed with the fixed `net.sf.jasperreports.export.xls.outline.level` prefix followed by a suffix representing the outline level: `net.sf.jasperreports.export.xls.outline.level.<suffix>`. It's recommended to use the outline level itself as property suffix, but it's not mandatory. The suffix may take any other string value, but one has to keep in mind that suffixes are used as sorted row level descriptors. For instance, the suffix `"aaa" < "bbb"`, therefore the outline level associated with the suffix `"aaa"` will be smaller than the level associated with `"bbb"`. To have the most simple and intuitive representation of the outline level property, the best practice is to use the numeric row level as property suffix.

Doing so, a `net.sf.jasperreports.export.xls.outline.level.2` property means that its value is correlated with the outline level `2`, so the current row belongs to a row group with outline level `2`. According to Office Open XML specs, allowed values for outline levels are positive integer values from `1` to `7`.

The value of this property could be any expression (including `null`). When such a property occurrence is met, the suffix indicates the outline level for that row. If multiple properties with the same prefix are defined for the same row, the deepest outline level is considered for that row. To end an outline row group one has to set the related outline level property with the `End` value. This is a special property value which instructs the JR engine that the current row group of that level ends on that row. This is the most simple way to perform outline row grouping at Excel export time. The `XlsFeaturesReport.jrxml` report sample shows an example of howto:

```
<group name="cityGroup">
  <expression><![CDATA[$F{city}]] ></expression>
<groupHeader>
  ...
  <groupFooter>
    <band height="20">
      <element kind="textField"...>
        <expression><![CDATA[$F{city} + " ID count: " + $V{cityGroup_COUNT}]] ></expression>
         <property name="net.sf.jasperreports.export.xls.row.outline.level.1" value="End"/>
        ...
      </element>
    </band>
  </groupFooter>
</group>
<group name="initialNameGroup">
  <expression><![CDATA[$F{name}.substring(0,1)]] ></expression>
  <groupHeader>
    <band height="10">
      <element kind="textField" ...>
        <expression><![CDATA["Names starting with letter " + $F{name}.substring(0,1)+":"]] ></expression>
        <property name="net.sf.jasperreports.export.xls.row.outline.level.1" value="Body"/>
        ...
      </element>
    </band>
  </groupHeader>
  <groupFooter>
    <band height="10">
      <element kind="textField" ...>
        <expression><![CDATA[$F{name}.substring(0,1)+ "-names ID count: " + $V{initialNameGroup_COUNT}]] ></expression>
        <property name="net.sf.jasperreports.export.xls.row.outline.level.2" value="End"/>
        ...
      </element>
    </band>
  </groupFooter>
</group>..
<detail>
  <band height="15">
    <element kind="textField" width="104" height="15">
      <expression><![CDATA[$F{state}]] ></expression>
      <property name="net.sf.jasperreports.export.xls.row.outline.level.2" value="Body"/>
      ...
    </element>
    ...
  </band>
</detail>
```

### Embedding Content From External Documents Into The Generated Document

Generated documents usually represent standalone, self-consistent pieces of information and don't require additional content from other documents in order to become meaningful. But sometimes a generated document may not uncover the whole picture. Let's imagine, for instance, a book where chapters are appended one at a time after they are completed. Or an Excel document where sheets are organized per years. Each new year comes with a new sheet to be added into the existing document. In this case there should be a possibility to concatenate our generated document with other existing ones.

A solution would be to use the batch export functionality as explained in the [Batch Export](../batchexport/README.md#batchexport) Sample. The main inconvenient here is that all documents to be concatenated should be available as [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects. But what if we have no [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects, but valid Excel documents instead? The batch export has no answers for this.

In order to solve this kind of requirements, two new exporter properties were added starting with v4.5.1:

- [`net.sf.jasperreports.export.xls.workbook.template`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsExporterConfiguration.html#PROPERTY_WORKBOOK_TEMPLATE) - one can use this property to store the location of an existing Excel workbook template. The content of the exported report will be appended to the content of the template document at export time. All settings of the template document along with its embedded binary objects will be also exported into the generated document. Templates can be loaded from Excel template files (`*.xlt`) as well as from valid Excel documents (`*.xls`).
This property may be used in the Excel exporter based on Apache POI APIs ([JRXlsExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/poi/export/JRXlsExporter.html)). There's no similar property for the [JRXlsxExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ooxml/JRXlsxExporter.html).

- [`net.sf.jasperreports.export.xls.workbook.template.keep.sheets`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsExporterConfiguration.html#PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS) - specifies whether to keep the sheets of the existing template into generated document. Sometimes only macros and/or other global settings from an existing template are needed in the generated document, without keeping the actual content of the template. If set to `false`, this property prevent the template sheets to be also exported.

This property is used in conjunction with `net.sf.jasperreports.export.xls.workbook.template` described above.

To get an idea about how these properties work, see how they are set in the `demo/samples/xlsfeatures/reports/MacroReport.jrxml` sample, at report level. Excel template files are saved in the `demo/samples/xlsfeatures/data` directory, where you could examine the `macro.xlt` template for this particular case.

After running the sample, take a look at generated `MacroReport.xls` report. You will see that the first sheet of this report was loaded from the template file. In the following sheets, you can run the macro imported from the template file by pressing `CTRL+SHIFT+T` and see how fonts and column width changed.

### Importing Macros Into The Generated XLSX Document

For the moment embedding entire content from external documents does not apply to the [JRXlsxExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ooxml/JRXlsxExporter.html). But it still keeps the possibility to import macros from an existing template into the generated document. To do so, just use the related [`net.sf.jasperreports.export.xlsx.macro.template`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsxExporterConfiguration.html#PROPERTY_MACRO_TEMPLATE) property that stores the location of an existing workbook template containing the macro object.

The binary object found in the template document will be copied into the generated document.

Macros can be loaded from Excel macro-enabled template files (`*.xltm`) as well as from valid Excel macro-enabled documents (`*.xlsm`).

See how this property was set in the `MacroReport.jrxml` sample at report level and examine the `macro.xltm` template in the data directory. Then take a look at the generated `MacroReport.xlsx` document and press `CTRL+SHIFT+T` after selecting a cell to run the macro.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/xlsfeatures` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/xlsfeatures/target/reports` directory.
