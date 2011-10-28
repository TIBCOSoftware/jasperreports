/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.ObjectUtils;

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


	protected JRExpression conditionExpression;



	public JRBaseConditionalStyle()
	{
		super();
	}

	public JRBaseConditionalStyle(JRConditionalStyle style, JRStyle parentStyle, JRAbstractObjectFactory factory)
	{
		this.parentStyle = parentStyle;

		modeValue = style.getOwnModeValue();
		forecolor = style.getOwnForecolor();
		backcolor = style.getOwnBackcolor();

		linePen = style.getLinePen().clone(this);
		fillValue = style.getOwnFillValue();

		radius = style.getOwnRadius();

		scaleImageValue = style.getOwnScaleImageValue();
		horizontalAlignmentValue = style.getOwnHorizontalAlignmentValue();
		verticalAlignmentValue = style.getOwnVerticalAlignmentValue();

		lineBox = style.getLineBox().clone(this);
		paragraph = style.getParagraph().clone(this);

		rotationValue = style.getOwnRotationValue();
		markup = style.getOwnMarkup();

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

	public Object clone()
	{
		JRBaseConditionalStyle clone = (JRBaseConditionalStyle) super.clone();
		clone.conditionExpression = JRCloneUtils.nullSafeClone(conditionExpression);
		return clone;
	}

	@Override
	public int getHashCode()
	{
		ObjectUtils.HashCode hash = ObjectUtils.hash();
		addStyleHash(hash);
		hash.add(conditionExpression == null ? null : conditionExpression.getText());
		return hash.getHashCode();
	}

	@Override
	public boolean isIdentical(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof JRBaseConditionalStyle))
		{
			return false;
		}
		
		JRBaseConditionalStyle style = (JRBaseConditionalStyle) object;

		if (!identicalStyle(style))
		{
			return false;
		}
		
		String expressionText = conditionExpression == null ? null : conditionExpression.getText();
		String otherExpressionText = style.conditionExpression == null ? null : style.conditionExpression.getText();
		return ObjectUtils.equals(expressionText, otherExpressionText);
	}
}
