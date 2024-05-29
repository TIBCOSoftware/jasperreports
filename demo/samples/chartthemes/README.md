
# JasperReports - Chart Themes Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the chart themes could be used to change the look and feel of all chart types.

### Main Features in This Sample

[Enhancing Charts Look and Feel (Chart Themes)](#chartthemes)

### Secondary Features
[Charts](../charts/README.md#charts)\
[Altering Charts (Chart Customizers)](../charts/README.md#chartcustomizers)
				
## <a name='chartthemes'>Enhancing</a> Charts Look and Feel (Chart Themes)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Customizing the appearance of all charts created using the chart element, across an entire application.

**Since:** 3.1.0

**IMPORTANT NOTIFICATION:**
<div style="border:1px solid #6A5ACD;padding:10px;">
As explained in this [Migration note](https://github.com/jfree/jfreechart?tab=readme-ov-file#migration), since JFreeChart v.1.5.0 all (pseudo) 3D charts have been removed. As a consequence, the (pseudo) 3D effect is no longer visible for 3D charts used in JasperReports Library v.7.0.0 and newer. The affected charts are the following:

- Bar3DChart (`chartType="bar3D"`)
- StackedBar3DChart (`chartType="stackedBar3D"`)
- Pie3DChart (`chartType="pie3D"`)

3D charts are now marked for deprecation and will be removed.
</div>

### Why are chart themes necessary?

- **Chart theme**

    - A chart theme represents a set of chart properties that are modified together on the target chart component in order to significantly alter its overall look and feel. The chart settings that make up a chart theme are usually not part of the chart settings of JFreeChart that are exposed in JRXML.
- **Chart customizer**
    - A chart customizer is similar to a chart theme in the sense that it too alters a chart look and feel and makes use of chart settings that are not exposed in JRXML. But unlike chart themes, a chart customizer only affects the current chart it is attached to, while a chart theme affects all types of charts supported by JR.

The purpose of this feature is to provide a way for users to change the overall appearance of the charts generated using the JasperReports built-in chart component, without having to write chart customizers or to use an extensive set of chart properties. Using a single chart property or a global configuration property, it will be possible to change the look and feel of all chart types supported by the built-in JasperReports chart component and give those graphics a more professional appearance.

### Creating and registering chart themes

Chart themes make use of the new extension support introduced in JasperReports v.3.1.0.
The extension support allows users to add chart themes to a JasperReports deployment without the need to change any configuration file. Simply dropping a JAR into the classpath will make the chart themes it comes with, automatically available to the JasperReports engine.

In order to create a single chart theme or several chart themes available as an extension to JasperReports, one must first implement the `net.sf.jasperreports.charts.ChartTheme` interface. This interface represents an implementation of a chart theme and has only one method:

```
public JFreeChart createChart(ChartContext charContext) throws JRException;
```

Basically, the whole purpose of a chart theme implementation is to create `JFreeChart` objects out of the incoming `net.sf.jasperreports.charts.ChartContext` object that is passed as parameter.\
The chart context contains all the runtime information needed including the original chart element definition from the report template, the ability to evaluate expressions at runtime, the chart dataset, a label generator and the locale. In the future, more runtime information could be added to the chart context interface, if needed, without affecting the already existing chart theme implementations.\
Secondly, the user must implement the `net.sf.jasperreports.charts.ChartThemeBundle` interface in order to make the chart themes available by giving them names and putting them together in a bundle. A chart theme bundle can contain one or more chart themes that are found by name.
The chart theme bundle interface has only two methods:

```
public String[] getChartThemeNames();
public ChartTheme getChartTheme(String themeName);
```

Once the chart theme bundle was created, it has to be added as an extension to JR. In order to do this, the `net.sf.jasperreports.extensions.ExtensionsRegistryFactory` interface must be implemented. This factory implementation will be responsible for creating an `net.sf.jasperreports.extensions.ExtensionsRegistry` object, which in turn will provide a list of chart theme bundles, including the one the user has just created above.
JasperReports will be able to automatically detect the presence of the custom chart theme bundle through its built-in extension support, if all the above mentioned classes (chart themes, chart bundle and extension registry factory) are put in a single JAR file and inside the root folder of this JAR file, a file named `jasperreports_extension.properties` is present.
This properties file placed in the root of the JAR, has to have a property in the form `net.sf.jasperreports.extension.registry.factory.<arbitrary_suffix>` that would be the name of a class that implements the `net.sf.jasperreports.extensions.ExtensionsRegistryFactory` interface, mentioned above.

### Chart themes implementations in JasperReports

In order to customize a chart, a sum of specific settings should be configured in a given chart theme. Then, when a chart gets created, if its own settings are not present, they are read from the related chart theme. For example, if chart's own title position is not set, then the title position will be set using the chart theme's title position attribute.
Chart settings are grouped in 4 main categories:

- general chart settings: available to all chart types. For example, the title color, the legend position, the chart border, etc.
- general plot settings: referring to the chart's plot, like: plot background color, orientation, gridlines, etc.
- general axis settings: referring to the chart's axis: axis color, label angle, tick marks count, etc.
- specific chart type settings: available only for a particular chart type. For example, only a pie chart can have a circular shape, only a meter chart can have a display value, only line charts can show or hide lines, etc.

In order to eliminate the need of hard-coding these settings in a chart theme class, it is recommended to use an XML approach instead.
In JasperReports there are 2 different XML-based chart themes implementations.

**XML Spring-based chart theme implementation**

This implementation is based on the Spring Framework platform, and uses Spring beans for binding the chart theme bundle class and the 4 above chart settings categories. All related java classes and XML beans are found in the `/ext/jasperreports-chart-themes/src/main/java/net/sf/jasperreports/chartthemes/spring` directory.
The implementation comes with 4 different sample chart themes, identified by their names: generic, default.spring, eye.candy.sixties, aegean. The generic chart theme is the parent implementation, and all Spring-based chart themes should extend it.
In the chartThemesBeans.xml file found in `/ext/jasperreports-chart-themes/src/main/java/net/sf/jasperreports/chartthemes/spring/beans` directory are mapped both the chart theme bundle class, and the parent chart theme named generic. The other Spring-based themes are mapped each one in a separate xml file (see the `aegeanChartPropertiesBean.xml` file, for example), which are referred in the `chartThemesBeans.xml` by their resource name.
Below is the theme bundle bean:

```
  <bean id="themeBundle" class="net.sf.jasperreports.chartthemes.ChartThemeMapBundle">
    <property name="themes">
      <map>
        <entry key="generic">
          <ref bean="genericChartTheme"/>
        </entry>
        <entry key="default.spring">
          <ref bean="defaultChartTheme"/>
        </entry>
        <entry key="eye.candy.sixties">
          <ref bean="eyeCandySixtiesChartTheme"/>
        </entry>
        <entry key="aegean">
          <ref bean="aegeanChartTheme"/>
        </entry>
      </map>
    </property>
  </bean>
```

The generic chart theme bean is referenced by `id="genericChartTheme"` and contains 4 bean elements related to the 4 chart settings categories:

- `defaultChartPropertiesMap` - contains general chart settings
- `defaultPlotPropertiesMap` - contains general plot settings
- `defaultAxisPropertiesMap` - contains axis settings
- `defaultChartTypePropertiesMap` - contains settings available only for specific chart types.

Any newly created chart theme should be saved in a new xml file. This file should be added to `chartThemesBeans.xml` resource imports, and the chart theme's name has to be added to the theme bundle bean. After that, the new theme will be available.
In order to make available Spring-based chart themes, in the `jasperreports_extension.properties` file the following lines should be uncommented:

```
net.sf.jasperreports.extension.registry.factory.chart.theme=net.sf.jasperreports.extensions.SpringExtensionsRegistryFactory
net.sf.jasperreports.extension.chart.theme.spring.beans.resource=net/sf/jasperreports/chartthemes/spring/beans/chartThemesBeans.xml
```

All other lines in the file have to be commented.

**Castor XML Mapping-based chart theme implementation**

This implementation based on Castor XML Mapping provides both marshalling and unmarshalling features available on the Castor platform. This is a good reason to write and use Castor XML Mapping-based implementations.
All related java classes and XML files are found in the `/ext/jasperreports-chart-themes/src/main/java/net/sf/jasperreports/chartthemes/simple` directory.

Let's take a look into the `chart.theme.mapping.xml` file, where is stored all the mapping configuration needed for this implementation.\
First we'll see the `net.sf.jasperreports.chartthemes.simple.ChartThemeSettings` class definition. It contains all necessary settings for a chart theme. Because title, subtitles and legend are chart subelements, their specific settings were grouped in specific fields. Also, settings for domain and range axis were separated in related fields:

```
  <class name="net.sf.jasperreports.chartthemes.simple.ChartThemeSettings">
    <map-to xml="chart-theme"/>
    <field name="chartSettings" type="net.sf.jasperreports.chartthemes.simple.ChartSettings">
      <bind-xml node="element"/>
    </field>
    <field name="titleSettings" type="net.sf.jasperreports.chartthemes.simple.TitleSettings">
      <bind-xml node="element"/>
    </field>
    <field name="subtitleSettings" type="net.sf.jasperreports.chartthemes.simple.TitleSettings">
      <bind-xml node="element"/>
    </field>
    <field name="legendSettings" type="net.sf.jasperreports.chartthemes.simple.LegendSettings">
      <bind-xml node="element"/>
    </field>
    <field name="plotSettings" type="net.sf.jasperreports.chartthemes.simple.PlotSettings">
      <bind-xml node="element"/>
    </field>
    <field name="domainAxisSettings" type="net.sf.jasperreports.chartthemes.simple.AxisSettings">
      <bind-xml node="element"/>
    </field>
    <field name="rangeAxisSettings" type="net.sf.jasperreports.chartthemes.simple.AxisSettings" >
      <bind-xml node="element"/>
    </field>
  </class>
```

Then each field above is described in its own class. For example, the LegendSettings:

```
  <class name="net.sf.jasperreports.chartthemes.simple.LegendSettings">
    <map-to xml="legend"/>
    <field name="showLegend" type="java.lang.Boolean">
      <bind-xml node="attribute"/>
    </field>
    <field name="position" type="net.sf.jasperreports.charts.type.EdgeEnum" handler="net.sf.jasperreports.chartthemes.simple.handlers.EdgeFieldHandler">
      <bind-xml node="attribute"/>
    </field>
    <field name="foregroundPaint" type="net.sf.jasperreports.chartthemes.simple.PaintProvider">
      <bind-xml auto-naming="deriveByField" node="element"/>
    </field>
    <field name="backgroundPaint" type="net.sf.jasperreports.chartthemes.simple.PaintProvider">
      <bind-xml auto-naming="deriveByField" node="element"/>
    </field>
    <field name="font" type="net.sf.jasperreports.engine.JRFont">
      <bind-xml auto-naming="deriveByClass" node="element"/>
    </field>
    <field name="horizontalAlignment" type="org.jfree.chart.ui.HorizontalAlignment" handler="net.sf.jasperreports.chartthemes.simple.handlers.HorizontalAlignmentFieldHandler">
      <bind-xml node="attribute"/>
    </field>
    <field name="verticalAlignment" type="org.jfree.chart.ui.VerticalAlignment" handler="net.sf.jasperreports.chartthemes.simple.handlers.VerticalAlignmentFieldHandler">
      <bind-xml node="attribute"/>
    </field>
    <field name="padding" type="org.jfree.chart.ui.RectangleInsets">
      <bind-xml node="element"/>
    </field>
  </class>
```

And finally, any object type involved in the class definitions above is also mapped in this xml file:

```
  <class name="net.sf.jasperreports.chartthemes.simple.ColorProvider">
    <map-to xml="color"/>
    <field name="color" type="java.awt.Color" handler="net.sf.jasperreports.chartthemes.simple.handlers.ColorFieldHandler">
      <bind-xml node="attribute"/>
    </field>
  </class>
```

All classes mapped above correspond to a related java implementation. You can see them in the `net.sf.jasperreports.chartthemes.simple` package.

Now, in order to create a new XML Mapping-based chart theme, one have to write an xml file with the `.jrctx` extension which is handled by Castor via marshalling/unmarshalling processes.
In this sample was implemented the `net.sf.jasperreports.chartthemes.simple.SimpleSettingsFactory` class which produces the simple theme's specific settings. These settings are related to the `net.sf.jasperreports.chartthemes.simple.SimpleChartTheme` class.\
The `simple.jrctx` file is produced when the ant `themes` task is called, and this file will be used further when a new `JFreeChart` object gets created.

In order to make available Castor XML Mapping-based chart themes, in the `jasperreports_extension.properties` file the following lines should be uncommented:

```
net.sf.jasperreports.extension.registry.factory.xml.chart.themes=net.sf.jasperreports.chartthemes.simple.XmlChartThemeExtensionsRegistryFactory
net.sf.jasperreports.xml.chart.theme.simple=net/sf/jasperreports/chartthemes/simple/simple.jrctx
```

All other lines in the file have to be commented.

### Using chart themes

Themes can be associated globally with all charts by using the following configuration property in the `jasperreports.properties` file:

`net.sf.jasperreports.chart.theme=<theme_name>`

This configuration property can be used at report level too, in order to override the chart theme to be used for the current report:

`<property name="net.sf.jasperreports.chart.theme" value="theme_name"/>`

Changing the chart theme is also possible at chart element level using the theme attribute:

```
<chart theme="theme_name">
...
</chart>
```

### Running the Sample

Running the sample requires the [Apache Maven](https://maven.apache.org) library. Make sure that `maven` is already installed on your system (version 3.6 or later).\
In a command prompt/terminal window set the current folder to `demo/samples/chartthemes` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/chartthemes/target/reports` directory.

