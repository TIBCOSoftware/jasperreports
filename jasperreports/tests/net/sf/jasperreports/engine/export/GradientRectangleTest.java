package net.sf.jasperreports.engine.export;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import net.sf.jasperreports.Report;
import net.sf.jasperreports.components.barcode4j.Barcode4JTest;
import net.sf.jasperreports.engine.JRException;

/**
 * Tests the Gradient Rectangle feature<br>
 * Inspired by {@link Barcode4JTest}
 * 
 * @author Diogo Sant'Ana <diogosantana@gmail.com>
 * 
 */
public class GradientRectangleTest {

	@Test
	public void gradientRectangle() throws JRException, IOException {
		Report report = new Report("net/sf/jasperreports/engine/export/repo/GradientRectangle.jrxml",
				"net/sf/jasperreports/engine/export/repo/GradientRectangle.jrpxml");
		report.init();

		Map<String, Object> params = new HashMap<>();
		report.runReport(params);
	}

}
