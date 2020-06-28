package com.hda.bachelorandroid.services.activity

import com.hda.bachelorandroid.services.activity.model.Activity
import com.hda.bachelorandroid.services.activity.model.CreateActivityBody
import com.hda.bachelorandroid.services.activity.model.UpdateActivityBody
import com.hda.bachelorandroid.services.model.Post
import retrofit2.Call
import retrofit2.http.*


/**
 * API for getting weather from https://darksky.net/
 */
interface APIActivity {

    @GET("api/activity")
    fun getUserList(@QueryMap options: Map<String, String>): Call<List<Activity>>
    @POST("api/activity")
    fun postJsonActivity(@Body body: CreateActivityBody): Call<Activity>;
    @DELETE("api/activity/{activityId}")
    fun deleteActivity(@Path(value = "activityId",encoded = true) activityId: String): Call<Activity>;
    @PUT("api/activity/{activityId}")
    fun updateActivity(@Path(value = "activityId",encoded = true) activityId: String, @Body body: UpdateActivityBody): Call<Activity>

}
