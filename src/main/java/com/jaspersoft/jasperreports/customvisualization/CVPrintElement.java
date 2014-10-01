/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.jasperreports.customvisualization;

import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

/**
 * Utility class that creates generic print elements of cv type.
 * 
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public final class CVPrintElement
{
    
        public static final String PARAMETER_ON_ERROR_TYPE = CVConstants.PROPERTY_ON_ERROR_TYPE;
	/**
	 * The qualified type of the Custom Visualization generic elements. 
	 */
	public static final JRGenericElementType CV_ELEMENT_TYPE = 
		new JRGenericElementType(CVConstants.NAMESPACE, CVConstants.COMPONENT_NAME);
	
	public static final String CONFIGURATION = "configuration";
        public static final OnErrorTypeEnum DEFAULT_ON_ERROR_TYPE = OnErrorTypeEnum.ERROR;
        
        
        /**
	 * The cached image renderer.
	 */
	public static final String PARAMETER_SVG_CACHE_RENDERER = "cacheRenderer";
        public static final String PARAMETER_PNG_CACHE_RENDERER = "pngCacheRenderer";
        
        
        /**
	 * The temporary cached image renderer.
         * 
         * It is used to render the image for this component when the element itself if not ready yet.
	 */
	public static final String PARAMETER_TEMPORARY_CACHE_RENDERER = "temporaryCacheRenderer";

	private CVPrintElement()
	{
	}
}
