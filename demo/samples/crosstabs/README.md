
# JasperReports - Crosstabs Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Crosstab and subdataset sample.

### Main Features in This Sample

[Crosstabs](#crosstabs)\
[Datasets](#datasets)\
[Dynamic Styles](#dynamic_styles)
				
## <a name='crosstabs'>Crosstabs</a>
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to use the built-in crosstab element to display aggregated data.

**Since:** 1.1.0

### Crosstabs Overview

Crosstabs are very helpful tools used to aggregate data into 2-dimensional grids. Like other data aggregating elements (charts, for instance), they are organized as regular report elements, but with a complex structure and granularity.
Crosstabs can be used with any level of data (nominal, ordinal, interval, or ratio), and usually display the summarized data, contained in report variables, in the form of a dynamic table. Variables are used to display aggregate data such as sums, counts, average values.
Crosstabs are also interesting due to their flexible layout capabilities. When a crosstab does not fit on a page, then either a column or row break occurs, and the crosstab splits into several pieces. Depending on the remaining available space, the crosstab could continue on the same page, or it could overflow onto a new page.

A crosstab element is characterized by the following attributes:

- `isRepeatColumnHeaders` - indicates whether the column headers should be placed in front of columns each time a row break occurs. The default value is `true`.
- `isRepeatRowHeaders` - indicates whether the row headers should be placed in front of rows each time a column break occurs. The default value is `true`.
- `columnBreakOffset` - when a column break occurs, indicates the amount of vertical space, measured in pixels, before the subsequent crosstab piece to be placed below the previous one on the same page. The default value is `10`.
- `runDirection` - indicates whether the crosstab data should be filled from left to right (`LTR`) or from right to left (`RTL`). The default value is `LTR`.
- `ignoreWidth` - indicates whether the crosstab will stretch beyond the initial crosstab width limit and dont't generate column breaks, or will stop rendering columns within the crosstab width limit and continue with the remaining columns only after all rows have started rendering. The default value is `false`.

Crosstabs are also able to represent standalone data, not contained in variables. Any number of dynamic values can be passed from the enclosing report to the crosstab as crosstab parameters. A crosstab parameter is characterized by its name and class attributes and by the `<parameterValueExpression>` tag.
One can declare crosstab parameters either one by one in the crosstab element, or grouping them in a parameters map referenced by the `<parametersMapExpression>` tag.

### Crosstab Datasets

Like charts, crosstabs can handle by themselves collections of data. They can access data either in the report main dataset, or they can handle their own subdataset. All subdataset information required by the crosstab element is contained in the `<dataset>` child element. To faster perform calculations on data it is recommended that data come already sorted within dataset, according to the row and column groups. If this is not the case, then the crosstab internal engine should be instructed to sort data during the aggregation process. One can do this using the `isDataPreSorted` dataset attribute. If no `<dataset>` is declared, then the crosstab is using the main dataset. More on datasets can be found further in the datasets section.

### Data Grouping in Crosstab

The crosstab calculation engine aggregates data by iterating through the associated dataset records. In order to aggregate data, one need to group them first. In a crosstab rows and columns are based on specific group items, called buckets. A bucket definition should contain:

- `bucketExpression` - the expression to be evaluated in order to obtain data group items
- `comparatorExpression` - needed in the case the natural ordering of the values is not the best choice
- `orderByExpression` - indicates the value used to sort data

### Row Groups

Any number of row groups can be declared within a crosstab. The first row group will enclose the second one, the second one will enclose the third one, and so on. A row group is characterized by the following attributes:

- `name` - the name identifying the row group; this attribute is required.
- `width` - the width in pixels of the row group header
- `totalPosition` - specifies the position of the row containing total values relative to the row group. Possible values are: `Start, End, None`. The default value is `None`.
- `headerPosition - specifies the position of the row header content when multiple enclosing row groups are declared. Possible values are: `Top, Middle, Bottom, Stretch`. The default value is `Top`.

and contains the following elements:

- `<bucket>`
- `<header>`
- `<totalHeader>`

### Column Groups

Any number of column groups can be declared within a crosstab. Like row groups, outer column groups are enclosing inner column groups, in the same order as they were declared. A column group is characterized by the following attributes:

- `name` - the name identifying the column group; this attribute is required.
- `height` - the height in pixels of the column group header
- `totalPosition` - specifies the position of the column containing total values relative to the column group. Possible values are: `Start, End, None`. The default value is `None`.
- `headerPosition` - specifies the position of the column header content when multiple enclosing column groups are declared. Possible values are: `Left, Center, Right, Stretch`. The default value is `Left`.

and contains the following elements:

- `<bucket>`
- `<header>`
- `<totalHeader>`

### Measures

A measure is a result of aggregate data calculation. Its value is typically displayed in the crosstab cells. A crosstab measure behaves just like a report variable and is completely characterized by:

- `name` - the name identifying the measure.
- `class` - specifies the measure object type.
- `calculation` - like for report variables, specifies one of the supported types of calculations, except `System`. The default value is `Nothing`.
- `incrementerFactoryClass` - the name of the incrementer factory class
- `percentageOf` - indicates if the calculation should be performed as percentage of some other total value (usually the crosstab grand total).
- `percentageCalculatorClass` - the name of a custom percentage calculator class.

### Built-in Crosstab Variables. Alternate Row/Column Colors

The current value of a measure calculation is stored in a variable having the same name as the measure.

- `<Measure>_<Column Group>_ALL` - yields the total for all the entries in the column group from the same row.
- `<Measure>_<Row Group>_ALL` - yields the total for all the entries in the row group from the same column.
- `<Measure>_<Row Group>_<Column Group>_ALL` - yields the combined total corresponding to all the entries in both row and column groups
- `ROW_COUNT` - variable that counts the rows in a row group
- `COLUMN_COUNT` - variable that counts the columns in a column group

**Note:** Based on the `ROW_COUNT` and `COLUMN_COUNT` one can generate alternate colors for rows or columns in a crosstab, using conditional styles:

```
<style name="RowStyle" default="false" mode="Opaque">
  <conditionalStyle>
    <conditionExpression><![CDATA[$V{ROW_COUNT} % 2 == 0]] ></conditionExpression>
    <style backcolor="#E0E0E0"/>
  </conditionalStyle>
</style>
or:
<style name="ColumnStyle" default="false" mode="Opaque">
  <conditionalStyle>
    <conditionExpression><![CDATA[$V{COLUMN_COUNT} % 2 == 0]] ></conditionExpression>
    <style backcolor="#E0E0E0"/>
  </conditionalStyle>
</style>
```

### Crosstab Cells

A crosstab cell is a rectangular report element that can contain any kind of other report element, except subreports, charts, and crosstabs. Usually, crosstab cells can be either detail cells (when its both row/column correspond to a bucket value), or total cells (when at least the row or column corresponds to a group total value).
Depending on their position within the crosstab, or on their special meaning, crosstab cell types are described below:

- `<crosstabHeaderCell>` - this is the topmost left crosstab cell, where row headers and column headers meet.
- `<crosstabCell>` - these are the regular detail cells, with no `rowTotalGroup` or `columnTotalGroup` attribute declarations within their body. For these cells the width and height attributes are mandatory.
- `<crosstabCell>` - if at least the `rowTotalGroup` or `columnTotalGroup` declaration is present, this cell becomes a total cell. With a `rowTotalGroup` attribute, it will display a column total for that row group. With a columnTotalGroup attribute, it will display a row total for that column group.
- `<whenNoDataCell>` - if present, it specifies the content to be rendered instead, when the crosstab dataset has no data

### Crosstab JRXML Samples

This sample contains 4 JRXML files illustrating various crosstab properties and usability.

1. **OrdersReport**

This report template shows how to use the most common crosstab features in order to represent aggregate data related to a set of shipment orders.

- The crosstab is placed in the report summary.
- It works with the report main dataset.
- It uses conditional styles based on the ROW_COUNT variable.
- Because data are not presorted, the engine performs internally data sorting, taking into account the `<orderByExpression>` criteria.
- It contains a single row group, bucketing the country names, and a single column group bucketing freight values.
- Detail cells contain multiple textfields with complementary information.
- Row and column headers are placed in the start position, and total rows and columns are placed on the last position in the crosstab.
- The crosstab do not split.

2. **LateOrdersReport**

The report uses settings already present in the OrdersReport template, but due to not enough available space on the page, the crosstab splits with a row break and continues on the next page. One can see that column headers are not repeated on the next page, because the `isRepeatColumnHeaders` is set to false.

3. **ProductsReport**

- The crosstab is placed in the report detail section.
- It works with the `Customer_Quantity` subdataset.

It uses settings already present in the OrdersReport template.\
A `<whenNoDataCell>` crosstab cell is declared within. If no records present in the dataset, the No data is printed instead.

4. **ShipmentsReport**

This is the most complex report sample.

- The crosstab is placed in the report detail section.
- It works with the Country_Orders subdataset.
- It uses settings already present in the above report templates.
- Depending on the available space the crosstab splits into multiple fragments, either by inserting column breaks, or row breaks.
- Row headers are repeating when columns break. Column headers are not repeating.
- The `<crosstabHeaderCell>` is present within the crosstab.
- Multiple row groups and column groups are declared. One can see how outer row headers are enclosing inner headers, and outer header columns are enclosing the inner headers. Also is shown the outer header cells content's behavior, relative to the available space in the header cell.
- Percentage calculations are performed in total cells.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/crosstabs` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@
```

This will generate all supported document types containing the sample report in the `demo/samples/crosstabs/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='datasets'>Datasets</a>
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to make use of subdatasets for chart elements and crosstab elements.

**Since:** 1.1.0

**Other Samples**\
[/demo/samples/charts](../charts)

### Report Datasets

A dataset is an entity that intermediates data source and subreport characteristics. Datasets allow the engine to iterate through some virtual records, just as data sources do, but they also enable calculations and data grouping during this iteration using variables and groups. A dataset declaration, containing parameters, fields, variables, and groups, is similar to subreport declarations, but datasets are not related to any visual content. There are no sections or layout information at the dataset level.

The report data source, along with the parameters, fields, variables, and groups declared at the report level, represent a special dataset declaration, implicitely used in every report template, the so-called main dataset.
One can consider the main dataset responsible for iterating through the report data source records, calculating variables, filtering out records, and estimating group breaks during the report-filling process.

### Subdatasets and Dataset Runs

User-defined datasets are declared as <subDataset/> elements. To completely characterize a subdataset we need to set:

- Subdataset attributes:
    - `name`
    - `scriptletClass`
    - `resourceBundle`
    - `whenResourceMissingType`
- Subdataset elements:
    - `<property/>`
    - `<scriptlet/>`
    - `<parameter/>`
    - `<query/>`
    - `<field/>`
    - `<sortField/>`
    - `<variable/>`
    - `<filterExpression/>`
    - `<group/>`

Subdatasets are useful only in conjunction with some other complex elements, such as charts and crosstabs, that need to iterate through data not belonging to the main report data source itself, to retrieve specific data for the chart or perform data bucketing for the crosstab. Just simply declaring a dataset at the report level does not have any effect. The dataset has to be further referenced by a chart or crosstab element, in order to be used.

Anytime a dataset is referenced by another report element, a `dataset run` is instantiated. A `dataset run` supplies additional information about either the appropriate data source to be iterated, or the database connection string. It comes also with its own parameters and/or parameters map. Dataset runs are similar to subreports in the way parameters and the data source/connection are passed in. For instance:

```
<datasetRun subDataset="Customer_Quantity">
  <parameter name="CityParam">
    <expression><![CDATA[$F{City}]] ></expression>
  </parameter>
</datasetRun>
```

The `subDataset` attribute is mandatory and contains the name of the subdataset to be used during the chart or crosstab filling process. If no dataset run is specified for a chart or crosstab, the main dataset of the report is used by default.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='dynamic_styles'>Dynamic</a> Styles
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to make use of dynamic style features in to customize the appearance of report elements.

**Since:** 4.8.0

### Overview

Since report styles are declared once per report, dynamic expressions cannot be used to set their attributes. However, in some situations the element's appearance may depend on certain conditions determined only at runtime, so it needs to be set dynamically. A common example is the necessity to use different foreground/background colors in table cells, depending on the cell content.

Conditional styles based on a dynamic condition expression are very helpful in such situations, but style attributes in a conditional style still don't support expressions. To solve this problem were introduced dynamic style features in JasperReports, starting with v.4.8.0. These features come in form of properties or property expressions having their names prefixed with `net.sf.jasperreports.style`, that are set per report element. Each feature is associated with a style attribute. If such a property is found at element level, its value will override the related style attribute of the element.

The following style properties are defined in [PropertyStyleProvider](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/style/PropertyStyleProvider.html) class. They are also presented in [this page](https://jasperreports.sourceforge.net/config.reference.html), in a sequence starting with the `net.sf.jasperreports.style.backcolor` property.

Style properties are grouped in 4 categories: common, box, pen and paragraph style:

- Properties for common style attributes:

    - `net.sf.jasperreports.style.mode`
    - `net.sf.jasperreports.style.backcolor`
    - `net.sf.jasperreports.style.forecolor`
    - `net.sf.jasperreports.style.fill`
    - `net.sf.jasperreports.style.radius`
    - `net.sf.jasperreports.style.scaleImage`
    - `net.sf.jasperreports.style.hTextAlign`
    - `net.sf.jasperreports.style.vTextAlign`
    - `net.sf.jasperreports.style.hImageAlign`
    - `net.sf.jasperreports.style.vImageAlign`
    - `net.sf.jasperreports.style.rotation`
    - `net.sf.jasperreports.style.markup`
    - `net.sf.jasperreports.style.blankWhenNull`
    - `net.sf.jasperreports.style.fontName`
    - `net.sf.jasperreports.style.fontSize`
    - `net.sf.jasperreports.style.bold`
    - `net.sf.jasperreports.style.italic`
    - `net.sf.jasperreports.style.underline`
    - `net.sf.jasperreports.style.strikeThrough`
    - `net.sf.jasperreports.style.pdfFontName`
    - `net.sf.jasperreports.style.pdfEncoding`
    - `net.sf.jasperreports.style.pdfEmbedded`
    - `net.sf.jasperreports.style.pattern`

- Properties for pen style attributes:

    - `net.sf.jasperreports.style.pen.lineWidth`
    - `net.sf.jasperreports.style.pen.lineStyle`
    - `net.sf.jasperreports.style.pen.lineColor`

- Properties for box style attributes:

    - `net.sf.jasperreports.style.box.padding`
    - `net.sf.jasperreports.style.box.topPadding`
    - `net.sf.jasperreports.style.box.leftPadding`
    - `net.sf.jasperreports.style.box.rightPadding`
    - `net.sf.jasperreports.style.box.bottomPadding`
    - `net.sf.jasperreports.style.box.pen.lineWidth`
    - `net.sf.jasperreports.style.box.pen.lineStyle`
    - `net.sf.jasperreports.style.box.pen.lineColor`
    - `net.sf.jasperreports.style.box.top.pen.lineWidth`
    - `net.sf.jasperreports.style.box.top.pen.lineStyle`
    - `net.sf.jasperreports.style.box.top.pen.lineColor`
    - `net.sf.jasperreports.style.box.left.pen.lineWidth`
    - `net.sf.jasperreports.style.box.left.pen.lineStyle`
    - `net.sf.jasperreports.style.box.left.pen.lineColor`
    - `net.sf.jasperreports.style.box.right.pen.lineWidth`
    - `net.sf.jasperreports.style.box.right.pen.lineStyle`
    - `net.sf.jasperreports.style.box.right.pen.lineColor`
    - `net.sf.jasperreports.style.box.bottom.pen.lineWidth`
    - `net.sf.jasperreports.style.box.bottom.pen.lineStyle`
    - `net.sf.jasperreports.style.box.bottom.pen.lineColor`

- Properties for paragraph style attributes:

    - `net.sf.jasperreports.style.paragraph.lineSpacing`
    - `net.sf.jasperreports.style.paragraph.lineSpacingSize`
    - `net.sf.jasperreports.style.paragraph.firstLineIndent`
    - `net.sf.jasperreports.style.paragraph.leftIndent`
    - `net.sf.jasperreports.style.paragraph.rightIndent`
    - `net.sf.jasperreports.style.paragraph.spacingBefore`
    - `net.sf.jasperreports.style.paragraph.spacingAfter`
    - `net.sf.jasperreports.style.paragraph.tabStopWidth`

All the above properties are exclusively used to override features of the element's style, and not to define a new style in the report. As can be seen in the common style attributes category, properties like `net.sf.jasperreports.style.name` and `net.sf.jasperreports.style.style` are not supported. They have no meaning and no effects in this implementation.
This is because styles cannot be defined at element level, and dynamic style features are intended only for overriding an existing style attribute for that element. For dynamic style definitions one can use conditional styles in the report.

Following is an example of dynamic style features declared for a given element in JRXML:

```
<element kind="textField" x="0" y="0" width="0" height="10">
  ...
  <property name="net.sf.jasperreports.style.bold" value="true"/>
  <propertyExpression name="net.sf.jasperreports.style.forecolor"><![CDATA[$F{Forecolor}]] ></propertyExpression>
  <propertyExpression name="net.sf.jasperreports.style.backcolor"><![CDATA[$P{Backcolor}]] ></propertyExpression>
  <propertyExpression name="net.sf.jasperreports.style.fontName"><![CDATA[$V{FontName}]] ></propertyExpression>
</element>
```

### Dynamic Styles Example

The `OrdersReport.jrxml` file in the crosstabs sample uses a dynamic style feature in order to allow different background color tones in crosstab cells, based on the value of the cell content relative to the grand total. For elements in detail cells the backcolor style feature was set using the following property expression:

```
<element kind="textField" mode="Opaque" width="60" height="10" hTextAlign="Center" vTextAlign="Middle" style="OrderCountStyle">
  <expression><![CDATA[$V{OrderCount}]] ></expression>
  <propertyExpression name="net.sf.jasperreports.style.backcolor">
    <![CDATA["#" + net.sf.jasperreports.engine.util.JRColorUtil.getColorHexa(new java.awt.Color(
                  255, 
                  255 - Math.min(255,(int)(255 * 20 * $V{OrderCount} / $V{OrderCount_CountryRow_FreightColumn_ALL})), 
                  255 - Math.min(255,(int)(255 * 20 * $V{OrderCount} / $V{OrderCount_CountryRow_FreightColumn_ALL}))))]] ></propertyExpression>
</element>
```

After running the sample we'll see the red background of crosstab cells colored more or less intensely depending on the value of the `OrderCount` measure as percent of `OrderCount_CountryRow_FreightColumn_ALL` quantity. Cells with `OrderCount = 0` display a white background.


