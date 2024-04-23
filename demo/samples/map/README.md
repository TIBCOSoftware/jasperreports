
# JasperReports - Map Component Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Illustrates the usage of the map component element.

### Main Features in This Sample

[Using the Built-in Map Component](#map)

## <a name='map'>Using</a> the Built-in Map Component
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to render maps using the built-in map component.

**Since:** 4.1.1

### Google Map Component Overview

The map component in JasperReports exposes some of the main characteristics necessary to generate the most common Google maps, but feature upgrades for this component are planned for future development.

**Important notes:**
1. **The JR built-in map component is based on the public [Google Maps APIs](http://code.google.com/apis/maps/documentation/javascript/reference.html) licensed under the [Creative Commons Attribution 3.0 License](http://creativecommons.org/licenses/by/3.0). Prior to start using this component, please consult both Google [terms of service](https://developers.google.com/maps/terms) and Google [site policies](https://developers.google.com/readme/policies) pages.**\
**Ensure the map component in your application is used in accordance with these terms and conditions.**

2. **In order to get accurate results with Google maps, you need to set the `net.sf.jasperreports.components.map.key` property with an appropriate Google API key value.**\
**The Google API key must be enabled for Google Javascript API, Google Maps Static API and Google Geocoding API. More information about API keys can be found at [Google Maps Platform](https://developers.google.com/maps/gmp-get-started).**\
**The `net.sf.jasperreports.components.map.key` property can be set either in the report itself or per report context.**

The current map component is characterized by the `kind="googlemap"` attribute and contains the following structure:

- Child elements
    - `<latitudeExpression/>`
    - `<longitudeExpression/>`
    - `<addressExpression/>`
    - `<zoomExpression/>`
    - `<languageExpression/>`
    - `<markerData/>`
- Attributes
    - `evaluationTime`
    - `evaluationGroup`
    - `mapType`
    - `mapScale`
    - `imageType`

### Map Attributes

One can customize the map component using the following attributes:

- `mapType` - indicates the map type. Possible values are:
    - `roadmap` (default)
    - `satellite`
    - `terrain`
    - `hybrid`
- `mapScale` - represents the scale value used to return higher-resolution map images when working with high resolution screens available on mobile devices. Possible values are:
    - `1` (default)
    - `2`
    - `4` (for Business customers only)
- `imageType` - represents the image format of the map. Possible values are:
    - `png` (default)
    - `png8`
    - `png32`
    - `gif`
    - `jpg`
    - `jpg-baseline`
- `onErrorType` - Applies to static maps only. This attribute specifies the behavior of the engine when the image is not available. Possible values are:
    - `Error` - An exception is raised when loading the map image. This is the default behavior.
    - `Blank` - The exception is ignored and the map image displays as blank.
    - `Icon` - The exception is ignored and an image replacement icon is displayed.

### The Latitude Expression

The `<latitudeExpression/>` represents the latitude coordinate of the map center, necessary to locate it on the Earth surface. Allowed values are floating point numbers representing degrees from -90° to 90°.

### The Longitude Expression

The `<longitudeExpression/>` represents the longitude coordinate of the map center, necessary to locate it on the Earth surface. Allowed values are floating point numbers representing degrees from -180° to 180°.

### The Address Expression

The `<addressExpression/>` represents the address of the map center and is optional. It will be considered only when the latitude and/or longitude expressions are missing or empty. Computing address expressions relies on expensive time and resources consumption, therefore the report filling performance may visibly decline. It is highly recommended to use latitude and longitude expressions whenever possible.

### The Zoom Expression

The `<zoomExpression/>` represents the initial map zoom level. The expression allows integer values and defaults to 0.

### The Language Expression

The `<languageExpression/>` represents the language code used to localize the textual information displayed on the map, such as control names, copyright notices and other various labels. Supported languages can be found [here](https://spreadsheets.google.com/spreadsheet/pub?key=0Ah0xU81penP1cDlwZHdzYWkyaERNc0xrWHNvTTA1S1E&gid=1).

### The Marker Data Element

The `<markerData/>` element is used to generate the list of marker elements to be displayed on the map. It can collect its data either from a subdataset, or from a hardcoded list of marker item elements.

The marker item element contains a list of specific item properties that can be set either by value or by `valueExpression`. If both are set, the `valueExpression` takes precedence over the value attribute. With few exceptions, property names must match the names of supported marker options available [here](https://developers.google.com/maps/documentation/javascript/reference#MarkerOptions).

The main exception is the position option, currently undefined in JR. It is actually replaced by the latitude and longitude property names, both necessary to compute the marker position on the map. If at least one of these two properties is missing or empty, try to set the equivalent `address` property. If no address is available, an exception will be thrown.

**Warnings:**

To avoid such exceptions when using a subdataset, use a `<filterExpression>` to skip markers with empty latitude/longitude and address properties. A data filtering example can be found in the map component sample.

The intensive use of the address property is not recommended, because it will definitely slow the report generation. Try to use the latitude and longitude properties when they are available.

Below is a list of valid `itemProperty` names:

- `latitude` - floating-point value
- `longitude` - floating-point value
- `address` - optional; string value; considered only when latitude and/or longitude are missing or empty
- `title` - string value
- `url` - string value. Link to a page or document that will be open when the marker is clicked on. See also [Info Windows](../map/README.md#infoWindows)
- `target` - string value. The target window/frame to load the marker URL. Allowed values are:
    - `_blank` - the URL is loaded into a new window
    - `_parent` - the URL is loaded into the parent frame
    - `_self` - the URL replaces the current page
    - `_top` - the URL replaces any framesets that may be loaded
    - a valid window/frame name
- `icon` - string value (representing the icon URL). See also [Custom Icons](../map/README.md#customIcons)
- `shadow` - string value (representing the shadow icon URL). See also [Custom Icons](../map/README.md#customIcons)
- `color` - string value representing the color for the default pin icons. This property is taken into account when none of icon or icon.url properties are set. Its value may represent:
    - one of the predefined color names in the following list (see also https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/type/ColorEnum.html):
        - `black`
        - `blue`
        - `cyan`
        - `darkGray`
        - `gray`
        - `green`
        - `lightGray`
        - `magenta`
        - `orange`
        - `pink`
        - `red`
        - `yellow`
        - `white`
    - a valid color hex code (prefixed by #). For instance: `#0000FF`.
    - the color RGB value.
- `label` - string value representing the label for the default pin icons. This property is taken into account when none of icon or icon.url properties are set.\
**Note:** Unlike interactive maps, Google static maps require single uppercase alphanumeric character labels. If multiple character labels are set, the JR engine will process the entire label for dynamic maps and only the first character in the label for static maps. Labels are displayed only for default and mid sized markers, small and tiny markers provide no capability to display labels.
- `cursor` - string value
- `zIndex` - integer value
- `clickable` - boolean value
- `draggable` - boolean value
- `flat` - boolean value
- `optimized` - boolean value
- `raiseOnDrag` - boolean value
- `visible` - boolean value
- `size` - predefined string value. This property is considered for static maps only. See the [Google Static Maps](../map/README.md#googleStaticMaps) section.

### Custom Icons

Both icon and shadow properties indicate an image location, without any additional information. In this case the image is loaded as it is and is placed in built-in positions on the map, using Google Maps defaults. But sometimes, marker icons need more detailed configurations, in order to get a better map appearance. In this case one can build custom icons, with the following properties:

- For marker icons:
    - `icon.url` - string value (representing the custom icon URL). If set, it takes precedence over the `icon` property.
    - `icon.width` - integer value representing the icon width, in pixels. To be considered, `icon.height` must be also set.
    - `icon.height` - integer value representing the icon height, in pixels. To be considered, `icon.width` must be also set.
    - `icon.origin.x` - integer value representing the horizontal position of the image within a sprite, if any, in pixels. Default value is `0`.
    - `icon.origin.y` - integer value representing the vertical position of the image within a sprite, if any, in pixels. Default value is `0`.
    - `icon.anchor.x` - integer value representing the horizontal position to anchor an image with respect to the location of the marker on the map. Default value is `0`.
    - `icon.anchor.y` - integer value representing the vertical position to anchor an image with respect to the location of the marker on the map. Default value is `0`.
- For shadow icons:
    - `shadow.url` - string value (representing the custom shadow icon URL). If set, it takes precedence over the shadow property.
    - `shadow.width` - integer value representing the shadow icon width, in pixels. To be considered, `shadow.height` must be also set.
    - `shadow.height` - integer value representing the shadow icon height, in pixels. To be considered, `shadow.width` must be also set.
    - `shadow.origin.x` - integer value representing the horizontal position of the shadow image within a sprite, if any, in pixels. Default value is `0`.
    - `shadow.origin.y` - integer value representing the vertical position of the shadow image within a sprite, if any, in pixels. Default value is `0`.
    - `shadow.anchor.x` - integer value representing the horizontal position to anchor a shadow image with respect to the location of the marker on the map. Default value is `0`.
    - `shadow.anchor.y` - integer value representing the vertical position to anchor a shadow image with respect to the location of the marker on the map. Default value is `0`.

### Info Windows

When a marker icon is clicked on the map, additional information can be displayed either in a separate window/frame or in an info window floating above the map. The text content in an info window is processed as HTML, therefore the HTML notation should be used for special characters (see reserved characters [here](http://www.w3schools.com/tags/ref_entities.asp).)

If both `url` and `infoWindow` settings are present in a marker definition, `infoWindow` takes precedence over the marker URL. Clicking on the marker icon will open the info window.
To configure an info window, the following item properties are available:

- `infowindow.content` - string value. Represents the content to be displayed in an info window, processed as HTML fragment. Is mandatory for info windows. Empty contents are not processed.
- `infowindow.latitude` - floating-point value. Represents the latitude used to determine the info window position on the map. Neglected for info windows attached to markers.
- `infowindow.longitude` - floating-point value. Represents the longitude used to determine the info window position on the map. Neglected for info windows attached to markers.
- `infowindow.pixelOffset` - integer value. Represents the offset (in pixels) from the tip of the info window to the location on which the info window is anchored.
- `infowindow.maxWidth` - integer value. Specifies the maximum width in pixels of the info window

###Paths on Google Maps

Various locations on a given map can be connected to form a path. To configure a path on the map, the following information is needed:

- an ordered sequence of points to be connected. The sequence must contain at least 2 points in order to be considered.
- various style descriptors (such as line color, opacity or weight), in order to create custom appearances for paths

### The Path Data Element

Points that have to be connected in a path are grouped into the `<pathData/>` element. A path data element is very similar to the marker data element described above, but comes with a different set of item properties. Each item in the path data element represents a place on the map and must provide either the latitude and longitude, or the equivalent address property from the following list of allowed properties:

- `latitude` - floating-point value
- `longitude` - floating-point value
- `address` - optional; string value
- `name` - optional; string value. Represents the identifier of a given path, necessary when multiple paths have to be represented on the same map. Points with the same name property belong to the same path. If not provided, the point will be added to the default path.
- `style` - optional; string value. Represents the name of a given path style (path styles are discussed in the next section of this document). If not provided, the path will be generated using Google default style settings for paths.
- `strokeColor` - optional; string value. Represents the path line color. Named colors are not supported for all Google Maps APIs, so the hex value representation for the color would be more suited in this case.
- `strokeOpacity` - optional; numeric value between 0 and 1 (default). Represents the path line opacity. 1 means 100% opaque, 0 means 100% transparent.
- `strokeWeight` - optional; integer value. Represents the path line weight in pixels. Default value is `5`.
- `fillColor` - optional; string value. Considered for polygons only, represents the fill color for the polygonal contour. Also named colors are not recommended in this case.
- `fillOpacity` - optional; numeric value between 0 (default) and 1. Considered for polygons only, represents the fill opacity for the polygonal contour. 1 means 100% opaque, 0 means 100% transparent.
- `isPolygon` - optional; boolean value. If set to true the last point in the path is connected to the first point in order to form a polygon (closed path). Default value is `false`
- `clickable` - optional; boolean value. If set to true, the path object may handle mouse events. Defaults to `true`.
- `editable` - optional; boolean value. If set to true, the user may edit the path using the control points shown along the path line. Defaults to `false`.
- `draggable` - optional; boolean value. If set to true, the user may drag the path contour over the map. Defaults to `false`.
- `geodesic` - optional; boolean value. Defines the mode of dragging. If set to true, edges of the polygon are interpreted as geodesic and will follow the curvature of the Earth when the path contour is dragged. Defaults to `false`.
- `visible` - optional; boolean value. If set to true, the path is visible on the map. Defaults to `true`.
- `zIndex` - optional; integer value. Represents the zIndex compared to other path elements
- `path.hyperlink` - optional; string value. Represents the hyperlink associated with this path on click event.
- `path.hyperlink.target` - optional; string value. Represents the hyperlink target associated with this path on click event. Allowed values are:
    - `_blank`
    - `_parent`
    - `_self`
    - `_top`
    - a valid window/frame name

**Notes:**

- A map component may contain 0, one or several `<pathData/>` elements.
- Points in a given path are connected one by one, in the same order they were declared in the report. To get consistent results, make sure items in the path data element are declared in the right order.
- If points in the same path provide different values for the same item property, the property value will be set by the last point in the path that provides this property.
- The intensive use of the `address` property is not recommended, because it will definitely slow the report generation. Try to use the `latitude` and `longitude` properties when they are available.

### The Path Style Element

In order to avoid redundant data, common style properties for a given path can be grouped in a `style` element, so that individual items (points) in the path have to remember only the name of the path style, instead of carrying several style properties by themselves. Path styles are grouped together into a `<pathStyle/>` element, very similar to `<pathData/>`, excepting the latitude and longitude properties that are not considered in a path style.

Below is the list of allowed properties in a path style, with the same meaning and defaults (excepting the name property) as in the path data element:

- `name` - required; string value. Represents the name (unique identifier) of a given style.
- `style` - optional; string value. Represents the name of a parent path style. If present, all the properties in the parent style will be inherited by the current style. The own properties of the current style will override the parent properties.
- `strokeColor`
- `strokeOpacity`
- `strokeWeight`
- `fillColor`
- `fillOpacity`
- `isPolygon`
- `clickable`
- `editable`
- `draggable`
- `geodesic`
- `visible`
- `zIndex`
- `path.hyperlink`
- `path.hyperlink.target`

**Notes:**

- A map component may contain 0, one or several `<pathStyle/>` elements.
- If style items with the same name provide different values for the same item property, the property value will be set by the last item in the list that provides this property.
- If present, an own style property in a path data item will override the same property inherited from the path style.

### Google Static Maps

Exporters that don't allow JavaScript or dynamic page loading may use the Google Static Map service that creates and returns a map image depending on a given set of HTTP request parameters. The map image does not support user interactivity, but can be used to substitute with some inherent limitations a dynamic map, for read-only purposes.
In these conditions, not all item properties defined for markers are considered in static maps. The available properties in this case are:

- `latitude`
- `longitude`
- `icon` - the icon URL has to be accessible for the Google Static Map service
- `icon.url` - the icon URL has to be accessible for the Google Static Map service
- `color`
- `label`
- `size` - predefined string value. Specifies the marker size (for the built-in markers only). Allowed values are:
    - `mid`
    - `tiny`
    - `small`

If no size is set, the default icon is loaded with its normal size.\
For similar reasons, path style properties considered in static maps are the following:

- `strokeColor`
- `strokeOpacity`
- `strokeWeight`
- `fillColor`
- `fillOpacity`
- `isPolygon`
- `geodesic`

### Map Component Examples

The JRXML sample contains five map components that illustrate how various settings can be applied in order to get the desired map rendering. Each map is loaded from a subreport and collect its data from the CSV datasource located in the data subfolder.

**Map #1: Simple Road Map**

The first map is loaded from the `reports/MapReport1.jrxml` template, and illustrates a road map with minimal settings. There are no markers and no additional information on the map:

```
<element kind="component" positionType="Float" y="255" width="515" height="326">
  <component kind="googlemap" evaluationTime="Report" onErrorType="Icon">
    <latitudeExpression><![CDATA[$P{latitude}]] ></latitudeExpression>
  <longitudeExpression><![CDATA[$P{longitude}]] ></longitudeExpression>
  <zoomExpression><![CDATA[$P{zoom}]] ></zoomExpression>
  <languageExpression><![CDATA[new java.util.Locale("ro").getLanguage()]] ></languageExpression>
  </component>
</element>
```

**Map #2: Satellite Map with Default Marker Icons**

The second map is loaded from the `reports/MapReport2.jrxml` template. One can notice the `mapType="satellite" mapScale="1" imageType="jpg"` attribute settings. The satellite map also contains a set of 5 markers with default icons and tooltips enabled. All marker settings are grouped in the `<markerData/>` section which collects its data from the `MarkersDataset` subdataset. Data with invalid latitude/longitude values are skipped due to the `<filterExpression/>` in the subdataset:

```
<dataset name="MarkersDataset">
  <property name="net.sf.jasperreports.data.adapter" value="/data/CsvDataAdapter.jrdax"/>
  <query language="csv"/>
  <field name="address" class="java.lang.String"/>
  <field name="latitude" class="java.lang.String"/>
  <field name="longitude" class="java.lang.String"/>
  <filterExpression>
    <![CDATA[!($F{latitude}==null || $F{latitude}.length() == 0 || $F{longitude}==null || $F{longitude}.length() == 0)]] >
  </filterExpression>
</dataset>
```

The map component configuration is the following:

```
<element kind="component" positionType="Float" y="150" width="515" height="326">
  <component kind="googlemap" mapType="satellite" imageType="jpg" onErrorType="Icon">
    <latitudeExpression><![CDATA[37.800056f]] ></latitudeExpression>
    <longitudeExpression><![CDATA[-122.4075f]] ></longitudeExpression>
    <zoomExpression><![CDATA[$P{zoom} + 1]] ></zoomExpression>
    <markerData>
    <dataset>
      <datasetRun subDataset="MarkersDataset"/>
    </dataset>
    <item>
      <property name="latitude">
        <expression><![CDATA[$F{latitude}]] ></expression>
      </property>
      <property name="longitude">
        <expression><![CDATA[$F{longitude}]] ></expression>
      </property>
      <property name="title">
        <expression><![CDATA[$F{address}]] ></expression>
      </property>
      <property name="label">
        <expression>
          <![CDATA[$V{REPORT_COUNT} % 5 < 2 ? ""+$V{REPORT_COUNT}*$V{REPORT_COUNT} :($V{REPORT_COUNT} == 2 ? "abc" : "M")]] >
        </expression>
      </property>
    </item>
  </markerData>
</component>
</element>
```

**Map #3: Terrain Map with Custom Marker Icons**

The 3rd map is a terrain sample loaded from the r`eports/MapReport3.jrxml` template. It comes with the same set of markers as Map #2, but this time marker icons are loaded from custom URLs, with the specified width and height. Below is the map component configuration (and the subdataset):

```
<dataset name="MarkersDataset">
  <property name="net.sf.jasperreports.data.adapter" value="/data/CsvDataAdapter.jrdax"/>
  <query language="csv"/>
  <field name="address" class="java.lang.String"/>
  <field name="latitude" class="java.lang.String"/>
  <field name="longitude" class="java.lang.String"/>
  <field name="iconurl" class="java.lang.String"/>
  <field name="iconwidth" class="java.lang.Integer"/>
  <field name="iconheight" class="java.lang.Integer"/>
  <filterExpression>
    <![CDATA[!($F{latitude}==null || $F{latitude}.length() == 0 || $F{longitude}==null || $F{longitude}.length() == 0)]] >
  </filterExpression>
</dataset>
...
<element kind="component" positionType="Float" y="150" width="515" height="326">
  <component kind="googlemap" mapType="terrain" imageType="jpg" onErrorType="Icon">
    <latitudeExpression><![CDATA[37.800056f]] ></latitudeExpression>
    <longitudeExpression><![CDATA[-122.4075f]] ></longitudeExpression>
    <zoomExpression><![CDATA[$P{zoom} + 1]] ></zoomExpression>
    <markerData>
      <dataset>
        <datasetRun subDataset="MarkersDataset"/>
      </dataset>
      <item>
        <property name="latitude">
          <expression><![CDATA[$F{latitude}]] ></expression>
        </property>
        <property name="longitude">
          <expression><![CDATA[$F{longitude}]] ></expression>
        </property>
        <property name="title">
          <expression><![CDATA[$F{address}]] ></expression>
        </property>
        <property name="icon.url">
          <expression><![CDATA[$F{iconurl}]] ></expression>
        </property>
        <property name="icon.width">
          <expression><![CDATA[$F{iconwidth}]] ></expression>
        </property>
        <property name="icon.height">
          <expression><![CDATA[$F{iconheight}]] ></expression>
        </property>
      </item>
    </markerData>
  </component>
</element>
```

**Map #4: Satellite Map with Marker Hyperlinks and Paths**

In the 4th sample loaded from the `reports/MapReport4.jrxml` template the same set of markers with default icons are placed on a satellite map. This time marker icons display custom colors, and in static maps only, custom sizes. Marker tooltips are disabled, but markers provide hyperlinks. Clicking on a marker icon will open a new browser window/tab with related information. In addition, all markers are connected to form a green polygonal contour and two of them are also connected with a blue line.

Two additional subdatasets were added in order to generate path styles and path data.See the configuration below:

```
<dataset name="MarkersDataset">
  <property name="net.sf.jasperreports.data.adapter" value="/data/CsvDataAdapter.jrdax"/>
  <query language="csv"/>
  <field name="latitude" class="java.lang.String"/>
  <field name="longitude" class="java.lang.String"/>
  <field name="size" class="java.lang.String"/>
  <field name="color" class="java.lang.String"/>
  <field name="url" class="java.lang.String"/>
  <filterExpression>
    <![CDATA[!($F{latitude}==null || $F{latitude}.length() == 0 || $F{longitude}==null || $F{longitude}.length() == 0)]] >
  </filterExpression>
</dataset>
<dataset name="PathStyleDataset">
  <property name="net.sf.jasperreports.data.adapter" value="/data/PathStyleDataAdapter.jrdax"/>
  <query language="csv"/>
  <field name="name" class="java.lang.String"/>
  <field name="strokecolor" class="java.lang.String"/>
  <field name="strokeopacity" class="java.lang.Double"/>
  <field name="strokeweight" class="java.lang.Integer"/>
  <field name="fillcolor" class="java.lang.String"/>
  <field name="fillopacity" class="java.lang.Double"/>
  <field name="draggable" class="java.lang.Boolean"/>
  <filterExpression><![CDATA[$F{name}!=null && $F{name}.length() != 0]] ></filterExpression>
</dataset>
<dataset name="PathLocationDataset">
  <property name="net.sf.jasperreports.data.adapter" value="/data/PathLocationDataAdapter.jrdax"/>
  <query language="csv"/>
  <field name="name" class="java.lang.String"/>
  <field name="style" class="java.lang.String"/>
  <field name="latitude" class="java.lang.String"/>
  <field name="longitude" class="java.lang.String"/>
  <field name="ispolygon" class="java.lang.String"/>
  <filterExpression>
    <![CDATA[!($F{latitude}==null || $F{latitude}.length() == 0 || $F{longitude}==null || $F{longitude}.length() == 0)]] >
  </filterExpression>
</dataset>
...
<element kind="component" positionType="Float" y="150" width="515" height="326">
  <component kind="googlemap" mapType="satellite" imageType="jpg" onErrorType="Icon">
    <latitudeExpression><![CDATA[37.800056f]] ></latitudeExpression>
    <longitudeExpression><![CDATA[-122.4075f]] ></longitudeExpression>
    <zoomExpression><![CDATA[$P{zoom} + 1]] ></zoomExpression>
    <pathStyleList>
      <pathStyleList>
        <dataset>
          <datasetRun subDataset="PathStyleDataset"/>
        </dataset>
        <item>
          <property name="name">
            <expression><![CDATA[$F{name}]] ></expression>
          </property>
          <property name="strokeColor">
            <expression><![CDATA[$F{strokecolor}]] ></expression>
          </property>
          <property name="strokeOpacity">
            <expression><![CDATA[$F{strokeopacity}]] ></expression>
          </property>
          <property name="strokeWeight">
            <expression><![CDATA[$F{strokeweight}]] ></expression>
          </property>
          <property name="fillColor">
            <expression><![CDATA[$F{fillcolor}]] ></expression>
          </property>
          <property name="fillOpacity">
            <expression><![CDATA[$F{fillopacity}]] ></expression>
          </property>
          <property name="draggable">
            <expression><![CDATA[$F{draggable}]] ></expression>
          </property>
        </item>
      </pathStyleList>
    </pathStyleList>
    <pathDataList>
      <pathDataList>
        <dataset>
          <datasetRun subDataset="PathLocationDataset"/>
        </dataset>
        <item>
          <property name="name">
            <expression><![CDATA[$F{name}]] ></expression>
          </property>
          <property name="style">
            <expression><![CDATA[$F{style}]] ></expression>
          </property>
          <property name="latitude">
            <expression><![CDATA[$F{latitude}]] ></expression>
          </property>
          <property name="longitude">
            <expression><![CDATA[$F{longitude}]] ></expression>
          </property>
          <property name="isPolygon">
            <expression><![CDATA[$F{ispolygon}]] ></expression>
          </property>
          <property name="path.hyperlink">
            <expression><![CDATA["http://www.google.com"]] ></expression>
          </property>
        </item>
      </pathDataList>
    </pathDataList>
    <markerData>
      <dataset>
        <datasetRun subDataset="MarkersDataset"/>
      </dataset>
      <item>
        <property name="latitude">
          <expression><![CDATA[$F{latitude}]] ></expression>
        </property>
        <property name="longitude">
          <expression><![CDATA[$F{longitude}]] ></expression>
        </property>
        <property name="color">
          <expression><![CDATA[$F{color}]] ></expression>
        </property>
        <property name="size">
          <expression><![CDATA[$F{size}]] ></expression>
        </property>
        <property name="url">
          <expression><![CDATA[$F{url}]] ></expression>
        </property>
        <property name="target">
          <expression><![CDATA["_blank"]] ></expression>
        </property>
      </item>
    </markerData>
  </component>
</element>
```

**Map #5: Hybrid Map with Custom Icons and Info Windows**

The last sample is loaded from `reports/MapReport5.jrxml` template. Markers provide custom icons and tooltips. Clicking on a marker icon will open an info window with related information about the marker position. The value of the `infowindow.content` property is a HTML fragment with escaped open and end tags.

```
<dataset name="MarkersDataset">
  <property name="net.sf.jasperreports.data.adapter" value="/data/CsvDataAdapter.jrdax"/>
  <query language="csv"/>
  <field name="address" class="java.lang.String"/>
  <field name="latitude" class="java.lang.String"/>
  <field name="longitude" class="java.lang.String"/>
  <field name="iconurl" class="java.lang.String"/>
  <field name="iconwidth" class="java.lang.Integer"/>
  <field name="iconheight" class="java.lang.Integer"/>
  <filterExpression>
    <![CDATA[!($F{latitude}==null || $F{latitude}.length() == 0 || $F{longitude}==null || $F{longitude}.length() == 0)]] >
  </filterExpression>
</dataset>
...
<element kind="component" positionType="Float" y="150" width="515" height="326">
  <component kind="googlemap" mapType="hybrid" imageType="jpg" onErrorType="Icon">
    <latitudeExpression><![CDATA[37.800056f]] ></latitudeExpression>
    <longitudeExpression><![CDATA[-122.4075f]] ></longitudeExpression>
    <zoomExpression><![CDATA[$P{zoom} + 1]] ></zoomExpression>
    <markerData>
      <dataset>
        <datasetRun subDataset="MarkersDataset"/>
      </dataset>
      <item>
        <property name="latitude">
          <expression><![CDATA[$F{latitude}]] ></expression>
        </property>
        <property name="longitude">
          <expression><![CDATA[$F{longitude}]] ></expression>
        </property>
        <property name="title">
          <expression><![CDATA[$F{address}]] ></expression>
        </property>
        <property name="icon.url">
          <expression><![CDATA[$F{iconurl}]] ></expression>
        </property>
        <property name="icon.width">
          <expression><![CDATA[$F{iconwidth}]] ></expression>
        </property>
        <property name="icon.height">
          <expression><![CDATA[$F{iconheight}]] ></expression>
        </property>
        <property name="infowindow.content">
          <expression>
            <![CDATA["<p style='text-align:right;'><img src='https://jasperreports.sourceforge.net/jasperreports.png'/></p>
            <p style='text-align:left;'><b>"+$F{address}+"</b></p>"]] >
          </expression>
        </property>
      </item>
    </markerData>
  </component>
</element>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/map` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/map/target/reports` directory.
