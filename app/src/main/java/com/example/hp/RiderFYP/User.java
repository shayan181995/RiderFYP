package com.example.hp.RiderFYP;

/**
 * Created by HP on 5/27/2017.
 */

public class User {


    public String Userid;
    public String email;
    public String name;
    public String country;
    public String city;
    public long phone;
    public String UserType;
    public String gender;
    public int PaymentMode;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String id, String email, String name, String country, String city, long phone, String userType, String gender, int paymentMode) {

        this.Userid = id;
        this.email = email;
        this.name = name;
        this.country = country;
        this.city = city;
        this.phone = phone;
        this.UserType = userType;
        this.gender = gender;
        this.PaymentMode = paymentMode;
    }
}
