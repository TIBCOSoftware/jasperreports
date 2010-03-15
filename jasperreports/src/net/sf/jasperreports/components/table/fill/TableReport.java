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
	private final JRDataset mainDataset;
	private final JRSection detail;
	private final JRBand title;
	
	public TableReport(FillContext fillContext, JRDataset mainDataset, 
			List<FillColumn> fillColumns)
	{
		this.fillContext = fillContext;
		this.parentReport = fillContext.getFiller().getJasperReport();
		this.mainDataset = mainDataset;
		
		this.detail = wrapBand(createDetailBand(fillColumns), new JROrigin(BandTypeEnum.DETAIL));
		this.title = createTitleBand(fillColumns);
	}

	protected class DetailBandVisitor implements ColumnVisitor<Void>
	{
		final JRDesignBand detailBand;
		final FillColumn fillColumn;
		int xOffset = 0;
		
		public DetailBandVisitor(JRDesignBand detailBand,
				FillColumn fillColumn, int xOffset)
		{
			this.detailBand = detailBand;
			this.fillColumn = fillColumn;
			this.xOffset = xOffset;
		}

		public Void visitColumn(Column column)
		{
			Cell detailCell = column.getDetailCell();
			
			if (detailBand.getHeight() < detailCell.getHeight())
			{
				detailBand.setHeight(detailCell.getHeight());
			}

			JRDesignFrame cellFrame = createCellFrame(detailCell, xOffset, 0);
			detailBand.addElement(cellFrame);
			
			xOffset += detailCell.getWidth();
			
			return null;
		}

		public Void visitColumnGroup(ColumnGroup columnGroup)
		{
			for (FillColumn subcolumn : fillColumn.getSubcolumns())
			{
				DetailBandVisitor subVisitor = new DetailBandVisitor(
						detailBand, subcolumn, xOffset);
				subVisitor.visit();
				xOffset = subVisitor.xOffset;
			}
			return null;
		}
		
		public void visit()
		{
			fillColumn.getTableColumn().visitColumn(this);
		}
	}
	
	protected JRBand createDetailBand(List<FillColumn> fillColumns)
	{
		final JRDesignBand detailBand = new JRDesignBand();
		detailBand.setSplitType(SplitTypeEnum.PREVENT);
		
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			DetailBandVisitor subVisitor = new DetailBandVisitor(
					detailBand, subcolumn, xOffset);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		return detailBand;
	}
	
	protected class TitleBandVisitor implements ColumnVisitor<Void>
	{
		final JRDesignBand titleBand;
		final FillColumn fillColumn;
		int xOffset = 0;
		int yOffset = 0;
		
		public TitleBandVisitor(JRDesignBand titleBand, FillColumn fillColumn,
				int xOffset, int yOffset)
		{
			this.titleBand = titleBand;
			this.fillColumn = fillColumn;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}

		public Void visitColumn(Column column)
		{
			Cell header = column.getHeader();
			
			if (titleBand.getHeight() < header.getHeight() + yOffset)
			{
				titleBand.setHeight(header.getHeight() + yOffset);
			}

			JRDesignFrame cellFrame = createCellFrame(header, xOffset, yOffset);
			titleBand.addElement(cellFrame);
			
			xOffset += header.getWidth();
			
			return null;
		}

		public Void visitColumnGroup(ColumnGroup columnGroup)
		{
			Cell header = columnGroup.getHeader();
			JRDesignFrame cellFrame = createCellFrame(header, xOffset, yOffset);
			titleBand.addElement(cellFrame);
			
			for (FillColumn subcolumn : fillColumn.getSubcolumns())
			{
				TitleBandVisitor subVisitor = new TitleBandVisitor(titleBand, 
						subcolumn, xOffset, yOffset + header.getHeight());
				subVisitor.visit();
				xOffset = subVisitor.xOffset;
			}
			
			return null;
		}
		
		public void visit()
		{
			fillColumn.getTableColumn().visitColumn(this);
		}
	}

	protected JRBand createTitleBand(List<FillColumn> fillColumns)
	{
		final JRDesignBand titleBand = new JRDesignBand();
		titleBand.setSplitType(SplitTypeEnum.PREVENT);
		
		//TODO element groups
		int xOffset = 0;
		for (FillColumn subcolumn : fillColumns)
		{
			TitleBandVisitor subVisitor = new TitleBandVisitor(
					titleBand, subcolumn, xOffset, 0);
			subVisitor.visit();
			xOffset = subVisitor.xOffset;
		}
		
		return titleBand;
	}

	protected JRDesignFrame createCellFrame(Cell cell, int x, int y)
	{
		JRDesignFrame frame = new JRDesignFrame(this);
		frame.setX(x);
		frame.setY(y);
		frame.setWidth(cell.getWidth());
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
				frame.addElement((JRElement) child);
			}
			else if (child instanceof JRElementGroup)
			{
				frame.addElementGroup((JRElementGroup) child);
			}
			else
			{
				throw new JRRuntimeException("Unknown JRChild type " + child.getClass().getName());
			}
		}
		
		return frame;
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
		return null;
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
		return JRReport.ORIENTATION_PORTRAIT;
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
		return JRReport.PRINT_ORDER_VERTICAL;
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
		return null;
	}

	public JRReportTemplate[] getTemplates()
	{
		// TODO Auto-generated method stub
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
		return JRReport.WHEN_NO_DATA_TYPE_NO_PAGES;
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
