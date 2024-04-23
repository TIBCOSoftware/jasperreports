
# JasperReports - Styled Text Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the styled-text feature works in static and dynamic text elements.

### Main Features in This Sample

[Styled Text](#styledtext)

### Secondary Features

[Creating Styled Text Using a Markup Language](../markup/README.md#markup)

## <a name='styledtext'>Styled</a> Text
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to format text using a styled text tags as markup language.

**Since:** 2.0.5

### Styled Text

The text content in text elements can be formatted using some particular style attributes such as foreground color, background color, font name, font size, font weight, etc. Document styles also contain text formatting attributes (forecolor, backcolor, etc) and can be used to customize the text appearance inside the report element. All these style attributes affect the entire text content in the report element.

But there are situations when one needs to apply a style only to some parts of the text paragraph. Users may want to highlight only few words within the text, with no affecting the rest of the paragraph.

The solution is to use of the `markup` attribute setting.
If `markup="styled"` is set, the engine gets notified that the text element contains a structured XML fragment with JasperReports proprietary processing rules.

### Styled Text Markup Tags

The JasperReports proprietary markup language is called styled text and is an XML based format in which the style of any portion of text inside a text element can be changed by embedding that portion inside a `<style>` tag or other simple HTML tag from the following list:

- `<b>`
- `<u>`
- `<i>`
- `<font>`
- `<sup>`
- `<sub>`
- `<li>`
- `<br>`

The `<style>` tag has various attributes for altering the color, font, or other style properties of the text. From the standard HTML `<font>` tag, only the fontFace, color, and size attributes are recognized by the JasperReports engine. Each individual style property is inherited from the overall text element or from the parent `<style>` tag when nested `<style>` tags are used.

### The Styled Text Sample

This sample contains various styled text paragraphs, illustrating the use of the styled text tags enumerated above.

The first paragraph is obtained by processing the following fragment:

```
    This is a <style isBold="true" isItalic="true" isUnderline="true">static text</style> element containing
	styled text. <style backcolor="yellow" isBold="true" isItalic="true">Styled text</style> elements are introduced by
	setting the <style forecolor="blue" isItalic="true">markup</style> attribute available for
	the <style isBold="true" forecolor="magenta">textElement</style> tag to <style forecolor="red" isItalic="true">styled</style> and
	by formatting the text content using nested <style isBold="true" forecolor="green">style</style> tags and simple HTML tags, including
	<a type="Reference" href="http://jasperreports.sf.net" target="Blank">
	<style isItalic="true" isBold="true" isUnderline="true" forecolor="yellow">hyperlinks</style></a>.
```

The next one is obtained by processing the following fragment:

```
	This text field element contains styled text displaying the text-only version of the
	<style size=\"12\" isBold=\"true\" forecolor=\"black\">^<style forecolor=\"#808080\">Jasper</style>
	<style forecolor=\"#990000\">Reports</style></style> logo and some <font size=\"10\"><sup>superscript</sup></font>
	text and <font size=\"10\"><sub>subscript</sub></font> text.
```

Then the text in the left column is obtained by processing the following fragment:

```
	Here is the list with the attributes supported by the <style isBold="true">style</style> 
	tag used to format the text content inside a styled text element: 

	fontName 
	<style size="32">size</style>
	<style isBold="true">isBold</style>
	<style isItalic="true">isItalic</style>
	<style isUnderline="true">isUnderline</style>
	<style isStrikeThrough="true">isStrikeThrough</style>
	pdfFontName 
	pdfEncoding 
	isPdfEmbedded 
	<style forecolor="red">forecolor</style>
	<style backcolor="blue" forecolor="white" isBold="true">backcolor</style>
```

The text in the column to the right is obtained by processing the following fragment:

```
	Here is the list with the supported HTML tags : 

	<font size="12">&lt;font size=&quot;12&quot;&gt;</font>
	<font color="red">&lt;font color=&quot;red&quot;&gt;</font>
	<font face="DejaVu Serif">&lt;font face=&quot;DejaVu Serif&quot;&gt;</font>
	<b>bold &lt;b&gt;</b> <i>italic &lt;i&gt;</i> <u>underlined &lt;u&gt;</u>
	an empty line follows (&lt;br/&gt;):
	<br/>
	<sup>&lt;sup&gt;</sup> and <sub>&lt;sub&gt;</sub>
	<br/>
	A bulleted list:
	<li>item 1 &lt;li&gt;</li>
```

The next paragraph is obtained by processing the following fragment:

```
	ampersand &amp; less &lt; greater &gt; quote &quot; apostrophe &apos;
	ampersand &amp; less &lt; greater &gt; quote &quot; apostrophe &apos;
```

And the last fragment is obtained by processing the following:

```
	<style size=\"16\">1</style>\n<style size=\"24\">2</style>\n<style size=\"32\">3</style>\n
	<style size=\"24\">2</style>\n<style size=\"16\">1</style>\n
```

For more information about the recommended text formatting solution, please take a look at the [Markup](../markup/README.md) Sample.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/styledtext ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/styledtext/target/reports` directory.
