package net.sf.jasperreports.components.headertoolbar.actions;

import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.web.commands.Command;

public class ResizeColumnCommand implements Command 
{
	private StandardBaseColumn column;
	private int oldWidth;
	private int width;
	

	public ResizeColumnCommand(StandardBaseColumn column, int width) 
	{
		this.column = column;
		this.width = width;
		this.oldWidth = column.getWidth();
	}

	public void execute() 
	{
		column.setWidth(width);
	}
	
	public void undo() 
	{
		column.setWidth(oldWidth);
	}

	public void redo() 
	{
		execute();
	}
}
