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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBandFactory.java 2697 2009-03-24 18:41:23Z teodord $
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
