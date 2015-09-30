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
package net.sf.jasperreports.engine.export;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
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
	/**
	 * 
	 */
	private Graphics2D grx;
	
	
	/**
	 * @deprecated Replaced by {@link #AwtTextRenderer(JasperReportsContext, boolean, boolean)}.
	 */
	public static AwtTextRenderer getInstance()
	{
		return 
			new AwtTextRenderer(
				DefaultJasperReportsContext.getInstance(),
				JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getBooleanProperty(JRGraphics2DExporter.MINIMIZE_PRINTER_JOB_SIZE),
				JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getBooleanProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT)
				);
	}
	
	
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
	}
	

	/**
	 * @deprecated Replaced by {@link #AwtTextRenderer(JasperReportsContext, boolean, boolean)}.
	 */
	public AwtTextRenderer(
		boolean isMinimizePrinterJobSize,
		boolean ignoreMissingFont
		)
	{
		this(DefaultJasperReportsContext.getInstance(), isMinimizePrinterJobSize, ignoreMissingFont);
	}
	

	/**
	 * 
	 */
	public void initialize(Graphics2D grx, JRPrintText text, int offsetX, int offsetY)
	{
		JRStyledText styledText = JRStyledTextUtil.getInstance(jasperReportsContext).getStyledText(text, noBackcolorSelector);
		if (styledText == null)
		{
			return;
		}
		
		this.grx = grx;
		
		super.initialize(text, styledText, offsetX, offsetY);
	}
		

	/**
	 * 
	 */
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

	
	/**
	 * 
	 */
	public FontRenderContext getFontRenderContext()
	{
		return grx.getFontRenderContext();
	}
	
}
