
# JasperReports - Batch Export Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how multiple reports could be concatenated during export.

### Main Features in This Sample

[Exporting Multiple Reports into a Single Output File (Batch Export)](#batchexport)
				
## <a name='batchexport'>Exporting</a> Multiple Reports into a Single Output File (Batch Export)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Several reports can be exported together to form a single resulting document.

**Since	:** 0.6.0


### Exporter Input - API Overview

Once generated, a [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object may be exported to various output formats such as PDF, HTML, XML, CSV, RTF, Excel, MSWord, PPTX, etc. The JasperReports library includes a builtin exporter class for each output format enumerated here. An exporter should be able to handle a wide range of report sources, along with their specific export configuration settings. In order to perform the export, exporters require some specific input data:

- a single [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) or a list of [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects to be exported at a time; these objects may:
    - be in-memory objects
    - come from the network through an input stream
    - be loaded from files on disk
- a set of export configuration settings to be applied either globally or per each report in the list

Exporter builtin implementations acquire input data based on methods inherited from their [JRAbstractExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRAbstractExporter.html) super class. When extending the [JRAbstractExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRAbstractExporter.html) class, one can reuse the

```
public void setExporterInput(ExporterInput exporterInput)
```

method to deal with the report sources and export configuration settings.

As shown in the method signature, all we need is an [ExporterInput](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInput.html) object. This object must implement the

```
public List<ExporterInputItem> getItems()
```

method in order to retrieve a list of [ExporterInputItem](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInputItem.html) objects. Each [ExporterInputItem](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInputItem.html) in the list contains a single [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object along with its related export configuration settings. Methods in the [ExporterInputItem](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInputItem.html) interface are:

- `public JasperPrint getJasperPrint()` - return the [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object
- `public ReportExportConfiguration getConfiguration()` - return export configuration settings to be applied for the [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object

Global configuration settings per exporter can be configured using the

```
public void setConfiguration(C configuration)
```

method in the [JRAbstractExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRAbstractExporter.html) class. This method accepts an [ExporterConfiguration](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterConfiguration.html) object that contain common export settings available for the exporter itself; they do not depend on particular reports and will be applied globally during the export. An example of global setting is the use of custom color palette in the Excel output. This is a global setting for the Excel app itself, rather than a report setting, so it should be set globally per export.

Another way to provide configuration settings globally is to use the overloaded

```
public void setConfiguration(RC configuration)
```

method in the [JRAbstractExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRAbstractExporter.html) class. It requires a [ReportExportConfiguration](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ReportExportConfiguration.html) object. This object contains common report-specific export settings and will be applied during the report per each report in the list.

### Builtin implementations

Below is a list of the builtin implementations for the exporter input APIs (see [net.sf.jasperreports.export](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/package-summary.html) package):

- [ExporterInput](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInput.html) interface:

    - `SimpleExporterInput` class

- [ExporterInputItem](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInputItem.html) interface:

    - `SimpleExporterInputItem` class

- [ExporterConfiguration](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterConfiguration.html) interface:

    - `SimpleCsvExporterConfiguration` class - global setting for the CSV export
    - `SimpleCsvMetadataExporterConfiguration` class - global setting for the CSV metadata export
    - `SimpleDocxExporterConfiguration` class - global setting for the DOCX export
    - `SimpleGraphics2DExporterConfiguration` - global setting for the Graphics2D export
    - `SimpleHtmlExporterConfiguration` class - global setting for the HTML export
    - `SimpleJsonExporterConfiguration` class - global setting for the JSON export
    - `SimpleOdsExporterConfiguration` class - global setting for the ODS export
    - `SimpleOdtExporterConfiguration` class - global setting for the ODT export
    - `SimplePdfExporterConfiguration` class - global setting for the PDF export
    - `SimplePptxExporterConfiguration` class - global setting for the PPTX export
    - `SimplePrintServiceExporterConfiguration` class - global setting for the PrintService export
    - `SimpleRtfExporterConfiguration` class - global setting for the RTF export
    - `SimpleTextExporterConfiguration` class - global setting for the Text export
    - `SimpleXlsExporterConfiguration` class - global setting for the XLS export
    - `SimpleXlsMetadataExporterConfiguration` class - global setting for the XLS metadata export
    - `SimpleXlsxExporterConfiguration` class - global setting for the XLSX export


- [ReportExportConfiguration](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ReportExportConfiguration.html) interface:

    - `SimpleCsvReportConfiguration` class - report setting for the CSV export
    - `SimpleCsvMetadataReportConfiguration` class - report setting for the CSV metadata export
    - `SimpleDocxReportConfiguration` class - report setting for the DOCX export
    - `SimpleGraphics2DReportConfiguration` class - report setting for the Graphics2D export
    - `SimpleHtmlReportConfiguration` class - report setting for the HTML export
    - `SimpleJsonReportConfiguration` class - report setting for the JSON export
    - `SimpleOdsReportConfiguration` class - report setting for the ODS export
    - `SimpleOdtReportConfiguration` class - report setting for the ODT export
    - `SimplePdfReportConfiguration` class - report setting for the PDF export
    - `SimplePptxReportConfiguration` class - report setting for the PPTX export
    - `SimplePrintServiceReportConfiguration` class - report setting for the PrintService export
    - `SimpleRtfReportConfiguration` class - report setting for the RTF export
    - `SimpleTextReportConfiguration` class - report setting for the Text export
    - `SimpleXlsReportConfiguration` class - report setting for the XLS export
    - `SimpleXlsMetadataReportConfiguration` class - report setting for the XLS metadata export
    - `SimpleXlsxReportConfiguration` class - report setting for the XLSX export

### Batch Mode Export

The first thing an exporter needs to know is whether it is acting on a single [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) report or on a list with several [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects. Exporting multiple reports to a single resulting document is called batch mode exporting. Not all exporters can work in batch mode, but those that do first look into the exporter input data to see whether a list of several [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects has been supplied to them. If so, the exporter loops through this list of reports and produces a single document from them. If the exporters act on a single report, then they check whether a single [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object is supplied to the exporter input data to be exported. As already shown, this object may be loaded from an input stream, an URL, a file object, or a file name. If no [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object is supplied, then the exporter throws an exception telling that no input source was set for the export process.

### Batch Mode Bookmarks

When several [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) documents must be concatenated in the same PDF file by batch export, you can introduce PDF bookmarks in the resulting PDF document to mark the beginning of each individual document that was part of the initial document list.

These bookmarks have the same name as the original [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) document, specified by the `jasperPrint.getName()` property. However, users can turn on and off the creation of those bookmarks by using the following export configuration setting:

```
SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
configuration.setCreatingBatchModeBookmarks(true);  //or false, if needed
exporter.setConfiguration(configuration);
```

The exporter does not create such bookmarks by default.

### Code Samples

This sample illustrates how to deal with exporter input settings when multiple reports are exported in batch mode.

Looking into the `src/BatchExportApp.java` source file one can see that, first of all, 3 reports are filled to generate [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects:

```
public void fill() throws JRException
{
  long start = System.currentTimeMillis();
  JasperFillManager.fillReportToFile(
    "target/reports/Report1.jasper",
    null, 
    new JREmptyDataSource(2)
    );
  JasperFillManager.fillReportToFile(
    "target/reports/Report2.jasper",
    null, 
    new JREmptyDataSource(2)
    );
  JasperFillManager.fillReportToFile(
    "target/reports/Report3.jasper",
    null, 
    new JREmptyDataSource(2)
    );
  System.err.println("Filling time : " + (System.currentTimeMillis() - start));
}

```

Let's see now the batch export to the PDF output:

```
public void pdf() throws JRException
{
  long start = System.currentTimeMillis();
  List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
  jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("target/reports/Report1.jrprint"));
  jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("target/reports/Report2.jrprint"));
  jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("target/reports/Report3.jrprint"));
  
  JRPdfExporter exporter = new JRPdfExporter();
  
  exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
  exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("target/reports/BatchExportReport.pdf"));
  SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
  configuration.setCreatingBatchModeBookmarks(true);
  exporter.setConfiguration(configuration);
  
  exporter.exportReport();
  
  System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
}
```

One can see that a list of [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects is loaded from previously created *.jrprint files. The list is used to populate the exporter input data, using the `SimpleExporterInput` builtin implementation:

```
exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
```

Based on the list of [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects, the `SimpleExporterInput` will create and save a list of corresponding [ExporterInputItem](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInputItem.html) objects.

Next, a global configuration setting is supplied to the exporter, using the `SimplePdfExporterConfiguration` implementation. This will instruct the exporter to take into account the batch export mode when creating bookmarks.

```
SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
configuration.setCreatingBatchModeBookmarks(true);
exporter.setConfiguration(configuration);
```

Another example is the batch export to Excel 2007 (XLSX) output format:

```
public void xlsx() throws JRException
{
  long start = System.currentTimeMillis();
  List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
  jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("target/reports/Report1.jrprint"));
  jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("target/reports/Report2.jrprint"));
  jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("target/reports/Report3.jrprint"));
  
  JRXlsxExporter exporter = new JRXlsxExporter();
  
  exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
  exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("target/reports/BatchExportReport.xlsx"));
  SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
  configuration.setOnePagePerSheet(false);
  exporter.setConfiguration(configuration);
  
  exporter.exportReport();

  System.err.println("XLSX creation time : " + (System.currentTimeMillis() - start));
}
```

The main difference here is the global use of the `SimpleXlsxReportConfiguration` class, the builtin implementation of the [ReportExportConfiguration](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ReportExportConfiguration.html) interface fot the XLSX output format:

```
SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
configuration.setOnePagePerSheet(false);
exporter.setConfiguration(configuration);
```

This configuration setting may differ from report to report, but when it's set on the exporter like above, it means that the same setting will be applied to al reports in the list. To have different settings per each report, we need to set a [ReportExportConfiguration](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ReportExportConfiguration.html) to each [ExporterInputItem](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/ExporterInputItem.html) in the list.

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/samples/batchexport` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/batchexport/target/reports` directory.



