
# JasperReports - I18n Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>
Shows how a report could be rendered in different languages.

### Main Features in This Sample

[Internationalized Report Templates](#i18n)\
[Updating current date and slide numbers in PPTX export](#pptxfields)

## <a name='i18n'>Internationalized</a> Report Templates
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to generate reports in different languages using internationalization support.

**Since:** 0.6.2

###Internationalization Overview

This sample illustrates the best way to configure a report that need to be run in different places in the world.\
French customers would prefer to see their data reported in French, Chinese customers would like to see the same data translated into Chinese, and Greek customers would think that reporting the same data in Greek would be the best approach.

In such cases, writing the same report for each different language implies a lot of redundant work, which is not recommended. Only pieces of text differing from language to language should be written separately, and loaded into text elements at runtime, depending on locale settings. This is the purpose of the report internationalization.

Internationalized reports once written, can run everywhere. Paragraphs that need to be translated from a language to another are stored in localized resource bundle files, loaded into `java.util.ResourceBundle` instances. JasperReports lets you associate a `java.util.ResourceBundle` with the report template, either at design time (by using the `resourceBundle` attribute) or at runtime (by providing a value for the built-in [REPORT_RESOURCE_BUNDLE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRParameter.html#REPORT_RESOURCE_BUNDLE) parameter). If the report needs to be generated in a locale that is different from the current one, the built-in [REPORT_LOCALE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRParameter.html#REPORT_LOCALE) parameter can be used to specify the runtime locale when filling the report.

To facilitate the report internationalization, a special syntax is available inside report expressions to reference `java.lang.String` resources placed inside a `java.util.ResourceBundle` object associated with the report. The `$R{}` syntax is for wrapping resource bundle keys to retrieve the value for that key.

For formatting messages in different languages based on the report locale, a built-in method inside the report’s [JRCalculator](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRCalculator.html) offers functionality similar to the `java.text.MessageFormat` class. This method, `msg()`, has three convenient signatures that allow you to use up to three message parameters in the messages.

Also provided is the built-in `str()` method (the equivalent of the `$R{}` syntax inside the report expressions), which gives access to the resource bundle content based on the report locale.

For date and time formatting, the built-in [REPORT_TIME_ZONE](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRParameter.html#REPORT_TIME_ZONE) parameter can be used to ensure proper time transformations.

In the generated output, the library keeps information about the text run direction so that documents generated in languages that have right-to-left writing (like Arabic and Hebrew) can be rendered properly.

If an application relies on the built-in Swing viewer to display generated reports, then it also must be internationalized by adapting the button ToolTips or other texts displayed. This is very easy to do since the viewer relies on a predefined resource bundle to extract locale-specific information. The base name for this resource bundle is `net.sf.jasperreports.view.viewer`.

### Internationalization Example

Let's take a look into the /src folder of this sample. There are several .properties files containing localized messages corresponding to the same keys. For example, in the `i18n.properties` file (which is the default resource bundles file), we'll find a line having the key `text.message`, and the associated message `The program picked up {0} as a random number.`:

```
text.message=The program picked up {0} as a random number.
```

In the localized `i18n_pt_PT.properties` file, the same line will read:

```
text.message=O programa escolheu acima {0} como um número aleatório.
```

Images can be also localized. There are several `.gif` files in the `/src` folder, containing images of national flags. When a report locale is set, the corresponding national flag image will be loaded and inserted into the generated report. The engine reads the localized `.gif` name using the `image.flag` key present in the resource bundle files.

Now, in the `reports/I18nReport.jrxml` template, the root name of the default resource bundle is specified using the `resourceBundle` attribute in the `<jasperReport />` element:

```
resourceBundle="i18n"
```

In order to set localized texts or images in their placeholders, either the `$R{}` syntax or the related `msg()` or `str()` methods should be used.

For images (the `$R{}` syntax):

```
<element kind="image" positionType="Float" x="20" y="30" width="100" height="50" scaleImage="Clip" onErrorType="Error">
  <expression><![CDATA[$R{image.flag}]] ></expression>
</element>
```

For texts (the `$R{}` syntax):

```
<element kind="textField" positionType="Float" x="20" y="100" width="530" height="20" hTextAlign="Justified" ...>
  <expression><![CDATA[$R{text.paragraph1}]] ></expression>
</element>
```

or (the `msg()` method):

```
<element kind="textField positionType="Float" x="20" y="210" width="530" height="20" textAdjust="StretchHeight" fontSize="14.0" ...>
  <expression><![CDATA[msg($R{text.message}, $P{number})]] ></expression>
</element>
```

When you run the sample in order to visualize it from within the built-in Swing viewer (by typing the

```
mvn clean compile exec:java -Dexec.args="compile fill view"
```

command in a command line), you will be asked for the report locale twice. That's because your first choice is used at fill time in order to generate the localized report, and the second choice is used for customizing the viewer itself.

For example, if you choose first the `fr_FR - français (France)` from the drop-down list, and then `pt_PT - português (Portugal)`, your report will be written in French, but all controls in the viewer (buttons, text boxes, drop-down lists) will have Portuguese tooltips.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/i18n` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/i18n/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='pptxfields'>Updating</a> current date and slide numbers in PPTX export
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how to use PPTX fields to update some predefined values such as current date or slide number.

**Since:** 6.8.0

**Other Samples**\
[/demo/samples/jasper](../jasper/README.md)

### PPTX field example

The following example of a datetime PPTX field property can be found in the `i18n.jrxml` file:

```
<element kind="textField" positionType="Float" x="20" width="530" height="20" hTextAlign="Right" pattern="EEEE, MMMM dd, yyyy" fontSize="14.0">
  <expression><![CDATA[new Date()]] ></expression>
  <property name="net.sf.jasperreports.export.pptx.field.type" value="datetime"/>
</element>
```

At export time this text element will be translated into a PPTX field that generates automatic current date.

Another example of a slidenum PPTX field can be found in the [jasper]((../jasper/README.md)) sample.
