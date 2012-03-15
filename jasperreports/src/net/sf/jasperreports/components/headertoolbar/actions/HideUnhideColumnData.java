package net.sf.jasperreports.components.headertoolbar.actions;


public class HideUnhideColumnData 
{
	private String tableUuid;
	private int[] columnIndexes;
	private boolean hide;

	public HideUnhideColumnData() {
	}

	public String getTableUuid() {
		return tableUuid;
	}

	public void setTableUuid(String tableUuid) {
		this.tableUuid = tableUuid;
	}

	public int[] getColumnIndexes() {
		return columnIndexes;
	}

	public void setColumnIndexes(int[] columnIndexes) {
		this.columnIndexes = columnIndexes;
	}
	
	public boolean getHide() {
		return hide;
	}
	
	public void setHide(boolean hide) {
		this.hide = hide;
	}

}
