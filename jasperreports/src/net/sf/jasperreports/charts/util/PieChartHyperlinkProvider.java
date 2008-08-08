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
package net.sf.jasperreports.charts.util;

import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintHyperlink;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRPieChartImageMapRenderer.java 1364 2006-08-31 15:13:20Z lucianc $
 */
public class PieChartHyperlinkProvider implements ChartHyperlinkProvider
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private Map sectionHyperlinks;
	
	public PieChartHyperlinkProvider(Map sectionHyperlinks)
	{
		this.sectionHyperlinks = sectionHyperlinks;
	}

	
	public JRPrintHyperlink getEntityHyperlink(ChartEntity entity)
	{
		JRPrintHyperlink printHyperlink = null;
		if (hasHyperlinks() && entity instanceof PieSectionEntity)
		{
			PieSectionEntity pieEntity = (PieSectionEntity) entity;
			printHyperlink = (JRPrintHyperlink) sectionHyperlinks.get(pieEntity.getSectionKey());
		}
		return printHyperlink;
	}

	public boolean hasHyperlinks()
	{
		return sectionHyperlinks != null && sectionHyperlinks.size() > 0;
	}
}
