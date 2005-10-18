package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.Map;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id
 */
public class JRStyleFactory extends JRBaseFactory
{
	private static final String ATTRIBUTE_name = "name";
	private static final String ATTRIBUTE_isDefault = "isDefault";
	private static final String ATTRIBUTE_mode = "mode";
	private static final String ATTRIBUTE_forecolor = "forecolor";
	private static final String ATTRIBUTE_backcolor = "backcolor";
	private static final String ATTRIBUTE_style = "style";

	private static final String ATTRIBUTE_pen = "pen";
	private static final String ATTRIBUTE_fill = "fill";

	private static final String ATTRIBUTE_radius = "radius";

	private static final String ATTRIBUTE_scaleImage = "scaleImage";
	// these are inherited by both images and texts.
	private static final String ATTRIBUTE_hAlign = "hAlign";
	private static final String ATTRIBUTE_vAlign = "vAlign";

	private static final String ATTRIBUTE_border = "border";
	private static final String ATTRIBUTE_borderColor = "borderColor";
	private static final String ATTRIBUTE_padding = "padding";
	private static final String ATTRIBUTE_topBorder = "topBorder";
	private static final String ATTRIBUTE_topBorderColor = "topBorderColor";
	private static final String ATTRIBUTE_topPadding = "topPadding";
	private static final String ATTRIBUTE_leftBorder = "leftBorder";
	private static final String ATTRIBUTE_leftBorderColor = "leftBorderColor";
	private static final String ATTRIBUTE_leftPadding = "leftPadding";
	private static final String ATTRIBUTE_bottomBorder = "bottomBorder";
	private static final String ATTRIBUTE_bottomBorderColor = "bottomBorderColor";
	private static final String ATTRIBUTE_bottomPadding = "bottomPadding";
	private static final String ATTRIBUTE_rightBorder = "rightBorder";
	private static final String ATTRIBUTE_rightBorderColor = "rightBorderColor";
	private static final String ATTRIBUTE_rightPadding = "rightPadding";

	private static final String ATTRIBUTE_rotation = "rotation";
	private static final String ATTRIBUTE_lineSpacing = "lineSpacing";
	private static final String ATTRIBUTE_isStyledText = "isStyledText";
	private static final String ATTRIBUTE_pattern = "pattern";
	private static final String ATTRIBUTE_isBlankWhenNull = "isBlankWhenNull";

	private static final String ATTRIBUTE_fontName = "fontName";
	private static final String ATTRIBUTE_isBold = "isBold";
	private static final String ATTRIBUTE_isItalic = "isItalic";
	private static final String ATTRIBUTE_isUnderline = "isUnderline";
	private static final String ATTRIBUTE_isStrikeThrough = "isStrikeThrough";
	private static final String ATTRIBUTE_fontSize = "fontSize";
	private static final String ATTRIBUTE_pdfFontName = "pdfFontName";
	private static final String ATTRIBUTE_pdfEncoding = "pdfEncoding";
	private static final String ATTRIBUTE_isPdfEmbedded = "isPdfEmbedded";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignStyle style = new JRDesignStyle();

		// get style name
		style.setName(atts.getValue(ATTRIBUTE_name));

		String isDefault = atts.getValue(ATTRIBUTE_isDefault);
		if (isDefault != null && isDefault.length() > 0)
		{
			style.setDefault(Boolean.valueOf(isDefault).booleanValue());
		}

		// get parent style
		if (atts.getValue(ATTRIBUTE_style) != null)
		{
			JRXmlLoader xmlLoader = null;
			JRPrintXmlLoader printXmlLoader = null;
			Map stylesMap = null;

			Object loader = digester.peek(digester.getCount() - 1);
			if (loader instanceof JRXmlLoader)
			{
				xmlLoader = (JRXmlLoader)loader;
				JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
				stylesMap = jasperDesign.getStylesMap();
			}
			else
			{
				printXmlLoader = (JRPrintXmlLoader)loader;
				JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);
				stylesMap = jasperPrint.getStylesMap();
			}

			if ( !stylesMap.containsKey(atts.getValue(ATTRIBUTE_style)) )
			{
				if (printXmlLoader == null)
				{
					xmlLoader.addError(new Exception("Unknown report style : " + atts.getValue(ATTRIBUTE_style)));
				}
				else
				{
					printXmlLoader.addError(new Exception("Unknown report style : " + atts.getValue(ATTRIBUTE_style)));
				}
			}

			style.setParentStyle((JRStyle) stylesMap.get(atts.getValue(ATTRIBUTE_style)));
		}


		// get JRElement attributes
		Byte mode = (Byte)JRXmlConstants.getModeMap().get(atts.getValue(ATTRIBUTE_mode));
		if (mode != null)
		{
			style.setMode(mode);//FIXME STYLE change all
		}

		String forecolor = atts.getValue(ATTRIBUTE_forecolor);
		style.setForecolor(getColor(forecolor, null));

		String backcolor = atts.getValue(ATTRIBUTE_backcolor);
		style.setBackcolor(getColor(backcolor, null));



		// get graphic element attributes
		Byte pen = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_pen));
		style.setPen(pen);

		Byte fill = (Byte)JRXmlConstants.getFillMap().get(atts.getValue(ATTRIBUTE_fill));
		style.setFill(fill);



		// get rectangle attributes
		String radius = atts.getValue(ATTRIBUTE_radius);
		if (radius != null && radius.length() > 0)
		{
			style.setRadius(Integer.parseInt(radius));
		}



		// get image attributes
		Byte scaleImage = (Byte)JRXmlConstants.getScaleImageMap().get(atts.getValue(ATTRIBUTE_scaleImage));
		if (scaleImage != null)
		{
			style.setScaleImage(scaleImage);
		}

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(ATTRIBUTE_hAlign));
		if (horizontalAlignment != null)
		{
			style.setHorizontalAlignment(horizontalAlignment);
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(ATTRIBUTE_vAlign));
		if (verticalAlignment != null)
		{
			style.setVerticalAlignment(verticalAlignment);
		}


		// get box attributes
		Byte border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_border));
		if (border != null)
		{
			style.setBorder(border);
		}

		Color borderColor = getColor(atts.getValue(ATTRIBUTE_borderColor), null);
		if (borderColor != null)
		{
			style.setBorderColor(borderColor);
		}

		String padding = atts.getValue(ATTRIBUTE_padding);
		if (padding != null && padding.length() > 0)
		{
			style.setPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_topBorder));
		if (border != null)
		{
			style.setTopBorder(border);
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_topBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setTopBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_topPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setTopPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_leftBorder));
		if (border != null)
		{
			style.setLeftBorder(border);
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_leftBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setLeftBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_leftPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setLeftPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_bottomBorder));
		if (border != null)
		{
			style.setBottomBorder(border);
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_bottomBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setBottomBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_bottomPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setBottomPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_rightBorder));
		if (border != null)
		{
			style.setRightBorder(border);
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_rightBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setRightBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_rightPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setRightPadding(Integer.parseInt(padding));
		}



		Byte rotation = (Byte)JRXmlConstants.getRotationMap().get(atts.getValue(ATTRIBUTE_rotation));
		if (rotation != null)
		{
			style.setRotation(rotation);
		}

		Byte lineSpacing = (Byte)JRXmlConstants.getLineSpacingMap().get(atts.getValue(ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			style.setLineSpacing(lineSpacing);
		}

		String isStyledText = atts.getValue(ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			style.setStyledText(Boolean.valueOf(isStyledText));
		}

		style.setPattern(atts.getValue(ATTRIBUTE_pattern));

		String isBlankWhenNull = atts.getValue(ATTRIBUTE_isBlankWhenNull);
		if (isBlankWhenNull != null && isBlankWhenNull.length() > 0)
		{
			style.setBlankWhenNull(Boolean.valueOf(isBlankWhenNull));
		}

		if (atts.getValue(ATTRIBUTE_fontName) != null)
			style.setFontName(atts.getValue(ATTRIBUTE_fontName));

		if (atts.getValue(ATTRIBUTE_isBold) != null)
			style.setBold(Boolean.valueOf(atts.getValue(ATTRIBUTE_isBold)));

		if (atts.getValue(ATTRIBUTE_isItalic) != null)
			style.setItalic(Boolean.valueOf(atts.getValue(ATTRIBUTE_isItalic)));

		if (atts.getValue(ATTRIBUTE_isUnderline) != null)
			style.setUnderline(Boolean.valueOf(atts.getValue(ATTRIBUTE_isUnderline)));

		if (atts.getValue(ATTRIBUTE_isStrikeThrough) != null)
			style.setStrikeThrough(Boolean.valueOf(atts.getValue(ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(ATTRIBUTE_fontSize) != null)
			style.setFontSize(Integer.valueOf(atts.getValue(ATTRIBUTE_fontSize)));

		if (atts.getValue(ATTRIBUTE_pdfFontName) != null)
			style.setPdfFontName(atts.getValue(ATTRIBUTE_pdfFontName));

		if (atts.getValue(ATTRIBUTE_pdfEncoding) != null)
			style.setPdfEncoding(atts.getValue(ATTRIBUTE_pdfEncoding));

		if (atts.getValue(ATTRIBUTE_isPdfEmbedded) != null)
			style.setPdfEmbedded(Boolean.valueOf(atts.getValue(ATTRIBUTE_isPdfEmbedded)));


		return style;
	}
}
