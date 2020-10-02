package dispatchApp.model;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data
class UserOption {
    private int id;
    private String startAddress;
    private String endAddress;
    private int carrierId; // 自己分配
    private String carrierType;
    private int stationId;

    private String startTime;
    private String departureTime;
    private String deliveryTime;
    private String endTime;
    private float weight;
    private float fee;


    private Option option;
    private Route stationToStart;
    private Route startToEnd;
    private Route endToStation;
}
