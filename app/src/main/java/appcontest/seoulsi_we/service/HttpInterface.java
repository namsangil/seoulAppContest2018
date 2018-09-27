package appcontest.seoulsi_we.service;


import appcontest.seoulsi_we.model.FeedData;
import appcontest.seoulsi_we.model.FeedDetailData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nam on 2018. 1. 13..
 */
public interface HttpInterface {

    @GET("api/eventsAll")
    Call<FeedData[]> getEvents(@Query("condition") int condition);

    @GET("api/eventsOne")
    Call<FeedDetailData> getEvent(@Query("pid") int feedID, @Query("device_id") String deviceID);

    @GET("api/searchEvent")
    Call<FeedData[]> searchEvents(@Query("keyword") String keyword);
}