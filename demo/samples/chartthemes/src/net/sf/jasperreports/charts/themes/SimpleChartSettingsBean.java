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
package net.sf.jasperreports.charts.themes;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class SimpleChartSettingsBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_backgroundPaint = "backgroundPaint";
	public static final String PROPERTY_showTitle = "showTitle";
	public static final String PROPERTY_showSubtitle = "showSubtitle";
	public static final String PROPERTY_showLegend = "showLegend";

	/**
	 *
	 */
	private PaintProvider backgroundPaint = null;
	private Boolean showTitle = null;
	private Boolean showSubtitle = null;
	private Boolean showLegend = null;
//	    <entry key-ref="titlePosition" value-ref="rectangleEdgeTop"/>
//	    <entry key-ref="titleBaseFontBoldStyle" value-ref="fontPlainStyle"/>
//	    <entry key-ref="titleBaseFontItalicStyle" value-ref="fontPlainStyle"/>
//	    <entry key-ref="titleForecolor" value-ref="blackColor"/>
//	    <entry key-ref="titleBackcolor" value-ref="transparentPaint"/>
//	    <entry key-ref="titleHorizontalAlignment" value-ref="centerHorizontalAlignment"/>
//	
//	    <entry key-ref="subtitlePosition" value-ref="rectangleEdgeTop"/>
//	    <entry key-ref="subtitleBaseFontBoldStyle" value-ref="fontPlainStyle"/>
//	    <entry key-ref="subtitleBaseFontItalicStyle" value-ref="fontPlainStyle"/>
//	    <entry key-ref="subtitleForecolor" value-ref="blackColor"/>
//	    <entry key-ref="subtitleBackcolor" value-ref="transparentPaint"/>
//	    <entry key-ref="subtitleHorizontalAlignment" value-ref="centerHorizontalAlignment"/>
//	    
//	    <entry key-ref="legendPosition" value-ref="rectangleEdgeBottom"/>
//	    <entry key-ref="legendBaseFontBoldStyle" value-ref="fontPlainStyle"/>
//	    <entry key-ref="legendBaseFontItalicStyle" value-ref="fontPlainStyle"/>
//	    <entry key-ref="legendForecolor" value-ref="blackColor"/>
//	    <entry key-ref="legendBackcolor" value-ref="transparentPaint"/>
//	    <entry key-ref="legendHorizontalAlignment" value-ref="centerHorizontalAlignment"/>
//
//    <entry key-ref="chartBorderVisible" value-ref="isFalse"/>
//    <entry key-ref="chartAntiAlias" value-ref="isTrue"/>
//    <entry key-ref="unitType" value-ref="unitTypeRelative"/>
	
	/**
	 *
	 */
	public SimpleChartSettingsBean()
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
