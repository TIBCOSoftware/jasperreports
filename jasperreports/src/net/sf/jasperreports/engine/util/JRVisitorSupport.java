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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
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
 * Abstract implementation of {@link JRVisitor} that has empty visit methods for
 * all visitable types.
 * 
 * This class can be used as base class by visitors that do not want to implement
 * all methods.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRVisitorSupport implements JRVisitor
{

	public void visitBreak(JRBreak breakElement)
	{
		// NOOP
	}

	public void visitChart(JRChart chart)
	{
		// NOOP
	}

	public void visitCrosstab(JRCrosstab crosstab)
	{
		// NOOP
	}

	public void visitElementGroup(JRElementGroup elementGroup)
	{
		// NOOP
	}

	public void visitEllipse(JREllipse ellipse)
	{
		// NOOP
	}

	public void visitFrame(JRFrame frame)
	{
		// NOOP
	}

	public void visitImage(JRImage image)
	{
		// NOOP
	}

	public void visitLine(JRLine line)
	{
		// NOOP
	}

	public void visitRectangle(JRRectangle rectangle)
	{
		// NOOP
	}

	public void visitStaticText(JRStaticText staticText)
	{
		// NOOP
	}

	public void visitSubreport(JRSubreport subreport)
	{
		// NOOP
	}

	public void visitTextField(JRTextField textField)
	{
		// NOOP
	}

	public void visitComponentElement(JRComponentElement componentElement)
	{
		// NOOP
	}

	public void visitGenericElement(JRGenericElement element)
	{
		// NOOP
	}

}
