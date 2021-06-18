package com.app.codeweb.codeweb.Others.Serialized;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SrlLesson implements Serializable {

    @SerializedName("lsn_id")
    public int lsn_id;

    @SerializedName("lsn_no")
    public int lsn_no;

    @SerializedName("lsn_icon")
    public String lsn_icon;

    @SerializedName("lsn_author")
    public String lsn_author;

    @SerializedName("lsn_course")
    public String lsn_course;

    @SerializedName("lsn_category")
    public String lsn_category;

    @SerializedName("lsn_ptCat")
    public String lsn_ptCat;

    @SerializedName("lsn_title")
    public String lsn_title;

    @SerializedName("lsn_content")
    public String lsn_content;

    @SerializedName("lsn_backpanel")
    public String lsn_backpanel;

    @SerializedName("lsn_lesson")
    public String lsn_lesson;

    @SerializedName("lsn_code_content")
    public String lsn_code_content;

    @SerializedName("lsn_output")
    public String lsn_output;

    @SerializedName("lsn_trivia")
    public String lsn_trivia;

   @SerializedName("lsn_video")
    public String lsn_video;

    @SerializedName("lsn_question")
    public String lsn_question;

    @SerializedName("lsn_optA")
    public String lsn_optA;

    @SerializedName("lsn_optB")
    public String lsn_optB;

    @SerializedName("lsn_optC")
    public String lsn_optC;

    @SerializedName("lsn_optD")
    public String lsn_optD;

    @SerializedName("lsn_rightOpt")
    public String lsn_rightOpt;

}
