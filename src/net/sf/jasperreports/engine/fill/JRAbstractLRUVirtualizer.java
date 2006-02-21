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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizer;

import org.apache.commons.collections.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Abstract base for LRU and serialization based virtualizer
 * 
 * @author John Bindel
 * @version $Id$
 */
public abstract class JRAbstractLRUVirtualizer implements JRVirtualizer
{
	private static final Log log = LogFactory.getLog(JRAbstractLRUVirtualizer.class);

	/**
	 * This class keeps track of how many objects are currently in memory, and
	 * when there are too many, it pushes the last touched one to disk.
	 */
	protected static class Cache extends LRUMap
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		private final JRVirtualizer virt;

		Cache(int maxSize, JRVirtualizer virt)
		{
			super(maxSize);

			this.virt = virt;
		}

		protected void processRemovedLRU(Object key, Object value)
		{
			virt.virtualizeData((JRVirtualizable) value);
		}
	}

	protected final Cache pagedIn;

	protected final HashMap pagedOut;

	protected JRVirtualizable lastObject;

	protected boolean readOnly;

	/**
	 * @param maxSize
	 *            the maximum size (in JRVirtualizable objects) of the paged in
	 *            cache.
	 */
	protected JRAbstractLRUVirtualizer(int maxSize)
	{
		this.pagedIn = new Cache(maxSize, this);
		this.pagedOut = new HashMap();
		this.lastObject = null;
		this.readOnly = false;
	}

	
	/**
	 * Sets the read only mode for the virtualizer.
	 * <p/>
	 * When in read-only mode, the virtualizer assumes that virtualizable objects are final
	 * and any change in a virtualizable object's data is discarded.
	 * 
	 * @param ro the read-only mode to set
	 */
	public void setReadOnly(boolean ro)
	{
		this.readOnly = ro;
	}

	
	/**
	 * Determines whether the virtualizer is in read-only mode.
	 * 
	 * @return whether the virtualizer is in read-only mode
	 */
	public boolean isReadOnly()
	{
		return this.readOnly;
	}

	public void registerObject(JRVirtualizable o)
	{
		Object old = pagedIn.put(o.getUID(), o);
		if (old != null)
		{
			pagedIn.put(o.getUID(), old);
			throw new IllegalStateException("Wrong object stored with UID \"" + o.getUID() + "\"");
		}
		this.lastObject = o;
	}

	public void deregisterObject(JRVirtualizable o)
	{
		String uid = o.getUID();
		Object old = pagedIn.remove(uid);
		if (old != null)
		{
			if (old != o)
			{
				pagedIn.put(uid, old);
				throw new IllegalStateException("Wrong object stored with UID \"" + o.getUID() + "\"");
			}
		}
		else
		{
			old = pagedOut.remove(uid);
			if (old != null && old != o)
			{
				pagedOut.put(uid, old);
				throw new IllegalStateException("Wrong object stored with UID \"" + o.getUID() + "\"");
			}
		}

		// We don't really care if someone deregisters an object
		// that's not registered.
	}

	public void touch(JRVirtualizable o)
	{
		// If we just touched this object, don't touch it again.
		if (this.lastObject != o)
		{
			this.lastObject = (JRVirtualizable) pagedIn.get(o.getUID());
		}
	}

	public void requestData(JRVirtualizable o)
	{
		String uid = o.getUID();
		if (pagedOut.containsKey(uid))
		{
			// unvirtualize
			try
			{
				pageIn(o);
			}
			catch (IOException e)
			{
				log.error("Error devirtualizing object", e);
				throw new JRRuntimeException(e);
			}

			o.afterInternalization();

			pagedOut.remove(uid);
			pagedIn.put(uid, o);
			this.lastObject = o;
		}
	}

	public void clearData(JRVirtualizable o)
	{
		String uid = o.getUID();
		if (pagedOut.containsKey(uid))
		{
			// remove virtual data
			dispose(o);
			pagedOut.remove(uid);
		}
	}

	public void virtualizeData(JRVirtualizable o)
	{
		String uid = o.getUID();
		if (!pagedOut.containsKey(uid))
		{
			o.beforeExternalization();

			// virtualize
			try
			{
				pageOut(o);
			}
			catch (IOException e)
			{
				log.error("Error virtualizing object", e);
				throw new JRRuntimeException(e);
			}

			// Wait until we know it worked before tossing the data.
			o.removeVirtualData();

			pagedOut.put(uid, o);
		}
	}

	protected void finalize() throws Throwable
	{
		cleanup();

		super.finalize();
	}

	/**
	 * Writes serialized indentity and virtual data of a virtualizable object to a stream.
	 * 
	 * @param o the serialized object
	 * @param out the output stream
	 * @throws JRRuntimeException
	 */
	protected final void writeData(JRVirtualizable o, OutputStream out)
			throws JRRuntimeException
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(o.getIdentityData());
			oos.writeObject(o.getVirtualData());
			oos.flush();
		}
		catch (IOException e)
		{
			log.error("Error virtualizing object", e);
			throw new JRRuntimeException(e);
		}
	}

	
	/**
	 * Reads serialized identity and virtual data for a virtualizable object
	 * from a stream.
	 * 
	 * @param o the virtualizable object
	 * @param in the input stream
	 * @throws JRRuntimeException
	 */
	protected final void readData(JRVirtualizable o, InputStream in)
			throws JRRuntimeException
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(in);
			o.setIdentityData(ois.readObject());
			o.setVirtualData(ois.readObject());
		}
		catch (IOException e)
		{
			log.error("Error devirtualizing object", e);
			throw new JRRuntimeException(e);
		}
		catch (ClassNotFoundException e)
		{
			log.error("Error devirtualizing object", e);
			throw new JRRuntimeException(e);
		}
	}


	/**
	 * Writes a virtualizable object's data to an external storage.
	 * 
	 * @param o a virtualizable object
	 * @throws IOException
	 */
	protected abstract void pageOut(JRVirtualizable o) throws IOException;
	

	/**
	 * Reads a virtualizable object's data from an external storage.
	 * 
	 * @param o a virtualizable object
	 * @throws IOException
	 */
	protected abstract void pageIn(JRVirtualizable o) throws IOException;
	
	
	/**
	 * Removes the external data associated with a virtualizable object.
	 * 
	 * @param o the virtualizable object
	 */
	protected abstract void dispose(JRVirtualizable o);
}
