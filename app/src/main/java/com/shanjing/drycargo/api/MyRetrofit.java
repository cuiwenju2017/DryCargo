package com.shanjing.drycargo.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.shanjing.drycargo.api.GitHubService.BASE_URL;

public class MyRetrofit {

    public static GitHubService getGitHubService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//支持把请求的json转换bean
                .build();
        return retrofit.create(GitHubService.class);
    }
}
