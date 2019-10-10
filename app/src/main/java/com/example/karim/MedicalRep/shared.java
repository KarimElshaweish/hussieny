package com.example.karim.MedicalRep;


import com.example.karim.MedicalRep.model.Order;

public class shared {
    public static String phoneNumber;
    public static String userName;
    public static Boolean admin=false;
    public static Order order;
    public static void reset(){
        admin=false;
    }
}
