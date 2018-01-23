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
