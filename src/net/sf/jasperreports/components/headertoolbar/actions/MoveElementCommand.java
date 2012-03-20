package net.sf.jasperreports.components.headertoolbar.actions;

import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.web.commands.Command;

public class MoveElementCommand implements Command 
{
	
	private int x;
	private int oldX;
	private JRBaseElement receiver;
	
	public MoveElementCommand(JRBaseElement receiver, int x) {
		this.receiver = receiver;
		this.x = x;
		this.oldX = receiver.getX();
	}

	public void execute() {
		receiver.setX(x);
	}		
	
	public void undo() {
		receiver.setX(oldX);
	}

	public void redo() {
		execute();
	}

}
