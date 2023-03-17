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
