package com.dataflair.carpooling.Model;

public class EmergencyContact {

    private int id;
    private String phoneNumber;
    private String relationship;

    public EmergencyContact() { }

    public EmergencyContact(int id, String phoneNumber, String relationship) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
    }

    public EmergencyContact(String phoneNumber, String relationship) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
