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

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class SimpleChartSettings implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_backgroundPaint = "backgroundPaint";
	public static final String PROPERTY_borderVisible = "borderVisible";
	public static final String PROPERTY_antiAlias = "antiAlias";

	public static final String PROPERTY_showTitle = "showTitle";
	public static final String PROPERTY_titlePosition = "titlePosition";
	public static final String PROPERTY_titleFont = "titleFont";
	public static final String PROPERTY_titleForecolor = "titleForecolor";
	public static final String PROPERTY_titleBackgroundPaint = "titleBackgroundPaint";
	public static final String PROPERTY_titleHorizontalAlignment = "titleHorizontalAlignment";

	public static final String PROPERTY_showSubtitle = "showSubtitle";
	public static final String PROPERTY_subtitlePosition = "subtitlePosition";
	public static final String PROPERTY_subtitleFont = "subtitleFont";
	public static final String PROPERTY_subtitleForecolor = "subtitleForecolor";
	public static final String PROPERTY_subtitleBackgroundPaint = "subtitleBackgroundPaint";
	public static final String PROPERTY_subtitleHorizontalAlignment = "subtitleHorizontalAlignment";

	public static final String PROPERTY_showLegend = "showLegend";
	public static final String PROPERTY_legendPosition = "legendPosition";
	public static final String PROPERTY_legendFont = "legendFont";
	public static final String PROPERTY_legendForecolor = "legendForecolor";
	public static final String PROPERTY_legendBackgroundPaint = "legendBackgroundPaint";
	public static final String PROPERTY_legendHorizontalAlignment = "legendHorizontalAlignment";

	/**
	 *
	 */
	private PaintProvider backgroundPaint = null;
	private Boolean borderVisible = null;
	private Boolean antiAlias = null;
//    <entry key-ref="unitType" value-ref="unitTypeRelative"/>//FIXMETHME

	private Boolean showTitle = null;
	private Byte titlePosition = null;
	private JRFont titleFont = null;
	private Color titleForecolor = null;//FIXMETHEME this and others might be paint
	private PaintProvider titleBackgroundPaint = null;
	private Byte titleHorizontalAlignment = null;

	private Boolean showSubtitle = null;
	private Byte subtitlePosition = null;
	private JRFont subtitleFont = null;
	private Color subtitleForecolor = null;
	private PaintProvider subtitleBackgroundPaint = null;
	private Byte subtitleHorizontalAlignment = null;

	private Boolean showLegend = null;
	private Byte legendPosition = null;
	private JRFont legendFont = null;
	private Color legendForecolor = null;//FIXMETHEME this and others might be paint
	private PaintProvider legendBackgroundPaint = null;
	private Byte legendHorizontalAlignment = null;
	
	/**
	 *
	 */
	public SimpleChartSettings()
	{
	}
	
	/**
	 *
	 */
	public PaintProvider getBackgroundPaint()
	{
		return backgroundPaint;
	}
	
	/**
	 *
	 */
	public void setBackgroundPaint(PaintProvider backgroundPaint)
	{
		PaintProvider old = getBackgroundPaint();
		this.backgroundPaint = backgroundPaint;
		getEventSupport().firePropertyChange(PROPERTY_backgroundPaint, old, getBackgroundPaint());
	}
	
	/**
	 *
	 */
	public Boolean getShowTitle()
	{
		return showTitle;
	}
	
	/**
	 *
	 */
	public void setShowTitle(Boolean showTitle)
	{
		Boolean old = getShowTitle();
		this.showTitle = showTitle;
		getEventSupport().firePropertyChange(PROPERTY_showTitle, old, getShowTitle());
	}
	
	/**
	 *
	 */
	public Boolean getShowSubtitle()
	{
		return showSubtitle;
	}
	
	/**
	 *
	 */
	public void setShowSubtitle(Boolean showSubtitle)
	{
		Boolean old = getShowSubtitle();
		this.showSubtitle = showSubtitle;
		getEventSupport().firePropertyChange(PROPERTY_showSubtitle, old, getShowSubtitle());
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
	 * @return the titlePosition
	 */
	public Byte getTitlePosition() {
		return titlePosition;
	}

	/**
	 * @param titlePosition the titlePosition to set
	 */
	public void setTitlePosition(Byte titlePosition) {
		Byte old = getTitlePosition();
		this.titlePosition = titlePosition;
		getEventSupport().firePropertyChange(PROPERTY_titlePosition, old, getTitlePosition());
	}

	/**
	 * @return the antiAlias
	 */
	public Boolean getAntiAlias() {
		return antiAlias;
	}

	/**
	 * @param antiAlias the antiAlias to set
	 */
	public void setAntiAlias(Boolean antiAlias) {
		Boolean old = getAntiAlias();
		this.antiAlias = antiAlias;
		getEventSupport().firePropertyChange(PROPERTY_antiAlias, old, getAntiAlias());
	}

	/**
	 * @return the borderVisible
	 */
	public Boolean getBorderVisible() {
		return borderVisible;
	}

	/**
	 * @param borderVisible the borderVisible to set
	 */
	public void setBorderVisible(Boolean borderVisible) {
		Boolean old = getBorderVisible();
		this.borderVisible = borderVisible;
		getEventSupport().firePropertyChange(PROPERTY_borderVisible, old, getBorderVisible());
	}

	/**
	 * @return the legendBackgroundPaint
	 */
	public PaintProvider getLegendBackgroundPaint() {
		return legendBackgroundPaint;
	}

	/**
	 * @param legendBackgroundPaint the legendBackgroundPaint to set
	 */
	public void setLegendBackgroundPaint(PaintProvider legendBackgroundPaint) {
		PaintProvider old = getLegendBackgroundPaint();
		this.legendBackgroundPaint = legendBackgroundPaint;
		getEventSupport().firePropertyChange(PROPERTY_legendBackgroundPaint, old, getLegendBackgroundPaint());
	}

	/**
	 * @return the legendFont
	 */
	public JRFont getLegendFont() {
		return legendFont;
	}

	/**
	 * @param legendFont the legendFont to set
	 */
	public void setLegendFont(JRFont legendFont) {
		JRFont old = getLegendFont();
		this.legendFont = legendFont;
		getEventSupport().firePropertyChange(PROPERTY_legendFont, old, getLegendFont());
	}

	/**
	 * @return the legendForecolor
	 */
	public Color getLegendForecolor() {
		return legendForecolor;
	}

	/**
	 * @param legendForecolor the legendForecolor to set
	 */
	public void setLegendForecolor(Color legendForecolor) {
		Color old = getLegendForecolor();
		this.legendForecolor = legendForecolor;
		getEventSupport().firePropertyChange(PROPERTY_legendForecolor, old, getLegendForecolor());
	}

	/**
	 * @return the legendHorizontalAlignment
	 */
	public Byte getLegendHorizontalAlignment() {
		return legendHorizontalAlignment;
	}

	/**
	 * @param legendHorizontalAlignment the legendHorizontalAlignment to set
	 */
	public void setLegendHorizontalAlignment(Byte legendHorizontalAlignment) {
		Byte old = getLegendHorizontalAlignment();
		this.legendHorizontalAlignment = legendHorizontalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_legendHorizontalAlignment, old, getLegendHorizontalAlignment());
	}

	/**
	 * @return the legendPosition
	 */
	public Byte getLegendPosition() {
		return legendPosition;
	}

	/**
	 * @param legendPosition the legendPosition to set
	 */
	public void setLegendPosition(Byte legendPosition) {
		Byte old = getLegendPosition();
		this.legendPosition = legendPosition;
		getEventSupport().firePropertyChange(PROPERTY_legendPosition, old, getLegendPosition());
	}

	/**
	 * @return the subtitleBackgroundPaint
	 */
	public PaintProvider getSubtitleBackgroundPaint() {
		return subtitleBackgroundPaint;
	}

	/**
	 * @param subtitleBackgroundPaint the subtitleBackgroundPaint to set
	 */
	public void setSubtitleBackgroundPaint(PaintProvider subtitleBackgroundPaint) {
		PaintProvider old = getSubtitleBackgroundPaint();
		this.subtitleBackgroundPaint = subtitleBackgroundPaint;
		getEventSupport().firePropertyChange(PROPERTY_subtitleBackgroundPaint, old, getSubtitleBackgroundPaint());
	}

	/**
	 * @return the subtitleForecolor
	 */
	public Color getSubtitleForecolor() {
		return subtitleForecolor;
	}

	/**
	 * @param subtitleForecolor the subtitleForecolor to set
	 */
	public void setSubtitleForecolor(Color subtitleForecolor) {
		Color old = getSubtitleForecolor();
		this.subtitleForecolor = subtitleForecolor;
		getEventSupport().firePropertyChange(PROPERTY_subtitleForecolor, old, getSubtitleForecolor());
	}

	/**
	 * @return the subtitleHorizontalAlignment
	 */
	public Byte getSubtitleHorizontalAlignment() {
		return subtitleHorizontalAlignment;
	}

	/**
	 * @param subtitleHorizontalAlignment the subtitleHorizontalAlignment to set
	 */
	public void setSubtitleHorizontalAlignment(Byte subtitleHorizontalAlignment) {
		Byte old = getSubtitleHorizontalAlignment();
		this.subtitleHorizontalAlignment = subtitleHorizontalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_subtitleHorizontalAlignment, old, getSubtitleHorizontalAlignment());
	}

	/**
	 * @return the subtitlePosition
	 */
	public Byte getSubtitlePosition() {
		return subtitlePosition;
	}

	/**
	 * @param subtitlePosition the subtitlePosition to set
	 */
	public void setSubtitlePosition(Byte subtitlePosition) {
		Byte old = getSubtitlePosition();
		this.subtitlePosition = subtitlePosition;
		getEventSupport().firePropertyChange(PROPERTY_subtitlePosition, old, getSubtitlePosition());
	}

	/**
	 * @return the subtitleFont
	 */
	public JRFont getSubtitleFont() {
		return subtitleFont;
	}

	/**
	 * @param subtitleFont the subtitleFont to set
	 */
	public void setSubttitleFont(JRFont subtitleFont) {
		JRFont old = getSubtitleFont();
		this.subtitleFont = subtitleFont;
		getEventSupport().firePropertyChange(PROPERTY_subtitleFont, old, getSubtitleFont());
	}

	/**
	 * @return the titleBackgroundPaint
	 */
	public PaintProvider getTitleBackgroundPaint() {
		return titleBackgroundPaint;
	}

	/**
	 * @param titleBackgroundPaint the titleBackgroundPaint to set
	 */
	public void setTitleBackgroundPaint(PaintProvider titleBackgroundPaint) {
		PaintProvider old = getTitleBackgroundPaint();
		this.titleBackgroundPaint = titleBackgroundPaint;
		getEventSupport().firePropertyChange(PROPERTY_titleBackgroundPaint, old, getTitleBackgroundPaint());
	}

	/**
	 * @return the titleFont
	 */
	public JRFont getTitleFont() {
		return titleFont;
	}

	/**
	 * @param titleFont the titleFont to set
	 */
	public void setTitleFont(JRFont titleFont) {
		JRFont old = getTitleFont();
		this.titleFont = titleFont;
		getEventSupport().firePropertyChange(PROPERTY_titleFont, old, getTitleFont());
	}

	/**
	 * @return the titleForecolor
	 */
	public Color getTitleForecolor() {
		return titleForecolor;
	}

	/**
	 * @param titleForecolor the titleForecolor to set
	 */
	public void setTitleForecolor(Color titleForecolor) {
		Color old = getTitleForecolor();
		this.titleForecolor = titleForecolor;
		getEventSupport().firePropertyChange(PROPERTY_titleForecolor, old, getTitleForecolor());
	}

	/**
	 * @return the titleHorizontalAlignment
	 */
	public Byte getTitleHorizontalAlignment() {
		return titleHorizontalAlignment;
	}

	/**
	 * @param titleHorizontalAlignment the titleHorizontalAlignment to set
	 */
	public void setTitleHorizontalAlignment(Byte titleHorizontalAlignment) {
		Byte old = getTitleHorizontalAlignment();
		this.titleHorizontalAlignment = titleHorizontalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_titleHorizontalAlignment, old, getTitleHorizontalAlignment());
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
