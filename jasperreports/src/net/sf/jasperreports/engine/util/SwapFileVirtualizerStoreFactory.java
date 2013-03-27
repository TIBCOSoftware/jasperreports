package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.fill.JRVirtualizationContext;
import net.sf.jasperreports.engine.fill.VirtualizerStore;
import net.sf.jasperreports.engine.fill.VirtualizerStoreFactory;

public class SwapFileVirtualizerStoreFactory implements VirtualizerStoreFactory
{
	private String directory = System.getProperty("java.io.tmpdir");//default value
	private int blockSize = 4096;//default value
	private int minGrowCount = 20;//default value
	
	@Override
	public VirtualizerStore createStore(JRVirtualizationContext virtualizationContext)
	{
		JRSwapFile swapFile = new JRSwapFile(directory, blockSize, minGrowCount);
		return new SwapFileVirtualizerStore(swapFile, true);
	}

	public String getDirectory()
	{
		return directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public int getBlockSize()
	{
		return blockSize;
	}

	public void setBlockSize(int blockSize)
	{
		this.blockSize = blockSize;
	}

	public int getMinGrowCount()
	{
		return minGrowCount;
	}

	public void setMinGrowCount(int minGrowCount)
	{
		this.minGrowCount = minGrowCount;
	}
}
