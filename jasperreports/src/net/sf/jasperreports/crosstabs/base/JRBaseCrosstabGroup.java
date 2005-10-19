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
package net.sf.jasperreports.crosstabs.base;

import java.io.Serializable;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * Base read-only implementation for crosstab row and column groups.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseCrosstabGroup implements JRCrosstabGroup, Serializable
{
	protected String name;
	protected byte totalPosition = BucketDefinition.TOTAL_POSITION_NONE;
	protected JRCrosstabBucket bucket;
	
	protected JRCellContents header;
	protected JRCellContents totalHeader;

	protected JRVariable variable;
	
	protected JRBaseCrosstabGroup()
	{
	}

	public JRBaseCrosstabGroup(JRCrosstabGroup group, JRBaseObjectFactory factory)
	{
		factory.put(group, this);
		
		this.name = group.getName();
		this.totalPosition = group.getTotalPosition();
		this.bucket = factory.getCrosstabBucket(group.getBucket());
		
		this.header = factory.getCell(group.getHeader());
		this.totalHeader = factory.getCell(group.getTotalHeader());
		
		this.variable = factory.getVariable(group.getVariable());
	}
	
	public String getName()
	{
		return name;
	}

	public JRCrosstabBucket getBucket()
	{
		return bucket;
	}

	public byte getTotalPosition()
	{
		return totalPosition;
	}

	public boolean hasTotal()
	{
		return totalPosition != BucketDefinition.TOTAL_POSITION_NONE;
	}

	public JRCellContents getHeader()
	{
		return header;
	}

	public JRCellContents getTotalHeader()
	{
		return totalHeader;
	}

	public JRVariable getVariable()
	{
		return variable;
	}
}
