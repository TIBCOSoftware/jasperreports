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
package net.sf.jasperreports.olap.mondrian;

import mondrian.olap.Axis;
import mondrian.olap.Cell;
import mondrian.olap.Query;
import mondrian.olap.Result;
import net.sf.jasperreports.olap.result.JROlapCell;
import net.sf.jasperreports.olap.result.JROlapResult;
import net.sf.jasperreports.olap.result.JROlapResultAxis;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRMondrianResult implements JROlapResult
{
	
	private final Result result;
	private final JRMondrianAxis[] axes;
	
	public JRMondrianResult(Result result)
	{
		this.result = result;

		JRMondrianFactory factory = new JRMondrianFactory();

		Query query = result.getQuery();
		Axis[] resultAxes = result.getAxes();
		axes = new JRMondrianAxis[resultAxes.length];
		for (int i = 0; i < resultAxes.length; i++)
		{
			axes[i] = new JRMondrianAxis(resultAxes[i], query.getMdxHierarchiesOnAxis(i), factory);
		}
	}

	public JROlapResultAxis[] getAxes()
	{
		return axes;
	}

	public JROlapCell getCell(int[] axisPositions)
	{
		Cell dataCell = result.getCell(axisPositions);
		JRMondrianCell cell = new JRMondrianCell(dataCell);
		return cell;
	}

}
