
# JasperReports - Functions Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how the functions could be used inside report template expressions.

## Main Features in This Sample

[Custom Functions in Report Expressions](#functions)

## <a name='functions'>Custom</a> Functions in Report Expressions
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to use JasperReports built-in functions inside expressions

**Since:** 5.1.2

### Expressions in JasperReports

For most common situations JasperReports expressions represent the perfect tool for acquiring and processing dynamic data. But in some particular cases expressions may also prove their own limitations, especially when series of complex calculations are required to compute a result. In order to avoid writing too long, often unreadable expressions, such calculations can be stored in Java/Groovy methods or Javascript functions. Further, calling a Java method from within an expression requires some specific background in Java programming, regarding imports, object initialization and so on. Similar programming background is expected for Groovy or Javascript expressions.

Custom functions, based on Java annotations and JasperReports extensions mechanism, represent a way to both enhance and simplify expressions. Calling custom functions does not require particular programming skills, all we need is to know the function name and its parameters. All other programming stuff (package imports, Java objects, complex algorithms, etc) is processed transparently to produce the expected output. Expressions also become very simple with custom functions: all their content can be replaced by a function call. Functions perform all necessary calculations and return the result you are looking for.
Now let's see how to create a custom function and integrate it into the JasperReports library.

### Creating Custom Functions in JasperReports

As based on the JasperReports extensions mechanism, custom functions need an extension point to be enabled first. To do so, in a j`asperreports_extension.properties` file add the following properties:

- `net.sf.jasperreports.extension.registry.factory.{registry_id}` - the extension registry factory class for custom functions
- `net.sf.jasperreports.extension.{registry_id}.{property_suffix}` - one or more properties providing a comma separated list of function classes

See a working example in [`jasperreports_extension.properties`](#extensions) section below.

Each function class declared in such a property has to be implemented first, in order to become operational. Basically they are common Java classes containing method implementations. Depending on their purpose, methods are allowed to be static and/or non-static. In order to make available non-static methods, a function class must implement the [FunctionSupport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/FunctionSupport.html) interface, possibly by extending the built-in [AbstractFunctionSupport](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/AbstractFunctionSupport.html).

Notice that since void methods are useless in an expression, properly implemented function classes should expose methods that provide return types.

### Functions Annotations

Custom function classes are characterized by specific class and method-level annotations. Annotations metadata collected at runtime are used to facilitate the function call mechanism, even in complex i18n environments.

- Class-level annotations:
    - [`@FunctionCategories`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategories.html) - stores metadata for an array of [`FunctionCategory`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategory.html) classes, used as category identifiers when functions are grouped by categories. A given function may belong to different categories. The annotation can be declared as class or method-level. As class-level declaration, it applies to all methods in the class.
    - [`@FunctionCategory`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategory.html) - stores metadata for any element in the [`@FunctionCategories`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategories.html) array
    - [`@FunctionMessagesBundle`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionMessagesBundle.html) - stores metadata for the message bundle file, including package information. If not provided, the default message bundle is considered a `jasperreports_messages.properties` file in the current package.
- Method-level annotations:
    - [`@FunctionCategories`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategories.html) - stores metadata for an array of [`FunctionCategory`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategory.html) classes at method level
    - [`@FunctionCategory`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategory.html) - stores metadata for any element in the above[`@FunctionCategories`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionCategories.html) array
    - [`@FunctionParameters`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionParameters.html) - stores metadata for an array of [`FunctionParameter`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionParameter.html) elements
    - [`@FunctionParameter`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionParameter.html) - stores metadata for any element in the above [`@FunctionParameters`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/functions/annotations/FunctionParameters.html) array.

### Built-in Functions in JasperReports Library

Built-in JasperReports functions are documented [here](../functions/FunctionsReport.html).

Next, let's see a step-by-step example of adding custom functions to JasperReports library. The built-in JR functions were created this way and are all available in the `net.sf.jasperreports.functions.standard` package (under the `/src directory` in the present sample). Following are the necessary steps to make functions ready-to-use:

1. Depending on your specific criteria, create one or many category classes. Or you can use the existing built-in classes in the `net.sf.jasperreports.functions.standard` package: `DateTimeCategory`, `LogicalCategory`, `MathCategory`, `TextCategory`.
2. Implement one or many function classes with annotated methods. In this example, a function class is implemented for each category, but keep in mind that methods in different categories may be placed together in the same file, since we have the possibility to declare categories at method-level. There are 4 function classes in the `net.sf.jasperreports.functions.standard` package: `DateTimeFunctions`, L`ogicalFunctions`, `MathFunctions`, `TextFunctions`
3. Create a properties file to store i18n labels for categories, functions and parameters: names, descriptions, other useful informations. Each category, function or parameter require a name and a description entry in the properties file. Entry keys are formed according to following rules:

    - Category name key: the category class name (including package) + .name suffix. For instance: `net.sf.jasperreports.functions.standard.DateTimeCategory.name`
    - Category description key: the category class name (including package) + .description suffix. For instance: `net.sf.jasperreports.functions.standard.DateTimeCategory.description`
    - Function name key: the function class name (including package) + the function name (as declared in the `@Function` method annotation) + .name suffix. For instance: `net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.name`
    - Function description key: the function class name (including package) + the function name (as declared in the `@Function` method annotation) + .description suffix. For instance: `net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.description`
    - Parameter name key: the function class name (including package) + the function name (as declared in the `@Function` method annotation) + the related parameter name (as declared in the related `@FunctionParameter` method annotation) + .name suffix. For instance: `net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.dayOfMonth.name`
    - Parameter description key: the function class name (including package) + the function name (as declared in the `@Function` method annotation) + the related parameter name (as declared in the related `@FunctionParameter` method annotation) + .description suffix. For instance: `net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.dayOfMonth.description`
4. Write the appropriate information in a `jasperreports_extension.properties` file as shown above.
5. Finally, archive your classes and properties files in a jar file and place it in your application classpath. Your custom functions are now ready to use.

### Coding Samples

- `DateTimeCategory.java`:

```
  package net.sf.jasperreports.functions.standard;

  import net.sf.jasperreports.functions.annotations.FunctionCategory;

  /**
   * This class should maintain all function methods that belongs to the category {@link #DATE_TIME}.
   */
  @FunctionCategory(
    //"DATE_TIME" // if not specified, the value is the name of the category class
    )
  public final class DateTimeCategory
  {
  }
```

Notice the `@FunctionCategory` annotation to ensure it is a function category class.

- `DateTimeFunctions.java`

Below is a code fragment extracted from the function class `DateTimeFunctions`:

```
package net.sf.jasperreports.functions.standard;

// import declarations here
// ...

/**
 * This class should maintain all function methods that belongs to the category {@link #DATE_TIME}.
 */
@FunctionCategories({DateTimeCategory.class})
public final class DateTimeFunctions
{
  private static final Log log = LogFactory.getLog(DateTimeFunctions.class);

  // ===================== TODAY function ===================== //
  /**
   * Returns the current date as date object.
   */
  @Function("TODAY")
  public static Date TODAY(){
    return new Date();
  }

  // other methods declared here
  // ...

  // ===================== DAY function ===================== //
  /**
   *
   * Returns the day of a given date. Date object can be a String, long value (milliseconds) or Date instance itself.
   */
  @Function("DAY")
  @FunctionParameters({
    @FunctionParameter("dateObject")})
  public static Integer DAY(Object dateObject){
    return getCalendarFieldFromDate(dateObject,Calendar.DAY_OF_MONTH);
  }

  // ===================== WEEKDAY function ===================== //
  /**
   * Returns the day of the week for a given date. Date object can be a String, long value (milliseconds) or Date instance itself.
   */
  @Function("WEEKDAY")
  @FunctionParameters({
    @FunctionParameter("dateObject"),
    @FunctionParameter("isSundayFirstDay")})
  public static Integer WEEKDAY(Object dateObject){
    return WEEKDAY(dateObject, false);
  }

  public static Integer WEEKDAY(Object dateObject, Boolean isSundayFirstDay){
    Integer dayOfWeek = getCalendarFieldFromDate(dateObject,Calendar.DAY_OF_WEEK);
    if(dayOfWeek==null) {
      if(log.isDebugEnabled()){
        log.debug("Unable to get the correct day of the week.");
      }
      return null;
    }
    if(isSundayFirstDay){
      // By default Sunday is considered first day in Java
      // Calendar.SUNDAY should be a constant with value 1.
      // See the Calendar.DAY_OF_WEEK javadoc    
      return dayOfWeek;
    }
    else{
      // shift the days
      if(dayOfWeek==Calendar.SUNDAY){
        return 7;
      }
      else{
        return dayOfWeek-1;
      }
    }
  }

  // other public methods declared here
  // ...

  // private methods declared here
  // ...
}
```

Notice the `@FunctionCategories({DateTimeCategory.class})` annotation at class level, ensuring that all methods (functions) in this class are Date/Time functions.

Public methods (excepting the overloaded ones) expose a `@Function` annotation necessary as function identifier. For overloaded methods the `@Function` and other annotations are declared only once, since these methods are sharing the same function name but provide different number of parameters. An example is given by the `WEEKDAY` overloaded method.

Also notice how parameters are annotated where they are present: the `@FunctionParameters` annotation contains an array of `@FunctionParameter` that stores the parameter identifier.

### I18n and Localization

2 related code fragments are shown below. The first one comes from the default `jasperreports_messages.properties file`:

```
net.sf.jasperreports.functions.standard.DateTimeCategory.description                           = Category for date and time manipulation functions
net.sf.jasperreports.functions.standard.DateTimeCategory.name                                  = Date & Time
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.dayOfMonth.description          = The day of the new date
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.dayOfMonth.name                 = Day of month
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.description                     = Creates a date object using the specified information on day, month and year.
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.month.description               = The month of the new date
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.month.name                      = Month
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.name                            = DATE
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.year.description                = The year of the new date
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.year.name                       = Year
```

The other one is the Italian translation of this fragment in the `jasperreports_messages_it.properties` file:

```
net.sf.jasperreports.functions.standard.DateTimeCategory.description                           = Categoria di funzioni per la manipolazione di date e tempo
net.sf.jasperreports.functions.standard.DateTimeCategory.name                                  = Data e tempo
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.dayOfMonth.description          = Il giorno della data
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.dayOfMonth.name                 = Giorno
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.description                     = Crea una nuova data utilizzando le informazioni di giorno, mese e anno specificati.
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.month.description               = Il mese della data
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.month.name                      = Mese
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.name                            = DATE
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.year.description                = L''anno della data
net.sf.jasperreports.functions.standard.DateTimeFunctions.DATE.year.name                       = Anno
```

Running the sample by default, all output texts will result in English. Changing the `REPORT_LOCALE` to `Italian` will translate the output texts into Italian.

### JasperReports_extension.properties

Following is the content of the related `jasperreports_extension.properties` file:

```
net.sf.jasperreports.extension.registry.factory.functions=net.sf.jasperreports.functions.FunctionsRegistryFactory
net.sf.jasperreports.extension.functions.datetime=net.sf.jasperreports.functions.standard.DateTimeFunctions
net.sf.jasperreports.extension.functions.math=net.sf.jasperreports.functions.standard.MathFunctions, net.sf.jasperreports.functions.standard.LogicalFunctions
net.sf.jasperreports.extension.functions.text=net.sf.jasperreports.functions.standard.TextFunctions
```

Here the `{registry_id}` is assumed to be `functions`. There are 3 different function classes properties with specific `{property_suffix}`-es. Math and logical functions are declared in a comma separated list.

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/functions` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/functions/target/reports` directory.
