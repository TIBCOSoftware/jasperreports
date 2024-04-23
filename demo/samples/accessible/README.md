
# JasperReports - Accessible Reports Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how to create accessible reports.

### Main Features in This Sample

[Designing Accessible Reports](#accessible)
				

## <a name='accessible'>Designing</a> Accessible Reports
<div align="right">Documented by <a href='mailto:teodord@users.sourceforge.net'>Teodor Danciu</a></div>


**Description / Goal**	\
Explains how to design reports that produce accessible content.

**Since:** 6.19.0

**Other Samples** \
[/demo/samples/crosstabs](../crosstabs/README.md)\
[/demo/samples/markup](../markup/README.md)\
[/demo/samples/styledtext](../styledtext/README.md)\
[/demo/samples/table](../table/README.md)\
[/demo/samples/pdfa](../pdfa/README.md)


JasperReports library accessibility support currently covers HTML and PDF export formats and allows screen readers to correctly identify and read through the following types of structured content:

- headings (h1 to h6)
- bulleted and numbered lists
- images and graphics alternate texts
- tables
- crosstabs

Special markup properties are used in the report templates to identify these types of structured content. Some of these markup properties need to be specified manually, while for others the reporting engine can help set them automatically in the generated output, if certain specialized reporting features or components are used. For example, in order to specify that a certain text element represents a level 2 heading, the person creating the report needs to use the following custom property at text element level\:

``` 
<property name=”net.sf.jasperreports.export.accessibility.tag” value=”h2”/>
```

On the other hand, if the report renders data in the form of a table, it would be enough for the person designing the report to make use of the table component. The table component would automatically add the following custom property to the top level frame that contains the table output in the resulting document\:

```
<property name=”net.sf.jasperreports.export.accessibility.tag” value=”table”/>
```

Not only this, but the table component would also automatically inject similar custom properties to markup the column headers, the rows and the individual cells and thus make up the entire table structure readable by screen readers.

The HTML and PDF exporters have different mechanisms to expose accessible content to the screen reader. While in HTML, the screen reader recognizes standard HTML tags and attributes as headings, bulleted and numbered list and semantic tables, in PDF, special metadata tags need to be inserted into the output to give it more structure.

By default, the JasperReports Library does not produce accessible HTML and PDF exports. This behavior needs to be activated using dedicated exporter configurations as follows\:

```
net.sf.jasperreports.export.html.accessible
net.sf.jasperreports.export.pdf.tagged
```

### Headings (h1 to h6)

Specifying that a certain text element represents a heading of level 1 up to level 6 can be done using the following custom property with a value from „h1” to „h6” respectively.
For example, using the following custom property attached to the text element, a level 2 heading can be marked for the screen reader\:

```
<property name=”net.sf.jasperreports.export.accessibility.tag” value=”h2”/>
```

This property works for both the HTML and the PDF exporters.

### Bulleted and Numbered Lists

Creating accessible bulleted and/or numberer lists in the HTML output can be achieved using styled text content or HTML markup text content in a text element.
The styled text feature as well as the HTML markup feature of text elements in the report templates have support for nested `<ul>` and `<ol>` tags as well as their children `<li>` tags. This type of text markup has its structure preserved when exported to HTML and thus is recognized as such by the screen readers.

Simply by using `<ul>`, `<ol>` and `<li>` tags in a text field configured to render styled text or HTML markup text is enough to create accessible bulleted or numbered lists.

For numbered lists, the `<ol>` tag should be used and this tag supports two attributes:

- `type`: Specifies the kind of marker to use in the list. The possible values for the type attribute are as follows:
    - 1 Default. Decimal numbers (1, 2, 3, 4)
    - a Alphabetically ordered list, lowercase (a, b, c, d)
    - A Alphabetically ordered list, uppercase (A, B, C, D)
    - i Roman numbers, lowercase (i, ii, iii, iv)
    - I Roman numbers, uppercase (I, II, III, IV)
- `start`: Specifies the start value of an ordered list

### Alternate Text for Images and Graphics

Making images or graphics accessible to screen readers means an alternate text needs to be provided for them so that the screen reader reads it out when going over the image or graphic. This alternate text is usually a short description of what the image or graphic represent.

The alternate text for an image or chart element in the report template can be specified using the `<hyperlinkTooltipExpression>`.

### Tables

The easiest way to create an accessible table in the HTML or PDF export of a report is by using the table component in the report template.
The table component has built-in capability to produce its own metadata that would ensures its content keeps its semantic table nature when exported to HTML or that specific tags are inserted into the PDF output to fully describe the tabular structure in the resulting PDF document.

By default, the table component does not generate at report fill time the structural metadata needed later for accessibility during HTML and PDF export. This behavior needs to be activated by using the following custom property either globally, at report level or table component level\:

```
<property name=”net.sf.jasperreports.components.table.accessible” value=”true”/>
```

Not every table is truly accessible if it does not meet certain criteria with respect with simplicity and clearness. If a table has a structure that is too complicated, then users would have a hard time understanding them even if the screen reader can properly read through them.\
Having said that, here are the main rules to follow when designing accessible tables in reports:

- use a single text element in each table cell;
- make the text element as big as the parent table cell;
- make sure all text elements on the same row grow in height in unison by setting stretchType="ContainerHeight" to each text element in each cell;
- make use of the text element border and padding properties and not the border or padding properties of the parent table cell;

Table-like structures can be rendered without using the table component. But in such case, it would be the responsibility of the person designing the report to make use of the appropriate custom properties at element level to markup the overall table container, its column headers, its rows and cells, which otherwise would not be recognizable as a semantic table by the screen reader tool, but seemingly as a flat series of text elements without structure.\
This is not a recommended technique to produce accessible tables, but if needed, an example can be found in the [tabular](../tabular/README.md) sample.

### Crosstabs

Just like in the case of tables, the easiest way to create accessible crosstabs in the HTML and PDF exported reports is to make use of the crosstab component at report design time.
Crosstab components have too the built-in ability to produce their own metadata needed to describe their structure at export time and thus produce semantic tables that are recognizable as such by the screen readers.

Unlike table components, crosstabs always produce their structural metadata at report fill time and there is no configuration property to turn off this behavior. This is mostly because crosstabs are supposed to be limited in size and the extra information attached to the output they produce does not significantly increase the size of the overall report output.

Crosstabs accessibility can also be affected by their design complexity and this is why it is important to follow certain rules when designing accessible crosstabs:

- use a single text element in each crosstab cell;
- make the text element as big as the parent crostab cell;
- make sure all text elements on the same row grow in height in unison by setting stretchType="ContainerHeight" to each text element in each cell;
- make use of the text element border and padding properties and not the border or padding properties of the parent crosstab cell;
