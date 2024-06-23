/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.crosstabs;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import net.sf.jasperreports.crosstabs.fill.calculation.OrderByColumnInfo;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.interactivity.crosstabs.OrderByColumnInfoImpl;
import net.sf.jasperreports.jackson.util.JacksonUtil;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class OrderByColumnInfoDeserializationTest
{
	
	private JacksonUtil jacksonUtil;

	@BeforeTest
	public void setup()
	{
		jacksonUtil = JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance());
	}
	
	@Test(dataProvider = "sortOrders")
	public void sortOrderTest(String jsonText, SortOrderEnum order)
	{
		OrderByColumnInfo info = jacksonUtil.loadObject(jsonText, OrderByColumnInfoImpl.class);
		assert info != null;
		assert info.getOrder() == order;
	}
	
	@DataProvider
	public Object[][] sortOrders()
	{
		return new Object[][] {
			{"{\"order\":\"Ascending\",\"measureIndex\":0,\"columnValues\":[{\"total\":false,\"valueType\":\"java.lang.Integer\",\"value\":\"3\"}]}", SortOrderEnum.ASCENDING},
			{"{\"order\":\"Descending\",\"measureIndex\":0,\"columnValues\":[{\"total\":false,\"valueType\":\"java.lang.Integer\",\"value\":\"3\"}]}", SortOrderEnum.DESCENDING},
			{"{\"order\":\"ASCENDING\",\"measureIndex\":0,\"columnValues\":[{\"total\":false,\"valueType\":\"java.lang.Integer\",\"value\":\"3\"}]}", SortOrderEnum.ASCENDING},
			{"{\"order\":\"DESCENDING\",\"measureIndex\":0,\"columnValues\":[{\"total\":false,\"valueType\":\"java.lang.Integer\",\"value\":\"3\"}]}", SortOrderEnum.DESCENDING},
		};
	}
	
	@Test(dataProvider = "invalidSortOrders")
	public void invalidSortOrderTest(String jsonText)
	{
		try
		{
			OrderByColumnInfo info = jacksonUtil.loadObject(jsonText, OrderByColumnInfo.class);
			assert false;
		}
		catch (JRRuntimeException e)
		{
			assert true;
		}
	}
	
	@DataProvider
	public Object[][] invalidSortOrders()
	{
		return new Object[][] {
			{"{\"order\":\"AScending\",\"measureIndex\":0,\"columnValues\":[{\"total\":false,\"valueType\":\"java.lang.Integer\",\"value\":\"3\"}]}"},
		};
	}

}
