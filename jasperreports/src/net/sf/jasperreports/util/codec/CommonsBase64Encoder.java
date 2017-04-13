/*
 * Copyright � World Wide Web Consortium, (Massachusetts Institute of Technology, 
 * Institut National de Recherche en Informatique et en Automatique, Keio University).
 * All Rights Reserved. http://www.w3.org/Consortium/Legal/
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
