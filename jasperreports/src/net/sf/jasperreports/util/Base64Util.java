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
package net.sf.jasperreports.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * Utility class to decode Base64 encoded input stream to output stream 
 * or to Base64 encode input stream to output stream
 * 
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class Base64Util
{
	public static final Integer DEFAULT_LINE_LENGTH = 76;
	public static final byte[] DEFAULT_LINE_SEPARATOR = "\n".getBytes();
	private static final Base64 processor = new Base64(DEFAULT_LINE_LENGTH, DEFAULT_LINE_SEPARATOR);
	
	/**
	 * Decode a byte array input and write processed data to an output stream
	 * @param in the byte array to be decoded
	 * @param out the output stream to write the decoded data
	 * @throws IOException
	 */
	public static void decode(byte[] in, OutputStream out) throws IOException
	{
		if(in != null)
		{
			out.write(processor.decode(in));
			out.flush();
		}
	}
	
	/**
	 * Decode an input stream and write processed data to an output stream
	 * @param in the input stream to be decoded
	 * @param out the output stream to write the decoded data
	 * @throws IOException
	 */
	public static void decode(InputStream in, OutputStream out) throws IOException
	{
		decode(getBytes(in), out);
	}
	
	/**
	 * Decode a String input and write processed data to an output stream
	 * @param in the String to be decoded
	 * @param out the output stream to write the decoded data
	 * @throws IOException
	 */
	public static void decode(String in, OutputStream out) throws IOException
	{
		if(in != null)
		{
			decode(in.getBytes(), out);
		}
	}
	
	/**
	 * Encode a byte array input and write processed data to an output stream
	 * @param in the byte array to be encoded
	 * @param out the output stream to write the encoded data
	 * @throws IOException
	 */
	public static void encode(byte[] in, OutputStream out) throws IOException
	{
		if(in != null)
		{
			out.write(processor.encode(in));
			out.flush();
		}
	}
	
	/**
	 * Encode an input stream and write processed data to an output stream
	 * @param in the input stream to be encoded
	 * @param out the output stream to write the encoded data
	 * @throws IOException
	 */
	public static void encode(InputStream in, OutputStream out) throws IOException
	{
		encode(getBytes(in), out);
	}
	
	/**
	 * Encode a String input and write processed data to an output stream
	 * @param in the String to be encoded
	 * @param out the output stream to write the encoded data
	 * @throws IOException
	 */
	public static void encode(String in, OutputStream out) throws IOException
	{
		if(in != null)
		{
			encode(in.getBytes(), out);
		}
	}
	
	/**
	 * 
	 * @param in the input stream to be processed
	 * @return the byte array contained in the input stream
	 * @throws IOException
	 */
	private static byte[] getBytes(InputStream in) throws IOException
	{
		if(in != null)
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] data = new byte[4096];
			int i = -1;
			while ((i = in.read(data, 0, data.length)) != -1) 
			{
				out.write(data, 0, i);
			}
			out.flush();
			return out.toByteArray();
		}
		return null;
	}
}
