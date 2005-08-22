package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import java.io.StringWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;

import org.xml.sax.SAXException;

/**
 * Exports filled reports in plain text format. The text exporter allows users to define a custom character resolution
 * (the number of columns and rows in text format). Since the character resolution is mapped on the actual pixel resolution,
 * every character corresponds to a rectangle of pixels. If a certain text element has a smaller size in pixels (width,
 * height, or both) than the number of pixels that map to a character, the text element will not be rendered. Because of
 * this, users must take some precautions when creating reports for text export. First, they must make sure the page size in
 * characters is large enough to render the report, because if the report pages contain too much text, some of it may be
 * rendered only partially. On the other hand, if the character resolution is too small compared to the pixel resolution
 * (say, a height of 20 characters for a page 800 pixels tall) all texts with sizes smaller than the one needed to map to a
 * character, will not be displayed (in the previous examplle, a text element needs to be at least 800/20 = 40 pixels tall
 * in order to be rendered).
 * <p>
 * As a conclusion, the text exporter will yield the better results if the space needed for displaying a text is large. So
 * users have to either design reports with few text or export to big text pages. Another good practice is to arrange text
 * elements at design time as similar as possible to a grid.
 *
 * @see JRExporterParameter
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRTextExporter extends JRAbstractExporter
{
	protected int pageWidth;
	protected int pageHeight;
	protected int characterHeight;
	protected int characterWidth;
	protected JRExportProgressMonitor progressMonitor;
	protected Writer writer;
	char[][] pageData;
	protected String betweenPagesText;

	/**
	 *
	 */
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);

		/*   */
		setOffset();

		/*   */
		setInput();

		/*   */
		if (!isModeBatch)
		{
			setPageRange();
		}

		String encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}

		Integer characterWidthParam = (Integer) parameters.get(JRTextExporterParameter.CHARACTER_WIDTH);
		if (characterWidthParam != null) {
            characterWidth = characterWidthParam.intValue();
			if (characterWidth < 0)
				throw new JRException("Character width must be greater than 0");
		}
		else {
			Integer pageWidthParam = (Integer) parameters.get(JRTextExporterParameter.PAGE_WIDTH);
			if (pageWidthParam != null) {
				pageWidth = pageWidthParam.intValue();
				if (pageWidth <= 0)
					throw new JRException("Page width must be greater than 0");
			} else {
                throw new JRException("Character or page width must be specified");
			}
		}


		Integer characterHeightParam = (Integer) parameters.get(JRTextExporterParameter.CHARACTER_HEIGHT);
		if (characterHeightParam != null) {
            characterHeight = characterHeightParam.intValue();
			if (characterHeight < 0)
				throw new JRException("Character height must be greater than 0");
		}
        else {
			Integer pageHeightParam = (Integer) parameters.get(JRTextExporterParameter.PAGE_HEIGHT);
			if (pageHeightParam != null) {
				pageHeight = pageHeightParam.intValue();
				if (pageHeight <= 0)
					throw new JRException("Page height must be greater than 0");
			}
			else {
				throw new JRException("Character or page height must be specified");
			}
		}


		betweenPagesText = (String) parameters.get(JRTextExporterParameter.BETWEEN_PAGES_TEXT);
		if (betweenPagesText == null) {
			betweenPagesText = "\n\n";
		}

		StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			try
			{
				writer = new StringWriter();
				exportReportToWriter();
				sb.append(writer.toString());
			}
			catch (IOException e)
			{
				throw new JRException("Error writing to StringBuffer writer : " + jasperPrint.getName(), e);
			}
			finally
			{
				if (writer != null)
				{
					try
					{
						writer.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		}
		else
		{
			writer = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
			if (writer != null)
			{
				try
				{
					exportReportToWriter();
				}
				catch (IOException e)
				{
					throw new JRException("Error writing to writer : " + jasperPrint.getName(), e);
				}
			}
			else
			{
				OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
				if (os != null)
				{
					try
					{
						writer = new OutputStreamWriter(os, encoding);
						exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to OutputStream writer : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
					if (destFile == null)
					{
						String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
						if (fileName != null)
						{
							destFile = new File(fileName);
						}
						else
						{
							throw new JRException("No output specified for the exporter.");
						}
					}

					try
					{
						os = new FileOutputStream(destFile);
						writer = new OutputStreamWriter(os, encoding);
						exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to file writer : " + jasperPrint.getName(), e);
					}
					finally
					{
						if (writer != null)
						{
							try
							{
								writer.close();
							}
							catch(IOException e)
							{
							}
						}
					}
				}
			}
		}
	}


	/**
	 *
	 */
	protected void exportReportToWriter() throws JRException, IOException
	{
		for(int reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);

			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				if (characterWidth > 0)
					pageWidth = jasperPrint.getPageWidth() / characterWidth;
				if (characterHeight > 0)
					pageHeight = jasperPrint.getPageHeight() / characterHeight;

				for(int i = startPageIndex; i <= endPageIndex; i++)
				{
					if (Thread.currentThread().isInterrupted())
					{
						throw new JRException("Current thread interrupted.");
					}

					JRPrintPage page = (JRPrintPage)pages.get(i);

					/*   */
					exportPage(page);
				}
			}
		}

		writer.flush();
	}


	/**
	 * Exports a page to the output writer. Only text elements within the page are considered. For each page, the engine
	 * creates a matrix of characters and each rendered text element is placed at the appropiate position in the matrix.
	 * After all texts are parsed, the character matrix is sent to the output writer.
	 */
	protected void exportPage(JRPrintPage page) throws IOException
	{
		List elements = page.getElements();

		pageData = new char[pageHeight][];
		for (int i = 0; i < pageHeight; i++) {
			pageData[i] = new char[pageWidth];
			Arrays.fill(pageData[i], ' ');
		}


		for (int i = 0; i < elements.size();i++) {
			Object element = elements.get(i);
			if (element instanceof JRPrintText)
				exportText((JRPrintText) element);
		}

		for (int i = 0; i < pageHeight; i++) {
			writer.write(pageData[i]);
			writer.write("\n");
		}

		writer.write(betweenPagesText);

		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	/**
	 * Renders a text and places it in the output matrix.
	 */
	protected void exportText(JRPrintText element)
	{
		int rowCount = calculateYCoord(element.getHeight());
		int columnCount = calculateXCoord(element.getWidth());
		int x = calculateXCoord(element.getX() + globalOffsetX);
		int y = calculateYCoord(element.getY() + globalOffsetY);

		String allText;
		JRStyledText styledText = getStyledText(element);
		if (styledText == null)
		{
			allText = "";
		}
		else
		{
			allText = styledText.getText();
		}

		// if the space is too small, the element will not be rendered
		if (rowCount <= 0 || columnCount <= 0)
			return;

		if (allText != null && allText.length() == 0)
			return;

        // uses an array of string buffers, since the maximum number of rows is already calculated
		StringBuffer[] rows = new StringBuffer[rowCount];
		rows[0] = new StringBuffer();
		int rowIndex = 0;
		int rowPosition = 0;

		// first search for \n, because it causes immediate line break
		StringTokenizer lfTokenizer = new StringTokenizer(allText, "\n");
		label:while (lfTokenizer.hasMoreTokens()) {
			String line = lfTokenizer.nextToken();
			StringTokenizer spaceTokenizer = new StringTokenizer(line, " ", true);

            // divide each text line in words
			while (spaceTokenizer.hasMoreTokens()) {
				String word = spaceTokenizer.nextToken();

				// situation: word is larger than the entire column
				// in this case breaking occurs in the middle of the word
				while (word.length() > columnCount) {
					rows[rowIndex].append(word.substring(0, columnCount - rowPosition));
					word = word.substring(columnCount - rowPosition, word.length());
					rowIndex++;
					if(rowIndex == rowCount)
						break label;
					rowPosition = 0;
					rows[rowIndex] = new StringBuffer();
				}

				// situation: word is larger than remaining space on the current line
				// in this case, go to the next line
				if (rowPosition + word.length() > columnCount) {
					rowIndex++;
					if (rowIndex == rowCount)
						break label;
					rowPosition = 0;
					rows[rowIndex] = new StringBuffer();
				}

				// situation: the word is actually a space and it situated at the beginning of a new line
				// in this case, it is removed
				if (rowIndex > 9 && rowPosition == 0 && word.equals(" "))
					break;

				// situation: the word is small enough to fit in the current line
				// in this case just add the word and increment the cursor position
				rows[rowIndex].append(word);
				rowPosition += word.length();
			}


			rowIndex++;
			if(rowIndex == rowCount)
				break;
			rowPosition = 0;
			rows[rowIndex] = new StringBuffer();
		}


		int xOffset = 0;
		int yOffset = 0;

		if (element.getVerticalAlignment() == JRAlignment.VERTICAL_ALIGN_BOTTOM)
			yOffset = rowCount - rowIndex;
		if (element.getVerticalAlignment() == JRAlignment.VERTICAL_ALIGN_MIDDLE)
			yOffset = (rowCount - rowIndex) / 2;

		for (int i = 0; i < rowIndex; i++) {
			String line = rows[i].toString();
			int pos = line.length() - 1;
			while (pos >= 0 && line.charAt(pos) == ' ')
				pos--;
            line = line.substring(0, pos + 1);
			if (element.getHorizontalAlignment() == JRAlignment.HORIZONTAL_ALIGN_RIGHT)
				xOffset = columnCount - line.length();
			if (element.getHorizontalAlignment() == JRAlignment.HORIZONTAL_ALIGN_CENTER)
				xOffset = (columnCount - line.length()) / 2;

			// if text is justified, there is no offset, but the line text is modified
			// the last line in the paragraph is not justified.
			if (element.getHorizontalAlignment() == JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED)
				if (i < rowIndex -1)
					line = justifyText(line, columnCount);

			char[] chars = line.toCharArray();
			System.arraycopy(chars, 0, pageData[y + yOffset + i], x + xOffset, chars.length);
		}
	}


	/**
	 * Justifies the text inside a specified space.
	 */
	private String justifyText(String s, int width)
	{
		StringBuffer justified = new StringBuffer();

		StringTokenizer t = new StringTokenizer(s, " ");
		int tokenCount = t.countTokens();
		if (tokenCount <= 1)
			return s;

		String[] words = new String[tokenCount];
		int i = 0;
		while (t.hasMoreTokens())
			words[i++] = t.nextToken();

		int emptySpace = width - s.length() + (words.length - 1);
		int spaceCount = emptySpace / (words.length - 1);
		int remainingSpace = emptySpace % (words.length - 1);

		char[] spaces = new char[spaceCount];
		Arrays.fill(spaces, ' ');

		for (i = 0; i < words.length - 1; i++)
		{
			justified.append(words[i]);
			justified.append(spaces);
			if (i < remainingSpace)
				justified.append(' ');
		}
        justified.append(words[words.length-1]);

		return justified.toString();
	}


	/**
	 * Transforms y coordinates from pixel space to character space.
	 */
	protected int calculateYCoord(int y)
	{
		return pageHeight * y / jasperPrint.getPageHeight();
	}

	/**
	 * Transforms x coordinates from pixel space to character space.
	 */
	protected int calculateXCoord(int x)
	{
		return pageWidth * x / jasperPrint.getPageWidth();
	}


	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		JRStyledText styledText = null;

		String text = textElement.getText();
		if (text != null)
		{
			if (textElement.isStyledText())
			{
				try
				{
					styledText = styledTextParser.parse(null, text);
				}
				catch (SAXException e)
				{
					//ignore if invalid styled text and treat like normal text
				}
			}

			if (styledText == null)
			{
				styledText = new JRStyledText();
				styledText.append(text);
				styledText.addRun(new JRStyledText.Run(null, 0, text.length()));
			}
		}

		return styledText;
	}

}
