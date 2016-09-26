/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.export.oasis;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class InternalImageProcessorResult 
{
	protected final String imagePath;
	protected int width;
	protected int height;
	protected int xoffset;
	protected int yoffset;
	protected double cropTop;
	protected double cropLeft;
	protected double cropBottom;
	protected double cropRight;

	protected InternalImageProcessorResult(
		String imagePath, 
		int width,
		int height,
		int xoffset,
		int yoffset,
		double cropTop,
		double cropLeft,
		double cropBottom,
		double cropRight
		
		)
	{
		this.imagePath = imagePath;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.cropTop = cropTop;
		this.cropLeft = cropLeft;
		this.cropBottom = cropBottom;
		this.cropRight = cropRight;
	}
}

