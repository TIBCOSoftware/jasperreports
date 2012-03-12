package net.sf.jasperreports.web.commands;

import java.util.LinkedList;

public class CommandStack {
	private LinkedList<Command> commandStack = new LinkedList<Command>();
	private LinkedList<Command> redoStack = new LinkedList<Command>();

	public void execute(Command command) {
		command.execute();
		commandStack.addFirst(command);
		redoStack.clear();
	}

	public void undo() {
		if (commandStack.isEmpty()) {
			return;
		}
		Command command = commandStack.removeFirst();
		command.undo();
		redoStack.addFirst(command);
	}

	public void redo() {
		if (redoStack.isEmpty()) {
			return;
		}
		Command command = redoStack.removeFirst();
		command.redo();
		commandStack.addFirst(command);
	}

	public void undoAll() {
		if (commandStack.isEmpty()) {
			return;
		}
		while (!commandStack.isEmpty()) {
			undo();
		}
	}
	
	public void redoAll() {
		if (redoStack.isEmpty()) {
			return;
		}
		while (!redoStack.isEmpty()) {
			redo();
		}
	}
}
