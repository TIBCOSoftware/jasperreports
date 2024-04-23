
# JasperReports - Date Range Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows different ways the DATERANGE function can be used to generate a complex document.

### Main Features in This Sample

[Date Range](#daterange)
				
## <a name='top'>Date</a> Range
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows different ways the DATERANGE function can be used to generate a complex document.

**Since:** 5.0.4

**Other Samples**\
[/demo/samples/query](../query)

### Working with Date Ranges

A date range is a time period completely characterized by its start date and end date. Such time periods are handled in JasperReports using [DateRange](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/types/date/DateRange.html) objects that expose the following methods:

- `public Date getStart()` - to retrieve a `java.util.Date` object, representing the start date
- `public Date getEnd()` - to retrieve a `java.util.Date` object, representing the end date

There are 2 kind of DateRange objects:

- single date object - a date range where the start date is the same as the end date
- relative date range - a date range calculated relatively to the current date, where the start date and the end date are different and depend on the time unit, that can be set as day, week, month, etc.

The built-in `DATERANGE()` custom function is implemented to generate [DateRange](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/types/date/DateRange.html) objects within JRXML reports. It can be used in expressions and SQL function clauses (using the `$X{}` syntax). The `DATERANGE()` function accepts either a `String` parameter or a `java.util.Date`. If called with a `java.util.Date` parameter, a single date object object will be generated. In case of a `String` parameter, the result depends on how the `String` parameter is constructed. If the `String` represents a valid date (for instance "2000-01-23") a single date object is returned. If the `String` parameter is in the form of `<keyword> <+|-> <number>`, a relative date range object will be delivered. In this expression the `<keyword>` represents the time unit and may have one of the following values:

- `DAY` - the date range will be calculated based on days relative to the current date
- `WEEK` - the date range will be calculated based on weeks relative to the current date
- `MONTH` - the date range will be calculated based on months relative to the current date
- `QUARTER` - the date range will be calculated based on quarters relative to the current date
- `SEMI` - the date range will be calculated based on semesters relative to the current date
- `YEAR` - the date range will be calculated based on years relative to the current date

Optionally, the `<keyword>` may be followed by a `+` or a `-` sign and an integer number, representing the amount of time units. Some examples are in the following table:

| **Expression** | **Generated DateRange object** |
|:----------|:----------|
| `DATERANGE("2000-01-23")` | The day of January 23, 2000, starting at 00:00:00, ending at the same time |
| `DATERANGE("DAY")` | All the current day, starting at 00:00:00, ending at 23:59:59 |
| `DATERANGE("DAY-1")` | Day of yesterday, starting at 00:00:00, ending at 23:59:59 |
| `DATERANGE("DAY+1")` | Day of tomorrow, starting at 00:00:00, ending at 23:59:59 |
| `DATERANGE("WEEK")`	| This week, starting* on Monday 00:00:00, ending on Sunday 23:59:59 |
| `DATERANGE("WEEK+1")` | Next week, starting* on Monday 00:00:00, ending on Sunday 23:59:59 |
| `DATERANGE("WEEK-2")` | 2 weeks ago, starting* on Monday 00:00:00, ending on Sunday 23:59:59 |
| `DATERANGE("MONTH") `| 	This month, starting on the first day of month at 00:00:00, ending on the last day of month at 23:59:59 |
| `DATERANGE("MONTH-6")` | 6 months ago, starting on the first day of month at 00:00:00, ending on the last day of month at 23:59:59 |
| `DATERANGE("MONTH+6")` | 6 months later from now, starting on the first day of month at 00:00:00, ending on the last day of month at 23:59:59 |
| `DATERANGE("QUARTER")` | this quarter, starting on the first day of quarter at 00:00:00, ending on the last day of quarter at 23:59:59 |
| `DATERANGE("QUARTER-10")` | 10 quarters ago, starting on the first day of quarter at 00:00:00, ending on the last day of quarter at 23:59:59 |
| `DATERANGE("QUARTER+1")` | 	Next quarter, starting on the first day of quarter at 00:00:00, ending on the last day of quarter at 23:59:59 |
| `DATERANGE("SEMI")` | this semester, starting on the first day of semester at 00:00:00, ending on the last day of semester at 23:59:59 |
| `DATERANGE("SEMI-3")` | 3 semesters ago, starting on the first day of semester at 00:00:00, ending on the last day of semester at 23:59:59 |
| `DATERANGE("SEMI+3")` | 3 semesters later from now, starting on the first day of semester at 00:00:00, ending on the last day of semester at 23:59:59 |
| `DATERANGE("YEAR")` | this year, starting on January 1, 00:00:00, ending on December 31, 23:59:59 |
| `DATERANGE("YEAR-1")` | last year, starting on January 1, 00:00:00, ending on December 31, 23:59:59 |
| `DATERANGE("YEAR+10")` | 10 years later from now, starting on January 1, 00:00:00, ending on December 31, 23:59:59 |

**Note:** When working with the `WEEK` time unit, we have to pay attention to the first day of week. In some cases the week may be considered to start on Monday, while in some other cases the first day of week may be considered Sunday. In order to specify the default behavior, we need to set the `net.sf.jasperreports.week.start.day` property on the report context. Possible values for this property are:

- `2` - the week starts on Monday (this is the default value)
- `1` - the week starts on Sunday

The `DATERANGE()` function can be used also as parameter in SQL query clauses listed in the table below:

| **SQL query clause** | **`$X{}` expression meaning** |
|:----------|:----------|
| `$X{EQUAL, column, parameter}`	| column value >= parameter.getStart() AND column value <= parameter.getEnd() |
| `$X{LESS, column, parameter}` | column value < parameter.getStart() |
| `$X{LESS], column, parameter}	` | column value <= parameter.getEnd() |
| `$X{GREATER, column, parameter}` | column value > parameter.getEnd() |
| `$X{[GREATER, column, parameter}` | column value >= parameter.getStart() |
| `$X{BETWEEN, column, parameterStart, parameterEnd}` | 	column value >= parameterStart.getStart() AND column value <= parameterEnd.getEnd() |

The `DATERANGE()` function uses the report `Locale` and `Timezone` to evaluate date ranges.

### The Date Range Sample

This sample illustrates how the `DATERANGE()` function can be used in expressions and SQL clause functions. First are shown some relative date ranges with their start/end moments. Following is a text field that contains a `DATERANGE()` function to retrieve a relative date range start:

```
<element kind="textField" mode="Transparent" ... backcolor="#EEEEEE" vTextAlign="Middle" pattern="EEE, MMM d, yyyy  HH:mm:ss">
  <expression><![CDATA[DATERANGE("DAY+3").getStart()]] ></expression>
  <property name="net.sf.jasperreports.export.xls.pattern" value="ddd, MMM d, yyyy  HH:mm:ss"/>
</element>
```

In the next report section are shown some examples of how to use the `DATERANGE()` function to generate single (fixed) date range objects. See this text field for guidance:

```
<element kind="textField" stretchType="ElementGroupHeight" mode="Opaque" x="375" y="380" width="180" height="20" 
        backcolor="#EEEEEE" vTextAlign="Middle" textAdjust="StretchHeight" pattern="EEE, MMM d, yyyy  HH:mm:ss z">
  <expression><![CDATA[DATERANGE("2017-05-01 14:25:48").getEnd()]] ></expression>
  <property name="net.sf.jasperreports.export.xls.pattern" value="ddd, MMM d, yyyy  HH:mm:ss"/>
</element>
```

In order to be used in a SQL clause, a report parameter of type `DateRange` is declared as follows:

```
<parameter name="StartDate" class="net.sf.jasperreports.types.date.DateRange">
  <defaultValueExpression><![CDATA[DATERANGE("1996-09-01")]] ></defaultValueExpression>
</parameter>

```

It will be used in this parameterized query:

```
<query>
SELECT * FROM Orders WHERE 
    OrderID <= $P{MaxOrderID}  
    AND $X{[GREATER, OrderDate, StartDate} 
    ORDER BY ShipCountry, ShipCity, OrderDate
</query>
```

As a result, the report data source will include only orders newer than September 1, 1996.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/daterange` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/daterange/target/reports` directory.
