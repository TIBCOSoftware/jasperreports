package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.commands.CommandStack;

public class HideUnhideColumnsCommand implements Command {
	
	private StandardTable table;
	private HideUnhideColumnData columnData;
	private CommandStack individualResizeCommandStack;
	
	public HideUnhideColumnsCommand(StandardTable table, HideUnhideColumnData columnData) {
		this.table = table;
		this.columnData = columnData;
		individualResizeCommandStack = new CommandStack();
	}


	public void execute() {
		List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
		int[] columnIndexes = columnData.getColumnIndexes();
		
		if (columnIndexes != null) {
			for(int colIndex: columnIndexes){
				individualResizeCommandStack.execute(new HideUnhideColumnCommand((StandardColumn)tableColumns.get(colIndex), columnData.getHide()));
			}
		}
	}


	public void undo() {
		individualResizeCommandStack.undoAll();
	}


	public void redo() {
		individualResizeCommandStack.redoAll();
	}

}
