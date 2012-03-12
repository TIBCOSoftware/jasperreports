package net.sf.jasperreports.components.headertoolbar.actions;


public class ResizeColumnData 
{
	private String uuid;
	private int columnIndex;
	private int width;
	private String direction;
	
	public ResizeColumnData() {
	}
	
	public ResizeColumnData(String uuid, int columnIndex, int width, String direction) {
		this.uuid = uuid;
		this.columnIndex = columnIndex;
		this.width = width;
		this.direction = direction;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	@Override
	public String toString() {
		return "uuid: " + uuid + "; columnIndex: " + columnIndex + "; width: " + width + "; direction: " + direction;
	}
}
