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

/*
 * Contributors:
 * Greg Hilton
 */

package net.sf.jasperreports.engine.export;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRXlsAbstractMetadataExporter extends JRXlsAbstractExporter
{
	/**
	 * 
	 */
	protected boolean writeHeader;
	protected List<String> columnNames;
	protected Map<String, Integer> columnNamesMap;
	protected int rowIndex;
	boolean hasDefinedColumns;
	Map<String, Object> currentRow;	
	Map<String, Object> repeatedValues;	

	@Override
	protected void setParameters() {
		super.setParameters();
		
		writeHeader = getBooleanParameter(
				JRXlsAbstractMetadataExporterParameter.WRITE_HEADER, 
				JRXlsAbstractMetadataExporterParameter.PROPERTY_WRITE_HEADER,
				false
				); 
		
		setColumnNames();
		currentRow = new HashMap<String, Object>();
		repeatedValues = new HashMap<String, Object>();
	}

	
	/**
	 * 
	 */
	protected void setColumnNames()
	{
		String[] columnNamesArray = 
			getStringArrayParameter(
				JRXlsAbstractMetadataExporterParameter.COLUMN_NAMES,
				JRXlsAbstractMetadataExporterParameter.PROPERTY_COLUMN_NAMES_PREFIX
				);
		
		columnNames = new ArrayList<String>();
		columnNamesMap = new HashMap<String, Integer>();
		if (columnNamesArray != null && columnNamesArray.length > 0)
		{
			for(int i = 0; i < columnNamesArray.length; ++i)
			{
				if (columnNamesArray[i] == null)
				{
					columnNames.add(null);
				}
				else
				{
					String[] currentColumnNamesArray = columnNamesArray[i].split(",");
					for(int j = 0; j < currentColumnNamesArray.length; j++)
					{
						String columnName = currentColumnNamesArray[j].trim();
						columnNamesMap.put(columnName, columnNames.size());
						columnNames.add(columnName);
					}
				}
			}
			hasDefinedColumns = true;
		}
		
	}
	
	@Override
	protected void exportReportToStream(OutputStream os) throws JRException
	{
		openWorkbook(os);
		sheetNamesMap = new HashMap<String,Integer>();

		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			setJasperPrint(jasperPrintList.get(reportIndex));
			
			defaultFont = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
			
			setExporterHints();

			if(
				getParameter(JRXlsAbstractExporterParameter.SHEET_NAMES) == null
				|| (getParameterResolver() instanceof ParameterOverriddenResolver
					&& sheetNames != null && sheetNames.length > 0)
				)
			{
				sheetNamesIndex = 0;
			}

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				if (isOnePagePerSheet)
				{

					for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new JRException("Current thread interrupted.");
						}

						JRPrintPage page = pages.get(pageIndex);

						createSheet(getSheetName(null));

						// we need to count all sheets generated for all exported documents
						sheetIndex++;
						sheetNamesIndex++;
						rowIndex = 0;
						resetAutoFilters();
						
						if(gridRowFreezeIndex > 0 || gridColumnFreezeIndex > 0)
						{
							setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
						}
						
						/*   */
						exportPage(page);
					}
				}
				else
				{
					// Create the sheet before looping.
					createSheet(getSheetName(jasperPrint.getName()));

					// we need to count all sheets generated for all exported documents
					sheetIndex++;
					sheetNamesIndex++;
					resetAutoFilters();
					
					if(gridRowFreezeIndex > 0 || gridColumnFreezeIndex > 0)
					{
						setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
					}
					
					if (filter instanceof ResetableExporterFilter)
					{
						((ResetableExporterFilter)filter).reset();
					}
					for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new JRException("Current thread interrupted.");
						}
						JRPrintPage page = pages.get(pageIndex);
						exportPage(page);
					}
					
				}
			}
		}

		closeWorkbook(os);
	}

	/**
	 * 
	 */
	protected int exportPage(JRPrintPage page) throws JRException
	{
		
		List<JRPrintElement> elements = page.getElements();
		currentRow = new HashMap<String, Object>();
		rowIndex += writeHeader ? 1 : 0;
		
		for (int i = 0; i < elements.size(); ++i) 
		{
			JRPrintElement element = elements.get(i);
			
			String sheetName = element.getPropertiesMap().getProperty(JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAME);
			if(sheetName != null)
			{
				setSheetName(sheetName);
			}
			
			if (element instanceof JRPrintLine)
			{
				exportLine((JRPrintLine)element);
			}
			else if (element instanceof JRPrintRectangle)
			{
				exportRectangle((JRPrintRectangle)element);
			}
			else if (element instanceof JRPrintEllipse)
			{
				exportRectangle((JRPrintEllipse)element);
			}
			else if (element instanceof JRPrintImage)
			{
				exportImage((JRPrintImage) element);
			}
			else if (element instanceof JRPrintText)
			{
				exportText((JRPrintText)element);
			}
			else if (element instanceof JRPrintFrame)
			{
				exportFrame((JRPrintFrame) element);
			}
			else if (element instanceof JRGenericPrintElement)
			{
				exportGenericElement((JRGenericPrintElement) element);
			}
			
			String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporterParameter.PROPERTY_COLUMN_NAME);
			
			String rowFreeze = JRProperties.getProperty(element, JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE);
			
			int rowFreezeIndex = rowFreeze == null 
				? 0 
				: (EdgeEnum.BOTTOM.getName().equals(rowFreeze) 
						? rowIndex + 1
						: rowIndex
						);
			
			String columnFreeze = JRProperties.getProperty(element, JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE);
				
			int columnFreezeIndex = columnFreeze == null 
				? 0 
				: (EdgeEnum.RIGHT.getName().equals(columnFreeze) 
						? columnNamesMap.get(currentColumnName) + 1
						: columnNamesMap.get(currentColumnName)
						);

			if(rowFreezeIndex > 0 || columnFreezeIndex > 0)
			{
				setFreezePane(rowFreezeIndex, columnFreezeIndex, rowFreezeIndex > 0, columnFreezeIndex > 0);
			}
			
		}
		// write last row
		if (columnNames.size() > 0)
		{
			writeCurrentRow(currentRow, repeatedValues);
		}

		if(autoFilterStart != null)
		{
			setAutoFilter(autoFilterStart + ":" + (autoFilterEnd != null ? autoFilterEnd : autoFilterStart));
		}
		else if(autoFilterEnd != null)
		{
			setAutoFilter(autoFilterEnd + ":" + autoFilterEnd);
		}

		setRowLevels(null, null);
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
		
		return 0;
	}
	
	
	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return textElement.getFullStyledText(JRStyledTextAttributeSelector.NONE);
	}

	/**
	 *
	 */
	protected static TextAlignHolder getTextAlignHolder(JRPrintText textElement)
	{
		HorizontalAlignEnum horizontalAlignment;
		VerticalAlignEnum verticalAlignment;
		RotationEnum rotation = textElement.getRotationValue();

		switch (textElement.getRotationValue())
		{
			case LEFT :
			{
				switch (textElement.getHorizontalAlignmentValue())
				{
					case LEFT :
					{
						verticalAlignment = VerticalAlignEnum.BOTTOM;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalAlignEnum.TOP;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalAlignEnum.BOTTOM;
					}
				}

				switch (textElement.getVerticalAlignmentValue())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalAlignEnum.LEFT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalAlignEnum.RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalAlignEnum.LEFT;
					}
				}

				break;
			}
			case RIGHT :
			{
				switch (textElement.getHorizontalAlignmentValue())
				{
					case LEFT :
					{
						verticalAlignment = VerticalAlignEnum.TOP;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalAlignEnum.BOTTOM;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalAlignEnum.TOP;
					}
				}

				switch (textElement.getVerticalAlignmentValue())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalAlignEnum.RIGHT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalAlignEnum.LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalAlignEnum.RIGHT;
					}
				}

				break;
			}
			case UPSIDE_DOWN:
			case NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalAlignmentValue();
				verticalAlignment = textElement.getVerticalAlignmentValue();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}

	/**
	 *
	 */
	protected int getImageBorderCorrection(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			if (lineWidth >= 2f)
			{
				return 2;
			}

			return 1;
		}
		
		return isImageBorderFixEnabled ? 1 : 0;
	}

	
	/**
	 *
	 */
	private String getSheetName(String sheetName)
	{
		if (sheetNames != null && sheetNamesIndex < sheetNames.length)
		{
			sheetName = sheetNames[sheetNamesIndex];
		}
		
		if (sheetName == null)
		{
			// no sheet name was specified or if it was null
			return "Page " + (sheetIndex + 1);
		}

		// sheet name specified; assuming it is first occurrence
		int crtIndex = Integer.valueOf(1);
		String txtIndex = "";

		if(sheetNamesMap.containsKey(sheetName))
		{
			// sheet names must be unique; altering sheet name using number of occurrences
			crtIndex = sheetNamesMap.get(sheetName).intValue() + 1;
			txtIndex = String.valueOf(crtIndex);
		}

		sheetNamesMap.put(sheetName, crtIndex);

		String name = sheetName;
		if(txtIndex.length() > 0)
		{
			name += " " + txtIndex;
		}
		
		if (name.length() > 31)
		{
			name = (sheetName + " ").substring(0, 31 - txtIndex.length()) + txtIndex;
		}
		
		return name;
	}

	/**
	 * 
	 */
	protected boolean isWrapText(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(PROPERTY_WRAP_TEXT)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return JRProperties.getBooleanProperty(element, PROPERTY_WRAP_TEXT, wrapText);
		}
		return wrapText;
	}

	/**
	 * 
	 */
	protected boolean isCellLocked(JRPrintElement element)
	{

		if (
				element.hasProperties()
				&& element.getPropertiesMap().containsProperty(PROPERTY_CELL_LOCKED)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return JRProperties.getBooleanProperty(element, PROPERTY_CELL_LOCKED, cellLocked);
			}
			return cellLocked;
	}

	/**
	 * 
	 */
	protected String getFormula(JRPrintText text)
	{
		String formula = text.getPropertiesMap().getProperty(JRXlsAbstractExporter.PROPERTY_CELL_FORMULA);
		if( formula != null)
		{
			formula = formula.trim();
			if(formula.startsWith("="))
			{
				formula = formula.substring(1);
			}
		}
		return formula;
	}

	/**
	 * 
	 */
	protected void setSheetNames()
	{
		String[] sheetNamesArray = 
			getStringArrayParameter(
				JRXlsAbstractExporterParameter.SHEET_NAMES,
				JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAMES_PREFIX
				);
		if (sheetNamesArray != null)
		{
			List<String> sheetNamesList = new ArrayList<String>();
			for(int i = 0; i < sheetNamesArray.length; i++)
			{
				if (sheetNamesArray[i] == null)
				{
					sheetNamesList.add(null);
				}
				else
				{
					String[] currentSheetNamesArray = sheetNamesArray[i].split("/");
					for(int j = 0; j < currentSheetNamesArray.length; j++)
					{
						sheetNamesList.add(currentSheetNamesArray[j]);
					}
				}
			}
			sheetNames = sheetNamesList.toArray(new String[sheetNamesList.size()]);
		}
		
	}
	
	/**
	 * 
	 */
	protected boolean isCellHidden(JRPrintElement element)
	{
		if (
				element.hasProperties()
				&& element.getPropertiesMap().containsProperty(PROPERTY_CELL_HIDDEN)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return JRProperties.getBooleanProperty(element, PROPERTY_CELL_HIDDEN, cellHidden);
			}
			return cellHidden;
	}

	/**
	 * Compares the highest index of the currentRow's columns with the index of the column to be inserted
	 * to determine if the current column is read in the proper order
	 * </p>
	 * @param currentRow
	 * @param currentColumnName
	 */
	protected boolean isColumnReadOnTime(Map<String, Object> currentRow, String currentColumnName)
	{
		int indexOfLastFilledColumn = -1;
		Set<String> currentlyFilledColumns = currentRow.keySet();
		
		for (String column: currentlyFilledColumns)
		{
			indexOfLastFilledColumn = Math.max(indexOfLastFilledColumn, columnNames.indexOf(column));
		}
		
		return indexOfLastFilledColumn < columnNames.indexOf(currentColumnName);
	}
	
	protected void exportText(JRPrintText text, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException
	{
	}

	public void exportImage(JRPrintImage image, JRExporterGridCell cell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException
	{
	}

	protected void exportRectangle(JRPrintGraphicElement element, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException
	{
	}

	protected void exportLine(JRPrintLine line, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException
	{
	}

	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException
	{
	}

	protected void exportGenericElement(JRGenericPrintElement element, JRExporterGridCell cell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException
	{
	}
	
	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException
	{
	}

	protected void addOccupiedCell(OccupiedGridCell occupiedGridCell, int colIndex, int rowIndex) throws JRException
	{
	}

	protected void setCell(JRExporterGridCell gridCell, int x, int y)
	{
	}
	
	protected abstract ExporterNature getNature();

	protected abstract void openWorkbook(OutputStream os) throws JRException;

	protected abstract void createSheet(String name);

	protected abstract void closeWorkbook(OutputStream os) throws JRException;

	protected void setColumnWidth(int col, int width)
	{
		setColumnWidth(col, width, false);
	}

	protected abstract void setColumnWidth(int col, int width, boolean autoFit);

	protected abstract void removeColumn(int col);

	protected void setRowHeight(int rowIndex, int lastRowHeight) throws JRException
	{
		setRowHeight(rowIndex, lastRowHeight, null, null);
	}

	protected abstract void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo) throws JRException;

	protected abstract void exportText(JRPrintText textElement) throws JRException;

	public abstract void exportImage(JRPrintImage image) throws JRException;

	protected abstract void exportRectangle(JRPrintGraphicElement element) throws JRException;

	protected abstract void exportLine(JRPrintLine line) throws JRException;

	protected abstract void exportFrame(JRPrintFrame frame) throws JRException;

	protected abstract void exportGenericElement(JRGenericPrintElement element) throws JRException;
	
	protected abstract void writeCurrentRow(Map<String, Object> currentRow, Map<String, Object> repeatedValues)  throws JRException;

}
