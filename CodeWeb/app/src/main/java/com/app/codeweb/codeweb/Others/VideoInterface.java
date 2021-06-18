package com.app.codeweb.codeweb.Others;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VideoInterface {

    @Multipart
    @POST("http://192.168.10.1/CodeWebScripts/index.php")
    Call<ResultObject> uploadVideoToServer(@Part MultipartBody.Part video);

}
