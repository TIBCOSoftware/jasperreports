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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.util.JRStyleResolver;


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
		return JRStyleResolver.getRadius(this);
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
		JRStyle style = getStyle();
		JRTemplateRectangle template = (JRTemplateRectangle) getTemplate(style);
		if (template == null)
		{
			template = 
				new JRTemplateRectangle(
					band == null ? null : band.getOrigin(), 
					filler.getJasperPrint().getDefaultStyleProvider(), 
					this
					);
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
		
		setValueRepeating(true);
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
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitRectangle(this);
	}

	/**
	 *
	 */
	protected void resolveElement (JRPrintElement element, byte evaluation)
	{
		// nothing
	}


	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillRectangle(this, factory);
	}

}
