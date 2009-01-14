/**
 * 
 */
package net.sf.jasperreports.charts.themes;

import java.awt.Font;
import java.math.BigDecimal;

import net.sf.jasperreports.engine.JRFont;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class ChartThemesUtilities
{
	public static int getScale(double value)
	{
		return BigDecimal.valueOf(value).precision() - BigDecimal.valueOf(value).scale() - 1;
	}

	public static double getTruncatedValue(double value, int scale)
	{
		String newValue;
		String sign = value < 0 ? "-" : "";
		value = Math.abs(value);

		if(scale < 0)
		{
			value *= Math.pow(10.0, -scale);
			newValue = (String.valueOf(value) + "0000").substring(0,4);
		}
		else if(scale > 2)
		{
			newValue =  (String.valueOf(value / Math.pow(10.0, scale - 2)) + "000").substring(0,3);
		}
		else
		{
			newValue = String.valueOf(value);
			if(newValue.length() > 4)
				newValue = newValue.substring(0,4);
		}
		return Double.valueOf(sign + newValue).doubleValue();
	}

	public static double getScaledValue(double value, int scale)
	{
		if(scale < 0)
		{
			return value * Math.pow(10.0, -scale);
		}
		else if(scale > 2)
		{
			return value / Math.pow(10.0, scale-2);
		}

		return value;
	}
	
	public static int getAwtFontStyle(JRFont font, int defaultBoldStyle, int defaultItalicStyle)
	{
		if(font == null)
			return Font.PLAIN;
		
		int style = Font.PLAIN;
		if((font.isOwnBold() == null && defaultBoldStyle == Font.BOLD) ||
		(font.isOwnBold() != null && font.isOwnBold().booleanValue()))
		{
				style = Font.BOLD;
		}
		
		if((font.isOwnItalic() == null && defaultItalicStyle == Font.ITALIC) ||
		(font.isOwnItalic() != null && font.isOwnItalic().booleanValue()))
		{
			if(style == Font.BOLD)
				style |= Font.ITALIC;
			else
				style = Font.ITALIC;
		}
		return style;
	}
}
