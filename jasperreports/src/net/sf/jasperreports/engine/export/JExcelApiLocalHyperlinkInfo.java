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
package net.sf.jasperreports.engine.export;

import jxl.write.WritableSheet;

/**
 * @deprecated To be removed.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JExcelApiLocalHyperlinkInfo 
{

	private String description;
	private int col, row, lastCol, lastRow;
	private WritableSheet sheet;
	
	public JExcelApiLocalHyperlinkInfo() {
	}
	
	public JExcelApiLocalHyperlinkInfo(
			String description,
			WritableSheet sheet, 
			int col, int row, int lastCol, int lastRow) {
		this.description = description;
		this.sheet = sheet; 
		this.col = col;
		this.row = row;
		this.lastCol = lastCol;
		this.lastRow = lastRow;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getLastCol() {
		return lastCol;
	}
	public void setLastCol(int lastCol) {
		this.lastCol = lastCol;
	}
	public int getLastRow() {
		return lastRow;
	}
	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}
	public WritableSheet getSheet() {
		return sheet;
	}
	public void setSheet(WritableSheet sheet) {
		this.sheet = sheet;
	}
}
