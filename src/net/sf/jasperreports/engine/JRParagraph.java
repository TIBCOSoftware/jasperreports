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
package net.sf.jasperreports.engine;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRAlignment.java 3419 2010-02-18 08:16:30Z teodord $
 */
public interface JRParagraph extends JRStyleContainer
{

	/**
	 * 
	 */
	public JRParagraph clone(JRParagraphContainer paragraphContainer);

	/**
	 * Gets the text tab stop width.
	 */
	public Integer getTabStop();
	
	/**
	 * Gets the text own tab stop width.
	 */
	public Integer getOwnTabStop();
	
	/**
	 * Sets the text own tab stop width.
	 */
	public void setTabStop(Integer tabStop);

}
