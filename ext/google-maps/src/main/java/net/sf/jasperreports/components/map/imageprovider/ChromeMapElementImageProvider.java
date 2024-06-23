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
package net.sf.jasperreports.components.map.imageprovider;

import net.sf.jasperreports.chrome.BrowserService;
import net.sf.jasperreports.chrome.Chrome;
import net.sf.jasperreports.chrome.ResourceManager;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.MapUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.SimpleDataRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;
import net.sf.jasperreports.velocity.util.VelocityUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class ChromeMapElementImageProvider extends AbstractMapElementImageProvider {

    private static final Log log = LogFactory.getLog(ChromeMapElementImageProvider.class);
    private static final String PAGE_TEMPLATE_RESOURCE = "net/sf/jasperreports/components/map/resources/templates/ChromeMapPage.vm";

    private Boolean isEnabled = null;

    public boolean isEnabled(JasperReportsContext jasperReportsContext) {
        if (isEnabled == null) {
            Chrome chrome = Chrome.instance(jasperReportsContext);
            isEnabled = chrome.isEnabled();
        }
        return isEnabled;
    }

    @Override
    protected Renderable createRenderable(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) throws JRException {
        Renderable cachedRenderable = (Renderable) element.getParameterValue(MapComponent.PARAMETER_CACHE_RENDERER);

        if (cachedRenderable == null) {
            try {
                byte[] imageData = getImageData(jasperReportsContext, element);
                cachedRenderable = new SimpleDataRenderer(imageData, null);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Generating Chrome image for Map element failed!", e);
                }

                OnErrorTypeEnum onErrorType =
                        element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE) == null
                                ? MapComponent.DEFAULT_ON_ERROR_TYPE
                                : OnErrorTypeEnum.getByName((String)element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE));

                cachedRenderable = RendererUtil.getInstance(jasperReportsContext).handleImageError(e, onErrorType);
            }

            element.setParameterValue(MapComponent.PARAMETER_CACHE_RENDERER, cachedRenderable);
        }

        return cachedRenderable;
    }

    public byte[] getImageData(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) throws JRException {
        ResourceManager resourceManager = ResourceManager.instance();
        File tempFolder = resourceManager.getTempFolder(jasperReportsContext);

        File mapPageTempFile = null;
        try {
            mapPageTempFile = File.createTempFile("map_", ".html", tempFolder);

            String mapPage = getMapHtmlPage(jasperReportsContext, element);
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(mapPageTempFile), StandardCharsets.UTF_8)) {
                writer.write(mapPage);
            }

            Chrome chrome = Chrome.instance(jasperReportsContext);
            BrowserService service = chrome.getService();
            byte[] data = service.evaluateInPage(mapPageTempFile.toURI().toString(), null , page -> {
                Object resultValue = page.evaluatePromise("renderResult()");
                if (log.isTraceEnabled()) {
                    log.trace("Got result from evaluating map page promise: " + resultValue);
                }

                byte[] imageData = null;
                if (resultValue != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> resultMap = (Map<String, Object>) resultValue;
                    int width = (int) resultMap.get("width");
                    int height = (int) resultMap.get("height");
                    float zoomFactor = 1.0f;

                    imageData = page.captureScreenshot(width, height, zoomFactor);
                }
                return imageData;
            });
            return data;

        } catch (IOException e) {
            throw new JRRuntimeException("Failed to create map page in temp folder " + tempFolder, e);
        } finally {
            if (mapPageTempFile != null) {
                boolean deleted = mapPageTempFile.delete();
                if (!deleted) {
                    if (log.isWarnEnabled()) {
                        log.warn("Failed to delete map temp page " + mapPageTempFile);
                    }
                }
            }
        }
    }

    public String getMapHtmlPage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) {
        Map<String, Object> velocityContext = new HashMap<>();

        velocityContext.put("elementWidth", element.getWidth());
        velocityContext.put("elementHeight", element.getHeight());

        if (element.getMode() == ModeEnum.OPAQUE) {
            velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
        }

        velocityContext.put("jasperreportsMapApiScript", VelocityUtil.processTemplate(MapUtils.MAP_API_SCRIPT, (VelocityContext) null));
        MapUtils.prepareContextForVelocityTemplate(velocityContext, jasperReportsContext, element);

        return VelocityUtil.processTemplate(PAGE_TEMPLATE_RESOURCE, velocityContext);
    }
}
