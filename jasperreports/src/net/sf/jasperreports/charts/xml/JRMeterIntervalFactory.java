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
package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRMeterIntervalFactory extends JRBaseFactory
{
	private static final String ATTRIBUTE_label = "label";
	private static final String ATTRIBUTE_color = "color";
	private static final String ATTRIBUTE_alpha = "alpha";

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRMeterInterval interval = new JRMeterInterval();

		String value = atts.getValue(ATTRIBUTE_label);
		if (value != null && value.length() > 0)
		{
			interval.setLabel(value);
		}

		value = atts.getValue(ATTRIBUTE_color);
		if (value != null && value.length() > 0)
		{
			interval.setBackgroundColor(JRColorUtil.getColor(value, null));
		}

		value = atts.getValue(ATTRIBUTE_alpha);
		if (value != null && value.length() > 0)
		{
			interval.setAlpha(Double.valueOf(value));
		}

		return interval;
	}
}
