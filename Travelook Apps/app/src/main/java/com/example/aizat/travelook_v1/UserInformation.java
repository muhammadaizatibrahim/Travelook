package com.example.aizat.travelook_v1;

public class UserInformation {

    public String name;
    public String address;
    public String phoneNumber;
    public String image;

    public UserInformation(){

    }

    public UserInformation(String name, String address, String phoneNumber, String image){
        this.name=name;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
