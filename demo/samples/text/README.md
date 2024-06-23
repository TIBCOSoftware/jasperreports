
# JasperReports - Text Export Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the pure text exporter could be used to export reports to simple text files.

### Main Features in This Sample

[Exporting to Pure Text Format](#textexport)

## <a name='textexport'>Exporting</a> to Pure Text Format
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to export reports to plain text files.

**Since:** 1.0.1

### Plain Text Export - Overview

One could imagine situations which require the text information from a JasperReport document to be saved separately as pure text document. In this case graphics, images, styles and pixel perfectness are not conserved. Text elements are converted into simple texts to be written in documents with predefined page widths and heights, measured in characters.

The [JRTextExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/JRTextExporter.html) implementation is intended to serve to this purpose. Users can specify the desired page width and height (in characters), then the engine will try to best fit text elements into the corresponding text page. Taking into account that positions and sizes are measured in pixels in a JasperReports template, and page dimensions in a text document are set in number of characters, some precautions must be taken when creating reports for text export:

- Report sizes and text page sizes should be divisible (for example, specify a template width of 1,000 pixels and a page width of 100 characters, resulting in a character width of 10 pixels).
- Text element sizes should also follow the preceding rule (for example, if the character height is 10 pixels and a particular text element is expected to span two rows, then the text element should be 20 pixels tall).
- For the most accurate results, text elements should be aligned in a grid-like fashion.
- Text elements having their height smaller than the character height will be not exported.
- Text elements having their width smaller than a minimum required width will be truncated. For a text element that contains a sequence of 20 characters, if the character width is 10 and the element width is 80, then only the first eight characters will be displayed. The minimum required width for the text element would be 200.

### Plain Text Export - Parameters And Properties

The basic idea when exporting to plain text output is to find the appropriate pixel/character ratio and convert sizes in the report in order to generate enough space for the exported text.
The following exporter configuration settings and properties are very helpful to accomplish such a task:

- [`getCharWidth()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#getCharWidth()) or [PROPERTY_CHARACTER_WIDTH](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#PROPERTY_CHARACTER_WIDTH), representing the pixel/character horizontal ratio
- [`getCharHeight()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#getCharHeight()) or [PROPERTY_CHARACTER_HEIGHT](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#PROPERTY_CHARACTER_HEIGHT), representing the pixel/character vertical ratio
- [`getPageWidthInChars()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#getPageWidthInChars()) or [PROPERTY_PAGE_WIDTH](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#PROPERTY_PAGE_WIDTH), representing the page width (in characters)
- [`getPageHeightInChars()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#getPageHeightInChars()) or [PROPERTY_PAGE_HEIGHT](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextReportConfiguration.html#PROPERTY_PAGE_HEIGHT), representing the page height (in characters)

Other useful configuration settings are:

- [CHARACTER_ENCODING](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/WriterExporterOutput.html#CHARACTER_ENCODING) or [PROPERTY_CHARACTER_ENCODING](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/WriterExporterOutput.html#PROPERTY_CHARACTER_ENCODING), representing the character encoding for the text document.
- [`getPageSeparator()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextExporterConfiguration.html#getPageSeparator()), containing the text that will be inserted between pages of the generated report, as page separator.
- [`getLineSeparator()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/TextExporterConfiguration.html#getLineSeparator()), parameter that stores the line separator sequence.

### The Text Export Sample

The `demo/samples/text/reports/TextReport.jrxml` template contains an example of report design prepared for the plain text export.

One has to pay attention to the following properties defined in the report:

```
<property name="net.sf.jasperreports.export.text.character.width" value="7.238"/>
<property name="net.sf.jasperreports.export.text.character.height" value="13.948"/>
```

They assume that characters are 7.238px wide and 13.948px high. Having a page width of 798px and a page height of 1000px, it follows that pages in the generated text document will count 110 characters per line and 71 lines per page.

Also notice that element sizes are set enough large to make room for all their content to be represented in the plain text output.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/text ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/text/target/reports` directory.
