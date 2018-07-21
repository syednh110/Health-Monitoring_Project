package com.respireplus.respire.apis;

import com.respireplus.respire.utils.AllUrls;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by namdar on 30/3/18.
 */

public class ApiDao {
    private static Retrofit retrofit=null;
    public static ApisInterface getApis(){
        if (null==ApiDao.retrofit){
            Retrofit.Builder builder=new Retrofit.Builder()
                    .baseUrl(AllUrls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            ApiDao.retrofit= builder.build();
        }
        return retrofit.create(ApisInterface.class);
    }

}
