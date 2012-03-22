package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.web.commands.Command;

public class SimpleMoveColumnCommand implements Command 
{
	
	private List<BaseColumn> columns;
	private BaseColumn column;
	private int srcColIndex;
	private int destColIndex;
	
	
	public SimpleMoveColumnCommand(List<BaseColumn> columns, BaseColumn column, int srcColIndex, int destColIndex) 
	{
		this.columns = columns;
		this.column = column;
		this.srcColIndex = srcColIndex;
		this.destColIndex = destColIndex;
	}

	
	public void execute() 
	{
		moveColumns(srcColIndex, destColIndex);
	}
	
	private void moveColumns(int srcSiblingColIndex, int destSiblingColIndex) 
	{
		columns.remove(srcSiblingColIndex);
		if (destSiblingColIndex == columns.size()) 
		{
			columns.add(column);
		} 
		else 
		{
			columns.add(destSiblingColIndex, column);
		}
	}

	public void undo() 
	{
		moveColumns(destColIndex, srcColIndex);
	}

	public void redo() 
	{
		execute();
	}

}
