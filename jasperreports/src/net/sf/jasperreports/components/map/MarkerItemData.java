package net.sf.jasperreports.components.map;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.engine.JRExpression;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
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
