
# JasperReports - Markup Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the text markup feature works in static and dynamic text elements.

### Main Features in This Sample

[Creating Styled Text Using a Markup Language](#markup)

### Secondary Features

[Styled Text](../styledtext/README.md#styledtext)

## <a name='markup'>Creating</a> Styled Text Using a Markup Language
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to format text using a markup language such as HTML and/or RTF.

**Since:** 2.0.5

### Structured Text Content

In order to achieve a particular look and feel, the text content in text elements can be customized with several style attributes such as text foreground color, text background color, font name, font size, font weight, etc. Some of these attributes (forecolor, backcolor) can be either set independently as report element attributes, or inherited from the style attribute. If set, these style attributes do affect all the text content in the report element.

But in some cases there is no need to apply a style to the entire text content. Users may want to highlight only few words within the text, without affecting the rest of the paragraph. There are situations when some words are needed in bold style, others require a specific font, and some characters have to be printed with a given color.

In such cases, the solution is to use specific markup tags inside the text content, and to instruct the engine that the text content within the report element is no longer pure text, but structured XML content with specific processing rules. The XML content either includes style information in the text itself, or some other form of markup language.

### The `markup` Attribute

Here comes the `markup` option attribute to accomplish its job. It specifies type of markup language that will be used inside the text element, and may have the following values:

- `none` - The content of the text element is plain text. There is nothing to be processed. This is the default value.
- `styled` - The content of the text element is styled text, the JasperReports proprietary XML type of markup text. It overrides the deprecated `isStyledText` attribute. (See the [Styled text](../styledtext/README.md) sample)
- `html` - The content of the text element is Hyper Text Markup Language
- `rtf` - The content of the text element is Rich Text Format

If the `html` or `rtf` values are set, then the engine calls a dedicated markup processor class based on the use of the javax.swing.JEditorPane processor. The structured XML fragment is parsed according to the processing rules, and its content gets formatted as included style attributes require to be.

### The Markup Sample

This sample contains 3 structured text paragraphs.\
The first one is formatted using the `markup="styled"` value\:

```
<element kind="staticText" mode="Opaque" width="555" height="110" backcolor="#C0C0C0" markup="styled" fontSize="16.0"> 
  <text><![CDATA[This is a <style isBold="true" isItalic="true" isUnderline="true">static text</style> element containing 
    styled text. <style backcolor="yellow" isBold="true" isItalic="true">Styled text</style> elements are introduced by setting the 
    <style forecolor="blue" isItalic="true">markup</style> attribute available for the 
    <style isBold="true" forecolor="magenta">textElement</style> 
    tag to <style forecolor="red" isItalic="true">styled</style> and by formatting the text content using 
    nested <style isBold="true" forecolor="green">style</style> tags and simple HTML tags, 
    including <a type="Reference" href="http://jasperreports.sf.net" target="Blank"> 
    <style isItalic="true" isBold="true" isUnderline="true" forecolor="yellow">hyperlinks</style></a>.]] ></text>
</element>
```

The second paragraph is read from the `rtf.txt` file in the `/reports` folder and stored in the `$P{RtfText}` parameter. Its content gets decoded using the RTF markup processor\:

```
<element kind="textField"positionType="Float" mode="Opaque" y="120" width="555" height="110" backcolor="#C0C0C0" markup="rtf" textAdjust="StretchHeight" fontSize="16.0">
  <expression><![CDATA[$P{RtfText}]] ></expression>
</element>
```

The third paragraph is read from the `html.txt` file in the `/reports` folder and stored in the `$P{HtmlText}` parameter. Its content gets decoded using the HTML markup processor\:

```
<element kind="textField" positionType="Float" mode="Opaque" y="240" width="555" height="110" backcolor="#C0C0C0" markup="html" textAdjust="StretchHeight" fontSize="16.0">
  <expression><![CDATA[$P{HtmlText}]] ></expression>
</element>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/markup` within the JasperReports source project and run the following command\:

```
> mvnn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/markup/target/reports` directory.

