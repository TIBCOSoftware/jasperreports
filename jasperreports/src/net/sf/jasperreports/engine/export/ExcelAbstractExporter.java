/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter.SheetInfo;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.ObjectUtils;
import net.sf.jasperreports.export.ExcelExporterProperties;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PrintPartUnrollExporterInput;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;
import net.sf.jasperreports.renderers.RenderersCache;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public abstract class ExcelAbstractExporter<RC extends XlsReportConfiguration, C extends XlsExporterConfiguration, E extends JRExporterContext>
		extends JRAbstractExporter<RC, C, OutputStreamExporterOutput, E> implements ExcelExporterProperties {

	protected static class TextAlignHolder {
		public final HorizontalTextAlignEnum horizontalAlignment;
		public final VerticalTextAlignEnum verticalAlignment;
		public final RotationEnum rotation;

		public TextAlignHolder(HorizontalTextAlignEnum horizontalAlignment, VerticalTextAlignEnum verticalAlignment,
				RotationEnum rotation) {
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
		}
	}

	/**
	 *
	 */
	protected List<Object> loadedFonts = new ArrayList<Object>();

	/**
	 *
	 */
	protected ExporterNature nature;

	/**
	 *
	 */
	protected String[] sheetNames;

	protected int reportIndex;
	protected int pageIndex;
	protected PrintPageFormat pageFormat;
	protected Map<Integer, Boolean> onePagePerSheetMap = new HashMap<Integer, Boolean>();
	protected int sheetsBeforeCurrentReport;
	protected Map<Integer, Integer> sheetsBeforeCurrentReportMap = new HashMap<Integer, Integer>();
	protected RenderersCache renderersCache;

	/**
	 *
	 */
	protected JRFont defaultFont;

	
	protected SheetInfo sheetInfo;
	
	/**
	 * Used for counting the total number of sheets.
	 */
	protected int sheetIndex;

	/**
	 * Used for iterating through sheet names.
	 */
	protected int sheetNamesIndex;

	/**
	 * Used when indexing the identical sheet name. Contains sheet names as keys and
	 * the number of occurrences of each sheet name as values.
	 */
	protected Map<String, Integer> sheetNamesMap;
	protected String autoFilterStart;
	protected String autoFilterEnd;

	protected boolean firstPageNotSet;

	protected Boolean keepTemplateSheets;
	protected String workbookTemplate;

	protected String invalidCharReplacement;
	protected int maxColumnIndex;

	protected Map<NameScope, String> definedNamesMap;

	public class NameScope {
		private String name;
		private String scope;

		public NameScope(String name, String scope) {
			this.name = name;
			this.scope = scope;
		}

		public String getName() {
			return name;
		}

		public String getScope() {
			return scope;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return this == null;
			} else {
				if (this == null || !obj.getClass().equals(NameScope.class)) {
					return false;
				} else {
					@SuppressWarnings("unchecked")
					NameScope obj1 = (NameScope) obj;
					return ObjectUtils.equals(this.name, obj1.name) && ObjectUtils.equals(this.scope, obj1.scope);
				}
			}
		}

		@Override
		public int hashCode() {
			ObjectUtils.HashCode hash = ObjectUtils.hash();
			hash.add(name);
			hash.add(scope);
			return hash.getHashCode();
		}
	}

	/**
	 *
	 */
	protected JRFont getDefaultFont() {
		return defaultFont;
	}

	/**
	 * @see #ExcelAbstractExporter(JasperReportsContext)
	 */
	public ExcelAbstractExporter() {
		this(DefaultJasperReportsContext.getInstance());
	}

	/**
	 *
	 */
	public ExcelAbstractExporter(JasperReportsContext jasperReportsContext) {
		super(jasperReportsContext);
	}

	@Override
	public void exportReport() throws JRException {
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		initExport();

		ensureOutput();

		OutputStream outputStream = getExporterOutput().getOutputStream();

		try {
			exportReportToStream(outputStream);
		} catch (IOException e) {
			throw new JRRuntimeException(e);
		} finally {
			getExporterOutput().close();
			resetExportContext();
		}
	}

	@Override
	protected void ensureInput() {
		super.ensureInput();

		exporterInput = new PrintPartUnrollExporterInput(exporterInput);

		jasperPrint = exporterInput.getItems().get(0).getJasperPrint();// this is just for the sake of
																		// getCurrentConfiguration() calls made prior to
																		// any setCurrentExporterInputItem() call
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void ensureOutput() {
		if (exporterOutput == null) {
			exporterOutput = new net.sf.jasperreports.export.parameters.ParametersOutputStreamExporterOutput(
					getJasperReportsContext(), getParameters(), getCurrentJasperPrint());
		}
	}

	@Override
	protected void initExport() {
		super.initExport();

		sheetInfo = null;
		sheetIndex = 0;
		onePagePerSheetMap.clear();
		sheetsBeforeCurrentReport = 0;
		sheetsBeforeCurrentReportMap.clear();
	}

	@Override
	protected void initReport() {
		super.initReport();

		setSheetNames();
		if (jasperPrint.hasProperties()
				&& jasperPrint.getPropertiesMap().containsProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS)) {
			// allows null values for the property
			invalidCharReplacement = jasperPrint.getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS);
		} else {
			invalidCharReplacement = getPropertiesUtil().getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS,
					jasperPrint);
		}

		renderersCache = new RenderersCache(getJasperReportsContext());
	}

	protected void updatePrintSettings(SheetInfo.SheetPrintSettings printSettings,
			XlsReportConfiguration configuration) {
		if (printSettings.getPageHeight() == null) {
			printSettings.setPageHeight(configuration.getPrintPageHeight() == null ? jasperPrint.getPageHeight()
					: configuration.getPrintPageHeight());
		}
		if (printSettings.getPageWidth() == null) {
			printSettings.setPageWidth(configuration.getPrintPageWidth() == null ? jasperPrint.getPageWidth()
					: configuration.getPrintPageWidth());
		}
		if (printSettings.getTopMargin() == null) {
			printSettings.setTopMargin(
					configuration.getPrintPageTopMargin() == null ? 0 : configuration.getPrintPageTopMargin());
		}
		if (printSettings.getLeftMargin() == null) {
			printSettings.setLeftMargin(
					configuration.getPrintPageLeftMargin() == null ? 0 : configuration.getPrintPageLeftMargin());
		}
		if (printSettings.getBottomMargin() == null) {
			printSettings.setBottomMargin(
					configuration.getPrintPageBottomMargin() == null ? 0 : configuration.getPrintPageBottomMargin());
		}
		if (printSettings.getRightMargin() == null) {
			printSettings.setRightMargin(
					configuration.getPrintPageRightMargin() == null ? 0 : configuration.getPrintPageRightMargin());
		}
		if (printSettings.getHeaderMargin() == null) {
			printSettings.setHeaderMargin(
					configuration.getPrintHeaderMargin() == null ? 0 : configuration.getPrintHeaderMargin());
		}
		if (printSettings.getFooterMargin() == null) {
			printSettings.setFooterMargin(
					configuration.getPrintFooterMargin() == null ? 0 : configuration.getPrintFooterMargin());
		}
	}

	protected void updateHeaderFooter(SheetInfo.SheetPrintSettings printSettings,
			XlsReportConfiguration configuration) {
		if (printSettings.getHeaderLeft() == null) {
			printSettings.setHeaderLeft(configuration.getSheetHeaderLeft());
		}
		if (printSettings.getHeaderCenter() == null) {
			printSettings.setHeaderCenter(configuration.getSheetHeaderCenter());
		}
		if (printSettings.getHeaderRight() == null) {
			printSettings.setHeaderRight(configuration.getSheetHeaderRight());
		}
		if (printSettings.getFooterLeft() == null) {
			printSettings.setFooterLeft(configuration.getSheetFooterLeft());
		}
		if (printSettings.getFooterCenter() == null) {
			printSettings.setFooterCenter(configuration.getSheetFooterCenter());
		}
		if (printSettings.getFooterRight() == null) {
			printSettings.setFooterRight(configuration.getSheetFooterRight());
		}
	}

	protected void mergeAndSetRowLevels(XlsRowLevelInfo levelInfo, SortedMap<String, Boolean> rowLevelMap,
			int rowIndex) {
		if (rowLevelMap != null) {
			SortedMap<String, Integer> crtLevelMap = levelInfo.getLevelMap();

			for (String level : rowLevelMap.keySet()) {
				Boolean isEndMarker = rowLevelMap.get(level);

				// check if this level group is already open
				if (crtLevelMap.containsKey(level)) {
					// the level group is already open

					if (isEndMarker) {
						// the level group needs to be closed, together with all its child level groups
						setRowLevels(levelInfo, level);

						// clean up current level group and nested level groups as they were closed
						for (Iterator<String> it = crtLevelMap.keySet().iterator(); it.hasNext();) {
							if (level.compareTo(it.next()) <= 0) {
								it.remove();
							}
						}
					}
				} else // if (!isEndMarker) // FIXMEXLS we should not add level if it is an end marker
				{
					// the level group is not yet open

					// we check to see if this level is higher than existing levels
					if (crtLevelMap.size() > 0 && level.compareTo(crtLevelMap.firstKey()) < 0) {
						// the level is higher than existing levels, so it has to close them all
						setRowLevels(levelInfo, level);

						// clean up nested level groups as they were closed; the current one is not yet
						// among them
						for (Iterator<String> it = crtLevelMap.keySet().iterator(); it.hasNext();) {
							if (level.compareTo(it.next()) < 0) {
								it.remove();
							}
						}
					}

					// create the current level group
//					XlsRowLevelRange range = new XlsRowLevelRange();
//					range.setStartIndex(rowIndex);
					// range.setEndIndex(rowIndex);
					// range.setName(groupName);
					crtLevelMap.put(level, rowIndex);
				}
			}
		}

		levelInfo.setEndIndex(rowIndex);
	}

	@Override
	protected JRStyledText getStyledText(JRPrintText textElement) {
		JRStyledTextAttributeSelector selector = textElement.getModeValue() == ModeEnum.OPAQUE ? allSelector
				: noBackcolorSelector;
		return textElement.getFullStyledText(selector);
	}

	/**
	 *
	 */
	public static TextAlignHolder getTextAlignHolder(JRPrintText textElement) {
		HorizontalTextAlignEnum horizontalAlignment;
		VerticalTextAlignEnum verticalAlignment;
		RotationEnum rotation = textElement.getRotationValue();

		switch (textElement.getRotationValue()) {
		case LEFT: {
			switch (textElement.getHorizontalTextAlign()) {
			case JUSTIFIED: {
				verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
				break;
			}
			case RIGHT: {
				verticalAlignment = VerticalTextAlignEnum.TOP;
				break;
			}
			case CENTER: {
				verticalAlignment = VerticalTextAlignEnum.MIDDLE;
				break;
			}
			case LEFT:
			default: {
				verticalAlignment = VerticalTextAlignEnum.BOTTOM;
			}
			}

			switch (textElement.getVerticalTextAlign()) {
			case JUSTIFIED: {
				horizontalAlignment = HorizontalTextAlignEnum.JUSTIFIED;
				break;
			}
			case BOTTOM: {
				horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
				break;
			}
			case MIDDLE: {
				horizontalAlignment = HorizontalTextAlignEnum.CENTER;
				break;
			}
			case TOP:
			default: {
				horizontalAlignment = HorizontalTextAlignEnum.LEFT;
			}
			}

			break;
		}
		case RIGHT: {
			switch (textElement.getHorizontalTextAlign()) {
			case JUSTIFIED: {
				verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
				break;
			}
			case RIGHT: {
				verticalAlignment = VerticalTextAlignEnum.BOTTOM;
				break;
			}
			case CENTER: {
				verticalAlignment = VerticalTextAlignEnum.MIDDLE;
				break;
			}
			case LEFT:
			default: {
				verticalAlignment = VerticalTextAlignEnum.TOP;
			}
			}

			switch (textElement.getVerticalTextAlign()) {
			case JUSTIFIED: {
				horizontalAlignment = HorizontalTextAlignEnum.JUSTIFIED;
				break;
			}
			case BOTTOM: {
				horizontalAlignment = HorizontalTextAlignEnum.LEFT;
				break;
			}
			case MIDDLE: {
				horizontalAlignment = HorizontalTextAlignEnum.CENTER;
				break;
			}
			case TOP:
			default: {
				horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
			}
			}

			break;
		}
		case UPSIDE_DOWN:
		case NONE:
		default: {
			horizontalAlignment = textElement.getHorizontalTextAlign();
			verticalAlignment = textElement.getVerticalTextAlign();
		}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}

	/**
	 *
	 */
	protected int getImageBorderCorrection(JRPen pen) {
		float lineWidth = pen.getLineWidth();

		if (lineWidth > 0f) {
			if (lineWidth >= 2f) {
				return 2;
			}

			return 1;
		}

		return getCurrentItemConfiguration().isImageBorderFixEnabled() ? 1 : 0;
	}

	/**
	 *
	 */
	protected String getSheetName(String sheetName, String defaultSheetName) {
		if (sheetName != null) {
			if (sheetNames != null && sheetNamesIndex < sheetNames.length) {
				sheetNames[sheetNamesIndex] = sheetName;
			}
			return getSheetName(sheetName);
		}
		return getSheetName(defaultSheetName);
	}

	protected String getSheetName(String sheetName) {
		if (sheetNames != null && sheetNamesIndex < sheetNames.length) {
			sheetName = sheetNames[sheetNamesIndex];
		}

		if (sheetName == null) {
			// no sheet name was specified or if it was null
			return DEFAULT_SHEET_NAME_PREFIX + (sheetIndex + 1);
		}

		// sheet name specified; assuming it is first occurrence
		int crtIndex = 1;
		String txtIndex = "";
		String validSheetName = sheetName.length() < 32 ? sheetName : sheetName.substring(0, 31);

		if (sheetNamesMap.containsKey(validSheetName)) {
			// sheet names must be unique; altering sheet name using number of occurrences
			crtIndex = sheetNamesMap.get(validSheetName) + 1;
			txtIndex = String.valueOf(crtIndex);
		}

		sheetNamesMap.put(validSheetName, crtIndex);

		String name = sheetName;
		if (txtIndex.length() > 0) {
			name += " " + txtIndex;
		}

		if (name.length() > 31) {
			name = (sheetName + " ").substring(0, 31 - txtIndex.length()) + txtIndex;
		}

		return name;
	}

	// Berechnungsvorschriften für die DIN Formate A, B, und C.
	// Die Angabe der Breite/Höhe erfolgt in [mm].

	protected final int calculateWidthForDinAN(int n) {
		return (int) (Math.pow(2.0, (-0.25 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateHeightForDinAN(int n) {
		return (int) (Math.pow(2.0, (0.25 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateWidthForDinBN(int n) {
		return (int) (Math.pow(2.0, -(n / 2.0)) * 1000.0);
	}

	protected final int calculateHeightForDinBN(int n) {
		return (int) (Math.pow(2.0, (0.5 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateWidthForDinCN(int n) {
		return (int) (Math.pow(2.0, (-0.125 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateHeightForDinCN(int n) {
		return (int) (Math.pow(2.0, (0.375 - (n / 2.0))) * 1000.0);
	}

	/**
	 * 
	 */
	protected boolean isWrapText(JRPrintElement element) {
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_WRAP_TEXT)) {
			// we make this test to avoid reaching the global default value of the property
			// directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_WRAP_TEXT,
					getCurrentItemConfiguration().isWrapText());
		}
		return getCurrentItemConfiguration().isWrapText();
	}

	/**
	 * 
	 */
	protected boolean isCellLocked(JRPrintElement element) {

		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_CELL_LOCKED)) {
			// we make this test to avoid reaching the global default value of the property
			// directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_CELL_LOCKED,
					getCurrentItemConfiguration().isCellLocked());
		}
		return getCurrentItemConfiguration().isCellLocked();
	}

	/**
	 * 
	 */
	protected boolean isShrinkToFit(JRPrintElement element) {
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_SHRINK_TO_FIT)) {
			// we make this test to avoid reaching the global default value of the property
			// directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_SHRINK_TO_FIT,
					getCurrentItemConfiguration().isShrinkToFit());
		}
		return getCurrentItemConfiguration().isShrinkToFit();
	}

	/**
	 * 
	 */
	protected boolean isIgnoreTextFormatting(JRPrintElement element) {
		if (element.hasProperties() && element.getPropertiesMap()
				.containsProperty(XlsReportConfiguration.PROPERTY_IGNORE_TEXT_FORMATTING)) {
			// we make this test to avoid reaching the global default value of the property
			// directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element,
					XlsReportConfiguration.PROPERTY_IGNORE_TEXT_FORMATTING,
					getCurrentItemConfiguration().isIgnoreTextFormatting());
		}
		return getCurrentItemConfiguration().isIgnoreTextFormatting();
	}

	/**
	 * 
	 */
	protected String getFormula(JRPrintText text) {
		String formula = JRPropertiesUtil.getOwnProperty(text, ExcelAbstractExporter.PROPERTY_CELL_FORMULA);
		if (formula != null) {
			formula = formula.trim();
			if (formula.startsWith("=")) {
				formula = formula.substring(1);
			}
		}
		return formula;
	}

	/**
	 * 
	 */
	protected void setSheetNames() {
		String[] sheetNamesArray = getCurrentItemConfiguration().getSheetNames();

		List<String> sheetNamesList = JRStringUtil.split(sheetNamesArray, "/");
		sheetNames = sheetNamesList == null ? null
				: (String[]) sheetNamesList.toArray(new String[sheetNamesList.size()]);
	}

	/**
	 * 
	 */
	protected boolean hasGlobalSheetNames()// FIXMEEXPORT check sheet names
	{
		Boolean globalSheetNames = null;

		boolean isOverrideHintsDefault = propertiesUtil
				.getBooleanProperty(ReportExportConfiguration.PROPERTY_EXPORT_CONFIGURATION_OVERRIDE_REPORT_HINTS);
		if (itemConfiguration != null && itemConfiguration.getSheetNames() != null) {
			boolean isExporterConfigOverrideHints = itemConfiguration.isOverrideHints() == null ? isOverrideHintsDefault
					: itemConfiguration.isOverrideHints();
			if (isExporterConfigOverrideHints) {
				globalSheetNames = true;
			}
		}

		if (globalSheetNames == null) {
			XlsReportConfiguration lcItemConfiguration = (XlsReportConfiguration) crtItem.getConfiguration();
			if (lcItemConfiguration != null && lcItemConfiguration.getSheetNames() != null) {
				boolean isItemConfigOverrideHints = lcItemConfiguration.isOverrideHints() == null
						? isOverrideHintsDefault
						: lcItemConfiguration.isOverrideHints();
				if (isItemConfigOverrideHints) {
					globalSheetNames = false;
				}
			}
		}

		if (globalSheetNames == null) {
			List<PropertySuffix> properties = JRPropertiesUtil.getProperties(getCurrentJasperPrint(),
					XlsReportConfiguration.PROPERTY_SHEET_NAMES_PREFIX);
			globalSheetNames = properties == null || properties.isEmpty();
		}

		return globalSheetNames;
	}

	/**
	 * 
	 */
	protected boolean isCellHidden(JRPrintElement element) {
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_CELL_HIDDEN)) {
			// we make this test to avoid reaching the global default value of the property
			// directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_CELL_HIDDEN,
					getCurrentItemConfiguration().isCellHidden());
		}
		return getCurrentItemConfiguration().isCellHidden();
	}

	/**
	 * 
	 */
	protected String getConvertedPattern(JRPrintText text, String pattern) {
		String convertedPattern = JRPropertiesUtil.getOwnProperty(text, PROPERTY_CELL_PATTERN);
		if (convertedPattern == null || convertedPattern.trim().length() == 0) {
			Map<String, String> formatPatternsMap = getCurrentItemConfiguration().getFormatPatternsMap();
			if (formatPatternsMap != null && formatPatternsMap.containsKey(pattern)) {
				return formatPatternsMap.get(pattern);
			}
			return pattern == null || pattern.trim().length() == 0 ? null : pattern;
		}
		return convertedPattern;
	}

	/**
	 * 
	 * @return the calculated column index
	 */
	protected int getColumnIndex(String columnName) {
		int index = -1;
		if (columnName != null) {
			String upperColumnName = columnName.toUpperCase();
			if (upperColumnName.matches("[A-Z]*")) {
				for (int i = 0; i < upperColumnName.length(); i++) {
					index += (upperColumnName.charAt(i) - 64) * (int) Math.pow(26, upperColumnName.length() - i - 1);
				}
			}
		}
		return index;
	}

	/**
	 * @deprecated Replaced by {@link JRStringUtil#getLetterNumeral(int, boolean)}.
	 */
	protected String getColumnName(int columnIndex) {
		return JRStringUtil.getLetterNumeral(columnIndex + 1, true);
	}

	protected void resetAutoFilters() {
		autoFilterStart = null;
		autoFilterEnd = null;
	}

	/**
	 * 
	 */
	protected boolean isUseTimeZone(JRPrintElement element) {
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_USE_TIMEZONE)) {
			// we make this test to avoid reaching the global default value of the property
			// directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_USE_TIMEZONE,
					getCurrentItemConfiguration().isUseTimeZone());
		}
		return getCurrentItemConfiguration().isUseTimeZone();
	}

	protected Date translateDateValue(JRPrintText text, Date value) {
		if (isUseTimeZone(text)) {
			// translate the date to the timezone used at fill time
			TimeZone tz = getTextTimeZone(text);
			value = JRDataUtils.translateToTimezone(value, tz);
		}
		return value;
	}

	protected boolean isValidScale(Integer scale) {
		return scale != null && scale > 9 && scale < 401;
	}

	protected Integer getMaxRowsPerSheet() {
		return getCurrentItemConfiguration().getMaxRowsPerSheet();
	}

	protected ExporterNature getNature() {
		return nature;
	}

	/**
	 *
	 * @param colIndex The 0-based integer column index
	 * @return The column name computed from the 0-based column index
	 */
	public static String getColumIndexName(int colIndex, int maxColIndex) {

		if (colIndex < 0) {
			throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_NEGATIVE_COLUMN_INDEX, new Object[] { colIndex });
		} else if (colIndex > maxColIndex) {
			throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_COLUMN_INDEX_BEYOND_LIMIT,
					new Object[] { colIndex, maxColIndex });
		} else if (colIndex < 26) {
			// first 26 column names are single letters
			return String.valueOf((char) (colIndex + 65));
		} else if (colIndex < 702) // 702 = 26 + 26^2
		{
			// next 626 (= 26^2) column names are 2-letter names
			return String.valueOf((char) (colIndex / 26 + 64)) + String.valueOf((char) (colIndex % 26 + 65));
		} else {
			// next 17576 (= 26^3) column names are 3-letter names;
			// anyway, the 0-based column index may not exceed maxColIndex value
			return String.valueOf((char) ((colIndex - 26) / 676 + 64))
					+ String.valueOf((char) (((colIndex - 26) % 676) / 26 + 65))
					+ String.valueOf((char) (colIndex % 26 + 65));
		}
	}

	protected void configureDefinedNames(ExporterNature exporterNature, JRPrintElement element) {
		if (exporterNature instanceof JRXlsAbstractExporterNature) {
			configureDefinedNames(((JRXlsAbstractExporterNature) exporterNature).getDefinedNames(element));
		}
	}

	protected void configureDefinedNames(PropertySuffix[] names) {
		if (names != null) {
			for (PropertySuffix definedName : names) {
				configureDefinedNames(definedName);
			}
		}
	}

	protected void configureDefinedNames(PropertySuffix propertySuffix) {
		if (propertySuffix != null) {
			String name = propertySuffix.getSuffix();
			String value = propertySuffix.getValue();
			if (name != null && name.trim().length() > 0 && value != null && value.length() > 0) {
				String[] valueScope = value.split(DEFAULT_DEFINED_NAME_SCOPE_SEPARATOR);
				if (valueScope[0] != null && valueScope[0].length() > 0) {
					String scope = valueScope.length > 1 ? valueScope[1] : DEFAULT_DEFINED_NAME_SCOPE;
					NameScope nameScope = new NameScope(name, scope);
					if (valueScope[0].startsWith("=")) {
						definedNamesMap.put(nameScope, valueScope[0].substring(1));
					} else {
						definedNamesMap.put(nameScope, valueScope[0]);
					}
				}
			}
		}
	}

	protected String toExcelName(String name) {
		if (name.isEmpty()) {
			// this is actually invalid, but let's leave it to POI to throw an exception
			return name;
		}

		char[] chars = name.toCharArray();
		StringBuilder escaped = null;

		// TODO? always prepend "_" to avoid invalid names like R/C/A1
		// valid character taken from the HSSFName.validateName method
		if (!Character.isLetter(chars[0]) && chars[0] != '_' && chars[0] != '\\') {
			escaped = new StringBuilder(chars.length * 4 / 3);
			escaped.append('_');
			escaped.append(Integer.toHexString(chars[0]));
		}

		for (int i = 1; i < chars.length; ++i) {
			if (Character.isLetterOrDigit(chars[i]) || chars[i] == '_' || chars[i] == '\\' || chars[i] == '.') {
				if (escaped != null) {
					escaped.append(chars[i]);
				}
			} else {
				if (escaped == null) {
					escaped = new StringBuilder(chars.length * 4 / 3);
					escaped.append(chars, 0, i);
				}

				escaped.append(Integer.toHexString(chars[i]));
			}
		}

		String excelName = escaped == null ? name : escaped.toString();
		if (excelName.length() > 255) {
			excelName = excelName.substring(0, 255);
		}
		// TODO? check for uniqueness
		return excelName;
	}

	// abstract methods

	protected abstract void openWorkbook(OutputStream os) throws JRException, IOException;

	protected abstract void closeSheet();

	protected abstract void closeWorkbook(OutputStream os) throws JRException, IOException;

	protected abstract void setColumnWidth(int col, int width, boolean autoFit);

	protected abstract void addRowBreak(int rowIndex);

	protected abstract void setFreezePane(int rowIndex, int colIndex);

	protected abstract void setSheetName(String sheetName);

	protected abstract void setAutoFilter(String autoFilterRange);

	protected abstract void setRowLevels(XlsRowLevelInfo levelInfo, String level);

	protected abstract void exportReportToStream(OutputStream os) throws JRException, IOException;

	protected abstract void exportEmptyReport() throws JRException, IOException;

}
