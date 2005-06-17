/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRGroupFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_name = "name";
	private static final String ATTRIBUTE_isStartNewColumn = "isStartNewColumn";
	private static final String ATTRIBUTE_isStartNewPage = "isStartNewPage";
	private static final String ATTRIBUTE_isResetPageNumber = "isResetPageNumber";
	private static final String ATTRIBUTE_isReprintHeaderOnEachPage = "isReprintHeaderOnEachPage";
	private static final String ATTRIBUTE_minHeightToStartNewPage = "minHeightToStartNewPage";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignGroup group = new JRDesignGroup();
		
		group.setName(atts.getValue(ATTRIBUTE_name));
		
		String isStartNewColumn = atts.getValue(ATTRIBUTE_isStartNewColumn);
		if (isStartNewColumn != null && isStartNewColumn.length() > 0)
		{
			group.setStartNewColumn(Boolean.valueOf(isStartNewColumn).booleanValue());
		}

		String isStartNewPage = atts.getValue(ATTRIBUTE_isStartNewPage);
		if (isStartNewPage != null && isStartNewPage.length() > 0)
		{
			group.setStartNewPage(Boolean.valueOf(isStartNewPage).booleanValue());
		}

		String isResetPageNumber = atts.getValue(ATTRIBUTE_isResetPageNumber);
		if (isResetPageNumber != null && isResetPageNumber.length() > 0)
		{
			group.setResetPageNumber(Boolean.valueOf(isResetPageNumber).booleanValue());
		}

		String isReprintHeaderOnEachPage = atts.getValue(ATTRIBUTE_isReprintHeaderOnEachPage);
		if (isReprintHeaderOnEachPage != null && isReprintHeaderOnEachPage.length() > 0)
		{
			group.setReprintHeaderOnEachPage(Boolean.valueOf(isReprintHeaderOnEachPage).booleanValue());
		}

		String minHeightToStartNewPage = atts.getValue(ATTRIBUTE_minHeightToStartNewPage);
		if (minHeightToStartNewPage != null && minHeightToStartNewPage.length() > 0)
		{
			group.setMinHeightToStartNewPage(Integer.parseInt(minHeightToStartNewPage));
		}

		return group;
	}


}
