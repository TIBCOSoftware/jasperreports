
# JasperReports - Scriptlet Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the scriptlets could be used to manipulate data during report filling.

### Main Features in This Sample

[Scriptlets](#scriptlets)\
[Alternate Row Colors In The Detail Section](#alternaterowcolor)

## <a name='scriptlets'>Scriptlets</a>
<div align="right">Documented by <a href='mailto:lshannon@users.sourceforge.net'>Luke Shannon</a></div>

**Description / Goal**\
How to perform custom calculation and tailor the report filling process using report scriptlets implementations.

**Since:** 0.2.5

There are situations when a calculation is required during the report filling stage that is not included in JasperReports provided calculations.\
Examples of this may be complex String manipulations, building of Maps or Lists of objects in memory or manipulations of dates using 3rd party Java APIs.\
Luckily JasperReports provides us with a simple and powerful means of doing this with Scriptlets.

### What is a Scriptlet?

A Scriptlet is a Java Class that extends one of the two following classes:

- [`JRDefaultScriptlet`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDefaultScriptlet.html)
- [`JRAbstractScriptlet`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRAbstractScriptlet.html)

The sample extends the [`JRDefaultScriptlet`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDefaultScriptlet.html). The difference between the two is with [`JRAbstractScriptlet`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRAbstractScriptlet.html) a developer must implement all the abstract methods.\
By extending [`JRDefaultScriptlet`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRDefaultScriptlet.html) a developer is only required to implement that methods he/she needs for their project.\
Once the Scriptlet has been referenced in the report, during the Filling stage of the Report Life Cycle, the JasperReports API will ensure to call the appropriate methods within the Scriptlet.
It is within these methods you will place your own logic to manipulate data in the report. 

### What does a Scriptlet do?

A Scriptlet allows the developer to obtain the values of Fields, Variables and Parameters from the report during specific events in the Filling stage of the Report Life Cycle.\
It also allows you to set data in the report as it executes its Fill Cycle. We will discuss this more in the sections below.\
If you look at Scriptlet.java within this sample you will see all possible methods have been implemented. Most contain output statements.\
By running the sample from the command line or in iReport you will see the outputs of these statements along with the generated report itself.\
The method `afterGroupInit` contains all the most interesting logic and will be the main focus of our discussion.

### Working with Report Data

A developer can read the values from Fields (which map to the data source), Values and Parameters from the report into variables within the Scriptlet.
Examples of this can be seen in afterGroupInit method. In this method we obtain the value of a variable and a field from the report as it is filling:

```
    String allCities = (String)this.getVariableValue("AllCities");
	String city = (String)this.getFieldValue("City");
```

In the first line of code we get the value of the AllCities variable, the second we get the value of the Field `"City"`.\
It is important to note we get these values at the time of the event this method in the Scriptlet corresponds with.\
In this case, after a JasperReports group has been initalized, the logic in this report will be executed (provided the check on the group name performed at the start of the method is successful).\
In the same method we see an example of how to write to a variable in the report:

```
	this.setVariableValue("AllCities", sbuffer.toString());
```

The important part when ensuring a variable in your report template is filled by a Scriptlet (or subreport) is to ensure the Variable has a calculation type of `'System'` in the report design:

```
	<variable name="AllCities" class="java.lang.String" calculation="System"/>
```

Also notice that there is **no** Variable Expression.

Make sure you remember these two points when creating Variables in your own report with values supplied by Scriptlets.

### Creating Helper Methods

At the end of the class a extra method called hello has been defined:

```
	public String hello() throws JRScriptletException
	{
		return "Hello! I'm the report's scriptlet object.";
	}
```

This is an example of a method that can added to the Scriptlet that actually returns a value, rather than setting a Variable.

The `ScripletReport.jrxml` has a method in the Summary band that illustrates how to use such a method. The expression is:

```
	$P{REPORT_SCRIPTLET}.hello()
```

The Parameter referenced is a built-in Parameter managed by the JasperReports API and contains a reference to the Scriptlet.\
As can be seen the `hello()` method is called. The Type of the TextField containing this expression is String.\
This corresponds to the type returned by the method in the Scriptlet.

How do you use a Scriptlet in a report? A Scriptlet can be associated with a Report by adding a `scriptletClass` property to the JasperReports tag:

```
    <jasperReport
    name="ListReport"
    columnCount="2"
    pageWidth="595"
    pageHeight="842"
    columnWidth="250"
    columnSpacing="15"
    leftMargin="40"
    rightMargin="40"
    topMargin="50"
    bottomMargin="50"
    scriptletClass="com.myproject.reporting.MyScriptlet">
```

**Note:** The fully qualified reference is used for the Scriptlet class.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='alternaterowcolor'>Alternate</a> Row Colors In The Detail Section
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to get alternate row colors in a detail section using conditional styles.

**Since:** 1.2.0

**Other Samples**\
[/demo/samples/crosstabs](../crosstabs/README.md)\
[/demo/samples/list](../list/README.md)

### Alternate Row Colors In The Detail Section

Conditional styles combined with some specific report built-in variables can be used to generate alternate row colors in the detail section.

As known, the detail section is repetitive and contains common layout information for records in the dataset. For each record in the dataset, a detail section filling process is called at fill time. Being related to records, one of the most common layouts used for the detail section is the table row layout. And like in any other table, alternate row colors might be needed.
To obtain alternate row colors, two things are necessary:

- to know the current record number in order to determine if it's odd or even
- a conditional style with specific style properties for odd or even rows

The first requirement is accomplished with the help of report built-in counting variables:

- `REPORT_COUNT - if the row color alternation does not depend on the page, column or group breaks
- `PAGE_COUNT` - if each new detail page should start with the same row color
- `COLUMN_COUNT` - if each new detail page column should start with the same row color
- `<group_name>_COUNT` - if each new group of records should start with the same row color
- `ROW_COUNT` - available only for crosstabs; the `ROW_COUNT` variable returns the current row number in a crosstab. An example of conditional styles in a crosstab can be found in the [Crosstabs](../crosstabs/README.md) sample.

The conditional style required at point (2) is illustrated in the scriptlet sample like below:

```
<style name="AlternateDetail">
  <conditionalStyle mode="Opaque" backcolor="#C0C0C0">
    <conditionExpression><![CDATA[$V{CityGroup_COUNT} % 2 == 0]] ></conditionExpression>
  </conditionalStyle>
</style>
```

Here, the odd rows in the `CityGroup` will be rendered in the default background color (usually white), and the even ones will be rendered in gray.

Other examples of generating alternate rows can be found in:

- [Crosstabs](../crosstabs/README.md) sample, based on the built-in `ROW_COUNT` and `COLUMN_COUNT` variables.
- [Crosstabs](../crosstabs/README.md) sample, based on the built-in `REPORT_COUNT` variable.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/scriptlet` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/scriptlet/target/reports` directory.
