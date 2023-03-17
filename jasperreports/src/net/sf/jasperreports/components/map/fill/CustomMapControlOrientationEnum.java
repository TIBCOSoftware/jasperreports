package net.sf.jasperreports.components.map.fill;

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public enum CustomMapControlOrientationEnum implements NamedEnum {

    HORIZONTAL("horizontal"),

    VERTICAL("vertical");

    private final transient String name;

    private CustomMapControlOrientationEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    static CustomMapControlOrientationEnum getByName(String name)
    {
        return EnumUtil.getEnumByName(values(), name);
    }

    static String getAllNames() {
        CustomMapControlOrientationEnum[] positionEnums = values();
        List<String> names = new ArrayList<>();
        for (CustomMapControlOrientationEnum positionEnum: positionEnums) {
            names.add(positionEnum.getName());
        }
        return String.join(", ", names);
    }
}
