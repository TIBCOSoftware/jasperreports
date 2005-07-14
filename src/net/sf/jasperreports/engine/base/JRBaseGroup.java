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

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseGroup implements JRGroup, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10000;

	/**
	 *
	 */
	protected String name = null;
	protected boolean isStartNewColumn = false;
	protected boolean isStartNewPage = false;
	protected boolean isResetPageNumber = false;
	protected boolean isReprintHeaderOnEachPage = false;
	protected int minHeightToStartNewPage = 0;

	/**
	 *
	 */
	protected JRExpression expression = null;
	protected JRBand groupHeader = null;
	protected JRBand groupFooter = null;
	protected JRVariable countVariable = null;
	

	/**
	 *
	 */
	protected JRBaseGroup()
	{
	}
		

	/**
	 *
	 */
	protected JRBaseGroup(JRGroup group, JRBaseObjectFactory factory)
	{
		factory.put(group, this);
		
		name = group.getName();
		isStartNewColumn = group.isStartNewColumn();
		isStartNewPage = group.isStartNewPage();
		isResetPageNumber = group.isResetPageNumber();
		isReprintHeaderOnEachPage = group.isReprintHeaderOnEachPage();
		minHeightToStartNewPage = group.getMinHeightToStartNewPage();
		
		expression = factory.getExpression(group.getExpression());

		groupHeader = factory.getBand(group.getGroupHeader());
		groupFooter = factory.getBand(group.getGroupFooter());
		countVariable = factory.getVariable(group.getCountVariable());
	}
		

	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 *
	 */
	public boolean isStartNewColumn()
	{
		return this.isStartNewColumn;
	}
		
	/**
	 *
	 */
	public void setStartNewColumn(boolean isStart)
	{
		this.isStartNewColumn = isStart;
	}
		
	/**
	 *
	 */
	public boolean isStartNewPage()
	{
		return this.isStartNewPage;
	}
		
	/**
	 *
	 */
	public void setStartNewPage(boolean isStart)
	{
		this.isStartNewPage = isStart;
	}
		
	/**
	 *
	 */
	public boolean isResetPageNumber()
	{
		return this.isResetPageNumber;
	}
		
	/**
	 *
	 */
	public void setResetPageNumber(boolean isReset)
	{
		this.isResetPageNumber = isReset;
	}
		
	/**
	 *
	 */
	public boolean isReprintHeaderOnEachPage()
	{
		return this.isReprintHeaderOnEachPage;
	}
		
	/**
	 *
	 */
	public void setReprintHeaderOnEachPage(boolean isReprint)
	{
		this.isReprintHeaderOnEachPage = isReprint;
	}
		
	/**
	 *
	 */
	public int getMinHeightToStartNewPage()
	{
		return this.minHeightToStartNewPage;
	}

	/**
	 *
	 */
	public void setMinHeightToStartNewPage(int minHeight)
	{
		this.minHeightToStartNewPage = minHeight;
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.expression;
	}
	
	/**
	 *
	 */
	public JRBand getGroupHeader()
	{
		return this.groupHeader;
	}
		
	/**
	 *
	 */
	public JRBand getGroupFooter()
	{
		return this.groupFooter;
	}
		
	/**
	 *
	 */
	public JRVariable getCountVariable()
	{
		return this.countVariable;
	}


}
