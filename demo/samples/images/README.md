
# JasperReports - Images Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how images could be used in a report template.

### Main Features in This Sample

[Images](#images)

## <a name='images'>Images</a>
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to render images in reports using the build-in image element.

**Since:** 0.1.0

**Other Samples**\
[/demo/samples/horizontal](../horizontal/README.md)\
[/demo/samples/jasper](../jasper/README.md)

### Images in JasperReports

Below is an image:

<img src="https://jasperreports.sourceforge.net/sample.reference/images/dukesign.jpg" alt="This is a jpg image"/>

Images are the most complex graphic objects in a report. Just as for text field elements, their content is dynamically evaluated at runtime, using a report expression. The process of image rendering depends on a set of attributes and expressions available for the image report elements (`<element kind="image"/>`).

### Image Attributes

- `scaleImage` - indicates how should be rendered the image when its actual size does not fit the size of the image report element. This is because in a lot of situations images are loaded at runtime and there is no way to know their exact size when creating the report template. Possible values for this attribute are:
   - `Clip` - if the actual image is larger than the image element size, it will be cut off so that it keeps its original resolution, and only the region that fits the specified size will be displayed.
   - `FillFrame` - if the dimensions of the actual image do not fit those specified for the image element that displays it, the image is forced to obey them and stretch itself so that it fits in the designated output area. It will be deformed if necessary.
   - `RetainShape` - if the actual image does not fit into the image element, it can be adapted to those dimensions while keeping its original undeformed proportions.
   - `RealHeight` - the image can be stretched vertically to match the actual image height, while preserving the declared width of the image element.
   - `RealSize` - the image can be stretched vertically to match the actual image height, while adjusting the width of the image element to match the actual image width.

If the scale type for the image is `Clip` or `RetainShape` and the actual image is smaller than its defined size in the report template or does not have the same proportions, the image might not occupy all the space allocated to it in the report template. In these conditions the image can be both horizontally and vertically aligned within its report element. Two attributes are responsible for the image alignment:

- `hAlign` - specifies the horizontal alignment. Possible values are: `Left, Center, Right`.
- `vAlign` - specifies the vertical alignment. Possible values are: `Top, Middle, Bottom`.
- `usingCache` - flag that indicates if an image should be loaded each time it gets displayed in a report, or if it should be loaded only once and then stored in a memory cache for better performance. For images with static content applying the caching mechanism is strongly recommended. When one set the `usingCache` attribute to true, the reporting engine will try to recognize previously loaded images using their specified source. This caching functionality is available for image elements whose expressions return objects of any type as the image source. The usingCache flag is set to `true` by default for images having `java.lang.String` expressions and to `false` for all other types.
- `lazy` - flag that specifies whether the image should be loaded and processed during report filling or during exporting, in case that the image is not available at fill time. By default this flag is set to false. When it is set to true, an image path String is stored at fill time instead of the image itself, and during the exporting process the image will be loaded from the location read from this path String. For example, this sample contains an image element pointing to the JasperReports logo, which is lazy loaded from the project’s web site.
- `onErrorType` - if an image is unavailable when the engine tries to load it, some kind of output should be provided instead. The `onErrorType` attribute specifies this output. Possible values are:
    - `Error` - an exception is thrown if the engine cannot load the image.
    - `Blank` - any image-loading exception is ignored and nothing will appear in the generated document.
    - `Icon` - the engine will put a small specific icon in the document to indicate that the actual image is missing.
- `evaluationTime` - specify the moment when the image expression gets evaluated. Possible values are:
    - `Now` - the image expression is evaluated when the current band is filled.
    - `Report` - the image expression is evaluated when the end of the report is reached.
    - `Page` - the image expression is evaluated when the end of the current page is reached.
    - `Column` - the image expression is evaluated when the end of the current column is reached.
    - `Group` - the image expression is evaluated when the group specified by the `evaluationGroup` attribute changes.
    - `Auto` - each variable participating in the image expression is evaluated at a time corresponding to its reset type.
- `evaluationGroup` - the group involved in the image evaluation process when the `evaluationTime` attribute is set to `Group`.

### Image Expression

The value returned by the image expression is the source for the image to be displayed. The image expression is introduced by the <expression/> element and can return values from only the limited range of classes listed following:

- `java.lang.String`
- `java.io.File`
- `java.net.URL`
- `java.io.InputStream`
- `java.awt.Image`
- `net.sf.jasperreports.renderers.Renderable`

### Image Hyperlinks

In JasperReports image elements provide hyperlink support. All available hyperlink expressions are allowed to be part of an image element. See also the [hyperlink](../hyperlink/README.md) sample.

### Image Examples

This reports/ImagesReport.jrxml report template contains a series of image examples, having a specific combination of the attributes above.

The first image in the report is loaded from the current folder at export time.

```
<element kind="image" width="150" height="40" onErrorType="Error">
  <expression><![CDATA["dukesign.jpg"]] ></expression>
  <box>
    <pen lineWidth="0.5"/>
  </box>
</element>
```

The last image in the report template is loaded from https://jasperreports.sourceforge.net website and is scaled to retain the image shape. It also provides a hyperlink expression pointing to its remote location:

```
<element kind="image" mode="Opaque"
  x="400" width="150" height="100" backcolor="#DDFFDD" scaleImage="RetainShape" onErrorType="Icon" linkType="Reference" lazy="true">
  <expression><![CDATA["https://jasperreports.sourceforge.net/jasperreports.svg"]] ></expression>
  <hyperlinkReferenceExpression><![CDATA["http://jasperreports.sf.net/jasperreports.svg"]] ></hyperlinkReferenceExpression>
</element>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/images` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/images/target/reports` directory.
