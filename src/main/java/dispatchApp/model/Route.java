package dispatchApp.model;

import lombok.Builder;
import lombok.Data;
import com.google.maps.model.LatLng;
import java.util.List;


@Builder
public @Data class Route {
    private int id;
    private String startAddress;
    private String endAddress;
    private double duration;//hour
    private double distance;//mile
    private List<LatLng> points;
}
