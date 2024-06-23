
# JasperReports - PDF Encrypt Sample <img src="../../resources/jasperreports.svg" alt="JasperReports logo" align="right"/>

Shows how reports could be encrypted when exported to PDF.

### Main Features in This Sample

[Encrypted PDF](#pdfencrypt)

## <a name='pdfencrypt'>Encrypted</a> PDF
<div align="right">Documented by <a href='mailto:shertage@users.sourceforge.net'>Sanda Zaharia</a></div>

**Description / Goal**\
How to produce encrypted PDF reports.

**Since:** 0.5.1

### PDF Encryption

There are situations when documents are supposed to be accessed by authorized users only.
Using the file encryption represents a method of protecting a document from unauthorized access.
 
There are two categories of document users: the document owner (which provides full document access granted by default) and regular document users (with limited access permissions. User permissions and document restrictions can be set by the document owner only.

To distinguish between these user categories, the document is allowed to store within up to two passwords: an owner password and a user password. The application decides whether to encrypt a document based on the presence of any of these passwords or access restrictions. If they are set, the document will be encrypted, making all its content unreadable to unauthorized users.

User permissions and all other information required to validate the passwords will be stored in the document's encryption dictionary.

When a user attempts to open an encrypted document protected with a password, a dialog will open prompting for the appropriate password. If the password matches, then all user permissions are activated and the document can be open, decrypted, and displayed in a readable form on the screen.

If no password is set, when open the document no password is requested and full access is granted by default.

Opening the document with the owner password enables the full access to the document, including the possibility to change passwords and access permissions.\
Opening the document with the user password enables some additional operations to be performed, with respect to the user access permissions stored in the encryption dictionary.

### Encrypted PDF in JasperReports

When exporting to the PDF format the engine has to know five additional things about the generated document and uses five dedicated exporter parameters for this:

- Is the document encrypted? The export configuration setting [`isEncrypted()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#isEncrypted()) answers to this question. If set to true, it instructs the exporter to encrypt the resulting PDF document. By default PDF files are not encrypted.\
One can use the related [PROPERTY_ENCRYPTED](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#PROPERTY_ENCRYPTED) flag property for the same purpose.
- Which key to use for the encrypted document? By default the PDF exporter uses a 40-bit key for encryption, but if needed, it can use a 128-bit one. Setting the [`is128BitKey()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#is128BitKey()) configuration setting or the equivalent [PROPERTY_128_BIT_KEY](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#PROPERTY_128_BIT_KEY) flag property to true will instruct the engine to use a 128-bit key.
- The user password. This can be set using the [`getUserPassword()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#getUserPassword()) export configuration setting or the related [PROPERTY_USER_PASSWORD](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#PROPERTY_USER_PASSWORD) property.
- The owner password. This can be set using the export configuration setting [`getOwnerPassword()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#getOwnerPassword()) or the related [PROPERTY_OWNER_PASSWORD](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#PROPERTY_OWNER_PASSWORD) property.
- User permissions. They can be set using the [`getPermissions()`](https://jasperreports.sourceforge.net/api/net/sf/jasperreports/pdf/PdfExporterConfiguration.html#getPermissions()) export configuration setting. This exporter parameter accepts `java.lang.Integer` values representing the PDF permissions for the generated document. The open permissions for the document can be 
    - `AllowPrinting`
    - `AllowModifyContents`
    - `AllowCopy`
    - `AllowModifyAnnotations`
    - `AllowFillIn`
    - `AllowScreenReaders`
    - `llowAssembly`
    - `AllowDegradedPrinting` 

(these are all in the [PdfWriter](https://javadoc.io/doc/com.github.librepdf/openpdf/latest/com.github.librepdf.openpdf/com/lowagie/text/pdf/PdfWriter.html) class of the [OpenPDF](https://openpdf.com/) library). 

Permissions can be combined by applying bitwise OR to them.

### Encrypted PDF Example

This sample makes use of the above export parameters in order to generate an encrypted document. Taking a look at the `pdf()` method in the `src/PdfEncryptApp.java` file, one can see how to set them all:

```
  SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
  configuration.setEncrypted(true);
  configuration.set128BitKey(true);
  configuration.setUserPassword("jasper");
  configuration.setOwnerPassword("reports");
  configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
  exporter.setConfiguration(configuration);
```

### Running the Sample

Running the sample requires the Apache Maven library. Make sure that maven is already installed on your system (version 3.6 or later).
In a command prompt/terminal window set the current folder to `demo/samples/pdfencrypt` within the JasperReports source project and run the following command:

```
> mvn clean compile exec:exec@all
```

It will generate all supported document types containing the sample report in the `demo/samples/pdfencrypt/target/reports` directory.
