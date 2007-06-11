/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 */
package net.sf.jasperreports.engine.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.jasperreports.engine.JRRuntimeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: FileBufferedOutputStream.java 8408 2007-05-29 23:29:12Z melih $
 */
public class FileBufferedOutputStream extends OutputStream 
{
	
	private static final Log log = LogFactory.getLog(FileBufferedOutputStream.class);
	
	/**
	 * Property specifying the ResultSet fetch size.
	 */
	public static final String PROPERTY_MEMORY_THRESHOLD = JRProperties.PROPERTY_PREFIX + "file.buffer.os.memory.threshold";
	//public static final int DEFAULT_MEMORY_THRESHOLD = 1 << 18;
	public static final int INFINIT_MEMORY_THRESHOLD = -1;
	public static final int DEFAULT_INITIAL_MEMORY_BUFFER_SIZE = 1 << 16;
	public static final int DEFAULT_INPUT_BUFFER_LENGTH = 1 << 14;
	
	private final int memoryThreshold;
	private final int initialMemoryBufferSize;
	private final int inputBufferLength;
	
	private final ByteArrayOutputStream memoryOutput;
	private int size;
	private File file;
	private BufferedOutputStream fileOutput;
	private boolean closed;
	private boolean disposed;
	
	public FileBufferedOutputStream() {
		this(JRProperties.getIntegerProperty(PROPERTY_MEMORY_THRESHOLD, INFINIT_MEMORY_THRESHOLD), DEFAULT_INITIAL_MEMORY_BUFFER_SIZE, DEFAULT_INPUT_BUFFER_LENGTH);
	}
	
	public FileBufferedOutputStream(int memoryThreshold) {
		this(memoryThreshold, DEFAULT_INITIAL_MEMORY_BUFFER_SIZE, DEFAULT_INPUT_BUFFER_LENGTH);
	}
	
	public FileBufferedOutputStream(int memoryThreshold, int initialMemoryBufferSize) {
		this(memoryThreshold, initialMemoryBufferSize, DEFAULT_INPUT_BUFFER_LENGTH);
	}
	
	public FileBufferedOutputStream(int memoryThreshold, int initialMemoryBufferSize, int inputBufferLength) {
		this.memoryThreshold = memoryThreshold;
		this.initialMemoryBufferSize = initialMemoryBufferSize;
		this.inputBufferLength = inputBufferLength;
		
		size = 0;
		memoryOutput = this.memoryThreshold != 0 ? new ByteArrayOutputStream(this.initialMemoryBufferSize) : null;
	}

	public void write(int b) throws IOException {
		checkClosed();
		
		if (availableMemorySpace() > 0) {
			memoryOutput.write(b);
		} else {
			ensureFileOutput().write(b);
		}
		
		++size;
	}

	protected int availableMemorySpace() {
		int availableMemorySpace;
		if (memoryOutput != null
				&& (memoryThreshold < 0 || memoryOutput.size() < memoryThreshold)) {
			availableMemorySpace = memoryThreshold - memoryOutput.size();
		} else {
			availableMemorySpace = 0;
		}
		return availableMemorySpace;
	}
	
	protected BufferedOutputStream ensureFileOutput() throws IOException, FileNotFoundException {
		if (fileOutput == null) {
			file = File.createTempFile("file.buff.os.", ".tmp");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutput = new BufferedOutputStream(fileOutputStream);
		}
		return fileOutput;
	}

	public void write(byte[] b, int off, int len) throws IOException {
		checkClosed();
		
		int memoryLen = availableMemorySpace();
		if (len < memoryLen) {
			memoryLen = len;
		}
		
		if (memoryLen > 0) {
			memoryOutput.write(b, off, memoryLen);
		}
		
		if (memoryLen < len) {
			ensureFileOutput().write(b, off + memoryLen, len - memoryLen);
		}
		
		size += len;
	}

	public void checkClosed() {
		if (closed) {
			throw new JRRuntimeException("Output stream already closed.");
		}
	}

	public void close() throws IOException {
		if (!closed && fileOutput != null) {
			fileOutput.flush();
			fileOutput.close();
		}
		
		closed = true;
	}

	public void flush() throws IOException {
		if (fileOutput != null) {
			fileOutput.flush();
		}
	}

	public int size() {
		return size;
	}
	
	public void writeData(OutputStream out) throws IOException {
		if (!closed) {
			close();
		}

		if (memoryOutput != null) {
			memoryOutput.writeTo(out);
		}
		
		if (file != null) {
			FileInputStream fileInput = new FileInputStream(file);
			boolean inputClosed = false;
			try {
				byte[] buffer = new byte[inputBufferLength];
				int read;
				while((read = fileInput.read(buffer)) > 0) {
					out.write(buffer, 0, read);
				}
				fileInput.close();
				inputClosed = true;
			} finally {
				if (!inputClosed) {
					try {
						fileInput.close();
					} catch (IOException e) {
						log.warn("Could not close file input stream", e);
					}
				}
			}
		}
	}
	
	public void dispose() {
		if (disposed) {
			return;
		}
		
		boolean success = true;
		if (!closed && fileOutput != null) {
			try {
				fileOutput.close();
			} catch (IOException e) {
				log.warn("Error while closing the temporary file output stream", e);
				success = false;
			}
		}
		
		if (file != null && !file.delete()) {
			log.warn("Error while deleting the temporary file");
			success = false;
		}
		
		disposed = success;
	}

	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}
	
	
}
