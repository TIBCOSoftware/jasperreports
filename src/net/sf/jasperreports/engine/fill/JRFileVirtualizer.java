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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizer;

import org.apache.commons.collections.LRUMap;
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
public class JRFileVirtualizer implements JRVirtualizer {
	
	private static final Log log = LogFactory.getLog(JRBaseFiller.class);

	/**
	 * This class keeps track of how many objects are currently in memory, and
	 * when there are too many, it pushes the last touched one to disk.
	 */
	private static class Cache extends LRUMap {
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		private final JRFileVirtualizer virt;

		Cache(int maxSize, JRFileVirtualizer virt) {
			super(maxSize);
			
			this.virt = virt;
		}

		protected void processRemovedLRU(Object key, Object value)
		{
			virt.virtualizeData((JRVirtualizable) value);
		}
	}

	private final Cache pagedIn;

	private final HashMap pagedOut;

	private final String directory;

	private JRVirtualizable lastObject;

	private boolean readOnly;

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
		this.pagedIn = new Cache(maxSize, this);
		this.pagedOut = new HashMap();
		this.directory = directory;
		this.lastObject = null;
		this.readOnly = false;
	}

	public void setReadOnly(boolean ro) {
		this.readOnly = ro;
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public void registerObject(JRVirtualizable o) {
		Object old = pagedIn.put(o.getUID(), o);
		if (old != null) {
			pagedIn.put(o.getUID(), old);
			throw new IllegalStateException("Wrong object stored with UID \""
					+ o.getUID() + "\"");
		}
		this.lastObject = o;
	}

	public void deregisterObject(JRVirtualizable o) {
		String uid = o.getUID();
		Object old = pagedIn.remove(uid);
		if (old != null) {
			if (old != o) {
				pagedIn.put(uid, old);
				throw new IllegalStateException(
						"Wrong object stored with UID \"" + o.getUID() + "\"");
			}
		} else {
			old = pagedOut.remove(uid);
			if (old != o) {
				pagedIn.put(uid, old);
				throw new IllegalStateException(
						"Wrong object stored with UID \"" + o.getUID() + "\"");
			}
		}

		// We don't really care if someone deregisters an object
		// that's not registered.
	}

	public void touch(JRVirtualizable o) {
		// If we just touched this object, don't touch it again.
		if (this.lastObject != o) {
			this.lastObject = (JRVirtualizable) pagedIn.get(o.getUID());
		}
	}

	public void requestData(JRVirtualizable o) {
		String uid = o.getUID();
		if (pagedOut.containsKey(uid)) {
			try {
				// unvirtualize
				filePageIn(o);
			} catch (IOException ioe) {
				throw new JRRuntimeException(ioe);
			} catch (ClassNotFoundException cnfe) {
				throw new JRRuntimeException(cnfe);
			}

			pagedOut.remove(uid);
			pagedIn.put(uid, o);
			this.lastObject = o;
		}
	}

	public void clearData(JRVirtualizable o) {
		String uid = o.getUID();
		if (pagedOut.containsKey(uid)) {
			// remove virtual data
			fileDispose(o);
			pagedOut.remove(uid);
		}
	}

	public void virtualizeData(JRVirtualizable o) {
		String uid = o.getUID();
		if (!pagedOut.containsKey(uid)) {
			try {
				// virtualize
				filePageOut(o);
			} catch (IOException ioe) {
				throw new JRRuntimeException(ioe);
			}

			pagedOut.put(uid, o);
		}
	}

	protected void finalize() throws Throwable {
		cleanup();
		
		super.finalize();
	}

	private String makeFilename(JRVirtualizable o) {
		String uid = o.getUID();
		return "virt" + uid;
	}

	private void filePageOut(JRVirtualizable o) throws IOException {
		// Store data to a file.
		String filename = makeFilename(o);
		File file = new File(directory, filename);
		if (file.createNewFile()) {
			file.deleteOnExit();

			ObjectOutputStream oos = null;
			try {
				FileOutputStream fos = new FileOutputStream(file);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(o.getIdentityData());
				oos.writeObject(o.getVirtualData());
				oos.flush();
			} finally {
				if (oos != null) {
					oos.close();
				}
			}
		} else {
			if (!readOnly) {
				throw new IllegalStateException(
						"Cannot virtualize data because the file \"" + filename
								+ "\" already exists.");
			}
		}

		// Wait until we know it worked before tossing the data.
		o.removeVirtualData();
	}

	private void filePageIn(JRVirtualizable o) throws IOException,
			ClassNotFoundException {
		// Load data from a file.
		String filename = makeFilename(o);
		File file = new File(directory, filename);

		ObjectInputStream ois = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			o.setIdentityData(ois.readObject());
			o.setVirtualData(ois.readObject());
		} finally {
			if (ois != null) {
				ois.close();
			}
		}

		if (!readOnly) {
			// Wait until we know it worked before tossing the data.
			file.delete();
		}
	}

	private void fileDispose(JRVirtualizable o) {
		String filename = makeFilename(o);
		File file = new File(directory, filename);
		file.delete();
	}
	
	
	/**
	 * Called when we are done with the virtualizer and wish to
	 * cleanup any resources it has.
	 */
	public void cleanup()
	{
		// Remove all paged-out swap files.
		for (Iterator it = pagedOut.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			try {
				fileDispose((JRVirtualizable) entry.getValue());
				it.remove();
			} catch (Exception e) {
				log.error("Error cleaning up virtualizer.", e);
				// Do nothing because we want to try to remove all swap files.
			}
		}

		for (Iterator it = pagedIn.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			try {
				fileDispose((JRVirtualizable) entry.getValue());
				it.remove();
			} catch (Exception e) {
				log.error("Error cleaning up virtualizer.", e);
				// Do nothing because we want to try to remove all swap files.
			}
		}
	}
}
