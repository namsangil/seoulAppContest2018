package appcontest.seoulsi_we.service;


import org.json.JSONArray;

import appcontest.seoulsi_we.model.BannerData;
import appcontest.seoulsi_we.model.FeedData;
import appcontest.seoulsi_we.model.FeedDetailData;
import appcontest.seoulsi_we.model.ResultData;
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
    Call<FeedDetailData> getEvent(@Query("pid") long pid, @Query("device_id") String deviceID);

    @GET("api/searchEvent")
    Call<FeedData[]> searchEvents(@Query("keyword") String keyword);

    @GET("api/eventsWrite")
    Call<ResultData> writeEvent(@Query("pid") long pid, @Query("date") long date, @Query("startTime") int startTime, @Query("endTime") int endTime, @Query("title") String title, @Query("content") String content, @Query("host") String host, @Query("place") JSONArray place, @Query("certificate") String certFileName);

    @GET("/api/events/reply/write")
    Call<ResultData> writeReply(@Query("pid") long pid, @Query("device_id") String deviceID, @Query("text") String text);

    @GET("/api/events/feel")
    Call<ResultData> setFeel(@Query("pid") long pid, @Query("device_id") String deviceID, @Query("feel") int feel, @Query("yn") boolean yn);

    @GET("/api/banner")
    Call<BannerData> getBanners();
}