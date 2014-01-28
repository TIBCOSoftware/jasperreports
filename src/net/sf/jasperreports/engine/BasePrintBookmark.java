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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperPrint.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class BasePrintBookmark implements PrintBookmark, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private final String label;
	private final int pageIndex;
	private String elementAddress;

	public List<PrintBookmark> bookmarks;

	public BasePrintBookmark(String label, int pageIndex, String elementAddress)
	{
		this.label = label;
		this.pageIndex = pageIndex;
		this.elementAddress = elementAddress;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public int getPageIndex()
	{
		return pageIndex;
	}
	
	public String getElementAddress()
	{
		return elementAddress;
	}
	
	public void addBookmark(PrintBookmark child)
	{
		if (bookmarks == null)
		{
			bookmarks = new ArrayList<PrintBookmark>();
		}
		bookmarks.add(child);
	}
	
	public List<PrintBookmark> getBookmarks()
	{
		return bookmarks;
	}
}
