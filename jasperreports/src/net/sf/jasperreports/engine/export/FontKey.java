package net.sf.jasperreports.engine.export;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id
 */
public class FontKey
{
	private String fontName;
	private boolean isBold;
	private boolean isItalic;

	public FontKey(String fontName, boolean bold, boolean italic)
	{
		this.fontName = fontName;
		isBold = bold;
		isItalic = italic;
	}

	public String getFontName()
	{
		return fontName;
	}

	public boolean isBold()
	{
		return isBold;
	}

	public boolean isItalic()
	{
		return isItalic;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final FontKey key = (FontKey) o;

		if (isBold != key.isBold) return false;
		if (isItalic != key.isItalic) return false;
		if (fontName != null ? !fontName.equals(key.fontName) : key.fontName != null) return false;

		return true;
	}

	public int hashCode()
	{
		int result;
		result = (fontName != null ? fontName.hashCode() : 0);
		result = 29 * result + (isBold ? 1 : 0);
		result = 29 * result + (isItalic ? 1 : 0);
		return result;
	}
}
