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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.type.FooterPositionEnum;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRGroupFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignGroup group = new JRDesignGroup();
		
		group.setName(atts.getValue(JRXmlConstants.ATTRIBUTE_name));
		
		String isStartNewColumn = atts.getValue(JRXmlConstants.ATTRIBUTE_isStartNewColumn);
		if (isStartNewColumn != null && isStartNewColumn.length() > 0)
		{
			group.setStartNewColumn(Boolean.valueOf(isStartNewColumn).booleanValue());
		}

		String isStartNewPage = atts.getValue(JRXmlConstants.ATTRIBUTE_isStartNewPage);
		if (isStartNewPage != null && isStartNewPage.length() > 0)
		{
			group.setStartNewPage(Boolean.valueOf(isStartNewPage).booleanValue());
		}

		String isResetPageNumber = atts.getValue(JRXmlConstants.ATTRIBUTE_isResetPageNumber);
		if (isResetPageNumber != null && isResetPageNumber.length() > 0)
		{
			group.setResetPageNumber(Boolean.valueOf(isResetPageNumber).booleanValue());
		}

		String isReprintHeaderOnEachPage = atts.getValue(JRXmlConstants.ATTRIBUTE_isReprintHeaderOnEachPage);
		if (isReprintHeaderOnEachPage != null && isReprintHeaderOnEachPage.length() > 0)
		{
			group.setReprintHeaderOnEachPage(Boolean.valueOf(isReprintHeaderOnEachPage).booleanValue());
		}

		String minHeightToStartNewPage = atts.getValue(JRXmlConstants.ATTRIBUTE_minHeightToStartNewPage);
		if (minHeightToStartNewPage != null && minHeightToStartNewPage.length() > 0)
		{
			group.setMinHeightToStartNewPage(Integer.parseInt(minHeightToStartNewPage));
		}

		FooterPositionEnum footerPosition = FooterPositionEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_footerPosition));
		if (footerPosition != null)
		{
			group.setFooterPosition(footerPosition);
		}
		
		String keepTogether = atts.getValue(JRXmlConstants.ATTRIBUTE_keepTogether);
		if (keepTogether != null && keepTogether.length() > 0)
		{
			group.setKeepTogether(Boolean.valueOf(keepTogether).booleanValue());
		}

		return group;
	}


}
