package net.sf.jasperreports.components.headertoolbar.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import net.sf.jasperreports.engine.JRRuntimeException;


public class EditColumnValueData 
{
	private String tableUuid;
	private int columnIndex;

	private String headingName;
	private String fontName;
	private int fontSize;
	private boolean fontBold;
	private boolean fontItalic;
	private boolean fontUnderline;
	private String fontColor;
	private String fontHAlign;
	private String formatPattern;
	
	public EditColumnValueData() {
	}

	public String getTableUuid() {
		return tableUuid;
	}

	public void setTableUuid(String tableUuid) {
		this.tableUuid = tableUuid;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getHeadingName() {
		return headingName;
	}

	public void setHeadingName(String headingName) {
		this.headingName = headingName;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public boolean getFontBold() {
		return fontBold;
	}

	public void setFontBold(boolean fontBold) {
		this.fontBold = fontBold;
	}

	public boolean getFontItalic() {
		return fontItalic;
	}

	public void setFontItalic(boolean fontItalic) {
		this.fontItalic = fontItalic;
	}

	public boolean getFontUnderline() {
		return fontUnderline;
	}

	public void setFontUnderline(boolean fontUnderline) {
		this.fontUnderline = fontUnderline;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getFontHAlign() {
		return fontHAlign;
	}

	public void setFontHAlign(String fontHAlign) {
		this.fontHAlign = fontHAlign;
	}

	public String getFormatPattern() {
		return urlDecode(formatPattern); // FIXMEJIVE don't do this here
	}

	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}
	
	private String urlDecode(String toDecode) {
		String result = null;
		if (toDecode != null) {
			try {
				result = URLDecoder.decode(toDecode, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new JRRuntimeException(e);
			}
		}
		return result;
	}
	

}
