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
package net.sf.jasperreports.charts;

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.engine.JRChartPlot;

import java.awt.Color;
import java.util.List;


/**
 * Represents the display options of a Meter Chart.  A meter chart consists of a dial,
 * a needle pointing to the current value on the dial, and the value itself.  The Meter
 * can be broken up into shaded regions to highlight portions of the range.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public interface JRMeterPlot extends JRChartPlot
{
	/**
	 * The portion of the circle described by the Meter that is not occupied by the
	 * Meter is drawn as a chord.  (A straight line connects the ends.)
	 */
	public static final byte SHAPE_CHORD = 0;

	/**
	 * The portion of the circle described by the Meter that is not occupied by the
	 * Meter is drawn as a circle.
	 */
	public static final byte SHAPE_CIRCLE = 1;

	/**
	 * The portion of the circle described by the Meter that is not occupied by the
	 * Meter is not drawn.
	 */
	public static final byte SHAPE_PIE = 2;

	/**
	 * Returns the range of values that the Meter can display.
	 *
	 * @return the range of values that the Meter can display
	 */
	public JRDataRange getDataRange();

	/**
	 * Returns a description of how the value of the Meter is displayed.  This includes
	 * any font and color information, the location of the value, a formatting mask and
	 * an optional "units" string to append to the value.
	 *
	 * @return a description of how the value of the Meter is displayed.
	 */
	public JRValueDisplay getValueDisplay();

	/**
	 * @deprecated Replaced by {@link #getShapeByte()}
	 */
	public byte getShape();

	/**
	 * Returns the shape of the Meter.  The shape is only relevant if the Meter face is
	 * over 180 degrees wide, and controls how the portion of the circle described by the
	 * Meter but outside of the Meter is drawn.  (If the meter is 240 degrees wide the shape
	 * setting controls how the remaining 120 degrees is displayed.)
	 * <br><br>
	 * The value returned is one of the <code>SHAPE_</code> constants defined in this class.
	 *
	 * @return a description of how the value of the Meter is displayed.
	 */
	public Byte getShapeByte();

	/**
	 * Returns a list of all the intervals contained in this Meter.  The return value is never
	 * <code>null</code> but can be an empty list.  Each element in the list is a
	 * {@link net.sf.jasperreports.charts.util.JRMeterInterval <code>net.sf.jasperreports.charts.util.JRMeterInterval</code>}
	 *
	 * @return a list of all the intervals contained in this Meter
	 */
	public List getIntervals();

	/**
	 * @deprecated Replaced by {@link #getMeterAngleInteger()}
	 */
	public int getMeterAngle();

	/**
	 * Returns the size of the Meter face in degrees.
	 *
	 * @return the size of the Meter face in degrees
	 */
	public Integer getMeterAngleInteger();

	/**
	 * Returns the name of the units that the Meter is displaying.  This value will be
	 * appended to the value when displayed.
	 *
	 * @return the name of the units that the Meter is displaying
	 */
	public String getUnits();

	/**
	 * @deprecated Replaced by {@link #getTickIntervalDouble()}
	 */
	public double getTickInterval();

	/**
	 * Returns the spacing between the ticks on the face of the meter.  The spacing is relative
	 * to the range that the meter is displaying - if the range is 0 to 500 and the tick interval
	 * is 50 then 10 ticks will be displayed.
	 *
	 * @return the spacing between the ticks on the face of the meter
	 */
	public Double getTickIntervalDouble();

	/**
	 * Returns the background color of the meter.  This is the color of the meter's face.
	 *
	 * @return the background color of the meter.
	 */
	public Color getMeterBackgroundColor();

	/**
	 * Returns the color used when drawing the meter's pointer.
	 *
	 * @return the color used when drawing the meter's pointer
	 */
	public Color getNeedleColor();

	/**
	 * Returns the color used when drawing tick marks on the meter.
	 *
	 * @return the color used when drawing tick marks on the meter
	 */
	public Color getTickColor();
}
