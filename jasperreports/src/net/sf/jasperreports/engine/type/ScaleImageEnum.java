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
package net.sf.jasperreports.engine.type;

import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum ScaleImageEnum implements JREnum
{
	/**
	 * A constant value specifying that if the actual image is larger than the image element size, it will be cut off so
	 * that it keeps its original resolution, and only the region that fits the specified size will be displayed.
	 */
	CLIP((byte)1, "Clip"),

	/**
	 * A constant value specifying that if the dimensions of the actual image do not fit those specified for the
	 * image element that displays it, the image can be forced to obey them and stretch itself so that it fits
	 * in the designated output area.
	 */
	FILL_FRAME((byte)2, "FillFrame"),
	
	/**
	 * A constant value specifying that if the actual image does not fit into the image element, it can be adapted
	 * to those dimensions without needing to change its original proportions.
	 */
	RETAIN_SHAPE((byte)3, "RetainShape"),
	
	/**
	 * A scale image type that instructs the engine to stretch the image height
	 * to fit the actual height of the image.
	 * 
	 * <p>
	 * Several restrictions apply to the image stretching mechanism:
	 * <ul>
	 * 	<li>It only works when the image renderer implements
	 *  {@link Renderable#getDimension(JasperReportsContext)}.</li>
	 *  <li>If the actual image width exceeds the declared image element width,
	 * the image is proportionally stretched to fit the declared width.</li>
	 * 	<li>Images with delayed evaluation (see {@link JRImage#getEvaluationTimeValue()}) 
	 * do not stretch and is proportionally shrunk to fit the declared
	 * height/width.</li>
	 * 	<li>An image overflows (to the next page/column) only once, after this
	 * the image gets rendered on the available space by proportionally
	 * shrinking its size.</li>
	 * </ul>
	 * </p>
	 * 
	 * @see #REAL_SIZE
	 */
	REAL_HEIGHT((byte)4, "RealHeight"),
	
	/**
	 * A scale image type that stretches the images height in the same way as 
	 * {@link #REAL_HEIGHT}, and in addition it changes the image
	 * width to the actual with of the image.
	 * 
	 * This can be useful when, for instance, a border has to be drawn around
	 * the image, respecting its actual size.
	 */
	REAL_SIZE((byte)5, "RealSize");
	
	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private ScaleImageEnum(byte value, String name)
	{
		this.value = value;
		this.name = name;
	}

	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public Byte getValueByte()
	{
		return new Byte(value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public final byte getValue()
	{
		return value;
	}
	
	/**
	 *
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 *
	 */
	public static ScaleImageEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static ScaleImageEnum getByValue(Byte value)
	{
		return (ScaleImageEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static ScaleImageEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
