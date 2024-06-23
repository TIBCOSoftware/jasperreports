
# JasperReports - Table Component Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Illustrates the usage of the table component element.

### Main Features in This Sample

[Using the Built-in Table Component](#table)

## <a name='table'>Using</a> the Built-in Table Component
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to render tabular data using the built-in table component and a subdataset.

**Since:** 3.7.2

**Other Samples**\
[/demo/samples/tabular](../tabular/README.md)\
[/demo/samples/list](../√/README.md)\
[/demo/samples/pdfa](../pdfa/README.md)

### The Built-in Table Component - Overview

Tables represent one of the most popular layout for data reporting, especially when numeric data are involved. Tables are necessary when data has to be organized in distinct rows and columns in order to be displayed. One could consider tables as groups of distinct columns sharing the same number of rows, populated with significant data. Such kind of simple tabular layout could be designed using either report elements along with their border to form a table-like structure (as shown in the [Tabular](../tabular/README.md) Sample), or a built-in list component with elements organized on a row in the list content (see the [List Component](../list/README.md) Sample).

But what if the table deals with a very complex structure, where table headers are present side by side with column headers, or column group footers are mixed together with single column footers, or particular cells span over multiple rows and/or columns? In this case tabular and list layouts require more and more elaborated work in order to put all this information together. \
A dedicated tool is needed instead. And here comes the JR built-in table component to demonstrate its utility.

### The Table Component Structure

The table component is characterized by the `kind="table"` component attribute. It is based on a very simple structure of column groups and/or single columns populated with data. Single columns (`kind="single"`) and column groups (`kind="group"`) are both extensions of a `<column>` element characterized by a series of headers and footers and a `printWhenExpression`. To be well defined, a `<column>` requires the `width` attribute to be set. By default the column width will be inherited by all table cells in the column.

In addition, columns and/or column groups may contain:

- table headers
- group headers
- column headers
- table footers
- group footers
- column footers

A column group is a recursive structure consisting in a group of columns and/or nested column groups that act together like a single column, sharing the same footers and headers.

Single columns provide their specific element: a `detailCell` to be populated with data and printed out for each iteration through the datasource, similar to elements defined in a report detail section.

Any detail cell may contain any number of report elements fitting within its bounds. It may have its own border, style and row span. But in order to be well defined, the `height` attribute is mandatory in a cell.

Finally, to make a table component fully functional, a `datasetRun` declaration is required within the table element.

### The Table Component Sample

Now let's see a table component in action. The table defined in the `TableReport.jrxml` is similar to that one defined in the [Tabular](//.tabular/README.md) sample: it contains three independent columns with column headers and footers, plus a column group with 2 columns sharing a common header, but owning also individual column headers and footers. Below is a fragment from the related code, that contains the definition of the first column in the table:

```
<element kind="component" y="50" width="555" height="100" style="Table">
  <component kind="table">
    <datasetRun subDataset="TableData">
      <dataSourceExpression><![CDATA[$P{TableDataSource}]] ></dataSourceExpression>
    </datasetRun>
    <column kind="single" width="100">
      <columnHeader height="30" rowSpan="2" style="TableHeader">
        <element kind="staticText" width="100" height="30" vTextAlign="Middle" bold="true" fontSize="12.0">
          <text><![CDATA[Header 1]] ></text>
          <box leftPadding="10">
            <pen lineColor="#000000"/>
            <bottomPen lineWidth="0.5"/>
          </box>
        </element>
      </columnHeader>
      <columnFooter height="15" style="TableFooter">
        <element kind="staticText" width="100" height="15" vTextAlign="Middle" bold="true" fontSize="12.0">
          <text><![CDATA[Total 1]] ></text>
          <box leftPadding="10">
            <pen lineColor="#000000"/>
          </box>
        </element>
      </columnFooter>
      <detailCell height="15">
        <element kind="textField" width="100" height="15" textAdjust="StretchHeight">
          <expression><![CDATA[$V{Column1}]] ></expression>
          <box leftPadding="10">
            <bottomPen lineWidth="0.5"/>
          </box>
        </element>
      </detailCell>
    </column>
    ...
  </component>
  <property name="net.sf.jasperreports.export.pptx.frame.as.table" value="true"/>
</element>
```

Notice that the column declaration contains a `columnHeader, columnFooter` and a `detailCell`.

The subsequent column grouping the report includes a `columnHeader` declaration and two separate columns. The group `columnHeader` will be shared by all columns in the group, inheriting the width of the column group. Individual column headers will be printed on a separate row, under the common group header. To keep the table rows consistency, the `rowSpan="2"` setting was required in the header declarations for the first three individual columns in the table.

There is no `columnFooter` declared at column group level in this example.

It should also be noticed how `box` elements and `style` attributes were used to configure borders and styles for different table cells.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/table ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/table/target/reports` directory.
