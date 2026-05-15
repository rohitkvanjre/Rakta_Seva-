package com.example.rakta_seva.utils;

public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    // Default logged-in user's location: Sector 12, New Delhi
    public static final double USER_LAT = 28.630;
    public static final double USER_LNG = 77.215;

    /**
     * Calculates distance between two points using the Haversine formula.
     * @return distance in kilometers
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Calculates distance from logged-in user's dummy location to a given point.
     */
    public static double distanceFromUser(double lat, double lon) {
        return calculateDistance(USER_LAT, USER_LNG, lat, lon);
    }

    /**
     * Formats distance to a readable string with one decimal place.
     */
    public static String formatDistance(double distanceKm) {
        return String.format("%.1f km", distanceKm);
    }
}
