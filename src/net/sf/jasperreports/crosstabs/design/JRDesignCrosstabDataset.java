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
package net.sf.jasperreports.crosstabs.design;

import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;

/**
 * Input crosstab dataset implementation to be used at design time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabDataset extends JRDesignElementDataset implements JRCrosstabDataset
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected boolean dataPreSorted = false;

	
	/**
	 * Creates a crosstab dataset.
	 */
	public JRDesignCrosstabDataset()
	{
		super();
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
	}

	public boolean isDataPreSorted()
	{
		return dataPreSorted;
	}

	
	/**
	 * Sets the presorted flag for the dataset.
	 * 
	 * @param dataPreSorted whether the input data is presorted
	 * @see JRCrosstabDataset#isDataPreSorted()
	 */
	public void setDataPreSorted(boolean dataPreSorted)
	{
		this.dataPreSorted = dataPreSorted;
	}

}
