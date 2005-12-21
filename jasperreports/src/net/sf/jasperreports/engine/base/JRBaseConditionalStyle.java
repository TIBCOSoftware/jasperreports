package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRConstants;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id
 */
public class JRBaseConditionalStyle extends JRBaseStyle implements JRConditionalStyle
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	protected JRExpression conditionExpression = null;



	public JRBaseConditionalStyle()
	{
		super();
	}

	public JRBaseConditionalStyle(JRConditionalStyle style, JRStyle parentStyle, JRAbstractObjectFactory factory)//FIXME STYLE recheck this
	{
		this.parentStyle = parentStyle;

		mode = style.getOwnMode();
		forecolor = style.getOwnForecolor();
		backcolor = style.getOwnBackcolor();

		pen = style.getOwnPen();
		fill = style.getOwnFill();

		radius = style.getOwnRadius();

		scaleImage = style.getOwnScaleImage();
		horizontalAlignment = style.getOwnHorizontalAlignment();
		verticalAlignment = style.getOwnVerticalAlignment();

		border = style.getOwnBorder();
		topBorder = style.getOwnTopBorder();
		leftBorder = style.getOwnLeftBorder();
		bottomBorder = style.getOwnBottomBorder();
		rightBorder = style.getOwnRightBorder();
		borderColor = style.getOwnBorderColor();
		topBorderColor = style.getOwnTopBorderColor();
		leftBorderColor = style.getOwnLeftBorderColor();
		bottomBorderColor = style.getOwnBottomBorderColor();
		rightBorderColor = style.getOwnRightBorderColor();
		padding = style.getOwnPadding();
		topPadding = style.getOwnTopPadding();
		leftPadding = style.getOwnLeftPadding();
		bottomPadding = style.getOwnBottomPadding();
		rightPadding = style.getOwnRightPadding();

		rotation = style.getOwnRotation();
		lineSpacing = style.getOwnLineSpacing();
		isStyledText = style.isOwnStyledText();

		pattern = style.getOwnPattern();

		fontName = style.getOwnFontName();
		isBold = style.isOwnBold();
		isItalic = style.isOwnItalic();
		isUnderline = style.isOwnUnderline();
		isStrikeThrough = style.isOwnStrikeThrough();
		fontSize = style.getOwnFontSize();
		pdfFontName = style.getOwnPdfFontName();
		pdfEncoding = style.getOwnPdfEncoding();
		isPdfEmbedded = style.isOwnPdfEmbedded();
		conditionExpression = factory.getExpression(style.getConditionExpression());
	}


	public JRExpression getConditionExpression()
	{
		return conditionExpression;
	}
}
