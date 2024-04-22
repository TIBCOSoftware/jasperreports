
# JasperReports - Shapes Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how various graphic elements could be used inside report templates.

## Main Features in This Sample

[Lines, Rectangles, Ellipses (Shapes)](#shapes)

## <a name='shapes'>Lines</a>, Rectangles, Ellipses (Shapes)
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to use shape elements such as rectangles, ellipses and lines.

**Since:** 0.1.0

### Common graphic elements

Shapes are predefined geometric objects, very useful when drawing diagrams, flowcharts and other schematic representations. There are a lot of shapes: lines, rectangles, arrows, callouts, stars, etc. All of them can be obtained from a set of line shapes, taking into account that a line shape can be either straight or curved.

JasperReports provides support for lines, rectangles and ellipses. All shape objects in JasperReports are graphic report elements which implement both [JRCommonElement](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRCommonElement.html) and [JRGraphicElement](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRGraphicElement.html) interfaces.

By default any common element is characterized by its width, height, unique key identifier, visualization mode, foreground and background colors. Methods exposed by the [JRCommonElement](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRCommonElement.html) are:

```
public int getWidth();
public int getHeight();
public String getKey();
public ModeEnum getModeValue();
public ModeEnum getOwnModeValue();
public void setMode(ModeEnum mode);
public Color getForecolor();
public Color getOwnForecolor();
public void setForecolor(Color forecolor);
public Color getBackcolor();
public Color getOwnBackcolor();
public void setBackcolor(Color backcolor);
```

The [JRGraphicElement](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRGraphicElement.html) interface extends the [JRCommonGraphicElement](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRCommonGraphicElement.html). By default any common graphic element (or shape) should have a contour line and a fill type (ie solid, no fill, etc).

Methods which should be implemented from the [JRCommonGraphicElement](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRCommonGraphicElement.html) interface are:

```
public JRPen getLinePen();
public FillEnum getFill();
public FillEnum getOwnFill();
public void setFill(FillEnum fillEnum);
```

### The line shape

Derived from common graphic elements, a line is an one-dimensional shape which is supposed to have a given direction. Any line element should implement the JRLine interface which exposes the following methods:

```
public LineDirectionEnum getDirection();
public void setDirection(LineDirectionEnum lineDirectionEnum);
```

Possible line directions are: `top-down` or `bottom-up`. The default direction is `top-down`.
In the `.jrxml` file a line element is represented as follows:

```
<element kind="line" y="150" width="200" height="40" forecolor="#00FF00" direction="BottomUp">
  <pen lineWidth="4.0"/>
</element>
```

The element's fill attribute is not specified because at the moment only the solid fill type is taken into account and, it is considered as default value.

### The rectangle shape

A rectangle is a bi-dimensional shape having either rounded corners or no-rounded ones. The radius element, if set, indicates how the rectangle's corners should be rounded. By default corners are not rounded in a rectangle.

Any rectangle shape implements the [JRRectangle](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRRectangle.html) interface, which extends the [JRCommonRectangle](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRCommonRectangle.html). The [JRCommonRectangle](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRCommonRectangle.html) exposes the following methods:

```
public int getRadius();
public Integer getOwnRadius();
public void setRadius(Integer radius);
```

In the .jrxml file a rectangle element is represented as follows:

```
<element kind="rectangle" y="250" width="200" height="40" radius="5" backcolor="#FFFF99">
	<pen lineWidth="0.0"/>
</element>
```

If corners are not rounded, then the `radius` attribute is not necessary.

### The ellipse shape

An ellipse is a closed bi-dimensional curved shape characterized by 2 semiaxis: the major and the minor semiaxis. When the major axis equals the minor semiaxis, the ellipse becomes a circle.

Any ellipse shape implements the [JREllipse](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JREllipse.html) interface, which extends the [JRGraphicElement](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRGraphicElement.html).

No supplementary methods are needed, because both ellipse's semiaxis can be determined from the common element's width and height attributes.

In the `.jrxml` file an ellipse element is represented as follows:

```
<element kind="ellipse" y="600" width="200" height="40" forecolor="#FF0000" backcolor="#FFFF99">
  <pen lineWidth="2.0"/>
</element>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/shapes` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/shapes/target/reports` directory.
