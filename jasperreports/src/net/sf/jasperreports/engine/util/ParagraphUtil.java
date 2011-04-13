/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.type.TabStopAlignEnum;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBoxUtil.java 4244 2011-03-10 13:35:47Z teodord $
 */
public final class ParagraphUtil
{

	
	/**
	 * 
	 */
	public static TabStop[] getTabStops(JRParagraph paragraph, float width)
	{
		List<TabStop> tabStopList = new ArrayList<TabStop>();

		TabStop lastTabStop = new TabStop();
		
		TabStop[] tabStops = paragraph.getTabStops();
		if (tabStops != null && tabStops.length > 0)
		{
			tabStopList.addAll(Arrays.asList(tabStops));
			lastTabStop = tabStopList.get(tabStopList.size() - 1);
		}
		
		while (lastTabStop.getPosition() + paragraph.getTabStopWidth() < width)
		{
			lastTabStop = new TabStop((lastTabStop.getPosition() / paragraph.getTabStopWidth() + 1) * paragraph.getTabStopWidth(), TabStopAlignEnum.LEFT);
			tabStopList.add(lastTabStop);
		}
		
		return tabStopList.toArray(new TabStop[tabStopList.size()]);
	}
	
	
	/**
	 * 
	 */
	public static int getRightX(TabStop tabStop, TextLayout layout)
	{
		int rightX = 0;
		switch (tabStop.getAlignment())
		{
			case RIGHT ://FIXMETAB RTL writings
			{
				rightX = tabStop.getPosition();
				break;
			}
			case CENTER :
			{
				rightX = (int)(tabStop.getPosition() + layout.getAdvance() / 2);
				break;
			}
			case LEFT :
			default :
			{
				rightX = (int)(tabStop.getPosition() + layout.getAdvance());
			}
		}
		return rightX;
	}
	

	/**
	 * 
	 */
	public static int getLeftX(TabStop tabStop, float advance)
	{
		int leftX = 0;
		switch (tabStop.getAlignment())
		{
			case RIGHT ://FIXMETAB RTL writings
			{
				leftX = (int)(tabStop.getPosition() - advance);
				break;
			}
			case CENTER :
			{
				leftX = (int)(tabStop.getPosition() - advance / 2);
				break;
			}
			case LEFT :
			default :
			{
				leftX = tabStop.getPosition();
			}
		}
		return leftX;
	}

	
	/**
	 * 
	 */
	public static int getSegmentOffset(TabStop tabStop, int rightX)
	{
		int segmentOffset = 0;
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
		return segmentOffset;
	}


	/**
	 * 
	 */
	public static TabStop getNextTabStop(JRParagraph paragraph, float formatWidth, int rightX)
	{
		TabStop nextTabStop = new TabStop();
		TabStop[] tabStops = getTabStops(paragraph, formatWidth);
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
			nextTabStop.setPosition((rightX / paragraph.getTabStopWidth() + 1) * paragraph.getTabStopWidth());
		}
		return nextTabStop;
	}


	/**
	 * 
	 */
	public static TabStop getFirstTabStop(JRParagraph paragraph, float formatWidth)
	{
		TabStop firstTabStop = new TabStop();
		TabStop[] tabStops = getTabStops(paragraph, formatWidth);
		firstTabStop = tabStops[0];
		return firstTabStop;
	}


	/**
	 * 
	 */
	public static TabStop getLastTabStop(JRParagraph paragraph, float formatWidth)
	{
		TabStop lastTabStop = new TabStop();
		TabStop[] tabStops = getTabStops(paragraph, formatWidth);
		int i = tabStops.length - 1;
		for (; i >= 0; i--)
		{
			TabStop tabStop = tabStops[i];
			if (tabStop.getPosition() < formatWidth)//FIXMETAB assumes tab stops are sorted by position
			{
				lastTabStop = tabStop;
				break;
			}
		}
		if (i < 0)
		{
			//FIXMETAB what to do here?
			lastTabStop.setPosition((int)formatWidth);
		}
		return lastTabStop;
	}
	

	private ParagraphUtil()
	{
	}
}
