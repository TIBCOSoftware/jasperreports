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
package net.sf.jasperreports.engine;


/**
 * Datasets are used to represent the actual data needed to generate a chart. The dataset structure may vary with each chart type. This
 * is the superinterface for all datasets and contains common dataset properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRChartDataset
{
	public static final byte PIE_DATASET = 1;
	public static final byte CATEGORY_DATASET = 2;
	public static final byte XY_DATASET = 3;
	public static final byte XYZ_DATASET = 4;
	public static final byte TIMEPERIOD_DATASET = 5;
	public static final byte TIMESERIES_DATASET = 6;
	public static final byte HIGHLOW_DATASET = 7;
	
	/**
	 * Gets the reset type. This specifies the range of report data used for filling the dataset.
	 * @return one of the reset constants in {@link JRVariable}
	 */
	public byte getResetType();

	/**
	 * Gets the selected reset group in case of reset type group.
	 */
	public JRGroup getResetGroup();
		
	/**
	 * Returns the increment type. This specifies dataset values increment step.
	 * @return one of the reset constants in {@link JRVariable}, since the increment type uses the same
	 * constants as the reset type.
	 */
	public byte getIncrementType();

	/**
	 * Gets the selected increment group in case of increment type group.
	 */
	public JRGroup getIncrementGroup();
	
	/**
	 * Gets the dataset type. Must be one of the dataset type constants defined in this class.
	 */
	public byte getDatasetType();
		
	/**
	 *  
	 */
	public void collectExpressions(JRExpressionCollector collector);

}
