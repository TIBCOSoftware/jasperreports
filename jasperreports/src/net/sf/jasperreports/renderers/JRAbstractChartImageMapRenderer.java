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
package net.sf.jasperreports.renderers;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImageArea;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;


/**
 * Abstract image map renderer for charts.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractChartImageMapRenderer extends JFreeChartRenderer implements JRImageMapRenderer
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public JRAbstractChartImageMapRenderer(JFreeChart chart)
	{
		super(chart);
	}


	public List getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		//FIXME don't render twice
		ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
		getChart().createBufferedImage((int) renderingArea.getWidth(), (int)  renderingArea.getHeight(), renderingInfo);
		
		EntityCollection entityCollection = renderingInfo.getEntityCollection();
		List areaHyperlinks = null;
		if (entityCollection != null && entityCollection.getEntityCount() > 0)
		{
			areaHyperlinks = new ArrayList(entityCollection.getEntityCount());
			
			for (Iterator it = entityCollection.iterator(); it.hasNext();)
			{
				ChartEntity entity = (ChartEntity) it.next();
				JRPrintHyperlink printHyperlink = getEntityHyperlink(entity);
				if (printHyperlink != null)
				{
					JRPrintImageArea area = getImageArea(entity);

					JRPrintImageAreaHyperlink areaHyperlink = new JRPrintImageAreaHyperlink();
					areaHyperlink.setArea(area);
					areaHyperlink.setHyperlink(printHyperlink);
					areaHyperlinks.add(areaHyperlink);
				}
			}
		}
		
		return areaHyperlinks;
	}

	protected JRPrintImageArea getImageArea(ChartEntity entity)
	{
		JRPrintImageArea area = new JRPrintImageArea();
		area.setShape(JRPrintImageArea.getShape(entity.getShapeType()));
		
		int[] coordinates = getCoordinates(entity);
		if (coordinates != null)
		{
			area.setCoordinates(coordinates);
		}
		return area;
	}
	
	protected int[] getCoordinates(ChartEntity entity)
	{
		int[] coordinates = null;
		String shapeCoords = entity.getShapeCoords();
		if (shapeCoords != null && shapeCoords.length() > 0)
		{
			StringTokenizer tokens = new StringTokenizer(shapeCoords, ",");
			coordinates = new int[tokens.countTokens()];
			int idx = 0;
			while (tokens.hasMoreTokens())
			{
				String coord = tokens.nextToken();
				coordinates[idx] = Integer.parseInt(coord);
				++idx;
			}
		}
		return coordinates;
	}

	protected abstract JRPrintHyperlink getEntityHyperlink(ChartEntity entity);
}
