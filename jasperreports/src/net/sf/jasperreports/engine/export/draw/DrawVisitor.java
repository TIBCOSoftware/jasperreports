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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.draw;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.convert.ConvertVisitor;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.util.UniformElementVisitor;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DrawVisitor extends UniformElementVisitor
{

	protected ConvertVisitor convertVisitor;
	protected PrintDrawVisitor drawVisitor;
	
	/**
	 *
	 */
	public DrawVisitor(JRReport report, Graphics2D grx)
	{
		this(new ReportConverter(report, true), grx);

		setTextRenderer(report);
	}

	/**
	 *
	 */
	public DrawVisitor(ReportConverter reportConverter, Graphics2D grx)
	{
		this.convertVisitor = new ConvertVisitor(reportConverter);
		this.drawVisitor = new PrintDrawVisitor();
		setTextRenderer(reportConverter.getReport());
		setGraphics2D(grx);
		this.drawVisitor.setClip(true);
	}

	/**
	 *
	 */
	public void setGraphics2D(Graphics2D grx)
	{
		drawVisitor.setGraphics2D(grx);
	}

	/**
	 *
	 */
	public void setTextRenderer(JRReport report)
	{
		drawVisitor.setTextRenderer(report);
	}

	/**
	 *
	 */
	@Override
	public void visitBreak(JRBreak breakElement)
	{
		//FIXMEDRAW
	}

	@Override
	protected void visitElement(JRElement element)
	{
		JRPrintElement printElement = convertVisitor.getVisitPrintElement(element);
		printElement.accept(drawVisitor, elementOffset(element));
	}

	protected Offset elementOffset(JRElement element)
	{
		return new Offset(-element.getX(), -element.getY());
	}
	
	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		//nothing to draw. elements are drawn individually.
	}

}
