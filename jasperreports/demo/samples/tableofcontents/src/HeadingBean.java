/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class HeadingBean
{


	/**
	 *
	 */
	private Integer headingType;
	private String headingText;
	private String reference;
	private Integer pageIndex;


	/**
	 *
	 */
	public HeadingBean(
		Integer type,
		String text,
		String reference,
		Integer pageIndex
		)
	{
		this.headingType = type;
		this.headingText = text;
		this.reference = reference;
		this.pageIndex = pageIndex;
	}


	/**
	 *
	 */
	public Integer getHeadingType()
	{
		return this.headingType;
	}


	/**
	 *
	 */
	public String getHeadingText()
	{
		return this.headingText;
	}


	/**
	 *
	 */
	public String getReference()
	{
		return this.reference;
	}


	/**
	 *
	 */
	public Integer getPageIndex()
	{
		return this.pageIndex;
	}


}
