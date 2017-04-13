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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Base64 encoder implementation.
 * This object encodes the input (byte array or String), using the Base64 encoding rules, as defined
 * in <a href="http://www.ietf.org/rfc/rfc2045.txt">MIME specification</a>
 * and emit the resulting data to an output stream.
 * 
 * @see org.apache.commons.codec.binary.Base64
 * 
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class CommonsBase64Encoder extends AbstractBase64Processor
{
	byte[] in;
	OutputStream out;
	boolean stringMode;
	Base64 encoder;
	
	/**
	 * Creates a new Base64 encoder, encoding input to output.
	 * @param in The byte[] array input to be encoded.
	 * @param out The output stream, to write encoded data to.
	 */
	public CommonsBase64Encoder(byte[] in, OutputStream out)
	{
		this.in = in;
		this.out = out;
		this.stringMode = false;
		this.encoder = new Base64(DEFAULT_LINE_LENGTH, DEFAULT_LINE_SEPARATOR);
	}

	/**
	 * Create a new Base64 encoder, to encode the given string.
	 * @param input The String to be encoded.
	 */
	public CommonsBase64Encoder(String input)
	{
		byte bytes[];
		try
		{
			bytes = input.getBytes("ISO-8859-1");
		}
		catch (UnsupportedEncodingException ex)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNABLE_TO_CONVERT_CHAR_TO_BYTE,
					new Object[]{this.getClass().getName()});
		}
		this.stringMode = true;
		this.in = bytes;
		this.out = new ByteArrayOutputStream();
		this.encoder = new Base64(DEFAULT_LINE_LENGTH, DEFAULT_LINE_SEPARATOR);
	}
	
	/**
	 * Process the data by encoding the input byte array to the output stream.
	 * @exception JRException If we weren't able to access the input stream or
	 *    the output stream.
	 */
	@Override
	public void process() throws JRException
	{
		if(in != null)
		{
			try {
				out.write(encoder.encode(in));
				out.flush();
			} catch (IOException e) {
				throw new JRException(e);
			}
		}
	}

	/**
	 * Encode the content of this encoder, as a string.
	 * This methods encode the String content, that was provided at creation 
	 * time, following the Base64 rules, as specified in the rfc2045.
	 * @return A String, representing the encoded content of the input String.
	 */
	public String processString() throws JRException
	{
		if (!stringMode)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_INVALID_CALL,
					new Object[]{this.getClass().getName()});
		}
		try
		{
			process();
		}
		catch (JRException e)
		{
		}
		String s;
		try
		{
			s = ((ByteArrayOutputStream) out).toString("ISO-8859-1");	//FIXME: other charsets?
		}
		catch (UnsupportedEncodingException ex)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_UNABLE_TO_CONVERT_CHAR_TO_BYTE,
					new Object[]{this.getClass().getName()});
		}
		return s;
	}
}
