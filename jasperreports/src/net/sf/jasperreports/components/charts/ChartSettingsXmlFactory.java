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
package net.sf.jasperreports.components.charts;

import java.awt.Color;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class ChartSettingsXmlFactory extends JRBaseFactory
{
	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		StandardChartSettings chartSettings = new StandardChartSettings();

		String isShowLegend = atts.getValue(JRXmlConstants.ATTRIBUTE_isShowLegend);
		if (isShowLegend != null && isShowLegend.length() > 0)
		{
			chartSettings.setShowLegend(Boolean.valueOf(isShowLegend));
		}

		Color backcolor = JRColorUtil.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_backcolor), null);
		if (backcolor != null)
		{
			chartSettings.setBackcolor(backcolor);
		}
		
		chartSettings.setLinkType(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkType));
		chartSettings.setLinkTarget(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTarget));

		String bookmarkLevelAttr = atts.getValue(JRXmlConstants.ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			chartSettings.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}

		chartSettings.setCustomizerClass(atts.getValue(JRXmlConstants.ATTRIBUTE_customizerClass));
		chartSettings.setRenderType(atts.getValue(JRXmlConstants.ATTRIBUTE_renderType));
		
		return chartSettings;
	}


	/**
	 *
	 */
	public static class ChartTitleFactory extends JRBaseFactory
	{
		public Object createObject(Attributes atts)
		{
			StandardChartSettings chartSettings = (StandardChartSettings) digester.peek();

			EdgeEnum position = EdgeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_position));
			if (position != null)
			{
				chartSettings.setTitlePosition(position);
			}
			
			Color color = JRColorUtil.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_color), Color.black);
			if (color != null)
			{
				chartSettings.setTitleColor(color);
			}
			
			return chartSettings;
		}
	}


	/**
	 *
	 */
	public static class ChartSubtitleFactory extends JRBaseFactory
	{
		public Object createObject(Attributes atts)
		{
			StandardChartSettings chartSettings = (StandardChartSettings) digester.peek();

			Color color = JRColorUtil.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_color), Color.black);
			if (color != null)
			{
				chartSettings.setSubtitleColor(color);
			}

			return chartSettings;
		}
	}


	/**
	 *
	 */
	public static class ChartLegendFactory extends JRBaseFactory
	{
		/**
		 *
		 */
		public Object createObject(Attributes atts) throws JRException
		{
			// Grab the chart from the object stack.
			StandardChartSettings chartSettings = (StandardChartSettings)digester.peek();

			// Set the text color
			String attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_textColor);
			if (attrValue != null && attrValue.length() > 0)
			{
				Color color = JRColorUtil.getColor(attrValue, null);
				chartSettings.setLegendColor(color);
			}

			// Set the background color
			attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_backgroundColor);
			if (attrValue != null && attrValue.length() > 0)
			{
				Color color = JRColorUtil.getColor(attrValue, null);
				chartSettings.setLegendBackgroundColor(color);
			}

			EdgeEnum position = EdgeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_position));
			if (position != null)
			{
				chartSettings.setLegendPosition(position);
			}

			return chartSettings;
		}
	}

}
