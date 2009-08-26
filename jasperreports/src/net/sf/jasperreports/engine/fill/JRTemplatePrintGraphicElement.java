/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 *(at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.fill;

import java.awt.Color;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.util.JRPenUtil;


/**
 * Base implementation of {@link net.sf.jasperreports.engine.JRPrintGraphicElement} that uses
 * a {@link net.sf.jasperreports.engine.fill.JRTemplateGraphicElement} instance to
 * store common attributes. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintGraphicElement extends JRTemplatePrintElement implements JRPrintGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	/**
	 *
	 */
	protected JRTemplatePrintGraphicElement(JRTemplateGraphicElement graphicElement)
	{
		super(graphicElement);
	}

	/**
	 *
	 */
	public JRPen getLinePen()
	{
		return ((JRTemplateGraphicElement)template).getLinePen();
	}
		
	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public byte getPen()
	{
		return JRPenUtil.getPenFromLinePen(getLinePen());
	}
		
	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public Byte getOwnPen()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLinePen());
	}
		
	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(byte pen)
	{
		setPen(new Byte(pen));
	}
		
	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(Byte pen)
	{
		JRPenUtil.setLinePenFromPen(pen, getLinePen());
	}
		
	/**
	 *
	 */
	public byte getFill()
	{
		return ((JRTemplateGraphicElement)this.template).getFill();
	}

	/**
	 *
	 */
	public Byte getOwnFill()
	{
		return ((JRTemplateGraphicElement)this.template).getOwnFill();
	}

	/**
	 *
	 */
	public void setFill(byte fill)
	{
	}

	/**
	 *
	 */
	public void setFill(Byte fill)
	{
	}
		

	/**
	 * 
	 */
	public Float getDefaultLineWidth() 
	{
		return ((JRTemplateGraphicElement)template).getDefaultLineWidth();
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return ((JRTemplateGraphicElement)template).getDefaultLineColor();
	}

	
}
