/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.util.json;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRSingletonCache;

/**
 * Helper class used to instantiate {@link JsonExecuter JSON executers}.
 * <p/>
 * The {@link JsonExecuterFactory JSON executer factory} class name is given by the
 * {@link #PROPERTY_JSON_EXECUTER_FACTORY net.sf.jasperreports.json.executer.factory} property.
 * The class should have a public default constructor so that it can be instantiated via reflection.
 * <p/>
 * By default, {@link CustomJsonPathExecuter JSON executers} are used.
 *
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public final class JsonExecuterUtil
{
    public static final String EXCEPTION_MESSAGE_KEY_JSON_EXECUTER_FACTORY_NOT_FOUND = "util.json.executer.factory.property.not.found";
    /**
     * Property that holds the {@link JsonExecuterFactory JSON executer factory} class name.
     */
    public static final String PROPERTY_JSON_EXECUTER_FACTORY = JRPropertiesUtil.PROPERTY_PREFIX + "json.executer.factory";

    private static final JRSingletonCache<JsonExecuterFactory> cache = new JRSingletonCache<>(JsonExecuterFactory.class);


    /**
     * Return a {@link JsonExecuterFactory JSON executer factory} instance.
     *
     * @return a JsonExecuterFactory instance
     * @throws JRException if the {@link #PROPERTY_JSON_EXECUTER_FACTORY JSON factory property} is not defined
     * or the factory cannot be instantiated.
     */
    public static JsonExecuterFactory getJsonExecuterFactory(JasperReportsContext jasperReportsContext) throws JRException
    {
        String factoryClassName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_JSON_EXECUTER_FACTORY);
        if (factoryClassName == null)
        {
            throw
                    new JRException(
                            EXCEPTION_MESSAGE_KEY_JSON_EXECUTER_FACTORY_NOT_FOUND,
                            new Object[]{PROPERTY_JSON_EXECUTER_FACTORY});
        }

        return cache.getCachedInstance(factoryClassName);
    }


    /**
     * Produces an {@link JsonExecuter JSON executer} instance by means of the factory
     * returned by {@link #getJsonExecuterFactory(JasperReportsContext) getJsonExecuterFactory(JasperReportsContext)}.
     *
     * @return an JsonExecuter instance
     * @throws JRException if the {@link #PROPERTY_JSON_EXECUTER_FACTORY JSON factory property} is not defined
     * or the factory cannot be instantiated.
     */
    public static JsonExecuter getJsonExecuter(JasperReportsContext jasperReportsContext) throws JRException
    {
        JsonExecuterFactory executerFactory = getJsonExecuterFactory(jasperReportsContext);
        return executerFactory.getJsonExecuter();
    }


    private JsonExecuterUtil()
    {
    }

}
