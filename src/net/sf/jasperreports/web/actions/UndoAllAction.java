package net.sf.jasperreports.web.actions;

import net.sf.jasperreports.web.commands.CommandStack;

public class UndoAllAction extends AbstractAction {

	public UndoAllAction() {
	}

	public String getName() {
		return "undo_all_action";
	}

	public void performAction() {
		// obtain command stack
		CommandStack commandStack = getCommandStack();

		// execute command
		commandStack.undoAll();
	}

}
