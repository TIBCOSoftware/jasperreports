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
package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRDesignHighLowDataset extends JRDesignChartDataset implements JRHighLowDataset
{

	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	protected JRExpression seriesExpression;
	protected JRExpression dateExpression;
	protected JRExpression highExpression;
	protected JRExpression lowExpression;
	protected JRExpression openExpression;
	protected JRExpression closeExpression;
	protected JRExpression volumeExpression;


	public JRExpression getSeriesExpression()
	{
		return seriesExpression;
	}


	public void setSeriesExpression(JRExpression seriesExpression)
	{
		this.seriesExpression = seriesExpression;
	}

	public JRExpression getDateExpression()
	{
		return dateExpression;
	}


	public void setDateExpression(JRExpression dateExpression)
	{
		this.dateExpression = dateExpression;
	}


	public JRExpression getHighExpression()
	{
		return highExpression;
	}


	public void setHighExpression(JRExpression highExpression)
	{
		this.highExpression = highExpression;
	}


	public JRExpression getLowExpression()
	{
		return lowExpression;
	}


	public void setLowExpression(JRExpression lowExpression)
	{
		this.lowExpression = lowExpression;
	}


	public JRExpression getOpenExpression()
	{
		return openExpression;
	}


	public void setOpenExpression(JRExpression openExpression)
	{
		this.openExpression = openExpression;
	}


	public JRExpression getCloseExpression()
	{
		return closeExpression;
	}


	public void setCloseExpression(JRExpression closeExpression)
	{
		this.closeExpression = closeExpression;
	}


	public JRExpression getVolumeExpression()
	{
		return volumeExpression;
	}


	public void setVolumeExpression(JRExpression volumeExpression)
	{
		this.volumeExpression = volumeExpression;
	}
}

