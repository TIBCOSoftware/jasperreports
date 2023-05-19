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
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.components.items.fill.FillItem;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.FillContextProvider;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class FillResetMapItem extends FillItem {
    public static final String[] RESET_MAP_AVAILABLE_ITEM_PROPERTIES = new String[]{
            MapComponent.LEGEND_OR_RESET_MAP_PROPERTY_enabled,
            MapComponent.LEGEND_OR_RESET_MAP_PROPERTY_position,
            MapComponent.LEGEND_OR_RESET_MAP_PROPERTY_label
        };

    public FillResetMapItem(
        FillContextProvider fillContextProvider,
        Item item,
        JRFillObjectFactory factory
    ) {
        super(fillContextProvider, item, factory);
    }

    @Override
    public void verifyValue(ItemProperty property, Object value) throws JRException {
    }

    @Override
    public void verifyValues(Map<String, Object> result) throws JRException {
        for (String propertyName: RESET_MAP_AVAILABLE_ITEM_PROPERTIES) {
            if (result.containsKey(propertyName)) {
                Object propertyValue = result.get(propertyName);
                if (propertyValue == null) {
                    throw new JRException(
                        MapFillComponent.EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUE_NOT_ALLOWED,
                        new Object[]{propertyName}
                    );
                }
            }
        }

        Object position = result.get(MapComponent.LEGEND_OR_RESET_MAP_PROPERTY_position);
        if (position != null && CustomMapControlPositionEnum.getByName((String)position) == null) {
            throw
                new JRException(
                    MapFillComponent.EXCEPTION_MESSAGE_KEY_INVALID_POSITION_VALUE,
                    new Object[]{position, CustomMapControlPositionEnum.getAllNames()}
                );
        }
    }

    Map<String, Object> getEvaluatedItemProperties(byte evaluation) throws JRException {
        this.evaluateProperties(this.fillContextProvider.getFillContext(), evaluation);
        return this.getEvaluatedProperties();
    }

}
