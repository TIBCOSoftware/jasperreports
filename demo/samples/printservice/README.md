
# JasperReports - Print Service Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the Java Print Service API could be used to print reports.

### Main Features in This Sample

[Printing Reports Using the Java Print Service API](#printservice)

## <a name='printservice'>Printing</a> Reports Using the Java Print Service API
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to print reports to network printers looked-up for based on their name, properties or printing capabilities.

**Since:** 0.4.3

### Printing Reports

The output of the report-filling process is always a pixel-perfect document, ready for viewing, printing, or exporting to other formats. These documents come in the form of serializable [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects. This allows the parent application to store them or transfer them over the network if needed.

One of the main purposes in report generation is to get documents accurately printed on paper. This can be accomplished either by exporting first the document to some other output format ( such as PDF) and then printing it, or directly, using a built-in facility. In this case, a dedicated service able to handle all the printing work is needed. This service should perform the following tasks:

- To find an available printer either locally or through network.
- To communicate to the printer all necessary printing attributes (page and print job settings).
- To send the serialized document to the printer

Initially, the report printing process was managed by the [JasperPrintManager](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrintManager.html) class based on the former Java 2 Printing API released with JDK 1.2. In the present all report printing work is done through an exporting-like mechanism handled by the [JRPrintServiceExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/JRPrintServiceExporter.html) class. Because the [JRPrintServiceExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/JRPrintServiceExporter.html) is based on the modern Java Print Service API, with better control over printer selection and entire printing process, we strongly encourage people to use this exporter instead of the [JasperPrintManager](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrintManager.html) class.

### The Java Print Service Exporter

The printing functionality in the [JRPrintServiceExporter](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/export/JRPrintServiceExporter.html) exporter is based on the printing 2D graphics mechanism using the `java.awt.print.PrinterJob` class.

First of all, this exporter tries to find a print service that supports the necessary print service attributes. To communicate these attributes to the print service one can use the [`getPrintServiceAttributeSet()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/PrintServiceExporterConfiguration.html#getPrintServiceAttributeSet()) exporter configuration setting which returns a `javax.print.attribute.PrintServiceAttributeSet` instance.

If the lookup procedure returns more than one print service able to handle the specified attributes, the exporter uses the first one in the list. If no suitable print service is found, an exception is thrown.

The print service lookup mechanism can be bypassed when the print service is well known and there are no more needs to search for it. In this case, a `javax.print.PrintService` instance can be obtained using the [`getPrintService()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/PrintServiceExporterConfiguration.html#getPrintService()) exporter configuration setting.

Once located a print service, it is associated with a `java.awt.print.PrinterJob` instance which can be further customized taking into account the following export configuration settings:

- [`getPrintRequestAttributeSet()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/PrintServiceExporterConfiguration.html#getPrintRequestAttributeSet()) - returns a `javax.print.attribute.PrintRequestAttributeSet` instance containing a set of specific print request settings.
- [`isDisplayPageDialog()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/PrintServiceExporterConfiguration.html#isDisplayPageDialog()) - returns a boolean value that specifies if a page dialog will open before the print job is submitted in order to modify some document settings such as page size or layout.
- [`isDisplayPrintDialog()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/PrintServiceExporterConfiguration.html#isDisplayPrintDialog()) - returns a boolean value that specifies if a print dialog will open before the print job is submitted in order to modify settings for some print request attributes.
- [`isDisplayPageDialogOnlyOnce()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/PrintServiceExporterConfiguration.html#isDisplayPageDialogOnlyOnce()) - when printing in batch mode this setting indicates whether the page dialog will popup globally for all documents in the list, or once per document. If it's true, then the page dialog will open only first time before submitting the batch job, and the page settings will be inherited by all the documents in the list.
- [`isDisplayPrintDialogOnlyOnce()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/PrintServiceExporterConfiguration.html#isDisplayPrintDialogOnlyOnce()) - when printing in batch mode this setting indicates whether the print dialog will popup globally for all documents in the list, or once per document. If it's true, then the print dialog will open only first time before submitting the batch job, and the print request settings will be inherited by all the documents in the list.


### The Java Print Service Sample

This sample shows how to use the Java print service exporter to print a generated report. One can see there is no .JRXML file, because the report is generated at fill time using report generation APIs (see the `fill()` and `getJasperPrint()` methods in the `src/PrintServiceApp.java` class file):

```
  public void fill() throws JRException
  {
    long start = System.currentTimeMillis();
    JasperPrint jasperPrint = getJasperPrint();
    File file = new File("target/reports/PrintServiceReport.jrprint");
    file.getParentFile().mkdirs();
    JRSaver.saveObject(jasperPrint, file);
    System.err.println("Filling time : " + (System.currentTimeMillis() - start));
  }
  ...
  private static JasperPrint getJasperPrint() throws JRException
  {
    //JasperPrint
    JasperPrint jasperPrint = new JasperPrint();
    jasperPrint.setName("NoReport");
    jasperPrint.setPageWidth(595);
    jasperPrint.setPageHeight(842);

    //Fonts
    JRDesignStyle normalStyle = new JRDesignStyle();
    normalStyle.setName("Sans_Normal");
    normalStyle.setDefault(true);
    normalStyle.setFontName("DejaVu Sans");
    normalStyle.setFontSize(8f);
    normalStyle.setPdfFontName("Helvetica");
    normalStyle.setPdfEncoding("Cp1252");
    normalStyle.setPdfEmbedded(Boolean.FALSE);
    jasperPrint.addStyle(normalStyle);

    JRDesignStyle boldStyle = new JRDesignStyle();
    boldStyle.setName("Sans_Bold");
    boldStyle.setFontName("DejaVu Sans");
    boldStyle.setFontSize(8f);
    boldStyle.setBold(Boolean.TRUE);
    boldStyle.setPdfFontName("Helvetica-Bold");
    boldStyle.setPdfEncoding("Cp1252");
    boldStyle.setPdfEmbedded(Boolean.FALSE);
    jasperPrint.addStyle(boldStyle);

    JRDesignStyle italicStyle = new JRDesignStyle();
    italicStyle.setName("Sans_Italic");
    italicStyle.setFontName("DejaVu Sans");
    italicStyle.setFontSize(8f);
    italicStyle.setItalic(Boolean.TRUE);
    italicStyle.setPdfFontName("Helvetica-Oblique");
    italicStyle.setPdfEncoding("Cp1252");
    italicStyle.setPdfEmbedded(Boolean.FALSE);
    jasperPrint.addStyle(italicStyle);

    JRPrintPage page = new JRBasePrintPage();

    JRPrintLine line = new JRBasePrintLine(jasperPrint.getDefaultStyleProvider());
    line.setX(40);
    line.setY(50);
    line.setWidth(515);
    line.setHeight(0);
    page.addElement(line);

    JRPrintImage image = new JRBasePrintImage(jasperPrint.getDefaultStyleProvider());
    image.setX(45);
    image.setY(55);
    image.setWidth(165);
    image.setHeight(40);
    image.setScaleImage(ScaleImageEnum.CLIP);
    image.setRenderer(
      ResourceRenderer.getInstance("jasperreports.png", false)
      );
    page.addElement(image);

    JRPrintText text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
    text.setX(210);
    text.setY(55);
    text.setWidth(345);
    text.setHeight(30);
    text.setTextHeight(text.getHeight());
    text.sethTextAlign(hTextAlignEnum.RIGHT);
    text.setLineSpacingFactor(1.3133681f);
    text.setLeadingOffset(-4.955078f);
    text.setStyle(boldStyle);
    text.setFontSize(18f);
    text.setText("JasperReports Project Description");
    page.addElement(text);

    text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
    text.setX(210);
    text.setY(85);
    text.setWidth(325);
    text.setHeight(15);
    text.setTextHeight(text.getHeight());
    text.sethTextAlign(hTextAlignEnum.RIGHT);
    text.setLineSpacingFactor(1.329241f);
    text.setLeadingOffset(-4.076172f);
    text.setStyle(italicStyle);
    text.setFontSize(12f);
    text.setText((new SimpleDateFormat("EEE, MMM d, yyyy")).format(new Date()));
    page.addElement(text);

    text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
    text.setX(40);
    text.setY(150);
    text.setWidth(515);
    text.setHeight(200);
    text.setTextHeight(text.getHeight());
    text.sethTextAlign(hTextAlignEnum.JUSTIFIED);
    text.setLineSpacingFactor(1.329241f);
    text.setLeadingOffset(-4.076172f);
    text.setStyle(normalStyle);
    text.setFontSize(14f);
    text.setText(
      "JasperReports is a powerful report-generating tool that has the ability to deliver rich content onto the screen, " +
      "to the printer or into PDF, HTML, XLS, CSV or XML files.\n\n" +
      "It is entirely written in Java and can be used in a variety of Java enabled applications, including J2EE or Web applications, " +
      "to generate dynamic content.\n\n" +
      "Its main purpose is to help creating page oriented, ready to print documents in a simple and flexible manner."
      );
    page.addElement(text);

    jasperPrint.addPage(page);

    return jasperPrint;
  }
```

Once saved the `PrintServiceReport.jrprint` file, it will be loaded and sent to the available printer when calling the print() method. A print dialog will popup before sending the print job:

```
  public void print() throws JRException
  {
    long start = System.currentTimeMillis();
    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
    printRequestAttributeSet.add(MediaSizeName.ISO_A4);

    PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
    //printServiceAttributeSet.add(new PrinterName("Epson Stylus 820 ESC/P 2", null));
    //printServiceAttributeSet.add(new PrinterName("hp LaserJet 1320 PCL 6", null));
    //printServiceAttributeSet.add(new PrinterName("PDFCreator", null));

    JRPrintServiceExporter exporter = new JRPrintServiceExporter();

    exporter.setExporterInput(new SimpleExporterInput("target/reports/PrintServiceReport.jrprint"));
    SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
    configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
    configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
    configuration.setDisplayPageDialog(false);
    configuration.setDisplayPrintDialog(true);
    exporter.setConfiguration(configuration);
    exporter.exportReport();

    System.err.println("Printing time : " + (System.currentTimeMillis() - start));
  }
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/printservice` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/printservice/target/reports` directory.
