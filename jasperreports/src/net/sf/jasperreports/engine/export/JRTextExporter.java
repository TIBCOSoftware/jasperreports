/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

/**
 * Exports filled reports in plain text format. The text exporter allows users to define a custom character resolution
 * (the number of columns and rows in text format). Since the character resolution is mapped on the actual pixel resolution,
 * every character corresponds to a rectangle of pixels. If a certain text element has a smaller size in pixels (width,
 * height, or both) than the number of pixels that map to a character, the text element will not be rendered. Because of
 * this, users must take some precautions when creating reports for text export. First, they must make sure the page size in
 * characters is large enough to render the report, because if the report pages contain too much text, some of it may be
 * rendered only partially. On the other hand, if the character resolution is too small compared to the pixel resolution
 * (say, a height of 20 characters for a page 800 pixels tall) all texts with sizes smaller than the one needed to map to a
 * character, will not be displayed (in the previous example, a text element needs to be at least 800/20 = 40 pixels tall
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
	private static final String TXT_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.txt.";

	protected int pageWidthInChars;
	protected int pageHeightInChars;
	protected float charWidth;
	protected float charHeight;
	protected JRExportProgressMonitor progressMonitor;
	protected Writer writer;
	char[][] pageData;
	protected String betweenPagesText;
	protected String lineSeparator;

	protected static final String systemLineSeparator = System.getProperty("line.separator");

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

		if (!parameters.containsKey(JRExporterParameter.FILTER))
		{
			filter = createFilter(TXT_EXPORTER_PROPERTIES_PREFIX);
		}

		/*   */
		if (!isModeBatch)
		{
			setPageRange();
		}

		String encoding = 
			getStringParameterOrDefault(
				JRExporterParameter.CHARACTER_ENCODING, 
				JRExporterParameter.PROPERTY_CHARACTER_ENCODING
				);

		betweenPagesText = (String) parameters.get(JRTextExporterParameter.BETWEEN_PAGES_TEXT);
		if (betweenPagesText == null) 
		{
			betweenPagesText = systemLineSeparator + systemLineSeparator;
		}

		lineSeparator = (String) parameters.get(JRTextExporterParameter.LINE_SEPARATOR);
		if (lineSeparator == null) 
		{
			lineSeparator = systemLineSeparator;
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
	public void setReportParameters() throws JRException
	{
		charWidth = 
			getFloatParameter(
				JRTextExporterParameter.CHARACTER_WIDTH,
				JRTextExporterParameter.PROPERTY_CHARACTER_WIDTH,
				0
				);
		if (charWidth < 0)
		{
			throw new JRException("Character width in pixels must be greater than zero.");
		}
		else if (charWidth == 0)
		{
			pageWidthInChars = 
				getIntegerParameter(
					JRTextExporterParameter.PAGE_WIDTH,
					JRTextExporterParameter.PROPERTY_PAGE_WIDTH,
					0
					);
			if (pageWidthInChars <= 0)
			{
				throw new JRException("Character width in pixels or page width in characters must be specified and must be greater than zero.");
			}
			
			charWidth = jasperPrint.getPageWidth() / (float)pageWidthInChars;
		}
		else
		{
			pageWidthInChars = (int)(jasperPrint.getPageWidth() / charWidth);
		}
		

		charHeight = 
			getFloatParameter(
				JRTextExporterParameter.CHARACTER_HEIGHT,
				JRTextExporterParameter.PROPERTY_CHARACTER_HEIGHT,
				0
				);
		if (charHeight < 0)
		{
			throw new JRException("Character height in pixels must be greater than zero.");
		}
		else if (charHeight == 0)
		{
			pageHeightInChars = 
				getIntegerParameter(
					JRTextExporterParameter.PAGE_HEIGHT,
					JRTextExporterParameter.PROPERTY_PAGE_HEIGHT,
					0
					);
			if (pageHeightInChars <= 0)
			{
				throw new JRException("Character height in pixels or page height in characters must be specified and must be greater than zero.");
			}

			charHeight = jasperPrint.getPageHeight() / (float)pageHeightInChars;
		}
		else
		{
			pageHeightInChars = (int)(jasperPrint.getPageHeight() / charHeight);
		}
	}


	/**
	 *
	 */
	protected void exportReportToWriter() throws JRException, IOException
	{
		for(int reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			setJasperPrint(jasperPrintList.get(reportIndex));

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				/*   */
				setReportParameters();//FIXMENOW check all report level exporter hints and make sure they are read from the current report, not from the first

				for(int i = startPageIndex; i <= endPageIndex; i++)
				{
					if (Thread.interrupted())
					{
						throw new JRException("Current thread interrupted.");
					}

					JRPrintPage page = pages.get(i);

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
		List<JRPrintElement> elements = page.getElements();

		pageData = new char[pageHeightInChars][];
		for (int i = 0; i < pageHeightInChars; i++) {
			pageData[i] = new char[pageWidthInChars];
			Arrays.fill(pageData[i], ' ');
		}

		exportElements(elements);

		for (int i = 0; i < pageHeightInChars; i++) {
			writer.write(pageData[i]);
			writer.write(lineSeparator);
		}

		writer.write(betweenPagesText);

		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void exportElements(List<JRPrintElement> elements)
	{
		for (int i = 0; i < elements.size();i++)
		{
			Object element = elements.get(i);
			if (element instanceof JRPrintText)
			{
				exportText((JRPrintText) element);
			}
			else if (element instanceof JRPrintFrame)
			{
				JRPrintFrame frame = (JRPrintFrame) element;
				setFrameElementsOffset(frame, false);
				try
				{
					exportElements(frame.getElements());
				}
				finally
				{
					restoreElementOffsets();
				}
			}
		}
	}


	/**
	 * Renders a text and places it in the output matrix.
	 */
	protected void exportText(JRPrintText element)
	{
		int colSpan = getWidthInChars(element.getWidth());
		int rowSpan = getHeightInChars(element.getHeight());
		int col = getWidthInChars(element.getX() + getOffsetX());
		int row = getHeightInChars(element.getY() + getOffsetY());
		
		if (col + colSpan > pageWidthInChars) 
		{
			//if the text exceeds the page width, truncate the column count
			colSpan = pageWidthInChars - col;
		}

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
		if (rowSpan <= 0 || colSpan <= 0)
		{
			return;
		}

		if (allText != null && allText.length() == 0)
		{
			return;
		}

		// uses an array of string buffers, since the maximum number of rows is already calculated
		StringBuffer[] rows = new StringBuffer[rowSpan];
		rows[0] = new StringBuffer();
		int rowIndex = 0;
		int rowPosition = 0;
		boolean isFirstLine = true;
		
		// first search for \n, because it causes immediate line break
		StringTokenizer lfTokenizer = new StringTokenizer(allText, "\n", true);
		label:while (lfTokenizer.hasMoreTokens()) {
			String line = lfTokenizer.nextToken();
			// if text starts with a new line:
			if(isFirstLine && line.equals("\n"))
			{
				rows[rowIndex].append("");
				rowIndex++;
				if(rowIndex == rowSpan || !lfTokenizer.hasMoreTokens())
				{
					break label;
				}
				rowPosition = 0;
				rows[rowIndex] = new StringBuffer();
				line = lfTokenizer.nextToken();
			}

			isFirstLine = false;
			
			// if there is a series of new lines:
			int emptyLinesCount = 0;
			while(line.equals("\n") && lfTokenizer.hasMoreTokens())
			{
				emptyLinesCount ++;
				line = lfTokenizer.nextToken();
			}
				
			if(emptyLinesCount > 1)
			{
				for(int i = 0; i < emptyLinesCount-1; i++)
				{
					rows[rowIndex].append("");
					rowIndex++;
					if(rowIndex == rowSpan)
					{
						break label;
					}
					rowPosition = 0;
					rows[rowIndex] = new StringBuffer();
					//if this is the last empty line:
					if(!lfTokenizer.hasMoreTokens() && line.equals("\n"))
					{
						rows[rowIndex].append("");
						break label;
					}
				}
			}
			if(!line.equals("\n"))
			{
				
				StringTokenizer spaceTokenizer = new StringTokenizer(line, " ", true);
	
				// divide each text line in words
				while (spaceTokenizer.hasMoreTokens()) {
					String word = spaceTokenizer.nextToken();
	
					// situation: word is larger than the entire column
					// in this case breaking occurs in the middle of the word
					while (word.length() > colSpan) {
						rows[rowIndex].append(word.substring(0, colSpan - rowPosition));
						word = word.substring(colSpan - rowPosition, word.length());
						rowIndex++;
						if(rowIndex == rowSpan)
						{
							break label;
						}
						rowPosition = 0;
						rows[rowIndex] = new StringBuffer();
					}
	
					// situation: word is larger than remaining space on the current line
					// in this case, go to the next line
					if (rowPosition + word.length() > colSpan) 
					{
						rowIndex++;
						if (rowIndex == rowSpan)
						{
							break label;
						}
						rowPosition = 0;
						rows[rowIndex] = new StringBuffer();
					}
	
					// situation: the word is actually a space and it situated at the beginning of a new line
					// in this case, it is removed
					if (rowIndex > 0 && rowPosition == 0 && word.equals(" "))
					{
						continue;
					}
					// situation: the word is small enough to fit in the current line
					// in this case just add the word and increment the cursor position
					rows[rowIndex].append(word);
					rowPosition += word.length();
				}
	
	
				rowIndex++;
				if(rowIndex == rowSpan)
				{
					break;
				}
				rowPosition = 0;
				rows[rowIndex] = new StringBuffer();
			}
		}

		int colOffset = 0;
		int rowOffset = 0;

		switch (element.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				rowOffset = rowSpan - rowIndex;
				break;
			}
			case MIDDLE :
			{
				rowOffset = (rowSpan - rowIndex) / 2;
				break;
			}
		}

		for (int i = 0; i < rowIndex; i++) {
			String line = rows[i].toString();
			int pos = line.length() - 1;
			while (pos >= 0 && line.charAt(pos) == ' ')
			{
				pos--;
			}
			line = line.substring(0, pos + 1);
			switch (element.getHorizontalAlignmentValue())
			{
				case RIGHT :
				{
					colOffset = colSpan - line.length();
					break;
				}
				case CENTER :
				{
					colOffset = (colSpan - line.length()) / 2;
					break;
				}
	
				// if text is justified, there is no offset, but the line text is modified
				// the last line in the paragraph is not justified.
				case JUSTIFIED :
				{
					if (i < rowIndex -1)
					{
						line = justifyText(line, colSpan);
					}
					break;
				}
			}

			char[] chars = line.toCharArray();
			System.arraycopy(chars, 0, pageData[row + rowOffset + i], col + colOffset, chars.length);
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
		{
			return s;
		}

		String[] words = new String[tokenCount];
		int i = 0;
		while (t.hasMoreTokens())
		{
			words[i++] = t.nextToken();
		}

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
			{
				justified.append(' ');
			}
		}
		justified.append(words[words.length-1]);

		return justified.toString();
	}


	/**
	 * Transforms height from pixel space to character space.
	 */
	protected int getHeightInChars(int height)
	{
		//return (int) (((long) pageHeightInChars * height) / jasperPrint.getPageHeight());
		return Math.round(height / charHeight);
	}

	/**
	 * Transforms width from pixel space to character space.
	 */
	protected int getWidthInChars(int width)
	{
//		return pageWidthInChars * width / jasperPrint.getPageWidth();
		return Math.round(width / charWidth);
	}


	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return textElement.getStyledText(JRStyledTextAttributeSelector.NONE);
	}

	/**
	 *
	 */
	protected String getExporterKey()
	{
		return null;
	}
}
