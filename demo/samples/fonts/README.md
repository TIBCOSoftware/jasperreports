
# JasperReports - Fonts Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how to work with fonts.

### Main Features in This Sample

[Fonts](#fonts)\
[Font Extensions](#fontextensions)

## <a name='top'>Fonts</a>
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to format texts using fonts properties.

**Since:** 0.1.0

### Characters and Fonts

Any text is represented by a character sequence with particular formatting settings. Some of these appearance settings can be specified using the attributes available in the <element/> tag. If there are shared font settings among several text elements, we strongly encourage people to group them in report styles defined in style templates (see the [Templates](../templates/README.md) sample).

The main font settings available in JasperReports are:

- `fontName` - the font name, which can be the name of a physical font or a logical one.
- `size` - the size of the font measured in points. It defaults to `10`.
- `bold` - flag specifying if a **bold** font is required. It defaults to `false`.
- `italic` - flag specifying if an *italic* font is required. It defaults to `false`.
- `underline` - flag specifying if the <ins>underline</ins> text decoration is required. It defaults to `false`.
- `strikeThrough` - flag specifying if the ~~strikethrough~~ text decoration is required. It defaults to `false`.
- `pdfFontName` - the name of an equivalent PDF font required by the OpenPDF library when exporting documents to PDF format.
- `pdfEncoding` - the equivalent PDF character encoding, also required by the OpenPDF library.
- `pdfEmbedded` - flag that specifies whether the font should be embedded into the document itself. It defaults to `false`.

### Character Encoding

Another important feature to consider when working with texts, especially if they are intended to be internationalized, is the character encoding. That's because different charsets provide their own character representation for the same character code.\
The default document encoding value is `UTF-8`.\
For more information about how to set the character encoding, please consult the Unicode sample.

### Default Fonts and Inheritance

Another interesting feature is that each text element inherits font and style attributes from its parent element. And each parent element inherits these attributes from its parent, etc. If no styles and/or fonts are defined for elements, the default style declared in the `<jasperReport/>` root element will be applied.

A default style is characterized by the `default` flag attribute. For instance:

```
<style name="Sans_Normal" default="true" fontName="DejaVu Sans" fontSize="8" bold="false" italic="false" underline="false" strikeThrough="false"/>
```

However, defining default styles or fonts in JasperReports is not mandatory. If no font is defined for a given element, the engine looks either for the inherited font attributes, or, if no attributes are found on this way, it looks for the

```
net.sf.jasperreports.default.font.name
```

property in the `/src/default.jasperreports.properties` file. Its value defines the name of the font family to be used when font properties are not explicitly defined for a text element or inherited from its parent.

The main default font properties and their values defined in the `/src/default.jasperreports.properties` file are:

- `net.sf.jasperreports.default.font.name=SansSerif` - the default font name.
- `net.sf.jasperreports.default.font.size=10` - the default font size.
- `net.sf.jasperreports.default.pdf.font.name=Helvetica` - the default PDF font.
- `net.sf.jasperreports.default.pdf.encoding=Cp1252` - the default PDF character encoding.
- `net.sf.jasperreports.default.pdf.embedded=false` - by default PDF fonts are not embedded

The `bold`, `italic`, and all other text decorations properties are missing, which means that default fonts are not bold, not oblique and not decorated.

### The Fonts Sample

This sample shows some practical examples of using fonts and font attributes in order to get a particular text appearance.

Because this sample uses a font extension based on the `Lobster Two` open source font files, and logical JVM font names also, it is strongly recommended to consult first the [Font Extensions](#fontextensions) section below, and then to compile and run the sample.

In the example below, a series of font attributes are defined for the static text element:

```
<element kind="staticText" y="350" width="150" height="40" fontName="Monospaced"
    pdfFontName="Courier-Oblique" italic="true" underline="true" fontSize="12.0">
  <text><![CDATA[The quick brown fox jumps over the lazy dog.]] ></text>
</element>
```

One can say that this text will use a monospaced character set, 12 pts sized, underlined and oblique, and when exporting to PDF format, the equivalent fonts will be `Courier-Oblique`.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/fonts` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/fonts/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='fontextensions'>Font</a> Extensions
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to ship the required fonts with your report templates when deploying them in the target application, using font extensions.

**Since:** 3.1.3

**Other Samples**\
[/demo/samples/unicode](../unicode/README.md)

### About Fonts Extension

- Formerly used font definitions relied on font files available on the machine. In this case, when defining how a piece of text should look like, one had to take care about the following possible issues:
- The needed font library might not be available to the JVM at runtime because the font file is not installed on the system.

When a font library is not available, the local JVM will replace it with some default fonts, and this could lead to various side effects, such as totally different text appearance or truncated pieces of text.

It's obviously that running a report in this kind of approach becomes completely dependent on the local environment, and one have to ensure that required font files are installed on the machine where the report is run. If they aren't, they should be installed first. And that's what should be done on every machine running the report. Quite a little bit embarrassing, isn't it.

Therefore, this is not the best way to control fonts in JasperReports. A much better one is due to the extension points support, available in JasperReports. Font files can be provided as library extensions. In a very simple manner, making a font extension consists in putting various True Type Font files in a JAR file together with a properties file describing the content of the JAR, and an XML file defining relationships between fonts and locales.

### Font Extensions Step By Step

Let's take a look into the `/ext/fonts` directory. It contains the `DejaVu` font extension available as default font for all samples shipped with the JasperReports project distribution package.

As known from extensions support, any JasperReports extension provides a `jasperreports_extension.properties` file in its root package, required by the JasperReports extension mechanism. This file is used to describe the content of the extension JAR file and consists in the following lines:

```
net.sf.jasperreports.extension.registry.factory.simple.font.families=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory
net.sf.jasperreports.extension.simple.font.families.dejavu=net/sf/jasperreports/fonts/fonts.xml
```

The [SimpleFontExtensionsRegistryFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fonts/SimpleFontExtensionsRegistryFactory.html) class represents an implementation of the [ExtensionsRegistryFactory](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/extensions/ExtensionsRegistryFactory.html) interface, used to instantiate a font extension registry.
The extension registry obtained from the factory is able to return a list of actual extension point implementations based on extension point class type.\
The second line provides the path to the XML file describing the actual font extension. The XML file in this case is named `fonts.xml`.

The main unit in the `fonts.xml` file is the `<fontFamily/>` element. A font family is an object instance which extends the [FontFamily](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fonts/FontFamily.html) public interface. This is the point where font extensions can be connected with the JasperReports engine.

Font families described in the `fonts.xml` file consist in up to 4 font faces: `normal, bold, italic` and `bolditalic`. A font face is described by the [FontFace](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fonts/FontFace.html) interface.

In order to completely describe a font family extension, one have to indicate the mapping between the font faces and font files, the `pdfEncoding` and `pdfEmbedded` attributes, equivalent font face names available for dedicated exporters, such as the HTML exporter) and a list of supported locales, because font files usually support only certain languages.

The fonts.xml file includes 3 different font families:

- `DejaVu Sans`
- `DejaVu Serif`
- `DejaVu Sans Mono`

and some mappings for logical JVM fonts available for HTML exporters:

- `SansSerif`
- `Serif`
- `Monospaced`

Let's take a look at the `DejaVu Sans` family. All the font family settings already discussed can be found in the example below:

```
  <fontFamily name="DejaVu Sans">
    <normal>net/sf/jasperreports/fonts/dejavu/DejaVuSans.ttf</normal>
    <bold>net/sf/jasperreports/fonts/dejavu/DejaVuSans-Bold.ttf</bold>
    <italic>net/sf/jasperreports/fonts/dejavu/DejaVuSans-Oblique.ttf</italic>
    <boldItalic>net/sf/jasperreports/fonts/dejavu/DejaVuSans-BoldOblique.ttf</boldItalic>
    <pdfEncoding>Identity-H</pdfEncoding>
    <pdfEmbedded>true</pdfEmbedded>
    <exportFonts>
      <export key="net.sf.jasperreports.html">'DejaVu Sans', Arial, Helvetica, sans-serif</export>
    </exportFonts>
    <!--
    <locales>
      <locale>en_US</locale>
      <locale>de_DE</locale>
    </locales>
    -->
  </fontFamily>
```

The name attribute and the `<normal/>` font face represent required elements in a font family definition, while all the others are optional.

The name of the font family will be used as the `fontName` attribute of the text element or style in the report template. The `fontName` together with the bold and italic attributes of the text field or style in the report help to locate and load the appropriate font face from the family. If a particular font face is not present or declared in the family, then the normal font face will be used instead.

In the example above we can see the mappings for the bold, italic and bolditalic font styles are also present. The `<exportFonts/>` tag instructs the HTML exporters to correlate this font family with other HTML supported font families, such as Arial, Helvetica, sans-serif.

The `<locales/>` contains a list of supported locales. This block being commented, the engine will try to apply this font family for any locale, without taking into account if the font file really provides support for that locale. If a particular locale is not supported, errors might occur at runtime and characters might be misrepresented.

However, if a given font family needs to be represented for locales supported by different font files, one can define separate font families in the XML file, having the same name but with differing `<locales/>` tag. This feature is very useful when the same report has to be run in both Japanese and Chinese, because there is no TTF file that simultaneously supports these two languages.

The `<pdfEncoding/>` and `<pdfEmbedded/>` are used to specify the PDF encoding attribute and the PDF embedding flag, and people are strongly encouraged to use them instead of deprecated `pdfEncoding` and `pdfEmbedded` attributes available in the JRXML <element/> tag.

Now, let's take a look at logical JVM fonts mappings:

```
  <fontFamily name="SansSerif">
    <exportFonts>
      <export key="net.sf.jasperreports.html">'DejaVu Sans', Arial, Helvetica, sans-serif</export>
    </exportFonts>
  </fontFamily>
  <fontFamily name="Serif">
    <exportFonts>
      <export key="net.sf.jasperreports.html">'DejaVu Serif', 'Times New Roman', Times, serif</export>
    </exportFonts>
  </fontFamily>
  <fontFamily name="Monospaced">
    <exportFonts>
      <export key="net.sf.jasperreports.html">'DejaVu Sans Mono', 'Courier New', Courier, monospace</export>
    </exportFonts>
  </fontFamily>
```

Here the DejaVu font families are added to the font families list available for HTML at export time.

Once you have the TTF files, the jasperreports_extension.properties and `fonts.xml` files, you can pack them together in a JAR file, and then put the JAR in your application's classpath, in order to make the new fonts available to your reports, wherever the application might run.

For more details about deploying fonts as extensions, you can take a look at the [fonts](../fonts) sample provided with the JasperReports project distribution package, which adds one more font extension for another open source font called `Lobster Two`. Running the sample using the

``` 
> mvn clean compile exec:java -Dexec.args="compile fontsXml"
```

command will generate in the `demo/samples/fonts/build/reports` a font extension xml file named `fonts.xml`. This file contains all font families available in the already installed font extensions.
