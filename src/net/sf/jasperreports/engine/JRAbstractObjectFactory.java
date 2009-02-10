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
package net.sf.jasperreports.engine;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractObjectFactory implements JRVisitor
{


	/**
	 *
	 */
	private Map objectsMap = new HashMap();
	private Object visitResult = null;


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
	public Object getVisitResult(JRVisitable visitable)
	{
		if (visitable != null)
		{
			visitable.visit(this);
			return visitResult;
		}
		return null;
	}


	/**
	 *
	 */
	protected void setVisitResult(Object visitResult)
	{
		this.visitResult = visitResult;
	}


	/**
	 *
	 */
	public abstract JRReportFont getReportFont(JRReportFont font);

	/**
	 *
	 */
	public abstract JRStyle getStyle(JRStyle style);

	/**
	 * Sets a style or a style reference on an object.
	 * <p/>
	 * If the container includes a style (see {@link JRStyleContainer#getStyle() getStyle()},
	 * a copy of this style will be created via {@link #getStyle(JRStyle) getStyle(JRStyle)}
	 * and set on the object.
	 * <p/>
	 * In addition to this, the implementation needs to handle the case when the container includes
	 * an external style reference (see {@link JRStyleContainer#getStyleNameReference() getStyleNameReference()}.
	 * 
	 * @param setter a setter for the object on which the style should be set.
	 * @param styleContainer the original style container
	 * @see #getStyle(JRStyle)
	 */
	public abstract void setStyle(JRStyleSetter setter, JRStyleContainer styleContainer);
	
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
	public abstract JRPie3DPlot getPie3DPlot(JRPie3DPlot pie3DPlot);


	/**
	 *
	 */
	public abstract JRCategoryDataset getCategoryDataset(JRCategoryDataset categoryDataset);


	/**
	 * 
	 */
	public abstract JRTimeSeriesDataset getTimeSeriesDataset( JRTimeSeriesDataset timeSeriesDataset );

	/**
	 * 
	 */
	public abstract JRTimePeriodDataset getTimePeriodDataset( JRTimePeriodDataset timePeriodDataset );

	/**
	 * 
	 */
	public abstract JRTimePeriodSeries getTimePeriodSeries( JRTimePeriodSeries timePeriodSeries );

	/**
	 * 
	 */
	public abstract JRTimeSeries getTimeSeries( JRTimeSeries timeSeries );

	/**
	 *
	 */
	public abstract JRCategorySeries getCategorySeries(JRCategorySeries categorySeries);

	/**
	 *
	 */
	public abstract JRXyzDataset getXyzDataset( JRXyzDataset xyzDataset );

	/**
	 *
	 */
	public abstract JRXyzSeries getXyzSeries( JRXyzSeries xyzSeries );


	/**
	 *
	 */
	public abstract JRBarPlot getBarPlot(JRBarPlot barPlot);

	/**
	 *
	 */
	public abstract JRBar3DPlot getBar3DPlot( JRBar3DPlot barPlot );


	/**
	 *
	 */
	public abstract JRLinePlot getLinePlot( JRLinePlot linePlot );


	/**
	 *
	 */
	public abstract JRAreaPlot getAreaPlot( JRAreaPlot areaPlot );


	/**
	 *
	 */
	public abstract JRBubblePlot getBubblePlot( JRBubblePlot bubblePlot );


	/**
	 *
	 */
	public abstract JRCandlestickPlot getCandlestickPlot(JRCandlestickPlot candlestickPlot);


	/**
	 *
	 */
	public abstract JRConditionalStyle getConditionalStyle(JRConditionalStyle conditionalStyle, JRStyle parentStyle);

	public abstract JRExpression getExpression(JRExpression expression, boolean assignNotUsedId);
	
	public JRExpression getExpression(JRExpression expression)
	{
		return getExpression(expression, false);
	}
}
