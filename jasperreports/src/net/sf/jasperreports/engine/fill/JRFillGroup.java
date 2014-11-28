/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRFillGroup implements JRGroup
{


	/**
	 *
	 */
	protected JRGroup parent;

	/**
	 *
	 */
	private JRFillSection groupHeaderSection;
	private JRFillSection groupFooterSection;
	private JRVariable countVariable;
	
	private boolean startNewColumn;
	private boolean startNewPage;
	private boolean resetPageNumber;

	/**
	 *
	 */
	private boolean hasChanged = true;
	private boolean isTopLevelChange;
	private boolean isHeaderPrinted;
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

		JRBaseFiller filler = factory.getFiller();
		if (filler != null)
		{
			String reportName = filler.isSubreport() ? factory.getFiller().getJasperReport().getName() : null;
			
			groupHeaderSection = factory.getSection(group.getGroupHeaderSection());
			if (groupHeaderSection != factory.getFiller().missingFillSection)
			{
				groupHeaderSection.setOrigin(
					new JROrigin(
						reportName,
						group.getName(),
						BandTypeEnum.GROUP_HEADER
						)
					);
			}

			groupFooterSection = factory.getSection(group.getGroupFooterSection());
			if (groupFooterSection != factory.getFiller().missingFillSection)
			{
				groupFooterSection.setOrigin(
					new JROrigin(
						reportName,
						group.getName(),
						BandTypeEnum.GROUP_FOOTER
						)
					);
			}
		}

		countVariable = factory.getVariable(group.getCountVariable());
		
		startNewColumn = parent.isStartNewColumn();
		startNewPage = parent.isStartNewPage();
		resetPageNumber = parent.isResetPageNumber();
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
		return startNewColumn;
	}
		
	/**
	 *
	 */
	public void setStartNewColumn(boolean isStart)
	{
		this.startNewColumn = isStart;
	}
		
	/**
	 *
	 */
	public boolean isStartNewPage()
	{
		return startNewPage;
	}
		
	/**
	 *
	 */
	public void setStartNewPage(boolean isStart)
	{
		this.startNewPage = isStart;
	}
		
	/**
	 *
	 */
	public boolean isResetPageNumber()
	{
		return resetPageNumber;
	}
		
	/**
	 *
	 */
	public void setResetPageNumber(boolean isReset)
	{
		this.resetPageNumber = isReset;
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
	public FooterPositionEnum getFooterPositionValue()
	{
		return parent.getFooterPositionValue();
	}
		
	/**
	 *
	 */
	public void setFooterPosition(FooterPositionEnum footerPosition)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public boolean isKeepTogether()
	{
		return parent.isKeepTogether();
	}
		
	/**
	 *
	 */
	public void setKeepTogether(boolean keepTogether)
	{
	}
		
	/**
	 *
	 */
	public JRSection getGroupHeaderSection()
	{
		return groupHeaderSection;
	}
		
	/**
	 *
	 */
	public JRSection getGroupFooterSection()
	{
		return groupFooterSection;
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

	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}

}
