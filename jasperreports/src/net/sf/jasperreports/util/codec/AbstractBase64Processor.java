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
package net.sf.jasperreports.util.codec;

import java.io.OutputStream;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public abstract class AbstractBase64Processor implements Base64Processor
{
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_CALL = "base64.invalid.call";
	public static final String EXCEPTION_MESSAGE_KEY_UNABLE_TO_CONVERT_CHAR_TO_BYTE = "base64.unable.to.convert.char.to.byte";
	public static final String EXCEPTION_MESSAGE_KEY_UNABLE_TO_READ_DATA = "base64.unable.to.read.data";
	
	public static final Integer DEFAULT_LINE_LENGTH = 76;
	public static final byte[] DEFAULT_LINE_SEPARATOR = "\n".getBytes();
	
	public static Base64Processor getDecoder(byte[] in, OutputStream out)
	{
		return getDecoder(null, in, out);
	}
	
	public static Base64Processor getDecoder(CodecTypeEnum type, byte[] in, OutputStream out)
	{
		return (type == null || type.equals(CodecTypeEnum.COMMONS))
				? new CommonsBase64Decoder(in, out) 
				: null;
	}
	
	public static Base64Processor getDecoder(String input)
	{
		return getDecoder(null, input);
	}
	
	public static Base64Processor getDecoder(CodecTypeEnum type, String input)
	{
		return (type == null || type.equals(CodecTypeEnum.COMMONS))
				? new CommonsBase64Decoder(input)
				: null;
	}
	
	public static Base64Processor getEncoder(byte[] in, OutputStream out)
	{
		return getEncoder(null, in, out);
	}
	
	public static Base64Processor getEncoder(CodecTypeEnum type, byte[] in, OutputStream out)
	{
		return (type == null || type.equals(CodecTypeEnum.COMMONS))
				? new CommonsBase64Encoder(in, out)
				: null;
	}
	
	public static Base64Processor getEncoder(String input)
	{
		return getEncoder(null, input);
	}
	
	public static Base64Processor getEncoder(CodecTypeEnum type, String input)
	{
		return (type == null || type.equals(CodecTypeEnum.COMMONS))
				? new CommonsBase64Encoder(input)
				: null;
	}
	
}
