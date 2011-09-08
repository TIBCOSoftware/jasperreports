/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.util;

import java.awt.Dimension;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public interface HtmlPrintElement {
	
	public static final String PARAMETER_HTML_CONTENT = "htmlContent";

	public static final String PARAMETER_SCALE_TYPE = "scaleType";
	
	public static final String PARAMETER_HORIZONTAL_ALIGN = "horizontalAlign";
	
	public static final String PARAMETER_VERTICAL_ALIGN = "verticalAlign";

	public static final String PARAMETER_CLIP_ON_OVERFLOW = "clipOnOverflow";

	public static final String BUILTIN_PARAMETER_HAS_OVERFLOWED = "hasOverflowed";
	
	JRPrintImage createImageFromElement(JRGenericPrintElement element)  throws JRException;

	JRPrintImage createImageFromComponentElement(JRComponentElement componentElement)  throws JRException;
	
	public Dimension getComputedSize(JRGenericPrintElement element);
}
