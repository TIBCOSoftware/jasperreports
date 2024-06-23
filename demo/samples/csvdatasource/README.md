
# JasperReports - CSV Data Source Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the CSV data source and the CSV query executer implementations could be used to fill reports.

### Main Features in This Sample

[CSV Data Source](#csvdatasource)\
[CSV Query Executer](#csvqueryexecuter)

### Secondary Features

[Data Sources](../datasource/README.md#datasources)\
[Query Executers](../hibernate/README.md#queryexecuters)

## <a name='csvdatasource'>CSV</a> Data Source
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill a report using data from a CSV file.

**Since:** 1.2.0

**Other Samples**\
[/demo/samples/datasource](../datasource/README.md)

### CSV Data Sources

Report filling is one of the basic steps during the report generation. After the report compilation, significant report data are read from the report data source, or calculated from report expressions, and the generated [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) object is filled section by section\
Data sources are very useful when data come as a set of structured records, either extracted from a relational database, or loaded from specific files. In order to become more familiar with data source objects please consult the [Data Sources](../datasources/README.md#datasources) section.\
Sometimes data that users need to fill the report with is found in plain text files, in a certain format, such as the popular `CSV` (comma-separated value).

JasperReports provides an implementation for such a data source, by wrapping the CSV data from a text file into a [JRCsvDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRCsvDataSource.html). The CSV data source usually needs to read a file from disk, or at least from an input stream.
CSV is a file format with very few formatting rules: data rows are separated by a record delimiter (text sequence) and fields inside each row are separated by a field delimiter (character). Fields containing delimiter characters can be placed inside quotes. If fields contain quotes themselves, these are duplicated (for example, "John ""Doe""" will be displayed as John "Doe").\
The most common delimiters for CSV files are:

- comma (`,`) - as field delimiter
- newline (`\n`) - as record delimiter

Users can override these default values by calling `setFieldDelimiter(char)` and `setRecordDelimiter(String)` of the [JRCsvDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRCsvDataSource.html) class.

There are two categories of CSV files:

- files with a header record containing column names, all the other records containing field values;
- files without a header record; all records contain only field values. This is the default category.

For the files in the first category, column names are read from the first row in the CSV file. But the engine should be informed that the input file belongs to the first category. This can be done by calling the `setUseFirstRowAsHeader(true)` method of the [JRCsvDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRCsvDataSource.html).

If files belong to the second category (default), column names have to be provided separately, taking into account that the column names order should be the same as the column order in a data record. If this is not possible, the default naming convention is to use the `COLUMN_` prefix followed by the zero-based column index.

For instance, if is known that a particular column is the third column in the record (`index=2`), then one could name the corresponding field `"COLUMN_2"` and use the column data without problems.

Another problem when working with CSV files is related to data types. Handling data types for fields in CSV data sources is special since the CSV file format does not provide such information. This kind of matter is solved by trying to match each field in the data source to its corresponding report field type. For number and date/time fields, converting text values to `java.lang.Number` and `java.util.Date` values respectively, requires text parsing using format objects. This is controlled by specifying the date and number format objects to be used with the [JRCsvDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRCsvDataSource.html) instance by calling its `setDateFormat(DateFormat)` and `setNumberFormat(NumberFormat)` methods before passing it to the report-filling process.

### CSV Data Source Example

In our example data records are stored in the /data/CsvDataSource.txt file. Although the file extension is `.txt`, it contains structured data according to the CSV rules:

```
  "Dallas",47,"Janet Fuller","445 Upland Pl.","Trial"
  "Lyon",38,"Andrew Heiniger","347 College Av.","Active"
  "Dallas",43,"Susanne Smith","2 Upland Pl.","Active"
  "Berne",22,"Bill Ott","250 - 20th Ave.","Active"
  "Boston",32,"Michael Ott","339 College Av.","Trial"
  "Dallas",4,"Sylvia Ringer","365 College Av.","Active"
  "Boston",23,"Julia Heiniger","358 College Av.","Active"
  "Chicago",39,"Mary Karsen","202 College Av.","Active"
  "Dallas",40,"Susanne Miller","440 - 20th Ave.","Trial"
  "Berne",9,"James Schneider","277 Seventh Av.","Active"
  "Dallas",36,"John Steel","276 Upland Pl.","Suspended"
  "Chicago",35,"George Karsen","412 College Av.","Suspended"
  "Dallas",37,"Michael Clancy","19 Seventh Av.","Deleted"
  "Lyon",2,"Anne Miller","20 Upland Pl.","Active"
  "Dallas",0,"Laura Steel","429 Seventh Av.","Active"
  "Lyon",28,"Susanne White","74 - 20th Ave.","Deleted"
  "Paris",5,"Laura Miller","294 Seventh Av.","Active"
  "Lyon",17,"Laura Ott","443 Seventh Av.","Active"
  "New York",46,"Andrew May","172 Seventh Av.","Active"
  "New York",44,"Sylvia Ott","361 College Av.","Active"
  "Dallas",19,"Susanne Heiniger","86 - 20th Ave.","Active"
  "Chicago",11,"Julia White","412 Upland Pl.","Active"
  "Dallas",10,"Anne Fuller","135 Upland Pl.","Active"
  "New York",41,"Bill King","546 College Av.","Deleted"
  "Oslo",45,"Janet May","396 Seventh Av.","Active"
  "Paris",18,"Sylvia Fuller","158 - 20th Ave.","Trial"
  "San Francisco",48,"Robert White","549 Seventh Av.","Active"
  "Paris",25,"Sylvia Steel","269 College Av.","Suspended"
  "San Francisco",7,"James Peterson","231 Upland Pl.","Active"
  "Oslo",42,"Robert Ott","503 Seventh Av.","Trial"
```

The file has no header row with column names. Column names are set independently, as shown in the `getDataSource()` method in the `/src/CsvDataSourceApp.java` file:

```
  private static JRCsvDataSource getDataSource() throws JRException
  {
    String[] columnNames = new String[]{"city", "id", "name", "address", "state"};
    JRCsvDataSource ds = new JRCsvDataSource(JRLoader.getLocationInputStream("data/CsvDataSource.txt"));
    ds.setRecordDelimiter("\r\n");
    ds.setColumnNames(columnNames);
    return ds;
  }
```

The five column names are: `city, id, name, address` and `state`, in this particular order. Field names are identical to the column names defined here.

The [JRCsvDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRCsvDataSource.html) object prepared above is passed to the engine at fill time (see again the `/src/CsvDataSourceApp.java` file):

```
  public void fill() throws JRException
  {
    long start = System.currentTimeMillis();
    //Preparing parameters
    Map parameters = new HashMap();
    parameters.put("ReportTitle", "Address Report");
    parameters.put("DataFile", "CsvDataSource.txt - CSV data source");
    Set states = new HashSet();
    states.add("Active");
    states.add("Trial");
    parameters.put("IncludedStates", states);

    JasperFillManager.fillReportToFile("build/reports/CsvDataSourceReport.jasper", parameters, getDataSource());
    System.err.println("Filling time : " + (System.currentTimeMillis() - start));
  }
```

The `IncludedStates` parameter defined above is used for data filtering. Only records with `Active` or `Trial` states will be taken into account:

```
  <parameter name="IncludedStates" class="java.util.Set"/>

  ...

  <filterExpression><![CDATA[$P{IncludedStates}.contains($F{state})]]></filterExpression>
```

Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to demo/samples/csvdatasource within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/csvdatasource/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='csvqueryexecuter'>CSV</a> Query Executer
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to fill a report using the CSV query executer.

**Since:** 4.0.0

**Other Samples**\
[/demo/samples/ejbql](../ejbql/README.md)\
[/demo/samples/hibernate](../hibernate/README.md)\
[/demo/samples/mondrian](../mondrian/README.md)\
[/demo/samples/xmldatasource](../xmldatasource/README.md)

### Using The CSV Query Executer

The other (and highly recommended) option available for preparing a [JRCsvDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRCsvDataSource.html) is to let the JasperReports built-in CSV query executer to prepare one for us. When the query language is set to `CSV` or `csv`, the CSV query executer registered for this language will take the responsibilty to create and populate the needed CSV data source.

JasperReports ships with a default implementation of the CSV query executer, named [JRCsvQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuter.html).In order to create a valid CSV data source this query executer needs:

- To access the resource containing the CSV data. Depending on its type, the resource could be located using one of the following:
    - [`CSV_READER`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_READER) parameter, if the resource is available as java.io.Reader object
    - [`CSV_INPUT_STREAM`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_INPUT_STREAM) parameter, if the resource is available as java.io.InpuStream object
    - [`CSV_URL`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_URL) parameter, if the resource is available is located at a given URL
    - [`CSV_FILE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_FILE) parameter, if the resource is available as java.io.File object
    - [`CSV_SOURCE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_SOURCE) - a String parameter or property, representing a valid path to the requested resource
- To know the charset encoding for the CSV data, in order to create a data source with an appropriate encoding. The encoding can be set using the [`CSV_ENCODING`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_ENCODING) parameter or property.
- To know the `java.util.Locale` associated with the CSV data, in order to create a properly localized data source. The `java.util.Locale` can be set using the [`CSV_LOCALE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_LOCALE) or [`CSV_LOCALE_CODE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_LOCALE_CODE) parameter.
- To know the `java.util.TimeZone` associated with the CSV data, in order to create a properly localized data source. The `java.util.TimeZone` can be set using the [`CSV_TIMEZONE`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_TIMEZONE) or [`CSV_TIMEZONE_ID`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_TIMEZONE_ID) parameter.
- To know the number format associated with the CSV data, in order to create proper `java.lang.Number` objects from numeric fields stored in the CSV data. The number format can be set using the [`CSV_NUMBER_FORMAT`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_NUMBER_FORMAT) or [`CSV_NUMBER_PATTERN`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_NUMBER_PATTERN) parameter.
- To know the date format associated with the CSV data, in order to create proper `java.util.Date` objects from date fields stored in the CSV data. The date format can be set using the [`CSV_DATE_FORMAT`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_DATE_FORMAT) or [`CSV_DATE_PATTERN`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_DATE_PATTERN) parameter.
- To know the field delimiter character, in order to parse the CSV resource in a proper way. The field delimiter can be set using the [`CSV_FIELD_DELIMITER`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_FIELD_DELIMITER) parameter or property. The default field delimiter is a comma.
- To know the record delimiter sequence, in order to parse the CSV resource in a proper way. The record delimiter can be set using the [`CSV_RECORD_DELIMITER`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_RECORD_DELIMITER) parameter or property.
- To know the column order and column names, to be used when create the mapping between fields in the report template and fields in the data source. Column names can be set using the [`CSV_COLUMN_NAMES`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_COLUMN_NAMES) parameter or property, or the [`CSV_COLUMN_NAMES_ARRAY`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_COLUMN_NAMES_ARRAY) parameter. In various situations the first row of the CSV resource contains no data, but column names. If so, the query executer will read the column names from this row if the [`CSV_USE_FIRST_ROW_AS_HEADER`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_USE_FIRST_ROW_AS_HEADER) parameter/property is set to true. In this case column names stored in [`CSV_COLUMN_NAMES`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_COLUMN_NAMES) or [`CSV_COLUMN_NAMES_ARRAY`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRCsvQueryExecuterFactory.html#CSV_COLUMN_NAMES_ARRAY) will be neglected.

Once all this input information is loaded, the query executer will lookup for the CSV resource to read and extract data to populate the requested [JRCsvDataSource](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/data/JRCsvDataSource.html).

### The CSV Query Executer Sample

An example of CSV Query Executer is available in the `reports/CsvQueryExecuterReport.jrxml` template.

There are two properties set at report level:

```
<property name="net.sf.jasperreports.csv.column.names" value="city, id, name, address, state"/>
<property name="net.sf.jasperreports.csv.source" value="data/CsvDataSource.txt"/>
```

The first one stores the column names, the last one stores a relative path to the CSV resource file.

Further we see a report parameter containing the record delimiter sequence:

```
<parameter name="net.sf.jasperreports.csv.record.delimiter" class="java.lang.String">
  <defaultValueExpression><![CDATA["\r\n"]] ></defaultValueExpression>
</parameter>
```

Notice also that the query language is set to csv, meaning that the CSV query executer will be enabled to prepare the data source for the report:

```
<query language="csv"/>
```

Field names in the report are the same as column names set in the `net.sf.jasperreports.csv.column.names` property:

```
<field name="id" class="java.lang.Integer"/>
<field name="name" class="java.lang.String"/>
<field name="address" class="java.lang.String"/>
<field name="city" class="java.lang.String"/>
<field name="state" class="java.lang.String"/>
```

In the `src/CsvDataSourceApp.java` one can see the absence of the data source or connection info at fill time:

```
// query executer filling
{
  start = System.currentTimeMillis();
  Map parameters = new HashMap();
  parameters.put("ReportTitle", "Address Report");
  parameters.put("DataFile", "CsvDataSource.txt - CSV query executer");
  Set states = new HashSet();
  states.add("Active");
  states.add("Trial");
  parameters.put("IncludedStates", states);

  JasperFillManager.fillReportToFile("build/reports/CsvQueryExecuterReport.jasper", parameters);
  System.err.println("Report : CsvQueryExecuterReport.jasper. Filling time : " + (System.currentTimeMillis() - start));
}
```

This is because the report datasource will be provided by the query executer.
To see the result, run the sample as shown here, and take a look at `CsvQueryExecuterReport.*` documents in the `build/reports` output folder.



