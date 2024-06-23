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
package net.sf.jasperreports.components.map.fill;

import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.fill.FillItem;
import net.sf.jasperreports.components.map.MarkerItemData;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.FillContextProvider;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class FillMarkerItemData extends FillItemData
{
	public static final String PROPERTY_MARKER_CLUSTERING = "markerClustering";
	public static final String PROPERTY_MARKER_SPIDERING = "markerSpidering";
	public static final String PROPERTY_LEGEND_ICON = "legendIcon";

	/**
	 *
	 */
	public FillMarkerItemData(
		FillContextProvider fillContextProvider,
		MarkerItemData itemData,
		JRFillObjectFactory factory
		)// throws JRException
	{
		super(fillContextProvider, itemData, factory);
	}

	@Override
	public FillItem getFillItem(FillContextProvider fillContextProvider, Item item, JRFillObjectFactory factory){
		return new FillPlaceItem(fillContextProvider, item, factory);
	}

	public Object getEvaluateSeriesNameExpression(byte evaluation) throws JRException {
		return fillContextProvider.getFillContext().evaluate(((MarkerItemData)itemData).getSeriesNameExpression(), evaluation);
	}

	public Object getEvaluateMarkerClusteringExpression(byte evaluation) throws JRException {
		return fillContextProvider.getFillContext().evaluate(((MarkerItemData)itemData).getMarkerClusteringExpression(), evaluation);
	}

	public Object getEvaluateMarkerSpideringExpression(byte evaluation) throws JRException {
		return fillContextProvider.getFillContext().evaluate(((MarkerItemData)itemData).getMarkerSpideringExpression(), evaluation);
	}

	public Object getEvaluateLegendIconExpression(byte evaluation) throws JRException {
		return fillContextProvider.getFillContext().evaluate(((MarkerItemData)itemData).getLegendIconExpression(), evaluation);
	}

}
