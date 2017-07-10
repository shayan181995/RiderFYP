package com.example.hp.RiderFYP;

/**
 * Created by HP on 7/7/2017.
 */

public class TripObj {
    public int CapacityLeft;
    public double TotalDistance;
    public String Status;
    public int NoOfRiders;

    public TripObj(){

    }

    public TripObj(int capacityLeft, double totalDistance, String status, int noOfRiders) {
        CapacityLeft = capacityLeft;
        TotalDistance = totalDistance;
        Status = status;
        NoOfRiders = noOfRiders;
    }
}
