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
package net.sf.jasperreports.customvisualization.export;

import net.sf.jasperreports.customvisualization.CVPrintElement;
import net.sf.jasperreports.customvisualization.CVUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.phantomjs.PhantomJS;
import net.sf.jasperreports.phantomjs.ScriptManager;
import net.sf.jasperreports.repo.RepositoryContext;
import net.sf.jasperreports.util.Base64Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class JRPhantomCVElementImageDataProvider extends CVElementAbstractImageDataProvider {
	private static final Log log = LogFactory.getLog(JRPhantomCVElementImageDataProvider.class);

	private final String[] scriptResourceLocations = new String[] {
			"net/sf/jasperreports/customvisualization/scripts/customvisualization-phantom.js",
			"net/sf/jasperreports/customvisualization/resources/require/require.js",
			"net/sf/jasperreports/customvisualization/resources/require/cv-component_static.js"
	};

	public boolean isEnabled() {
		return PhantomJS.isEnabled();
	}

	@Override
	public byte[] getImageData(
		RepositoryContext repositoryContext, 
		JRGenericPrintElement element) throws Exception {

		if (element.getParameterValue(CVPrintElement.CONFIGURATION) == null) {
			throw new JRRuntimeException("Configuration object is null.");
		}

		JasperReportsContext jasperReportsContext = repositoryContext.getJasperReportsContext();
		PhantomJS phantom = PhantomJS.instance();
		ScriptManager scriptManager = phantom.getScriptManager();
		List<String> scriptFilenames = new ArrayList<>();

		for (String scriptLocation: scriptResourceLocations) {
			scriptFilenames.add(scriptManager.getScriptFilename(scriptLocation, jasperReportsContext));
		}

		if (log.isDebugEnabled()) {
			log.debug("Configured script URI: " + element.getParameterValue(CVPrintElement.SCRIPT_URI));
			log.debug("Configured css URI: " + element.getParameterValue(CVPrintElement.CSS_URI));
		}

		scriptFilenames.add(scriptManager.getScriptFilename(
				(String)element.getParameterValue(CVPrintElement.SCRIPT_URI),
				jasperReportsContext));

		String cssUriParameter = (String)element.getParameterValue(CVPrintElement.CSS_URI);
		String cssUri = null;
		if (cssUriParameter != null) {
			cssUri= scriptManager.getScriptFilename(cssUriParameter, jasperReportsContext);
		}

		StringBuilder script = new StringBuilder();
		script.append("{\"script\": \"" + JRStringUtil.escapeJSONString(scriptFilenames.get(0)) + "\", \"data\": {");

		String htmlPage = getHtmlPage(
				jasperReportsContext,
				element,
				scriptFilenames.subList(1, scriptFilenames.size()),
				cssUri);

		File htmlTempFile = File.createTempFile("cv_", ".html", scriptManager.getTempFolder());

		try (InputStream is = new ByteArrayInputStream(htmlPage.getBytes());
			 OutputStream os = new FileOutputStream(htmlTempFile) ) {

			CVUtils.byteStreamCopy(is, os);
		}

		script.append("\"componentHtmlFile\": \"" + htmlTempFile.getName() + "\",");

		boolean renderAsPng = CVUtils.isRenderAsPng(element);
		script.append("\"outputFormat\": \"" + (renderAsPng ? "png" : "svg") + "\",");

		script.append("\"zoomFactor\": " + CVUtils.getZoomFactor(element) + "}}");

		String requestString = script.toString();
		if (log.isTraceEnabled()) {
			log.trace("Sending request: \n" + requestString);
		}

		String requestOutput = phantom.runRequest(requestString);

		// Remove the temporary component HTML file
		htmlTempFile.delete();

		if (log.isTraceEnabled()) {
			if (requestOutput == null || requestOutput.length() == 0) {
				log.trace("Got null or empty request output!");
			} else {
				log.trace("Got output:\n" + requestOutput);
			}
		}

		if (requestOutput != null) {
			if (renderAsPng) {
				try(ByteArrayInputStream is = new ByteArrayInputStream(requestOutput.getBytes());
					ByteArrayOutputStream os = new ByteArrayOutputStream()) {

					Base64Util.decode(is, os);
					return os.toByteArray();
				}
			}

			return requestOutput.getBytes("UTF-8");
		}

		return null;
	}
}
