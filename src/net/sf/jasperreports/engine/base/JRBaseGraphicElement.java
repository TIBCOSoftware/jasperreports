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
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.util.JRPenUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This class provides functionality common to graphic elements. It provides implementation for the methods described
 * in <tt>JRTextElement</tt>.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseGraphicElement extends JRBaseElement implements JRGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	
	/**
	 *
	 */
	protected JRPen linePen;
	protected Byte fill;


	/**
	 * Constructs an empty graphic element. By default graphic elements are opaque.
	 *
	protected JRBaseGraphicElement()
	{
		super();
	}
		

	/**
	 * Initializes properties that are specific to graphic elements. Common properties are initialized by its
	 * parent constructor.
	 * @param graphicElement an element whose properties are copied to this element. Usually it is a
	 * {@link net.sf.jasperreports.engine.design.JRDesignGraphicElement} that must be transformed into an
	 * <tt>JRBaseGraphicElement</tt> at compile time.
	 * @param factory a factory used in the compile process
	 */
	protected JRBaseGraphicElement(JRGraphicElement graphicElement, JRBaseObjectFactory factory)
	{
		super(graphicElement, factory);
		
		linePen = graphicElement.getLinePen().clone(this);
		
		fill = graphicElement.getOwnFill();
	}
		

	/**
	 *
	 */
	public JRPen getLinePen()
	{
		return linePen;
	}

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public byte getPen()
	{
		return JRPenUtil.getPenFromLinePen(linePen);
	}
		
	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public Byte getOwnPen()
	{
		return JRPenUtil.getOwnPenFromLinePen(linePen);
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
		JRPenUtil.setLinePenFromPen(pen, linePen);
	}
		
	/**
	 *
	 */
	public byte getFill()
	{
		return JRStyleResolver.getFill(this);
	}

	public Byte getOwnFill()
	{
		return this.fill;
	}
	
	/**
	 *
	 */
	public void setFill(byte fill)
	{
		setFill(new Byte(fill));
	}
	
	/**
	 *
	 */
	public void setFill(Byte fill)
	{
		Object old = this.fill;
		this.fill = fill;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_FILL, old, this.fill);
	}

	/**
	 * 
	 */
	public Float getDefaultLineWidth() 
	{
		return JRPen.LINE_WIDTH_1;
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}
		

	/**
	 * This field is only for serialization backward compatibility.
	 */
	private Byte pen;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (linePen == null)
		{
			linePen = new JRBasePen(this);
			JRPenUtil.setLinePenFromPen(pen, linePen);
			pen = null;
		}
	}
}
