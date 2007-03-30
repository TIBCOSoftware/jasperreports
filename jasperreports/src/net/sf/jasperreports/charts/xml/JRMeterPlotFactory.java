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
package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;


/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRMeterPlotFactory extends JRBaseFactory
{
	public static final String ELEMENT_meterPlot = "meterPlot";
	
    public static final String ATTRIBUTE_shape = "shape";
    public static final String ATTRIBUTE_angle = "angle";
    public static final String ATTRIBUTE_units = "units";
    public static final String ATTRIBUTE_tickInterval = "tickInterval";
    public static final String ATTRIBUTE_meterColor = "meterColor";
    public static final String ATTRIBUTE_needleColor = "needleColor";
    public static final String ATTRIBUTE_tickColor = "tickColor";
    
	/**
	 *
	 */
	public Object createObject(Attributes atts) throws JRException
	{
		JRChart chart = (JRChart)digester.peek();
		JRDesignMeterPlot meterPlot = (JRDesignMeterPlot)chart.getPlot();

        String shapeAttr = atts.getValue(ATTRIBUTE_shape);
        Byte shape = (Byte)JRXmlConstants.getMeterShapeMap().get(shapeAttr);
        if (shape == null)
        {
            throw new JRException("Invalid meter shape: " + shapeAttr);
        }
        else
        {
            meterPlot.setShape(shape.byteValue());
        }
        
        String angle = atts.getValue(ATTRIBUTE_angle);
        if (angle != null && angle.length() > 0)
        {
            meterPlot.setMeterAngle(Integer.parseInt(angle));
        }
        
        String units = atts.getValue(ATTRIBUTE_units);
        if (units != null && units.length() > 0)
        {
            meterPlot.setUnits(units);
        }
        
        String tickInterval = atts.getValue(ATTRIBUTE_tickInterval);
        if (tickInterval != null && tickInterval.length() > 0)
        {
            meterPlot.setTickInterval(Double.parseDouble(tickInterval));
        }
        
        String meterColor = atts.getValue(ATTRIBUTE_meterColor);
        if (meterColor != null && meterColor.length() > 0)
        {
            meterPlot.setMeterBackgroundColor(JRXmlConstants.getColor(meterColor, null));
        }
        
        String needleColor = atts.getValue(ATTRIBUTE_needleColor);
        if (needleColor != null && needleColor.length() > 0)
        {
            meterPlot.setNeedleColor(JRXmlConstants.getColor(needleColor, null));
        }
        
        String tickColor = atts.getValue(ATTRIBUTE_tickColor);
        if (tickColor != null && tickColor.length() > 0)
        {
            meterPlot.setTickColor(JRXmlConstants.getColor(tickColor, null));
        }
        
		return meterPlot;
	}
}
