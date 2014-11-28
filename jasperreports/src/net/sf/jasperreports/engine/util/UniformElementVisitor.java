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

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;

/**
 * An abstract visitor class that treats all report elements in the same way. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see #visitElement(JRElement)
 */
public abstract class UniformElementVisitor implements JRVisitor
{

	/**
	 * Method that gets called when any element is visited, no matter what its type is.
	 * 
	 * @param element the element to be visited
	 */
	protected abstract void visitElement(JRElement element);
	
	public void visitBreak(JRBreak breakElement)
	{
		visitElement(breakElement);
	}

	public void visitChart(JRChart chart)
	{
		visitElement(chart);
	}

	public void visitComponentElement(JRComponentElement componentElement)
	{
		visitElement(componentElement);
	}

	public void visitCrosstab(JRCrosstab crosstab)
	{
		visitElement(crosstab);
	}

	public void visitEllipse(JREllipse ellipse)
	{
		visitElement(ellipse);
	}

	public void visitFrame(JRFrame frame)
	{
		visitElement(frame);
	}

	public void visitGenericElement(JRGenericElement element)
	{
		visitElement(element);
	}

	public void visitImage(JRImage image)
	{
		visitElement(image);
	}

	public void visitLine(JRLine line)
	{
		visitElement(line);
	}

	public void visitRectangle(JRRectangle rectangle)
	{
		visitElement(rectangle);
	}

	public void visitStaticText(JRStaticText staticText)
	{
		visitElement(staticText);
	}

	public void visitSubreport(JRSubreport subreport)
	{
		visitElement(subreport);
	}

	public void visitTextField(JRTextField textField)
	{
		visitElement(textField);
	}

}
