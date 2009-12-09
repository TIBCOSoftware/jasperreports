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
 * @version $Id$
 */
public interface JRPrintGraphicElement extends JRPrintElement, JRCommonGraphicElement
{

	/**
	 * Indicates the pen type used for this element.
	 * @return one of the pen constants in this class
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public byte getPen();

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public Byte getOwnPen();

	/**
	 * Sets the pen type that will used for this element.
	 * @param pen one of the pen constants in this class
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(byte pen);

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(Byte pen);
		
	/**
	 * Sets the fill type used for this element.
	 * @param fill one of the pen constants in this class
	 */
	public void setFill(byte fill);
	
	public void setFill(Byte fill);
		
	
}
