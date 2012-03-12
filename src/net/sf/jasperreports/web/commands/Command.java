package net.sf.jasperreports.web.commands;


public interface Command {
	
	public void execute();
	
	public void undo();
	
	public void redo();
	
}
