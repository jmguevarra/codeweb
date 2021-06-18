package com.app.codeweb.codeweb.Others.Serialized;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SrlFeedbacks implements Serializable {

    @SerializedName("ID")
    public int ID;

    @SerializedName("Student_ID")
    public String Student_ID;

    @SerializedName("Image")
    public String Image;

    @SerializedName("Fullname")
    public String Fullname;

    @SerializedName("Section")
    public String Section;

    @SerializedName("Message")
    public String Message;

    @SerializedName("Date")
    public String Date;





}
