package com.asifahmad.donatelife.model;

import androidx.recyclerview.widget.RecyclerView;

public class RequestListModel {

    String BloodType,Email,Name,PhoneNo,RequestFor;

    public RequestListModel() {
    }

    public RequestListModel(String bloodType, String email, String name, String phoneNo, String requestFor) {
        BloodType = bloodType;
        Email = email;
        Name = name;
        PhoneNo = phoneNo;
        RequestFor = requestFor;
    }

    public String getBloodType() {
        return BloodType;
    }

    public void setBloodType(String bloodType) {
        BloodType = bloodType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getRequestFor() {
        return RequestFor;
    }

    public void setRequestFor(String requestFor) {
        RequestFor = requestFor;
    }


}
