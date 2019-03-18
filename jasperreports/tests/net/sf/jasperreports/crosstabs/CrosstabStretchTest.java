/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import net.sf.jasperreports.AbstractTest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.type.StretchTypeEnum;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CrosstabStretchTest extends AbstractTest
{
	@Test(dataProvider = "testArgsLegacy")
	public void testReportLegacy(String jrxmlFileName, String referenceFileNamePrefix) 
			throws JRException, NoSuchAlgorithmException, IOException
	{
		SimpleJasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();
		setJasperReportsContext(jasperReportsContext);

		jasperReportsContext.setProperty(StretchTypeEnum.PROPERTY_LEGACY_ELEMENT_STRETCH_ENABLED, "true");
		runReport(jrxmlFileName, referenceFileNamePrefix);
	}
	
	@DataProvider
	public Object[][] testArgsLegacy()
	{
		return runReportArgs("net/sf/jasperreports/crosstabs/repo", "CrosstabStretchReport", "CrosstabLegacyStretchReport", 7);
	}
	
	@Test(dataProvider = "testArgs")
	public void testReport(String jrxmlFileName, String referenceFileNamePrefix) 
			throws JRException, NoSuchAlgorithmException, IOException
	{
		SimpleJasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();
		setJasperReportsContext(jasperReportsContext);
		
		jasperReportsContext.setProperty(StretchTypeEnum.PROPERTY_LEGACY_ELEMENT_STRETCH_ENABLED, "false");
		runReport(jrxmlFileName, referenceFileNamePrefix);
	}
	
	@DataProvider
	public Object[][] testArgs()
	{
		return runReportArgs("net/sf/jasperreports/crosstabs/repo", "CrosstabStretchReport", 7);
	}
}
