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
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementContainer;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.PrintElementVisitor;

/**
 * Implementation of {@link net.sf.jasperreports.engine.JRPrintFrame JRPrintFrame} that uses
 * {@link net.sf.jasperreports.engine.fill.JRTemplateFrame template frames} to store common
 * attributes. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintFrame extends JRTemplatePrintElement implements JRPrintFrame, JRPrintElementContainer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private List elements;

	/**
	 * Creates a print frame element.
	 * 
	 * @param templateFrame the template frame that the element will use
	 */
	public JRTemplatePrintFrame(JRTemplateFrame templateFrame)
	{
		super(templateFrame);
		
		elements = new ArrayList();
	}

	public List getElements()
	{
		return elements;
	}

	public void addElement(JRPrintElement element)
	{
		elements.add(element);
	}

	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return ((JRTemplateFrame)template).getLineBox();
	}
		
	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}

	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		visitor.visit(this, arg);
	}
}
