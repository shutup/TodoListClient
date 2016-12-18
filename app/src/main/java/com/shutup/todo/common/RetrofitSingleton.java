package com.shutup.todo.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shutup on 2016/12/18.
 */

public class RetrofitSingleton implements Constants{

    private static Retrofit mRetrofit;
    private RetrofitSingleton(){

    }


    public static <T> T getApiInstance(Class<T> tClass) {
        if (mRetrofit == null) {
            synchronized (Retrofit.class) {
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    return mRetrofit.create(tClass);
                }else {
                    return mRetrofit.create(tClass);
                }
            }
        }else {
                    return mRetrofit.create(tClass);
        }
    }
}
