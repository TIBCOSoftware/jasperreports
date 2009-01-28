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
package net.sf.jasperreports.chartthemes.simple;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

import org.jfree.ui.HorizontalAlignment;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class LegendSettings implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_showLegend = "showLegend";
	public static final String PROPERTY_position = "position";
	public static final String PROPERTY_foregroundPaint = "foregroundPaint";
	public static final String PROPERTY_backgroundPaint = "backgroundPaint";
	public static final String PROPERTY_font = "font";
	public static final String PROPERTY_horizontalAlignment = "horizontalAlignment";
//	public static final String LEGEND_FRAME = "legendFrame";
//	public static final String LEGEND_VERTICAL_ALIGNMENT = "legendVerticalAlignment";
//	public static final String LEGEND_HEIGHT = "legendHeight";
//	public static final String LEGEND_WIDTH = "legendWidth";
//	public static final String LEGEND_BOUNDS = "legendBounds";
//	public static final String LEGEND_MARGIN = "legendMargin";
//	public static final String LEGEND_PADDING = "legendPadding";
//	public static final String LEGEND_ITEM_GRAPHIC_PADDING = "legendItemGraphicPadding";
//	public static final String LEGEND_ITEM_GRAPHIC_LOCATION = "legendItemGraphicLocation";
//	public static final String LEGEND_ITEM_GRAPHIC_EDGE = "legendItemGraphicEdge";
//	public static final String LEGEND_ITEM_GRAPHIC_ANCHOR = "legendItemGraphicAnchor";
//	public static final String LEGEND_ITEM_LABEL_PADDING = "legendItemLabelPadding";

	/**
	 *
	 */
	private Boolean showLegend = null;
	private Byte position = null;
	private PaintProvider foregroundPaint = null;
	private PaintProvider backgroundPaint = null;
	private JRFont font = new JRBaseFont();
	private HorizontalAlignment horizontalAlignment = null;
	
	/**
	 *
	 */
	public LegendSettings()
	{
	}
	
	/**
	 *
	 */
	public Boolean getShowLegend()
	{
		return showLegend;
	}
	
	/**
	 *
	 */
	public void setShowLegend(Boolean showLegend)
	{
		Boolean old = getShowLegend();
		this.showLegend = showLegend;
		getEventSupport().firePropertyChange(PROPERTY_showLegend, old, getShowLegend());
	}
	
	/**
	 * @return the backgroundPaint
	 */
	public PaintProvider getBackgroundPaint() {
		return backgroundPaint;
	}

	/**
	 * @param backgroundPaint the backgroundPaint to set
	 */
	public void setBackgroundPaint(PaintProvider backgroundPaint) {
		PaintProvider old = getBackgroundPaint();
		this.backgroundPaint = backgroundPaint;
		getEventSupport().firePropertyChange(PROPERTY_backgroundPaint, old, getBackgroundPaint());
	}

	/**
	 * @return the font
	 */
	public JRFont getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(JRFont font) {
		JRFont old = getFont();
		this.font = font;
		getEventSupport().firePropertyChange(PROPERTY_font, old, getFont());
	}

	/**
	 * @return the foregroundPaint
	 */
	public PaintProvider getForegroundPaint() {
		return foregroundPaint;
	}

	/**
	 * @param foregroundPaint the foregroundPaint to set
	 */
	public void setForegroundPaint(PaintProvider foregroundPaint) {
		PaintProvider old = getForegroundPaint();
		this.foregroundPaint = foregroundPaint;
		getEventSupport().firePropertyChange(PROPERTY_foregroundPaint, old, getForegroundPaint());
	}

	/**
	 * @return the horizontalAlignment
	 */
	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	/**
	 * @param horizontalAlignment the horizontalAlignment to set
	 */
	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		HorizontalAlignment old = getHorizontalAlignment();
		this.horizontalAlignment = horizontalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_horizontalAlignment, old, getHorizontalAlignment());
	}

	/**
	 * @return the position
	 */
	public Byte getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Byte position) {
		Byte old = getPosition();
		this.position = position;
		getEventSupport().firePropertyChange(PROPERTY_position, old, getPosition());
	}


	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

}
