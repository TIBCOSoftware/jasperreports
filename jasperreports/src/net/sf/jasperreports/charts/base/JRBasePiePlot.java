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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePiePlot extends JRBaseChartPlot implements JRPiePlot
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_CIRCULAR = "circular";

	protected boolean isCircular = true;
	
	/**
	 *
	 */
	public JRBasePiePlot(JRChartPlot piePlot, JRChart chart)
	{
		super(piePlot, chart);
	}


	/**
	 *
	 */
	public JRBasePiePlot(JRPiePlot piePlot, JRBaseObjectFactory factory)
	{
		super(piePlot, factory);
		isCircular = piePlot.isCircular();
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
	}


	/**
	 * @return the isCircular
	 */
	public boolean isCircular() {
		return isCircular;
	}


	/**
	 * @param isCircular the isCircular to set
	 */
	public void setCircular(boolean isCircular) {
		boolean old = this.isCircular;
		this.isCircular = isCircular;
		getEventSupport().firePropertyChange(PROPERTY_CIRCULAR, old, this.isCircular);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		ObjectInputStream.GetField fields = in.readFields();
		isCircular = fields.get("isCircular", true);
	}
}
