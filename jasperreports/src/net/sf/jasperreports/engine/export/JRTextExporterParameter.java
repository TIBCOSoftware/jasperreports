package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;

/**
 * Contains parameters useful for export in plain text format.
 * <p>
 * The text exporter can send data to a string buffer, output stream, character stream or file on disk. The engine looks
 * among the export parameters in order to find the selected output type in this order: OUTPUT_STRING_BUFFER, OUTPUT_WRITER,
 * OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRTextExporterParameter extends JRExporterParameter
{
	/**
	 *
	 */
	public JRTextExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * An integer representing the pixel/character horizontal ratio.
	 */
	public static final JRTextExporterParameter CHARACTER_WIDTH = new JRTextExporterParameter("Character Width");


	/**
	 * An integer representing the pixel/character horizontal ratio.
	 */
	public static final JRTextExporterParameter CHARACTER_HEIGHT = new JRTextExporterParameter("Character Height");


	/**
	 * An integer representing the page width in characters.
	 */
	public static final JRTextExporterParameter PAGE_WIDTH = new JRTextExporterParameter("Page Width");


	/**
	 * An integer representing the page height in characters.
	 */
	public static final JRTextExporterParameter PAGE_HEIGHT = new JRTextExporterParameter("Page Width");


	/**
	 * A string representing text that will be inserted between pages of the generated report. By default, JasperReports
	 * separates pages by two empty lines, but this behaviour can be overriden by this parameter.
	 */
	public static final JRTextExporterParameter BETWEEN_PAGES_TEXT = new JRTextExporterParameter("Between Pages Text");

}
