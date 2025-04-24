package ru.practicum.event.repository;

public class DistanceFunction {
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double dist = 0;
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double theta = lon1 - lon2;
        double radTheta = Math.toRadians(theta);

        dist = Math.sin(radLat1) * Math.sin(radLat2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radTheta);

        if (dist > 1) {
            dist = 1;
        }

        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.8524;

        return dist;
    }
}
