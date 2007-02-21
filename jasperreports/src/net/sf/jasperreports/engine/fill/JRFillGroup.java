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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillGroup implements JRGroup
{


	/**
	 *
	 */
	protected JRGroup parent = null;

	/**
	 *
	 */
	private JRBand groupHeader = null;
	private JRBand groupFooter = null;
	private JRVariable countVariable = null;

	/**
	 *
	 */
	private boolean hasChanged = true;
	private boolean isTopLevelChange = false;
	private boolean isHeaderPrinted = false;
	private boolean isFooterPrinted = true;
	

	/**
	 *
	 */
	public JRFillGroup(
		JRGroup group, 
		JRFillObjectFactory factory
		)
	{
		factory.put(group, this);

		parent = group;

		groupHeader = factory.getBand(group.getGroupHeader());
		groupFooter = factory.getBand(group.getGroupFooter());
		countVariable = factory.getVariable(group.getCountVariable());
	}


	/**
	 *
	 */
	public String getName()
	{
		return parent.getName();
	}
	
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return parent.getExpression();
	}
		
	/**
	 *
	 */
	public boolean isStartNewColumn()
	{
		return parent.isStartNewColumn();
	}
		
	/**
	 *
	 */
	public void setStartNewColumn(boolean isStart)
	{
		parent.setStartNewColumn(isStart);
	}
		
	/**
	 *
	 */
	public boolean isStartNewPage()
	{
		return parent.isStartNewPage();
	}
		
	/**
	 *
	 */
	public void setStartNewPage(boolean isStart)
	{
		parent.setStartNewPage(isStart);
	}
		
	/**
	 *
	 */
	public boolean isResetPageNumber()
	{
		return parent.isResetPageNumber();
	}
		
	/**
	 *
	 */
	public void setResetPageNumber(boolean isReset)
	{
		parent.setResetPageNumber(isReset);
	}
		
	/**
	 *
	 */
	public boolean isReprintHeaderOnEachPage()
	{
		return parent.isReprintHeaderOnEachPage();
	}
		
	/**
	 *
	 */
	public void setReprintHeaderOnEachPage(boolean isReprint)
	{
	}
		
	/**
	 *
	 */
	public int getMinHeightToStartNewPage()
	{
		return parent.getMinHeightToStartNewPage();
	}
		
	/**
	 *
	 */
	public void setMinHeightToStartNewPage(int minHeight)
	{
	}
		
	/**
	 *
	 */
	public JRBand getGroupHeader()
	{
		return groupHeader;
	}
		
	/**
	 *
	 */
	public JRBand getGroupFooter()
	{
		return groupFooter;
	}
		
	/**
	 *
	 */
	public JRVariable getCountVariable()
	{
		return countVariable;
	}
	
	/**
	 *
	 */
	public boolean hasChanged()
	{
		return hasChanged;
	}
		
	/**
	 *
	 */
	public void setHasChanged(boolean hasChanged)
	{
		this.hasChanged = hasChanged;
	}

	/**
	 *
	 */
	public boolean isTopLevelChange()
	{
		return isTopLevelChange;
	}
		
	/**
	 *
	 */
	public void setTopLevelChange(boolean isTopLevelChange)
	{
		this.isTopLevelChange = isTopLevelChange;
	}

	/**
	 *
	 */
	public boolean isHeaderPrinted()
	{
		return isHeaderPrinted;
	}
			
	/**
	 *
	 */
	public void setHeaderPrinted(boolean isHeaderPrinted)
	{
		this.isHeaderPrinted = isHeaderPrinted;
	}

	/**
	 *
	 */
	public boolean isFooterPrinted()
	{
		return isFooterPrinted;
	}
		
	/**
	 *
	 */
	public void setFooterPrinted(boolean isFooterPrinted)
	{
		this.isFooterPrinted = isFooterPrinted;
	}


}
