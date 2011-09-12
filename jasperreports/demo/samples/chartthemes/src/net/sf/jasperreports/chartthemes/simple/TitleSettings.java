/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.chartthemes.simple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TitleSettings implements JRChangeEventsSupport, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_showTitle = "showTitle";
	public static final String PROPERTY_position = "position";
	public static final String PROPERTY_foregroundPaint = "foregroundPaint";
	public static final String PROPERTY_backgroundPaint = "backgroundPaint";
	public static final String PROPERTY_font = "font";
	public static final String PROPERTY_horizontalAlignment = "horizontalAlignment";
	public static final String PROPERTY_verticalAlignment = "verticalAlignment";
	public static final String PROPERTY_padding = "padding";

	/**
	 *
	 *
	private SimpleChartSettings chartSettings;

	/**
	 *
	 */
	private Boolean showTitle;
	private EdgeEnum positionValue;
	private PaintProvider foregroundPaint;
	private PaintProvider backgroundPaint;
	private JRFont font = new JRBaseFont();
	private HorizontalAlignment horizontalAlignment;
	private VerticalAlignment verticalAlignment;
	private RectangleInsets padding;
	
	/**
	 *
	 */
	public TitleSettings()
	{
		//font = new JRBaseFont(chartSettings.getFont(), null);//inheritence is too much trouble for such a small gain
	}
	
	/**
	 *
	 *
	public SimpleTitleSettings(SimpleChartSettings chartSettings)
	{
		this.chartSettings = chartSettings;
		
		font = new JRBaseFont(chartSettings.getFont(), null);
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
	 * @return the position
	 */
	public EdgeEnum getPositionValue() {
		return positionValue;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(EdgeEnum positionValue) {
		EdgeEnum old = this.positionValue ;
		this.positionValue = positionValue;
		getEventSupport().firePropertyChange(PROPERTY_position, old, this.positionValue);
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
		//this.font = new JRBaseFont(chartSettings.getFont(), font);
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
	 * @return the verticalAlignment
	 */
	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	/**
	 * @param verticalAlignment the verticalAlignment to set
	 */
	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		VerticalAlignment old = getVerticalAlignment();
		this.verticalAlignment = verticalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_verticalAlignment, old, getVerticalAlignment());
	}

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
	

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte position;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			positionValue = EdgeEnum.getByValue(position);
			
			position = null;
			
		}
	}
	
}
