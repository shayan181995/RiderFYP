package com.example.hp.RiderFYP;

import java.util.Date;

/**
 * Created by HP on 7/7/2017.
 */

public class Trip {
    public double StartLat;
    public double StartLng;
    public double EndLat;
    public double EndLng;
    public double Distance;
    public Date StartTime;
    public Date EndTime;
    public double Fare;

    public Trip(){

    }

    public Trip(double startLat, double startLng, double endLat, double endLng, double distance, Date startTime, Date endTime,double fare) {
        StartLat = startLat;
        StartLng = startLng;
        EndLat = endLat;
        EndLng = endLng;
        Distance = distance;
        StartTime = startTime;
        EndTime = endTime;
        Fare = fare;
    }
}
