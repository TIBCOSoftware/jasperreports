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
package net.sf.jasperreports.engine.util;

import java.util.List;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.PrintElementVisitor;

/**
 * Print element visitor decorator that recursively visits {@link JRPrintFrame}
 * subelements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DeepPrintElementVisitor<T> implements PrintElementVisitor<T>
{
	private final PrintElementVisitor<T> visitor;

	/**
	 * Creates a deep visitor by wrapping a visitor to which the calls will be
	 * delegated.
	 * 
	 * @param visitor
	 */
	public DeepPrintElementVisitor(PrintElementVisitor<T> visitor)
	{
		this.visitor = visitor;
	}
	
	public void visit(JRPrintText textElement, T arg)
	{
		visitor.visit(textElement, arg);
	}

	public void visit(JRPrintImage image, T arg)
	{
		visitor.visit(image, arg);
	}

	public void visit(JRPrintRectangle rectangle, T arg)
	{
		visitor.visit(rectangle, arg);
	}

	public void visit(JRPrintLine line, T arg)
	{
		visitor.visit(line, arg);
	}

	public void visit(JRPrintEllipse ellipse, T arg)
	{
		visitor.visit(ellipse, arg);
	}

	public void visit(JRPrintFrame frame, T arg)
	{
		visitor.visit(frame, arg);
		
		List<JRPrintElement> elements = frame.getElements();
		if (elements != null)
		{
			for (JRPrintElement element : elements)
			{
				element.accept(this, arg);
			}
		}
	}

	public void visit(JRGenericPrintElement printElement, T arg)
	{
		visitor.visit(printElement, arg);
	}

}
