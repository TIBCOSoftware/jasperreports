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
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.ui.RectangleInsets;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class PlotSettings implements JRChangeEventsSupport, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
//	FIXMETHEME complete props
	public static final String PROPERTY_orientation = "orientation";
	public static final String PROPERTY_foregroundAlpha = "foregroundAlpha";
	public static final String PROPERTY_backgroundPaint = "backgroundPaint";
	public static final String PROPERTY_backgroundAlpha = "backgroundAlpha";
	public static final String PROPERTY_backgroundImage = "backgroundImage";
	public static final String PROPERTY_backgroundImageAlpha = "backgroundImageAlpha";
	public static final String PROPERTY_backgroundImageAlignment = "backgroundImageAlignment";
	public static final String PROPERTY_labelRotation = "labelRotation";
	public static final String PROPERTY_padding = "padding";
	public static final String PROPERTY_outlineVisible = "outlineVisible";
	public static final String PROPERTY_outlinePaint = "outlinePaint";
	public static final String PROPERTY_outlineStroke = "outlineStroke";
	public static final String PROPERTY_seriesColorSequence = "seriesColorSequence";
	public static final String PROPERTY_seriesGradientPaintSequence = "seriesGradientPaintSequence";
	public static final String PROPERTY_seriesOutlinePaintSequence = "seriesOutlinePaintSequence";
	public static final String PROPERTY_seriesStrokeSequence = "seriesStrokeSequence";
	public static final String PROPERTY_seriesOutlineStrokeSequence = "seriesOutlineStrokeSequence";
//	public static final String PROPERTY_shapeSequence = "shapeSequence";

	/**
	 *
	 */
	private PlotOrientation orientation = null;
	private Float foregroundAlpha = null;
	private PaintProvider backgroundPaint = null;
	private Float backgroundAlpha = null;
	private ImageProvider backgroundImage = null;
	private Float backgroundImageAlpha = null;
	private Integer backgroundImageAlignment = null;
	private Double labelRotation = null;
	private RectangleInsets padding = null;
	private Boolean outlineVisible = null;
	private PaintProvider outlinePaint = null;
	private Stroke outlineStroke = null;
	private List seriesColorSequence = null;
	private List seriesGradientPaintSequence = null;
	private List seriesOutlinePaintSequence = null;
	private List seriesStrokeSequence = null;
	private List seriesOutlineStrokeSequence = null;
	
	/**
	 * @return the padding
	 */
	public RectangleInsets getPadding() {
		return padding;
	}

	/**
	 * @param padding the padding to set
	 */
	public void setPadding(RectangleInsets padding) {
		RectangleInsets old = getPadding();
		this.padding = padding;
		getEventSupport().firePropertyChange(PROPERTY_padding, old, getPadding());
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
	 * @return the backgroundAlpha
	 */
	public Float getBackgroundAlpha() {
		return backgroundAlpha;
	}

	/**
	 * @param backgroundAlpha the backgroundAlpha to set
	 */
	public void setBackgroundAlpha(Float backgroundAlpha) {
		Float old = getBackgroundAlpha();
		this.backgroundAlpha = backgroundAlpha;
		getEventSupport().firePropertyChange(PROPERTY_backgroundAlpha, old, getBackgroundAlpha());
	}

	/**
	 * @return the backgroundImageAlignment
	 */
	public Integer getBackgroundImageAlignment() {
		return backgroundImageAlignment;
	}

	/**
	 * @param backgroundImageAlignment the backgroundImageAlignment to set
	 */
	public void setBackgroundImageAlignment(Integer backgroundImageAlignment) {
		Integer old = getBackgroundImageAlignment();
		this.backgroundImageAlignment = backgroundImageAlignment;
		getEventSupport().firePropertyChange(PROPERTY_backgroundImageAlignment, old, getBackgroundImageAlignment());
	}

	/**
	 * @return the backgroundImageAlpha
	 */
	public Float getBackgroundImageAlpha() {
		return backgroundImageAlpha;
	}

	/**
	 * @param backgroundImageAlpha the backgroundImageAlpha to set
	 */
	public void setBackgroundImageAlpha(Float backgroundImageAlpha) {
		Float old = getBackgroundImageAlpha();
		this.backgroundImageAlpha = backgroundImageAlpha;
		getEventSupport().firePropertyChange(PROPERTY_backgroundImageAlpha, old, getBackgroundImageAlpha());
	}

	/**
	 * @return the foregroundAlpha
	 */
	public Float getForegroundAlpha() {
		return foregroundAlpha;
	}

	/**
	 * @param foregroundAlpha the foregroundAlpha to set
	 */
	public void setForegroundAlpha(Float foregroundAlpha) {
		Float old = getForegroundAlpha();
		this.foregroundAlpha = foregroundAlpha;
		getEventSupport().firePropertyChange(PROPERTY_foregroundAlpha, old, getForegroundAlpha());
	}

	/**
	 * @return the labelRotation
	 */
	public Double getLabelRotation() {
		return labelRotation;
	}

	/**
	 * @param labelRotation the labelRotation to set
	 */
	public void setLabelRotation(Double labelRotation) {
		Double old = getLabelRotation();
		this.labelRotation = labelRotation;
		getEventSupport().firePropertyChange(PROPERTY_labelRotation, old, getLabelRotation());
	}

	/**
	 * @return the backgroundImage
	 */
	public ImageProvider getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * @param backgroundImage the backgroundImage to set
	 */
	public void setBackgroundImage(ImageProvider backgroundImage) {
		ImageProvider old = getBackgroundImage();
		this.backgroundImage = backgroundImage;
		getEventSupport().firePropertyChange(PROPERTY_backgroundImage, old, getBackgroundImage());
	}

	/**
	 * @return the orientation
	 */
	public PlotOrientation getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(PlotOrientation orientation) {
		PlotOrientation old = getOrientation();
		this.orientation = orientation;
		getEventSupport().firePropertyChange(PROPERTY_orientation, old, getOrientation());
	}

	/**
     * @return the seriesColorSequence
     */
    public List getSeriesColorSequence()
    {
    	return seriesColorSequence;
    }

	/**
     * @param seriesColorSequence the seriesColorSequence to set
     */
    public void setSeriesColorSequence(List seriesColorSequence)
    {
    	List old = getSeriesColorSequence();
    	this.seriesColorSequence = seriesColorSequence;
		getEventSupport().firePropertyChange(PROPERTY_seriesColorSequence, old, getSeriesColorSequence());
    }

	/**
     * @return the seriesGradientPaintSequence
     */
    public List getSeriesGradientPaintSequence()
    {
    	return seriesGradientPaintSequence;
    }

	/**
     * @param seriesGradientPaintSequence the seriesGradientPaintSequence to set
     */
    public void setSeriesGradientPaintSequence(List seriesGradientPaintSequence)
    {
    	List old = getSeriesGradientPaintSequence();
    	this.seriesGradientPaintSequence = seriesGradientPaintSequence;
		getEventSupport().firePropertyChange(PROPERTY_seriesGradientPaintSequence, old, getSeriesGradientPaintSequence());
    }

	/**
     * @return the seriesOutlinePaintSequence
     */
    public List getSeriesOutlinePaintSequence()
    {
    	return seriesOutlinePaintSequence;
    }

	/**
     * @param seriesOutlinePaintSequence the seriesOutlinePaintSequence to set
     */
    public void setSeriesOutlinePaintSequence(List seriesOutlinePaintSequence)
    {
    	List old = getSeriesOutlinePaintSequence();
    	this.seriesOutlinePaintSequence = seriesOutlinePaintSequence;
		getEventSupport().firePropertyChange(PROPERTY_seriesOutlinePaintSequence, old, getSeriesOutlinePaintSequence());
    }

	/**
     * @return the seriesStrokeSequence
     */
    public List getSeriesStrokeSequence()
    {
    	return seriesStrokeSequence;
    }

	/**
     * @param seriesStrokeSequence the seriesStrokeSequence to set
     */
    public void setSeriesStrokeSequence(List seriesStrokeSequence)
    {
    	List old = getSeriesStrokeSequence();
    	this.seriesStrokeSequence = seriesStrokeSequence;
		getEventSupport().firePropertyChange(PROPERTY_seriesStrokeSequence, old, getSeriesStrokeSequence());
    }

	/**
     * @return the seriesOutlineStrokeSequence
     */
    public List getSeriesOutlineStrokeSequence()
    {
    	return seriesOutlineStrokeSequence;
    }

	/**
     * @param seriesOutlineStrokeSequence the seriesOutlineStrokeSequence to set
     */
    public void setSeriesOutlineStrokeSequence(List seriesOutlineStrokeSequence)
    {
    	List old = getSeriesOutlineStrokeSequence();
    	this.seriesOutlineStrokeSequence = seriesOutlineStrokeSequence;
		getEventSupport().firePropertyChange(PROPERTY_seriesOutlineStrokeSequence, old, getSeriesOutlineStrokeSequence());
    }

}
