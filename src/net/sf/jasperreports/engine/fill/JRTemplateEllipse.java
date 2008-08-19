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
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.base.JRBasePen;


/**
 * Ellipse information shared by multiple print ellipse objects.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 * @see JRTemplatePrintEllipse
 */
public class JRTemplateEllipse extends JRTemplateGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	/**
	 *
	 */
	protected JRTemplateEllipse(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JREllipse ellipse)
	{
		super(origin, defaultStyleProvider);

		setEllipse(ellipse);
	}

	/**
	 * Creates an ellipse template.
	 * 
	 * @param origin the origin of the elements that will use this template
	 * @param defaultStyleProvider the default style provider to use for
	 * this template
	 */
	public JRTemplateEllipse(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		super(origin, defaultStyleProvider);

		this.linePen = new JRBasePen(this);
	}


	/**
	 *
	 */
	protected void setEllipse(JREllipse ellipse)
	{
		super.setGraphicElement(ellipse);
	}


}
