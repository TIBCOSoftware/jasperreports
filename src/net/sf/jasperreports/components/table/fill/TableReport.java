/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.ColumnVisitor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
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

	private final FillContext fillContext;
	private final JasperReport parentReport;
	private final TableReportDataset mainDataset;
	private final JRSection detail;
	private final JRBand title;
	private final JRBand summary;
	private final JRBand columnHeader;
	private final JRBand columnFooter;
	
	public TableReport(FillContext fillContext, TableReportDataset mainDataset, 
			List<FillColumn> fillColumns)
	{
		this.fillContext = fillContext;
		this.parentReport = fillContext.getFiller().getJasperReport();
		this.mainDataset = mainDataset;
		
		this.detail = wrapBand(createDetailBand(fillColumns), new JROrigin(BandTypeEnum.DETAIL));
		this.title = createTitle(fillColumns);
		//TODO make table footers appear after column footers
		this.summary = createSummary(fillColumns); 
		this.columnHeader = createColumnHeader(fillColumns);
		this.columnFooter = createColumnFooter(fillColumns);
		setGroupBands(fillColumns);
	}

	protected abstract class ReportBandContents implements ColumnVisitor<Void>
	{
		final JRDesignBand band;
		final FillColumn fillColumn;
		int xOffset = 0;
		int yOffset = 0;
		
		public ReportBandContents(JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			this.band = band;
			this.fillColumn = fillColumn;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}

		public Void visitColumn(Column column)
		{
			Cell cell = columnCell(column);
			
			if (cell != null)
			{
				if (band.getHeight() < cell.getHeight() + yOffset)
				{
					band.setHeight(cell.getHeight() + yOffset);
				}

				JRDesignFrame cellFrame = createCellFrame(cell, 
						column.getWidth(), fillColumn.getWidth(), 
						xOffset, yOffset);
				band.addElement(cellFrame);
				
				yOffset += cell.getHeight();
			}
			
			xOffset += column.getWidth();
			
			return null;
		}

		protected abstract Cell columnCell(Column column);
		
		public Void visitColumnGroup(ColumnGroup columnGroup)
		{
			Cell cell = columnGroupCell(columnGroup);
			int cellHeight = 0;
			if (cell != null)
			{
				JRDesignFrame cellFrame = createCellFrame(cell, 
						columnGroup.getWidth(), fillColumn.getWidth(), 
						xOffset, yOffset);
				band.addElement(cellFrame);
				cellHeight = cell.getHeight();
			}
			
			for (FillColumn subcolumn : fillColumn.getSubcolumns())
			{
				ReportBandContents subVisitor = createSubVisitor(subcolumn, 
						xOffset, yOffset + cellHeight);
				subVisitor.visit();
				xOffset = subVisitor.xOffset;
			}
			
			return null;
		}

		protected abstract Cell columnGroupCell(ColumnGroup group);
		
		protected abstract ReportBandContents createSubVisitor(FillColumn subcolumn, 
				int xOffset, int yOffset);
		
		public void visit()
		{
			fillColumn.getTableColumn().visitColumn(this);
		}
	}
	
	protected abstract class ReverseReportBandContents extends ReportBandContents
	{
		public ReverseReportBandContents(JRDesignBand band,
				FillColumn fillColumn, int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
		}

		@Override
		public Void visitColumnGroup(ColumnGroup columnGroup)
		{
			int origXOffset = xOffset;
			int origYOffset = yOffset;
			
			for (FillColumn subcolumn : fillColumn.getSubcolumns())
			{
				ReportBandContents subVisitor = createSubVisitor(subcolumn, 
						xOffset, origYOffset);
				subVisitor.visit();
				xOffset = subVisitor.xOffset;
				if (subVisitor.yOffset > yOffset)
				{
					yOffset = subVisitor.yOffset;
				}
			}
			
			Cell cell = columnGroupCell(columnGroup);
			if (cell != null)
			{
				JRDesignFrame cellFrame = createCellFrame(cell, 
						columnGroup.getWidth(), fillColumn.getWidth(), 
						origXOffset, yOffset);
				band.addElement(cellFrame);
				yOffset += cell.getHeight();
			}
			
			return null;
		}
	}
	
	protected class DetailBandContents extends ReportBandContents
	{

		public DetailBandContents(JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
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
		protected ReportBandContents createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset)
		{
			return new DetailBandContents(band, subcolumn, xOffset, yOffset);
		}
	}
	
	protected JRBand createDetailBand(List<FillColumn> fillColumns)
	{
		final JRDesignBand detailBand = new JRDesignBand();
		detailBand.setSplitType(SplitTypeEnum.PREVENT);
		
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			DetailBandContents subVisitor = new DetailBandContents(
					detailBand, subcolumn, xOffset, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		return detailBand;
	}
	
	protected class ColumnHeaderContents extends ReportBandContents
	{
		public ColumnHeaderContents(JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
		}

		@Override
		protected Cell columnCell(Column column)
		{
			return column.getColumnHeader();
		}

		@Override
		protected Cell columnGroupCell(ColumnGroup group)
		{
			return group.getColumnHeader();
		}

		@Override
		protected ReportBandContents createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset)
		{
			return new ColumnHeaderContents(band, subcolumn, xOffset, yOffset);
		}
	}

	protected JRBand createColumnHeader(List<FillColumn> fillColumns)
	{
		JRDesignBand columnHeader = new JRDesignBand();
		columnHeader.setSplitType(SplitTypeEnum.PREVENT);
		
		//TODO element groups
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			ColumnHeaderContents subVisitor = new ColumnHeaderContents(
					columnHeader, subcolumn, xOffset, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (columnHeader.getHeight() == 0)
		{
			columnHeader = null;
		}
		return columnHeader;
	}
	
	protected class ColumnFooterContents extends ReverseReportBandContents
	{
		public ColumnFooterContents(JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
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
		protected ReportBandContents createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset)
		{
			return new ColumnFooterContents(band, subcolumn, xOffset, yOffset);
		}
	}

	protected JRBand createColumnFooter(List<FillColumn> fillColumns)
	{
		JRDesignBand columnFooter = new JRDesignBand();
		columnFooter.setSplitType(SplitTypeEnum.PREVENT);
		
		//TODO element groups
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			ColumnFooterContents subVisitor = new ColumnFooterContents(
					columnFooter, subcolumn, xOffset, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (columnFooter.getHeight() == 0)
		{
			columnFooter = null;
		}
		return columnFooter;
	}
	
	protected class TitleContents extends ReportBandContents
	{
		public TitleContents(JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
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
		protected ReportBandContents createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset)
		{
			return new TitleContents(band, subcolumn, xOffset, yOffset);
		}
	}

	protected JRBand createTitle(List<FillColumn> fillColumns)
	{
		JRDesignBand title = new JRDesignBand();
		title.setSplitType(SplitTypeEnum.PREVENT);
		
		//TODO element groups
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			TitleContents subVisitor = new TitleContents(
					title, subcolumn, xOffset, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (title.getHeight() == 0)
		{
			title = null;
		}
		return title;
	}
	
	protected class SummaryContents extends ReverseReportBandContents
	{
		public SummaryContents(JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
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
		protected ReportBandContents createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset)
		{
			return new SummaryContents(band, subcolumn, xOffset, yOffset);
		}
	}

	protected JRBand createSummary(List<FillColumn> fillColumns)
	{
		JRDesignBand summary = new JRDesignBand();
		summary.setSplitType(SplitTypeEnum.PREVENT);
		
		//TODO element groups
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			SummaryContents subVisitor = new SummaryContents(
					summary, subcolumn, xOffset, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (summary.getHeight() == 0)
		{
			summary = null;
		}
		return summary;
	}
	
	protected class GroupHeaderContents extends ReportBandContents
	{
		private final String groupName;
		
		public GroupHeaderContents(String groupName,
				JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
			
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
		protected ReportBandContents createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset)
		{
			return new GroupHeaderContents(groupName, 
					band, subcolumn, xOffset, yOffset);
		}
	}

	protected JRBand createGroupHeader(String groupName, List<FillColumn> fillColumns)
	{
		JRDesignBand header = new JRDesignBand();
		header.setSplitType(SplitTypeEnum.PREVENT);
		
		//TODO element groups
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			GroupHeaderContents subVisitor = new GroupHeaderContents(groupName,
					header, subcolumn, xOffset, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		if (header.getHeight() == 0)
		{
			header = null;
		}
		return header;
	}
	
	protected class GroupFooterContents extends ReverseReportBandContents
	{
		private final String groupName;
		
		public GroupFooterContents(String groupName,
				JRDesignBand band, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			super(band, fillColumn, xOffset, yOffset);
			
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
		protected ReportBandContents createSubVisitor(FillColumn subcolumn,
				int xOffset, int yOffset)
		{
			return new GroupFooterContents(groupName, 
					band, subcolumn, xOffset, yOffset);
		}
	}

	protected JRBand createGroupFooter(String groupName, List<FillColumn> fillColumns)
	{
		JRDesignBand footer = new JRDesignBand();
		footer.setSplitType(SplitTypeEnum.PREVENT);
		
		//TODO element groups
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			GroupFooterContents subVisitor = new GroupFooterContents(groupName,
					footer, subcolumn, xOffset, 0);
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
		
		for (Iterator it = cell.getChildren().iterator(); it.hasNext();)
		{
			JRChild child = (JRChild) it.next();
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
		return columnFooter;
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
		return null;
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

	@SuppressWarnings("deprecation")
	public JRReportFont[] getFonts()
	{
		return parentReport.getFonts();
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
		return null;
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

	@Deprecated
	public byte getOrientation()
	{
		return OrientationEnum.PORTRAIT.getValue();
	}

	public OrientationEnum getOrientationValue()
	{
		return OrientationEnum.PORTRAIT;
	}

	public JRBand getPageFooter()
	{
		return null;
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

	@Deprecated
	public byte getPrintOrder()
	{
		return PrintOrderEnum.VERTICAL.getValue();
	}

	public PrintOrderEnum getPrintOrderValue()
	{
		return PrintOrderEnum.VERTICAL;
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

	@Deprecated
	public byte getWhenNoDataType()
	{
		return WhenNoDataTypeEnum.NO_PAGES.getValue();
	}

	public WhenNoDataTypeEnum getWhenNoDataTypeValue()
	{
		return WhenNoDataTypeEnum.NO_PAGES;
	}

	@Deprecated
	public byte getWhenResourceMissingType()
	{
		return mainDataset.getWhenResourceMissingType();
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

	@Deprecated
	public void setWhenNoDataType(byte whenNoDataType)
	{
		throw new UnsupportedOperationException();
	}

	public void setWhenNoDataType(WhenNoDataTypeEnum whenNoDataType)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	public void setWhenResourceMissingType(byte whenResourceMissingType)
	{
		throw new UnsupportedOperationException();
	}

	public void setWhenResourceMissingType(
			WhenResourceMissingTypeEnum whenResourceMissingType)
	{
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("deprecation")
	public JRReportFont getDefaultFont()
	{
		return parentReport.getDefaultFont();
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
