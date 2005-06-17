/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
package net.sf.jasperreports.engine;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractObjectFactory
{

	
	/**
	 *
	 */
	private Map objectsMap = new HashMap();


	/**
	 *
	 */
	protected Object get(Object object)
	{
		return objectsMap.get(object);
	}

	/**
	 *
	 */
	public void put(Object object, Object copy)
	{
		objectsMap.put(object, copy);
	}


	/**
	 *
	 */
	public abstract JRReportFont getReportFont(JRReportFont font);

	/**
	 *
	 */
	public abstract JRLine getLine(JRLine line);

	/**
	 *
	 */
	public abstract JRRectangle getRectangle(JRRectangle rectangle);

	/**
	 *
	 */
	public abstract JREllipse getEllipse(JREllipse ellipse);

	/**
	 *
	 */
	public abstract JRImage getImage(JRImage image);

	/**
	 *
	 */
	public abstract JRStaticText getStaticText(JRStaticText staticText);

	/**
	 *
	 */
	public abstract JRTextField getTextField(JRTextField textField);

	/**
	 *
	 */
	public abstract JRSubreport getSubreport(JRSubreport subreport);

	/**
	 *
	 */
	public abstract JRPieChart getPieChart(JRPieChart pieChart);

	/**
	 *
	 */
	public abstract JRPieDataset getPieDataset(JRPieDataset pieDataset);

	/**
	 *
	 */
	public abstract JRPiePlot getPiePlot(JRPiePlot piePlot);

	/**
	 *
	 */
	public abstract JRPie3DChart getPie3DChart(JRPie3DChart pie3DChart);

	/**
	 *
	 */
	public abstract JRPie3DPlot getPie3DPlot(JRPie3DPlot pie3DPlot);

	/**
	 *
	 */
	public abstract JRBarChart getBarChart(JRBarChart barChart);

	/**
	 *
	 */
	public abstract JRCategoryDataset getCategoryDataset(JRCategoryDataset categoryDataset);

	/**
	 *
	 */
	public abstract JRBarPlot getBarPlot(JRBarPlot barPlot);

}
