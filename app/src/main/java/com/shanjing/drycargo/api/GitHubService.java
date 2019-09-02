package com.shanjing.drycargo.api;

import com.shanjing.drycargo.bean.PhotoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("api/data/福利/{count}/{page}")
    Call<PhotoBean> dataPhoto(@Path("count") String count, @Path("page") int page);
}
