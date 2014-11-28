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
package net.sf.jasperreports.components.table.fill;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class TableReportGroup implements JRGroup
{

	private final JRGroup datasetGroup;
	private JRBand header;
	private JRSection headerSection;
	private JRBand footer;
	private JRSection footerSection;
	
	public TableReportGroup(JRGroup datasetGroup)
	{
		this.datasetGroup = datasetGroup;
	}

	public JRVariable getCountVariable()
	{
		return datasetGroup.getCountVariable();
	}

	public JRExpression getExpression()
	{
		return datasetGroup.getExpression();
	}

	public FooterPositionEnum getFooterPositionValue()
	{
		return datasetGroup.getFooterPositionValue();
	}

	@Deprecated
	public JRBand getGroupFooter()
	{
		return footer;
	}

	public void setGroupFooter(JRBand footer)
	{
		this.footer = footer;
		this.footerSection = wrapBand(footer, BandTypeEnum.GROUP_FOOTER);
	}
	
	protected JRSection wrapBand(JRBand band, BandTypeEnum bandType)
	{
		if (band == null)
		{
			return null;
		}
		
		JROrigin origin = new JROrigin(null, getName(), bandType);
		JRDesignSection section = new JRDesignSection(origin);
		section.addBand(band);
		return section;
	}
	
	public JRSection getGroupFooterSection()
	{
		return footerSection;
	}

	@Deprecated
	public JRBand getGroupHeader()
	{
		return header;
	}

	public void setGroupHeader(JRBand header)
	{
		this.header = header;
		this.headerSection = wrapBand(header, BandTypeEnum.GROUP_HEADER);
	}

	public JRSection getGroupHeaderSection()
	{
		return headerSection;
	}

	public int getMinHeightToStartNewPage()
	{
		return datasetGroup.getMinHeightToStartNewPage();
	}

	public String getName()
	{
		return datasetGroup.getName();
	}

	public boolean isKeepTogether()
	{
		return datasetGroup.isKeepTogether();
	}

	public boolean isReprintHeaderOnEachPage()
	{
		return datasetGroup.isReprintHeaderOnEachPage();
	}

	public boolean isResetPageNumber()
	{
		return datasetGroup.isResetPageNumber();
	}

	public boolean isStartNewColumn()
	{
		return datasetGroup.isStartNewColumn();
	}

	public boolean isStartNewPage()
	{
		return datasetGroup.isStartNewPage();
	}

	public void setFooterPosition(FooterPositionEnum footerPosition)
	{
		throw new UnsupportedOperationException();
	}

	public void setKeepTogether(boolean keepTogether)
	{
		throw new UnsupportedOperationException();
	}

	public void setMinHeightToStartNewPage(int minHeight)
	{
		throw new UnsupportedOperationException();
	}

	public void setReprintHeaderOnEachPage(boolean isReprint)
	{
		throw new UnsupportedOperationException();
	}

	public void setResetPageNumber(boolean isReset)
	{
		throw new UnsupportedOperationException();
	}

	public void setStartNewColumn(boolean isStart)
	{
		throw new UnsupportedOperationException();
	}

	public void setStartNewPage(boolean isStart)
	{
		throw new UnsupportedOperationException();
	}

	public Object clone()
	{
		throw new UnsupportedOperationException();
	}

	public JRGroup getOriginalGroup()
	{
		return datasetGroup;
	}
	
}
