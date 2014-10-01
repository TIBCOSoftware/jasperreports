/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.bridge;

import java.util.Map;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public interface Processor {
    
    
    public static final String CONF_WIDTH  = "width";
    public static final String CONF_HEIGHT = "height";
    public static final String CONF_KEY    = "key";
    public static final String CONF_SERIES = "series";
    public static final String CONF_PRINT_ELEMENT = "element";
    
    /**
     * Process a set of values, to build the script that will perform the
     * visualization.
     * 
     * Why a Map and not a serious object?! Well, to be able to implement this
     * class without having to declare any dependency by JasperReports.
     * 
     * Moreover, it is not really required to implement this interface, it is
     * enough that a class provides this method, the component will look for it!
     * 
     * Input: a Map with the component, the item properties and the series.
     * 
     * <table>
     * <tr><th>key</th><th>Type</th><th>Description</th></tr>
     * <tr>
     *      <td>Processor.CONF_PRINT_ELEMENT</td>
     *      <td>net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement</td>
     *      <td>The print element produced by the fill process</td>
     * <tr>
     * <tr>
     *      <td>Processor.CONF_SERIES</td>
     *      <td>Collection</td>
     *      <td>a collection of series, which are List of Maps.</td>
     * <tr>
     * <tr>
     *      <td>Processor.CONF_WIDTH</td>
     *      <td>Integer</td>
     *      <td>the element width</td>
     * <tr>
     * <tr>
     *      <td>Processor.CONF_HEIGHT</td>
     *      <td>Integer</td>
     *      <td>the element height</td>
     * <tr>
     * <tr>
     *      <td>Processor.CONF_KEY</td>
     *      <td>String</td>
     *      <td>the element key/id to be used in the HTML</td>
     * <tr>
     * 
     * 
     * Series are represented as lists of Maps. The Map is the record, and contain the
     * properties set for the series.
     *
     * 
     * The result should include at least the "script key.
     * output_html: a string with the main script, optional styles and other things that will be output

     * 
     * @param configuration the Map that will be passed to the velocity Template.
     * @return the processing result
     */
    public Map<String, Object> processConfiguration( Map<String, Object> configuration );

}
