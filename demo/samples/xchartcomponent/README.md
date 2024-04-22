
# JasperReports - XChart Component Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>
Shows how XChart components can be included in reports.

### Main Features in This Sample

[Implementing Custom Components to Embed Third Party Visualisation Tools (XChart Library)](#xchartcomponent)

## <a name='xchartcomponent'>Implementing</a> Custom Components to Embed Third Party Visualisation Tools (XChart Library)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to implement a custom component to wrap charts rendered by the [XChart](https://knowm.org/open-source/xchart/) library.

**Since:** 6.4.3

### The XChart Component - Overview

This sample contains an XY Area chart component implementation based on the [XChart](https://knowm.org/open-source/xchart/) library, that illustrates how charts generated with 3-rd party APIs can be embedded in reports generated with the JasperReports library. To make such an integration possible, the chart component provides its specific XSD schema in the `src/xchart/xchart.xsd` file.

On the API side, the chart component is represented by the `xchart.XYChartComponent` class, which exposes the members declared in the schema. When the report is filled, a [Renderable](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/renderers/Renderable.html) instance is created in order to generate a chart image to be included into the report.

### The XChart Component - Schema

Here is the structure of the XY chart component:

```
<element name="XYChart" substitutionGroup="jr:component">
  <complexType>
    <complexContent>
      <extension base="jr:componentType">
        <sequence>
          <element ref="xc:XYDataset"/>
          <element name="chartTitleExpression">
            <complexType mixed="true"/>
          </element>
          <element name="xAxisTitleExpression">
            <complexType mixed="true"/>
          </element>
          <element name="yAxisTitleExpression">
            <complexType mixed="true"/>
          </element>
        </sequence>
        <attribute name="evaluationTime" type="jr:basicEvaluationTime" use="optional" default="Now"/>
        <attribute name="evaluationGroup" type="string" use="optional"/>
      </extension>
    </complexContent>
  </complexType>
</element>

<element name="XYDataset">
  <complexType>
    <sequence>
      <element ref="jr:dataset" minOccurs="0" maxOccurs="1"/>
      <element ref="xc:XYSeries" minOccurs="0" maxOccurs="unbounded"/>
    </sequence>
  </complexType>
</element>

<element name="XYSeries">
  <annotation>
    <documentation>Defines a series used in an <elem>XYDataset</elem></documentation>
  </annotation>
  <complexType>
    <sequence>
      <element ref="jr:seriesExpression" minOccurs="1" maxOccurs="1"/>
      <element ref="jr:xValueExpression" minOccurs="1" maxOccurs="1"/>
      <element ref="jr:yValueExpression" minOccurs="1" maxOccurs="1"/>
      <element name="colorExpression" minOccurs="0" maxOccurs="1">
        <complexType mixed="true"/>
      </element>
    </sequence>
  </complexType>
</element>
```

A well defined XY chart configuration contains:

- an `XYDataset` - the XY chart dataset characterized by its dataset and XYSeries elements.
- a `chartTitleExpression` - the expression used to set the chart title.
- an `xAxisTitleExpression` - the expression used to set the X axis title.
- an `yAxisTitleExpression` - the expression used to set the Y axis title.

and provides the following attributes:

- `evaluationTime`
- `evaluationGroup`

with the same meaning as for built-in report elements.

Each `XYSeries` element is characterized by:

- a `seriesExpression` - mandatory expression used to set the series name
- an `xValueExpression` - mandatory numeric expression used to set the X axis value
- an `yValueExpression` - mandatory numeric expression used to set the Y axis value
- a `colorExpression` - optional expression used to set the series color. If multiple colors are set for the same series during the component filling process, the last color will be used

### Embedding The XChart Component

The `src/jasperreports_extension.properties` file contains the following entries:

```
net.sf.jasperreports.extension.registry.factory.xchart=net.sf.jasperreports.extensions.SpringExtensionsRegistryFactory
net.sf.jasperreports.extension.xchart.spring.beans.resource=xchart/xchart_beans.xml
```

Hence this component will be registered as Spring-based extension, in accordance with beans defined in the `src/xchart/xchart_beans.xml`:

```
<bean id="componentsBundle" class="net.sf.jasperreports.engine.component.DefaultComponentsBundle">
  <property name="xmlParser">
    <ref bean="xmlParser"/>
  </property>
  <property name="componentManagers">
    <map>
      <entry key="XYChart">
        <ref bean="XYChartManager"/>
      </entry>
    </map>
  </property>
</bean>

<bean id="xmlParser" class="net.sf.jasperreports.engine.component.DefaultComponentXmlParser">
  <property name="namespace">
    <value>http://jasperreports.sourceforge.net/jasperreports/xchart</value>
  </property>
  <property name="publicSchemaLocation">
    <value>http://jasperreports.sourceforge.net/dtds/xchart.xsd</value>
  </property>
  <property name="internalSchemaResource">
    <value>xchart/xchart.xsd</value>
  </property>
  <property name="digesterConfigurer">
    <bean class="xchart.XChartDigester"/>
</property>
</bean>

<bean id="XYChartManager" class="net.sf.jasperreports.engine.component.DefaultComponentManager">
  <property name="componentCompiler">
    <bean class="xchart.XYChartCompiler"/>
  </property>
  <property name="componentXmlWriter">
    <bean class="xchart.XYChartXmlWriter"/>
  </property>
  <property name="componentFillFactory">
    <bean class="xchart.XYChartFillFactory"/>
  </property>
</bean>
```

The `src/xchart` directory contains all necessary implementation APIs for this component:

```
CompiledXYDataset.java
DesignXYDataset.java
DesignXYSeries.java
FillXYChart.java
FillXYDataset.java
FillXYSeries.java
XChartDigester.java
XYChartCompiler.java
XYChartComponent.java
XYChartFillFactory.java
XYChartXmlFactory.java
XYChartXmlWriter.java
XYDataset.java
XYDatasetXmlFactory.java
XYSeries.java
XYSeriesData.java
XYSeriesFactory.java
```

### The XChart Component - Sample

An example of how to use the XY area chart component is illustrated in the `reports/XYChart.jrxml` template:

```
<style name="chartStyle" backcolor="#DDDDDD">
  <box>
    <pen lineWidth="1"/>
  </box>
</style>
<subDataset name="xyDataset">
  <parameter class="net.sf.jasperreports.engine.data.JRCsvDataSource" name="xyDatasource"/>
  <field name="name" class="java.lang.String"/>
  <field name="x" class="java.lang.Integer"/>
  <field name="y" class="java.lang.Integer"/>
  <field name="color" class="java.lang.String"/>
</subDataset>
<parameter class="net.sf.jasperreports.engine.data.JRCsvDataSource" name="xyDatasource"/>

...

<componentElement>
  <element x="0" y="80" width="545" height="320" style="chartStyle"/>
  <xc:XYChart xmlns:xc="http://jasperreports.sourceforge.net/jasperreports/xchart"
    xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports/xchart
    http://jasperreports.sourceforge.net/dtds/xchart.xsd" evaluationTime="Now">
    <xc:XYDataset>
      <dataset>
        <datasetRun subDataset="xyDataset">
          <dataSourceExpression><![CDATA[$P{xyDatasource}]] ></dataSourceExpression>
        </datasetRun>
      </dataset>
      <xc:XYSeries>
        <seriesExpression><![CDATA[$F{name}]] ></seriesExpression>
        <xValueExpression><![CDATA[$F{x}]] ></xValueExpression>
        <yValueExpression><![CDATA[$F{y}]] ></yValueExpression>
        <xc:colorExpression><![CDATA[$F{color}]] ></xc:colorExpression>
      </xc:XYSeries>
    </xc:XYDataset>
    <xc:chartTitleExpression><![CDATA["XY Area Chart"]] ></xc:chartTitleExpression>
    <xc:xAxisTitleExpression><![CDATA["Probability"]] ></xc:xAxisTitleExpression>
    <xc:yAxisTitleExpression><![CDATA["Amount"]] ></xc:yAxisTitleExpression>
  </xc:XYChart>
</componentElement>
```

The report is filled using a CSV data source based on the `data/xyDatasource.csv` file, as shown in `src/XChartApp.java`:

```
public void fill() throws JRException
{
  long start = System.currentTimeMillis();
  Map<String, Object> parameters = new HashMap<String, Object>();
  try
  {
    JRCsvDataSource xyds = new JRCsvDataSource(
        JRLoader.getLocationInputStream("data/xyDatasource.csv"),
        "UTF-8");
    xyds.setRecordDelimiter("\r\n");
    xyds.setUseFirstRowAsHeader(true);
    parameters.put("xyDatasource", xyds);
  }
  catch (Exception e)
  {
    throw new JRException(e);
  }
  JasperFillManager.fillReportToFile(
      "build/reports/XYChart.jasper",
      new HashMap<String, Object>(parameters),
      new JREmptyDataSource());
  System.err.println("Filling time : " + (System.currentTimeMillis() - start));
}
```

The CSV datasource is stored in the `xyDatasource` parameter that will be passed to the XY chart dataset at runtime. The output will be an XY area chart with three different series, having the amount probability on the x-axis and the amount on the y-axis. The series colors are also provided by the CSV datasource parameter.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/xchartcomponent ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/xchartcomponent/target/reports` directory.
