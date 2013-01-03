/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.FooterPositionEnum;


/**
 * Groups represent a flexible way to organize data on a report. A report group is represented by sequence
 * of consecutive records in the data source that have something in common, like the value of a certain report
 * field for example.
 * <p>
 * The value of the associated group expression is what makes group records stick together. This value is the
 * thing that they have in common.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRGroup extends JRCloneable
{


	/**
	 * Gets the group name
	 */
	public String getName();
	
	/**
	 * Gets the flag that signals if the group header should be printed always on a new column.
	 */
	public boolean isStartNewColumn();
		
	/**
	 * Sets the flag that signals if the group header should be printed always on a new column.
	 */
	public void setStartNewColumn(boolean isStart);
		
	/**
	 * Gets the flag that signals if the group header should be printed always on a new page.
	 */
	public boolean isStartNewPage();
		
	/**
	 * Sets the flag that signals if the group header should be printed always on a new page.
	 */
	public void setStartNewPage(boolean isStart);
		
	/**
	 * Gets the flag that signals if the group header should be printed always on a new page, along with the
	 * re-initialization of the page number.
	 */
	public boolean isResetPageNumber();
		
	/**
	 * Sets the flag that signals if the group header should be printed always on a new page, along with the
	 * re-initialization of the page number.
	 */
	public void setResetPageNumber(boolean isReset);
		
	/**
	 * Gets the flag that signals if the group header should be reprinted at the beginning of each page.
	 */
	public boolean isReprintHeaderOnEachPage();
		
	/**
	 * Sets the flag that signals if the group header should be reprinted at the beginning of each page.
	 */
	public void setReprintHeaderOnEachPage(boolean isReprint);
		
	/**
	 * Gets the minimum amount of vertical space needed at the bottom of the column in order to place the
	 * group header on the current column.
	 */
	public int getMinHeightToStartNewPage();

	/**
	 * Sets the minimum amount of vertical space needed at the bottom of the column in order to place the
	 * group header on the current column.
	 */
	public void setMinHeightToStartNewPage(int minHeight);
		
	/**
	 * Specifies how the group footer section behaves with regards to its position on the current page.
	 */
	public FooterPositionEnum getFooterPositionValue();

	/**
	 * Specifies the group footer section behavior with regards to its position on the current page.
	 */
	public void setFooterPosition(FooterPositionEnum footerPosition);
		
	/**
	 * Gets the flag that signals if the group should be prevented from splitting on first break attempt.
	 */
	public boolean isKeepTogether();
		
	/**
	 * Sets the flag that signals if the group should be prevented from splitting on first break attempt.
	 */
	public void setKeepTogether(boolean keepTogether);
		
	/**
	 * Gets the expression that defines what records in the group have in common.
	 */
	public JRExpression getExpression();
	
	/**
	 * Gets the header section created for this group.
	 */
	public JRSection getGroupHeaderSection();
		
	/**
	 * Gets the footer section created for this group.
	 */
	public JRSection getGroupFooterSection();

	/**
	 *
	 */
	public JRVariable getCountVariable();


}
