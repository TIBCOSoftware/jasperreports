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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.util.JRPenUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.ObjectUtils;
import net.sf.jasperreports.engine.util.ObjectUtils.HashCode;


/**
 * Base class consisting of graphic print element information shared by multiple
 * print element instances.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRTemplateGraphicElement extends JRTemplateElement implements JRCommonGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected JRPen linePen;
	private FillEnum fillValue;


	/**
	 *
	 */
	protected JRTemplateGraphicElement(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		super(origin, defaultStyleProvider);
	}

	/**
	 *
	 */
	protected JRTemplateGraphicElement(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JRGraphicElement graphicElement)
	{
		super(origin, defaultStyleProvider);

		setGraphicElement(graphicElement);
	}


	/**
	 *
	 */
	protected void setGraphicElement(JRGraphicElement graphicElement)
	{
		super.setElement(graphicElement);
		
		copyLinePen(graphicElement.getLinePen());
		
		setFill(graphicElement.getOwnFillValue());
	}

	/**
	 * Copies {@link JRPen pen} attributes.
	 * 
	 * @param pen the object to copy the attributes from
	 */
	public void copyLinePen(JRPen pen)
	{
		linePen = pen.clone(this);
	}

	/**
	 *
	 */
	public JRPen getLinePen()
	{
		return linePen;
	}
		

	/**
	 *
	 */
	public FillEnum getFillValue()
	{
		return JRStyleResolver.getFillValue(this);
	}

	public FillEnum getOwnFillValue()
	{
		return this.fillValue;
	}
	
	/**
	 *
	 */
	public void setFill(FillEnum fillValue)
	{
		this.fillValue = fillValue;
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

		
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte pen;
	/**
	 * @deprecated
	 */
	private Byte fill;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			fillValue = FillEnum.getByValue(fill);
			fill = null;
		}
		if (linePen == null)
		{
			linePen = new JRBasePen(this);
			JRPenUtil.setLinePenFromPen(pen, linePen);
			pen = null;
		}
	}

	protected void addGraphicHash(HashCode hash)
	{
		addTemplateHash(hash);
		hash.addIdentical(linePen);
		hash.add(fillValue);
	}

	protected boolean graphicIdentical(JRTemplateGraphicElement template)
	{
		return templateIdentical(template)
				&& ObjectUtils.identical(linePen, template.linePen)
				&& ObjectUtils.equals(fillValue, template.fillValue);
	}
	
	
}
