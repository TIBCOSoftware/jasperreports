
# JasperReports - Excel Data Adapter Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the Excel data adapters can be used to fill reports.

### Main Features in This Sample

[Excel Data Adapter](#exceldataadapter)

### Secondary Features
[Excel Data Source](../exceldatasource/README.md#exceldatasource)

## <a name='exceldataadapter'>Excel</a> Data Adapter
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill a report using data from an Excel file.

**Since:** 

**Other Samples**\
[/demo/samples/exceldatasource](../exceldatasource)

### The Excel Data Source

The next step after the report compilation is the report filling. During this process required data is read from the report data source and/or calculated from report expressions, while report sections are filled one by one.

Data sources are used when data come as a set of structured records, either extracted from a relational database, or loaded from specific files. In order to become more familiar with data source objects please consult the [Data Sources](../datasource) section.

When reporting data is stored in Microsoft Excel files (either XLSX or XLS format), the [ExcelDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/ExcelDataSource.html) implementation can be used to read it and feed it into the report. Excel files are parsed according to their internal structure and resulting data are returned in the form of one or multiple data source records. In order to obtain such records, the [ExcelDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/ExcelDataSource.html) needs to know:

- the object that stores the Excel data. This may be:
    - an Excel data file saved on the disk. Can be set using the [EXCEL_FILE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_FILE) parameter.
    - a `java.io.InputStream` object. Can be set using the [EXCEL_INPUT_STREAM](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_INPUT_STREAM) parameter.
    - an in-memory Excel workbook object. Can be set using the [EXCEL_WORKBOOK](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_WORKBOOK) parameter.
    - a path to the location of an Excel data file. Can be set using the [EXCEL_SOURCE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_SOURCE) parameter or report property.
- the internal format of the Excel document. Can be set using the [EXCEL_FORMAT](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_FORMAT) parameter or report property. Allowed values for the Excel format property are enumerated in the [ExcelFormatEnum](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/data/excel/ExcelFormatEnum.html) class:
    - `xls` - for Excel 2003 and older documents
    - `xlsx` - for Excel 2007 and newer documents
    - `autodetect` - this is the default value. If autodetect is set, the engine will try to detect the format based on the internal structure of the document
- the number pattern of numeric columns. Can be set using either [EXCEL_NUMBER_FORMAT](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_NUMBER_FORMAT) parameter or [EXCEL_NUMBER_PATTERN](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_NUMBER_PATTERN) parameter/report property.
- the date pattern of date columns. Can be set using either [EXCEL_DATE_FORMAT](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_DATE_FORMAT) parameter or [EXCEL_DATE_PATTERN](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_DATE_PATTERN) parameter/report property.
- whether the first row in the data file should be used to provide the column names. Can be set using the [EXCEL_USE_FIRST_ROW_AS_HEADER](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_USE_FIRST_ROW_AS_HEADER) parameter.
- if column names are not specified in the first row of the data file, they have to be specified along with their column indexes. Report-field mapping for the data source implementation is very similar to the CSV data source field-mapping explained in the [CSV Data Source](../csvdatasource/README.md) sample. It works on the assumption that the workbook contains data in a tabular form (rows are records and columns contain report-field values).
    - Column names can be set using either [EXCEL_COLUMN_NAMES_ARRAY](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_COLUMN_NAMES_ARRAY) parameter or [EXCEL_COLUMN_NAMES](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_COLUMN_NAMES) parameter/report property.
    - Column indexes can be set using either [EXCEL_COLUMN_INDEXES_ARRAY](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_COLUMN_INDEXES_ARRAY) parameter or [EXCEL_COLUMN_INDEXES](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_COLUMN_INDEXES) parameter/report property.
- optionally, the name of the sheet in the Excel document to be used as single sheet data source. If not provided, data records will be collected from all sheets in the document. The Excel sheet can be specified using the [XLS_SHEET_SELECTION](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#XLS_SHEET_SELECTION) parameter or report property.
- the `java.util.Locale` to be used when reading data. Can be set using either [EXCEL_LOCALE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_LOCALE) parameter or [EXCEL_LOCALE_CODE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_LOCALE_CODE) parameter/report property.
- the `java.util.TimeZone` to be used when reading data. Can be set using either [EXCEL_TIMEZONE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_TIMEZONE) parameter or [EXCEL_TIMEZONE_ID](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuterFactory.html#EXCEL_TIMEZONE_ID) parameter/report property.

### The Excel Data Adapter

The built-in Excel data adapter tool can be used to create and populate an Excel data source. Necessary parameters or properties can be set using the existing [ExcelDataAdapter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/data/excel/ExcelDataAdapter.html) implementation:

```
public interface ExcelDataAdapter extends XlsDataAdapter
{
  public ExcelFormatEnum getFormat();
  public void setFormat(ExcelFormatEnum format);
}
```

Settings inherited from the XlsDataAdapter are presented below:

```
public interface XlsDataAdapter extends DataAdapter
{
  public String getDatePattern();
  public String getNumberPattern();
  public String getFileName();
  public void setFileName(String filename);
  public boolean isUseFirstRowAsHeader();
  public List<String> getColumnNames();
  public List<Integer> getColumnIndexes();
  public void setColumnNames(List<String> columnNames);
  public void setColumnIndexes(List<Integer> columnIndexes);
  public void setUseFirstRowAsHeader(boolean useFirstRowAsHeader);
  public void setDatePattern(String datePattern);
  public void setNumberPattern(String numberPattern);
  public boolean isQueryExecuterMode();
  public void setQueryExecuterMode(boolean queryExecuterMode);
  public String getSheetSelection();
  public void setSheetSelection(String sheetSelection);
}
```

All operations required to create and populate the Excel data source are performed in the [ExcelDataAdapterService](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/data/excel/ExcelDataAdapterService.html) class.
The `isQueryExecuterMode()` setting specifies whether the built-in [ExcelQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/ExcelQueryExecuter.html) class will be used to prepare the data source. If not set, the data source will be created and populated by the [ExcelDataAdapterService](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/data/excel/ExcelDataAdapterService.html).

### The Excel Data Adapter Sample

Now we'll see how to configure and use the built-in Excel data adapter in order to obtain a valid data source.

There are 4 distinct configurations in this sample, all of them saved in the data folder:

- `ExcelXlsDataAdapter.jrdax` - reads data from a XLS data file (see `data/XlsDataSource.data.xls` Excel file) and works in direct data source mode
- `ExcelXlsQeDataAdapter.jrdax` - reads data from the same XLS data file, but works in query executer mode
- `ExcelXlsxDataAdapter.jrdax` - reads data from a XLSX data file (see `data/XlsxDataSource.data.xlsx` Excel file) and works in direct data source mode
- `ExcelXlsxQeDataAdapter.jrdax` - reads data from the same XLSX data file, but works in query executer mode

Below is the content of the `ExcelXlsDataAdapter.jrdax`:

```
<?xml version="1.0" encoding="UTF-8"?>
<excelDataAdapter class="net.sf.jasperreports.data.excel.ExcelDataAdapterImpl">
  <name>excel</name>
  <fileName>/data/XlsDataSource.data.xls</fileName>
  <useFirstRowAsHeader>false</useFirstRowAsHeader>
  <queryExecuterMode>false</queryExecuterMode>
  <numberPattern>#,##0</numberPattern>
  <datePattern>yyyy-MM-dd</datePattern>
  <columnNames>city</columnNames>
  <columnNames>id</columnNames>
  <columnNames>name</columnNames>
  <columnNames>address</columnNames>
  <columnNames>state</columnNames>
  <columnNames>date</columnNames>
  <columnIndexes>0</columnIndexes>
  <columnIndexes>2</columnIndexes>
  <columnIndexes>3</columnIndexes>
  <columnIndexes>4</columnIndexes>
  <columnIndexes>5</columnIndexes>
  <columnIndexes>6</columnIndexes>
  <sheetSelection>xlsdatasource2</sheetSelection>
  <format>xls</format>
</excelDataAdapter>
```

One can see there are 6 columns with their 0-based indexes (0,2,3,4,5,6) and appropriate names: (`city, id, name, address, state, date`). The second column (ie the one having the index 1) is an empty column, so it can be omitted. Dates are represented using the `"yyyy-MM-dd"` date pattern and numbers are represented as integer values with the `"#,##0"` number pattern. The first row in the data file may not be used as column names holder and the data adapter doesn't work in query executer mode.

Data are read from a single sheet named `xlsdatasource2`. This is the second sheet in the data file.

The other 3 data adapter configurations are set in a similar way, with differences regarding the data file, the query executer mode and the sheet selection.

For each data adapter there is a JRXML file to be compiled, filled and exported to various output formats:

- `reports/ExcelXlsDataAdapterReport.jrxml` - uses `ExcelXlsDataAdapter.jrdax`, that works in direct data source mode
- `reports/ExcelXlsQeDataAdapterReport.jrxml` - uses `ExcelXlsQeDataAdapter.jrdax`, that works in query executer mode
- `reports/ExcelXlsxDataAdapterReport.jrxml` - uses `ExcelXlsxDataAdapter.jrdax`, that works in direct data source mode
- `reports/ExcelXlsxQeDataAdapterReport.jrxml` - uses `ExcelXlsxQeDataAdapter.jrdax`, that works in query executer mode

Settings for data adapter are very simple in JRXML files. We need to set the `net.sf.jasperreports.data.adapter` report property to point to the appropriate data adapter configuration. We also have to define the fields to be picked up from the data source.\
For instance, in the `ExcelXlsDataAdapterReport.jrxml` we have the following settings:

```
<property name="net.sf.jasperreports.data.adapter" value="/data/ExcelXlsDataAdapter.jrdax"/>
...
<field name="id" class="java.lang.Integer"/>
<field name="name" class="java.lang.String"/>
<field name="address" class="java.lang.String"/>
<field name="city" class="java.lang.String"/>
<field name="state" class="java.lang.String"/>
<field name="date" class="java.util.Date"/>

<sortField name="city" order="Descending"/>
<sortField name="name"/>
...
<filterExpression><![CDATA[$P{IncludedStates}.contains($F{state})]] ></filterExpression>
```

We can notice that data can be sorted as well as filtered in such a dataset.

Similar settings can be found in the `ExcelXlsxDataAdapterReport.jrxml` file.

If the data adapter is designed to work in query executer mode (ie `<queryExecuterMode>true</queryExecuterMode>`), we need to add a query string in the JRXML file. The query language should be set to `"excel"` (or `"EXCEL"`). An example can be seen in the `ExcelXlsQeDataAdapterReport.jrxml` file:

```
<property name="net.sf.jasperreports.data.adapter" value="/data/ExcelXlsQeDataAdapter.jrdax"/>
...
<query language="excel">
  <![CDATA[]] >
</query>
<field name="id" class="java.lang.Integer"/>
<field name="name" class="java.lang.String"/>
<field name="address" class="java.lang.String"/>
<field name="city" class="java.lang.String"/>
<field name="state" class="java.lang.String"/>
<field name="date" class="java.util.Date"/>

<sortField name="city" order="Descending"/>
<sortField name="name"/>
...
<filterExpression><![CDATA[$P{IncludedStates}.contains($F{state})]] ></filterExpression>
```

Similar settings can be found in the `ExcelXlsxQeDataAdapterReport.jrxml` file.

After having all the necessary input prepared as shown above, we can now fill the report. See the `fill()` method in the `src/ExcelDataAdapterApp.java` class:

```
public void fill() throws JRException
{
  long start = System.currentTimeMillis();
  //Preparing parameters
  Map<String, Object> parameters = new HashMap<String, Object>();
  parameters.put("ReportTitle", "Address Report");
  Set<String> states = new HashSet<String>();
  states.add("Active");
  states.add("Trial");
  parameters.put("IncludedStates", states);

  //query executer mode
  parameters.put("DataFile", "XLS query executer mode for Excel data adapter");
  JasperFillManager.fillReportToFile("target/reports/ExcelXlsQeDataAdapterReport.jasper", new HashMap<String, Object>(parameters));
  parameters.put("DataFile", "XLSX query executer mode for Excel data adapter");
  JasperFillManager.fillReportToFile("target/reports/ExcelXlsxQeDataAdapterReport.jasper", new HashMap<String, Object>(parameters));

  JasperReport jasperReport = (JasperReport)JRLoader.loadObjectFromFile("target/reports/ExcelXlsQeDataAdapterReport.jasper");
  jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsQeDataAdapter.jrdax");
  JasperFillManager.fillReportToFile(jasperReport, "target/reports/XlsQeDataAdapterReport.jrprint", new HashMap<String, Object>(parameters));

  jasperReport = (JasperReport)JRLoader.loadObjectFromFile("target/reports/ExcelXlsxQeDataAdapterReport.jasper");
  jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsxQeDataAdapter.jrdax");
  JasperFillManager.fillReportToFile(jasperReport, "target/reports/XlsxQeDataAdapterReport.jrprint", new HashMap<String, Object>(parameters));

  //data source mode
  parameters.put("DataFile", "Excel data adapter for XLS data source");
  JasperFillManager.fillReportToFile("target/reports/ExcelXlsDataAdapterReport.jasper", new HashMap<String, Object>(parameters));
  parameters.put("DataFile", "Excel data adapter for XLSX data source");
  JasperFillManager.fillReportToFile("target/reports/ExcelXlsxDataAdapterReport.jasper", new HashMap<String, Object>(parameters));

  jasperReport = (JasperReport)JRLoader.loadObjectFromFile("target/reports/ExcelXlsDataAdapterReport.jasper");
  jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsDataAdapter.jrdax");
  JasperFillManager.fillReportToFile(jasperReport, "target/reports/XlsDataAdapterReport.jrprint", new HashMap<String, Object>(parameters));

  jasperReport = (JasperReport)JRLoader.loadObjectFromFile("target/reports/ExcelXlsxDataAdapterReport.jasper");
  jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsxDataAdapter.jrdax");
  JasperFillManager.fillReportToFile(jasperReport, "target/reports/XlsxDataAdapterReport.jrprint", new HashMap<String, Object>(parameters));

  System.err.println("Filling time : " + (System.currentTimeMillis() - start));
}
```

One can notice that there are no data source or connection parameters for the `fillReportToFile(...)` method. The data adapter will prepare for us the needed data source before filling the report.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/exceldataadapter` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/exceldataadapter/target/reports` directory.
