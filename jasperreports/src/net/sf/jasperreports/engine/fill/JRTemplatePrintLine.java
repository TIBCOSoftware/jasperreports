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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.type.LineDirectionEnum;


/**
 * Implementation of {@link net.sf.jasperreports.engine.JRPrintLine} that uses
 * a {@link net.sf.jasperreports.engine.fill.JRTemplateLine} instance to
 * store common attributes. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintLine extends JRTemplatePrintGraphicElement implements JRPrintLine
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	/**
	 * Creates a print line element.
	 * 
	 * @param line the template line that the element will use
	 */
	public JRTemplatePrintLine(JRTemplateLine line)
	{
		super(line);
	}
	
	/**
	 * @deprecated Replaced by {@link #getDirectionValue()}.
	 */
	public byte getDirection()
	{
		return getDirectionValue().getValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setDirection(LineDirectionEnum)}.
	 */
	public void setDirection(byte direction)
	{
	}

	/**
	 * 
	 */
	public LineDirectionEnum getDirectionValue()
	{
		return ((JRTemplateLine)this.template).getDirectionValue();
	}
		
	/**
	 * 
	 */
	public void setDirection(LineDirectionEnum direction)
	{
	}

	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		visitor.visit(this, arg);
	}

}
