package com.example.hp.RiderFYP;

import java.util.Date;

/**
 * Created by HP on 7/1/2017.
 */

public class OwnerRide {
    public String CarID;
    public double StartLat;
    public double StartLng;
    public double EndLat;
    public double EndLng;
    public double Distance;
    public double TotalFare;
    public Date StartTime;
    public Date EndTime;
    public String Status;
    public String PushKey;

    public OwnerRide(){

    }

    public OwnerRide(String carID, double startLat, double startLng, double endLat, double endLng, double distance, double totalFare, Date startTime, Date endTime, String status,String pushkey) {
        CarID = carID;
        StartLat = startLat;
        StartLng = startLng;
        EndLat = endLat;
        EndLng = endLng;
        Distance = distance;
        TotalFare = totalFare;
        StartTime = startTime;
        EndTime = endTime;
        Status = status;
        PushKey = pushkey;
    }
}
