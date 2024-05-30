
# JasperReports - Charts Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the chart element could be used to render different types of charts.

### Main Features in This Sample

[Charts](#charts)\
[Altering Charts (Chart Customizers)](#chartcustomizers)

### Secondary Features
[Datasets](../crosstabs/README.md#datasets)
				
## <a name='charts'>Charts</a>
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to render different types of charts using the built-in chart element.

**Since:** 1.0.0

**Other Samples**\
[/demo/samples/chartthemes](../chartthemes)\
[/demo/samples/chartcustomizers](../chartcustomizers)

### Charts in JasperReports

The JasperReports library does not produce charts and graphics itself. However, it can easily integrate charts, barcodes, and graphics produced by other more specialized Java libraries.
The great majority of available Java libraries that produce charts and graphics can output to image files or in-memory Java image objects. And such image objects can be integrated into a JasperReports document using a normal image element.
To simplify the integration of charts inside reports, JasperReports provides a specific attribute setting: `kind=chart` for the report elements.
This sample contains various charts based on the JasperReports Chart component that are generated using the [JFreeChart](https://www.jfree.org/jfreechart/) library.

**IMPORTANT NOTIFICATION:**
<div style="border:1px solid #6A5ACD;padding:10px;">
As explained in this [Migration note](https://github.com/jfree/jfreechart?tab=readme-ov-file#migration), since JFreeChart v.1.5.0 all (pseudo) 3D charts have been removed. As a consequence, the (pseudo) 3D effect is no longer visible for 3D charts used in JasperReports Library v.7.0.0 and newer. The affected charts are the following:

- Bar3DChart (`chartType="bar3D"`)
- StackedBar3DChart (`chartType="stackedBar3D"`)
- Pie3DChart (`chartType="pie3D"`)

3D charts are now marked for deprecation and will be removed.
</div>

### Chart Types

In a JRXML file a chart element is characterized by the `kind=&quot;chart&quot;` attribute setting of the element.\
Chart types are specified in the `chartType` attribute of the chart report element. Possible values for the `chartType` attribute are:

- `pie` - a combination of a Pie dataset and a Pie plot.
- `pie3D` - **deprecated**. The 3D effect is no longer visible. Groups a Pie dataset and a Pie plot.
- `bar` - a basic combination of a Category dataset and a Bar plot.
- `bar3D` - **deprecated**. The 3D effect is no longer visible. Wraps a Category dataset and a Bar plot.
- `xyBar` - supports Time Period datasets, Time Series datasets, and XY datasets, and uses a Bar plot to render the axis and the items.
- `stackedBar` - uses data from a Category dataset and renders its content using a Bar plot.
- `stackedBar3D` - **deprecated**. The 3D effect is no longer visible. Uses data from a Category dataset and renders its content using a Bar plot.
- `line` - groups a Category dataset and a Line plot.
- `xyLine` - groups an XY dataset and a Line plot.
- `area` - items from a Category dataset are rendered using an Area plot.
- `stackedArea` - items from a Category dataset are rendered using an Area plot.
- `xyArea` - uses data from an XY dataset and renders it through an Area plot.
- `scatter` - wraps an XY dataset with a Scatter plot.
- `bubble` - combines an XYZ dataset with a Bubble plot.
- `timeSeries` - groups a Time Series dataset and a Time Series plot.
- `highLow` - a combination of a High-Low dataset and a High-Low plot.
- `candlestick` - uses data from a High-Low dataset but with a special Candlestick plot.
- `meter` - displays a single value from a Value dataset on a dial, using rendering options from a Meter plot.
- `thermometer` - displays the single value in a Value dataset using rendering options from a Thermometer plot.
- `multiAxis` - contains multiple range axes, all sharing a common domain axis.

### Chart Properties

When including and configuring a chart component, three entities are involved:

- The overall element of kind `chart`
- The chart dataset (which groups chart data–related settings)
- The chart plot (which groups visual settings related to the way the chart items are rendered)

All chart types have a common set of properties. Charts are normal report elements, so they share some of their properties with all the other report elements. Charts are also box elements and can have hyperlinks associated with them.\
Chart-specific settings are grouped under the `<dataset />` and  `<plot />` child elements:

- `<box />`
- `<chartTitle />`
- `<chartSubtitle />`
- `<chartLegend />`
- `<anchorNameExpression />`
- `<hyperlinkReferenceExpression />`
- `<hyperlinkAnchorExpression />`
- `<hyperlinkPageExpression />`
- `<hyperlinkTooltipExpression />`
- `<hyperlinkParameter />`

Attributes available for all chart types are:

- `isShowLegend` - indicates whether the legend will be rendered visible
- `evaluationTime` - indicates the moment when chart goes rendered. Default value is `Now`
- `evaluationGroup` - used to specify the group at which to render the chart, when evaluationTime is `Group`
- `hyperlinkType` - indicates the type of the hyperlink element.
- `hyperlinkTarget` - indicates the target of the hyperlink.
- `bookmarkLevel` - the level of the bookmark corresponding to the anchor.
- `customizerClass` - the name of the chart customizer class.
- `renderType` - represents the specified format used to render the chart.
- `theme` - the name of the chart theme used to draw the chart.

### Chart Rendering

In generated reports, the output produced by a chart element is an image element. Image elements are drawn using implementations of the [Renderable](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/renderers/Renderable.html) interface. Charts have an attribute called `renderType` which specifies the renderer implementation that will be used to render the chart during export or report display. By default, JasperReports recognizes the following values for this attribute:

- `draw` - the chart is drawn directly on the target graphic context using the [JFreeChart API](http://jfree.org/jfreechart/).
- `image` - an image is first produced from the chart and this image in turn gets rendered onto the target graphic context.
- `svg` - the chart is transformed into the Scalable Vector Graphics format and from that format is then rendered onto the target graphic context.

### Chart Title, Subtitle and Legend

All charts can have one title and one subtitle. All chart types can display a legend that explains the values represented by the chart. By default all charts display the legend, but one can suppress this display by setting the `isShowLegend` flag to false.

Chart titles, subtitles and legends are optional and can be customized for color, font, and position. They can be placed at the top of the chart, at the bottom of the chart, or on the left or right side of the chart, depending on the value of the position attribute.

### Chart Datasets

Charts rely on a data-oriented component called the chart dataset for mapping report data and retrieving chart data at runtime.\
A chart dataset is an entity which can get initialized and incremented at specified moments during the report-filling process and iteration through the report data source. Like a report variable, at any moment a chart dataset holds a certain value, which is a complex data structure that gets incremented and will be used for rendering the chart at the appropriate moment.
Several types of chart datasets are available in JasperReports because each type of chart works with a certain dataset type. A dataset type is stored in the `kind` attribute of the `<dataset/>` element. Available dataset types are: `pie, category, xy, timeSeries, timePeriod, xyz, highLow, value` and `gantt`.

The JasperReports object model uses the [JRChartDataset](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/JRChartDataset.html) interface to define chart datasets. There are implementations of this interface for each of the aforementioned dataset types. All chart datasets initialize and increment in the same way, and differ only in the type of data or data series they map. The increment or reset type and increment or reset groups can be specified by setting the dataset common attributes below:

- `resetType`
- `resetGroup`
- `incrementType`
- `incrementGroup`

Specific dataset types are:

**Pie Dataset**

A type of `pie` dataset series is characterized by the following expressions:

- `<keyExpression />` - represent the categories that will make up the slices in the pie chart. This expression can return any `java.lang.Comparable` object.
- `<valueExpression />` - produces the values that correspond to each category/key in the dataset. Values are always `java.lang.Number` objects.
- `<labelExpression />` - if this expression is missing, the chart will display default labels for each slice in the pie chart. Use this expression, which returns `java.lang.String` values, to customize the item labels for the pie chart.
- `<sectionHyperlink />` - sets hyperlinks associated with pie sections

**Category Dataset**

A type of `category` dataset series is characterized by the following expressions:

- `<seriesExpression />` - indicates the name of the series. This expression can return any `java.lang.Comparable` object.
- `<categoryExpression />` - returns the name of the category for each value inside the series specified by the series expression. Categories are `java.lang.Comparable` objects.
- `<valueExpression />` - produces the values that correspond to each category in the dataset. Values are always `java.lang.Number` objects.
- `<labelExpression />` - if this expression is missing, the chart will display default labels for each item in the chart. Use this expression, which returns `java.lang.String` values, to customize the item labels for the chart.
- `<itemHyperlink />` - sets hyperlinks associated with chart items

**XY Dataset**

A type of `xy` dataset series is characterized by the following expressions:

- `<seriesExpression />` - indicates the name of the series. This expression can return any `java.lang.Comparable` object.
- `<xValueExpression />` - returns the `java.lang.Number` value representing the X value from the (x, y) pair that will be added to the current data series.
- `<yValueExpression />` - returns the `java.lang.Number` value representing the Y value from the (x, y) pair that will be added to the current data series.
- `<labelExpression />` - if this expression is missing, the chart will display default labels for each item in the chart. Use this expression, which returns `java.lang.String` values, to customize the item labels for the chart.
- `<itemHyperlink />` - sets hyperlinks associated with chart items

**XYZ Dataset**

A type of `xyz` dataset series is characterized by the following expressions:

- `<seriesExpression />` - indicates the name of the series. This expression can return any `java.lang.Comparable` object.
- `<xValueExpression />` - returns the `java.lang.Number` value representing the X value from the (x, y, z) item that will be added to the current data series.
- `<yValueExpression />` - returns the `java.lang.Number` value representing the Y value from the (x, y, z) item that will be added to the current data series.
- `<zValueExpression />` - returns the `java.lang.Number` value representing the Z value from the (x, y, z) item that will be added to the current data series.
- `<labelExpression />` - if this expression is missing, the chart will display default labels for each item in the chart. Use this expression, which returns `java.lang.String` values, to customize the item labels for the chart.
- `<itemHyperlink />` - sets hyperlinks associated with chart items

**Time Series Dataset**

A type of `timeSeries` dataset is characterized by the `timePeriod` attribute and a specific type of series.

The `timePeriod` attribute specifies the type of the data series inside the dataset. Time series can contain numeric values associated with days, months, years, or other predefined time periods. Possible values are:

- `Year`
- `Quarter`
- `Month`
- `Week`
- `Day` - this is the default value
- `Hour`
- `Minute`
- `Second`
- `Millisecond`

A type of `timeSeries` dataset series is characterized by the following expressions:

- `<seriesExpression />` - indicates the name of the series. This expression can return any `java.lang.Comparable` object.
- `<timePeriodExpression />` - returns a `java.util.Date` value from which the engine will extract the corresponding time period depending on the value set for the `timePeriod` attribute mentioned above.
- `<valueExpression />` - returns the `java.lang.Number` value to associate with the corresponding time period value when incrementing the current series of the dataset.
- `<labelExpression />` - if this expression is missing, the chart will display default labels for each item in the chart. Use this expression, which returns `java.lang.String` values, to customize the item labels for the chart.
- `<itemHyperlink />` - sets hyperlinks associated with chart items

**Time Period Dataset**

A type of `timePeriod` dataset series is characterized by the following expressions:

- `<seriesExpression />` - indicates the name of the series. This expression can return any `java.lang.Comparable` object.
- `<startDateExpression />` - specifies the beginning of the date interval with which the numeric value will be associated when it is added to the time period series.
- `<endDateExpression />` - specifies the end of the date interval with which the numeric value will be associated when it is added to the time period series.
- `<valueExpression />` - returns the java.lang.Number value to associate with the current date interval specified by the start date and end date expressions.
- `<labelExpression />` - if this expression is missing, the chart will display default labels for each item in the chart. Use this expression, which returns `java.lang.String` values, to customize the item labels for the chart.
- `<itemHyperlink />` - sets hyperlinks associated with chart items

**High Low Dataset**

A type of `highLow` dataset series is characterized by the following expressions:

- `<seriesExpression />` - currently only one series is supported inside a High-Low or Candlestick chart. However, this single series must be identified by a `java.lang.Comparable` value returned by this expression, and it must also be used as the series name in the chart’s legend.
- `<dateExpression />` - returns the date to which the current (`high, low, open, close, volume`) item refers.
- `<highExpression />` - returns a `java.lang.Number` value, which will be part of the data item added to the series when the dataset gets incremented.
- `<lowExpression />` - returns a `java.lang.Number` value, which will be part of the data item added to the series when the dataset gets incremented.
- `<openExpression />` - returns a `java.lang.Number` value, which will be part of the data item added to the series when the dataset gets incremented.
- `<closeExpression />` - returns a `java.lang.Number` value, which will be part of the data item added to the series when the dataset gets incremented.
- `<volumeExpression />` - a numeric expression that returns the volume value to use for the current data item. It is used only for Candlestick charts.
- `<itemHyperlink />` - sets hyperlinks associated with chart items

**Value Dataset**

This is a special chart dataset implementation that contains a single value and is used for rendering Meter and Thermometer charts. The value is collected using the `<valueExpression />` expression.

### Common Settings for Chart Plots

The chart plot is the area of the chart on which the axes and items are rendered. Plots differ based on the type of chart. Some plots are specialized in drawing pies; others, in drawing bar items or lines.\
Each type of plot comes with its own set of properties or attributes for customizing the chart’s appearance and behavior.\
There is, however, a subset of plot properties which is common to all plot types. Some other properties are only applicable to a specific kind of plot. All properties are grouped under the `<plot />` element in JRXML and can be part of any chart/plot definition in the report template.
Common plot attributes are:

- `backcolor` - specifies the background color for the plot.
- `orientation` - specifies whether plot items will be rendered horizontally or vertically. Possible values are `Horizontal` and `Vertical`. The default is `Vertical`.
- `backgroundAlpha` - specifies the background transparency for the plot.
- `foregroundAlpha` - specifies the foreground transparency for the plot.

A plot contains also a `<seriesColor />` element which customize colors for series, and their position within in the color sequence.

### Specific Settings for Chart Plots

Following are the specific plot properties for different chart types:

- `pie` - it has no specific settings
- `pie3D` - **deprecated**. The 3D effect is no longer visible. Contains the `depthFactor` attribute, a numeric value ranging from 0 to 1 that represents the depth of the pie as a percentage of the height of the plot area.
- `bar` - one can show or hide tick labels, tick marks or item labels, and provides settings for both axis.
- `bar3D` - **deprecated**. The 3D effect is no longer visible. Provides the same settings as the `bar` plot, and generates a 3D effect using the `xOffset` and `yOffset` attributes.
- `line` - one can show or hide lines connecting item points, can show or hide shapes associated with item points, and provides settings for both axis.
- `scatter` - like the `line` plot, it can show or hide lines connecting item points, can show or hide shapes associated with item points, and provides settings for both axis.
- `area` - provides settings for both axis.
- `bubble` - one can set the bubble dimensions by setting the `scaleType` attribute, and provides settings for both axis.
- `timeSeries` - one can show or hide lines connecting item points, can show or hide shapes associated with item points, and provides settings for both axis.
- `highLow` - one can show or hide open ticks, can show or hide close ticks, and provides settings for both axis.
- `candlestick` - one can show or hide the volume, and provides settings for both axis.
- `meter` - contains specific settings for the dial shape, scale angle, measurement units, tick interval, dial color, needle color, tick color, value display font, color and format pattern, data range and meter intervals.
- `thermometer` - contains specific settings for the value location, mercury color, show/hide value lines, value display font, color and format pattern, data range, low range, medium range and high range.
- `multiAxis` - contains specific settings for axis included in the plot

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/hsqldb` within the JasperReports source project and run the following command:
```
> mvn exec:java
```

This will start the `HSQLDB` server shipped with the JasperReports distribution package. Let this terminal running the `HSQLDB` server.

Open a new command prompt/terminal window and set the current folder to `demo/samples/charts` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

This will generate all supported document types containing the sample report in the `demo/samples/charts/target/reports` directory.

<div align="right"><a href='#top'>top</a></div>

---

## <a name='chartcustomizers'>Altering</a> Charts (Chart Customizers)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to make various changes to a generated chart using the [JFreeChart API](http://jfree.org/jfreechart/), which is used by JasperReports to draw the charts created with the built-in chart element.

**Since:** 1.1.1

**Other Samples**\
[/demo/samples/chartthemes](../chartthemes/README.md)


### Chart Customizers and Chart Themes

Although the [JFreeChart](http://jfree.org/jfreechart/) library is a fairly complete charting package that offers great flexibility and a comprehensive range of settings to fully customize the appearance and the content of the charts it renders, the built-in chart component offered by JasperReports exposes only a subset of the library’s original charting functionality. This ensures that JasperReports charts are easily embeddable into reports and that the basic set of properties exposed through JRXML and the object model is enough for the majority of use cases.
In time, other JFreeChart settings will be exposed through the built-in chart component, but certainly JasperReports will never be able to expose all the `JFreeChart` settings through JRXML tags or the API.

To provide full control over chart customization even when using the built-in chart component, JasperReports can make use of either a chart theme implementation, or of a chart customizer implementation associated with the chart element, or both.

Chart themes are a more recent addition to the library and in a way they deprecate the chart customizers because they bring enhanced capabilities in controlling chart output.
A chart customizer is an implementation of the [JRChartCustomizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/JRChartCustomizer.html) interface that is associated with the chart element using the `customizerClass` attribute. The easiest way to implement this interface is by extending the [JRAbstractChartCustomizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/JRAbstractChartCustomizer.html) having access to parameters, fields, and variables, for more flexible chart customization based on report data.

However, it is recommended to implement and work with chart themes instead of chart customizers, because chart themes give more control over chart output, including the creation of the `JFreeChart` object itself. Chart customizer only allow modifying the `JFreeChart` object that is created externally and passed in to them. Also, chart themes affect a whole range of chart types across multiple reports and are not necessarily tied to a specific chart element within a report. They can even apply globally to all charts within a given JasperReports deployment, applying a new look and feel to all charts created within that environment.

### A Simple Chart Customizer Class Example

When one implements the [JRChartCustomizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/JRChartCustomizer.html) interface, the only method which has to be implemented is:

```
public void customize(JFreeChart chart, JRChart jasperChart);
```

The Charts sample contains a very simple customizer class, which sets specific colors for series in a Bar chart.

First, let's take a look at the `BarChartCustomizer` class in the src directory:

```
public class BarChartCustomizer implements JRChartCustomizer
{
  @Override
  public void customize(JFreeChart chart, JRChart jasperChart)
  {
    BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
    renderer.setSeriesPaint(0, Color.green);
    renderer.setSeriesPaint(1, Color.orange);
  }
}
```

The `customize()` method gets applied after the `JFreeChart` object is already created. Then, the only thing one have to do is to take this `JFreeChart` object and modify its characteristics according to some more specific needs.

In the example above, two older series colors were replaced with new ones in the bar renderer, and so, the color scheme was altered for the Bar chart.
Now, one have to tell to the JasperReports engine that a given chart should be customized using this customizer class. In the `reports/BarChartReport.jrxml` file this class is set as chart customizer:

```
<element kind="chart" chartType="bar" ... customizerClass="BarChartCustomizer" ...>
  ...
</element>
```

**Note:** When running the Charts sample, one can see that the Bar chart generated reports are the only with their items colored in green and orange. All the other generated reports are sharing another color scheme.
