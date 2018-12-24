package net.sf.jasperreports.bands.infinite;

import java.util.HashMap;

import org.testng.annotations.Test;

import net.sf.jasperreports.Report;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;

public class HorizontalColumnHeaderTest
{

	@Test
	public void test()
	{
		Report report = new Report("net/sf/jasperreports/bands/infinite/repo/ColumnOverflowTest");
		report.init();
		
		HashMap<String, Object> params = new HashMap<>();
		params.put(JRParameter.REPORT_DATA_SOURCE, new JREmptyDataSource());
		report.runReport(params);
	}
	
}
