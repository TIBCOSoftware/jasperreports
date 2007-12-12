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
package net.sf.jasperreports.engine.base;

import java.awt.Color;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseBox.java 1924 2007-10-25 13:31:38Z lucianc $
 */
public class JRBaseBoxPen extends JRBasePen implements JRBoxPen
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected JRLineBox lineBox = null;
	
	/**
	 *
	 */
	public JRBaseBoxPen(JRLineBox box)
	{
		super(box);

		this.lineBox = box;
	}
	
	
	/**
	 *
	 */
	public JRLineBox getBox() 
	{
		return lineBox;
	}

	/**
	 *
	 */
	public Float getLineWidth()
	{
		return JRStyleResolver.getLineWidth(this, penContainer.getDefaultLineWidth());
	}

	/**
	 *
	 */
	public Byte getLineStyle()
	{
		return JRStyleResolver.getLineStyle(this);
	}

	/**
	 *
	 */
	public Color getLineColor()
	{
		return JRStyleResolver.getLineColor(this, penContainer.getDefaultLineColor());
	}

	/**
	 *
	 */
	public JRPen getPen(JRLineBox box) 
	{
		return box.getPen();
	}

	/**
	 * 
	 */
	public JRBoxPen clone(JRLineBox lineBox)
	{
		JRBaseBoxPen clone = (JRBaseBoxPen)super.clone(lineBox);
		
		clone.lineBox = lineBox;
		
		return clone;
	}
	
}
