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

import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.type.BorderSplitType;



/**
 * An abstract representation of a report elements container.
 * <p>
 * A frame is a report element that contains sub elements.
 * It has a backgroud, a border and it stretches to accommodate its content.
 * It is usually helpful when a common background and/or common border must 
 * be put around a group of elements.
 * <p>
 * For the Graphics2D and PDF exporters, a frame is equivalent to a rectangle
 * placed behind a group of elements.  The HTML exporter creates sub-tables for frames
 * and the XLS exporter includes the frame sub elements into the grid.
 * <p>
 * For elements inside a frame, the coordinates, <code>positionType</code> and 
 * <code>stretchType</code> properties are relative to the frame instead of the band. 
 * <p/>
 * Frames can be nested into one another to any depth.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface JRFrame extends JRElement, JRElementGroup, JRBoxContainer
{
	
	/**
	 * A property that provides the default border split type for frames.
	 * 
	 * <p>
	 * The property can be set at report and global/context levels.
	 * The property value should be one of the names of the {@link BorderSplitType} enum,
	 * that is NoBorders or DrawBorders.
	 * By default {@link BorderSplitType#NO_BORDERS} is used.
	 * </p>
	 * 
	 * @see #getBorderSplitType()
	 */
	String PROPERTY_BORDER_SPLIT_TYPE = JRPropertiesUtil.PROPERTY_PREFIX + "frame.border.split.type";
	
	/**
	 * Determines how should the frames borders behave when the frame splits on two pages.
	 * 
	 * @return the border split type
	 * @see JRFrame#PROPERTY_BORDER_SPLIT_TYPE
	 * @see JRDesignFrame#setBorderSplitType(BorderSplitType)
	 */
	BorderSplitType getBorderSplitType();

}
