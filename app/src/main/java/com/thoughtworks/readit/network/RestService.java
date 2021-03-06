package com.thoughtworks.readit.network;

import com.thoughtworks.readit.domain.Resource;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import java.util.List;

public interface RestService {

    @GET("/resources")
    void listResources(Callback<List<Resource>> callback);

    @GET("/resources/{term}")
    void searchListResources(@Path("term") String term, Callback<List<Resource>> callback);

    @POST("/resources")
    void shareResource(@Body Resource user, Callback<Resource> cb);

}
