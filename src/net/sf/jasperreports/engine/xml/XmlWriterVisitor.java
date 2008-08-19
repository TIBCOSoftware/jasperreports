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
package net.sf.jasperreports.engine.xml;

import java.io.IOException;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public class XmlWriterVisitor implements JRVisitor
{
	
	private JRXmlWriter xmlWriter = null;
	
	/**
	 *
	 */
	public XmlWriterVisitor(JRXmlWriter xmlWriter)
	{
		this.xmlWriter = xmlWriter;
	}

	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement)
	{
		try
		{
			xmlWriter.writeBreak(breakElement);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitChart(JRChart chart)
	{
		try
		{
			xmlWriter.writeChartTag(chart);
		}
		catch (IOException e)
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
			xmlWriter.writeCrosstab(crosstab);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		try
		{
			xmlWriter.writeElementGroup(elementGroup);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse)
	{
		try
		{
			xmlWriter.writeEllipse(ellipse);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitFrame(JRFrame frame)
	{
		try
		{
			xmlWriter.writeFrame(frame);
		}
		catch (IOException e)
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
			xmlWriter.writeImage(image);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitLine(JRLine line)
	{
		try
		{
			xmlWriter.writeLine(line);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle)
	{
		try
		{
			xmlWriter.writeRectangle(rectangle);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText)
	{
		try
		{
			xmlWriter.writeStaticText(staticText);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport)
	{
		try
		{
			xmlWriter.writeSubreport(subreport);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public void visitTextField(JRTextField textField)
	{
		try
		{
			xmlWriter.writeTextField(textField);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public void visitComponentElement(JRComponentElement componentElement)
	{
		try
		{
			xmlWriter.writeComponentElement(componentElement);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
