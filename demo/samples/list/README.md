
# JasperReports - List Component Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>
Illustrates the usage of the list component element.

### Main Features in This Sample

[Using the Built-in List Component](#list)

## <a name='list'>Using</a> the Built-in List Component
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to render simple listings using the built-in list component and a subdataset.

**Since:** 3.5.1

**Other Samples**\
[/demo/samples/table](../table/README.md)

### The Built-in List Component - Overview

The most common way to process data from datasources/queries different from the report's datasource/query is to use subreports. But working with subreports always requires to create and compile separate report templates, one per each subreport, even for the most simple situation that can be imagined.

For instance, let's consider a subreport containing a single textfield, ready to be used in a detail band. In this case we need a separate report template containing this only textfield to be created and then maintained along with the master report, in order to generate a column of data at fill time. This particular layout is easily recognized as a list of data. So, why not use a dedicated list component instead, which combines the advantages of subreports with a specific mechanism to avoid separate report templates creation and maintenance?

This is the main purpose of the built-in list component. It also provides support for complex content, so that users may define more than single textfields as list content elements, as shown in the following section.

###The List Component Structure

A list needs only two elements in order to become functional: a dataset run and a list content.

- `datasetRun` - the dataset run used to store information about list datasource/connection/subdataset and various useful parameters.
- `contents` - this element stores the layout information for list entries. The layout will be repeated for each row in the dataset. Any kind of JasperReports element may be part of a list content.

The `printOrder` attribute in the list component specifies how to render the list elements: in a vertical sequence or in a horizontal one. The default value is `Vertical`.\
The `ignoreWidth` flag is used when the print order is `Horizontal` to indicate the list behavior when the width set for the list component is reached. Unless the flag is set to `true`, the list will break by default when the critical width is reached. By default the flag is unset.

The `contents` element can be customized with 2 attributes:

- `height` - specifies the height of each list entry and is mandatory.
- `width` - specifies the width of each list entry and is optional.

###The List Component Sample

This sample shows how to use the built-in list component for both `Vertical` and `Horizontal` printing order. In the `ListReport.jrxml` sample is configured a `Vertical` list component as follows:

```
<element kind="component" y="25" width="250" height="20">
  <component kind="list" printOrder="Vertical">
    <datasetRun  subDataset="Addresses">
      <parameter name="City">
        <expression><![CDATA[$F{City}]] ></expression>
      </parameter>
    </datasetRun>
    <contents height="14">
      <element kind="frame" width="250" height="14" style="ListRow">
        <element kind="textField" y="2" width="15" height="10" hTextAlign="Right" style="Sans_Small">
          <expression><![CDATA[$F{Id}]] ></expression>
        </element>
        <element kind="textField" positionType="Float" x="20" y="2" width="110" height="10" textAdjust="StretchHeight" style="Sans_Small">
          <expression><![CDATA[$F{FirstName} + " " + $F{LastName}]] ></expression>
        </element>
        <element kind="textField" positionType="Float" x="135" y="2" width="105" height="10" textAdjust="StretchHeight" style="Sans_Small">
          <expression><![CDATA[$F{Street}]] ></expression>
        </element>
      </element>
    </contents>
  </component>
</element>
```

A list containing the `ID, name` and `address` is generated for each `city` in the datasource and list entries are printed in the default `Vertical` order.

For the horizontally printed list, let's take a look to the `HorizontalListReport.jrxml` sample:

```
<element kind="component" y="25" width="515" height="40">
  <component kind="list" printOrder="Horizontal">
    <datasetRun subDataset="Addresses">
      <parameter name="City">
        <expression><![CDATA[$F{City}]] ></expression>
      </parameter>
    </datasetRun>
    <contents height="40" width="128">
      <element kind="frame" stretchType="ContainerHeight" x="4" width="120" height="38" style="ListCell">
        <element kind="textField" y="2" width="100" height="12" style="Sans_SmallBold">
          <expression><![CDATA["#" + $V{REPORT_COUNT} + " - " + $F{Id}]] ></expression>
        </element>
        <element kind="textField" positionType="Float" x="10" y="14" width="110" height="12" textAdjust="StretchHeight" style="Sans_Small">
          <expression><![CDATA[$F{FirstName} + " " + $F{LastName}]] ></expression>
        </element>
        <element kind="textField" positionType="Float" x="10" y="26" width="110" height="12" textAdjust="StretchHeight" style="Sans_Small">
          <expression><![CDATA[$F{Street} + ", " + $F{City}]] ></expression>
        </element>
        <box style="ListCell">
          <topPen lineWidth="0.5"/>
          <bottomPen lineWidth="0.5"/>
        </box>
      </element>
    </contents>
  </component>
</element>
```

Here each list entry is printed one after another horizontally, for each `city` in the datasource.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/list` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/list/target/reports` directory.
