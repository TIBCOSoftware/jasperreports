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

/*
 * Contributors:
 * Michael McMahon - Michael.McMahon@activewire.net
 */

package net.sf.jasperreports.engine;

import org.jfree.chart.JFreeChart;

/**
 * This interface allows users to provide pluggable chart customizers. A class that implements this interface can
 * be defined and its name must be specified in the report template. At fill time, the corresponding <i>customize()</i>
 * method of this class will be called, if advanced chart customization is needed.
 * <p/>
 * If values of report parameters, variables or fields are needed in the customization, the customizer implementation
 * should extend {@link net.sf.jasperreports.engine.JRAbstractChartCustomizer JRAbstractChartCustomizer}
 * to gain access to such values.
 *
 * @author Michael McMahon (Michael.McMahon@activewire.net)
 */
public interface JRChartCustomizer {

	/**
	 * This method is called at fill time, before the chart is rendered.
 	 * @param chart the JFreeChart object, which can be accessed and modified
	 * @param jasperChart the JasperReports version of the chart, containing data that might be needed in
	 * customization
	 */
	public void customize(JFreeChart chart, JRChart jasperChart);

}

