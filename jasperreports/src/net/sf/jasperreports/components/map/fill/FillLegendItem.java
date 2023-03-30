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
