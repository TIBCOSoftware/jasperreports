/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.web.commands;

import java.util.LinkedList;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class CommandStack {
	private LinkedList<Command> commandStack = new LinkedList<Command>();
	private LinkedList<Command> redoStack = new LinkedList<Command>();

	public void execute(Command command) throws CommandException{
		try {
			command.execute();
		} catch (CommandException e) {
			command.undo();
			throw e;
		}
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
	
	public void clear()
	{
		commandStack.clear();
		redoStack.clear();
	}
	
	public int getExecutionStackSize() {
		return commandStack.size();
	}
}
