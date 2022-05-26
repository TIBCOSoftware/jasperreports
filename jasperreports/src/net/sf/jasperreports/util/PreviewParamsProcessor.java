package net.sf.jasperreports.util;

import java.util.Map;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.fill.JRFillParameter;

public class PreviewParamsProcessor extends JRDefaultScriptlet {

    private static final String PREVIEW_PARAMS_FIELD_NAME = "previewParams";

    @Override
    @SuppressWarnings("unchecked")
    public void afterReportInit() throws JRScriptletException {
        super.afterReportInit();

        Map<String, Object> previewParams;

        try {
            previewParams = (Map<String, Object>) getFieldValue(PREVIEW_PARAMS_FIELD_NAME);
        } catch (ClassCastException e) {
            throw new RuntimeException(
                    "Unable to cast field " + PREVIEW_PARAMS_FIELD_NAME + " as Map<String, Object>", e);
        }

        if (previewParams != null) {
            loadPreviewParams(previewParams);
        }
    }

    private void loadPreviewParams(Map<String, Object> previewParams) {

        for (Map.Entry<String, JRFillParameter> entry : parametersMap.entrySet()) {
            if (!previewParams.containsKey(entry.getKey())) {
                continue;
            }
            entry.getValue().setValue(previewParams.get(entry.getKey()));
        }
    }
}
