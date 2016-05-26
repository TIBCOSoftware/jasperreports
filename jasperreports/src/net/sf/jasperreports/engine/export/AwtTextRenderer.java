/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.export;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class AwtTextRenderer extends AbstractTextRenderer
{
	protected final JRStyledTextAttributeSelector noBackcolorSelector;
	protected final JRStyledTextUtil styledTextUtil;
	
	/**
	 * 
	 */
	private Graphics2D grx;
	
	
	/**
	 * 
	 */
	public AwtTextRenderer(
		JasperReportsContext jasperReportsContext,
		boolean isMinimizePrinterJobSize,
		boolean ignoreMissingFont
		)
	{
		super(jasperReportsContext, isMinimizePrinterJobSize, ignoreMissingFont);
		
		this.noBackcolorSelector = JRStyledTextAttributeSelector.getNoBackcolorSelector(jasperReportsContext);
		styledTextUtil = JRStyledTextUtil.getInstance(jasperReportsContext);
	}
	

	/**
	 * 
	 */
	public void initialize(Graphics2D grx, JRPrintText text, int offsetX, int offsetY)
	{
		JRStyledText styledText = styledTextUtil.getProcessedStyledText(text, noBackcolorSelector, null);
		if (styledText == null)
		{
			return;
		}
		
		this.grx = grx;
		
		super.initialize(text, styledText, offsetX, offsetY);
	}
		

	@Override
	public void draw()
	{
		TabSegment segment = segments.get(segmentIndex);
		
		segment.layout.draw(
			grx,
			x + drawPosX,// + leftPadding,
			//y + topPadding + verticalAlignOffset + text.getLeadingOffset() + drawPosY
			y + topPadding + verticalAlignOffset + drawPosY
			);
	}

	
	@Override
	public FontRenderContext getFontRenderContext()
	{
		return grx.getFontRenderContext();
	}
	
}
