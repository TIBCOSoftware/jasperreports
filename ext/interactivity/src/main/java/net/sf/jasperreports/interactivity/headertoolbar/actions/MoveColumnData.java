/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.interactivity.headertoolbar.actions;

import net.sf.jasperreports.engine.JRConstants;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class MoveColumnData extends BaseColumnData {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private int columnToMoveIndex;
	private int columnToMoveNewIndex;
	
	public MoveColumnData() {
	}

	public int getColumnToMoveIndex() {
		return columnToMoveIndex;
	}

	public void setColumnToMoveIndex(int columnToMoveIndex) {
		this.columnToMoveIndex = columnToMoveIndex;
	}

	public int getColumnToMoveNewIndex() {
		return columnToMoveNewIndex;
	}

	public void setColumnToMoveNewIndex(int columnToMoveNewIndex) {
		this.columnToMoveNewIndex = columnToMoveNewIndex;
	}
	
}
