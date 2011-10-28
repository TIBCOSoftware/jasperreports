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
package net.sf.jasperreports.components.table.fill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.sort.SortElement;
import net.sf.jasperreports.components.sort.SortElementUtils;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.ColumnVisitor;
import net.sf.jasperreports.components.table.TableComponent;
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
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElementParameter;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TableReport implements JRReport
{
	protected static final String SUMMARY_GROUP_NAME = "__SummaryGroup";
	
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
	
	public TableReport(
		FillContext fillContext, 
		TableComponent table, 
		TableReportDataset mainDataset, 
		List<FillColumn> fillColumns, 
		Map<JRExpression, BuiltinExpressionEvaluator> builtinEvaluators
		)
	{
		this.fillContext = fillContext;
		this.table = table;
		this.parentReport = fillContext.getFiller().getJasperReport();
		this.mainDataset = mainDataset;
		this.builtinEvaluators = builtinEvaluators;
		
		this.detail = wrapBand(createDetailBand(fillColumns), new JROrigin(BandTypeEnum.DETAIL));
		this.title = createTitle(fillColumns);
		this.summary = createSummary(fillColumns); 
		this.columnHeader = createColumnHeader(fillColumns);
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
		
		void addElement(int rowLevel, JRDesignElement element)
		{
			if (band.getHeight() < element.getHeight() + element.getY())
			{
				band.setHeight(element.getHeight() + element.getY());
			}
			
			JRDesignElementGroup rowGroup = getRowElementGroup(rowLevel);
			rowGroup.addElement(element);
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

		public Void visitColumn(Column column)
		{
			Cell cell = columnCell(column);
			
			if (cell != null)
			{
				JRDesignFrame cellFrame = createColumnCell(column, cell);
				int rowSpan = cell.getRowSpan() == null ? 1 : cell.getRowSpan();
				bandInfo.addElement(level + rowSpan - 1, cellFrame);
				
				yOffset += cell.getHeight();
			}
			
			xOffset += column.getWidth();
			
			return null;
		}

		protected abstract Cell columnCell(Column column);
		
		protected JRDesignFrame createColumnCell(Column column, Cell cell)
		{
			return createCellFrame(cell, 
					column.getWidth(), fillColumn.getWidth(), 
					xOffset, yOffset);
		}
		
		public Void visitColumnGroup(ColumnGroup columnGroup)
		{
			Cell cell = columnGroupCell(columnGroup);
			int cellHeight = 0;
			int sublevel = level;
			if (cell != null)
			{
				int rowSpan = cell.getRowSpan() == null ? 1 : cell.getRowSpan();
				JRDesignFrame cellFrame = createCellFrame(cell, 
						columnGroup.getWidth(), fillColumn.getWidth(), 
						xOffset, yOffset);
				bandInfo.addElement(level + rowSpan - 1, cellFrame);
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
				JRDesignFrame cellFrame = createCellFrame(cell, 
						columnGroup.getWidth(), fillColumn.getWidth(), 
						origXOffset, yOffset);
				bandInfo.addElement(level + rowSpan - 1, cellFrame);
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
		public ColumnHeaderCreator(ReportBandInfo bandInfo, FillColumn fillColumn,
				int xOffset, int yOffset, int level)
		{
			super(bandInfo, fillColumn, xOffset, yOffset, level);
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getColumnHeader();
		}

		@Override
		protected JRDesignFrame createColumnCell(Column column, Cell cell)
		{
			JRDesignFrame frame = super.createColumnCell(column, cell);
			JRTextField sortTextField = getColumnSortTextField(column);
			if (sortTextField != null)
			{
				addSortElement(column, frame, sortTextField.getExpression().getChunks()[0], sortTextField.getPattern());
			}
			return frame;
		}

		protected JRTextField getColumnSortTextField(Column column)
		{
			Cell detailCell = column.getDetailCell();
			List<JRChild> detailElements = detailCell == null ? null : detailCell.getChildren();
			// only consider cells with a single text fields
			if (detailElements == null || detailElements.size() != 1)
			{
				return null;
			}
			
			JRChild detailElement = detailElements.get(0);
			if (!(detailElement instanceof JRTextField))
			{
				return null;
			}
			
			// see if the text field expression is $F{..} of $V{..}
			JRTextField text = (JRTextField) detailElement;
			JRExpression textExpression = text.getExpression();
			JRExpressionChunk[] chunks = textExpression == null ? null : textExpression.getChunks();
			if (chunks == null || chunks.length != 1
					|| (chunks[0].getType() != JRExpressionChunk.TYPE_FIELD
					&& chunks[0].getType() != JRExpressionChunk.TYPE_VARIABLE))
			{
				return null;
			}
			
			// success
			return text;
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

		protected void addSortElement(Column column, JRDesignFrame frame,
				JRExpressionChunk sortExpression, String filterPattern)
		{
			Cell header = column.getColumnHeader();

			JRDesignGenericElement genericElement = new JRDesignGenericElement(header.getDefaultStyleProvider());

			genericElement.setGenericType(SortElement.SORT_ELEMENT_TYPE);
			genericElement.setPositionType(net.sf.jasperreports.engine.type.PositionTypeEnum.FIX_RELATIVE_TO_TOP);
			genericElement.setX(0);
			genericElement.setY(0);
			genericElement.setHeight(header.getHeight());
			genericElement.setWidth(fillColumn.getWidth());
			genericElement.setMode(ModeEnum.TRANSPARENT);
            genericElement.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			
			genericElement.getPropertiesMap().setProperty(SortElement.PROPERTY_DATASET_RUN, getName());
			
			String name = sortExpression.getText();
			SortFieldTypeEnum columnType;
			FilterTypesEnum filterType = null;
			
			switch (sortExpression.getType())
			{
			case JRExpressionChunk.TYPE_FIELD:
				columnType = SortFieldTypeEnum.FIELD;
				JRField field = getField(name);
				filterType = SortElementUtils.getFilterType(field.getValueClass());
				break;
				
			case JRExpressionChunk.TYPE_VARIABLE:
				columnType = SortFieldTypeEnum.VARIABLE;
				JRVariable variable = getVariable(name);
				filterType = SortElementUtils.getFilterType(variable.getValueClass());
				break;
				
			default:
				// never
				throw new JRRuntimeException("Unrecognized filter expression type " + sortExpression.getType());
			}
			
			addElementParameter(genericElement, SortElement.PARAMETER_SORT_COLUMN_NAME, name);
			addElementParameter(genericElement, SortElement.PARAMETER_SORT_COLUMN_LABEL, getColumnHeaderLabelExpression(header));
			addElementParameter(genericElement, SortElement.PARAMETER_SORT_COLUMN_TYPE, columnType.getName());
			addElementParameter(genericElement, SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN, "Right");
			addElementParameter(genericElement, SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN, "Middle");
			
			if (filterType != null)
			{
				genericElement.getPropertiesMap().setProperty(SortElement.PROPERTY_FILTER_TYPE, filterType.getName());
			}
			genericElement.getPropertiesMap().setProperty(SortElement.PROPERTY_FILTER_PATTERN, filterPattern);
			
			frame.addElement(genericElement);
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
		
		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getColumnHeader();
		}

		@Override
		protected ReportBandCreator createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset, int sublevel)
		{
			return new ColumnHeaderCreator(bandInfo, subcolumn, xOffset, yOffset, sublevel);
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
					bandInfo, subcolumn, xOffset, 0, 0);
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
		
		public Object evaluate() throws JRExpressionEvalException
		{
			ensureValue();
			return tableScriptlet.hasDetailOnPage();
		}

		public Object evaluateEstimated() throws JRExpressionEvalException
		{
			ensureValue();
			return tableScriptlet.hasDetailOnPage();
		}

		public Object evaluateOld() throws JRExpressionEvalException
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
	
	protected JRDesignFrame createCellFrame(Cell cell, 
			int originalWidth, int width, 
			int x, int y)
	{
		JRDesignFrame frame = new JRDesignFrame(this);
		frame.setX(x);
		frame.setY(y);
		frame.setWidth(width);
		frame.setHeight(cell.getHeight());
		frame.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
		
		frame.setStyle(cell.getStyle());
		frame.setStyleNameReference(cell.getStyleNameReference());
		frame.copyBox(cell.getLineBox());
		
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

}
