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
package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.base.JRBaseThermometerPlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;

import java.awt.Color;

/**
 * The layout options of a thermometer chart.
 * 
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignThermometerPlot extends JRBaseThermometerPlot
{


    /**
     *
     */
    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

    
    /**
     * Constructs a new plot that is a copy of an existing one.
     * 
     * @param thermoPlot the plot to copy
     */
    public JRDesignThermometerPlot(JRChartPlot thermoPlot)
    {
        super(thermoPlot);
    }
    
    /**
     * Sets the range of values that can be displayed by this thermometer.
     * Specifies the upper and lower bounds of the display area of the meter.
     * 
     * @param dataRange the range of values to display
     */
    public void setDataRange(JRDataRange dataRange)
    {
        this.dataRange = dataRange;
    }

    /**
     * Sets the formatting option for the textual display of the
     * value.
     * 
     *  @param valueDisplay the value display formatting options
     */
    public void setValueDisplay(JRValueDisplay valueDisplay)
    {
        this.valueDisplay = valueDisplay;
    }
    
        
    /**
     * Turns the display of value lines on and off.
     * 
     * @param showValueLines <code>true</code> to turn value lines on,
     * 						 <code>false</code> to disable them
     */
    public void setShowValueLines(boolean showValueLines)
    {
        this.showValueLines = showValueLines;
    }
        
    /**
     * Sets where to show the textual display of the value.
     * 
     * @param valueLocation where to show the textual display of the value
     */
    public void setValueLocation(byte valueLocation)
    {
        this.valueLocation = valueLocation;
    }

    /**
     * Sets the default color of the mercury in the thermometer.  This color
     * will be used when the value is not in a specified range.
     * 
     * @param mercuryColor the default color of the mercury
     */
    public void setMercuryColor(Color mercuryColor)
    {
        this.mercuryColor = mercuryColor;
    }
    
    /**
     * Specifies the low range of the thermometer.
     * 
     * @param lowRange the low range of the thermometer
     */
    public void setLowRange(JRDataRange lowRange)
    {
        this.lowRange = lowRange;
    }
        
    /**
     * Specifies the medium range of the thermometer.
     * 
     * @param mediumRange the medium range of the thermometer
     */
    public void setMediumRange(JRDataRange mediumRange)
    {
        this.mediumRange = mediumRange;
    }
        
    /**
     * Specifies the high range of the thermometer.
     * 
     * @param highRange the high range of the thermometer
     */
    public void setHighRange(JRDataRange highRange)
    {
        this.highRange = highRange;
    }
}
