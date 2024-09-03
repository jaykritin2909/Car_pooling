package com.dataflair.carpooling.Model;

public class Rides {

    private String name;
    private String source;
    private String dest;
    private String total;
    private String date;
    private String time;
    private String price;
    private String phone;
    private String aadhar;
    private String via;



     Rides() {
        // Default constructor required for calls to DataSnapshot.getValue(Rides.class)
    }


    public Rides(String name, String source, String dest, String total, String date, String time, String price, String phone, String aadhar, String via) {
        this.name = name;
        this.source = source;
        this.dest = dest;
        this.total = total;
        this.date = date;
        this.time = time;
        this.price = price;
        this.phone = phone;
        this.aadhar = aadhar;
        this.via = via;

    }

    public void setsource(String source) {
        this.source = source;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public  String getName() {
        return name;
    }

    public  String getSource() {
        return source;
    }

    public  String getDest() {
        return dest;
    }

    public  String getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public  String getTime() {
        return time;
    }

    public  String getPrice() {
        return price;
    }

    public  String getPhone() {
        return phone;
    }

    public  String getAadhar() {
        return aadhar;
    }

    public  String getVia() {
        return via;
    }
}
