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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRSectionFactory extends JRBaseFactory
{
	
	/**
	 *
	 */
	public abstract JRSection getSection();
	
	
	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		return getSection();
	}
	

	/**
	 *
	 */
	public static class DetailSectionFactory extends JRSectionFactory
	{
		public JRSection getSection()
		{
			return ((JasperDesign)digester.peek(digester.getCount() - 2)).getDetailSection();
		}
	}


	/**
	 *
	 */
	public static class GroupHeaderSectionFactory extends JRSectionFactory
	{
		public JRSection getSection()
		{
			return ((JRDesignGroup)digester.peek()).getGroupHeaderSection();
		}
	}


	/**
	 *
	 */
	public static class GroupFooterSectionFactory extends JRSectionFactory
	{
		public JRSection getSection()
		{
			return ((JRDesignGroup)digester.peek()).getGroupFooterSection();
		}
	}


}
