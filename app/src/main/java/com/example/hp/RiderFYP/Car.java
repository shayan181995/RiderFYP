package com.example.hp.RiderFYP;

/**
 * Created by HP on 6/29/2017.
 */

public class Car {
    public String OwnerID;
    public int Category;
    public String CarMake;
    public int CarModel;
    public int CarCapacity;

    public Car(){

    }

    public Car(String ownerID, int category, String carMake, int carModel, int carCapacity) {
        OwnerID = ownerID;
        Category = category;
        CarMake = carMake;
        CarModel = carModel;
        CarCapacity = carCapacity;
    }
}
