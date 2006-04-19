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

/**
 * Element datasets are used to represent the report data needed to generate a chart or crosstab.
 * The dataset structure may vary with each chart type or crosstab.
 * This is the superinterface for all datasets and contains common dataset properties.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRElementDataset
{

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
	 *  
	 */
	public void collectExpressions(JRExpressionCollector collector);

	/**
	 * Returns the sub dataset run for this chart dataset.
	 * 
	 * @return the sub dataset run for this chart dataset
	 */
	public JRDatasetRun getDatasetRun();

}