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
package net.sf.jasperreports.crosstabs.fill.calculation;

import java.util.Comparator;

import net.sf.jasperreports.engine.JRRuntimeException;

import org.apache.commons.collections.map.ReferenceMap;

/**
 * A comparator that assigns arbitrary ranks to objects and uses the ranks
 * to impose an arbitrary order on them.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ArbitraryRankComparator implements Comparator<Object>
{
	public static final String EXCEPTION_MESSAGE_KEY_FOUND_OBJECTS_WITH_SAME_RANK = "crosstabs.calculation.found.objects.with.same.rank";
	public static final String EXCEPTION_MESSAGE_KEY_RANK_COMPARATOR_OVERFLOW = "crosstabs.calculation.rank.comparator.overflow";

	// using a weak ref map to store ranks per objects
	private final ReferenceMap ranks = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.HARD);
	private long rankCounter = Long.MIN_VALUE;
	
	public int compare(Object o1, Object o2)
	{
		if (o1 == o2 || o1.equals(o2))
		{
			return 0;
		}
		
		long rank1 = rank(o1);
		long rank2 = rank(o2);
		
		if (rank1 < rank2)
		{
			return -1;
		}
		
		if (rank1 > rank2)
		{
			return 1;
		}
		
		// this should not happen
		throw 
			new JRRuntimeException(
				EXCEPTION_MESSAGE_KEY_FOUND_OBJECTS_WITH_SAME_RANK,
				(Object[])null);
	}

	protected synchronized long rank(Object o)
	{
		long rank;
		Long existingRank = (Long) ranks.get(o);
		if (existingRank == null)
		{
			rank = rankCounter;
			
			++rankCounter;
			// check for overflow, very unlikely
			if (rankCounter == Long.MIN_VALUE)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_RANK_COMPARATOR_OVERFLOW,
						(Object[])null);
			}
			
			ranks.put(o, new Long(rank));
		}
		else
		{
			rank = existingRank.longValue();
		}
		return rank;
	}
	
}
