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

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public enum CustomMapControlPositionEnum implements NamedEnum {

    TOP_LEFT("TOP_LEFT"),
    TOP_CENTER("TOP_CENTER"),
    TOP_RIGHT("TOP_RIGHT"),

    LEFT_TOP("LEFT_TOP"),
    LEFT_CENTER("LEFT_CENTER"),
    LEFT_BOTTOM("LEFT_BOTTOM"),

    RIGHT_TOP("RIGHT_TOP"),
    RIGHT_CENTER("RIGHT_CENTER"),
    RIGHT_BOTTOM("RIGHT_BOTTOM"),

    BOTTOM_LEFT("BOTTOM_LEFT"),
    BOTTOM_CENTER("BOTTOM_CENTER"),
    BOTTOM_RIGHT("BOTTOM_RIGHT");

    private final transient String name;

    private CustomMapControlPositionEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    static CustomMapControlPositionEnum getByName(String name)
    {
        return EnumUtil.getEnumByName(values(), name);
    }

    static String getAllNames() {
        CustomMapControlPositionEnum[] positionEnums = values();
        List<String> names = new ArrayList<>();
        for (CustomMapControlPositionEnum positionEnum: positionEnums) {
            names.add(positionEnum.getName());
        }
        return String.join(", ", names);
    }
}
