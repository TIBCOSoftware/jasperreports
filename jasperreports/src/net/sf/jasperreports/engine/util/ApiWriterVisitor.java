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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
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
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class ApiWriterVisitor implements JRVisitor
{
	
	private JRApiWriter apiWriter;
	private String name;
	
	/**
	 *
	 */
	public ApiWriterVisitor(JRApiWriter apiWriter)
	{
		this.apiWriter = apiWriter;
	}

	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement)
	{
		apiWriter.writeBreak(breakElement, name);
	}

	/**
	 *
	 */
	public void visitChart(JRChart chart)
	{
		apiWriter.writeChartTag(chart, name);
	}

	/**
	 *
	 */
	public void visitCrosstab(JRCrosstab crosstab)
	{
		apiWriter.writeCrosstab(crosstab, name);
	}

	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		apiWriter.writeElementGroup(elementGroup, name);
	}

	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse)
	{
		apiWriter.writeEllipse(ellipse, name);
	}

	/**
	 *
	 */
	public void visitFrame(JRFrame frame)
	{
		apiWriter.writeFrame(frame, name);
	}

	/**
	 *
	 */
	public void visitImage(JRImage image)
	{
		apiWriter.writeImage(image, name);
	}

	/**
	 *
	 */
	public void visitLine(JRLine line)
	{
		apiWriter.writeLine(line, name);
	}

	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle)
	{
		apiWriter.writeRectangle(rectangle, name);
	}

	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText)
	{
		apiWriter.writeStaticText(staticText, name);
	}

	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport)
	{
		apiWriter.writeSubreport(subreport, name);
	}

	/**
	 *
	 */
	public void visitTextField(JRTextField textField)
	{
		apiWriter.writeTextField(textField, name);
	}
	
	public void visitComponentElement(JRComponentElement componentElement)
	{
		apiWriter.writeComponentElement(componentElement, name);
	}

	public void visitGenericElement(JRGenericElement element)
	{
		apiWriter.writeGenericElement(element, name);
	}

	/**
     * @return the name
     */
    public String getName()
    {
    	return name;
    }

	/**
     * @param name the name to set
     */
    public void setName(String name)
    {
    	this.name = name;
    }

}
