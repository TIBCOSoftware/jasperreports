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
package net.sf.jasperreports.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import net.sf.jasperreports.Report;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;

public class ReturnValuesTest
{
	
	@Test
	public void tableReturn() throws JRException, IOException
	{
		Report report = new Report("net/sf/jasperreports/components/repo/TableReturn.jrxml", 
				"net/sf/jasperreports/components/repo/TableReturn.jrpxml");
		report.init();
		
		Map<String, Object> params = new HashMap<>();
		report.runReport(params);
	}
	
	@Test
	public void tableWithSubreportReturn() throws JRException, IOException
	{
		Report subreport = new Report("net/sf/jasperreports/components/repo/SubreportForReturn.jrxml", null);
		JasperReport compiledSubreport = subreport.compileReport();
		
		Report report = new Report("net/sf/jasperreports/components/repo/TableWithSubreportReturn.jrxml", 
				"net/sf/jasperreports/components/repo/TableWithSubreportReturn.jrpxml");
		report.init();
		
		Map<String, Object> params = new HashMap<>();
		params.put("subreport", compiledSubreport);
		report.runReport(params);
	}
	
	@Test
	public void tableWithListReturn() throws JRException, IOException
	{
		Report report = new Report("net/sf/jasperreports/components/repo/TableWithListReturn.jrxml", 
				"net/sf/jasperreports/components/repo/TableWithListReturn.jrpxml");
		report.init();
		
		Map<String, Object> params = new HashMap<>();
		report.runReport(params);
	}
	
	@Test
	public void tableWithTableReturn() throws JRException, IOException
	{
		Report report = new Report("net/sf/jasperreports/components/repo/TableWithTableReturn.jrxml", 
				"net/sf/jasperreports/components/repo/TableWithTableReturn.jrpxml");
		report.init();
		
		Map<String, Object> params = new HashMap<>();
		report.runReport(params);
	}
	
	@Test
	public void listWithSubreportReturn() throws JRException, IOException
	{
		Report subreport = new Report("net/sf/jasperreports/components/repo/SubreportForReturn.jrxml", null);
		JasperReport compiledSubreport = subreport.compileReport();
		
		Report report = new Report("net/sf/jasperreports/components/repo/ListWithSubreportReturn.jrxml", 
				"net/sf/jasperreports/components/repo/ListWithSubreportReturn.jrpxml");
		report.init();
		
		Map<String, Object> params = new HashMap<>();
		params.put("subreport", compiledSubreport);
		report.runReport(params);
	}
	
	@Test
	public void listWithListReturn() throws JRException, IOException
	{
		Report report = new Report("net/sf/jasperreports/components/repo/ListWithListReturn.jrxml", 
				"net/sf/jasperreports/components/repo/ListWithListReturn.jrpxml");
		report.init();
		
		Map<String, Object> params = new HashMap<>();
		report.runReport(params);
	}
	
	@Test
	public void listWithTableReturn() throws JRException, IOException
	{
		Report report = new Report("net/sf/jasperreports/components/repo/ListWithTableReturn.jrxml", 
				"net/sf/jasperreports/components/repo/ListWithTableReturn.jrpxml");
		report.init();
		
		Map<String, Object> params = new HashMap<>();
		report.runReport(params);
	}

}
