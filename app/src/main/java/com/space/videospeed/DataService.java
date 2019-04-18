package com.space.videospeed;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by licht on 2019/1/10.
 */

public interface DataService {
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);
}
