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
package net.sf.jasperreports.engine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.type.BandTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JROrigin implements JRCloneable, Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * @deprecated Replaced by {@link BandTypeEnum#UNKNOWN}.
	 */
	public static final byte UNKNOWN = 0;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#BACKGROUND}.
	 */
	public static final byte BACKGROUND = 1;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#UNKNOWN}.
	 */
	public static final byte TITLE = 2;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#PAGE_HEADER}.
	 */
	public static final byte PAGE_HEADER = 3;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#COLUMN_HEADER}.
	 */
	public static final byte COLUMN_HEADER = 4;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#GROUP_HEADER}.
	 */
	public static final byte GROUP_HEADER = 5;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#DETAIL}.
	 */
	public static final byte DETAIL = 6;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#GROUP_FOOTER}.
	 */
	public static final byte GROUP_FOOTER = 7;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#COLUMN_FOOTER}.
	 */
	public static final byte COLUMN_FOOTER = 8;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#PAGE_FOOTER}.
	 */
	public static final byte PAGE_FOOTER = 9;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#LAST_PAGE_FOOTER}.
	 */
	public static final byte LAST_PAGE_FOOTER = 10;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#SUMMARY}.
	 */
	public static final byte SUMMARY = 11;
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#NO_DATA}.
	 */
	public static final byte NO_DATA = 12;

	private BandTypeEnum bandTypeValue = BandTypeEnum.UNKNOWN;
	private String groupName = null;
	private String reportName = null;
	
	private int hashCode = 0;
	
	/**
	 * 
	 */
	public JROrigin(
		BandTypeEnum bandType
		)
	{
		this(null, null, bandType);
	}

	/**
	 * 
	 */
	public JROrigin(
		String reportName,
		BandTypeEnum bandType
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
		BandTypeEnum bandTypeValue
		)
	{
		this.reportName = reportName;
		this.groupName = groupName;
		this.bandTypeValue = bandTypeValue;

		int hash = 17;
		hash = 31 * hash + (reportName == null ? 0 : reportName.hashCode());
		hash = 31 * hash + (groupName == null ? 0 : groupName.hashCode());
		hash = 31 * hash + bandTypeValue.hashCode();
		hashCode = hash;
	}

	/**
	 * @deprecated Replaced by {@link #JROrigin(BandTypeEnum)}.
	 */
	public JROrigin(
		byte bandType
		)
	{
		this(null, null, BandTypeEnum.getByValue(bandType));
	}

	/**
	 * @deprecated Replaced by {@link #JROrigin(String, BandTypeEnum)}.
	 */
	public JROrigin(
		String reportName,
		byte bandType
		)
	{
		this(reportName, null, BandTypeEnum.getByValue(bandType));
	}

	/**
	 * @deprecated Replaced by {@link #JROrigin(String, String, BandTypeEnum)}.
	 */
	public JROrigin(
		String reportName,
		String groupName,
		byte bandType
		)
	{
		this(reportName, groupName, BandTypeEnum.getByValue(bandType));
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
	 * @deprecated Replaced by {@link #getBandTypeValue()}. 
	 */
	public byte getBandType()
	{
		return getBandTypeValue().getValue();
	}
	
	/**
	 * 
	 */
	public BandTypeEnum getBandTypeValue()
	{
		return bandTypeValue;
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
				origin.getBandTypeValue() == bandTypeValue
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


	/**
	 * 
	 */
	public Object clone() 
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 * This field is only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	private byte bandType;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			bandTypeValue = BandTypeEnum.getByValue(bandType);
		}
	}

}
