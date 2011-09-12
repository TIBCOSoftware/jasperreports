/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.fill;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.util.JRSwapFile;


/**
 * A virtualizer that uses a single swap file to serialize virtual data.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRSwapFileVirtualizer extends JRAbstractLRUVirtualizer
{
	private final JRSwapFile swap;
	private final boolean swapOwner;
	private final Map<String,JRSwapFile.SwapHandle> handles;
	
	
	/**
	 * Creates a virtualizer that uses a swap file.
	 * <p>
	 * The virtualizer will be considered the owner of the swap file.
	 * 
	 * @param maxSize the maximum size (in JRVirtualizable objects) of the paged in cache.
	 * @param swap the swap file to use for data virtualization
	 */
	public JRSwapFileVirtualizer(int maxSize, JRSwapFile swap)
	{
		this(maxSize, swap, true);
	}

	
	/**
	 * Creates a virtualizer that uses a swap file.
	 * 
	 * @param maxSize the maximum size (in JRVirtualizable objects) of the paged in cache.
	 * @param swap the swap file to use for data virtualization
	 * @param swapOwner whether the virtualizer is the owner (single user) of the swap file.
	 * If <code>true</code>, the virtualizer will dispose the swap file on
	 * {@link #cleanup() cleanup}.
	 */
	public JRSwapFileVirtualizer(int maxSize, JRSwapFile swap, boolean swapOwner)
	{
		super(maxSize);

		this.swap = swap;
		this.swapOwner = swapOwner;
		handles = Collections.synchronizedMap(new HashMap<String,JRSwapFile.SwapHandle>());
	}

	protected void pageOut(JRVirtualizable o) throws IOException
	{
		if (!handles.containsKey(o.getUID()))
		{
			ByteArrayOutputStream bout = new ByteArrayOutputStream(3000);
			writeData(o, bout);
			byte[] data = bout.toByteArray();
			
			JRSwapFile.SwapHandle handle = swap.write(data);
			handles.put(o.getUID(), handle);
		}
		else
		{
			if (!isReadOnly(o))
			{
				throw new IllegalStateException("Cannot virtualize data because the data for object UID \"" + o.getUID() + "\" already exists.");
			}
		}
	}

	protected void pageIn(JRVirtualizable o) throws IOException
	{
		JRSwapFile.SwapHandle handle = handles.get(o.getUID());
		byte[] data = swap.read(handle, !isReadOnly(o));

		readData(o, new ByteArrayInputStream(data));
		
		if (!isReadOnly(o))
		{
			handles.remove(o.getUID());
		}
	}

	protected void dispose(String id)
	{
		JRSwapFile.SwapHandle handle = handles.remove(id);
		if (handle != null)
		{
			swap.free(handle);
		}
	}

	
	/**
	 * Disposes the swap file used if this virtualizer owns it.
	 * @see #JRSwapFileVirtualizer(int, JRSwapFile, boolean)
	 */
	public void cleanup()
	{
		handles.clear();
		if (swapOwner)
		{
			swap.dispose();
		}
	}
}
