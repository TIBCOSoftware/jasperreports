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
package net.sf.jasperreports.engine;

import java.io.Serializable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRElement.java 1229 2006-04-19 10:27:35Z teodord $
 */
public class JROrigin implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	public static final byte UNKNOWN = 0;
	public static final byte BACKGROUND = 1;
	public static final byte TITLE = 2;
	public static final byte PAGE_HEADER = 3;
	public static final byte COLUMN_HEADER = 4;
	public static final byte GROUP_HEADER = 5;
	public static final byte DETAIL = 6;
	public static final byte GROUP_FOOTER = 7;
	public static final byte COLUMN_FOOTER = 8;
	public static final byte PAGE_FOOTER = 9;
	public static final byte LAST_PAGE_FOOTER = 10;
	public static final byte SUMMARY = 11;
	public static final byte NO_DATA = 12;

	private byte bandType = UNKNOWN;
	private String groupName = null;
	private String reportName = null;
	
	private int hashCode = 0;
	
	/**
	 * 
	 */
	public JROrigin(
		byte bandType
		)
	{
		this(null, null, bandType);
	}

	/**
	 * 
	 */
	public JROrigin(
		String reportName,
		byte bandType
		)
	{
		this(reportName, null, bandType);
	}

	/**
	 * 
	 */
	public JROrigin(
		String reportName,
		String groupName,
		byte bandType
		)
	{
		this.reportName = reportName;
		this.groupName = groupName;
		this.bandType = bandType;

		int hash = 17;
		hash = 31 * hash + (reportName == null ? 0 : reportName.hashCode());
		hash = 31 * hash + (groupName == null ? 0 : groupName.hashCode());
		hash = 31 * hash + bandType;
		hashCode = hash;
	}

	/**
	 * 
	 */
	public String getReportName()
	{
		return reportName;
	}

	/**
	 * 
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * 
	 */
	public byte getBandType()
	{
		return bandType;
	}
	
	/**
	 * 
	 */
	public boolean equals(Object obj) 
	{
		if (obj instanceof JROrigin)
		{
			JROrigin origin = (JROrigin)obj;
			String groupName2 = origin.getGroupName();
			String reportName2 = origin.getReportName();
			return
				origin.getBandType() == bandType
				&& ((groupName == null && groupName2 == null) || (groupName.equals(groupName2)))
				&& ((reportName == null && reportName2 == null) || (reportName.equals(reportName2)));
		}
		return false;
	}
	
	/**
	 * 
	 */
	public int hashCode() 
	{
		return hashCode;
	}

}
