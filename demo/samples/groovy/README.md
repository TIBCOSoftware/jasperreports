
# JasperReports - Groovy Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>
Shows how the Groovy scripting languages could be used inside report templates.

### Main Features in This Sample

[Report Compilers](#reportcompilers)\
[Using the Groovy Scripting Language for Report Expressions (Groovy Report Compiler)](#groovy)

## <a name='reportcompilers'>Report</a> Compilers
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to implement a custom report compiler and how to register it with a custom defined report expression language.

**Since:** 0.6.6

**Other Samples**\
[/demo/samples/antcompile](../antcompile/README.md)\
[/demo/samples/java1.5](../java1.5/README.md)\
[/demo/samples/javascript](../javascript/README.md)

### Compiling Report Templates

Source report templates stored into [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) objects are produced when parsing JRXML files using the [JRXmlLoader](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/xml/JRXmlLoader.html) class, or created directly by the parent application if dynamic report templates are required. The GUI tools for editing JasperReports templates also work with this class to make in-memory modifications to the report templates before storing them on disk.

[JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) objects contain all static information needed for a report template design. In order to make various consistency validations and to incorporate into these report templates data used to evaluate all report expressions at runtime, [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) objects should became subject to the report compilation process before they are filled with data.

The compilation process transforms [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) objects into [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) objects. Both classes are implementations of the same basic [JRReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRReport.html) interface. However, [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) objects cannot be modified once they are produced, while [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) objects can. This is because some modifications made on the report template would probably require re-validation, or if a report expression is modified, the compiler-associated data stored inside the report template would have to be updated.

The report compilation process relies on the [JRCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRCompiler.html) interface, which defines four methods:

```
public JasperReport compileReport(JasperDesign design) throws JRException;
public JREvaluator loadEvaluator(JasperReport jasperReport) throws JRException;
public JREvaluator loadEvaluator(JasperReport jasperReport, JRDataset dataset) throws JRException;
public JREvaluator loadEvaluator(JasperReport jasperReport, JRCrosstab crosstab) throws JRException;
```

The first method is responsible for the report compilation, while the other three generate expression evaluators depending on various input parameters.

There are several implementations for this compiler interface depending on the language used for the report expressions or the mechanism used for their runtime evaluation.

### Report Java Compilers

The default language used for the report expressions is Java, but report expressions can be written in any other scripting language (like Groovy, JavaScript, etc) as long as a report compiler implementation that can evaluate them at runtime is available. The expression language is specified using the language attribute within the `<jasperReport/>` element.

Since the most common scenario is to use the Java language for writing report expressions, default implementations of the report compiler interface are shipped with the library and are ready to use. They generate a Java class from the report expressions and store bytecode in the generated [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) object for use at report-filling time.

There are several available Java report compilers, depending on the JVM compiler used to compile the class that is generated on the fly:

- [`JRJdtCompiler`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/jdt/JRJdtCompiler.html)
- [`JRJdk13Compiler`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRJdk13Compiler.html)
- [`JRJavacCompiler`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRJavacCompiler.html)

The report-compilation process is based on the [JasperCompileManager](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperCompileManager.html) facade class. This class has various public static methods for compiling report templates that come from files, input streams, or in-memory objects.

The report compilation facade relies on the report template's `language` attribute to determine an appropriate report compiler. If the language is either not set or Java, the facade class reads first a configuration property called `net.sf.jasperreports.compiler.java` which usually stores the name of the compiler implementation class for the Java expression language. If this property is found, the facade instantiates a compiler object of that class and delegates the report compilation to it.

Similar properties that map the Groovy and JavaScript report compilers to the `groovy` and `javascript` report languages are also available in JasperReports (see the next section).

If the report uses Java as language and no specific compiler has been set for this language, the report compilation facade uses the [DefaultReportCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/repo/DefaultReportCompiler.html), which triggers a built-in fall back mechanism that picks the best Java-based report compiler available in the environment in which the report compilation process takes place.

The [DefaultReportCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/repo/DefaultReportCompiler.html) first reads the configuration property called `net.sf.jasperreports.compiler.class` to allow users to override its built-in compiler-detection logic by providing the name of the report compiler implementation to use directly. If no overrides are found, then it first tries to see if the JDT compiler from the [Eclipse Foundation](https://www.eclipse.org/) is available in the application’s classpath. If it is, the `JRJdtCompiler` implementation is used.

The current JasperReports distribution ships the JDT compiler packed in the `/lib/jdtcompiler. jar` file.

If the JDT compiler is not available, the compilation facade then tries to locate the JDK 1.3–compatible Java compiler from Sun Microsystems. This is normally found in the `tools.jar` file that comes with the JDK installation.

If the JDK 1.3–compatible Java compiler is not in the classpath, the fall back search mechanism looks for the JDK 1.2–compatible Java compiler, also from Sun Microsystems, in case the application is running in an environment that has a JDK version prior to 1.3 installed. This is also found in the `tools.jar` file from the JDK installation.

If all these fail, the last thing the fall back mechanism does is to try to launch the `javac.exe` program from the command line in order to compile the temporarily generated Java source file on the fly.

### Other Expression Scripting Languages

As shown above, report expressions can be written in scripting languages other than Java, taking advantage of these specific languages features. The only condition is to make available a report compiler implementation able to evaluate them at runtime.

The JasperReports library provides built-in compiler implementations for these scripting languages: `Groovy, JavaScript`:

- [`JRGroovyCompiler`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/groovy/JRGroovyCompiler.html)
- [`JavaScriptCompiler`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/javascript/JavaScriptCompiler.html)

If the `language` attribute is set to `groovy`, or `javascript` or any other scripting language, then the [JasperCompileManager](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperCompileManager.html) facade looks for the `net.sf.jasperreports.compiler.<language>` property, to see whether a compiler implementation class is available for the specified language. Default values for these language-specific properties are:

```
net.sf.jasperreports.compiler.groovy=net.sf.jasperreports.compilers.JRGroovyCompiler
net.sf.jasperreports.compiler.javascript=net.sf.jasperreports.compilers.JavaScriptCompiler
```

### The Built-in Groovy Compiler

When the `language` attribute is set to `groovy`, it means that Groovy scripting language will be used within report expressions. After loading the report template into a [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) object, the report expressions should be read and evaluated properly, and the evaluation data should be then communicated to the resulting [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) object. These operations are performed by the compiler implementation.
The `net.sf.jasperreports.compiler.groovy` property indicates the Groovy compiler class. The default implementation is:

```
net.sf.jasperreports.compiler.groovy=net.sf.jasperreports.compilers.JRGroovyCompiler
```

Let's take a look inside the [JRGroovyCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/groovy/JRGroovyCompiler.html). It extends the [JRAbstractJavaCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRAbstractJavaCompiler.html) abstract class, which extends itself the less specific [JRAbstractCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRAbstractCompiler.html) abstract class. While its parents handle general or Java-related compiling features, the [JRGroovyCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/groovy/JRGroovyCompiler.html) takes care only for the Groovy-specific operations. It implements four methods from the [JRAbstractCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRAbstractCompiler.html) class:

```
protected abstract String compileUnits(JRCompilationUnit[] units, String classpath, File tempDirFile) throws JRException;
protected abstract void checkLanguage(String language) throws JRException;
protected abstract JRCompilationSourceCode generateSourceCode(JRSourceCompileTask sourceTask) throws JRException;
protected abstract String getSourceFileName(String unitName);
```

### The Built-in JavaScript Compiler

When the `language` attribute is set to `javascript`, report expressions will be written using JavaScript. The default compiler implementation to handle all JavaScript specific stuff is specified below:

```
net.sf.jasperreports.compiler.javascript=net.sf.jasperreports.compilers.JavaScriptCompiler
```

The [JavaScriptCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/javascript/JavaScriptCompiler.html) class extends the [JRAbstractCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRAbstractCompiler.html) class and implements five methods from this:

```
protected JREvaluator loadEvaluator(Serializable compileData, String unitName) throws JRException;
protected abstract String compileUnits(JRCompilationUnit[] units, String classpath, File tempDirFile) throws JRException;
protected abstract void checkLanguage(String language) throws JRException;
protected abstract JRCompilationSourceCode generateSourceCode(JRSourceCompileTask sourceTask) throws JRException;
protected abstract String getSourceFileName(String unitName);
```

### Configuration Properties to Customize Report Compilation

- `net.sf.jasperreports.compiler.<language>` - this property was already presented above.
- `net.sf.jasperreports.compiler.xml.validation` - specifies whether the XML validation should be turned on or off. By default, it is considered turned on.
- `net.sf.jasperreports.compiler.classpath` - supplies the classpath for some specific compilers.
- `net.sf.jasperreports.compiler.temp.dir` - specifies the temporary location for the files generated on the fly. By default is considered the current directory.
- `net.sf.jasperreports.compiler.keep.java.file` - specifies whether the generated source files or scripts should be kept in the temporary location after the report gets compiled. By default, they are not kept.

### Specific JDT-Compiler Configuration Properties

The [JRJdtCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/jdt/JRJdtCompiler.html) report compiler can use special JasperReports configuration properties to configure the underlying JDT Java compiler, all of them starting with the `org.eclipse.jdt.core.` prefix.
For example, to instruct the JDT compiler to observe Java 1.5 code compatibility, the following properties should be set:

```
org.eclipse.jdt.core.compiler.source=1.5
org.eclipse.jdt.core.compiler.compliance=1.5
org.eclipse.jdt.core.compiler.codegen.TargetPlatform=1.5
```

### Ant Task for Report Compiling

Since report template compilation is more like a design-time job than a runtime one, a custom Ant task has been provided with the library to simplify application development. This Ant task is implemented by the [JRAntCompileTask](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/ant/JRAntCompileTask.html) class. Its syntax and behavior are very similar to the built-in `<javac>` Ant task.

This user-defined Ant task can be used to compile multiple JRXML report template files in a single operation by specifying the root directory that contains those files or by selecting them using file patterns.

**Parameters for Report Compilation Task**

Following is the list of parameters that can be used inside the Ant report compilation task to specify the source files, the destination directory, and other configuration properties:

- `srcdir` (required) - location of the JRXML report template files to be compiled.
- `destdir` - location to store the compiled report template files (the same as the source directory by default).
- `compiler` - name of the class that implements the net.sf.jasperreports.engine.design.JRCompiler interface to be used for compiling the reports (optional).
- `xmlvalidation` - flag to indicate whether the XML validation should be performed on the source report template files (true by default).
- `tempdir` - location to store the temporarily generated files (the current working directory by default).
- `keepjava` - flag to indicate if the temporary Java files generated on the fly should be kept and not deleted automatically (false by default).

The report template compilation task supports nested `src` and `classpath` elements, just like the Ant `<javac>` built-in task.

To figure out more on report compiling process, take a look at the Groovy sample below, and to the other related samples enumerated in the **Other Samples** section.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='groovy'>Using</a> the Groovy Scripting Language for Report Expressions (Groovy Report Compiler)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to use Groovy scripting for report expressions.

**Since:** 1.2.2

**Other Samples**\
[/demo/samples/antcompile](../antcompile/README.md)\
[/demo/samples/java1.5](../java1.5/README.md)\
[/demo/samples/javascript](../javascript/README.md)

### Groovy Scripting Example

This sample contains expressions written using both Java and Groovy languages. This is possible due to the [`JRGroovyCompiler`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/groovy/JRGroovyCompiler.html) implementation which allows mixing Java and Groovy together. The report language attribute is declared below:

```
language="groovy"
```

The main purpose is to show how the Groovy compiler implementation works. In the report template are presented some scripting differences between Java and Groovy, and one could notice some advantages that Groovy scripting comes with.

Having two numbers, 3 and 5, their sum is calculated first using a Java expression and then using a Groovy one. The two numbers are declared as follows:

```
<parameter name="A" class="java.lang.Double">
  <defaultValueExpression><![CDATA[3d]] ></defaultValueExpression>
</parameter>
<parameter name="B" class="java.lang.Double">
  <defaultValueExpression><![CDATA[5d]] ></defaultValueExpression>
</parameter>
```

Both A and B values are declared of `java.lang.Double` type. But their values are let as primitive int types, because Groovy is able to work with dynamic types. All type conversions are performed at runtime, according to the type declarations above. The Groovy scripting above uses a very simplified syntax. Equivalent Java expressions would be:

```
<defaultValueExpression><![CDATA[Double.valueOf(3.0)]]></defaultValueExpression>
<defaultValueExpression><![CDATA[Double.valueOf(5.0)]]></defaultValueExpression>
```

The next expression in the report template uses Java to calculate their sum:

```
<expression><![CDATA[Double.valueOf($P{A}.doubleValue() + $P{B}.doubleValue())]]></expression>
```

Here, A and B being `Double`, their `doubleValue()` method is called in order to calculate the sum, keeping in mind the backward compatibility with JDK 1.4.x or earlier. After that, because the sum should be stored itself in a `Double` value, a new `Double` object is instantiated for this purpose. The Java expression looks rather complicate and one has to take care to instantiate objects with their proper types in order to avoid class cast exceptions.
Things are changing when using Groovy expressions:

```
  <expression><![CDATA[$P{A} + $P{B}]]></expression>
```

Object creation, autoboxing and unboxing are transparent processes here, the user is no more preoccupated to carefully handle object types, the only thing to do being to write a simple addition operation between two report parameters (however, the specific parameter syntax still has to be respected). The dynamic data binding and simplified writing represent major advantages of Groovy scripts.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/groovy` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/groovy/target/reports` directory.
