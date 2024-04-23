
# JasperReports - JFreeChart Spider Chart Component Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the built-in spider chart component can be included in reports.

### Main Features in This Sample

[Using the Built-in Spider Chart Component](#spiderchartcomponent)

## <a name='spiderchartcomponent'>Using</a> the Built-in Spider Chart Component
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to configure the built-in spider chart component rendered by the JFreeChart library.

**Since:** 3.7.4

**Other Samples**\
[/demo/samples/jfreechart](../jfreechart/README.md)

### The Built-in Spider Chart Component - Overview

Spider charts (also known as radar charts) are very useful when multivariate data sets have to be represented. Such data sets are usually displayed as overlapping spider webs anchored in a multiple radial axis system.

The JR built-in spider chart was developed as component element in order to illustrate how to implement custom components based on the [JFreeChart](http://www.jfree.org/jfreechart/) charting engine. The component in this case should output a [JFreeChart](http://www.jfree.org/jfreechart/api/javadoc/org/jfree/chart/JFreeChart.html) object, collecting its data from an usual [CategoryDataset](http://www.jfree.org/jfreechart/api/javadoc/org/jfree/data/category/CategoryDataset.html), but rendering it as a spider web based on the [SpiderWebPlot](http://www.jfree.org/jfreechart/api/javadoc/org/jfree/chart/plot/SpiderWebPlot.html) API.

The content of a spider chart component is structured in 3 complementary elements:

- `chartSettings` - stores general settings for the generated chart object such as title, subtitles and chart legend. Plot settings are not included.
- `spiderDataset` - stores appropriate settings for the category dataset
- `spiderPlot` - stores settings available for the spider web plot

As any other component element, the spider chart provides the evaluationTime and evaluationGroup attributes.

### Chart Settings In The Spider Chart Component

In the `<chartSettings>` element one can configure the following structure similar to the `chart` element in usual JR charts:

- `chartTitle`
- `chartSubtitle`
- `chartLegend`
- `anchorNameExpression`
- `hyperlinkReferenceExpression`
- `hyperlinkAnchorExpression`
- `hyperlinkPageExpression`
- `hyperlinkTooltipExpression`
- `hyperlinkParameter`

The attributes below are also available for the <chartSettings> element, with similar meanings as in usual JR charts:

- `isShowLegend`
- `backcolor`
- `hyperlinkType`
- `hyperlinkTarget`
- `bookmarkLevel`
- `customizerClass`
- `renderType`

**Note:** The main difference here is that the chart customizer must be an implementation of the [ChartCustomizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/components/charts/ChartCustomizer.html) interface that exposes the

```
public void customize(JFreeChart chart, ChartComponent chartComponent)
```

method adapted for chart components. The easiest way to implement this interface is by extending the context-aware [AbstractChartCustomizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/components/charts/AbstractChartCustomizer.html) class in order to get access to the component element with its properties, for a completely flexible chart customization:

```
public void customize(JFreeChart chart, ChartComponent chartComponent)
{
  chart.getPlot().setOutlineVisible(true);
  chart.getPlot().setOutlinePaint(new Color(0,0,255));
  chart.getPlot().setOutlineStroke(new BasicStroke(1f));
}
```

### The Spider Dataset

The spider dataset is a [JRElementDataset](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRElementDataset.html) based on category series. It contains the same structure as the `<categoryDataset>` element in usual JR charts:

- `dataset`
- `categorySeries`

### The Spider Web Plot

The spider plot element displays available data in the form of a "spider web", depending on specific settings. Multiple series can be plotted on the same (category) axis. Neither tick marks and tick labels nor negative values on axis are supported for the moment.

The following attributes can be used to configure a spider plot:

- `rotation` - Specifies the mode the radar axis are drawn. Possible values are `Clockwise` and `Anticlockwise`.
- `tableOrder - Specifies whether the data series are stored in rows or in columns. Possible values are `Row` and `Column`.
- `isWebFilled` - Specifies if the web polygons are filled or not. Possible values are `true` and `false`.
- `startAngle` - Specifies the starting angle (in degrees) for the spider chart axis. The default value is `90` deg.
- `headPercent` - Specifies the head radius percent.
- `interiorGap` - Specifies the interior gap percent.
- `axisLineColor` - Specifies the color to use when drawing the line showing the axis.
- `axisLineWidth` - Specifies the axis line width.
- `labelGap` - Specifies the axis label gap percent.
- `labelColor` - Specifies the label foreground color.
- `backcolor` - Specifies the background color of the plot.
- `backgroundAlpha` - Specifies the Alpha (transparency) parameter for the plot background.
- `foregroundAlpha` - Specifies the Alpha (transparency) parameter for the plot foreground.

One can also define the `labelFont` element for the spider plot, and a `maxValueExpression` representing the maximum value any category axis can take.

### The Spider Chart Sample

Here is the piece of code used to configure the spider chart in the JRXML sample:

```
<element kind="component" mode="Opaque" x="50" y="90" width="400" height="400">
  <component kind="spiderChart">
    <chartSettings showLegend="true" backcolor="#00FF00" subtitleColor="#0000FF" legendBackgroundColor="#EEFFEE"
      titlePosition="Top" customizerClass="SpiderChartCustomizer">
      <titleFont italic="true" fontSize="12.0"/>
      <subtitleFont bold="true" fontSize="6.0"/>
      <legendFont fontSize="6.0"/>
      <titleExpression><![CDATA["Spider Chart"]] ></titleExpression>
      <subtitleExpression><![CDATA["Chart Displaying Spider Web Contours"]] ></subtitleExpression>
      <hyperlinkTooltipExpression><![CDATA["Spider Chart"]] ></hyperlinkTooltipExpression>
    </chartSettings>
    <dataset incrementType="Group" incrementGroup="category">
      <series>
        <seriesExpression><![CDATA[$F{series}]] ></seriesExpression>
        <categoryExpression><![CDATA[$F{category}]] ></categoryExpression>
        <valueExpression><![CDATA[$F{value}]] ></valueExpression>
        <itemHyperlink linkType="Reference" linkTarget="Blank">
          <hyperlinkReferenceExpression>
            <![CDATA["http://www.jfree.org/jfreechart/api/javadoc/org/jfree/chart/plot/SpiderWebPlot.html"]] >
          </hyperlinkReferenceExpression>
        </itemHyperlink>
      </series>
    </dataset>
    <plot rotation="Anticlockwise" tableOrder="Row" startAngle="39.0" headPercent="0.01" interiorGap="0.2"
      axisLineColor="#FF0000" axisLineWidth="2.0" labelGap="0.05" backcolor="#FFFF00">
      <labelFont bold="true" italic="true" fontSize="6.0"/>
      <maxValueExpression><![CDATA[10d]] ></maxValueExpression>
    </plot>
  </component>
</element>
```

The resulting spider chart will have a green background, an italicized title and a blue subtitle, a legend with its own background color and font size, and a plot with rotation anticlockwise, start angle = 39deg, red axis, yellow backcolor and italicized bold labels on axis. See also how the interior gaps and axis line widths were set.

Note also that the plot outline in the spider chart is customized via the `SpiderChartCustomizer` class.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/spiderchartcomponent ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/spiderchartcomponent/target/reports` directory.
