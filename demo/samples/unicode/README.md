
# JasperReports - Unicode Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how text in different languages could be used inside reports.

### Main Features in This Sample

[Creating Reports in Any Language Using Unicode Support](#unicode)

### Secondary Features

[Font Extensions](../fonts/README.md#fontextensions)

## <a name='unicode'>Creating</a> Reports in Any Language Using Unicode Support
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to create reports in any language using unicode support.

**Since:** 0.1.0

**Other Samples**\
[/demo/samples/fonts](../fonts/README.md)\
[/demo/samples/i18n](../i18n/README.md)

### Working with Texts in JasperReports

Working with texts needs some dedicated tools to process both character representations and text formatting properties. Any text can be considered as a character sequence with a particular representation structure. The text appearance consists in both layout (or paragraph) and font settings. But while in most cases the text layout remains invariant, font settings may change when running the report in different locales. As already known, different languages need different character sets with respect to specific characters representation.

Therefore, working with texts means first of all working with fonts. A detailed discussion about how to use fonts in JasperReports can be found in both [Fonts](../fonts/README.md#fonts) and [Font Extensions](../fonts/README.md#fontextensions) sections of the [Fonts](../fonts/README.md) sample.

One of the main features concerning the text content in a given report is the possibility to internationalize it. That means the same report can be run without any hardcoded modification in different localized environments, using different languages and other localization settings. The only thing to pay attention to is to provide an appropriate set of font files able to get correct representations for characters in report texts.

And here is where this sample comes to show us how to.

### Character Encodings

An important feature to consider when working with texts, especially if they are intended to be internationalized, is the character encoding. That's because different charsets provide their own character representation for the same character code.

Usually the encoding attribute is specified in the header of the JRXML file and is used at report compilation time to decode the XML content. For instance, if the report contains French words only and characters such as ç, é, â , then the `ISO-8859-1` (a.k.a `Latin-1`) encoding is sufficient:

```
  <?xml version="1.0" encoding="ISO-8859-1"?>
```

As seen above, ideally would be to choose the encoding fit to the minimal character set which can correctly represent all the characters in the document. But how about documents containing words in multiple languages? And how about internationalized texts?

Concerning multilanguage documents (i.e. documents containing words spelled in several languages), one should choose the encoding adapted to the minimal character set able to correctly represent all the characters in the document, even if they belong to different languages. One of the character encodings able to handle multilingual documents is the `UTF-8`, used as default encoding value by JasperReports.

As for internationalized texts, these are usually kept in resource bundle files rather than within the document itself. So, there are cases where the JRXML itself looks completely ASCII-compatible, but generated reports at runtime do contain texts totally unreadable with ASCII. As a consequence, for a certain type of document export formats (such as CSV, HTML, XML, text) one has to know the encoding for the generated document too.

More than that, different languages are supported by different character encodings, so each time we need to run a report in a localized environment, we have to know which is the most appropriate character encoding for the generated document language. In this case, the `encoding` property defined in the JRXML file itself might be no more useful.

To solve this kind of issues an export custom property was introduced:

```
net.sf.jasperreports.export.character.encoding
```

which defaults to UTF-8 too. This default value is set in the `default.jasperreports.properties` file.

For more specific options at export time, the [`getEncoding()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/WriterExporterOutput.html#getEncoding()) export output setting or the equivalent [PROPERTY_CHARACTER_ENCODING](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/WriterExporterOutput.html#PROPERTY_CHARACTER_ENCODING) property are also available.

### The Unicode Sample

The `UTF-8` encoding is one of the most recommended to be used with multilingual reports. It not only provides support for almost all standardized languages, but also is handling the very simple and powerful escape sequence `\uXXXX` for Unicode characters. Any character encoded with `UTF-8` can be represented using only its 4-digits hexadecimal code. For instance, the greek letter `Γ` can be written as `\u0393`.

When such a notation is encountered, the engine calls for the appropriate character representation in the character set, and only that particular character will be printed out.

The sample provides a multilingual document containing several pieces of text written using the Unicode escape sequence.

Localized Unicode pieces of text are stored in document parameters:

```
  <parameter name="GreekText" class="java.lang.String" forPrompting="false">
    <defaultValueExpression>
      \u0393 \u0394 \u0398 \u039B \u039E \u03A0 \u03A3 \u03A6 \u03A8 \u03A9
    </defaultValueExpression>
  </parameter>
  <parameter name="CyrillicText" class="java.lang.String" forPrompting="false">
    <defaultValueExpression>
      \u0402 \u040B \u040F \u0414 \u0416 \u0418 \u041B \u0426 \u0429 \u042E
    </defaultValueExpression>
  </parameter>
  <parameter name="ChineseText" class="java.lang.String" forPrompting="false">
    <defaultValueExpression>
      \u6211\u559c\u6b22\u4e2d\u56fd\u98df\u7269
    </defaultValueExpression>
  </parameter>
  <parameter name="ArabicText" class="java.lang.String" forPrompting="false">
    <defaultValueExpression>
      \u0647\u0630\u0627 \u0639\u0631\u0636 \u0644\u0645\u062C\u0645\u0648\u0639\u0629 TextLayout
    </defaultValueExpression>
  </parameter>
  <parameter name="HebrewText" class="java.lang.String" forPrompting="false">
    <defaultValueExpression>
      \u05D0\u05E0\u05D9 \u05DC\u05D0 \u05DE\u05D1\u05D9\u05DF \u05E2\u05D1\u05E8\u05D9\u05EA
    </defaultValueExpression>
  </parameter>
```

When running the report, one can see that not only different languages can be grouped together on the same page, but they can be mixed into the same text element too:

```
<expression class="java.lang.String"><![CDATA[$P{GreekText} + "\n" + $P{CyrillicText}]]></expression>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/unicode ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/unicode/target/reports` directory.
