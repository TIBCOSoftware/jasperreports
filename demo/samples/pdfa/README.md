
# JasperReports - PDF/A Conformance Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how to generate PDF/A compliant documents.

### Main Features in This Sample

[Generating PDF/A compliant documents](#pdfa)

## <a name='pdfa'>Generating</a> PDF/A compliant documents
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to use PDF/A conformance settings to generate various levels of PDF/A documents.

**Since:** 3.1.2

**Other Samples**\
[/demo/samples/accessible](../accessible/README.md)\
[/demo/samples/jasper](../jasper/README.md)\
[/demo/samples/table](../table/README.md)\
[/demo/samples/tabular](../tabular/README.md)

### PDF/A conformance overview

In general, PDF/A refers to an ISO standardized set of guidance rules that a rendered PDF document must follow in order to ensure the archiving and long-term safeguarding of that document. The PDF/A standard was necessary in order to address the expanding need to ensure that documents intended for preservation over a long period of time could be retrieved and rendered in a consistent mode in the future. This requirement is essential for a large variety of documents produced in the government, industry and academic environments worldwide.

Some features are mandatory to met the PDF/A standard. For instance: specific color management guidelines, support for embedded fonts, support for reading embedded annotations. All features that are unsuitable for long-term archiving are prohibited. For instance, unembedded fonts, PDF encryption, audio and video content, etc.

The PDF/A standards attempts to maximize:

- Device independence
- Self-containment of the document
- Self-documentation

The constraints include:

- Fonts must be embedded and also must be legally embeddable for unlimited, universal rendering
- Colorspaces must be specified in a device-independent manner
- Javascript and executable file triggering are not allowed
- Audio and video content are prohibited
- Encryption is forbidden
- Use of standards-based metadata is mandatory

### PDF/A conformance levels

1. PDF/A-1b (basic PDF/A-1 conformance level represents the minimal compliance required to ensure the preservability over long terms of the rendered visual appearance of a document). Level B is a lower level of conformance, including the general requirements of [ISO 19005-1:2005](https://www.iso.org/standard/38920.html) standard regarding the visual appearance of electronic documents, but not their internal structure or semantic properties. All constraints described in the section above must be applied to PDF/A-1b compliant documents.\
Also, PDF/A-1b documents may contain only scanned images without any representation of text. OCR text that supports indexing or extraction of text is permitted, but only if it is invisible.
This conformance level requires the use of PDF 1.4
2. PDF/A-1a (accessible PDF/A-1 conformance level includes all the PDF/A-1b rules, plus a set of rules regarding the document accessibility and reading order). PDF/A-1a requires a representation of the logical structure of the document as specified in section 6.8 of [ISO 19005-1:2005](https://www.iso.org/standard/38920.html). It is recommended to capture a document's logical structure hierarchy to the finest granularity possible. The standard also indicates that "PDF/A-1 writers should not add structural or semantic information that is not explicitly or implicitly present in the source material solely for the purpose of achieving conformance." The logical structure of a document will only include the minimal relevant structural tagging.\
In order to obtain persistent accessible documents one should:
    - make use of simple layouts
    - use alternate text for graphics
    - never use color or contrast alone to indicate meaning
    - never convey meaning through design in a way that can't be expressed with the document's text
    - avoid spanning table cells and complex table structures
    - avoid table tags without tabular data
    - avoid illustrations consisted of many small vector graphics
    - avoid elements overlapping
    - avoid background images\
PDF/A-1a requires the use of PDF 1.4.
3. PDF/A-2b (basic PDF/A-2 conformance level) requires the use of PDF 1.7 (see also [ISO 32000-1:2008](https://www.iso.org/standard/51502.html)). Conformance rules are described in [ISO 19005-2:2011](https://www.iso.org/standard/50655.html) and encapsulates all rules for PDF/A-1b. Additionally includes support for:
    - compressed Object and XRef streams (for smaller file sizes)
    - PDF/A-compliant file attachments, portable collections and PDF packages, archiving of sets of documents as individual documents in one file
    - transparency for graphical elements
    - JPEG 2000 compression\
PDF/A-2b imposes a few additional constraints on number of color channels, bit depth, and colorspaces for compatibility with versions of PDF/X and PDF/E current in 2010.
4. PDF/A-2u (unicode PDF/A-2 conformance level) represents the PDF/A-2b conformance level with the additional requirement that all text in the document have Unicode equivalents.
5. PDF/A-2a (accessible PDF/A-2 conformance level) defines rules for enhanced accessibility and includes the representation of the logical structure of the document, as specified in section 6.8 of [ISO 19005-2:2011](https://www.iso.org/standard/50655.html).
6. PDF/A-3b (basic PDF/A-2 conformance level) requires the use of PDF 1.7 (see also [ISO 32000-1:2008](https://www.iso.org/standard/51502.html)). Conformance rules are described in [ISO 19005-2:2011](https://www.iso.org/standard/50655.html) and encapsulates all rules for PDF/A-2b. PDF/A-3 is equivalent to PDF/A-2, except for allowing files in any format to be embedded. Each embedded file in a PDF/A-3 document must be identified in a file specification dictionary (as described in section 7.11.3 of ISO 32000-1:2008). The inclusion of a human-readable description of the file is recommended.
7. PDF/A-3u (unicode PDF/A-3 conformance level) represents the PDF/A-34b conformance level with the additional requirement that all text in the document have Unicode equivalents.
8. PDF/A-3a (accessible PDF/A-3 conformance level) rules are similar to PDF/A-2a.

For more information and guidance related to PDF/A conformance levels, also see the following:

- [PDF/A-1](https://www.loc.gov/preservation/digital/formats/fdd/fdd000125.shtml)
- [PDF/A-2](https://www.loc.gov/preservation/digital/formats/fdd/fdd000319.shtml)
- [PDF/A-3](https://www.loc.gov/preservation/digital/formats/fdd/fdd000360.shtml)

The conformance level for the generated PDF report can be set in JasperReports using the following configuration properties:

- `getPdfaConformance()` - Exporter configuration setting used to specify the conformance level of the PDF/A document. Possible values are:
    - `pdfa1a`
    - `pdfa1b`
    - `pdfa2a`
    - `pdfa2b`
    - `pdfa2u`
    - `pdfa3a`
    - `pdfa3b`
    - `pdfa3u`
    - `none `(default)
- `net.sf.jasperreports.export.pdfa.conformance` - Configuration property used as default for the getPdfaConformance() setting above. It can be set per context or report level.

### Color Management Guidelines

In order to ensure the same appearance for the document on every device, we need to associate a color management profile to the report. An ICC profile is a set of data that characterizes a color input or output device, or a color space, according to standards promulgated by the [International Color Consortium](https://en.wikipedia.org/wiki/International_Color_Consortium) (ICC).\
ICC profiles are available for both RGB and CMYK color spaces. They can also be embedded in the generated PDF/A compliant document.

In order to customize the color spaces we can use the following properties:

- `net.sf.jasperreports.export.pdfa.icc.profile.path` - Specifies the path to the ICC profile file for the PDF/A compliant documents. Its value is used as default for the `getIccProfilePath()` export configuration setting.
- `net.sf.jasperreports.export.pdfa.embed.icc.profile` - Specifies whether the ICC profile, which in this case must be provided by `getIccProfilePath()`, is embedded into the PDF. PDFA compliance requires embedding the ICC profile, which inhibits this setting.
- `net.sf.jasperreports.export.pdf.use.cmyk.colors` - Specifies whether the ICC profile, which in this case must be provided by `getIccProfilePath()`, is used to convert colors from RGB to CMYK color space.

### Additional Properties for PDF/A-1a

There are 2 more properties to be considered in order to ensure the accessibility requirements for PDF/A level "a" documents:

- `net.sf.jasperreports.export.pdf.metadata.title` - Required. Contains the title information to use for the generated PDF metadata. Its value is used as default for the `getMetadataTitle()` export configuration setting.
- `net.sf.jasperreports.components.table.accessible` - Property that enables/disables the automatic addition of specific custom properties to the elements that make up the table and its cells.

### Section 508 conformance in Tagged PDF Reports

A PDF document can be internally structured in order to enable accessibility tools in PDF readers. Based on specific hidden tags in their content, documents can be read aloud to people with disabilities. Tagged PDF files comply with the requirements of the Section 508 of the U.S. Rehabilitation Act (see https://www.section508.gov).
JasperReports supports Section 508 compliant PDF documents based on the PDF tags. According to a set of built-in configuration properties, PDF tags required for accessibility are incorporated into the structure of generated documents.

### Tagged PDF Output

To enable accessibility in generated documents a set of export parameters are available in JasperReports along with their related exporter hint properties:

- Tagged Documents
   - [`isTagged()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#isTagged()) - Exporter configuration setting used to turn on the creation of hidden structural tags. By default the feature is turned off.
   - [`net.sf.jasperreports.export.pdf.tagged`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#PROPERTY_TAGGED) - Configuration property used as default for the isTagged() setting above. It can be set at global or report level.

- Tag Language
   - [`getTagLanguage()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#getTagLanguage()) - Export configuration setting used to specify the language to be used in the language tag of the generated PDF.
   - [`net.sf.jasperreports.export.pdf.tag.language`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#PROPERTY_TAG_LANGUAGE) - Configuration property used as default for the getTagLanguage() setting above. It can be set at global or report level.

### Accessibility Tags

The following property can be set per element in JRXML:

[`net.sf.jasperreports.export.accessibility.tag`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/export/AccessibilityUtil.html#PROPERTY_ACCESSIBILITY_TAG)

This property that specifies the type of accessible content introduced by the element. Possible values are:

- `H1`
- `H2`
- `H3`
- `H4`
- `H5`
- `H6`
- `TABLE`
- `TABLE_LAYOUT`
- `COLUMN_HEADER`
- `ROW_HEADER`

All the next consecutive properties can be set per element and share the same set of possible values (excepting the column span and row span properties, which accept numeric values only):

- `start` - if this value is set, the element will be marked with a starting PDF tag corresponding to the property.
- `full `- if this value is set, the element will be wrapped in a full PDF tag corresponding to the property.
- `end` - if this value is set, the element will be marked with an ending PDF tag corresponding to the property.

Available properties related to PDF accessibility tags are enumerated below:

- `net.sf.jasperreports.export.pdf.tag.h1` - Property used to mark a text field as a level 1 heading.
- `net.sf.jasperreports.export.pdf.tag.h2` - Property used to mark a text field as a level 2 heading.
- `net.sf.jasperreports.export.pdf.tag.h3` - Property used to mark a text field as a level 3 heading.
- `net.sf.jasperreports.export.pdf.tag.table` - Property used to indicate where the table starts and where it ends in the report template.
- `net.sf.jasperreports.export.pdf.tag.th` - Property used to mark heading cells in a table.
- `net.sf.jasperreports.export.pdf.tag.tr` - Property used to indicate where a table row starts and where it ends in the report template.
- `net.sf.jasperreports.export.pdf.tag.td` - Property used to mark detail cells in a table.
- `net.sf.jasperreports.export.pdf.tag.colspan` - Property used to indicate the column span of a table cell. Allowed values are positive integers.
- `net.sf.jasperreports.export.pdf.tag.rowspan` - Property used to indicate the row span of a table cell. Allowed values are positive integers.

The use of these individual tag properties is illustrated in the TabularPdfA1Report JRXML sample report.

### Alternate Text for Images and Reading Order

Accessible documents must provide also an alternate text for images and an appropriate reading order. In JasperReports the alternate text for images is specified in the `<hyperlinkTooltipExpression/>` of the image element.

The reading order is processed using the Z-order of elements as present in the report template. No additional processing is necessary here.

To find out more about tagged PDF output see the "Section 508 conformance" chapter in the JasperReports Ultimate Guide.

### Sample Reports

To illustrate various levels of PDF/A conformance, this sample contains the following reports:
- `PdfA1Report` - a PDF/A-1a compliant report. It contains an embedded true type font (LiberationSans), a non transparent image of the JasperReports logo and a SVG image. All images provide alternate texts. It embeds a RGB type ICC profile
- `PdfA2Report` - a PDF/A-2a compliant report. It contains an embedded true type font (LiberationSans), 2 transparent images of the JasperReports logo and some overlapped elements. All images provide alternate texts. It embeds a RGB type ICC profile
- `PdfA3Report` - a PDF/A-3a compliant report, which is in fact similar to `PdfA2Report`, since there are no other document attachments.
- `TabularPdfA1Report` - a PDF/A-1a compliant report. It contains a table-like structure, with elements tagged individually to generate an accessible PDF table
- `TaggedPdfA1Report` - a PDF/A-1a compliant report. It contains several elements configured for accessibility, and an accessible table element that automatically generates table tags when it is exported to PDF

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/pdfa` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/pdfa/target/reports` directory.
