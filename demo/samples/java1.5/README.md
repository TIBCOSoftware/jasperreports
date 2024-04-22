
# JasperReports - Java 1.5 Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how Java 1.5 could be used inside report templates.

### Main Features in This Sample

[Using Java 1.5 Syntax in Report Expressions (Java 1.5 Report Compiler)](#java1.5)

### Secondary Features

[Report Compilers](../groovy/README.md#reportcompilers)

## <a name='java1.5'>Using</a> Java 1.5 Syntax in Report Expressions (Java 1.5 Report Compiler)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to use Java 1.5 language specific features inside report expressions.

**Since:** 1.1.1

**Other Samples**\
[/demo/samples/antcompile](../antcompile/README.md)\
[/demo/samples/groovy](../groovy/README.md)\
[/demo/samples/javascript](../javascript/README.md)

### Java 1.5 Scripting Example

The main purpose of this sample is to show how the Java 1.5 compiler implementation works. Useful information about various Java compiler implementations can be found [here](../groovy/README.md#javaCompilers).

This sample contains report expressions written using Java 1.5. The [JRJdtCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/jdt/JRJdtCompiler.html) default implementation is Java 1.5-compatible and is strongly recommended to use it when handling Java 1.5-related expressions.

In order to use Java 1.5, the report `language` attribute can be either not set (because Java represents the default scripting language), or declared as follows:

```
language="java"
```

In the sample's report template the `language` attribute is not set.

Next, one have to instruct the JDT compiler to observe Java 1.5 code compatibility. In the `/src/jasperreports.properties` file the following properties are set:

```
org.eclipse.jdt.core.compiler.source=1.5
org.eclipse.jdt.core.compiler.compliance=1.5
org.eclipse.jdt.core.compiler.codegen.TargetPlatform=1.5
```

In the report template are included specific Java 1.5 expressions not compatible with Java 1.4 or earlier, requiring transparent autoboxing and unboxing, or enumerated types. An equivalent Java 1.4-compatible expression is also included, for comparison.

Having two integer numbers, 3 and 5, the report will output first their values, and then their calculated sum. The two numbers are declared as follows:

```
<parameter name="A" class="java.lang.Integer">
  <defaultValueExpression><![CDATA[3]] ></defaultValueExpression>
</parameter>
<parameter name="B" class="java.lang.Integer">
  <defaultValueExpression><![CDATA[5]] ></defaultValueExpression>
</parameter>
```

Both A and B values are declared of `java.lang.Integer` type. But their values are let as primitive int types, because Java 1.5 performs both autoboxing and unboxing mechanisms. When needed, primitive types are automatically converted into their wrapper class. The Java 1.5 syntax becomes a lot simplified. Equivalent Java 1.4 expressions would be:

```
<defaultValueExpression><![CDATA[Integer.valueOf(3)]] ></defaultValueExpression>
<defaultValueExpression><![CDATA[Integer.valueOf(5)]] ></defaultValueExpression>
```

The next two expressions in the report template read values from parameters declared above and store them in two text fields. These expressions can be evaluated using either Java 1.5 or Java 1.4:

```
<expression><![CDATA[$P{A}]] ></expression>
...
<expression><![CDATA[$P{B}]] ></expression>
```

Next, the A + B sum is calculated using a Java 1.4 expression:

```
<expression><![CDATA[Integer.valueOf($P{A}.intValue() + $P{B}.intValue())]] ></expression>
```

A and B being `Integer`, their `intValue()` method is called in order to calculate the sum. After that, because the sum should be stored itself in an `Integer` value, an `Integer` object is made available for this purpose. The Java expression looks rather complicate and one has to take care to instantiate objects with their proper types in order to avoid class cast exceptions.

Now, the same A + B sum is calculated using a Java 1.5 expression:

```
<expression><![CDATA[Integer.valueOf($P{A} + $P{B})]] ></expression>
```

Object creation, autoboxing, unboxing and type conversion are transparent processes here, the user only has to write a simple addition operation between the two report parameters (however, the specific parameter syntax still has to be respected).

Finally, depending on the greeting parameter's value, a greeting formula is shown. This parameter is an enumerated `Greeting` type (another specific Java 1.5 feature):

```
<parameter name="greeting" class="Greeting"/>
```
The `Greeting` type is defined in the `/src/Greeting.java` file:
```
public enum Greeting { bye, day }
```

When the parameter's value is `Greeting.bye`, the output message will be `'Bye!'``; when the parameter's value is Greeting.day`, the message will be `'Have a nice day!'`:

```
<element kind="staticText" y="450" width="480" height="35" hTextAlign="Center" fontSize="24.0">
  <printWhenExpression><![CDATA[$P{greeting} == Greeting.bye]] ></printWhenExpression>
  <text><![CDATA[Bye!]] ></text>
</element>
<element kind="staticText" y="450" width="480" height="35" hTextAlign="Center" fontSize="24.0">
  <printWhenExpression><![CDATA[$P{greeting} == Greeting.day]] ></printWhenExpression>
  <text><![CDATA[Have a nice day!]] ></text>
</element>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/java1.5` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/java1.5/target/reports` directory.
