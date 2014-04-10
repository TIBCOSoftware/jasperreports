/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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


/*
 * Contributors:
 * John Bindel - jbindel@users.sourceforge.net 
 */

package net.sf.jasperreports.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperPrint.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class BookmarkHelper
{
	private BookmarkStack bookmarkStack;

	public BookmarkHelper(boolean isCollapseMissingBookmarkLevels)
	{
		bookmarkStack = new BookmarkStack(isCollapseMissingBookmarkLevels);
	}
	
	public void addBookmarks(JRPrintPage page, int pageIndex)
	{
		bookmarkStack.addBookmarks(page.getElements(), pageIndex, null);
	}
	
	public void updateBookmark(JRPrintElement element)
	{
		bookmarkStack.updateBookmark(element);
	}
	
	public List<PrintBookmark> getRootBookmarks()
	{
		return bookmarkStack.getRootBookmarks();
	}
}


/**
 * 
 */
class BookmarkStack
{
	private static final Log log = LogFactory.getLog(BookmarkStack.class); 
	
	LinkedList<Bookmark> stack;
	boolean isCollapseMissingBookmarkLevels;
	Map<PrintElementId, Bookmark> updateableBookmarks;

	public BookmarkStack(boolean isCollapseMissingBookmarkLevels)
	{
		stack = new LinkedList<Bookmark>();
		this.isCollapseMissingBookmarkLevels = isCollapseMissingBookmarkLevels;
		push(new Bookmark(null, null, 0, null));//root bookmark is only used as container for root node
		
		updateableBookmarks = new HashMap<PrintElementId, Bookmark>();
	}

	public void push(Bookmark bookmark)
	{
		stack.add(bookmark);
	}

	public Bookmark pop()
	{
		return stack.removeLast();
	}

	public Bookmark peek()
	{
		return stack.getLast();
	}

	protected Bookmark addBookmark(int level, String label, int pageIndex, String elementAddress)
	{
		Bookmark parent = this.peek();
		// searching for parent
		while(parent.getLevel() >= level)
		{
			this.pop();
			parent = this.peek();
		}

		if (!isCollapseMissingBookmarkLevels)
		{
			PrintBookmark parentPrintBookmark = parent.getPrintBookmark();
			// creating empty bookmarks in order to preserve the bookmark level
			for (int i = parent.getLevel() + 1; i < level; ++i)
			{
				Bookmark emptyBookmark = new Bookmark(parent, "", parentPrintBookmark.getPageIndex(), parentPrintBookmark.getElementAddress());
				this.push(emptyBookmark);
				parent = emptyBookmark;
			}
		}

		Bookmark bookmark = new Bookmark(parent, label, pageIndex, elementAddress);
		this.push(bookmark);
		return bookmark;
	}
	
	protected void addBookmarks(List<JRPrintElement> elements, int pageIndex, String elementAddress)
	{
		if (elements != null)
		{
			elementAddress = elementAddress == null ? "" : elementAddress;

			int i = 0;
			for (JRPrintElement element : elements)
			{
				if (element instanceof JRPrintFrame)
				{
					addBookmarks(((JRPrintFrame)element).getElements(), pageIndex, elementAddress + (elementAddress.length() == 0 ? "" : "_") + i + "_");
				}
				else if (element instanceof JRPrintAnchor)
				{
					JRPrintAnchor anchor = (JRPrintAnchor)element;
					int level = anchor.getBookmarkLevel();
					if (level != JRAnchor.NO_BOOKMARK)
					{
						String anchorName = anchor.getAnchorName();
						Bookmark bookmark = addBookmark(level, anchorName, pageIndex, elementAddress + i);
						
						// we're keeping a map with bookmarks for elements with late evaluation
						// not placing bookmarks for other elements in the map to save some space
						if (anchorName == null)// the element could have late evaluation; there's no exact test for it
						{
							PrintElementId elementId = PrintElementId.forElement(element);
							updateableBookmarks.put(elementId, bookmark);
						}
					}
				}
				i++;
			}
		}
	}

	public void updateBookmark(JRPrintElement element)
	{
		if (element instanceof JRPrintAnchor)
		{
			JRPrintAnchor anchor = (JRPrintAnchor) element;
			int level = anchor.getBookmarkLevel();
			if (level != JRAnchor.NO_BOOKMARK)
			{
				PrintElementId elementId = PrintElementId.forElement(element);
				Bookmark bookmark = updateableBookmarks.get(elementId);//FIXME remove from the map?
				if (bookmark == null)
				{
					if (log.isDebugEnabled())
					{
						// this can happen when the element has a delayed evaluation on the same page
						log.debug("Cound not find bookmark for " + elementId + " to update");
					}
				}
				else
				{
					String anchorName = anchor.getAnchorName();
					bookmark.updateLabel(anchorName);
				}
			}
		}
	}
	
	protected List<PrintBookmark> getRootBookmarks()
	{
		return stack.getFirst().getPrintBookmark().getBookmarks();
	}
}


/**
 *
 */
class Bookmark
{
	private final int level;
	private final PrintBookmark printBookmark;
	
	public Bookmark(Bookmark parent, String label, int pageIndex, String elementAddress)
	{
		this.level = parent == null ? 0 : (parent.getLevel() + 1);
		this.printBookmark = new BasePrintBookmark(label, pageIndex, elementAddress);
		
		if (parent != null)
		{
			((BasePrintBookmark)parent.getPrintBookmark()).addBookmark(printBookmark);
		}
	}
	
	public void updateLabel(String label)
	{
		((BasePrintBookmark) printBookmark).setLabel(label);
	}

	public int getLevel()
	{
		return level;
	}
	
	public PrintBookmark getPrintBookmark()
	{
		return printBookmark;
	}
}

