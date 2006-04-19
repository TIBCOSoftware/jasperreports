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
package net.sf.jasperreports.crosstabs;

import net.sf.jasperreports.engine.JRElementDataset;

/**
 * Input dataset interface used by crosstabs.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCrosstabDataset extends JRElementDataset
{
	/**
	 * Returns whether the crosstab data is pre-sorted according to the
	 * crosstab's groups.
	 * <p>
	 * The crosstab calculation engine can optimize the calculations
	 * if the data is sorted by the row groups and column groups.
	 * For example, if there are two row groups R1 and R2 (subgroup of R1)
	 * and three column groups C1, C2 and C3 the data should be sorted 
	 * by R1, R2, C1, C2, C3.
	 * 
	 * @return whether the crosstab data is pre-sorted
	 */
	public boolean isDataPreSorted();
}
