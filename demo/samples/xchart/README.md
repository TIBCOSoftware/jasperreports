
# JasperReports - XChart Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how third-party charting APIs could be used for rendering charts as images.

### Main Features in This Sample

[Rendering Images Using Third Party APIs (XChart Library)](#xchart)

## <a name='xchart'>Rendering</a> Images Using Third Party APIs (XChart Library)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to render images using the [XChart](https://knowm.org/open-source/xchart/) library.

**Since:** 6.4.3

**Other Samples**\
[/demo/samples/scriptlets](../scriptlets/README.md)\
[/demo/samples/jfreechart](../jfreechart/README.md)

### The XChart Sample

This sample illustrates an interesting example of report scriptlets working in collaboration with third-party APIs, in order to output a chart image generated with the [XChart](https://knowm.org/open-source/xchart/) library.

First let's see the `XChartReport.jrxml` template in the `samples/xcharts/reports` directory. It provides a `scriptletClass="XChartScriptlet"` attribute, and a parametrized image element:

```
<element kind="image" y="70" width="515" height="400" scaleImage="Clip" hImageAlign="Center" onErrorType="Error">
  <expression><![CDATA[$V{ChartImage}]] ></expression>
</element>
```

The `java.awt.Image` object is stored in the `ChartImage` report variable:

```
<variable name="ChartImage" class="java.awt.Image" calculation="System"/>
```

To see how the `ChartImage` variable was "calculated", let's dig a little into the `XChartScriptlet.java` file in the src directory:

```
public void afterReportInit() throws JRScriptletException
{
  try
  {
    XYChart xyChart = new XYChartBuilder()
                          .width(400)
                          .height(400)
                          .title("Fruits Order")
                          .xAxisTitle("Day of Week")
                          .yAxisTitle("Quantity (t)")
                          .build();

    xyChart.addSeries("Apples", new double[] { 1, 3, 5}, new double[] { 4, 10, 7});
    xyChart.addSeries("Bananas", new double[] { 1, 2, 3, 4, 5}, new double[] { 6, 8, 4, 4, 6});
    xyChart.addSeries("Cherries", new double[] { 1, 3, 4, 5}, new double[] { 2, 6, 1, 9});
    XYStyler styler = xyChart.getStyler();
    styler.setLegendPosition(Styler.LegendPosition.InsideNW);
    styler.setAxisTitlesVisible(true);
    styler.setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

    BufferedImage bufferedImage = BitmapEncoder.getBufferedImage(xyChart);
    super.setVariableValue("ChartImage", bufferedImage);
  }
  catch(Exception e)
  {
    throw new JRScriptletException(e);
  }
}
```

Here an area chart is created after the report initialization, using APIs in the [XChart](https://knowm.org/open-source/xchart/) library. The chart is rendered as a `java.awt.Image` and stored in the `ChartImage` variable. From now on, the chart image is ready to be used by the report filler when needed.

And that's all the story here. With only a report scriptlet and a third-party library, one could embed interesting, complex, spectacular objects in a given report.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/xchart` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/xchart/target/reports` directory.
