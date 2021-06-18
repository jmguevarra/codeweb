package com.app.codeweb.codeweb.Others.Serialized;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class SrlStudents implements Serializable {

    @SerializedName("ID")
    public int ID;

    @SerializedName("Type")
    public String Type;

    @SerializedName("Student_ID")
    public String Student_ID;

    @SerializedName("Username")
    public String Username;

    @SerializedName("Password")
    public String Password;

    @SerializedName("Fullname")
    public String Fullname;

    @SerializedName("Section")
    public String Section;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Phone_number")
    public String Phone_number;

    @SerializedName("Image")
    public String Image;

    @SerializedName("User_badge")
    public String User_badge;

    @SerializedName("User_level")
    public int User_level;

    @SerializedName("Course_Level")
    public String Course_Level;

    @SerializedName("Category_Level")
    public String Category_Level;

    @SerializedName("Points")
    public int Points;

    @SerializedName("App_Tutorial")
    public String App_Tutorial;

    @SerializedName("HtmlQuiz")
    public String HtmlQuiz;

    @SerializedName("CssQuiz")
    public String CssQuiz;

    @SerializedName("JsQuiz")
    public String JsQuiz;

    @SerializedName("HtmlQuiz_Score")
    public int HtmlQuiz_Score;

    @SerializedName("CssQuiz_Score")
    public int CssQuiz_Score;

    @SerializedName("JsQuiz_Score")
    public int JsQuiz_Score;


}
