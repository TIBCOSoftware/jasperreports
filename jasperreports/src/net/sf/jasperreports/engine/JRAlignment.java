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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;


/**
 * An interface that defines constants useful for alignment. All report elements that can be aligned in some way
 * implement this interface.
 *
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRAlignment extends JRStyleContainer
{

	/**
	 * Gets the text horizontal alignment.
	 * @return a value representing one of the horizontal alignment constants in {@link HorizontalAlignEnum}
	 */
	public HorizontalAlignEnum getHorizontalAlignmentValue();

	public HorizontalAlignEnum getOwnHorizontalAlignmentValue();

	/**
	 * Sets the text horizontal alignment.
	 * @param horizontalAlignment a value representing one of the horizontal alignment constants in {@link HorizontalAlignEnum}
	 */
	public void setHorizontalAlignment(HorizontalAlignEnum horizontalAlignment);

	/**
	 * Gets the text vertical alignment.
	 * @return a value representing one of the vertical alignment constants in {@link VerticalAlignEnum}
	 */
	public VerticalAlignEnum getVerticalAlignmentValue();
	
	public VerticalAlignEnum getOwnVerticalAlignmentValue();

	/**
	 * Gets the text vertical alignment.
	 * @param verticalAlignment a value representing one of the vertical alignment constants in {@link VerticalAlignEnum}
	 */
	public void setVerticalAlignment(VerticalAlignEnum verticalAlignment);

}
