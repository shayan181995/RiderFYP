package com.example.hp.RiderFYP;

import java.util.Date;

/**
 * Created by HP on 7/1/2017.
 */

public class RequestRide {
    public String RiderID;
    public double StartLat;
    public double StartLng;
    public double EndLat;
    public double EndLng;
    public double Distance;
    public Date StartTime;
    public Date EndTime;
    public String Status;

    public RequestRide(){

    }

    public RequestRide(String riderID, double startLat, double startLng, double endLat, double endLng, double distance, Date startTime, Date endTime, String status) {
        RiderID = riderID;
        StartLat = startLat;
        StartLng = startLng;
        EndLat = endLat;
        EndLng = endLng;
        Distance = distance;
        StartTime = startTime;
        EndTime = endTime;
        Status = status;
    }
}
