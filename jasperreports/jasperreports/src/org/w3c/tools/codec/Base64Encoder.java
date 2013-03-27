/*
 * Copyright � World Wide Web Consortium, (Massachusetts Institute of Technology, 
 * Institut National de Recherche en Informatique et en Automatique, Keio University).
 * All Rights Reserved. http://www.w3.org/Consortium/Legal/
 */
package org.w3c.tools.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * BASE64 encoder implementation.
 * This object takes as parameter an input stream and an output stream. It
 * encodes the input stream, using the BASE64 encoding rules, as defined
 * in <a href="http://ds.internic.net/rfc/rfc1521.txt">MIME specification</a>
 * and emit the resulting data to the output stream.
 * @see org.w3c.tools.codec.Base64Decoder
 */
public class Base64Encoder
{
	private static final int BUFFER_SIZE = 1024;
	private static byte encoding[] =
		{
			(byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', // 0-7
			(byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', // 8-15
			(byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', // 16-23
			(byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', // 24-31
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', // 32-39
			(byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', // 40-47
			(byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', // 48-55
			(byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/', // 56-63
			(byte) '=' // 64
	};

	InputStream in = null;
	OutputStream out = null;
	boolean stringp = false;

	private final int get1(byte buf[], int off)
	{
		return (buf[off] & 0xfc) >> 2;
	}

	private final int get2(byte buf[], int off)
	{
		return ((buf[off] & 0x3) << 4) | ((buf[off + 1] & 0xf0) >>> 4);
	}

	private final int get3(byte buf[], int off)
	{
		return ((buf[off + 1] & 0x0f) << 2) | ((buf[off + 2] & 0xc0) >>> 6);
	}

	private static final int get4(byte buf[], int off)
	{
		return buf[off + 2] & 0x3f;
	}

	/**
	 * Process the data: encode the input stream to the output stream.
	 * This method runs through the input stream, encoding it to the output 
	 * stream.
	 * @exception IOException If we weren't able to access the input stream or
	 *    the output stream.
	 */
	public void process() throws IOException
	{
		byte buffer[] = new byte[BUFFER_SIZE];
		int got = -1;
		int off = 0;
		int count = 0;
		while ((got = in.read(buffer, off, BUFFER_SIZE - off)) > 0)
		{
			if (got >= 3)
			{
				got += off;
				off = 0;
				while (off + 3 <= got)
				{
					int c1 = get1(buffer, off);
					int c2 = get2(buffer, off);
					int c3 = get3(buffer, off);
					int c4 = get4(buffer, off);
					switch (count)
					{
						case 73 :
							out.write(encoding[c1]);
							out.write(encoding[c2]);
							out.write(encoding[c3]);
							out.write('\n');
							out.write(encoding[c4]);
							count = 1;
							break;
						case 74 :
							out.write(encoding[c1]);
							out.write(encoding[c2]);
							out.write('\n');
							out.write(encoding[c3]);
							out.write(encoding[c4]);
							count = 2;
							break;
						case 75 :
							out.write(encoding[c1]);
							out.write('\n');
							out.write(encoding[c2]);
							out.write(encoding[c3]);
							out.write(encoding[c4]);
							count = 3;
							break;
						case 76 :
							out.write('\n');
							out.write(encoding[c1]);
							out.write(encoding[c2]);
							out.write(encoding[c3]);
							out.write(encoding[c4]);
							count = 4;
							break;
						default :
							out.write(encoding[c1]);
							out.write(encoding[c2]);
							out.write(encoding[c3]);
							out.write(encoding[c4]);
							count += 4;
							break;
					}
					off += 3;
				}
				// Copy remaining bytes to beginning of buffer:
				for (int i = 0; i < 3; i++)
				{
					buffer[i] = (i < got - off) ? buffer[off + i] : ((byte) 0);
				}
				off = got - off;
			}
			else
			{
				// Total read amount is less then 3 bytes:
				off += got;
			}
		}
		// Manage the last bytes, from 0 to off:
		switch (off)
		{
			case 1 :
				out.write(encoding[get1(buffer, 0)]);
				out.write(encoding[get2(buffer, 0)]);
				out.write('=');
				out.write('=');
				break;
			case 2 :
				out.write(encoding[get1(buffer, 0)]);
				out.write(encoding[get2(buffer, 0)]);
				out.write(encoding[get3(buffer, 0)]);
				out.write('=');
		}
		return;
	}

	/**
	 * Encode the content of this encoder, as a string.
	 * This methods encode the String content, that was provided at creation 
	 * time, following the BASE64 rules, as specified in the rfc1521.
	 * @return A String, reprenting the encoded content of the input String.
	 */
	public String processString()
	{
		if (!stringp)
		{
			throw new RuntimeException(
				this.getClass().getName()
					+ "[processString]"
					+ "invalid call (not a String)");
		}
		try
		{
			process();
		}
		catch (IOException e)
		{
		}
		return ((ByteArrayOutputStream) out).toString();
	}

	/**
	 * Create a new Base64 encoder, to encode the given string.
	 * @param input The String to be encoded.
	 */
	public Base64Encoder(String input)
	{
		byte bytes[];
		try
		{
			bytes = input.getBytes("ISO-8859-1");
		}
		catch (UnsupportedEncodingException ex)
		{
			throw new RuntimeException(
				this.getClass().getName()
					+ "[Constructor] Unable to convert"
					+ "properly char to bytes");
		}
		this.stringp = true;
		this.in = new ByteArrayInputStream(bytes);
		this.out = new ByteArrayOutputStream();
	}

	/**
	 * Create a new Base64 encoder, encoding input to output.
	 * @param in The input stream to be encoded.
	 * @param out The output stream, to write encoded data to.
	 */
	public Base64Encoder(InputStream in, OutputStream out)
	{
		this.in = in;
		this.out = out;
		this.stringp = false;
	}

	/**
	 * Testing the encoder.
	 * Run with one argument, prints the encoded version of it.
	 */
	public static void main(String args[])
	{
		if (args.length != 1)
		{
			System.out.println("Base64Encoder <string>");
			System.exit(0);
		}
		Base64Encoder b = new Base64Encoder(args[0]);
		System.out.println("[" + b.processString() + "]");
		// joe:eoj -> am9lOmVvag==
		// 12345678:87654321 -> MTIzNDU2Nzg6ODc2NTQzMjE=
	}
}
