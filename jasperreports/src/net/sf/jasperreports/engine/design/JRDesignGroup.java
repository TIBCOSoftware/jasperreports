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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseGroup;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignGroup extends JRBaseGroup
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_COUNT_VARIABLE = "countVariable";
	
	public static final String PROPERTY_EXPRESSION = "expression";
	
	public static final String PROPERTY_GROUP_FOOTER = "groupFooter";
	
	public static final String PROPERTY_GROUP_HEADER = "groupHeader";
	
	public static final String PROPERTY_NAME = "name";


	/**
	 *
	 */
	public JRDesignGroup() 
	{
		groupHeaderSection = new JRDesignSection(new JROrigin(null, getName(), JROrigin.GROUP_HEADER));
		groupFooterSection = new JRDesignSection(new JROrigin(null, getName(), JROrigin.GROUP_FOOTER));
	}
	
	/**
	 *
	 */
	public void setName(String name)
	{
		Object old = this.name;
		this.name = name;
		updateSectionOrigins();
		getEventSupport().firePropertyChange(PROPERTY_NAME, old, this.name);
	}

	/**
	 *
	 */
	public void setExpression(JRExpression expression)
	{
		Object old = this.expression;
		this.expression = expression;
		getEventSupport().firePropertyChange(PROPERTY_EXPRESSION, old, this.expression);
	}
	
	/**
	 * @deprecated Replaced by {@link #getGroupHeaderSection()}.
	 */
	public void setGroupHeader(JRBand groupHeader)
	{
		Object old = getGroupHeader();
		
//		if (groupHeaderSection == null)
//		{
//			groupHeaderSection = new JRDesignSection();
//		}
		
		JRBand[] bands = groupHeaderSection.getBands(); 
		if (bands == null || bands.length == 0)
		{
			((JRDesignSection)groupHeaderSection).addBand(groupHeader);
		}
		else
		{
			((JRDesignSection)groupHeaderSection).removeBand(0);
			((JRDesignSection)groupHeaderSection).addBand(0, groupHeader);
		}

//		setBandOrigin(groupHeader, JROrigin.GROUP_HEADER);
		getEventSupport().firePropertyChange(PROPERTY_GROUP_HEADER, old, groupHeader);
	}
		
	/**
	 *
	 *
	public void setGroupHeader(JRSection groupHeaderSection)
	{
		Object old = this.groupHeaderSection;
		this.groupHeaderSection = groupHeaderSection;
		setSectionOrigin(this.groupHeaderSection, JROrigin.GROUP_HEADER);
		getEventSupport().firePropertyChange(PROPERTY_GROUP_HEADER, old, this.groupHeaderSection);
	}
		
	/**
	 * @deprecated Replaced by {@link #getGroupFooterSection()}.
	 */
	public void setGroupFooter(JRBand groupFooter)
	{
		Object old = getGroupFooter();
		
//		if (groupFooterSection == null)
//		{
//			groupFooterSection = new JRDesignSection();
//		}
		
		JRBand[] bands = groupFooterSection.getBands(); 
		if (bands == null || bands.length == 0)
		{
			((JRDesignSection)groupFooterSection).addBand(groupFooter);
		}
		else
		{
			((JRDesignSection)groupFooterSection).removeBand(0);
			((JRDesignSection)groupFooterSection).addBand(0, groupFooter);
		}

//		setBandOrigin(groupFooter, JROrigin.GROUP_FOOTER);
		getEventSupport().firePropertyChange(PROPERTY_GROUP_FOOTER, old, groupFooter);
	}

	/**
	 *
	 *
	public void setGroupFooter(JRSection groupFooterSection)
	{
		Object old = this.groupFooterSection;
		this.groupFooterSection = groupFooterSection;
		setSectionOrigin(this.groupFooterSection, JROrigin.GROUP_FOOTER);
		getEventSupport().firePropertyChange(PROPERTY_GROUP_FOOTER, old, this.groupFooterSection);
	}

	/**
	 *
	 */
	public void setCountVariable(JRVariable countVariable)
	{
		Object old = this.countVariable;
		this.countVariable = countVariable;
		getEventSupport().firePropertyChange(PROPERTY_COUNT_VARIABLE, old, this.countVariable);
	}

	protected void setSectionOrigin(JRSection section, byte type)
	{
		if (section instanceof JRDesignSection)
		{
			JROrigin origin = new JROrigin(null, getName(), type);
			((JRDesignSection) section).setOrigin(origin);
		}
	}
	
	protected void setBandOrigin(JRBand band, byte type)
	{
		if (band instanceof JRDesignBand)
		{
			JROrigin origin = new JROrigin(null, getName(), type);
			((JRDesignBand) band).setOrigin(origin);
		}
	}
	
	protected void updateSectionOrigins()
	{
		setSectionOrigin(getGroupHeaderSection(), JROrigin.GROUP_HEADER);
		setSectionOrigin(getGroupFooterSection(), JROrigin.GROUP_FOOTER);
	}

}
