package appcontest.seoulsi_we.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nam on 2018. 1. 13..
 */

public class HttpUtil {
    private static HttpUtil mInstance;

    private Retrofit retrofit;

    public static final String URL = "http://ec2-52-78-138-9.ap-northeast-2.compute.amazonaws.com:3000/";


    private HttpUtil() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getInstance() {
        if (null == mInstance) {
            mInstance = new HttpUtil();
        }
        return mInstance.retrofit;
    }

    public static HttpInterface getHttpService() {
        return getInstance().create(HttpInterface.class);
    }
}
