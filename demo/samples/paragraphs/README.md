
# JasperReports - Paragraph Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how to work with paragraphs.

### Main Features in This Sample

[Paragraphs](#paragraphs)

## <a name='paragraphs'>Paragraphs</a>
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to format texts using paragraph properties.

**Since:** 4.0.2

### Working with Paragraphs

When we place text content in a report element, we may need to make it look like a distinct paragraph. The information needed in this case can be retrieved either from the element's style (where we have attributes for text alignment), or from the `<paragraph/>` tag that is available within `<style/>` and/or `<element/>`.

Example:

```
  <element kind="textField" hTextAlign="Right">
    <paragraph lineSpacing="Fixed" lineSpacingSize="20.0">
      <tabStop position="20" alignment="Center"/>
      <tabStop position="40" alignment="Center"/>
    </paragraph>
  <expression>...</expression>
</element>
```

### Paragraph Alignment

A paragraph can be aligned both horizontally and vertically.\
For the horizontal alignment we can choose one of the following options:

- set the` hTextAlign` attribute in the `<element/>` tag or in a report style that will be applied to the text element
- set the `net.sf.jasperreports.style.hTextAlign` style feature property per report element

Possible values for the above settings are stored in [HorizontalTextAlignEnum](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/type/HorizontalTextAlignEnum.html) class:

- `Left` - default value
- `Center`
- `Right`
- `Justified`

For the vertical alignment, the following options are available:

- set the `vTextAlign` attribute in the `<element/>` tag or in a report style that will be applied to the text element
- set the `net.sf.jasperreports.style.vTextAlign` style feature property per report element

Possible values for the vertical text alignment are stored in [VerticalTextAlignEnum](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/type/VerticalTextAlignEnum.html) class:

- `Top` - default value
- `Middle`
- `Bottom`
- `Justified`

Examples:

```
<style name="MyParagraphStyle" hTextAlign="Center" vTextAlign="Middle" ... />
...
 <element kind="textField" style="MyParagraphStyle"...>
  <expression>...</expression>
 </element>
```

or:

```
 <element kind="textField"  hTextAlign="Center" vTextAlign="Middle"...>
  <expression>...</expression>
 </element>
```

or:

```
 <element kind="textField" ...>
  <expression>...</expression>
  <property name="net.sf.jasperreports.style.hTextAlign" value="Center"/>
  <property name="net.sf.jasperreports.style.vTextAlign" value="Middle"/>  
 </element>
```

### Line Spacing

For a given paragraph one can apply various settings for line spacing, as attributes of the `<paragraph/>` tag associated with the text element:

- `lineSpacing` - describes the type of line spacing for the paragraph. Possible values for this attribute are stored in the [LineSpacingEnum](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/type/LineSpacingEnum.html) class:
    - `Single` - normal spacing between lines; this is the default value.
    - `1_1_2` - spacing between lines set to 50% more than normal
    - `Double` - spacing between lines set to double normal size
    - `AtLeast` - spacing between lines set at least to a specified size indicated by `lineSpacingSize` attribute
    - `Fixed` - spacing between lines set exactly as specified by `lineSpacingSize` attribute
    - `Proportional` - spacing between lines set to a specified proportion of the normal line spacing
- `lineSpacingSize` - numeric (floating-point) value that specifies the line spacing size to be used in combination with the above `lineSpacing` type
- `spacingBefore` - refers to the spacing before the paragraph (in pixels)
- `spacingAfter` - refers to the spacing after the paragraph (in pixels)

Example:

```
  <element kind="textField" style="indentStyle" ...>
    <paragraph lineSpacing="Fixed" lineSpacingSize="20.0" style="indentStyle">
      <tabStop position="33" alignment="Center"/>
    </paragraph>
    <expression>...</expression>
  </element>
```

### Paragraph Indentation

JasperReports also supports paragraph indentation, which can be customized using the following attributes of the `<paragraph/>` tag:

- `firstLineIndent` - represents the indent (in pixels) of the first line of text
- `leftIndent` - represents the left indent (in pixels) of the paragraph
- `rightIndent` - represents the right indent (in pixels) of the paragraph

Example:

```
  <style name="indentStyle" mode="Transparent" backcolor="#FFFFFF" style="myDefault">
    <paragraph firstLineIndent="20" leftIndent="35" rightIndent="20" style="myDefault"/>
    <box style="myDefault">
      <pen lineWidth="1.0"/>
    </box>
  </style>
```

### Tab Stops

In a given text element we can configure custom tab stops as well, by placing a sequence of `<tabStop/>` elements inside a `<paragraph/>` tag. A custom tab stop is completely characterized by its position and alignment:

- `position` - is required; represents the position (in pixels) of the custom tab stop
- `alignment` - optional; represents the alignment of the custom tab stop. Possible values for this attribute are (see [TabStopAlignEnum](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/type/TabStopAlignEnum.html)):
    - `Left` - default value
    - `Center`
    - `Right`

Another useful setting is the tab stop width (in pixels) of the paragraph, that can be customized using the `tabStopWidth` attribute of the `<paragraph/>`.

Examples:

```
<paragraph lineSpacing="Fixed" lineSpacingSize="20.0" tabStopWidth="20"/>
```

or

```
<paragraph lineSpacing="Fixed" lineSpacingSize="20.0">
  <tabStop position="20"/>
  <tabStop position="40" alignment="Center"/>
  <tabStop position="80" alignment="Right"/>
</paragraph>
```

### Default Values for Paragraph Settings

In case we need the same paragraph settings to be applied for all reports that share the same context, we can use the following properties that can be set on the report context:

- `net.sf.jasperreports.default.line.spacing.size` - sets the default value for the line spacing size
- `net.sf.jasperreports.default.first.line.indent` - sets the default value for the first line indent
- `net.sf.jasperreports.default.left.indent` - sets the default value for the left indent
- `net.sf.jasperreports.default.right.indent` - sets the default value for the right indent
- `net.sf.jasperreports.default.spacing.before` - sets the default value for the spacing before paragraph
- `net.sf.jasperreports.default.spacing.after` - sets the default value for the spacing after paragraph
- `net.sf.jasperreports.default.tab.stop.width` - sets the default value for the tab stop width

Following are the default values as stored in the `default.jasperreports.properties` file:

```
net.sf.jasperreports.default.line.spacing.size=1
net.sf.jasperreports.default.first.line.indent=0
net.sf.jasperreports.default.left.indent=0
net.sf.jasperreports.default.right.indent=0
net.sf.jasperreports.default.spacing.before=0
net.sf.jasperreports.default.spacing.after=0
net.sf.jasperreports.default.tab.stop.width=40
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/paragraphs` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/paragraphs/target/reports` directory.
