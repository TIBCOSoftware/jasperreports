package net.sf.jasperreports.components.headertoolbar.actions;

import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.web.commands.Command;

public class ResizeElementCommand implements Command 
{
	
	private int width;
	private int oldWidth;
	private JRBaseElement receiver;
	
	public ResizeElementCommand(JRBaseElement receiver, int width) {
		this.receiver = receiver;
		this.width = width;
		this.oldWidth = receiver.getWidth();
	}

	public void execute() {
		receiver.setWidth(width);
	}		
	
	public void undo() {
		receiver.setWidth(oldWidth);
	}

	public void redo() {
		execute();
	}

}
