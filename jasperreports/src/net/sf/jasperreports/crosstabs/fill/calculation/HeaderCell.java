/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs.fill.calculation;

/**
 * Crosstab header cell produced by the crosstab bucketing engine.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class HeaderCell
{
	private final BucketDefinition.Bucket[] bucketValues;
	private final int levelSpan;
	private final int depthSpan;
	private final boolean isTotal;
	
	
	/**
	 * Creates a crosstab header cell.
	 * 
	 * @param bucketValues the bucket values for the cell
	 * @param levelSpan the span across cells on the same level (bucket)
	 * @param depthSpan the span across cells on subsequent levels (buckets) 
	 */
	public HeaderCell(
			BucketDefinition.Bucket[] bucketValues, 
			int levelSpan,
			int depthSpan)
	{
		this.bucketValues = bucketValues;
		this.levelSpan = levelSpan;
		this.depthSpan = depthSpan;
		
		boolean foundTotal = false;
		for (int i = 0; i < bucketValues.length; i++)
		{
			if (bucketValues[i] != null && bucketValues[i].isTotal())
			{
				foundTotal = true;
				break;
			}
		}
		
		isTotal = foundTotal;
	}


	/**
	 * Returns the bucket values for this cell.
	 * 
	 * @return the bucket values for this cell
	 */
	public BucketDefinition.Bucket[] getBucketValues()
	{
		return bucketValues;
	}


	/**
	 * Returns the span across cells on the same level (bucket).
	 * <p>
	 * This is used for headers of buckets having sub-buckets.
	 * 
	 * @return the span across cells on the same level (bucket)
	 */
	public int getLevelSpan()
	{
		return levelSpan;
	}


	/**
	 * Returns the span across cells on subsequent levels (buckets).
	 * <p>
	 * This is used for total headers.
	 * 
	 * @return the span across cells on subsequent levels (buckets)
	 */
	public int getDepthSpan()
	{
		return depthSpan;
	}
	
	
	/**
	 * Returns whether this header is a total header.
	 * 
	 * @return whether this header is a total header
	 */
	public boolean isTotal()
	{
		return isTotal;
	}
	
	public static HeaderCell createLevelSpanCopy(HeaderCell cell, int newLevelSpan)
	{
		return new HeaderCell(cell.bucketValues, newLevelSpan, cell.getDepthSpan());
	}
}
