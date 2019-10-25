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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.protocol.commands.Page;
import com.github.kklisura.cdt.protocol.commands.Runtime;
import com.github.kklisura.cdt.protocol.types.page.CaptureScreenshotFormat;
import com.github.kklisura.cdt.protocol.types.page.Viewport;
import com.github.kklisura.cdt.protocol.types.runtime.AwaitPromise;
import com.github.kklisura.cdt.protocol.types.runtime.Evaluate;
import com.github.kklisura.cdt.protocol.types.runtime.RemoteObject;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;

import net.sf.jasperreports.chrome.Chrome;
import net.sf.jasperreports.customvisualization.CVPrintElement;
import net.sf.jasperreports.customvisualization.CVUtils;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.phantomjs.ScriptManager;
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
		JasperReportsContext jasperReportsContext, 
		JRGenericPrintElement element) throws Exception {

		if (element.getParameterValue(CVPrintElement.CONFIGURATION) == null) {
			throw new JRRuntimeException("Configuration object is null.");
		}

		Chrome chrome = Chrome.instance(jasperReportsContext);
		ScriptManager scriptManager = chrome.getScriptManager();
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

		String htmlPage = getHtmlPage(
				jasperReportsContext,
				element,
				scriptFilenames,
				cssUri);

		File htmlTempFile = File.createTempFile("cv_", ".html", scriptManager.getTempFolder());
		try {
			try (InputStream is = new ByteArrayInputStream(htmlPage.getBytes());
					 OutputStream os = new FileOutputStream(htmlTempFile) ) {
					CVUtils.byteStreamCopy(is, os);
				}
				if (log.isDebugEnabled()) {
					log.debug("wrote CV render HTML page to " + htmlTempFile);
				}

				boolean renderAsPng = CVUtils.isRenderAsPng(element);
				
				ChromeService chromeService = chrome.getService().getChromeService();
				ChromeTab tab = null;
				try {
					tab = chromeService.createTab();
					ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);
					
					Page page = devToolsService.getPage();
					Runtime runtime = devToolsService.getRuntime();

					CompletableFuture<Object> resultFuture = new CompletableFuture<Object>();
					
					page.onLoadEventFired(event -> {
						Evaluate evaluate = runtime.evaluate("renderResult(" + (!renderAsPng) + ")");
						RemoteObject result = evaluate.getResult();
						
						AwaitPromise promise = runtime.awaitPromise(result.getObjectId(), true, false);
						RemoteObject pResult = promise.getResult();
						Object resultValue = pResult.getValue();
						resultFuture.complete(resultValue);
					});

					page.enable();
					page.navigate(htmlTempFile.toURI().toString());
					
					Object resultValue = resultFuture.get();
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
							
							Viewport viewport = new Viewport();
							viewport.setX(0d);
							viewport.setY(0d);
							viewport.setWidth((double) width);
							viewport.setHeight((double) height);
							viewport.setScale((double) zoomFactor);
							
							String screenshotString = page.captureScreenshot(CaptureScreenshotFormat.PNG, 100,
									viewport, true);
							imageData = Base64Util.decode(screenshotString);
						} else {
							imageData = ((String) resultValue).getBytes(StandardCharsets.UTF_8);
						}
					}
					return imageData;
				} catch (InterruptedException | ExecutionException e) {
					throw new JRRuntimeException(e);
				} finally {
					if (tab != null) {
						chromeService.closeTab(tab);
					}
				}
		} finally {
			// Remove the temporary component HTML file
			htmlTempFile.delete();
		}
	}
}
