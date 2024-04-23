
# JasperReports - Tabular Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how a table structure could be created using simple text elements and their border.

### Main Features in This Sample

[Creating Table-like Structures](#tabular)\
[Using PDF tags to create tables in generated PDF documents](../tabular/README.md#taggedpdf)

## <a name='tabular'>Creating</a> Table-like Structures
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to create tables using text elements and their border.

**Since:** 0.6.3

**Other Samples**\
[/demo/samples/table](../table/README.md)\
[/demo/samples/list](../√/README.md)\
[/demo/samples/pdfa](../pdfa/README.md)

### Table-like Structures - Overview

Whenever data has to be organized in distinct rows and columns, a table structure should be created, or at least emulated, in order to display these data. Before the built-in [table component](../table/README.md) being included in the JasperReports library, the most common way to represent tabular data was to create table-like structures based on particular styles and layout configuration of the report elements. For instance, if elements in a detail band are configured to share the same y-coordinate and the same height, they will result as perfectly aligned in a row at runtime. This row layout is then repeated for each iteration through the datasource, leading to a table-like structure organized in rows and columns.

To obtain excellent results with table-like structures, it's recommended to carefully set the related layout attributes and styles, in order to get elements perfectly aligned, horizontally and vertically. Styles and box elements may also be used to individualize cell borders, backgrounds, forecolors, cell paddings, alternative row colors, etc.
Attributes that should be carefully handled are:

- `x`
- `y`
- `width`
- `height`
- `style`

### The Tabular Sample

The `TabularReport.jrxml` in the sample shows how to create a table using the page header section to generate the table header, the detail section to generate the table rows, and the column footer section to generate column footers. If the JRXML file is open with a report design viewer (such as iReport or JasperStudio), one can see the elements being perfectly aligned, both horizontally and vertically.

Let's take a closer look at how the last three elements in the page header section were configured:

```
<pageHeader height="30">
  ...
  <element kind="staticText" mode="Opaque" x="300" width="255" height="15" backcolor="#808080" hTextAlign="Center" bold="true" fontSize="12.0">
    <text><![CDATA[Header 4]] ></text>
    <property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
    <property name="net.sf.jasperreports.export.pdf.tag.colspan" value="2"/>
    <property name="net.sf.jasperreports.export.pdf.tag.tr" value="end"/>
    <box>
      <pen lineWidth="0.5" lineColor="#000000"/>
      <topPen lineWidth="1.0" lineColor="#FF0000"/>
      <rightPen lineWidth="1.0" lineColor="#FF0000"/>
    </box>
  </element>
  <element kind="staticText" mode="Opaque" x="300" y="15" width="155" height="15" backcolor="#808080" bold="true">
    <text><![CDATA[Header 4.1]] ></text>
    <property name="net.sf.jasperreports.export.pdf.tag.tr" value="start"/>
    <property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
    <box leftPadding="10">
      <pen lineColor="#000000"/>
      <topPen lineWidth="0.5"/>
      <leftPen lineWidth="0.5"/>
      <bottomPen lineWidth="0.5"/>
    </box>
  </element>
  <element kind="staticText" mode="Opaque" x="455" y="15" width="100" height="15" backcolor="#808080" hTextAlign="Right" bold="true">
    <text><![CDATA[Header 4.2]] ></text>
    <property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
    <property name="net.sf.jasperreports.export.pdf.tag.tr" value="end"/>
    <box rightPadding="10">
      <pen lineColor="#000000"/>
      <topPen lineWidth="0.5"/>
      <leftPen lineWidth="0.5"/>
      <bottomPen lineWidth="0.5"/>
      <rightPen lineWidth="1.0" lineColor="#FF0000"/>
    </box>
  </element>
</pageHeader>
```

Notice that the `Header 4` element is only 15 pixels high, while the band height is 30. It means that there is left some unused space below this static text. It starts from `y=0, x=300` and is `255` pixels wide.

The next element labeled `Header 4.1` starts from `y=15` and is `15` pixels high. Vertically, it fits perfectly in the unused space below the `Header 4` element. But its `width=155` pixels is not enough to fill the entire available horizontal space. So, we still have unused space in this layout design.

This space will be completely covered by the last element labeled `Header 4.2`. It starts from `y=15, x=455`, is `15` pixels high and `100` pixels wide, therefore it fits perfectly in the available space.

This way was emulated a table header with nested column headers. With a little bit more accurate design, one could emulate column group headers and other header structures with increased complexity.

It should also be noticed how the box elements were designed, piece by piece, to configure borders for table and header cells. The background color for all header cells is set to `grey`.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/tabular` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/tabular/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='taggedpdf'>Using</a> PDF tags to create tables in generated PDF documents
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to create tables in generated PDF documents using PDF tags.

**Since:** 3.1.2

### Section 508 Compliance

A PDF document can be structured in order to enable accessibility tools in PDF readers. Based on specific hidden tags in their structure, documents can be read aloud to people with disabilities.

Tagged PDF files comply with the requirements of the Section 508 of the U.S. Rehabilitation Act (see http://www.section508.gov).

JasperReports supports Section 508 compliant PDF documents based on the PDF tags feature introduced in v3.1.2. According to a set of built-in configuration properties, PDF tags required for accessibility are added to the structure of generated documents.

### Tagged PDF Output

To enable accessibility in generated documents a set of export parameters are available in JasperReports along with their related exporter hint properties:

- Tagged Documents
    - [`isTagged()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#isTagged()) - Exporter configuration setting used to turn on the creation of hidden structural tags. By default the feature is turned off.
    - `net.sf.jasperreports.export.pdf.tagged` - Configuration property used as default for the `isTagged()` setting above. It can be set at global or report level.

- Tag Language
    - [`getTagLanguage()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#isTagged()) - Export configuration setting used to specify the language to be used in the language tag of the generated PDF.
    - `net.sf.jasperreports.export.pdf.tag.language` - Configuration property used as default for the `getTagLanguage()` setting above. It can be set at global or report level.

### Accessibility Tags

All properties in this category are set at element level and share the same set of possible values (excepting the column span and row span properties, which accept numeric values only):

- `start` - if this value is set, the element will be marked with a starting PDF tag corresponding to the property.
- `full` - if this value is set, the element will be wrapped in a full PDF tag corresponding to the property.
- `end `- if this value is set, the element will be marked with an ending PDF tag corresponding to the property.

Available properties related to PDF accessibility tags are enumerated below:

- `net.sf.jasperreports.export.pdf.tag.h1` - Property used to mark a text field as a level 1 heading.
- `net.sf.jasperreports.export.pdf.tag.h2` - Property used to mark a text field as a level 2 heading.
- `net.sf.jasperreports.export.pdf.tag.h3` - Property used to mark a text field as a level 3 heading.
- `net.sf.jasperreports.export.pdf.tag.table` - Property used to indicate where the table starts and where it ends in the report template.
- `net.sf.jasperreports.export.pdf.tag.th` - Property used to mark heading cells in a table.
- `net.sf.jasperreports.export.pdf.tag.tr` - Property used to indicate where a table row starts and where it ends in the report template.
- `net.sf.jasperreports.export.pdf.tag.td` - Property used to mark detail cells in a table.
- `net.sf.jasperreports.export.pdf.tag.colspan` - Property used to indicate the column span of a table cell. Allowed values are positive integers.
- `net.sf.jasperreports.export.pdf.tag.rowspan` - Property used to indicate the row span of a table cell. Allowed values are positive integers.

### Alternate Text for Images and Reading Order

Accessible documents must provide also an alternate text for images and an appropriate reading order. In JasperReports the alternate text for images is specified in the `<hyperlinkTooltipExpression/>` of the image element.

The reading order is processed using the Z-order of elements as present in the report template. No additional processing is necessary here.

To find out more about tagged PDF output see the "Section 508 Compliance" chapter in the JasperReports Ultimate Guide.

Tags in Tabular Sample

In the tabular sample one can notice the presence of all properties enumerated above, with their values depending on the element's position within the table. Properties are listed below in the same order they are defined in the report template:

```
<property name="net.sf.jasperreports.export.pdf.tagged" value="true"/>
<property name="net.sf.jasperreports.export.pdf.tag.language" value="EN-US"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.h1" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.table" value="start"/>
<property name="net.sf.jasperreports.export.pdf.tag.tr" value="start"/>
<property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
<property name="net.sf.jasperreports.export.pdf.tag.rowspan" value="2"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
<property name="net.sf.jasperreports.export.pdf.tag.rowspan" value="2"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
<property name="net.sf.jasperreports.export.pdf.tag.rowspan" value="2"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
<property name="net.sf.jasperreports.export.pdf.tag.colspan" value="2"/>
<property name="net.sf.jasperreports.export.pdf.tag.tr" value="end"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.tr" value="start"/>
<property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.th" value="full"/>
<property name="net.sf.jasperreports.export.pdf.tag.tr" value="end"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.tr" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.tr" value="start"/>
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
...
<property name="net.sf.jasperreports.export.pdf.tag.td" value="full"/>
<property name="net.sf.jasperreports.export.pdf.tag.tr" value="end"/>
<property name="net.sf.jasperreports.export.pdf.tag.table" value="end"/>
```

The generated PDF document is Section 508 compliant and can be read aloud by dedicated reader tools.
