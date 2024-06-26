/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.components.map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.engine.JRExpression;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
@JsonDeserialize(as = StandardMarkerItemData.class)
public interface MarkerItemData extends ItemData {

    /**
     * Returns a {@link net.sf.jasperreports.engine.JRExpression JRExpression} representing
     * the series name expression.
     *
     * @return the series name expression
     */
    JRExpression getSeriesNameExpression();

    JRExpression getMarkerClusteringExpression();

    JRExpression getMarkerSpideringExpression();

    JRExpression getLegendIconExpression();
}
