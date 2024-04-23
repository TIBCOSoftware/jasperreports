
# JasperReports - Query Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the report query can be build dynamically using report parameters.

### Main Features in This Sample

[Parameterized Queries (Dynamic Queries)](#query)\
[Using Conditional Styles Defined in Style Templates](#conditionalStylesInTemplates)

### Secondary Features

[Creating Watermarks and Page Backgrounds](../hyperlink/README.md#watermark)

## <a name='query'>Parameterized</a> Queries (Dynamic Queries)
<div align="right">Documented by <a href='mailto:lshannon@users.sourceforge.net'>Luke Shannon</a>, <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to pass parameter references to report queries and how to change the report query at runtime.

**Since:** 0.1.0

One of the most powerful features in the JasperReports API, when running JDBC based reports, is the ability to perform complex manipulations
of the report query during the Filling stage of the report life cycle (this is what the JasperReports API executes the query against the
datasource getting back the data for the report). The API offers three powerful tools for query manipulation that are reviewed in this sample.
We will be discussing each of these in this document. Also demonstrated in this sample is how to use the Background band for adding a watermark
style background image.

Before we go further we should discuss the concept of a DataSet. A DataSet is an element of the report template and it the combination
of a report data source (JDBC in this case), parameters (object references that are passed into the report-filling operations by the parent
application), fields (maps data from the data source into the report template), variables (objects built on top of a report expression that
perform various calculations) and groups (covered in the groups sample).

All report templates (JRXML) implicitly declare and use a main dataset. The main dataset is responsible for
iterating through the data source records, calculating variables, filtering out records, and estimating group breaks during the
report-filling process.

In the case of a JDBC based report, the Fields map to the columns coming back from the query. Modifications to the
query allows for fundamentally changes to the data the report works with.
Using Parameters to do this allows us to use information gathered from the parent application, which in turn could come from a
report user (example: A user may provide a start and end date for which they want the data the report show to occur within).

### Tools for Parameterizing the Query

**`$P{}`**

Using `$P{}` in the report query is for situations where the query is fixed at design time and you only wish to inject values into the
query before it is executed.

```
  <query>
    <![CDATA[SELECT Id, FirstName, LastName, Street, City
      FROM Address WHERE City = $P{CityParam}]] >
  </query>
```

If we changed the query this way and turned on Debugging for the [JRJdbcQueryExecuter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/query/JRJdbcQueryExecuter.html) in an application running this report,
we would get an output like this (the hosting application also collected the value for the parameter and supplied it to JasperReports
when it was time to Fill the report):

```
    2010-03-11 12:47:53,648 DEBUG JRJdbcQueryExecuter,http-8080-5:155 - SQL query string: SELECT Id, FirstName, LastName, Street, City FROM Address WHERE City = ?
    2010-03-11 12:47:53,660 DEBUG JRJdbcQueryExecuter,http-8080-5:252 - Parameter #1 (city of type java.lang.String): New York
```

In this case the query as seen above and the parameter are passed to the database via the JDBC Driver (MySQL in this example) to be executed.

As report developers we don't have to worry about adding quotes around the String value for city in the query as that will be done for us.

This illustrates one way of injecting values into the query.

**`$P!{}`**

Using `$P!{}` allows you to modify the query syntax itself. The query in the sample uses this:

```
  <query>
    <![CDATA[SELECT Id, FirstName, LastName, Street, City, (Id < 30) as ConditionalField
      FROM Address ORDER BY $P!{OrderClause}]] >
  </query>
```

If we run the report in an application and collect values for the parameters (`OrderBy` was given the value `'LastName'`) we will see an output like this:

```
    2010-03-11 13:01:05,818 DEBUG JRJdbcQueryExecuter,http-8080-4:155 - SQL query string: SELECT Id, FirstName, LastName, Street, City, (Id < 30) as ConditionalField FROM Address ORDER BY LastName
    2010-03-11 13:01:05,821 DEBUG JRJdbcQueryExecuter,http-8080-4:303 - Parameter #1 (ExcludedCities[0] of type java.lang.String): New York
```

Here we can see the value of `$P!{OrderClause}` was added into the query directly by JasperReports. For this reason, when working with `$P!{}` you must ensure
any values collected will not result in a syntax error in the query as they will be inserted directly into the query. However this does give us the power
to modify the query entirely. For example we could have set the whole `'ORDER BY'` clause using `$P!{}`, or chosen to omit it entirely.

**`$X{}`**

There are also cases when just using $P{} in the report query is not enough, because parts of the query need to be dynamically built, depending on one or more report parameter values, in order to construct a valid query. The most common case is the `<column_name> = $P{<param_name>}` equality clause. When the value of the parameter is null, the expression `<column_name> = NULL` becomes invalid and it has to be replaced with `<column_name> IS NULL`. Another case comes with `IN` and `NOT IN` query clauses that need to use a collection report parameter as a list of values, unavailable for the simple `$P{}` syntax.

Such complex query clauses are introduced into the query using the $X{} syntax. The general form of a `$X{}` clause is `$X{functionName, param1, param2,...}`.

```
  <query>
    <![CDATA[SELECT Id, FirstName, LastName, Street, City, (Id < 30) as ConditionalField
      FROM Address
      WHERE $X{NOTIN, City, ExcludedCities}
      ORDER BY $P!{OrderClause}]] >
  </query>
```

Similar to the `$P{}` explanation above, `$X{}` results in ? being added to the query before submitting it to the DB. Also submitted are the values collected leaving it to the JDBC driver to add the values in and ensure the syntax of the query is correct.

```
    2010-03-11 13:01:05,818 DEBUG JRJdbcQueryExecuter,http-8080-4:155 - SQL query string:
      SELECT Id, FirstName, LastName, Street, City, (Id < 30) as ConditionalField FROM Address WHERE City NOT IN (?) ORDER BY LastName
    2010-03-11 13:01:05,821 DEBUG JRJdbcQueryExecuter,http-8080-4:303 - Parameter #1 (ExcludedCities[0] of type java.lang.String): New York
```

### Built-in SQL Clause Functions

As shown above, complex queries generation might depend on parameter values. JasperReports provides built-in support for several SQL clause functions which require some additional processing:

**The `$X{IN, <column_name>, <parameter_name>}` clause function**

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `IN`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value list. The value of this parameter has to be an `array`, a `java.util.Collection` or null.

If the parameter's value is a collection of not null values, the function constructs a `<column_name> IN (?, ?, .., ?)` clause\
If the parameter's value is a collection containing both null and not null values, the function constructs a `(<column_name> IS NULL OR <column_name> IN (?, ?, .., ?))` clause\
If the parameter's value is a collection containing only null values, the function constructs a `<column_name> IS NULL` clause\
If the parameter's value is null, the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

**The `$X{NOTIN, <column_name>, <parameter_name>}` clause function**

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `NOTIN`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value list. The value of this parameter has to be an array, a `java.util.Collection` or null.

If the parameter's value is a collection of not null values, the function constructs a `<column_name> NOT IN (?, ?, .., ?)` clause\
If the parameter's value is a collection containing both null and not null values, the function constructs a `(<column_name> IS NOT NULL AND <column_name> NOT IN (?, ?, .., ?))` clause\
If the parameter's value is a collection containing only null values, the function constructs a `<column_name> IS NOT NULL` clause\
If the parameter's value is null, the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

Since JasperReports v4.0.1, the following built-in clause functions are also available:

**The `$X{EQUAL, <column_name>, <parameter_name>}` clause function**

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `EQUAL`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value to compare to.

If the parameter's value is not null, the function constructs a `<column_name> = ?` clause.\
If the parameter's value is null, the function generates a <column_name> IS NULL clause.

**The `$X{NOTEQUAL, <column_name>, <parameter_name>}` clause function**

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `NOTEQUAL`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value to compare to.

If the parameter's value is not null, the function constructs a `<column_name> <> ?` clause.\
If the parameter's value is null, the function generates a `<column_name> IS NOT NULL` clause.

**The `$X{LESS, <column_name>, <parameter_name>}` clause function**

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `LESS`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value to compare to.

If the parameter's value is not null, the function constructs a `<column_name> < ?` clause.\
If the parameter's value is null, the comparison with null will be neglected and the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

**Note:** If the comparison with null (which always return false) should not be ignored, then one can use the `<column_name> < $P{<parameter_name>}` instead.

**The $X{LESS], <column_name>, <parameter_name>}** clause function

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `LESS]`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value to compare to.

If the parameter's value is not null, the function constructs a `<column_name> <= ?` clause.\
If the parameter's value is null, the comparison with null will be neglected and the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

**Note:** If the comparison with null (which always return false) should not be ignored, then one can use the `<column_name> <= $P{<parameter_name>}` instead.

**The `$X{GREATER, <column_name>, <parameter_name>}` clause function**

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `GREATER`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value to compare to.

If the parameter's value is not null, the function constructs a `<column_name> > ?` clause.\
If the parameter's value is null, the comparison with null will be neglected and the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

**Note:** If the comparison with null (which always return false) should not be ignored, then one can use the `<column_name> > $P{<parameter_name>}` instead.

**The `$X{[GREATER, <column_name>, <parameter_name>}` clause function**

The function expects three mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `[GREATER`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the report parameter that contains the value to compare to.

If the parameter's value is not null, the function constructs a `<column_name> >= ?` clause.\
If the parameter's value is null, the comparison with null will be neglected and the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

**Note:** If the comparison with null (which always return false) should not be ignored, then one can use the `<column_name> >= $P{<parameter_name>}` instead.

**The `$X{BETWEEN, <column_name>, <left_parameter_name>, <right_parameter_name>}` clause function**

The function expects four mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `BETWEEN`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the parameter that contains the left member value.
- The fourth token is the name of the parameter that contains the right member value.

If both parameter values are not null, the function constructs a double comparison, similar to the `BETWEEN` SQL clause where both interval endpoints are excluded: `(<column_name> > ? AND <column_name> < ?)`\
If the left parameter's value is null, the function constructs a `<column_name> < ?` clause, using the right parameter's value at fill time.\
If the right parameter's value is null, the function constructs a `<column_name> > ?` clause, using the left parameter's value at fill time.\
If both parameter values are null, the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

**The `$X{[BETWEEN, <column_name>, <left_parameter_name>, <right_parameter_name>}` clause function**

The function expects four mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `[BETWEEN`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the parameter that contains the left member value.
- The fourth token is the name of the parameter that contains the right member value.

If both parameter values are not null, the function constructs a double comparison, similar to the `BETWEEN` SQL clause where the left interval endpoint is included and the right endpoint is excluded: `(<column_name> >= ? AND <column_name> < ?)`\
If the left parameter's value is null, the function constructs a `<column_name> < ?` clause, using the right parameter's value at fill time.\
If the right parameter's value is null, the function constructs a `<column_name> >= ?` clause, using the left parameter's value at fill time.\
If both parameter values are null, the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0)`.

**The `$X{BETWEEN], <column_name>, <left_parameter_name>, <right_parameter_name>}` clause function**

The function expects four mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `BETWEEN]`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the parameter that contains the left member value.
- The fourth token is the name of the parameter that contains the right member value.

If both parameter values are not null, the function constructs a double comparison, similar to the `BETWEEN` SQL clause where the left interval endpoint is excluded and the right endpoint is included: `(<column_name> > ? AND <column_name> <= ?)`\
If the left parameter's value is null, the function constructs a `<column_name> <= ?` clause, using the right parameter's value at fill time.\
If the right parameter's value is null, the function constructs a `<column_name> > ?` clause, using the left parameter's value at fill time.\
If both parameter values are null, the function generates a SQL clause that will always evaluate to `true` (e.g. 0 = 0).

**The `$X{[BETWEEN], <column_name>, <left_parameter_name>, <right_parameter_name>}` clause function**

The function expects four mandatory clause tokens:

- The first token represents the function ID and always takes the fixed value `[BETWEEN]`.
- The second token is the SQL column (or column combination) to be used in the clause.
- The third token is the name of the parameter that contains the left member value.
- The fourth token is the name of the parameter that contains the right member value.

If both parameter values are not null, the function constructs a double comparison, similar to the `BETWEEN` SQL clause where both interval endpoints are included: `(<column_name> >= ? AND <column_name> <= ?)`\
If the left parameter's value is null, the function constructs a `<column_name> <= ?` clause, using the right parameter's value at fill time.\
If the right parameter's value is null, the function constructs a `<column_name> >= ?` clause, using the left parameter's value at fill time.\
If both parameter values are null, the function generates a SQL clause that will always evaluate to `true` (e.g. `0 = 0`).

In this sample the query string contains a `$X{NOTIN, City, ExcludedCities}` piece of code:

```
  <parameter name="ExcludedCities" class="java.util.Collection"/>
  <parameter name="OrderClause" class="java.lang.String"/>
  <query>
    <![CDATA[SELECT Id, FirstName, LastName, Street, City, (Id < 30) as ConditionalField
      FROM Address
      WHERE $X{NOTIN, City, ExcludedCities}
      ORDER BY $P!{OrderClause}]] >
  </query>
```

If we run the report again and pass two values into `$P{ExcludedCities}`:

```
    2010-03-11 13:25:23,300 DEBUG JRJdbcQueryExecuter,http-8080-4:155 - SQL query string:
      SELECT Id, FirstName, LastName, Street, City, (Id < 30) as ConditionalField FROM Address WHERE City NOT IN (?, ?) ORDER BY LastName
    2010-03-11 13:25:23,302 DEBUG JRJdbcQueryExecuter,http-8080-4:303 - Parameter #1 (ExcludedCities[0] of type java.lang.String): New York
    2010-03-11 13:25:23,302 DEBUG JRJdbcQueryExecuter,http-8080-4:303 - Parameter #2 (ExcludedCities[1] of type java.lang.String): Boston
```

<div align="right"><a href='#top'>top</a></div>

---

## <a name='conditionalStylesInTemplates'>Conditional</a> Styles in Style Templates
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to declare conditional styles in style templates and refer them in the report.

**Since:** 6.20.0

**Other Samples**\
[/demo/samples/templates](../templates/README.md)\
[/demo/samples/jasper](../jasper/README.md)

### Defining Conditional Styles in Style Templates

The most convenient way to configure a report element appearance is to use the style attribute for that element. Any type of report element can use this attribute to reference a report style definition, and to inherit all applicable properties declared in that style.

In case there are several reports that need to work with very similar (or identical) styles, a good practice would be to define those styles only once in an external style template file (`*.jrtx`), and then refer to this template everywhere it is needed. All styles defined in a style template become visible in the report after it is referenced via the `<template/>` tag. For more information about style templates you could take a look at [Templates](../templates/README.md)) sample.

In our report sample we can see this style template declaration:

```
  <template><![CDATA["ReportStyles.jrtx"]] ></template>
```

In various situations we need to configure elements' L&F depending on certain conditions that will be evaluated at runtime. For instance, generating alternate row colors in a table with a large number of rows, or using different text styles and colors that depend on the current value of the element. In such cases we need a tool for setting up different style features for the same element or cell. Conditional styles are used to solve this problem. They can be defined either in the report itself, or, for some particular values, in style templates.

For more information about conditional styles defined in reports you could take a look at [Jasper](../jasper/README.md)) sample.

Starting with JasperReports v.6.20.0 conditional styles can be also defined in a style template file, with respect to the following requirement:

The `<conditionExpression/>` used to particularize a style must be a simple parameter/field/variable reference.

In other words, the `<conditionExpression/>` is only allowed to have one of the following forms:

- `$P{parameter_name}`
- `$F{field_name}`
- `$V{variable_name}`

Complex expressions are not allowed. And because they represent a condition, the `$P{parameter_name}`, `$F{field_name}` or `$V{variable_name}` expressions should be `Boolean` (or `boolean`) type. Null values are evaluated as `Boolean.FALSE`.

### Conditional Styles in the Query Sample

Let's take a look at the content of the external `ReportStyles.jrtx` template file. We'll see the following style definitions:

```
  <jasperTemplate>
    <style name="Sans_Normal" fontName="DejaVu Sans" fontSize="12.0" default="true"/>
    <style name="Sans_Bold" fontName="DejaVu Sans" fontSize="12.0" bold="true"/>
    <style name="Sans_Italic" fontName="DejaVu Sans" fontSize="12.0" italic="true"/>
    <style name="RowStyle" style="Sans_Normal">
      <conditionalStyle mode="Opaque" backcolor="rgba(238, 238, 238, 0.5)">
        <conditionExpression><![CDATA[$V{ConditionalRow}]] ></conditionExpression>
      </conditionalStyle>
    </style>
    <style name="FieldStyle" style="RowStyle">
      <conditionalStyle forecolor="#0000FF">
        <conditionExpression><![CDATA[$F{ConditionalField}]] ></conditionExpression>
      </conditionalStyle>
    </style>
    <style name="TitleStyle" fontSize="22.0" style="Sans_Normal">
      <conditionalStyle fontSize="26.0" bold="true">
        <conditionExpression><![CDATA[$P{ConditionalParam}]] ></conditionExpression>
      </conditionalStyle>
    </style>
  </jasperTemplate>
```

There are 3 static styles in this template, followed by 3 conditional styles:

- The `RowStyle` depends on the $V{ConditionalRow} boolean variable and configures the element's backcolor when the condition is met. It will be used in the report to generate alternate row colors in a tabular structure.
- The `FieldStyle` depends on the dynamic $F{ConditionalField} boolean field value and configures the element's forecolor when the condition is met.
- The `TitleStyle` depends on the `$P{ConditionalParam}` boolean parameter and configures the element's font size and font weight when the condition is met. It will decorate the report title in our sample.

Now let's see how were these styles referred to in the report. First, we referenced the external style template file:

```
  <template><![CDATA["ReportStyles.jrtx"]] ></template>
```

Then, we declared a boolean parameter, for instance named `ConditionalParam`:

```
<parameter name="ConditionalParam" class="java.lang.Boolean">
  <defaultValueExpression><![CDATA[true]] ></defaultValueExpression>
</parameter>
```

Then, we have a report query that retrieves at least a `Boolean` field (here identified as `ConditionalField`):

```
<query language="sql"><![CDATA[SELECT Id, FirstName, LastName, Street, City, (Id < 30) as ConditionalField
  FROM Address
  WHERE $X{NOTIN, City, ExcludedCities}
  ORDER BY $P!{OrderClause}]] ></query>
```

Finally, we have a boolean report variable (named `ConditionalRow`) that depends on the built-in `CityGroup_COUNT` variable:

```
  <variable name="ConditionalRow" resetType="Group" resetGroup="CityGroup" class="java.lang.Boolean">
    <expression><![CDATA[$V{CityGroup_COUNT} % 2 == 0]] ></expression>
    <initialValueExpression><![CDATA[null]] ></initialValueExpression>
  </variable>
```

After ensuring the needed parameter, field or variable are present in the report, we just use the style attribute of the element to reference the needed style. For instance, for the text element in the title section:

```
<element kind="textField" y="10" width="515" height="35" hTextAlign="Center" blankWhenNull="true" style="TitleStyle">
	<paragraph lineSpacing="Single"/>
	<expression><![CDATA[$P{ReportTitle}]] ></expression>
</element>
```

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/query` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/query/target/reports` directory.
