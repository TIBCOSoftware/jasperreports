
# JasperReports - No Report Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how final documents can be created without using the reporting engine, but only the API directly.

### Main Features in This Sample

[Creating Final Documents Using the API Directly (No Report Filling)](#noreport)

## <a name='noreport'>Creating</a> Final Documents Using the API Directly (No Report Filling)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to create final in-memory documents without using the report generation process, but rather the object model of the JasperReports API.

**Since:** 0.4.2

### The JasperPrint Object

When generating reports, the main purpose is to create a pixel-perfect document, ready for viewing, printing, or exporting to other formats. These documents are stored in serializable [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) objects, that can be saved on the disk, or transferred over the network if needed.

Usually, one can obtain a [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object by compiling and filling a report template, following the steps below:

- Create a report template containing all information needed for the report design. Usually, report templates are stored in JRXML source files.
- Parse the report template source file using the [JRXmlLoader](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/xml/JRXmlLoader.html) in order to obtain a [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) design template object.
- Compile the design template object using a [JRCompiler](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JRCompiler.html) in order to complete all consistency validations and create a [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) object.
- Fill the [JasperReport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperReport.html) object with all its data and generate the pixel-perfect document stored in a [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object.

This is the most common way to generate documents in JasperReports. But there are situations when the steps above cannot be all performed. Even in this case, a [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object can be generated from scratch as in-memory document. Once generated, it can be serialized and stored on disk, either in a `*.jrprint` file, or in a specific XML file with the `.jrpxml` extension. Or it can be then exported to various other output formats.

Taking a look inside the [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object, one can see that it exposes a series of member properties such as:

- `name`
- `pageWidth`
- `pageHeight`
- `topMargin`
- `leftMargin`
- `bottomMargin`
- `rightMargin`
- `orientation`
- `parts`
- `pages`
- `styles`
- etc...

Therefore, creating a [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object may be accomplished by creating an empty [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) instance using the default constructor, and then customizing its members with appropriate values.

### The No Report Sample

This sample shows how to create an in-memory [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object. The sample doesn't contain any JRXML template file. The [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object is hardcoded in the `src/NoReportApp.java` file (see the `getJasperPrint()` method).
Report compilation and filling are no more necessary.

```
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
    "JasperReports is a powerful report-generating tool that has the ability to deliver rich content onto the screen, to the printer or into PDF, HTML, XLS, CSV or XML files.\n\n" +
    "It is entirely written in Java and can be used in a variety of Java enabled applications, including J2EE or Web applications, to generate dynamic content.\n\n" +
    "Its main purpose is to help creating page oriented, ready to print documents in a simple and flexible manner."
    );
  page.addElement(text);

  jasperPrint.addPage(page);

  return jasperPrint;
}
```

The Ant `compile` task is no longer necessary. When the fill task is performed, the only things it has to perform is to call the `getJasperPrint()` method, which produces an already filled [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object, and to save this in-memory object in a `*.jrprint` file:

```
  public void fill() throws JRException
  {
    long start = System.currentTimeMillis();
    JasperPrint jasperPrint = getJasperPrint();
    File file = new File("target/reports/NoReport.jrprint");
    file.getParentFile().mkdirs();
    JRSaver.saveObject(jasperPrint, file);
    System.err.println("Filling time : " + (System.currentTimeMillis() - start));
  }
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/noreport` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/noreport/target/reports` directory.
