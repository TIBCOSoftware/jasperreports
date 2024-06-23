
# JasperReports - Virtualizer Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how very large reports could be generated using a report virtualizer to optimize memory consumption.

## Main Features in This Sample

[Generating Very Large Documents Using Report Virtualizers](#virtualizer)

## <a name='virtualizer'>Generating</a> Very Large Documents Using Report Virtualizers
<div align="right">Documented by <a href='mailto:lshannon@users.sourceforge.net'>Luke Shannon</a></div>

**Description / Goal**\
How to generate very large documents using report virtualizers that optimize memory consumption.

**Since:** 1.0.0

### What is Virtualization?

A JasperReport goes through 3 stages in its life cycle:

- Compilation
- Filling
- Exporting

In each stage objects are generated in memory. During the filling stages especially many objects
can be generated as data is processed through the report logic populating multiple pages
and the elements within.\
In a situation such as this, there is always the risk of an Out of Memory error before the Filling of the report has concluded.\
Virtualization is a feature that allows for some of the objects that would be stored in memory during the filling stage to be stored on the file system instead.\
The virtualizer is a simple interface [JRVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/JRVirtualizer.html).

There are currently three implementations of this interface:

- [JRFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRFileVirtualizer.html)
- [JRSwapFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRSwapFileVirtualizer.html)
- [JRGzipVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRGzipVirtualizer.html)

Each of this will be discussed in greater detail in the proceeding sections of this document.

### Configuring Virtualization

Virtualization is not configured in the JRXML. The details around the implementation of the virtualizer can be seen in the sample Java application that is used to run the report: `VirtualizerApp.java.`

In the `fillReport()` method we can see where the virtualizer is instantiated:

```
// creating the virtualizer
JRFileVirtualizer virtualizer = new JRFileVirtualizer(2, "tmp");
```

The [JRFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRFileVirtualizer.html) is created with a `maxSize` of 2 and `"tmp"` as the name of the directory to store data.\
This means once 2 pages of the filled report have been created in memory, the virtualizer will begin to store the data required during the filling in the "tmp" directory.

A single instance of this object can be shared over multiple reports. However, this does mean that the max page number will be respected for all reports that are generated simultaneously.

In the `fillReport(JRFileVirtualizer virtualizer)` method we can see how the virtualizer is configured for use during the filling process:

```
//Preparing parameters
Map parameters = new HashMap();
parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
```

The virtualizer must be configured as a parameter passed in during filling.


### Types of Virtualizers

**[JRFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRFileVirtualizer.html)**

As described above the [JRFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRFileVirtualizer.html) works with temporary files on disk. It has a built in mechanism for cleaning up these temp files after they are no longer needed. However when the clean up of these files occurs may vary (it depends on when the virtualizer reference is garbaged collected by the JVM).\
To control when virtualization occurs there is a `cleanup()` method available that can be called to remove the
files immediatly.

**[JRSwapFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRSwapFileVirtualizer.html)**

Similar to the [JRFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRFileVirtualizer.html) except that rather than creating a file per virtualized page, as the [JRFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRFileVirtualizer.html) does, it shares a single swap file among all report filling processes configured to use the virtualizer.\
A [JRSwapFile](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/JRSwapFile.html) has to be instantiated and passed into the [JRSwapFileVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRSwapFileVirtualizer.html).\
When creating the [JRSwapFile](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/JRSwapFile.html) the targer directory, size of the file and rate at which the file can grow should its current size become insufficient, are all specified.

**[JRGzipVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRGzipVirtualizer.html)**

Using this virtualizer the results in the pages created in memory during filling to be compressed
using the GZIP algorithm. Thus greatly reducing the amount of memory required.

### When to use Virtualization

Virtualization will result in slowed performance for larger reports (the [JRGzipVirtualizer](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/fill/JRGzipVirtualizer.html) doesn't write to the to the file system so its effect on report filling time will be less than the other virtualizers).\
However, using virtualization might mean the difference between waiting a longer time to get a report or getting a out of memory error and no report.\
Setting the `maxPages` in memory is key. Setting the value too low will result in virtualization occuring when it is not necessary. Setting it too high could result in an out of memory exception occuring before virtualization has a chance to start. Picking the correct value for a given system will require some trial and error.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/virtualizer ` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/virtualizer/target/reports` directory.
