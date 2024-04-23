
# JasperReports - PDF Forms Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>
Shows how to generate editable PDF content using JasperReports.

### Main Features in This Sample

[Generating Editable PDF Content](#pdfforms)

## <a name='pdfforms'>Generating</a> Editable PDF Content
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
Shows how specific PDF export properties can be used in order to generate editable PDF content.

**Since:** 6.12.0

### PDF Forms and Input Fields

The content of a PDF document can be edited inside a PDF form that contains various input fields such as textboxes, lists, comboboxes, checkboxes, radio groups, etc. Using the JasperReports library, one can generate these input fields by setting specific custom properties in the report. In the following sections we will see how these properties can be set and how they work in conjunction to generate editable content in PDF documents.

### Input Field Types

In order to determine the appropriate content and layout of an input field we need to know its type first. The JasperReports library offers support for the following types:

- Textbox
- List
- Combobox
- Checkbox
- Radio groups

The field type can be specified in the `net.sf.jasperreports.export.pdf.field.type` property, that accepts the following values:

- `Text` - specifies a textbox; this type of input field can be applied only to text elements (static text and textfield) in JasperReports
- `Combo` - specifies a combobox
- `List` - specifies a list
- `Check` - specifies a checkbox
- `Radio` - specifies a radio group

Each input field needs a unique identifier within the PDF form. This can be set using the `net.sf.jasperreports.export.pdf.field.name` element property. In case this is not set, the field name is generated using the report element UUID.

The background color, forecolor and text formatting inside a generated input field are inherited from the JasperReports element. For the border style we need to set the following property, with values compatible with PDF border styles: `net.sf.jasperreports.export.pdf.field.border.style`.\
Possible values are:

- `Solid` - solid border
- `Dashed` - dashed border
- `Beveled` - beveled border
- `Inset` - inset border
- `Underline` - underline border

When this property is not set, the border is inherited from the element's `pen` style: if the `pen` style is `Dashed`, this style will be applied, otherwise the pen `Solid` style will be considered by default.

The border thickness is rounded and recalculated to have one of the following values compatible with PDF: [1,2,3]. In case we need some input field content to be read-only, we can set the `net.sf.jasperreports.export.pdf.field.read.only` flag for that element.

Example:

```
<element kind="ellipse" positionType="Float" mode="Opaque" width="25" height="25">
  <pen lineWidth="1.0"/>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="Check"/>
  <property name="net.sf.jasperreports.export.pdf.field.check.type" value="Square"/>
  <property name="net.sf.jasperreports.export.pdf.field.border.style" value="Beveled"/>
  <property name="net.sf.jasperreports.export.pdf.field.read.only" value="true"/>
  <property name="net.sf.jasperreports.export.pdf.field.checked" value="true"/>
</element>
```

### Text Input Fields

Textboxes are text input fields. They can be either single line or multi line textboxes. To specify which kind of textbox is considered, we can set the following flag property per report element: `net.sf.jasperreports.export.pdf.field.text.multiline`. The initial text content of the input field is provided by the net.sf.jasperreports.export.pdf.field.value custom property and in case that one is missing, then the actual text content of the JasperReports text element is used instead.

Example:

```
<element kind="textField" positionType="Float" mode="Opaque"
  x="250" width="200" height="45" forecolor="#FFFF00" backcolor="#0000FF" hTextAlign="Right" fontName="DejaVu Serif" fontSize="24.0">
  <expression><![CDATA["This\nis a multi\nline text inside a multi line text field."]] ></expression>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="Text"/>
  <property name="net.sf.jasperreports.export.pdf.field.text.multiline" value="true"/>
  <property name="net.sf.jasperreports.print.keep.full.text" value="true"/>
</element>
```

### List and Combobox Input Fields

List and combobox input fields can be populated by setting the following property: `net.sf.jasperreports.export.pdf.field.choices`. Its value contains list items separated by a special character or set of characters provided by the `net.sf.jasperreports.export.pdf.field.choice.separators` property. If not set, the default separator is `'|'`.

Example:

```
<element kind="textField" positionType="Float" mode="Opaque" width="200" height="95" forecolor="#00FFFF" backcolor="#00FF00"
hTextAlign="Center" fontName="DejaVu Serif" bold="true" fontSize="24.0">
  <expression><![CDATA["This text does not matter in PDF export."]] ></expression>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="List"/>
  <property name="net.sf.jasperreports.export.pdf.field.choice.separators" value=","/>
  <property name="net.sf.jasperreports.export.pdf.field.choices" value="Un,Deux,Trois,Quatre,Cinq"/>
  <property name="net.sf.jasperreports.export.pdf.field.value" value="Quatre"/>
  <box>
    <pen lineWidth="2.0" lineStyle="Dashed" lineColor="#FF0000"/>
  </box>
</element>
```

In case we need the textbox in the combobox field to be editable, we can set the `net.sf.jasperreports.export.pdf.field.combo.edit` property per combobox element, while the a preselected value can be set using the `net.sf.jasperreports.export.pdf.field.value` property:

```
<element kind="textField" ... forecolor="#0000FF" backcolor="#FFFF00" hTextAlign="Center" fontName="DejaVu Serif" bold="true" fontSize="24.0">
  <expression><![CDATA["This text does not matter in PDF export."]] ></expression>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="Combo"/>
  <property name="net.sf.jasperreports.export.pdf.field.choices" value="One|Two|Three|Four|Five"/>
  <property name="net.sf.jasperreports.export.pdf.field.combo.edit" value="true"/>
</element>
```

### Checkbox Input Field

In order to properly set a checkbox we need to know how it's decorated and whether it's checked or no. We can set the checkbox appearance by using the `net.sf.jasperreports.export.pdf.field.check.type` property with the following possible values:

- `Check` - specifies a checkbox type decoration
- `Circle` - specifies a circle tipe decoration
- `Cross` - specifies a cross type decoration
- `Diamond` - specifies a diamond type decoration
- `Square` - specifies a square type decoration
- `Star` - specifies a star type decoration

To indicate if the checkbox is checked when the PDF document is open, we can use the `net.sf.jasperreports.export.pdf.field.checked` property, as in the following example:

```
<element kind="ellipse"  positionType="Float" mode="Opaque" width="25" height="25">
  <pen lineWidth="1.0"/>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="Check"/>
  <property name="net.sf.jasperreports.export.pdf.field.check.type" value="Square"/>
  <property name="net.sf.jasperreports.export.pdf.field.border.style" value="Beveled"/>
  <property name="net.sf.jasperreports.export.pdf.field.read.only" value="true"/>
  <property name="net.sf.jasperreports.export.pdf.field.checked" value="true"/>
</element>
```

### Radio Groups

Radio groups are radio option input fields grouped together so that only a single option can be selected at a time. To group together several radio options we can use the `net.sf.jasperreports.export.pdf.field.name property`. By assigning the same name for multiple JasperReports elements, they will be automatically grouped as radio options. To decorate these radio elements we can use the same `net.sf.jasperreports.export.pdf.field.check.type` property with the same possible values as for checkboxes. Also, to indicate which option is selected when opening the document, we can use the `net.sf.jasperreports.export.pdf.field.checked` property.

Example:

```
<element kind="textField" positionType="Float" mode="Opaque" x="250" width="25" height="25">
  <expression><![CDATA["This text does not matter in PDF export."]] ></expression>
  <property name="net.sf.jasperreports.export.pdf.field.name" value="MyRadioGroup"/>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="Radio"/>
  <property name="net.sf.jasperreports.export.pdf.field.check.type" value="Check"/>
  <box>
    <pen lineWidth="1.0"/>
  </box>
</element>
...
<element kind="textField" positionType="Float" mode="Opaque" x="250" width="25" height="25">
  <expression><![CDATA["This text does not matter in PDF export."]] ></expression>
  <property name="net.sf.jasperreports.export.pdf.field.name" value="MyRadioGroup"/>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="Radio"/>
  <property name="net.sf.jasperreports.export.pdf.field.check.type" value="Circle"/>
  <property name="net.sf.jasperreports.export.pdf.field.checked" value="true"/>
  <box>
    <pen lineWidth="1.0"/>
  </box>
</element>
...
<element kind="textField" positionType="Float" mode="Opaque" x="250" width="25" height="25">
  <expression><![CDATA["This text does not matter in PDF export."]] ></expression>
  <property name="net.sf.jasperreports.export.pdf.field.name" value="MyRadioGroup"/>
  <property name="net.sf.jasperreports.export.pdf.field.type" value="Radio"/>
  <property name="net.sf.jasperreports.export.pdf.field.check.type" value="Cross"/>
  <box>
    <pen lineWidth="1.0"/>
  </box>
</element>
...
```

**Known Limitations and Issues**

- Radio groups cannot overflow on the next page.
- Radio group options cannot be made read-only.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/forms` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/forms/target/reports` directory.
