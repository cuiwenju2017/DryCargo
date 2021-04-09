package com.shanjing.drycargo.api;

import com.shanjing.drycargo.bean.PhotoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    String BASE_URL = "https://gank.io/";

    @GET("api/v2/data/category/Girl/type/Girl/page/{page}/count/{count}")
    Call<PhotoBean> dataPhoto(@Path("count") int count, @Path("page") int page);
}
