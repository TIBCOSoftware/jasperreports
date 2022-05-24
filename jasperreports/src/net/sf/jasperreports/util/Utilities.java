package net.sf.jasperreports.util;

import java.util.Map;

import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class Utilities extends JRDefaultScriptlet {

    public boolean getRequiredStringBooleanMapValue(Map<String, Boolean> map, String mapKey) {
        return map.computeIfAbsent(mapKey, s -> {
            throw missingKeyException(mapKey);
        });
    }

    public String getRequiredStringStringMapValue(Map<String, String> map, String mapKey) {
        return map.computeIfAbsent(mapKey, s -> {
            throw missingKeyException(mapKey);
        });
    }

    private RuntimeException missingKeyException(String key) {
        return new RuntimeException("Missing entry in map for required key: \"" + key + "\"");
    }
}
