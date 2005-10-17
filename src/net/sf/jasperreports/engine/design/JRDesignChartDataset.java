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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRDesignChartDataset extends JRBaseChartDataset
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	
	public JRDesignChartDataset()
	{
		super();
	}

	/**
	 *
	 */
	public JRDesignChartDataset(JRChartDataset dataset)
	{
		super(dataset);
	}


	/**
	 *
	 */
	public JRDesignChartDataset(JRChartDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);
	}


	/**
	 *
	 */
	public void setResetType(byte resetType)
	{
		this.resetType = resetType;
	}
		
	/**
	 *
	 */
	public void setIncrementType(byte incrementType)
	{
		this.incrementType = incrementType;
	}
		
	/**
	 *
	 */
	public void setResetGroup(JRGroup group)
	{
		this.resetGroup = group;
	}
		
	/**
	 *
	 */
	public void setIncrementGroup(JRGroup group)
	{
		this.incrementGroup = group;
	}

	/**
	 * 
	 */
	public byte getDatasetType() {
		return -1;
	}
	
	
	/**
	 * Sets the sub dataset run for this dataset.
	 * 
	 * @param datasetRun the dataset run
	 * @see JRChartDataset#getDatasetRun()
	 */
	public void setDatasetRun(JRDatasetRun datasetRun)
	{
		this.datasetRun = datasetRun;
	}
}
