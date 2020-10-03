package dispatchApp.external;

import com.google.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.oracle.javafx.jmx.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dispatchApp.utils.Constants;


public class DirectionsResponse {

    private final static int hourToSecond = 3600;
    private final static int mileToMeter = 1609;

    private JsonElement jsonElement;
    private JsonObject jsonObject;
    private JsonObject jsonObjectResult;
    private double startLat;
    private double endLat;
    private double startLng;
    private double endLng;
    public DirectionsResponse(String json) {
        jsonElement = new JsonParser().parse(json);
        jsonObject = jsonElement.getAsJsonObject();

        // Get leg. There is no Waypoint so we just have one leg here
        jsonObjectResult = jsonObject.getAsJsonArray("routes")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("legs")
                .get(0).getAsJsonObject();

        startLat = Double.parseDouble(jsonObjectResult.getAsJsonObject("start_location").get("lat").getAsString());
        startLng = Double.parseDouble(jsonObjectResult.getAsJsonObject("start_location").get("lng").getAsString());
        endLat = Double.parseDouble(jsonObjectResult.getAsJsonObject("end_location").get("lat").getAsString());
        endLng = Double.parseDouble(jsonObjectResult.getAsJsonObject("end_location").get("lng").getAsString());
    }

    // Method for getting start_address
    public String getStartAddress() {
        String start_address = jsonObjectResult.get("start_address").getAsString();
        return start_address;
    }

    // Method for getting end_address
    public String getEndAddress() {
        String end_address = jsonObjectResult.get("end_address").getAsString();
        return end_address;
    }

    // Method for getting robotDuration (second)
    public double getRobotDuration() {
        String duration = jsonObjectResult.getAsJsonObject("duration").get("value").getAsString();
        double durationValue = Double.parseDouble(duration) / hourToSecond;
        return durationValue;
    }

    // Method for getting robotDistance (mile)
    public double getRobotDistance() {
        String robotDistance = jsonObjectResult.getAsJsonObject("distance").get("value").getAsString();
        double robotDistanceValue = Double.parseDouble(robotDistance) / mileToMeter;
        return robotDistanceValue;
    }

    // Method for getting droneDuration (hour)
    public double getDroneDuration() {
        double droneDistanceValue = getDroneDistance();
        return droneDistanceValue / Constants.DEFAULT_DRONE_SPEED;
    }

    // Method for getting Approximate droneDistance (mile)
    public double getDroneDistance() {

        LatLng a = new LatLng(startLat, startLng);
        LatLng b = new LatLng(endLat, endLng);

        if ((a.lat == b.lat) && (a.lng == b.lng)) {
            return 0;
        } else {
            double theta = a.lng - b.lng;
            double dist = Math.sin(Math.toRadians(a.lat)) * Math.sin(Math.toRadians(b.lat))
                    + Math.cos(Math.toRadians(a.lat)) * Math.cos(Math.toRadians(b.lat)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return dist;
        }
    }
    public List<LatLng> getDronePoints(){

        LatLng a = new LatLng(startLat, startLng);
        LatLng b = new LatLng(endLat, endLng);

        List<LatLng> list = Arrays.asList(new LatLng[]{a, b});
        return list;
    }

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<LatLng> getRobotPoints(){
        int SAMPLE_INTERVAL = 1; // Sample a point every 100 points.

        // routes result
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>() ;

        // Sampling points on a route btw source and dest.
        List<LatLng> points = new ArrayList<>();

        JsonArray jRoutes = null;
        JsonArray jLegs = null;
        JsonArray jSteps = null;

        try {

            // jRoutes = jObject.getJSONArray("routes");
            jRoutes = jsonObject.getAsJsonArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.size(); i++){
                jLegs = ((JsonObject)jRoutes.get(i)).getAsJsonArray("legs");
                List<HashMap<String, String>> path = new ArrayList<>();

                /** Traversing all legs */
                for (int j = 0; j<jLegs.size(); j++){
                    jSteps = ((JsonObject)jLegs.get(j)).getAsJsonArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.size(); k++){
                        String polyline = "";
                        //polyline = (String)(((JsonObject)((JsonObject)jSteps.get(k)).get("polyline")).get("points"));
                        polyline = ((JsonObject)(((JsonObject)jSteps.get(k)).get("polyline"))).get("points").getAsString();

                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).lat) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).lng) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

            for (int i = 0; i < routes.size(); i++) {

                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    // 13.7 mile has 863 points. 100 points represents for 1.58 mile.
                    if (j % SAMPLE_INTERVAL == 0) { // sample at every 1.58 mile
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                }
            }

        } catch (Exception e){
        }

        return points;
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}