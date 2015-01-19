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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;


/**
 * An interface that defines constants useful for image alignment.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRImageAlignment extends JRStyleContainer
{

	/**
	 * Gets the image horizontal alignment.
	 * @return a value representing one of the horizontal image alignment constants in {@link HorizontalImageAlignEnum}
	 */
	public HorizontalImageAlignEnum getHorizontalImageAlign();

	public HorizontalImageAlignEnum getOwnHorizontalImageAlign();

	/**
	 * Sets the image horizontal alignment.
	 * @param horizontalAlignment a value representing one of the horizontal image alignment constants in {@link HorizontalImageAlignEnum}
	 */
	public void setHorizontalImageAlign(HorizontalImageAlignEnum horizontalAlignment);

	/**
	 * Gets the image vertical alignment.
	 * @return a value representing one of the vertical image alignment constants in {@link VerticalImageAlignEnum}
	 */
	public VerticalImageAlignEnum getVerticalImageAlign();
	
	public VerticalImageAlignEnum getOwnVerticalImageAlign();

	/**
	 * Gets the image vertical alignment.
	 * @param verticalAlignment a value representing one of the vertical image alignment constants in {@link VerticalImageAlignEnum}
	 */
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalAlignment);

}
