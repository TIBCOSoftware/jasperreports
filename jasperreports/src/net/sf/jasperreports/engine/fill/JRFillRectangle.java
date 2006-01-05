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
package net.sf.jasperreports.engine.fill;

import java.io.IOException;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillRectangle extends JRFillGraphicElement implements JRRectangle
{


	/**
	 *
	 */
	protected JRFillRectangle(
		JRBaseFiller filler,
		JRRectangle rectangle, 
		JRFillObjectFactory factory
		)
	{
		super(filler, rectangle, factory);
	}


	protected JRFillRectangle(JRFillRectangle rectangle, JRFillCloneFactory factory)
	{
		super(rectangle, factory);
	}


	/**
	 * 
	 */
	public int getRadius()
	{
		return ((JRRectangle)this.parent).getRadius();
	}
		
	public Integer getOwnRadius()
	{
		return ((JRRectangle)this.parent).getOwnRadius();
	}

	/**
	 *
	 */
	public void setRadius(int radius)
	{
	}

	/**
	 *
	 */
	public void setRadius(Integer radius)
	{
	}

	/**
	 *
	 */
	protected JRTemplateRectangle getJRTemplateRectangle()
	{
		JRStyle style = getElementStyle();
		JRTemplateRectangle template = (JRTemplateRectangle) getTemplate(style);
		if (template == null)
		{
			template = new JRTemplateRectangle(filler.getJasperPrint().getDefaultStyleProvider(), (JRRectangle)parent, style);
			registerTemplate(style, template);
		}
		return template;
	}


	/**
	 *
	 */
	protected void evaluate(
		byte evaluation
		) throws JRException
	{
		this.reset();
		
		this.evaluatePrintWhenExpression(evaluation);
	}


	/**
	 *
	 */
	protected JRPrintElement fill()
	{
		JRPrintRectangle printRectangle = null;

		printRectangle = new JRTemplatePrintRectangle(this.getJRTemplateRectangle());
		printRectangle.setX(this.getX());
		printRectangle.setY(this.getRelativeY());
		printRectangle.setWidth(getWidth());
		printRectangle.setHeight(this.getStretchHeight());
		
		return printRectangle;
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
	public void writeXml(JRXmlWriter xmlWrite) throws IOException
	{
		xmlWrite.writeRectangle(this);
	}

	/**
	 *
	 */
	protected void resolveElement (JRPrintElement element, byte evaluation)
	{
		// nothing
	}


	public JRCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillRectangle(this, factory);
	}

}
