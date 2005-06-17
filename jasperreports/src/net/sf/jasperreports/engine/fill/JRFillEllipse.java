/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillEllipse extends JRFillGraphicElement implements JREllipse
{


	/**
	 *
	 */
	protected JRFillEllipse(
		JRBaseFiller filler,
		JREllipse ellipse, 
		JRFillObjectFactory factory
		)
	{
		super(filler, ellipse, factory);
	}


	/**
	 *
	 */
	protected JRTemplateEllipse getJRTemplateEllipse()
	{
		if (template == null)
		{
			template = new JRTemplateEllipse((JREllipse)this.parent);
		}
		
		return (JRTemplateEllipse)template;
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
	protected JRPrintElement fill() throws JRException
	{
		JRPrintEllipse printEllipse = null;

		printEllipse = new JRTemplatePrintEllipse(this.getJRTemplateEllipse());
		printEllipse.setX(this.getX());
		printEllipse.setY(this.getRelativeY());
		printEllipse.setHeight(this.getStretchHeight());
		
		return printEllipse;
	}


	/**
	 *
	 */
	public JRElement getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getEllipse(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


}
