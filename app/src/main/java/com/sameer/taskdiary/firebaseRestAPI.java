package com.sameer.taskdiary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface firebaseRestAPI {
    @GET("{data}.json")
    Call<tasksDetails> getTasks(@Path("data") String path);

    @PUT("{data}.json")
    Call<tasksDetails> putTasks(@Path("data") String path, @Body tasksDetails tasks);

    @GET("dataCounter.json")
    Call<Integer> getDataCounter();

    @PUT("dataCounter.json")
    Call<Integer> putDataCounter(@Body int dataCounter);
}
