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
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class AxisSettings implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_axisVisible = "axisVisible";
//FIXMETHEME complete props
//	public static final String AXIS_LOCATION = "axisLocation";
//	public static final String AXIS_LINE_PAINT = "axisLinePaint";
//	public static final String AXIS_LINE_STROKE = "axisLineStroke";
//	public static final String AXIS_LINE_VISIBLE = "axisLineVisible";
//	public static final String AXIS_FIXED_DIMENSION = "axisFixedDimension";
//	public static final String AXIS_LABEL = "axisLabel";
//	public static final String AXIS_LABEL_ANGLE = "axisLabelAngle";
//	public static final String AXIS_LABEL_PAINT = "axisLabelPaint";
//	public static final String AXIS_LABEL_FONT = "axisLabelFont";
//	public static final String AXIS_LABEL_FONT_SIZE = "axisLabelFontSize";
//	public static final String AXIS_LABEL_FONT_BOLD_STYLE = "axisLabelFontBoldStyle";
//	public static final String AXIS_LABEL_FONT_ITALIC_STYLE = "axisLabelFontItalicStyle";
//	public static final String AXIS_LABEL_INSETS = "axisLabelInsets";
//	public static final String AXIS_LABEL_VISIBLE = "axisLabelVisible";
//	public static final String AXIS_TICK_LABEL_PAINT = "axisTickLabelPaint";
//	public static final String AXIS_TICK_LABEL_FONT = "axisTickLabelFont";
//	public static final String AXIS_TICK_LABEL_FONT_SIZE = "axisTickLabelFontSize";
//	public static final String AXIS_TICK_LABEL_FONT_BOLD_STYLE = "axisTickLabelFontBoldStyle";
//	public static final String AXIS_TICK_LABEL_FONT_ITALIC_STYLE = "axisTickLabelFontItalicStyle";
//	public static final String AXIS_TICK_LABEL_INSETS = "axisTickLabelInsets";
//	public static final String AXIS_TICK_LABELS_VISIBLE = "axisTickLabelsVisible";
//	public static final String AXIS_TICK_MARKS_INSIDE_LENGTH = "axisTickMarksInsideLength";
//	public static final String AXIS_TICK_MARKS_OUTSIDE_LENGTH = "axisTickMarksOutsideLength";
//	public static final String AXIS_TICK_MARKS_PAINT = "axisTickMarksPaint";
//	public static final String AXIS_TICK_MARKS_STROKE = "axisTickMarksStroke";
//	public static final String AXIS_TICK_MARKS_VISIBLE = "axisTickMarksVisible";
//	public static final String AXIS_MIN_VALUE = "minValue";
//	public static final String AXIS_MAX_VALUE = "maxValue";
//	public static final String AXIS_TICK_COUNT = "tickCount";

	/**
	 *
	 */
	private Boolean axisVisible = null;
	
	/**
	 *
	 */
	public AxisSettings()
	{
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
	 * @return the axisVisible
	 */
	public Boolean getAxisVisible() {
		return axisVisible;
	}

	/**
	 * @param axisVisible the axisVisible to set
	 */
	public void setAxisVisible(Boolean axisVisible) {
		Boolean old = getAxisVisible();
		this.axisVisible = axisVisible;
		getEventSupport().firePropertyChange(PROPERTY_axisVisible, old, getAxisVisible());
	}

}
