
# JasperReports - JFreeChart Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how third-party charting APIs could be used for rendering charts as images.

### Main Features in This Sample

[Rendering Images Using Third Party APIs (JFreeChart Library)](#jfreechart)

## <a name='jfreechart'>Rendering</a> Images Using Third Party APIs (JFreeChart Library)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to render images using the JFreeChart library.

**Since:** 0.6.0

**Other Samples**\
[/demo/samples/xchart](../xchart/README.md)

### Rendering Graphic Objects

Usually, the mechanism of producing complex images, charts and other graphic objects is part of some more specialized Java libraries, and does not represent one of the JasperReports goals. However, one of its important goals is to easily integrate within a generated report charts, barcodes and other graphics produced by third party libraries.

This integration is based on the fact that great majority of graphic objects produced by these libraries can output to image files or in-memory Java image objects. Then all these generated image objects can be handled by the JasperReports engine using a common image element, as described in the [Images](../images/README.md) sample.

One problem is that the content of an image element can come either directly from an image file like a JPG, GIF, PNG, or can be a Scalable Vector Graphics (SVG) file that is rendered using some business logic or a dedicated graphics API related to a specific charting or a barcode library. In this case, JasperReports treats all kind of images in a very transparent way because it relies on a special interface called [Graphics2DRenderable](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/renderers/Graphics2DRenderable.html) to offer a common way to render images.

The [Graphics2DRenderable](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/renderers/Graphics2DRenderable.html) interface has a method called `render(Graphics2D grx, Rectangle2D r)`, which gets called by the engine each time it needs to draw the image on a given device or graphic context. This approach provides the best quality for the SVG images when they must be drawn on unknown devices or zoomed into without losing sharpness.

Other methods specified in this interface can be used to obtain the native size of the actual image that the renderer wraps or the binary data for an image that must be stored in a separate file during export.

The JasperReports library comes with various implementations for the [Graphics2DRenderable](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/renderers/Graphics2DRenderable.html) interface, which wrap images that come from files or binary image data in JPG, GIF, or PNG format. 

For instance, the [JCommonDrawableRendererImpl](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/renderers/JCommonDrawableRendererImpl.html) class is actually a container for binary image data loaded from a `org.jfree.chart.ui.Drawable` object, which then draws itself on the supplied `java.awt.Graphics2D` context when the engine requires it.

Image renderers are serializable because inside the generated document for each image is a renderer object kept as reference, which is serialized along with the whole [JasperPrint](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JasperPrint.html) object.

When a [Graphics2DRenderable](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/renderers/Graphics2DRenderable.html) instance is serialized, so is the binary image data it contains. However, if the image element must be lazy loaded (see the `lazy` attribute in the [Images](../images/README.md) sample), then the engine will not load the binary image data at report-filling time. Rather, it stores inside the renderer only the `java.lang.String` location of the image. The actual image data is loaded only when needed for rendering at report-export or view time.

To simplify the implementation of SVG image renderers, JasperReports ships with an abstract renderer: [AbstractSvgDataToGraphics2DRenderer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/renderers/AbstractSvgDataToGraphics2DRenderer.html). This implementation contains the code to produce binary image data from the SVG graphic format. 

This is needed when the image must be stored in separate files on disk or delivered in binary format to a consumer (like a web browser).

Specific image renderers are called when the class of the `<expression/>` element is `net.sf.jasperreports.renderers.Graphics2DRenderable`. In this case the image expression value should point to a Graphics2DRenderable object which is designated to render the image.

### Rendering a JFreeChart Object Example

This sample shows how to integrate a JFreeChart pie chart into a report, letting the JFreeChart engine to draw itself the chart.

In order to put together both the JFreeChart chart and the Graphics2DRenderable renderer, the `JFreeChartReport.jrxml` template provides a scriptlet class atribute:

```
scriptletClass="JFreeChartScriptlet"
```

The scriptlet class source is available as `JFreeChartScriptlet.java` file in the src directory.
In the `afterReportInit()` method, a chart object is created, with all necessary information to be represented:

```
  DefaultPieDataset dataset = new DefaultPieDataset();
  dataset.setValue("Java", 43.2d);
  dataset.setValue("Visual Basic", 10.0d);
  dataset.setValue("C/C++", 17.5d);
  dataset.setValue("PHP", 32.5d);
  dataset.setValue("Perl", 1.0d);

  JFreeChart chart =
    ChartFactory.createPieChart(
      "Pie Chart Demo 1",
      dataset,
      true,
      true,
      false
      );

  PiePlot plot = (PiePlot) chart.getPlot();
  plot.setStartAngle(290);
  plot.setDirection(Rotation.CLOCKWISE);
  plot.setForegroundAlpha(0.5f);
  plot.setNoDataMessage("No data to display");
```

The resulting chart is passed to a [JCommonDrawableRendererImpl](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/renderers/JCommonDrawableRendererImpl.html) class constructor. The [JCommonDrawableRendererImpl](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/renderers/JCommonDrawableRendererImpl.html) constructor needs an `org.jfree.chart.ui.Drawable` object, characterized by its own `draw()` method. A JFreeChart chart represents such a `Drawable` object.

The `render()` method in the [JCommonDrawableRendererImpl](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/renderers/JCommonDrawableRendererImpl.html) class just calls the `Drawable` object's own `draw()` method, and all the rendering process will be executed by this dedicated API:

```
public JCommonDrawableRenderer(Drawable drawable)
{
  this.drawable = drawable;
}

public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle)
{
  if (drawable != null)
  {
    drawable.draw(grx, rectangle);
  }
}
```

Once the renderer gets available, one have to instruct the JasperReports engine to use it. The new renderer is passed to the engine in the `Chart` report variable:

```
  this.setVariableValue("Chart", new JCommonDrawableRendererImpl(chart));
```

This variable is referred to in the `reports/JFreeChartReport.jrxml` file:

```
  <variable name="Chart" calculation="System" class="net.sf.jasperreports.renderers.Renderable"/>
```

Now, let's take a look at the `image` element itself:

```
 <element kind="image" y="110" width="515" height="300" scaleImage="Clip" hImageAlign="Center" onErrorType="Error" linkType="Reference">
   <expression><![CDATA[$V{Chart}]] ></expression>
   <hyperlinkReferenceExpression><![CDATA["http://www.jfree.org/jfreechart"]] ></hyperlinkReferenceExpression>
 </element>
```

The image expression class is `net.sf.jasperreports.renderers.Renderable`, and its value points to the `Chart` report variable prepared in the report scriptlet.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/jfreechart` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/jfreechart/target/reports` directory.
