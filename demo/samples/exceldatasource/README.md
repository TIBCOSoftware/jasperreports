
# JasperReports - Excel Data Source Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the Excel data source implementation could be used to fill reports.

### Main Features in This Sample

[Excel Data Source](#exceldatasource)

### Secondary Features
[Data Sources](../datasource/README.md#datasources)

## <a name='exceldatasource'>Excel</a> Data Source
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill a report using data from an Excel file.

**Since:** 3.6.1

**Other Samples**\
[/demo/samples/datasource](../datasource)

## Excel Data Sources

Report filling is one of the basic operations during the report generation. After the report compilation, report data are read from the report data source, and/or calculated from report expressions, and the generated report sections are filled one by one.

Data sources are very useful when data come as a set of structured records, either extracted from a relational database, or loaded from specific files. In order to become more familiar with data source objects please consult the [Data Sources](../datasource/README.md#datasources) section.

When reporting data is stored in Microsoft Excel files (XLS or XLSX), the [ExcelDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/poi/data/ExcelDataSource.html) data source implementation can be used to read it and feed it into the report.

The Excel data source uses the [Apache POI](https://poi.apache.org/) library to load the Excel workbook and read from it. Instances of this data source can be created by supplying either an in-memory workbook object, a file, or an input stream to read the data from.\
Report-field mapping for this data source implementation is very similar to the CSV data source field-mapping explained in the [CSV Data Source](../csvdatasource/README.md#csvdatasource) sample. It works on the assumption that the workbook contains data in a tabular form (rows are records and columns contain report-field values). The main difference is that one can specify a given sheet to be used as single sheet data source using the [XLS_SHEET_SELECTION](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/AbstractXlsQueryExecuterFactory.html#XLS_SHEET_SELECTION) parameter or report property.

### Excel Data Source Example

In our example data records are stored in the `/data/MultisheetXlsDataSource.data.xls` file and the `MultisheetXlsxDataSource.data.xlsx` file respectively, depending on the Excel file format (XLS vs XLSX). It contains the same records as in the [CSV Data Source](../csvdatasource/README.md#csvdatasource) sample, but the `city` and `id` columns are separated by an empty column (ie. records contain in fact 6 fields, but the second field in each record is always empty).

There are no column headers in the Excel files. This means that column names are set independently, as shown in the `getDataSource()` method in the `/src/ExcelDataSourceApp.java` file:

```
private static ExcelDataSource getDataSource(String fileName) throws JRException
{
  ExcelDataSource ds;

  try
  {
    String[] columnNames = new String[]{"city", "id", "name", "address", "state", "date"};
    int[] columnIndexes = new int[]{0, 2, 3, 4, 5, 6};
    ds = new ExcelDataSource(JRLoader.getLocationInputStream(fileName));
    //ds.setUseFirstRowAsHeader(true);
    ds.setColumnNames(columnNames, columnIndexes);
    //uncomment the below line to see how sheet selection works
    //ds.setSheetSelection("Data Sheet 2");
  }
  catch (IOException e)
  {
    throw new JRException(e);
  }

  return ds;
}
```

Column names are the same as in the CSV example: `city, id, name, address` and `state`. But they are associated with particular column indexes: 0, 2, 3, 4, 5. The empty column's index (1) is skipped, and doing so, the empty content of the second column will be neglected.
The [ExcelDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/poi/data/ExcelDataSource.html) object prepared above is passed to the engine at fill time (see again the `/src/ExcelDataSourceApp.java` file).\
The following method uses the XLS data source:

```
public void fill1() throws JRException
{
  long start = System.currentTimeMillis();
  //Preparing parameters
  Map<String, Object> parameters = new HashMap<String, Object>();
  parameters.put("ReportTitle", "Address Report");
  parameters.put("DataFile", "MultisheetXlsDataSource.data.xls - XLS data source");
  Set<String> states = new HashSet<String>();
  states.add("Active");
  states.add("Trial");
  parameters.put("IncludedStates", states);

  JasperFillManager.fillReportToFile("target/reports/ExcelDataSourceReport.jasper", parameters, getDataSource("data/MultisheetXlsDataSource.data.xls"));
  System.err.println("Filling time : " + (System.currentTimeMillis() - start));
}
```

The following method uses the XLSX data source:

```
public void fill2() throws JRException
{
  long start = System.currentTimeMillis();
  //Preparing parameters
  Map<String, Object> parameters = new HashMap<String, Object>();
  parameters.put("ReportTitle", "Address Report");
  parameters.put("DataFile", "MultisheetXlsxDataSource.data.xlsx - XLSX data source");
  Set<String> states = new HashSet<String>();
  states.add("Active");
  states.add("Trial");
  parameters.put("IncludedStates", states);

  JasperFillManager.fillReportToFile("target/reports/ExcelDataSourceReport.jasper", parameters, getDataSource("data/MultisheetXlsxDataSource.data.xlsx"));
  System.err.println("Filling time : " + (System.currentTimeMillis() - start));
}
```

The `IncludedStates` parameter defined above is used for data filtering. Only records with `Active` or `Trial` states will be taken into account:

```
  <parameter name="IncludedStates" class="java.util.Set"/>

  ...

  <filterExpression><![CDATA[$P{IncludedStates}.contains($F{state})]]></filterExpression>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/exceldatasource` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/exceldatasource/target/reports` directory.
