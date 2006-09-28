/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRConstants;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
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

	public JRBaseConditionalStyle(JRConditionalStyle style, JRStyle parentStyle, JRAbstractObjectFactory factory)
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
		conditionExpression = factory.getExpression(style.getConditionExpression(), true);
	}


	public JRExpression getConditionExpression()
	{
		return conditionExpression;
	}
}
