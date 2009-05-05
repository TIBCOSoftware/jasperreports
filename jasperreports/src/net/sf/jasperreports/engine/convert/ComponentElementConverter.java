/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.util.JRImageLoader;


/**
 * Converter of {@link JRComponentElement} into print elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ComponentElementConverter extends ElementConverter
{
	
	private final static ComponentElementConverter INSTANCE = new ComponentElementConverter();

	/**
	 * Returns the singleton instance of this converter.
	 * 
	 * @return the singleton component converter instance 
	 */
	public static ComponentElementConverter getInstance()
	{
		return INSTANCE;
	}
	
	private final static ElementIconConverter ICON_CONVERTER = new ElementIconConverter(
			JRImageLoader.COMPONENT_IMAGE_RESOURCE);
	
	private ComponentElementConverter()
	{
	}

	public JRPrintElement convert(ReportConverter reportConverter,
			JRElement element)
	{
		JRComponentElement componentElement = (JRComponentElement) element;
		JRPrintElement converted = null;
		ComponentKey componentKey = componentElement.getComponentKey();
		if (componentKey != null)
		{
			ComponentManager manager = ComponentsEnvironment.getComponentManager(
					componentKey);
			if (manager != null && manager.getDesignConverter() != null)
			{
				// convert using the component converter
				converted = manager.getDesignConverter().convert(reportConverter, 
						componentElement);
			}
		}
		
		if (converted == null)
		{
			// fallback to the icon converter
			converted = ICON_CONVERTER.convert(reportConverter, element);
		}
		
		return converted;
	}

}
