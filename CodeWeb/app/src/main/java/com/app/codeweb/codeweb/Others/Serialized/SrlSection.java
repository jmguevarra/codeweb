package com.app.codeweb.codeweb.Others.Serialized;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;



public class SrlSection implements Serializable {

    @SerializedName("sec_id")
    public int sec_id;

    @SerializedName("yr_sec")
    public String yr_sec;
    
    @SerializedName("yrsec_icon")
    public String yrsec_icon;
}
