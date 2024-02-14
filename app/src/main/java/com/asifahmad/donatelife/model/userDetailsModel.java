package com.asifahmad.donatelife.model;

public class userDetailsModel {

    String Name, Email, BloodType, DOB, Gender, PhoneNumber, Disease;

    public userDetailsModel() {
    }

    public userDetailsModel(String name, String email, String bloodType, String DOB, String gender, String phoneNumber, String disease) {
        Name = name;
        Email = email;
        BloodType = bloodType;
        this.DOB = DOB;
        Gender = gender;
        PhoneNumber  = phoneNumber;

        Disease = disease;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBloodType() {
        return BloodType;
    }

    public void setBloodType(String bloodType) {
        BloodType = bloodType;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }
}
