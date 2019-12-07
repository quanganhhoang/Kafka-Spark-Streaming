package model;

import lombok.Value;

@Value
public class LiftRide {
    int resortId;
    String seasonId;
    String dayId;
    int skierId;
    short liftId;
    short time;
    int verticalDistance;

}
