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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import java.util.List;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;
import net.sf.jasperreports.engine.base.JRBasePrintRectangle;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ConvertVisitor implements JRVisitor
{
	
	protected ReportConverter reportConverter;
	protected JRBasePrintFrame parentFrame;
	protected JRPrintElement printElement;
	
	/**
	 *
	 */
	public ConvertVisitor(ReportConverter reportConverter)
	{
		this(reportConverter, null);
	}

	/**
	 *
	 */
	public ConvertVisitor(ReportConverter reportConverter, JRBasePrintFrame parentFrame)
	{
		this.reportConverter = reportConverter;
		this.parentFrame = parentFrame;
	}

	/**
	 *
	 */
	public JRPrintElement getVisitPrintElement(JRVisitable visitable)
	{
		if (visitable != null)
		{
			visitable.visit(this);
			return printElement;
		}
		return null;
	}

	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement)
	{
		//FIXMECONVERT
	}

	/**
	 *
	 */
	public void visitChart(JRChart chart)
	{
		JRPrintElement printImage = ChartConverter.getInstance().convert(reportConverter, chart);
		addElement(parentFrame, printImage);
		addContour(reportConverter, parentFrame, printImage);
	}

	/**
	 *
	 */
	public void visitCrosstab(JRCrosstab crosstab)
	{
		JRPrintElement printFrame = CrosstabConverter.getInstance().convert(reportConverter, crosstab); 
		addElement(parentFrame, printFrame);
		addContour(reportConverter, parentFrame, printFrame);
	}

	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		List<JRChild> children = elementGroup.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				children.get(i).visit(this);
			}
		}
	}

	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse)
	{
		addElement(parentFrame, EllipseConverter.getInstance().convert(reportConverter, ellipse));
	}

	/**
	 *
	 */
	public void visitFrame(JRFrame frame)
	{
		JRPrintElement printFrame = FrameConverter.getInstance().convert(reportConverter, frame); 
		addElement(parentFrame, printFrame);
		addContour(reportConverter, parentFrame, printFrame);
	}

	/**
	 *
	 */
	public void visitImage(JRImage image)
	{
		JRPrintElement printImage = ImageConverter.getInstance().convert(reportConverter, image);
		addElement(parentFrame, printImage);
		addContour(reportConverter, parentFrame, printImage);
	}

	/**
	 *
	 */
	public void visitLine(JRLine line)
	{
		addElement(parentFrame, LineConverter.getInstance().convert(reportConverter, line));
	}

	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle)
	{
		addElement(parentFrame, RectangleConverter.getInstance().convert(reportConverter, rectangle));
	}

	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText)
	{
		JRPrintElement printText = StaticTextConverter.getInstance().convert(reportConverter, staticText);
		addElement(parentFrame, printText);
		addContour(reportConverter, parentFrame, printText);
	}

	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport)
	{
		JRPrintElement printImage = SubreportConverter.getInstance().convert(reportConverter, subreport);
		addElement(parentFrame, printImage);
		addContour(reportConverter, parentFrame, printImage);
	}

	/**
	 *
	 */
	public void visitTextField(JRTextField textField)
	{
		JRPrintElement printText = TextFieldConverter.getInstance().convert(reportConverter, textField);
		addElement(parentFrame, printText);
		addContour(reportConverter, parentFrame, printText);
	}

	/**
	 *
	 */
	protected void addElement(JRBasePrintFrame frame, JRPrintElement element)
	{
		printElement = element;
		if (frame != null)
		{
			frame.addElement(element);
		}
	}
	
	/**
	 *
	 */
	protected void addContour(ReportConverter reportConverter, JRBasePrintFrame frame, JRPrintElement element)
	{
		if (frame != null)
		{
			boolean hasContour = false;
			JRLineBox box = element instanceof JRBoxContainer ? ((JRBoxContainer)element).getLineBox() : null; 
			if (box == null)
			{
				JRPrintGraphicElement graphicElement = element instanceof JRPrintGraphicElement ? (JRPrintGraphicElement)element : null;
				hasContour = (graphicElement == null) || graphicElement.getLinePen().getLineWidth().floatValue() <= 0f; 
			}
			else
			{
				hasContour = 
					box.getTopPen().getLineWidth().floatValue() <= 0f 
					&& box.getLeftPen().getLineWidth().floatValue() <= 0f 
					&& box.getRightPen().getLineWidth().floatValue() <= 0f 
					&& box.getBottomPen().getLineWidth().floatValue() <= 0f;
			}
			
			if (hasContour)
			{
				JRBasePrintRectangle rectangle = new JRBasePrintRectangle(reportConverter.getDefaultStyleProvider());
				rectangle.setUUID(element.getUUID());
				rectangle.setX(element.getX());
				rectangle.setY(element.getY());
				rectangle.setWidth(element.getWidth());
				rectangle.setHeight(element.getHeight());
				rectangle.getLinePen().setLineWidth(0.1f);
				rectangle.getLinePen().setLineStyle(LineStyleEnum.DASHED);
				rectangle.getLinePen().setLineColor(ReportConverter.GRID_LINE_COLOR);
				rectangle.setMode(ModeEnum.TRANSPARENT);
				frame.addElement(rectangle);
			}
		}
	}

	public void visitComponentElement(JRComponentElement componentElement)
	{
		JRPrintElement image = ComponentElementConverter.getInstance()
			.convert(reportConverter, componentElement);
		addElement(parentFrame, image);
		addContour(reportConverter, parentFrame, image);
	}

	public void visitGenericElement(JRGenericElement element)
	{
		JRPrintElement image = GenericElementConverter.getInstance()
			.convert(reportConverter, element);
		addElement(parentFrame, image);
		addContour(reportConverter, parentFrame, image);
	}
	
}
