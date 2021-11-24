package net.sf.jasperreports.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to gather page numbers during report fill time. Each page number is associated to a String key.
 * During collection, key/page number pairs are added. When finished, the TOC data are returned as a JRDataSource.
 */
public class TocCreator {

    private static final String TOC_KEY = "tocKey";
    private static final String PAGE_NUMBER = "pageNumber";

    private final List<Map<String, Object>> tocEntries = new ArrayList<>();

    /**
     * Returns the page number for the given key.
     * @param tocKey
     * @return
     */
    public Integer getPageNumberForTocKey(String tocKey) {
        for (Map<String, Object> tocEntry : this.tocEntries) {
            if (tocKey.equals(tocEntry.get(TOC_KEY))) {
                return (Integer) tocEntry.get(PAGE_NUMBER);
            }
        }
        return null;
    }

    /**
     * Sets the page number for the given key.
     * @param tocKey A key that uniquely identifies an anchor name of a report field.
     * @param pageNumber An Integer value, holding the page number.
     * @return
     */
    public String setPageNumberForTocKey(String tocKey, Integer pageNumber) {
        tocEntries.add(createTocEntry(tocKey, pageNumber));
        return tocKey;
    }

    private Map<String, Object> createTocEntry(String tocKey, Integer pageNumber) {
        Map<String, Object> tocEntry = new HashMap<>();
        tocEntry.put(TOC_KEY, tocKey);
        tocEntry.put(PAGE_NUMBER, pageNumber);
        return tocEntry;
    }
}
