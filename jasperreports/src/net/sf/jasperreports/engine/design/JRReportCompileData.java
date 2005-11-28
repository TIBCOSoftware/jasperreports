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
package net.sf.jasperreports.engine.design;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;

/**
 * Structure used to hold a report's expression evaluator compile data.
 * <p>
 * An instantce consists of expression evaluators for the main report dataset
 * and for sub datasets.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * 
 * @see net.sf.jasperreports.engine.JasperReport#getCompileData()
 */
public class JRReportCompileData implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Main report dataset compile data.
	 */
	private Serializable mainDatasetCompileData;
	
	/**
	 * Map containing compiled data per sub dataset name.
	 */
	private Map datasetCompileData;
	
	/**
	 * Map containing compiled data per crosstab name.
	 */
	private Map crosstabCompileData;
	
	
	/**
	 * Default constructor.
	 */
	public JRReportCompileData()
	{
		datasetCompileData = new HashMap();
		crosstabCompileData = new HashMap();
	}
	
	
	/**
	 * Sets the main dataset compile data.
	 * 
	 * @param compileData the main dataset compile data
	 */
	public void setMainDatasetCompileData(Serializable compileData)
	{
		mainDatasetCompileData = compileData;
	}
	
	
	/**
	 * Sets the compile data for a dataset.
	 * 
	 * @param dataset the dataset
	 * @param compileData the compile data
	 */
	public void setDatasetCompileData(JRDataset dataset, Serializable compileData)
	{
		if (dataset.isMainDataset())
		{
			setMainDatasetCompileData(compileData);
		}
		else
		{
			datasetCompileData.put(dataset.getName(), compileData);
		}
	}
	
	
	/**
	 * Sets the compile data for a crosstab.
	 * 
	 * @param crosstabId the generated crosstab Id, which will be used to retreive the crosstab compile data at fill time.
	 * @param compileData the compile data
	 */
	public void setCrosstabCompileData(int crosstabId, Serializable compileData)
	{
		crosstabCompileData.put(new Integer(crosstabId), compileData);
	}
	
	
	/**
	 * Returns the compile data for the main dataset.
	 * 
	 * @return the compile data for the main dataset
	 */
	public Serializable getMainDatasetCompileData()
	{
		return mainDatasetCompileData;
	}
	
	
	/**
	 * Returns the compile data for a dataset.
	 * 
	 * @param dataset the dataset
	 * @return the compile data
	 * @throws JRException
	 */
	public Serializable getDatasetCompileData(JRDataset dataset) throws JRException
	{
		Serializable compileData;
		if (dataset.isMainDataset())
		{
			compileData = getMainDatasetCompileData();
		}
		else
		{
			compileData = (Serializable) datasetCompileData.get(dataset.getName());
			if (compileData == null)
			{
				throw new JRException("Compile data for dataset " + dataset.getName() + " not found in the report.");
			}
		}
		
		return compileData;
	}
	
	
	/**
	 * Returns the compile data for a crosstab.
	 * 
	 * @param crosstab the crosstab
	 * @return the compile data
	 * @throws JRException
	 */
	public Serializable getCrosstabCompileData(JRCrosstab crosstab) throws JRException
	{
		Serializable compileData = (Serializable) crosstabCompileData.get(new Integer(crosstab.getId()));
		if (compileData == null)
		{
			throw new JRException("Compile data for crosstab not found in the report.");
		}
		
		return compileData;
	}
}
