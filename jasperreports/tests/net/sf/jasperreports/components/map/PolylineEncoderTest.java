package net.sf.jasperreports.components.map;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PolylineEncoderTest {

    /* Example from google:
     * const path = [
     *   [38.5, -120.2],
     *   [40.7, -120.95],
     *   [43.252, -126.453],
     * ];
     * console.log(encode(path, 5));
     * // "_p~iF~ps|U_ulLnnqC_mqNvxq`@"
     */
    @Test
    public void testEncodeNonPolygon() {

        List<Map<String, Object>> locations = new ArrayList<>();
        locations.add(createTestLocation(38.5, -120.2));
        locations.add(createTestLocation(40.7, -120.95));
        locations.add(createTestLocation(43.252, -126.453));

        String enc = new PolylineEncoder(locations, false).encode();

        final String expected = "_p~iF~ps|U_ulLnnqC_mqNvxq`@";

        assertEquals(expected, enc);
    }

    @Test
    public void testEncodePolygon() {

        List<Map<String, Object>> locations = new ArrayList<>();
        locations.add(createTestLocation(38.5, -120.2));
        locations.add(createTestLocation(40.7, -120.95));
        locations.add(createTestLocation(43.252, -126.453));

        String enc = new PolylineEncoder(locations, true).encode();

        final String expected = "_p~iF~ps|U_ulLnnqC_mqNvxq`@~b_\\ghde@";

        assertEquals(expected, enc);
    }

    private static Map<String, Object> createTestLocation(double lat, double lng) {
        Map<String, Object> location = new HashMap<>();

        location.put(MapComponent.ITEM_PROPERTY_latitude, lat);
        location.put(MapComponent.ITEM_PROPERTY_longitude, lng);

        return location;
    }
}