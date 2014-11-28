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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;

/**
 * Factory of {@link JdkGlyphFixTextMeasurer} instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JRTextMeasurerUtil#PROPERTY_TEXT_MEASURER_FACTORY
 */
public class JdkGlyphFixTextMeasurerFactory extends AbstractTextMeasurerFactory
{

	/**
	 * Creates a {@link JdkGlyphFixTextMeasurer} instance.
	 */
	public JRTextMeasurer createMeasurer(JasperReportsContext jasperReportsContext, JRCommonText text)
	{
		return new JdkGlyphFixTextMeasurer(jasperReportsContext, text);
	}

}
