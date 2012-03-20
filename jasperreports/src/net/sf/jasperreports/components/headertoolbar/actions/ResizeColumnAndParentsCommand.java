package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.commands.CommandStack;

public class ResizeColumnAndParentsCommand implements Command 
{
	
	private StandardTable table;
	//private JRDesignComponentElement componentElement;
	private ResizeColumnData resizeColumnData;
	private CommandStack individualResizeCommandStack;
	
//	private StandardColumn modColumn;
//	private int oldModColumnWidth;

//	private StandardColumnGroup modColumnGroup;
//	private int oldModColumnGroupWidth;
//	private int newModColumnGroupWidth;
	

	public ResizeColumnAndParentsCommand(JRDesignComponentElement componentElement, ResizeColumnData resizeColumnData) 
	{
		//this.componentElement = componentElement;
		this.table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
		this.resizeColumnData = resizeColumnData;
		this.individualResizeCommandStack = new CommandStack();
	}

	public void execute() 
	{
		List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
		
		int modIndex = resizeColumnData.getColumnIndex();
		
		StandardColumn modColumn = (StandardColumn) tableColumns.get(modIndex);
//		oldModColumnWidth = modColumn.getWidth();
		
		int deltaWidth = resizeColumnData.getWidth() - modColumn.getWidth();
		int startX = 0;
		List<BaseColumn> allColumns = TableUtil.getAllColumns(table);
		if (allColumns != null)
		{
			for (int i = 0; i <= modIndex; i++)
			{
				startX += allColumns.get(i).getWidth();
			}
		}
		
		
		List<ColumnGroupInfo> parentColumnGroups = new ColumnUtil(resizeColumnData.getColumnIndex()).getParentColumnGroups(table.getColumns());
		
		for(ColumnGroupInfo colGroupInfo : parentColumnGroups)
		{
			if (
				colGroupInfo.x <= startX
				&& startX <= colGroupInfo.x + colGroupInfo.column.getWidth()
				)
			{
				resizeColumn(colGroupInfo.column, startX - colGroupInfo.x, deltaWidth);
			}
		}

	
	
		// resize the column
//		modColumn.setWidth(resizeColumnData.getWidth());
		resizeColumn(modColumn, modColumn.getWidth(), deltaWidth);
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
	
	private void resizeColumn(BaseColumn column, int startX, int amount) 
	{
		if (amount < -startX)
		{
			amount = -startX;
		}
		
		StandardBaseColumn standardBaseColumn = column instanceof StandardBaseColumn ? (StandardBaseColumn)column : null;
		if (standardBaseColumn != null)
		{
			individualResizeCommandStack.execute(new ResizeColumnCommand(standardBaseColumn, standardBaseColumn.getWidth() + amount));
			//standardBaseColumn.setWidth(standardBaseColumn.getWidth() + amount);
		}
		
		StandardColumn standardColumn = column instanceof StandardColumn ? (StandardColumn)column : null;
		if (standardColumn != null)
		{
			resizeChildren(standardColumn.getDetailCell(), startX, amount);
		}

		resizeChildren(column.getTableHeader(), startX, amount);
		resizeChildren(column.getColumnHeader(), startX, amount);
		resizeChildren(column.getColumnFooter(), startX, amount);
		resizeChildren(column.getTableFooter(), startX, amount);

		for (GroupCell header: column.getGroupHeaders()) {
			resizeChildren(header.getCell(), startX, amount);
		}

		for (GroupCell footer: column.getGroupFooters()) {
			resizeChildren(footer.getCell(), startX, amount);
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
	
	private void resizeChildren(JRElementGroup elementGroup, int startX, int amount) {
		if (elementGroup != null) {
			for (JRChild child: elementGroup.getChildren()) {
				if (child instanceof JRBaseElement) 
				{
					JRBaseElement be = (JRBaseElement) child;

					int resizeAmount = 0;
					int moveAmount = 0;

					if (startX <= be.getX())
					{
						resizeAmount = 0;
						moveAmount = amount;
					}
					else if (startX <= be.getX() + be.getWidth())
					{
						if (amount < be.getX() - startX)
						{
							resizeAmount = be.getX() - startX;
							moveAmount = amount - resizeAmount;
						}
						else
						{
							resizeAmount = amount;
							moveAmount = amount - resizeAmount;
						}
					}
					else
					{
						if (startX + amount < be.getX() + be.getWidth())
						{
							if (startX + amount > be.getX())
							{
								resizeAmount = amount - (startX - (be.getX() + be.getWidth()));
								moveAmount = 0;
							}
							else
							{
								resizeAmount = - be.getWidth();
								moveAmount = amount - be.getWidth() - (startX - (be.getX() +  be.getWidth()));								
							}
						}
						else
						{
							resizeAmount = 0;
							moveAmount = 0;
						}
					}

					if (resizeAmount != 0)
					{
						individualResizeCommandStack.execute(new ResizeElementCommand(be, be.getWidth() + resizeAmount));
					}
					
					if (moveAmount != 0)
					{
						moveAmount = be.getX() + moveAmount < 0 ? -be.getX() : moveAmount;
						individualResizeCommandStack.execute(new MoveElementCommand(be, be.getX() + moveAmount));
					}
				}
				if (child instanceof JRElementGroup) {
					JRElementGroup eg = (JRElementGroup) child;
					resizeChildren(eg, startX, amount);
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
//		modColumn.setWidth(oldModColumnWidth);
//		
//		if (modColumnGroup != null) {
//			modColumnGroup.setWidth(oldModColumnGroupWidth);
//		}
		
		individualResizeCommandStack.undoAll();
	}

	public void redo() 
	{
//		modColumn.setWidth(resizeColumnData.getWidth());
//		
//		if (modColumnGroup != null) {
//			modColumnGroup.setWidth(newModColumnGroupWidth);
//		}
		
		individualResizeCommandStack.redoAll();
	}

	static public class ColumnUtil
	{
		private boolean found = false;
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
			Iterator<BaseColumn> it = columns.iterator();
			while(!found && it.hasNext())
			{
				BaseColumn column = it.next();
				if (column instanceof ColumnGroup)
				{
					ColumnGroupInfo colGroupInfo = new ColumnGroupInfo();
					colGroupInfo.x = crtColX;
					colGroupInfo.colIndex = crtColIndex;
					colGroupInfo.column = column;
					parentColumnGroups.add(colGroupInfo);
					
					getParentColumnGroups(((ColumnGroup)column).getColumns());
					
					if (!found)
					{
						parentColumnGroups.remove(parentColumnGroups.size() - 1);
					}
				}
				else
				{
					if (colIndex == crtColIndex)
					{
						found = true;
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
