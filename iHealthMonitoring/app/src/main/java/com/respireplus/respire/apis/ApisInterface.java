package com.respireplus.respire.apis;

import com.respireplus.respire.models.HealthResponse;
import com.respireplus.respire.utils.AllUrls;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by namdar on 30/3/18.
 */

public interface ApisInterface {
    @POST(AllUrls.CHECKUP)
    @FormUrlEncoded
    Call<HealthResponse> checkUp(@Field("input") String input);

}
