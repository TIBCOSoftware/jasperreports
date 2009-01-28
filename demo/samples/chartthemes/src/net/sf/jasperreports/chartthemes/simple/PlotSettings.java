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

import java.awt.Stroke;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

import org.jfree.ui.RectangleInsets;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class PlotSettings implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

//	public static final String PLOT_BACKGROUND_PAINT = "plotBackgroundPaint";
//	public static final String PLOT_BACKGROUND_ALPHA = "plotBackgroundAlpha";
//	public static final String PLOT_FOREGROUND_ALPHA = "plotForegroundAlpha";
//	public static final String PLOT_BACKGROUND_IMAGE = "plotBackgroundImage";
//	public static final String PLOT_BACKGROUND_IMAGE_ALIGNMENT = "plotBackgroundImageAlignment";
//	public static final String PLOT_BACKGROUND_IMAGE_ALPHA = "plotBackgroundImageAlpha";
//	public static final String PLOT_OUTLINE_PAINT_SEQUENCE = "plotOutlinePaintSequence";
//	public static final String PLOT_STROKE_SEQUENCE = "plotStrokeSequence";
//	public static final String PLOT_OUTLINE_STROKE_SEQUENCE = "plotOutlineStrokeSequence";
//	public static final String PLOT_SHAPE_SEQUENCE = "plotShapeSequence";
//	public static final String PLOT_LABEL_ROTATION = "plotLabelRotation";
//	public static final String PLOT_ORIENTATION = "plotOrientation";
	public static final String PROPERTY_insets = "insets";//FIXMETHEME padding
	public static final String PROPERTY_outlineVisible = "outlineVisible";
	public static final String PROPERTY_outlinePaint = "outlinePaint";
	public static final String PROPERTY_outlineStroke = "outlineStroke";

	/**
	 *
	 */
	private RectangleInsets insets = null;
	private Boolean outlineVisible = null;
	private PaintProvider outlinePaint = null;
	private Stroke outlineStroke = null;
	
	/**
	 * @return the insets
	 */
	public RectangleInsets getInsets() {
		return insets;
	}

	/**
	 * @param insets the insets to set
	 */
	public void setInsets(RectangleInsets insets) {
		RectangleInsets old = getInsets();
		this.insets = insets;
		getEventSupport().firePropertyChange(PROPERTY_insets, old, getInsets());
	}

	/**
	 *
	 */
	public PlotSettings()
	{
	}
	
	/**
	 * @return the outlinePaint
	 */
	public PaintProvider getOutlinePaint() {
		return outlinePaint;
	}

	/**
	 * @param outlinePaint the outlinePaint to set
	 */
	public void setOutlinePaint(PaintProvider outlinePaint) {
		PaintProvider old = getOutlinePaint();
		this.outlinePaint = outlinePaint;
		getEventSupport().firePropertyChange(PROPERTY_outlinePaint, old, getOutlinePaint());
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

	/**
	 * @return the outlineVisible
	 */
	public Boolean getOutlineVisible() {
		return outlineVisible;
	}

	/**
	 * @param outlineVisible the outlineVisible to set
	 */
	public void setOutlineVisible(Boolean outlineVisible) {
		Boolean old = getOutlineVisible();
		this.outlineVisible = outlineVisible;
		getEventSupport().firePropertyChange(PROPERTY_outlineVisible, old, getOutlineVisible());
	}

	/**
	 * @return the outlineStroke
	 */
	public Stroke getOutlineStroke() {
		return outlineStroke;
	}

	/**
	 * @param outlineStroke the outlineStroke to set
	 */
	public void setOutlineStroke(Stroke outlineStroke) {
		Stroke old = getOutlineStroke();
		this.outlineStroke = outlineStroke;
		getEventSupport().firePropertyChange(PROPERTY_outlineStroke, old, getOutlineStroke());
	}

}
