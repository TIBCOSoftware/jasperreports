
# JasperReports - XLS Formula Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how formulas could be introduced in reports exported to XLS format.

### Main Features in This Sample

[Exporting XLS Formulas](#xlsformula)

## <a name='xlsformula'>Exporting</a> XLS Formulas
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to generate formulas, when exporting the report to XLS.

**Since:** 3.0.0

### Why to use a formula

A formula represents one of the most useful features in Excel. It is designated to perform various calculations in order to determine the value contained in a given cell. These calculations can be very simple ones, like just adding two numbers, or with a higher degree of complexity, derived from any application's requirements specification.

A formula can be written using number or string constants, or, better, cell references. A cell reference locates the cell position within the sheet. It is formed of the cell's related column letter and row number. When a cell reference appears in a formula, Excel will perform the calculation using the data located in the referenced cell.

An interesting consequence of using cell references is that once written, a formula keeps its result up-to-date at any moment. When the content of a referenced cell changes, the calculation result will be automatically updated. This is a very important feature when cells are dynamically filled with their data.

Another useful fact is that there are several optimized built-in functions that can be called in order to perform a calculation using a large set of data categories:

- Financial
- Date & Time
- Math & Trig
- Statistical
- Text
- Logical
- etc.

**Note:** As seen above, formulas can operate with several data types. But data types cannot be mixed in a formula. For example, Date & Time functions cannot be applied on numbers, Math & Trig functions cannot be applied on text values, etc. Therefore is very important to provide the appropriate data type to a formula in order to obtain a successful result.

All these above are good reasons to use formulas in Excel as frequent as possible.

### Excel formula feature in JasperReports

The JasperReports engine provides a feature which allows using a formula as content of a text field.

The formula will affect the text field value only when the document will be exported to XLS or XLSX format, and will be neglected when exporting the document to other formats. All other exporters will take into account the value given by the `<expression />` element.

But first of all one have to ensure that cells are enabled to detect their own data type. By default all data are exported as text only. Enabling the cell type detection can be done setting the export hint property `net.sf.jasperreports.export.xls.detect.cell.type` to `true`:

```
<property name="net.sf.jasperreports.export.xls.detect.cell.type" value="true"/>
```

In JasperReports a formula can be stored using the [PROPERTY_CELL_FORMULA](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/ExcelAbstractExporter.html#PROPERTY_CELL_FORMULA) text field property.

The property name is `net.sf.jasperreports.export.xls.formula`, and usually its value is a string containing the formula expression. The expression can be a very simple one, or a more and more complex, as needed.

If the expression contains only constants or static data, then using the `<property />` element is recommended.\
If the expression contains one or more dynamic data, then the `<propertyExpression />` element should be used instead.

Although in Excel any formula expression should start with the `"="` sign, the JasperReports engine is more permissive and allows a formula expression to also start without it. Both formula expressions are considered as valid, and are further evaluated and prepared to be successfully exported to the XLS output format.

### Formula usage examples

The `XlsFormulaReport.jrxml` sample report illustrates how to use the cell formula property in several cases. It contains 2 cells, `A2` and `A3`, having simple static values (given by very simple formulas, as will be seen below), a cell `A4` containing the sum of `A2` and `A3` calculated using static data, and a cell `A5` containing the difference between `A2` and `A3` calculated using a dynamic expression.

```
A2 = 7;
A3 = 4;
A4 = SUM(7,4) = 11;
A5 = A2 - A3 = 3.
```

Below are some pieces of code showing how formulas should be written:

The text field containing the `A2` cell's value:

```
<element kind="textField" key="textField-1"...>
  <expression><![CDATA[7]] ></expression>
  <property name="net.sf.jasperreports.export.xls.formula" value="7"/>
  ...
</element>
```

Two things of interest are here:

- The `<property name="net.sf.jasperreports.export.xls.formula" value="7"/>` element, containing the most simple formula possible. The number 7 could be considered itself as a formula result.
- The `<expression class="java.lang.Integer">0</expression>` element, containing also the value of 0. This value will be exported to all other output formats but XLS, instead of the formula property.

Next, the text field containing the `A4` cell's value:

```
<element kind="textField" ...>
  <expression><![CDATA[0]] ></expression>
  <property name="net.sf.jasperreports.export.xls.formula" value="SUM(A2,A3)"/>
  ...
</element>
```

Again, the `<property name="net.sf.jasperreports.export.xls.formula" value="SUM(A2,A3)"/>` element contains only a `sum` formula, and not the effective value. When exported to XLS format, Excel will calculate the correct value and will write it in the cell.\
All other exporters will consider that the A4 value is 0, as shown in the `<expression />` tag.

Finally, the text field containing the `A5` cell's value:

```
<element kind="textField" ...>
  <expression><![CDATA[0]] ></expression>
  <propertyExpression name="net.sf.jasperreports.export.xls.formula"><![CDATA["A" + 2 + "-A" + 3]] ></propertyExpression>
  ...
</element>
```

Here was used a `<propertyExpression />` instead of simple `<property />`, and the formula expression is more complicated, and needs a `CDATA` section to be written. In a `CDATA` section could be used any dynamic expression we need, containing also variable names, parameter names, field names, and any other valid java expression.

The value of `A5` will be calculated by Excel when opening the generated Excel document, and for other output formats the 0 value will be exported.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/xlsformula` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/xlsformula/target/reports` directory.
