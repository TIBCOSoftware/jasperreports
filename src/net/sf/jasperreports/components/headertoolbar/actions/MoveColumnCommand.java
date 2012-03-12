package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.web.commands.Command;

public class MoveColumnCommand implements Command 
{
	
	private StandardTable table;
	private MoveColumnData moveColumnData;
	private MoveColumnData oldMoveColumnData;
	
	
	public MoveColumnCommand(StandardTable table, MoveColumnData moveColumnData) 
	{
		this.table = table;
		this.moveColumnData = moveColumnData;
	}

	
	public void execute() 
	{
		moveColumns(moveColumnData);
		oldMoveColumnData = new MoveColumnData();
		oldMoveColumnData.setColumnToMoveIndex(moveColumnData.getColumnToMoveNewIndex());
		oldMoveColumnData.setColumnToMoveNewIndex(moveColumnData.getColumnToMoveIndex());
	}
	
	private void moveColumns(MoveColumnData moveColumnData) {
		int colIndex = moveColumnData.getColumnToMoveIndex();
		int newColIndex = moveColumnData.getColumnToMoveNewIndex();
		
		List<BaseColumn> allColumns = TableUtil.getAllColumns(table);
		BaseColumn columnToMove = allColumns.get(colIndex);

		if (colIndex != newColIndex) {
			List<BaseColumn> groupColumns = getGroupColumnsForColumn(columnToMove, table.getColumns());
			
			if (groupColumns != null) {
				groupColumns.remove(colIndex);
				if (newColIndex == groupColumns.size()) {
					groupColumns.add(columnToMove);
				} else {
					groupColumns.add(newColIndex, columnToMove);
				}
			}
		}
	}

	private List<BaseColumn> getGroupColumnsForColumn(BaseColumn column, List<BaseColumn> columns) {
		for (BaseColumn bc: columns) {
			if (bc instanceof ColumnGroup) {
				ColumnGroup cg = (ColumnGroup) bc;
				if (cg.getColumns().contains(column)) {
					return cg.getColumns();
				} else {
					return getGroupColumnsForColumn(column, cg.getColumns());
				}
			}
			else {
				return columns;
			}
		}
		return null;
	}
	
	public void undo() 
	{
		moveColumns(oldMoveColumnData);
	}

	public void redo() 
	{
		moveColumns(moveColumnData);
	}

}
