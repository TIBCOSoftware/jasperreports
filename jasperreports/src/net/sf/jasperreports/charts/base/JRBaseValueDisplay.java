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
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

import java.awt.Color;
import java.io.Serializable;

/**
 * An immutable representation of the formatting options for showing the
 * value of a value dataset.  Used by charts that display a single value,
 * such as a Meter or Thermometer.
 * 
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseValueDisplay implements JRValueDisplay, Serializable
{


    /**
     *
     */
    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

    /**
     * The color to use when writing the value.
     */
    protected Color color = null;
    
    /**
     * The formatting mask to use when writing the value.  Must a pattern
     * that is accepted by a code>java.text.DecimalFormat</code> object.
     */
    protected String mask = null;
    
    /**
     * The font to use when writing the value.
     */
    protected JRFont font = null;
    
    
    /**
     * Constructs a copy of an existing value format specification.
     * 
     * @param valueDisplay the value formatting object to copy
     */
    public JRBaseValueDisplay(JRValueDisplay valueDisplay)
    {
        if (valueDisplay != null)
        {
            color = valueDisplay.getColor();
            mask = valueDisplay.getMask();
            font = valueDisplay.getFont();
        }
    }
    
    /**
     * Constructs a copy of an existing value format specification and registers
     * any expression in the new copy with the specified factory.
     * 
     * @param valueDisplay the value formatting object to copy
     * @param factory the factory object to register expressions with
     */
    public JRBaseValueDisplay(JRValueDisplay valueDisplay, JRBaseObjectFactory factory)
    {
        factory.put(valueDisplay, this);
        
        if (valueDisplay != null)
        {
            color = valueDisplay.getColor();
            mask = valueDisplay.getMask();
            font = valueDisplay.getFont();
        }
    }
        
        
    /**
     *
     */
    public Color getColor()
    {
        return color;
    }
    /**
     *
     */
    public String getMask()
    {
        return mask;
    }
    /**
     *
     */
    public JRFont getFont()
    {
        return font;
    }

    /**
     * Adds all the expression used by this plot with the specified collector.
     * All collected expression that are also registered with a factory will
     * be included with the report is compiled.
     * 
     * @param collector the expression collector to use
     */
    public void collectExpressions(JRExpressionCollector collector)
    {
    }

}
