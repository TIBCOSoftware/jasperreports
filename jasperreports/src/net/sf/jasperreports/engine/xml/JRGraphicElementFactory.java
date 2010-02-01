/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.design.JRDesignGraphicElement;
import net.sf.jasperreports.engine.util.JRPenUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRGraphicElementFactory extends JRBaseFactory
{
	private static final Log log = LogFactory.getLog(JRGraphicElementFactory.class);
	
	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignGraphicElement graphicElement = (JRDesignGraphicElement)digester.peek();
		
		Byte stretchType = (Byte)JRXmlConstants.getStretchTypeMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_stretchType));
		if (stretchType != null)
		{
			graphicElement.setStretchType(stretchType.byteValue());
		}

		Byte pen = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_pen));
		if (pen != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'pen' attribute is deprecated. Use the <pen> tag instead.");
				
			JRPenUtil.setLinePenFromPen(pen, graphicElement.getLinePen());
		}

		Byte fill = (Byte)JRXmlConstants.getFillMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_fill));
		if (fill != null)
		{
			graphicElement.setFill(fill);
		}

		return graphicElement;
	}
	

}
