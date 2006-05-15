/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2005 Works, Inc.  http://www.works.com/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Works, Inc.
 * 6034 West Courtyard Drive
 * Suite 210
 * Austin, TX 78730-5032
 * USA
 * http://www.works.com/
 */

/*
 * Licensed to JasperSoft Corporation under a Contributer Agreement
 */
package net.sf.jasperreports.engine.fill;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Virtualizes data to the filesystem. When this object is finalized, it removes
 * the swap files it makes. The virtualized objects have references to this
 * object, so finalization does not occur until this object and the objects
 * using it are only weakly referenced.
 * 
 * @author John Bindel
 * @version $Id$
 */
public class JRFileVirtualizer extends JRAbstractLRUVirtualizer {
	
	private static final Log log = LogFactory.getLog(JRFileVirtualizer.class);

	private final String directory;

	/**
	 * Uses the process's working directory as the location to store files.
	 * 
	 * @param maxSize
	 *            the maximum size (in JRVirtualizable objects) of the paged in
	 *            cache.
	 */
	public JRFileVirtualizer(int maxSize) {
		this(maxSize, null);
	}

	/**
	 * @param maxSize
	 *            the maximum size (in JRVirtualizable objects) of the paged in
	 *            cache.
	 * @param directory
	 *            the base directory in the filesystem where the paged out data
	 *            is to be stored
	 */
	public JRFileVirtualizer(int maxSize, String directory) {
		super(maxSize);
		
		this.directory = directory;
	}

	private String makeFilename(JRVirtualizable o) {
		String uid = o.getUID();
		return "virt" + uid;
	}

	private String makeFilename(String virtualId) {
		return "virt" + virtualId;
	}

	protected void pageOut(JRVirtualizable o) throws IOException {
		// Store data to a file.
		String filename = makeFilename(o);
		File file = new File(directory, filename);
		
		if (file.createNewFile()) {
			file.deleteOnExit();

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				BufferedOutputStream bufferedOut = new BufferedOutputStream(fos);
				writeData(o, bufferedOut);
			}
			catch (FileNotFoundException e) {
				log.error("Error virtualizing object", e);
				throw new JRRuntimeException(e);
			}
			finally {
				if (fos != null) {
					fos.close();
				}
			}
		} else {
			if (!isReadOnly(o)) {
				throw new IllegalStateException(
						"Cannot virtualize data because the file \"" + filename
								+ "\" already exists.");
			}
		}
	}

	protected void pageIn(JRVirtualizable o) throws IOException {
		// Load data from a file.
		String filename = makeFilename(o);
		File file = new File(directory, filename);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			BufferedInputStream bufferedIn = new BufferedInputStream(fis);
			readData(o, bufferedIn);
		}
		catch (FileNotFoundException e) {
			log.error("Error devirtualizing object", e);
			throw new JRRuntimeException(e);
		}
		finally {
			if (fis != null) {
				fis.close();
			}
		}

		if (!isReadOnly(o)) {
			// Wait until we know it worked before tossing the data.
			file.delete();
		}
	}

	protected void dispose(String virtualId) {
		String filename = makeFilename(virtualId);
		File file = new File(directory, filename);
		file.delete();
	}
	
	
	/**
	 * Called when we are done with the virtualizer and wish to
	 * cleanup any resources it has.
	 */
	public synchronized void cleanup()
	{
		disposeAll();
		reset();
	}
}
