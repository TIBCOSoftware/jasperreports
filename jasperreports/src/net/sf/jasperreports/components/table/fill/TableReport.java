/*
G * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.table.fill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElementUtils;
import net.sf.jasperreports.components.iconlabel.ContainerFillEnum;
import net.sf.jasperreports.components.iconlabel.IconLabelComponent;
import net.sf.jasperreports.components.iconlabel.IconPositionEnum;
import net.sf.jasperreports.components.sort.FieldFilter;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.sort.SortElementHtmlHandler;
import net.sf.jasperreports.components.sort.actions.FilterCommand;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.ColumnVisitor;
import net.sf.jasperreports.components.table.TableComponent;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.CompositeDatasetFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBaseTextElement;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElementParameter;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.fill.DatasetExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.util.StyleUtil;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TableReport implements JRReport
{
	/**
	 * Global property that specifies the character to be used on the column header when the tables's column is sorted ascending
	 */
	private static final String PROPERTY_UP_ARROW_CHAR = JRPropertiesUtil.PROPERTY_PREFIX + "components.sort.up.arrow.char"; //FIXMEJIVE move these from here

	/**
	 * Global property that specifies the character to be used on the column header when the tables's column is sorted descending
	 */
	private static final String PROPERTY_DOWN_ARROW_CHAR = JRPropertiesUtil.PROPERTY_PREFIX + "components.sort.down.arrow.char";

	/**
	 * Global property that specifies the character to be used on the column header when the tables's column has a filtered applied
	 */
	private static final String PROPERTY_FILTER_CHAR = JRPropertiesUtil.PROPERTY_PREFIX + "components.filter.char";

	/**
	 * Property that enables/disables the interactivity in the table component
	 * 
	 * <p>
	 * The property can be set:
	 * <ul>
	 * 	<li>globally</li>
	 * 	<li>at report level</li>
	 * 	<li>at component level</li>
	 * 	<li>at column level</li>
	 * </ul>
	 * 
	 * <p>
	 * The default global value of this property is <code>true</code>
	 */
	private static final String PROPERTY_INTERACTIVE_TABLE = JRPropertiesUtil.PROPERTY_PREFIX + "components.table.interactive";

	/**
	 * Column property that specifies the field to be used for sorting, filtering and conditional formatting 
	 */
	public static final String PROPERTY_COLUMN_FIELD = JRPropertiesUtil.PROPERTY_PREFIX + "components.table.column.field";

	/**
	 * Column property that specifies the variable to be used for sorting, filtering and conditional formatting 
	 */
	public static final String PROPERTY_COLUMN_VARIABLE = JRPropertiesUtil.PROPERTY_PREFIX + "components.table.column.variable";

	/**
	 * Column property that enables/disables sorting
	 * 
	 * <p>
	 * It defaults to <code>true</code>
	 */
	public static final String PROPERTY_COLUMN_SORTABLE = JRPropertiesUtil.PROPERTY_PREFIX + "components.table.column.sortable";

	/**
	 * Column property that enables/disables filtering
	 * 
	 * <p>
	 * It defaults to <code>true</code>
	 */
	public static final String PROPERTY_COLUMN_FILTERABLE = JRPropertiesUtil.PROPERTY_PREFIX + "components.table.column.filterable";

	/**
	 * Column property that enables/disables conditional formatting
	 * 
	 * <p>
	 * It defaults to <code>true</code>
	 */
	public static final String PROPERTY_COLUMN_CONDITIONALLY_FORMATTABLE = JRPropertiesUtil.PROPERTY_PREFIX + "components.table.column.conditionally.formattable";


	protected static final String SUMMARY_GROUP_NAME = "__SummaryGroup";

	protected static final String HTML_CLASS_CELL_PREFIX = "cel_";
	protected static final String HTML_CLASS_CELL = "jrcel";
	
	private final FillContext fillContext;
	private final TableComponent table;
	private final JasperReport parentReport;
	private final TableReportDataset mainDataset;
	private final Map<JRExpression, BuiltinExpressionEvaluator> builtinEvaluators;
	private final JRSection detail;
	private final JRDesignBand title;
	private final JRDesignBand summary;
	private final JRDesignBand columnHeader;
	private final JRDesignBand pageFooter;
	private final JRDesignBand lastPageFooter;
	
	private final List<TableIndexProperties> tableIndexProperties;
	private final Map<Integer, JRPropertiesMap> headerHtmlBaseProperties;
	
	private final JRPropertiesUtil propertiesUtil;
	private boolean isInteractiveTable;
	private Map<Column, Boolean> columnInteractivityMapping;
	
	public TableReport(
		FillContext fillContext, 
		TableComponent table, 
		TableReportDataset mainDataset, 
		List<FillColumn> fillColumns, 
		Map<JRExpression, BuiltinExpressionEvaluator> builtinEvaluators
		)
	{
		this.tableIndexProperties = new ArrayList<TableIndexProperties>();
		this.headerHtmlBaseProperties = new HashMap<Integer, JRPropertiesMap>();
		
		this.fillContext = fillContext;
		this.table = table;
		this.parentReport = fillContext.getFiller().getJasperReport();
		this.mainDataset = mainDataset;
		this.builtinEvaluators = builtinEvaluators;
		
		this.propertiesUtil = JRPropertiesUtil.getInstance(fillContext.getFiller().getJasperReportsContext());
		
		// begin: table interactivity
		this.isInteractiveTable  = Boolean.valueOf(propertiesUtil.getProperty(PROPERTY_INTERACTIVE_TABLE, fillContext.getComponentElement(), this.parentReport));

		this.columnInteractivityMapping = new HashMap<Column, Boolean>();
		int interactiveColumnCount = 0;
		for (BaseColumn column: TableUtil.getAllColumns(table)) {
			boolean interactiveColumn = isInteractiveTable;
			if (column.getPropertiesMap().containsProperty(PROPERTY_INTERACTIVE_TABLE)) {
				interactiveColumn = Boolean.valueOf(column.getPropertiesMap().getProperty(PROPERTY_INTERACTIVE_TABLE));
			}
			if (interactiveColumn) {
				interactiveColumnCount++;
			}
			columnInteractivityMapping.put((Column)column, interactiveColumn);
		}
		
		if (interactiveColumnCount > 0) {
			this.isInteractiveTable = true;
		}
		// end: table interactivity
		
		
		this.columnHeader = createColumnHeader(fillColumns);
		this.detail = wrapBand(createDetailBand(fillColumns), new JROrigin(BandTypeEnum.DETAIL));
		this.title = createTitle(fillColumns);
		this.summary = createSummary(fillColumns); 
		this.pageFooter = createPageFooter(fillColumns);
		
		setGroupBands(fillColumns);
		
		if (pageFooter != null && summary != null)
		{
			// if the table has both column footers and table footers, we need to use
			// a dummy group's footer to print the last column footers so that they
			// appear before the table footers
			addSummaryGroup(fillColumns);
			
			// use an empty last page footer so that the regular page footer doesn't
			// show on the last page
			this.lastPageFooter = new JRDesignBand();
			this.lastPageFooter.setHeight(0);
		}
		else
		{
			// use the regular page footer
			this.lastPageFooter = null;
		}
	}
	
	protected JRDesignExpression createBuiltinExpression(BuiltinExpressionEvaluator evaluator)
	{
		// we only need an empty expression object here
		// the evaluation logic is separate
		JRDesignExpression expression = new JRDesignExpression();
		builtinEvaluators.put(expression, evaluator);
		return expression;
	}
	
	protected class ReportBandInfo
	{
		final JRDesignBand band;
		final List<JRDesignElementGroup> rowGroups = new ArrayList<JRDesignElementGroup>();
		
		ReportBandInfo(JRDesignBand band)
		{
			this.band = band;
		}

		protected void elementAdded(JRElement element)
		{
			if (band.getHeight() < element.getHeight() + element.getY())
			{
				band.setHeight(element.getHeight() + element.getY());
			}
		}
		
		JRDesignElementGroup getRowElementGroup(int rowLevel)
		{
			int rowCount = rowGroups.size();
			if (rowLevel >= rowCount)
			{
				for (int level = rowCount; level <= rowLevel; ++level)
				{
					JRDesignElementGroup group = new JRDesignElementGroup();
					band.addElementGroup(group);
					rowGroups.add(group);
				}
			}
			
			return rowGroups.get(rowLevel);
		}
	}

	protected abstract class ReportBandCreator implements ColumnVisitor<Void>
	{
		final ReportBandInfo bandInfo;
		final FillColumn fillColumn;
		int xOffset;
		int yOffset;
		int level;
		
		public ReportBandCreator(ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			this.bandInfo = bandInfo;
			this.fillColumn = fillColumn;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.level = level;
		}

		protected boolean isEmpty(Cell cell)
		{
			return cell == null;
		}
		
		public Void visitColumn(Column column)
		{
			Cell cell = columnCell(column);
			
			if (!isEmpty(cell))
			{
				int rowSpan = cell.getRowSpan() == null ? 1 : cell.getRowSpan();
				int rowLevel = level + rowSpan - 1;
				JRDesignElementGroup elementGroup = bandInfo.getRowElementGroup(rowLevel);
				
				JRElement cellElement = createColumnCell(column, elementGroup, cell);
				elementGroup.addElement(cellElement);
				bandInfo.elementAdded(cellElement);
				
				yOffset += cell.getHeight();
			}
			
			xOffset += column.getWidth();
			
			return null;
		}

		protected abstract Cell columnCell(Column column);
		
		protected JRElement createColumnCell(Column column, JRElementGroup parentGroup, Cell cell)
		{
			return createColumnCell(column, parentGroup, cell, false);
		}
		
		protected JRElement createColumnCell(Column column, JRElementGroup parentGroup, Cell cell, boolean forceFrame)
		{
			return createCell(parentGroup, cell, 
					column.getWidth(), fillColumn.getWidth(), 
					xOffset, yOffset, column.hashCode(), forceFrame);
		}
		
		public Void visitColumnGroup(ColumnGroup columnGroup)
		{
			Cell cell = columnGroupCell(columnGroup);
			int cellHeight = 0;
			int sublevel = level;
			if (cell != null)
			{
				int rowSpan = cell.getRowSpan() == null ? 1 : cell.getRowSpan();
				int rowLevel = level + rowSpan - 1;
				JRDesignElementGroup elementGroup = bandInfo.getRowElementGroup(rowLevel);
				
				JRElement cellElement = createCell(elementGroup, cell, 
						columnGroup.getWidth(), fillColumn.getWidth(), 
						xOffset, yOffset, null, false);
				elementGroup.addElement(cellElement);
				bandInfo.elementAdded(cellElement);
				
				cellHeight = cell.getHeight();
				sublevel += rowSpan;
			}
			
			for (FillColumn subcolumn : fillColumn.getSubcolumns())
			{
				ReportBandCreator subVisitor = createSubVisitor(subcolumn, 
						xOffset, yOffset + cellHeight, sublevel);
				subVisitor.visit();
				xOffset = subVisitor.xOffset;
			}
			
			return null;
		}

		protected abstract Cell columnGroupCell(ColumnGroup group);
		
		protected abstract ReportBandCreator createSubVisitor(FillColumn subcolumn, 
				int xOffset, int yOffset, int subLevel);
		
		public void visit()
		{
			fillColumn.getTableColumn().visitColumn(this);
		}
	}
	
	protected abstract class ReverseReportBandCreator extends ReportBandCreator
	{
		public ReverseReportBandCreator(ReportBandInfo bandInfo,
				FillColumn fillColumn, int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
		}

		@Override
		public Void visitColumnGroup(ColumnGroup columnGroup)
		{
			Cell cell = columnGroupCell(columnGroup);
			int rowSpan;
			if (cell == null)
			{
				rowSpan = 0;
			}
			else if (cell.getRowSpan() == null)
			{
				rowSpan = 1;
			}
			else
			{
				rowSpan = cell.getRowSpan();
			}
			
			int origXOffset = xOffset;
			int origYOffset = yOffset;
			
			for (FillColumn subcolumn : fillColumn.getSubcolumns())
			{
				ReportBandCreator subVisitor = createSubVisitor(subcolumn, 
						xOffset, origYOffset, level + rowSpan);
				subVisitor.visit();
				xOffset = subVisitor.xOffset;
				if (subVisitor.yOffset > yOffset)
				{
					yOffset = subVisitor.yOffset;
				}
			}
			
			if (cell != null)
			{
				int rowLevel = level + rowSpan - 1;
				JRDesignElementGroup elementGroup = bandInfo.getRowElementGroup(rowLevel);
				
				JRElement cellElement = createCell(elementGroup, cell, 
						columnGroup.getWidth(), fillColumn.getWidth(), 
						origXOffset, yOffset, null, false);
				elementGroup.addElement(cellElement);
				bandInfo.elementAdded(cellElement);
				
				yOffset += cell.getHeight();
			}
			
			return null;
		}
	}
	
	protected class DetailBandCreator extends ReportBandCreator
	{

		public DetailBandCreator(ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
		}
		
		@Override
		protected Cell columnCell(Column column)
		{
			return column.getDetailCell();
		}

		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return null;
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new DetailBandCreator(bandInfo, subcolumn, xOffset, yOffset, sublevel);
		}

		@Override
		protected boolean isEmpty(Cell cell)
		{
			if (super.isEmpty(cell))
			{
				return true;
			}
			
			// also threat zero height cells as empty.
			// FIXME only doing this for the detail cells to minimize impact, it should apply to all cells
			List<JRChild> children = cell.getChildren();
			return cell.getHeight() == 0 && (children == null || children.isEmpty());
		}
	}
	
	protected JRBand createDetailBand(List<FillColumn> fillColumns)
	{
		final JRDesignBand detailBand = new JRDesignBand();
		detailBand.setSplitType(SplitTypeEnum.PREVENT);
		
		ReportBandInfo bandInfo = new ReportBandInfo(detailBand);
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			DetailBandCreator subVisitor = new DetailBandCreator(
					bandInfo, subcolumn, xOffset, 0, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		return detailBand;
	}
	
	protected class ColumnHeaderCreator extends ReportBandCreator
	{
		private Map<Integer, JRPropertiesMap> headerBaseProperties;
		private final AtomicBoolean firstColumn;// we need a mutable boolean reference
		
		public ColumnHeaderCreator(ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level, 
				Map<Integer, JRPropertiesMap> headerBaseProperties, AtomicBoolean firstColumn)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
			this.headerBaseProperties = headerBaseProperties;
			this.firstColumn = firstColumn;
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getColumnHeader();
		}

		@Override
		protected JRDesignFrame createColumnCell(Column column, JRElementGroup parentGroup, Cell cell)
		{
			JRDesignFrame frame = (JRDesignFrame) createColumnCell(column, parentGroup, cell, true);
			addHeaderToolbarElement(column, frame, TableUtil.getColumnDetailTextElement(column));
			return frame;
		}

		protected JRExpression getColumnHeaderLabelExpression(Cell header)
		{
			List<JRChild> detailElements = header == null ? null : header.getChildren();
			// only consider cells with a single text fields
			if (detailElements == null || detailElements.size() != 1)
			{
				return null;
			}

			JRChild detailElement = detailElements.get(0);
			if (detailElement instanceof JRTextField)
			{
				return ((JRTextField) detailElement).getExpression();
			}

			if (detailElement instanceof JRStaticText)
			{
				return createBuiltinExpression(new ConstantBuiltinExpression(((JRStaticText)detailElement).getText()));
			}
			
			return null;
		}

		protected void addHeaderToolbarElement(Column column, JRDesignFrame frame, JRTextField sortTextField)
		{
			int columnIndex = TableUtil.getColumnIndex(column, table);
			boolean interactiveColumn = columnInteractivityMapping.get(column);
			
			if (sortTextField != null && interactiveColumn)
			{
				Cell header = column.getColumnHeader();
				
				JRDesignGenericElement genericElement = new JRDesignGenericElement(header.getDefaultStyleProvider());
				
				genericElement.setGenericType(HeaderToolbarElement.ELEMENT_TYPE);
				genericElement.setPositionType(net.sf.jasperreports.engine.type.PositionTypeEnum.FIX_RELATIVE_TO_TOP);
				genericElement.setX(0);
				genericElement.setY(0);
				// TODO lucianc setting to 1 for now; we can't set to frame size as we might not know the padding
				genericElement.setHeight(1);
				genericElement.setWidth(1);
				genericElement.setMode(ModeEnum.TRANSPARENT);
				genericElement.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
				
				String fieldOrVariableName = null;
				SortFieldTypeEnum columnType = null;
				FilterTypesEnum filterType = null;
				String suffix = "";
				
				if (column.getPropertiesMap().containsProperty(PROPERTY_COLUMN_FIELD))
				{
					fieldOrVariableName = column.getPropertiesMap().getProperty(PROPERTY_COLUMN_FIELD);
					columnType = SortFieldTypeEnum.FIELD;
					JRField field = getField(fieldOrVariableName);
					if (field != null) 
					{
						filterType = HeaderToolbarElementUtils.getFilterType(field.getValueClass());
					} else 
					{
						throw new JRRuntimeException("Could not find field '" + fieldOrVariableName + "'");
					}
				} else if (column.getPropertiesMap().containsProperty(PROPERTY_COLUMN_VARIABLE))
				{
					fieldOrVariableName = column.getPropertiesMap().getProperty(PROPERTY_COLUMN_VARIABLE);
					columnType = SortFieldTypeEnum.VARIABLE;
					JRVariable variable = getVariable(fieldOrVariableName);
					if (variable != null)
					{
						filterType = HeaderToolbarElementUtils.getFilterType(variable.getValueClass());
					} else
					{
						throw new JRRuntimeException("Could not find variable '" + fieldOrVariableName + "'");
					}
				} else if (TableUtil.hasSingleChunkExpression(sortTextField))
				{
					JRExpressionChunk sortExpression = sortTextField.getExpression().getChunks()[0];
					fieldOrVariableName = sortExpression.getText();
					
					switch (sortExpression.getType())
					{
					case JRExpressionChunk.TYPE_FIELD:
						columnType = SortFieldTypeEnum.FIELD;
						JRField field = getField(fieldOrVariableName);
						filterType = HeaderToolbarElementUtils.getFilterType(field.getValueClass());
						break;
						
					case JRExpressionChunk.TYPE_VARIABLE:
						columnType = SortFieldTypeEnum.VARIABLE;
						JRVariable variable = getVariable(fieldOrVariableName);
						filterType = HeaderToolbarElementUtils.getFilterType(variable.getValueClass());
						break;
						
					default:
						// never
						throw new JRRuntimeException("Unrecognized filter expression type " + sortExpression.getType());
					}	
				}
				
				boolean isSortable = propertiesUtil.getBooleanProperty(column.getPropertiesMap(), PROPERTY_COLUMN_SORTABLE, true) && fieldOrVariableName != null;
				boolean isFilterable = propertiesUtil.getBooleanProperty(column.getPropertiesMap(), PROPERTY_COLUMN_FILTERABLE, true) && fieldOrVariableName != null && TableUtil.isFilterable(sortTextField);
				boolean isConditionallyFormattable = propertiesUtil.getBooleanProperty(column.getPropertiesMap(), PROPERTY_COLUMN_CONDITIONALLY_FORMATTABLE, true) && fieldOrVariableName != null && TableUtil.isFilterable(sortTextField);
				
				if (isSortable)
				{	// column is sortable
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_CAN_SORT, Boolean.TRUE.toString());
					
					//FIXMEJIVE consider moving in separate method
					JRSortField[] sortFields = TableReport.this.mainDataset.getSortFields();
					if (sortFields != null)
					{
						for(JRSortField sortField : sortFields)
						{
							if (
								sortField.getName().equals(fieldOrVariableName)
								&& sortField.getType() == columnType
								)
							{
								suffix += 
									"" 
									+ (sortField.getOrderValue() == SortOrderEnum.ASCENDING 
										? propertiesUtil.getProperty(PROPERTY_UP_ARROW_CHAR)
										: (sortField.getOrderValue() == SortOrderEnum.DESCENDING 
											? propertiesUtil.getProperty(PROPERTY_DOWN_ARROW_CHAR)
											: ""));
							}
						}
					}
					
				} else
				{	// column is not sortable
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_CAN_SORT, Boolean.FALSE.toString());
				}
				
				if (isFilterable)
				{	// column is filterable
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_CAN_FILTER, Boolean.TRUE.toString());
					
					JasperReportsContext jasperReportsContext = fillContext.getFiller().getJasperReportsContext();
					String serializedFilters = TableReport.this.mainDataset.getPropertiesMap().getProperty(FilterCommand.DATASET_FILTER_PROPERTY);
					if (serializedFilters != null)
					{
						List<? extends DatasetFilter> existingFilters = JacksonUtil.getInstance(jasperReportsContext).loadList(serializedFilters, FieldFilter.class);
						if (existingFilters != null)
						{
							List<FieldFilter> fieldFilters = new ArrayList<FieldFilter>();
							SortElementHtmlHandler.getFieldFilters(new CompositeDatasetFilter(existingFilters), fieldFilters, fieldOrVariableName);
							if (fieldFilters.size() > 0)
							{
								suffix += "" + propertiesUtil.getProperty(PROPERTY_FILTER_CHAR);
							}
						}
					}
					
				} else
				{	// column is not filterable
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_CAN_FILTER, Boolean.FALSE.toString());
					
				}
				
				if (isConditionallyFormattable)
				{	// column is conditionally formattable
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_CAN_FORMAT_CONDITIONALLY, Boolean.TRUE.toString());
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_FIELD_OR_VARIABLE_NAME, fieldOrVariableName);
				} else
				{	// column is not conditionally formattable
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_CAN_FORMAT_CONDITIONALLY, Boolean.FALSE.toString());
				}
				
				if (suffix.length() > 0)
				{
					IconLabelComponent iconLabelComponent = addIconLabelComponent(column, frame);
					
					if (iconLabelComponent != null) {
						((JRDesignTextField)iconLabelComponent.getIconTextField()).setExpression(createBuiltinExpression(new ConstantBuiltinExpression(suffix)));
					}
					
//					HeaderLabelBuiltinExpression evaluator = HeaderLabelUtil.alterHeaderLabel(frame, " " + suffix);
//					if (evaluator != null)
//					{
//						builtinEvaluators.put(evaluator.getExpression(), evaluator);
//					}
				}

				if (isSortable || isFilterable || isConditionallyFormattable) {
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_TYPE, columnType.getName());
				}
				
				if (filterType != null && (isFilterable || isConditionallyFormattable))
				{
					genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_FILTER_TYPE, filterType.getName());
				}
				
				String columnName = fieldOrVariableName != null ? fieldOrVariableName : String.valueOf(columnIndex);
				String columnUuid = column.getUUID().toString();//columnName + "_" + column.hashCode();
				String cellId = columnName + "_" + column.hashCode();
				
				if (firstColumn.compareAndSet(false, true)) {
					// only setting on the first column to save memory
					//FIXME a cleaner approach would be to set these another single generic element 
					addColumnLabelParameters(genericElement, table);
				}
	
				genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_UUID, columnUuid);
				genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_INDEX, Integer.toString(columnIndex));
	
				genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_NAME, columnName);
				genericElement.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_TABLE_UUID, fillContext.getComponentElement().getUUID().toString());
				addElementParameter(genericElement, HeaderToolbarElement.PARAMETER_COLUMN_LABEL, getColumnHeaderLabelExpression(header));
	
				frame.getPropertiesMap().setProperty(JRHtmlExporter.PROPERTY_HTML_CLASS, "jrcolHeader" + (interactiveColumn ? " interactiveElement" : ""));
				frame.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_UUID, columnUuid);
				frame.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_TABLE_UUID, fillContext.getComponentElement().getUUID().toString());
				frame.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_INDEX, String.valueOf(columnIndex));

				String cellIdFixedPart = cellId + "_";
				TableIndexProperties cellIdProperties = new TableIndexProperties(
						HeaderToolbarElement.PROPERTY_CELL_ID, cellIdFixedPart);
				tableIndexProperties.add(cellIdProperties);
				assert frame.getPropertiesMap().getBaseProperties() == null;
				frame.getPropertiesMap().setBaseProperties(cellIdProperties.getPropertiesMap());

				String classFixedPart = TableReport.HTML_CLASS_CELL + " " + TableReport.HTML_CLASS_CELL_PREFIX + cellIdFixedPart;
				TableIndexProperties columnClassProperties = new TableIndexProperties(
						JRHtmlExporter.PROPERTY_HTML_CLASS, classFixedPart);
				tableIndexProperties.add(columnClassProperties);
				headerBaseProperties.put(column.hashCode(), columnClassProperties.getPropertiesMap());
				
				frame.addElement(0, genericElement);
			} else 
			{
				String columnName = String.valueOf(columnIndex);
				String cellId = columnName + "_" + column.hashCode();
				
				frame.getPropertiesMap().setProperty(JRHtmlExporter.PROPERTY_HTML_CLASS, "jrcolHeader");
				frame.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_UUID, column.getUUID().toString());
				frame.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_CELL_ID, cellId);
				frame.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_TABLE_UUID, fillContext.getComponentElement().getUUID().toString());
				frame.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_INDEX, String.valueOf(columnIndex));
			}
		}
		
		protected IconLabelComponent addIconLabelComponent(Column column, JRDesignFrame frame)
		{
			JRBaseTextElement headerTextElement = (JRBaseTextElement)frame.getChildren().get(0);
			if (headerTextElement != null) {
				Cell header = column.getColumnHeader();
				
				JRDesignComponentElement designComponent = new JRDesignComponentElement(header.getDefaultStyleProvider());
				designComponent.setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components", null, "iconLabel"));
				designComponent.setX(headerTextElement.getX());
				designComponent.setY(headerTextElement.getY());
				designComponent.setHeight(headerTextElement.getHeight());
				designComponent.setWidth(headerTextElement.getWidth());
				designComponent.setMode(headerTextElement.getModeValue());
				designComponent.setForecolor(headerTextElement.getForecolor());
				designComponent.setBackcolor(headerTextElement.getBackcolor());
				
				IconLabelComponent iconLabelComponent = new IconLabelComponent(header.getDefaultStyleProvider());
				iconLabelComponent.setHorizontalAlign(HorizontalAlignEnum.LEFT);
				iconLabelComponent.setVerticalAlign(VerticalAlignEnum.MIDDLE);
				iconLabelComponent.setIconPosition(IconPositionEnum.END);
				iconLabelComponent.setLabelFill(ContainerFillEnum.NONE);
				
				JRDesignTextField labelTextField = new JRDesignTextField();
				labelTextField.setStretchWithOverflow(true);
				labelTextField.setX(0);
				labelTextField.setY(0);
				labelTextField.setWidth(1);
				labelTextField.setHeight(headerTextElement.getHeight());
				labelTextField.setMode(headerTextElement.getModeValue());
				labelTextField.setFontSize(headerTextElement.getFontSize());
				labelTextField.setFontName(headerTextElement.getFontName());
				labelTextField.setForecolor(headerTextElement.getForecolor());
				labelTextField.setBackcolor(headerTextElement.getBackcolor());
				
				if (headerTextElement instanceof JRTextField) {
					labelTextField.setExpression(((JRTextField) headerTextElement).getExpression());
				} else if (headerTextElement instanceof JRStaticText) {
					labelTextField.setExpression(createBuiltinExpression(new ConstantBuiltinExpression(((JRStaticText)headerTextElement).getText())));
				}

				iconLabelComponent.setLabelTextField(labelTextField);
				
				JRDesignTextField iconTextField = new JRDesignTextField();
				iconTextField.setStretchWithOverflow(true);
				iconTextField.setX(0);
				iconTextField.setY(0);
				iconTextField.setWidth(1);
				iconTextField.setHeight(1);
//				iconTextField.setHeight(headerTextElement.getHeight());
				iconTextField.setMode(headerTextElement.getModeValue());
				iconTextField.setFontSize(headerTextElement.getFontSize()-1);
				iconTextField.setForecolor(headerTextElement.getForecolor());
				iconTextField.setBackcolor(headerTextElement.getBackcolor());
				
				iconLabelComponent.setIconTextField(iconTextField);
				
				designComponent.setComponent(iconLabelComponent);
				
				frame.getChildren().remove(0);
				frame.getChildren().add(designComponent);
				
				return iconLabelComponent;
			}
			return null;
		}
		
		protected void addElementParameter(JRDesignGenericElement element, String name, Object value)
		{
			JRDesignGenericElementParameter param = new JRDesignGenericElementParameter();
			param.setName(name);
			
			JRDesignExpression valueExpression = createBuiltinExpression(
					new ConstantBuiltinExpression(value));
			param.setValueExpression(valueExpression);
			
			element.addParameter(param);
		}

		protected void addElementParameter(JRDesignGenericElement element, String name, JRExpression expression)
		{
			JRDesignGenericElementParameter param = new JRDesignGenericElementParameter();
			param.setName(name);
			param.setValueExpression(expression);
			element.addParameter(param);
		}
		
		protected void addColumnLabelParameters(JRDesignGenericElement element, TableComponent table) {
			List<BaseColumn> columns = TableUtil.getAllColumns(table);
			for(int i = 0, ln = columns.size(); i < ln; i++) {
				BaseColumn column = columns.get(i);
				JRExpression columnHeaderExpression = getColumnHeaderLabelExpression(column.getColumnHeader());
				boolean interactiveColumn = columnInteractivityMapping.get(column) && (TableUtil.getColumnDetailTextElement((Column)column) != null);
				String paramName = HeaderToolbarElement.PARAM_COLUMN_LABEL_PREFIX + i + "|" + column.getUUID().toString() + "|" + interactiveColumn;
				addElementParameter(element, paramName, columnHeaderExpression);
			}
		}
		
		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getColumnHeader();
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new ColumnHeaderCreator(bandInfo, subcolumn, xOffset, yOffset, sublevel, 
					headerHtmlBaseProperties, firstColumn);
		}
	}

	protected JRDesignBand createColumnHeader(List<FillColumn> fillColumns)
	{
		JRDesignBand columnHeader = new JRDesignBand();
		columnHeader.setSplitType(SplitTypeEnum.PREVENT);
		
		ReportBandInfo bandInfo = new ReportBandInfo(columnHeader);
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			ColumnHeaderCreator subVisitor = new ColumnHeaderCreator(
					bandInfo, subcolumn, xOffset, 0, 0, headerHtmlBaseProperties,
					new AtomicBoolean());
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (columnHeader.getHeight() == 0)
		{
			columnHeader = null;
		}
		return columnHeader;
	}
	
	protected class PageFooterCreator extends ReverseReportBandCreator
	{
		public PageFooterCreator(ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getColumnFooter();
		}

		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getColumnFooter();
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new PageFooterCreator(bandInfo, subcolumn, xOffset, yOffset, sublevel);
		}
	}

	protected JRDesignBand createPageFooter(List<FillColumn> fillColumns)
	{
		JRDesignBand pageFooter = new JRDesignBand();
		pageFooter.setSplitType(SplitTypeEnum.PREVENT);
		
		ReportBandInfo bandInfo = new ReportBandInfo(pageFooter);
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			PageFooterCreator subVisitor = new PageFooterCreator(
					bandInfo, subcolumn, xOffset, 0, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (pageFooter.getHeight() == 0)
		{
			pageFooter = null;
		}
		return pageFooter;
	}
	
	protected class TitleCreator extends ReportBandCreator
	{
		public TitleCreator(ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getTableHeader();
		}

		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getTableHeader();
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new TitleCreator(bandInfo, subcolumn, xOffset, yOffset, sublevel);
		}
	}

	protected JRDesignBand createTitle(List<FillColumn> fillColumns)
	{
		JRDesignBand title = new JRDesignBand();
		title.setSplitType(SplitTypeEnum.PREVENT);
		
		ReportBandInfo bandInfo = new ReportBandInfo(title);
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			TitleCreator subVisitor = new TitleCreator(
					bandInfo, subcolumn, xOffset, 0, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (title.getHeight() == 0)
		{
			title = null;
		}
		return title;
	}
	
	protected class SummaryCreator extends ReverseReportBandCreator
	{
		public SummaryCreator(ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getTableFooter();
		}

		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getTableFooter();
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new SummaryCreator(bandInfo, subcolumn, xOffset, yOffset, sublevel);
		}
	}

	protected JRDesignBand createSummary(List<FillColumn> fillColumns)
	{
		JRDesignBand summary = new JRDesignBand();
		summary.setSplitType(SplitTypeEnum.PREVENT);
		
		ReportBandInfo bandInfo = new ReportBandInfo(summary);
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			SummaryCreator subVisitor = new SummaryCreator(
					bandInfo, subcolumn, xOffset, 0, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (summary.getHeight() == 0)
		{
			summary = null;
		}
		return summary;
	}
	
	protected class GroupHeaderCreator extends ReportBandCreator
	{
		private final String groupName;
		
		public GroupHeaderCreator(String groupName,
				ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
			
			this.groupName = groupName;
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getGroupHeader(groupName);
		}

		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getGroupHeader(groupName);
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new GroupHeaderCreator(groupName, 
					bandInfo, subcolumn, xOffset, yOffset, sublevel);
		}
	}

	protected JRBand createGroupHeader(String groupName, List<FillColumn> fillColumns)
	{
		JRDesignBand header = new JRDesignBand();
		header.setSplitType(SplitTypeEnum.PREVENT);
		
		ReportBandInfo bandInfo = new ReportBandInfo(header);
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			GroupHeaderCreator subVisitor = new GroupHeaderCreator(groupName,
					bandInfo, subcolumn, xOffset, 0, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (header.getHeight() == 0)
		{
			header = null;
		}
		return header;
	}
	
	protected class GroupFooterCreator extends ReverseReportBandCreator
	{
		private final String groupName;
		
		public GroupFooterCreator(String groupName,
				ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
			
			this.groupName = groupName;
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getGroupFooter(groupName);
		}

		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getGroupFooter(groupName);
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new GroupFooterCreator(groupName, 
					bandInfo, subcolumn, xOffset, yOffset, sublevel);
		}
	}

	protected JRBand createGroupFooter(String groupName, List<FillColumn> fillColumns)
	{
		JRDesignBand footer = new JRDesignBand();
		footer.setSplitType(SplitTypeEnum.PREVENT);
		
		ReportBandInfo bandInfo = new ReportBandInfo(footer);
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			GroupFooterCreator subVisitor = new GroupFooterCreator(groupName,
					bandInfo, subcolumn, xOffset, 0, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (footer.getHeight() == 0)
		{
			footer = null;
		}
		return footer;
	}
	
	private void setGroupBands(List<FillColumn> fillColumns)
	{
		TableReportGroup[] groups = mainDataset.getTableGroups();
		if (groups != null)
		{
			for (TableReportGroup group : groups)
			{
				JRBand header = createGroupHeader(group.getName(), fillColumns);
				if (header != null)
				{
					group.setGroupHeader(header);
				}
				JRBand footer = createGroupFooter(group.getName(), fillColumns);
				if (footer != null)
				{
					group.setGroupFooter(footer);
				}
			}
		}
		
		
	}

	protected static final String TABLE_SCRIPTLET_NAME = "__Table";
	
	protected class SummaryGroupFooterPrintWhenEvaluator implements BuiltinExpressionEvaluator
	{

		private JRValueParameter tableScriptletParam;
		private TableReportScriptlet tableScriptlet;
		
		public void init(Map<String, JRFillParameter> parametersMap, 
				Map<String, JRFillField> fieldsMap, 
				Map<String, JRFillVariable> variablesMap, 
				WhenResourceMissingTypeEnum resourceMissingType)
				throws JRException
		{
			tableScriptletParam = parametersMap.get(TABLE_SCRIPTLET_NAME 
					+ JRScriptlet.SCRIPTLET_PARAMETER_NAME_SUFFIX);
		}

		protected void ensureValue()
		{
			if (tableScriptlet == null)
			{
				tableScriptlet = (TableReportScriptlet) tableScriptletParam.getValue();
			}
		}
		
		public Object evaluate(DatasetExpressionEvaluator evaluator) throws JRExpressionEvalException
		{
			ensureValue();
			return tableScriptlet.hasDetailOnPage();
		}

		public Object evaluateEstimated(DatasetExpressionEvaluator evaluator) throws JRExpressionEvalException
		{
			ensureValue();
			return tableScriptlet.hasDetailOnPage();
		}

		public Object evaluateOld(DatasetExpressionEvaluator evaluator) throws JRExpressionEvalException
		{
			ensureValue();
			return tableScriptlet.hasDetailOnPage();
		}
	}
	
	protected int computeTableWidth(List<FillColumn> fillColumns)
	{
		int width = 0;
		for (FillColumn column : fillColumns)
		{
			width += column.getWidth();
		}
		return width;
	}
	
	protected void addSummaryGroup(List<FillColumn> fillColumns)
	{
		JRDesignGroup summaryGroup = new JRDesignGroup();
		summaryGroup.setName(SUMMARY_GROUP_NAME);//TODO check for uniqueness
		
		JRDesignBand groupFooter = new JRDesignBand();
		groupFooter.setSplitType(SplitTypeEnum.PREVENT);
		groupFooter.setHeight(pageFooter.getHeight());
		
		// we need to put everything in a frame so that we can tell the frame
		// not to print when there are no detail bands on the current page
		// 
		// we can't do that directly to the band since its print when expression
		// is evaluated too soon
		JRDesignFrame footerFrame = new JRDesignFrame();
		footerFrame.setX(0);
		footerFrame.setY(0);
		footerFrame.setWidth(computeTableWidth(fillColumns));
		footerFrame.setHeight(pageFooter.getHeight());
		footerFrame.getLineBox().setPadding(0);
		footerFrame.getLineBox().getPen().setLineWidth(0f);
		footerFrame.setRemoveLineWhenBlank(true);
		
		JRDesignExpression footerPrintWhen = createBuiltinExpression(new SummaryGroupFooterPrintWhenEvaluator());
		footerFrame.setPrintWhenExpression(footerPrintWhen);
		
		// clone the contents of the page footer in the frame
		List<JRChild> footerElements = pageFooter.getChildren();
		for (Iterator<JRChild> iterator = footerElements.iterator(); iterator
				.hasNext();)
		{
			JRChild child = iterator.next();
			JRChild childClone = (JRChild) child.clone(footerFrame);
			if (childClone instanceof JRElement)
			{
				footerFrame.addElement((JRElement) childClone);
			}
			else if (childClone instanceof JRElementGroup)
			{
				footerFrame.addElementGroup((JRElementGroup) childClone);
			}
			else
			{
				throw new JRRuntimeException("Uknown child type " 
						+ childClone.getClass().getName());
			}
		}
		
		groupFooter.addElement(footerFrame);
		((JRDesignSection) summaryGroup.getGroupFooterSection()).addBand(groupFooter);
		
		mainDataset.addScriptlet(TABLE_SCRIPTLET_NAME, TableReportScriptlet.class);
		mainDataset.addFirstGroup(summaryGroup);
	}

	protected JRElement createCell(JRElementGroup parentGroup, Cell cell, 
			int originalWidth, int width, 
			int x, int y, Integer columnHashCode, 
			boolean forceFrame)
	{
		if (!forceFrame)
		{
			JRElement cellElement = createCellElement(parentGroup, cell, originalWidth, width, x, y, columnHashCode);
			if (cellElement != null)
			{
				return cellElement;
			}
		}
		
		JRDesignFrame frame = new JRDesignFrame(this);
		frame.setElementGroup(parentGroup);
		frame.setX(x);
		frame.setY(y);
		frame.setWidth(width);
		frame.setHeight(cell.getHeight());
		frame.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
		
		frame.setStyle(cell.getStyle());
		frame.setStyleNameReference(cell.getStyleNameReference());
		frame.copyBox(cell.getLineBox());

		if (columnHashCode != null && headerHtmlBaseProperties.get(columnHashCode) != null) {
			JRPropertiesMap propertiesMap = frame.getPropertiesMap();
			assert propertiesMap != null && propertiesMap.getBaseProperties() == null;
			propertiesMap.setBaseProperties(headerHtmlBaseProperties.get(columnHashCode));
		}
		// not transferring cell properties to the frame/element for now
		
		for (Iterator<JRChild> it = cell.getChildren().iterator(); it.hasNext();)
		{
			JRChild child = it.next();
			if (child instanceof JRElement)
			{
				JRElement element = (JRElement) child;
				// clone the element in order to set the frame as group
				element = (JRElement) element.clone(frame);
				if (width != originalWidth)
				{
					scaleCellElement(element, originalWidth, width);
					
					if (element instanceof JRElementGroup)//i.e. frame
					{
						JRElementGroup elementGroup = (JRElementGroup) element;
						for (JRElement subelement : elementGroup.getElements())
						{
							scaleCellElement(subelement, originalWidth, width);
						}
					}
				}
				frame.addElement(element);
			}
			else if (child instanceof JRElementGroup)
			{
				JRElementGroup elementGroup = (JRElementGroup) child;
				// clone the elements in order to set the frame as group
				elementGroup = (JRElementGroup) elementGroup.clone(frame);
				frame.addElementGroup(elementGroup);
				
				if (width != originalWidth)
				{
					for (JRElement element : elementGroup.getElements())
					{
						scaleCellElement(element, originalWidth, width);
					}
				}
			}
			else
			{
				throw new JRRuntimeException("Unknown JRChild type " + child.getClass().getName());
			}
		}
		
		return frame;
	}
	
	protected JRElement createCellElement(JRElementGroup elementGroup, Cell cell, 
			int originalWidth, int width, 
			int x, int y, Integer columnHashCode)
	{
		List<JRChild> children = cell.getChildren();
		if (children.size() != 1)
		{
			// several children
			return null;
		}
		
		JRChild child = children.get(0);
		if (!(child instanceof JRStaticText || child instanceof JRTextField))
		{
			// only doing this for texts for now
			return null;
		}
		
		JRElement element = (JRElement) child;
		if (element.getX() != 0 
				|| element.getY() != 0 
				|| element.getWidth() != originalWidth 
				|| element.getHeight() != cell.getHeight())
		{
			// the element is not as large as the cell
			return null;
		}
		
		ModeEnum elementMode = StyleUtil.instance().resolveElementMode(element);
		if (elementMode == null || elementMode == ModeEnum.TRANSPARENT)
		{
			// if the element is not necessarily opaque, check that the cell is transparent
			ModeEnum cellMode = StyleUtil.instance().resolveMode(cell);
			if (cellMode != ModeEnum.TRANSPARENT)
			{
				// the cell is not necessarily transparent, we need the frame
				return null;
			}
		}
		
		if (StyleUtil.instance().hasBox(cell))
		{
			// the cell has box, we need the frame
			return null;
		}
		
		JRElement cellElement = element.clone(elementGroup, y);
		cellElement.setX(x);
		cellElement.setWidth(width);
		cellElement.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);

		if (columnHashCode != null && headerHtmlBaseProperties.get(columnHashCode) != null)
		{
			JRPropertiesMap propertiesMap = cellElement.getPropertiesMap();
			assert propertiesMap != null && propertiesMap.getBaseProperties() == null;
			propertiesMap.setBaseProperties(headerHtmlBaseProperties.get(columnHashCode));
		}
		
		if (width != originalWidth)
		{
			scaleCellElement(element, originalWidth, width);
		}
		
		return cellElement;
	}

	protected void scaleCellElement(JRElement element, Integer cellWidth,
			int scaledCellWidth)
	{
		int scaledWidth = Math.round(((float) element.getWidth() * scaledCellWidth) / cellWidth);
		element.setWidth(scaledWidth);
	}
	
	protected JRSection wrapBand(JRBand band, JROrigin origin)
	{
		JRDesignSection section = new JRDesignSection(origin);
		section.addBand(band);
		return section;
	}
	
	public JRBand getBackground()
	{
		return null;
	}

	public int getBottomMargin()
	{
		return 0;
	}

	public int getColumnCount()
	{
		return 1;
	}

	public JRBand getColumnFooter()
	{
		return null;
	}

	public JRBand getColumnHeader()
	{
		return columnHeader;
	}

	public int getColumnSpacing()
	{
		return 0;
	}

	public int getColumnWidth()
	{
		return fillContext.getComponentElement().getWidth();
	}

	public JRDataset[] getDatasets()
	{
		return parentReport.getDatasets();
	}

	@Deprecated
	public JRBand getDetail()
	{
		// see #getDetailSection()
		return null;
	}

	public JRSection getDetailSection()
	{
		return detail;
	}

	public JRField[] getFields()
	{
		return mainDataset.getFields();
	}

	protected JRField getField(String name)
	{
		JRField found = null;
		for (JRField field : getFields())
		{
			if (name.equals(field.getName()))
			{
				found = field;
				break;
			}
		}
		return found;
	}

	public String getFormatFactoryClass()
	{
		return parentReport.getFormatFactoryClass();
	}

	public JRGroup[] getGroups()
	{
		return mainDataset.getGroups();
	}

	public String[] getImports()
	{
		return parentReport.getImports();
	}

	public String getLanguage()
	{
		return parentReport.getLanguage();
	}

	public JRBand getLastPageFooter()
	{
		return lastPageFooter;
	}

	public int getLeftMargin()
	{
		return 0;
	}

	public JRDataset getMainDataset()
	{
		return mainDataset;
	}

	public String getName()
	{
		return mainDataset.getName();
	}

	public JRBand getNoData()
	{
		return null;
	}

	public OrientationEnum getOrientationValue()
	{
		return OrientationEnum.PORTRAIT;
	}

	public JRBand getPageFooter()
	{
		return pageFooter;
	}

	public JRBand getPageHeader()
	{
		return null;
	}

	public int getPageHeight()
	{
		return parentReport.getPageHeight();
	}

	public int getPageWidth()
	{
		return fillContext.getComponentElement().getWidth();
	}

	public JRParameter[] getParameters()
	{
		return mainDataset.getParameters();
	}

	public PrintOrderEnum getPrintOrderValue()
	{
		return PrintOrderEnum.VERTICAL;
	}

	public RunDirectionEnum getColumnDirection()
	{
		return RunDirectionEnum.LTR;
	}

	public String getProperty(String name)
	{
		return mainDataset.getPropertiesMap().getProperty(name);	
	}

	public String[] getPropertyNames()
	{
		return mainDataset.getPropertiesMap().getPropertyNames();
	}

	public JRQuery getQuery()
	{
		return mainDataset.getQuery();
	}

	public String getResourceBundle()
	{
		return mainDataset.getResourceBundle();
	}

	public int getRightMargin()
	{
		return 0;
	}

	public String getScriptletClass()
	{
		return mainDataset.getScriptletClass();
	}

	public JRScriptlet[] getScriptlets()
	{
		return mainDataset.getScriptlets();
	}

	public JRSortField[] getSortFields()
	{
		return mainDataset.getSortFields();
	}

	public JRStyle[] getStyles()
	{
		return parentReport.getStyles();
	}

	public JRBand getSummary()
	{
		return summary;
	}

	public JRReportTemplate[] getTemplates()
	{
		// the parent report's templates are always used for the subreport
		return null;
	}

	public JRBand getTitle()
	{
		return title;
	}

	public int getTopMargin()
	{
		return 0;
	}

	public JRVariable[] getVariables()
	{
		return mainDataset.getVariables();
	}

	protected JRVariable getVariable(String name)
	{
		JRVariable found = null;
		for (JRVariable var : getVariables())
		{
			if (name.equals(var.getName()))
			{
				found = var;
				break;
			}
		}
		return found;
	}
	
	public WhenNoDataTypeEnum getWhenNoDataTypeValue()
	{
		WhenNoDataTypeEnum whenNoDataType = WhenNoDataTypeEnum.NO_PAGES;
		if (table.getWhenNoDataType() != null)
		{
			switch (table.getWhenNoDataType())
			{
				case ALL_SECTIONS_NO_DETAIL :
				{
					whenNoDataType = WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL;
					break;
				}
				case BLANK :
				default :
				{
					whenNoDataType = WhenNoDataTypeEnum.NO_PAGES;
					break;
				}
			}
		}
		return whenNoDataType;
	}

	public WhenResourceMissingTypeEnum getWhenResourceMissingTypeValue()
	{
		return mainDataset.getWhenResourceMissingTypeValue();
	}

	public boolean isFloatColumnFooter()
	{
		return true;
	}

	public boolean isIgnorePagination()
	{
		return false;
	}

	public boolean isSummaryNewPage()
	{
		return false;
	}

	public boolean isSummaryWithPageHeaderAndFooter()
	{
		return false;
	}

	public boolean isTitleNewPage()
	{
		return false;
	}

	public void removeProperty(String name)
	{
		throw new UnsupportedOperationException();
	}

	public void setProperty(String name, String value)
	{
		throw new UnsupportedOperationException();
	}

	public void setWhenNoDataType(WhenNoDataTypeEnum whenNoDataType)
	{
		throw new UnsupportedOperationException();
	}

	public void setWhenResourceMissingType(
			WhenResourceMissingTypeEnum whenResourceMissingType)
	{
		throw new UnsupportedOperationException();
	}

	public JRStyle getDefaultStyle()
	{
		return parentReport.getDefaultStyle();
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

	public JRPropertiesMap getPropertiesMap()
	{
		return mainDataset.getPropertiesMap();
	}

	public boolean hasProperties()
	{
		return mainDataset.hasProperties();
	}

	@Override
	public UUID getUUID()
	{
		return mainDataset.getUUID();
	}

	public void setTableInstanceIndex(int instanceIndex)
	{
		for (TableIndexProperties properties : tableIndexProperties)
		{
			properties.setTableInstanceIndex(instanceIndex);
		}
	}

	// creates a JRPropertiesMap instance that is used as base properties for table elements.
	// on each table instantiation, a property in the base instance changes its value and the
	// value propagates to the print elements created by the table.
	protected static class TableIndexProperties
	{
		private final String propertyName;
		private final String classFixedPart;
		private JRPropertiesMap propertiesMap;
		
		public TableIndexProperties(String propertyName, String classFixedPart)
		{
			this.propertyName = propertyName;
			this.classFixedPart = classFixedPart;
			
			this.propertiesMap = new JRPropertiesMap();
		}

		public JRPropertiesMap getPropertiesMap()
		{
			return propertiesMap;
		}

		public void setTableInstanceIndex(int instanceIndex)
		{
			propertiesMap.setProperty(propertyName, classFixedPart + instanceIndex);
		}
	}
}
