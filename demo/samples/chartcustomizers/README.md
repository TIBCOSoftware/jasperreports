
# JasperReports - Chart Customizers Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how to use multiple chart customizers on the same chart

### Main Features in This Sample

[Multiple Chart Customizers](#chartcustomizers)

### Secondary Features
[Charts](../charts/README.md#charts)\
[Altering Charts (Chart Customizers)](../charts/README.md#chartcustomizers)				
## <a name='chartcustomizers'>Multiple</a> Chart Customizers
<div align="right">Documented by <a href='mailto:teodord@users.sourceforge.net'>Teodor Danciu</a></div>

**Description / Goal**\
How to make various changes to a generated chart using the [JFreeChart API](http://jfree.org/jfreechart/), using one or more chart customizer on the same report.

**Since:** 6.3.1

**IMPORTANT NOTIFICATION:**
<div style="border:1px solid #6A5ACD;padding:10px;">
As explained in this [Migration note](https://github.com/jfree/jfreechart?tab=readme-ov-file#migration), since JFreeChart v.1.5.0 all (pseudo) 3D charts have been removed. As a consequence, the (pseudo) 3D effect is no longer visible for 3D charts used in JasperReports Library v.7.0.0 and newer. The affected charts are the following:

- Bar3DChart (`chartType="bar3D"`)
- StackedBar3DChart (`chartType="stackedBar3D"`)
- Pie3DChart (`chartType="pie3D"`)

3D charts are now marked for deprecation and will be removed.
</div>

### Multiple Chart Customizers

When the chart customizer feature was first introduced, only one chart customizer object could be assigned to a chart element using the `customizerClass` attribute of the chart element.\
Although a chart customizer gives great flexibility in altering or enhancing the output of the chart element, having only one such object did not allow easy reuse and piling/stacking up several chart customizer at a time.

Support for multiple chart customizers per chart element was introduced using custom properties at chart element level having their names in the following format:

```
net.sf.jasperreports.customizer.class.{arbitrary_name}
```

Such a property is supposed to specify the name of a class that implements the [JRChartCustomizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/JRChartCustomizer.html) interface and has an empty constructor. This constructor is used to instantiate a chart customizer object at report fill time and is then called to customizer the chart output. The `{arbitrary_name}` suffix of the property is optional and represents the name of the chart customizer. This value will be injected into the chart customizer object using the `setName(String)` method in case the chart customizer implements the [NamedChartCustomizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/charts/NamedChartCustomizer.html) interface.
The name of the chart customizer allows the chart object to identify additional custom properties at chart element level which are considered to be its own and help configure it, because they contain the name as part of the full property name which have the following format:

```
net.sf.jasperreports.customizer.{arbitrary_name}.{property_name}
```

Here follows an example in which the domain axis of an XY line chart is configured by setting the minimum and maximum values of the axis as well as the distance between ticks:

```
<element kind="chart" chartType="xyLine" width="555" height="300" evaluationTime="Report">
  ...
  <property name="net.sf.jasperreports.customizer.class.domainAxis" value="net.sf.jasperreports.customizers.axis.DomainAxisCustomizer"/>
  <property name="net.sf.jasperreports.customizer.domainAxis.minValue" value="60"/>
  <property name="net.sf.jasperreports.customizer.domainAxis.maxValue" value="90"/>
  <property name="net.sf.jasperreports.customizer.domainAxis.tickUnit" value="5"/>
  ...
</element>
```

### Packaged Chart Customizers

The chart customizers sample provides a series of chart customizer implementations that can be added as a separate JAR file to an application that uses the JasperReports library.
These chart customizers leverage various features of the [JFreeChart](http://jfree.org/jfreechart/) library and are as follows:

- Axis Configurations
    - `net.sf.jasperreports.customizers.axis.DomainAxisCustomizer`
    - `net.sf.jasperreports.customizers.axis.RangeAxisCustomizer`

- Chart Markers
    - `net.sf.jasperreports.customizers.marker.CategoryMarkerCustomizer`
    - `net.sf.jasperreports.customizers.marker.DomainIntervalMarkerCustomizer`
    - `net.sf.jasperreports.customizers.marker.DomainValueMarkerCustomizer`
    - `net.sf.jasperreports.customizers.marker.RangeIntervalMarkerCustomizer`
    - `net.sf.jasperreports.customizers.marker.RangeValueMarkerCustomizer`

- Custom Shapes
    - `net.sf.jasperreports.customizers.shape.LegendShapeCustomizer`
    - `net.sf.jasperreports.customizers.shape.LineDotShapeCustomizer`

- Miscellaneous
    - `net.sf.jasperreports.customizers.LevelRenderCustomizer`
    - `net.sf.jasperreports.customizers.SplineCustomizer`
    - `net.sf.jasperreports.customizers.StepCustomizer`

More implementations will be added to this package in the future, but until then, you can leverage this feature and implement and use your own chart customizers.