# JasperReports - Ant Update Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how multiple JRXML files can be updated in batch mode using the ANT build tool.

### Main Features in This Sample

[Updating Multiple Report Template Files Using the Ant Build Tool](#antupdate)

### Secondary Features

- [Compiling Multiple Report Template Files Using the Ant Build Tool](../antcompile/README.md#antcompile)
- [Generating the JRXML Source Files for Multiple Compiled Report Template Files Using the Ant Build Tool (Decompiling)](../antcompile/README.md#antdecompile)

## <a name='antupdate'>Updating</a> Multiple Report Template Files Using the Ant Build Tool
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**

How to update all your JRXML report source files using the Ant build tool, in order to perform the same modification on them or to bring them up-to-date with respect to the JasperReports Schema Reference.

**Since:** 3.7.1


### The Ant Update Task

A problem that may occur in applications with several report templates stored in the same resource repository is the amount of time required to modify each report template individually when the same change has to be applied for all reports. Manual modification is not the best practice in this case, the entire process should be done automatically.
Based on the fact that JRXML templates can be read into [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) objects that can be easily modified and then saved again as JRXML templates, we only need to know where the initial JRXML templates are located, and which changes are to be applied, in order to process a bulk update on all these templates.
A very good tool for operations such as scanning source folders for JRXML templates, triggering methods in java objects and performing batch modifications is the Apache Ant build tool. To accomplish our task described above, we can create an Ant task able to execute all the necessary steps. JasperReports provides the built-in task [JRAntUpdateTask](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/ant/JRAntUpdateTask.html) for batch-updating JRXML report designs, that:

- needs to know the location of the source report templates. This location should be passed as a valid `org.apache.tools.ant.types.Path` object and is mandatory.
- needs to know where to save the updated report templates. This output directory should be passed as a valid `java.io.File` object. If not set, the base directory set in the `build.xml` file will be considered instead.

Once the source location and destination folder are specified, the next step is to identify the individual report templates that need to be updated. The source folders are scanned for files with the appropriate `.jrxml` extension, ready to be updated. The update decision is based on timestamp: only JRXML files that either have no corresponding file in the destination directory, or their corresponding report design destination file is older than the source file, will be updated. They will be also updated if a given [ReportUpdater](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/ReportUpdater.html) is added to the task.

After report templates are identified and loaded into [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) objects, the update operations are applied on each of them, then the modified report designs will be saved in the destination directory.

### Report Updaters

Changes can be very easily applied on report designs using specific [ReportUpdater](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/ReportUpdater.html) implementations. Classes that inherit from the [ReportUpdater](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/ReportUpdater.html) interface should implement the following method:

```
public JasperDesign update(JasperDesign jasperDesign)
```

in order to perform specific modifications on the [JasperDesign](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/design/JasperDesign.html) object.

The [JRAntUpdateTask](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/ant/JRAntUpdateTask.html) accepts any number of report updaters to be added to its updaters list. Update operations will be applied sequential in the same order as the report updaters were added to the list. A small example of report updater implementations can be found in the` /src/com/update` directory of our sample (see the `StyleUpdater.java` and `RenewUuidsUpdater.java` files) sample. This report updater changes the font size, color and weight of the default report style.

### The Ant Update Sample

First, let's take a look at how the `demo/samples/antupdate` sample is structured, to see if this structure is conserved after reports are updated.
The reports directory is the root for the following tree:

```
                               reports
                                  |
                                 com
                               /  |  \
                            bar   |   foo
                             |    |    |
                BarReport.jrxml   |   FooReport.jrxml
                                  |
                             ignoreme.txt
```

One can notice the presence of the `ignoreme.txt` file in the `/reports/com` folder. This is not a report template, hence it will be not considered for update.
Both `BarReport.jrxml` and `FooReport.jrxml` sources contain a title section with a `printWhenExpression`:

- `<printWhenExpression><![CDATA[com.bar.SomeBarClass.isToPrint()]]></printWhenExpression>` - in `BarReport.jrxml`
- `<printWhenExpression><![CDATA[com.foo.SomeFooClass.isToPrint()]]></printWhenExpression>` - in `FooReport.jrxml`

The `com.bar.SomeBarClass` and `com.foo.SomeFooClass` sources are located in the `/src` directory as follows:

```
                                 src
                                  |
                                 com
                               /  |  \
                            bar   |   foo
                             |    |    |
              SomeBarClass.java   |   SomeFooClass.java
                                  |
                                update
                                  |
                           StyleUpdater.java
					 RenewUuidsUpdater.java
```

and the related classes will be saved into the `build/classes` directory and added to the classpath.

Now let's update these report templates. There are some specific Ant targets declared in the `build.xml` file:

```
<target name="update1" description="Updates report designs specified using the &quot;srcdir&quot; in the &lt;jru&gt; tag." depends="define-jru"> 
  <mkdir dir="./build/reports"/> 
  <jru 
      srcdir="./reports"
      destdir="./build/reports">
    <classpath refid="sample-classpath"/>
    <include name="**/*.jrxml"/>
    <!--
    <updater>com.update.RenewUuidsUpdater</updater>
    <updater>com.update.StyleUpdater</updater>
    -->
  </jru>
</target> 

<target name="update2" description="Updates report designs specified using a &lt;fileset&gt; in the &lt;src&gt; tag." depends="define-jru">
  <mkdir dir="./build/reports"/> 
  <jru destdir="./build/reports">
    <src>
      <fileset dir="./reports">
        <include name="**/*.jrxml"/>
      </fileset>
    </src>
    <classpath refid="sample-classpath"/>
    <!--
    <updater>com.update.RenewUuidsUpdater</updater>
    <updater>com.update.StyleUpdater</updater>
    -->
  </jru> 
</target> 

...

<target name="compileUpdated" depends="javac, define-jrc" description="Compiles the updated report designs."> 
  <mkdir dir="./build/reports"/> 
  <delete dir="./build">
    <include name="**/*.jasper"/>
  </delete>
  <jrc 
      srcdir="./build/reports"
      destdir="./build/reports">
    <classpath refid="sample-classpath"/>
    <include name="**/*.jrxml"/>
  </jrc>
</target>
```

When `update1` target is called, the root directory for the JRXML sources, the destination folder and the source file type (`*.jrxml`) will be passed as parameters to the updater Ant task. The root source directory will be recursively scanned and any JRXML file in the path will be localized. Other file types (see the `ignoreme.txt`) will be ignored.
By default, the Ant updater task updates sources based on timestamp differences between source and destination files, or on schema changes. Updated JRXML files will reflect all these changes and syntax updates.
Also notice here the commented block:

```
<!--
<updater>com.update.RenewUuidsUpdater</updater>
<updater>com.update.StyleUpdater</updater>
-->
``` 

that calls the `RenewUuidsUpdater` and `StyleUpdater` after source files are scanned. When the `<updater>` tag is present, default updates will be executed anyway, but the related report updater is also called to perform its specific update operations. In order to see how they act, just uncomment this block and run the sample again. If enabled, the `StyleUpdater` will modify the report Arial_Normal style attributes for font color (`blue: #0000FF`), font size (`14px`) and font weight (`bold`). The UUIDs for elements in the updated reports will be updated too.

When `update2` target is called, sources are passed to the Ant task as file set in the `<src>` tag. Once again we can see that only `*.jrxml` files are allowed. This target also provides a commented `<updater>` tag. Default updates will be performed in this case. If the `<updater>` tag is uncommented, StyleUpdater will also execute its update operations.
After the batch update the destination folder tree will look like this:

```
                                build
                                  |
                               reports
                                  |
                                 com
                               /     \
                            bar       foo
                             |         |
                BarReport.jrxml       FooReport.jrxml
```

Both `BarReport.jrxml`and `FooReport.jrxml` are present, but the `ignoreme.txt` is obviously missing.

If the updated reports have to be also compiled, the `<compileUpdated>` specific target can be executed to accomplish this need. It will compile all JRXML files found in the destination folder tree. Compiled reports also conserve the tree structure and will be saved in the `build/classes` directory.

### Running the Sample

Running the sample requires the [Apache Ant](https://ant.apache.org) library. Make sure that ant is already installed on your system (version 1.5 or later).\
In a command prompt/terminal window set the current folder to `demo/samples/antupdate` within the JasperReports source project and run the `> ant test` command.

It will generate updated reports in the `demo/samples/antupdate/build/reports` directory. 

Compare the updated JRXMLs with the original ones. Notice that the title band comes with the `splitType` attribute and report elements provide the `uuid` atribute. The deprecated `border` attribute was replaced by the `pen` element. All these represent changes applied by default in order to get up-to-date report designs, in accordance with the current JasperReports model.
Then uncomment the block:

```
<!--
<updater>com.update.RenewUuidsUpdater</updater>
<updater>com.update.StyleUpdater</updater>
-->
```

in the update1 target and run again the `> ant test` command. The build directory will be cleaned up, then the `update1` target will be executed. The updated JRXMLs will be saved in the destination folder. Then the `update2` target will be executed, but because there are no newer default updates to be performed, this target will do nothing. It won't override changes performed by the `update1` target. When opening the updated JRXMLs we'll see that style modifications introduced by the `update1` task are still present after the consequent execution of `update2`.

To have updated AND compiled reports in the destination folder, just run the run the `> ant test compileUpdated` command. The compiled `*.jasper` reports will be available in the `demo/samples/antupdate/build/classes` directory.
