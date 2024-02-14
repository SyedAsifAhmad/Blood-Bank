package com.asifahmad.donatelife.model;

public class model {

    String Name, Email, PhoneNo,BloodType,DOB,Disease,Gender, userId;

    public model() {
    }

    public model(String name, String email, String phoneNo,String bloodType,String DOB,String Disease, String Gender, String userId) {
        this.Name = name;
        this.Email = email;
        this.PhoneNo = phoneNo;
        this.BloodType = bloodType;
        this.DOB = DOB;
        this.Disease = Disease;
        this.Gender = Gender;
        this.userId= userId;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.PhoneNo = phoneNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
