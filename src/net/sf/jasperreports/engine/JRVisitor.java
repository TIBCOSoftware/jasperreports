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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.crosstabs.JRCrosstab;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRChild.java 1229 2006-04-19 10:27:35Z teodord $
 */
public interface JRVisitor
{

	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement);

	/**
	 *
	 */
	public void visitChart(JRChart chart);

	/**
	 *
	 */
	public void visitCrosstab(JRCrosstab crosstab);

	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup);

	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse);

	/**
	 *
	 */
	public void visitFrame(JRFrame frame);

	/**
	 *
	 */
	public void visitImage(JRImage image);

	/**
	 *
	 */
	public void visitLine(JRLine line);

	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle);

	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText);

	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport);

	/**
	 *
	 */
	public void visitTextField(JRTextField textField);

	/**
	 * Visits a component wrapper element.
	 * 
	 * @param componentElement the element to visit
	 */
	public void visitComponentElement(JRComponentElement componentElement);

	/**
	 * Visits a generic report element.
	 * 
	 * @param element the element to visit
	 */
	public void visitGenericElement(JRGenericElement element);
}
