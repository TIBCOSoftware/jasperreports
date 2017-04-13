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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Decode a Base64 encoded input (byte array, input stream, or String) to an output stream.
 * This class implements Base64 decoding, as specified in the
 * <a href="http://www.ietf.org/rfc/rfc2045.txt">MIME specification</a>.
 * 
 * @see org.apache.commons.codec.binary.Base64
 * 
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class CommonsBase64Decoder extends AbstractBase64Processor
{
	byte[] in;
	OutputStream out;
	boolean stringMode;
	Base64 decoder;

	/**
	 * Create a decoder to decode a byte array.
	 * @param in The byte[] array input to be decoded.
	 * @param out The output stream, to write decoded data to.
	 */
	public CommonsBase64Decoder(byte[] in, OutputStream out)
	{
		this.in = in;
		this.out = out;
		this.stringMode = false;
		this.decoder = new Base64(DEFAULT_LINE_LENGTH, DEFAULT_LINE_SEPARATOR);
	}
	
	/**
	 * Create a decoder to decode a String.
	 * @param input The string to be decoded.
	 */
	public CommonsBase64Decoder(String input)
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
		this.decoder = new Base64(DEFAULT_LINE_LENGTH, DEFAULT_LINE_SEPARATOR);
	}

	/**
	 * Create a decoder to decode a stream.
	 * @param in The input stream (to be decoded).
	 * @param out The output stream, to write decoded data to.
	 */
	public CommonsBase64Decoder(InputStream in, OutputStream out)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		try
		{
			while ((i = in.read()) > 0)
			{
				baos.write(i);
			}
			baos.flush();
			this.in = baos.toByteArray();
			this.out = out;
			this.stringMode = false;
			this.decoder = new Base64(DEFAULT_LINE_LENGTH, DEFAULT_LINE_SEPARATOR);
			baos.close();
		}
		catch(IOException e)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNABLE_TO_READ_DATA,
					new Object[]{this.getClass().getName()});
		}
	}

	/**
	 * Does the actual decoding.
	 * Process the input by decoding it and writing the resulting bytes
	 * into the output stream.
	 * @exception JRException If the input or output stream accesses failed, or 
	 * the input stream is not compliant with the Base64 specification.
	 */
	@Override
	public void process() throws JRException
	{
		if(in != null)
		{
			try {
				out.write(decoder.decode(in));
				out.flush();
			} catch (IOException e) {
				throw new JRException(e);
			}
		}
	}

	/**
	 * Do the decoding, and return a String.
	 * This methods should be called when the decoder is used in
	 * <em>String</em> mode. It decodes the input string to an output string
	 * that is returned.
	 * @exception JRException If the object wasn't constructed to
	 * decode a String, or if the input string is not compliant 
	 * with the BASE64 specification.
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
			s = ((ByteArrayOutputStream) out).toString("ISO-8859-1");
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
