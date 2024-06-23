
# JasperReports - Alter Design Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how compiled report templates can be modified on-the-fly without requiring recompilation.

### Main Features in This Sample

[Altering Compiled Report Templates](#alterdesign)
				
## <a name='alterdesign'>Altering</a> Compiled Report Templates 
 <div align="right">Documented by <a href='mailto:teodord@users.sourceforge.net'>Teodor Danciu</a></div>

**Description / Goal**	

Altering a compiled report template means making modifications on the report template prior to sending it for filling with data and without the need to revalidate or recompile it.

**Since:** 0.4.5


Not all runtime information can be passed to a report as a report fill-time parameter. For instance, the background color of a rectangle element, cannot be provided at runtime as a parameter value.
While there is certainly a way to make a rectangle element change its background color depending on some runtime condition, it can be done so only by using a conditional style and thus switch to one of the predefined colors that are associated with the conditions in that conditional style. For more information about the use of conditional report styles, see the [jasper](../jasper/README.md) sample.
The background color cannot be completely dynamic, as the color code cannot be provided as a report parameter or report field value. The background color property of the rectangle is not backed by an expression.

In order to be able to set any runtime color code as the rectangle's background color, the report template has to be created at runtime, or at least modified (altered) at runtime, using the JasperReports API. Creating report templates using the API is demonstrated in the [noxmldesign](../noxmldesign/README.md) sample.

Note that only compiled report template objects (JasperReport objects) can be filled with data. If the report template is in the form of a source [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) object or in the form of a JRXML file, they need to be compiled into a [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) object or a `*.jasper` file before being filled. This is explained in the [antcompile](../antcompile/README.md) sample.

On an existing compiled report template object (`JasperReport` object), only certain modifications/alterations are allowed using the API. This is because some modifications, such as modifying an expression or a variable name, might require compiling/validating the report template again. Only modifications that do not require the report template to be revalidated or recompiled, are allowed on a compiled report template object. Changing the background color of a rectangle element is one of the operations allowed. For a complete reference to all modifications allowed, please consult the [Javadoc](https://jasperreports.sourceforge.net/api/README.md) and look for setter methods. For properties in the object model for which you only find getter methods, it means modifying the value for that property is not allowed on a compiled report object model, and you should be working with the report design object model instead (`JasperDesign` objects).

If you open the `reports/AlterDesignReport.jrxml` source file in the current sample, you'll notice it has 3 rectangle elements in its detail section.

```
<element kind="rectangle" key="first.rectangle" y="100" width="555" height="90">
  <pen lineWidth="4.0"/>
</element>
<element kind="rectangle" key="second.rectangle" y="200" width="555" height="90">
  <pen lineWidth="4.0"/>
</element>
<element kind="rectangle" key="third.rectangle" y="300" width="555" height="90">
  <pen lineWidth="4.0"/>
</element>
```

Notice the key attribute in each rectangle <element> tag. This attribute represents an user defined value that will help us locate the rectangle element inside the reports section later on.

Open the `src/AlterDesignApp.java` source file in the current sample and go to the `fill()` method corresponding to the `fill` ant task.
Notice how the compiled report template is first loaded from the *.jasper file.

```
File sourceFile = new File("target/reports/AlterDesignReport.jasper");
...
JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
```

Then for each of the three rectangles found in the title section of the report template, we change the forecolor and the backcolor using runtime random color codes. Each rectangle is looked up for inside the parent section using its unique user defined key value, as specified in the JRXML.

```
JRRectangle rectangle = (JRRectangle)jasperReport.getTitle().getElementByKey("first.rectangle");
rectangle.setForecolor(new Color((int)(16000000 * Math.random())));
rectangle.setBackcolor(new Color((int)(16000000 * Math.random())));
	
rectangle = (JRRectangle)jasperReport.getTitle().getElementByKey("second.rectangle");
rectangle.setForecolor(new Color((int)(16000000 * Math.random())));
rectangle.setBackcolor(new Color((int)(16000000 * Math.random())));
	
rectangle = (JRRectangle)jasperReport.getTitle().getElementByKey("third.rectangle");
rectangle.setForecolor(new Color((int)(16000000 * Math.random())));
rectangle.setBackcolor(new Color((int)(16000000 * Math.random())));
```

In addition to changing rectangle colors, we also change the font size and the font style to the first report style, as defined in the JRXML.

```
JRStyle style = jasperReport.getStyles()[0];
style.setFontSize(16f);
style.setItalic(Boolean.TRUE);
```

The in-memory report template is then passed to the report filling process, the result being a document which will have the rectangle colors and style properties as specified above, different from the static values they had in the original report template loaded from the file.

```
JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, (JRDataSource)null);
```

### Running the sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
To test this sample, open a command prompt and go to the /demo/samples/alterdesign folder of the JR project source tree. Type the following command:

```
> mvn clean compile exec:java -Dexec.args="compile fill view"
```

Every time you run this command, you should be seeing the same result, but with different random colors for the 3 rectangles.
