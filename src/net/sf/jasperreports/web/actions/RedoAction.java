package net.sf.jasperreports.web.actions;

import net.sf.jasperreports.web.commands.CommandStack;

public class RedoAction extends AbstractAction {

	public RedoAction() {
	}

	public String getName() {
		return "redo_action";
	}

	public void performAction() {
		// obtain command stack
		CommandStack commandStack = getCommandStack();

		// execute command
		commandStack.redo();
	}

}
