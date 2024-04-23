
# JasperReports - Horizontal Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how multi-column report templates could be filled horizontally.

### Main Features in This Sample

[Creating Horizontally-Filled, Multi-Column Reports](#horizontal)

## <a name='horizontal'>Creating</a> Horizontally-Filled, Multi-Column Reports
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to create multi-column, horizontally filled reports.

**Since:** 0.1.0

### Filling Report Templates

The report-filling process manipulates sets of data to produce high-quality documents. It represents a very important piece of JasperReports library functionality, because it completes a precompiled report template with its meaningful data. The result is a final document ready to be viewed, printed, or exported to other formats. This is the main purpose of any reporting tool.

The following things should be supplied to the report-filling process as input:

- A compiled report template
- Report parameters
- A data source

The [JasperFillManager](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperFillManager.html) class is usually used for filling a report template with data. This class has various methods that fill report templates located on disk, come from input streams, or are supplied directly as in-memory [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) objects.

The report-filling methods in the [JasperFillManager](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperFillManager.html) class always produce an output that corresponds to the type of input received. When receiving a file name for the report template, the generated report is also placed in a file on disk. When the report template is read from an input stream, the generated report is written to an output stream, etc.

The various utility methods for filling the reports may not be sufficient for a particular application. In such cases, manually loading the report template objects before passing them to the report-filling routines shoud be considered. A report template can be loaded using the [JRLoader](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/JRLoader.html) utility class. This way, one can retrieve report template properties, such as the report name, to construct the name of the resulting document and place it at the desired disk location.

The report-filling manager class covers only the most common scenarios. However, one can always customize the report-filling process using the library’s basic functionality.

### Reporting Data

JasperReports can make use of any data that the parent application might have for generating reports because it relies on two simple things: the report parameters and the report data source.

- **Report parameters** represent single-named values that are passed to the engine at report-filling time. The report parameter values are always packed in a `java.util.Map` object, having the parameter names as keys.
- **Data sources** represent a set of tabular data made of virtual rows and columns that the engine uses for iteration during the report-filling process. Normally, the engine works with an instance of the [JRDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDataSource.html) interface, from which it extracts the data when filling the report. If no data source can be retrieved using this interface, there is another possibility to get a data source: the manager class provides a set of report-filling methods that receive a `java.sql.Connection` object as a parameter. This way, data can be retrieved by executing a query on a database related to this connection parameter. More on data sources can be found at **Data Source / Query Executer** section [here](../sample.reference.html#DataSourceQueryExecuter).

### Filling order

JasperReports templates allow documents to be structured into multiple columns, like a newspaper. The column-count attribute in the `<jasperReport />` root element specifies the number of columns in the document. If this number is not specified, a single-column document is considered by default. When multiple-column report templates are used, the order used for filling those columns is important.

There are two possible column filling orders, specified by the `printOrder` attribute in the `<jasperReport />` element:

- `Vertical` - the filling process run first from top to bottom and then from left to right; the first column is entirely filled, then the second one, the third, etc.
- `Horizontal` - the filling process run first from left to right and then from top to bottom; the first row is filled in any column, then the second row, etc.

When filling report templates horizontally, dynamic text fields inside the detail section do not stretch to their entire text content, because this might cause misalignment on the horizontal axis of subsequent detail sections. The detail band actually behaves the same as the page and column footers, preserving its declared height when horizontal filling is used.

The `reports/HorizontalReport.jrxml` template in this sample contains a 3-column report filled horizontally. In order to do that, the only needed steps are:

- set the column count: `columnCount="3"`
- set the print (filling) order: `printOrder="Horizontal"`
- set the column width and column spacing, according them to the available page width and margins:

```
pageWidth="595" pageHeight="842" columnWidth="175" columnSpacing="5" leftMargin="30" rightMargin="30"
```

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/horizontal` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/horizontal/target/reports` directory.
