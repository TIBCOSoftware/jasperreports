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
package net.sf.jasperreports.components.map.imageprovider;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class DefaultMapElementImageProvider implements MapImageProvider {

    private static final DefaultMapElementImageProvider INSTANCE = new DefaultMapElementImageProvider();

    private ChromeMapElementImageProvider chromeMapElementImageProvider;

    private MapElementImageProvider mapElementImageProvider;

    private DefaultMapElementImageProvider() {
        chromeMapElementImageProvider = new ChromeMapElementImageProvider();
        mapElementImageProvider = new MapElementImageProvider();
    }

    public static DefaultMapElementImageProvider getInstance() {
        return INSTANCE;
    }

    @Override
    public JRPrintImage getImage(
            JasperReportsContext jasperReportsContext,
            JRGenericPrintElement element) throws JRException {

        if (chromeMapElementImageProvider.isEnabled(jasperReportsContext)) {
            return chromeMapElementImageProvider.getImage(jasperReportsContext, element);
        }

        return mapElementImageProvider.getImage(jasperReportsContext, element);
    }
}
