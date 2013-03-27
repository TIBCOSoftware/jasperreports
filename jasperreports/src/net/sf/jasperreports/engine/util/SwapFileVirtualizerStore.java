package net.sf.jasperreports.engine.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.fill.VirtualizerStore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SwapFileVirtualizerStore implements VirtualizerStore
{
	private static final Log log = LogFactory.getLog(SwapFileVirtualizerStore.class);
	
	private final JRSwapFile swap;
	private final boolean swapOwner;
	private final Map<String,JRSwapFile.SwapHandle> handles;

	public SwapFileVirtualizerStore(JRSwapFile swap, boolean swapOwner)
	{
		this.swap = swap;
		this.swapOwner = swapOwner;
		this.handles = Collections.synchronizedMap(new HashMap<String,JRSwapFile.SwapHandle>());
	}
	
	@Override
	public String toString()
	{
		return "SwapFileVirtualizerStore " + swap.toString(); 
	}
	
	protected boolean isStored(JRVirtualizable<?> o)
	{
		return handles.containsKey(o.getUID());
	}
	
	@Override
	public boolean store(JRVirtualizable<?> o, VirtualizationSerializer serializer)
	{
		if (isStored(o))
		{
			if (log.isTraceEnabled())
			{
				log.trace("object " + o.getUID() + " already stored");
			}
			return false;
		}
		
		try
		{
			ByteArrayOutputStream bout = new ByteArrayOutputStream(3000);
			serializer.writeData(o, bout);
			byte[] data = bout.toByteArray();
			
			if (log.isTraceEnabled())
			{
				log.trace("writing " + data.length + " for object " + o.getUID() + " to " + swap);
			}
			
			JRSwapFile.SwapHandle handle = swap.write(data);
			handles.put(o.getUID(), handle);
			return true;
		}
		catch (IOException e)
		{
			log.error("Error virtualizing object " + o.getUID() + " to " + swap, e);
			throw new JRRuntimeException("Error virtualizing object", e);
		}
	}
	
	@Override
	public void retrieve(JRVirtualizable<?> o, boolean remove, VirtualizationSerializer serializer)
	{
		JRSwapFile.SwapHandle handle = handles.get(o.getUID());
		if (handle == null)
		{
			// should not happen
			log.error("No swap handle found for " + o.getUID() + " in " + this);
			throw new JRRuntimeException("Unable to read virtualized data");
		}
		
		try
		{
			byte[] data = swap.read(handle, remove);
			if (log.isTraceEnabled())
			{
				log.trace("read " + data.length + " for object " + o.getUID() + " from " + swap);
			}
			
			serializer.readData(o, new ByteArrayInputStream(data));
		}
		catch (IOException e)
		{
			log.error("Error reading object data " + o.getUID() + " from " + swap, e);
			throw new JRRuntimeException("Error devirtualizing object", e);
		}
		
		if (remove)
		{
			handles.remove(o.getUID());
		}
	}
	
	@Override
	public void remove(String objectId)
	{
		JRSwapFile.SwapHandle handle = handles.remove(objectId);
		if (handle == null)
		{
			if (log.isTraceEnabled())
			{
				log.trace("object " + objectId + " not found for removal");
			}
		}
		else
		{
			if (log.isTraceEnabled())
			{
				log.trace("removing object " + objectId + " from " + swap);
			}
			
			swap.free(handle);
		}
	}


	/**
	 * Disposes the swap file used if this virtualizer owns it.
	 * @see #SwapFileVirtualizerStore(JRSwapFile, boolean)
	 */
	@Override
	public void dispose()
	{
		handles.clear();
		if (swapOwner)
		{
			if (log.isDebugEnabled())
			{
				log.debug("disposing " + swap);
			}
			
			swap.dispose();
		}
	}
}
