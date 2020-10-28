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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.chrome.BrowserService;
import net.sf.jasperreports.chrome.Chrome;
import net.sf.jasperreports.chrome.PageOptions;
import net.sf.jasperreports.chrome.ResourceManager;
import net.sf.jasperreports.customvisualization.CVPrintElement;
import net.sf.jasperreports.customvisualization.CVUtils;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.repo.RepositoryContext;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.util.Base64Util;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ChromeCVElementImageDataProvider extends CVElementAbstractImageDataProvider {
	private static final Log log = LogFactory.getLog(ChromeCVElementImageDataProvider.class);

	private final String[] scriptResourceLocations = new String[] {
			"net/sf/jasperreports/customvisualization/resources/require/require.js",
			"net/sf/jasperreports/customvisualization/resources/require/cv-component_static.js",
			"net/sf/jasperreports/customvisualization/scripts/customvisualization-chrome.js"
	};

	public boolean isEnabled(JasperReportsContext jasperReportsContext) {
		Chrome chrome = Chrome.instance(jasperReportsContext);
		return chrome.isEnabled();
	}

	@Override
	public byte[] getImageData(
		RepositoryContext repositoryContext, 
		JRGenericPrintElement element) throws Exception {

		if (element.getParameterValue(CVPrintElement.CONFIGURATION) == null) {
			throw new JRRuntimeException("Configuration object is null.");
		}
		
		JasperReportsContext jasperReportsContext = repositoryContext.getJasperReportsContext();
		Chrome chrome = Chrome.instance(jasperReportsContext);
		ResourceManager resourceManager = ResourceManager.instance();
		List<String> scriptFilenames = new ArrayList<>();

		for (String scriptLocation: scriptResourceLocations) {
			scriptFilenames.add(resourceManager.getResourceLocation(scriptLocation, repositoryContext));
		}

		if (log.isDebugEnabled()) {
			log.debug("Configured script URI: " + element.getParameterValue(CVPrintElement.SCRIPT_URI));
			log.debug("Configured css URI: " + element.getParameterValue(CVPrintElement.CSS_URI));
		}

		scriptFilenames.add(resourceManager.getResourceLocation(
				(String)element.getParameterValue(CVPrintElement.SCRIPT_URI),
				repositoryContext));

		boolean renderAsPng = CVUtils.isRenderAsPng(element);

		String cssUriParameter = (String)element.getParameterValue(CVPrintElement.CSS_URI);
		String cssUri = null;
		if (cssUriParameter != null) {
			String cssResourceLocation = resourceManager.getResourceLocation(cssUriParameter, repositoryContext);
			if (renderAsPng) {
				cssUri = cssResourceLocation;
			} else {
				//embedding the CSS as data: URL in the HTML because Chrome doesn't allow 
				//accessing document.styleSheets.cssRules for file system CSS resources
				byte[] cssBytes = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(cssResourceLocation);
				//TODO lucian cache
				String cssBase64 = Base64Util.encode(cssBytes);
				cssUri = "data:text/css;base64," + cssBase64;
			}
		}

		String htmlPage = getHtmlPage(
				jasperReportsContext,
				element,
				scriptFilenames,
				cssUri);

		File htmlTempFile = File.createTempFile("cv_", ".html", resourceManager.getTempFolder(jasperReportsContext));
		try {
			try (InputStream is = new ByteArrayInputStream(htmlPage.getBytes());
					 OutputStream os = new FileOutputStream(htmlTempFile) ) {
					CVUtils.byteStreamCopy(is, os);
				}
				if (log.isDebugEnabled()) {
					log.debug("wrote CV render HTML page to " + htmlTempFile);
				}
				
				PageOptions options = new PageOptions();
				options.setTimeout(CVUtils.getOwnTimeout(element));
				
				BrowserService service = chrome.getService();
				byte[] data = service.evaluateInPage(htmlTempFile.toURI().toString(), options, page -> {
					Object resultValue = page.evaluatePromise("renderResult(" + (!renderAsPng) + ")");
					if (log.isTraceEnabled()) {
						log.trace("got result " + resultValue);
					}
					
					byte[] imageData = null;
					if (resultValue != null) {
						if (renderAsPng) {
							@SuppressWarnings("unchecked")
							Map<String, Object> resultMap = (Map<String, Object>) resultValue;
							int width = (int) resultMap.get("w");
							int height = (int) resultMap.get("h");
							float zoomFactor = CVUtils.getZoomFactor(element);
							
							imageData = page.captureScreenshot(width, height, zoomFactor);
						} else {
							imageData = ((String) resultValue).getBytes(StandardCharsets.UTF_8);
						}
					}
					return imageData;					
				});
				return data;
		} finally {
			// Remove the temporary component HTML file
			htmlTempFile.delete();
		}
	}
}
