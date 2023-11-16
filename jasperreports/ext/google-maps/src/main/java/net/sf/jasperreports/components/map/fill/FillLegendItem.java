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
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.FillContextProvider;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class FillLegendItem extends FillResetMapItem {
    public static final String[] LEGEND_AVAILABLE_ITEM_PROPERTIES = new String[]{
            MapComponent.LEGEND_PROPERTY_orientation,
            MapComponent.LEGEND_PROPERTY_legendMaxWidth,
            MapComponent.LEGEND_PROPERTY_legendMaxWidth_fullscreen,
            MapComponent.LEGEND_PROPERTY_legendMaxHeight,
            MapComponent.LEGEND_PROPERTY_legendMaxHeight_fullscreen,
            MapComponent.LEGEND_PROPERTY_useMarkerIcons
        };

    public FillLegendItem(
        FillContextProvider fillContextProvider,
        Item item,
        JRFillObjectFactory factory
    ) {
        super(fillContextProvider, item, factory);
    }

    @Override
    public void verifyValues(Map<String, Object> result) throws JRException {
        super.verifyValues(result);

        for (String propertyName: LEGEND_AVAILABLE_ITEM_PROPERTIES) {
            if (result.containsKey(propertyName)) {
                Object propertyValue = result.get(propertyName);
                if (propertyValue == null) {
                    throw new JRException(
                        MapFillComponent.EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUE_NOT_ALLOWED,
                        new Object[]{propertyName}
                    );
                }

                if (propertyName.toLowerCase().indexOf("width") != -1 ||
                        propertyName.toLowerCase().indexOf("height") != -1) {
                    if (!(propertyValue instanceof String)) {
                        throw new JRException(
                            MapFillComponent.EXCEPTION_MESSAGE_KEY_INVALID_LEGEND_PROPERTY_VALUE,
                            new Object[]{propertyValue, propertyName, "java.lang.String"}
                        );
                    }
                    if (!((String)propertyValue).endsWith("px")) {
                        throw new JRException(
                            MapFillComponent.EXCEPTION_MESSAGE_KEY_INVALID_LEGEND_SIZE_PROPERTY_VALUE,
                            new Object[]{propertyValue, propertyName}
                        );
                    }
                }
            }
        }

        Object orientation = result.get(MapComponent.LEGEND_PROPERTY_orientation);
        if (orientation != null && CustomMapControlOrientationEnum.getByName((String)orientation) == null) {
            throw
                new JRException(
                    MapFillComponent.EXCEPTION_MESSAGE_KEY_INVALID_ORIENTATION_VALUE,
                    new Object[]{orientation, CustomMapControlOrientationEnum.getAllNames()}
                );
        }
    }

}
