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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JROrigin;

/**
 * Generic print element information shared by multiple elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRTemplateGenericPrintElement
 */
public class JRTemplateGenericElement extends JRTemplateElement
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private JRGenericElementType genericType;

	protected JRTemplateGenericElement(JROrigin origin, 
			JRDefaultStyleProvider defaultStyleProvider, JRGenericElement element)
	{
		super(origin, defaultStyleProvider);
		
		setElement(element);
		setGenericType(element.getGenericType());
	}

	/**
	 * Creates a generic print element template.
	 * 
	 * @param origin the origin of the elements that will use the template
	 * @param defaultStyleProvider the style provider to be used for the elements
	 * @param genericType the type of the generic elements
	 */
	public JRTemplateGenericElement(JROrigin origin, 
			JRDefaultStyleProvider defaultStyleProvider,
			JRGenericElementType genericType)
	{
		super(origin, defaultStyleProvider);
		
		setGenericType(genericType);
	}
	
	/**
	 * Returns the type of the generic elements that use this template.
	 * 
	 * @return the type of the generic elements
	 */
	public JRGenericElementType getGenericType()
	{
		return genericType;
	}
	
	/**
	 * Sets the type of the generic elements that use this template.
	 * 
	 * @param genericType the generic type
	 */
	public void setGenericType(JRGenericElementType genericType)
	{
		this.genericType = genericType;
	}

}
