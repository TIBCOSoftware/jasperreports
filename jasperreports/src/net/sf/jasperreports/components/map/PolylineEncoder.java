package net.sf.jasperreports.components.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Encodes paths into Google static maps API encoded polylines to facilitate shorter URLs.
 * See https://developers.google.com/maps/documentation/utilities/polylinealgorithm.
 * Based on the C# implementation at https://gist.github.com/shinyzhu/4617989
 */
public class PolylineEncoder {

    private static class LatLng implements Cloneable {
        double lat;
        double lng;

        LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public LatLng clone() {
            try {
                return (LatLng) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    private final List<LatLng> locations;

    public PolylineEncoder(List<Map<String, Object>> locations, boolean isPolygon) {
        this.locations = transformLocations(locations, isPolygon);
    }

    public String encode() {
        StringBuilder str = new StringBuilder();

        Consumer<Integer> encodeDiff = (diff -> {
            int shifted = diff << 1;
            if (diff < 0) {
                shifted = ~shifted;
            }
            int rem = shifted;

            while (rem >= 0x20) {
                str.append((char) ((0x20 | (rem & 0x1f)) + 63));

                rem >>= 5;
            }

            str.append((char) (rem + 63));
        });

        int lastLat = 0;
        int lastLng = 0;

        for (LatLng location : locations) {
            int lat = (int) Math.round(location.lat * 1E5);
            int lng = (int) Math.round(location.lng * 1E5);

            encodeDiff.accept(lat - lastLat);
            encodeDiff.accept(lng - lastLng);

            lastLat = lat;
            lastLng = lng;
        }

        return str.toString();
    }

    private static List<LatLng> transformLocations(List<Map<String, Object>> locations, boolean isPolygon) {

        List<LatLng> latLngs = new ArrayList<>();
        if (locations == null || locations.isEmpty()) {
            return latLngs;
        }

        Object sample = locations.get(0).get(MapComponent.ITEM_PROPERTY_latitude);
        if (sample instanceof Double) {
            latLngs = locations.stream().map(PolylineEncoder::transformLocationFromDouble).collect(Collectors.toList());
        } else if (sample instanceof Float) {
            latLngs = locations.stream().map(PolylineEncoder::transformLocationFromFloat).collect(Collectors.toList());
        } else if (sample instanceof String) {
            latLngs = locations.stream().map(PolylineEncoder::transformLocationFromString).collect(Collectors.toList());
        } else {
            throw new RuntimeException("Unhandled type for location coordinates: " + sample.getClass().getName());
        }

        if (isPolygon && !latLngs.isEmpty()) {
            latLngs.add(latLngs.get(0).clone());
        }

        return latLngs;
    }

    private static LatLng transformLocationFromDouble(Map<String, Object> location) {
        return new LatLng((Double) location.get(MapComponent.ITEM_PROPERTY_latitude),
                (Double) location.get(MapComponent.ITEM_PROPERTY_longitude));
    }

    private static LatLng transformLocationFromFloat(Map<String, Object> location) {
        return new LatLng(Double.valueOf((Float) location.get(MapComponent.ITEM_PROPERTY_latitude)),
                Double.valueOf((Float) location.get(MapComponent.ITEM_PROPERTY_longitude)));
    }

    private static LatLng transformLocationFromString(Map<String, Object> location) {
        return new LatLng(Double.parseDouble((String) location.get(MapComponent.ITEM_PROPERTY_latitude)),
                Double.parseDouble((String) location.get(MapComponent.ITEM_PROPERTY_longitude)));
    }
}
