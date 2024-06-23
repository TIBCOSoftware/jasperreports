
# JasperReports - Templates Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how to work with style templates.

### Main Features in This Sample

[Style Templates](#templates)

## <a name='templates'>Style</a> Templates
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to reuse report style definitions from external style templates.

**Since:** 2.0.0

**Other Samples**\
[/demo/samples/query](../query/README.md)

### Styles in JasperReports

In a given report, elements can have different appearances. They can have borders, foreground and background colors, alignments, text decorations, etc. JasperReports provides the `<style />` element in order to manage all these appearance characteristics.

A `<style />` element is defined by the following attributes:

- `default` - indicates whether this style is the document's default style
- `style` - a reference to the parent style
- `mode` - sets the element's transparency. Possible values are `Opaque` and `Transparent`.
- `forecolor` - the foreground color
- `backcolor` - the background color
- `fill` - sets the fill mode. At the moment the single value allowed is `Solid`.
- `radius` - sets the radius value, if necessary.
- `scaleImage` - sets the scale for images only. Possible values: `Clip, FillFrame, RetainShape, RealHeight, RealSize`.
- `hTextAlign` - sets the text horizontal alignment. Possible values: `Left, Center, Right, Justified`.
- `vTextAlign` - sets the text vertical alignment. Possible values: `Top, Middle, Bottom`.
- `hImageAlign` - sets the image horizontal alignment. Possible values: `Left, Center, Right`.
- `vImageAlign` - sets the image vertical alignment. Possible values: `Top, Middle, Bottom`.
- `rotation` - sets the element's rotation. Possible values: `None, Left, Right, UpsideDown`.
- `lineSpacing` - sets the line spacing for text elements only. Possible values: `Single, 1_1_2, Double`.
- `markup` - sets the markup style for styled texts
- `fontName` - sets the font name
- `fontSize` - sets the font size
- `bold` - indicates if the font style is bold
- `italic` - indicates if the font style is italic
- `underline` - indicates if the font style is underline
- `strikeThrough` - indicates if the font style is strikethrough
- `pdfFontName` - sets the related PDF font name
- `pdfEncoding` - sets the character encoding for the PDF output format
- `pdfEmbedded` - indicates if the PDF font is embedded
- `pattern` - sets the format pattern for formatted texts
- `blankWhenNull` - indicates if a white space should be shown if the text is not present

In a `style` element can be set also the element's `<box />` and `<pen />` styles.

In some situations, a style should be applied only if a certain condition is met. For example, consecutive rows in a table should alternate their background colors. JasperReports provides conditional styles in order to manage conditions for styles.

### Style Templates

Any type of report element can reference a report style definition using its `style` attribute.\
By doing so, all the style properties declared by the style definition that are applicable to the current element will be inherited. Style properties specified at the report element level can be used to override the inherited values.

But what if there are several reports that require the same report style to be applied? One can write the same report style definition in each report, but this is not an optimized approach. Better than that, and recommended, is to use style templates instead.

Report styles can also be defined in external style template files that are referenced by report templates. This allows report designers to define in a single place a common look for a set of reports.

A style template is an XML file that contains one or more style definitions. A template can include references to other style template files, hence one can organize a style library as a hierarchical set of style template files.

Style template files use by convention the `*.jrtx` extension, but this is not mandatory.

The `<jasperTemplate />` element is the root of a style template file. The `<template />` element is used to include references to other template files; the contents of this element is interpreted as the location of the referred template file.

The `<style />` element is identical to the element with the same name from report design templates (JRXML files).

**Note:** Conditional styles may be defined in style templates only when the condition expression is a simple reference to a Boolean report parameter, field or variable (see the [Query](../query/README.md#conditionalStylesInTemplates) sample).

This limitation is caused by the fact that more complex expressions can only be interpreted in the context of a single report definition.

Each style must specify a name, and the style names have to be unique inside a style template.
Also, styles defined in style templates can be used as parent styles for styles defined in the report.

The following is an example of a styles template file which refers a base template file named `base_styles.jrtx`, overrides 2 parent styles and defines other 3 own styles:

```
<jasperTemplate>
  <template>base_styles.jrtx</template>
  <style name="Regular" fontSize="12.0" default="true" style="Base"/>
  <style name="Special Emphasis" forecolor="#FF0000" fontSize="12.0" style="Emphasis"/>
  <style name="Strong" fontSize="14.0" underline="true" style="Base"/>
  <style name="Serif" fontName="DejaVu Serif" fontSize="12.0" style="Base"/>
  <style name="Serif Note" underline="true" style="Serif"/>
</jasperTemplate>
```

A report can use style templates by explicitly referring them in its definition. References to a style templates are included in JRXML reports as `<template />` elements. Such an element contains an expression that is resolved at fill time to a style template instance.

The `template` expression can only use constants/literals and report parameters. Variables and fields cannot be used because the template expressions are evaluated before the report calculation engine is initialized. If the `template` expression evaluates to null, the engine ignores the template reference.

Below is an example of how to reference a style template in a report:

```
<jasperReport ...>
  <template>”report_styles.jrtx”</template>
  <!-- parameters can be used in style template expressions -->
  <template>$P{BaseTemplateLocation} + ”report_styles.jrtx”</template>
  <template class=”java.net.URL”>$P{StyleTemplateURL}</template>
  <parameter name=”BaseTemplateLocation”/>
  <parameter name=”StyleTemplateURL” class=”java.net.URL”/>
  ...
```

Style template locations are interpreted in the same manner as image or subreport locations, that is, the engine attempts to load the location as an URL, a disk file or a classpath resource. The `class` attribute in the `<template />` element represents the class for the style template source expression. Possible values are:

```
java.lang.String
java.io.File
java.net.URL
java.io.InputStream
net.sf.jasperreports.engine.JRTemplate
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/templates ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/templates/target/reports` directory.
