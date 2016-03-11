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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum ImageTypeEnum implements JREnum
{
	/**
	 * Specifies that the image is of unknown type.
	 */ 
	UNKNOWN((byte)0, "Unknown", null, null),
	
	/**
	 * Specifies that the image is of GIF type.
	 */ 
	GIF((byte)1, "GIF", "image/gif", "gif"),
	
	/**
	 * Specifies that the image is of JPEG type.
	 */ 
	JPEG((byte)2, "JPEG", "image/jpeg", "jpg"),
	
	/**
	 * Specifies that the image is of PNG type.
	 */ 
	PNG((byte)3, "PNG", "image/png", "png"),
	
	/**
	 * Specifies that the image is of TIFF type.
	 */ 
	TIFF((byte)3, "TIFF", "image/tiff", "tiff");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;
	private final transient String mimeType;
	private final transient String fileExtension;

	private ImageTypeEnum(
		byte value, 
		String name, 
		String mimeType,
		String fileExtension
		)
	{
		this.value = value;
		this.name = name;
		this.mimeType = mimeType;
		this.fileExtension = fileExtension;
	}

	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	@Override
	public Byte getValueByte()
	{
		return new Byte(value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	@Override
	public final byte getValue()
	{
		return value;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	/**
	 *
	 */
	public String getMimeType()
	{
		return mimeType;
	}
	
	/**
	 *
	 */
	public String getFileExtension()
	{
		return fileExtension;
	}
	
	/**
	 *
	 */
	public static ImageTypeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static ImageTypeEnum getByValue(Byte value)
	{
		return (ImageTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static ImageTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
