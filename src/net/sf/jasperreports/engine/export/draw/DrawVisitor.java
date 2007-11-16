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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.draw;

import java.awt.Graphics2D;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.convert.ConvertVisitor;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.util.JRStyledTextParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public class DrawVisitor implements JRVisitor
{
	
	private TextRenderer textRenderer = new TextRenderer(false);
	private JRStyledTextParser styledTextParser = new JRStyledTextParser();

	private ConvertVisitor convertVisitor = null;
	private Graphics2D grx = null;

	private LineDrawer lineDrawer = new LineDrawer();
	private RectangleDrawer rectangleDrawer = new RectangleDrawer();
	private EllipseDrawer ellipseDrawer = new EllipseDrawer();
	private ImageDrawer imageDrawer = new ImageDrawer();
	private TextDrawer textDrawer = new TextDrawer(textRenderer, styledTextParser);
	private FrameDrawer frameDrawer = new FrameDrawer(null, textRenderer, styledTextParser);
	
	/**
	 *
	 */
	public DrawVisitor(JRReport report, Graphics2D grx)
	{
		this(new ReportConverter(report, true), grx);
	}

	/**
	 *
	 */
	public DrawVisitor(ReportConverter reportConverter, Graphics2D grx)
	{
		this.convertVisitor = new ConvertVisitor(reportConverter);
		setGraphics2D(grx);
		frameDrawer.setClip(true);
	}

	/**
	 *
	 */
	public void setGraphics2D(Graphics2D grx)
	{
		this.grx = grx;
	}

	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement)
	{
		//FIXMEDRAW
	}

	/**
	 *
	 */
	public void visitChart(JRChart chart)
	{
		try
		{
			imageDrawer.draw(
				grx,
				convertVisitor.getVisitPrintElement(chart), 
				-chart.getX(), 
				-chart.getY()
				);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitCrosstab(JRCrosstab crosstab)
	{
		try
		{
			frameDrawer.draw(
				grx,
				convertVisitor.getVisitPrintElement(crosstab), 
				-crosstab.getX(), 
				-crosstab.getY()
				);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		//nothing to draw. elements are drawn individually.
	}

	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse)
	{
		ellipseDrawer.draw(
			grx,
			convertVisitor.getVisitPrintElement(ellipse), 
			-ellipse.getX(), 
			-ellipse.getY()
			);
	}

	/**
	 *
	 */
	public void visitFrame(JRFrame frame)
	{
		try
		{
			frameDrawer.draw(
				grx,
				convertVisitor.getVisitPrintElement(frame), 
				-frame.getX(), 
				-frame.getY()
				);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitImage(JRImage image)
	{
		try
		{
			imageDrawer.draw(
					grx,
					convertVisitor.getVisitPrintElement(image), 
					-image.getX(), 
					-image.getY()
					);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitLine(JRLine line)
	{
		lineDrawer.draw(
			grx,
			convertVisitor.getVisitPrintElement(line), 
			-line.getX(), 
			-line.getY()
			);
	}

	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle)
	{
		rectangleDrawer.draw(
			grx,
			convertVisitor.getVisitPrintElement(rectangle), 
			-rectangle.getX(), 
			-rectangle.getY()
			);
	}

	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText)
	{
		textDrawer.draw(
			grx,
			convertVisitor.getVisitPrintElement(staticText), 
			-staticText.getX(), 
			-staticText.getY()
			);
	}

	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport)
	{
		try
		{
			imageDrawer.draw(
				grx,
				convertVisitor.getVisitPrintElement(subreport), 
				-subreport.getX(), 
				-subreport.getY()
				);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitTextField(JRTextField textField)
	{
		textDrawer.draw(
			grx,
			convertVisitor.getVisitPrintElement(textField), 
			-textField.getX(), 
			-textField.getY()
			);
	}

}
