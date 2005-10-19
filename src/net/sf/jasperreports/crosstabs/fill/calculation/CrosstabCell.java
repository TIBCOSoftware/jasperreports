/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs.fill.calculation;

/**
 * Crosstab cell produced by the crosstab bucketing engine.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class CrosstabCell
{
	private final BucketDefinition.Bucket[] rowBucketValues;
	private final int rowTotalGroupIndex;
	private final BucketDefinition.Bucket[] columnBucketValues;
	private final int columnTotalGroupIndex;
	private final MeasureDefinition.MeasureValue[] mesureValues;
	
	
	/**
	 * Create a crosstab cell.
	 * 
	 * @param rowBucketValues the row bucket values corresponding to the cell
	 * @param columnBucketValues the column bucket values corresponding to the cell
	 * @param mesureValues the measure values
	 */
	public CrosstabCell(
			BucketDefinition.Bucket[] rowBucketValues, 
			BucketDefinition.Bucket[] columnBucketValues, 
			MeasureDefinition.MeasureValue[] mesureValues)
	{
		this.rowBucketValues = rowBucketValues;
		rowTotalGroupIndex = getTotalIndex(rowBucketValues);
		this.columnBucketValues = columnBucketValues;
		columnTotalGroupIndex = getTotalIndex(columnBucketValues);
		this.mesureValues = mesureValues;
	}

	
	private static int getTotalIndex(BucketDefinition.Bucket[] values)
	{
		int i = 0;
		while (i < values.length && !values[i].isTotal())
		{
			++i;
		}
		
		return i;
	}

	
	/**
	 * Returns the measure values for this cell.
	 * 
	 * @return the measure values for this cell
	 */
	public MeasureDefinition.MeasureValue[] getMesureValues()
	{
		return mesureValues;
	}


	/**
	 * Returns the column bucket values corresponding to this cell.
	 * 
	 * @return the column bucket values corresponding to this cell
	 */
	public BucketDefinition.Bucket[] getColumnBucketValues()
	{
		return columnBucketValues;
	}


	/**
	 * Returns the row bucket values corresponding to this cell.
	 * 
	 * @return the row bucket values corresponding to this cell
	 */
	public BucketDefinition.Bucket[] getRowBucketValues()
	{
		return rowBucketValues;
	}


	/**
	 * Returns the index of the column total bucket this cell corresponds to.
	 * <p>
	 * If this cell corresponds to a column total bucket, this method returns the index of the
	 * bucket.  Otherwise it returns the number of column buckets.
	 * 
	 * @return the index of the column total bucket this cell corresponds to 
	 */
	public int getColumnTotalGroupIndex()
	{
		return columnTotalGroupIndex;
	}


	/**
	 * Returns the index of the row total bucket this cell corresponds to.
	 * <p>
	 * If this cell corresponds to a row total bucket, this method returns the index of the
	 * bucket.  Otherwise it returns the number of row buckets.
	 * 
	 * @return the index of the row total bucket this cell corresponds to 
	 */
	public int getRowTotalGroupIndex()
	{
		return rowTotalGroupIndex;
	}
}
