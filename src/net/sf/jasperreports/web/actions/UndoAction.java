package net.sf.jasperreports.web.actions;

import net.sf.jasperreports.web.commands.CommandStack;

public class UndoAction extends AbstractAction {

	public UndoAction() {
	}

	public String getName() {
		return "undo_action";
	}

	public void performAction() {
		// obtain command stack
		CommandStack commandStack = getCommandStack();

		// execute command
		commandStack.undo();
	}

}
