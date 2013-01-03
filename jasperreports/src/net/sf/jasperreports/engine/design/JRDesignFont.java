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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.base.JRBaseFont;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignFont extends JRBaseFont
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_STYLE = "style";
	public static final String PROPERTY_STYLE_NAME_REFERENCE = "styleNameReference";

	/**
	 * @deprecated To be removed.
	 */
	public JRDesignFont()
	{
	}
		

	/**
	 *
	 */
	public JRDesignFont(JRStyleContainer styleContainer)
	{
		super(styleContainer);
	}
		

	/**
	 *
	 */
	public void setStyle(JRStyle style)
	{
		Object old = this.style;
		this.style = style;
		getEventSupport().firePropertyChange(PROPERTY_STYLE, old, this.style);
	}

	
	/**
	 *
	 */
	public void setStyleNameReference(String styleNameReference)
	{
		Object old = this.styleNameReference;
		this.styleNameReference = styleNameReference;
		getEventSupport().firePropertyChange(PROPERTY_STYLE_NAME_REFERENCE, old, this.styleNameReference);
	}

}
