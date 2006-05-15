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
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizer;

import org.apache.commons.collections.LRUMap;
import org.apache.commons.collections.ReferenceMap;
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

	protected static class CacheReference extends WeakReference
	{
		private final String id;

		public CacheReference(JRVirtualizable o, ReferenceQueue queue)
		{
			super(o, queue);
			id = o.getUID();
		}
		
		public String getId()
		{
			return id;
		}
	}
	
	/**
	 * This class keeps track of how many objects are currently in memory, and
	 * when there are too many, it pushes the last touched one to disk.
	 */
	protected class Cache
	{		
		protected class LRUScanMap extends LRUMap
		{
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
			
		    public LRUScanMap(int maxSize)
		    {
		    	super(maxSize);
			}

			protected void removeLRU()
		    {
		        Map.Entry entry = getFirst();
		        boolean found = isRemovable(entry);
		        if (!found)
		        {
		        	Iterator entriesIt = entrySet().iterator();
		        	entriesIt.next(); //skipping the first, which is already checked
		        	while(!found && entriesIt.hasNext())
		        	{
		        		entry = (Entry) entriesIt.next();
		        		found = isRemovable(entry);
		        	}
		        }

		        if (!found)
		        {
		        	throw new JRRuntimeException("The virtualizer is used by more contexts than its in-memory cache size " + getMaximumSize());
		        }

		        Object key = entry.getKey();
		        Object value = entry.getValue();
		        remove(key);
		        processRemovedLRU(key,value);
		    }

			protected boolean isRemovable(Map.Entry entry)
			{
		        JRVirtualizable value = getMapValue(entry.getValue());
		        return value == null || !lastObjectSet.containsKey(value);
			}

			protected void processRemovedLRU(Object key, Object value)
			{
				JRVirtualizable o = getMapValue(value);
				if (o != null)
				{
					virtualizeData(o);
				}
			}
		}
		
		private final ReferenceQueue refQueue;
		private final LRUScanMap map;

		Cache(int maxSize)
		{
			map = new LRUScanMap(maxSize);
			refQueue = new ReferenceQueue();
		}
		
		protected JRVirtualizable getMapValue(Object val)
		{
			JRVirtualizable o;
			if (val == null)
			{
				o = null;
			}
			else
			{
				Reference ref = (Reference) val;
				if (ref.isEnqueued())
				{
					o = null;
				}
				else
				{
					o = (JRVirtualizable) ref.get();
				}
			}
			return o;
		}
		
		protected Object toMapValue(JRVirtualizable val)
		{
			return val == null ? null : new CacheReference(val, refQueue);
		}
		
		protected void purge()
		{
			CacheReference ref;
			while ((ref = (CacheReference) refQueue.poll()) != null)
			{
				map.remove(ref.getId());
			}
		}

		public JRVirtualizable get(String id)
		{
			purge();
			
			return getMapValue(map.get(id));
		}

		public JRVirtualizable put(String id, JRVirtualizable o)
		{
			purge();
			
			return getMapValue(map.put(id, toMapValue(o)));
		}

		public JRVirtualizable remove(String id)
		{
			purge();
			
			return getMapValue(map.remove(id));
		}
		
		public Iterator idIterator()
		{
			purge();
			
			final Iterator valsIt = map.values().iterator();
			return new Iterator()
			{
				public boolean hasNext()
				{
					return valsIt.hasNext();
				}

				public Object next()
				{
					CacheReference ref = (CacheReference) valsIt.next();
					return ref.getId();
				}

				public void remove()
				{
					valsIt.remove();
				}		
			};
		}
	}

	private final Cache pagedIn;

	private final ReferenceMap pagedOut;

	protected JRVirtualizable lastObject;
	protected ReferenceMap lastObjectMap;
	protected ReferenceMap lastObjectSet;

	private boolean readOnly;

	/**
	 * @param maxSize
	 *            the maximum size (in JRVirtualizable objects) of the paged in
	 *            cache.
	 */
	protected JRAbstractLRUVirtualizer(int maxSize)
	{
		this.pagedIn = new Cache(maxSize);
		this.pagedOut = new ReferenceMap(ReferenceMap.HARD, ReferenceMap.WEAK);
		this.lastObject = null;
		
		this.lastObjectMap = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);
		this.lastObjectSet = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.HARD);
	}

	protected synchronized final boolean isPagedOut(String id)
	{
		return pagedOut.containsKey(id);
	}

	protected synchronized boolean isPagedOutAndTouch(JRVirtualizable o, String uid)
	{
		boolean virtualized = isPagedOut(uid);
		if (!virtualized)
		{
			touch(o);
		}
		return virtualized;
	}
	
	protected final void setLastObject(JRVirtualizable o)
	{
		if (lastObject != o)
		{
			if (o != null)
			{
				JRVirtualizationContext context = o.getContext();
				Object ownerLast = lastObjectMap.get(context);
				if (ownerLast != o)
				{
					if (ownerLast != null)
					{
						lastObjectSet.remove(ownerLast);
					}
					lastObjectMap.put(context, o);
					lastObjectSet.put(o, Boolean.TRUE);
				}
			}
			this.lastObject = o;
		}
	}

	/**
	 * Sets the read only mode for the virtualizer.
	 * <p/>
	 * When in read-only mode, the virtualizer assumes that virtualizable objects are final
	 * and any change in a virtualizable object's data is discarded.
	 * <p/>
	 * When the virtualizer is used for multiple virtualization contexts (in shared mode),
	 * calling this method would override the read-only flags from all the contexts and all the 
	 * objects will be manipulated in read-only mode.
	 * Use {@link JRVirtualizationContext#setReadOnly(boolean) JRVirtualizationContext.setReadOnly(boolean)}
	 * to set the read-only mode for one specific context.
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
	 * @see #setReadOnly(boolean)
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}

	protected final boolean isReadOnly(JRVirtualizable o)
	{
		return readOnly || o.getContext().isReadOnly();
	}

	public synchronized void registerObject(JRVirtualizable o)
	{
		setLastObject(o);
		JRVirtualizable old = pagedIn.put(o.getUID(), o);
		if (old != null)
		{
			pagedIn.put(o.getUID(), old);
			throw new IllegalStateException("Wrong object stored with UID \"" + o.getUID() + "\"");
		}
	}

	public void deregisterObject(JRVirtualizable o)
	{
		String uid = o.getUID();

		//try to remove virtual data
		try
		{
			dispose(o.getUID());
		}
		catch (Exception e)
		{
			log.error("Error removing virtual data", e);
			//ignore
		}

		synchronized(this)
		{			
			JRVirtualizable oldIn = pagedIn.remove(uid);
			if (oldIn != null)
			{
				if (oldIn != o)
				{
					pagedIn.put(uid, oldIn);
					throw new IllegalStateException("Wrong object stored with UID \"" + o.getUID() + "\"");
				}
			}
			else
			{
				Object oldOut = pagedOut.remove(uid);
				if (oldOut != null && oldOut != o)
				{
					pagedOut.put(uid, oldOut);
					throw new IllegalStateException("Wrong object stored with UID \"" + o.getUID() + "\"");
				}
			}

			// We don't really care if someone deregisters an object
			// that's not registered.
		}
	}

	public synchronized void touch(JRVirtualizable o)
	{
		// If we just touched this object, don't touch it again.
		if (this.lastObject != o)
		{
			setLastObject(pagedIn.get(o.getUID()));
		}
	}
	
	public void requestData(JRVirtualizable o)
	{
		String uid = o.getUID();
		if (isPagedOutAndTouch(o, uid))
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

			synchronized (this)
			{
				setLastObject(o);
				pagedOut.remove(uid);
				pagedIn.put(uid, o);
			}
		}
	}

	public void clearData(JRVirtualizable o)
	{
		String uid = o.getUID();
		if (isPagedOutAndTouch(o, uid))
		{
			// remove virtual data
			dispose(uid);
			
			synchronized (this)
			{
				pagedOut.remove(uid);
			}
		}
	}

	public void virtualizeData(JRVirtualizable o)
	{
		String uid = o.getUID();
		if (!isPagedOut(uid))
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

			synchronized (this)
			{
				pagedOut.put(uid, o);
			}
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

	protected synchronized void reset()
	{
		readOnly = false;
	}

	protected final void disposeAll()
	{
		// Remove all paged-out swap files.
		for (Iterator it = pagedOut.keySet().iterator(); it.hasNext();)
		{
			String id = (String) it.next();
			try
			{
				dispose(id);
				it.remove();
			}
			catch (Exception e)
			{
				log.error("Error cleaning up virtualizer.", e);
				// Do nothing because we want to try to remove all swap files.
			}
		}

		for (Iterator it = pagedIn.idIterator(); it.hasNext();)
		{
			String id = (String) it.next();
			try
			{
				dispose(id);
				it.remove();
			}
			catch (Exception e)
			{
				log.error("Error cleaning up virtualizer.", e);
				// Do nothing because we want to try to remove all swap files.
			}
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
	 * @param virtualId the ID of the virtualizable object
	 */
	protected abstract void dispose(String virtualId);
}
