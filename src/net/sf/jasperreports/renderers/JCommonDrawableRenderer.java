/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.renderers;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRConstants;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.Drawable;


/**
 * A wrapper for the Drawable interface in the JCommon library: you will need the
 * JCommon classes in your classpath to compile this class. In particular this can be
 * used to allow JFreeChart objects to be included in the output report in vector form.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JCommonDrawableRenderer extends JRAbstractSvgRenderer
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */	
	private Drawable drawable = null;
	private LegendItemSource[] legendItemSources = null;


	/**
	 *
	 */	
	public JCommonDrawableRenderer(Drawable drawable) 
	{
		this.drawable = drawable;
		LegendTitle legend = ((JFreeChart)drawable).getLegend();
		if (legend != null)
		{
			legendItemSources = legend.getSources();
		}
	}


	/**
	 *
	 */
	public void render(Graphics2D grx, Rectangle2D rectangle) 
	{
		if (drawable != null) 
		{
			//FIXME remove this when upgrading JFreeChart
			//-- fix to avoid bug in JFreeChart RC1 http://www.jfree.org/phpBB2/viewtopic.php?t=13275&highlight=legend+transient
			LegendTitle legend = ((JFreeChart)drawable).getLegend();
			if (legend != null && legendItemSources != null)
			{
				legend.setSources(legendItemSources);
			}
			//-- end fix
			drawable.draw(grx, rectangle);
		}
	}

	
}
