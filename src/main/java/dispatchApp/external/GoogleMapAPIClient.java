package dispatchApp.external;

import dispatchApp.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class GoogleMapAPIClient {

    private final String apiKey = "AIzaSyBjnVnvNgiL3PpPS5cNDghW85fqQM8jbsk";

    @Autowired
    private RestTemplate restTemplate;

    private DirectionsResponse getMapResponse(String start, String end) {
        restTemplate = new RestTemplate();
        //from station to pickup location
        String response = restTemplate.getForObject(
                "https://maps.googleapis.com/maps/api/directions/json?"
                        + "origin=" + start
                        + "&destination=" + end
                        //+ "&waypoints=" + xxx
                        + "&key=" + apiKey,
                String.class
        );


        DirectionsResponse directionsResponse = new DirectionsResponse(response);

        return directionsResponse;
    }

    public Route getRobotRoute(String start, String end) {
        DirectionsResponse directionsResponse = getMapResponse(start, end);
        Route result = Route.builder()
                .startAddress(start)
                .endAddress(end)
                .distance(directionsResponse.getRobotDistance())
                .duration(directionsResponse.getRobotDuration())
                .points(directionsResponse.getRobotPoints())
                .build();
        return result;
    }

    public Route getDroneRoute(String start, String end) {
        DirectionsResponse directionsResponse = getMapResponse(start, end);
        Route result = Route.builder()
                .startAddress(start)
                .endAddress(end)
                .distance(directionsResponse.getDroneDistance())
                .duration(directionsResponse.getDroneDuration())
                .points(directionsResponse.getDronePoints())
                .build();
        return result;
    }

}