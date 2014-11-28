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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.type.ModeEnum;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public final class StyleUtil
{

	private static final StyleUtil INSTANCE = new StyleUtil();
	
	public static StyleUtil instance()
	{
		return INSTANCE;
	}
	
	private StyleUtil()
	{
	}
	
	public ModeEnum resolveMode(JRStyleContainer styleContainer)
	{
		JRStyle style = styleContainer.getStyle();
		if (style != null)
		{
			ModeEnum styleMode = style.getOwnModeValue();
			if (styleMode != null)
			{
				JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();
				if (conditionalStyles != null)
				{
					for (JRConditionalStyle conditionalStyle : conditionalStyles)
					{
						ModeEnum conditionalMode = conditionalStyle.getOwnModeValue();
						if (conditionalMode != null && conditionalMode != styleMode)
						{
							// a conditional style overrides the style mode
							return null;
						}
					}
				}
				
				// we have a style
				return styleMode;
			}
			
			// going to the parent
			return resolveMode(style);
		}
		
		if (styleContainer.getStyleNameReference() != null)
		{
			// we can't resolve external style references here
			return null;
		}
		
		// if no style set, default
		return ModeEnum.TRANSPARENT;
	}
	
	public ModeEnum resolveElementMode(JRElement element)
	{
		ModeEnum elementMode = element.getOwnModeValue();
		if (elementMode != null)
		{
			return elementMode;
		}
		
		return resolveMode(element);
	}
	
	private interface BoxSideSelector
	{
		JRBoxPen getPen(JRLineBox lineBox);
		
		Integer getPadding(JRLineBox lineBox);
	}
	
	private static final BoxSideSelector RIGHT_SIDE = new BoxSideSelector()
	{
		@Override
		public JRBoxPen getPen(JRLineBox lineBox)
		{
			return lineBox.getRightPen();
		}

		@Override
		public Integer getPadding(JRLineBox lineBox)
		{
			return lineBox.getOwnRightPadding();
		}
	};
	
	private static final BoxSideSelector TOP_SIDE = new BoxSideSelector()
	{
		@Override
		public JRBoxPen getPen(JRLineBox lineBox)
		{
			return lineBox.getTopPen();
		}

		@Override
		public Integer getPadding(JRLineBox lineBox)
		{
			return lineBox.getOwnTopPadding();
		}
	};
	
	private static final BoxSideSelector LEFT_SIDE = new BoxSideSelector()
	{
		@Override
		public JRBoxPen getPen(JRLineBox lineBox)
		{
			return lineBox.getLeftPen();
		}

		@Override
		public Integer getPadding(JRLineBox lineBox)
		{
			return lineBox.getOwnLeftPadding();
		}
	};
	
	private static final BoxSideSelector BOTTOM_SIDE = new BoxSideSelector()
	{
		@Override
		public JRBoxPen getPen(JRLineBox lineBox)
		{
			return lineBox.getBottomPen();
		}

		@Override
		public Integer getPadding(JRLineBox lineBox)
		{
			return lineBox.getOwnBottomPadding();
		}
	};

	public boolean hasBox(JRBoxContainer boxContainer)
	{
		return hasBorder(boxContainer, RIGHT_SIDE)
				|| hasBorder(boxContainer, TOP_SIDE)
				|| hasBorder(boxContainer, LEFT_SIDE)
				|| hasBorder(boxContainer, BOTTOM_SIDE)
				|| hasPadding(boxContainer, RIGHT_SIDE)
				|| hasPadding(boxContainer, TOP_SIDE)
				|| hasPadding(boxContainer, LEFT_SIDE)
				|| hasPadding(boxContainer, BOTTOM_SIDE);
	}
	
	protected boolean hasBorder(JRBoxContainer boxContainer, BoxSideSelector selector)
	{
		JRLineBox lineBox = boxContainer.getLineBox();
		Float rightLineWidth = selector.getPen(lineBox).getOwnLineWidth();
		if (rightLineWidth != null)
		{
			return rightLineWidth > .0f;
		}
		
		Float lineWidth = lineBox.getPen().getOwnLineWidth();
		if (lineWidth != null)
		{
			return lineWidth > .0f;
		}
		
		JRStyle style = boxContainer.getStyle();
		if (style != null)
		{
			return hasBorder(style, selector);
		}
		
		String styleReference = boxContainer.getStyleNameReference();
		if (styleReference != null)
		{
			// we can't resolve the style reference, return pessimistically
			return true;
		}
		
		return false;
	}
	
	protected boolean hasPadding(JRBoxContainer boxContainer, BoxSideSelector penSelector)
	{
		JRLineBox lineBox = boxContainer.getLineBox();
		Integer sidePadding = penSelector.getPadding(lineBox);
		if (sidePadding != null)
		{
			return sidePadding > 0;
		}
		
		Integer padding = lineBox.getOwnPadding();
		if (padding != null)
		{
			return padding > 0;
		}
		
		JRStyle style = boxContainer.getStyle();
		if (style != null)
		{
			return hasPadding(style, penSelector);
		}
		
		String styleReference = boxContainer.getStyleNameReference();
		if (styleReference != null)
		{
			// we can't resolve the style reference, return pessimistically
			return true;
		}
		
		return false;
	}
	
}
