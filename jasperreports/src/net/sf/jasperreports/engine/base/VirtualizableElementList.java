/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.fill.JRVirtualizationContext;
import net.sf.jasperreports.engine.fill.VirtualizationObjectInputStream;
import net.sf.jasperreports.engine.fill.VirtualizationObjectOutputStream;
import net.sf.jasperreports.engine.util.DeepPrintElementCounter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * List implementation that can virtualize blocks of elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class VirtualizableElementList extends AbstractList<JRPrintElement> implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(VirtualizableElementList.class);

	private final JRVirtualizationContext virtualizationContext;

	private ElementStore store;
	
	public VirtualizableElementList(JRVirtualizationContext virtualizationContext, JRVirtualPrintPage page)
	{
		this.virtualizationContext = virtualizationContext;
		
		initStore(page);
	}

	private void initStore(JRVirtualPrintPage page)
	{
		// start with a simple block to reduce memory consumption for small pages
		this.store = new ElementsBlock(virtualizationContext, page);
		
		if (log.isDebugEnabled())
		{
			log.debug("created block " + this.store + " for " + this);
		}
	}

	public synchronized void set(List<JRPrintElement> elements)
	{
		clear();
		addAll(elements);
	}

	private void cacheInContext(JRPrintElement element)
	{
		virtualizationContext.cacheTemplate(element);
	}

	@Override
	public synchronized JRPrintElement get(int index)
	{
		return store.get(index);
	}

	@Override
	public synchronized int size()
	{
		return store.size();
	}

	@Override
	public synchronized JRPrintElement set(int index, JRPrintElement element)
	{
		cacheInContext(element);
		return store.set(index, element);
	}

	private void createBlockList()
	{
		ElementsBlockList blockList = new ElementsBlockList((ElementsBlock) store);
		blockList.addBlock();
		
		store = blockList;
	}

	@Override
	public synchronized boolean add(JRPrintElement element)
	{
		cacheInContext(element);

		if (!store.add(element))
		{
			// we had a single block and it overflowed.
			// create a block list.
			createBlockList();
			
			// add again, block list never overflows.
			store.add(element);
		}
		return true;
	}

	@Override
	public synchronized void add(int index, JRPrintElement element)
	{
		cacheInContext(element);

		if (!store.add(index, element))
		{
			// we had a single block and it overflowed.
			// create a block list.
			createBlockList();
			
			// add again, block list never overflows.
			store.add(index, element);
		}
	}

	@Override
	public synchronized JRPrintElement remove(int index)
	{
		return store.remove(index);
	}

	@Override
	public synchronized void clear()
	{
		// recreating the store
		JRVirtualPrintPage page = store.getPage();
		store.dispose();
		initStore(page);
	}

	public synchronized void dispose()
	{
		store.dispose();
	}

	public JRVirtualizationContext getVirtualizationContext()
	{
		return virtualizationContext;
	}
	
	//FIXME implement faster bulk methods such as addAll
}

interface ElementStore extends VirtualizablePageElements
{
	int size();

	JRPrintElement get(int index);

	boolean add(JRPrintElement element);

	boolean add(int index, JRPrintElement element);

	JRPrintElement set(int index, JRPrintElement element);

	JRPrintElement remove(int index);
	
	void dispose();
}

class ElementsBlock implements JRVirtualizable<VirtualElementsData>, ElementStore, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(ElementsBlock.class);

	private static final AtomicInteger uidCounter = new AtomicInteger();
	private static final Random uidRandom = new Random();
	
	private JRVirtualizationContext context;
	
	/**
	 * A unique identifier that is useful for serialization and deserialization
	 * to some persistence mechanism.
	 */
	private String uid;
	
	private List<JRPrintElement> elements;
	private transient volatile int size;
	private transient VirtualElementsData virtualData;
	private transient int deepElementCount;
	
	// only kept during the fill and does not get restored on deserialization
	private transient JRVirtualPrintPage page;
	
	public ElementsBlock(JRVirtualizationContext context, JRVirtualPrintPage page)
	{
		this.context = context;
		this.page = page;
		this.uid = makeUID();
		
		if (log.isDebugEnabled())
		{
			log.debug("generated uid " + uid + " for " + this);
		}
		
		this.elements = new ArrayList<JRPrintElement>();
		this.size = 0;
		this.deepElementCount = 0;
	}

	private void lockContext()
	{
		//FIXME locking the whole context is too much, ideally we'd have a lock for this object only
		context.lock();
	}

	private void unlockContext()
	{
		context.unlock();
	}

	private void register()
	{
		if (context.getVirtualizer() != null)
		{
			context.getVirtualizer().registerObject(this);
		}
	}
	
	/**
	 * Make a new identifier for an object.
	 * 
	 * @return the new identifier
	 */
	private String makeUID()
	{
		//FIXME use UUID?
		return 
				System.identityHashCode(context) 
				+ "_" + System.identityHashCode(this)
				+ "_" + uidCounter.incrementAndGet()
				+ "_" + uidRandom.nextInt();
	}

	public int size()
	{
		return size;
	}

	public boolean isEmpty()
	{
		return size == 0;
	}

	public JRPrintElement get(int index)
	{
		lockContext();
		try
		{
			ensureDataAndTouch();
			return elements.get(index);
		}
		finally
		{
			unlockContext();
		}
	}

	protected boolean preAdd(JRPrintElement element, boolean force)
	{
		boolean empty = elements != null && elements.isEmpty();
		if (empty)
		{
			// if there are no elements, the object has not yet been registered with the virtualizer
			register();
		}
		else
		{
			ensureDataAndTouch();
		}
		
		// check whether we passed the element count threshold.
		// if the list is empty, allow at least one element to be added
		// no matter the element count.
		int elementCount = DeepPrintElementCounter.count(element);
		if (!force && !elements.isEmpty())
		{
			int pageSize = context.getPageElementSize();
			if (pageSize > 0 && deepElementCount + elementCount > pageSize)
			{
				if (log.isDebugEnabled())
				{
					log.debug("overflow of block with size " + deepElementCount 
							+ ", adding " + elementCount);
				}
				
				return false;
			}				
		}
		
		deepElementCount += elementCount;
		return true;
	}

	public boolean add(JRPrintElement element, boolean force)
	{
		lockContext();
		try
		{
			boolean adding = preAdd(element, force);
			if (adding)
			{
				elements.add(element);
				++size;
			}
			return adding;
		}
		finally
		{
			unlockContext();
		}
	}
	
	public boolean add(JRPrintElement element)
	{
		return add(element, false);
	}

	public boolean add(int index, JRPrintElement element, boolean force)
	{
		lockContext();
		try
		{
			boolean adding = preAdd(element, force);
			if (adding)
			{
				elements.add(index, element);
				++size;
			}
			return adding;
		}
		finally
		{
			unlockContext();
		}
	}
	
	public boolean add(int index, JRPrintElement element)
	{
		return add(index, element, false);
	}

	public JRPrintElement set(int index, JRPrintElement element)
	{
		lockContext();
		try
		{
			ensureDataAndTouch();
			
			JRPrintElement oldElement = elements.get(index);
			deepElementCount -= DeepPrintElementCounter.count(oldElement);
			deepElementCount += DeepPrintElementCounter.count(element);
			
			return elements.set(index, element);
		}
		finally
		{
			unlockContext();
		}
	}

	public JRPrintElement remove(int index)
	{
		lockContext();
		try
		{
			ensureDataAndTouch();
			
			JRPrintElement element = elements.remove(index);
			--size;
			
			// decrement the deep count
			deepElementCount -= DeepPrintElementCounter.count(element);

			if (elements.isEmpty())
			{
				// if the list is empty now, deregister with the virtualizer.
				// this helps with subreports by immediately releasing the external storage.
				deregister();
			}

			return element;
		}
		finally
		{
			unlockContext();
		}
	}
	
	public String getUID()
	{
		return uid;
	}

	private void ensureDataAndTouch()
	{
		if (elements == null)
		{
			ensureData();
		}
		else
		{
			if (context.getVirtualizer() != null)
			{
				context.getVirtualizer().touch(this);
			}
		}
	}
	
	public void ensureVirtualData()
	{
		lockContext();
		try
		{
			if (elements == null)
			{
				ensureData();
			}
		}
		finally
		{
			unlockContext();
		}
	}

	private void ensureData()
	{
		if (context.getVirtualizer() != null)
		{
			context.getVirtualizer().requestData(this);
		}
	}

	public void setVirtualData(VirtualElementsData virtualData)
	{
		lockContext();
		try
		{
			this.virtualData = virtualData;
			this.elements = virtualData.getElements();
			//FIXME recheck size?
		}
		finally
		{
			unlockContext();
		}
	}

	public VirtualElementsData getVirtualData()
	{
		return virtualData;
	}

	public void removeVirtualData()
	{
		lockContext();
		try
		{
			virtualData = null;
			elements = null;
		}
		finally
		{
			unlockContext();
		}
	}

	public void beforeExternalization()
	{
		lockContext();
		try
		{
			virtualData = new VirtualElementsData(elements);
			context.beforeExternalization(this);
		}
		finally
		{
			unlockContext();
		}
	}

	public void afterExternalization()
	{
		lockContext();
		try
		{
			context.afterExternalization(this);
		}
		finally
		{
			unlockContext();
		}
	}

	public void afterInternalization()
	{
		lockContext();
		try
		{
			context.afterInternalization(this);
			virtualData = null;
		}
		finally
		{
			unlockContext();
		}
	}

	public JRVirtualizationContext getContext()
	{
		return context;
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		// we do not use the original ID as the source object might still be
		// alive in the JVM
		String oldUid = (String) in.readObject();
		uid = makeUID();
		if (log.isDebugEnabled())
		{
			log.debug("Original uid " + oldUid + "; new uid " + uid);
		}
		
		context = (JRVirtualizationContext) in.readObject();
		
		int length = in.readInt();
		//FIXME put a limit on the buffer
		byte[] buffer = new byte[length];
		in.readFully(buffer);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer, 0, buffer.length);
		VirtualizationObjectInputStream elementsStream = new VirtualizationObjectInputStream(inputStream, context);
		elements = (List<JRPrintElement>) elementsStream.readObject();
		size = elements.size();
		
		if (!elements.isEmpty())
		{
			register();
		}
	}

	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		lockContext();
		try
		{
			ensureDataAndTouch();
			beforeExternalization();
			
			try
			{
				// maybe we should no longer serialize the id, as a new one is
				// generated on deserialization
				out.writeObject(uid);
				out.writeObject(context);

				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				VirtualizationObjectOutputStream stream = new VirtualizationObjectOutputStream(bout, context);
				stream.writeObject(elements);
				stream.flush();

				byte[] bytes = bout.toByteArray();
				out.writeInt(bytes.length);
				out.write(bytes);
			}
			finally
			{
				afterExternalization();
			}
		}
		finally
		{
			unlockContext();
		}
	}

	// not implementing finalize because it can slow down GC to the point it can no longer handle the rate of newly created objects.
	// we're relying on the virtualizer to keep track of garbage collected objects via weak references.
/*	protected void finalize() throws Throwable //NOSONAR
	{
		dispose();
		super.finalize();
	}
*/
	public void dispose()
	{
		lockContext();
		try
		{
			// if empty, the object is not registered
			if (elements == null || !elements.isEmpty())
			{
				deregister();
			}
			// removeVirtualData?  letting GC do his thing for now.
		}
		finally
		{
			unlockContext();
		}
	}

	private void deregister()
	{
		if (context.getVirtualizer() != null)
		{
			context.getVirtualizer().deregisterObject(this);
		}
	}
	
	public JRVirtualPrintPage getPage()
	{
		return page;
	}
}

class ElementsBlockList implements ElementStore, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(ElementsBlockList.class);
	
	private static final int INITIAL_SIZE = 3;
	private static final int MAX_INCREMENT = 200;
	
	private int blockCount;
	private int[] offsets;
	private ElementsBlock[] blocks;
	private int size;
	
	private transient int lastIndex = -1;
	
	public ElementsBlockList(ElementsBlock block)
	{
		blocks = new ElementsBlock[INITIAL_SIZE];
		offsets = new int[INITIAL_SIZE];
		
		blockCount = 1;
		blocks[0] = block;
		offsets[0] = 0;
		size = block.size();
	}

	protected void addBlock()
	{
		incrementBlocks();
		
		ElementsBlock block = new ElementsBlock(blocks[0].getContext(), blocks[0].getPage());
		blocks[blockCount] = block;
		offsets[blockCount] = size;
		++blockCount;
		
		if (log.isDebugEnabled())
		{
			log.debug("created block " + block + " at offset " + size);
		}
	}

	protected void incrementBlocks()
	{
		if (blockCount == blocks.length)
		{
			int newSize = Math.min((blockCount * 3) / 2 + 1, blockCount + MAX_INCREMENT);
			
			ElementsBlock[] newBlocks = new ElementsBlock[newSize];
			System.arraycopy(blocks, 0, newBlocks, 0, blockCount);
			blocks = newBlocks;
			
			int[] newOffsets = new int[newSize];
			System.arraycopy(offsets, 0, newOffsets, 0, blockCount);
			offsets = newOffsets;
		}
	}

	public int size()
	{
		return size;
	}

	public JRPrintElement get(int index)
	{
		int blockIndex = blockIndex(index);
		return blocks[blockIndex].get(index - offsets[blockIndex]);
	}

	public boolean add(JRPrintElement element)
	{
		// allow the last block to overflow.
		if (!blocks[blockCount - 1].add(element, false))
		{
			// the last block overflowed, create a new one
			addBlock();
			
			// add the element to the new block
			blocks[blockCount - 1].add(element, true);
		}

		++size;
		return true;
	}

	public boolean add(int index, JRPrintElement element)
	{
		int blockIndex = blockIndex(index);
		
		// only allow the last block to overflow.
		// we're not moving elements across blocks when adding an element 
		// in the middle of a page.
		// this can lead to bigger blocks, but the case is exotic.
		boolean last = blockIndex == blockCount - 1;
		if (last)
		{
			if (!blocks[blockIndex].add(index - offsets[blockIndex], element, false))
			{
				// the last block overflowed, create a new one
				addBlock();
				
				// add the element to the new block
				blockIndex = blockCount - 1;
				blocks[blockIndex].add(index - offsets[blockIndex], element, true);
			}
		}
		else
		{
			blocks[blockIndex].add(index - offsets[blockIndex], element, true);
			
			// increment offsets of subsequent blocks
			for (int idx = blockIndex + 1; idx < blockCount; ++idx)
			{
				++offsets[blockIndex];
			}
		}

		++size;
		return true;
	}

	public JRPrintElement set(int index, JRPrintElement element)
	{
		int blockIndex = blockIndex(index);
		return blocks[blockIndex].set(index - offsets[blockIndex], element);
	}

	public JRPrintElement remove(int index)
	{
		// FIXME removing elements from an existing block without moving elements
		// across blocks.  this can lead to small blocks if many elements are
		// removed from a page, such as when we have a group with keep together.
		
		int blockIndex = blockIndex(index);
		ElementsBlock block = blocks[blockIndex];
		JRPrintElement element = block.remove(index - offsets[blockIndex]);
		
		// decrement offsets of subsequent blocks
		if (blockIndex + 1 < blockCount)
		{
			for (int idx = blockIndex + 1; idx < blockCount; ++idx)
			{
				--offsets[idx];
			}
		}
		
		// if the block was left empty and this is not the only block, get rid of it
		if (blockCount > 1 && block.isEmpty())
		{
			// shift the remaining blocks
			for (int idx = blockIndex + 1; idx < blockCount; ++idx)
			{
				blocks[idx - 1] = blocks[idx];
				offsets[idx - 1] = offsets[idx];
			}
			--blockCount;
			blocks[blockCount] = null;
			offsets[blockCount] = 0;
		}
		
		--size;
		return element;
	}

	public void dispose()
	{
		for (int idx = 0; idx < blockCount; ++idx)
		{
			blocks[idx].dispose();
		}
	}
	
	protected int blockIndex(int index)
	{
		if (index < 0)
		{
			throw new IndexOutOfBoundsException("index: " + index);
		}

		// see if the index falls in the lastIndex block
		if (lastIndex >= 0 && lastIndex < blockCount)
		{
			if (index >= offsets[lastIndex]
					&& (lastIndex + 1 == blockCount
					|| index < offsets[lastIndex + 1]))
			{
				return lastIndex;
			}
		}
		
		int blockIndex = Arrays.binarySearch(offsets, 0, blockCount, index);
		if (blockIndex < 0)
		{
			blockIndex = -blockIndex - 2;
		}
		// caching last index for fast serial access
		lastIndex = blockIndex;
		return blockIndex;
	}

	public JRVirtualPrintPage getPage()
	{
		return blocks[0].getPage();
	}
}
