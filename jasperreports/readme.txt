
JasperReports Library
=============================


1. ANT Build Tool
-----------------------------

In order to compile the project or to run the sample applications 
provided with this distribution, you need to have the ANT build tool 
installed on your system. 
You can get a copy of this tool and details about how to use it 
at this address: http://ant.apache.org/

There are several ANT specific build.xml files in this project that 
help perform different tasks. Each task has a description explaining
what it does and the list of all available tasks inside a build.xml 
file can be obtained by going to the parent directory of that particular
build.xml file and launching "ant -p" from the command line.



2. Compile the source files
-----------------------------

In the project's root directory there is a build.xml file that 
exposes different targets and helps compiling the Java source files, 
the documentation or build the JAR files.
Make sure you have the ANT build tool correctly installed on your machine
and then launch "ant -p" from the command line in the JasperReports root directory 
to see what tasks are available for building up the library from the source files.



3. Run the samples
-----------------------------

The /demo directory contains some JasperReports sample applications 
and a HSQLDB demo database.


3.1 HSQLDB
Some of the supplied samples use the HSQL Database Engine Server 
found in the /demo/hsqldb directory. In order to run those samples 
you should start the HSQLDB server first. 
There is a build.xml file in the /demo/hsqldb directory which contains 
two ANT targets: "runServer" and "runManager".
The first is for starting up the HSQL database and the second is for
lanching the HSQL client application in case you need to view the
database structure or run some queries against it.

3.2 Fonts
Some of the supplied samples use the TrueType font files found in the 
/demo/fonts directory. In order to run those samples you should install 
these fonts into your operating system so that they are available to the JVM. 
The procedure required to install fonts depends on the operating system. 
On Windows systems this is equivalent to copying the font files into 
the <WINDOWS>\Fonts directory.

3.3 Build and run samples
Inside each sample directory there is a build.xml file that 
helps compiling the java source files and also run the application. 
More information about each ANT task available for each sample can be obtained
by running "ant -p" from the command prompt inside the sample directory.



4. Support and training
-----------------------------

Jaspersoft Corporation now offers support, services and training 
for JasperReports and you can learn more about all these here:
http://www.jaspersoft.com/ss_overview.html
