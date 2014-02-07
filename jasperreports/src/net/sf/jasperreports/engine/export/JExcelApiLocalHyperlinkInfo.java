package net.sf.jasperreports.engine.export;

import jxl.write.WritableSheet;

public class JExcelApiLocalHyperlinkInfo {

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
