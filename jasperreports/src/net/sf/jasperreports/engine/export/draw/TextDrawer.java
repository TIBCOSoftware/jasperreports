/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.AwtTextRenderer;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class TextDrawer extends ElementDrawer<JRPrintText>
{

	/**
	 *
	 */
	protected AwtTextRenderer textRenderer;

	
	/**
	 * @deprecated Replaced by {@link #TextDrawer(JasperReportsContext, AwtTextRenderer)}.
	 */
	public TextDrawer(
		AwtTextRenderer textRenderer
		)
	{
		this(DefaultJasperReportsContext.getInstance(), textRenderer);
	}
	
	
	/**
	 *
	 */
	public TextDrawer(
		JasperReportsContext jasperReportsContext,
		AwtTextRenderer textRenderer
		)
	{
		super(jasperReportsContext);
		this.textRenderer = textRenderer;
	}
	
	
	/**
	 *
	 */
	public void draw(Graphics2D grx, JRPrintText text, int offsetX, int offsetY)
	{
		textRenderer.initialize(grx, text, offsetX, offsetY);
		
		JRStyledText styledText = textRenderer.getStyledText();
		
		if (styledText == null)
		{
			return;
		}

		double angle = 0;
		
		switch (text.getRotationValue())
		{
			case LEFT :
			{
				angle = - Math.PI / 2;
				break;
			}
			case RIGHT :
			{
				angle = Math.PI / 2;
				break;
			}
			case UPSIDE_DOWN :
			{
				angle = Math.PI;
				break;
			}
			case NONE :
			default :
			{
			}
		}
		
		grx.rotate(angle, textRenderer.getX(), textRenderer.getY());

		if (text.getModeValue() == ModeEnum.OPAQUE)
		{
			grx.setColor(text.getBackcolor());
			grx.fillRect(textRenderer.getX(), textRenderer.getY(), textRenderer.getWidth(), textRenderer.getHeight()); 
		}
//		else
//		{
//			/*
//			grx.setColor(text.getForecolor());
//			grx.setStroke(new BasicStroke(1));
//			grx.drawRect(x, y, width, height);
//			*/
//		}

		String allText = textRenderer.getPlainText();
		if (allText.length() > 0)
		{
			grx.setColor(text.getForecolor());

			/*   */
			textRenderer.render();
		}
		
		grx.rotate(-angle, textRenderer.getX(), textRenderer.getY());

		/*   */
		drawBox(grx, text.getLineBox(), text, offsetX, offsetY);
	}

	
}
