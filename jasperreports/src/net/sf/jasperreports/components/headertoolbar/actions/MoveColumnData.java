package net.sf.jasperreports.components.headertoolbar.actions;


public class MoveColumnData 
{
	private String uuid;
	private int columnToMoveIndex;
	private int columnToMoveNewIndex;
	
	public MoveColumnData() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
