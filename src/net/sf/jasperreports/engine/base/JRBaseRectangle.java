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
package net.sf.jasperreports.engine.base;

import java.io.IOException;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * The actual implementation of a graphic element representing a rectangle.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseRectangle extends JRBaseGraphicElement implements JRRectangle
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected Integer radius;


	/**
	 * Initializes properties that are specific to rectangles. Common properties are initialized by its
	 * parent constructors.
	 * @param rectangle an element whose properties are copied to this element. Usually it is a
	 * {@link net.sf.jasperreports.engine.design.JRDesignRectangle} that must be transformed into an
	 * <tt>JRBaseRectangle</tt> at compile time.
	 * @param factory a factory used in the compile process
	 */
	protected JRBaseRectangle(JRRectangle rectangle, JRBaseObjectFactory factory)
	{
		super(rectangle, factory);

		radius = rectangle.getOwnRadius();
	}


	/**
	 *
	 */
	public int getRadius()
	{
		return JRStyleResolver.getRadius(this);
	}

	public Integer getOwnRadius()
	{
		return this.radius;
	}

	/**
	 *
	 */
	public void setRadius(int radius)
	{
		this.radius = new Integer(radius);
	}

	/**
	 *
	 */
	public void setRadius(Integer radius)
	{
		this.radius = radius;
	}

	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getRectangle(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeRectangle(this);
	}


}
