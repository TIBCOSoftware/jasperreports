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
package net.sf.jasperreports.crosstabs.fill;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.fill.JRFillCellContents;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRFillVariable;

/**
 * Base crosstab row/column group implementation used at fill time. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillCrosstabGroup implements JRCrosstabGroup
{
	protected JRCrosstabGroup parentGroup;
	protected JRFillCellContents header;
	protected JRFillCellContents totalHeader;
	protected JRFillVariable variable;

	public JRFillCrosstabGroup(JRCrosstabGroup group, JRFillObjectFactory factory)
	{
		factory.put(group, this);
		
		parentGroup = group;
		
		header = factory.getCell(group.getHeader());
		totalHeader = factory.getCell(group.getTotalHeader());
		
		variable = factory.getVariable(group.getVariable());
	}

	public String getName()
	{
		return parentGroup.getName();
	}

	public byte getTotalPosition()
	{
		return parentGroup.getTotalPosition();
	}

	public boolean hasTotal()
	{
		return parentGroup.hasTotal();
	}

	public JRCrosstabBucket getBucket()
	{
		return parentGroup.getBucket();
	}

	public JRCellContents getHeader()
	{
		return header;
	}

	public JRCellContents getTotalHeader()
	{
		return totalHeader;
	}

	public JRFillCellContents getFillHeader()
	{
		return header;
	}

	public JRFillCellContents getFillTotalHeader()
	{
		return totalHeader;
	}
	
	public JRVariable getVariable()
	{
		return variable;
	}
	
	public JRFillVariable getFillVariable()
	{
		return variable;
	}

}
