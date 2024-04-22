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

import net.sf.jasperreports.components.items.StandardItemData;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class StandardMarkerItemData extends StandardItemData implements MarkerItemData 
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
    public static final String PROPERTY_SERIES_NAME_EXPRESSION = "seriesNameExpression";
    public static final String PROPERTY_MARKER_CLUSTERING_EXPRESSION = "markerClusteringExpression";
    public static final String PROPERTY_MARKER_SPIDERING_EXPRESSION = "markerSpideringExpression";
    public static final String PROPERTY_LEGEND_ICON_EXPRESSION = "legendIconExpression";
    private JRExpression seriesNameExpression;
    private JRExpression markerClusteringExpression;
    private JRExpression markerSpideringExpression;
    private JRExpression legendIconExpression;

    public StandardMarkerItemData() {
    }

    public StandardMarkerItemData(MarkerItemData data, JRBaseObjectFactory factory) {
        super(data, factory);
        seriesNameExpression = factory.getExpression(data.getSeriesNameExpression());
        markerClusteringExpression = factory.getExpression(data.getMarkerClusteringExpression());
        markerSpideringExpression = factory.getExpression(data.getMarkerSpideringExpression());
        legendIconExpression = factory.getExpression(data.getLegendIconExpression());
    }

    @Override
    public JRExpression getSeriesNameExpression() {
        return seriesNameExpression;
    }

    public void setSeriesNameExpression(JRExpression seriesNameExpression) {
        Object old = this.seriesNameExpression;
        this.seriesNameExpression = seriesNameExpression;
        getEventSupport().firePropertyChange(PROPERTY_SERIES_NAME_EXPRESSION, old, this.seriesNameExpression);
    }

    @Override
    public JRExpression getMarkerClusteringExpression() {
        return markerClusteringExpression;
    }

    public void setMarkerClusteringExpression(JRExpression markerClusteringExpression) {
        Object old = this.markerClusteringExpression;
        this.markerClusteringExpression = markerClusteringExpression;
        getEventSupport().firePropertyChange(PROPERTY_MARKER_CLUSTERING_EXPRESSION, old, this.markerClusteringExpression);
    }

    @Override
    public JRExpression getMarkerSpideringExpression() {
        return markerSpideringExpression;
    }

    public void setMarkerSpideringExpression(JRExpression markerSpideringExpression) {
        Object old = this.markerSpideringExpression;
        this.markerSpideringExpression = markerSpideringExpression;
        getEventSupport().firePropertyChange(PROPERTY_MARKER_SPIDERING_EXPRESSION, old, this.markerSpideringExpression);
    }

    @Override
    public JRExpression getLegendIconExpression() {
        return legendIconExpression;
    }

    public void setLegendIconExpression(JRExpression legendIconExpression) {
        Object old = this.legendIconExpression;
        this.legendIconExpression = legendIconExpression;
        getEventSupport().firePropertyChange(PROPERTY_LEGEND_ICON_EXPRESSION, old, this.legendIconExpression);
    }

	/**
	 * Used only during JRXML parsing for the backward compatibility of the <markerDataset> tag.
	 * @param datasetRun
	 */
    public void setDatasetRun(JRDatasetRun datasetRun)
	{
    	JRDesignElementDataset dataset = (JRDesignElementDataset)getDataset();
    	if (dataset == null)
    	{
    		dataset = new JRDesignElementDataset();
    		setDataset(dataset);
    	}
    	
    	dataset.setDatasetRun(datasetRun);
	}

	@Override
    public Object clone() {
        StandardMarkerItemData clone = (StandardMarkerItemData) super.clone();
        clone.seriesNameExpression = JRCloneUtils.nullSafeClone(seriesNameExpression);
        clone.markerClusteringExpression = JRCloneUtils.nullSafeClone(markerClusteringExpression);
        clone.markerSpideringExpression = JRCloneUtils.nullSafeClone(markerSpideringExpression);
        clone.legendIconExpression = JRCloneUtils.nullSafeClone(legendIconExpression);

        return clone;
    }
}
