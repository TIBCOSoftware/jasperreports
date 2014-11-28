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
package net.sf.jasperreports.engine.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.type.TabStopAlignEnum;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class ParagraphUtil
{

	
	/**
	 * 
	 */
	public static TabStop[] getTabStops(JRParagraph paragraph, float endX)
	{
		List<TabStop> tabStopList = new ArrayList<TabStop>();

		TabStop lastTabStop = new TabStop();
		
		TabStop[] tabStops = paragraph.getTabStops();
		if (tabStops != null && tabStops.length > 0)
		{
			for (int i = 0; i < tabStops.length; i++)
			{
				lastTabStop = tabStops[i];
				if (
					//startX <= lastTabStop.getPosition() &&
					lastTabStop.getPosition() <= endX
					)
				{
					tabStopList.add(lastTabStop);
				}
			}
		}
		
		while (
			//startX <= lastTabStop.getPosition() + paragraph.getTabStopWidth() &&
			lastTabStop.getPosition() + paragraph.getTabStopWidth() <= endX
			)
		{
			lastTabStop = new TabStop((lastTabStop.getPosition() / paragraph.getTabStopWidth() + 1) * paragraph.getTabStopWidth(), TabStopAlignEnum.LEFT);
			tabStopList.add(lastTabStop);
		}
		
		return tabStopList.toArray(new TabStop[tabStopList.size()]);
	}
	
	
	/**
	 * 
	 */
	public static float getRightX(TabStop tabStop, float advance)
	{
		float rightX = advance;
		if (tabStop != null)
		{
			switch (tabStop.getAlignment())
			{
				case RIGHT ://FIXMETAB RTL writings
				{
					rightX = tabStop.getPosition();
					break;
				}
				case CENTER :
				{
					rightX = tabStop.getPosition() + advance / 2;
					break;
				}
				case LEFT :
				default :
				{
					rightX = tabStop.getPosition() + advance;
				}
			}
		}
		return rightX;
	}
	

	/**
	 * 
	 */
	public static float getLeftX(TabStop tabStop, float advance)
	{
		float leftX = 0;
		if (tabStop != null)
		{
			switch (tabStop.getAlignment())
			{
				case RIGHT ://FIXMETAB RTL writings
				{
					leftX = tabStop.getPosition() - advance;
					break;
				}
				case CENTER :
				{
					leftX = tabStop.getPosition() - advance / 2;
					break;
				}
				case LEFT :
				default :
				{
					leftX = tabStop.getPosition();
				}
			}
		}
		return leftX;
	}

	
	/**
	 * 
	 */
	public static float getSegmentOffset(TabStop tabStop, float rightX)
	{
		float segmentOffset = rightX;
		if (tabStop != null)
		{
			switch (tabStop.getAlignment())
			{
				case RIGHT ://FIXMETAB RTL writings
				{
					segmentOffset = rightX;
					break;
				}
				case CENTER :
				{
					segmentOffset = rightX;
					break;
				}
				case LEFT :
				default :
				{
					segmentOffset = tabStop.getPosition();
				}
			}
		}
		return segmentOffset;
	}


	/**
	 * 
	 */
	public static TabStop getNextTabStop(JRParagraph paragraph, float endX, float rightX)
	{
		TabStop nextTabStop = null;
		TabStop[] tabStops = getTabStops(paragraph, endX);
		int i = 0;
		for (; i < tabStops.length; i++)
		{
			TabStop tabStop = tabStops[i];
			if (tabStop.getPosition() > rightX)//FIXMETAB assumes tab stops are sorted by position
			{
				nextTabStop = tabStop;
				break;
			}
		}
		if (i == tabStops.length)
		{
			//FIXMETAB what to do here?
			nextTabStop = new TabStop();
			nextTabStop.setPosition((int)((rightX / paragraph.getTabStopWidth() + 1) * paragraph.getTabStopWidth()));
		}
		return nextTabStop;
	}


	/**
	 * 
	 */
	public static TabStop getFirstTabStop(JRParagraph paragraph, float endX)
	{
		TabStop firstTabStop = new TabStop();
		TabStop[] tabStops = getTabStops(paragraph, endX);
		firstTabStop = tabStops[0];
		return firstTabStop;
	}


	/**
	 * 
	 */
	public static TabStop getLastTabStop(JRParagraph paragraph, float endX)
	{
		TabStop lastTabStop = new TabStop();
		TabStop[] tabStops = getTabStops(paragraph, endX);
		int i = tabStops.length - 1;
		for (; i >= 0; i--)
		{
			TabStop tabStop = tabStops[i];
			if (tabStop.getPosition() < endX)//FIXMETAB assumes tab stops are sorted by position
			{
				lastTabStop = tabStop;
				break;
			}
		}
		if (i < 0)
		{
			//FIXMETAB what to do here?
			lastTabStop.setPosition((int)endX);
		}
		return lastTabStop;
	}
	

	private ParagraphUtil()
	{
	}
}
