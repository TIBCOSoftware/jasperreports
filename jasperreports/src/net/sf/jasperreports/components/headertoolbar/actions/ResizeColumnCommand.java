package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.commands.CommandStack;

public class ResizeColumnCommand implements Command 
{
	
	private StandardTable table;
	private JRDesignComponentElement componentElement;
	private ResizeColumnData resizeColumnData;
	private CommandStack individualResizeCommandStack;
	
	private StandardColumn modColumn;
	private int oldModColumnWidth;

	private StandardColumnGroup modColumnGroup;
	private int oldModColumnGroupWidth;
	private int newModColumnGroupWidth;
	

	public ResizeColumnCommand(JRDesignComponentElement componentElement, ResizeColumnData resizeColumnData) 
	{
		this.componentElement = componentElement;
		this.table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
		this.resizeColumnData = resizeColumnData;
		this.individualResizeCommandStack = new CommandStack();
	}

	public void execute() 
	{
		List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
		
		int modIndex = resizeColumnData.getColumnIndex();
		
		modColumn = (StandardColumn) tableColumns.get(modIndex);
		oldModColumnWidth = modColumn.getWidth();
		
		int deltaWidth = resizeColumnData.getWidth() - modColumn.getWidth();

		
		
		
		
		List<ColumnGroupInfo> parentColumnGroups = new ColumnUtil(resizeColumnData.getColumnIndex()).getParentColumnGroups(table.getColumns());
		
		for(ColumnGroupInfo colGroupInfo : parentColumnGroups)
		{
			resizeColumn(colGroupInfo.column, deltaWidth);
		}

	
	
		// resize the column
//		modColumn.setWidth(resizeColumnData.getWidth());
		resizeColumn(modColumn, deltaWidth);
	}
	
//	public void execute() 
//	{
//		
//		List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
//		
//		int modIndex = resizeColumnData.getColumnIndex();
//		
//		modColumn = (StandardColumn) tableColumns.get(modIndex);
//		oldModColumnWidth = modColumn.getWidth();
//		
//		
//		int deltaWidth = resizeColumnData.getWidth() - modColumn.getWidth();
//		
//		// resize the component that contains the table
//		individualResizeCommandStack.execute(new ResizeElementCommand(componentElement, componentElement.getWidth() + deltaWidth));
//		
//		// resize the column group that contains modColumn
//		modColumnGroup = (StandardColumnGroup) getColumnGroupForColumn(modColumn, table.getColumns());
//		if (modColumnGroup != null) {
//			oldModColumnGroupWidth = modColumnGroup.getWidth();
//			newModColumnGroupWidth = oldModColumnGroupWidth + deltaWidth;
//			
//			modColumnGroup.setWidth(newModColumnGroupWidth);
//			resizeColumnGroupHF(modColumnGroup, deltaWidth);
//		}
//
//		// resize the column
//		modColumn.setWidth(resizeColumnData.getWidth());
//		resizeColumn(modColumn, deltaWidth);
//		
//	}
	
	private void resizeColumn(BaseColumn column, int amount) {
		
		StandardBaseColumn standardBaseColumn = column instanceof StandardBaseColumn ? (StandardBaseColumn)column : null;
		if (standardBaseColumn != null)
		{
			standardBaseColumn.setWidth(standardBaseColumn.getWidth() + amount);
		}
		
		StandardColumn standardColumn = column instanceof StandardColumn ? (StandardColumn)column : null;
		if (standardColumn != null)
		{
			resizeChildren(standardColumn.getDetailCell(), amount);
		}

		resizeChildren(column.getTableHeader(), amount);
		resizeChildren(column.getColumnHeader(), amount);
		resizeChildren(column.getColumnFooter(), amount);
		resizeChildren(column.getTableFooter(), amount);

		for (GroupCell header: column.getGroupHeaders()) {
			resizeChildren(header.getCell(), amount);
		}

		for (GroupCell footer: column.getGroupFooters()) {
			resizeChildren(footer.getCell(), amount);
		}
	}

//	private void resizeColumn(StandardColumn column, int amount) {
//		resizeChildren(column.getTableHeader(), amount);
//		resizeChildren(column.getColumnHeader(), amount);
//		resizeChildren(column.getDetailCell(), amount);
//		resizeChildren(column.getColumnFooter(), amount);
//		resizeChildren(column.getTableFooter(), amount);
//
//		resizeColumnGroupHF(column, amount);
//	}

//	private void resizeColumnGroupHF(StandardBaseColumn column, int amount) {
//		for (GroupCell header: column.getGroupHeaders()) {
//			resizeChildren(header.getCell(), amount);
//		}
//
//		for (GroupCell footer: column.getGroupFooters()) {
//			resizeChildren(footer.getCell(), amount);
//		}
//	}
	
	private void resizeChildren(JRElementGroup elementGroup, int amount) {
		if (elementGroup != null) {
			for (JRChild child: elementGroup.getChildren()) {
				if (child instanceof JRBaseElement) {
					JRBaseElement be = (JRBaseElement) child;
					individualResizeCommandStack.execute(new ResizeElementCommand(be, be.getWidth() + amount));
				}
				if (child instanceof JRElementGroup) {
					JRElementGroup eg = (JRElementGroup) child;
					resizeChildren(eg, amount);
				}
			}
		}
	}
	
//moved in TableUtil	
//	private ColumnGroup getColumnGroupForColumn(BaseColumn column, List<BaseColumn> columns) {
//		for (BaseColumn bc: columns) {
//			if (bc instanceof ColumnGroup) {
//				ColumnGroup cg = (ColumnGroup) bc;
//				if (cg.getColumns().contains(column)) {
//					return cg;
//				} else {
//					return getColumnGroupForColumn(column, cg.getColumns());
//				}
//			}
//		}
//		return null;
//	}

	public void undo() 
	{
		modColumn.setWidth(oldModColumnWidth);
		
		if (modColumnGroup != null) {
			modColumnGroup.setWidth(oldModColumnGroupWidth);
		}
		
		individualResizeCommandStack.undoAll();
	}

	public void redo() 
	{
		modColumn.setWidth(resizeColumnData.getWidth());
		
		if (modColumnGroup != null) {
			modColumnGroup.setWidth(newModColumnGroupWidth);
		}
		
		individualResizeCommandStack.redoAll();
	}

	static public class ColumnUtil
	{
		private int colIndex;
		private int crtColIndex;
		private int crtColX;
		private List<ColumnGroupInfo> parentColumnGroups = new ArrayList<ColumnGroupInfo>();
		
		ColumnUtil(int colIndex)
		{
			this.colIndex = colIndex;
		}

		public List<ColumnGroupInfo> getParentColumnGroups(List<BaseColumn> columns) 
		{
			for(BaseColumn column : columns)
			{
				if (column instanceof ColumnGroup)
				{
					ColumnGroupInfo colGroupInfo = new ColumnGroupInfo();
					colGroupInfo.x = crtColX;
					colGroupInfo.colIndex = crtColIndex;
					colGroupInfo.column = column;
					parentColumnGroups.add(colGroupInfo);
					
					getParentColumnGroups(((ColumnGroup)column).getColumns());
				}
				else
				{
					if (colIndex == crtColIndex)
					{
						return parentColumnGroups;
					}
					
					crtColIndex++;
					crtColX += column.getWidth();
				}
			}
			
			return parentColumnGroups;
		}
	}

	static public class ColumnGroupInfo
	{
		public int x;
		public int colIndex;
		public BaseColumn column;
	}
}
