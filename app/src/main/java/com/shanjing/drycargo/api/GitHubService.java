package com.shanjing.drycargo.api;

import com.shanjing.drycargo.bean.PhotoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    String BASE_URL = "https://api.ixiaowai.cn/";

    @GET("api/api.php?return=json")
    Call<PhotoBean> dataPhoto();
}
