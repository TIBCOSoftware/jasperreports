
# JasperReports - No Page Break Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how reports can be designed and exported in a way that hides the existence of page breaks.

### Main Features in This Sample

[Suppress Pagination](#nopagebreak)

## <a name='nopagebreak'>Suppress</a> Pagination
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to suppress pagination in generated documents.

**Since:** 1.0.1

### The `ignorePagination` Flag

Usually JasperReports generates page-oriented document layouts: each document is a collection of pages having the same width, height, and page orientation. However, if needed, multiple documents with particular page settings can be concatenated to produce a single document output with different page layouts, using the batch export facility.

In the case where no batch export is implied, all document pages share the same page settings, and they are by default separated by page breaks. At export time each exporter has its own way to handle the page break element in order to make visible the page separation.

But there are situations, especially for web-oriented applications, where pagination is irrelevant and it causes the document to be displayed improperly. For instance, when exporting documents to HTML format, one might prefer to display it as a continuous scrollable page rather than a succession of pages separated by visible empty spaces. Or, when exporting to Excel in a single sheet document, a layout without visible page breaks would be preferred.

This is why the engine needs to be instructed how to handle page breaks in various situations. A possibility would be to completely ignore the pagination, with no respect to the output document format. Responsible for this kind of approach is the `ignorePagination` flag property. It can be found among the `<jasperReport/>` element attributes:

```
<jasperReport ... ignorePagination="true" ...>
...
</jasperReport>
```

When `ignorePagination` is set to true, the report-filling engine will generate the document on a single, very long page. Running the generated report to any output format, a single page document will be visualized.

While this is the desired behavior for some document types, such as HTML, XML or XLS, it's quite uncomfortably for documents with page-oriented layout (like PDF, DOCX, etc) to be displayed as single very long pages, without the possibility to navigate between pages.

By default, `ignorePagination` is set to `false`. For various purposes this flag can be overridden at report filling time using the optional built-in [IS_IGNORE_PAGINATION](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRParameter.html#IS_IGNORE_PAGINATION) parameter.

### Handling Page Breaks at Export Time

When `ignorePagination` is `true` the report will be generated in a single page. And this layout will be irreversible. The resulting page can no more be fragmented back into multiple sections at runtime.

Therefore, using a `ignorePagination="true"` attribute works properly if the report will be definitely printed out in flow-oriented document layouts. In the case of multiple output document layout, the best choice is to let the pagination being handled at export time, each exporter providing its own handling mode.

In order to accomplish this, one have to let the `ignorePagination` with its default value (ie `false`). At fill time the document will be generated as multiple pages document. Then, at export time, the exporter could decide whether to print out the document as multipage, or to concatenate all pages into a single one, by suppressing all empty space between pages.

### Handling Page Breaks with HTML Exporter

When a multipage document gets exported into HTML format there are three possibilities to configure its layout:

- The document will be printed out page by page; only a single document page can be visualized at a time; one can navigate between pages using a navigation bar script.
- All document pages will be printed out in a sequence where page breaks appear as noticeable amount of empty space on the vertical axis. Sometimes this is not a very user-friendly webpage and removing the extra blank space between pages is required. In such a case a lot of help comes from the `net.sf.jasperreports.export.html.remove.emtpy.space.between.rows` exporter hint property. If this property is true, then all empty rows in the generated HTML table will be collapsed, leading to a completely flow-oriented layout. By default, the exporter preserves all the whitespace for pixel-perfect page layouts.
- All document pages will be printed out in a sequence where page breaks appear as a custom HTML separator. One can replace the default blank space between pages with a customized separator element (a horizontal line, for example). The HTML code fragment for the custom separator can be obtained using the [`getBetweenPagesHtml()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/HtmlExporterConfiguration.html#getBetweenPagesHtml()) export configuration setting.

### Handling Page Breaks with XLS Exporters

JasperReports provides several dedicated exporters to export documents in Excel formats. All of them generate Excel-compatible documents organized as workbooks which store inside one or more separated sheets. Because Excel documents are collections of sheets, one can consider that usually an Excel sheet is equivalent to a document page, and each XLS exporter should be able to translate a page content into an equivalent sheet content.

Like in HTML, there are multiple possibilities to handle sheet breaks when exporting to Excel format:

- The document will be printed as one page per sheet; each page will be printed out on a separate sheet in the workbook. This setting can be done using the [`PROPERTY_ONE_PAGE_PER_SHEET`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_ONE_PAGE_PER_SHEET) exporter hint property (or the equivalent [`isOnePagePerSheet()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#isOnePagePerSheet()) exporter configuration setting). If this property is true then one page per sheet will be printed out. By default this property is set to false.
- When [`PROPERTY_ONE_PAGE_PER_SHEET`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_ONE_PAGE_PER_SHEET) is not set, all document pages will be printed out into a single sheet. Page breaks appear as supplementary empty rows between pages. Like in HTML, the `net.sf.jasperreports.export.xls.remove.emtpy.space.between.rows` hint property (or the associated [`isRemoveEmptySpaceBetweenRows()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#isRemoveEmptySpaceBetweenRows()) exporter configuration setting) becomes very useful.\
If this property is `true`, then all empty rows in the generated sheet will be collapsed, leading to a completely flow-oriented layout. However, by default the property is `false`. The exporter preserves by default all the whitespace for pixel-perfect layouts.
- Another specific sheet-related feature (therefore available only for XLS exporters) is that any sheet can be broken into multiple sheet children, all of them having the same maximum number of rows. For instance, if a generated sheet contains 1234 rows, it can be broken into 3 sheets with maximum 600 rows. The first 2 sheets contain 600 rows each, and the third contains the remaining 34 rows. In this case, even the fill-time `ignorePagination` flag is `true`, it is still possible at export time to get multiple sheets starting with a single one, by limiting the maximum number of rows per sheet. This can be done using the [`PROPERTY_MAXIMUM_ROWS_PER_SHEET`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#PROPERTY_MAXIMUM_ROWS_PER_SHEET) (or the associated [`getMaxRowsPerSheet()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/XlsReportConfiguration.html#getMaxRowsPerSheet()) export configuration setting).

### Suppress Pagination Examples

This sample works with the default value for the `ignorePagination` flag, which means that the document will be generated into separate pages. Then, when the document gets exported, some exporters are instructed to suppress the pagination in specific ways.

In the `NoPageBreakApp.java` one can see how the export parameters responsible for page breaks handling are set.

- For the HTML exporter (see the `html()` method):

```
  SimpleHtmlExporterConfiguration exporterConfig = new SimpleHtmlExporterConfiguration();
  exporterConfig.setBetweenPagesHtml("");
  exporter.setConfiguration(exporterConfig);

  SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
  reportConfig.setRemoveEmptySpaceBetweenRows(true);
  exporter.setConfiguration(reportConfig);
```

- And for XLS and XLSX exporters (see `xls()` and `xlsx()` methods)
    - XLS:
    
```
  SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
  configuration.setOnePagePerSheet(false);
  configuration.setRemoveEmptySpaceBetweenRows(true);
  exporter.setConfiguration(configuration);
```

    - XLSX:
    
```
  SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
  configuration.setOnePagePerSheet(false);
  configuration.setRemoveEmptySpaceBetweenRows(true);
  exporter.setConfiguration(configuration);
```

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:

```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/nopagebreak` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/nopagebreak/target/reports` directory.

