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
package net.sf.jasperreports.engine.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import net.sf.jasperreports.engine.util.DeepPrintElementCounter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * List implementation that can virtualize blocks of elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class VirtualizableElementList extends AbstractList<JRPrintElement> implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(VirtualizableElementList.class);

	private final JRVirtualizationContext virtualizationContext;

	private ElementStore store;
	
	public VirtualizableElementList(JRVirtualizationContext virtualizationContext)
	{
		this.virtualizationContext = virtualizationContext;
		
		initStore();
	}

	private void initStore()
	{
		// start with a simple block to reduce memory consumption for small pages
		this.store = new ElementsBlock(virtualizationContext);
		
		if (log.isDebugEnabled())
		{
			log.debug("created block " + this.store + " for " + this);
		}
	}

	public void set(List<JRPrintElement> elements)
	{
		clear();
		addAll(elements);
	}

	private void cacheInContext(JRPrintElement element)
	{
		virtualizationContext.cacheTemplate(element);
	}

	@Override
	public JRPrintElement get(int index)
	{
		return store.get(index);
	}

	@Override
	public int size()
	{
		return store.size();
	}

	@Override
	public JRPrintElement set(int index, JRPrintElement element)
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
	public boolean add(JRPrintElement element)
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
	public void add(int index, JRPrintElement element)
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
	public JRPrintElement remove(int index)
	{
		return store.remove(index);
	}

	@Override
	public void clear()
	{
		// recreating the store
		store.dispose();
		initStore();
	}

	public void dispose()
	{
		store.dispose();
	}
	
	//FIXME implement faster bulk methods such as addAll
}

interface ElementStore
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
	private transient VirtualElementsData virtualData;
	private transient int deepElementCount;
	
	public ElementsBlock(JRVirtualizationContext context)
	{
		this.context = context;
		this.uid = makeUID();
		
		if (log.isDebugEnabled())
		{
			log.debug("generated uid " + uid + " for " + this);
		}
		
		this.elements = new ArrayList<JRPrintElement>();
		this.deepElementCount = 0;
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
		//FIXME store the size separate from the list?
		ensureVirtualData();
		return elements.size();
	}

	public boolean isEmpty()
	{
		ensureVirtualData();
		return elements.isEmpty();
	}

	public JRPrintElement get(int index)
	{
		ensureDataAndTouch();
		return elements.get(index);
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
		boolean adding = preAdd(element, force);
		if (adding)
		{
			elements.add(element);
		}
		return adding;
	}
	
	public boolean add(JRPrintElement element)
	{
		return add(element, false);
	}

	public boolean add(int index, JRPrintElement element, boolean force)
	{
		boolean adding = preAdd(element, force);
		if (adding)
		{
			elements.add(index, element);
		}
		return adding;
	}
	
	public boolean add(int index, JRPrintElement element)
	{
		return add(index, element, false);
	}

	public JRPrintElement set(int index, JRPrintElement element)
	{
		ensureDataAndTouch();
		
		JRPrintElement oldElement = elements.get(index);
		deepElementCount -= DeepPrintElementCounter.count(oldElement);
		deepElementCount += DeepPrintElementCounter.count(element);
		
		return elements.set(index, element);
	}

	public JRPrintElement remove(int index)
	{
		ensureDataAndTouch();
		
		JRPrintElement element = elements.remove(index);
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
		if (elements == null)
		{
			ensureData();
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
		this.virtualData = virtualData;
		this.elements = virtualData.getElements();
	}

	public VirtualElementsData getVirtualData()
	{
		return virtualData;
	}

	public void removeVirtualData()
	{
		virtualData = null;
		elements = null;
	}

	public void beforeExternalization()
	{
		virtualData = new VirtualElementsData(elements);
		context.beforeExternalization(this);
	}

	public void afterExternalization()
	{
		context.afterExternalization(this);
	}

	public void afterInternalization()
	{
		context.afterInternalization(this);
		virtualData = null;
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
		ObjectInputStream elementsStream = new ObjectInputStream(inputStream);
		elements = (List<JRPrintElement>) elementsStream.readObject();
		context.restoreElementsData(elements);
		
		if (!elements.isEmpty())
		{
			register();
		}
	}

	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
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
			ObjectOutputStream stream = new ObjectOutputStream(bout);
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


	protected void finalize() throws Throwable //NOSONAR
	{
		dispose();
		super.finalize();
	}

	public void dispose()
	{
		// if empty, the object is not registered
		if (elements == null || !elements.isEmpty())
		{
			deregister();
		}
		// removeVirtualData?  letting GC do his thing for now.
	}

	private void deregister()
	{
		if (context.getVirtualizer() != null)
		{
			context.getVirtualizer().deregisterObject(this);
		}
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
		
		ElementsBlock block = new ElementsBlock(blocks[0].getContext());
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
}
