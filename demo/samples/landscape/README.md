
# JasperReports - Landscape Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how reports can be generated and printed in "Landscape" page format.

### Main Features in This Sample

[Creating Landscape Orientation Report Templates](#landscape)

## <a name='landscape'>Creating</a> Landscape Orientation Report Templates
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to create landscape orientation reports.

**Since:** 0.1.0

This feature is used when a report should be visualized or printed in the `Landscape` page layout. A page is considered to be in a `Landscape` orientation when its width and height appear as interchanged.

In order to manage its pages layout, the `<jasperReport />` element contains the `orientation` attribute.\
Values allowed for this attribute are:

- `Portrait`
- `Landscape`

By default, in JasperReports pages are visualized as `Portrait`. The `orientation` being an attribute at report level, its value applies to all pages in the generated document.

Below is a code snippet showing how to set in a report the Landscape orientation:

```
<jasperReport name="LandscapeReport" language="java"
  pageWidth="842" pageHeight="595" orientation="Landscape"
  columnWidth="842" leftMargin="0" rightMargin="0" topMargin="0" bottomMargin="0">
```

Here one can observe that pageWidth and pageHeight values were interchanged and the `orientation` attribute takes the `Landscape` value.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/landscape` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/landscape/target/reports` directory.
