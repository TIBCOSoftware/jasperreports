
# JasperReports - Stretch Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how dynamic text fields can stretch to display their entire content and different ways to put a border around such text fields.

### Main Features in This Sample

[Stretching Text Fields](#stretch)

## <a name='stretch'>Stretching</a> Text Fields
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to make text fields resize dynamically and render all their runtime text content.

**Since:** 0.1.0

### Pixel-Perfect Representation and Report Element Stretch

JasperReports prepares documents ready to be printed out in a pixel-perfect representation. In order to accomplish this, each report element provides four mandatory attributes used to determine the absolute position of the element within the document layout: two coordinates (`x` and `y`) and two rectangular dimensions (`width` and `height`). When all elements in the document contain static information only, these four attributes are sufficient to completely localize an element within the page, because the amount of space needed to represent static data can be calculated at report design time.

But dynamic elements may be involved in a report design too. These elements may acquire their data at runtime, and one cannot calculate the most appropriate dimensions needed to display the entire element's information. It is possible that when running the report the content of a text element do not fit into its precalculated area. In this case the engine will either truncate the text content or, if stretching is allowed for that element, increase the height of the element to accommodate the content. To do so, runtime measurements and calculations are required.

Usually, the stretching process refers to the height adjustment only. When stretching report elements, adjusting the width could affect also the page width and raise unexpected errors at runtime (for instance, truncated information could be printed out on pages). This is why stretching an element let its width unchanged, while its height gets definitely enlarged in order to make room for all information that have to be displayed.

### Report Element Size

Any report element's size can be calculated using the two mandatory attributes width and height, measured in pixels. They can be set only at report design time and afterwards their value remains invariant.

This is a satisfactory solution for elements with static content only, because one can very simple to determine the rectangle area needed to expose the entire element content, and the element won't need any size adjustment at runtime.

However, for dynamic elements both content and actual size are completely determined at runtime. Therefore some element stretching settings are necessary to instruct the reporting engine to ignore the element's original size attributes and allow it to stretch in height. Even in this case, the static `width` and `height` attributes still remain mandatory, since the element may not be smaller than the originally specified dimensions.

Stretching elements in height can be managed very well, due to the section split facility: with very few exceptions (ie the column and page footers), report sections can also stretch beyond the initial specified height, and split onto next page (see the `spliType` attribute in the `<band>` element).

### Report Element Position

The other two mandatory attributes in a report element are the `x` and `y` coordinates, measured in pixels, that mark the absolute position of the top-left corner of the specified element within its parent report section.

These coordinates remain invariant if all elements in the report would have only static content.\
Things change when dynamic text fields are implied and some of them stretch beyond the initial dimensions to allow the whole information to be displayed at runtime. This may affect the neighboring elements in the same report section, especially those placed immediately below them.

When a given element stretches in height, all elements below should rearrange themselves in order to avoid their overlapping. Usually they have to be translated with the same amount downwards along the vertical axis. As consequence, the report section containing that element will be stretched too.

The `positionType` attribute specifies the behavior that a report element will have if the layout of the report section in which it is been placed is stretched.\
There are three possible values for the positionType attribute:

- `positionType="Float"` - The element floats in its parent section if it is pushed downward by other elements found above it. It tries to conserve the distance between it and the neighboring elements placed immediately above it.
- `positionType="FixRelativeToTop"` - The current report element simply ignores what happens to the other section elements and tries to conserve the y offset measured from the top of its parent report section. This is the default position.
- `positionType="FixRelativeToBottom"` - If the height of the parent report section is affected by elements that stretch, the current element tries to conserve the original distance between its bottom margin and the bottom of the section.

### The `stretchType` Attribute

When stretchable text fields are present on a report section, the height of the report section itself is affected by the stretch. In this case the engine should be instructed how to represent the other report elements existing in a stretched section, in order to preserve as much as possible the pixel-perfect representation.

The `stretchType` attribute of a report element can be used to customize the stretch behavior of the element when the enclosing report section stretches itself according to the text fields which stretch themselves. The element response to the modification of the report section layout could be:

- `strechType= "NoStretch"` - The element won't stretch. It preserves its original height.
- `stretchType="ElementGroupHeight"` - the element will adapt its height to match the changing height of the overall element group it is part of, without taking into account changes of the Y position of the element due to floating and/or collapsing white space above it.
- `stretchType="ElementGroupBottom"` - the element will adapt its height to match the changing height of the overall element group it is part of, taking into account changes of the Y position of the element due to floating and/or collapsing white space above it. The element's distance to the group's initial bottom edge will be preserved.
- `stretchType="ContainerHeight"` - the element will adapt its height to match the new height of the container it is placed on, without taking into account changes of the Y position of the element due to floating and/or collapsing white space above it.
- `stretchType="ContainerBottom"` - the element will adapt its height to match the new height of the container it is placed on, taking into account changes of the Y position of the element due to floating and/or collapsing white space above it. The element's distance to the container's initial bottom edge will be preserved.

### Dynamic Text Fields

Text fields are elements with specific stretch behavior. When their content does not fit within their boundaries, the engine either truncate them to fit within the initial dimensions, or recalculates their height in order to make room for all the text content. Text fields are the elements which require dynamic text measurement at runtime and then start the element size recalculation for all the elements in the affected section. After text fields are measured and stretched, all other elements in the same section are resized or repositioned, according to their own stretching attributes.
To decide whether a text field content gets truncated at runtime or no, one can use the `textAdjust` attribute in the `<element/>` tag. If `StretchHeight value is set, then the text field height will be automatically increased until the whole text content can be displayed. The font size can be decreased so that the text fits using the `ScaleFont` value. The default value is `CutText`.

**Notes:**

- Text fields with delayed evaluation do not stretch to acquire all the expression’s content. This is because the text element height is calculated when the report section is generated, and even if the engine comes back later with the text content of the text field, the element height will not adapt, because this would ruin the already created layout.
- When filling report templates horizontally, dynamic text fields inside the detail section do not stretch to their entire text content, because this might cause misalignment on the horizontal axis of subsequent detail sections. The detail band actually behaves the same as the page and column footers, preserving its declared height when horizontal filling is used.

### Text Truncation

In some situations preserving the initial layout takes precedence over any other runtime modification. If the text content gets wider than the available area, then a truncation mechanism is applied in order to keep only the part of text which fits within the original boundaries. If one choose the text truncation as desired behavior for a text element, a series of custom properties can be set to indicate how to perform the truncation:

- `net.sf.jasperreports.text.truncate.at.char` - Flag that determines whether text elements are to be truncated at the last character that fits. By default, when the entire text of a text element does not fit the element's area, the text is truncated at the last word that fits the area.
- `net.sf.jasperreports.text.truncate.suffix` - Contains a suffix for the truncated text. The suffix is appended to the text when truncation occurs. If the property is not defined, no suffix will be used when the text is truncated.
- `net.sf.jasperreports.print.keep.full.text` - Flag to determine whether the fill process must preserve the original text for text elements that are not able to fit their entire contents. When this property is set, the engine saves the original text in the JRPrintText print text object, along with the index at which the text is to be truncated by the print object. The original text can be then entirely exported to layout insensitive formats such as CSV, XML, Excel.

### Text Truncation and Element Stretching Examples

This sample provides some examples of element stretching and text truncations. One can see various combinations between different elements stretching attributes (`positionType, stretchType, textAdjust`):

```
<element kind="elementGroup">
  <element kind="line" positionType="Float" stretchType="ElementGroupHeight" x="145" y="5" width="1" height="1" printWhenDetailOverflows="true"/>
  <element kind="textField" x="150" y="5" width="100" hTextAlign="Justified" textAdjust="StretchHeight" removeLineWhenBlank="true" fontSize="12.0">
    <expression><![CDATA["This is a FIRST long chunk of text that will cause the text field to stretch outside
      its defined height and force other elements to move downwards."]] ></expression>
  </element>
  <element kind="line" positionType="Float" stretchType="ElementGroupHeight" x="275" y="5" width="1" height="1" printWhenDetailOverflows="true"/>
</element>
or text truncation properties:
<element kind="staticText" x="145" y="205" width="130" height="100" fontSize="10.0">
  <text><![CDATA[Text elements can also be truncated at the last character that fits the element reserved area; the behavior is triggered
by setting a property. This sentence might not fit fully in the space reserved for the element.]] ></text>
  <property name="net.sf.jasperreports.text.truncate.at.char" value="true"/>
</element>
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/stretch ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/stretch/target/reports` directory.
