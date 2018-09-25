package appcontest.seoulsi_we.service;


import appcontest.seoulsi_we.model.FeedData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nam on 2018. 1. 13..
 */
public interface HttpInterface {

    @GET("api/eventsAll")
    Call<FeedData[]> getEvents();

    @GET("api/eventsOne")
    Call<FeedData> getEvent(@Query("pid") int feedID);
}