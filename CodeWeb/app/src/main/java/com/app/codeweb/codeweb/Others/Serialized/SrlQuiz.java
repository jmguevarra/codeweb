package com.app.codeweb.codeweb.Others.Serialized;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SrlQuiz implements Serializable {

    @SerializedName("quiz_Id")
    public int quiz_Id;

    @SerializedName("quiz_Icon")
    public String quiz_Icon;

    @SerializedName("quiz_Title")
    public String quiz_Title;

    @SerializedName("quiz_Course")
    public String quiz_Course;

    @SerializedName("quiz_Type")
    public String quiz_Type;

    @SerializedName("quiz_Author")
    public String quiz_Author;

    @SerializedName("quiz_Date")
    public String quiz_Date;

    @SerializedName("quiz_Question")
    public String quiz_Question;

    @SerializedName("quiz_OptA")
    public String quiz_OptA;

    @SerializedName("quiz_OptB")
    public String quiz_OptB;

    @SerializedName("quiz_OptC")
    public String quiz_OptC;

    @SerializedName("quiz_OptD")
    public String quiz_OptD;

    @SerializedName("quiz_Ans")
    public String quiz_Ans;
}
