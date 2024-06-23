
# JasperReports - JavaScript Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the JavaScript language could be used inside report templates.

### Main Features in This Sample

[Using the JavaScript Language for Report Expressions (JavaScript Report Compiler)](#javascript)

### Secondary Features

[Report Compilers](../groovy/README.md#reportcompilers)

## <a name='javascript'>Using</a> the JavaScript Language for Report Expressions (JavaScript Report Compiler)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to use JavaScript to write report expressions.

**Since:** 3.1.2

**Other Samples**\
[/demo/samples/antcompile](../antcompile/README.md)\
[/demo/samples/groovy](../groovy/README.md)\
[/demo/samples/java1.5](../java1.5/README.md)

### JavaScript Scripting Example

The main purpose of this sample is to show how the JavaScript compiler implementation works. Useful information about the default JavaScript compiler implementation can be found [here](../groovy/README.md#jsCompiler).

This sample contains report expressions written using JavaScript. The [JavaScriptCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/compilers/JavaScriptCompiler.html) default implementation creates the JavaScript-related expression evaluator during the report compilation and prepares the JasperReport object for the filling process.

In order to use JavaScript, the report `language` attribute is declared below:

```
language="javascript"
```

In the report template are presented some JavaScript expressions which cannot be evaluated using Java, and one could notice some advantages that JavaScript scripting comes with.

Having two numbers, 3 and 5, the report will output first their values as real numbers, and then their calculated sum. The two numbers are declared as follows:

```
<parameter name="A" class="java.lang.Double">
  <defaultValueExpression><![CDATA[3]] ></defaultValueExpression>
</parameter>
<parameter name="B" class="java.lang.Double">
  <defaultValueExpression><![CDATA[5]] ></defaultValueExpression>
</parameter>
```

Both A and B values are declared of `java.lang.Double` type. But their values are let as primitive int types, because JavaScript is able to allocate types at runtime. All type conversions are performed dynamically, according to the type declarations above. The JavaScript scripting above uses a very simplified syntax. Taking into account the backward compatibility with JDK 1.4.x or earlier, equivalent Java expressions would be:

```
<defaultValueExpression><![CDATA[Double.valueOf(3.0)]] ></defaultValueExpression>
<defaultValueExpression><![CDATA[Double.valueOf(5.0)]] ></defaultValueExpression>
```

The next two expressions in the report template read values from parameters declared above and store them in two text fields. These expressions can be evaluated using either JavaScript or Java:

```
<expression><![CDATA[$P{A}]] ></expression>
...
<expression><![CDATA[$P{B}]] ></expression>
```

Next, the A + B sum is calculated within a JavaScript expression:

```
<expression><![CDATA[Integer.valueOf($P{A} + $P{B})]] ></expression>
```

Object instantiation and type conversion are transparent processes here, the user only has to write a simple addition operation between the two report parameters (however, the specific parameter syntax still has to be respected).

The equivalent Java (1.4) expression would be:

```
<expression><![CDATA[Double.valueOf($P{A}.doubleValue() + $P{B}.doubleValue())]] ></expression>
```

with a lot more complicated syntax.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/javascript` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/javascript/target/reports` directory.
