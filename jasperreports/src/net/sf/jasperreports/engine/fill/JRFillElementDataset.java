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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillElementDataset implements JRElementDataset
{


	/**
	 *
	 */
	protected JRElementDataset parent = null;
	private final JRBaseFiller filler;

	protected JRGroup resetGroup = null;
	protected JRGroup incrementGroup = null;

	private boolean isIncremented = true;

	protected JRFillDatasetRun datasetRun;

	/**
	 *
	 */
	protected JRFillElementDataset(
		JRElementDataset dataset, 
		JRFillObjectFactory factory
		)
	{
		factory.put(dataset, this);

		parent = dataset;
		filler = factory.getFiller();
		
		resetGroup = factory.getGroup(dataset.getResetGroup());
		incrementGroup = factory.getGroup(dataset.getIncrementGroup());
		
		datasetRun = factory.getDatasetRun(dataset.getDatasetRun());
	}


	/**
	 *
	 */
	public byte getResetType()
	{
		return parent.getResetType();
	}
		
	/**
	 *
	 */
	public byte getIncrementType()
	{
		return parent.getIncrementType();
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
		
	/**
	 *
	 */
	protected void initialize()
	{
		customInitialize();
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		customEvaluate(calculator);
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void increment()
	{
		if (!isIncremented)
		{
			customIncrement();
		}
		isIncremented = true;
	}


	/**
	 *
	 */
	protected abstract void customInitialize();

	/**
	 *
	 */
	protected abstract void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException;

	/**
	 *
	 */
	protected abstract void customIncrement();


	public JRDatasetRun getDatasetRun()
	{
		return datasetRun;
	}
	
	
	public void evaluateDatasetRun(byte evaluation) throws JRException
	{
		if (datasetRun != null)
		{
			datasetRun.evaluate(this, evaluation);
		}
	}
	
	public JRFillDataset getInputDataset()
	{
		JRFillDataset inputDataset;
		if (datasetRun != null)
		{
			inputDataset = datasetRun.getDataset();
		}
		else
		{
			inputDataset = filler.mainDataset;
		}
		
		return inputDataset;
	}
}
