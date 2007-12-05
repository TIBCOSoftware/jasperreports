/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRStyle;
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
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_DEFAULT = "default";
	
	public static final String PROPERTY_NAME = "name";
	
	public static final String PROPERTY_PARENT_STYLE = "parentStyle";
	
	public static final String PROPERTY_PARENT_STYLE_NAME_REFERENCE = "parentStyleNameReference";
	
	public static final String PROPERTY_CONDITIONAL_STYLES = "conditionalStyles";

	private List conditionalStylesList = new ArrayList();


	/**
	 *
	 */
	public JRDesignStyle()
	{
		super();
	}

	/**
	 *
	 */
	public void setName(String name)
	{
		Object old = this.name;
		this.name = name;
		getEventSupport().firePropertyChange(PROPERTY_NAME, old, this.name);
	}

	/**
	 *
	 */
	public void setDefault(boolean isDefault)
	{
		boolean old = this.isDefault;
		this.isDefault = isDefault;
		getEventSupport().firePropertyChange(PROPERTY_DEFAULT, old, this.isDefault);
	}

	/**
	 *
	 */
	public void setParentStyle(JRStyle parentStyle)
	{
		Object old = getStyle();
		super.setParentStyle(parentStyle);
		getEventSupport().firePropertyChange(PROPERTY_PARENT_STYLE, old, getStyle());
	}

	/**
	 *
	 */
	public void addConditionalStyle(JRConditionalStyle conditionalStyle)
	{
		conditionalStylesList.add(conditionalStyle);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_CONDITIONAL_STYLES, 
				conditionalStyle, conditionalStylesList.size() - 1);
	}

	/**
	 *
	 */
	public boolean removeConditionalStyle(JRConditionalStyle conditionalStyle)
	{
		int idx = conditionalStylesList.indexOf(conditionalStyle);
		if (idx >= 0)
		{
			conditionalStylesList.remove(idx);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CONDITIONAL_STYLES, 
					conditionalStyle, idx);
			return true;
		}
		return false;
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

	/**
	 * Set the name of the external style to be used as parent style.
	 * <p/>
	 * An external style is only effective when there is no internal parent style set,
	 * i.e. {@link #getStyle() getStyle()} returns <code>null</code>
	 * The external style will be resolved at fill time from the templates used in the report.
	 * 
	 * @param styleName the name of the external style
	 * @see #getStyleNameReference()
	 */
	public void setParentStyleNameReference(String styleName)
	{
		Object old = this.parentStyleNameReference;
		this.parentStyleNameReference = styleName;
		getEventSupport().firePropertyChange(PROPERTY_PARENT_STYLE_NAME_REFERENCE, old, this.parentStyleNameReference);
	}
}
