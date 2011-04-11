/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.draw;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TextDrawer extends ElementDrawer<JRPrintText>
{

	/**
	 *
	 */
	protected TextRenderer textRenderer;

	
	/**
	 *
	 */
	public TextDrawer(
		TextRenderer textRenderer
		)
	{
		this.textRenderer = textRenderer;
	}
	
	
	/**
	 *
	 */
	public void draw(Graphics2D grx, JRPrintText text, int offsetX, int offsetY)
	{
		JRStyledText styledText = getStyledText(text);
		
		if (styledText == null)
		{
			return;
		}

		String allText = styledText.getText();
		
		int x = text.getX() + offsetX;
		int y = text.getY() + offsetY;
		int width = text.getWidth();
		int height = text.getHeight();
		int topPadding = text.getLineBox().getTopPadding().intValue();
		int leftPadding = text.getLineBox().getLeftPadding().intValue();
		int bottomPadding = text.getLineBox().getBottomPadding().intValue();
		int rightPadding = text.getLineBox().getRightPadding().intValue();
		
		double angle = 0;
		
		switch (text.getRotationValue())
		{
			case LEFT :
			{
				y = text.getY() + offsetY + text.getHeight();
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = leftPadding;
				leftPadding = bottomPadding;
				bottomPadding = rightPadding;
				rightPadding = tmpPadding;
				angle = - Math.PI / 2;
				break;
			}
			case RIGHT :
			{
				x = text.getX() + offsetX + text.getWidth();
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = rightPadding;
				rightPadding = bottomPadding;
				bottomPadding = leftPadding;
				leftPadding = tmpPadding;
				angle = Math.PI / 2;
				break;
			}
			case UPSIDE_DOWN :
			{
				int tmpPadding = topPadding;
				x = text.getX() + offsetX + text.getWidth();
				y = text.getY() + offsetY + text.getHeight();
				topPadding = bottomPadding;
				bottomPadding = tmpPadding;
				tmpPadding = leftPadding;
				leftPadding = rightPadding;
				rightPadding = tmpPadding;
				angle = Math.PI;
				break;
			}
			case NONE :
			default :
			{
			}
		}
		
		grx.rotate(angle, x, y);

		if (text.getModeValue() == ModeEnum.OPAQUE)
		{
			grx.setColor(text.getBackcolor());
			grx.fillRect(x, y, width, height); 
		}
//		else
//		{
//			/*
//			grx.setColor(text.getForecolor());
//			grx.setStroke(new BasicStroke(1));
//			grx.drawRect(x, y, width, height);
//			*/
//		}

		if (allText.length() > 0)
		{
			grx.setColor(text.getForecolor());

			/*   */
			textRenderer.render(
				grx, 
				x, 
				y, 
				width, 
				height, 
				topPadding,
				leftPadding,
				bottomPadding,
				rightPadding,
				text.getTextHeight(), 
				text.getHorizontalAlignmentValue(), 
				text.getVerticalAlignmentValue(), 
				text.getParagraph(),
				text.getLineSpacingFactor(),
				text.getLeadingOffset(),
				text.getFontSize(),
				!JRCommonText.MARKUP_NONE.equals(text.getMarkup()),
				styledText, 
				allText
				);
			
		}
		
		grx.rotate(-angle, x, y);

		/*   */
		drawBox(grx, text.getLineBox(), text, offsetX, offsetY);
	}

	
	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return textElement.getStyledText(JRStyledTextAttributeSelector.NO_BACKCOLOR);
	}

	
}
