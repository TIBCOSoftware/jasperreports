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
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


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


	protected JRFillEllipse(JRFillEllipse ellipse, JRFillCloneFactory factory)
	{
		super(ellipse, factory);
	}


	/**
	 * 
	 */
	protected JRTemplateEllipse getJRTemplateEllipse()
	{
		if (template == null)
		{
			template = new JRTemplateEllipse(filler.getJasperPrint().getDefaultStyleProvider(), (JREllipse)this.parent);
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
	protected JRPrintElement fill()
	{
		JRPrintEllipse printEllipse = null;

		printEllipse = new JRTemplatePrintEllipse(this.getJRTemplateEllipse());
		printEllipse.setX(this.getX());
		printEllipse.setY(this.getRelativeY());
		printEllipse.setWidth(getWidth());
		printEllipse.setHeight(this.getStretchHeight());
		
		return printEllipse;
	}


	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
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

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeEllipse(this);
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
		return new JRFillEllipse(this, factory);
	}

}
