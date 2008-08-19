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
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.base.JRBasePen;


/**
 * Line element information shared by multiple print line objects.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 * @see JRTemplatePrintLine
 */
public class JRTemplateLine extends JRTemplateGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private byte direction = JRLine.DIRECTION_TOP_DOWN;


	/**
	 *
	 */
	protected JRTemplateLine(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JRLine line)
	{
		super(origin, defaultStyleProvider);

		setLine(line);
	}

	
	/**
	 * Creates a template line.
	 * 
	 * @param origin the origin of the elements that will use this template
	 * @param defaultStyleProvider the default style provider to use for
	 * this template
	 */
	public JRTemplateLine(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		super(origin, defaultStyleProvider);
		
		this.linePen = new JRBasePen(this);
	}


	/**
	 *
	 */
	protected void setLine(JRLine line)
	{
		super.setGraphicElement(line);

		setDirection(line.getDirection());
	}

	/**
	 *
	 */
	public byte getDirection()
	{
		return this.direction;
	}

	/**
	 *
	 */
	public void setDirection(byte direction)
	{
		this.direction = direction;
	}


}
