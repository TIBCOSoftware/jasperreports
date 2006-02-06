/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.util.List;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.base.JRBaseStyle;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignStyle extends JRBaseStyle
{

	/**
	 *
	 */
	private static final long serialVersionUID = 10001;

	private List conditionalStylesList = new ArrayList();


	/**
	 *
	 */
	public JRDesignStyle()
	{
	}

	/**
	 *
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 *
	 */
	public void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}

	/**
	 *
	 */
	public void setParentStyle(JRStyle parentStyle)
	{
		this.parentStyle = parentStyle;
	}

	/**
	 *
	 */
	public void addConditionalStyle(JRConditionalStyle conditionalStyle)
	{
		conditionalStylesList.add(conditionalStyle);
	}

	/**
	 *
	 */
	public boolean removeConditionalStyle(JRConditionalStyle conditionalStyle)
	{
		return conditionalStylesList.remove(conditionalStyle);
	}

	/**
	 *
	 */
	public JRConditionalStyle[] getConditionalStyles()
	{
		return (JRConditionalStyle[]) conditionalStylesList.toArray(new JRDesignConditionalStyle[conditionalStylesList.size()]);
	}

	/**
	 *
	 */
	public List getConditionalStyleList()
	{
		return conditionalStylesList;
	}

}
