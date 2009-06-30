/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.fill;

import java.awt.Color;

import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillItemLabel implements JRItemLabel
{

	/**
	 *
	 */
	protected JRItemLabel parent = null;

	/**
	 *
	 */
	protected JRChart chart = null;

	/**
	 *
	 */
	public JRFillItemLabel(JRItemLabel itemLabel, JRFillObjectFactory factory)
	{
		factory.put(itemLabel, this);

		parent = itemLabel;
		
		chart = (JRChart)factory.getVisitResult(itemLabel.getChart());
	}

	/**
	 *
	 */
	public JRChart getChart()
	{
		return chart;
	}
	
	/**
	 *
	 */
	public Color getColor()
	{
		return parent.getColor();
	}

	/**
	 *
	 */
	public Color getBackgroundColor()
	{
		return parent.getBackgroundColor();
	}

	/**
	 *
	 */
//	public String getMask(){
//		return parent.getMask();
//	}

	/**
	 *
	 */
	public JRFont getFont()
	{
		return parent.getFont();
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
}
