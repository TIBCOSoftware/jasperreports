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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseElementDataset implements JRElementDataset, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected byte resetType = JRVariable.RESET_TYPE_NONE;
	protected byte incrementType = JRVariable.RESET_TYPE_NONE;
	protected JRGroup resetGroup = null;
	protected JRGroup incrementGroup = null;
	
	protected JRDatasetRun datasetRun;

	
	protected JRBaseElementDataset()
	{
	}

	
	/**
	 *
	 */
	protected JRBaseElementDataset(JRElementDataset dataset)
	{
		if (dataset != null) {
			resetType = dataset.getResetType();
			incrementType = dataset.getIncrementType();
			resetGroup = dataset.getResetGroup();
			incrementGroup = dataset.getIncrementGroup();
			datasetRun = new JRBaseDatasetRun(dataset.getDatasetRun(), null);
		}
	}


	/**
	 *
	 */
	protected JRBaseElementDataset(JRElementDataset dataset, JRBaseObjectFactory factory)
	{
		factory.put(dataset, this);

		resetType = dataset.getResetType();
		incrementType = dataset.getIncrementType();
		resetGroup = factory.getGroup(dataset.getResetGroup());
		incrementGroup = factory.getGroup(dataset.getIncrementGroup());
		
		datasetRun = factory.getDatasetRun(dataset.getDatasetRun());
	}

	
	/**
	 *
	 */
	public byte getResetType()
	{
		return this.resetType;
	}
		
	/**
	 *
	 */
	public byte getIncrementType()
	{
		return this.incrementType;
	}
		
	/**
	 *
	 */
	public JRGroup getResetGroup()
	{
		return resetGroup;
	}
		
	/**
	 *
	 */
	public JRGroup getIncrementGroup()
	{
		return incrementGroup;
	}


	public JRDatasetRun getDatasetRun()
	{
		return datasetRun;
	}
		
}
