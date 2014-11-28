/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.type.LineDirectionEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRFillLine extends JRFillGraphicElement implements JRLine
{


	/**
	 *
	 */
	protected JRFillLine(
		JRBaseFiller filler,
		JRLine line, 
		JRFillObjectFactory factory
		)
	{
		super(filler, line, factory);
	}


	protected JRFillLine(JRFillLine line, JRFillCloneFactory factory)
	{
		super(line, factory);
	}


	/**
	 * 
	 */
	public LineDirectionEnum getDirectionValue()
	{
		return ((JRLine)this.parent).getDirectionValue();
	}
		
	/**
	 * 
	 */
	public void setDirection(LineDirectionEnum direction)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	protected JRTemplateLine getJRTemplateLine()
	{
		return (JRTemplateLine) getElementTemplate();
	}

	protected JRTemplateElement createElementTemplate()
	{
		return new JRTemplateLine(
				getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider(), 
				this
				);
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
		evaluateProperties(evaluation);
		evaluateStyle(evaluation);
		
		setValueRepeating(true);
	}


	/**
	 *
	 */
	protected JRPrintElement fill()
	{
		JRTemplatePrintLine printLine = new JRTemplatePrintLine(this.getJRTemplateLine(), printElementOriginator);
		printLine.setUUID(this.getUUID());
		printLine.setX(this.getX());
		printLine.setY(this.getRelativeY());
		printLine.setWidth(getWidth());
		printLine.setHeight(this.getStretchHeight());
		transferProperties(printLine);
		
		return printLine;
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
		visitor.visitLine(this);
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
		return new JRFillLine(this, factory);
	}

}
